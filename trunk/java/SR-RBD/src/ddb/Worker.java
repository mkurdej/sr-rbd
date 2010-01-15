package ddb;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ddb.msg.Message;

public abstract class Worker {
	protected BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
	
	public void putMessage(Message msg) throws InterruptedException
	{
		queue.put(msg);
	}
}
