/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb;

import ddb.Logger.Level;

/**
 *
 * @author xeonic
 */
public class Main
{
	private final static String LOGGING_NAME = "Main";
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
    	int port;
    	
    	// parse args
    	if(args.length != 1)
    	{
    		Logger.getInstance().log("Invalid arguments: missing port number", LOGGING_NAME, Logger.Level.SEVERE);
    		return;
    	}
    	
    	try
    	{
    		port = Integer.parseInt(args[0]);
    	}
    	catch(NumberFormatException ex)
    	{
    		Logger.getInstance().log("Invalid arguments: invalid port number", LOGGING_NAME, Logger.Level.SEVERE);
    		return;
    	}
    		
    	// launch
    	Logger.getInstance().log("Starting node", LOGGING_NAME, Logger.Level.INFO);
    	Dispatcher dispatcher = new Dispatcher(port);
    	
    	Logger.getInstance().log("Running dispatcher", LOGGING_NAME, Logger.Level.INFO);
		
    	try {
    		dispatcher.Run();
	   	} catch (InterruptedException e) {
			Logger.getInstance().log("InterruptedException" + e.getMessage(), LOGGING_NAME, Level.SEVERE);
		}
		
    	Logger.getInstance().log("Closing node", LOGGING_NAME, Logger.Level.INFO);
    }

}
