package ddb.tpc.cor;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ddb.db.DBconnectorStub;
import ddb.db.DatabaseStateStub;
import ddb.db.communication.TcpSenderStub;
import ddb.msg.client.ConflictMessage;
import ddb.msg.client.ResultsetMessage;
import ddb.msg.client.SuccessMessage;
import ddb.msg.client.TimeoutMessage;
import ddb.tpc.msg.AckPreCommitMessage;
import ddb.tpc.msg.DoCommitMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.NoForCommitMessage;
import ddb.tpc.msg.PreCommitMessage;
import ddb.tpc.msg.TransactionMessage;
import ddb.tpc.msg.YesForCommitMessage;
import junit.framework.TestCase;

public class CoordinatorTests extends TestCase {
	private CoordinatorImpl instance;
	private Set<String> nodeList;
	private DatabaseStateStub databaseState;
	private TcpSenderStub tcpSender;

	protected void setUp() throws Exception {
		super.setUp();

		instance = new CoordinatorImpl();

		nodeList = new HashSet<String>();
		nodeList.add("192.168.0.1");
		nodeList.add("192.168.0.2");
		nodeList.add("192.168.0.3");
		databaseState = new DatabaseStateStub();
		databaseState.setNodeList(nodeList);
		instance.setDatabaseState(databaseState);

		instance.setConnector(new DBconnectorStub());

		tcpSender = new TcpSenderStub();
		instance.setTcpSender(tcpSender);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testTransactionMessage() throws InterruptedException {
		assertNotNull(instance.getDatabaseState());

		TransactionMessage message = new TransactionMessage();
		String queryString = "UPDATE KLIENCI SET NAZWA = 'jasio' WHERE ID = 1024435";
		message.setQueryString(queryString);
		//instance.onNewMessage(message);
		instance.processMessage(message);

		//Thread.sleep(200);
		
		assertEquals(WaitingState.class, instance.getState().getClass());
		assertEquals("KLIENCI", instance.getTableName());
		
	}

	@Test
	public void testSelectRequestMessage() throws InterruptedException {
		assertNotNull(instance.getDatabaseState());

		TransactionMessage message = new TransactionMessage();
		String queryString = "SELECT * FROM KLIENCI";
		message.setQueryString(queryString);
		//instance.onNewMessage(message);
		instance.processMessage(message);

		//Thread.sleep(200);
		
		assertEquals(CommitState.class, instance.getState().getClass());
		assertEquals("KLIENCI", instance.getTableName());
		assertEquals(ResultsetMessage.class, tcpSender.getLastMessage()
				.getClass());
	}
	
	@Test
	public void testYesForCommitMessage() throws InterruptedException {
		TransactionMessage message = new TransactionMessage();
		String queryString = "UPDATE KLIENCI SET NAZWA = 'jasio' WHERE ID = 1024435";
		message.setQueryString(queryString);
		//instance.onNewMessage(message);
		instance.processMessage(message);

		//Thread.sleep(200);
		
		for (String node : databaseState.getNodes()) {
			YesForCommitMessage yesForCommitMessage = new YesForCommitMessage();
			yesForCommitMessage.setSenderAddress(node);
			assertEquals(WaitingState.class, instance.getState().getClass());
			//instance.onNewMessage(yesForCommitMessage);
			instance.processMessage(yesForCommitMessage);
			
			//Thread.sleep(200);
		}

		assertEquals(PreparedState.class, instance.getState().getClass());
		assertEquals(PreCommitMessage.class, tcpSender.getLastMessage()
				.getClass());
	}

	@Test
	public void testNoForCommitMessage() {
		TransactionMessage message = new TransactionMessage();
		String queryString = "UPDATE KLIENCI SET NAZWA = 'jasio' WHERE ID = 1024435";
		message.setQueryString(queryString);
		//instance.onNewMessage(message);
		instance.processMessage(message);

		NoForCommitMessage noForCommitMessage = new NoForCommitMessage();
		noForCommitMessage.setSenderAddress("192.168.0.1");
		assertEquals(WaitingState.class, instance.getState().getClass());
		//instance.onNewMessage(noForCommitMessage);
		instance.processMessage(noForCommitMessage);

		assertEquals(AbortState.class, instance.getState().getClass());
		assertEquals(ConflictMessage.class, tcpSender.getLastMessage()
				.getClass());
	}

	@Test
	public void testAckPreCommitMessage() {
		TransactionMessage message = new TransactionMessage();
		String queryString = "UPDATE KLIENCI SET NAZWA = 'jasio' WHERE ID = 1024435";
		message.setQueryString(queryString);
		//instance.onNewMessage(message);
		instance.processMessage(message);

		for (String node : databaseState.getNodes()) {
			YesForCommitMessage yesForCommitMessage = new YesForCommitMessage();
			yesForCommitMessage.setSenderAddress(node);
			assertEquals(WaitingState.class, instance.getState().getClass());
			//instance.onNewMessage(yesForCommitMessage);
			instance.processMessage(yesForCommitMessage);
		}
		
		for (String node : databaseState.getNodes()) {
			AckPreCommitMessage ackPreCommitMessage = new AckPreCommitMessage();
			ackPreCommitMessage.setSenderAddress(node);
			assertEquals(PreparedState.class, instance.getState().getClass());
			//instance.onNewMessage(ackPreCommitMessage);
			instance.processMessage(ackPreCommitMessage);
		}
		
		assertEquals(CommitState.class, instance.getState().getClass());
		assertEquals(DoCommitMessage.class, tcpSender.getLastMessage()
				.getClass());
	}

	@Test
	public void testHaveCommittedMessage() {
		TransactionMessage message = new TransactionMessage();
		String queryString = "UPDATE KLIENCI SET NAZWA = 'jasio' WHERE ID = 1024435";
		message.setQueryString(queryString);
		//instance.onNewMessage(message);
		instance.processMessage(message);

		for (String node : databaseState.getNodes()) {
			YesForCommitMessage yesForCommitMessage = new YesForCommitMessage();
			yesForCommitMessage.setSenderAddress(node);
			//instance.onNewMessage(yesForCommitMessage);
			instance.processMessage(yesForCommitMessage);
		}
		
		for (String node : databaseState.getNodes()) {
			AckPreCommitMessage ackPreCommitMessage = new AckPreCommitMessage();
			ackPreCommitMessage.setSenderAddress(node);
			//instance.onNewMessage(ackPreCommitMessage);
			instance.processMessage(ackPreCommitMessage);
		}
		
		for (String node : databaseState.getNodes()) {
			HaveCommittedMessage haveCommittedMessage = new HaveCommittedMessage();
			haveCommittedMessage.setSenderAddress(node);
			assertEquals(CommitState.class, instance.getState().getClass());
			//instance.onNewMessage(haveCommittedMessage);
			instance.processMessage(haveCommittedMessage);
		}
		
		assertEquals(CommitState.class, instance.getState().getClass());
		assertEquals(SuccessMessage.class, tcpSender.getLastMessage()
				.getClass());
	}
	
	@Test
	public void testTimeoutOnInitState() {
		instance.onTimeout();

		assertEquals(AbortState.class, instance.getState().getClass());
		assertEquals(TimeoutMessage.class, tcpSender.getLastMessage()
				.getClass());
	}

	@Test
	public void testTimeoutOnWaitingState() {
		TransactionMessage message = new TransactionMessage();
		String queryString = "UPDATE KLIENCI SET NAZWA = 'jasio' WHERE ID = 1024435";
		message.setQueryString(queryString);
		//instance.onNewMessage(message);
		instance.processMessage(message);

		instance.onTimeout();

		assertEquals(AbortState.class, instance.getState().getClass());
		assertEquals(TimeoutMessage.class, tcpSender.getLastMessage()
				.getClass());
	}
	
	@Test
	public void testTimeoutOnPreparedState() {
		TransactionMessage message = new TransactionMessage();
		String queryString = "UPDATE KLIENCI SET NAZWA = 'jasio' WHERE ID = 1024435";
		message.setQueryString(queryString);
		//instance.onNewMessage(message);
		instance.processMessage(message);

		for (String node : databaseState.getNodes()) {
			YesForCommitMessage yesForCommitMessage = new YesForCommitMessage();
			yesForCommitMessage.setSenderAddress(node);
			//instance.onNewMessage(yesForCommitMessage);
			instance.processMessage(yesForCommitMessage);
		}
		
		instance.onTimeout();

		assertEquals(AbortState.class, instance.getState().getClass());
		assertEquals(TimeoutMessage.class, tcpSender.getLastMessage()
				.getClass());
	}
	
	@Test
	public void testTimeoutOnCommitState() {
		TransactionMessage message = new TransactionMessage();
		String queryString = "UPDATE KLIENCI SET NAZWA = 'jasio' WHERE ID = 1024435";
		message.setQueryString(queryString);
		//instance.onNewMessage(message);
		instance.processMessage(message);

		for (String node : databaseState.getNodes()) {
			YesForCommitMessage yesForCommitMessage = new YesForCommitMessage();
			yesForCommitMessage.setSenderAddress(node);
			//instance.onNewMessage(yesForCommitMessage);
			instance.processMessage(yesForCommitMessage);
		}
		
		for (String node : databaseState.getNodes()) {
			AckPreCommitMessage ackPreCommitMessage = new AckPreCommitMessage();
			ackPreCommitMessage.setSenderAddress(node);
			//instance.onNewMessage(ackPreCommitMessage);
			instance.processMessage(ackPreCommitMessage);
		}
		
		instance.onTimeout();

		assertEquals(AbortState.class, instance.getState().getClass());
		assertEquals(TimeoutMessage.class, tcpSender.getLastMessage()
				.getClass());
	}

}
