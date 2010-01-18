using RBD.TPC.Msg;

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
		
		coordinator.setNodeList( TcpSender.getInstance().getAllServerNodes() );
		SqlParser parser = new SqlParserImpl();
		
		if(!parser.parse(message.QueryString))
		{
			Logger.getInstance().log("Unparsable query - send back error and end transaction", LOGGING_NAME, Logger.Level.INFO);
			
			TcpSender.getInstance().sendToNode(new ErrorMessage("Bad query"), message.Sender);
			coordinator.endTransaction();
			return;
		}
		
		coordinator.setQueryString( parser.getQueryString() );
		coordinator.tableName = parser.getTableName();
		
		
		if(parser.getOperationType().equals(SqlOperationType.SELECT))
		{
			Logger.getInstance().log("Select query detected", LOGGING_NAME, Logger.Level.INFO);
			coordinator.processSelect();
		}
		else 
		{
			CanCommitMessage msg = new CanCommitMessage();
			msg.TableName = parser.getTableName();
			msg.QueryString = parser.getQueryString();
			msg.TableVersion = coordinator.databaseState.getTableVersion(msg.TableName);
			if(parser.getOperationType().equals(SqlOperationType.CREATE)) {
				msg.IsCreate = true;
			}
			else {
                msg.IsCreate = false;
			}
			coordinator.broadcastMessage(msg);
			coordinator.changeState(new WaitingState());
		}
	}
}
}