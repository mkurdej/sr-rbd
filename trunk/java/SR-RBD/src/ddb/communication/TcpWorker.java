/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ddb.communication;

import java.io.DataInputStream;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import ddb.Logger;
import ddb.msg.Message;
import ddb.msg.MessageType;
import ddb.msg.InvalidMessageTypeException;

/**
 *
 * @author xeonic
 */
public class TcpWorker implements Runnable
{
    private final static String LOGGING_NAME = "TcpWorker";
    
    protected BlockingQueue<Message> storage;
    protected Socket socket;
    
    public TcpWorker(Socket newSocket, BlockingQueue<Message> queue)
    {
    	storage = queue;
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

    			Message m = Message.Unserialize(MessageType.fromInt(type), b);
    			
    			storage.put(m);
    		}
    		catch (IOException ex)
    		{
    			Logger.getInstance().log("Socket exception! " + ex.getMessage(), 
    					LOGGING_NAME,
    					Logger.Level.WARNING);
    			break;
    		} 
    		catch (InterruptedException ex) 
    		{
    			Logger.getInstance().log("InterruptedException " + ex.getMessage(), 
    					LOGGING_NAME,
    					Logger.Level.WARNING);
				break;
			} catch (InvalidMessageTypeException ex) {
				Logger.getInstance().log("InvalidMessageTypeException " + ex.getMessage(), 
    					LOGGING_NAME,
    					Logger.Level.WARNING);
				break;
			}
    		
    	} while(true);
    }
}
