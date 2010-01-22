using System;

using RBD.Communication;
using RBD.TPC.Msg;
using RBD.Msg;
using RBD.Msg.Client;

namespace RBD.Restore {
    public class BlockedCoordinator : Worker {

	    private static string LOGGING_NAME = "BlockedCoordinator";
    	
	    public void ForbidTransaction()
	    {
		    try 
		    {
			    Message msg = accept(Message.MessageType.TRANSACTION_MESSAGE, null);
			    TcpSender.getInstance().sendToNode(new ConflictMessage(), msg.Sender);
    		
			    Logger.getInstance().log(
					    "Client transaction forbidden", 
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
    	
	    public void run() {
		    try {
			    while(true)
				    ForbidTransaction();
		    } catch (Exception) {
			    Logger.getInstance().log(
					    "InterruptedException - terminating", 
					    LOGGING_NAME, 
					    Logger.Level.WARNING);
		    }	
	    }
    }
}