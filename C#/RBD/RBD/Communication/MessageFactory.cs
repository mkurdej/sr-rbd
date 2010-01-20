// +--

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
                case Message.MessageType.RESTORE_INCENTIVE: return new RestoreIncentive();
                case Message.MessageType.RESTORE_ACK: return new RestoreAck();
                case Message.MessageType.RESTORE_NACK: return new RestoreNack();
                case Message.MessageType.RESTORE_TABLELIST: return new RestoreTableList();
                case Message.MessageType.RESTORE_TABLE: return new RestoreTable();

                case Message.MessageType.TPC_ABORT: return new AbortMessage();
                case Message.MessageType.TPC_PRECOMMIT: return new PreCommitMessage();
                case Message.MessageType.TPC_ACKPRECOMMIT: return new AckPreCommitMessage();
                case Message.MessageType.TPC_CANCOMMIT: return new PreCommitMessage();
                case Message.MessageType.TPC_YESFORCOMMIT: return new YesForCommitMessage();
                case Message.MessageType.TPC_DOCOMMIT: return new DoCommitMessage();
                case Message.MessageType.TPC_HAVECOMMITED: return new HaveCommittedMessage();
                case Message.MessageType.TPC_NOFORCOMMIT: return new NoForCommitMessage();
                case Message.MessageType.TPC_ERROR: return new RBD.TPC.Msg.ErrorMessage();
                case Message.MessageType.TRANSACTION_MESSAGE: return new TransactionMessage();

                case Message.MessageType.CLIENT_SUCCESS: return new SuccessMessage();
                case Message.MessageType.CLIENT_CONFLICT: return new ConflictMessage();
                case Message.MessageType.CLIENT_ERROR: return new RBD.Msg.Client.ErrorMessage();
                case Message.MessageType.CLIENT_RESULTSET: return new ResultsetMessage();
                case Message.MessageType.CLIENT_TIMEOUT: return new RBD.Msg.Client.TimeoutMessage();

                case Message.MessageType.HELLO_MESSAGE: return new RBD.Msg.HelloMessage();
            }

            Logger.getInstance().log("Unknown message type", LOGGING_NAME, Logger.Level.SEVERE);
            return null;
        }
    }
}
