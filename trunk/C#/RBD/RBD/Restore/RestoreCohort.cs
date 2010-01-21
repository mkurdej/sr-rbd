using System;
using RBD.Msg;
using RBD.Restore.Msg;
using System.Net;
using RBD.DB;

namespace RBD.Restore {
    public class RestoreCohort : Worker {
	    private static string LOGGING_NAME = "RestoreCohort";
	    private int RESTORE_TIMEOUT = 1000;

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
				    catch(TimeoutException)
				    {
					    Logger.getInstance().log("TimeoutException",
							    LOGGING_NAME, Logger.Level.WARNING);
				    }
			    }
		    } catch (Exception) {
			    Logger.getInstance().log("InterruptedException",
					    LOGGING_NAME, Logger.Level.WARNING);
		    }
	    }

	    public void restoreCycle()
	    {
    		
		    Message msg;
            Message.MessageType[] awaitingTableList = { Message.MessageType.RESTORE_TABLELIST, Message.MessageType.RESTORE_INCENTIVE };
            Message.MessageType[] awaitingTables = { Message.MessageType.RESTORE_TABLE, Message.MessageType.RESTORE_INCENTIVE };
    		
		    Logger.getInstance().log(
				    "Awaiting restore incentives", 
				    LOGGING_NAME, 
				    Logger.Level.INFO);
    		
		    // take RestoreIncentive ( other will get ignored )
		    clearTimeout();
            msg = accept(Message.MessageType.RESTORE_INCENTIVE, null);
    		
		    Logger.getInstance().log(
				    "Got incentive - responding ack", 
				    LOGGING_NAME, 
				    Logger.Level.INFO);
    		
		    // answer ok
		    IPEndPoint node = msg.Sender;
		    TcpSender.getInstance().sendToNode(new RestoreAck(), node);

		    // set timeout for restore process
		    setTimeout(RESTORE_TIMEOUT);
    		
		    // get table list
		    while(true)
		    {
			    msg = accept(awaitingTableList, node);
    			
			    if(msg is RestoreIncentive)
			    {
				    // deny restore since its already happening
				    TcpSender.getInstance().sendToNode(new RestoreNack(), node);
    				
				    Logger.getInstance().log(
						    "RestoreIncentive - restoration in progress - responding nack to: " + msg.Sender.ToString(),
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
		    int left = rtl.getTables().Count;
		    foreach(TableVersion table in rtl.getTables())
		    {
			    // TODO: implement? is this really required? RestoreTableRequest
			    Logger.getInstance().log(
					    "Table '" + table.Name + "' version " + table.Version, 
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
    			
			    if(msg is RestoreIncentive)
			    {
				    Logger.getInstance().log(
						    "RestoreIncentive - restoration in progress - responding nack to: " + msg.Sender.ToString(), 
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
                            Convert.ToInt32(left) + " tables left", 
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
                    "Restore table '" + tableName + "' to version " + version + " - my is " + myversion, 
				    LOGGING_NAME, 
				    Logger.Level.INFO);
    		
		    if( myversion < version)
		    {
			    try {
				    DbConnectorImpl.getInstance().importTable(tableName, version, rtm.getTableDump());
			    } catch (ImportTableException e) {
				    Logger.getInstance().log("ImportTableException" + e.Message,
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
}