using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD
{
    public abstract class Message : BinarySerializable
    {

        private const string LOGGING_NAME = "Message";

        //private InetSocketAddress sender;

        //public void setSender(InetSocketAddress sender)
        //{
        //    this.sender = sender;
        //}

        //public InetSocketAddress getSender()
        //{
        //    return sender;
        //}

        override public String ToString()
        {
            StringBuilder builder = new StringBuilder();

            builder.Append("[");
            builder.Append(getType());
            builder.Append(" from ");
            //builder.Append(sender.toString());
            builder.Append("]");

            return builder.ToString();
        }

        /**
         * 
         * @return type constant for specialized class
         */
        public abstract MessageType getType();

        //final
        //public byte[] Serialize()
        //{
        //    try
        //    {
        //        // serialize data
        //        ByteArrayOutputStream data = new ByteArrayOutputStream();
        //        DataOutputStream dataStream = new DataOutputStream(data);
        //        toBinary(dataStream);

        //        // wrap data in envelope ( byte[][] would be more efficient, but this is more convinient )
        //        // btw where is writev() in case of sending this through udp in java?!
        //        ByteArrayOutputStream envelope = new ByteArrayOutputStream();
        //        DataOutputStream envelopeStream = new DataOutputStream(envelope);

        //        byte[] bytes = data.toByteArray();

        //        // size + type + data
        //        envelopeStream.writeInt(bytes.length);
        //        envelopeStream.writeInt(getType().ordinal());
        //        envelopeStream.write(bytes);

        //        return envelope.toByteArray();

        //    }
        //    catch (IOException ex)
        //    {
        //        // should never happen due to stream is wrapped around ByteArrayOutputStream 
        //        Logger.getInstance().log("Serialization failure: " + ex.getMessage(),
        //                LOGGING_NAME,
        //                Logger.Level.SEVERE);

        //        return null;
        //    }
        //}

        //static public Message Unserialize(MessageType type, byte[] bytes, InetSocketAddress sender) //throws IOException
        //{
        //    // create message object of given type
        //    Message result = MessageFactory.create(type);

        //    // unserialize it
        //    ByteArrayInputStream data = new ByteArrayInputStream(bytes);
        //    DataInputStream dataStream = new DataInputStream(data);
        //    result.fromBinary(dataStream);

        //    // mark sender
        //    result.setSender(sender);

        //    return result;
        //}
    }
}

//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Text;
//using System.Net;
//using System.IO;
//using System.Net.Sockets;
//using System.Runtime.Serialization;
//using System.Runtime.Serialization.Formatters;
//using System.Runtime.Serialization.Formatters.Binary;

//namespace RBD
//{
//    [Serializable]
//    class Message
//    {
//        static String LOGGING_NAME = "Message";

//        IPAddress sender;

//        public void setSender(IPAddress newSender)
//        {
//            sender = newSender;
//        }

//        public byte[] Serialize()
//        {
//            MemoryStream stream = new MemoryStream();
//            StreamWriter streamWriter = new StreamWriter(stream);

//            //TODO - nie wiem, jak to zserializowac, zeby potem gadalo z reszta...
//        }

//        static public Message Unserialize(MessageType type, byte[] bytes, IPAddress sender)
//        {
//            Message result = MessageFactory.create(type);
//            MemoryStream stream = new MemoryStream(bytes);
//            StreamReader streamReader = new StreamReader(stream);
//            result.fromBinary(streamReader);
//            result.setSender(sender);
//            return result;
//        }
//    }
//}
