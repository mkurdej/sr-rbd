package ddb.restore.msg;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.MessageType;

public class RestoreTableList extends RestoreMessage{

	@Override
	protected void fromBinary(DataInputStream s) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MessageType getType() {
		return MessageType.RESTORE_TABLELIST;
	}

	@Override
	protected void toBinary(DataOutputStream s) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
