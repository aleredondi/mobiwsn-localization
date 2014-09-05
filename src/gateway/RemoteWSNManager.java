package gateway;

import gateway.group.mote.InternalMoteGroup;
import gateway.mote.InternalMote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteGroup;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.sensor.SensorGroup;

public interface RemoteWSNManager extends Remote {

	/**
	 * Method used for getting the pan coordinator mote
	 * @return an object mote
	 * @throws RemoteException
	 */
	Mote getPanCoordinator() throws RemoteException;
		
	int getMoteNumber() throws RemoteException;
	
	/**
	 * Method used to get the list of the mote connected with this gateway
	 * @return an arraylist of the motes
	 * @throws RemoteException
	 */
	ArrayList<Mote> getMoteList() throws RemoteException;
	
	InternalMote getMote( String id ) throws RemoteException;
	
	/**
	 * Method used to refresh the mote list
	 * @throws RemoteException
	 */
	boolean refreshMoteList() throws RemoteException;
	
		
	//WSNGatewayInternal getGateway() throws RemoteException;
	
	//void setGateway( WSNGatewayInternal gw ) throws RemoteException;

	//ArrayList<Mote> getRemoteMoteList();



	/**
	 * Method used for getting the list of the MoteGroup object saved on this gateway
	 * @return the list of the group Object
	 * @throws RemoteException
	 */
	ArrayList<MoteGroup> getMoteGroupList() throws RemoteException;
	
	
	/**
	 * This method is used to create a new mote group
	 * @param groupName name of the group that the user wants to create
	 * @param moteList arrayList of the motes
	 * @return result indicates if the group is created or not
	 * @throws RemoteException
	 */
	boolean createMoteGroup(String groupName, Mote mote) throws RemoteException;
	boolean createMoteGroup(String groupName, ArrayList<Mote> moteList) throws RemoteException;
	
	
	/**
	 * This method is used to create a new mote group, used when the managaer has to send files to associate to group
	 * @param hostName represent the ip address of server
	 * @param port represent the port number of the server application
	 * @param groupName name of the group that the user wants to create
	 * @param moteList arrayList of the motes
	 * @param functions list of the functions associated to a specific group
	 * @param oldgw is teh name of the gateway to which the mote was connected before now (if it is the first oldgw=null)
	 * @return result indicates if the group is created or not
	 * @throws IOException, RemoteException 
	 */
	//boolean createMoteGroup(String hostName, int port, String groupName, ArrayList<Mote> moteList, ArrayList functions, String oldgw) throws IOException, RemoteException;
	
	
	/**
	 * This method is used to add a new mote to an existent group
	 * @param groupName name of the group
	 * @param mote mote to add
	 * @param oldgw is teh name of the gateway to which the mote was connected before now (if it is the first oldgw=null)
	 * @return a boolean that indicates if the operation is positive or not
	 * @throws RemoteException
	 */
	//boolean addMoteToGroup(String groupName, Mote mote, String oldgw) throws RemoteException;
	
	
	/**
	 * This method is used to remove a mote from an existent group
	 * @param groupName name of the group
	 * @param mote mote to remove
	 * @return if the operation is positive or not
	 * @throws RemoteException
	 */
	//int removeMoteFromGroup(String groupName, Mote mote) throws RemoteException;
	
	
	/**
	 * This method is used to delete a moteGroup
	 * @param groupName name of the group that the user wants to delete
	 * @return result indicates if the group is deleted or not
	 * @throws RemoteException
	 */
	boolean removeMoteGroup(String groupName) throws RemoteException;
	
	
	/**
	 * This method is used to get a moteGroup
	 * @param groupName name of the group that the user wants
	 * @return the object that represent the group
	 * @throws RemoteException
	 */
	MoteGroup getMoteGroup(String groupName) throws RemoteException;
	
	
	/**
	 * This method send the class of a mote group to another gateway
	 * @throws RemoteException
	 */
	//void sendMoteGroupClass(String group, String gwname) throws RemoteException;







	/**
	 * Method used for getting the list of the MoteGroup object saved on this gateway
	 * @return the list of the group Object
	 * @throws RemoteException
	 */
	ArrayList<SensorGroup> getSensorGroupList() throws RemoteException;
	
	
	/**
	 * This method is used to create a new mote group
	 * @param groupName name of the group that the user wants to create
	 * @param moteList arrayList of the motes
	 * @return result indicates if the group is created or not
	 * @throws RemoteException
	 */
	boolean createSensorGroup(String groupName, Sensor sensor) throws RemoteException;
	boolean createSensorGroup(String groupName, ArrayList<Sensor> sensorList) throws RemoteException;
	
	
	/**
	 * This method is used to create a new mote group, used when the managaer has to send files to associate to group
	 * @param hostName represent the ip address of server
	 * @param port represent the port number of the server application
	 * @param groupName name of the group that the user wants to create
	 * @param moteList arrayList of the motes
	 * @param functions list of the functions associated to a specific group
	 * @param oldgw is teh name of the gateway to which the mote was connected before now (if it is the first oldgw=null)
	 * @return result indicates if the group is created or not
	 * @throws IOException, RemoteException 
	 */
	//boolean createMoteGroup(String hostName, int port, String groupName, ArrayList<Mote> moteList, ArrayList functions, String oldgw) throws IOException, RemoteException;
	
	
	/**
	 * This method is used to add a new mote to an existent group
	 * @param groupName name of the group
	 * @param mote mote to add
	 * @param oldgw is teh name of the gateway to which the mote was connected before now (if it is the first oldgw=null)
	 * @return a boolean that indicates if the operation is positive or not
	 * @throws RemoteException
	 */
	//boolean addMoteToGroup(String groupName, Mote mote, String oldgw) throws RemoteException;
	
	
	/**
	 * This method is used to remove a mote from an existent group
	 * @param groupName name of the group
	 * @param mote mote to remove
	 * @return if the operation is positive or not
	 * @throws RemoteException
	 */
	//int removeMoteFromGroup(String groupName, Mote mote) throws RemoteException;
	
	
	/**
	 * This method is used to delete a moteGroup
	 * @param groupName name of the group that the user wants to delete
	 * @return result indicates if the group is deleted or not
	 * @throws RemoteException
	 */
	boolean removeSensorGroup(String groupName) throws RemoteException;
	
	
	/**
	 * This method is used to get a moteGroup
	 * @param groupName name of the group that the user wants
	 * @return the object that represent the group
	 * @throws RemoteException
	 */
	SensorGroup getSensorGroup(String groupName) throws RemoteException;
	
	
	/**
	 * This method send the class of a mote group to another gateway
	 * @throws RemoteException
	 */
	//void sendMoteGroupClass(String group, String gwname) throws RemoteException;
}
