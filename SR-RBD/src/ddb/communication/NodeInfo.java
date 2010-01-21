package ddb.communication;

import java.net.Socket;

public class NodeInfo {
	private Socket socket;
	private boolean isServer;
	
	public NodeInfo(Socket s, boolean isServer)
	{
		setSocket(s);
		setIsServer(isServer);
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public Socket getSocket() {
		return socket;
	}
	protected void setIsServer(boolean isServer) {
		this.isServer = isServer;
	}
	protected boolean getIsServer() {
		return isServer;
	}
}
