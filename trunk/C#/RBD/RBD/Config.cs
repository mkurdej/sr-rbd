// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Xml;
using System.Xml.XPath;
using System.IO;

namespace RBD
{
    public class Config
    {
        const String LOGGING_NAME = "Config";

        static IPAddress tcpaddress;
        static int tcpPort;
        static int udpPort;
        static int maxNodes;
        static String database;
        static String host;
        static String user;
        static String password;
        static Encoding encoding = Encoding.GetEncoding("ISO-8859-1");

        //public static Charset getCharset()
        public static Encoding getCharset()
        {
            return GetEncoding();
        }

        public static Encoding GetEncoding()
        {
            return encoding;
        }

        public static int TcpPort()
        {
            return tcpPort;
        }

        public static IPAddress TcpAddress()
        {
            return tcpaddress;
        }

        public static int UdpPort()
        {
            return udpPort;
        }

        public static int MaxNodes()
        {
            return maxNodes;
        }

        public static int MinOtherNodes()
        {
            return maxNodes == -1 ? 0 : maxNodes / 2;
        }

        public static String Database()
        {
            return database;
        }

        public static String Host()
        {
            return host;
        }

        public static String User()
        {
            return user;
        }

        public static String Password()
        {
            return password;
        }

        public static bool Load(String path)
        {
            try
            {
                FileStream stream = new FileStream(path, FileMode.Open);
                XPathDocument document = new XPathDocument(stream);
                XPathNavigator navigator = document.CreateNavigator();
                XPathNodeIterator iter;
                iter = navigator.Select("/config/tcpaddress");
                if (iter.MoveNext())
                {
                    tcpaddress = IPAddress.Parse(iter.Current.Value);
                    //Console.WriteLine("tcpaddress = {0}", tcpaddress.ToString());
                }
                iter = navigator.Select("/config/tcpport");
                if (iter.MoveNext())
                {
                    tcpPort = Convert.ToInt32(iter.Current.Value);
                    //Console.WriteLine("tcpPort = {0}", tcpPort);
                }
                iter = navigator.Select("/config/udpport");
                if (iter.MoveNext())
                {
                    udpPort = Convert.ToInt32(iter.Current.Value);
                    //Console.WriteLine("udpPort = {0}", udpPort);
                }
                iter = navigator.Select("/config/maxnodes");
                if (iter.MoveNext())
                {
                    maxNodes = Convert.ToInt32(iter.Current.Value);
                    //Console.WriteLine("maxnodes = {0}", maxNodes);
                }
                iter = navigator.Select("/config/database");
                if (iter.MoveNext())
                {
                    database = iter.Current.Value;
                    //Console.WriteLine("database = {0}", database);
                }
                iter = navigator.Select("/config/host");
                if (iter.MoveNext())
                {
                    host = iter.Current.Value;
                    //Console.WriteLine("host = {0}", host);
                }
                iter = navigator.Select("/config/user");
                if (iter.MoveNext())
                {
                    user = iter.Current.Value;
                    //Console.WriteLine("user = {0}", user);
                }
                iter = navigator.Select("/config/password");
                if (iter.MoveNext())
                {
                    password = iter.Current.Value;
                    //Console.WriteLine("password = {0}", password);
                }
            }
            catch (Exception ex)
            {
                Logger.getInstance().log(
                    "Config parsing error: " + ex.Message,
                    LOGGING_NAME,
                    Logger.Level.WARNING);

                return false;
            }

            return true;
        }


    }
}
