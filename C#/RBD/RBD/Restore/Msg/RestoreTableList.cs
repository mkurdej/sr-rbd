// + TODO check

using System.Collections;
using System.Collections.Generic;
using RBD.DB;
using RBD.Communication;

namespace RBD.Restore.Msg
{
    public class RestoreTableList : RestoreMessage {

        private IList<TableVersion> tables = new List<TableVersion>();
    	
	    public RestoreTableList()
	    {
    		
	    }

        public RestoreTableList(IList<TableVersion> list)
        {
            this.tables = list;
        }

        public void setTables(IList<TableVersion> tables)
        {
            this.tables = tables;
        }

        public IList<TableVersion> getTables()
        {
            return tables;
        }
    	
	    override public void FromBinary(DataInputStream s)  {
		    // read count
		    int count = s.readInt();
            setTables(new List<TableVersion>());

            // read contents
            for(int i = 0; i < count; ++i)
                getTables().Add(new TableVersion(s));
	    }

        override public MessageType GetMessageType()
        {
		    return MessageType.RESTORE_TABLELIST;
	    }

        override public void ToBinary(DataOutputStream s)
        {
		    // write length
            s.Write((int)getTables().Count);

            // write tables
            foreach (TableVersion tv in getTables())
                tv.ToBinary(s);
	    }
    }
}
