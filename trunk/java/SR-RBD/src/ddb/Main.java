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
    	Logger.getInstance().log("Starting node", LOGGING_NAME, Logger.Level.INFO);
    	
    	Dispatcher dispatcher = new DispatcherImpl();
    	
    	try 
    	{
        	Logger.getInstance().log("Running dispatcher", LOGGING_NAME, Logger.Level.INFO);
			dispatcher.Run();
	   } 
    	catch (InterruptedException e) 
    	{
			Logger.getInstance().log("InterruptedException" + e.getMessage(), "MAIN", Level.SEVERE);
		}
    	
    	Logger.getInstance().log("Closing node", LOGGING_NAME, Logger.Level.INFO);
    }

}
