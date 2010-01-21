package ddb.tpc.msg;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.Message;
import ddb.msg.MessageType;
/**
 * 
 * @author PKalanski
 * Wiadomosc reprezentujaca wystapienie timeout
 */
public class TimeoutMessage extends Message {

	@Override
	public MessageType getType() {
		// empty
		return MessageType.TPC_TIMEOUT_MESSAGE;
	}

	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		// empty

	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		// empty

	}

}
