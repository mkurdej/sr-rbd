﻿// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Msg;

namespace RBD.TPC
{
    public interface MessageRecipient
    {
        void processMessage(Message message);
    }
}
