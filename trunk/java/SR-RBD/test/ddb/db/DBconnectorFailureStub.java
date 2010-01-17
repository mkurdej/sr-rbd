package ddb.db;

import java.sql.SQLException;

public class DBconnectorFailureStub implements DbConnector {

	@Override
	public DatabaseTable query(String queryString) throws DBException {
		throw new DBException(new SQLException());
		//return null;
	}

	@Override
	public void clearDatabase() throws DBException {
		// empty
		
	}

	@Override
	public String dumpTable(String tableName) throws DumpTableException {
		// empty
		return null;
	}

	@Override
	public void importTable(String dump) throws ImportTableException {
		// empty
		
	}

}
