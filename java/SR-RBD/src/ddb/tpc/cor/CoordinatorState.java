/**
 * 
 */
package ddb.tpc.cor;

import java.net.InetSocketAddress;

import ddb.Logger;
import ddb.Logger.Level;
import ddb.tpc.TimeoutListener;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.ErrorMessage;
import ddb.tpc.msg.TransactionMessage;

/** 
 * @author PKalanski
 */
abstract public class CoordinatorState implements TimeoutListener {
	protected CoordinatorImpl coordinator;
	/**
	 * Nazwa logera
	 */
	private static final String LOGGER_NAME = "KOORDYNATOR";
	
	public void setCoordinator(CoordinatorImpl coordinator) {
		this.coordinator = coordinator;
	}

	/** 
	 * @param node
	 */
	public void onYesForCommit(InetSocketAddress node) {
		Logger.getInstance().log("Nie oczekiwano wiadomosci: YesForCommit", LOGGER_NAME, Level.WARNING);
	}

	/** 
	 * @param node
	 */
	public void onNoForCommit(InetSocketAddress node) {
		Logger.getInstance().log("Nie oczekiwano wiadomosci: NoForCommit", LOGGER_NAME, Level.WARNING);
	}

	/** 
	 * @param node
	 */
	public void onAckPreCommit(InetSocketAddress node)  {
		Logger.getInstance().log("Nie oczekiwano wiadomosci: AckPreCommit", LOGGER_NAME, Level.WARNING);
	}

	/** 
	 * @param message
	 */
	public void onHaveCommitted(HaveCommittedMessage message) {
		Logger.getInstance().log("Nie oczekiwano wiadomosci: HaveCommitted", LOGGER_NAME, Level.WARNING);
	}
	/** 
	 */
	public void onTransaction(TransactionMessage message) {
		Logger.getInstance().log("Nie oczekiwano wiadomosci: TransactionMessage", LOGGER_NAME, Level.WARNING);
	}

	/** 
	 * @param message
	 */
	public void onErrorMessage(ErrorMessage message) {
		ddb.msg.client.ErrorMessage reply = new ddb.msg.client.ErrorMessage();
		reply.setException(message.getException());
		
		coordinator.abortTransaction(reply);
	}
	
	public void onTimeout() {
		// empty
	}
}