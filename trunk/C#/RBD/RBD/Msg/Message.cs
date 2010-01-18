// +--

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.IO;

using RBD.Communication;

namespace RBD.Msg
{
    [Serializable]
    public abstract class Message : BinarySerializable
    {
        public enum MessageType
        {
            RESTORE_INCENTIVE,
            RESTORE_ACK,
            RESTORE_NACK,
            RESTORE_TABLELIST,
            RESTORE_TABLE,
            TPC_ABORT,
            TPC_PRECOMMIT,
            TPC_ACKPRECOMMIT,
            TPC_CANCOMMIT,
            TPC_YESFORCOMMIT,
            TPC_DOCOMMIT,
            TPC_HAVECOMMITED,
            TPC_NOFORCOMMIT,
            TPC_ERROR,
            TRANSACTION_MESSAGE,
            CLIENT_SUCCESS,
            CLIENT_CONFLICT,
            CLIENT_ERROR,
            CLIENT_RESULTSET,
            CLIENT_TIMEOUT,
            HELLO_MESSAGE
        };

        public static MessageType FromInt(int type) //throws InvalidMessageTypeException 
        {
            if (type < 0 || type >= Enum.GetValues(typeof(MessageType)).Length)
                throw new InvalidMessageTypeException(type);

            return (MessageType)type;
        }
        /// //////////////////////////////////////////////////////////
        /// interface
        public abstract void ToBinary(DataOutputStream dos);
        public abstract void FromBinary(DataInputStream dis);
        /// //////////////////////////////////////////////////////////

        const string LOGGING_NAME = "Message";

        //InetSocketAddress sender;
        public IPEndPoint Sender { get; set; }

        override public String ToString()
        {
            StringBuilder builder = new StringBuilder();

            builder.Append("[");
            builder.Append(GetMessageType());
            builder.Append(" from ");
            //builder.Append(sender.toString());
            builder.Append("]");

            return builder.ToString();
        }

        /**
         * 
         * @return type constant for specialized class
         */
        public abstract MessageType GetMessageType();

        //final
        public byte[] Serialize()
        {
            MemoryStream ms = new MemoryStream();
            DataOutputStream dataStream = new DataOutputStream(ms);
            ToBinary(dataStream);

            // wrap data in envelope ( byte[][] would be more efficient, but this is more convinient )
            MemoryStream envelopeMs = new MemoryStream();
            DataOutputStream envelopeDataStream = new DataOutputStream(ms);

            byte[] bytes = ms.ToArray();

            // size + type + data
            envelopeDataStream.Write((int)bytes.Length);
            envelopeDataStream.Write((int)GetMessageType());
            envelopeDataStream.Write(bytes);

            return envelopeMs.ToArray();
            //catch (IOException ex)
            //{
            //    // should never happen due to stream is wrapped around ByteArrayOutputStream 
            //    Logger.getInstance().log("Serialization failure: " + ex.getMessage(),
            //            LOGGING_NAME,
            //            Logger.Level.SEVERE);

            //    return null;
            //}
        }

        static public Message Unserialize(MessageType type, byte[] bytes, IPEndPoint sender) //throws IOException
        {
            // create message object of given type
            Message result = MessageFactory.create(type);

            // unserialize it
            MemoryStream data = new MemoryStream(bytes);
            DataInputStream dataStream = new DataInputStream(data);
            result.FromBinary(dataStream);

            // mark sender
            result.Sender = sender;
            return result;
        }
    }
}
