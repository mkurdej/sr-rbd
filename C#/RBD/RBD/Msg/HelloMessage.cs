// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

using RBD.DB;
using RBD.Communication;

namespace RBD.Msg
{
    public class HelloMessage : Message
    {
        public int ListeningPort { get; set; }
        public IList<TableVersion> Tables { get; set; }

        public HelloMessage()
        {
            // empty
        }

        public HelloMessage(IList<TableVersion> tables, int port)
        {
            setTables(tables);
            setListeningPort(port);
        }

        public void setListeningPort(int listeningPort)
        {
            ListeningPort = listeningPort;
        }

        public int getListeningPort()
        {
            return ListeningPort;
        }

        public void setTables(IList<TableVersion> tables)
        {
            Tables = tables;
        }

        public IList<TableVersion> getTables()
        {
            return Tables;
        }


        override public void FromBinary(DataInputStream s) //throws IOException
        {
            // read port
            ListeningPort = s.readInt();

            // read count
            int count = s.readInt();
            Tables = new List<TableVersion>();

            // read contents
            while (count-- > 0)
                Tables.Add(new TableVersion(s));
        }

        override public MessageType GetMessageType()
        {
            return MessageType.HELLO_MESSAGE;
        }

        override public void ToBinary(DataOutputStream s) //throws IOException
        {
            // write port
            s.writeInt(ListeningPort);

            // write length
            s.writeInt(Tables.Count);

            // write tables
            foreach (TableVersion tv in Tables)
                tv.ToBinary(s);
        }
    }
}
