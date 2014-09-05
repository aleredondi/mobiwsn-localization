
package remote_interfaces.sensor;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Calendar;

import remote_interfaces.group.Groupable;
import remote_interfaces.mote.Mote;
import remote_interfaces.result.ValueResult;

import javax.measure.Measure;
import javax.measure.quantity.Duration;
import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;

import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;
import common.exceptions.ResponseTimeoutException;


/** 
 * Declares a basic abstract sensor class for interaction with 
 * simple sensors
 * @author Alessandro Laurucci
 * @version 1.1
 */
public interface Sensor<RT extends ValueResult, V, Q extends Quantity> extends Remote, Groupable
{	
	public static enum ReadTimes {
		REAL_TIME ( Measure.valueOf(0, SI.SECOND)),
		ULTRA_HIGH_SAMPLE_RATE ( Measure.valueOf(1 ,SI.SECOND) ),
		VERY_HIGH_SAMPLE_RATE ( Measure.valueOf(10, SI.SECOND) ),
		HIGH_SAMPLE_RATE (Measure.valueOf(30, SI.SECOND) ),
		NORM_SAMPLE_RATE (Measure.valueOf(60, SI.SECOND) ),
		LOW_SAMPLE_RATE ( Measure.valueOf(600 ,SI.SECOND) ),
		VERY_LOW_SAMPLE_RATE ( Measure.valueOf(1800, SI.SECOND) ),
		ULTRA_LOW_SAMPLE_RATE ( Measure.valueOf(3600, SI.SECOND) );
	
		
		private final Measure<Integer, Duration> time;
		
		ReadTimes( Measure<Integer, Duration> time ) {
			this.time = time;
		}
		
		public Measure<Integer, Duration> getTime() {
			return time;
		}
	}
			
	
	/**
	 * Perform a read operation on Sensor and return an object that contain the value reed from the sensor
	 * @return an Object result
	 * @throws MiddlewareException 
	 */
	//Object readValue() throws RemoteException, MiddlewareException;
	
	/**
	 * Perform a read operation the last value reed from Sensor and return an object that contain this value
	 * @return an Object result
	 * @throws MoteUnreachableException 
	 */
	RT getValue(Measure<Integer, Duration>  maxOldness) throws RemoteException, MiddlewareException, MoteUnreachableException, ResponseTimeoutException;
	RT getValue(Calendar  maxOldness) throws RemoteException, MiddlewareException, MoteUnreachableException, ResponseTimeoutException;

	/**
	 * This method is used to get two value that represent the range for the sensor reading
	 * @return the range
	 */
	public Measure<V,Q> getMaxVal() throws RemoteException;	

	public Measure<V,Q> getMinVal() throws RemoteException;	
	
	/**
	 * Generic write method
	 * @param value byte array to be written to the Sensor
	 * @return true if the write operation has been successful, false otherwise
	 */
	//boolean write(byte[] value) throws RemoteException;
	
	/**
	 * Instruct the sensor object to make periodic readings of the data
	 * and send it to the gateway 
	 * @param boSet true to enable periodic readings, false to disable them
	 * @param milliSecondPeriod, period of time after which perform a read operation
	 * @return true if the operation has been performed successfully, false otherwise
	 */
//	boolean setPeriodicRead(boolean boSet, int milliSecondPeriod) throws RemoteException;
	
	/**
	 * Instruct the sensor object to read and send data to the Gateway only when
	 * the sensed values are outside a given range
	 * @param boSet true to enable threshold readings, false to disable them
	 * @param upperThreshold byte array that represent the upper threshold value
	 * @param lowerThreshold byte array that represent the lower threshold value
	 * @return true if the operation has been performed successfully, false otherwise
	 */
//	boolean setReadByThreshold(boolean boSet, byte[] upperThreshold, byte[] lowerThreshold) throws RemoteException;
	
	/**
	 * 
	 * @return Identifier of this Sensor
	 */
	byte getId() throws RemoteException;
	
	
	/**
	 * 
	 * @return SensorType of this Sensor
	 */
	SensorType getType() throws RemoteException;
	
	
	/**
	 * 
	 * @return size of value read by the sensor
	 * @throws RemoteException
	 */
	int getValueLength() throws RemoteException;
	
	
	/**
	 * 
	 * @return Identifier of the Mote this Sensor is installed on 
	 */
	String getOwnerMoteId() throws RemoteException;
	Mote getOwnerMote() throws RemoteException;
	
	
	/**
	 * This method return the gateway with which the Sensor is connected
	 * @return the name of the Gateway 
	 * @throws RemoteException
	 */
	//String getWSNGatewayParent() throws RemoteException;
	
}
