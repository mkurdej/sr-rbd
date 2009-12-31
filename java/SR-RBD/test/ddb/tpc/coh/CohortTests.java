package ddb.tpc.coh;



import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ddb.db.DBconnectorFailureStub;
import ddb.db.DBconnectorStub;
import ddb.db.DatabaseStateStub;
import ddb.db.TableLockedException;
import ddb.db.communication.TcpSenderStub;
import ddb.tpc.msg.AckPreCommitMessage;
import ddb.tpc.msg.CanCommitMessage;
import ddb.tpc.msg.DoCommitMessage;
import ddb.tpc.msg.ErrorMessage;
import ddb.tpc.msg.HaveCommittedMessage;
import ddb.tpc.msg.NoForCommitMessage;
import ddb.tpc.msg.PreCommitMessage;
import ddb.tpc.msg.YesForCommitMessage;

public class CohortTests extends TestCase {
	private CohordImpl instance;
	private DatabaseStateStub databaseState;
	private TcpSenderStub tcpSender;
	private String coordinatorAddress;
	
	@Before
	public void setUp() throws Exception {
		coordinatorAddress = "192.168.0.167";
		
		instance = new CohordImpl();
		
		tcpSender = new TcpSenderStub();
		instance.setTcpSender(tcpSender);
		
		instance.setCoordinatorAddress(coordinatorAddress);
		
		instance.setConnector(new DBconnectorStub());
		
		databaseState = new DatabaseStateStub();
		instance.setDatabaseState(databaseState);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCanCommitSuccess() {
		CanCommitMessage message = new CanCommitMessage();
		message.setQueryString("UPDATE KLIENCI SET NAZWA = 'st' WHERE ID = 1");
		message.setTableName("KLIENCI");
		
		assertFalse(databaseState.isLocked("KLIENCI"));
		
		instance.onNewMessage(message);
		
		assertEquals(PreparedState.class, instance.getState().getClass());
		assertEquals(YesForCommitMessage.class, tcpSender.getLastMessage().getClass());
		assertEquals(coordinatorAddress, tcpSender.getLastAddress());
		assertEquals("KLIENCI", instance.getTableName());	
		assertTrue(databaseState.isLocked("KLIENCI"));
	}
	
	@Test
	public void testCanCommitFailure() throws TableLockedException {
		CanCommitMessage message = new CanCommitMessage();
		message.setQueryString("UPDATE KLIENCI SET NAZWA = 'st' WHERE ID = 1");
		message.setTableName("KLIENCI");
		
		databaseState.lockTable("KLIENCI");
		
		instance.onNewMessage(message);
		
		assertEquals(AbortState.class, instance.getState().getClass());
		assertEquals(NoForCommitMessage.class, tcpSender.getLastMessage().getClass());
	}
	
	@Test
	public void testPreCommit() {
		CanCommitMessage message = new CanCommitMessage();
		message.setQueryString("UPDATE KLIENCI SET NAZWA = 'st' WHERE ID = 1");
		message.setTableName("KLIENCI");
		
		instance.onNewMessage(message);
		
		PreCommitMessage preCommitMessage = new PreCommitMessage();
		instance.onNewMessage(preCommitMessage);
		
		assertEquals(WaitingDoCommit.class, instance.getState().getClass());
		assertEquals(AckPreCommitMessage.class, tcpSender.getLastMessage().getClass());
	}
	
	@Test
	public void testDoCommitSuccess() {
		CanCommitMessage message = new CanCommitMessage();
		message.setQueryString("UPDATE KLIENCI SET NAZWA = 'st' WHERE ID = 1");
		message.setTableName("KLIENCI");
		instance.onNewMessage(message);
		PreCommitMessage preCommitMessage = new PreCommitMessage();
		instance.onNewMessage(preCommitMessage);
		DoCommitMessage doCommitMessage = new DoCommitMessage();
		instance.onNewMessage(doCommitMessage);
		
		assertEquals(CommittedState.class, instance.getState().getClass());
		assertEquals(HaveCommittedMessage.class, tcpSender.getLastMessage().getClass());
	}
	
	@Test
	public void testDoCommitFailure() {
		instance.setConnector(new DBconnectorFailureStub());
		
		CanCommitMessage message = new CanCommitMessage();
		message.setQueryString("UPDATE KLIENCI SET NAZWA = 'st' WHERE ID = 1");
		message.setTableName("KLIENCI");
		instance.onNewMessage(message);
		PreCommitMessage preCommitMessage = new PreCommitMessage();
		instance.onNewMessage(preCommitMessage);
		DoCommitMessage doCommitMessage = new DoCommitMessage();
		instance.onNewMessage(doCommitMessage);
		
		assertEquals(AbortState.class, instance.getState().getClass());
		assertEquals(ErrorMessage.class, tcpSender.getLastMessage().getClass());
	}
	
	@Test
	public void testTimeoutOnInitState() {
		instance.onTimeout();
		
		assertEquals(AbortState.class, instance.getState().getClass());
	}

	@Test
	public void testTimeoutOnPreparedState() {
		CanCommitMessage message = new CanCommitMessage();
		instance.onNewMessage(message);
		
		instance.onTimeout();
		
		assertEquals(AbortState.class, instance.getState().getClass());
	}
	
	@Test
	public void testTimeoutOnWaitingDoCommitState() {
		CanCommitMessage message = new CanCommitMessage();
		instance.onNewMessage(message);
		
		instance.onTimeout();
		
		assertEquals(AbortState.class, instance.getState().getClass());
	}
	
}
