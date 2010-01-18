// ?

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace RBD.DB
{
    class SqlParserImpl : SqlParser
    {
        const String LOGGING_NAME = "SqlParser";

        String query;
        String tableName;
        String lockQueryString;
        String parsedQueryString;
        SqlOperationType operationType;

        Regex selectRegExp;
        Regex insertRegExp;
        Regex updateRegExp;
        Regex deleteRegExp;
        Regex createRegExp;
        Regex lockRegExp; 
        Regex unlockRegExp;

        public SqlParserImpl()
        {
            selectRegExp = new Regex("^SELECT(.*)FROM([^;]*)", 
                RegexOptions.IgnoreCase);
            insertRegExp = new Regex("^INSERT\\s+INTO(.*)VALUES([^;]*)",
                RegexOptions.IgnoreCase);
            updateRegExp = new Regex("^UPDATE\\s+(.*)SET([^;]*)",
                RegexOptions.IgnoreCase);
            deleteRegExp = new Regex("^DELETE\\s+FROM([^;]*)",
                RegexOptions.IgnoreCase);
            createRegExp = new Regex("^CREATE\\s+TABLE([^(]*)(.*)",
                RegexOptions.IgnoreCase);
            lockRegExp = new Regex("^LOCK([^;]*)",
                RegexOptions.IgnoreCase);
            unlockRegExp = new Regex("^UNLOCK([^;]*)",
                RegexOptions.IgnoreCase); 
        }

        public bool parse(string queryString)
        {
            operationType = SqlOperationType.UNDEFINED;
            query = queryString.Trim();
            parsedQueryString = "";
            tableName = "";
            lockQueryString = "";

            if (query.EndsWith(";"))
                query = query.Substring(0, query.Length - 1);

            if (selectRegExp.Matches(query).Count > 0)
                parseSelect();
            else if (insertRegExp.Matches(query).Count > 0)
                parseInsert();
            else if (updateRegExp.Matches(query).Count > 0)
                parseUpdate();
            else if (deleteRegExp.Matches(query).Count > 0)
                parseDelete();
            else if (createRegExp.Matches(query).Count > 0)
                parseCreate();
            else if (lockRegExp.Matches(query).Count > 0)
                parseLock();
            else if (unlockRegExp.Matches(query).Count > 0)
                parseUnlock();
            else
                return false;

            Logger.getInstance().log("[" + tableName + "] " + parsedQueryString +
                "\t" + lockQueryString, LOGGING_NAME, Logger.Level.INFO);
            return true;
        }

        void parseSelect()
        {
            operationType = SqlOperationType.SELECT;
            MatchCollection matchList = selectRegExp.Matches(query);
            if (matchList.Count > 0)
            {
                Match match = matchList[0];
                String fields = match.Groups[1].Value.Trim();
                tableName = match.Groups[2].Value.Trim();
                parsedQueryString = "SELECT " + fields + " FROM " + tableName;
            }
        }

        void parseInsert()
        {
            operationType = SqlOperationType.INSERT;
            MatchCollection matchList = insertRegExp.Matches(query);
            if (matchList.Count > 0)
            {
                Match match = matchList[0];
                tableName = match.Groups[1].Value.Trim();
                String values = match.Groups[2].Value.Trim();
                values = values.Remove(0,1);
                values = values.Insert(0, "(0,");
                parsedQueryString = "INSERT INTO " + tableName+ " VALUES" + values;
            }
        }

        void parseUpdate()
        {
            operationType = SqlOperationType.UPDATE;
            MatchCollection matchList = updateRegExp.Matches(query);
            if (matchList.Count > 0)
            {
                Match match = matchList[0];
                tableName = match.Groups[1].Value.Trim();
                String set;
                String where;
                String rest = match.Groups[2].Value.Trim();
                Regex withWhereRegExp = new Regex("(.*)WHERE(.*)",
                    RegexOptions.IgnoreCase);

                MatchCollection matchList2 = withWhereRegExp.Matches(rest);
                if (matchList2.Count > 0)
                {
                    Match match2 = matchList2[0];
                    set = match2.Groups[1].Value.Trim();
                    where = match2.Groups[2].Value.Trim();
                    where = where.Insert(0, " WHERE (");
                    where = where.Insert(where.Length, ") AND _lock = 0");
                }
                else
                {
                    where = " WHERE _lock = 0";
                    set = rest;
                }
                parsedQueryString = "UPDATE " + tableName + " SET " + set + where;

                where = where.Remove(where.Length - 1);
                where = where.Insert(where.Length, "1");
                lockQueryString = "SELECT * FROM " + tableName + where;
            }
        }
        
        void parseDelete()
        {
            operationType = SqlOperationType.DELETE;
            MatchCollection matchList = deleteRegExp.Matches(query);
            if (matchList.Count > 0)
            {
                Match match = matchList[0];
                String where;
                String rest = match.Groups[1].Value.Trim();
                Regex withWhereRegExp = new Regex("(.*)WHERE(.*)",
                    RegexOptions.IgnoreCase);

                MatchCollection matchList2 = withWhereRegExp.Matches(rest);
                if (matchList2.Count > 0)
                {
                    Match match2 = matchList2[0];
                    tableName = match2.Groups[1].Value.Trim();
                    where = match2.Groups[2].Value.Trim();
                    where = where.Insert(0, " WHERE (");
                    where = where.Insert(where.Length, ") AND _lock = 0");
                }
                else
                {
                    where = " WHERE _lock = 0";
                    tableName = rest;
                }
                parsedQueryString = "DELETE FROM " + tableName + where;

                where = where.Remove(where.Length - 1);
                where = where.Insert(where.Length, "1");
                lockQueryString = "SELECT * FROM " + tableName + where;
            }
        }
        
        void parseCreate()
        {
            operationType = SqlOperationType.CREATE;
            MatchCollection matchList = createRegExp.Matches(query);
            if (matchList.Count > 0)
            {
                Match match = matchList[0];
                tableName = match.Groups[1].Value.Trim();
                String fields = match.Groups[2].Value.Trim();
                fields = fields.Remove(0, 1);
                fields = fields.Insert(0, "( _lock BOOL, ");
                parsedQueryString = "CREATE TABLE " + tableName + fields;
            }
        }
        
        void parseLock()
        {
            operationType = SqlOperationType.LOCK;
            MatchCollection matchList = lockRegExp.Matches(query);
            if (matchList.Count > 0)
            {
                Match match = matchList[0];
                String where;
                String rest = match.Groups[1].Value.Trim();
                Regex withWhereRegExp = new Regex("(.*)WHERE(.*)",
                    RegexOptions.IgnoreCase);

                MatchCollection matchList2 = withWhereRegExp.Matches(rest);
                if (matchList2.Count > 0)
                {
                    Match match2 = matchList2[0];
                    tableName = match2.Groups[1].Value.Trim();
                    where = match2.Groups[2].Value.Trim();
                    where = where.Insert(0, " WHERE (");
                    where = where.Insert(where.Length, ") AND _lock = 0");
                }
                else
                {
                    where = " WHERE _lock = 0";
                    tableName = rest;
                }
                parsedQueryString = "UPDATE " + tableName + " SET _lock = 1" + where;

                where = where.Remove(where.Length - 1);
                where = where.Insert(where.Length, "1");
                lockQueryString = "SELECT * FROM " + tableName + where;
            }
        }
        
        void parseUnlock()
        {
            operationType = SqlOperationType.UNLOCK;
            MatchCollection matchList = unlockRegExp.Matches(query);
            if (matchList.Count > 0)
            {
                Match match = matchList[0];
                String where;
                String rest = match.Groups[1].Value.Trim();
                Regex withWhereRegExp = new Regex("(.*)WHERE(.*)",
                    RegexOptions.IgnoreCase);

                MatchCollection matchList2 = withWhereRegExp.Matches(rest);
                if (matchList2.Count > 0)
                {
                    Match match2 = matchList2[0];
                    tableName = match2.Groups[1].Value.Trim();
                    where = match2.Groups[2].Value.Trim();
                    where = where.Insert(0, " WHERE (");
                    where = where.Insert(where.Length, ") AND _lock = 1");
                }
                else
                {
                    where = " WHERE _lock = 1";
                    tableName = rest;
                }
                parsedQueryString = "UPDATE " + tableName + " SET _lock = 0" + where;

                where = where.Remove(where.Length - 1);
                where = where.Insert(where.Length, "1");
                lockQueryString = "SELECT * FROM " + tableName + where;
            }
        }

        public String getQueryString()
        {
            return query;
        }

        public String getTableName()
        {
            return tableName;
        }

        public String getLockQueryString()
        {
            return lockQueryString;
        }

        public SqlOperationType getOperationType()
        {
            return operationType;
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
    }
}
