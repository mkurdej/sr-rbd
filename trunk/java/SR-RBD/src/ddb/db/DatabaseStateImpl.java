/**
 * 
 */
package ddb.db;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class DatabaseStateImpl implements DatabaseState {
	private Map<String, TableState> tables;
	
	public DatabaseStateImpl() {
		this.tables = new HashMap<String, TableState>();
	}
	
	
	/** 
	 * /* (non-Javadoc)
	 *  * @see DatabaseState#lockTable(String tableName)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void lockTable(String tableName) throws TableLockedException {
		tables.get(tableName).lockTable();
	}

	
	public void incrementTableVersion(String tableName) {
		tables.get(tableName).incrementVersion();
	}
	
	public void addTable(String tableName, String createStatement) {
		tables.put(tableName, new TableState(tableName, createStatement));
	}
	
	public void removeTable(String tableName) {
		tables.remove(tableName);
	}
	/** 
	 * /* (non-Javadoc)
	 *  * @see DatabaseState#getNodes()
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Set<String> getNodes() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * /* (non-Javadoc)
	 *  * @see DatabaseState#unlockTable(String tableName)
	 * 
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void unlockTable(String tableName) {
		tables.get(tableName).unlockTable();
	}


	@Override
	public int getTableVersion(String tableName) {
		return tables.get(tableName).getVersion();
	}
	
	List<TableVersion> getTableVersions()
	{
		List<TableVersion> result = new LinkedList<TableVersion>();
		
		for(Map.Entry<String, TableState> e : tables.entrySet())
			result.add(new TableVersion(e.getKey(), e.getValue().getVersion()));
		
		return result;
	}
	
}