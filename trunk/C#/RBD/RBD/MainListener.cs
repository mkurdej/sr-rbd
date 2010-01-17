using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Threading;

namespace RBD
{
    class MainListener
    {
        const String LOGGING_NAME = "TcpListener";
        public const Int32 LISTEN_PORT = 1501;

        BlockingQueue<Message> storage = null;
        int listenPort;


        public MainListener(BlockingQueue<Message> queue, int port)
        {
            storage = queue;
            listenPort = port;
        }

        public void listen()
        {
            try
            {
                IPAddress address = IPAddress.Parse("0.0.0.0");
                TcpListener server = new TcpListener(address, listenPort);
                server.Start();

                Logger.getInstance().log("Server is listening on port " + listenPort + ".",
                                         LOGGING_NAME, Logger.Level.INFO);

                while (true)
                {
                    TcpClient client = server.AcceptTcpClient();
                    TcpWorker worker = new TcpWorker(client);
                    IPEndPoint ip = (IPEndPoint)tcpClient.Client.RemoteEndPoint;
                    TcpSender.getInstance().addNodeBySocket(ip.Address, client.Client);
                    Thread workerThread = new Thread(worker.run);
                    workerThread.Start();
                }
            }
            catch (SocketException e)
            {
                Logger.getInstance().log("Could not listen on port: " + listenPort + ": " + e.Message,
                                         LOGGING_NAME, Logger.Level.WARNING);
            }
        }
    }
}
