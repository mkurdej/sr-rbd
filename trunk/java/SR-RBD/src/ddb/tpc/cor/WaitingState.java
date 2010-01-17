/**
 * 
 */
package ddb.tpc.cor;

import java.net.InetSocketAddress;

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
	public void onYesForCommit(InetSocketAddress node) {
		coordinator.processAnswer(node, new PreCommitMessage(), new PreparedState());
	}

	/** 
	 *  @see CoordinatorState#onNoForCommit(String node)
	 * 
	 */
	@Override
	public void onNoForCommit(InetSocketAddress node) {
		coordinator.abortTransaction(new ConflictMessage());
	}
}