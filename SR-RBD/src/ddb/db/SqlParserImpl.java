/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ddb.db;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ddb.Logger;

/**
 *
 * @author xeonic
 */
public class SqlParserImpl implements SqlParser
{
    private final String LOGGING_NAME = "SqlParser";

    private String parsedQueryString;
    private String tableName;
    private String lockQueryString;
    private SqlOperationType operationType;

    private String query;

    private Pattern selectRegExp;
    private Pattern insertRegExp;
    private Pattern updateRegExp;
    private Pattern deleteRegExp;
    private Pattern createRegExp;
    private Pattern lockRegExp;
    private Pattern unlockRegExp;

    public SqlParserImpl()
    {
        selectRegExp = Pattern.compile("^SELECT(.*)FROM([^;]*)",
                Pattern.CASE_INSENSITIVE);
        insertRegExp = Pattern.compile("^INSERT\\s+INTO(.*)VALUES([^;]*)",
                Pattern.CASE_INSENSITIVE);
        updateRegExp = Pattern.compile("^UPDATE\\s+(.*)SET([^;]*)",
                Pattern.CASE_INSENSITIVE);
        deleteRegExp = Pattern.compile("^DELETE\\s+FROM([^;]*)",
                Pattern.CASE_INSENSITIVE);
        createRegExp = Pattern.compile("^CREATE\\s+TABLE([^(]*)(.*)",
                Pattern.CASE_INSENSITIVE);
        lockRegExp   = Pattern.compile("^LOCK([^;]*)",
                Pattern.CASE_INSENSITIVE);
        unlockRegExp = Pattern.compile("^UNLOCK([^;]*)",
                Pattern.CASE_INSENSITIVE);
    }

    public boolean parse(String queryString)
    {
    	operationType = SqlOperationType.UNDEFINED;
        query = queryString.trim();
        parsedQueryString = "";
        tableName = "";
        lockQueryString = "";

        if(query.endsWith(";"))
            query = query.substring(0, query.length()-1);

        if(selectRegExp.matcher(query).find())
            parseSelect();
        else if(insertRegExp.matcher(query).find())
            parseInsert();
        else if(updateRegExp.matcher(query).find())
            parseUpdate();
        else if(deleteRegExp.matcher(query).find())
            parseDelete();
        else if(createRegExp.matcher(query).find())
            parseCreate();
        else if(lockRegExp.matcher(query).find())
            parseLock();
        else if(unlockRegExp.matcher(query).find())
            parseUnlock();
        else
        	return false;

        Logger.getInstance().log("[" + tableName + "] " + parsedQueryString +
                "\t" + lockQueryString + "",
                LOGGING_NAME, Logger.Level.INFO);
        
        return true;
    }

    public String getQueryString()
    {
        return parsedQueryString;
    }

    public String getTableName()
    {
        return tableName;
    }
    
    public String getLockQueryString()
    {
        return lockQueryString;
    }

    public void parseSelect()
    {
    	operationType = SqlOperationType.SELECT;
        Matcher m = selectRegExp.matcher(query);
        if(m.find())
        {
            String fields = m.group(1).trim();
            tableName = m.group(2).trim();
            parsedQueryString = "SELECT " + fields + " FROM " + tableName;
        }
    }

    public void parseInsert()
    {
    	operationType = SqlOperationType.INSERT;
        Matcher m = insertRegExp.matcher(query);
        if(m.find())
        {
            tableName = m.group(1).trim();
            String values = m.group(2).trim();
            values = values.replaceFirst("[(]", "(0,");
            parsedQueryString = "INSERT INTO " + tableName + " VALUES" + values;
        }

    }

    public void parseUpdate()
    {
    	operationType = SqlOperationType.UPDATE;
        Matcher m = updateRegExp.matcher(query);
        if(m.find())
        {
            tableName = m.group(1).trim();
            String set = new String();
            StringBuilder where = new StringBuilder();
            String rest = m.group(2).trim();
            Pattern withWhereRegExp = Pattern.compile("(.*)WHERE(.*)",
                    Pattern.CASE_INSENSITIVE);

            Matcher m2 = withWhereRegExp.matcher(rest);
            if(m2.find())
            {
                set = m2.group(1).trim();
                where.append(m2.group(2).trim());
                where.insert(0," WHERE (");
                where.append(") AND _lock = 0");
            }
            else
            {
                where.append(" WHERE _lock = 0");
                set = rest;
            }

            parsedQueryString = "UPDATE " + tableName + " SET " + set + where;

            where.deleteCharAt(where.length()-1);
            where.append("1");
            lockQueryString = "SELECT * FROM " + tableName + where;
        }
    }

    public void parseDelete()
    {
    	operationType = SqlOperationType.DELETE;
        Matcher m = deleteRegExp.matcher(query);
        if(m.find())
        {
            StringBuilder where = new StringBuilder();
            String rest = m.group(1).trim();
            Pattern withWhereRegExp = Pattern.compile("(.*)WHERE(.*)",
                    Pattern.CASE_INSENSITIVE);

            Matcher m2 = withWhereRegExp.matcher(rest);
            if(m2.find())
            {
                tableName = m2.group(1).trim();
                where.append(m2.group(2).trim());
                where.insert(0," WHERE (");
                where.append(") AND _lock = 0");
            }
            else
            {
                where.append(" WHERE _lock = 0");
                tableName = rest;
            }

            parsedQueryString = "DELETE FROM " + tableName + where;

            where.deleteCharAt(where.length()-1);
            where.append("1");
            lockQueryString = "SELECT * FROM " + tableName + where;
        }
    }

    public void parseCreate()
    {
    	operationType = SqlOperationType.CREATE;
        Matcher m = createRegExp.matcher(query);
        if(m.find())
        {
            tableName = m.group(1).trim();
            String fields = m.group(2).trim();
            fields = fields.replaceFirst("[(]", "( _lock BOOL, ");
            parsedQueryString = "CREATE TABLE " + tableName + fields;
        }
    }

    public void parseUnlock()
    {
    	operationType = SqlOperationType.UNLOCK;
        Matcher m = unlockRegExp.matcher(query);
        if(m.find())
        {
            StringBuilder where = new StringBuilder();
            String rest = m.group(1).trim();
            Pattern withWhereRegExp = Pattern.compile("(.*)WHERE(.*)",
                    Pattern.CASE_INSENSITIVE);

            Matcher m2 = withWhereRegExp.matcher(rest);
            if(m2.find())
            {
                tableName = m2.group(1).trim();
                where.append(m2.group(2).trim());
                where.insert(0," WHERE (");
                where.append(") AND _lock = 1");
            }
            else
            {
                where.append(" WHERE _lock = 1");
                tableName = rest;
            }

            parsedQueryString = "UPDATE " + tableName + " SET _lock = 0" + where;

            where.deleteCharAt(where.length()-1);
            where.append("1");
            lockQueryString = "SELECT * FROM " + tableName + where;
        }

    }

    public void parseLock()
    {
    	operationType = SqlOperationType.LOCK;
        Matcher m = lockRegExp.matcher(query);
        if(m.find())
        {
            StringBuilder where = new StringBuilder();
            String rest = m.group(1).trim();
            Pattern withWhereRegExp = Pattern.compile("(.*)WHERE(.*)",
                    Pattern.CASE_INSENSITIVE);

            Matcher m2 = withWhereRegExp.matcher(rest);
            if(m2.find())
            {
                tableName = m2.group(1).trim();
                where.append(m2.group(2).trim());
                where.insert(0," WHERE (");
                where.append(") AND _lock = 0");
            }
            else
            {
                where.append(" WHERE _lock = 0");
                tableName = rest;
            }

            parsedQueryString = "UPDATE " + tableName + " SET _lock = 1" + where;

            where.deleteCharAt(where.length()-1);
            where.append("1");
            lockQueryString = "SELECT * FROM " + tableName + where;
        }

    }

    public void test()
    {
        parse("SELECT * FROM tabela");
        parse("SELECT pole FROM tabela1");
        parse("SELECT pole1,pole2 FROM tabela");
        parse("sELECT pole1, pole2 FROM tabela");
        parse("SELeCT *  FROM  tabela ; ");
        parse("SELECT pole FROM tabela ;");
        parse("SELECT  pole1,pole2 FROM tabela;");
        parse("select pole1,  pole2 from tabela;");

        parse("INSERT INTO tabela VALUES('test','cos')");
        parse("INSERT INTO tabela VALUES('test','cos');");
        parse("INSERT INTO tabela1 VALUES('test','cos')");
        parse("INSERT INTO tabela1 VALUES(123,'cos1')");
        parse("INSERT INTO tabela1 VALUES(123,'cos1');");

        parse("DELETE FROM tabela1");
        parse("DELETE FROM tabela1 where blah = 1");
        parse("DELETE FROM tabela1 WHERE blah = 1;");
        parse("DELETE FROM tabela1 WHERE (blah = 1) AND cos = 2");
        parse("delete FROM tabela1 WHERE (blah = 1) AND cos = 2;");
        parse("DeletE from tabela1 WHERE (blah = 1) AND cos = 2");

        parse("UPDATE tabela1 SET dupa = 1;");
        parse("UPDATE tabela1 SET dupa = 1 WHERE x=y;");
        parse("UPDATE tabela1 SET dupa = 1 WHERE x=y AND y=z;");
        parse("update tabela1 set dupa = 1");

        parse("CREATE TABLE blah ( dupa VARCHAR(12) );");
        parse("CREATE TABLE blah2 ( dupa VARCHAR(12), dupa2 INT );");

        parse("LOCK tabela1;");
        parse("LocK tab2 WHere a=b;");
        parse("LOCK tabela1 WHERE cos = 1 AND cos2 = 2)");

        parse("UNLOCK tabela1;");
        parse("uNLocK tab2 WHere a=b;");
        parse("UNLOCK tabela1 WHERE cos = 1 AND cos2 = 2)");
    }

	public SqlOperationType getOperationType() 
	{
		return operationType;
	}

}
