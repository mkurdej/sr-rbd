// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.TPC
{
    public interface EndTransactionListener
    {
        void onEndTransaction(TPCParticipant participant);
    }
}
