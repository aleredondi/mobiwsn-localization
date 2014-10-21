
package gateway.sensor.temperature;


import remote_interfaces.result.ValueResult;
import remote_interfaces.sensor.SensorType;

import gateway.mote.InternalMote;
import gateway.sensor.GenericInternalSensor;
import gateway.sensor.GenericRemoteSensor;

import java.rmi.RemoteException;

import javax.measure.Measure;
import javax.measure.quantity.Temperature;
import javax.measure.unit.SI;

import common.result.ValueResultImpl;


/**
 * Class which is sued from the gateway to maintain the values reed from a temperature sensor, in fact this object has inside
 * a ValueResultImpl object that allows this. Besides a Temperature sensor object has other methods to manage sensor and values, 
 * as converter from byte to int,...
 * @author Alessandro Laurucci
 * @version 1.0
 */
public class TemperatureInternalSensor extends GenericInternalSensor<ValueResult,Double,Temperature>
{
	
	private final static long serialVersionUID=772847294302L;
	
//	private ValueResult value; //Object container of the value
	
	/**
	 * Constructor method
	 * @param sensorId id of the sensor
	 * @param owner Mote object that represents the mote this sensor is installed on
	 * @param sender reference to the Value object used to send messages to the WSN
	 * @param gw name of the gateway to which the sensor (genuinely the mote) is connected
	 */
	public TemperatureInternalSensor(byte sensorId,InternalMote owner) throws RemoteException
	{
		super(sensorId, owner, Measure.valueOf(new Double(0), SI.CELSIUS), Measure.valueOf(new Double(0), SI.CELSIUS));
		
		this.sensorType = SensorType.TEMPERATURE;
		
		//System.out.println("Son un temp sensor");
	}
	
	
	/**
	 * Sets the sensor_value byte array
	 * @param sensor_value value to be set
	 */
	//public void setValue(byte[] sensor_value) 
	public void convertValue(short sensor_value) 
	
	{
		//Converte il valore di temperatura per l'mts300 dei micaz è da sistemare per gli altri
		double ADC =  sensor_value;
		
/*		Formula presa dal user guide della crossbow page 3 cap 2.2
		2.2 Conversion to Engineering Units
		The Mote’s ADC output can be converted to Kelvin using the following approximation over 0 to
		50 °C:
		    1/T(K) = a + b × ln(Rthr) + c × [ln(Rthr)]3
		    where:
		      Rthr = R1(ADC_FS-ADC)/ADC
		      a = 0.001010024
		      b = 0.000242127
		      c = 0.000000146
		      R1 = 10 kΩ
		      ADC_FS = 1023, and
		      ADC = output value from Mote’s ADC measurement
*/
		
		final double R1 = 10000; //kOhm
		final double ADC_FS = 1023;
		final double a = 0.001010024;
		final double b = 0.000242127;
		final double c = 0.000000146;
		
		double R_thr = R1*(ADC_FS-ADC)/ADC;
		double T = 1/(a+b*Math.log(R_thr)+c*(Math.pow(Math.log(R_thr), 3)));
		
		Measure<Double,Temperature> measure=Measure.valueOf(new Double(T), SI.KELVIN);
		Measure<Double,Temperature> precision=Measure.valueOf(new Double(0.2), SI.CELSIUS);
		
		try {
			ValueResult<Double,Temperature> val = new ValueResultImpl<Double,Temperature>( this.remoteSensor, measure, precision );
			setValue(val);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		
	
	/**
	 * Instruct the sensor object to read and send data to the Gateway only when
	 * the sensed values are outside a given range
	 * @param boSet true to enable 
	 * @param upperThreshold represent the upper temperature threshold value
	 * @param lowerThreshold represent the lower temperature threshold value
	 * @return true if the operation has been performed successfully, false otherwise
	 */	
	public boolean setReadByThreshold(boolean boSet, short lowerThreshold, short upperThreshold) 
	{
		//	NOT IMPLEMENTED YET	
		//	MiddlewareMessage message = new MiddlewareMessage(owner.getName(false));
		//		
		//		message.setEnable(boSet);
		//		message.setThreshold(
		//						shortToByteArray(upperThreshold), 
		//						shortToByteArray(lowerThreshold)
		//							);
		//		
		//		sender.sendMessageTask(message);
		//		
		//		return message.isWritten();
		
		return false;
	} 
	
	
	/**
	 * method used to get the length of the reed values
	 * @return the length of the value reed
	 */
	public int getValueLength()
	{
		return  Short.SIZE/8;
	}
	
	
	/**
	 * Convert short temperature value in byte to a short
	 * @param value is the value to convert
	 * @return the value after conversion
	 */
	private short byteArrayToShort(byte[] byteArray)
	{
		short value =(short)((byteArray[0]*(2^8))+(byteArray[1]));
		return value;
	}
	
	
	/**
	 * Convert short temperature value to byte array
	 * @param value is the value to convert
	 * @return the value after conversion
	 */
	private byte[] shortToByteArray(short value)
	{
		byte[] temperatureByteArray = new byte[2];
		temperatureByteArray[0] = (byte) ((value & 65280) >> 8);
		temperatureByteArray[1] = (byte) (value & 255);
		
		return temperatureByteArray;	
	}


	@Override
	protected GenericRemoteSensor createRemote() {
		try {
			return new TemperatureRemoteSensor(this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return remoteSensor;
	}


	@Override
	public TemperatureRemoteSensor getRemote() {
		return (TemperatureRemoteSensor) remoteSensor;
	}


	/*public void messageReceived(SensorReadResponseMessage message) {
		// TODO Questo non deve esserci		
	}*/

}
