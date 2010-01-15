/**
 * 
 */
package ddb.restore;

import java.util.Arrays;

import ddb.Logger;
import ddb.TimeoutException;
import ddb.Worker;
import ddb.msg.Message;
import ddb.msg.MessageType;
import ddb.communication.TcpSender;
import ddb.db.DatabaseStateImpl;
import ddb.db.DbConnector;
import ddb.db.DbConnectorImpl;
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
		
		// take RestoreIncentive ( other will get ignored )
		msg = accept(MessageType.RESTORE_INCENTIVE, null);
		
		// answer ok
		String node = msg.getSenderAddress();
		TcpSender.getInstance().sendTo(new RestoreAck(), node);

		// set timeout for restore process
		setTimeout(RESTORE_TIMEOUT);
		
		// get table list
		while(true)
		{
			msg = accept(awaitingTableList, node);
			
			if(msg instanceof RestoreIncentive)
			{
				// deny restore since its already happening
				TcpSender.getInstance().sendTo(new RestoreNack(), node);
			}
			else
			{
				break; // to the next step
			}
		}
		
		// set table state as out of sync if versions dont match
		RestoreTableList incentive = (RestoreTableList)msg;
		int left = incentive.getTables().length;
		for(TableVersion table : incentive.getTables())
		{
			// TODO: implement
		}
		
		// send back ack to unlock database
		TcpSender.getInstance().sendTo(new RestoreAck(), node);
		setTimeout(RESTORE_TIMEOUT);
		
		// read all tables
		while(left > 0) 
		{
			msg = accept(awaitingTables, node);
			
			if(msg instanceof RestoreIncentive)
			{
				// deny restore since its already happening
				TcpSender.getInstance().sendTo(new RestoreNack(), node);
			}
			else // RestoreTable
			{
				restoreTable((RestoreTable)msg);
				--left;
				setTimeout(RESTORE_TIMEOUT);
			}
			
		}
		
		clearTimeout();
	}
	
	protected void restoreTable(RestoreTable rtm)
	{
		String tableName = rtm.getTableName();
		int version = rtm.getTableVersion();
		
		if(DatabaseStateImpl.getInstance().getTableVersion(tableName) < version)
		{
			// TODO: czy w dumpach jest drop table?
			DbConnectorImpl.getInstance().importTable(tableName, version, rtm.getTableDump());
		}
	}
}