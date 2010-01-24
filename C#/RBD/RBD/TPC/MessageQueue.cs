// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Msg;
using RBD.Util;

namespace RBD.TPC
{
    public class MessageQueue
    {
        private BlockingQueue<Message> messages = new BlockingQueue<Message>();

        public void putMessage(Message message) //throws InterruptedException
        {
            messages.put(message);
        }

        public Message getMessage() //throws InterruptedException 
        {
            return messages.take();
        }
    }
}
