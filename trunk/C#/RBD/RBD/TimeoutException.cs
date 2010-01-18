// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD
{
    class TimeoutException : Exception
    {
        public TimeoutException()
            : base("Timed out")
        {
        }

    }
}
