/**
 * 
 */
package ddb.restore;

import ddb.Logger;
import ddb.msg.Message;
import ddb.tpc.cor.CoordinatorState;
import ddb.tpc.msg.AckPreCommitMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.NoForCommitMessage;
import ddb.tpc.msg.TransactionMessage;
import ddb.tpc.msg.YesForCommitMessage;


/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class RestoreCoordinatorImpl implements RestoreCoordinator 
{
	private RestoreCoordinatorState state = new RestoreCoordinatorInitialState();
	String targetNode;

	public RestoreCoordinatorImpl(string node)
	{
		targetNode = node;
	}
	
	public void RestoreNode()
	{
		// TODO: finish
		// TcpSender.getInstance().sendToNode(new RestoreIncentive(), targetNode);
	}
	
	synchronized public void processMessage(Message message) {
		/*
		 * try { this.messageQueue.putMessage(message); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * Logger.getInstance().log(e.getMessage(), "TPC", Level.SEVERE); }
		 */
		this.onNewMessage(message);
	}
	
	protected void onNewMessage(Message message) {
		Logger.getInstance().log("NewMessage: " + message, "RestoreCoordinatorImpl", Logger.Level.INFO);
		
		if(message instanceof RestoreAck) {
			state.onYesForCommit(message.getSenderAddress());
		}
		else if(message instanceof RestoreNack) {
			state.onNoForCommit(message.getSenderAddress());
		}
	}
}