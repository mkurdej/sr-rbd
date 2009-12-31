/**
 * 
 */
package ddb.tpc.msg;

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
}