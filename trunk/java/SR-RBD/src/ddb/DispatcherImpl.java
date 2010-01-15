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
import ddb.restore.RestoreCoordinator;
import ddb.restore.msg.RestoreIncentive;
import ddb.restore.msg.RestoreMessage;
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

	private final static String LOGGING_NAME = "DispatcherImpl";
	private final static int OUT_OF_SYNC_BEATS_THRESHOLD = 4;
	private final static int OUT_OF_SYNC_RESET_TIME_MS = 5000;
	
	// destination for messages
	protected BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
	
	// communication
	protected TcpListener tcp = new TcpListener(queue);
	protected UdpListener udp = new UdpListener(queue);
	protected HelloGenerator hello = new HelloGenerator();
	
	// workers
	private Map<String, Cohort> cohorts = new HashMap<String, Cohort>();
	private Map<String, Coordinator> coordinators = new HashMap<String, Coordinator>();
	
	private RestoreCohort restoreCohort = new RestoreCohort();
	private Map<String, RestoreCoordinator> restoreCoordinators = new HashMap<String, RestoreCoordinator>();
	
	private Map<String, String> coordinatorAddresses = new HashMap<String, String>();
	private Map<String, NodeSyncInfo> nodeSynchronization = new HashMap<String, NodeSyncInfo>();
	
	class NodeSyncInfo
	{
		public int BeatsOutOfSync;
		public int LastBeat;
	}
	
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
        
        // install restore thread
        new Thread(restoreCohort).start();
	}
	
	public void Run() throws InterruptedException
	{
    	Logger.getInstance().log("Initializing", LOGGING_NAME, Logger.Level.INFO);
		Initialize();
		Logger.getInstance().log("Initializing Done", LOGGING_NAME, Logger.Level.INFO);
		
		// main loop
		while(true)
		{
			Message m = queue.take();
			process(m);
		}
	}
	
	// IMPROVEMENT: should be visitor pattern
	public void process(Message msg) throws InterruptedException
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
	
	public void processTpcMessage(TPCMessage msg) throws InterruptedException
	{
		String transactionId = msg.getTransactionId();
		String coorAddr = coordinatorAddresses.get(transactionId);
		
		if(coorAddr == null)
		{
			Logger.getInstance().log(
					"processTpcMessage coordinator address for tid " + transactionId + " is null", 
					LOGGING_NAME, 
					Logger.Level.WARNING);
			return;
		}
		
		if (msg instanceof CanCommitMessage) 
		{
			// create cohort and start his job
			Cohort coh = new CohortImpl();
			cohorts.put(transactionId, coh);
			coh.setTransactionId(transactionId);
			coh.setCoordinatorAddress(coorAddr);
			coh.addEndTransactionListener(this);
			coh.putMessage(msg);
		} 
		else if (msg instanceof PreCommitMessage
				|| msg instanceof DoCommitMessage
				|| msg instanceof AbortMessage) 
		{
			// check if cohort exists
			Cohort cohort = cohorts.get(transactionId);
			
			if(cohort == null)
			{
				Logger.getInstance().log(
						"No cohort for tid" + transactionId, 
						LOGGING_NAME, 
						Logger.Level.WARNING);
				return;
			}
			
			cohort.putMessage(msg);
		} 
		else if (msg instanceof YesForCommitMessage
				|| msg instanceof NoForCommitMessage
				|| msg instanceof AckPreCommitMessage
				|| msg instanceof HaveCommittedMessage) 
		{
			// send to COORDINATOR
			Coordinator coordinator = coordinators.get(transactionId);
			
			if(coordinator == null)
			{
				Logger.getInstance().log(
						"No coordinator for tid" + transactionId, 
						LOGGING_NAME, 
						Logger.Level.WARNING);
				return;
			}
			
			coordinator.putMessage(msg);
		}
	}
	
	public void  processRestoreMessage(RestoreMessage msg) throws InterruptedException
	{
		restoreCohort.putMessage(msg);
	}
	
	public void  processTransactionMessage(TransactionMessage msg) throws InterruptedException
	{
		// create COORDINATOR
		String transactionId = Util.generateGUID();
		Coordinator coor = new CoordinatorImpl();
		coordinators.put(transactionId, coor);
		coor.setTransactionId(transactionId);
		coor.setClientAddress(msg.getSenderAddress());
		coor.setClientPort(msg.getSenderPort());
		coor.addEndTransactionListener(this);
		
		// give him the message
		coor.putMessage(msg);
	}
	
	public void  processHelloMessage(HelloMessage msg)
	{
		String node = msg.getSenderAddress();
		
		// TODO: sprawdzanie czy wezel istnieje - nie istnieje dodanie
		// if( !TcpSender.has(node) ) TcpSender.addNode(node);
		
		// TODO: zliczanie jak dlugo wezel jest out of sync
		
		/*
		NodeSyncInfo nsi = nodeSynchronization.get(node);
		
		if(nsi == null)
		{
			nsi = new NodeSyncInfo(0,0);
			nodeSynchronization.put(node, nsi);
		}
		else
		{
			if(System.cnsi.LastBeat < OUT_OF_SYNC_RESET_TIME_MS)
			{
				// reset counter and increment by 1
			}
			else if(nsi.BeatsOutOfSync >= OUT_OF_SYNC_BEATS_THRESHOLD)
			{
				RestoreCoordinator rc = new RestoreCoordinator(node);
				restoreCoordinators.put(rc);
			}
		}
		*/
		
		// TODO: jezeli nie byl aktywny przez dlugi okres czasu - stworzernie
		// Restore Coordinator
	}
	
	
	public void onEndTransaction(TPCParticipant participant) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}



}
