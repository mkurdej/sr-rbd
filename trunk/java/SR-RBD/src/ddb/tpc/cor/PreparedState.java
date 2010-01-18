/**
 * 
 */
package ddb.tpc.cor;

import java.net.InetAddress;

import ddb.msg.client.TimeoutMessage;
import ddb.tpc.msg.DoCommitMessage;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class PreparedState extends CoordinatorState {
	/** 
	 * /* (non-Javadoc)
	 *  * @see TimeoutListener#onTimeout()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void onTimeout() {
		coordinator.abortTransaction(new TimeoutMessage());
	}


	/** 
	 * /* (non-Javadoc)
	 *  * @see CoordinatorState#onAckPreCommit(String node)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void onAckPreCommit(InetAddress node) {
		coordinator.processAnswer(node, new DoCommitMessage(), new CommitState());
	}
}