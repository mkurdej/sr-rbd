/**
 * 
 */
package ddb.tpc.coh;

import ddb.tpc.msg.AckPreCommitMessage;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class PreparedState extends CohortState {
	/** 
	 * /* (non-Javadoc)
	 *  * @see TimeoutListener#onTimeout()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onTimeout() {
		this.cohort.endTransaction();
		this.cohort.setState(new AbortState());
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CohordState#onPreCommit()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onPreCommit() {
		this.cohort.replyToCoordinator(new AckPreCommitMessage());
		this.cohort.changeState(new WaitingDoCommit());
	}
}