
package common.exceptions;


/**
 * @author Davide Roveran
 *
 */
public class ResponseTimeoutException extends Exception 
{

	/**
	 * Creates a new ResponseTimeoutException with the specified message
	 * @param strExceptionMessage
	 */
	public ResponseTimeoutException(String requestMessageName, String sourceId, String destinationId)
	{
		super("Timeout while waiting for response to : " + requestMessageName + 
				  "; Source Id: " + sourceId + 
				  "; Destination Id: " + destinationId);
	}
	
}
