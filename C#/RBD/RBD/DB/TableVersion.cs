﻿// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Communication;

namespace RBD.DB
{
    public class TableVersion : BinarySerializable
    {

        public TableVersion(String name, int version)
        {
            Name = name;
            Version = version;
        }

        public TableVersion(DataInputStream dis) //throws IOException
        {
            FromBinary(dis);
        }

        public String getTableName()
        {
            return Name;
        }
        public void setTableName(String tableName)
        {
            Name = tableName;
        }
        public String Name { get; set; }

        public int getVersion()
        {
            return Version;
        }
        public void setVersion(int tableVersion)
        {
            Version = tableVersion;
        }
        public int getTableVersion() {
            return Version;
        }
        public void setTableVersion(int tableVersion) {
            Version = tableVersion;
        }
        public int Version { get; set; }

        public void FromBinary(DataInputStream dis) //throws IOException
        {
            Name = dis.readString();
            Version = dis.readInt();
        }

        public void ToBinary(DataOutputStream dos) //throws IOException
        {
            dos.writeString(Name);
            dos.writeInt(Version);
        }

    }
}
