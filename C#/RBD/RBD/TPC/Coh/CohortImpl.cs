
using RBD.DB;
using RBD.Msg;
using RBD.TPC.Msg;
using RBD.Communication;

namespace RBD.TPC.COH
{
public class CohortImpl : Cohort {
	
	public CohortImpl() {
		setState(new InitState());
	}

	override public void replyToCoordinator(TPCMessage message) {
        message.TransactionId = this.TransactionId;
		TcpSender.getInstance().sendToNode(message, getCoordinatorAddress());
	}


    override public void changeState(CohortState cohstate)
    {
		stopTimer();
		startTimer(TIMEOUT);
		setState(cohstate);
	}

	override public void commitTransaction() {
		try {
			this.connector.query(getQueryString());
			this.DatabaseState.incrementTableVersion(this.TableName);
			replyToCoordinator(new HaveCommittedMessage());
			setState(new CommittedState());
		} catch (DBException exception) {
			ErrorMessage msg = new ErrorMessage();
			msg.Exception = exception;
			replyToCoordinator(msg);
			setState(new AbortState());
		}
		endTransaction();
	}

	public void processDBException(DBException exception) {
		// empty
	}

    override protected void onNewMessage(Message message)
    {
		if (message is CanCommitMessage) {
			onCanCommit((CanCommitMessage) message);
		} else if (message is AbortMessage) {
			onAbort();
		} else if (message is DoCommitMessage) {
			onDoCommit();
		} else if (message is PreCommitMessage) {
			onPreCommit();
		} else if(message is TimeoutMessage) {
			state.onTimeout();
		}
	}

    override protected void cleanupTransaction()
    {
		this.DatabaseState.unlockTable(this.TableName);
	}

	public void onPreCommit() {
		state.onPreCommit();
	}

	public void onCanCommit(CanCommitMessage message) {
		state.onCanCommit(message);

	}

	public void onAbort() {
		state.onAbort();
	}

	public void onDoCommit() {
		state.onDoCommit();
	}
}

}