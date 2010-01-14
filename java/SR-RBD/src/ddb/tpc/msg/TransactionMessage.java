/**
 * 
 */
package ddb.tpc.msg;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.Message;
import ddb.msg.MessageType;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TransactionMessage extends Message {
	/** 
	 * <!-- begin-UML-doc -->
	 * Zapytanie wyslane przez klienta.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String queryString;

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		queryString = s.readString();
	}

	@Override
	public MessageType getType() {
		return MessageType.TRANSACTION_MESSAGE;
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		s.WriteString(queryString);
	}
}