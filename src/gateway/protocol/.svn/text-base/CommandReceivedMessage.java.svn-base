package gateway.protocol;


import gateway.protocol.basic_message.ErrorCodeType;
import gateway.protocol.basic_message.MiddlewareMessage;


/**
 * @authors Luca Pietro Borsani
 *
 * Define a particular MiddlewareMessage sent to respond to a received  
 * FunctionalityCommand and MoteGroupCommand message
 */
public class CommandReceivedMessage extends MiddlewareMessage
{

	private final byte ERROR_CODE_POSITION = 0;


	public CommandReceivedMessage(byte[] data)
	{
		super(data);
	
		System.out.println(">CommandReceived message:");
		System.out.println("      Error Code: " + getReadErrorCode() + "\n");

	}
	
	/*
	 * Command Received message structure:
	 *
	 * Error code: 					1 byte;
 	 *
	 * typedef nx_struct command_received_message_t
	 * {
	 *  nx_error_t					error_code;
	 * } command_received_message_t;
	 */
	
	/**
	 * 
	 * @param error_code the error_code to set
	 */
	public void setReadErrorCode(ErrorCodeType error_code)
	{
		data_byte[super.getPayload()+ERROR_CODE_POSITION] = (byte) error_code.ordinal();
	}
	
	/**
	 * 
	 * @return the error_code
	 */
	public ErrorCodeType getReadErrorCode()
	{	
		return ErrorCodeType.convert(data_byte[super.getPayload()+ERROR_CODE_POSITION]);
	}
	
}
