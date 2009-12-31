/**
 * 
 */
package ddb.communication;

import ddb.tpc.TPCParticipant;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class DispatcherImpl implements Dispatcher {
	/** 
	 * /* (non-Javadoc)
	 *  * @see EndTransactionListener#onEndTransaction(TPCParticipant participant)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onEndTransaction(TPCParticipant participant) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see Dispatcher#dispatchMessage(String msgString, String senderAddress, String senderPort)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void dispatchMessage(String msgString, String senderAddress,
			String senderPort) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private static final DispatcherImpl instance = new DispatcherImpl();

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private DispatcherImpl() {
		// begin-user-code
		// TODO: Implement constructor logic
		// end-user-code
	}

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	public static DispatcherImpl getInstance() {
		// begin-user-code

		return instance;
		// end-user-code
	}

	@Override
	public void dispatchMessage(String msgString, String senderAddress,
			int senderPort) {
		// TODO Auto-generated method stub
		
	}
}