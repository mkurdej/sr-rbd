/**
 * 
 */
package ddb.msg.client;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.db.DBException;
import ddb.msg.MessageType;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ErrorMessage extends ClientResponse {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private DBException exception;

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public DBException getException() {
		// begin-user-code
		return exception;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param exception
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setException(DBException e) {
		// begin-user-code
		exception = e;
		// end-user-code
	}

	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		exception = new DBException(s);
		
	}

	@Override
	public MessageType getType() {
		return MessageType.CLIENT_ERROR;
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		exception.toBinary(s);
	}
}