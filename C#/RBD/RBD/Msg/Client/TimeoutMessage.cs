// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Communication;

namespace RBD.Msg.Client
{
    class TimeoutMessage : ClientResponse
    {
        override
        public void FromBinary(DataInputStream s) //throws IOException 
        {
            // empty
        }

        override
        public MessageType GetMessageType()
        {
            return MessageType.CLIENT_TIMEOUT;
        }

        override
        public void ToBinary(DataOutputStream s) //throws IOException 
        {
            // empty
        }
    }
}
