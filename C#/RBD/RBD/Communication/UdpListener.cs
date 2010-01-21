// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.IO;
using System.Threading;

using RBD.Util;
using RBD.Msg;

namespace RBD
{
    public class UdpListener : Runnable
    {
        private const int DATAGRAM_SIZE = 500;
        private const String LOGGING_NAME = "UdpListener";
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
                IPEndPoint ip = new IPEndPoint(IPAddress.Any, listenPort);
                Socket socket = new Socket(AddressFamily.InterNetwork,
                                SocketType.Dgram, ProtocolType.Udp);
                socket.Bind(ip);
                Logger.getInstance().log("Listening on port " + listenPort,
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
            catch (SocketException)
            {
                Logger.getInstance().log("Could not listen on port " + listenPort,
                        LOGGING_NAME, Logger.Level.SEVERE);
            }
            catch (IOException)
            {
                Logger.getInstance().log("Error reading packet!",
                        LOGGING_NAME, Logger.Level.INFO);
            }
            catch (InvalidMessageTypeException e)
            {
                Logger.getInstance().log("InvalidMessageTypeException " + e.Message,
                        LOGGING_NAME,
                        Logger.Level.WARNING);
            }
            catch (ThreadInterruptedException e)
            {
                Logger.getInstance().log("InterruptedException " + e.Message,
                        LOGGING_NAME,
                        Logger.Level.WARNING);
            }
        }
    }
}
