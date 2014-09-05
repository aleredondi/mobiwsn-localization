
package gateway.protocol;

import gateway.protocol.basic_message.MiddlewareMessage;


/**
 * @author Luca Pietro Borsani
 * 
 * Basic definition of a functionality message. Defines accessor
 * method for standard functionality message header field.
 */
public class FunctionalityMessage extends MiddlewareMessage 
{

	private final byte FUNCTIONALITY_HDR_LEN = 1; 
	
	private final byte FUNCTIONALITY_HDR_POSITION = 0;
	
	public FunctionalityMessage()
	{
		//super();
	}
	
	public FunctionalityMessage(byte[] data)
	{
		super(data);
		
		System.out.println(">Functionality message:");
		System.out.println("      Functionality ID: " + getFunctionalityId());
	}
	
	public int getPayload()
	{
		return super.getPayload()+FUNCTIONALITY_HDR_LEN;
	}
	
	/*
	 * Functionality message structure :
	 * 
	 * Functionality ID:		1 byte
	 * 
	 * typedef nx_struct 
	 * {
	 * nx_functionality_id_t	functionality_id;
	 * } functionality_header_t;
	 * 
	 */	
	
	public byte getFunctionalityId()
	{
		return data_byte[super.getPayload()+FUNCTIONALITY_HDR_POSITION];
	}
	
	public void setFunctionalityId(byte new_functionality_hdr)
	{
		data_byte[super.getPayload()+FUNCTIONALITY_HDR_POSITION] = new_functionality_hdr;	
	}
	
}
