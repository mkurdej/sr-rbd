/**
 * 
 */
package ddb.communication;

import ddb.Logger;
import ddb.msg.Message;
import ddb.msg.MessageType;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class MessageFactory {
	
	private final static String LOGGING_NAME = "MessageFactory";
	
	/**
	 * Private constructor -- there are only static methods
	 */
	private MessageFactory() {}

	public static Message create(MessageType type) {
		switch(type)
		{
		case RESTORE_INCENTIVE: 	return new ddb.restore.msg.RestoreIncentive();
		case RESTORE_ACK:			return new ddb.restore.msg.RestoreAck();
		case RESTORE_NACK:			return new ddb.restore.msg.RestoreNack();
		case RESTORE_TABLELIST:		return new ddb.restore.msg.RestoreTableList();
		case RESTORE_TABLE:			return new ddb.restore.msg.RestoreTable();
		
		case TPC_ABORT:				return new ddb.tpc.msg.AbortMessage();
		case TPC_PRECOMMIT:			return new ddb.tpc.msg.PreCommitMessage();
		case TPC_ACKPRECOMMIT:		return new ddb.tpc.msg.AckPreCommitMessage();
		case TPC_CANCOMMIT:			return new ddb.tpc.msg.CanCommitMessage();
		case TPC_YESFORCOMMIT:		return new ddb.tpc.msg.YesForCommitMessage();
		case TPC_DOCOMMIT:			return new ddb.tpc.msg.DoCommitMessage();
		case TPC_HAVECOMMITED:		return new ddb.tpc.msg.HaveCommittedMessage();
		case TPC_NOFORCOMMIT:		return new ddb.tpc.msg.NoForCommitMessage();
		case TPC_ERROR:				return new ddb.tpc.msg.ErrorMessage();
		case TRANSACTION_MESSAGE:	return new ddb.tpc.msg.TransactionMessage();
		
		case CLIENT_SUCCESS:		return new ddb.msg.client.SuccessMessage();
		case CLIENT_CONFLICT:		return new ddb.msg.client.ConflictMessage();
		case CLIENT_ERROR:			return new ddb.msg.client.ErrorMessage();
		case CLIENT_RESULTSET:		return new ddb.msg.client.ResultsetMessage();
		case CLIENT_TIMEOUT:		return new ddb.msg.client.TimeoutMessage();
		
		case HELLO_MESSAGE:			return new ddb.msg.HelloMessage();
		}
		
		Logger.getInstance().log("Unknown message type", LOGGING_NAME, Logger.Level.SEVERE);
		return null;
	}
}
