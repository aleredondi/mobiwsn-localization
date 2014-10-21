
package gateway.comm;


/**
 * @author Davide Roveran
 * @version 1.0
 * <br><br>
 * Enumeration of types of physical connection with a mote
 */
public enum MoteConnectionType 
{
	SERIAL,
	
	MICAZ_SERIAL,
	MICAZ_ETHERNET,
	MICA2_SERIAL,
	MICA2_ETHERNET,
	UNDEFINED;
	
	public static MoteConnectionType convert(int i)
	{
		try 
		{
			return values()[i];
		} 
		catch ( ArrayIndexOutOfBoundsException e ) 
		{
			return UNDEFINED;
		}
	}
}
