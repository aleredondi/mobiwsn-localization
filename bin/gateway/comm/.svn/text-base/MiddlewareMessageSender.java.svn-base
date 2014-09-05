
package gateway.comm;


import common.exceptions.MiddlewareException;
import common.exceptions.ResponseTimeoutException;
import gateway.protocol.basic_message.MiddlewareMessage;


/**
 * @author Davide Roveran
 *
 * sends a MiddlewareMessage and waits for the response message from the wsn
 * and any further message processing done by the wsnGateway
 */
public interface MiddlewareMessageSender 
{
	/**
	 * sends the message passed and waits for the MiddlewareMessage response
	 * if expected
	 * @param message MiddlewareMessage object to be sent to the wsn
	 * @throws MiddlewareException 
	 */
	public void sendMessageTask(MiddlewareMessage message) throws ResponseTimeoutException, MiddlewareException;

}
