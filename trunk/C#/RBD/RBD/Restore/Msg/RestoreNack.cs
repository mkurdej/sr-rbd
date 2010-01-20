using RBD.Communication;

namespace RBD.Restore.Msg
{
    public class RestoreNack : RestoreMessage {

        override public void FromBinary(DataInputStream s)
        {
		    // empty
	    }

        override public MessageType GetMessageType() {
		    return MessageType.RESTORE_NACK;
	    }

        override public void ToBinary(DataOutputStream s)
        {
		    // empty
	    }

    }
}
