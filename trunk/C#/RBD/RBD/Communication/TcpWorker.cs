//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Text;
//using System.Net;
//using System.Net.Sockets;

//namespace RBD
//{
//    class TcpWorker
//    {
//        const String LOGGING_NAME = "TcpWorker";

        
//        BlockingQueue<Message> storage;
//        TcpClient tcpClient;

//        public TcpWorker(TcpClient newTcpClient, BlockingQueue<Message> newStorage)
//        {
//            tcpClient = newTcpClient;
//            storage = newStorage;
//        }

//        public void run()
//        {
//            IPEndPoint ip = (IPEndPoint)tcpClient.Client.RemoteEndPoint;
//            Logger.getInstance().log("New connection from: " + ip.Address.ToString()
//                                     + ":" + ip.Port.ToString(),
//                                     LOGGING_NAME, Logger.Level.INFO);
//            NetworkStream stream = tcpClient.GetStream();

//            try
//            {
//                while (true)
//                {
//                    Byte[] sizeBuf = new Byte[4];
//                    for (int nread = 0; nread < 4; )
//                        nread += stream.Read(sizeBuf, nread, 4 - nread);
//                    int size = BitConverter.ToInt32(sizeBuf, 0);
//                    size = IPAddress.NetworkToHostOrder(size);

//                    Byte[] typeBuf = new Byte[4];
//                    for (int nread = 0; nread < 4; )
//                        nread += stream.Read(typeBuf, nread, 4 - nread);
//                    int type = BitConverter.ToInt32(typeBuf, 0);
//                    type = IPAddress.NetworkToHostOrder(type);

//                    Logger.getInstance().log("Size = " + size, LOGGING_NAME, Logger.Level.INFO);

//                    Byte[] buffer = new Byte[size];
//                    for (int nread = 0; nread < size; )
//                        nread += stream.Read(buffer, nread, size - nread);

//                    String messageStr = System.Text.Encoding.ASCII.GetString(buffer);
//                    Message m = Message.Unserialize(MessageType.fromInt(type), messageStr, ip.Address);
//                    storage.put(m);

//                    Logger.getInstance().log("Message: [" + message + "]",
//                                             LOGGING_NAME, Logger.Level.INFO);
//                }
//            }
//            catch(System.IO.IOException ex)
//            {
//                Logger.getInstance().log("Socket exception! " + ex.Message, LOGGING_NAME, Logger.Level.WARNING);
//                break;
//            }
//            TcpSender.getInstance().removeNode(ip.Address);
//        }
//    }
//}
