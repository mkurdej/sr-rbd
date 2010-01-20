using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.DB
{
    class ImportTableException : Exception
    {
        public ImportTableException(Exception e) : base(e.Message)
        {
        }
    }
}
