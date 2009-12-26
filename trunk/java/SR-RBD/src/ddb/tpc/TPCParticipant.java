/**
 * 
 */
package ddb.tpc;

import java.util.Set;
import ddb.communication.TcpSender;
import ddb.msg.Message;
import ddb.tpc.msg.TPCMessage;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public abstract class TPCParticipant implements MessageRecipient,
		TimeoutListener {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String transactionId;

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private MessageQueue messageQueue;

	/** 
	 * <!-- begin-UML-doc -->
	 * Nazwa tabeli, na ktorej wykonywana jest transakcja.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String tableName;

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TimeoutGenerator timeoutGenerator;

	/** 
	 * <!-- begin-UML-doc -->
	 * Czy w¹tek dzia³a.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean stopped;

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Set<EndTransactionListener> endTransactionListeners;

	/** 
	 * <!-- begin-UML-doc -->
	 * Zapytanie, ktore ma zostac wykonane w bazie danych.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String queryString;

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TcpSender tcpSender;

	/** 
	 * <!-- begin-UML-doc -->
	 * Ustawia id transakcji.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTransactionId() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Wstawienie wiadomosci do kolejki wiadomosci. Wiadomosc zostania odebrana gdy zostanie wywolana metoda waitForMessage().
	 * <!-- end-UML-doc -->
	 * @param message
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void processMessage(Message message) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;id transakcji.
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getTransactionId() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Ustawia tabele, ktorej dotyczy transakcja.
	 * <!-- end-UML-doc -->
	 * @param tableName
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTableName(String tableName) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;tabele, ktorej dotyczy transakcja.
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getTableName() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Oczekiwanie na nadejscie nowej wiadomosci. Gdy nadejdzie nowa wiadomosc zostanie wywolany callback onNewMessage().
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void waitForMessage() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>
	 *     Wlacza timer.
	 * </p>
	 * <!-- end-UML-doc -->
	 * @param miliseconds
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void startTimer(long miliseconds) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Wylacza timer.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void stopTimer() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Metoda wywolywana, gdy nadejdzie nowa wiadomosc.
	 * <!-- end-UML-doc -->
	 * @param message
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected abstract void onNewMessage(TPCMessage message);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>
	 *     Konczy transakcje. Trzeba poinformowac Disptachera o koncu transakcji, aby mogl zwolnic zasoby, zakonczyc watek.
	 *     Wywolywana jest metoda cleanupTransaction() w celu dokonania dodatakowych czynnosci w zakonczeniu transakcji.
	 * </p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final void endTransaction() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Dokonanie dodatakowych czynnosci&nbsp;przy zakonczeniu transakcji.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected abstract void cleanupTransaction();

	/** 
	 * <!-- begin-UML-doc -->
	 * Rozpoczyna&nbsp;prace nowego&nbsp;watku. Oczekiwanie na nadejscie wiadomosci.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void startThread() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Konczy prace watku.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void stopThread() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Dodaje obiekt, ktory chce zostac poinformowany o koncu transakcji.
	 * <!-- end-UML-doc -->
	 * @param listener
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addEndTransactionListener(EndTransactionListener listener) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Informuje wszystkich zainteresowanych o tym, ze zakonczono transakcje.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void notifyEndTransactionListeners() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Ustawia zapytanie, ktore ma zostac wykonane przez baze danych w ramach transakcji
	 * <!-- end-UML-doc -->
	 * @param queryString
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setQueryString(String queryString) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;zapytanie,&nbsp;ktore&nbsp;ma&nbsp;zostac&nbsp;wykonane&nbsp;przez&nbsp;baze&nbsp;danych&nbsp;w&nbsp;ramach&nbsp;transakcji
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getQueryString() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}
}