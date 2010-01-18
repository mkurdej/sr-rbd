// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD
{
    public class NodeSyncInfo
    {
        int vBeatsOutOfSync;
        public int BeatsOutOfSync
        {
            get
            {
                return vBeatsOutOfSync;
            }
        }

        //long LastBeat; // time in miliseconds
        DateTime LastBeat;

        public NodeSyncInfo()
        {
            InSync();
        }

        public void InSync()
        {
            vBeatsOutOfSync = 0;
            //LastBeat = System.currentTimeMillis();
            LastBeat = DateTime.Now;
        }

        public void BeatOutOfSync()
        {
            ++vBeatsOutOfSync;
            //LastBeat = System.currentTimeMillis();
            LastBeat = DateTime.Now;
        }

        public long getMsSinceLastBeat()
        {
            return (DateTime.Now - LastBeat).Milliseconds;
            //return System.currentTimeMillis() - LastBeat;
        }
    }
}
