/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb.communication;

import java.io.IOException;
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
    public final static int LISTEN_PORT = 1501;

	protected BlockingQueue<Message> storage = null;
	protected ServerSocket serverSocket = null;

    public TcpListener(BlockingQueue<Message> queue)
    {
    	storage = queue;
    }

    public void run()
    {
        try
        {
            serverSocket = new ServerSocket(LISTEN_PORT);
            Logger.getInstance().log("Server is listening on port " +
                    LISTEN_PORT + ".", LOGGING_NAME, Logger.Level.INFO);
        }
        catch (IOException ex)
        {
            Logger.getInstance().log("Could not listen on port " +
                    Integer.toString(LISTEN_PORT) + "!", LOGGING_NAME,
                    Logger.Level.SEVERE);
            return;
        }

        while(true)
        {
            try
            {
                Socket newConnection = serverSocket.accept();
                TcpWorker worker = new TcpWorker(newConnection, storage);
                new Thread(worker).start();
            }
            catch (IOException ex)
            {
                Logger.getInstance().log("Could not accept new connection!",
                        LOGGING_NAME, Logger.Level.SEVERE);
            }

        }
    }
}
