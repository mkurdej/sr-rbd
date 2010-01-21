// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;

using RBD.Util;
using RBD.Msg;

namespace RBD.Communication
{
    public class TcpListener : Runnable
    {
        private const String LOGGING_NAME = "TcpListener";

        protected BlockingQueue<Message> storage = null;

        protected Socket serverSocket = null;
        protected IPAddress listenHost;
        protected int listenPort;

        public TcpListener(BlockingQueue<Message> queue, IPAddress host, int port)
        {
            storage = queue;
            listenHost = host;
            listenPort = port;
        }

        public void run()
        {
            try
            {
                IPEndPoint ip = new IPEndPoint(listenHost, listenPort);
                serverSocket = new Socket(AddressFamily.InterNetwork,
                                            SocketType.Stream, ProtocolType.Tcp);
                serverSocket.Bind(ip);

                Logger.getInstance().log("Server is listening on port " +
                        listenPort + ".", LOGGING_NAME, Logger.Level.INFO);
            }
            catch (SocketException)
            {
                Logger.getInstance().log("Could not listen on port " + listenPort + "!", LOGGING_NAME, Logger.Level.SEVERE);
                return;
            }

            while (true)
            {
                try
                {
                    Socket newConnection = serverSocket.Accept();
                    TcpSender.getInstance().addNodeBySocket((IPEndPoint)newConnection.RemoteEndPoint,
                        newConnection
                    );

                    TcpWorker worker = new TcpWorker(newConnection, storage);
                    new Thread(new ThreadStart(worker.run)).Start();
                }
                catch (SocketException)
                {
                    Logger.getInstance().log("Could not accept new connection!",
                            LOGGING_NAME, Logger.Level.WARNING);
                }

            }
        }
    }
}