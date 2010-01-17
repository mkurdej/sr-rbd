package ddb.db.communication;

import ddb.communication.TcpSender;
import ddb.msg.Message;

public class TcpSenderStub implements TcpSender {

	private Message lastMessage;
	private String lastAddress;
	
	@Override
	public void sendToAllNodes(Message message) {
		lastMessage = message;

	}

	@Override
	public void sendToClient(Message message, String inetAddress, int port) {
		lastMessage = message;
		lastAddress = inetAddress;

	}

	@Override
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
