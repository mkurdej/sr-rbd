/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb;

import java.util.Locale;

import ddb.communication.TcpListener;
import ddb.communication.UdpListener;
import ddb.restore.HelloGenerator;

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
    	// set locale as in all nodes
    	Locale.setDefault(new Locale("en", "US"));
    	
        new Thread(TcpListener.getInstance()).start();
        new Thread(UdpListener.getInstance()).start();
        new Thread(HelloGenerator.getInstance()).start();
    }

}
