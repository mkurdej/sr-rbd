// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace RBD.Communication
{
    public class DataOutputStream : BinaryWriter
    {
        //public class DataInputStream extends java.io.DataInputStream
        //public DataInputStream(InputStream in) : base {
        public DataOutputStream(Stream output)
            : base(output)
        {
        }

        public void writeString(String s)
        {
            WriteString(s);
        }

        public void WriteString(String s) //throws IOException
        {
            // transform string to bytes
            byte[] bytes = Config.GetEncoding().GetBytes(s);

            // write length
            Write(bytes.Length);

            // write content to stream
            Write(bytes, 0, bytes.Length);
        }
    }

}

