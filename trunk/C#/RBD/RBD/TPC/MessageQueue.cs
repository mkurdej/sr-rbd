// +--- TODO BlockingQueue

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

        /** 
         * <!-- begin-UML-doc -->
         * <!-- end-UML-doc -->
         * @param message
         * @throws InterruptedException 
         */
        public void putMessage(Message message) //throws InterruptedException
        {
            messages.put(message);
        }

        /** 
         * <!-- begin-UML-doc -->
         * <!-- end-UML-doc -->
         * @return
         * @throws InterruptedException 
         */
        public Message getMessage() //throws InterruptedException 
        {
            //return new HelloMessage();
            return messages.take();
        }
    }
}
