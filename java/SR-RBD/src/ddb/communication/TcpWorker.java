/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ddb.communication;

import java.io.DataInputStream;

import java.io.IOException;
import java.net.Socket;

import ddb.Logger;

/**
 *
 * @author xeonic
 */
public class TcpWorker implements Runnable
{

    private final static String LOGGING_NAME = "TcpWorker";
    private final static int READ_TIMEOUT = 2000;
    Socket socket;

    public TcpWorker(Socket newSocket)
    {
        socket = newSocket;
    }

    public void run()
    {
        Logger.getInstance().log("New connection from: "
                + socket.getInetAddress().getHostAddress().toString() + ":"
                + socket.getPort()
                , LOGGING_NAME, Logger.Level.INFO);
        try
        {
            socket.setSoTimeout(READ_TIMEOUT);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            int size = in.readInt();
            byte[] b = new byte[size];

            Logger.getInstance().log("Size = " + Integer.toString(size),
                    LOGGING_NAME, Logger.Level.INFO);

            int left;
            for(left = size; left > 0; )
                left -= in.read(b, size - left, left);

            DispatcherImpl.getInstance().dispatchMessage(new String(b),
            		socket.getInetAddress().getHostAddress().toString(),
            		socket.getPort());
        }
        catch (IOException ex)
        {
            Logger.getInstance().log("Error reading from socket!", LOGGING_NAME,
                    Logger.Level.WARNING);
        }
        try
        {
            socket.close();
        }
        catch (IOException ex)
        {
            Logger.getInstance().log("Error closing socket!", LOGGING_NAME,
            		Logger.Level.WARNING);
        }
    }
}
