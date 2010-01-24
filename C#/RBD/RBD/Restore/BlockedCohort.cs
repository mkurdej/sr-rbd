// +

using System;
using System.Threading;

using RBD.Communication;
using RBD.Msg;
using RBD.Restore.Msg;
using RBD.TPC.Msg;

namespace RBD.Restore {
    public class BlockedCohort : Worker
    {
	    private static string LOGGING_NAME = "BlockedCohort";
    	
	    public void ForbidTransaction()
	    {
		    try 
		    {
			    Message msg = accept(Message.MessageType.TPC_CANCOMMIT, null);
		        TcpSender.getInstance().sendToNode(new NoForCommitMessage(), msg.Sender);
    		
			    Logger.getInstance().log(
					    "Transaction forbidden", 
					    LOGGING_NAME, 
					    Logger.Level.INFO);
    		
		    } 
		    catch (TimeoutException) 
		    {
			    Logger.getInstance().log(
					    "TimeoutException (should never happen)", 
					    LOGGING_NAME, 
					    Logger.Level.WARNING);
		    }
	    }
    	
	    override public void run() {
		    try {
			    while(true)
				    ForbidTransaction();
		    } catch (ThreadInterruptedException) {
			    Logger.getInstance().log(
					    "InterruptedException - terminating", 
					    LOGGING_NAME, 
					    Logger.Level.WARNING);
		    }
    		
	    }
    }
}
