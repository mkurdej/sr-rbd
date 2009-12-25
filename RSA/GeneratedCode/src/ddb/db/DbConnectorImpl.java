/**
 * 
 */
package ddb.db;

import static ddb.db.DbParser.*;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class DbConnectorImpl implements DbConnector {
	/** 
	 * /* (non-Javadoc)
	 *  * @see DbConnector#query(String queryString)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String query(String queryString) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see DbConnector#isLockPresent(String lockQuery)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isLockPresent(String lockQuery) {
		// begin-user-code
		// TODO Auto-generated method stub
		return false;
		// end-user-code
	}

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private static final DbConnectorImpl instance = new DbConnectorImpl();

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private DbConnectorImpl() {
		// begin-user-code
		// TODO: Implement constructor logic
		// end-user-code
	}

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	public static DbConnectorImpl getInstance() {
		// begin-user-code

		return instance;
		// end-user-code
	}
}