using System.Collections;
using System.Collections.Generic;
using RBD.DB;
using RBD.Communication;

namespace RBD.Restore.Msg
{
    public class RestoreTableList : RestoreMessage {

	    private List<TableVersion> tables = new LinkedList<TableVersion>();
    	
	    public RestoreTableList()
	    {
    		
	    }
    	
	    public RestoreTableList(List<TableVersion> list)
	    {
		    this.tables = list;
	    }
    	
	    public void setTables(List<TableVersion> tables) {
		    this.tables = tables;
	    }

	    public List<TableVersion> getTables() {
		    return tables;
	    }
    	
	    override public void FromBinary(DataInputStream s)  {
		    // read count
		    int count = s.ReadInt();
		    setTables(new LinkedList<TableVersion>());
    		
		    // read contents
		    for(int i = 0; i < count; ++i)
			    getTables().add(new TableVersion(s));
	    }

        override public MessageType GetMessageType()
        {
		    return MessageType.RESTORE_TABLELIST;
	    }

        override public void ToBinary(DataOutputStream s)
        {
		    // write length
		    s.writeInt(getTables().size());
    		
		    // write tables
		    foreach(TableVersion tv in getTables())
			    tv.toBinary(s);
	    }
    }
}
