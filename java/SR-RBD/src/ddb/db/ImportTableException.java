package ddb.db;

import java.io.IOException;

public class ImportTableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImportTableException(IOException e) {
		super(e);
	}

}
