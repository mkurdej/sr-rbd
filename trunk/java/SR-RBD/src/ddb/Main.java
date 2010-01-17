/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb;

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
    	
    	if(Config.Load("./config.xml"))
    	{
    		// launch
        	Logger.getInstance().log("Starting node", LOGGING_NAME, Logger.Level.INFO);
        	Dispatcher dispatcher = new Dispatcher();
        	
        	Logger.getInstance().log("Running dispatcher", LOGGING_NAME, Logger.Level.INFO);
    		
        	try {
        		dispatcher.Run();
    	   	} catch (InterruptedException e) {
    			Logger.getInstance().log("InterruptedException" + e.getMessage(), LOGGING_NAME, Logger.Level.SEVERE);
    		}
    	}
    	else
    	{
    		Logger.getInstance().log("Error while reading config", LOGGING_NAME, Logger.Level.INFO);
    	}
    		
    	Logger.getInstance().log("Closing node", LOGGING_NAME, Logger.Level.INFO);
    }

}
