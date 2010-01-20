using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Sockets;
using System.Net;
using RBD.Msg;
using RBD.Communication;

namespace RBD
{
    class TcpSender
    {
        const string LOGGING_NAME = "TcpSender";        
        SortedDictionary<IPEndPoint, NodeInfo> nodes = new SortedDictionary<IPEndPoint, NodeInfo>();
        static TcpSender instance = new TcpSender();

        protected TcpSender()
        {

        }

        public static TcpSender getInstance()
        {
            return instance;
        }

        public void removeNode(IPEndPoint address)
        {
            lock (typeof(TcpSender))
            {

                if (nodes.ContainsKey(address))
                {
                    Logger.getInstance().log("Request to remove unexisting node: " + address.ToString(),
                                    LOGGING_NAME,
                                    Logger.Level.WARNING);
                }
                else
                {
                    nodes.Remove(address);
                }
            }
        }

        public void addNodeBySocket(IPEndPoint node, Socket s)
        {
            lock (typeof(TcpSender))
            {
                if (nodes.ContainsKey(node))
                {
                    Logger.getInstance().log("Request to add already existing node: " + node.ToString(),
                                    LOGGING_NAME,
                                    Logger.Level.WARNING);
                }

                nodes.Add(node, new NodeInfo(s, false));
            }
        }

        public void AddServerNode(IPEndPoint node)
        {
            lock (typeof(TcpSender))
            {
                NodeInfo nodeInfo = nodes[node];

                if (nodeInfo == null)
                {
                    // connect to node
                    Socket socket;
                    try
                    {
                        socket = new Socket(node.Address, node.Port);
                    }
                    catch (Exception e)
                    {
                        Logger.getInstance().log("Failed to add server node: " + node.ToString(),
                                        LOGGING_NAME,
                                        Logger.Level.WARNING);
                        return;
                    }

                    nodes.put(node, new NodeInfo(socket, true));
                }
                else
                {
                    // mark node as server                      
                    nodeInfo.setIsServer(true);
                }
            }
        }

        public int getServerNodesCount()
        {
            lock (typeof(TcpSender))
            {
                int count = 0;

                foreach (NodeInfo node in nodes.values())
                    if (node.getIsServer())
                        ++count;

                return count;
            }
        }

        public List<IPAddress> getAllServerNodes()
        {
            lock (typeof(TcpSender))
            {
                List<IPAddress> result = new LinkedList<IPAddress>();

                foreach (DictionaryEntry e in nodes)
                {
                    if (((NodeInfo)e.Value).getIsServer())
                    {
                        result.Add(e.getKey());
                    }
                }

                return result;
            }
        }

        public void sendToAllServerNodes(Message message)
        {
            lock (typeof(TcpSender))
            {
                // begin-user-code                                                                  
                byte[] data;

                // serialize
                data = message.Serialize();

                // search for server nodes
                IDictionaryEnumerator it = nodes.GetEnumerator();
                Iterator<Map.Entry<InetSocketAddress, NodeInfo>> it = nodes.entrySet().iterator();

                while (it.MoveNext())
                {
                    NodeInfo node = it.Value;

                    if (node.getIsServer())
                    {
                        if (!writeToNode(it.Key, node.getSocket(), data))
                        {
                            nodes.Remove(it.Key);
                        }
                    }
                }
            }
            // end-user-code                                                        
        }

        public void sendToNode(Message message, IPEndPoint to)
        {
            lock (typeof(TcpSender))
            {
                // begin-user-code                                                                  
                byte[] data;

                // serialize
                data = message.Serialize();
                NodeInfo node = nodes.get(to);

                if (node == null)
                {
                    Logger.getInstance().log("Request to send to unexisting node: " + to.toString(),
                                    LOGGING_NAME,
                                    Logger.Level.WARNING);
                }

                if (!writeToNode(to, node.getSocket(), data))
                {
                    nodes.Remove(to);
                }
                // end-user-code     
            }
        }

        private bool writeToNode(IPAddress node, Socket s, byte[] data)
        {
            try
            {
                s.Send(data);
            }
            catch (System.IO.IOException e)
            {

                Logger.getInstance().log("Failure to write to node: " + node.ToString,
                                LOGGING_NAME,
                                Logger.Level.WARNING);

                // remove node from pool
                nodes.Remove(node);
                return false;
            }
            return true;
        }
    }
}
