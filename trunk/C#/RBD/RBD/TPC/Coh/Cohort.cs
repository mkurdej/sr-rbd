
using System;
using System.Net;
using System.Net.Sockets;
using RBD.TPC.Msg;

namespace RBD.TPC.COH
{

public abstract class Cohort : TPCParticipant {
	private bool _isCreate;

	public bool isCreate() {
        return _isCreate;
	}

	public void setCreate(bool isCreate) {
        this._isCreate = isCreate;
	}
	/**
	 * <!-- begin-UML-doc --> Adres koordynator transakcji, w ktorej uczestniczy
	 * ten kohort. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    protected IPEndPoint coordinatorAddress;

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
    public void setCoordinatorAddress(IPEndPoint address)
    {
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
    public IPEndPoint getCoordinatorAddress()
    {
		return this.coordinatorAddress;
	}

	public CohortState getState() {
		return state;
	}

	public void setState(CohortState state) {
		this.state = state;
		state.setCohort(this);
	}
	
	abstract public void changeState(CohortState cohstate);
	abstract public void replyToCoordinator(TPCMessage message);
	abstract public void commitTransaction();
}

}