
package gateway.sensor;


import remote_interfaces.sensor.*;

import gateway.comm.MessageSender;
import gateway.mote.InternalMote;
import gateway.sensor.acceleration.AccelerationXInternalSensor;
import gateway.sensor.acceleration.AccelerationYInternalSensor;
import gateway.sensor.light.LightInternalSensor;
import gateway.sensor.noise.NoiseInternalSensor;
import gateway.sensor.pir.PirInternalSensor;
import gateway.sensor.temperature.TemperatureInternalSensor;
import gateway.sensor.acceleration.*;

import java.rmi.RemoteException;

import common.exceptions.ExceptionHandler;


/**
 * @author Davide Roveran
 *
 * Provides a method to instantiate the right type of Sensor object
 */
public class SensorFactory 
{
	public static InternalSensor createSensorInstance(
										byte sensorId,
										SensorType type, 
										InternalMote ownerMote)
	{
		try
		{
			System.out.println("New sensor created: ");
			System.out.println("    Id = " + sensorId);
			System.out.println("    Type = " + type.name());
			System.out.println("------------------------------------------------------------\n");	
			
			switch (type)
			{
				
				case VOLTAGE:
					//return new VoltageSensor(sensorId, ownerMote, sender);
					break;
					
				case TEMPERATURE :
					return new TemperatureInternalSensor(sensorId, ownerMote);
				
				case LIGHT :
					return new LightInternalSensor(sensorId, ownerMote);
						
				case NOISE :
					return new NoiseInternalSensor(sensorId, ownerMote);
					
				case INFRARED:
					return new PirInternalSensor(sensorId, ownerMote);
					
				case ACCELEROMETERX:
					return new AccelerationXInternalSensor(sensorId, ownerMote);
					
				case ACCELEROMETERY:
					return new AccelerationYInternalSensor(sensorId, ownerMote);
					
				case ACCELEROMETERZ:
					return new AccelerationZInternalSensor(sensorId, ownerMote);
					
				default:
					return null;	
			}	
			
		}
		catch (RemoteException rex)
		{
			ExceptionHandler.getInstance().handleException(rex);
		}
		
		return null;
		
	}

}
