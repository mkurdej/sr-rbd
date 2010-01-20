using RBD.Communication;
using RBD.Msg;

namespace RBD.Restore.Msg
{
    public class RestoreIncentive : RestoreMessage
    {

        override public void FromBinary(DataInputStream dis)
        {
		    // empty
	    }

        public override MessageType GetMessageType()
        {
		    return MessageType.RESTORE_INCENTIVE;
	    }

        public override void ToBinary(DataOutputStream s)
        {
		    // empty
	    }
    }
}
