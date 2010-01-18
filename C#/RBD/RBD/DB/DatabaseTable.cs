using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data.MySqlClient;

namespace RBD.DB
{
    public class DatabaseTable
    {
        public DatabaseTable(MySqlDataReader recordSet)
        {
            while (recordSet.Read())
            {
                String thisrow = "";
                for (int i = 0; i < recordSet.FieldCount; i++)
                    thisrow += "[" + recordSet.GetValue(i).ToString() + "]";
                Logger.getInstance().log(thisrow, "DatabaseTable", Logger.Level.INFO);
            }
        }
    }
}
