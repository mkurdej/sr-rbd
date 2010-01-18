﻿// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Msg;
using RBD.Communication;

namespace RBD.TPC.Msg
{
    public class CanCommitMessage : TPCMessage
    {
        public String QueryString { get; set; }
        public String TableName { get; set; }
        /**
         * Czy transakcja dotyczy utworzenia nowej tabeli
         */
        public bool IsCreate { get; set; }
        /**
         * Numer wersji tabeli na koordynatorze
         */
        public int TableVersion { get; set; }

        override
        public void FromBinary(DataInputStream s) //throws IOException 
        {
            base.FromBinary(s);
            QueryString = s.ReadString();
            TableName = s.ReadString();
            //http://java.sun.com/j2se/1.4.2/docs/api/java/io/DataInput.html#readBoolean()
            IsCreate = s.ReadBoolean(); // 1 Byte
            TableVersion = s.ReadInt32();
        }

        override
        public MessageType GetMessageType()
        {
            return MessageType.TPC_CANCOMMIT;
        }

        override
        public void ToBinary(DataOutputStream s) //throws IOException 
        {
            base.ToBinary(s);
            s.WriteString(QueryString);
            s.WriteString(TableName);
            s.Write((bool)IsCreate);
            s.Write((int)TableVersion);
        }
    }
}
