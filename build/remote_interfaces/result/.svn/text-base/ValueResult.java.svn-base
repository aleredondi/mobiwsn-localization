
package remote_interfaces.result;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Calendar;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Dimension;

import remote_interfaces.sensor.Sensor;


/**
 * This interface is used from the user to invoke the method of the object ValueResultImpl
 * in this case this object has the same method of the ObjectResultInterface so no one method is added
 * @author Alessandro Laurucci
 */
public interface ValueResult<V, Q extends Quantity> extends Remote
{	
	public Measure<V,Q> getValue() throws RemoteException;

	/**
	 * This method is used to get the precision of the value reed
	 * @return the precision of the value
	 */
	Measure<V,Q> getPrecision()throws RemoteException;

//	/**
//	 * This method is used to get two value that represent the range for the sensor reading
//	 * @return the range
//	 */
//	public Measure<V,Q> getMaxVal() throws RemoteException;	
//
//	public Measure<V,Q> getMinVal() throws RemoteException;	

	public Calendar getTimeRead() throws RemoteException;
	
	/**
	 * This method is used to get the name of the gateway which has this read value saved
	 * @return the name of gateway
	 */
	public Sensor getSensor() throws RemoteException;
	
}
