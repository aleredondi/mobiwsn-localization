
package gateway.protocol.basic_message;


import gateway.protocol.address.*;


public abstract class NetworkMessage extends SerialMessage
{

	private boolean ctrl_fld_is_nxle = true;
	private boolean nwk_id_is_nxle = true;
	private boolean nwk_dst_is_nxle = true;
	private boolean nwk_src_is_nxle = true;
	
	private final byte NWK_HDR_LEN = 8; 
	
	private final byte FORWARDER_HDR_POSITION = 0;
	private final byte CTRL_FLD_POSITION = 1;	
	private final byte NWK_ID_POSITION = 3;
	private final byte NWK_SRC_ADDR_POSITION = 5;
	private final byte NWK_DST_ADDR_POSITION = 7;
	
	private final byte CTRL_FLD_HEADER_LENGTH_POSITION = 12;
	private final byte PKT_SCOPE_POSITION = 9;
	private final byte PAYLOAD_TYPE_POSITION = 6;
	private final byte SEQ_NUM_POSITION = 0;
	
	private InternalNetworkAddress srcAddress;
	private InternalNetworkAddress dstAddress;

	public NetworkMessage()
	{
		//super();
	}
	
	public NetworkMessage(byte[] data)
	{
		super(data);
		
		//System.out.println("Creo un nuovo messaggio di network");
		if ( getShortField(super.getPayload()+NWK_SRC_ADDR_POSITION, nwk_src_is_nxle) != -1 )
			srcAddress = new InternalNetworkAddressImpl(getShortField(super.getPayload()+NWK_SRC_ADDR_POSITION, nwk_src_is_nxle));
		if ( getShortField(super.getPayload()+NWK_DST_ADDR_POSITION, nwk_dst_is_nxle) != -1 )
			dstAddress = new InternalNetworkAddressImpl(getShortField(super.getPayload()+NWK_DST_ADDR_POSITION, nwk_dst_is_nxle));
	}
	
	public int getPayload()
	{
		return super.getPayload()+NWK_HDR_LEN+1; // 1 = sizeof(forward_header_t)
	}
	
	/*
	 * Forwarder header structure :
	 * 
	 * Forwarder Sequence Number:	1 byte
	 * 
	 * typedef nx_struct
	 * {
	 * nx_forward_seqnum_t			fwd_seq_num;
	 * } forward_header_t;
	 * 
	 */
	
	public void resetFwdSeqNum()
	{
		data_byte[super.getPayload()+FORWARDER_HDR_POSITION] = 0;
	}
	
	public byte getFwdSeqNum()
	{		
		return data_byte[super.getPayload()+FORWARDER_HDR_POSITION];
	}
	
	/*
	 * Network message header structure :
	 * 
	 * Control Field : 				2 byte
	 *		Header Length: 				bit 0-3 (fixed to sizeof(network_header_t));
	 *		Packet Scope:				bit 4-6 (see Network Packet Scope enum);
	 *		Payload Type:				bit 7-9 (see Network Payload Type enum);
	 *		Broadcast Sequence Number:	bit 10-15
	 * Network ID : 				2 byte
	 * Network Source Address :		2 byte
	 * Network Destination Address :2 byte
	 * 
	 * typedef nx_struct 
	 * {
	 * nx_control_field_t	ctrl_fld;
	 * nx_network_id_t 		nwk_id;
	 * nx_network_addr_t	nwk_src_addr;
	 * nx_network_addr_t	nwk_dst_addr;
	 * } network_header_t;
	 *
	 */
	
	public void setCtrlField(PckScopeType pkt_scope)
	{	
		short new_ctrl_fld = 0;
		
		new_ctrl_fld |= ( NWK_HDR_LEN  & 0xF ) << CTRL_FLD_HEADER_LENGTH_POSITION; 
		
		new_ctrl_fld |= ( pkt_scope.ordinal() & 0x7 ) << PKT_SCOPE_POSITION;
		
		new_ctrl_fld |= ( PayloadType.MIDDLEWARE.ordinal() & 0x7 ) << PAYLOAD_TYPE_POSITION;
		
		// inizialmente è pari a zero:
		// new_ctrl_fld |= ( 0 & 0x3F) << SEQ_NUM_POSITION;
		
		setShortField(new_ctrl_fld, super.getPayload()+CTRL_FLD_POSITION, ctrl_fld_is_nxle);
				
/*		if(ctrl_fld_is_nxle)
			new_ctrl_fld = nxTOnxleType(new_ctrl_fld);
			
		// nx_network_id_t nwk_id
 		data_byte[super.getPayload()+CTRL_FLD_POSITION] = (byte)( (new_ctrl_fld & 0xFF00) >> 8);
		data_byte[super.getPayload()+CTRL_FLD_POSITION+1] = (byte)( new_ctrl_fld & 0xFF);	
*/				
	}
	
	public short getCtrlField()
	{
		return getShortField(super.getPayload()+CTRL_FLD_POSITION, ctrl_fld_is_nxle);
/*		
		if(ctrl_fld_is_nxle)
			return (short) ( (data_byte[super.getPayload()+CTRL_FLD_POSITION+1] <<8) + data_byte[super.getPayload()+CTRL_FLD_POSITION] );
		else
			return (short) ( (data_byte[super.getPayload()+CTRL_FLD_POSITION] <<8) + data_byte[super.getPayload()+CTRL_FLD_POSITION+1] );
*/
		}
	
	public PckScopeType getPckScope()
	{		
		return PckScopeType.convert((byte)((getCtrlField() >> PKT_SCOPE_POSITION) & 0x7));
	}
	
	public byte getPldType()
	{	
		return (byte)((getCtrlField() >> PAYLOAD_TYPE_POSITION) & 0x7);	
	}
	
	public PayloadType getBroadcastSeqNum()
	{	
		return PayloadType.convert((byte)((getCtrlField() >> SEQ_NUM_POSITION) & 0x3F));	
	}
	
	public void setNwkId(short new_nwk_id)
	{
		
		setShortField(new_nwk_id, super.getPayload()+NWK_ID_POSITION, nwk_id_is_nxle);
/*		
		if(nwk_id_is_nxle)
			new_nwk_id = nxTOnxleType(new_nwk_id);
			
		// nx_network_id_t nwk_id
 		data_byte[super.getPayload()+NWK_ID_POSITION] = (byte)( (new_nwk_id & 0xFF00) >> 8);
		data_byte[super.getPayload()+NWK_ID_POSITION+1] = (byte)( new_nwk_id & 0xFF);
*/
		}

	public short getNwkId()
	{	
		return getShortField(super.getPayload()+NWK_ID_POSITION, nwk_id_is_nxle);
/*		
		if(nwk_id_is_nxle)
			return (short) ( (data_byte[super.getPayload()+NWK_ID_POSITION+1] <<8) + data_byte[super.getPayload()+NWK_ID_POSITION] );
		else
			return (short) ( (data_byte[super.getPayload()+NWK_ID_POSITION] <<8) + data_byte[super.getPayload()+NWK_ID_POSITION+1] );
*/
	}

	public void setNwkSrc(short new_nwk_src)
	{
		setShortField(new_nwk_src, super.getPayload()+NWK_SRC_ADDR_POSITION, nwk_src_is_nxle);
		srcAddress = new InternalNetworkAddressImpl(getShortField(super.getPayload()+NWK_SRC_ADDR_POSITION, nwk_src_is_nxle));

/*		
		//new_nwk_src = nxTOnxleType(new_nwk_src);
		if(nwk_src_is_nxle)
		{
	 		data_byte[super.getPayload()+NWK_SRC_ADDR_POSITION+1] = (byte)( (new_nwk_src & 0xFF00) >> 8);
			data_byte[super.getPayload()+NWK_SRC_ADDR_POSITION] = (byte)( new_nwk_src & 0xFF);
		}
		else
		{
			data_byte[super.getPayload()+NWK_SRC_ADDR_POSITION] = (byte)( (new_nwk_src & 0xFF00) >> 8);
			data_byte[super.getPayload()+NWK_SRC_ADDR_POSITION+1] = (byte)( new_nwk_src & 0xFF);
		}
*/
	}
	
	public void setNwkSrc(InternalNetworkAddress new_nwk_src)
	{
		setShortField( (short) new_nwk_src.intValue(), super.getPayload()+NWK_SRC_ADDR_POSITION, nwk_dst_is_nxle);
		srcAddress = new InternalNetworkAddressImpl(getShortField(super.getPayload()+NWK_SRC_ADDR_POSITION, nwk_dst_is_nxle));
	}
	

	public InternalNetworkAddress getNwkSrc()
	{	
		return srcAddress;
/*		
		if(nwk_src_is_nxle)
			return (short) ( (data_byte[super.getPayload()+NWK_SRC_ADDR_POSITION+1] <<8) + data_byte[super.getPayload()+NWK_SRC_ADDR_POSITION] );
		else
			return (short) ( (data_byte[super.getPayload()+NWK_SRC_ADDR_POSITION] <<8) + data_byte[super.getPayload()+NWK_SRC_ADDR_POSITION+1] );
*/
		}
	
	public void setNwkDest(short new_nwk_dest)
	{
		setShortField(new_nwk_dest, super.getPayload()+NWK_DST_ADDR_POSITION, nwk_dst_is_nxle);
		dstAddress = new InternalNetworkAddressImpl(getShortField(super.getPayload()+NWK_DST_ADDR_POSITION, nwk_dst_is_nxle));

/*		
		if(nwk_dst_is_nxle)
			new_nwk_dest = nxTOnxleType(new_nwk_dest);
			
		// nx_network_addr_t nwk_dst_addr
 		data_byte[super.getPayload()+NWK_DST_ADDR_POSITION] = (byte)( (new_nwk_dest & 0xFF00) >> 8);
		data_byte[super.getPayload()+NWK_DST_ADDR_POSITION+1] = (byte)( new_nwk_dest & 0xFF);
*/
	}
	
	public void setNwkDest(InternalNetworkAddress new_nwk_dest)
	{
		setShortField( (short) new_nwk_dest.intValue(), super.getPayload()+NWK_DST_ADDR_POSITION, nwk_dst_is_nxle);
		dstAddress = new InternalNetworkAddressImpl(getShortField(super.getPayload()+NWK_DST_ADDR_POSITION, nwk_dst_is_nxle));
	}
	

	public InternalNetworkAddress getNwkDest()
	{		
		return dstAddress;
/*		
		if(nwk_dst_is_nxle)
			return (short) ( (data_byte[super.getPayload()+NWK_DST_ADDR_POSITION+1] <<8) + data_byte[super.getPayload()+NWK_DST_ADDR_POSITION] );
		else
			return (short) ( (data_byte[super.getPayload()+NWK_DST_ADDR_POSITION] <<8) + data_byte[super.getPayload()+NWK_DST_ADDR_POSITION+1] );
*/
		}
	
}
