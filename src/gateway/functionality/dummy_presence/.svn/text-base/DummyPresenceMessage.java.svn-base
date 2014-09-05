
package gateway.functionality.dummy_presence;


import gateway.protocol.FunctionalityMessage;


public class DummyPresenceMessage extends FunctionalityMessage
{
	
	private final byte PERSONS_POSITION = 0;
			
	public DummyPresenceMessage(FunctionalityMessage f_msg)
	{
		super(f_msg.getDataBytes());
			
		System.out.println(">DummyPresence message:");
		System.out.println("      person number: " + getPersonNumber() + "\n");		
	}

	/*
	 * DummyPresenceFunctionality message structure:
	 * 
	 * Persons:			1 byte;
	 * 
	 * typedef nx_struct 
	 * {
	 * nx_uint8_t		persons;
	 * } dummy_presence_message_t;
	 * 
	 */	
		
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
