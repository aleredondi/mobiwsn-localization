
package remote_interfaces.mote;


import remote_interfaces.WSNGateway;
import remote_interfaces.functionality.Functionality;
import remote_interfaces.functionality.FunctionalityType;
import remote_interfaces.group.Groupable;
import remote_interfaces.protocol.NetworkAddress;
import remote_interfaces.sensor.Sensor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

import common.exceptions.MoteUnreachableException;


/**
 * @author Davide Roveran
 * @version 1.0
 * 
 * Basic interface of a Mote object
 */
public interface Mote extends Remote, Groupable 
{
	
	final static String MOTE_BROADCAST_ID = "FFFFFFFF";
	final static String MOTE_NULL_ID = "00000000";
	final static short NWK_NULL_ADDRESS = (short) 0xFFFF;
	final static short MAC_NULL_ADDRESS = (short) -1;
	
	/**
	 * 
	 * @return identifier assigned to the Mote
	 */
	String getId() throws RemoteException;

	MoteType getType() throws RemoteException;
	
	/**
	 * send a message to the mote represented by this Mote, to 
	 * change the identifier of the mote
	 * 
	 * @param id identifier to be assigned to the mote
	 * @return true if the operation is successful;
	 */
	boolean changeId(String id) throws RemoteException, MoteUnreachableException;
	
	/**
     *
	 * @return current network address of the mote
	 */
	NetworkAddress getNetworkAddress() throws RemoteException;
	
	/**  
	 * @return MAC address of the mote
	 */
	short getMACAddress() throws RemoteException;
	

	/**
	 * 
	 * @return true if this Mote is PAN Coordinator of the network
	 */
	boolean isPanCoordinator() throws RemoteException;
	
	/**
	 * sends a message to the mote to read fresh data
	 * @return true if the operation is successful
	 */
	boolean refreshData() throws RemoteException, MoteUnreachableException;
	
	
	/**
	 * Returns a reference to the parent of this Mote.
	 * @return
	 */
	Mote getParentMote() throws RemoteException;
	
	int getChildNumber() throws RemoteException;
	
	Vector<Mote> getChildMotes() throws RemoteException;
	
	boolean hasChild( Mote mote ) throws RemoteException;
	
	/**
	 * 
	 * @return True if the corresponding Mote in the WSN is reachable
	 */
	boolean isReachable() throws RemoteException;

	 /**
	 * This method return the gateway with which the mote is connected
	 * @return the name of the Gateway 
	 * @throws RemoteException
	 */
	WSNGateway getWSNGatewayParent() throws RemoteException;
	//Get parent mote
	
	/**
	 * This method add element to the list
	 * @param name of the object
	 * @param groupName name of the group
	 * @param typeIn represent the inputs type of the method requested
	 * @param paramIn contain the inputs of the method
	 * @throws RemoteException
	 * @throws ClassNotFoundException, RemoteException
	 */
	void addObjectToList(String name, String groupName, Class typeIn[], Object paramIn[])  throws RemoteException, ClassNotFoundException;
	
	/**
	 * This method is used by the client to invoke the methods of the objects loaded on this mote
	 * @param inputSearched list of parameters input for the searching
	 * @param typeIn represent the inputs type of the method requested
	 * @param paramIn contain the inputs of the method
	 * @return the method return an object that represent the result of the method invoked
	 * @throws RemoteException
	 */
	Object useMethod(ArrayList<String> inputSearched,String nameMethod, Class typeIn[], Object paramIn[]) throws RemoteException;

	
	/**
	 * Method used for deleting an object from the list
	 * @param inputSearch
	 */
	void removeObjectFromList(ArrayList<String> inputSearch) throws RemoteException;
	
	
	/**
	 * print the list of the function loaded on gateway
	 * @throws RemoteException
	 */
	void printList() throws RemoteException;

	
	
	//Functionality
	int getFunctionalityNumber() throws RemoteException;
	boolean isFunctionalityInList(byte id, FunctionalityType type) throws RemoteException;

	/**
	 * @return List of Functionality currently installed on the node;
	 */
	ArrayList<Functionality> getFunctionalityList() throws RemoteException;

	/**
	 * @return List of Sensors currently attached to the node;
	 */
	ArrayList<Sensor> getSensorList() throws RemoteException;

	//void setMoteGroupName(String mGroupName) throws RemoteException;
	//String getMoteGroupName() throws RemoteException;
	
}
