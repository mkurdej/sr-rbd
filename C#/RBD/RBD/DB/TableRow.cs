// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.DB
{
    [Serializable]
    public class TableRow
    {
        /**
         * Utworz wiersz do tabeli
         * @param table tabela, do ktorej nalezy wiersz
         * @param values wartosci w wierszu, w takiej kolejnosci, jak nazwy kolumn
         * w tabeli
         */
        public TableRow(DatabaseTable table, IList<Object> values)
        {
            this.table = table;
            this.values = values;
            if (table.getColumnCount() != values.Count)
            {
                throw new IndexOutOfRangeException("table.getColumnCount() != values.size()");
            }
        }

        public IList<Object> getValues()
        {
            return values;
        }

        public DatabaseTable getTable()
        {
            return table;
        }

        /**
         * Pobierz wartość z danej kolumny
         * @param columnIndex numer kolumny
         * @return wartosc w tej kolumnie
         */
        public Object getValue(int columnIndex)
        {
            return values[columnIndex];
        }

        override
        public String ToString()
        {
            StringBuilder builder = new StringBuilder();

            foreach (Object v in getValues())
            {
                builder.Append(v);
                builder.Append("\t");
            }

            return builder.ToString();

        }

        /** Wartosci w poszczegolnych kolumnach. */
        private IList<Object> values;
        private DatabaseTable table;
    }

}

