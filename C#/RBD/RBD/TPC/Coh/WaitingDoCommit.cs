namespace RBD.TPC.COH
{
    public class WaitingDoCommit : CohortState {
        override public void onTimeout()
        {
		    this.cohort.commitTransaction();
	    }

        override public void onDoCommit()
        {
		    this.cohort.commitTransaction();
	    }
    }
}