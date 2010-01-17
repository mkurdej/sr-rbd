using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Security.Permissions;

namespace RBD
{
    class Program
    {
        static void Main(string[] args)
        {
            Thread mainListenerThread = new Thread(MainListener.getInstance().listen);
            mainListenerThread.Start();
            Thread udpListenerThread = new Thread(UdpListener.getInstance().listen);
            udpListenerThread.Start();
            UdpSender udpSender = UdpSenderImpl.getInstance();
            SqlConnector sqlConnector = SqlConnectorImpl.getInstance();
            sqlConnector.query("SELECT * FROM blah");
            SqlParser sqlParser = new SqlParserImpl();
            ((SqlParserImpl)sqlParser).test();
            while(true)
            {
                udpSender.sendToAll(new Message());
                Thread.Sleep(5000);
            }
        }
    }
}
