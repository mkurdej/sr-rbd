/**
 * 
 */
package ddb.restore;

import java.net.InetSocketAddress;

import ddb.Logger;
import ddb.TimeoutException;
import ddb.Worker;
import ddb.msg.Message;
import ddb.msg.MessageType;
import ddb.communication.TcpSender;
import ddb.db.DatabaseStateImpl;
import ddb.db.DbConnectorImpl;
import ddb.db.ImportTableException;
import ddb.db.TableVersion;
import ddb.restore.msg.RestoreAck;
import ddb.restore.msg.RestoreIncentive;
import ddb.restore.msg.RestoreNack;
import ddb.restore.msg.RestoreTable;
import ddb.restore.msg.RestoreTableList;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author User
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class RestoreCohort extends Worker {
	private final static String LOGGING_NAME = "RestoreCohort";
	private final int RESTORE_TIMEOUT = 1000;

	
	@Override
	public void run() {

		// loop unit worker finishes his task
		try {
			// listen for restoring incentives forever
			while (true) 
			{
				try
				{
					restoreCycle();
				}
				catch(TimeoutException e)
				{
					Logger.getInstance().log("TimeoutException" + e.getMessage(),
							LOGGING_NAME, Logger.Level.WARNING);
				}
			}
		} catch (InterruptedException e) {
			Logger.getInstance().log("InterruptedException" + e.getMessage(),
					LOGGING_NAME, Logger.Level.WARNING);
		}
	}

	public void restoreCycle() throws InterruptedException, TimeoutException
	{
		
		Message msg;
		MessageType[] awaitingTableList = {MessageType.RESTORE_TABLELIST, MessageType.RESTORE_INCENTIVE};
		MessageType[] awaitingTables = {MessageType.RESTORE_TABLE, MessageType.RESTORE_INCENTIVE};
		
		Logger.getInstance().log(
				"Awaiting restore incentives", 
				LOGGING_NAME, 
				Logger.Level.INFO);
		
		// take RestoreIncentive ( other will get ignored )
		clearTimeout();
		msg = accept(MessageType.RESTORE_INCENTIVE, null);
		
		Logger.getInstance().log(
				"Got incentive - responding ack", 
				LOGGING_NAME, 
				Logger.Level.INFO);
		
		// answer ok
		InetSocketAddress node = msg.getSender();
		TcpSender.getInstance().sendToNode(new RestoreAck(), node);

		// set timeout for restore process
		setTimeout(RESTORE_TIMEOUT);
		
		// get table list
		while(true)
		{
			msg = accept(awaitingTableList, node);
			
			if(msg instanceof RestoreIncentive)
			{
				// deny restore since its already happening
				TcpSender.getInstance().sendToNode(new RestoreNack(), node);
				
				Logger.getInstance().log(
						"RestoreIncentive - restoration in progress - responding nack to: " + msg.getSender().toString(),
						LOGGING_NAME, 
						Logger.Level.INFO);
				
			}
			else
			{
				break; // to the next step
			}
		}
		
		Logger.getInstance().log(
				"Got table list - scanning", 
				LOGGING_NAME, 
				Logger.Level.INFO);
		
		
		// set table state as out of sync if versions dont match
		RestoreTableList rtl = (RestoreTableList)msg;
		int left = rtl.getTables().size();
		for(TableVersion table : rtl.getTables())
		{
			Logger.getInstance().log(
					"Table '" + table.getTableName() + "' version " + table.getVersion(), 
					LOGGING_NAME, 
					Logger.Level.INFO);
		}
		
		// send back ack to unlock database
		TcpSender.getInstance().sendToNode(new RestoreAck(), node);
		setTimeout(RESTORE_TIMEOUT);
		
		Logger.getInstance().log(
				"Got table list - responding ack", 
				LOGGING_NAME, 
				Logger.Level.INFO);
		
		// read all tables
		while(left > 0) 
		{
			msg = accept(awaitingTables, node);
			
			if(msg instanceof RestoreIncentive)
			{
				Logger.getInstance().log(
						"RestoreIncentive - restoration in progress - responding nack to: " + msg.getSender().toString(), 
						LOGGING_NAME, 
						Logger.Level.INFO);
				
				// deny restore since its already happening
				TcpSender.getInstance().sendToNode(new RestoreNack(), node);
			}
			else // RestoreTable
			{
				restoreTable((RestoreTable)msg);
				--left;
				setTimeout(RESTORE_TIMEOUT);
				
				Logger.getInstance().log(
						Integer.toString(left) + " tables left", 
						LOGGING_NAME, 
						Logger.Level.INFO);
			}
			
		}
		
		Logger.getInstance().log(
				"Restore complete", 
				LOGGING_NAME, 
				Logger.Level.INFO);
	}
	
	protected void restoreTable(RestoreTable rtm)
	{
		String tableName = rtm.getTableName();
		int version = rtm.getTableVersion();
		int myversion = DatabaseStateImpl.getInstance().getTableVersion(tableName);
		
		Logger.getInstance().log(
				"Restore table '" + tableName + "' to version " + Integer.toString(version) + " - my is " + Integer.toString(myversion), 
				LOGGING_NAME, 
				Logger.Level.INFO);
		
		if( myversion < version)
		{
			try {
				DbConnectorImpl.getInstance().importTable(tableName, version, rtm.getTableDump());
			
			} catch (ImportTableException e) {
				Logger.getInstance().log("ImportTableException" + e.getMessage(),
						LOGGING_NAME, Logger.Level.WARNING);
			}
		}
		else
		{
			Logger.getInstance().log(
					"Table skipped - my version is newer or equal", 
					LOGGING_NAME, 
					Logger.Level.INFO);
		}
	}
}