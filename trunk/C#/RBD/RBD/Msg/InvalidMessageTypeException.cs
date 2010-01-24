// +

using System;

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
