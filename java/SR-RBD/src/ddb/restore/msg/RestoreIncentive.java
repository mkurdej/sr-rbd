package ddb.restore.msg;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.Message;
import ddb.msg.MessageType;

public class RestoreIncentive extends Message {

	public RestoreIncentive() {
		// empty
	}
	
	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		// empty
	}

	@Override
	public MessageType getType() {
		return MessageType.RESTORE_INCENTIVE;
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		// empty
	}
}
