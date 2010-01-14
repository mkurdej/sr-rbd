/**
 * 
 */
package ddb.communication;

import java.util.HashMap;
import java.util.Map;

import ddb.msg.Message;
import ddb.restore.RestoreCohort;
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

	private Map<String, Cohort> cohorts = new HashMap<String, Cohort>();
	private Map<String, Coordinator> coordinators = new HashMap<String, Coordinator>();
	private Map<String, RestoreCohort> restoreCohorts = new HashMap<String, RestoreCohort>();

	private Map<String, String> coordinatorAddresses = new HashMap<String, String>();

	/**
	 * /* (non-Javadoc) * @see
	 * EndTransactionListener#onEndTransaction(TPCParticipant participant)
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void onEndTransaction(TPCParticipant participant) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * @see Dispatcher#dispatchMessage(String msgString, String senderAddress,
	 *      String senderPort)
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void dispatchMessage(String msgString, String senderAddress,
			String senderPort) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private static final DispatcherImpl instance = new DispatcherImpl();

	/**
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	private DispatcherImpl() {
		// begin-user-code
		// TODO: Implement constructor logic
		// end-user-code
	}

	/**
	 * @generated "Singleton (com.ibm.xtools.patterns.content.gof.creational.singleton.SingletonPattern)"
	 */
	public static DispatcherImpl getInstance() {
		// begin-user-code

		return instance;
		// end-user-code
	}

	@Override
	public void dispatchMessage(Message msg, String senderAddress, int senderPort) {

		if (msg instanceof TPCMessage) {
			String transactionId = ((TPCMessage) msg).getTransactionId();
			String coorAddr = coordinatorAddresses.get(transactionId);
			if (msg instanceof CanCommitMessage) {
				// CREATE COHORT
				// TODO change CohortImpl to extend Cohort (write to interface,
				// not to implementation!)
				Cohort coh = new CohortImpl();
				cohorts.put(transactionId, coh);
				coh.setTransactionId(transactionId);
				coh.setCoordinatorAddress(coorAddr);
				coh.addEndTransactionListener(this);
				coh.processMessage(msg);
			} else if (msg instanceof PreCommitMessage
					|| msg instanceof DoCommitMessage
					|| msg instanceof AbortMessage) {
				Cohort coh = cohorts.get(transactionId);
				coh.processMessage(msg);
			} else if (msg instanceof YesForCommitMessage
					|| msg instanceof NoForCommitMessage
					|| msg instanceof AckPreCommitMessage
					|| msg instanceof HaveCommittedMessage) {
				// send to COORDINATOR
				Coordinator coor = coordinators.get(transactionId);
				coor.processMessage(msg);
			}
		} else if (msg instanceof TransactionMessage) { // from client
			// create COORDINATOR
			String transactionId = Util.generateGUID();
			Coordinator coor = new CoordinatorImpl();
			coordinators.put(transactionId, coor);
			coor.setTransactionId(transactionId);
			coor.setClientAddress(senderAddress);
			coor.setClientPort(senderPort);
			coor.addEndTransactionListener(this);
			coor.processMessage(msg);
		}
		// ClientResponse -- should not come to Dispatcher
		// TODO DiscoverMessage (udp)
		// to RESTORE

	}
}
