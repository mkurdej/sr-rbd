// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.DB
{
    public class NoSuchTableFieldException : Exception
    {
        public NoSuchTableFieldException(String table, String column)
        {
            this.table = table;
            this.column = column;
        }

        public String getMessage()
        {
            return "Niewłaściwa nazwa tabeli (" + table + ") lub nazwa kolumny (" + column + ").";
        }
        String table, column;
    }

}
