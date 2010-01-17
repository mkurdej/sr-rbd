using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD
{
    interface SqlConnector
    {
        DatabaseTable query(String queryString);
    }
}
