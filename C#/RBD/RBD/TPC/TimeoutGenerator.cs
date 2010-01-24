// +

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
            stopped = false;
            Timer t = new Timer(new TimerCallback(this.runTimer), null, miliseconds, Timeout.Infinite);
        }

        private void runTimer(object o)
        {
            if (!stopped)
            {
                timeoutListener.onTimeout();
            }
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
