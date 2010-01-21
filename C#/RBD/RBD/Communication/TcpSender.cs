// +

using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Sockets;
using System.Net;
using System.Threading;

using RBD.Util;
using RBD.Msg;
using RBD.Communication;

namespace RBD
{
    class TcpSender
    {
        const string LOGGING_NAME = "TcpSender";        
        SortedDictionary<IPEndPoint, NodeInfo> nodes = new SortedDictionary<IPEndPoint, NodeInfo>();
        static TcpSender instance = new TcpSender();
	    protected BlockingQueue<Message> queue;

        protected TcpSender()
        {
        }

        public static TcpSender getInstance()
        {
            return instance;
        }

        public void setQueue(BlockingQueue<Message> q)
        {
            queue = q;
        }

        public void removeNode(IPEndPoint address)
        {
            lock (this)
            {
                if (nodes.Remove(address) == false)
                {
                    Logger.getInstance().log("Request to remove unexisting node: " + address.ToString(),
                                    LOGGING_NAME,
                                    Logger.Level.WARNING);
                }
                Logger.getInstance().log("Node disconnected " + address.ToString(),
                        LOGGING_NAME,
                        Logger.Level.INFO);
            }
        }

        public void addNodeBySocket(IPEndPoint node, Socket s)
        {
            lock (this)
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

        public void AddServerNode(IPEndPoint node, BlockingQueue<Message> storage) 
        {
            lock (this)
            {
                NodeInfo nodeInfo = nodes[node];

                if (nodeInfo == null)
                {
                    // connect to node
                    IPEndPoint nodeEndPoint;
                    Socket socket;
                    try
                    {
                        nodeEndPoint = new IPEndPoint(node.Address, node.Port);
                        socket = new Socket(AddressFamily.InterNetwork,
                                    SocketType.Stream, ProtocolType.Tcp);
                        socket.Connect(nodeEndPoint);

                        TcpWorker worker = new TcpWorker(socket, storage);
                        new Thread(new ThreadStart(worker.run)).Start();
                        nodes[node] = new NodeInfo(socket, true);
                    }
                    catch (Exception)
                    {
                        Logger.getInstance().log("Failed to add server node: " + node.ToString(),
                                        LOGGING_NAME,
                                        Logger.Level.WARNING);
                        return;
                    }
                    nodes[node] = new NodeInfo(socket, true);
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
            lock (this)
            {
                int count = 0;

                foreach (NodeInfo node in nodes.Values)
                    if (node.getIsServer())
                        ++count;

                return count;
            }
        }

        public void sendToAllServerNodes(Message message)
        {
            lock (this)
            {
                // begin-user-code                                                                  
                byte[] data;

                // serialize
                data = message.Serialize();

                // search for server nodes
                IDictionaryEnumerator it = nodes.GetEnumerator();
                //Iterator<Map.Entry<InetSocketAddress, NodeInfo>> it = nodes.entrySet().iterator();  // w javie
                while (it.MoveNext())
                {
                    NodeInfo node = (NodeInfo)it.Value;
                    if (node.getIsServer())
                    {
                        if (!writeToNode((IPEndPoint)it.Key, node.getSocket(), data))
                        {
                            // TODO nie czaję tego kawałka -- usuwamy
                            nodes.Remove((IPEndPoint)it.Key);
                            //it.remove();  // w javie
                        }
                    }
                }

                // write to self
                try
                {
                    queue.put(message);
                }
                catch (ThreadInterruptedException e)
                {
                    Logger.getInstance().log("InterruptedException Cannot send to self",
                            LOGGING_NAME,
                            Logger.Level.SEVERE);
                }
            }
            // end-user-code                                                        
        }

        public void sendToNode(Message message, IPEndPoint to)
        {
            lock (this)
            {
                // begin-user-code
                byte[] data;

                if (to == null)
                {
                    // write to self
                    try
                    {
                        queue.put(message);
                    }
                    catch (ThreadInterruptedException)
                    {
                        Logger.getInstance().log("InterruptedException Cannot send to self",
                                LOGGING_NAME,
                                Logger.Level.SEVERE);
                    }

                    return;
                }

                // serialize
                data = message.Serialize();
                NodeInfo node = nodes[to];

                if (node == null)
                {
                    Logger.getInstance().log("Request to send to unexisting node: " + to.ToString(),
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

        private bool writeToNode(IPEndPoint node, Socket s, byte[] data)
        {
            try
            {
                s.Send(data);
            }
            catch (System.IO.IOException)
            {
                Logger.getInstance().log("Failure to write to node: " + node.ToString(),
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
