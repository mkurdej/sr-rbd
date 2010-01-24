// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Msg;
using RBD.Communication;

namespace RBD.TPC.Msg
{
    public abstract class TPCMessage : Message
    {

        public String TransactionId { get; set; }

        public String getTransactionId()
        {
            return TransactionId;
        }

        public void setTransactionId(String transactionId)
        {
            TransactionId = transactionId;
        }

        override
        public void FromBinary(DataInputStream s)
        {
            TransactionId = s.ReadString();
        }

        override
        public void ToBinary(DataOutputStream s)
        {
            s.WriteString(TransactionId);
        }
    }
}
