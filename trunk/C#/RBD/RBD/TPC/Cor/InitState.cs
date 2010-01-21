using RBD.TPC.Msg;
using RBD.DB;

namespace RBD.TPC.COR
{
public class InitState : CoordinatorState {
	
	private static string LOGGING_NAME = "Coordinator.InitState";

    override public void onTimeout()
    {
		coordinator.abortTransaction(new TimeoutMessage());
	}

    override public void onTransaction(TransactionMessage message)
    {
	
		Logger.getInstance().log("Processing transaction", LOGGING_NAME, Logger.Level.INFO);

        coordinator.setNodeCount(TcpSender.getInstance().getServerNodesCount());
		SqlParser parser = new SqlParserImpl();
		
		if(!parser.parse(message.QueryString))
		{
			Logger.getInstance().log("Unparsable query - send back error and end transaction", LOGGING_NAME, Logger.Level.INFO);
			
			TcpSender.getInstance().sendToNode(new ErrorMessage("Bad query"), message.Sender);
			coordinator.endTransaction();
			return;
		}
		
		coordinator.setQueryString( parser.getQueryString() );
		coordinator.TableName = parser.getTableName();
		
		
		if(parser.getOperationType().Equals(SqlOperationType.SELECT))
		{
			Logger.getInstance().log("Select query detected", LOGGING_NAME, Logger.Level.INFO);
			coordinator.processSelect();
		}
		else 
		{
			CanCommitMessage msg = new CanCommitMessage();
			msg.TableName = parser.getTableName();
			msg.QueryString = parser.getQueryString();
			msg.TableVersion = coordinator.DatabaseState.getTableVersion(msg.TableName);
			if(parser.getOperationType().Equals(SqlOperationType.CREATE)) {
				msg.IsCreate = true;
			}
			else {
                msg.IsCreate = false;
			}

            Logger.getInstance().log("Changing state to waiting", LOGGING_NAME, Logger.Level.INFO);

			coordinator.broadcastMessage(msg);
			coordinator.changeState(new WaitingState());
		}
	}
}
}