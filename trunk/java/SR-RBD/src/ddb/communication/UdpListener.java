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
import java.net.SocketException;

import ddb.Logger;

/**
 *
 * @author xeonic
 */
public class UdpListener implements Runnable
{

    private static UdpListener instance = new UdpListener();
    public static final int LISTEN_PORT = 1234;
    private final int DATAGRAM_SIZE = 100;
    private final String LOGGING_NAME = "UdpListener";
    
    private UdpListener()
    {
    }

    public static UdpListener getInstance()
    {
        return instance;
    }

    public void run()
    {
        try
        {
            DatagramSocket socket = new DatagramSocket(LISTEN_PORT);
            while(true)
            {
                byte[] buffer = new byte[DATAGRAM_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, DATAGRAM_SIZE);

                socket.receive(packet);
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                DataInputStream dis = new DataInputStream(bais);
                int size = dis.readInt();
                byte[] rest = new byte[DATAGRAM_SIZE];
                dis.read(rest, 4, size);
                String restString = new String(rest).substring(4, size + 4);
                System.out.println("Broadcast from " +
                        packet.getAddress().getHostAddress().toString() +
                        ":" + packet.getPort() + ", size = " + size + " : " + restString);
            }
        }
        catch (SocketException ex)
        {
            Logger.getInstance().log("Could not listen on port " + LISTEN_PORT,
                    LOGGING_NAME, Logger.Level.SEVERE);
        }
        catch (IOException ex)
        {
            Logger.getInstance().log("Error reading packet!",
                    LOGGING_NAME, Logger.Level.INFO);
        }
    }
}
