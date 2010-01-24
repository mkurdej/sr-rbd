using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;
using System.IO;
using MySql.Data.MySqlClient;

namespace RBD.DB
{
    public class DbConnectorImpl : DbConnector
    {
        static DbConnector instance = new DbConnectorImpl();
        const String LOGGING_NAME = "SqlConnector";

        String server;
        String database;
        String username;
        String password;

        MySqlConnection connection;

        DbConnectorImpl()
        {
            server = Config.Host();
            database = Config.Database();
            username = Config.User();
            password = Config.Password();
        }

        void openDatabase()
        {
            try
            {
                string MyConString = "SERVER=" + server +
                                     ";DATABASE=" + database +
                                     ";UID=" + username +
                                     ";PASSWORD=" + password + ";";
                Logger.getInstance().log(MyConString, LOGGING_NAME, Logger.Level.INFO);
                connection = new MySqlConnection(MyConString);
                connection.Open();
            }
            catch (MySqlException e)
            {
                Logger.getInstance().log("Could not open database: " + e.Message,
                    LOGGING_NAME, Logger.Level.SEVERE);
            }
        }

        public static DbConnector getInstance()
        {
            return instance;
        }

        public DatabaseTable query(String queryString)
        {
            if (connection == null)
                openDatabase();

            MySqlDataReader Reader = null;
            if (connection.State == System.Data.ConnectionState.Open)
            {

                MySqlCommand command = connection.CreateCommand();
                command.CommandText = queryString;
                Reader = command.ExecuteReader();
            }
            return new DatabaseTable(Reader);
        }

        public void clearDatabase()
        {
            if (connection == null)
                openDatabase();

            MySqlDataReader reader = null;
            Logger.getInstance().log("Clearing database...", "SqlConnector", Logger.Level.INFO);
            if (connection.State == System.Data.ConnectionState.Open)
            {
                MySqlCommand command = connection.CreateCommand();
                command.CommandText = "SHOW TABLES";
                reader = command.ExecuteReader();
            }
            List<String> tables = new List<string>();
            while(reader.Read())
                tables.Add(reader.GetValue(0).ToString());
            
            reader.Close();

            if (connection.State == System.Data.ConnectionState.Open)
            {
                foreach (String tableName in tables)
                {
                    MySqlCommand command = connection.CreateCommand();
                    command.CommandText = "DROP TABLE " + tableName;
                    Logger.getInstance().log(command.CommandText, "SqlConnector", Logger.Level.INFO);
                    MySqlDataReader reader2 = command.ExecuteReader();
                    reader2.Close();
                }
            }
        }

        public String dumpTable(String tableName)
        {
            String output = "";
            ProcessStartInfo mysqldump = new ProcessStartInfo();
            mysqldump.FileName = "mysqldump";
            //mysqldump.Arguments = "-u " + USERNAME + " -p" + PASSWORD + " " + DATABASE + " " + tableName;
            mysqldump.Arguments = "-u " + username + " -p" + password + " -h " + server + " " + database + " " + tableName;
            mysqldump.UseShellExecute = false;
            mysqldump.RedirectStandardOutput = true;

            Logger.getInstance().log(mysqldump.FileName + " " + mysqldump.Arguments, LOGGING_NAME, Logger.Level.INFO);
            
            using (Process process = Process.Start(mysqldump))
            {
                using (StreamReader reader = process.StandardOutput)
                {
                    while (!reader.EndOfStream)
                    {
                        string line = reader.ReadLine();
                        if (!line.StartsWith("/*") && !line.StartsWith("--"))
                            output += line.Trim();
                    }
                }
            }
            
            return output;
        }

        public void importTable(string tableName, int version, String dump)
        {
            ProcessStartInfo mysql = new ProcessStartInfo();
            mysql.FileName = "mysql";
            //mysql.Arguments = "-u " + USERNAME + " -p" + PASSWORD + " " + DATABASE;
            mysql.Arguments = "-u " + username + " -p" + password + " -h " + server + " " + database;
            mysql.UseShellExecute = false;
            mysql.RedirectStandardInput = true;

            Logger.getInstance().log(mysql.FileName + " " + mysql.Arguments, LOGGING_NAME, Logger.Level.INFO);

            using (Process process = Process.Start(mysql))
                using (StreamWriter writer = process.StandardInput)
                   writer.Write(dump);
            DatabaseStateImpl.getInstance().setTableVersion(tableName, version);

        }
    }
}
