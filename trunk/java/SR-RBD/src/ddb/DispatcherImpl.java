/**
 * 
 */
package ddb;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ddb.communication.HelloGenerator;
import ddb.communication.TcpListener;
import ddb.communication.UdpListener;
import ddb.msg.HelloMessage;
import ddb.msg.Message;
import ddb.restore.RestoreCohort;
import ddb.restore.RestoreMessage;
import ddb.tpc.TPCParticipant;
import ddb.tpc.coh.Cohort;
import ddb.tpc.coh.CohortImpl;
import ddb.tpc.cor.Coordinator;
import ddb.tpc.cor.CoordinatorImpl;
import ddb.tpc.msg.AbortMessage;
import ddb.tpc.msg.AckPreCommitMessage;
import ddb.tpc.msg.CanCommitMessage;
import ddb.tpc.msg.DoCommitMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.NoForCommitMessage;
import ddb.tpc.msg.PreCommitMessage;
import ddb.tpc.msg.TPCMessage;
import ddb.tpc.msg.TransactionMessage;
import ddb.tpc.msg.YesForCommitMessage;
import ddb.util.Util;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author User
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class DispatcherImpl implements Dispatcher {

	// destination for messages
	BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
	
	// communication
	TcpListener tcp = new TcpListener(queue);
	UdpListener udp = new UdpListener(queue);
	HelloGenerator hello = new HelloGenerator();
	
	// workers
	private Map<String, Cohort> cohorts = new HashMap<String, Cohort>();
	private Map<String, Coordinator> coordinators = new HashMap<String, Coordinator>();
	private Map<String, RestoreCohort> restoreCohorts = new HashMap<String, RestoreCohort>();
	private Map<String, String> coordinatorAddresses = new HashMap<String, String>();
	
	public DispatcherImpl()
	{
		// empty
	}
	
	public void Initialize()
	{
		// install threads for managing communication
		new Thread(tcp).start();
        new Thread(udp).start();
        new Thread(hello).start();
	}
	
	public void Run() throws InterruptedException
	{
		Initialize();
		
		// main loop
		while(true)
		{
			Message m = queue.take();
			process(m);
		}
	}
	
	// IMPROVEMENT: should be visitor pattern
	public void process(Message msg) 
	{
		if (msg instanceof TPCMessage) 
		{
			processTpcMessage((TPCMessage)msg);
		}
		else if (msg instanceof RestoreMessage)
		{
			processRestoreMessage((RestoreMessage)msg);
		}
		else if (msg instanceof TransactionMessage)
		{
			processTransactionMessage((TransactionMessage)msg);
		}
		else if (msg instanceof HelloMessage)
		{
			processHelloMessage((HelloMessage)msg);
		}
	}
	
	public void processTpcMessage(TPCMessage msg)
	{
		String transactionId = ((TPCMessage) msg).getTransactionId();
		String coorAddr = coordinatorAddresses.get(transactionId);
		
		if (msg instanceof CanCommitMessage) 
		{
			// CREATE COHORT
			// TODO change CohortImpl to extend Cohort (write to interface,
			// not to implementation!)
			Cohort coh = new CohortImpl();
			cohorts.put(transactionId, coh);
			coh.setTransactionId(transactionId);
			coh.setCoordinatorAddress(coorAddr);
			coh.addEndTransactionListener(this);
			coh.processMessage(msg);
		} 
		else if (msg instanceof PreCommitMessage
				|| msg instanceof DoCommitMessage
				|| msg instanceof AbortMessage) 
		{
			Cohort coh = cohorts.get(transactionId);
			coh.processMessage(msg);
		} 
		else if (msg instanceof YesForCommitMessage
				|| msg instanceof NoForCommitMessage
				|| msg instanceof AckPreCommitMessage
				|| msg instanceof HaveCommittedMessage) 
		{
			// send to COORDINATOR
			Coordinator coor = coordinators.get(transactionId);
			coor.processMessage(msg);
		}
	}
	
	public void  processRestoreMessage(RestoreMessage msg)
	{
		// TODO: finish
	}
	
	public void  processTransactionMessage(TransactionMessage msg)
	{
		// create COORDINATOR
		String transactionId = Util.generateGUID();
		Coordinator coor = new CoordinatorImpl();
		coordinators.put(transactionId, coor);
		coor.setTransactionId(transactionId);
		coor.setClientAddress(msg.getSenderAddress());
		coor.setClientPort(msg.getSenderPort());
		coor.addEndTransactionListener(this);
		coor.processMessage(msg);
	}
	
	public void  processHelloMessage(HelloMessage msg)
	{
		// TODO: sprawdzanie czy wezel istnieje - nie istnieje dodanie
		
		// TODO: zliczanie jak dlugo wezel jest out of sync
		
		// TODO: jezeli nie byl aktywny przez dlugi okres czasu - stworzernie
		// Restore Coordinator
	}
	
	
	public void onEndTransaction(TPCParticipant participant) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}



}
