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
	
	private void setTables(List<TableVersion> tables) {
		this.tables = tables;
	}

	private List<TableVersion> getTables() {
		return tables;
	}
	
	// TODO: zaimplementowac knostrukt przyjmujacy numer wersji bazy danych
	// TODO: dodac zmienna przechowywujaca numer wersji bazy danych
	
	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		// read count
		int count = s.readInt();
		setTables(new LinkedList<TableVersion>());
		
		// read contents
		String name;
		int version;
		
		for(int i = 0; i < count; ++i)
		{
			name = s.readString();
			version = s.readInt();
			
			getTables().add(new TableVersion(name, version));
		}
		
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
		{
			s.writeString(tv.getTableName());
			s.writeInt(tv.getVersion());
		}
	}
}
