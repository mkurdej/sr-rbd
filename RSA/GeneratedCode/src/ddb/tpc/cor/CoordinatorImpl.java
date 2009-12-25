/**
 * 
 */
package ddb.tpc.cor;

import static ddb.db.DatabaseState.*;
import static ddb.communication.TcpSender.*;
import static ddb.db.DbConnector.*;
import java.util.Set;
import ddb.tpc.msg.TPCMessage;
import ddb.Message;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class CoordinatorImpl extends Coordinator {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private CoordinatorState state;
	/** 
	 * <!-- begin-UML-doc -->
	 * Lista pozytywnych&nbsp;odpowiedzi&nbsp;od&nbsp;wezlow
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Set<String> answers;
	/** 
	 * <!-- begin-UML-doc -->
	 * Lista wezlo bioracych udzial w transakcji
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Set<String> nodes;

	/** 
	 * <!-- begin-UML-doc -->
	 * Czysci wszystkie odpowiedzi.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void clearAnswers() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Ustawia liste wezlow bioracych udzial w transakcji.
	 * <!-- end-UML-doc -->
	 * @param nodeList
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setNodeList(String... nodeList) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>
	 *     Ustawia odpowiedz od podanego wezla
	 * </p>
	 * <!-- end-UML-doc -->
	 * @param nodeName
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void addAnswer(String nodeName) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Sprawdza czy wszystkie wezly odpowiedzialy pozytywnie.
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean checkAnswers() {
		// begin-user-code
		// TODO Auto-generated method stub
		return false;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Wyslij wiadomosc do wszystkich wezlow bioracych udzial w transakcji.
	 * <!-- end-UML-doc -->
	 * @param message
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void broadcastMessage(Object message) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param state
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void changeState(CoordinatorState state) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Metoda wywolywana, gdy transakcja zostala zakonczona niepomyslnie.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void abortTransaction() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
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
	 * <p>
	 *     Obsluguje odpowiedz wezla na wiadomosc.
	 * </p>
	 * <p>
	 *     Jezeli nie jest to ostatni wezel, to koordynator czeka na nastepna wiadomosc.
	 * </p>
	 * <p>
	 *     Jezeli jest to ostatni wezel, to koordynator wysyla nastepna wiadomosc do wszystkich wezlow uczestniczacych w
	 *     transakcji oraz zmienia stan.
	 * </p>
	 * <!-- end-UML-doc -->
	 * @param node <p>
	 *    Wezel, ktory odpowiedzial na poprzednia wiadomosc.
	 * /p>
	 * @param message Nastepna wiadomosc do wyslania do wszystkich wezlow uczestniczacych w transakcji.
	 * @param nextState Nastepny stan.
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void processAnswer(String node, TPCMessage message,
			CoordinatorState nextState) {
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
}