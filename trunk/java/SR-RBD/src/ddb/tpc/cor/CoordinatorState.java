/**
 * 
 */
package ddb.tpc.cor;

import java.net.InetSocketAddress;

import ddb.tpc.TimeoutListener;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.ErrorMessage;
import ddb.tpc.msg.TransactionMessage;

/** 
 * @author PKalanski
 */
abstract public class CoordinatorState implements TimeoutListener {
	protected CoordinatorImpl coordinator;
	
	
	public void setCoordinator(CoordinatorImpl coordinator) {
		this.coordinator = coordinator;
	}

	/** 
	 * @param node
	 */
	abstract public void onYesForCommit(InetSocketAddress node);

	/** 
	 * @param node
	 */
	abstract public void onNoForCommit(InetSocketAddress node);

	/** 
	 * @param node
	 */
	abstract public void onAckPreCommit(InetSocketAddress node);

	/** 
	 * @param message
	 */
	abstract public void onHaveCommitted(HaveCommittedMessage message);

	/** 
	 */
	abstract public void onTransaction(TransactionMessage message);

	/** 
	 * @param message
	 */
	abstract public void onErrorMessage(ErrorMessage message);
}