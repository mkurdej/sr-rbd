/**
 * 
 */
package ddb.tpc.cor;

import java.net.SocketAddress;

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
	abstract public void onYesForCommit(SocketAddress node);

	/** 
	 * @param node
	 */
	abstract public void onNoForCommit(SocketAddress node);

	/** 
	 * @param node
	 */
	abstract public void onAckPreCommit(SocketAddress node);

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