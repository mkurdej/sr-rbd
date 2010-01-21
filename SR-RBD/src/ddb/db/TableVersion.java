package ddb.db;

import java.io.IOException;

import ddb.BinarySerializable;
import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;

public class TableVersion implements BinarySerializable
{
	public TableVersion(String name, int version)
	{
		setTableName(name);
		setVersion(version);
	}
	
	public TableVersion(DataInputStream s) throws IOException
	{
		fromBinary(s);
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableName() {
		return tableName;
	}

	public void setVersion(int version) {
		this.tableVersion = version;
	}

	public int getVersion() {
		return tableVersion;
	}
	
	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		tableName = s.readString();
		tableVersion = s.readInt();
	}
	
	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		s.writeString(tableName);
		s.writeInt(tableVersion);
	}

	private String tableName;
	private int tableVersion;
}
