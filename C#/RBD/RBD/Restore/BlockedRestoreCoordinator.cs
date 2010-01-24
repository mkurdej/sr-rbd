using System;
using System.Threading;

using RBD.Communication;
using RBD.Msg;
using RBD.Restore.Msg;

namespace RBD.Restore {
    public class BlockedRestoreCoordinator : Worker {

    private static string LOGGING_NAME = "BlockedRestoreCoordinator";
    	
	    public void ForbidRestoration()
	    {
		    try 
		    {
			    Message msg = accept(Message.MessageType.RESTORE_INCENTIVE, null);
			    TcpSender.getInstance().sendToNode(new RestoreNack(), msg.Sender);
    		
			    Logger.getInstance().log(
					    "Restoration forbidden", 
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

        override public void run()
        {
		    try {
			    while(true)
				    ForbidRestoration();
		    } catch (ThreadInterruptedException) {
			    Logger.getInstance().log(
					    "InterruptedException - terminating", 
					    LOGGING_NAME, 
					    Logger.Level.WARNING);
		    }
    		
	    }

    }
}