using System.Net;
using System.Collections;
using System.Collections.Generic;
using RBD.TPC.Msg;
using RBD.DB;
using RBD.Msg;
using RBD.Msg.Client;

namespace RBD.TPC.COR
{
public class CoordinatorImpl : Coordinator {
	/**
	 * 
	 */
	private CoordinatorState state;
	/** 
	 * Lista pozytywnych&nbsp;odpowiedzi&nbsp;od&nbsp;wezlow
	 */
    private IList<IPAddress> answers;
	/** 
	 * Lista wezlo bioracych udzial w transakcji
	 */
    private IList<IPAddress> nodes;
	/**
	 * Adres klienta, ktory zarzadal wykonania transakcji
	 */
    private IPEndPoint clientAddress;
	
	public CoordinatorImpl() {
		//super();
		//this.connector = DbConnectorImpl.getInstance();
		setState(new InitState());
		this.answers = new List<IPAddress>();
	}

	protected void setState(CoordinatorState state) {
		this.state = state;
		state.setCoordinator(this);
	}


    public int getAnswerCount()
    {
        return this.answers.Count;
    }

    public int getNodesCount()
    {
        return this.nodes.Count;
    }


	/** 
	 * Czysci wszystkie odpowiedzi.
	 */
	private void clearAnswers() {
        this.answers.Clear();
	}

	/** 
	 * Ustawia liste wezlow bioracych udzial w transakcji.
	 * @param nodeList
	 */
	public void setNodeList(IList<IPAddress> nodeList) {
		this.nodes = nodeList;
	}

	/** 
	 * <p>
	 *     Ustawia odpowiedz od podanego wezla
	 * </p>
	 * @param nodeName nazwa wezla
	 */
    private void addAnswer(IPAddress nodeName)
    {
		this.answers.Add(nodeName);
	}

	/** 
	 * Sprawdza czy wszystkie wezly odpowiedzialy pozytywnie.
	 * @return
	 */
	private bool checkAnswers() {
		return this.answers.Count == this.nodes.Count;
	}

	/** 
	 * Wyslij wiadomosc do wszystkich wezlow bioracych udzial w transakcji.
	 * @param message
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void broadcastMessage(TPCMessage message) {
		this.clearAnswers();
        message.TransactionId = this.TransactionId;
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
    public void processAnswer(IPAddress node, TPCMessage message, CoordinatorState nextState)
    {
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
    override public void setClientAddress(IPEndPoint clientAddress)
    {
		this.clientAddress = clientAddress;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;adres&nbsp;klienta,&nbsp;ktory&nbsp;zarzadal&nbsp;wykonania&nbsp;transakcji
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public IPEndPoint getClientAddress()
    {
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
			msg.Result = table.ToString();
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
	public void processDBException(DBException exception) {
        RBD.Msg.Client.ErrorMessage msg = new RBD.Msg.Client.ErrorMessage();
		msg.Exception = exception;
		TcpSender.getInstance().sendToNode(msg, this.getClientAddress());
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see TPCParticipant#onNewMessage(TPCMessage message)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    override protected void onNewMessage(Message message)
    {
		Logger.getInstance().log("NewMessage: " + message, "Coordinator", Logger.Level.INFO);
		
		if(message is YesForCommitMessage) {
			state.onYesForCommit(message.Sender.Address);
		}
		else if(message is NoForCommitMessage) {
            state.onNoForCommit(message.Sender.Address);
		}
		else if(message is HaveCommittedMessage) {
			state.onHaveCommitted((HaveCommittedMessage)message);
		}
		else if(message is AckPreCommitMessage) {
			state.onAckPreCommit(message.Sender.Address);
		}
		else if(message is TransactionMessage) {
			state.onTransaction((TransactionMessage)message);
		}
		else if(message is RBD.TPC.Msg.TimeoutMessage) {
			state.onTimeout();
		}
        else if (message is RBD.TPC.Msg.ErrorMessage)
        {
            state.onErrorMessage((RBD.TPC.Msg.ErrorMessage)message); 
 		}
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see TPCParticipant#cleanupTransaction()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    override protected void cleanupTransaction()
    {
		//do nothing
	}

	public CoordinatorState getState() {
		return state;
	}	
}
}
