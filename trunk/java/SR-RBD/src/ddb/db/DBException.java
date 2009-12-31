/**
 * 
 */
package ddb.db;

import java.sql.SQLException;

/** 
 * <!-- begin-UML-doc -->
 * Wyjatek rzucany, gdy wystapi blad w bazie danych.
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class DBException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DBException(SQLException e)
	{
		errorMessage = e.getMessage();
		errorCode = Integer.toString(e.getErrorCode());
	}
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String errorMessage;
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String errorCode;

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getErrorCode()
	{
		return errorCode;
	}
}