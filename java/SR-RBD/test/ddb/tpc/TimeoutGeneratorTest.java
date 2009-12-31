package ddb.tpc;


import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimeoutGeneratorTest extends TestCase {
	static public boolean timeout;
	
	@Before
	public void setUp() throws Exception {
		timeout = false;
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testTimer() throws InterruptedException {
		TimeoutGenerator generator = new TimeoutGenerator();
		generator.startTimer(100);
		
		generator.setTimeoutListener(new TimeoutListener() {
			@Override
			public void onTimeout() {
				timeout = true;
			}
			
		});
		Thread.sleep(200);
		assertTrue(timeout);
	}

	@Test
	public void testStopTimer() throws InterruptedException {
		TimeoutGenerator generator = new TimeoutGenerator();
		generator.startTimer(100);
		
		generator.setTimeoutListener(new TimeoutListener() {
			@Override
			public void onTimeout() {
				timeout = true;
			}
			
		});
		Thread.sleep(10);
		generator.stopTimer();
		Thread.sleep(200);
		assertFalse(timeout);
		
	}
	
}
