/**
 * 
 */
package ddb.tpc.cor;

import ddb.communication.TcpSender;
import ddb.msg.client.SuccessMessage;
import ddb.msg.client.TimeoutMessage;
import ddb.tpc.msg.HaveCommittedMessage;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class CommitState extends CoordinatorState {
	/** 
	 * /* (non-Javadoc)
	 *  * @see TimeoutListener#onTimeout()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onTimeout() {
		coordinator.abortTransaction(new TimeoutMessage());
	}
	/** 
	 * /* (non-Javadoc)
	 *  * @see CoordinatorState#onHaveCommitted(HaveCommittedMessage message)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onHaveCommitted(HaveCommittedMessage message) {
		TcpSender.getInstance().sendToNode(new SuccessMessage(), coordinator.getClientAddress());
		coordinator.endTransaction();
	}
}