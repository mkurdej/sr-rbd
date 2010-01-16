package ddb.msg;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.db.TableVersion;
import ddb.msg.Message;

public class HelloMessage extends Message 
{
	private List<TableVersion> tables = new LinkedList<TableVersion>();

	
	public HelloMessage()
	{
		// empty
	}
	
	public HelloMessage(List<TableVersion> tables)
	{
		setTables(tables);
	}
	
	private void setTables(List<TableVersion> tables) {
		this.tables = tables;
	}

	private List<TableVersion> getTables() {
		return tables;
	}
	
	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		// read count
		int count = s.readInt();
		setTables(new LinkedList<TableVersion>());
		
		// read contents
		for(int i = 0; i < count; ++i)
			getTables().add(new TableVersion(s));
		
	}

	@Override
	public MessageType getType() {
		return MessageType.HELLO_MESSAGE;
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		// write length
		s.writeInt(getTables().size());
		
		// write tables
		for(TableVersion tv : getTables())
			tv.toBinary(s);
	}
}
