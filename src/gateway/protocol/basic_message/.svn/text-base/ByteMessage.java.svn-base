
package gateway.protocol.basic_message;


public abstract class ByteMessage
{
	
	protected byte[] data_byte;
	
	public ByteMessage()
	{
		//System.out.println("WARNING: This costructor exists only for its extended class !");
	}

	public ByteMessage(byte[] data)
	{
		data_byte = new byte[data.length];	
		
		System.arraycopy(data, 0, data_byte, 0, data.length);	
/*
		short first_nwk_byte = (short) (0x00FF & ((short)data[40]));		
		System.out.println("\ndata_byte[40] : " + data_byte[40] + " first_nwk_byte : " +  first_nwk_byte);
*/
	}
	
	public byte[] getDataBytes()
	{
		return data_byte;		
	} 
	
	/*protected short nxTOnxleType(short nx_format)
	{
		
		byte[] temp_byte = new byte[2];
		
		temp_byte[0] = (byte)((nx_format & 0xFF00) >> 8);
		temp_byte[1] = (byte)(nx_format & 0xFF);
			
		return (short)((temp_byte[1] <<8) + temp_byte[0]);

	}*/

	protected void setShortField(short short_field, int pos, boolean field_is_nxle)
	{
		if(field_is_nxle)
		{
			data_byte[pos] = (byte)( short_field & 0xFF );
			pos++;
			data_byte[pos] = (byte)( (short_field & 0xFF00) >> 8 );
		}	
		else
		{
			data_byte[pos] = (byte)( (short_field & 0xFF00) >> 8 );
			pos++;
			data_byte[pos] = (byte)( short_field & 0xFF );
		}
	}
	
	protected short getShortField(int pos, boolean field_is_nxle)
	{		
		if(field_is_nxle)
			return  (short) ( ( (0x00FF & (short)data_byte[++pos]) << 8) + (0x00FF & (short)data_byte[--pos]) ) ;
		else
			return  (short) ( ( (0x00FF & (short)data_byte[pos]) << 8) + (0x00FF & (short)data_byte[++pos]) ) ;		
	}
	
}
