package gateway;


import gateway.comm.MessageSender;
import gateway.group.mote.InternalMoteGroup;
import gateway.group.sensor.InternalSensorGroup;
import gateway.mote.InternalMote;
import gateway.protocol.basic_message.MiddlewareMessage;
import gateway.sensor.InternalSensor;

import java.rmi.RemoteException;
import java.util.ArrayList;

import remote_interfaces.Remotizable;


public interface InternalWSNManager extends MessageSender<MiddlewareMessage>, Remotizable {

	/**
	 * Method used for getting the pan coordinator mote
	 * @return an object mote
	 * @throws RemoteException
	 */
	InternalMote getPanCoordinator();
		
	int getMoteNumber();
	
	/**
	 * Method used to get the list of the mote connected with this gateway
	 * @return an arraylist of the motes
	 * @throws RemoteException
	 */
	ArrayList<InternalMote> getMoteList() ;
	
	//InternalMote getInternalMote( Mote mote );
	InternalMote getInternalMote( String id );
	InternalMote getInternalMote( short macAddress );
	
	/**
	 * Method used to refresh the mote list
	 * @throws RemoteException
	 */
	boolean refreshMoteList();
	
	
	/**
	 * This function is used to remove the object internalMote and the object mote 
	 * when a mobile mote is disconnected from this specific gateway
	 * @param moteId id of mote
	 * @throws RemoteException
	 */
	void removeMote(String moteId);
	
	InternalWSNGateway getGateway();
	
	void setGateway( InternalWSNGateway gw );

	//ArrayList<Mote> getRemoteMoteList();

	
	/**
	 * This method is used to create a new mote group
	 * @param groupName name of the group that the user wants to create
	 * @param moteList arrayList of the motes
	 * @return result indicates if the group is created or not
	 * @throws RemoteException
	 */
	boolean createMoteGroup(String groupName, InternalMote mote);
	boolean createMoteGroup(String groupName, ArrayList<InternalMote> moteList);
	
	
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
	boolean removeMoteGroup(String groupName);
	
	
	/**
	 * Method used for getting the list of the MoteGroup object saved on this gateway
	 * @return the list of the group Object
	 * @throws RemoteException
	 */
	ArrayList<InternalMoteGroup> getMoteGroupList();
	InternalMoteGroup getMoteGroup( String groupName );
	
	
	
	
	//Sensor	
	
	/**
	 * This method send the class of a mote group to another gateway
	 * @throws RemoteException
	 */
	//void sendMoteGroupClass(String group, String gwname) throws RemoteException;


	
	
	/**
	 * This method is used to create a new mote group
	 * @param groupName name of the group that the user wants to create
	 * @param moteList arrayList of the motes
	 * @return result indicates if the group is created or not
	 * @throws RemoteException
	 */
	boolean createSensorGroup(String groupName, InternalSensor sensor);
	boolean createSensorGroup(String groupName, ArrayList<InternalSensor> sensorList);
	
	
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
	boolean removeSensorGroup(String groupName);
	
	
	/**
	 * This method is used to get a moteGroup
	 * @param groupName name of the group that the user wants
	 * @return the object that represent the group
	 * @throws RemoteException
	 */
	InternalSensorGroup getSensorGroup(String groupName);
	
	
	/**
	 * This method send the class of a mote group to another gateway
	 * @throws RemoteException
	 */
	//void sendMoteGroupClass(String group, String gwname) throws RemoteException;

}
