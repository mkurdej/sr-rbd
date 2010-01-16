package ddb.tpc.cor;

import java.net.InetSocketAddress;

import ddb.tpc.msg.ErrorMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.TransactionMessage;

public class AbortState extends CoordinatorState {

	@Override
	public void onAckPreCommit(InetSocketAddress node) {
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
	public void onNoForCommit(InetSocketAddress node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTransaction(TransactionMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onYesForCommit(InetSocketAddress node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub

	}

}
