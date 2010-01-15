/**
 * 
 */
package ddb.tpc.coh;

import ddb.tpc.TPCParticipant;
import ddb.tpc.msg.TPCMessage;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author User
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public abstract class Cohort extends TPCParticipant {
	private boolean isCreate;
	
	public boolean isCreate() {
		return isCreate;
	}

	public void setCreate(boolean isCreate) {
		this.isCreate = isCreate;
	}

	
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Cohort() {
		// begin-user-code
		// TODO Auto-generated constructor stub
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> Adres koordynator transakcji, w ktorej uczestniczy
	 * ten kohort. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected String coordinatorAddress;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected CohortState state;

	/**
	 * <!-- begin-UML-doc --> Ustawia
	 * adres&nbsp;koordynator&nbsp;transakcji,&nbsp
	 * ;w&nbsp;ktorej&nbsp;uczestniczy&nbsp;ten&nbsp;kohort <!-- end-UML-doc -->
	 * 
	 * @param address
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setCoordinatorAddress(String address) {
		this.coordinatorAddress = address;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * Pobiera&nbsp;adres&nbsp;koordynator&nbsp;transakcji
	 * ,&nbsp;w&nbsp;ktorej&nbsp;uczestniczy&nbsp;ten&nbsp;kohort <!--
	 * end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getCoordinatorAddress() {
		return this.coordinatorAddress;
	}

	public CohortState getState() {
		return state;
	}

	protected void setState(CohortState state) {
		this.state = state;
		state.setCohort(this);
	}
	
	abstract public void changeState(CohortState cohstate);
	abstract public void replyToCoordinator(TPCMessage message);
	abstract public void commitTransaction();
}