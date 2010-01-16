/**
 * 
 */
package ddb.tpc.cor;

import java.net.SocketAddress;

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
	@Override
	public void onTimeout() {
		coordinator.abortTransaction(new TimeoutMessage());
	}

	/** 
	 *  @see CoordinatorState#onYesForCommit(String node)
	 */
	@Override
	public void onYesForCommit(SocketAddress node) {
		coordinator.processAnswer(node, new PreCommitMessage(), new PreparedState());
	}

	/** 
	 *  @see CoordinatorState#onNoForCommit(String node)
	 * 
	 */
	@Override
	public void onNoForCommit(SocketAddress node) {
		coordinator.abortTransaction(new ConflictMessage());
	}

	/** 
	 *  @see CoordinatorState#onAckPreCommit(String node)
	 *
	 */
	@Override
	public void onAckPreCommit(SocketAddress node) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 *  @see CoordinatorState#onHaveCommitted(HaveCommittedMessage message)
	 */
	@Override
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
	@Override
	public void onErrorMessage(ErrorMessage message) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}
}