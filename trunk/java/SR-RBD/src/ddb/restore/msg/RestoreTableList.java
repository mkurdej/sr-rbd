package ddb.restore.msg;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.MessageType;

public class RestoreTableList extends RestoreMessage {

	private TableVersion[] tables = null;
	
	public RestoreTableList()
	{
		// empty
	}
	
	public RestoreTableList(TableVersion[] tv)
	{
		setTables(tv);
	}
	
	public TableVersion[] getTables() {
		return tables;
	}
	
	public void setTables(TableVersion[] t) {
		tables = t;
	}
	
	
	@Override
	protected void fromBinary(DataInputStream s) throws IOException {
		int count = s.readInt();
		tables = new TableVersion[count];
		
		String name;
		int version;
		
		for(int i = 0; i < count; ++i)
		{
			name = s.readString();
			version = s.readInt();
			
			tables[i] = new TableVersion(name, version);
		}
	}
	
	@Override
	public MessageType getType() {
		return MessageType.RESTORE_TABLELIST;
	}

	@Override
	protected void toBinary(DataOutputStream s) throws IOException {
		// write length
		s.writeInt(tables.length);
		
		// write tables
		for(TableVersion tv : tables)
		{
			s.writeString(tv.getTableName());
			s.writeInt(tv.getVersion());
		}
		
	}

}
