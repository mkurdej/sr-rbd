package ddb.restore;

import java.util.HashSet;
import java.util.Set;

import ddb.Logger;
import ddb.Logger.Level;
import ddb.communication.TcpSender;
import ddb.db.DbConnector;
import ddb.msg.Message;
import ddb.tpc.EndTransactionListener;
import ddb.tpc.MessageQueue;
import ddb.tpc.MessageRecipient;
import ddb.tpc.TimeoutGenerator;
import ddb.tpc.TimeoutListener;

public abstract class RestoreParticipant implements MessageRecipient, TimeoutListener {
	public static final int TIMEOUT = 5000;
	
	private MessageQueue messageQueue;
	private TimeoutGenerator timeoutGenerator;
	protected volatile boolean stopped = false;
	private Set<EndTransactionListener> endTransactionListeners;
	protected TcpSender tcpSender; // TODO: wtf?
	protected DbConnector connector;
	
	public RestoreParticipant() {
		this.endTransactionListeners = new HashSet<EndTransactionListener>();
		this.timeoutGenerator = new TimeoutGenerator();
		this.messageQueue = new MessageQueue();
		// startThread();
	}
	
	synchronized public void processMessage(Message message) {
		/*
		 * try { this.messageQueue.putMessage(message); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * Logger.getInstance().log(e.getMessage(), "TPC", Level.SEVERE); }
		 */
		this.onNewMessage(message);
	}
	
	synchronized private void waitForMessage() {
		Logger.getInstance().log("waitForMessage " + Thread.currentThread(),
				"TPC", Logger.Level.INFO);

		try {
			Message msg = this.messageQueue.getMessage();
			onNewMessage(msg);
		} catch (InterruptedException e) {
			Logger.getInstance().log(e.getMessage(), "RestoreParticipant", Logger.Level.SEVERE);
		}

	}
	
	protected void startTimer(long miliseconds) {
		this.timeoutGenerator.startTimer(miliseconds);
	}
	
	protected void stopTimer() {
		this.timeoutGenerator.stopTimer();
	}
	
	protected abstract void onNewMessage(Message message);
	
	public final void endTransaction() {
		stopThread();
		cleanupTransaction();
		notifyEndTransactionListeners();
	}
	
	protected abstract void cleanupTransaction();
	
	private void startThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Logger.getInstance().log(
						"startThread " + Thread.currentThread(), "RestoreParticipant",
						Level.INFO);
				while (!stopped) {
					waitForMessage();
				}
			}
		}, "RestoreParticipant_THREAD").start();
	}
	
	private void stopThread() {
		stopped = true;
	}
	
	public void addEndTransactionListener(EndTransactionListener listener) {
		endTransactionListeners.add(listener);
	}
	
	public void removeEndTransactionListener(EndTransactionListener listener) {
		endTransactionListeners.remove(listener);
	}
	
	private void notifyEndTransactionListeners() {
		for (EndTransactionListener listener : this.endTransactionListeners) {
			listener.onEndTransaction(this);
		}
	}
}
