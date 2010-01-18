// +-- TODO

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.TPC
{
    public class TimeoutGenerator {
	private volatile bool stopped = false;
	private TimeoutListener timeoutListener;

    //private Timer timer = new Timer();	
	
	public void setTimeoutListener(TimeoutListener timeoutListener) {
		this.timeoutListener = timeoutListener;
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param miliseconds
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void startTimer(long miliseconds) {
    //    timer.schedule(new TimerTask() {

    //        override
    //        public void run() {
    //            if(!stopped) {
    //                timeoutListener.onTimeout();
    //            }
    //        }
			
    //    }, miliseconds);
    }

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void stopTimer() {
		stopped = true;
	}
}
}
