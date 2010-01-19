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
public class SuccessMessage extends ClientResponse {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String databaseMessage = "SUCCESS";

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param message
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setDatabaseMessage(String message) {
		// begin-user-code
		databaseMessage = message;
		// end-user-code
	}
	
	public String getDatabaseMessage()
	{
		return databaseMessage;
	}

	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		databaseMessage = s.readString();
	}

	@Override
	public MessageType getType() {
		return MessageType.CLIENT_SUCCESS;
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		s.writeString(databaseMessage);
		
	}
}