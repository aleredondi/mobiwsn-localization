package gateway.group.mote;


import gateway.mote.InternalMote;
import gateway.protocol.address.*;
import gateway.protocol.basic_message.MiddlewareMessageType;

import java.util.ArrayList;


public class AddMoteToGroupMessage extends MoteGroupCommandMessage
{
	
	private boolean master_nwk_addr_is_nxle;
	private boolean[] my_group_addr_is_nxle;
	
	private final byte STANDARD_MOTE_ADD_GROUP_COMMAND_LEN = 3; // se non ci sono membri al di fuori del master
	
	private final byte MASTER_ADDR_POSITION = 0;
	private final byte MEMBERS_LEN_POSITION = 2;
	private final byte MY_GROUP_ADDR_POSITION = 3;
	
	private final byte MAX_MEMBERS_OF_A_GROUP = 5; 
	
	public AddMoteToGroupMessage(InternalMoteGroup group)
	{	
		ArrayList<InternalMote> members = group.getList();
		
		InternalMote group_master = group.getGroupMaster();

		int other_members = 0;
		
		for (InternalMote m : members)
			if(m.isReachable())
				other_members++;
		
		other_members--;	//da togliere il -1 se il master non farà più parte dell'array
		
		master_nwk_addr_is_nxle = true;
		
		my_group_addr_is_nxle = new boolean[other_members];
		
		for (int i = 0; i<other_members; i++)
			my_group_addr_is_nxle[i] = true;
		
		byte len = (byte) (super.getPayload()+STANDARD_MOTE_ADD_GROUP_COMMAND_LEN);

		// incrementa len a seconda di quanti nodi fanno parte del gruppo
		// (se è composto solo dal master num_members sarà pari a zero)	
		len = (byte) ( len + (2*other_members) );

		data_byte = new byte[len];
		
		addSerialHdr(len);			
			
		//setCorrelationId((short)0);
		
		setDestinationMessage(group_master);
		
		// per ELIMINARE il campo group_id dal MoteGroupCommandMessage
		// sovrascrivo il campo destination_id del Middleware message:
		setDstMoteId((short) group.getId());
			
		setMessageType(MiddlewareMessageType.MOTE_GROUP_COMMAND);
		
		setCommandType(MoteGroupCommandType.GC_ADD);
		
		
		// aggiungere AddGroup message structure...
		
		setMasterNwkAddress(group_master.getNetworkAddress());
		
		// N.B: prima bisogna settare setMembersLen(other_members) !
		setMembersLen((byte) other_members);

		for (int m = 0; m<getMembersLen(); m++)
			setMyGroupAddress(members.get(m+1).getNetworkAddress(), m);		
		
	}
	
	/*
	 * AddGroup message structure:
	 *
	 * Master Network Address:	2 byte;
	 * Members Len: 			1 byte;
	 * My group address: 		2 byte * MAX_MEMBERS_OF_A_GROUP-1
	 *
	 * typedef nx_struct add_group_message_t
	 * {
	 *  nx_network_addr_t       master_addr;
	 *  nx_uint8_t              members_len;
	 *  nx_network_addr_t       my_group_addr[MAX_MEMBERS_OF_A_GROUP-1];
	 * } add_group_message_t;
	 * 
	 */
	
	/**
	 * @param new_master_nwk_address the master_addr to set
	 */
	public void setMasterNwkAddress(InternalNetworkAddress new_master_nwk_address) 
	{	
		// nx_network_addr_t master_addr;
		setShortField ( (short) new_master_nwk_address.intValue(), super.getPayload()+MASTER_ADDR_POSITION, master_nwk_addr_is_nxle);
	}

	/**
	 * @return the master_addr
	 */
	public short getMasterNwkAddress() 
	{
		return getShortField(super.getPayload()+MASTER_ADDR_POSITION, master_nwk_addr_is_nxle);
	}
	
	/**
	 * @param new_members_len to set
	 */
	public void setMembersLen(byte new_members_len) 
	{
		if ( new_members_len < MAX_MEMBERS_OF_A_GROUP )
		{	
			// nx_uint8_t members_len;
			data_byte[super.getPayload()+MEMBERS_LEN_POSITION] = new_members_len;
		}
		else
			throw new IllegalArgumentException("every mote group cannot have more than "+ MAX_MEMBERS_OF_A_GROUP +" members! ("
					+ new_members_len +" is too long because it doesn't include the group master)");
	}

	/**
	 * @return the members_len
	 */
	public byte getMembersLen() 
	{
		return data_byte[super.getPayload()+MEMBERS_LEN_POSITION];
	}
	
	/**
	 * @param set the my_group_addr of the n-th group member
	 */
	public void setMyGroupAddress(InternalNetworkAddress new_my_group_addr, int n) 
	{	
		if (n < getMembersLen())
		{
			int pos = super.getPayload()+MY_GROUP_ADDR_POSITION + (n * 2);
			
			setShortField( (short) new_my_group_addr.intValue(), pos, my_group_addr_is_nxle[n]);
		}
		else
			throw new IllegalArgumentException("this add mote to group message has only "+ getMembersLen() +" members! ("+ n +" is too long)");
	}

	/**
	 * @return the my_group_addr of the n-th group member
	 */
	public short getMyGroupAddress(int n)
	{
		if (n < getMembersLen())
		{	
			int pos = super.getPayload()+MY_GROUP_ADDR_POSITION + (n * 2);
			
			return getShortField(pos, my_group_addr_is_nxle[n]);
		}
		else
			throw new IllegalArgumentException("this add mote to group message has only "+ getMembersLen() +" members! ("+ n +" is too long)");
	}				

}
