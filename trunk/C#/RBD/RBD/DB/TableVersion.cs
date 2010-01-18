// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Communication;

namespace RBD.DB
{
    public class TableVersion : BinarySerializable
    {
        public String Name { get; set; }
        public int Version { get; set; }

        public TableVersion(String name, int version)
        {
            Name = name;
            Version = version;
        }

        public TableVersion(DataInputStream dis) //throws IOException
        {
            FromBinary(dis);
        }

        public void FromBinary(DataInputStream dis) //throws IOException
        {
            Name = dis.ReadString();
            Version = dis.ReadInt32();
        }

        public void ToBinary(DataOutputStream dos) //throws IOException
        {
            dos.WriteString(Name);
            dos.Write((int)Version);
        }

    }
}
