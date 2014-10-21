
package common.result;


import remote_interfaces.result.ArrayResult;
import remote_interfaces.sensor.Sensor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.measure.Measure;


/**
 * Implementation of the ArrayResult interface, this class save inside the last value reed and all the properties 
 * about this value and about the sensor that reed it
 * @author Alessandro Laurucci
 */
public class ArrayResultImpl extends UnicastRemoteObject implements ArrayResult
{
	public ArrayResultImpl() throws RemoteException
	{}
	
	/**
	 * This method is used to set the value reed in the Object
	 */
	public void setValue(ArrayList<Measure> list) 
	{}
	
	
	/**
	 * This method is used to get the precision of the value reed
	 * @return the precision of the value
	 */
	public int getPrecision()
	{	
		int a=0;
		return a;
	}
	/**
	 * This method is used to get the object that represent the unit of the value 
	 * @return the misureUnit
	 */
	public String getUnitMisure()
	{
		String a="";
		return a;
	}
	/**
	 * This method is used to get the values reed on the sensor
	 * @return the list of values
	 */
	public ArrayList<Measure> getValue()
	{
		ArrayList<Measure> list=null;
		return list;
	}
	
	public GregorianCalendar getTimeRead()
	{
		return new GregorianCalendar();
	}
	
	/**
	 * This method is used to get two value that represent the range for the sensor reading
	 * @return the range
	 */
	public Measure<?,?>[] getRange() throws RemoteException
	{
		return new Measure<?,?>[2];
	}
	
	/**
	 * This method is used to get the name of the gateway which has this read value saved
	 * @return the name of gateway
	 */
	public String getParent() throws RemoteException
	{
		String a=null;
		return a;
	}

	public Sensor getSensor() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
