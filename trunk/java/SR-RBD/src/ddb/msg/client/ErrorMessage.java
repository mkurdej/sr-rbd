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
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param exception
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setException(DBException exception) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	@Override
	protected void fromBinary(DataInputStream s) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected MessageType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void toBinary(DataOutputStream s) throws IOException {
		// TODO Auto-generated method stub
		
	}
}