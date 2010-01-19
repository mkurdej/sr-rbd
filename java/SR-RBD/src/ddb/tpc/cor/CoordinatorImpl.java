/**
 * 
 */
package ddb.tpc.cor;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import ddb.Logger;
import ddb.communication.TcpSender;
import ddb.db.DBException;
import ddb.db.DatabaseTable;
import ddb.msg.Message;
import ddb.msg.client.ResultsetMessage;
import ddb.msg.client.TimeoutMessage;
import ddb.tpc.msg.AbortMessage;
import ddb.tpc.msg.AckPreCommitMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.NoForCommitMessage;
import ddb.tpc.msg.TPCMessage;
import ddb.tpc.msg.TransactionMessage;
import ddb.tpc.msg.YesForCommitMessage;

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
	private Set<InetSocketAddress> answers;
	/** 
	 * Lista wezlo bioracych udzial w transakcji
	 */
	private int nodes;
	/**
	 * Adres klienta, ktory zarzadal wykonania transakcji
	 */
	private InetSocketAddress clientAddress;
	
	public CoordinatorImpl() {
		super();
		//this.connector = DbConnectorImpl.getInstance();
		setState(new InitState());
		this.answers = new HashSet<InetSocketAddress>();
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
	public void setNodeCount(int nodeCount) {
		this.nodes = nodeCount;
	}

	/** 
	 * <p>
	 *     Ustawia odpowiedz od podanego wezla
	 * </p>
	 * @param nodeName nazwa wezla
	 */
	private void addAnswer(InetSocketAddress nodeName) {
		this.answers.add(nodeName);
	}

	/** 
	 * Sprawdza czy wszystkie wezly odpowiedzialy pozytywnie.
	 * @return
	 */
	private boolean checkAnswers() {
		return this.answers.size() == this.nodes;
	}

	/** 
	 * Wyslij wiadomosc do wszystkich wezlow bioracych udzial w transakcji.
	 * @param message
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void broadcastMessage(TPCMessage message) {
		this.clearAnswers();
		message.setTransactionId(getTransactionId());
		TcpSender.getInstance().sendToAllServerNodes(message);
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
	}

	/** 
	 * Metoda wywolywana, gdy transakcja zostala zakonczona niepomyslnie.
	 */
	public void abortTransaction(Message messageToClient) {
		broadcastMessage(new AbortMessage());
		TcpSender.getInstance().sendToNode(messageToClient, getClientAddress());
		setState(new AbortState());
		endTransaction();
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
	public void processAnswer(InetSocketAddress node, TPCMessage message, CoordinatorState nextState) {
		addAnswer(node);
		if(checkAnswers()) {
			broadcastMessage(message);
			changeState(nextState);
		}
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Ustawia adres&nbsp;klienta,&nbsp;ktory&nbsp;zarzadal&nbsp;wykonania&nbsp;transakcji
	 * <!-- end-UML-doc -->
	 * @param clientAddress
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setClientAddress(InetSocketAddress clientAddress) {
		this.clientAddress = clientAddress;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;adres&nbsp;klienta,&nbsp;ktory&nbsp;zarzadal&nbsp;wykonania&nbsp;transakcji
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public InetSocketAddress getClientAddress() {
		return clientAddress;
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
			TcpSender.getInstance().sendToNode(msg, this.getClientAddress());
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
		TcpSender.getInstance().sendToNode(msg, this.getClientAddress());
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
			state.onYesForCommit(message.getSender());
		}
		else if(message instanceof NoForCommitMessage) {
			state.onNoForCommit(message.getSender());
		}
		else if(message instanceof HaveCommittedMessage) {
			state.onHaveCommitted((HaveCommittedMessage)message);
		}
		else if(message instanceof AckPreCommitMessage) {
			state.onAckPreCommit(message.getSender());
		}
		else if(message instanceof TransactionMessage) {
			state.onTransaction((TransactionMessage)message);
		}
		else if(message instanceof TimeoutMessage) {
			state.onTimeout();
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