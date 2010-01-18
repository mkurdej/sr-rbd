// +--

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using RBD.Msg;

namespace RBD.Communication
{
    class MessageFactory
    {
        const String LOGGING_NAME = "MessageFactory";

        /**
         * Private constructor -- there are only static methods
         */
        private MessageFactory() { }

        public static Message create(Message.MessageType type)
        {
            switch (type)
            {
                //case Message.MessageType.RESTORE_INCENTIVE: return new ddb.restore.msg.RestoreIncentive();
                //case Message.MessageType.RESTORE_ACK: return new ddb.restore.msg.RestoreAck();
                //case Message.MessageType.RESTORE_NACK: return new ddb.restore.msg.RestoreNack();
                //case Message.MessageType.RESTORE_TABLELIST: return new ddb.restore.msg.RestoreTableList();
                //case Message.MessageType.RESTORE_TABLE: return new ddb.restore.msg.RestoreTable();

                //case Message.MessageType.TPC_ABORT: return new ddb.tpc.msg.AbortMessage();
                //case Message.MessageType.TPC_PRECOMMIT: return new ddb.tpc.msg.PreCommitMessage();
                //case Message.MessageType.TPC_ACKPRECOMMIT: return new ddb.tpc.msg.AckPreCommitMessage();
                //case Message.MessageType.TPC_CANCOMMIT: return new ddb.tpc.msg.PreCommitMessage();
                //case Message.MessageType.TPC_YESFORCOMMIT: return new ddb.tpc.msg.YesForCommitMessage();
                //case Message.MessageType.TPC_DOCOMMIT: return new ddb.tpc.msg.DoCommitMessage();
                //case Message.MessageType.TPC_HAVECOMMITED: return new ddb.tpc.msg.HaveCommittedMessage();
                //case Message.MessageType.TPC_NOFORCOMMIT: return new ddb.tpc.msg.NoForCommitMessage();
                //case Message.MessageType.TPC_ERROR: return new ddb.tpc.msg.ErrorMessage();
                //case Message.MessageType.TRANSACTION_MESSAGE: return new ddb.tpc.msg.TransactionMessage();

                //case Message.MessageType.CLIENT_SUCCESS: return new ddb.msg.client.SuccessMessage();
                //case Message.MessageType.CLIENT_CONFLICT: return new ddb.msg.client.ConflictMessage();
                //case Message.MessageType.CLIENT_ERROR: return new ddb.msg.client.ErrorMessage();
                //case Message.MessageType.CLIENT_RESULTSET: return new ddb.msg.client.ResultsetMessage();
                //case Message.MessageType.CLIENT_TIMEOUT: return new ddb.msg.client.TimeoutMessage();

                case Message.MessageType.HELLO_MESSAGE: return new RBD.Msg.HelloMessage();
            }

            Logger.getInstance().log("Unknown message type", LOGGING_NAME, Logger.Level.SEVERE);
            return null;
        }
    }
}
