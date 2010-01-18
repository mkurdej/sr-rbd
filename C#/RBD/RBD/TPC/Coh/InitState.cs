using System;
using RBD.TPC.Msg;

namespace RBD.TPC.COH
{
    public class InitState : CohortState
    {

        override public void onTimeout()
        {
            this.cohort.endTransaction();
            this.cohort.setState(new AbortState());
        }

        override public void onCanCommit(CanCommitMessage message)
        {

            try
            {
                this.cohort.tableName = message.TableName;
                this.cohort.setQueryString(message.QueryString);
                this.cohort.setCreate(message.IsCreate);
                int coordinatorTableVersion = message.TableVersion;
                string tableName = message.TableName;
                int localTableVersion = this.cohort.databaseState.getTableVersion(tableName);
                if (coordinatorTableVersion < localTableVersion)
                {
                    throw new LessTableVersionException(tableName, coordinatorTableVersion, localTableVersion);
                }
                this.cohort.databaseState.lockTable(message.TableName);
                this.cohort.replyToCoordinator(new YesForCommitMessage());
                this.cohort.changeState(new PreparedState());
                if (cohort.isCreate())
                {
                    this.cohort.databaseState.addTable(cohort.tableName, cohort.getQueryString());
                }
            }
            catch (Exception e)
            {
                Logger.getInstance().log(e.Message, "KOHORT", Logger.Level.WARNING);
                this.cohort.replyToCoordinator(new NoForCommitMessage());
                this.cohort.endTransaction();
                this.cohort.setState(new AbortState());
            }

        }
    }
}