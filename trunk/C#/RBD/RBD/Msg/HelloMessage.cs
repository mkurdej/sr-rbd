// + TODO check

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
            Tables = tables;
            ListeningPort = port;
        }

        override public void FromBinary(DataInputStream s) //throws IOException
        {
            // read port
            ListeningPort = s.readInt();

            // read count
            int count = s.readInt();
            Tables = new List<TableVersion>();  // TODO check -- nie jestem pewny czy jest to odpowiednik LinkedList z Javy

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
            s.Write((int)ListeningPort);

            // write length
            s.Write((int)Tables.Count);

            // write tables
            foreach (TableVersion tv in Tables)
                tv.ToBinary(s);
        }
    }
}
