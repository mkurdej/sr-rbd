using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.TPC
{
    public class LessTableVersionException : Exception
    {
        public LessTableVersionException(String tableName, int coordinatorTableVersion, int localTableVersion)
            : base("Less table version " + tableName + " coordinator: " + coordinatorTableVersion + " local:" + localTableVersion)
        {
        }
    }
}
