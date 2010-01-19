/**
 * 
 */
package ddb.communication;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

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
	private Map<InetSocketAddress, NodeInfo> nodes = new HashMap<InetSocketAddress, NodeInfo>();
	protected BlockingQueue<Message> queue;
	
	
	/**
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private static TcpSender instance = null;

	/**
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	protected TcpSender() {
		
	}

	/**
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	public static TcpSender getInstance() {
		// begin-user-code
		if(instance == null)
			instance = new TcpSender();
		
		return instance;
		// end-user-code
	}
	
	public void setQueue(BlockingQueue<Message> q)
	{
		queue = q;
	}
	
	public synchronized void removeNode(InetSocketAddress address) {
		if(nodes.remove(address) == null)
		{
			Logger.getInstance().log("Request to remove unexisting node: " + address.toString(), 
					LOGGING_NAME,
					Logger.Level.WARNING);
		}
		
		Logger.getInstance().log("Node disconnected legally " + address.toString(), 
				LOGGING_NAME,
				Logger.Level.INFO);
	}

	public synchronized void addNodeBySocket(InetSocketAddress node, Socket s) 
	{
		if(nodes.get(node) != null)
		{
			Logger.getInstance().log("Request to add already existing node: " + node.toString(), 
					LOGGING_NAME,
					Logger.Level.WARNING);
		}
		
		nodes.put(node, new NodeInfo(s, false));
	}
	
	public synchronized void AddServerNode(InetSocketAddress node, BlockingQueue<Message> storage)
	{
		NodeInfo nodeInfo = nodes.get(node);
		
		if(nodeInfo == null)
		{
			// connect to node
			try {
				Socket socket = new Socket(node.getAddress(), node.getPort());
				
				TcpWorker worker = new TcpWorker(socket, storage);
                new Thread(worker, 
            		"TcpWorker " + socket.getInetAddress() + ":" + socket.getPort()
        		).start();
                
                nodes.put(node, new NodeInfo(socket, true));
				
			} catch (IOException e) {
				Logger.getInstance().log("Failed to add server node: " + node.toString(), 
						LOGGING_NAME,
						Logger.Level.WARNING);
				return;
			}
			
			
		}
		else
		{
			// mark node as server
			nodeInfo.setIsServer(true);
		}
	}
	
	public synchronized int getServerNodesCount()
	{
		int count = 0;
		
		for(NodeInfo node : nodes.values())
			if(node.getIsServer())
				++count;
		
		// add self
		return count + 1;
	}
	
	/**
	 * /* (non-Javadoc) * @see TcpSender#sendToAllNodes(Message message)
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public synchronized void sendToAllServerNodes(Message message) {
		// begin-user-code
		byte[] data; 
		
		// serialize
		data = message.Serialize();
		
		// search for server nodes
		Iterator<Map.Entry<InetSocketAddress, NodeInfo>> it = nodes.entrySet().iterator();

		while(it.hasNext())
		{
			Map.Entry<InetSocketAddress, NodeInfo> entry = it.next();
			NodeInfo node = entry.getValue();
			
			if(node.getIsServer())
			{
				if(!writeToNode(entry.getKey(), node.getSocket(), data))
				{
					it.remove();
				}
			}
		}
		
		// write to self
		try {
			queue.put(message);
		} catch (InterruptedException e) {
			Logger.getInstance().log("InterruptedException Cannot send to self" , 
					LOGGING_NAME,
					Logger.Level.SEVERE);
		}
		// end-user-code
	}

	/**
	 * /* (non-Javadoc) * @see TcpSender#sendToNode(Message message, String to)
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public synchronized void sendToNode(Message message, InetSocketAddress to) {
		// begin-user-code
		byte[] data; 
		
		if(to == null)
		{
			// write to self
			try {
				queue.put(message);
			} catch (InterruptedException e) {
				Logger.getInstance().log("InterruptedException Cannot send to self" , 
						LOGGING_NAME,
						Logger.Level.SEVERE);
			}
			
			return;
		}
		
		
		// serialize
		data = message.Serialize();
		NodeInfo node = nodes.get(to);
		
		if(node == null)
		{
			Logger.getInstance().log("Request to send to unexisting node: " + to.toString(), 
					LOGGING_NAME,
					Logger.Level.WARNING);
		}
		
		if(!writeToNode(to, node.getSocket(),data))
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
	private boolean writeToNode(InetSocketAddress node, Socket s, byte[] data) {

		try {
			OutputStream sos = s.getOutputStream();
			sos.write(data);
		} catch (IOException e) {
			
			Logger.getInstance().log("Failure to write to node: " + s.getRemoteSocketAddress(), 
					LOGGING_NAME,
					Logger.Level.WARNING);
			
			// remove node from pool
			nodes.remove(node);
			return false;
		}
		
		return true;
	}






}