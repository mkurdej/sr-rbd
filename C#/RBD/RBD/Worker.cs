using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD
{
    //public abstract class Worker implements Runnable
    class Worker
    {
        private const String LOGGING_NAME = "Worker";

        //protected BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
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

        //final
        //public void putMessage(Message msg) //throws InterruptedException
        //{
        //    queue.put(msg);
        //}

        //public Message accept(MessageType type, InetSocketAddress node) //throws InterruptedException, TimeoutException
        //{
        //    MessageType[] types = { type };
        //    return accept(types, node);
        //}

        //public Message accept(MessageType[] types, InetSocketAddress node) //throws InterruptedException, TimeoutException
        //{
        //    while (true)
        //    {
        //        Message msg;

        //        if (timeoutTime == -1)
        //        {
        //            msg = queue.take();
        //        }
        //        else
        //        {
        //            msg = queue.poll(timeoutTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);

        //            if (msg == null)
        //                throw new TimeoutException();
        //        }

        //        MessageType type = msg.getType();

        //        // check if source matches
        //        if (node == null || msg.getSender().equals(node))
        //        {
        //            // check if type matches
        //            foreach (MessageType t in types)
        //            {
        //                if (type.equals(t))
        //                    return msg;
        //            }
        //        }

        //        Logger.getInstance().log(
        //                "Unexcepted message" + msg.toString(),
        //                LOGGING_NAME,
        //                Logger.Level.WARNING);
        //    }
        //}

    }
}
