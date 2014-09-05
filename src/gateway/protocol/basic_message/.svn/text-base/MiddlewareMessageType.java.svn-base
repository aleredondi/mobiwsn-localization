

package gateway.protocol.basic_message;


/**
 * @author Davide Roveran
 * <br> enumerates types of middleware messages
 */
public enum MiddlewareMessageType 
{
	UNKNOWN,
	MOTE_DISCOVERY,
	MOTE_ANNOUNCEMENT,
	MOTE_LOSS,
	MOTE_CONFIGURATION,
	SENSOR_READ,
	SENSOR_READ_RESPONSE,
	MOTE_GROUP_COMMAND,
	MOTE_GROUP_ANNOUNCEMENT,
	FUNCTIONALITY_COMMAND,
	FUNCTIONALITY_ANNOUNCEMENT,
	COMMAND_RECEIVED;
	
	public static MiddlewareMessageType convert(int i)
	{
		try 
		{
			return values()[i];
		} 
		catch ( ArrayIndexOutOfBoundsException e ) 
		{
			return UNKNOWN;
		}
	}
}
