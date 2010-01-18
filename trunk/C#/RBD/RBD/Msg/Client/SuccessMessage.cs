// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Communication;

namespace RBD.Msg.Client
{
    public class SuccessMessage : ClientResponse
    {
        public String DatabaseMessage { get; set; }

        override
        public void FromBinary(DataInputStream s) //throws IOException 
        {
            DatabaseMessage = s.ReadString();
        }

        override
        public void ToBinary(DataOutputStream s) //throws IOException 
        {
            s.WriteString(DatabaseMessage);

        }

        override
        public MessageType GetMessageType()
        {
            return MessageType.CLIENT_SUCCESS;
        }

    }
}
