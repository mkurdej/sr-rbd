/**
 * 
 */
package ddb.communication;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class UdpSenderImpl implements UdpSender {
	/** 
	 * /* (non-Javadoc)
	 *  * @see UdpSender#sendToAll(Object msg)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void sendToAll(Object msg) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private static final UdpSenderImpl instance = new UdpSenderImpl();

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private UdpSenderImpl() {
		// begin-user-code
		// TODO: Implement constructor logic
		// end-user-code
	}

	/** 
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	public static UdpSenderImpl getInstance() {
		// begin-user-code

		return instance;
		// end-user-code
	}
}