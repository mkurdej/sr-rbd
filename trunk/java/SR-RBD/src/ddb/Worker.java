package ddb;

import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import ddb.msg.Message;
import ddb.msg.MessageType;

public abstract class Worker implements Runnable
{
	private final static String LOGGING_NAME = "Worker";
	
	protected BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
	protected long timeoutTime;
	
	public final void setTimeout(long ms)
	{
		timeoutTime = System.currentTimeMillis() + ms;
	}
	
	public final void clearTimeout()
	{
		timeoutTime = -1;
	}
	
	public final void putMessage(Message msg) throws InterruptedException
	{
		queue.put(msg);
	}
	
	public Message accept(MessageType type, InetSocketAddress node) throws InterruptedException, TimeoutException
	{
		MessageType[] types = { type };
		return accept(types, node);
	}
	
	public Message accept(MessageType[] types, InetSocketAddress node) throws InterruptedException, TimeoutException
	{
		while(true)
		{
			Message msg;
			
			if(timeoutTime == -1)
			{
				msg = queue.take();
			}
			else
			{
				msg = queue.poll(timeoutTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
				
				if(msg == null)
					throw new TimeoutException();
			}
			
			MessageType type = msg.getType();
			
			// check if source matches
			if(node == null || msg.getSender().equals(node))
			{
				// check if type matches
				for(MessageType t : types)
				{
					if(type.equals(t))
						return msg;
				}
			}
			
			Logger.getInstance().log(
					"Unexcepted message" + msg.toString(),
					LOGGING_NAME, 
					Logger.Level.WARNING);
		}
	}

	
	
}
