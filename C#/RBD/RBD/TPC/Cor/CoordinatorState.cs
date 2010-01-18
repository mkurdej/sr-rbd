using System.Net;
using RBD;
using RBD.TPC.Msg;

namespace RBD.TPC.COR
{
abstract public class CoordinatorState : TimeoutListener {
	protected CoordinatorImpl coordinator;
	/**
	 * Nazwa logera
	 */
	private static string LOGGER_NAME = "KOORDYNATOR";
	
	public void setCoordinator(CoordinatorImpl coordinator) {
		this.coordinator = coordinator;
	}

	/** 
	 * @param node
	 */
    virtual public void onYesForCommit(IPAddress node)
    {
		Logger.getInstance().log("Nie oczekiwano wiadomosci: YesForCommit", LOGGER_NAME, Logger.Level.WARNING);
	}

	/** 
	 * @param node
	 */
    virtual public void onNoForCommit(IPAddress node)
    {
        Logger.getInstance().log("Nie oczekiwano wiadomosci: NoForCommit", LOGGER_NAME, Logger.Level.WARNING);
	}

	/** 
	 * @param node
	 */
    virtual public void onAckPreCommit(IPAddress node)
    {
        Logger.getInstance().log("Nie oczekiwano wiadomosci: AckPreCommit", LOGGER_NAME, Logger.Level.WARNING);
	}

	/** 
	 * @param message
	 */
    virtual public void onHaveCommitted(HaveCommittedMessage message)
    {
        Logger.getInstance().log("Nie oczekiwano wiadomosci: HaveCommitted", LOGGER_NAME, Logger.Level.WARNING);
	}
	/** 
	 */
    virtual public void onTransaction(TransactionMessage message)
    {
        Logger.getInstance().log("Nie oczekiwano wiadomosci: TransactionMessage", LOGGER_NAME, Logger.Level.WARNING);
	}

	/** 
	 * @param message
	 */
    virtual public void onErrorMessage(ErrorMessage message)
    {
        Logger.getInstance().log("Nie oczekiwano wiadomosci: ErrorMessage", LOGGER_NAME, Logger.Level.WARNING);
	}

    virtual public void onTimeout()
    {
		// empty
	}
}
}