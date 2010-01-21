/**
 * 
 */
package ddb.tpc;

/** 
 * <!-- begin-UML-doc -->
 * Interfejs dla obiektow, ktore chca zostac poinformowane o koncu transakcji.
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface EndTransactionListener {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param participant
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onEndTransaction(TPCParticipant participant);
}