package ddb.db;

/**
 * Wyjątek rzucany, gdy w tabeli nie ma wskazanego pola
 * @author mateusz
 */
public class NoSuchTableFieldException extends Exception{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public NoSuchTableFieldException(String table, String column) {
        this.table = table;
        this.column = column;
    }

    @Override
    public String getMessage(){
        return "Niewłaściwa nazwa tabeli (" + table + ") lub nazwa kolumny (" + column + ").";
    }
    String table, column;
}
