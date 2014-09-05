
package gateway.functionality;


public enum FunctionalityCommandType {
	
	FC_UNKNOWN,
	FC_START,
	FC_STOP;
	
	public static FunctionalityCommandType convert(int i)
	{
		try 
		{
			return values()[i];
		} 
		catch ( ArrayIndexOutOfBoundsException e ) 
		{
			return FC_UNKNOWN;
		}
	}

}
