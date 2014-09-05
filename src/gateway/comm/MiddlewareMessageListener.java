
package gateway.comm;


import gateway.protocol.basic_message.MiddlewareMessage;


/**
 * @author Davide Roveran
 * @version 1.0
 *
 */
public interface MiddlewareMessageListener 
{
   void messageReceived(MiddlewareMessage message);
}
