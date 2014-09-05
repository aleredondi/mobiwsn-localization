
package remote_interfaces.sensor;


import remote_interfaces.group.Group;
import remote_interfaces.sensor.Sensor;

import java.rmi.*;
import java.util.*;


/**
 * @author Alessandro Laurucci
 *
 */
public interface SensorGroup extends Remote, Group<Sensor>
{	
	/**
	 * Method used to get the name of the group
	 * @return the name
	 */
	//String getName() throws RemoteException;
	
	
	/**
	 * This method is used to add a Sensor to the list of this group
	 * @param sensor is object Sensor to add
	 * @throws RemoteException 
	 */
	//void addSensor(Sensor sensor) throws RemoteException;

	
	/**
	 * This method is used for getting the id of the group
	 * @return the Id of the group
	 * @throws RemoteException
	 */
	//int getId() throws RemoteException;

	
	/**
	 * This method is used to get the list of the motes that belong to this group
	 * @throws RemoteException 
	 */
	//ArrayList<Sensor> getSensors() throws RemoteException;
	

	/**
	 * This method is used to remove a Sensor from the group
	 * @param Idsensor Sensor to remove
	 * @throws RemoteException 
	 */
	//void removeSensor(byte Idsensor) throws RemoteException;
	

	/**
	 * This method allow the user to invoke with a single function call a method defined in the Sensor class
	 * for all the sensors of the group 
	 * @param methodName name of the method to invoke
	 * @param parameterTypes type of the input of the method
	 * @param paramIn list of the input
	 * @return the arrayList of the result of the method for each sensor of the group 
	 * @throws RemoteException 
	 */
	//ArrayList<Object> useGroupMethod(String methodName, Class[] parameterTypes, Object[] paramIn) throws RemoteException;
	
	/**
	 * This method is used for searching a specific Sensor in the list of this object
	 * @param own mote which has the sensor attached
	 * @param id string that represent the name of the Sensor to search
	 * @return a boolean, true if the Sensor is in the list, false if it is not.
	 */
	//boolean sensorSearch(String own, byte id) throws RemoteException;
	
}