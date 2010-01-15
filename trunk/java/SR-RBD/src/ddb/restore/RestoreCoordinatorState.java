package ddb.restore;

import ddb.tpc.TimeoutListener;

public abstract class RestoreCoordinatorState implements TimeoutListener{
	
	protected RestoreCoordinatorImpl coordinator;
	
	public void setCoordinator(RestoreCoordinatorImpl coordinator) {
		this.coordinator = coordinator;
	}

	/** 
	 * @param node
	 */
	abstract public void onRestoreAck(String node);
	
	/**
	 * 
	 * @param node
	 */
	abstract public void onRestoreNack(String node);


}
