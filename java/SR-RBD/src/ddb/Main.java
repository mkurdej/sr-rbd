/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb;

import java.util.Locale;

import ddb.Logger.Level;
import ddb.communication.HelloGenerator;
import ddb.communication.TcpListener;
import ddb.communication.UdpListener;

/**
 *
 * @author xeonic
 */
public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
    	Dispatcher dispatcher = new DispatcherImpl();
    	
    	try 
    	{
			dispatcher.Run();
		} 
    	catch (InterruptedException e) 
    	{
			Logger.getInstance().log("InterruptedException" + e.getMessage(), "MAIN", Level.SEVERE);
		}
    }

}
