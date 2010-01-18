// +-

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

using RBD.DB;
using RBD.Communication;

namespace RBD.Msg
{
    class HelloMessage : Message
    {
        public int ListeningPort { get; set; }
        public IList<TableVersion> Tables { get; set; }

        public HelloMessage()
        {
            // empty
        }

        public HelloMessage(List<TableVersion> tables, int port)
        {
            Tables = tables;
            ListeningPort = port;
        }

        override public void FromBinary(DataInputStream s)
        {//throws IOException {
            // read port
            ListeningPort = s.ReadInt32();

            // read count
            int count = s.ReadInt32();
            Tables = (IList<TableVersion>)new ArrayList();  // TODO check -- nie znam odpowiednika LinkedList w C#

            // read contents
            while (count-- > 0)
                Tables.Add(new TableVersion(s));

        }

        override public Message.MessageType GetMessageType()
        {
            return Message.MessageType.HELLO_MESSAGE;
        }

        override public void ToBinary(DataOutputStream s) {//throws IOException {
		// write port
		s.Write((int)ListeningPort);
		
		// write length
        s.Write((int)Tables.Count);
		
		// write tables
		foreach(TableVersion tv in Tables)
			tv.ToBinary(s);
	}
    }
}
