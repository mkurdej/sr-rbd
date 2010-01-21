/**
 * 
 */
package ddb.msg.client;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.db.DatabaseTable;
import ddb.msg.MessageType;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ResultsetMessage extends SuccessMessage {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String result;

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param resultSet
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setResultSet(DatabaseTable table) {
		this.setResult(table.toString());
	}
	
	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}
	
	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		setResult(s.readString());
	}

	@Override
	public MessageType getType() {
		return MessageType.CLIENT_RESULTSET;
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		s.writeString(getResult());
	}
}