/**
 * 
 */
package ddb.communication;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ddb.Logger;
import ddb.msg.Message;
import ddb.util.Util;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author Administrator
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TcpSenderImpl implements TcpSender {

	private final static String LOGGING_NAME = "TcpSenderImpl";
	private Map<String, Socket> nodeSockets = new HashMap<String, Socket>();
	private List<String> nodes = new ArrayList<String>();

	/**
	 * /* (non-Javadoc) * @see TcpSender#sendToAllNodes(Message message)
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void sendToAllNodes(Message message) {
		// begin-user-code
		byte[][] data = getMessageAsByteArray(message);

		for (String to : this.nodes) {
			if (sendToNode(data, to)) {
				// TODO success
			} else {
				// TODO failure
				// remove node from the list?
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
	public void sendToNode(Message message, String to) {
		// begin-user-code
		byte[][] data = getMessageAsByteArray(message);

		if (sendToNode(data, to)) {
			// TODO success
		} else {
			// TODO failure
			// remove node from the list?
		}
		// end-user-code
	}

	/**
	 * @see TcpSender#sendToClient(Message message, String inetAddress, int
	 *      port)
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void sendToClient(Message message, String to, int port) {
		// begin-user-code
		byte[][] data = getMessageAsByteArray(message);

		Socket socket = null;
		try {
			socket = new Socket(to, port);
			Logger.getInstance().log(
					"Socket to " + to + ":" + port + " established.",
					LOGGING_NAME, Logger.Level.INFO);
			writeToSocket(data, socket);
			socket.close();
		} catch (UnknownHostException e) {
			Logger.getInstance().log("Unknown host '" + to + "'!",
					LOGGING_NAME, Logger.Level.WARNING);
			nodeSockets.remove(to);
			return;
		} catch (IOException e) {
			Logger.getInstance().log(
					"Error in connection to '" + to + ":" + port + "'!",
					LOGGING_NAME, Logger.Level.WARNING);
			nodeSockets.remove(to);
			return;
		}
		// end-user-code
	}

	private boolean sendToNode(byte[][] data, String to) {
		int port = TcpListener.LISTEN_PORT;
		Socket socket = null;

		try {
			socket = nodeSockets.get(to);
			nodeSockets.put(to, socket);
			if (socket == null) {
				socket = new Socket(to, port);
				Logger.getInstance().log(
						"Socket to " + to + ":" + port + " established.",
						LOGGING_NAME, Logger.Level.INFO);
			}
			writeToSocket(data, socket);
			// socket.close(); // don't close the socket -- it will be reused in
			// ongoing communication
		} catch (UnknownHostException e) {
			Logger.getInstance().log("Unknown host '" + to + "'!",
					LOGGING_NAME, Logger.Level.WARNING);
			nodeSockets.remove(to);
			return false;
		} catch (IOException e) {
			Logger.getInstance().log(
					"Error in connection to '" + to + ":" + port + "'!",
					LOGGING_NAME, Logger.Level.WARNING);
			nodeSockets.remove(to);
			return false;
		}
		return true;
	}

	/**
	 * @param data
	 *            the data to be sent -- array of byte arrays
	 * @param socket
	 *            an established socket to the target
	 * @throws IOException
	 */
	private void writeToSocket(byte[][] data, Socket socket) throws IOException {
		OutputStream sos = socket.getOutputStream();
		for (int i = 0; i < data.length; ++i) {
			sos.write(data[i]);
		}
	}

	private byte[][] getMessageAsByteArray(Message message) {
		String msg = message.toString();

		byte[] toSend = msg.getBytes();
		byte[] toSendLength = Util.intToByteArray(toSend.length);
		byte[][] data = new byte[][] { toSendLength, toSend };
		return data;
	}

	/**
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private static final TcpSenderImpl instance = new TcpSenderImpl();

	/**
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private TcpSenderImpl() {
		// begin-user-code
		// TODO: Implement constructor logic
		// end-user-code
	}

	/**
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	public static TcpSenderImpl getInstance() {
		// begin-user-code
		return instance;
		// end-user-code
	}

}