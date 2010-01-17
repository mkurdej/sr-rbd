package ddb.db;
/**
 * 
 * @author PKalanski
 *
 * Klasa reprezentujaca stan tabeli.
 */
public class TableState {
	/**
	 * Czy tabela jest zablokowana
	 */
	private boolean locked;
	/**
	 * Nazwa tabeli
	 */
	private String name;
	/**
	 * Numer wersji
	 */
	private int version;
	/**
	 * Zapytanie tworzace tabele
	 */
	private String createStatement;
	/**
	 * 
	 * @param name nazwa tabeli
	 * @param createStatement zapytanie tworzace tabele
	 */
	public TableState(String name, String createStatement) {
		this.name = name;
		this.createStatement = createStatement;
		this.version = 0;
		this.locked = false;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCreateStatement() {
		return this.createStatement;
	}
	
	public void setCreateStatement(String createStatement) {
		this.createStatement = createStatement;
	}
	
	public int getVersion() {
		return this.version;
	}
	
	public void incrementVersion() {
		this.version++;
	}
	
	public void lockTable() throws TableLockedException {
		if(locked) {
			throw new TableLockedException();
		}
		locked = true;
	}
	
	public void unlockTable() {
		locked = false;
	}
	
}
