
package gateway.protocol;


import common.exceptions.MiddlewareException;

import remote_interfaces.sensor.SensorReadType;
import gateway.protocol.basic_message.MiddlewareMessage;
import gateway.protocol.basic_message.MiddlewareMessageType;
import gateway.sensor.InternalSensor;


/**
 * @authors Davide Roveran Luca Pietro Borsani
 *
 * Define a particular MiddlewareMessage sent to read a fresh value from a
 * sensor on a mote
 */
public class SensorReadMessage extends MiddlewareMessage 
{
	
	private boolean packet_num_is_nxle = false;
	private boolean thr_value_is_nxle = false;
	
	private final byte SENSOR_READ_HDR_LEN = 2;
	
	private final byte READ_SENSOR_ID_POSITION = 0;
	private final byte SENSOR_READ_TYPE_POSITION = 1;
	
	// START_PERIODIC_READ
	private final byte PERIODIC_READ_PAYLOAD_LEN = 4;
	
	private final byte CHECK_TIME_POSITION = 2;
	private final byte SAMPLES_PER_PACKET_POSITION = 3;
	private final byte PACKET_NUMBER_POSITION = 4;
	
	// THRESHOLD_READ
	// (SET_UPPER_THRESHOLD_READ, SET_LOWER_THRESHOLD_READ or SET_READ_ON_CHANGE)
	private final byte THRESHOLD_READ_PAYLOAD_LEN = 3;
	
	//private final byte CHECK_TIME_POSITION = 2;
	private final byte THRESHOLD_VALUE_POSITION = 3;
	
	// STOP_READ
	private final byte STOP_READ_PAYLOAD_LEN = 1;
	
	private final byte HANDLER_POSITION = 2;
	
	/*public SensorReadMessage(byte[] data)
	{
		super(data);
	}*/
	
	public SensorReadMessage (InternalSensor sensor)
	{
	
		byte len = (byte) (super.getPayload()+SENSOR_READ_HDR_LEN);
		
		initSensorRead(sensor, len);
			
		setSensorReadType(SensorReadType.SIMPLE_SENSOR_READ);
		
	}
	
	private void initSensorRead(InternalSensor sensor, byte len) {
			
		data_byte = new byte[len];
			
		addSerialHdr(len);			
	
		setMessageType(MiddlewareMessageType.SENSOR_READ);
		
		//setCorrelationId((short)0);
		
		setDestinationMessage(sensor.getOwnerMote());
		
		setReadSensorId(sensor.getId());
		
	}

	public SensorReadMessage (InternalSensor sensor, byte check_time, byte samples_per_packet, byte packet_num)
	{
		
		byte len = (byte) (super.getPayload()+SENSOR_READ_HDR_LEN+PERIODIC_READ_PAYLOAD_LEN);
		
		initSensorRead(sensor, len);
			
		setCheckTime(check_time);
		setSemplesPerPacket(samples_per_packet);
		setPacketNum(packet_num);
		
		setSensorReadType(SensorReadType.START_PERIODIC_READ);
	}
	
	public SensorReadMessage (InternalSensor sensor, byte check_time, byte thr_value, SensorReadType sr_type) throws MiddlewareException
	{
		
		byte len = (byte) (super.getPayload()+SENSOR_READ_HDR_LEN+THRESHOLD_READ_PAYLOAD_LEN);
		
		initSensorRead(sensor, len);
		
		
		switch (sr_type)
		{
		
			case SET_UPPER_THRESHOLD_READ:
				setSensorReadType(SensorReadType.SET_UPPER_THRESHOLD_READ);
				break;
				
			case SET_LOWER_THRESHOLD_READ:
				setSensorReadType(SensorReadType.SET_LOWER_THRESHOLD_READ);
				break;
				
			case SET_READ_ON_CHANGE:
				setSensorReadType(SensorReadType.SET_READ_ON_CHANGE);
				break;
				
			default:
				throw new MiddlewareException("This sensor read message is not a THRESHOLD_READ but " + sr_type + " !");		
						
		}	
		
		setCheckTime(check_time);
		setThrValue(thr_value);	
	}
	
	
	public SensorReadMessage (InternalSensor sensor, byte handler)
	{
		
		byte len = (byte) (super.getPayload()+SENSOR_READ_HDR_LEN+STOP_READ_PAYLOAD_LEN);
		
		initSensorRead(sensor, len);
			
		setHandlerToStop(handler);
		
		setSensorReadType(SensorReadType.STOP_READ);
	}
	
	

	
	/*
	 * Sensor Read message structure:
	 * 
	 * Sensor Read header:					2 byte
	 * 
	 * Optional Payload:					X byte (depend on the Sensor Read Type)
	 * 
	 * typedef nx_struct sensor_read_message_t
	 * {
	 * sensor_read_message_header_t   		sensor_read_header;
	 * nx_uint8_t                      		optional_payload[X];
	 * } sensor_read_message_t;
	 * 
	 * where:
	 * 
	 * 		Sensor Read message header structure:
	 *  
	 * 		Sensor ID:						1 byte;
	 * 		Read Type:						1 byte
	 * 
	 * 		typedef nx_struct sensor_read_message_header_t
	 * 		{
	 * 		nx_sensor_id_t					sensor_id;
	 * 		nx_read_type_t					read_type;
	 * 		} sensor_read_message_header_t;
	 * 
	 *
	 * 		Optional Payload structure:
	 * 
	 *			SIMPLE_SENSOR_READ: X = 0
	 *
	 *      	This read type has not a payload !
	 * 
	 * 			START_PERIODIC_READ: X = 4
	 * 
	 * 			Check Time: 				1 byte
	 * 			Samples per packet:			1 byte
	 * 			Packet Number: 				2 byte
	 *
	 * 			typedef nx_struct start_periodic_read_param_t
	 * 			{
	 * 			nx_sensor_check_time_t      check_time;
	 * 			nx_uint8_t                  samples_per_packet;
	 * 			nx_uint16_t                 packet_num;
	 * 			} start_periodic_read_param_t;
	 * 
	 *			SET_UPPER_THRESHOLD_READ: X = 3
	 *
	 * 			Check Time: 				1 byte
	 * 			Up threshold value: 		2 byte
	 * 
	 * 			ypedef nx_struct start_upper_thr_read_param_t
	 * 			{
	 * 			nx_sensor_check_time_t      check_time;
	 * 			nx_uint16_t                 up_thr_value;
	 * 			} start_upper_thr_read_param_t;
	 * 
	 *			SET_LOWER_THRESHOLD_READ: X = 3
	 *
	 * 			Check Time: 				1 byte
	 * 			Low threshold value: 		2 byte
	 * 
	 * 			typedef nx_struct start_lower_thr_read_param_t
	 * 			{
	 * 			nx_sensor_check_time_t      check_time;
	 * 			nx_uint16_t                 low_thr_value;
	 * 			} start_lower_thr_read_param_t;
	 * 
	 * 			SET_READ_ON_CHANGE: X = 3
	 * 
	 * 			Check Time: 				1 byte
	 * 			Threshold change: 		    2 byte
	 * 
	 * 			typedef nx_struct set_read_on_change_t
	 * 			{
	 * 			nx_sensor_check_time_t      check_time;
	 * 			nx_uint16_t                 thr_change;
	 * 			} set_read_on_change_t;
	 * 
	 * 			STOP_READ: X = 1
	 * 			
	 * 			Handler						1 byte
	 * 
	 * 			typedef nx_struct stop_read_param_t
	 * 			{
	 * 			nx_uint8_t                  handler;
	 * 			} stop_read_param_t;
	 * 
	 * 	 
	 */
		
	/**
	 * 
	 * @param sensor_id the sensor_id to set
	 */
	public void setReadSensorId(byte read_sensor_id)
	{
		// nx_sensor_id_t sensor_id;
		data_byte[super.getPayload()+READ_SENSOR_ID_POSITION] = read_sensor_id;	
	}
	
	/**
	 * 
	 * @return the sensor identifier specified for this sensor read message
	 */
	public byte getReadSensorId()
	{
		return data_byte[super.getPayload()+READ_SENSOR_ID_POSITION];	
	}
	
	/**
	 * 
	 * @param sensor_read_type the read_type to set
	 */
	public void setSensorReadType(SensorReadType sensor_read_type)
	{
		// nx_read_type_t read_type;
		data_byte[super.getPayload()+SENSOR_READ_TYPE_POSITION] = (byte)sensor_read_type.ordinal();	
	}
	
	/**
	 * 
	 * @return the read type specified for this sensor read message
	 */
	public SensorReadType getSensorReadType()
	{
		return SensorReadType.convert(data_byte[super.getPayload()+SENSOR_READ_TYPE_POSITION]);
	}

	// START_PERIODIC_READ or THRESHOLD_READ
	/**
	 * 
	 * @param check_time the check_time to set
	 */
	private void setCheckTime(byte check_time)
	{
		// nx_sensor_check_time_t check_time;
		data_byte[super.getPayload()+CHECK_TIME_POSITION] = check_time;				
	}
	
	/**
	 * 
	 * @return the check time
	 * @throws MiddlewareException 
	 */
	public byte getCheckTime() throws MiddlewareException
	{
		SensorReadType sr_type = getSensorReadType();
		
		switch (sr_type)
		{
			case START_PERIODIC_READ:
			case SET_UPPER_THRESHOLD_READ:
			case SET_LOWER_THRESHOLD_READ:
			case SET_READ_ON_CHANGE:
				
				return data_byte[super.getPayload()+CHECK_TIME_POSITION];				

			default:
				throw new MiddlewareException("This sensor read message is not a START_PERIODIC_READ or a THRESHOLD_READ but " + sr_type + " !");			
		}
		
	}
	
	// START_PERIODIC_READ
	/**
	 * 
	 * @param samples_per_packet the samples_per_packet to set
	 */
	private void setSemplesPerPacket(byte samples_per_packet)
	{			
		// nx_uint8_t samples_per_packet;
		data_byte[super.getPayload()+SAMPLES_PER_PACKET_POSITION] = samples_per_packet;	
	}
	
	/**
	 * 
	 * @return the samples per packet
	 * @throws MiddlewareException 
	 */
	public byte getSemplesPerPacket() throws MiddlewareException
	{
		SensorReadType sr_type = getSensorReadType();
		
		switch (sr_type)
		{
			case START_PERIODIC_READ:
				
				return data_byte[super.getPayload()+SAMPLES_PER_PACKET_POSITION];
				
			default:
				throw new MiddlewareException("This sensor read message is not a START_PERIODIC_READ but " + sr_type + " !");
		}
			
	}

	// START_PERIODIC_READ
	/**
	 * 
	 * @param packet_num the packet_num to set
	 */
	private void setPacketNum(short packet_num)
	{
		// nx_uint16_t packet_num;
		setShortField(packet_num, super.getPayload()+PACKET_NUMBER_POSITION, packet_num_is_nxle);
	}
	
	/**
	 * 
	 * @return the packet num
	 * @throws MiddlewareException 
	 */
	public short getPacketNum() throws MiddlewareException
	{
		SensorReadType sr_type = getSensorReadType();
		
		switch (sr_type)
		{
			case START_PERIODIC_READ:
				
				return getShortField(super.getPayload()+PACKET_NUMBER_POSITION, packet_num_is_nxle);
				
			default:
				throw new MiddlewareException("This sensor read message is not a START_PERIODIC_READ but " + sr_type + " !");		
		}	

	}
	
	// THRESHOLD_READ
	/**
	 * 
	 * @param thr_value the thr_value to set
	 */
	private void setThrValue(short thr_value)
	{
		// nx_uint16_t thr_value;
		setShortField(thr_value, super.getPayload()+THRESHOLD_VALUE_POSITION, thr_value_is_nxle);
	}
	
	/**
	 * 
	 * @return the thr value
	 * @throws MiddlewareException 
	 */
	public short getThrValue() throws MiddlewareException
	{
		SensorReadType sr_type = getSensorReadType();
		
		switch (sr_type)
		{
			case SET_UPPER_THRESHOLD_READ:
			case SET_LOWER_THRESHOLD_READ:
			case SET_READ_ON_CHANGE:
				
				return getShortField(super.getPayload()+THRESHOLD_VALUE_POSITION, thr_value_is_nxle);				

			default:
				throw new MiddlewareException("This sensor read message is not a THRESHOLD_READ but " + sr_type + " !");			
		}

	}
	
	// STOP_READ
	/**
	 * 
	 * @param handler the handler to set
	 */
	private void setHandlerToStop(byte handler)
	{			
		// nx_uint8_t handler;
		data_byte[super.getPayload()+HANDLER_POSITION] = handler;	
	}
	
	/**
	 * 
	 * @return the samples per packet
	 * @throws MiddlewareException 
	 */
	public byte getHandlerToStop() throws MiddlewareException
	{
		SensorReadType sr_type = getSensorReadType();
		
		switch (sr_type)
		{
			case STOP_READ:
				
				return data_byte[super.getPayload()+HANDLER_POSITION];
				
			default:
				throw new MiddlewareException("This sensor read message is not a STOP_READ but " + sr_type + " !");
		}
			
	}
	
	
}
