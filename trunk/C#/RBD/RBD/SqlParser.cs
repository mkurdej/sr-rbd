using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD
{
    interface SqlParser
    {
        void parse(String queryString);
        String getQueryString();
        String getTableName();
        String getLockQueryString();
        SqlOperationType getOperationType();
    }
}
