package ddb.msg.client;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.MessageType;

public class ConflictMessage extends ClientResponse {

	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		// empty
	}

	@Override
	public MessageType getType() {
		return MessageType.CLIENT_CONFLICT;
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		// empty
	}

}
