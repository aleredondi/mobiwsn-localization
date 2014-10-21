
package gateway.group.mote;


import gateway.protocol.basic_message.MiddlewareMessage;


/**
 * @authors Luca Pietro Borsani
 */
public abstract class MoteGroupCommandMessage extends MiddlewareMessage
{
	
	private final byte MOTE_GROUP_COMMAND_LEN = 1;
	
	private final byte COMMAND_TYPE_POSITION = 0;

	public MoteGroupCommandMessage()
	{
		//super();
	}
		
	public MoteGroupCommandMessage(byte[] data)
	{
		super(data);	
	}
	
	public int getPayload()
	{
		return super.getPayload()+MOTE_GROUP_COMMAND_LEN;
	}
	
	/*
	 * GroupCommand message structure:
	 *
	 * Command Type: 1 byte
	 *
	 * typedef nx_struct group_command_message_t
	 * {
	 *   nx_group_command_t      command_type;
	 * } group_command_message_t;
	 * 
	 */
	
	public void setCommandType(MoteGroupCommandType new_command_type)
	{
		data_byte[super.getPayload()+COMMAND_TYPE_POSITION] = (byte) new_command_type.ordinal();
	}	
	
	public MoteGroupCommandType getCommandType()
	{
		return MoteGroupCommandType.convert( data_byte[super.getPayload()+COMMAND_TYPE_POSITION]);
	}
	
}
