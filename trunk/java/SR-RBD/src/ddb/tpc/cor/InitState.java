/**
 * 
 */
package ddb.tpc.cor;

import ddb.db.SqlOperationType;
import ddb.db.SqlParser;
import ddb.db.SqlParserImpl;
import ddb.msg.client.TimeoutMessage;
import ddb.tpc.msg.CanCommitMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.ErrorMessage;
import ddb.tpc.msg.TransactionMessage;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class InitState extends CoordinatorState {
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
	public void onYesForCommit(String node) {
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
	public void onNoForCommit(String node) {
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
	public void onAckPreCommit(String node) {
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
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see CoordinatorState#onTransaction()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onTransaction(TransactionMessage message) {
		coordinator.setNodeList( coordinator.getDatabaseState().getNodes() );
		SqlParser parser = new SqlParserImpl();
		parser.parse(message.getQueryString());
		coordinator.setQueryString( parser.getQueryString() );
		coordinator.setTableName( parser.getTableName() );
		if(parser.getOperationType().equals(SqlOperationType.SELECT)) {
			coordinator.processSelect();
		}
		else {
			CanCommitMessage msg = new CanCommitMessage();
			msg.setTableName(parser.getTableName());
			msg.setQueryString(parser.getQueryString());
			if(parser.getOperationType().equals(SqlOperationType.CREATE)) {
				msg.setCreate(true);
			}
			else {
				msg.setCreate(false);
			}
			coordinator.broadcastMessage(msg);
			coordinator.changeState(new WaitingState());
		}
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