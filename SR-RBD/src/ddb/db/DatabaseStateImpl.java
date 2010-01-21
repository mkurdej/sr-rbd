/**
 * 
 */
package ddb.db;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ddb.Logger;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class DatabaseStateImpl implements DatabaseState {
	private Map<String, TableState> tables;
	private final static String LOGGING_NAME = "DatabaseStateImpl";
	
	private DatabaseStateImpl() {
		this.tables = new HashMap<String, TableState>();
	}
	
	private static DatabaseStateImpl instance = null;
	
	/** 
	 * /* (non-Javadoc)
	 *  * @see DatabaseState#lockTable(String tableName)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void lockTable(String tableName) throws TableLockedException {
		getTableByName(tableName).lockTable();
	}

	public void setTableVersion(String tableName, int version) {
		getTableByName(tableName).setVersion(version);
	}
	
	public void incrementTableVersion(String tableName) {
		getTableByName(tableName).incrementVersion();
	}
	
	@Override
	public void addTable(String tableName) 
	{
		if( tables.get(tableName) != null )
		{
			Logger.getInstance().log("Adding table that exists", LOGGING_NAME, Logger.Level.WARNING);
			return;
		}
		
		tables.put(tableName, new TableState(tableName));
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see DatabaseState#unlockTable(String tableName)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void unlockTable(String tableName) {
		getTableByName(tableName).unlockTable();
	}


	@Override
	public int getTableVersion(String tableName) {
		
		return getTableByName(tableName).getVersion();
	}
	
	protected TableState getTableByName(String tableName)
	{
		TableState ts = tables.get(tableName);
		
		if( ts == null )
		{
			ts = new TableState(tableName);
			tables.put(tableName, ts);
		}
		
		return ts;
	}
	
	public List<TableVersion> getTableVersions()
	{
		List<TableVersion> result = new LinkedList<TableVersion>();
		
		for(Map.Entry<String, TableState> e : tables.entrySet())
			result.add(new TableVersion(e.getKey(), e.getValue().getVersion()));
		
		return result;
	}
	
	public boolean checkSync(List<TableVersion> tvs) {
		
		// check tables
		for(TableVersion tv : tvs)
		{
			TableState ts = getTableByName(tv.getTableName());
			
			if(ts.getVersion() > tv.getVersion())
				return false;
		}
		
		return true;
	}
	
	synchronized public static DatabaseStateImpl getInstance() {
		if(instance == null) {
			instance = new DatabaseStateImpl();
		}
		
		return instance;
	}
}