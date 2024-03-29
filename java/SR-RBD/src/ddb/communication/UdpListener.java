/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb.communication;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

import ddb.Logger;
import ddb.msg.InvalidMessageTypeException;
import ddb.msg.Message;
import ddb.msg.MessageType;

/**
 *
 * @author xeonic
 */
public class UdpListener implements Runnable
{
    private static final int DATAGRAM_SIZE = 500;
    private static final String LOGGING_NAME = "UdpListener";
    protected int listenPort;
    protected BlockingQueue<Message> storage = null;
    
    public UdpListener(BlockingQueue<Message> queue, int port)
    {
    	storage = queue;
    	listenPort = port;
    }

    public void run()
    {
        try
        {
            DatagramSocket socket = new DatagramSocket(listenPort);
            while(true)
            {
                byte[] buffer = new byte[DATAGRAM_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, DATAGRAM_SIZE);

                socket.receive(packet);
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                DataInputStream dis = new DataInputStream(bais);
                
                int size = dis.readInt();
                int type = dis.readInt(); 
                
                byte[] data = new byte[size];
                System.arraycopy(buffer, 8, data, 0, size);
                          
                Message m = Message.Unserialize(MessageType.fromInt(type), data, (InetSocketAddress)packet.getSocketAddress());
                storage.put(m);
            }
        }
        catch (SocketException ex)
        {
            Logger.getInstance().log("Could not listen on port " + listenPort,
                    LOGGING_NAME, Logger.Level.SEVERE);
        }
        catch (IOException ex)
        {
            Logger.getInstance().log("Error reading packet!",
                    LOGGING_NAME, Logger.Level.INFO);
        } catch (InvalidMessageTypeException e) {
        	Logger.getInstance().log("InvalidMessageTypeException " + e.getMessage(), 
					LOGGING_NAME,
					Logger.Level.WARNING);
		} catch (InterruptedException e) {
			Logger.getInstance().log("InterruptedException " + e.getMessage(), 
					LOGGING_NAME,
					Logger.Level.WARNING);
		}
    }
}
