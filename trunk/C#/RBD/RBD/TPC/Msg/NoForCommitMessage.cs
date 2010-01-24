// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Msg;
using RBD.Communication;

namespace RBD.TPC.Msg
{
    public class NoForCommitMessage : TPCMessage
    {
        override
        public void FromBinary(DataInputStream s) //throws IOException 
        {
            base.FromBinary(s);
        }

        override
        public MessageType GetMessageType()
        {
            return MessageType.TPC_NOFORCOMMIT;
        }

        override
        public void ToBinary(DataOutputStream s) //throws IOException 
        {
            base.ToBinary(s);
        }
    }
}
