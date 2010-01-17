/**
 * 
 */
package ddb.communication;

import ddb.Logger;
import ddb.msg.HelloMessage;
import ddb.db.DatabaseStateImpl;


/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class HelloGenerator implements Runnable {
	// configuration
	private final static String LOGGING_NAME = "HelloGenerator";
	private final static int BROADCAST_INTERVAL_MS = 5000;
	
	// data
	private int listeningPort;
	
	// construction
	public HelloGenerator(int port)
	{
		listeningPort = port;
	}
	
	// methods
	public void run()
    {
		Logger.getInstance().log("Running", LOGGING_NAME, Logger.Level.INFO);

		UdpSenderImpl sender = UdpSenderImpl.getInstance();
		
		try {
			// broadcast hello's
	        while(true)
	        {
	        	HelloMessage hm = new HelloMessage(DatabaseStateImpl.getInstance().getTableVersions(), listeningPort);
	        	sender.sendToAll(hm);
	        	
	        	Thread.sleep(BROADCAST_INTERVAL_MS);
	        } 
		}
        catch (InterruptedException e) {
        	Logger.getInstance().log("InterruptedException", LOGGING_NAME, Logger.Level.INFO);
		}
    }
}