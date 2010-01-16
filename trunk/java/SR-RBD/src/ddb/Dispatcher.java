/**
 * 
 */
package ddb;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ddb.communication.HelloGenerator;
import ddb.communication.TcpListener;
import ddb.communication.TcpSender;
import ddb.communication.UdpListener;
import ddb.msg.HelloMessage;
import ddb.msg.Message;
import ddb.restore.EndRestorationListener;
import ddb.restore.RestoreCohort;
import ddb.restore.RestoreCoordinator;
import ddb.restore.msg.RestoreIncentive;
import ddb.restore.msg.RestoreMessage;
import ddb.restore.msg.RestoreTable;
import ddb.restore.msg.RestoreTableList;
import ddb.tpc.EndTransactionListener;
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
public class Dispatcher implements EndTransactionListener, EndRestorationListener {

	private final static String LOGGING_NAME = "DispatcherImpl";
	private final static int OUT_OF_SYNC_BEATS_THRESHOLD = 4;
	private final static int OUT_OF_SYNC_RESET_TIME_MS = 5000;
	
	// destination for messages
	protected BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
	
	// communication
	protected TcpListener tcp;
	protected UdpListener udp;
	protected HelloGenerator hello;
	
	// workers
	private Map<String, Cohort> 		cohorts = new HashMap<String, Cohort>();
	private Map<String, Coordinator> 	coordinators = new HashMap<String, Coordinator>();
	
	private RestoreCohort restoreCohort = new RestoreCohort();
	private Map<InetSocketAddress, RestoreCoordinator> restoreCoordinators = new HashMap<InetSocketAddress, RestoreCoordinator>();
	
	// others
	private Map<InetSocketAddress, NodeSyncInfo> nodeSynchronization = new HashMap<InetSocketAddress, NodeSyncInfo>();
	
	public Dispatcher(int port)
	{
		tcp = new TcpListener(queue, port);
		udp = new UdpListener(queue);
		hello = new HelloGenerator(port);
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
		
		if (msg instanceof CanCommitMessage) 
		{
			// check if cohort already exists
			if(cohorts.get(transactionId) != null)
			{
				Logger.getInstance().log(
						"Cohort already exists for tid: " + transactionId, 
						LOGGING_NAME, 
						Logger.Level.WARNING);
				
				return;
			}
			
			// create cohort and start his job
			Cohort coh = new CohortImpl();
			cohorts.put(transactionId, coh);
			coh.setTransactionId(transactionId);
			coh.setCoordinatorAddress(msg.getSender());
			coh.addEndTransactionListener(this);
			coh.processMessage(msg);
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
			
			cohort.processMessage(msg);
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
			
			coordinator.processMessage(msg);
		}
	}
	
	public void  processRestoreMessage(RestoreMessage msg) throws InterruptedException
	{
		if(msg instanceof RestoreIncentive
			|| msg instanceof RestoreTableList
			|| msg instanceof RestoreTable)
		{
			restoreCohort.putMessage(msg);
		}
		else
		{
			InetSocketAddress node = msg.getSender();
			RestoreCoordinator coordinator =  restoreCoordinators.get(node);
			
			if(coordinator != null)
			{
				coordinator.putMessage(msg);
			}
			else
			{
				Logger.getInstance().log(
						"No restore coordinator for node " + node.toString(), 
						LOGGING_NAME, 
						Logger.Level.WARNING);
			}
		}
	}
	
	public void  processTransactionMessage(TransactionMessage msg) throws InterruptedException
	{
		// create COORDINATOR
		String transactionId = Util.generateGUID();
		Coordinator coor = new CoordinatorImpl();
		coordinators.put(transactionId, coor);
		coor.setTransactionId(transactionId);
		coor.setClientAddress(msg.getSender());
		coor.addEndTransactionListener(this);
		
		// give him the message
		coor.processMessage(msg);
	}
	
	public void  processHelloMessage(HelloMessage msg)
	{
		InetSocketAddress node = msg.getSender();
		
		TcpSender.getInstance().AddServerNode(
			new InetSocketAddress(
					node.getAddress(), 
					msg.getListeningPort()
			)
		);
		
		NodeSyncInfo nsi = nodeSynchronization.get(node);
		
		// create entry for new nodes
		if(nsi == null)
		{
			nsi = new NodeSyncInfo();
			nodeSynchronization.put(node, nsi);
		}
		// reset counter for nodes which were unreachable for long period of time
		else if( nsi.getMsSinceLastBeat() < OUT_OF_SYNC_RESET_TIME_MS)
		{
			nsi.InSync();
		}
		
		// check synchronization
		if(DatabaseState.getInstance().checkSync(msg.getTables()))
		{
			nsi.InSync();
		}	
		else 
		{
			if(nsi.getBeatsOutOfSync() < OUT_OF_SYNC_BEATS_THRESHOLD)
			{
				nsi.BeatOutOfSync();
			}
			else if(restoreCoordinators.get(node) == null)
			{
				// TODO: wylaczenie calej bazy
				
				// need to restore
				RestoreCoordinator rc = new RestoreCoordinator(node, this);
				restoreCoordinators.put(node, rc);
				
				new Thread(rc).start();
			}
		}
	}
	
	
	public void onEndTransaction(TPCParticipant participant) {
		// begin-user-code
		String tid = participant.getTransactionId();
		
		if(cohorts.remove(tid) == null)
		{
			if(coordinators.remove(tid) == null)
			{
				Logger.getInstance().log(
						"onEndTransaction didnt delete participant (should not happen)", 
						LOGGING_NAME, 
						Logger.Level.SEVERE);
			}
		}

		// end-user-code
	}

	@Override
	public void onEndRestoration(RestoreCoordinator coordinator) {
		if(restoreCoordinators.remove(coordinator.getTargetNode()) != coordinator)
		{
			Logger.getInstance().log(
					"onEndRestoration deleted wrong coordinator (should not happen)", 
					LOGGING_NAME, 
					Logger.Level.SEVERE);
		}
	}
}
