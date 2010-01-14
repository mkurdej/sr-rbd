/**
 * 
 */
package ddb.communication;

import ddb.msg.Message;
import ddb.tpc.EndTransactionListener;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface Dispatcher extends EndTransactionListener {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param msgString
	 * @param senderAddress
	 * @param senderPort
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public abstract void dispatchMessage(Message m, String senderAddress,
			int senderPort);
}