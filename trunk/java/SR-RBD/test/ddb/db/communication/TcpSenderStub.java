package ddb.db.communication;

import ddb.communication.TcpSender;
import ddb.msg.Message;

public class TcpSenderStub extends TcpSender {

	public TcpSenderStub() {
		super();
	}


	private Message lastMessage;
	private String lastAddress;
	
	public void sendToAllNodes(Message message) {
		lastMessage = message;
	}

	public void sendToClient(Message message, String inetAddress, int port) {
		lastMessage = message;
		lastAddress = inetAddress;

	}

	public void sendToNode(Message message, String to) {
		lastMessage = message;
		lastAddress = to;
	}

	public Message getLastMessage() {
		return lastMessage;
	}

	
	public String getLastAddress() {
		return lastAddress;
	}
}
