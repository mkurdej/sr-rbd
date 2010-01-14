/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ddb.communication;

import java.io.DataInputStream;

import java.io.IOException;
import java.net.Socket;

import ddb.Logger;
import ddb.msg.Message;
import ddb.msg.MessageType;

/**
 *
 * @author xeonic
 */
public class TcpWorker implements Runnable
{

    private final static String LOGGING_NAME = "TcpWorker";
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
    	do
    	{
    		try
    		{
    			socket.setSoTimeout(0);

    			DataInputStream in = new DataInputStream(socket.getInputStream());
    			int size = in.readInt();
    			int type = in.readInt();
    			byte[] b = new byte[size];

    			Logger.getInstance().log("Size = " + Integer.toString(size),
    					LOGGING_NAME, Logger.Level.INFO);

    			int left;
    			for(left = size; left > 0; )
    				left -= in.read(b, size - left, left);

    			Message m = Message.Unserialize(MessageType.values()[type], b);
    			
    			DispatcherImpl.getInstance().dispatchMessage(m,
    					socket.getInetAddress().getHostAddress().toString(),
    					socket.getPort());
    		}
    		catch (IOException ex)
    		{
    			Logger.getInstance().log("Socket exception!", LOGGING_NAME,
    					Logger.Level.WARNING);
    			break;
    		}
    	} while(true);
    }
}
