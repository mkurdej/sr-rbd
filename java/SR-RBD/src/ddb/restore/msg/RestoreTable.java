package ddb.restore.msg;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.MessageType;

public class RestoreTable extends RestoreMessage{

	protected int tableVersion; 
	protected String tableName;
	protected String tableDump;
	
	public RestoreTable()
	{
		
	}
	
	public RestoreTable(int version, String name, String dump) {
		setTableVersion(version);
		setTableName(name);
		setTableDump(dump);
	}
	
	public void setTableName(String n) {
		tableName = n;
	}
	
	public int getTableVersion() {
		return tableVersion;
	}
	
	public void setTableVersion(int v) {
		tableVersion = v;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableDump(String d) {
		tableDump = d;
	}
	
	public String getTableDump() {
		return tableDump;
	}
	
	@Override
	protected void fromBinary(DataInputStream s) throws IOException {
		tableVersion = s.readInt();
		tableName = s.readString();
		tableDump = s.readString();
	}

	@Override
	public MessageType getType() {
		return MessageType.RESTORE_TABLE;
	}

	@Override
	protected void toBinary(DataOutputStream s) throws IOException {
		s.writeInt(tableVersion);
		s.writeString(tableName);
		s.writeString(tableDump);
	}

	

}
