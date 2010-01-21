/**
 * 
 */
package ddb.msg.client;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.MessageType;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TimeoutMessage extends ClientResponse {

	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		// empty
		
	}

	@Override
	public MessageType getType() {
		return MessageType.CLIENT_TIMEOUT;
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		// empty
	}
}