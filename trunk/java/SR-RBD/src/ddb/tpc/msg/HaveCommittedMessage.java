/**
 * 
 */
package ddb.tpc.msg;

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
public class HaveCommittedMessage extends TPCMessage {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String databaseMessage; //TODO: kolejna zmienna ktora nie jest nigdzie ustawiana

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getDatabaseMessage() {
		// begin-user-code
		return databaseMessage;
		// end-user-code
	}

	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		databaseMessage = s.readString();
		
	}

	@Override
	public MessageType getType() {
		return MessageType.TPC_HAVECOMMITED;
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		s.writeString(databaseMessage);
	}
}