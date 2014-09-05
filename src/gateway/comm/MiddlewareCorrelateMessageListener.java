
package gateway.comm;


import gateway.protocol.basic_message.MiddlewareMessage;


/**
 * @author Davide Roveran, Luca Pietro Borsani
 * 
 */
public class MiddlewareCorrelateMessageListener 
{
	
	private MiddlewareMessage message;
	private boolean correlateMessageReceived;
	
	public MiddlewareCorrelateMessageListener(MiddlewareMessage message)
	{
		/*
		 * controlla che il tipo di messaggio di cui vogliamo attendere
		 * una risposta lo preveda
		 */
		this.message = message;
		this.correlateMessageReceived = false;
	}
	
	/**
	 * 
	 * @return reference to the correlate Message for which to wait a response
	 */
	public MiddlewareMessage getCorrelateMessage()
	{
		return this.message;
	}
	
	/**
	 * 
	 * @return true if a correlate Message has been received;
	 */
	public boolean isCorrelateMessageReceived()
	{
		return this.correlateMessageReceived;
	}
	
	
	/**
	 * Set correlateMessageReceived for this MiddlewareMessageListener
	 * @param correlateMessageReceived the value to be set
	 */
	public void setCorrelateMessageReceived(boolean correlateMessageReceived)
	{
		this.correlateMessageReceived = correlateMessageReceived;
	}
	
	
}
