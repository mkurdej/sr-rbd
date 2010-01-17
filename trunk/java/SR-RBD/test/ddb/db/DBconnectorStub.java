package ddb.db;

public class DBconnectorStub implements DbConnector {

	@Override
	public DatabaseTable query(String queryString) throws DBException {
		return null;
	}

	@Override
	public void clearDatabase() throws DBException {
		
	}

	@Override
	public String dumpTable(String tableName) throws DumpTableException {
		return null;
	}

	@Override
	public void importTable(String dump) throws ImportTableException {
	}

}
