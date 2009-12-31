package ddb.db;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Przechowuje wartosci wiersza tabeli. Przechowuje rowniez referencje do tabeli,
 * co pozwala na pobieranie wartosci wg nazwy kolumny.
 * @author mateusz
 */
public class TableRow implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Utworz wiersz do tabeli
     * @param table tabela, do ktorej nalezy wiersz
     * @param values wartosci w wierszu, w takiej kolejnosci, jak nazwy kolumn
     * w tabeli
     */
    public TableRow(DatabaseTable table, ArrayList<Object> values) {
        this.table = table;
        this.values = values;
        if(table.getColumnCount() != values.size()){
            throw new IndexOutOfBoundsException("table.getColumnCount() != values.size()");
        }
    }

    public ArrayList<Object> getValues() {
        return values;
    }

    public DatabaseTable getTable() {
        return table;
    }

    /**
     * Pobierz wartość z danej kolumny
     * @param columnIndex numer kolumny
     * @return wartosc w tej kolumnie
     */
    public Object getValue(int columnIndex) {
        return values.get(columnIndex);
    }
    
    /** Wartosci w poszczegolnych kolumnach. */
    private ArrayList<Object> values;
    private DatabaseTable table;
}
