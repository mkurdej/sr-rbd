/**
 * 
 */
package ddb.msg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import ddb.BinarySerializable;
import ddb.Logger;
import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.communication.MessageFactory;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public abstract class Message implements BinarySerializable {
	
	private final static String LOGGING_NAME = "Message";
	
	private InetSocketAddress sender;
	
	public void setSender(InetSocketAddress sender) {
		this.sender = sender;
	}

	public InetSocketAddress getSender() {
		return sender;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("[");
		builder.append(getType());
		builder.append(" from ");
		if(sender != null)
			builder.append(sender.toString());
		else
			builder.append("self");
		builder.append("]");
		
		return builder.toString();
	}
	
	/**
	 * 
	 * @return type constant for specialized class
	 */
	public abstract MessageType getType();
	
	final public byte[] Serialize()
	{
		try {
			// serialize data
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			DataOutputStream dataStream = new DataOutputStream(data);
			toBinary(dataStream);
			
			// wrap data in envelope ( byte[][] would be more efficient, but this is more convinient )
			// btw where is writev() in case of sending this through udp in java?!
			ByteArrayOutputStream envelope = new ByteArrayOutputStream();
			DataOutputStream envelopeStream = new DataOutputStream(envelope);
			
			byte[] bytes = data.toByteArray();
			
			// size + type + data
			envelopeStream.writeInt(bytes.length);
			envelopeStream.writeInt(getType().ordinal());
			envelopeStream.write(bytes);
			
			return envelope.toByteArray();
			
		} catch (IOException ex) {
			// should never happen due to stream is wrapped around ByteArrayOutputStream 
			Logger.getInstance().log("Serialization failure: " + ex.getMessage(), 
					LOGGING_NAME,
					Logger.Level.SEVERE);
			
			return null;
		}
	}
	
	static public Message Unserialize(MessageType type, byte[] bytes, InetSocketAddress sender) throws IOException
	{
		// create message object of given type
		Message result = MessageFactory.create(type);
		
		// unserialize it
		ByteArrayInputStream data = new ByteArrayInputStream(bytes);
		DataInputStream dataStream = new DataInputStream(data);
		result.fromBinary(dataStream);
		
		// mark sender
		result.setSender(sender);
		
		return result;
	}
}