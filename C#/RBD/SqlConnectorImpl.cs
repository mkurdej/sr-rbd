using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data.MySqlClient;

namespace RBD
{
    class SqlConnectorImpl : SqlConnector
    {
        static SqlConnector instance = new SqlConnectorImpl();
        const String LOGGING_NAME = "SqlConnector";

        MySqlConnection connection;

        SqlConnectorImpl()
        {

        }

        void openDatabase()
        {
            try
            {
                string MyConString = "SERVER=192.168.0.1;" +
                                     "DATABASE=sr;" +
                                     "UID=sr;" +
                                     "PASSWORD=sr;";
                connection = new MySqlConnection(MyConString);
                connection.Open();
            }
            catch (MySqlException e)
            {
                Logger.getInstance().log("Could not open database: " + e.Message,
                    LOGGING_NAME, Logger.Level.SEVERE);
            }
        }

        public static SqlConnector getInstance()
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
    }
}
