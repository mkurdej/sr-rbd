/**
 * 
 */
package ddb.restore;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import ddb.Logger;
import ddb.TimeoutException;
import ddb.Worker;
import ddb.msg.Message;
import ddb.msg.MessageType;
import ddb.communication.TcpSender;
import ddb.db.DatabaseStateImpl;
import ddb.db.DbConnectorImpl;
import ddb.db.DumpTableException;
import ddb.db.TableVersion;
import ddb.restore.msg.RestoreIncentive;
import ddb.restore.msg.RestoreNack;
import ddb.restore.msg.RestoreTable;
import ddb.restore.msg.RestoreTableList;



/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class RestoreCoordinator extends Worker 
{
	private final static String LOGGING_NAME = "RestoreCoordinator";
	private static final int RESTORE_TIMEOUT = 1000;
	
	private Set<EndRestorationListener> endRestorationListeners = new HashSet<EndRestorationListener>();
	private InetSocketAddress targetNode;

	public RestoreCoordinator(InetSocketAddress node)
	{
		targetNode = node;
	}
	
	public void addEndRestorationListener(EndRestorationListener listener) {
		endRestorationListeners.add(listener);
	}
	
	public void removeEndRestorationListener(EndRestorationListener listener) {
		endRestorationListeners.remove(listener);
	}
	
	private void notifyEndRestorationListener() {
		for (EndRestorationListener listener : endRestorationListeners) {
			listener.onEndRestoration(this);
		}
	}
	
	public InetSocketAddress getTargetNode()
	{
		return targetNode;
	}
	
	public void Restore() throws TimeoutException, InterruptedException
	{
		Message msg;
		MessageType[] incentiveReply = { MessageType.RESTORE_ACK, MessageType.RESTORE_NACK };
		
		// send restore incentive
		TcpSender.getInstance().sendToNode(new RestoreIncentive(), targetNode);
		
		// receive reply
		setTimeout(RESTORE_TIMEOUT);
		msg = accept(incentiveReply, targetNode);

		if(msg instanceof RestoreNack)
			return;
			
		// TODO: tutaj nie moze byc zadnych transakcji
		// TODO: lock whole database ( all tables )
		RestoreTableList rtl = new RestoreTableList(DatabaseStateImpl.getInstance().getTableVersions()); 
		TcpSender.getInstance().sendToNode(rtl, targetNode);
		
		// receive reply
		setTimeout(RESTORE_TIMEOUT);
		msg = accept(MessageType.RESTORE_ACK, targetNode);

		
		// TODO: release lock
		
		// send all tables
		for(TableVersion tv : rtl.getTables())
		{
			String dump;
			try {
				dump = DbConnectorImpl.getInstance().dumpTable(tv.getTableName());
				RestoreTable rt = new RestoreTable(tv.getVersion(), tv.getTableName(), dump);
				TcpSender.getInstance().sendToNode(rt, targetNode);
			
			} catch (DumpTableException e) {
				Logger.getInstance().log(
						"DumpTableException " + e.getMessage(),
						LOGGING_NAME, 
						Logger.Level.WARNING);
				
				RestoreTable rt = new RestoreTable(-1, tv.getTableName(), "");
				TcpSender.getInstance().sendToNode(rt, targetNode);
			}
		}	
		
		// finished
	}
	
	
	@Override
	public void run() {
		try
		{
			// try to restore
			Restore();
		}
		catch(TimeoutException ex)
		{
			Logger.getInstance().log(
					"Restore timed out",
					LOGGING_NAME, 
					Logger.Level.WARNING);
		} catch (InterruptedException e) {
			Logger.getInstance().log(
					"InterruptedException",
					LOGGING_NAME, 
					Logger.Level.WARNING);
		}
		
		// notify
		notifyEndRestorationListener();
	}
}