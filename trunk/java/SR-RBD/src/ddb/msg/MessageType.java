package ddb.msg;


public enum MessageType {
	RESTORE_INCENTIVE,
	RESTORE_ACK,
	RESTORE_NACK,
	RESTORE_TABLELIST,
	RESTORE_TABLE,
	TPC_ABORT,
	TPC_PRECOMMIT,
	TPC_ACKPRECOMMIT,
	TPC_CANCOMMIT,
	TPC_YESFORCOMMIT,
	TPC_DOCOMMIT,
	TPC_HAVECOMMITED,
	TPC_NOFORCOMMIT,
	TPC_ERROR,
	TRANSACTION_MESSAGE,
	CLIENT_SUCCESS,
	CLIENT_CONFLICT,
	CLIENT_ERROR,
	CLIENT_RESULTSET,
	CLIENT_TIMEOUT,
	HELLO_MESSAGE,
	TIMEOUT_MESSAGE;

	public static MessageType fromInt(int type) throws InvalidMessageTypeException 
	{
		if(type < 0 || type >=  MessageType.values().length)
			throw new InvalidMessageTypeException(type);
		
		return MessageType.values()[type];
	}
}
