// +

using System.Net;
using RBD.TPC.Msg;

using RBD.Msg.Client;

namespace RBD.TPC.COR
{
    public class WaitingState : CoordinatorState
    {
        private static string LOGGING_NAME = "Coordinator.WaitingState";

        override public void onTimeout()
        {
            coordinator.abortTransaction(new RBD.Msg.Client.TimeoutMessage());
        }

        override public void onYesForCommit(IPEndPoint node)
        {
            coordinator.processAnswer(node, new PreCommitMessage(), new PreparedState());
            Logger.getInstance().log("Got answer "
 				+ coordinator.getAnswerCount() + " of "
 				+ coordinator.getNodesCount(), LOGGING_NAME,
 				Logger.Level.INFO);
        }

        override public void onNoForCommit(IPEndPoint node)
        {
            Logger.getInstance().log("Got no - aborting!", LOGGING_NAME, Logger.Level.INFO);
            coordinator.abortTransaction(new ConflictMessage());
        }
    }
}