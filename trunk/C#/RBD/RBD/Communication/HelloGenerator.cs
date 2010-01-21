// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Threading;

using RBD.DB;
using RBD.Msg;
using RBD.Util;

namespace RBD.Communication
{

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
    public class HelloGenerator : Runnable
    {
	// configuration
	private const String LOGGING_NAME = "HelloGenerator";
	private const int BROADCAST_INTERVAL_MS = 5000;
	
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
	        	
	        	Thread.Sleep(BROADCAST_INTERVAL_MS);
	        } 
		}
        catch (ThreadInterruptedException) {
        	Logger.getInstance().log("InterruptedException", LOGGING_NAME, Logger.Level.INFO);
		}
    }
}
}