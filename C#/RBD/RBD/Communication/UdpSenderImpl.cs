// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;

using RBD.Msg;
namespace RBD
{
    class UdpSenderImpl : UdpSender
    {
        const String LOGGING_NAME = "UdpSenderImpl";
        static UdpSenderImpl instance = new UdpSenderImpl();
        protected Socket socket;

        IPEndPoint broadcast;

        private UdpSenderImpl()
        {
            try
            {
                broadcast = new IPEndPoint(IPAddress.Broadcast, Config.UdpPort());
                socket = new Socket(AddressFamily.InterNetwork,
                                    SocketType.Dgram, ProtocolType.Udp);
                socket.Connect(broadcast);
            }
            catch (SocketException ex)
            {
                Logger.getInstance().log("SocketException in constructor: " + ex.Message,
                        LOGGING_NAME,
                        Logger.Level.SEVERE);
            }
        }

        public static UdpSenderImpl getInstance()
        {
            return instance;
        }

        public void sendToAll(Message msg)
        {
            lock (this)
            {
                try
                {
                    byte[] data = msg.Serialize();
                    socket.Send(data);
                    Logger.getInstance().log("Sending: [" + msg.ToString() + "]", LOGGING_NAME, Logger.Level.INFO);
                }
                catch (SocketException ex)
                {
                    Logger.getInstance().log("SocketException: " + ex.Message, LOGGING_NAME, Logger.Level.WARNING);
                }
            }
        }
    }
}
