/**
 * 
 */
package ddb.tpc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import ddb.msg.Message;

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
	private BlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param message
	 * @throws InterruptedException 
	 */
	public void putMessage(Message message) throws InterruptedException {
		messages.put(message);
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 * @throws InterruptedException 
	 */
	public Message getMessage() throws InterruptedException {
		return messages.take();
	}
}