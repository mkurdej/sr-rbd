package ddb.db;

import java.sql.SQLException;

public class DBconnectorFailureStub implements DbConnector {

	@Override
	public DatabaseTable query(String queryString) throws DBException {
		throw new DBException(new SQLException());
		//return null;
	}

}
