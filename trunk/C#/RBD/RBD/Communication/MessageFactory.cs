// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Msg;
using RBD.Msg.Client;
using RBD.Restore.Msg;
using RBD.TPC.Msg;

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
                case Message.MessageType.RESTORE_INCENTIVE: return new RBD.Restore.Msg.RestoreIncentive();
                case Message.MessageType.RESTORE_ACK: return new RBD.Restore.Msg.RestoreAck();
                case Message.MessageType.RESTORE_NACK: return new RBD.Restore.Msg.RestoreNack();
                case Message.MessageType.RESTORE_TABLELIST: return new RBD.Restore.Msg.RestoreTableList();
                case Message.MessageType.RESTORE_TABLE: return new RBD.Restore.Msg.RestoreTable();

                case Message.MessageType.TPC_ABORT: return new RBD.TPC.Msg.AbortMessage();
                case Message.MessageType.TPC_PRECOMMIT: return new RBD.TPC.Msg.PreCommitMessage();
                case Message.MessageType.TPC_ACKPRECOMMIT: return new RBD.TPC.Msg.AckPreCommitMessage();
                case Message.MessageType.TPC_CANCOMMIT: return new RBD.TPC.Msg.PreCommitMessage();
                case Message.MessageType.TPC_YESFORCOMMIT: return new RBD.TPC.Msg.YesForCommitMessage();
                case Message.MessageType.TPC_DOCOMMIT: return new RBD.TPC.Msg.DoCommitMessage();
                case Message.MessageType.TPC_HAVECOMMITED: return new RBD.TPC.Msg.HaveCommittedMessage();
                case Message.MessageType.TPC_NOFORCOMMIT: return new RBD.TPC.Msg.NoForCommitMessage();
                case Message.MessageType.TPC_ERROR: return new RBD.TPC.Msg.ErrorMessage();
                case Message.MessageType.TRANSACTION_MESSAGE: return new RBD.TPC.Msg.TransactionMessage();

                case Message.MessageType.CLIENT_SUCCESS: return new RBD.Msg.Client.SuccessMessage();
                case Message.MessageType.CLIENT_CONFLICT: return new RBD.Msg.Client.ConflictMessage();
                case Message.MessageType.CLIENT_ERROR: return new RBD.Msg.Client.ErrorMessage();
                case Message.MessageType.CLIENT_RESULTSET: return new RBD.Msg.Client.ResultsetMessage();
                case Message.MessageType.CLIENT_TIMEOUT: return new RBD.Msg.Client.TimeoutMessage();

                case Message.MessageType.HELLO_MESSAGE: return new RBD.Msg.HelloMessage();
            }

            Logger.getInstance().log("Unknown message type", LOGGING_NAME, Logger.Level.SEVERE);
            return null;
        }
    }
}
