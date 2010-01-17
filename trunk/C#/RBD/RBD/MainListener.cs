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
        static MainListener instance = new MainListener();
        public const Int32 LISTEN_PORT = 1501;

        MainListener()
        {

        }

        public static MainListener getInstance()
        {
            return instance;
        }

        public void listen()
        {
            try
            {
                IPAddress address = IPAddress.Parse("0.0.0.0");
                TcpListener server = new TcpListener(address, LISTEN_PORT);
                server.Start();

                Logger.getInstance().log("Listening on port " + LISTEN_PORT,
                                         LOGGING_NAME, Logger.Level.INFO);

                while (true)
                {
                    TcpClient client = server.AcceptTcpClient();
                    TcpWorker worker = new TcpWorker(client);
                    Thread workerThread = new Thread(worker.run);
                    workerThread.Start();

                }
            }
            catch (SocketException e)
            {
                Logger.getInstance().log("Socket exception: " + e.Message,
                                         LOGGING_NAME, Logger.Level.WARNING);
            }
        }
    }
}
