/**
 * 
 */
package ddb.communication;

import static ddb.communication.TcpWorker.*;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TcpListener {
	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private static final TcpListener instance = new TcpListener();

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private TcpListener() {
		// begin-user-code
		// TODO: Implement constructor logic
		// end-user-code
	}

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	public static TcpListener getInstance() {
		// begin-user-code

		return instance;
		// end-user-code
	}
}