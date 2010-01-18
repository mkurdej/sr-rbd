// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.DB;
using RBD.Communication;

namespace RBD.Msg.Client
{
    class ErrorMessage : ClientResponse
    {
    public DBException Exception { get; set; }

	public ErrorMessage()
	{
		// empty
	}
	
	public ErrorMessage(String msg)
	{
		Exception = new DBException(msg);
	}
	
	override
	public void FromBinary(DataInputStream s) //throws IOException 
    {
		Exception = new DBException(s);
	}

	override
	public MessageType GetMessageType() {
		return MessageType.CLIENT_ERROR;
	}

	override
	public void ToBinary(DataOutputStream s) //throws IOException 
    {
		Exception.ToBinary(s);
	}
    }
}
