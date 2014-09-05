
package gateway.functionality.dummy;

import gateway.protocol.FunctionalityMessage;


public class DummyMessage extends FunctionalityMessage
{	

	//private boolean dummy_sensor_id_is_nxle;
	private boolean dummy_sensor_value_is_nxle;
	
	private final byte DUMMY_SENSOR_ID_POSITION = 0;
	private final byte DUMMY_SENSOR_VALUE_POSITION = 1;
	
	public DummyMessage(FunctionalityMessage f_msg)
	{
		super(f_msg.getDataBytes());
		
		//dummy_sensor_id_is_nxle = false;
			
		System.out.println(">Dummy message:");
		System.out.println("      Sensor Id: " + getDummySensorId() + "; Sensor Value: " + getDummySensorValue() + "\n");
	}
	
	/*
	 * DummyFunctioanlity message structure:
	 * 
	 * Sensor ID:		1 byte;
	 * Sensor value:	2 byte;
	 * 
	 * typedef nx_struct 
	 * {
	 * nx_sensor_id_t	sensor_id;
	 * nx_uint16_t 		sensor_value;
	 * } dummy_message_t;
	 * 
	 */	
	
	/**
	 * 
	 * @param dummy_sensor_id the sensor_id to set
	 * @throws IllegalArgumentException exception is thrown if sensor_id.length() is 
	 * greater than 2;
	 */
	public void setDummySensorId(byte dummy_sensor_id)
	{
		/*
		if (dummy_sensor_id.length() > 2) 
			throw new IllegalArgumentException("a maximum of 2 characters is allowed for sensor identiers!");
		else 
		{		
			try
			{
				byte[] byte_Id = dummy_sensor_id.getBytes("ASCII");
					
				if (dummy_sensor_id_is_nxle)
					byte_Id = invertByteArray(byte_Id);
			
				// nx_sensor_id_t sensor_id;
				data_byte[super.getPayload()+DUMMY_SENSOR_ID_POSITION] = byte_Id[0];
				data_byte[super.getPayload()+DUMMY_SENSOR_ID_POSITION+1] = byte_Id[1];
					
			}
			catch (UnsupportedEncodingException ueex)
			{
				ExceptionHandler.getInstance().handleException(ueex);
			}				
		}
		*/
		
		// nx_sensor_id_t sensor_id;
		data_byte[super.getPayload()+DUMMY_SENSOR_ID_POSITION] = dummy_sensor_id;
	}
	
	/**
	 * 
	 * @return the sensor identifier specified for this message
	 */
	public byte getDummySensorId()
	{
		/*
		byte[] sensor_id = new byte[2];
		System.arraycopy(data_byte, super.getPayload()+DUMMY_SENSOR_ID_POSITION, sensor_id, 0, 2);
		
		if (dummy_sensor_id_is_nxle)
			sensor_id = invertByteArray(sensor_id);
		
		return new String(sensor_id);
		*/
		return data_byte[super.getPayload()+DUMMY_SENSOR_ID_POSITION];
	}

	/**
	 * 
	 * @param dummy_sensor_value the sensor_value to set
	 */
	public void setDummySensorValue(short dummy_sensor_value)
	{
		//nx_uint16_t sensor_value;
		setShortField(dummy_sensor_value, super.getPayload()+DUMMY_SENSOR_VALUE_POSITION, dummy_sensor_value_is_nxle);	
	}
	
	/**
	 * 
	 * @return the value read from sensor
	 */
	public short getDummySensorValue()
	{	
		return getShortField(super.getPayload()+DUMMY_SENSOR_VALUE_POSITION, dummy_sensor_value_is_nxle);
	}
	

}