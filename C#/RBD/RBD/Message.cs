using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.IO;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters;
using System.Runtime.Serialization.Formatters.Binary;

namespace RBD
{
    [Serializable]
    class Message
    {
        static String LOGGING_NAME = "Message";

        IPAddress sender;

        public void setSender(IPAddress newSender)
        {
            sender = newSender;
        }

        public byte[] Serialize()
        {
            MemoryStream stream = new MemoryStream();
            StreamWriter streamWriter = new StreamWriter(stream);
            
            //TODO - nie wiem, jak to zserializowac, zeby potem gadalo z reszta...
        }

        static public Message Unserialize(MessageType type, byte[] bytes, IPAddress sender)
        {
            Message result = MessageFactory.create(type);
            MemoryStream stream = new MemoryStream(bytes);
            StreamReader streamReader = new StreamReader(stream);
            result.fromBinary(streamReader);
            result.setSender(sender);
            return result;
        }
    }
}
