/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb.communication;

import java.io.ByteArrayOutputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ddb.msg.Message;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author Administrator
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class UdpSenderImpl implements UdpSender {
	/** 
	 * /* (non-Javadoc)
	 *  * @see UdpSender#sendToAll(Object msg)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void sendToAll(Object msg) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private static final UdpSenderImpl instance = new UdpSenderImpl();

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private UdpSenderImpl() {
		// begin-user-code

		// end-user-code
	}

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	public static UdpSenderImpl getInstance() {
		// begin-user-code
		return instance;
		// end-user-code
	}

	public synchronized void sendToAll(Message msg) {
		String s = msg.toString();
		try {
			// TODO use Util.intToByteArray
			// TODO String.length() and byte[].length can differ!
			DatagramSocket socket = new DatagramSocket();
			ByteArrayOutputStream baos = new ByteArrayOutputStream(4);
			DataOutputStream das = new DataOutputStream(baos);
			das.writeInt(s.length());

			StringBuilder toSend = new StringBuilder();
			toSend.append(baos.toString());
			toSend.append(s);

			DatagramPacket packet = new DatagramPacket(toSend.toString()
					.getBytes(), toSend.length());
			packet.setAddress(InetAddress.getByName("255.255.255.255"));
			packet.setPort(UdpListener.LISTEN_PORT);
			socket.send(packet);
		} catch (SocketException ex) {
			Logger.getLogger(UdpSenderImpl.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IOException ex) {
			Logger.getLogger(UdpSenderImpl.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
}
