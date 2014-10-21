
package gateway.comm;


import gateway.protocol.basic_message.MiddlewareMessage;


/**
 * 
 * @author Davide Roveran
 *<br>
 * 
 * Declares the basic interface methods used to interact (at a middleware level)
 * with the wireless sensor network attached to a Mesh Router
 */
public interface WSNInterface 
{
	/**
	 * Start interface to the mote network 
	 * (message sending, receiving, parsing ...)
	 */
	public void startInterface();
	
	/**
	 * 
	 * @return true if the interface to the mote network has been started
	 */
	public boolean isStarted();
	
	/**
	 * Stop interface to the mote network
	 */
	public void stopInterface();
	
	/**
	 * add a listener to the group of components interested in messages
	 * coming from the wireless sensor network
	 * @param listener a component interested in listening for MiddlewareMessage objects
	 */
	public void addListener(MessageListener listener);
	
	/**
	 * send a message to the sensor network
	 * 
	 * @param message the message 
	 * @return true if sending of message has been successful;
	 * 		   false otherwise
	 */
	public boolean sendMessage(MiddlewareMessage message);

}
