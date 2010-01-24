// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Communication;

namespace RBD.Msg.Client
{
    public class SuccessMessage : ClientResponse
    {
        private String databaseMessage = "SUCCESS";

        public void setDatabaseMessage(String message)
        {
            databaseMessage = message;
        }

        public String getDatabaseMessage()
        {
            return databaseMessage;
        }
        override
        public void FromBinary(DataInputStream s) //throws IOException 
        {
            databaseMessage = s.readString();
        }

        override
        public void ToBinary(DataOutputStream s) //throws IOException 
        {
            s.writeString(databaseMessage);

        }

        override
        public MessageType GetMessageType()
        {
            return MessageType.CLIENT_SUCCESS;
        }

    }
}
