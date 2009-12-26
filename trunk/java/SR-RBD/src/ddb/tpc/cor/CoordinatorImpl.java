/**
 * 
 */
package ddb.tpc.cor;

import ddb.tpc.TPCParticipant;
import static ddb.db.DatabaseState.*;
import static ddb.communication.TcpSender.*;
import static ddb.db.DbConnector.*;
import java.util.Set;
import ddb.tpc.msg.TPCMessage;
import ddb.db.DBException;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class CoordinatorImpl extends TPCParticipant {
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
	 * Adres klienta, ktory zarzadal wykonania transakcji
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String clientAddress;
	/** 
	 * <!-- begin-UML-doc -->
	 * Numer&nbsp;portu&nbsp;klienta, ktory zarzadal wykonania transakcji
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String clientPort;

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
	public void broadcastMessage(TPCMessage message) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Zmienia stan koordynatora
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
	 * Callback&nbsp;wywolywany,&nbsp;gdy&nbsp;wystapi&nbsp;timeout
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
	 * <!-- begin-UML-doc -->
	 * Ustawia adres&nbsp;klienta,&nbsp;ktory&nbsp;zarzadal&nbsp;wykonania&nbsp;transakcji
	 * <!-- end-UML-doc -->
	 * @param clientAddress
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setClientAddress(String clientAddress) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;adres&nbsp;klienta,&nbsp;ktory&nbsp;zarzadal&nbsp;wykonania&nbsp;transakcji
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getClientAddress() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Ustawia numer&nbsp;portu&nbsp;klienta, ktory zarzadal wykonania transakcji
	 * <!-- end-UML-doc -->
	 * @param port
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setClientPort(String port) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;numer&nbsp;portu&nbsp;klienta, ktory zarzadal wykonania transakcji
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getClientPort() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>
	 *     Wykanie zapytania SELECT do bazy danych.
	 * </p>
	 * <p>
	 *     SELECT obslugiwany jest przez koordynatora, poniewaz nie ma potrzeba, aby zostal wykonany na wszystkich wezlach.
	 * </p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void processSelect() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Metoda wywo³ywana, gdy nie powiedzie sie wykonanie transakcji z powodu bledu w bazie danych
	 * <!-- end-UML-doc -->
	 * @param exception
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final void processDBException(DBException exception) {
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
}