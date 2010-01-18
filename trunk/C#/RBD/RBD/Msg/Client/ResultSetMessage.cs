// +-

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Communication;

namespace RBD.Msg.Client
{
    public class ResultsetMessage : SuccessMessage
    {
        private String result;
        public String Result
        {
            get
            {
                return result;
            }
            set
            {
                result = value;
            }
        }

        //public void setResultSet(DatabaseTable table)
        //{
        //    this.setResult(table.toString());
        //}

        override
        public void FromBinary(DataInputStream s) //throws IOException 
        {
            Result = s.ReadString();
        }

        override
        public MessageType GetMessageType()
        {
            return MessageType.CLIENT_RESULTSET;
        }

        override
        public void ToBinary(DataOutputStream s) //throws IOException 
        {
            s.WriteString(Result);
        }
    }
}
