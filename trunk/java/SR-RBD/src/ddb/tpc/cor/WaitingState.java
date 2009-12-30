/**
 * 
 */
package ddb.tpc.cor;

import ddb.msg.client.ConflictMessage;
import ddb.msg.client.TimeoutMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.ErrorMessage;
import ddb.tpc.msg.PreCommitMessage;
import ddb.tpc.msg.TransactionMessage;

/**
 */
public class WaitingState extends CoordinatorState {
	/** 
	 * 
	 * @see TimeoutListener#onTimeout()
	 * 
	 */
	public void onTimeout() {
		coordinator.abortTransaction(new TimeoutMessage());
	}

	/** 
	 *  @see CoordinatorState#onYesForCommit(String node)
	 */
	public void onYesForCommit(String node) {
		coordinator.processAnswer(node, new PreCommitMessage(), new PreparedState());
	}

	/** 
	 *  @see CoordinatorState#onNoForCommit(String node)
	 * 
	 */
	public void onNoForCommit(String node) {
		coordinator.abortTransaction(new ConflictMessage());
	}

	/** 
	 *  @see CoordinatorState#onAckPreCommit(String node)
	 *
	 */
	public void onAckPreCommit(String node) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 *  @see CoordinatorState#onHaveCommitted(HaveCommittedMessage message)
	 */
	public void onHaveCommitted(HaveCommittedMessage message) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * @see CoordinatorState#onTransaction()
	 * 
	 */
	public void onTransaction(TransactionMessage message) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 *  @see CoordinatorState#onErrorMessage(ErrorMessage message)
	 * 
	 */
	public void onErrorMessage(ErrorMessage message) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}
}