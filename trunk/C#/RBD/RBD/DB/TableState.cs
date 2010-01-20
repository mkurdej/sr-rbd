using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.DB
{
    class TableState
    {
        /**
	 * Czy tabela jest zablokowana
	 */
	private bool locked;
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
	
	/**
	 * 
	 * @param name nazwa tabeli
	 * @param createStatement zapytanie tworzace tabele
	 */
	public TableState(String name) {
		this.name = name;
		this.version = 0;
		this.locked = false;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getVersion() {
		return this.version;
	}
	
	public void incrementVersion() {
		this.version++;
	}
	
	public void lockTable() //throws TableLockedException 
    {
		if(locked) {
			throw new TableLockedException();
		}
		locked = true;
	}
	
	public void unlockTable() {
		locked = false;
	}

	public void setVersion(int version) {
		this.version = version;
	}
    }
}
