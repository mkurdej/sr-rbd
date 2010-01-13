/**
 * 
 */
package ddb.restore;

import ddb.Logger;
import ddb.communication.UdpSenderImpl;
import ddb.msg.HelloMessage;


/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class HelloGenerator implements Runnable {
	
	private final static HelloGenerator instance = new HelloGenerator();
	private final static String LOGGING_NAME = "HelloGenerator";
	private final static int BROADCAST_INTERVAL_MS = 200;
	
	public void run()
    {
		Logger.getInstance().log("Running", LOGGING_NAME, Logger.Level.INFO);
		
		// TODO: powinien byc uzyty interfejs UdpSender, ale nie ma on odpowiednich metod
		
		UdpSenderImpl sender = UdpSenderImpl.getInstance();
		
		try {
			// broadcast hello's
	        while(true)
	        {
	        	Thread.sleep(BROADCAST_INTERVAL_MS);
	        	// TODO: przekazac do hello message numer wersji bazy
	        	sender.sendToAll(new HelloMessage());
	        } 
		}
        catch (InterruptedException e) {
        	Logger.getInstance().log("Terminating", LOGGING_NAME, Logger.Level.INFO);
		}
    }
	
	public static HelloGenerator getInstance()
    {
        return instance;
    }
}