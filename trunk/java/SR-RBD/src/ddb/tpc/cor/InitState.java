/**
 * 
 */
package ddb.tpc.cor;

import ddb.Logger;
import ddb.communication.TcpSender;
import ddb.db.SqlOperationType;
import ddb.db.SqlParser;
import ddb.db.SqlParserImpl;
import ddb.msg.client.ErrorMessage;
import ddb.msg.client.TimeoutMessage;
import ddb.tpc.msg.CanCommitMessage;
import ddb.tpc.msg.TransactionMessage;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class InitState extends CoordinatorState {
	
	private final static String LOGGING_NAME = "Coordinator.InitState";
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
	 *  * @see CoordinatorState#onTransaction()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void onTransaction(TransactionMessage message) {
	
		Logger.getInstance().log("Processing transaction", LOGGING_NAME, Logger.Level.INFO);
		
		coordinator.setNodeCount( TcpSender.getInstance().getServerNodesCount() );
		SqlParser parser = new SqlParserImpl();
		
		if(!parser.parse(message.getQueryString()))
		{
			Logger.getInstance().log("Unparsable query - send back error and end transaction", LOGGING_NAME, Logger.Level.INFO);
			
			TcpSender.getInstance().sendToNode(new ErrorMessage("Bad query"), message.getSender());
			coordinator.endTransaction();
			return;
		}
		
		coordinator.setQueryString( parser.getQueryString() );
		coordinator.setTableName( parser.getTableName() );
		
		
		if(parser.getOperationType().equals(SqlOperationType.SELECT))
		{
			Logger.getInstance().log("Select query detected", LOGGING_NAME, Logger.Level.INFO);
			coordinator.processSelect();
		}
		else 
		{
			CanCommitMessage msg = new CanCommitMessage();
			msg.setTableName(parser.getTableName());
			msg.setQueryString(parser.getQueryString());
			msg.setTableVersion(coordinator.getDatabaseState().getTableVersion(msg.getTableName()));
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
}