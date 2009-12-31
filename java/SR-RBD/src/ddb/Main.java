/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb;

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
        new Thread(TcpListener.getInstance()).start();
        new Thread(UdpListener.getInstance()).start();
    }

}
