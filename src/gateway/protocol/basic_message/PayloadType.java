
package gateway.protocol.basic_message;


public enum PayloadType {

	TREE_ROUTING,
	GROUP_MANAGEMENT,
	MIDDLEWARE,
	FUNCTIONALITY;

	public static PayloadType convert(int i)
	{
		try 
		{
			return values()[i];
		} 
		catch ( ArrayIndexOutOfBoundsException e ) 
		{
			throw new IllegalArgumentException("there is not i element!");
		}
	}
	
}
