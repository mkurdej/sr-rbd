/**
 * 
 */
package ddb.tpc.cor;

import ddb.tpc.msg.ErrorMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.TransactionMessage;

/**
 * TODO 
 *
 * @author Marek Kurdej (curdeius[at]gmail.com)
 *
 */
public class AbortState extends CoordinatorState {

	/* (non-Javadoc)
	 * @see ddb.tpc.cor.CoordinatorState#onAckPreCommit(java.lang.String)
	 */
	@Override
	public void onAckPreCommit(String node) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ddb.tpc.cor.CoordinatorState#onErrorMessage(ddb.tpc.msg.ErrorMessage)
	 */
	@Override
	public void onErrorMessage(ErrorMessage message) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ddb.tpc.cor.CoordinatorState#onHaveCommitted(ddb.tpc.msg.HaveCommittedMessage)
	 */
	@Override
	public void onHaveCommitted(HaveCommittedMessage message) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ddb.tpc.cor.CoordinatorState#onNoForCommit(java.lang.String)
	 */
	@Override
	public void onNoForCommit(String node) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ddb.tpc.cor.CoordinatorState#onTransaction(ddb.tpc.msg.TransactionMessage)
	 */
	@Override
	public void onTransaction(TransactionMessage message) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ddb.tpc.cor.CoordinatorState#onYesForCommit(java.lang.String)
	 */
	@Override
	public void onYesForCommit(String node) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ddb.tpc.TimeoutListener#onTimeout()
	 */
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub

	}

}
