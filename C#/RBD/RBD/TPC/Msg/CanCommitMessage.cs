// + TODO check

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

        public bool IsCreate { get; set; }

        public int TableVersion { get; set; }

        public String getTableName()
        {
            return TableName;
        }

        public void setTableName(String tableName)
        {
            TableName = tableName;
        }

        public void setQueryString(String queryString)
        {
            QueryString = queryString;
        }

        public String getQueryString()
        {
            return QueryString;
        }

        /**
         * Czy transakcja dotyczy utworzenia nowej tabeli
         */
        public bool isCreate()
        {
            return IsCreate;
        }
        public void setCreate(bool isCreate)
        {
            IsCreate = isCreate;
        }
        /**
         * Numer wersji tabeli na koordynatorze
         */
        public int getTableVersion()
        {
            return TableVersion;
        }
        public void setTableVersion(int tableVersion)
        {
            TableVersion = tableVersion;
        }

        override
        public void FromBinary(DataInputStream s) //throws IOException 
        {
            base.FromBinary(s);
            QueryString = s.readString();
            TableName = s.readString();
            IsCreate = s.readBoolean();
            TableVersion = s.readInt();
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
            s.writeString(QueryString);
            s.writeString(TableName);
            s.writeBoolean(IsCreate);
            s.writeInt(TableVersion);
        }
    }
}
