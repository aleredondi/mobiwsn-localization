
package common.result;


import remote_interfaces.result.ValueResult;
import remote_interfaces.sensor.Sensor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;


/**
 * Implementation of the ValueResult interface, this class save inside the last value reed and all the properties 
 * about this value and about the sensor that reed it
 * @author Alessandro Laurucci
 */
public class ValueResultImpl<V, Q extends Quantity> extends UnicastRemoteObject implements ValueResult<V,Q>
{
	private Measure<V,Q> precision; //define the precision of the value reed from sensor
	private Measure<V,Q> value; //last value read
	private Calendar date;
	private Sensor sensor;
	
	
	/**
	 * Constructor method
	 * @param prec precision of the value reed from sensor
	 * @param unit misure unit
	 * @param measure initialize the first measure read, with a default set by the user, who sets also the form and the unit of this value
	 * @param rangeTop is the the top of the range trough which the sensor works
	 * @param rangeDown is the lowest value of the range trough which the sensor works
	 * @throws RemoteException
	 */
	public ValueResultImpl(Sensor sensor, Measure<V,Q> measure, Measure<V,Q> prec) throws RemoteException
	{
		this.precision=prec;
		this.value= measure;
		this.sensor=sensor;
		this.date=new GregorianCalendar();
	}
	
	/**
	 * This method is used to get the precision of the value reed
	 * @return the precision of the value
	 */
	public Measure<V,Q> getPrecision() throws RemoteException
	{
		return this.precision;
	}
	
	
//	/**
//	 * This method is used to get the object that represent the unit of the value 
//	 * @return the misureUnit
//	 */
//	public String getUnitMisure() throws RemoteException
//	{
//		return this.misureUnit;
//	}
	
	
	/**
	 * This method is used to get the value reed on the sensor
	 * @return the value
	 */
	public Measure<V,Q> getValue() throws RemoteException
	{
		return this.value;
	}
	
	
//	/**
//	 * This method is used to get two value that represent the range for the sensor reading
//	 * @return the range
//	 */
//	public Measure<V,Q> getMaxVal() throws RemoteException
//	{
//		return sensor.getMaxVal();
//	}
//	
//	/**
//	 * This method is used to get two value that represent the range for the sensor reading
//	 * @return the range
//	 */
//	public Measure<V,Q> getMinVal() throws RemoteException
//	{
//		return sensor.getMinVal();
//	}
//	
//	/**
//	 * This method is used to set the the instant and the value of the read from sensor
//	 */
//	public void setValue(Measure<?,?> m) throws RemoteException
//	{
//		this.date=new GregorianCalendar();
//
//		this.value= m;
//	}
//	
//	/**
//	 * Method used to set the data of the reading
//	 * @param time date to set
//	 */
//	public void setData(Calendar time) throws RemoteException
//	{
//		this.date=time;
//	}
	
	/**
	 * This method is used to get the the instant of the read from sensor
	 * @return the time
	 */
	public Calendar getTimeRead() throws RemoteException
	{
		return this.date;
	}
	
	/**
	 * This method is used to get the name of the gateway which has this read value saved
	 * @return the name of gateway
	 */
	public Sensor getSensor() throws RemoteException
	{
		return this.sensor;
	}

	
}
