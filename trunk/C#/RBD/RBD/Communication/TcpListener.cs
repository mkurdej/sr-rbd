// +--

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
    //public class TcpListener implements Runnable
    public class TcpListener
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

        static void WorkerThread(object o)
        {
            TcpWorker worker = (TcpWorker)o;
            worker.run();
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
            catch (IOException ex)
            {
                Logger.getInstance().log("Could not listen on port " + listenPort + "!" + " (" + ex.Message + ")", LOGGING_NAME, Logger.Level.SEVERE);
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
                    ParameterizedThreadStart threadStart = new ParameterizedThreadStart(WorkerThread);
                    Thread t = new Thread(threadStart);
                    t.Start(worker);
                }
                catch (IOException ex)
                {
                    Logger.getInstance().log("Could not accept new connection!" + " (" + ex.Message + ")",
                            LOGGING_NAME, Logger.Level.WARNING);
                }

            }
        }
    }
}