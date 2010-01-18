using System.Net;

namespace RBD.TPC.COR
{
    public abstract class Coordinator : TPCParticipant
    {

        public abstract void setClientAddress(IPAddress clientAddress);
    }
}