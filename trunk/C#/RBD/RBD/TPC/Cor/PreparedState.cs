using System.Net;
using RBD.TPC.Msg;

namespace RBD.TPC.COR
{
public class PreparedState : CoordinatorState {
    override public void onTimeout()
    {
		coordinator.abortTransaction(new TimeoutMessage());
	}

    override public void onAckPreCommit(IPAddress node)
    {
		coordinator.processAnswer(node, new DoCommitMessage(), new CommitState());
	}
}
}