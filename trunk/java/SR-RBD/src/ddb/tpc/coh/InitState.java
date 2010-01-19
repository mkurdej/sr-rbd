/**
 * 
 */
package ddb.tpc.coh;

import ddb.Logger;
import ddb.Logger.Level;
import ddb.tpc.LessTableVersionException;
import ddb.tpc.msg.CanCommitMessage;
import ddb.tpc.msg.NoForCommitMessage;
import ddb.tpc.msg.YesForCommitMessage;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class InitState extends CohortState {
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
	 *  * @see CohordState#onCanCommit()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onCanCommit(CanCommitMessage message) {
		
		try {
			this.cohort.setTableName(message.getTableName());
			this.cohort.setQueryString(message.getQueryString());
			this.cohort.setCreate(message.isCreate());
			int coordinatorTableVersion = message.getTableVersion();
			String tableName = message.getTableName();
			int localTableVersion = this.cohort.getDatabaseState().getTableVersion(tableName);
			if(coordinatorTableVersion < localTableVersion) {
				throw new LessTableVersionException(tableName, coordinatorTableVersion, localTableVersion);
			}
			this.cohort.getDatabaseState().lockTable(message.getTableName());
			this.cohort.replyToCoordinator(new YesForCommitMessage());
			this.cohort.changeState(new PreparedState());
			// TODO: debugging is this really needed?
			//if(cohort.isCreate()) {
			//	this.cohort.getDatabaseState().addTable(cohort.getTableName(), cohort.getQueryString());
			//}
		} catch (Exception e) {
			Logger.getInstance().log(e.toString(), "KOHORT", Level.WARNING);
			this.cohort.replyToCoordinator(new NoForCommitMessage());
			this.cohort.endTransaction();
			this.cohort.setState(new AbortState());
		}
		
	}
}