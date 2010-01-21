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
	private int listeningPort;
	private List<TableVersion> tables;

	
	public HelloMessage()
	{
		// empty
	}
	
	public HelloMessage(List<TableVersion> tables, int port)
	{
		setTables(tables);
		setListeningPort(port);
	}
	
	
	public void setListeningPort(int listeningPort) {
		this.listeningPort = listeningPort;
	}

	public int getListeningPort() {
		return listeningPort;
	}
	
	public void setTables(List<TableVersion> tables) {
		this.tables = tables;
	}

	public List<TableVersion> getTables() {
		return tables;
	}
	
	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		// read port
		listeningPort = s.readInt();
		
		// read count
		int count = s.readInt();
		setTables(new LinkedList<TableVersion>());
		
		// read contents
		while(count-- > 0)
			getTables().add(new TableVersion(s));
		
	}

	@Override
	public MessageType getType() {
		return MessageType.HELLO_MESSAGE;
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		// write port
		s.writeInt(listeningPort);
		
		// write length
		s.writeInt(getTables().size());
		
		// write tables
		for(TableVersion tv : getTables())
			tv.toBinary(s);
	}
}
