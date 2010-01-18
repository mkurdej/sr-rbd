// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.DB;
using RBD.Msg;
using RBD.Communication;

namespace RBD.TPC.Msg
{
    public class ErrorMessage : TPCMessage
    {
        public DBException Exception { get; set; }

        override
        public void FromBinary(DataInputStream s) //throws IOException 
        {
            base.FromBinary(s);
            Exception = new DBException(s);
        }

        override
        public MessageType GetMessageType()
        {
            return MessageType.TPC_ERROR;
        }

        override
        public void ToBinary(DataOutputStream s) //throws IOException 
        {
            base.ToBinary(s);
            Exception.ToBinary(s);
        }
    }
}
