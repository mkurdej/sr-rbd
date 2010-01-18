using RBD.TPC.Msg;

namespace RBD.TPC.COH
{
    public class PreparedState : CohortState {
        override public void onTimeout()
        {
		    this.cohort.endTransaction();
		    this.cohort.setState(new AbortState());
	    }

        override public void onPreCommit()
        {
		    this.cohort.replyToCoordinator(new AckPreCommitMessage());
		    this.cohort.changeState(new WaitingDoCommit());
	    }
    }
}