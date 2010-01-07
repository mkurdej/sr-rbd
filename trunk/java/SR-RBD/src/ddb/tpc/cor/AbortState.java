package ddb.tpc.cor;

import ddb.tpc.msg.ErrorMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.TransactionMessage;

public class AbortState extends CoordinatorState {

	@Override
	public void onAckPreCommit(String node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onErrorMessage(ErrorMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHaveCommitted(HaveCommittedMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNoForCommit(String node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTransaction(TransactionMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onYesForCommit(String node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub

	}

}
