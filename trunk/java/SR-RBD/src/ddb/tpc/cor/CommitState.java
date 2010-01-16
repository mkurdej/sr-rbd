/**
 * 
 */
package ddb.tpc.cor;

import java.net.SocketAddress;

import ddb.communication.TcpSender;
import ddb.msg.client.SuccessMessage;
import ddb.msg.client.TimeoutMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.ErrorMessage;
import ddb.tpc.msg.TransactionMessage;

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
	 *  * @see CoordinatorState#onYesForCommit(String node)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onYesForCommit(SocketAddress node) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CoordinatorState#onNoForCommit(String node)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onNoForCommit(SocketAddress node) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CoordinatorState#onAckPreCommit(String node)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onAckPreCommit(SocketAddress node) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
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

	/** 
	 * /* (non-Javadoc)
	 *  * @see CoordinatorState#onTransaction()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onTransaction(TransactionMessage message) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CoordinatorState#onErrorMessage(ErrorMessage message)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onErrorMessage(ErrorMessage message) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}
}