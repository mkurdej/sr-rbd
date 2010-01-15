package ddb.restore;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.MessageType;

public class RestoreNack extends RestoreMessage {

	@Override
	protected void fromBinary(DataInputStream s) throws IOException {
		// empty
	}

	@Override
	protected MessageType getType() {
		return MessageType.RESTORE_NACK;
	}

	@Override
	protected void toBinary(DataOutputStream s) throws IOException {
		// empty
	}

}
