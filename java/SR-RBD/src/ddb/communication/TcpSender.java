/**
 * 
 */
package ddb.communication;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ddb.Logger;
import ddb.msg.Message;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author Administrator
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TcpSender {

	private final static String LOGGING_NAME = "TcpSender";
	private Map<SocketAddress, NodeInfo> nodes = new HashMap<SocketAddress, NodeInfo>();

	/**
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private static final TcpSender instance = new TcpSender();

	/**
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private TcpSender() {
		// begin-user-code
		// empty
		// end-user-code
	}

	/**
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	public static TcpSender getInstance() {
		// begin-user-code
		return instance;
		// end-user-code
	}
	
	public void removeNode(SocketAddress address) {
		if(nodes.remove(address) == null)
		{
			Logger.getInstance().log("Request to remove unexisting node: " + address.toString(), 
					LOGGING_NAME,
					Logger.Level.WARNING);
		}
	}

	public void addNodeBySocket(Socket s) {
		
		SocketAddress address = s.getRemoteSocketAddress();
		NodeInfo nodeInfo = new NodeInfo(s, false);
		
		if(nodes.get(address) != null)
		{
			Logger.getInstance().log("Request to add already existing node: " + address.toString(), 
					LOGGING_NAME,
					Logger.Level.WARNING);
		}
		
		nodes.put(address, nodeInfo);
	}
	
	public void markNodeAsServer(SocketAddress address){
		
		NodeInfo info = nodes.get(address);
		
		if(info != null)
		{
			info.setIsServer(true);
		}
		else
		{
			Logger.getInstance().log("Request to mark unexisting node: " + address.toString(), 
					LOGGING_NAME,
					Logger.Level.WARNING);
		}
	}
	
	
	
	/**
	 * /* (non-Javadoc) * @see TcpSender#sendToAllNodes(Message message)
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void sendToAllServerNodes(Message message) {
		// begin-user-code
		byte[] data; 
		
		// serialize
		data = message.Serialize();
		
		// search for server nodes
		Iterator<Map.Entry<SocketAddress, NodeInfo>> it = nodes.entrySet().iterator();

		while(it.hasNext())
		{
			Map.Entry<SocketAddress, NodeInfo> entry = it.next();
			NodeInfo node = entry.getValue();
			
			if(node.getIsServer())
			{
				if(!writeToNode(node.getSocket(),data))
				{
					it.remove();
				}
			}
		}
		// end-user-code
	}

	/**
	 * /* (non-Javadoc) * @see TcpSender#sendToNode(Message message, String to)
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void sendToNode(Message message, SocketAddress to) {
		// begin-user-code
		byte[] data; 
		
		// serialize
		data = message.Serialize();
		NodeInfo node = nodes.get(to);
		
		if(node == null)
		{
			Logger.getInstance().log("Request to send to unexisting node: " + to.toString(), 
					LOGGING_NAME,
					Logger.Level.WARNING);
		}
		
		if(!writeToNode(node.getSocket(),data))
		{
			nodes.remove(to);
		}
		// end-user-code
	}

	/**
	 * @param data
	 *            the data to be sent -- array of byte arrays
	 * @param socket
	 *            an established socket to the target
	 * @throws IOException
	 */
	private boolean writeToNode(Socket s, byte[] data) {

		try {
			OutputStream sos = s.getOutputStream();
			sos.write(data);
		} catch (IOException e) {
			
			Logger.getInstance().log("Failure to write to node: " + s.getRemoteSocketAddress(), 
					LOGGING_NAME,
					Logger.Level.WARNING);
			
			// remove node from pool
			nodes.remove(s.getRemoteSocketAddress());
			return false;
		}
		
		return true;
	}






}