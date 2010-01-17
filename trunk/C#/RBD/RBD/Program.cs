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
            /*
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
             */
            DbConnector sqlConnector = DbConnectorImpl.getInstance();
            Console.Write(sqlConnector.dumpTable("blah"));
            sqlConnector.importTable(@"DROP TABLE IF EXISTS `blah234`;CREATE TABLE `blah234` (`id` varchar(123) DEFAULT NULL)ENGINE=MyISAM DEFAULT CHARSET=utf8;LOCK TABLES `blah234` WRITE;UNLOCK TABLES;");
            Thread.Sleep(10000);
            
        }
    }
}
