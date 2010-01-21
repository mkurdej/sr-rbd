/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import ddb.Config;
import ddb.Logger;
import ddb.msg.Message;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author Administrator
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class UdpSenderImpl implements UdpSender {

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private final static String LOGGING_NAME = "UdpSenderImpl";
	private static UdpSenderImpl instance = new UdpSenderImpl();
	protected DatagramSocket socket;

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private UdpSenderImpl() {
		// begin-user-code
		try {
			socket = new DatagramSocket();
		} catch (SocketException ex) {
			Logger.getInstance().log("SocketException in constructor: " + ex.getMessage(), 
					LOGGING_NAME, 
					Logger.Level.SEVERE);
		}
		// end-user-code
	}

	public static UdpSenderImpl getInstance() {
		return instance;
	}

	public synchronized void sendToAll(Message msg) {
		
		
		try {
			byte[] data = msg.Serialize();

			DatagramPacket packet = new DatagramPacket(data, data.length);
			packet.setAddress(InetAddress.getByName("255.255.255.255"));
			packet.setPort(Config.UdpPort());
			socket.send(packet);
			
		} catch (IOException ex) {
			Logger.getInstance().log("IOException: " + ex.getMessage(), LOGGING_NAME, Logger.Level.WARNING);
		}
	}
}
