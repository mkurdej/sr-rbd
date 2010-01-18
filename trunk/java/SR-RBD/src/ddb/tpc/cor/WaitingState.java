/**
 * 
 */
package ddb.tpc.cor;

import java.net.InetAddress;

import ddb.msg.client.ConflictMessage;
import ddb.msg.client.TimeoutMessage;
import ddb.tpc.TimeoutListener;
import ddb.tpc.msg.PreCommitMessage;

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
	public void onYesForCommit(InetAddress node) {
		coordinator.processAnswer(node, new PreCommitMessage(), new PreparedState());
	}

	/** 
	 *  @see CoordinatorState#onNoForCommit(String node)
	 * 
	 */
	@Override
	public void onNoForCommit(InetAddress node) {
		coordinator.abortTransaction(new ConflictMessage());
	}
}