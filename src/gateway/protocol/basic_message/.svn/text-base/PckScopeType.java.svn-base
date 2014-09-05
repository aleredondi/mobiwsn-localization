
package gateway.protocol.basic_message;


public enum PckScopeType {

	LOCAL_BROADCAST,
	FROM_PAN_COORD,
	TO_PAN_COORD,
	UNICAST,
	TO_GROUP,
	TO_GROUP_MASTER,
	UNDEFINED,
	BROADCAST;

	public static PckScopeType convert(int i)
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
