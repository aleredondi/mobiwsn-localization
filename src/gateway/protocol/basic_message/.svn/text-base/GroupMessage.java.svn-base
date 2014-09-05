
package gateway.protocol.basic_message;


public abstract class GroupMessage extends NetworkMessage
{
	
	private final byte GROUP_HDR_LEN = 2; 
	
	private final byte GROUP_ID_POSITION = 0;
	private final byte AREA_ID_POSITION = 1;

	public GroupMessage()
	{
		//super();
	}
	
	public GroupMessage(byte[] data)
	{
		super(data);	
	}
	
	public int getPayload()
	{
		return super.getPayload()+GROUP_HDR_LEN;
	}
	
	/*
	 * Group message header structure :
	 * 
	 * Group Id:		1 byte
	 * Area Id:			1 byte
	 * 
	 * typedef nx_struct 
	 * {
	 * nx_group_id_t	group_id;
	 * nx_area_id_t		area_id;
	 * } group_header_t;
	 * 
	 */
	
	public void setGroupId(byte new_group_id)
	{	
		// nx_group_id_t group_id;
 		data_byte[super.getPayload()+GROUP_ID_POSITION] = new_group_id;
	}

	public byte getGroupId()
	{		
		return data_byte[super.getPayload()+GROUP_ID_POSITION];
	}
	
	public void setAreaId(byte new_area_id)
	{	
		// nx_area_id_t area_id;
 		data_byte[super.getPayload()+AREA_ID_POSITION] = new_area_id;
	}

	public byte getAreaId()
	{		
		return data_byte[super.getPayload()+AREA_ID_POSITION];
	}
		
}
