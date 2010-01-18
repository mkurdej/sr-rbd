// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Communication;

namespace RBD
{
    public interface BinarySerializable
    {
        void FromBinary(DataInputStream dis); //throws IOException;
        void ToBinary(DataOutputStream dos); // throws IOException;
    }
}
