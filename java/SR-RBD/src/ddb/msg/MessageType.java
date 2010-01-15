package ddb.msg;


public enum MessageType {
	RESTORE_INCENTIVE,
	RESTORE_ACK,
	RESTORE_NACK,
	RESTORE_TABLELIST,
	RESTORE_TABLE,
	TRANSACTION_MESSAGE,
	HELLO_MESSAGE;

	public static MessageType fromInt(int type) throws InvalidMessageTypeException 
	{
		if(type < 0 || type >=  MessageType.values().length)
			throw new InvalidMessageTypeException(type);
		
		return MessageType.values()[type];
	}
}
