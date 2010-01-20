// + TODO check

using RBD.Communication;

namespace RBD.Restore.Msg
{
    public class RestoreTable : RestoreMessage {

	    protected int tableVersion; 
	    protected string tableName;
	    protected string tableDump;
    	
	    public RestoreTable()
	    {
    		
	    }
    	
	    public RestoreTable(int version, string name, string dump) {
		    setTableVersion(version);
		    setTableName(name);
		    setTableDump(dump);
	    }
    	
	    public void setTableName(string n) {
		    tableName = n;
	    }
    	
	    public int getTableVersion() {
		    return tableVersion;
	    }
    	
	    public void setTableVersion(int v) {
		    tableVersion = v;
	    }
    	
	    public string getTableName() {
		    return tableName;
	    }
    	
	    public void setTableDump(string d) {
		    tableDump = d;
	    }
    	
	    public string getTableDump() {
		    return tableDump;
	    }

        override public void FromBinary(DataInputStream s)
        {
		    tableVersion = s.ReadInt32();
		    tableName = s.ReadString();
		    tableDump = s.ReadString();
	    }

        override public MessageType GetMessageType()
        {
		    return MessageType.RESTORE_TABLE;
	    }

        override public void ToBinary(DataOutputStream s)
        {
            s.Write((int)tableVersion);
		    s.WriteString(tableName);
		    s.WriteString(tableDump);
	    }
    }
}
