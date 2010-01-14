/**
 * 
 */
package ddb.msg;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;

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
	public abstract void toBinary(DataOutputStream s) throws IOException;
	
	/**
	 * Object constructs itself from binary data
	 * @param s data storage
	 */
	public abstract void fromBinary(DataInputStream s) throws IOException;
}