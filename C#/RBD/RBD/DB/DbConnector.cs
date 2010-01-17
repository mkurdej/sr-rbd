using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD
{
    interface DbConnector
    {
        DatabaseTable query(String queryString);
        void clearDatabase();
        String dumpTable(String tableName);
        void importTable(String dump);
    }
}
