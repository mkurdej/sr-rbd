package ddb.db;

import java.io.IOException;

public class DumpTableException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMessage;
	
	public DumpTableException(IOException e)
	{
		errorMessage = e.getMessage();
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}
}
