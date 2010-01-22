// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;

using RBD.Communication;
using RBD.Msg;
using RBD.Util;

namespace RBD.Communication
{
    class TcpWorker : Runnable
    {
        const String LOGGING_NAME = "TcpWorker";

        protected BlockingQueue<Message> storage;
        protected Socket socket;

        public TcpWorker(Socket newSocket, BlockingQueue<Message> queue)
        {
            storage = queue;
            socket = newSocket;
        }

        public void run()
        {
            IPEndPoint address = (IPEndPoint)socket.RemoteEndPoint;
            Logger.getInstance().log("New connection from: "
                + address.ToString(), LOGGING_NAME, Logger.Level.INFO);
            try
            {
                int size;
                int type;
                socket.SendTimeout = 0;
                NetworkStream networkStream = new NetworkStream(socket);
                DataInputStream dis = new DataInputStream(networkStream);

                while (true)
                {
                    try
                    {
                        size = dis.readInt();
                    }
                    catch (EndOfStreamException)
                    {
                        break; // node has disconnected legally
                    }

                    type = dis.readInt();
                    byte[] b = new byte[size];

                    Logger.getInstance().log("Size = " + size,
                            LOGGING_NAME, Logger.Level.INFO);

                    int left;
                    for (left = size; left > 0; )
                        left -= dis.Read(b, size - left, left);

                    Message m = Message.Unserialize(Message.FromInt(type), b, address);
                    storage.put(m);
                }
            }
            catch (IOException ex)
            {
                Logger.getInstance().log("Socket exception! " + ex.Message,
                        LOGGING_NAME,
                        Logger.Level.WARNING);
            }
            catch (ThreadInterruptedException ex)
            {
                Logger.getInstance().log("InterruptedException " + ex.Message,
                        LOGGING_NAME,
                        Logger.Level.WARNING);
            }
            catch (InvalidMessageTypeException ex)
            {
                Logger.getInstance().log("InvalidMessageTypeException " + ex.Message,
                        LOGGING_NAME,
                        Logger.Level.WARNING);
            }
            finally
            {
                TcpSender.getInstance().removeNode(address);
            }
        }
    }
}