
package gateway.group.mote;


public enum MoteGroupCommandType {
	
    GC_UNKNOWN,
    GC_ADD,
    GC_REMOVE;
	
	public static MoteGroupCommandType convert(int i)
	{
		try 
		{
			return values()[i];
		} 
		catch ( ArrayIndexOutOfBoundsException e ) 
		{
			return GC_UNKNOWN;
		}
	}

}
