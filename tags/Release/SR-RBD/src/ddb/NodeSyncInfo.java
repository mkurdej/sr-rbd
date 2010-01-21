package ddb;


public class NodeSyncInfo
{
	private int BeatsOutOfSync;
	private long LastBeat; // time in miliseconds
	
	public NodeSyncInfo()
	{
		InSync();
	}
	
	public void InSync()
	{
		BeatsOutOfSync = 0;
		LastBeat = System.currentTimeMillis();
	}
	
	public void BeatOutOfSync()
	{
		++BeatsOutOfSync;
		LastBeat = System.currentTimeMillis();
	}

	public int getBeatsOutOfSync() {
		return BeatsOutOfSync;
	}

	public long getMsSinceLastBeat() {
		return System.currentTimeMillis() - LastBeat;
	}
}