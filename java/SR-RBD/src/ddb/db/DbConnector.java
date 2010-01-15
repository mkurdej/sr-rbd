/**
 * 
 */
package ddb.db;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface DbConnector {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param queryString
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public DatabaseTable query(String queryString) throws DBException;
    public void clearDatabase() throws DBException;
    public String dumpTable(String tableName) throws DumpTableException;
    public void importTable(String dump) throws ImportTableException;
}