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
		// TODO Auto-generated method stub
		
	}

	@Override
	public String dumpTable(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void importTable(String dump) {
		// TODO Auto-generated method stub
		
	}

}
