using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data.MySqlClient;

namespace RBD.DB
{
    public class DatabaseTable
    {
        private string table;

        public DatabaseTable(MySqlDataReader recordSet)
        {
            StringBuilder builder = new StringBuilder();

            while (recordSet.Read())
            {
                String thisrow = "";
                for (int i = 0; i < recordSet.FieldCount; i++)
                {
                    builder.Append(recordSet.GetValue(i).ToString());
                    builder.Append("\t");
                }
                builder.Append("\n");
                    //thisrow += "[" + recordSet.GetValue(i).ToString() + "]";
                Logger.getInstance().log(thisrow, "DatabaseTable", Logger.Level.INFO);
            }

            builder.Append("TOTAL ROWS: ");
            builder.Append(recordSet.RecordsAffected);

            this.table = builder.ToString();
        }

        public String toString() {
            return this.table;
        }

    }
}
