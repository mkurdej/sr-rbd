/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb.communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import ddb.Logger;
import ddb.msg.Message;

/**
 *
 * @author xeonic
 */
public class TcpListener implements Runnable
{
	private final static String LOGGING_NAME = "TcpListener";

	protected BlockingQueue<Message> storage = null;
	
	protected ServerSocket serverSocket = null;
	protected InetAddress listenHost;
	protected int listenPort;

    public TcpListener(BlockingQueue<Message> queue, InetAddress host, int port)
    {
    	storage = queue;
    	listenHost = host;
    	listenPort = port;
    }

    public void run()
    {
        try
        {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(listenHost, listenPort));
            Logger.getInstance().log("Server is listening on port " +
            		listenPort + ".", LOGGING_NAME, Logger.Level.INFO);
        }
        catch (IOException ex)
        {
            Logger.getInstance().log("Could not listen on port " +
                    Integer.toString(listenPort) + "!", LOGGING_NAME,
                    Logger.Level.SEVERE);
            return;
        }

        while(true)
        {
            try
            {
                Socket newConnection = serverSocket.accept();
                TcpSender.getInstance().addNodeBySocket(
            		(InetSocketAddress)newConnection.getRemoteSocketAddress(),
            		newConnection
        		);
                
                TcpWorker worker = new TcpWorker(newConnection, storage);
                new Thread(worker).start();
            }
            catch (IOException ex)
            {
                Logger.getInstance().log("Could not accept new connection!",
                        LOGGING_NAME, Logger.Level.WARNING);
            }

        }
    }
}
