// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD
{
    public class NodeSyncInfo
    {
        int BeatsOutOfSync;
        //long LastBeat; // time in miliseconds
        DateTime LastBeat;

        public NodeSyncInfo()
        {
            InSync();
        }

        public void InSync()
        {
            BeatsOutOfSync = 0;
            //LastBeat = System.currentTimeMillis();
            LastBeat = DateTime.Now;
        }

        public void BeatOutOfSync()
        {
            ++BeatsOutOfSync;
            //LastBeat = System.currentTimeMillis();
            LastBeat = DateTime.Now;
        }

        public int getBeatsOutOfSync()
        {
            return BeatsOutOfSync;
        }

        public long getMsSinceLastBeat()
        {
            return (DateTime.Now - LastBeat).Milliseconds;
            //return System.currentTimeMillis() - LastBeat;
        }
    }
}
