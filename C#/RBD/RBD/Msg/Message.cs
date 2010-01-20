// + TODO check, wartości enuma !

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
            // TODO trzeba zachować te same wartości we wszystkich implementacjach
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
            TPC_TIMEOUTMESSAGE, // TODO dodany w C#
            TRANSACTION_MESSAGE,
            CLIENT_SUCCESS,
            CLIENT_CONFLICT,
            CLIENT_ERROR,
            CLIENT_RESULTSET,
            CLIENT_TIMEOUT,
            HELLO_MESSAGE
        };

        public static MessageType fromInt(int type) //throws InvalidMessageTypeException 
        {
            return FromInt(type);
        }
        public static MessageType FromInt(int type) //throws InvalidMessageTypeException 
        {
            if (type < 0 || type >= Enum.GetValues(typeof(MessageType)).Length)
                throw new InvalidMessageTypeException(type);

            return (MessageType)type;
        }
        /// //////////////////////////////////////////////////////////
        public void toBinary(DataOutputStream dos)
        {
            ToBinary(dos);
        }
        public void fromBinary(DataInputStream dis)
        {
            FromBinary(dis);
        }
        /// interface
        public abstract void ToBinary(DataOutputStream dos);
        public abstract void FromBinary(DataInputStream dis);
        /// //////////////////////////////////////////////////////////

        const string LOGGING_NAME = "Message";

        //InetSocketAddress sender;
        public IPEndPoint getSender() {
            return Sender;
        }
        public void setSender(IPEndPoint sender)
        {
            Sender = sender;
        }
        public IPEndPoint Sender { get; set; }

        public String toString()
        {
            return ToString();
        }
        override public String ToString()
        {
            StringBuilder builder = new StringBuilder();

            builder.Append("[");
            builder.Append(GetMessageType());
            builder.Append(" from ");
            if(Sender != null)
 				builder.Append(Sender.ToString());
 			else 
 			    builder.Append("self");
            builder.Append("]");

            return builder.ToString();
        }

        /**
         * 
         * @return type constant for specialized class
         */
        public MessageType getMessageType()
        {
            return GetMessageType();
        }
        public abstract MessageType GetMessageType();

        //final
        public byte[] serialize()
        {
            return Serialize();
        }
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
        }

        static public Message unserialize(MessageType type, byte[] bytes, IPEndPoint sender) //throws IOException
        {
            return Unserialize(type, bytes, sender);
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
