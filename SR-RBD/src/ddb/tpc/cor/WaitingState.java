/**
 * 
 */
package ddb.tpc.cor;

import java.net.InetSocketAddress;

import ddb.Logger;
import ddb.msg.client.ConflictMessage;
import ddb.msg.client.TimeoutMessage;
import ddb.tpc.TimeoutListener;
import ddb.tpc.msg.PreCommitMessage;

/**
 */
public class WaitingState extends CoordinatorState {
	
	private final static String LOGGING_NAME = "Coordinator.WaitingState";
	
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
		Logger.getInstance().log("Got answer " 
			+ coordinator.getAnswerCount() + " of " 
			+ coordinator.getNodesCount(), LOGGING_NAME, 
			Logger.Level.INFO);
	}

	/** 
	 *  @see CoordinatorState#onNoForCommit(String node)
	 * 
	 */
	@Override
	public void onNoForCommit(InetSocketAddress node) {
		Logger.getInstance().log("Got no - aborting!", LOGGING_NAME, Logger.Level.INFO);
		coordinator.abortTransaction(new ConflictMessage());
	}
}