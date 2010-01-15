package ddb.db;

public class TableVersion 
{
	public TableVersion(String name, int version)
	{
		setTableName(name);
		setVersion(version);
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableName() {
		return tableName;
	}

	public void setVersion(int version) {
		this.tableVersion = version;
	}

	public int getVersion() {
		return tableVersion;
	}

	private String tableName;
	private int tableVersion;
}
