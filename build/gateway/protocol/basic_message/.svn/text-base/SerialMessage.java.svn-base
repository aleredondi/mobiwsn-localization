
package gateway.protocol.basic_message;


import gateway.config.ConfigurationManager;
import net.tinyos.packet.Serial;


public abstract class SerialMessage extends ByteMessage
{
	
	private boolean serial_dst_is_nxle = false;
	private boolean serial_src_is_nxle = false;
	
	private final byte SERIAL_HDR_LEN = 7; 
	
	private final static byte MIDDLEWARE_AM_TYPE = 9;

	public SerialMessage()
	{
		//super();
	}
	
	public SerialMessage(byte[] data)
	{
		super(data);
	}
	
	public int getPayload()
	{
		return 1+SERIAL_HDR_LEN; // 1 = Tos Serial Active Message ID field
	}
	
	/* 
	 * Tos Serial Active Message ID: 1 byte
	 */
	public void setSerialAMid()
	{
		data_byte[0] = Serial.TOS_SERIAL_ACTIVE_MESSAGE_ID;
	}
	
	/* 
	 * Serial message header structure :
	 * 
	 * Serial destination:	2 byte
	 * Serial source:		2 byte
	 * Message length:		1 byte
	 * AM group:			1 byte
	 * AM type:				1 byte
	 * 
	 * typedef nx_struct serial_header {
	 * nx_am_addr_t 		dest;
	 * nx_am_addr_t 		src;
	 * nx_uint8_t 			length;
	 * nx_am_group_t 		group;
	 * nx_am_id_t 			type;
	 * } serial_header_t;
	 * 
	 */
	
	public void setSerialDest(short new_serial_dest)
	{
		setShortField(new_serial_dest, 1, serial_dst_is_nxle);
		
/*		if(serial_dst_is_nxle)
			new_serial_dest = nxTOnxleType(new_serial_dest);
			
		// nx_am_addr_t dest
 		data_byte[1] = (byte)( (new_serial_dest & 0xFF00) >> 8);
		data_byte[2] = (byte)( new_serial_dest & 0xFF);
*/
	}

	public short getSerialDest()
	{		
		return getShortField(1, serial_dst_is_nxle);
/*		
		if(serial_dst_is_nxle)
			return (short) ( (data_byte[2] <<8) + data_byte[1] );
		else
			return (short) ( (data_byte[1] <<8) + data_byte[2] );
*/
	}
	
	public void setSerialSrc(short new_serial_src)
	{	
		setShortField(new_serial_src, 3, serial_src_is_nxle);
/*		
		if(serial_src_is_nxle)
			new_serial_src = nxTOnxleType(new_serial_src);
		
		// nx_am_addr_t src
 		data_byte[3] = (byte)( (new_serial_src & 0xFF00) >> 8);
		data_byte[4] = (byte)( new_serial_src & 0xFF);
*/
	}
	
	public short getSerialSrc()
	{		
		return getShortField(3, serial_src_is_nxle);
/*
		if(serial_src_is_nxle)
			return (short) ( (data_byte[4] <<8) + data_byte[3] );
		else	
			return (short) ( (data_byte[3] <<8) + data_byte[4] );
*/
	}
	
	/**
	 * @param new_length refers to all byte after the serial header
	 */
	public void setMessageLength(byte new_length)
	{	
		// nx_uint8_t length
 		data_byte[5] = new_length;
	}
	
	/**
	 * @return the number of byte after the serial header
	 */
	public byte getMessageLength()
	{		
		return data_byte[5];
	}
	
	public void setAMgroup(byte new_am_group)
	{	
		// nx_am_group_t group
 		data_byte[6] = new_am_group;
	}

	public byte getAMgroup()
	{		
		return data_byte[6];
	}
	
	public void setAMtype(byte new_am_type)
	{	
		// nx_am_id_t type
 		data_byte[7] = new_am_type;
	}

	public byte getAMtype()
	{		
		return data_byte[7];
	}

	public void addSerialHdr(byte len) {
		  
		// adding TOS_SERIAL_ACTIVE_MESSAGE_ID and SerialPacket header (see TEP 113 and Serial.h (in nesC Code)
		  	
		setSerialAMid();
		
		setSerialDest(Byte.parseByte(ConfigurationManager.getInstance().getParameter("pan_coord_id")));
		setSerialSrc(Byte.parseByte(ConfigurationManager.getInstance().getParameter("pan_coord_id")));
		
		//len comprende l'hdr seriale e quindi lo devo togliere
		setMessageLength((byte) ( len - (SERIAL_HDR_LEN+1) ));
		
		setAMgroup((byte) 0);
			
		setAMtype((byte) MIDDLEWARE_AM_TYPE);
		
	}
	
	
}
