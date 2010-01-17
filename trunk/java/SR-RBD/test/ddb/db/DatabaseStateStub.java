package ddb.db;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class DatabaseStateStub implements DatabaseState {
	private Set<InetSocketAddress> nodeList;
	private Set<String> locked;
	
	public DatabaseStateStub() {
		this.locked = new HashSet<String>();
	}
	
	@Override
	public Set<InetSocketAddress> getNodes() {
		return nodeList;
	}

	@Override
	public void lockTable(String tableName) throws TableLockedException{
		if(isLocked(tableName)) {
			throw new TableLockedException();
		}
		locked.add(tableName);
	}

	@Override
	public void unlockTable(String tableName) {
		locked.remove(tableName);
	}

	
	public void setNodeList(Set<InetSocketAddress> nodeList) {
		this.nodeList = nodeList;
	}
	
	public boolean isLocked(String tableName){
		return locked.contains(tableName);
	}

	@Override
	public void addTable(String tableName, String createStatement) {
		
	}

	@Override
	public int getTableVersion(String tableName) {
		return 0;
	}

	@Override
	public void incrementTableVersion(String tableName) {
		
	}
}
