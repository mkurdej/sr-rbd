/**
 * 
 */
package ddb.tpc.coh;

import ddb.Logger;
import ddb.Logger.Level;
import ddb.tpc.TimeoutListener;
import ddb.tpc.msg.CanCommitMessage;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
abstract public class CohortState implements TimeoutListener {
	/**
	 * 
	 */
	protected Cohort cohort;
	/**
	 * Nazwa logera
	 */
	private static final String LOGGER_NAME = "KOHORT";
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onPreCommit() {
		Logger.getInstance().log("Nie oczekiwano wiadomosci: PreCommit", LOGGER_NAME, Level.WARNING);
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onCanCommit(CanCommitMessage message) {
		Logger.getInstance().log("Nie oczekiwano wiadomosci: CanCommit", LOGGER_NAME, Level.WARNING);
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onAbort() {
		Logger.getInstance().log("Nie oczekiwano wiadomosci: Abort", LOGGER_NAME, Level.WARNING);
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onDoCommit() {
		Logger.getInstance().log("Nie oczekiwano wiadomosci: DoCommit", LOGGER_NAME, Level.WARNING);
	}

	public void onTimeout() {
		// empty
	}
	
	public Cohort getCohort() {
		return cohort;
	}

	public void setCohort(Cohort cohort) {
		this.cohort = cohort;
	}
}