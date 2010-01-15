/**
 * 
 */
package ddb.restore;

import ddb.Logger;
import ddb.TimeoutException;
import ddb.Worker;
import ddb.msg.Message;
import ddb.msg.MessageType;
import ddb.communication.TcpSender;
import ddb.db.DbConnectorImpl;
import ddb.restore.msg.RestoreIncentive;
import ddb.restore.msg.RestoreNack;
import ddb.restore.msg.RestoreTable;
import ddb.restore.msg.RestoreTableList;
import ddb.restore.msg.TableVersion;



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
	String targetNode;

	public RestoreCoordinator(String node)
	{
		targetNode = node;
	}
	

	@Override
	public void run() {
		
		// TODO: tutaj nie moze byc zadnych transakcji
		Message msg;
		MessageType[] incentiveReply = { MessageType.RESTORE_ACK, MessageType.RESTORE_NACK };
		
		// send restore incentive
		TcpSender.getInstance().sendToNode(new RestoreIncentive(), targetNode);
		
		// receive reply
		try
		{
			setTimeout(RESTORE_TIMEOUT);
			msg = accept(incentiveReply, targetNode);
		}
		catch(TimeoutException ex)
		{
			msg = null;
		}
		
		if(msg == null || msg instanceof RestoreNack)
		{
			Logger.getInstance().log("Restore incentive rejected by " + targetNode,
					LOGGING_NAME, Logger.Level.INFO);
			return;
		}
		
		// TODO: lock whole database ( all tables )
		RestoreTableList rtl = new RestoreTableList(); // TODO: fill data
		TcpSender.getInstance().sendToNode(rtl, targetNode);
		
		// receive reply
		try
		{
			setTimeout(RESTORE_TIMEOUT);
			msg = accept(MessageType.RESTORE_ACK, targetNode);
		}
		catch(TimeoutException ex)
		{
			Logger.getInstance().log(
					"Restore of '" + targetNode + "' failed due to timeout while receiving tablelist ack ",
					LOGGING_NAME, 
					Logger.Level.INFO);
			return;
		}
		
		// TODO: release lock
		
		// send all tables
		for(TableVersion tv : ri.getTables())
		{
			String dump = DbConnectorImpl.getInstance().dumpTable(tv.getTableName());
			RestoreTable rt = new RestoreTable(tv.getVersion(), tv.getTableName(), dump);
			
			TcpSender.getInstance().sendToNode(rt, targetNode);
		}	
		
		// finished
	}
}