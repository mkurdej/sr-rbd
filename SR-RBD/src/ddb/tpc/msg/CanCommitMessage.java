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
public class CanCommitMessage extends TPCMessage {
	/** 
	 * <!-- begin-UML-doc -->
	 * Zapytanie klienta przerobione przez parser sql.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String queryString;
	/** 
	 * <!-- begin-UML-doc -->
	 * Nazwa tabeli jakiej dotyczy zapytanie.
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String tableName;
	/**
	 * Czy transakcja dotyczy utworzenia nowej tabeli
	 */
	private boolean isCreate;
	/**
	 * Numer wersji tabeli na koordynatorze
	 */
	private int tableVersion;
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getTableName() {
		return this.tableName;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param tableName
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param queryString
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getQueryString() {
		return  queryString;
	}
	
	public boolean isCreate() {
		return isCreate;
	}

	public void setCreate(boolean isCreate) {
		this.isCreate = isCreate;
	}

	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		super.fromBinary(s);
		queryString = s.readString();
		tableName = s.readString();
		isCreate = s.readBoolean();
		tableVersion = s.readInt();
	}

	@Override
	public MessageType getType() {
		return MessageType.TPC_CANCOMMIT;
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		super.toBinary(s);
		s.writeString(queryString);
		s.writeString(tableName);
		s.writeBoolean(isCreate);
		s.writeInt(tableVersion);
	}

	public void setTableVersion(int tableVersion) {
		this.tableVersion = tableVersion;
		
	}
	
	public int getTableVersion() {
		return this.tableVersion;
	}
}