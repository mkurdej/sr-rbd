package ddb.tpc;

public class LessTableVersionException extends Exception {
	
	public LessTableVersionException(String tableName, int coordinatorTableVersion, int localTableVersion) {
		super("Less table version " + tableName + " coordinator: " + coordinatorTableVersion + " local:" + localTableVersion );
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
