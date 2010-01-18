using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.Msg
{
    [Serializable]
    public class InvalidMessageTypeException : Exception
    {
        public InvalidMessageTypeException(int type)
            : base("Invalid message type: " + type.ToString())
        {
        }
    }

}
