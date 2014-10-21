
package gateway.sensor.pir;


import remote_interfaces.result.ValueResult;
import remote_interfaces.sensor.SensorType;

import gateway.mote.InternalMote;
import gateway.sensor.GenericInternalSensor;
import gateway.sensor.GenericRemoteSensor;

import java.rmi.RemoteException;

import javax.measure.Measure;
import javax.measure.unit.SI;
import javax.measure.quantity.DataAmount;

import common.result.ValueResultImpl;


public class PirInternalSensor extends GenericInternalSensor<ValueResult, Integer,DataAmount>
{
	private ValueResult value; //Object container of the value
	
	/**
	 * Constructor method
	 * @param sensorId id of the sensor
	 * @param owner Mote object that represents the mote this sensor is installed on
	 * @param sender reference to the MiddlewareMessageSender object used to send messages to the WSN
	 * @param gw name of the gateway to which the sensor (genuinely the mote) is connected
	 */
	public PirInternalSensor(byte sensorId,InternalMote owner) throws RemoteException
	{
		super(sensorId, owner, Measure.valueOf(new Byte((byte) 0), SI.BIT), Measure.valueOf(new Byte((byte) 1), SI.BIT) );
				
		this.sensorType = SensorType.INFRARED;
		//System.out.println("Son un pir sensor");
}
	
	
	/**
	 * Sets the sensor_value byte array
	 * @param sensor_value value to be set
	 */
	//public void setValue(byte[] sensor_value) 
	
	public void convertValue(short sensor_value) 
	{
		
		Measure<Integer,DataAmount> measure=Measure.valueOf(new Byte( (byte) sensor_value), SI.BIT);
		Measure<Integer,DataAmount> precision=Measure.valueOf(new Byte( (byte)1), SI.BIT);
		
		try {
			ValueResult<Integer, DataAmount> val = new ValueResultImpl<Integer, DataAmount>( this.remoteSensor, measure, precision );
			setValue(val);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * return an Object that contain the reed value
	 * @param sensor_value value to be set
	 */
	public Object getValue()
	{ 
		return this.value;
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
		short value =(short)((byteArray[1]*(2^8))+(byteArray[0]));
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
			return new PirRemoteSensor(this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return remoteSensor;
	}

}
