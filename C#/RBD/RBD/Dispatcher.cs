// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Collections;
using System.Threading;

using RBD.TPC;
using RBD.TPC.Msg;
using RBD.Restore;
using RBD.Restore.Msg;
using RBD.Util;
using RBD.Msg;
using RBD.DB;
using RBD.TPC.COH;
using RBD.TPC.COR;
using RBD.Communication;

namespace RBD
{
    public class Dispatcher : EndTransactionListener, EndRestorationListener
    {
        private const String LOGGING_NAME = "Dispatcher";
        protected const int OUT_OF_SYNC_BEATS_THRESHOLD = 4;
        protected const int OUT_OF_SYNC_RESET_TIME_MS = 15000;
	    protected const int DOWN_NODES_CHECK_INTERVAL = 8000;

        // destination for messages
        protected BlockingQueue<Message> queue = new BlockingQueue<Message>();

        // communication
        protected TcpListener tcp;
        protected UdpListener udp;
        protected HelloGenerator hello;

        // workers
        protected Dictionary<String, Cohort> cohorts = new Dictionary<String, Cohort>();
        protected Dictionary<String, Coordinator> coordinators = new Dictionary<String, Coordinator>();

        protected RestoreCohort restoreCohort = new RestoreCohort();
        protected BlockedCohort blockedCohort = new BlockedCohort();
        protected BlockedCoordinator blockedCoordinator = new BlockedCoordinator();
        protected BlockedRestoreCoordinator blockedRestoreCoordinator = new BlockedRestoreCoordinator();
        protected Dictionary<IPEndPoint, RestoreCoordinator> restoreCoordinators = new Dictionary<IPEndPoint, RestoreCoordinator>();

        // others
        protected Dictionary<IPEndPoint, NodeSyncInfo> nodeSynchronization = new Dictionary<IPEndPoint, NodeSyncInfo>();
        protected IPEndPoint me;

        public Dispatcher()
        {
            tcp = new TcpListener(queue, Config.TcpAddress(), Config.TcpPort());
            udp = new UdpListener(queue, Config.UdpPort());
            hello = new HelloGenerator(Config.TcpPort());
        }

        public void Initialize()
        {
            // set queue for self writing
            TcpSender.getInstance().setQueue(queue);

            // assign self identifier
            me = new IPEndPoint(Config.TcpAddress(), Config.TcpPort());

            Thread t;
            // install threads for managing communication
            t = new Thread(new ThreadStart(tcp.run));
            t.Name = "TCP_LISTENER"; t.Start();
            t = new Thread(new ThreadStart(udp.run));
            t.Name = "UDP_LISTENER"; t.Start();
            t = new Thread(new ThreadStart(hello.run));
            t.Name = "HELLO_SENDER"; t.Start();

            // install restore thread
            t = new Thread(new ThreadStart(restoreCohort.run));
            t.Name = "RESTORE_COHORT"; t.Start();

            // install blocked threads
            t = new Thread(new ThreadStart(blockedCohort.run));
            t.Name = "BLOCKED_COHORT"; t.Start();
            t = new Thread(new ThreadStart(blockedCoordinator.run));
            t.Name = "BLOCKED_COORDINATOR"; t.Start();
            t = new Thread(new ThreadStart(blockedRestoreCoordinator.run));
            t.Name = "BLOCKER_RESTORE_COORDINATOR"; t.Start();

            // install stale connection detecting thread
            t = new Thread(new ThreadStart(this.detectStaleConnectionThread));
            t.Name = "DETECT_STALE_CONNECTION"; t.Start();
        }

        public void detectStaleConnectionThread()
        {
            try
            {
                while (true)
                {
                    Thread.Sleep(DOWN_NODES_CHECK_INTERVAL);
                    detectDownNodes();
                }
            }
            catch (ThreadInterruptedException)
            {
                Logger.getInstance().log(
                        "InterruptedException in stale detection thread",
                        LOGGING_NAME,
                        Logger.Level.SEVERE);
            }
        }

        public void Run() //throws InterruptedException
        {
            Logger.getInstance().log("Initializing", LOGGING_NAME, Logger.Level.INFO);
            Initialize();
            Logger.getInstance().log("Initializing Done", LOGGING_NAME, Logger.Level.INFO);

            // main loop
            while (true)
            {
                Message m = queue.take();

                int count = TcpSender.getInstance().getServerNodesCount();

                if (count < Config.MinOtherNodes())
                {
                    Logger.getInstance().log(
                        "Brainsplit mode due to "
                            + count.ToString()
                            + " of "
                            + Config.MinOtherNodes()
                            + " seen - message: "
                            + m.toString(),
                        LOGGING_NAME,
                        Logger.Level.WARNING);

                    brainsplit(m);
                }
                else
                {
                    if (!(m is HelloMessage))
                    {
                        Logger.getInstance().log(
                                "Processing message:" + m.toString(),
                                LOGGING_NAME,
                                Logger.Level.INFO);
                    }

                    process(m);
                }
            }
        }

        // IMPROVEMENT: should be visitor pattern
        public void process(Message msg) //throws InterruptedException
        {
            lock (this)
            {

                if (msg is TPCMessage)
                {
                    processTpcMessage((TPCMessage)msg);
                }
                else if (msg is RestoreMessage)
                {
                    processRestoreMessage((RestoreMessage)msg);
                }
                else if (msg is TransactionMessage)
                {
                    processTransactionMessage((TransactionMessage)msg);
                }
                else if (msg is HelloMessage)
                {
                    processHelloMessage((HelloMessage)msg);
                }
            }
        }

        public void brainsplit(Message msg) //throws InterruptedException
        {
            lock (this)
            {
                // current transaction can timeout
                if (msg is CanCommitMessage)
                {
                    // forbid new transactions
                    Logger.getInstance().log(
                            "Sending message to blocked cohort: " + msg.toString(),
                            LOGGING_NAME,
                            Logger.Level.INFO);

                    blockedCohort.putMessage(msg);
                }
                else if (msg is TransactionMessage)
                {
                    // forbid new transactions
                    Logger.getInstance().log(
                            "Sending message to blocked coordinator: " + msg.toString(),
                            LOGGING_NAME,
                            Logger.Level.INFO);

                    blockedCoordinator.putMessage(msg);
                }
                else if (msg is RestoreIncentive)
                {
                    // forbid new restoration
                    Logger.getInstance().log(
                            "Sending message to blocked restore coordinator: " + msg.toString(),
                            LOGGING_NAME,
                            Logger.Level.INFO);

                    blockedRestoreCoordinator.putMessage(msg);
                }
                else if (msg is HelloMessage)
                {
                    HelloMessage hm = (HelloMessage)msg;

                    IPEndPoint node = new IPEndPoint(
                            hm.getSender().Address,
                            hm.ListeningPort
                    );

                    if (node.Equals(me))
                        return;


                    // try to add new node
                    TcpSender.getInstance().AddServerNode(node, queue);
                }
                else
                {
                    Logger.getInstance().log(
                            "Undelivered message due to brainsplit: " + msg.toString(),
                            LOGGING_NAME,
                            Logger.Level.WARNING);
                }
            }
        }

        public void processTpcMessage(TPCMessage msg) //throws InterruptedException
        {
            String transactionId = msg.getTransactionId();

            if (msg is CanCommitMessage)
            {
                // check if cohort already exists
                if (cohorts.ContainsKey(transactionId))
                {
                    Logger.getInstance().log(
                            "Cohort already exists for tid: " + transactionId,
                            LOGGING_NAME,
                            Logger.Level.WARNING);

                    return;
                }

                Logger.getInstance().log(
                        "Creating new cohort with tid " + transactionId + " : " + msg.toString(),
                        LOGGING_NAME,
                        Logger.Level.INFO);

                // create cohort and start his job
                Cohort coh = new CohortImpl();
                cohorts[transactionId] = coh;
                coh.setTransactionId(transactionId);
                coh.setCoordinatorAddress(msg.getSender());
                coh.setDatabaseState(DatabaseStateImpl.getInstance());
                coh.setConnector(DbConnectorImpl.getInstance());
                coh.addEndTransactionListener(this);
                coh.processMessage(msg);

            }
            else if (msg is PreCommitMessage
                    || msg is DoCommitMessage
                    || msg is AbortMessage)
            {
                Logger.getInstance().log(
                        "Dispatching to cohort with tid " + transactionId + " : " + msg.toString(),
                        LOGGING_NAME,
                        Logger.Level.INFO);

                // check if cohort exists
                Cohort cohort = null;
                if (cohorts.ContainsKey(transactionId))
                {
                    cohort = cohorts[transactionId];
                }
                if (cohort == null)
                {
                    Logger.getInstance().log(
                            "No cohort for tid" + transactionId,
                            LOGGING_NAME,
                            Logger.Level.WARNING);
                    return;
                }

                cohort.processMessage(msg);
            }
            else if (msg is YesForCommitMessage
                    || msg is NoForCommitMessage
                    || msg is AckPreCommitMessage
                    || msg is HaveCommittedMessage
                    || msg is RBD.TPC.Msg.ErrorMessage)
            {
                Logger.getInstance().log(
                        "Dispatching to coordinator with tid " + transactionId + " : " + msg.toString(),
                        LOGGING_NAME,
                        Logger.Level.INFO);

                // send to COORDINATOR
                Coordinator coordinator = null;
                if (coordinators.ContainsKey(transactionId))
                {
                    coordinator = coordinators[transactionId];
                }
                if (coordinator == null)
                {
                    //				Logger.getInstance().log(
                    //						"No coordinator for tid" + transactionId, 
                    //						LOGGING_NAME, 
                    //						Logger.Level.WARNING);
                    return;
                }

                coordinator.processMessage(msg);
            }
        }

        public void processRestoreMessage(RestoreMessage msg) //throws InterruptedException
        {
            if (msg is RestoreIncentive
                || msg is RestoreTableList
                || msg is RestoreTable)
            {
                Logger.getInstance().log(
                        "Dispatching to restore cohort: " + msg.toString(),
                        LOGGING_NAME,
                        Logger.Level.INFO);

                restoreCohort.putMessage(msg);
            }
            else
            {
                IPEndPoint node = msg.getSender();
                RestoreCoordinator coordinator = null;
                if (restoreCoordinators.ContainsKey(node))
                {
                    coordinator = restoreCoordinators[node];
                }
                if (coordinator != null)
                {
                    Logger.getInstance().log(
                            "Dispatching to restore coordinator for node - " + node.ToString() + " : " + msg.ToString(),
                            LOGGING_NAME,
                            Logger.Level.INFO);

                    coordinator.putMessage(msg);
                }
                else
                {
                    Logger.getInstance().log(
                            "No restore coordinator for node " + node.ToString(),
                            LOGGING_NAME,
                            Logger.Level.WARNING);
                }
            }
        }

        public void processTransactionMessage(TransactionMessage msg) //throws InterruptedException
        {
            String transactionId = Util.Util.generateGUID();

            Logger.getInstance().log(
                    "Creating coordinator with tid - " + transactionId + " : " + msg.toString(),
                    LOGGING_NAME,
                    Logger.Level.INFO);

            // create COORDINATOR
            Coordinator coor = new CoordinatorImpl();
            coordinators[transactionId] = coor;
            coor.setTransactionId(transactionId);
            coor.setClientAddress(msg.getSender());
            coor.setDatabaseState(DatabaseStateImpl.getInstance());
            coor.setConnector(DbConnectorImpl.getInstance());
            coor.addEndTransactionListener(this);

            // give him the message
            coor.processMessage(msg);

        }

        public void detectDownNodes()
        {
            lock (this)
            {
                IDictionaryEnumerator it = nodeSynchronization.GetEnumerator();
                while (it.MoveNext())
                {
                    IPEndPoint isa = (IPEndPoint)it.Key;
                    NodeSyncInfo nsi = (NodeSyncInfo)it.Value;

                    if (nsi.getMsSinceLastBeat() > OUT_OF_SYNC_RESET_TIME_MS)
                    {
                        Logger.getInstance().log(
                                "Detected stale node - removing connection " + isa.ToString(),
                                LOGGING_NAME,
                                Logger.Level.INFO);

                        TcpSender.getInstance().removeNode(isa);
                        nodeSynchronization.Remove((IPEndPoint)it.Key);
                    }
                }
            }
        }

        public void updateNodeSyncInfo(IPEndPoint node, IList<TableVersion> tvs)
        {
            lock (this)
            {
                NodeSyncInfo nsi = null;
                if (nodeSynchronization.ContainsKey(node))
                {
                    nsi = nodeSynchronization[node];
                }
                // create entry for new nodes
                if (nsi == null)
                {
                    nsi = new NodeSyncInfo();
                    nodeSynchronization[node] = nsi;
                }
                // reset counter for nodes which were unreachable for long period of time
                else if (nsi.getMsSinceLastBeat() > OUT_OF_SYNC_RESET_TIME_MS)
                {
                    Logger.getInstance().log(
                            "Node was inactive for too long - " + nsi.getMsSinceLastBeat() + " - reseting nsi - " + node.ToString(),
                            LOGGING_NAME,
                            Logger.Level.INFO);

                    nsi.InSync();
                }

                // check synchronization
                if (DatabaseStateImpl.getInstance().checkSync(tvs))
                {
                    nsi.InSync();
                }
                else
                {
                    if (nsi.BeatsOutOfSync > OUT_OF_SYNC_BEATS_THRESHOLD)
                    {
                        Logger.getInstance().log(
                                "Node out of sync " + nsi.BeatsOutOfSync + " times - " + node.ToString(),
                                LOGGING_NAME,
                                Logger.Level.INFO);
                        nsi.BeatOutOfSync();
                    }
                    else if (restoreCoordinators.ContainsKey(node) == false)
                    {
                        Logger.getInstance().log(
                                "Node out of sync detected creating restorator - " + node.ToString(),
                                LOGGING_NAME,
                                Logger.Level.INFO);

                        // need to restore
                        RestoreCoordinator rc = new RestoreCoordinator(node);
                        restoreCoordinators[node] = rc;
                        rc.addEndRestorationListener(this);

                        Thread t = new Thread(new ThreadStart(rc.run));
                        t.Name = "RESTORE_COORDINATOR"; t.Start();
                    }
                }
            }
        }

        public void processHelloMessage(HelloMessage msg)
        {
            IPEndPoint node = new IPEndPoint(
                    msg.getSender().Address,
                    msg.ListeningPort
            );

            if (node.Equals(me))
                return;

            Logger.getInstance().log(
                    "Processing hello from - " + node.ToString() + " : " + msg.toString(),
                    LOGGING_NAME,
                    Logger.Level.INFO);

            TcpSender.getInstance().AddServerNode(node, queue);
            updateNodeSyncInfo(node, msg.Tables);
        }

        public void onEndTransaction(TPCParticipant participant)
        {
            lock (this)
            {
                // begin-user-code
                String tid = participant.getTransactionId();

                if (cohorts.Remove(tid) == false)
                {
                    if (coordinators.Remove(tid) == false)
                    {
                        Logger.getInstance().log(
                                "onEndTransaction didn't delete participant (should not happen)",
                                LOGGING_NAME,
                                Logger.Level.SEVERE);
                    }
                }

                Logger.getInstance().log(
                        "Transaction ended - tid: " + tid,
                        LOGGING_NAME,
                        Logger.Level.INFO);
            }
            // end-user-code
        }

        public void onEndRestoration(RestoreCoordinator coordinator)
        {
            lock (this)
            {
                IPEndPoint node = coordinator.getTargetNode();

                NodeSyncInfo nsi = null;
                if (nodeSynchronization.ContainsKey(node))
                {
                    nsi = nodeSynchronization[node];
                }
                // mark node as synchronized
                if (nsi == null)
                {
                    Logger.getInstance().log(
                            "onEndRestoration nsi missing (should not happen)",
                            LOGGING_NAME,
                            Logger.Level.WARNING);
                }
                else
                {
                    nsi.InSync();
                }

                // remove coordinator
                if (restoreCoordinators.Remove(node) == false)
                {
                    Logger.getInstance().log(
                            "onEndRestoration deleted wrong coordinator (should not happen)",
                            LOGGING_NAME,
                            Logger.Level.WARNING);
                }

                Logger.getInstance().log(
                        "Restoration ended for node: " + node.ToString(),
                        LOGGING_NAME,
                        Logger.Level.INFO);
            }
        }
    }

}