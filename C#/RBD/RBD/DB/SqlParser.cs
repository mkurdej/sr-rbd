// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.DB
{
    interface SqlParser
    {
        bool parse(String queryString);
        String getQueryString();
        String getTableName();
        String getLockQueryString();
        SqlOperationType getOperationType();
    }
}
