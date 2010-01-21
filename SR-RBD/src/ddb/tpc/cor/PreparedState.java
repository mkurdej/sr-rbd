/**
 * 
 */
package ddb.tpc.cor;

import java.net.InetSocketAddress;

import ddb.Logger;
import ddb.msg.client.TimeoutMessage;
import ddb.tpc.msg.DoCommitMessage;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class PreparedState extends CoordinatorState {
	
	private final static String LOGGING_NAME = "Coordinator.PreparedState";
	
	/** 
	 * /* (non-Javadoc)
	 *  * @see TimeoutListener#onTimeout()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void onTimeout() {
		Logger.getInstance().log("Got no - aborting!", LOGGING_NAME, Logger.Level.INFO);
		coordinator.abortTransaction(new TimeoutMessage());
	}


	/** 
	 * /* (non-Javadoc)
	 *  * @see CoordinatorState#onAckPreCommit(String node)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void onAckPreCommit(InetSocketAddress node) {
		coordinator.processAnswer(node, new DoCommitMessage(), new CommitState());
		Logger.getInstance().log("Got answer " 
				+ coordinator.getAnswerCount() + " of " 
				+ coordinator.getNodesCount(), LOGGING_NAME, 
				Logger.Level.INFO);
	}
}