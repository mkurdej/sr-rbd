using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;

namespace RBD
{
    class UdpListener
    {
        public const int LISTEN_PORT = 1502;
        public const int DATAGRAM_SIZE = 100;
        const String LOGGING_NAME = "UdpListener";
        static UdpListener instance = new UdpListener();
        
        UdpListener()
        {

        }

        public static UdpListener getInstance()
        {
            return instance;
        }

        public void listen()
        {
            IPEndPoint ip = new IPEndPoint(IPAddress.Any, LISTEN_PORT);

            Socket socket = new Socket(AddressFamily.InterNetwork,
                            SocketType.Dgram, ProtocolType.Udp);

            socket.Bind(ip);

            Logger.getInstance().log("Listening on port " + LISTEN_PORT,
                                     LOGGING_NAME, Logger.Level.INFO);

            while (true)
            {
                IPEndPoint sender = new IPEndPoint(IPAddress.Any, 0);
                EndPoint senderEP = (EndPoint)(sender);

                Byte[] buffer = new Byte[DATAGRAM_SIZE];
                socket.ReceiveFrom(buffer, ref senderEP);

                int size = BitConverter.ToInt32(buffer, 0);
                size = IPAddress.NetworkToHostOrder(size);
                Byte[] buffer2 = new Byte[DATAGRAM_SIZE];
                for (int c1 = 4; c1 < DATAGRAM_SIZE; c1++)
                    buffer2[c1 - 4] = buffer[c1];


                String Message = Encoding.ASCII.GetString(buffer2, 0, size);

                Logger.getInstance().log("Message from " + senderEP.ToString() + 
                                         ": [" + Message + "](" + size + ")", 
                                         LOGGING_NAME, Logger.Level.INFO);
            }
        }
    }
}
