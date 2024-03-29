﻿// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.DB
{
    public interface DbConnector
    {
        DatabaseTable query(String queryString);
        void clearDatabase();
        String dumpTable(String tableName);
        void importTable(string tableName, int version, String dump);
    }
}
