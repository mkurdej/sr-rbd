// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Communication;
using RBD.DB;

namespace RBD.Msg.Client
{
    public class ResultsetMessage : SuccessMessage
    {
        private String result;

        public void setResultSet(DatabaseTable table)
        {
            this.setResult(table.ToString());
        }

        public void setResult(String result)
        {
            this.result = result;
        }

        public String getResult()
        {
            return result;
        }

        override
        public void FromBinary(DataInputStream s) //throws IOException 
        {
            result = s.ReadString();
        }

        override
        public MessageType GetMessageType()
        {
            return MessageType.CLIENT_RESULTSET;
        }

        override
        public void ToBinary(DataOutputStream s) //throws IOException 
        {
            s.WriteString(result);
        }
    }
}
