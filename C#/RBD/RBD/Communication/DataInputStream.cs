// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

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

        override public String ReadString() //throws IOException
        {
            // read bytes length
            int length = ReadInt32();

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
