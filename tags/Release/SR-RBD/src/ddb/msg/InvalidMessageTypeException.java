package ddb.msg;

public class InvalidMessageTypeException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InvalidMessageTypeException(int type)
	{
		super("Invalid message type: " + Integer.toString(type));
	}
}
