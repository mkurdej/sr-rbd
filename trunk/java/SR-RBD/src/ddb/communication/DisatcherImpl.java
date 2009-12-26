/**
 * 
 */
package ddb.communication;

import static ddb.tpc.coh.Cohord.*;
import static ddb.tpc.cor.Coordinator.*;
import static ddb.communication.MessageParser.*;
import static ddb.restore.RestoreCoordinator.*;
import static ddb.restore.RestoreCohord.*;
import ddb.tpc.TPCParticipant;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class DisatcherImpl implements Dispatcher {
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
	private static final DisatcherImpl instance = new DisatcherImpl();

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private DisatcherImpl() {
		// begin-user-code
		// TODO: Implement constructor logic
		// end-user-code
	}

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	public static DisatcherImpl getInstance() {
		// begin-user-code

		return instance;
		// end-user-code
	}
}