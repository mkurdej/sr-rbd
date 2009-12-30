/**
 * 
 */
package ddb.tpc.coh;

import ddb.msg.Message;
import ddb.tpc.TPCParticipant;
import static ddb.db.DatabaseState.*;
import static ddb.communication.TcpSender.*;
import static ddb.db.DbConnector.*;
import ddb.tpc.msg.AbortMessage;
import ddb.tpc.msg.CanCommitMessage;
import ddb.tpc.msg.DoCommitMessage;
import ddb.tpc.msg.ErrorMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.PreCommitMessage;
import ddb.tpc.msg.TPCMessage;
import ddb.db.DBException;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class CohordImpl extends TPCParticipant {
	private static final int TIMEOUT = 5000;
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

	
	public CohordImpl() {
		setState(new InitState());
		
	}
	/** 
	 * <!-- begin-UML-doc -->
	 * Wyslanie wiadomosci do koordynatora transakcji
	 * <!-- end-UML-doc -->
	 * @param message
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void replyToCoordinator(TPCMessage message) {
		this.tcpSender.sendToNode(message, getCoordinatorAddress());
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Zmiana stanu kohorta.
	 * <!-- end-UML-doc -->
	 * @param state
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void changeState(CohordState state) {
		stopTimer();
		startTimer(TIMEOUT);
		setState(state);
		waitForMessage();
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Callback wywolywany, gdy wystapi timeout
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onTimeout() {
		state.onTimeout();
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Ustawia adres&nbsp;koordynator&nbsp;transakcji,&nbsp;w&nbsp;ktorej&nbsp;uczestniczy&nbsp;ten&nbsp;kohort
	 * <!-- end-UML-doc -->
	 * @param address
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setCoordinatorAddress(String address) {
		this.coordinatorAddress = address;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;adres&nbsp;koordynator&nbsp;transakcji,&nbsp;w&nbsp;ktorej&nbsp;uczestniczy&nbsp;ten&nbsp;kohort
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getCoordinatorAddress() {
		return this.coordinatorAddress;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * Metoda&nbsp;dokonujaca&nbsp;wykonania&nbsp;transakcji
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void commitTransaction() {
		try {
			this.connector.query(getQueryString());
			replyToCoordinator(new HaveCommittedMessage());
			setState(new CommittedState());
		} catch (DBException exception) {
			ErrorMessage msg = new ErrorMessage();
			msg.setException(exception);
			replyToCoordinator(msg);
			setState(new AbortState());
		}
		endTransaction();
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
	protected void onNewMessage(Message message) {
		if(message instanceof CanCommitMessage) {
			onCanCommit((CanCommitMessage)message);
		}
		else if(message instanceof AbortMessage) {
			onAbort();
		}
		else if(message instanceof DoCommitMessage) {
			onDoCommit();
		}
		else if(message instanceof PreCommitMessage) {
			onPreCommit();
		}
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see TPCParticipant#cleanupTransaction()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void cleanupTransaction() {
		this.getDatabaseState().unlockTable(this.getTableName());
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CohordState#onPreCommit()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onPreCommit() {
		state.onPreCommit();
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CohordState#onCanCommit()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onCanCommit(CanCommitMessage message) {
		state.onCanCommit(message);
		
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CohordState#onAbort()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onAbort() {
		state.onAbort();
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CohordState#onDoCommit()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onDoCommit() {
		state.onDoCommit();
	}

	public CohordState getState() {
		return state;
	}

	protected void setState(CohordState state) {
		this.state = state;
		state.setCohord(this);
	}
	
	
}