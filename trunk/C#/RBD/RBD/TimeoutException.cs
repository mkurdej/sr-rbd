// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD
{
    [Serializable]
    public class TimeoutException : Exception
    {
        public TimeoutException()
            : base("Timed out")
        {
        }

    }
}
