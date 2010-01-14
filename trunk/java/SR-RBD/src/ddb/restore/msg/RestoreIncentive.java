package ddb.restore.msg;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.Message;
import ddb.msg.MessageType;

public class RestoreIncentive extends Message {

	@Override
	public void fromBinary(DataInputStream s) {
		// TODO Auto-generated method stub

	}

	@Override
	public MessageType getType() {
		return MessageType.RESTORE_INCENTIVE;
	}

	@Override
	public void toBinary(DataOutputStream s) {
		// TODO Auto-generated method stub

	}

}
