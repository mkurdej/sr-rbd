// +--

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD
{
    class Dispatcher
    {
        //public class Dispatcher implements EndTransactionListener, EndRestorationListener {

        //    private final static String LOGGING_NAME = "Dispatcher";
        //    protected final static int OUT_OF_SYNC_BEATS_THRESHOLD = 4;
        //    protected final static int OUT_OF_SYNC_RESET_TIME_MS = 60000;

        //    // destination for messages
        //    protected BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();

        //    // communication
        //    protected TcpListener tcp;
        //    protected UdpListener udp;
        //    protected HelloGenerator hello;

        //    // workers
        //    protected Map<String, Cohort> 		cohorts = new HashMap<String, Cohort>();
        //    protected Map<String, Coordinator> 	coordinators = new HashMap<String, Coordinator>();

        //    protected RestoreCohort restoreCohort = new RestoreCohort();
        //    protected BlockedCohort blockedCohort = new BlockedCohort();
        //    protected BlockedCoordinator blockedCoordinator = new BlockedCoordinator();
        //    protected BlockedRestoreCoordinator blockedRestoreCoordinator = new BlockedRestoreCoordinator();
        //    protected Map<InetSocketAddress, RestoreCoordinator> restoreCoordinators = new HashMap<InetSocketAddress, RestoreCoordinator>();

        //    // others
        //    protected Map<InetSocketAddress, NodeSyncInfo> nodeSynchronization = new HashMap<InetSocketAddress, NodeSyncInfo>();
        //    protected InetSocketAddress me;
        //    protected int isRestoring = 0; // TODO: remove


        //    public Dispatcher()
        //    {
        //        tcp = new TcpListener(queue, Config.TcpAddress(), Config.TcpPort());
        //        udp = new UdpListener(queue, Config.UdpPort());
        //        hello = new HelloGenerator(Config.TcpPort());
        //    }

        //    public void Initialize()
        //    {
        //        // assign self identifier
        //        me = new InetSocketAddress(Config.TcpAddress(), Config.TcpPort());

        //        // install threads for managing communication
        //        new Thread(tcp, "TPC_LISTENER").start();
        //        new Thread(udp, "UDP_LISTENER").start();
        //        new Thread(hello, "HELLO_SENDER").start();

        //        // install restore thread
        //        new Thread(restoreCohort, "RESTORE_COHORT").start();

        //        // install blocked threads
        //        new Thread(blockedCohort, "BLOCKED_COHORT").start();
        //        new Thread(blockedCoordinator, "BLOCKED_COORDINATOR").start();
        //        new Thread(blockedRestoreCoordinator, "BLOCKER_RESTORE_COORDINATOR").start();
        //    }

            public void Run() //throws InterruptedException
            {
        //        Logger.getInstance().log("Initializing", LOGGING_NAME, Logger.Level.INFO);
        //        Initialize();
        //        Logger.getInstance().log("Initializing Done", LOGGING_NAME, Logger.Level.INFO);

        //        // main loop
        //        while(true)
        //        {
        //            Message m = queue.take();

        //            int count = TcpSender.getInstance().getServerNodesCount();

        //            if( count < Config.MinOtherNodes())
        //            {

        //                Logger.getInstance().log(
        //                    "Brainsplit mode due to " 
        //                        + Integer.toString(count) 
        //                        + " of " 
        //                        + Config.MinOtherNodes() 
        //                        + " seen - message: " 
        //                        + m.toString(), 
        //                    LOGGING_NAME, 
        //                    Logger.Level.WARNING);

        //                brainsplit(m);
        //            }
        //            else
        //            {
        //                //if(!(m instanceof HelloMessage))
        //                //{
        //                    Logger.getInstance().log(
        //                            "Processing message:" + m.toString(), 
        //                            LOGGING_NAME, 
        //                            Logger.Level.INFO);
        //                //}

        //                process(m);
        //            }
        //        }
            }

        //    // IMPROVEMENT: should be visitor pattern
        //    public synchronized void process(Message msg) throws InterruptedException
        //    {
        //        if (msg instanceof TPCMessage) 
        //        {
        //            processTpcMessage((TPCMessage)msg);
        //        }
        //        else if (msg instanceof RestoreMessage)
        //        {
        //            processRestoreMessage((RestoreMessage)msg);
        //        }
        //        else if (msg instanceof TransactionMessage)
        //        {
        //            processTransactionMessage((TransactionMessage)msg);
        //        }
        //        else if (msg instanceof HelloMessage)
        //        {
        //            processHelloMessage((HelloMessage)msg);
        //        }
        //    }

        //    public synchronized void brainsplit(Message msg) throws InterruptedException
        //    {
        //        // current transaction can timeout
        //        if(msg instanceof CanCommitMessage) 
        //        {
        //            // forbid new transactions
        //            Logger.getInstance().log(
        //                    "Sending message to blocked cohort: " + msg.toString(), 
        //                    LOGGING_NAME, 
        //                    Logger.Level.INFO);

        //            blockedCohort.putMessage(msg);
        //        }
        //        else if(msg instanceof TransactionMessage)
        //        {
        //            // forbid new transactions
        //            Logger.getInstance().log(
        //                    "Sending message to blocked coordinator: " + msg.toString(), 
        //                    LOGGING_NAME, 
        //                    Logger.Level.INFO);

        //            blockedCoordinator.putMessage(msg);
        //        }
        //        else if(msg instanceof RestoreIncentive)
        //        {
        //            // forbid new restoration
        //            Logger.getInstance().log(
        //                    "Sending message to blocked restore coordinator: " + msg.toString(), 
        //                    LOGGING_NAME, 
        //                    Logger.Level.INFO);

        //            blockedRestoreCoordinator.putMessage(msg);
        //        }
        //        else if(msg instanceof HelloMessage)
        //        {
        //            HelloMessage hm = (HelloMessage)msg;

        //            InetSocketAddress node = new InetSocketAddress(
        //                    hm.getSender().getAddress(), 
        //                    hm.getListeningPort()
        //            );

        //            if(node.equals(me))
        //                return;


        //            // try to add new node
        //            TcpSender.getInstance().AddServerNode(node);
        //        }
        //        else
        //        {
        //            Logger.getInstance().log(
        //                    "Undelivered message due to brainsplit: " + msg.toString(), 
        //                    LOGGING_NAME, 
        //                    Logger.Level.WARNING);
        //        }
        //    }

        //    public void processTpcMessage(TPCMessage msg) throws InterruptedException
        //    {
        //        String transactionId = msg.getTransactionId();

        //        if (msg instanceof CanCommitMessage) 
        //        {
        //            if(isRestoring > 0)
        //            {
        //                Logger.getInstance().log(
        //                        "Sending message to blocked cohort due to restoring in progress: " + msg.toString(), 
        //                        LOGGING_NAME, 
        //                        Logger.Level.INFO);

        //                blockedCohort.putMessage(msg);
        //            }
        //            else
        //            {	
        //                // check if cohort already exists
        //                if(cohorts.get(transactionId) != null)
        //                {
        //                    Logger.getInstance().log(
        //                            "Cohort already exists for tid: " + transactionId, 
        //                            LOGGING_NAME, 
        //                            Logger.Level.WARNING);

        //                    return;
        //                }

        //                Logger.getInstance().log(
        //                        "Creating new cohort with tid " + transactionId + " : " + msg.toString(), 
        //                        LOGGING_NAME, 
        //                        Logger.Level.INFO);

        //                // create cohort and start his job
        //                Cohort coh = new CohortImpl();
        //                cohorts.put(transactionId, coh);
        //                coh.setTransactionId(transactionId);
        //                coh.setCoordinatorAddress(msg.getSender());
        //                coh.setDatabaseState(DatabaseStateImpl.getInstance());
        //                coh.addEndTransactionListener(this);
        //                coh.processMessage(msg);
        //            }
        //        } 
        //        else if (msg instanceof PreCommitMessage
        //                || msg instanceof DoCommitMessage
        //                || msg instanceof AbortMessage) 
        //        {
        //            Logger.getInstance().log(
        //                    "Dispatching to cohort with tid " + transactionId + " : " + msg.toString(), 
        //                    LOGGING_NAME, 
        //                    Logger.Level.INFO);

        //            // check if cohort exists
        //            Cohort cohort = cohorts.get(transactionId);

        //            if(cohort == null)
        //            {
        //                Logger.getInstance().log(
        //                        "No cohort for tid" + transactionId, 
        //                        LOGGING_NAME, 
        //                        Logger.Level.WARNING);
        //                return;
        //            }

        //            cohort.processMessage(msg);
        //        } 
        //        else if (msg instanceof YesForCommitMessage
        //                || msg instanceof NoForCommitMessage
        //                || msg instanceof AckPreCommitMessage
        //                || msg instanceof HaveCommittedMessage) 
        //        {
        //            Logger.getInstance().log(
        //                    "Dispatching to coordinator with tid " + transactionId + " : " + msg.toString(), 
        //                    LOGGING_NAME, 
        //                    Logger.Level.INFO);

        //            // send to COORDINATOR
        //            Coordinator coordinator = coordinators.get(transactionId);

        //            if(coordinator == null)
        //            {
        //                Logger.getInstance().log(
        //                        "No coordinator for tid" + transactionId, 
        //                        LOGGING_NAME, 
        //                        Logger.Level.WARNING);
        //                return;
        //            }

        //            coordinator.processMessage(msg);
        //        }
        //    }

        //    public void processRestoreMessage(RestoreMessage msg) throws InterruptedException
        //    {
        //        if(msg instanceof RestoreIncentive
        //            || msg instanceof RestoreTableList
        //            || msg instanceof RestoreTable)
        //        {

        //            Logger.getInstance().log(
        //                    "Dispatching to restore cohort: " + msg.toString(), 
        //                    LOGGING_NAME, 
        //                    Logger.Level.INFO);

        //            restoreCohort.putMessage(msg);
        //        }
        //        else
        //        {
        //            InetSocketAddress node = msg.getSender();
        //            RestoreCoordinator coordinator =  restoreCoordinators.get(node);

        //            if(coordinator != null)
        //            {
        //                Logger.getInstance().log(
        //                        "Dispatching to restore coordinator for node - " + node.toString() + " : " + msg.toString(), 
        //                        LOGGING_NAME, 
        //                        Logger.Level.INFO);

        //                coordinator.putMessage(msg);
        //            }
        //            else
        //            {
        //                Logger.getInstance().log(
        //                        "No restore coordinator for node " + node.toString(), 
        //                        LOGGING_NAME, 
        //                        Logger.Level.WARNING);
        //            }
        //        }
        //    }

        //    public void processTransactionMessage(TransactionMessage msg) throws InterruptedException
        //    {
        //        if(isRestoring > 0)
        //        {
        //            blockedCoordinator.putMessage(msg);
        //        }
        //        else
        //        {
        //            String transactionId = Util.generateGUID();

        //            Logger.getInstance().log(
        //                    "Creating coordinator with tid - " + transactionId + " : " + msg.toString(), 
        //                    LOGGING_NAME, 
        //                    Logger.Level.INFO);


        //            // create COORDINATOR
        //            Coordinator coor = new CoordinatorImpl();
        //            coordinators.put(transactionId, coor);
        //            coor.setTransactionId(transactionId);
        //            coor.setClientAddress(msg.getSender());
        //            coor.setDatabaseState(DatabaseStateImpl.getInstance());
        //            coor.addEndTransactionListener(this);

        //            // give him the message
        //            coor.processMessage(msg);
        //        }
        //    }

        //    public void processHelloMessage(HelloMessage msg)
        //    {
        //        InetSocketAddress node = new InetSocketAddress(
        //                msg.getSender().getAddress(), 
        //                msg.getListeningPort()
        //        );

        //        if(node.equals(me))
        //            return;

        //        Logger.getInstance().log(
        //                "Processing hello from - " + node.toString() + " : " + msg.toString(), 
        //                LOGGING_NAME, 
        //                Logger.Level.INFO);

        //        TcpSender.getInstance().AddServerNode(node);

        //        NodeSyncInfo nsi = nodeSynchronization.get(node);

        //        // create entry for new nodes
        //        if(nsi == null)
        //        {
        //            nsi = new NodeSyncInfo();
        //            nodeSynchronization.put(node, nsi);
        //        }
        //        // reset counter for nodes which were unreachable for long period of time
        //        else if( nsi.getMsSinceLastBeat() < OUT_OF_SYNC_RESET_TIME_MS)
        //        {
        //            Logger.getInstance().log(
        //                    "Node was inactive for too long - " + nsi.getMsSinceLastBeat() + " - reseting nsi - " + node.toString() + " : " + msg.toString(), 
        //                    LOGGING_NAME, 
        //                    Logger.Level.INFO);

        //            nsi.InSync();
        //        }

        //        // check synchronization
        //        if(DatabaseStateImpl.getInstance().checkSync(msg.getTables()))
        //        {
        //            nsi.InSync();
        //        }	
        //        else 
        //        {
        //            if(nsi.getBeatsOutOfSync() < OUT_OF_SYNC_BEATS_THRESHOLD)
        //            {
        //                Logger.getInstance().log(
        //                        "Node out of sync " + nsi.getBeatsOutOfSync() + " times - " + node.toString() + " : " + msg.toString(), 
        //                        LOGGING_NAME, 
        //                        Logger.Level.INFO);


        //                nsi.BeatOutOfSync();
        //            }
        //            else if(restoreCoordinators.get(node) == null)
        //            {
        //                Logger.getInstance().log(
        //                        "Node out of sync detected creating restorator - " + node.toString() + " : " + msg.toString(), 
        //                        LOGGING_NAME, 
        //                        Logger.Level.INFO);


        //                // need to restore
        //                RestoreCoordinator rc = new RestoreCoordinator(node);
        //                restoreCoordinators.put(node, rc);
        //                rc.addEndRestorationListener(this);
        //                new Thread(rc).start();
        //            }
        //        }
        //    }


        //    public synchronized void onEndTransaction(TPCParticipant participant) {
        //        // begin-user-code
        //        String tid = participant.getTransactionId();

        //        if(cohorts.remove(tid) == null)
        //        {
        //            if(coordinators.remove(tid) == null)
        //            {
        //                Logger.getInstance().log(
        //                        "onEndTransaction didnt delete participant (should not happen)", 
        //                        LOGGING_NAME, 
        //                        Logger.Level.SEVERE);
        //            }
        //        }

        //        Logger.getInstance().log(
        //                "Transaction ended - tid: " + tid, 
        //                LOGGING_NAME, 
        //                Logger.Level.INFO);

        //        // end-user-code
        //    }

        //    @Override
        //    public synchronized void onEndRestoration(RestoreCoordinator coordinator) {

        //        InetSocketAddress node = coordinator.getTargetNode();

        //        NodeSyncInfo nsi = nodeSynchronization.get(node);

        //        // mark node as synchronized
        //        if(nsi == null)
        //        {
        //            Logger.getInstance().log(
        //                    "onEndRestoration nsi missing (should not happen)", 
        //                    LOGGING_NAME, 
        //                    Logger.Level.WARNING);
        //        }
        //        else
        //        {
        //            nsi.InSync();
        //        }

        //        // remove coordinator
        //        if(restoreCoordinators.remove(node) != coordinator)
        //        {
        //            Logger.getInstance().log(
        //                    "onEndRestoration deleted wrong coordinator (should not happen)", 
        //                    LOGGING_NAME, 
        //                    Logger.Level.WARNING);
        //        }

        //        Logger.getInstance().log(
        //                "Restoration ended for node: " + node.toString(), 
        //                LOGGING_NAME, 
        //                Logger.Level.INFO);
        //    }
        //}

    }
}
