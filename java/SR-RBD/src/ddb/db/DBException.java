/**
 * 
 */
package ddb.db;

import java.io.IOException;
import java.sql.SQLException;

import ddb.BinarySerializable;
import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;

/** 
 * <!-- begin-UML-doc -->
 * Wyjatek rzucany, gdy wystapi blad w bazie danych.
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class DBException extends Exception implements BinarySerializable
{
	private static final long serialVersionUID = 1L;

	public DBException(SQLException e)
	{
		errorMessage = e.getMessage();
		errorCode = Integer.toString(e.getErrorCode());
	}
	public DBException(DataInputStream s) throws IOException {
		fromBinary(s);
	}

	private String errorMessage;
	private String errorCode;

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public String getErrorCode()
	{
		return errorCode;
	}
	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		errorMessage = s.readString();
		errorCode = s.readString();
		
	}
	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		s.writeString(errorMessage);
		s.writeString(errorCode);
	}
}