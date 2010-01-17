using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;

namespace RBD
{
    class TcpWorker
    {
        const String LOGGING_NAME = "TcpWorker";

        TcpClient tcpClient;

        public TcpWorker(TcpClient newTcpClient)
        {
            tcpClient = newTcpClient;
        }

        public void run()
        {
            IPEndPoint ip = (IPEndPoint)tcpClient.Client.RemoteEndPoint; 
            Logger.getInstance().log("Connection from: " + ip.Address.ToString()
                                     + ":" + ip.Port.ToString(),
                                     LOGGING_NAME, Logger.Level.INFO);
            NetworkStream stream = tcpClient.GetStream();
            
            Byte[] sizeBuf = new Byte[4];
            for(int nread = 0; nread < 4;)
                nread += stream.Read(sizeBuf, nread, 4 - nread);
            int size = BitConverter.ToInt32(sizeBuf, 0);
            size = IPAddress.NetworkToHostOrder(size);
            Logger.getInstance().log("Size = " + size, LOGGING_NAME, Logger.Level.INFO);

            Byte[] buffer = new Byte[size];
            for (int nread = 0; nread < size; )
                nread += stream.Read(buffer, nread, size - nread);

            String message = System.Text.Encoding.ASCII.GetString(buffer);

            Logger.getInstance().log("Message: [" + message + "]",
                                     LOGGING_NAME, Logger.Level.INFO);
        }
    }
}
