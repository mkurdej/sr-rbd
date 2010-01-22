using System;
using System.Collections;
using System.Collections.Generic;
using System.Net;

using RBD.Communication;
using RBD.Msg;
using RBD.DB;
using RBD.Restore.Msg;

namespace RBD.Restore {
    public class RestoreCoordinator : Worker 
    {
	    private static string LOGGING_NAME = "RestoreCoordinator";
	    private static int RESTORE_TIMEOUT = 1000;

        private List<EndRestorationListener> endRestorationListeners = new List<EndRestorationListener>();
	    private IPEndPoint targetNode;

        public RestoreCoordinator(IPEndPoint node)
	    {
		    targetNode = node;
	    }
    	
	    public void addEndRestorationListener(EndRestorationListener listener) {
		    endRestorationListeners.Add(listener);
	    }
    	
	    public void removeEndRestorationListener(EndRestorationListener listener) {
		    endRestorationListeners.Remove(listener);
	    }
    	
	    private void notifyEndRestorationListener() {
		    foreach (EndRestorationListener listener in endRestorationListeners) {
			    listener.onEndRestoration(this);
		    }
	    }

        public IPEndPoint getTargetNode()
	    {
		    return targetNode;
	    }
    	
	    public void Restore()
	    {
		    Message msg;
            Message.MessageType[] incentiveReply = { Message.MessageType.RESTORE_ACK, Message.MessageType.RESTORE_NACK };
    		
		    Logger.getInstance().log(
				    "Starting restoration - sending incentive : " + targetNode.ToString(), 
				    LOGGING_NAME, 
				    Logger.Level.INFO);
    		
    		
		    // send restore incentive
		    TcpSender.getInstance().sendToNode(new RestoreIncentive(), targetNode);
    		
		    // receive reply
		    setTimeout(RESTORE_TIMEOUT);
		    msg = accept(incentiveReply, targetNode);

		    if(msg is RestoreNack)
		    {
			    Logger.getInstance().log(
					    "Restore rejected by node : " + targetNode.ToString(), 
					    LOGGING_NAME, 
					    Logger.Level.INFO);
    			
			    return;
		    }
    		
		    Logger.getInstance().log(
				    "Restore accepted by node, sending tables' versions : " + targetNode.ToString(), 
				    LOGGING_NAME, 
				    Logger.Level.INFO);
    			
		    // TODO: tutaj nie moze byc zadnych transakcji
		    // TODO: lock whole database ( all tables )
		    RestoreTableList rtl = new RestoreTableList(DatabaseStateImpl.getInstance().getTableVersions()); 
		    TcpSender.getInstance().sendToNode(rtl, targetNode);
    		
		    Logger.getInstance().log(
				    "Awaiting ack : " + targetNode.ToString(), 
				    LOGGING_NAME, 
				    Logger.Level.INFO);
    		
		    // receive reply
		    setTimeout(RESTORE_TIMEOUT);
		    msg = accept(Message.MessageType.RESTORE_ACK, targetNode);

		    Logger.getInstance().log(
				    "Got ack, trasfering tables : " + targetNode.ToString(), 
				    LOGGING_NAME, 
				    Logger.Level.INFO);
    		
		    // TODO: release lock
    		
		    // send all tables
		    foreach(TableVersion tv in rtl.getTables())
		    {
			    Logger.getInstance().log(
					    "Transfering table '" + tv.Name + "' version " + tv.Version + " : " + targetNode.ToString(), 
					    LOGGING_NAME, 
					    Logger.Level.INFO);
    			
			    String dump;
			    try {
				    dump = DbConnectorImpl.getInstance().dumpTable(tv.Name);
				    RestoreTable rt = new RestoreTable(tv.Version, tv.Name, dump);
				    TcpSender.getInstance().sendToNode(rt, targetNode);
    			
			    } catch (DumpTableException e) {
				    Logger.getInstance().log(
						    "DumpTableException " + e.Message,
						    LOGGING_NAME, 
						    Logger.Level.WARNING);
    				
				    RestoreTable rt = new RestoreTable(tv.Version, tv.Name, "");
				    TcpSender.getInstance().sendToNode(rt, targetNode);
			    }
		    }	
    		
		    // finished
	    }
    	
	    public void run() {
		    try
		    {
			    // try to restore
			    Restore();
		    }
		    catch(TimeoutException)
		    {
			    Logger.getInstance().log(
					    "Restore timed out",
					    LOGGING_NAME, 
					    Logger.Level.WARNING);
		    } catch (Exception) {
			    Logger.getInstance().log(
					    "InterruptedException",
					    LOGGING_NAME, 
					    Logger.Level.WARNING);
		    }
    		
		    // notify
		    notifyEndRestorationListener();
	    }
    }
}