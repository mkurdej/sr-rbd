package ddb.restore;

import ddb.Logger;
import ddb.TimeoutException;
import ddb.Worker;
import ddb.communication.TcpSender;
import ddb.msg.Message;
import ddb.msg.MessageType;
import ddb.tpc.msg.NoForCommitMessage;

public class BlockedCohort extends Worker
{
	private final static String LOGGING_NAME = "BlockedCohort";
	
	public void ForbidTransaction() throws InterruptedException
	{
		try 
		{
			Message msg = accept(MessageType.TPC_CANCOMMIT, null);
			TcpSender.getInstance().sendToNode(new NoForCommitMessage(), msg.getSender());
		
			Logger.getInstance().log(
					"Transaction forbidden", 
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
