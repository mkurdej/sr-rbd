/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb.communication;

import java.io.ByteArrayOutputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ddb.msg.Message;

/**
 *
 * @author xeonic
 */
public class UdpSenderImpl implements UdpSender
{
    private static UdpSenderImpl instance = new UdpSenderImpl();

    private UdpSenderImpl()
    {

    }

    public static UdpSenderImpl getInstance()
    {
        return instance;
    }

    public synchronized void sendToAll(Message msg)
    {
    	String s = msg.toString();
        try
        {
            DatagramSocket socket = new DatagramSocket();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(4);
            DataOutputStream das = new DataOutputStream(baos);
            das.writeInt(s.length());

            StringBuilder toSend = new StringBuilder();
            toSend.append(baos.toString());
            toSend.append(s);

            DatagramPacket packet = new DatagramPacket(toSend.toString().getBytes(),
                    toSend.length());
            packet.setAddress(InetAddress.getByName("255.255.255.255"));
            packet.setPort(UdpListener.LISTEN_PORT);
            socket.send(packet);
        }
        catch (SocketException ex)
        {
            Logger.getLogger(UdpSenderImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(UdpSenderImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
