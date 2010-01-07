/**
 * 
 */
package ddb.tpc;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import ddb.Logger;
import ddb.Logger.Level;
import ddb.msg.Message;
import ddb.tpc.cor.CoordinatorImpl;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author PKalanski
 */
public class MessageQueue {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 */
	//private LinkedList<Message> messages;
	private BlockingQueue<Message> messages;
	
	public MessageQueue() {
		//this.messages = new LinkedList<Message>();
		this.messages = new LinkedBlockingQueue<Message>();
	}
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param message
	 * @throws InterruptedException 
	 */
	public synchronized void putMessage(Message message) throws InterruptedException {
		//TODO:
		messages.put(message);
		//notify();
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 * @throws InterruptedException 
	 */
	public synchronized Message getMessage() throws InterruptedException {
		//TODO:
		Logger.getInstance().log("Liczba wiadomosci w kolejce: " + messages.size(), "MQ", Level.INFO);
		//return messages.poll();
		return messages.take();
	}
}