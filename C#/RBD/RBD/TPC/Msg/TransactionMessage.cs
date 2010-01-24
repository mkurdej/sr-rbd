// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Msg;
using RBD.Communication;

namespace RBD.TPC.Msg
{
    public class TransactionMessage : Message
    {
        public String QueryString { get; set; }

        public TransactionMessage()
        {
            // empty
        }

        public TransactionMessage(String queryString)
        {
            QueryString = queryString;
        }

        public String getQueryString()
        {
            return QueryString;
        }

        public void setQueryString(String queryString)
        {
            QueryString = queryString;
        }

        override
        public void FromBinary(DataInputStream s) //throws IOException 
        {
            QueryString = s.ReadString();
        }

        override
        public MessageType GetMessageType()
        {
            return MessageType.TRANSACTION_MESSAGE;
        }

        override
        public void ToBinary(DataOutputStream s) //throws IOException 
        {
            s.WriteString(QueryString);
        }
    }
}