/**
 * 
 */
package ddb.tpc.coh;

import ddb.tpc.TPCParticipant;
import static ddb.db.DatabaseState.*;
import static ddb.communication.TcpSender.*;
import static ddb.db.DbConnector.*;
import ddb.tpc.msg.TPCMessage;
import ddb.db.DBException;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class CohordImpl extends TPCParticipant implements CohordState {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private CohordState state;
	/** 
	 * <!-- begin-UML-doc -->
	 * Adres koordynator transakcji, w ktorej uczestniczy ten kohort.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String coordinatorAddress;

	/** 
	 * <!-- begin-UML-doc -->
	 * Wyslanie wiadomosci do koordynatora transakcji
	 * <!-- end-UML-doc -->
	 * @param message
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void replyToCoordinator(TPCMessage message) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Zmiana stanu kohorta.
	 * <!-- end-UML-doc -->
	 * @param state
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void changeState(CohordState state) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Callback wywolywany, gdy wystapi timeout
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onTimeout() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Ustawia adres&nbsp;koordynator&nbsp;transakcji,&nbsp;w&nbsp;ktorej&nbsp;uczestniczy&nbsp;ten&nbsp;kohort
	 * <!-- end-UML-doc -->
	 * @param address
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setCoordinatorAddress(String address) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;adres&nbsp;koordynator&nbsp;transakcji,&nbsp;w&nbsp;ktorej&nbsp;uczestniczy&nbsp;ten&nbsp;kohort
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getCoordinatorAddress() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Metoda&nbsp;dokonujaca&nbsp;wykonania&nbsp;transakcji
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void commitTransaction() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Metoda&nbsp;wywo³ywana,&nbsp;gdy&nbsp;nie&nbsp;powiedzie&nbsp;sie&nbsp;wykonanie&nbsp;transakcji&nbsp;z&nbsp;powodu&nbsp;bledu&nbsp;w&nbsp;bazie&nbsp;danych
	 * <!-- end-UML-doc -->
	 * @param exception
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void processDBException(DBException exception) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see TPCParticipant#onNewMessage(TPCMessage message)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void onNewMessage(TPCMessage message) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see TPCParticipant#cleanupTransaction()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void cleanupTransaction() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CohordState#onPreCommit()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onPreCommit() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CohordState#onCanCommit()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onCanCommit() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CohordState#onAbort()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onAbort() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CohordState#onDoCommit()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onDoCommit() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}
}