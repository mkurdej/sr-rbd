// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Net;

namespace RBD.Communication
{
    public class DataInputStream : BinaryReader
    {
        //public class DataInputStream extends java.io.DataInputStream
        //public DataInputStream(InputStream in) : base {
        public DataInputStream(Stream input)
            : base(input)
        {
        }

        public bool readBoolean()
        {
            return ReadBoolean();
        }

        public String readString()
        {
            return ReadString();
        }

        public int readInt()
        {
            return IPAddress.NetworkToHostOrder(ReadInt32());
        }

        override public String ReadString() //throws IOException
        {
            // read bytes length
            int length = readInt();

            // read bytes
            byte[] bytes = new byte[length];
            int left = length;

            while (left > 0)
                left -= Read(bytes, length - left, left);

            // convert bytes using given encoding
            return Config.GetEncoding().GetString(bytes, 0, length);
        }
    }
}
