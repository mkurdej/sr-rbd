// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace RBD.TPC
{
    public class TimeoutGenerator
    {
        private volatile bool stopped = false;
        private TimeoutListener timeoutListener;

        private TimerCallback timerCallback = new TimerCallback(TimeoutCallback);

        static void TimeoutCallback(object o)
        {
            TimeoutGenerator tg = (TimeoutGenerator)o;
            if (!tg.stopped)
            {
                tg.timeoutListener.onTimeout();
            }
        }

        public void setTimeoutListener(TimeoutListener timeoutListener)
        {
            this.timeoutListener = timeoutListener;
        }

        /** 
         * <!-- begin-UML-doc -->
         * <!-- end-UML-doc -->
         * @param miliseconds
         * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        public void startTimer(long miliseconds)
        {
            Timer t = new Timer(timerCallback, this, miliseconds, Timeout.Infinite);
        }

        /** 
         * <!-- begin-UML-doc -->
         * <!-- end-UML-doc -->
         * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        public void stopTimer()
        {
            stopped = true;
        }
    }
}
