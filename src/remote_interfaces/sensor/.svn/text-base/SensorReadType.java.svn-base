package remote_interfaces.sensor;

public enum SensorReadType {

	UNDEFINED,
    SIMPLE_SENSOR_READ,
    START_PERIODIC_READ,
    SET_UPPER_THRESHOLD_READ,
    SET_LOWER_THRESHOLD_READ,
    SET_READ_ON_CHANGE,
    STOP_READ;
	
	public static SensorReadType convert(int i)
	{
		try 
		{
			return values()[i];
		} 
		catch ( ArrayIndexOutOfBoundsException e ) 
		{
			return UNDEFINED;
		}
	}	
	
}
