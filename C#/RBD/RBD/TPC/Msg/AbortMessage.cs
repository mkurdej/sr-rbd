// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Msg;
using RBD.Communication;

namespace RBD.TPC.Msg
{
    public class AbortMessage : TPCMessage
    {
        override
        public void FromBinary(DataInputStream s)
        {
            base.FromBinary(s);
        }

        override
        public MessageType GetMessageType()
        {
            return MessageType.TPC_ABORT;
        }

        override
        public void ToBinary(DataOutputStream s) //throws IOException 
        {
            base.ToBinary(s);
        }
    }
}
