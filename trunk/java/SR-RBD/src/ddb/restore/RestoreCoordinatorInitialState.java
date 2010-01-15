package ddb.restore;

import ddb.tpc.cor.WaitingState;
import ddb.tpc.msg.ErrorMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.TransactionMessage;

public class RestoreCoordinatorInitialState extends RestoreCoordinatorState {

	@Override
	public void onRestoreAck(String node) {
		coordinator.changeState(new RestoreCoordinatorRestoringState());
	}

	@Override
	public void onRestoreNack(String node) {
		coordinator.finished();
	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub

	}

}
