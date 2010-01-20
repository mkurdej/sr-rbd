using RBD.Communication;

namespace RBD.Restore.Msg
{
    public class RestoreAck : RestoreMessage {

        override public void FromBinary(DataInputStream s)
        {
		    // empty

	    }

        override public MessageType GetMessageType()
        {
		    return MessageType.RESTORE_ACK;
	    }

        override public void ToBinary(DataOutputStream s)
        {
		    // empty
	    }

    }
}