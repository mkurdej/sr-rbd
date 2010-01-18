// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.DB
{
    public interface DatabaseState
    {
     /** 
	 * <!-- begin-UML-doc -->
	 * <p>
	 *     Blokuje tabele. Gdy tabela jest juz zablokowana to zostaje rzucony wyjatek.
	 * </p>
	 * <!-- end-UML-doc -->
	 * @param tableName
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
        void lockTable(String tableName); // throws TableLockedException;
        /** 
         * <!-- begin-UML-doc -->
         * <p>
         *     Dokonuje odblokowania tabeli. Tabela od tej pory moze zostac uzyta przez inna transakcje.
         * </p>
         * <!-- end-UML-doc -->
         * @param tableName
         * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        void unlockTable(String tableName);
        /**
         * Zwieksza numer wersji tabeli. Jest to liczba zdan DML wykonanych na tej tabeli.
         * 
         * @param tableName nazwa tabeli
         */
        void incrementTableVersion(String tableName);
        /**
         * Dodaje nowa tabele
         * @param tableName nazwa tabeli
         * @param createStatement zapytanei tworzace tabele
         */
        void addTable(String tableName, String createStatement);
        /**
         * Pobiera numer wersji tabeli
         * @param tableName nazwa tabeli
         */
        int getTableVersion(String tableName);
        /**
         * UStawia numer wersji tabeli
         * @param tableName nazwa tabeli
         * @param version numer wersji
         */
        void setTableVersion(String tableName, int version);
    }
}
