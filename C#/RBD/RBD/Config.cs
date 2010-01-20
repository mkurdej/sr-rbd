// +--

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using System.Net;

namespace RBD
{
    public class Config
    {
        const String LOGGING_NAME = "Config";

        static IPEndPoint tcpaddress;
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

        public static IPEndPoint TcpAddress()
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
            //        try
            //        {
            //            Document inputSource = DocumentBuilderFactory.newInstance().
            //                newDocumentBuilder().parse(path);    

            //            XPathFactory factory = XPathFactory.newInstance();
            //            XPath xPath = factory.newXPath();

            //            tcpaddress 	= InetAddress.getByName(xPath.evaluate("/config/tcpaddress", inputSource));
            //            tcpPort 	= Integer.parseInt(xPath.evaluate("/config/tcpport", inputSource));
            //            udpPort 	= Integer.parseInt(xPath.evaluate("/config/udpport", inputSource));
            //            maxNodes 	= Integer.parseInt(xPath.evaluate("/config/maxnodes", inputSource));
            //            database 	= xPath.evaluate("/config/database", inputSource);
            //            host 		= xPath.evaluate("/config/host", inputSource);
            //            user 		= xPath.evaluate("/config/user", inputSource);
            //            password 	= xPath.evaluate("/config/password", inputSource);
            //        }
            //        catch(Exception ex)
            //        {
            //            Logger.getInstance().log(
            //                "Config parsing error " + ex.toString(), 
            //                LOGGING_NAME, 
            //                Logger.Level.WARNING);

            //            return false;
            //        }

            return true;
        }


    }
}
