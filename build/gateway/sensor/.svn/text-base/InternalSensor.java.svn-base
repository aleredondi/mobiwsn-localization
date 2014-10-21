package gateway.sensor;

import gateway.comm.MessageListener;
import gateway.group.InternalGroupable;
import gateway.group.sensor.InternalSensorGroup;
import gateway.mote.InternalMote;
import gateway.protocol.SensorReadResponseMessage;

import java.util.Calendar;

import remote_interfaces.result.ValueResult;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.sensor.SensorType;
import remote_interfaces.sensor.Sensor.ReadTimes;

import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;
import common.exceptions.ResponseTimeoutException;

import javax.measure.Measure;
import javax.measure.quantity.Duration;
import javax.measure.quantity.Quantity;

public interface InternalSensor<RV extends ValueResult, V, Q extends Quantity> extends InternalGroupable<InternalSensorGroup>, MessageListener<SensorReadResponseMessage> {	
	
	/**
	 * Perform a read operation on Sensor and return an object that contain the value reed from the sensor
	 * @return an Object result
	 * @throws MiddlewareException 
	 */
	RV getValue(Measure<Integer,Duration> maxOldness) throws MiddlewareException, MoteUnreachableException, ResponseTimeoutException;
	RV getValue(Calendar maxOldness) throws MiddlewareException, MoteUnreachableException, ResponseTimeoutException;

	/**
	 * This method is used to get two value that represent the range for the sensor reading
	 * @return the range
	 */
	public Measure<V,Q> getMaxVal();	

	public Measure<V,Q> getMinVal();	

//	/**
//	 * Perform a read operation the last value reed from Sensor and return an object that contain this value
//	 * @return an Object result
//	 */
//	Object getValue();
//	
//	/**
//	 * Generic write method
//	 * @param value byte array to be written to the Sensor
//	 * @return true if the write operation has been successful, false otherwise
//	 */
//	//boolean write(byte[] value) throws RemoteException;
	
	/**
	 * Instruct the sensor object to make periodic readings of the data
	 * and send it to the gateway 
	 * @param boSet true to enable periodic readings, false to disable them
	 * @param milliSecondPeriod, period of time after which perform a read operation
	 * @return true if the operation has been performed successfully, false otherwise
	 */
//	boolean setPeriodicRead( ReadTimes time, int sample_per_packet, int packet_number );
	
	/**
	 * Instruct the sensor object to read and send data to the Gateway only when
	 * the sensed values are outside a given range
	 * @param boSet true to enable threshold readings, false to disable them
	 * @param upperThreshold byte array that represent the upper threshold value
	 * @param lowerThreshold byte array that represent the lower threshold value
	 * @return true if the operation has been performed successfully, false otherwise
	 */
//	boolean setReadByThreshold(boolean boSet, byte[] upperThreshold, byte[] lowerThreshold);
	
	/**
	 * 
	 * @return Identifier of this Sensor
	 */
	byte getId();
	
	
	/**
	 * 
	 * @return SensorType of this Sensor
	 */
	SensorType getType();
	
	
	/**
	 * 
	 * @return size of value read by the sensor
	 * @throws RemoteException
	 */
	int getValueLength();
	
	
	/**
	 * 
	 * @return Identifier of the Mote this Sensor is installed on 
	 */
	String getOwnerMoteId();
	
	InternalMote getOwnerMote();
		
}
