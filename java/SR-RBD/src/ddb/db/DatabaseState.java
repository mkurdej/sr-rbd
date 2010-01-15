/**
 * 
 */
package ddb.db;

import java.util.Set;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author User
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface DatabaseState {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>
	 *     Blokuje tabele. Gdy tabela jest juz zablokowana to zostaje rzucony wyjatek.
	 * </p>
	 * <!-- end-UML-doc -->
	 * @param tableName
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void lockTable(String tableName) throws TableLockedException;

	/** 
	 * <!-- begin-UML-doc -->
	 * Zwraca liste wezlow
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Set<String> getNodes();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>
	 *     Dokonuje odblokowania tabeli. Tabela od tej pory moze zostac uzyta przez inna transakcje.
	 * </p>
	 * <!-- end-UML-doc -->
	 * @param tableName
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void unlockTable(String tableName);
	/**
	 * Zwieksza numer wersji tabeli. Jest to liczba zdan DML wykonanych na tej tabeli.
	 * 
	 * @param tableName nazwa tabeli
	 */
	public void incrementTableVersion(String tableName);
	/**
	 * Dodaje nowa tabele
	 * @param tableName nazwa tabeli
	 * @param createStatement zapytanei tworzace tabele
	 */
	public void addTable(String tableName, String createStatement);
}