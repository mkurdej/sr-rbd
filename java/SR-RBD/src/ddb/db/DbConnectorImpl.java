/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import ddb.Logger;

/**
 *
 * @author xeonic
 */
public class DbConnectorImpl implements DbConnector
{
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL_BEGIN = "jdbc:mysql";
    private static final String DATABASE = "sr";
    private static final String HOST = "localhost";
    private static final String USER = "sr";
    private static final String PASSWORD = "sr";

    private Connection connection;
    private static final String LOGGING_NAME = "DbConnector";

    private static final DbConnector instance = new DbConnectorImpl();

    private DbConnectorImpl()
    {
        String url = URL_BEGIN + "://" + HOST + "/" + DATABASE;

        try
        {
            Class.forName(DRIVER).newInstance();
            connection = DriverManager.getConnection(url, USER, PASSWORD);
        }
        catch (InstantiationException ex)
        {
            Logger.getInstance().log("Can't connect to the database: " + ex.getMessage(),
                    LOGGING_NAME, Logger.Level.SEVERE);
        }
        catch (IllegalAccessException ex)
        {
            Logger.getInstance().log("Can't connect to the database: " + ex.getMessage(),
                    LOGGING_NAME, Logger.Level.SEVERE);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getInstance().log("Can't connect to the database: " + ex.getMessage(),
                    LOGGING_NAME, Logger.Level.SEVERE);
        }
        catch (SQLException ex)
        {
            Logger.getInstance().log("Can't connect to the database: " + ex.getMessage(),
                    LOGGING_NAME, Logger.Level.SEVERE);

        }
    }

    public static DbConnector getInstance()
    {
        return instance;
    }

    public DatabaseTable query(String queryString) throws DBException
    {
    	Statement statement = null;
    	DatabaseTable result = null;
        try
        {
        	statement = connection.createStatement();
			statement.execute(queryString);
			result = new DatabaseTable(statement.getResultSet());
		} 
        catch (SQLException e) 
        {
        	throw new DBException(e);
		}
        return result;
    }
}
