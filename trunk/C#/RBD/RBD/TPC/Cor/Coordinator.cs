// +

using System.Net;

namespace RBD.TPC.COR
{
    public abstract class Coordinator : TPCParticipant
    {
        public Coordinator()
            : base()
        {
        }

        public abstract void setClientAddress(IPEndPoint clientAddress);
    }
}