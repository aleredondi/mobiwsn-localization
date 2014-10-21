
package remote_interfaces.sensor;


/**
 * @author Davide Roveran
 * @version 1.0
 *
 */
public enum SensorType 
{
	UNDEFINED,
	VOLTAGE,
	TEMPERATURE,
	PRESSURE,
	HUMIDITY,
	LIGHT,
	ACCELEROMETERX,
	ACCELEROMETERY,
	ACCELEROMETERZ,
	MAGNETOMETERX,
	MAGNETOMETERY,
	MICROPHONERAW,
	MICROPHONETUNE,
	SOUNDER,
	NOISE,
	INFRARED;
	
	public static SensorType convert(int i)
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
