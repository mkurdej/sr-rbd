/**
 * 
 */
package ddb.tpc;

import java.util.LinkedList;
import ddb.tpc.msg.TPCMessage;

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
	private LinkedList<TPCMessage> messages;
	
	public MessageQueue() {
		this.messages = new LinkedList<TPCMessage>();
	}
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param message
	 */
	public void putMessage(TPCMessage message) {
		messages.offer(message);
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 */
	public TPCMessage getMessage() {
		return messages.poll();
	}
}