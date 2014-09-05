
package gateway.protocol;

import gateway.protocol.basic_message.MiddlewareMessage;


/**
 * @author Davide Roveran
 *
 * Define a particular MiddlewareMessage sent to respond to a received  
 * SensorRead message
 */
public class SensorReadResponseMessage extends MiddlewareMessage 
{
	
	//private boolean read_resp_sensor_id_is_nxle;
	private boolean[] sensor_value_is_nxle;

	private final byte READ_HANDLER_POSITION = 0;
	private final byte ERROR_CODE_POSITION = 1; 
	private final byte READ_RESP_SENSOR_ID_POSITION = 2; 
	private final byte SENSOR_VALUE_LEN_POSITION = 3;
	private final byte SENSOR_VALUE_POSITION = 4;
	
	private final byte SENSOR_VALUE_SIZE = 5; 

	public SensorReadResponseMessage(byte[] data)
	{
		super(data);
		
		//read_resp_sensor_id_is_nxle = false;
		
		sensor_value_is_nxle = new boolean[getSensorValueLen()];

		for (int i =0; i < getSensorValueLen(); i++)
			this.sensor_value_is_nxle[i] = false;
		
		System.out.println(">SensorReadResponse message:");
		System.out.println("      Handler: " + getReadHandler() + "; Error Code: " + getReadErrorCode());
		System.out.println("      Sensor ID : " + getReadRespSensorId() + "; Sensor Value Len: " + getSensorValueLen() + ";");

		for (int j = 0; j < getSensorValueLen(); j++)
				System.out.println("Sensor Value [" + j + "]: " + getSensorValue(j));
		
		System.out.println("");
	}	
	
	/*
	 * Sensor Rea dResponse message structure:
	 * 
	 * Sensor Read Response message header:		4 byte;
	 * Sensor value[SENSOR_VALUE_SIZE]:			2 byte * SENSOR_VALUE_SIZE;
	 * 
	 * typedef nx_struct 
	 * {
	 * sensor_read_response_message_header_t	header;
	 * nx_uint16_t 								sensor_value [SENSOR_VALUE_SIZE];
	 * } sensor_read_response_message_t;
	 * 
	 * where:
	 * 
	 *
	 *		Sensor Read Response message header structure:
	 * 
	 *		Handler:							1 byte;
	 *		Error code:							1 byte;
	 *		Sensor ID:							1 byte;
	 * 		Sensor value len:					1 byte;
	 * 
	 * 		typedef nx_struct sensor_read_response_message_header_t
	 * 		{
	 * 		nx_uint8_t							handler;
	 * 		nx_uint8_t							error_code;
	 *		nx_sensor_id_t						sensor_id;
	 * 		nx_uint8_t							sensor_value_len;
	 * 		} sensor_read_response_message_header_t;
	 * 
	 */
	
	/**
	 * 
	 * @param read_handler the handler to set
	 */
	public void setReadHandler(byte read_handler)
	{
		data_byte[super.getPayload()+READ_HANDLER_POSITION] = read_handler;
	}
	
	/**
	 * 
	 * @return the handler specified for a read
	 */
	public byte getReadHandler()
	{	
		return data_byte[super.getPayload()+READ_HANDLER_POSITION];
	}

	/**
	 * 
	 * @param error_code the error_code to set
	 */
	public void setReadErrorCode(byte error_code)
	{
		data_byte[super.getPayload()+ERROR_CODE_POSITION] = error_code;
	}
	
	/**
	 * 
	 * @return the error_code
	 */
	public byte getReadErrorCode()
	{	
		return data_byte[super.getPayload()+ERROR_CODE_POSITION];
	}	
	
	/**
	 * 
	 * @param sensor_id the sensor_id to set
	 */
	public void setReadRespSensorId(byte read_resp_sensor_id)
	{
		// nx_sensor_id_t sensor_id;
		data_byte[super.getPayload()+READ_RESP_SENSOR_ID_POSITION] = read_resp_sensor_id;
		
	}
	
	/**
	 * 
	 * @return the sensor identifier specified for this message
	 */
	public byte getReadRespSensorId()
	{
		return data_byte[super.getPayload()+READ_RESP_SENSOR_ID_POSITION];	
	}
	
	/**
	 * 
	 * @param sensor_value_len the sensor_value_len to set
	 */
	public void setSensorValueLen(byte sensor_value_len)
	{
		if (sensor_value_len < SENSOR_VALUE_SIZE)
		{
			// nx_uint8_t sensor_value_len;
			data_byte[super.getPayload()+SENSOR_VALUE_LEN_POSITION] = sensor_value_len;		
		}
		else
			throw new IllegalArgumentException("every sensor read response message cannot have more than "+ SENSOR_VALUE_SIZE +" sensors! ("+ sensor_value_len +" is too long)");

	}
	
	/**
	 * 
	 * @return number of values read from sensor
	 */
	public byte getSensorValueLen()
	{
		return data_byte[super.getPayload()+SENSOR_VALUE_LEN_POSITION];
	}
	
	/**
	 * 
	 * @param sensor_value the sensor_value to set
	 */
	public void setSensorValue(short sensor_value, int n)
	{
		if (n < getSensorValueLen())
		{		
			setShortField(sensor_value, super.getPayload()+SENSOR_VALUE_POSITION + (n * 2), sensor_value_is_nxle[n]);					
		}
		else
			throw new IllegalArgumentException("this read response message has only "+ getSensorValueLen() +" values! ("+ n +" is too long)");	
	}
	
	/**
	 * 
	 * @return n-th value read from sensor
	 */
	public short getSensorValue(int n)
	{
		if (n < getSensorValueLen())
		{		
			return getShortField(super.getPayload()+SENSOR_VALUE_POSITION + (n * 2), sensor_value_is_nxle[n]);
		}
		else
			throw new IllegalArgumentException("this read response message has only "+ getSensorValueLen() +" values! ("+ n +" is too long)");	
			
	}
	
}
