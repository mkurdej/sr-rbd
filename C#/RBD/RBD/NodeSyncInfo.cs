// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD
{
    class NodeSyncInfo
    {
        private int BeatsOutOfSync;
        //private long LastBeat; // time in miliseconds
        private DateTime LastBeat;

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
