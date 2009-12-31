/**
 * 
 */
package ddb.tpc;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TimeoutGenerator {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private volatile boolean stopped = false;
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TimeoutListener timeoutListener;

	private Timer timer = new Timer();	
	
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
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if(!stopped) {
					timeoutListener.onTimeout();
				}
			}
			
		}, miliseconds);
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