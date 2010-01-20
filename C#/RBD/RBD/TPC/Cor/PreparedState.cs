using System.Net;
using RBD.TPC.Msg;

namespace RBD.TPC.COR
{
public class PreparedState : CoordinatorState {
    private static string LOGGING_NAME = "Coordinator.PreparedState";

    override public void onTimeout()
    {
        Logger.getInstance().log("Got no - aborting!", LOGGING_NAME, Logger.Level.INFO);
		coordinator.abortTransaction(new TimeoutMessage());
	}

    override public void onAckPreCommit(IPAddress node)
    {
		coordinator.processAnswer(node, new DoCommitMessage(), new CommitState());
        Logger.getInstance().log(
           "Got answer "
            + coordinator.getAnswerCount() + " of "
            + coordinator.getNodesCount(),
            LOGGING_NAME,
 			Logger.Level.INFO);
	}
}
}