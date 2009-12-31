/**
 * 
 */
package ddb.tpc.cor;

import ddb.Logger;
import ddb.msg.Message;
import ddb.msg.client.ResultsetMessage;
import ddb.msg.client.TimeoutMessage;
import ddb.tpc.TPCParticipant;
import static ddb.db.DatabaseState.*;
import static ddb.communication.TcpSender.*;
import static ddb.db.DbConnector.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.rowset.spi.TransactionalWriter;

import sun.security.jca.GetInstance;

import ddb.tpc.msg.AbortMessage;
import ddb.tpc.msg.AckPreCommitMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.NoForCommitMessage;
import ddb.tpc.msg.TPCMessage;
import ddb.tpc.msg.TransactionMessage;
import ddb.tpc.msg.YesForCommitMessage;
import ddb.db.DBException;
import ddb.db.DatabaseState;
import ddb.db.DatabaseTable;
import ddb.db.DbConnector;
import ddb.db.DbConnectorImpl;

/** 
 */
public class CoordinatorImpl extends Coordinator {
	/**
	 * 
	 */
	private CoordinatorState state;
	/** 
	 * Lista pozytywnych&nbsp;odpowiedzi&nbsp;od&nbsp;wezlow
	 */
	private Set<String> answers;
	/** 
	 * Lista wezlo bioracych udzial w transakcji
	 */
	private Set<String> nodes;
	/**
	 * Adres klienta, ktory zarzadal wykonania transakcji
	 */
	private String clientAddress;
	/** 
	 * Numer portu klienta, ktory zarzadal wykonania transakcji
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int clientPort;
	
	public CoordinatorImpl() {
		super();
		//this.connector = DbConnectorImpl.getInstance();
		setState(new InitState());
		this.answers = new HashSet<String>();
	}

	protected void setState(CoordinatorState state) {
		this.state = state;
		state.setCoordinator(this);
	}


	/** 
	 * Czysci wszystkie odpowiedzi.
	 */
	private void clearAnswers() {
		this.answers.clear();
	}

	/** 
	 * Ustawia liste wezlow bioracych udzial w transakcji.
	 * @param nodeList
	 */
	public void setNodeList(Set<String> nodeList) {
		this.nodes = nodeList;
	}

	/** 
	 * <p>
	 *     Ustawia odpowiedz od podanego wezla
	 * </p>
	 * @param nodeName nazwa wezla
	 */
	private void addAnswer(String nodeName) {
		this.answers.add(nodeName);
	}

	/** 
	 * Sprawdza czy wszystkie wezly odpowiedzialy pozytywnie.
	 * @return
	 */
	private boolean checkAnswers() {
		return this.answers.size() == this.nodes.size();
	}

	/** 
	 * Wyslij wiadomosc do wszystkich wezlow bioracych udzial w transakcji.
	 * @param message
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void broadcastMessage(TPCMessage message) {
		this.clearAnswers();
		message.setTransactionId(getTransactionId());
		this.tcpSender.sendToAllNodes(message);
	}

	/** 
	 * Zmienia stan koordynatora
	 * @param state nowy stan
	 */
	public void changeState(CoordinatorState state) {
		this.stopTimer();
		this.startTimer(TIMEOUT);
		this.state = state;
		state.setCoordinator(this);
		this.waitForMessage();
	}

	/** 
	 * Metoda wywolywana, gdy transakcja zostala zakonczona niepomyslnie.
	 */
	public void abortTransaction(Message messageToClient) {
		broadcastMessage(new AbortMessage());
		tcpSender.sendToClient(messageToClient, getClientAddress(), getClientPort());
		setState(new AbortState());
		endTransaction();
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Callback&nbsp;wywolywany,&nbsp;gdy&nbsp;wystapi&nbsp;timeout
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onTimeout() {
		state.onTimeout();
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
	public void processAnswer(String node, TPCMessage message, CoordinatorState nextState) {
		addAnswer(node);
		if(checkAnswers()) {
			broadcastMessage(message);
			changeState(nextState);
		}
		else {
			waitForMessage();
		}
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Ustawia adres&nbsp;klienta,&nbsp;ktory&nbsp;zarzadal&nbsp;wykonania&nbsp;transakcji
	 * <!-- end-UML-doc -->
	 * @param clientAddress
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;adres&nbsp;klienta,&nbsp;ktory&nbsp;zarzadal&nbsp;wykonania&nbsp;transakcji
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getClientAddress() {
		return clientAddress;
	}

	/** 
	 * Ustawia numer portu klienta, ktory zarzadal wykonania transakcji
	 * @param port
	 */
	public void setClientPort(int port) {
		this.clientPort = port;
	}

	/** 
	 * Pobiera numer portu klienta, ktory zarzadal wykonania transakcji
	 * @return
	 */
	public int getClientPort() {
		return clientPort;
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
		try {
			DatabaseTable table = this.connector.query(getQueryString());
			ResultsetMessage msg = new ResultsetMessage();
			msg.setResultSet(table);
			tcpSender.sendToClient(msg, this.getClientAddress(), this.getClientPort());
			setState(new CommitState());
		}
		catch(DBException ex) {
			processDBException(ex);
		}
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Metoda wywo³ywana, gdy nie powiedzie sie wykonanie transakcji z powodu bledu w bazie danych
	 * <!-- end-UML-doc -->
	 * @param exception
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final void processDBException(DBException exception) {
		ddb.msg.client.ErrorMessage msg = new ddb.msg.client.ErrorMessage();
		msg.setException(exception);
		tcpSender.sendToClient(msg, this.getClientAddress(), this.getClientPort());
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see TPCParticipant#onNewMessage(TPCMessage message)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void onNewMessage(Message message) {
		Logger.getInstance().log("NewMessage: " + message, "Coordinator", Logger.Level.INFO);
		
		if(message instanceof YesForCommitMessage) {
			state.onYesForCommit(message.getSenderAddress());
		}
		else if(message instanceof NoForCommitMessage) {
			state.onNoForCommit(message.getSenderAddress());
		}
		else if(message instanceof HaveCommittedMessage) {
			state.onHaveCommitted((HaveCommittedMessage)message);
		}
		else if(message instanceof AckPreCommitMessage) {
			state.onAckPreCommit(message.getSenderAddress());
		}
		else if(message instanceof TransactionMessage) {
			state.onTransaction((TransactionMessage)message);
		}
		
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see TPCParticipant#cleanupTransaction()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void cleanupTransaction() {
		//do nothing
	}

	public CoordinatorState getState() {
		return state;
	}
	
}