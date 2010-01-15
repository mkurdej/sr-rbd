package ddb.restore.msg;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.MessageType;

public class RestoreAck extends RestoreMessage {

	@Override
	protected void fromBinary(DataInputStream s) throws IOException {
		// empty

	}

	@Override
	public MessageType getType() {
		return MessageType.RESTORE_ACK;
	}

	@Override
	protected void toBinary(DataOutputStream s) throws IOException {
		// empty
	}

}
