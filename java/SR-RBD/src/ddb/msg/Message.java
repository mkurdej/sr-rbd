/**
 * 
 */
package ddb.msg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.communication.MessageFactory;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public abstract class Message {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String senderAddress;
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int senderPort;

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getSenderAddress() {
		return this.senderAddress;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getSenderPort() {
		return this.senderPort;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public void setSenderPort(int senderPort) {
		this.senderPort = senderPort;
	}
	
	/**
	 * 
	 * @return type constant for specialized class
	 */
	public abstract MessageType getType();
	
	/**
	 * Converts object into binary representation
	 * @param s data storage
	 */
	protected abstract void toBinary(DataOutputStream s) throws IOException;
	
	/**
	 * Object constructs itself from binary data
	 * @param s data storage
	 */
	protected abstract void fromBinary(DataInputStream s) throws IOException;
	
	final public byte[] Serialize() throws IOException
	{
		// serialize data
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(data);
		toBinary(dataStream);
		
		// wrap data in envelope ( byte[][] would be more efficient, but this is more convinient )
		// btw where is writev() in case of sending this through udp in java?!
		ByteArrayOutputStream envelope = new ByteArrayOutputStream();
		DataOutputStream envelopeStream = new DataOutputStream(envelope);
		
		// size + type + data
		envelopeStream.write(data.size());
		envelopeStream.write(getType().ordinal());
		envelopeStream.write(data.toByteArray());
		
		return envelope.toByteArray();
	}
	
	static public Message Unserialize(MessageType type, byte[] bytes) throws IOException
	{
		Message result = MessageFactory.create(type);
		
		ByteArrayInputStream data = new ByteArrayInputStream(bytes);
		DataInputStream dataStream = new DataInputStream(data);
		result.fromBinary(dataStream);
		
		return result;
	}
	
}