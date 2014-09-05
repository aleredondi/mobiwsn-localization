package gateway.group.mote;


import gateway.mote.InternalMote;
import gateway.protocol.address.InternalNetworkAddress;
import gateway.protocol.basic_message.MiddlewareMessageType;


public class RemoveMoteFromGroupMessage extends MoteGroupCommandMessage
{
	
	private boolean member_nwk_addr_is_nxle;
	
	private final byte REMOVE_MOTE_FROM_GROUP_COMMAND_LEN = 2;
	
	private final byte MEMBER_ADDR_POSITION = 0;
	
	public RemoveMoteFromGroupMessage(InternalMoteGroup group, InternalMote mote)
	{	
		member_nwk_addr_is_nxle = true;
		
		byte len = (byte) (super.getPayload()+REMOVE_MOTE_FROM_GROUP_COMMAND_LEN);

		data_byte = new byte[len];
		
		addSerialHdr(len);			
			
		//setCorrelationId((short)0);
		
		setDestinationMessage(group.getGroupMaster());
		
		// per ELIMINARE il campo group_id dal MoteGroupCommandMessage
		// sovrascrivo il campo destination_id del Middleware message:
		setDstMoteId((short) group.getId());
			
		setMessageType(MiddlewareMessageType.MOTE_GROUP_COMMAND);
		
		setCommandType(MoteGroupCommandType.GC_REMOVE);
		
		
		// aggiungere RemoveGroup message structure...
		
		setMemberNwkAddress(mote.getNetworkAddress());
				
	}
	
	/*
	 * RemoveGroup message structure:
	 *
	 * Member Network Address:		2 byte;
	 *
	 * typedef nx_struct remove_group_message_t
	 * {
	 *  nx_network_addr_t			member_addr;
	 * } remove_group_message_t;
	 * 
	 */
	
	/**
	 * @param new_member_nwk_address the member_addr to set
	 */
	public void setMemberNwkAddress(InternalNetworkAddress new_member_nwk_address) 
	{	
		setShortField ( (short) new_member_nwk_address.intValue(), super.getPayload()+MEMBER_ADDR_POSITION, member_nwk_addr_is_nxle);
	}

	/**
	 * @return the member_addr
	 */
	public short getMemberNwkAddress() 
	{
		return getShortField(super.getPayload()+MEMBER_ADDR_POSITION, member_nwk_addr_is_nxle);
	}

}
