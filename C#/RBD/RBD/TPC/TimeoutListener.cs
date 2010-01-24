// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.TPC
{
    public interface TimeoutListener
    {
        void onTimeout();
    }
}
