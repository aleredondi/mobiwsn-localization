
package gateway.functionality;


import gateway.protocol.FunctionalityMessage;
import gateway.protocol.basic_message.MiddlewareMessageType;


public class FunctionalityCommandMessage extends FunctionalityMessage
{
	
	private final byte STANDARD_FUNCTIONALITY_COMMAND_LEN = 1;
	
	private final byte COMMAND_TYPE_POSITION = 0;
	
	/*public FunctionalityCommandMessage(FunctionalityMessage f_msg)
	{
		super(f_msg.getDataBytes());
	}*/
	
	/*public FunctionalityCommandMessage(GenericInternalFunctionality genericInternalFunctionality, byte[] byte_start_payload) {
		
	}*/

	public FunctionalityCommandMessage(InternalFunctionality functionality, byte[] byte_start_payload)
	{
		byte len = (byte) (super.getPayload()+STANDARD_FUNCTIONALITY_COMMAND_LEN);
		
		if (byte_start_payload != null)
			len = (byte) (len + byte_start_payload.length);
		
		data_byte = new byte[len];
		
		addSerialHdr(len);			
	
		setMessageType(MiddlewareMessageType.FUNCTIONALITY_COMMAND);
		
		//setCorrelationId((short)0);
		
		setDestinationMessage(functionality.getOwner());
		setFunctionalityId(functionality.getId());
				
		if (byte_start_payload != null)
			addStartPayload(byte_start_payload);	
				
	}
	
	private void addStartPayload(byte[] byte_start_payload) {

		// riempie gli ultimi byte del messaggio con byte_start_payload
		for (int i = 0; i<byte_start_payload.length; i++ )
			data_byte[super.getPayload()+STANDARD_FUNCTIONALITY_COMMAND_LEN + i] = byte_start_payload[i];	
		
	}

	/*
	 * FunctionaliyCommand message structure:
	 * 
	 * Command Type:				1 byte
	 * 
	 * typedef nx_struct functionality_command_message_t
	 * {
	 * nx_functionality_command_t	command_type;
	 * } functionality_command_message_t;
	 * 
	 */	
	
	public void setCommandType(FunctionalityCommandType new_command_type)
	{
		data_byte[super.getPayload()+COMMAND_TYPE_POSITION] = (byte) new_command_type.ordinal();
	}	
	
	public FunctionalityCommandType getCommandType()
	{
		return FunctionalityCommandType.convert( data_byte[super.getPayload()+COMMAND_TYPE_POSITION]);
	}
	
	
}
