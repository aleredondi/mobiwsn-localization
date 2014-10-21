package remote_interfaces.clients.profiling_system;


/**
 * @author Antimo Barbato
 *
 */

public class ProfilingSystemException extends Exception 
{

	/**
	 * Creates a new MiddlewareException with the specified message
	 * @param strExceptionMessage
	 */
	public ProfilingSystemException(String strExceptionMessage)
	{
		super(strExceptionMessage);
	}
	
}