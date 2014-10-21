
package remote_interfaces;


import remote_interfaces.mote.Mote;
import remote_interfaces.sensor.Sensor;
//import common.classes.FunctionList;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

import common.exceptions.MiddlewareException;
import common.exceptions.RegisteringGatewayException;



/**
 * Class that implements all the need manager methods, used to manage its functions, 
 * it allows the manager to manage group, to register new gateway, and allows so the client to has information
 * about these things   
 * @author Alessandro Laurucci
 *
 */
public interface WSNGatewayManager extends Remote
{		
	/**
	 * @return the rmi address of manager
	 * @throws RemoteException
	 */
	String getRmiAddress() throws RemoteException;
	
	
	/**
	 * Methods which return the list of the gateway recorded on this manager
	 * @return list of gateways
	 * @throws RemoteException
	 */
	ArrayList<WSNGateway> getWSNGatewayList() throws RemoteException;
	
	
	/**
	 * This method is used to receive the pointer of a specific gateway object 
	 * @param gwName is the name of the gateway
	 * @return the Object WSNGateway requested
	 * @throws RemoteException
	 */
	WSNGateway getWSNGateway(String gwName) throws RemoteException;
	
	
	/**
	 * This method is used to register a new Gateway on the list
	 * @param gateway represent the new gateway to add
	 * @return the result of the registration
	 */
	boolean registerGateway(WSNGateway gateway) throws RemoteException, RegisteringGatewayException;
	
	boolean forceRegisterGateway(WSNGateway gateway) throws RemoteException;
	
	public boolean unregisterGateway(WSNGateway gateway) throws RemoteException;
	
	
//METHODS FOR MANAGER WSNGATEWAYGROUP
	
	/**
	 * This method is used to create a new WSNGatewayGroup
	 * @param groupName represent the name of the group that the user wants to create
	 * @param gwList represent a String list that contains the name of the gateway of the new created group
	 * @return result indicates if the group is created or not
	 * @throws RemoteException
	 */
	boolean createWSNGatewayGroup(String groupName, ArrayList<WSNGateway> gwList) throws RemoteException;
	
	
	/**
	 * This method is used to delete a WSNGatewayGroup
	 * @param groupName represent the name of the group that the user wants to delete
	 * @return result indicates if the group is deleted or not
	 * @throws RemoteException
	 */
	boolean removeWSNGatewayGroup(String groupName) throws RemoteException;
	
	
	/**
	 * This method is used to add a WSNGateway to an existent group
	 * @param groupName represent the name of the group
	 * @param gwName is the name of the Gateway
	 * @return result indicates if the Gateway is added/it is already on the list (true) or not and it is not added (false)
	 * @throws RemoteException
	 */
	boolean addWSNGatewayToGroup(String groupName, WSNGateway gwName) throws RemoteException;
	
	
	/**
	 * This method is used to remove a WSNGateway from a group
	 * @param groupName represent the name of the group
	 * @param gwName is the name of the Gateway
	 * @return result indicates if the Gateway is added/it is already on the list (true) or not and it is not added (false)
	 * @throws RemoteException
	 */
	boolean removeWSNGatewayFromGroup(String groupName, WSNGateway gwName) throws RemoteException;
	
	
	/**
	 * This method is used to get a list of WSNGateway that belong to a specific group
	 * @param groupName represent the name of the group
	 * @return an arrayList that contains the Gateway belong to a specific group
	 * @throws RemoteException
	 */
	ArrayList<WSNGateway> getWSNGatewayGroup(String groupName) throws RemoteException;
	
	/**
	 * This method is used to verify if the Gateway Groups list is empty  
	 * @return boolean to specify if the gateway groups list is empty or not
	 * @throws RemoteException
	 */
	public boolean isWSNGatewayGroupsListEmpty()throws RemoteException;
	
	/**
	 * This method is used to get the names of the groups of gateway   
	 * @return ArrayList<String> to specify the names of the groups
	 * @throws RemoteException
	 */
	public ArrayList<String> getWSNGatewayGroupsNames()throws RemoteException;
	
	//METHODS FOR MANAGE MOTE GROUP
	
	/**
	 * This method is used to create a new MoteGroup
	 * @param groupName represent the name of the group that the user wants to create
	 * @param moteList represent the list of the motes for the new created group
	 * @return result indicates if the group is created or not
	 * @throws RemoteException
	 */
	boolean createMoteGroup(String groupName, Mote mote) throws RemoteException;
	boolean createMoteGroup(String groupName, ArrayList<Mote> moteList) throws RemoteException;
	
	
	/**
	 * This method is used to create a new MoteGroup
	 * @param hostName represent the ip address of the client
	 * @param port represent the port number of the client application
	 * @param groupName represent the name of the group that the user wants to create
	 * @param moteList represent the list of the motes for the new created group
	 * @param functions list of the functions associated to a specific group
	 * @return result indicates if the group is created or not
	 * @throws IOException, RemoteException 
	 */
	//TODO Sistemare
	//boolean createMoteGroup(/*String client_name,*/ String hostName, int port, String groupName, ArrayList<Mote> moteList, ArrayList function) throws IOException, RemoteException;
	
	/**
	 * This method is used to delete a moteGroup
	 * @param groupName represent the name of the group that the user wants to delete
	 * @return result indicates if the group is deleted or not
	 * @throws RemoteException
	 */
	boolean removeMoteGroup(String groupName) throws RemoteException;
	
	
	/**
	 * This method is used to add a mote to an existent group
	 * @param groupName represent the name of the group
	 * @param mote is the mote to add
	 * @return result indicates if the Gateway is added/it is already on the list (true) or not and it is not added (false)
	 * @throws RemoteException
	 */
	boolean addMoteToGroup(String groupName, Mote mote) throws RemoteException;
	
	
	/**
	 * This method is used to remove a Mote from a group
	 * @param groupName represent the name of the group
	 * @param mote is the mote to remove
	 * @return result indicates if the Mote is added/it is already on the list (true) or not and it is not added (false)
	 * @throws RemoteException
	 */
	boolean removeMoteFromGroup(String groupName, Mote mote) throws RemoteException;
	
	
	/**
	 * This method is used to get a list of Mote that belong to a specific group
	 * @param groupName represent the name of the group
	 * @return an arrayList that contains the Mote belong to a specific group
	 * @throws RemoteException
	 */
	ArrayList<Mote> getMoteGroup(String groupName) throws RemoteException;
	
	
	/**
	 * This method allows the user to invoke a specific method for a group a motes using a single invocation
	 * @param nameClass name of the class which has the method
	 * @param method is the name of the method to use for the group
	 * @param groupName is the name of the group
	 * @param parameterTypes are the types of the input parameters
	 * @param paramIn are the input parameters for the method (null if it hasn't parameters)
	 * @return the result of the method desired used on each mote of the group
	 * @throws RemoteException
	 */
	ArrayList<Object> useGroupMoteMethod(String nameClass, String method, String groupName, Class[] parameterTypes, Object[] paramIn) throws RemoteException;	
	
	
	/**
	 * Search the previous gateway to which a mobile mote was connected
	 * @param id of the mote to search
	 * @return the name of the gateway which has the mote connected
	 * @throws RemoteException 
	 */
	String searchingMoteInNetwork(String id) throws RemoteException;
	
	/**
	 * This method is used to verify if the Motes Groups list is empty  
	 * @return boolean  to specify if the motes list is empty or not
	 * @throws RemoteException
	 */
	public boolean isMoteGroupsListEmpty() throws RemoteException;
	
	/**
	 * This method is used to get the names of the groups of motes 
	 * @return ArrayList<String>  to specify the names of the groups
	 * @throws RemoteException
	 */
	public ArrayList<String> getMoteGroupsNames()throws RemoteException;
	
//METHODS FOR MANAGE SENSOR GROUP
	
	
	/**
	 * This method is used to create a new SensorGroup
	 * @param groupName represent the name of the group that the user wants to create
	 * @param sensorList represent a list that contains the sensor to add on the new created group
	 * @return result indicates if the group is created or not
	 * @throws RemoteException
	 */
	boolean createSensorGroup(String groupName, ArrayList<Sensor> sensorList) throws RemoteException;
	
	
	/**
	 * This method is used to delete a sensorGroup
	 * @param groupName represent the name of the group that the user wants to delete
	 * @return result indicates if the group is deleted or not
	 * @throws RemoteException
	 */
	boolean removeSensorGroup(String groupName) throws RemoteException;
	
	
	/**
	 * This method is used to add a sensor to an existent group
	 * @param groupName represent the name of the group
	 * @param sensor is the sensor to add
	 * @return result indicates if the Gateway is added/it is already on the list (true) or not and it is not added (false)
	 * @throws RemoteException
	 */
	boolean addSensorToGroup(String groupName, Sensor sensor) throws RemoteException;
	
	
	/**
	 * This method is used to remove a Sensor from a group
	 * @param groupName represent the name of the group
	 * @param sensor is the sensor to remove
	 * @return result indicates if the Sensor is added/it is already on the list (true) or not and it is not added (false)
	 * @throws RemoteException
	 */
	boolean removeSensorFromGroup(String groupName, Sensor sensor) throws RemoteException;
	
	
	/**
	 * This method is used to get a list of Sensor that belong to a specific group
	 * @param groupName represent the name of the group
	 * @return an arrayList that contains the Sensor belong to a specific group
	 * @throws RemoteException
	 */
	ArrayList<Sensor> getSensorGroup(String groupName) throws RemoteException;
	
	
	/**
	 * This method allows the user to invoke a specific method for a group a Sensors using a single invocation
	 * @param method is the name of the method to use for the group
	 * @param groupName is the name of the group
	 * @param parameterTypes are the types of the input parameters
	 * @param paramIn are the input parameters for the method (null if it hasn't parameters)
	 * @return the result of the method desired used on each Sensor of the group
	 * @throws RemoteException
	 */
	ArrayList<Object> useGroupSensorMethod(String method, String groupName, Class[] parameterTypes, Object[] paramIn) throws RemoteException;



	
	//boolean sendAddMotesToGroup(String groupName) throws RemoteException, MiddlewareException;

}
