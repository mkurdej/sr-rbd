/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb.db;

import java.io.*;
import java.sql.*;

import ddb.Config;
import ddb.Logger;

/**
 *
 * @author xeonic
 */
public class DbConnectorImpl implements DbConnector
{
    private Connection connection;
    private static final String LOGGING_NAME = "DbConnector";
    

    private static DbConnector instance = null;
    
    String URL_BEGIN = "jdbc:mysql";
    String DRIVER = "com.mysql.jdbc.Driver";

    private DbConnectorImpl()
    {
        String url = URL_BEGIN + "://" + Config.Host() + "/" + Config.Database();

        try
        {
            Class.forName(DRIVER).newInstance();
            connection = DriverManager.getConnection(url, Config.User(), Config.Password());
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
    	if( instance == null )
    		instance = new DbConnectorImpl();
    	
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
    
    public void clearDatabase() throws DBException
    {                                             
        Statement statement = null;               
        ResultSet result = null;                  
        try                                       
        {                                         
            statement = connection.createStatement();
            statement.execute("SHOW TABLES");        

            Logger.getInstance().log("Clearing database...", LOGGING_NAME,
                    Logger.Level.INFO);                                   

            result = statement.getResultSet();

            while(result.next())
            {                   
               statement = connection.createStatement();
                statement.execute("DROP TABLE " + result.getString(1));
                Logger.getInstance().log("DROP TABLE: " + result.getString(1),
                        LOGGING_NAME, Logger.Level.INFO);                     
            }                                                                 
        }                                                                     
        catch (SQLException e)                                                
        {                                                                     
            throw new DBException(e);                                         
        }                                                                     
    }                                                                         

    public String dumpTable(String tableName) throws DumpTableException
    {                                        
        String output = "";                  
        String cmd = "mysqldump -u " + Config.User() + " -p" + Config.Password() + " " + 
        				Config.Database() + " " + tableName;                                             

        Logger.getInstance().log(cmd, LOGGING_NAME, Logger.Level.INFO);

        Process p = null;
        try              
        {                
            p = Runtime.getRuntime().exec(cmd);
        }                                      
        catch (IOException e)                  
        {                                      
            Logger.getInstance().log("IO error during table dump: " + e.getMessage(),
                    LOGGING_NAME, Logger.Level.WARNING); 
            throw new DumpTableException(e);
        }                                                                            

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

        try
        {  
            String line = null;
            while ((line = input.readLine()) != null)
            {                                        
                if(!line.startsWith("/*") && !line.startsWith("--"))
                    output += line;                                 
            }                                                       
        }                                                           
        catch (IOException e)                                       
        {                                                           
            Logger.getInstance().log("IO error during table dump: " + e.getMessage(),
                    LOGGING_NAME, Logger.Level.WARNING);
            throw new DumpTableException(e);
        }                                                                            

        return output;
    }                 

    public void importTable(String tableName, int version, String dump) throws ImportTableException
    {                                   
        String cmd = "mysql -u " + Config.User() + " -p" + Config.Password() + " " +
        				Config.Database();

        Logger.getInstance().log(cmd, LOGGING_NAME, Logger.Level.INFO);

        Process p = null;
        try
        {
            p = Runtime.getRuntime().exec(cmd);
            OutputStream stdin = p.getOutputStream();
            stdin.write(dump.toString().getBytes());
            stdin.flush();
        }
        catch (IOException e)
        {
            Logger.getInstance().log("IO error during table import: " + e.getMessage(),
                    LOGGING_NAME, Logger.Level.WARNING);
            throw new ImportTableException(e);
        }
        DatabaseStateImpl.getInstance().setTableVersion(tableName, version);
    }

}
