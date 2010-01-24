// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Net;

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

        public void writeBoolean(bool value)
        {
            Write(value);
        }

        public void writeInt(int value)
        {
            Write(IPAddress.HostToNetworkOrder(value));
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
            writeInt(bytes.Length);

            // write content to stream
            Write(bytes, 0, bytes.Length);
        }
    }

}

