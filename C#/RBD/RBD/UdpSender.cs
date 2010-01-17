using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD
{
    interface UdpSender
    {
        void sendToAll(Message msg);
    }
}
