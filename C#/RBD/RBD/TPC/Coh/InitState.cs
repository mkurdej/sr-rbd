// +

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
                    this.cohort.setTableName(message.getTableName());
                    this.cohort.setQueryString(message.getQueryString());
                    this.cohort.setCreate(message.isCreate());
                    int coordinatorTableVersion = message.getTableVersion();
                    String tableName = message.getTableName();
                    int localTableVersion = this.cohort.getDatabaseState().getTableVersion(tableName);
                    if (coordinatorTableVersion < localTableVersion)
                    {
                        throw new LessTableVersionException(tableName, coordinatorTableVersion, localTableVersion);
                    }
                    this.cohort.getDatabaseState().lockTable(message.getTableName());
                    this.cohort.replyToCoordinator(new YesForCommitMessage());
                    this.cohort.changeState(new PreparedState());
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