package ddb.restore.msg;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.db.TableVersion;
import ddb.msg.MessageType;

public class RestoreTableList extends RestoreMessage {

	private List<TableVersion> tables = new LinkedList<TableVersion>();
	
	public RestoreTableList()
	{
		
	}
	
	public RestoreTableList(List<TableVersion> list)
	{
		this.tables = list;
	}
	
	public void setTables(List<TableVersion> tables) {
		this.tables = tables;
	}

	public List<TableVersion> getTables() {
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
		return MessageType.RESTORE_TABLELIST;
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
