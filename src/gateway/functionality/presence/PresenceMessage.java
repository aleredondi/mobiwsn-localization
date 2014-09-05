
package gateway.functionality.presence;


import gateway.protocol.FunctionalityMessage;


public class PresenceMessage extends FunctionalityMessage
{

	private final byte PRESENCE_TYPE_POSITION = 0;
	private final byte PERSONS_POSITION = 1;
	
	public PresenceMessage(FunctionalityMessage f_msg)
	{
		super(f_msg.getDataBytes());
				
		System.out.println("Middleware header: Source ID: " + super.getSrcMoteId()+ "; Destination Id: " + super.getDstMoteId() + 
				"; Message Id: " + super.getMessageId()+"; Corrrelation Id: "+ super.getCorrelationId());
		
		System.out.println("Functionality message: functionality hdr: " + super.getFunctionalityId());
		
		System.out.println("Presence message: presence_type: " + getPresenceType()
				+ " person number: " + getPersonNumber());		
	}
	
	/*
	 * PresenceFunctioanlity message structure:
	 * 
	 * Presence Type:		1 byte;
	 * State value :		1 byte;
	 * 
	 * typedef nx_struct 
	 * {
	 * nx_presence_type_t	presence_type;
	 * nx_uint8_t			persons;
	 * } presence_message_t;
	 * 
	 */
		
	public void setPresenceType(byte new_presence_type)
	{
		// nx_presence_type_t presence_type;
		data_byte[super.getPayload()+PRESENCE_TYPE_POSITION] = new_presence_type;
	}
	
	public byte getPresenceType()
	{
		return data_byte[super.getPayload()+PRESENCE_TYPE_POSITION];
	}
		
	public void setPersonNumber(byte new_persons)
	{
		// nx_uint8_t persons;
		data_byte[super.getPayload()+PERSONS_POSITION] = new_persons;
	}
	
	public byte getPersonNumber()
	{
		return data_byte[super.getPayload()+PERSONS_POSITION];
	}

}
