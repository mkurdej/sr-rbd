﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using RBD.Msg;
using RBD.Util;
using System.Net;

namespace RBD
{
    //public abstract class Worker implements Runnable
    public abstract class Worker
    {
        const String LOGGING_NAME = "Worker";

        protected BlockingQueue<Message> queue = new BlockingQueue<Message>();
        protected long timeoutTime = -1;

        //final
        public void setTimeout(long ms)
        {
            //timeoutTime = System.currentTimeMillis() + ms;
            timeoutTime = (long) TimeSpan.FromTicks(DateTime.Now.Ticks).TotalMilliseconds + ms;
        }

        //final
        public void clearTimeout()
        {
            timeoutTime = -1;
        }

        
        public void putMessage(Message msg) //throws InterruptedException
        {
            queue.put(msg);
        }

        public Message accept(Message.MessageType type, IPEndPoint node) //throws InterruptedException, TimeoutException
        {
            Message.MessageType[] types = { type };
            return accept(types, node);
        }

        public Message accept(Message.MessageType[] types, IPEndPoint node) //throws InterruptedException, TimeoutException
        {
            while (true)
            {
                Message msg;

                if (timeoutTime == -1)
                {
                    msg = queue.take();
                }
                else
                {
                    msg = queue.poll((int)timeoutTime - (int)TimeSpan.FromTicks(DateTime.Now.Ticks).TotalMilliseconds);
                    //zakomentowane, bo patrz na queue.poll()
                    //if (msg == null)
                    //    throw new TimeoutException();
                }

                Message.MessageType type = msg.GetMessageType();

                // check if source matches
                if (node == null || msg.Sender.Equals(node))
                {
                    // check if type matches
                    foreach (Message.MessageType t in types)
                    {
                        if (type.Equals(t))
                            return msg;
                    }
                }

                Logger.getInstance().log(
                        "Unexcepted message" + msg.ToString(),
                        LOGGING_NAME,
                        Logger.Level.WARNING);
            }
        }

    }
}
