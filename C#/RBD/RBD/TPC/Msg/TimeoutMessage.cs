// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Msg;
using RBD.Communication;

namespace RBD.TPC.Msg
{
    public class TimeoutMessage : Message
    {
        override
        public MessageType GetMessageType()
        {
            return MessageType.TPC_TIMEOUTMESSAGE;
        }

        override
        public void FromBinary(DataInputStream s) //throws IOException 
        {
            // empty
        }

        override
        public void ToBinary(DataOutputStream s) //throws IOException 
        {
            // empty
        }

    }
}
