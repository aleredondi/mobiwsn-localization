
package common.exceptions;

import remote_interfaces.protocol.NetworkAddress;


/**
 * @author Davide Roveran
 *
 */
public class MoteUnreachableException extends Exception 
{

	/**
	 * Creates a new MoteUnreachableException with the specified message
	 * @param strExceptionMessage
	 */
	public MoteUnreachableException(String moteId, NetworkAddress networkAddr, short macAddr)
	{
		super("Mote with id: "+ moteId + "; network address: " + networkAddr.toString() + "; mac address: " + macAddr + " is unreachable!");
	}
	
}
