using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;

namespace RBD.Communication
{
    public class NodeInfo
    {
        private Socket socket;
        private bool isServer;

        public NodeInfo(Socket s, bool isServer)
        {
            setSocket(s);
            setIsServer(isServer);
        }

        public void setSocket(Socket socket)
        {
            this.socket = socket;
        }
        public Socket getSocket()
        {
            return socket;
        }
        public void setIsServer(bool isServer)
        {
            this.isServer = isServer;
        }
        public bool getIsServer()
        {
            return isServer;
        }
    }

}
