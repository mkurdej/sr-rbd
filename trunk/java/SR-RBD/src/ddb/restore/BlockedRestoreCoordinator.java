package ddb.restore;

import ddb.Logger;
import ddb.TimeoutException;
import ddb.Worker;
import ddb.communication.TcpSender;
import ddb.msg.Message;
import ddb.msg.MessageType;
import ddb.restore.msg.RestoreNack;

public class BlockedRestoreCoordinator extends Worker {

private final static String LOGGING_NAME = "BlockedRestoreCoordinator";
	
	public void ForbidRestoration() throws InterruptedException
	{
		try 
		{
			Message msg = accept(MessageType.RESTORE_INCENTIVE, null);
			TcpSender.getInstance().sendToNode(new RestoreNack(), msg.getSender());
		} 
		catch (TimeoutException e) 
		{
			Logger.getInstance().log(
					"TimeoutException (should never happen)", 
					LOGGING_NAME, 
					Logger.Level.WARNING);
		}
	}
	
	@Override
	public void run() {
		try {
			while(true)
				ForbidRestoration();
		} catch (InterruptedException e) {
			Logger.getInstance().log(
					"InterruptedException - terminating", 
					LOGGING_NAME, 
					Logger.Level.WARNING);
		}
		
	}

}
