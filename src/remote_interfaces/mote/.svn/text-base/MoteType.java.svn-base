
package remote_interfaces.mote;


/**
 * 
 * @author Davide Roveran
 *
 * Enumerates the different types of motes
 */
public enum MoteType 
{
	UNDEFINED,
	MICAZ,
	IMOTE,
	IRIS,
	SHIMMER;
		
	public static MoteType convert(int i)
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
/*
	public static MoteType getType(String s)
	{
		if( s.equals("MICAz") )
			return MICAZ;
		else if ( s.equals("IMOTE") ) {
			return IMOTE;
		}
		else if ( s.equals("IRIS-")){
			return IRIS;
		}
		else if (s.equals("SHIMR")){
			return SHIMMER;
		}
		else {
			return UNDEFINED;
		}
	}
*/
}
