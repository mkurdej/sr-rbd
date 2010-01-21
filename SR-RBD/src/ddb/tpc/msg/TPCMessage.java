/**
 * 
 */
package ddb.tpc.msg;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.Message;

/** 
 * @author PKalanski
 */
public abstract class TPCMessage extends Message {
	/** 
	 */
	private String transactionId;

	/** 
	 * @return
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/** 
	 * @param transactionId
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		transactionId = s.readString();
		
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		s.writeString(transactionId);
	}
}