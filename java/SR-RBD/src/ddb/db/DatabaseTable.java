package ddb.db;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Obiekty tej klasy przechowują całe tabele wczytane z bazy danych. Obiekty tej
 * klasy można przesyłać przez sieć poprzez serializację.
 * @author Mateusz
 */
public class DatabaseTable implements Serializable 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Buduje tablelę zawierającą wszystkie kolumny i wiersze, które znajdują się
     * w resultSet. Dane z obiektu resultSet są wczytywane z bazy danych i ładowane
     * do pamięci.
     * @param resultSet wynik zapytania, który jest w całości ładowany do pamięci.
     */
    public DatabaseTable(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        tableName = metaData.getTableName(1);

        for(int i=1; i <= metaData.getColumnCount(); i++){
            tableNames.add(metaData.getTableName(i));
            columnNames.add(metaData.getColumnName(i));
        }

        while(resultSet.next()){
            ArrayList<Object> newRow;
            newRow = new ArrayList<Object>();
            for(int i=0; i < columnNames.size(); i++){
                newRow.add(resultSet.getObject(i + 1));
            }
            rows.add(new TableRow(this, newRow));
        }
    }
    
    public String getTableName() {
        return tableName;
    }
    public int getColumnCount(){
        return columnNames.size();
    }
    public int getRowCount(){
        return rows.size();
    }
    /**
     * @return Nazwy tabel z ktorych pochodza poszczegolne kolumny
     */
    public ArrayList<String> getTableNames() {
        return tableNames;
    }
    public ArrayList< String > getColumnNames(){
        return columnNames;
    }
    public ArrayList<TableRow> getRows() {
        return rows;
    }
    public TableRow getRow(int idx){
        return rows.get(idx);
    }
    
    @Override
    public String toString() {
                StringBuilder builder = new StringBuilder();
                
                for(String col : getColumnNames()) {
                        builder.append( col );
                        builder.append( "\t" );
                }
                
                builder.append( "\n" );
                
                for(TableRow row : getRows()) {
                        builder.append( row.toString() );
                        builder.append( "\n" );
                }
                
                return builder.toString();
        
    }

    /** Nazwa tabeli */
    private String tableName;
    /** Nazwy kolumn w tabeli. */
    ArrayList< String > columnNames = new ArrayList<String>();
    /** Nazwy tabel zrodlowych, z ktorych pochodza kolumny */
    ArrayList< String > tableNames = new ArrayList<String>();
    /** Wiersze w tabeli. */
    ArrayList< TableRow > rows = new ArrayList<TableRow>();
    
}
