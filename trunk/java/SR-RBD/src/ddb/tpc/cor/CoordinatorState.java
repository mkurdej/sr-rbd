/**
 * 
 */
package ddb.tpc.cor;

import java.net.InetAddress;

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
	public void onYesForCommit(InetAddress node) {
		Logger.getInstance().log("Nie oczekiwano wiadomosci: YesForCommit", LOGGER_NAME, Level.WARNING);
	}

	/** 
	 * @param node
	 */
	public void onNoForCommit(InetAddress node) {
		Logger.getInstance().log("Nie oczekiwano wiadomosci: NoForCommit", LOGGER_NAME, Level.WARNING);
	}

	/** 
	 * @param node
	 */
	public void onAckPreCommit(InetAddress node)  {
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
		Logger.getInstance().log("Nie oczekiwano wiadomosci: ErrorMessage", LOGGER_NAME, Level.WARNING);
	}
	
	public void onTimeout() {
		// empty
	}
}