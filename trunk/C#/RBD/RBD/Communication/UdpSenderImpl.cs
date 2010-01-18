//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Text;
//using System.Net;
//using System.Net.Sockets;

//namespace RBD
//{
//    class UdpSenderImpl : UdpSender
//    {
//        const String LOGGING_NAME = "UdpSender";
//        static UdpSenderImpl instance = new UdpSenderImpl();
//        Socket socket;
//        IPEndPoint broadcast;
//        EndPoint broadcastEP;

//        UdpSenderImpl()
//        {
//            broadcast = new IPEndPoint(IPAddress.Broadcast, UdpListener.LISTEN_PORT);
//            broadcastEP = (EndPoint)broadcast;
//            socket = new Socket(AddressFamily.InterNetwork,
//                                SocketType.Dgram, ProtocolType.Udp);
//            socket.Connect(broadcast);
//        }

//        public static UdpSenderImpl getInstance()
//        {
//            return instance;
//        }

//        public void sendToAll(Message msg)
//        {
//            lock (typeof(UdpSenderImpl))
//            {
//                try
//                {
//                    //TODO - też zmienić na serializację jak w Javie.
//                    String message = msg.toString();

//                    int size = message.Length;
//                    size = IPAddress.HostToNetworkOrder(size);
//                    Byte[] sizeStr = BitConverter.GetBytes(size);
//                    message = message.Insert(0, Encoding.ASCII.GetString(sizeStr, 0, 4));
//                    Byte[] buffer = Encoding.ASCII.GetBytes(message);
//                    socket.Send(buffer);
//                    Logger.getInstance().log("Sending: [" + message + "]", LOGGING_NAME, Logger.Level.INFO);
//                }
//                catch (SocketException e)
//                {
//                    Logger.getInstance().log("Socket exception: " + e.Message, 
//                        LOGGING_NAME, Logger.Level.WARNING);
//                }
//            }
//        }
//    }
//}
