package ddb.restore;

import ddb.Logger;
import ddb.TimeoutException;
import ddb.Worker;
import ddb.communication.TcpSender;
import ddb.msg.Message;
import ddb.msg.MessageType;
import ddb.msg.client.ConflictMessage;


public class BlockedCoordinator extends Worker {

	private final static String LOGGING_NAME = "BlockedCoordinator";
	
	public void ForbidTransaction() throws InterruptedException
	{
		try 
		{
			Message msg = accept(MessageType.TRANSACTION_MESSAGE, null);
			TcpSender.getInstance().sendToNode(new ConflictMessage(), msg.getSender());
		
			Logger.getInstance().log(
					"Client transaction forbidden", 
					LOGGING_NAME, 
					Logger.Level.INFO);
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
				ForbidTransaction();
		} catch (InterruptedException e) {
			Logger.getInstance().log(
					"InterruptedException - terminating", 
					LOGGING_NAME, 
					Logger.Level.WARNING);
		}
		
	}

}
