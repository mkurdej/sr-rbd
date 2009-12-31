/**
 * 
 */
package ddb.tpc;

import java.util.HashSet;
import java.util.Set;

import ddb.Logger;
import ddb.Logger.Level;
import ddb.communication.TcpSender;
import ddb.db.DatabaseState;
import ddb.db.DbConnector;
import ddb.msg.Message;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public abstract class TPCParticipant implements MessageRecipient,
		TimeoutListener {
	public static final int TIMEOUT = 5000;
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
	 * 
	 */
	private DatabaseState databaseState;
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected TcpSender tcpSender;

	/**
	 * 
	 */
	protected DbConnector connector;

	public TPCParticipant() {
		this.endTransactionListeners = new HashSet<EndTransactionListener>();
		this.timeoutGenerator = new TimeoutGenerator();
		this.messageQueue = new MessageQueue();
		startThread();
	}
	
	public DatabaseState getDatabaseState() {
		return databaseState;
	}

	public void setDatabaseState(DatabaseState databaseState) {
		this.databaseState = databaseState;
	}
	
	/** 
	 * <!-- begin-UML-doc -->
	 * Ustawia id transakcji.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Wstawienie wiadomosci do kolejki wiadomosci. Wiadomosc zostania odebrana gdy zostanie wywolana metoda waitForMessage().
	 * <!-- end-UML-doc -->
	 * @param message
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	synchronized public void processMessage(Message message) {
		this.messageQueue.putMessage(message);
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;id transakcji.
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getTransactionId() {
		return this.transactionId;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Ustawia tabele, ktorej dotyczy transakcja.
	 * <!-- end-UML-doc -->
	 * @param tableName
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;tabele, ktorej dotyczy transakcja.
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getTableName() {
		return this.tableName;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Oczekiwanie na nadejscie nowej wiadomosci. Gdy nadejdzie nowa wiadomosc zostanie wywolany callback onNewMessage().
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	synchronized public void waitForMessage() {
		Logger.getInstance().log("waitForMessage " + Thread.currentThread(), "TPC", Logger.Level.INFO);
		
		try {
			Message msg = this.messageQueue.getMessage();
			onNewMessage(msg);
		} catch (InterruptedException e) {
			Logger.getInstance().log(e.getMessage(), "TPC", Logger.Level.SEVERE);
		}
		
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
		this.timeoutGenerator.startTimer(miliseconds);
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Wylacza timer.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void stopTimer() {
		this.timeoutGenerator.stopTimer();
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Metoda wywolywana, gdy nadejdzie nowa wiadomosc.
	 * <!-- end-UML-doc -->
	 * @param message
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected abstract void onNewMessage(Message message);

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
		stopThread();
		cleanupTransaction();
		notifyEndTransactionListeners();
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
		new Thread(
				new Runnable() {
					@Override
					public void run() {
						Logger.getInstance().log("startThread " + Thread.currentThread() , "TPC", Level.INFO);
						waitForMessage();
					}
				}, "TPC_THREAD"
		).start();
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Konczy prace watku.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void stopThread() {
		//TODO
	}

	/** 
	 * Dodaje obiekt, ktory chce zostac poinformowany o koncu transakcji.
	 * @param listener
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addEndTransactionListener(EndTransactionListener listener) {
		endTransactionListeners.add(listener);
	}

	/** 
	 * Informuje wszystkich zainteresowanych o tym, ze zakonczono transakcje.
	 */
	private void notifyEndTransactionListeners() {
		for(EndTransactionListener listener : this.endTransactionListeners) {
			listener.onEndTransaction(this);
		}
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Ustawia zapytanie, ktore ma zostac wykonane przez baze danych w ramach transakcji
	 * <!-- end-UML-doc -->
	 * @param queryString
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;zapytanie,&nbsp;ktore&nbsp;ma&nbsp;zostac&nbsp;wykonane&nbsp;przez&nbsp;baze&nbsp;danych&nbsp;w&nbsp;ramach&nbsp;transakcji
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getQueryString() {
		return queryString;
	}

	public TcpSender getTcpSender() {
		return tcpSender;
	}

	public void setTcpSender(TcpSender tcpSender) {
		this.tcpSender = tcpSender;
	}
	
	public void setConnector(DbConnector connector) {
		this.connector = connector;
	}
}