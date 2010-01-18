using System.Net;
using RBD.TPC.Msg;

using RBD.Msg.Client;

namespace RBD.TPC.COR
{
    public class WaitingState : CoordinatorState
    {

        override public void onTimeout()
        {
            coordinator.abortTransaction(new RBD.TPC.Msg.TimeoutMessage());
        }

        override public void onYesForCommit(IPAddress node)
        {
            coordinator.processAnswer(node, new PreCommitMessage(), new PreparedState());
        }

        override public void onNoForCommit(IPAddress node)
        {
            coordinator.abortTransaction(new ConflictMessage());
        }
    }
}