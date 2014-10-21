
package gateway.protocol.basic_message;


import java.nio.ByteBuffer;

import gateway.mote.InternalMote;

import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteType;


/**
 * @authors Davide Roveran, Luca Pietro Borsani
 * 
 * Basic definition of a middleware message. Defines accessor
 * methods for standard middleware message header fields.
 */
public abstract class MiddlewareMessage extends NetworkMessage //GroupMessage
{

	private boolean mote_id_is_nxle = false;
	private boolean message_id_is_nxle = false;
	private boolean correlation_id_is_nxle = false;

	private final byte MIDDLEWARE_HDR_LEN = 13; 
	
	private final byte SOURCE_ID_POSITION = 0;
	private final byte DESTINATION_ID_POSITION = 4;
	private final byte MESSAGE_TYPE_POSITION = 8;
	private final byte MESSAGE_ID_POSITION = 9;
	private final byte CORRELATION_ID_POSITION = 11;
	
	private final byte MOTE_ID_LEN = 4;
	private final byte NODE_BYTE_POSITION = 2;
	
	private final byte NODE_MODEL_POSITION = 4;
	private final byte NODE_TYPE_POSITION = 3;
	
	public MiddlewareMessage()
	{
		//super();		
	}

	public MiddlewareMessage(byte[] data)
	{
		super(data);	
		
		System.out.println(">Middleware header:");
		System.out.println("      Source ID: " + getSrcMoteId() + "; Destination Id: " + getDstMoteId());
		System.out.println("      Message Id: " + getMessageId() +"; Corrrelation Id: "+ getCorrelationId());

	}
		
	public int getPayload()
	{
		return super.getPayload()+MIDDLEWARE_HDR_LEN;
	}
/*
	protected byte[] nxTOnxleType(byte[] nx_format) {
		
		byte[] nxle_format = new byte[nx_format.length];
		
		for (int i = 0; i < nx_format.length; i++)
			nxle_format[nx_format.length-i] = nx_format[i];
		
		return nxle_format;
	}
*/
	protected byte[] invertByteArray(byte[] byte_array_field)
	{
		byte[] new_byte_array_field = new byte[byte_array_field.length];
		
		for (int i = 1; i <= byte_array_field.length; i++)		
			new_byte_array_field[i-1] = byte_array_field[byte_array_field.length-i];
		
		return new_byte_array_field;
		
	}
	
	private int byteToInt(byte[] b)
	{
		ByteBuffer bb = ByteBuffer.wrap(b);
		return bb.getInt();
	}
	
	public void setDestinationMessage(InternalMote dest_mote)
	{	
		
		if (dest_mote == null)
		{
			setCtrlField(PckScopeType.BROADCAST);
			setNwkDest(Mote.NWK_NULL_ADDRESS);
			
			setDstMoteId(Mote.MOTE_BROADCAST_ID);
		}
		else
		{
			setCtrlField(PckScopeType.FROM_PAN_COORD); // is equal to PckScopeType.UNICAST
			setNwkDest(dest_mote.getNetworkAddress());
			
			setDstMoteId(dest_mote.getId());
		}
		
	}
	
	/*
	 * Middleware message header structure :
	 * 
	 * Source Id:				4 byte
	 * Destination Id:			4 byte
	 * Message Type:			1 byte
	 * Message Id:				2 byte
	 * Correlation Id:			2 byte
	 * 
	 * typedef nx_struct 
	 * {
	 * nx_mote_id_t  			source_id;
	 * nx_mote_id_t 			destination_id;
	 * nx_message_type_t		message_type;
	 * nx_message_id_t			message_id;
	 * nx_correlation_id_t		correlation_id;
	 * } middleware_header_t;
	 *  
	 */
	
	private String crateMoteId(byte[] byte_mote_id, boolean src) {

		MoteType node_model;
		String node_type;
		short node_id;
		String mote_id;

		int int_mote_id = byteToInt(byte_mote_id);
		
		//System.out.println("\n### byte_mote_id = " + byte_mote_id[0] +" "+ byte_mote_id[1] +" "+ byte_mote_id[2] +" "+ byte_mote_id[3]);
		//System.out.println("### int_mote_id = " + int_mote_id);

		if (int_mote_id == 0)
		{	
			mote_id = "00000000";
			//System.out.println("### mote_id = " + mote_id);
		}
		else if (int_mote_id == -1)
		{
			mote_id = "FFFFFFFF";
			//System.out.println("### mote_id = " + mote_id);
		}	
		else
		{
			node_model = getMoteType(src);

			if ( ((byte_mote_id[NODE_BYTE_POSITION] >> NODE_TYPE_POSITION) & 0x1) == 0x1 )
				node_type = "M";
			else
				node_type ="F";

			node_id = (short) ( ( (0x00FF & (short)byte_mote_id[0]) << 8) + (0x00FF & (short)byte_mote_id[1]) );

			mote_id = new String(node_model.toString() + "." + node_type + "." + node_id);

			/*System.out.println("### mote_id = " + mote_id + " = " +
					" node_type : " + node_model.ordinal() + 
					" node_model : " + node_type +
					" node_id : " + node_id);	
			*/
		}

		return mote_id;
	}
	
	private byte[] convertMoteId(String mote_id) {
			
		int mobile;
		int i; 
		short node_id;
		MoteType node_model;
		byte[] byte_mote_id = new byte[MOTE_ID_LEN];
		
		//System.out.println("\n### mote_id = " + mote_id);
			
		if (mote_id.equals("FFFFFFFF") )
		{
			for (i = 0; i < MOTE_ID_LEN; i++)
				byte_mote_id[i] = (byte) 0xFF;
			
			/*System.out.println("### byte_mote_id = " +
					"" + byte_mote_id[0] +" "+ byte_mote_id[1] + 
                    " "+ byte_mote_id[2] +" "+ byte_mote_id[3]);
            */
		}
		else if (mote_id.equals("00000000") )
		{
			for (i = 0; i < MOTE_ID_LEN; i++)
				byte_mote_id[i] = 0;
			
			/*System.out.println("### byte_mote_id = " +
					"" + byte_mote_id[0] +" "+ byte_mote_id[1] + 
                    " "+ byte_mote_id[2] +" "+ byte_mote_id[3]);
            */
		}
		else
		{		
			i = mote_id.indexOf("."); 
			
			// 3 = caratteri prima del node_id = "." + "M/F" + "."			
			node_id = Short.parseShort(mote_id.substring(i+3));
						
			node_model = MoteType.valueOf(mote_id.substring(0, i));
				
			if (mote_id.charAt(i+1)=='M')
				mobile = 1;
			else
				mobile = 0;
	
			//TODO: scrivere l'array...
			
			byte_mote_id[0] = (byte)( (node_id & 0xFF00) >> 8 );
			byte_mote_id[1] = (byte)( node_id & 0xFF );
			
			byte_mote_id[2] = (byte)( (node_model.ordinal() & 0xF) << NODE_MODEL_POSITION);
		    // - il node_model è definito nei bit 0-3 (tra 0 e 15);   
			byte_mote_id[2] |= (byte)( mobile << NODE_TYPE_POSITION);
		    // - il node_type è definito nel bit 4;  
			
			byte_mote_id[3] = 0; //reservati	
			
			/*System.out.println("### byte_mote_id = " +
								"" + byte_mote_id[0] +" "+ byte_mote_id[1] + 
			                    " "+ byte_mote_id[2] +" "+ byte_mote_id[3] + " = " + 					
							" node_type : " + node_model.ordinal() + 
							" node_model : " + mobile +
							" node_id : " + node_id);
			*/
		}
		
		return byte_mote_id;
	}
		
	/**
	 * @param src_id the source_id to set
	 */
	public void setSrcMoteId(String src_mote_id) 
	{ 
		
		byte [] byte_src_id = convertMoteId(src_mote_id);
		
		if(mote_id_is_nxle)
			byte_src_id = invertByteArray(byte_src_id);
				
		// nx_mote_id_t	source_id			
		for (int i = 0; i < MOTE_ID_LEN; i++)
			data_byte[super.getPayload()+SOURCE_ID_POSITION+i] = byte_src_id[i];
		
		/*	
		if (src_mote_id.length() > 8) 
			throw new IllegalArgumentException("a maximum of 8 characters is allowed for mote identification!");
		else 
		{
			try
			{		
				// convert from String to byte array
				byte[] byte_src_Id = src_mote_id.getBytes("ASCII");
				
				if(mote_id_is_nxle)
					byte_src_Id = invertByteArray(byte_src_Id);
				
				// nx_mote_id_t	source_id			
				for (int i = 0; i < 8; i++)
					data_byte[super.getPayload()+SOURCE_ID_POSITION+i] = byte_src_Id[i];				
			}
			catch (UnsupportedEncodingException ueex)
			{
				ExceptionHandler.getInstance().handleException(ueex);
			}				
		}
		*/
	}
	
	/**
	 * @return the source_id
	 */
	public String getSrcMoteId() 
	{
		byte[] byte_src_Id = new byte[MOTE_ID_LEN];
		System.arraycopy(data_byte, super.getPayload()+SOURCE_ID_POSITION, byte_src_Id, 0, MOTE_ID_LEN);

		if(mote_id_is_nxle)
			byte_src_Id = invertByteArray(byte_src_Id);
		
		return crateMoteId(byte_src_Id, true);
	}

	/**
	 * @param src specifics of which node type is returnable (source or destination)
	 * 
	 * @return the mote type of the source node if src is true, otherwise of the destination
	 */	
	public MoteType getMoteType(boolean src) {
		
		int pos;
		byte src_model;
		MoteType t;
		
		if(src)
			pos = super.getPayload()+SOURCE_ID_POSITION;
		else
			pos = super.getPayload()+DESTINATION_ID_POSITION;
		
		if (mote_id_is_nxle)
			pos += (MOTE_ID_LEN-1) - NODE_BYTE_POSITION;
		else
			pos += NODE_BYTE_POSITION;	
				
		src_model = data_byte[pos];
		
		t = MoteType.convert((src_model >> NODE_MODEL_POSITION) & 0xF); 	
		
		//if (t != null )
			//System.out.println( t.toString() );
		
		return t;
		
	}
	
	/**
	 * @param dst_mote_id the destination_id to set
	 */
	public void setDstMoteId(String dst_mote_id) 
	{
		
		byte [] byte_dst_id = convertMoteId(dst_mote_id);
		
		if(mote_id_is_nxle)
			byte_dst_id = invertByteArray(byte_dst_id);
		
		// nx_mote_id_t	destination_id			
		for (int i = 0; i < MOTE_ID_LEN; i++)
			data_byte[super.getPayload()+DESTINATION_ID_POSITION+i] = byte_dst_id[i];		
		
		/*		
		if (dst_mote_id.length() > 8) 
			throw new IllegalArgumentException("a maximum of 8 characters is allowed for mote identification!");
		else 
		{		
			try
			{
				// convert from String to byte array
				byte[] byte_dst_Id = dst_mote_id.getBytes("ASCII");
						
				if(mote_id_is_nxle)
					byte_dst_Id = invertByteArray(byte_dst_Id);
				
				// nx_mote_id_t	destination_id			
				for (int i = 0; i < 8; i++)
					data_byte[super.getPayload()+DESTINATION_ID_POSITION+i] = byte_dst_Id[i];	
				
			}
			catch (UnsupportedEncodingException ueex)
			{
				ExceptionHandler.getInstance().handleException(ueex);
			}
			
		}
		*/				
	}
	
	/**
	 * @param dst_group_id the destination_id to set
	 */
	public void setDstMoteId(short dst_group_id) 
	{
		// dove 2 = MOTE_ID_LEN - GRUOP_ID_LEN = 4 - 2
		
		for (int i = 0; i <2; i++)
			data_byte[super.getPayload()+DESTINATION_ID_POSITION+i] = 0;
		
		setShortField(dst_group_id, super.getPayload()+DESTINATION_ID_POSITION+2, false);	
	}
	
	/**
	 * @return the destination_id
	 */
	public String getDstMoteId() 
	{
		byte[] byte_dst_mote_id = new byte[MOTE_ID_LEN];
		System.arraycopy(data_byte, super.getPayload()+DESTINATION_ID_POSITION, byte_dst_mote_id, 0, MOTE_ID_LEN);
		
		if (mote_id_is_nxle)
			byte_dst_mote_id = invertByteArray(byte_dst_mote_id);
		
		return crateMoteId(byte_dst_mote_id, false);
	}
	
	/**
	 * @return the destination_id
	 */
	public short getDstGroupId() 
	{
		// dove 2 = MOTE_ID_LEN - GRUOP_ID_LEN = 4 - 2	
		
		return getShortField(super.getPayload()+DESTINATION_ID_POSITION+2, false);
	}

	/**
	 * @param msg_type the message_type to set
	 */
	public void setMessageType(MiddlewareMessageType msg_type)
	{
		// nx_message_type_t message_type
		data_byte[super.getPayload()+MESSAGE_TYPE_POSITION] = (byte)msg_type.ordinal();
	}
	
	/**
	 * @return type of this message
	 */
	public MiddlewareMessageType getMessageType()
	{
		return MiddlewareMessageType.convert((byte) data_byte[super.getPayload()+MESSAGE_TYPE_POSITION]);
	}
	
	/**
	 * @param msg_id the message_id to set
	 */
	public void setMessageId(short msg_id) 
	{
		setShortField(msg_id, super.getPayload()+MESSAGE_ID_POSITION, message_id_is_nxle);	
/*		
		if(message_id_is_nxle)
			msg_id = nxTOnxleType(msg_id);
			
		// nx_message_id_t message_id
 		data_byte[super.getPayload()+MESSAGE_ID_POSITION] = (byte)( (msg_id & 0xFF00) >> 8);
		data_byte[super.getPayload()+MESSAGE_ID_POSITION+1] = (byte)( msg_id & 0xFF);
*/
	}

	/**
	 * @return the message_id
	 */
	public short getMessageId() 
	{
		return getShortField(super.getPayload()+MESSAGE_ID_POSITION, message_id_is_nxle);
/*		
		if(message_id_is_nxle)
			return (short) ( (data_byte[super.getPayload()+MESSAGE_ID_POSITION+1] <<8) + data_byte[super.getPayload()+MESSAGE_ID_POSITION] );
		else
			return (short) ( (data_byte[super.getPayload()+MESSAGE_ID_POSITION] <<8) + data_byte[super.getPayload()+MESSAGE_ID_POSITION+1] );
*/
		}

	/**
	 * @param corr_id the correlation_id to set
	 */
	public void setCorrelationId(short corr_id) 
	{
		setShortField(corr_id, super.getPayload()+CORRELATION_ID_POSITION, correlation_id_is_nxle);	
/*		
		if(correlation_id_is_nxle)
			corr_id = nxTOnxleType(corr_id);
			
		// nx_message_id_t message_id
 		data_byte[super.getPayload()+CORRELATION_ID_POSITION] = (byte)( (corr_id & 0xFF00) >> 8);
		data_byte[super.getPayload()+CORRELATION_ID_POSITION+1] = (byte)( corr_id & 0xFF);
*/
	}

	/**
	 * @return the correlation_id
	 */
	public short getCorrelationId() 
	{		
		return getShortField(super.getPayload()+CORRELATION_ID_POSITION, correlation_id_is_nxle);
/*		
		if(correlation_id_is_nxle)
			return (short) ( (data_byte[super.getPayload()+CORRELATION_ID_POSITION+1] <<8) + data_byte[super.getPayload()+CORRELATION_ID_POSITION] );
		else
			return (short) ( (data_byte[super.getPayload()+CORRELATION_ID_POSITION] <<8) + data_byte[super.getPayload()+CORRELATION_ID_POSITION+1] );
*/
		}

	/**
	 * @return true if this message is correlate with correlateMessage,
	 * otherwise returns false
	 */
	public boolean isCorrelateWith(MiddlewareMessage correlateMessage)
	{
		return ((correlateMessage.getMessageId() == this.getCorrelationId()) &&
				(correlateMessage.getDstMoteId().equals(this.getSrcMoteId())) &&
				(correlateMessage.getSrcMoteId().equals(this.getDstMoteId())) );
	}
	
	/**
	 * 
	 * @return true if this message is a request message, otherwise returns false
	 */
	public boolean isRequest()
	{
		MiddlewareMessageType msg_type = getMessageType();
		
		switch (msg_type)
		{
			case MOTE_DISCOVERY:
			case SENSOR_READ:
			case MOTE_CONFIGURATION:
				return true;
				
			default:
				return false;
		}
	}
	
	/**
	 * 
	 * @return true if this message is a command message, otherwise returns false
	 */
	public boolean isCommand()
	{
		MiddlewareMessageType msg_type = getMessageType();
		
		switch (msg_type)
		{
			case MOTE_GROUP_COMMAND:
			case FUNCTIONALITY_COMMAND:
				return true;
				
			default:
				return false;
		}
	}

	
}
