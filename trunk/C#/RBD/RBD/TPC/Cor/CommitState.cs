// +

using RBD.Communication;
using RBD.TPC.Msg;
using RBD.Msg.Client;

namespace RBD.TPC.COR
{
    public class CommitState : CoordinatorState {

        override public void onTimeout()
        {
            coordinator.abortTransaction(new RBD.Msg.Client.TimeoutMessage());
	    }

        override public void onHaveCommitted(HaveCommittedMessage message)
        {
		    TcpSender.getInstance().sendToNode(new SuccessMessage(), coordinator.getClientAddress());
		    coordinator.endTransaction();
	    }
    }
}