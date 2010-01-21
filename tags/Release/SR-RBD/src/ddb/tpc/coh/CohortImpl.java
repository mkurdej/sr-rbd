/**
 * 
 */
package ddb.tpc.coh;

import ddb.communication.TcpSender;
import ddb.db.DBException;
import ddb.msg.Message;
import ddb.msg.client.TimeoutMessage;
import ddb.tpc.TPCParticipant;
import ddb.tpc.msg.AbortMessage;
import ddb.tpc.msg.CanCommitMessage;
import ddb.tpc.msg.DoCommitMessage;
import ddb.tpc.msg.ErrorMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.PreCommitMessage;
import ddb.tpc.msg.TPCMessage;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author User
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class CohortImpl extends Cohort {
	
	public CohortImpl() {
		setState(new InitState());
	}
	/**
	 * <!-- begin-UML-doc --> Wyslanie wiadomosci do koordynatora transakcji
	 * <!-- end-UML-doc -->
	 * 
	 * @param message
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void replyToCoordinator(TPCMessage message) {
		message.setTransactionId(getTransactionId());
		TcpSender.getInstance().sendToNode(message, getCoordinatorAddress());
	}

	/**
	 * <!-- begin-UML-doc --> Zmiana stanu kohorta. <!-- end-UML-doc -->
	 * 
	 * @param cohstate
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void changeState(CohortState cohstate) {
		stopTimer();
		startTimer(TIMEOUT);
		setState(cohstate);
	}
	/**
	 * <!-- begin-UML-doc -->
	 * Metoda&nbsp;dokonujaca&nbsp;wykonania&nbsp;transakcji <!-- end-UML-doc
	 * -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void commitTransaction() {
		try {
			this.connector.query(getQueryString());
			this.getDatabaseState().incrementTableVersion(getTableName());
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
	 * Metoda&nbsp;wywo³ywana,&nbsp;gdy&nbsp;nie&nbsp;powiedzie
	 * &nbsp;sie&nbsp;wykonanie
	 * &nbsp;transakcji&nbsp;z&nbsp;powodu&nbsp;bledu&nbsp
	 * ;w&nbsp;bazie&nbsp;danych <!-- end-UML-doc -->
	 * 
	 * @param exception
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void processDBException(DBException exception) {
		// empty
	}

	/**
	 * @see TPCParticipant#onNewMessage(TPCMessage message)
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	protected void onNewMessage(Message message) {
		if (message instanceof CanCommitMessage) {
			onCanCommit((CanCommitMessage) message);
		} else if (message instanceof AbortMessage) {
			onAbort();
		} else if (message instanceof DoCommitMessage) {
			onDoCommit();
		} else if (message instanceof PreCommitMessage) {
			onPreCommit();
		} else if(message instanceof TimeoutMessage) {
			state.onTimeout();
		}
	}

	/**
	 * @see TPCParticipant#cleanupTransaction()
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	protected void cleanupTransaction() {
		this.getDatabaseState().unlockTable(this.getTableName());
	}

	/**
	 * @see CohortState#onPreCommit()
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onPreCommit() {
		state.onPreCommit();
	}

	/**
	 * @see CohortState#onCanCommit()
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onCanCommit(CanCommitMessage message) {
		state.onCanCommit(message);

	}

	/**
	 * @see CohortState#onAbort()
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onAbort() {
		state.onAbort();
	}

	/**
	 * @see CohortState#onDoCommit()
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onDoCommit() {
		state.onDoCommit();
	}

}