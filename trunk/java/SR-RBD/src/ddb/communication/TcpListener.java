/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ddb.Logger;

/**
 *
 * @author xeonic
 */
public class TcpListener implements Runnable
{
    private final static String LOGGING_NAME = "TcpListener";
    public final static int LISTEN_PORT = 1234;
    private final static TcpListener instance = new TcpListener();

    ServerSocket serverSocket = null;

    private TcpListener()
    {
    }

    public static TcpListener getInstance()
    {
        return instance;
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
                TcpWorker worker = new TcpWorker(newConnection);
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
