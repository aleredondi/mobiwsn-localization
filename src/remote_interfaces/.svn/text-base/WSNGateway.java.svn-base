
package remote_interfaces;


import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteGroup;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.sensor.SensorGroup;
import remote_interfaces.services.Publisher;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

import common.exceptions.MiddlewareException;


/**
 * 
 * @author Alessandro Laurucci
 * @version 1.1
 *
 * Defines the basic gateway component to a wireless sensor network.
 * Depending on the specific PAN Coordinator mote connected to the Mesh Router,
 * this component must create the right interface component with which it can
 * exchange information from/to the WSN, following the rules defined for the middleware
 * protocol
 */
public interface WSNGateway extends Remote, Publisher<WSNGateway, ArrayList<Mote>>
{	
	/**
	 * This method is used to get the coordinates of the gateway
	 * @return an array with the coordinates of the gateway
	 * @throws RemoteException
	 */
	int[] getCoord() throws RemoteException;
	
	/**
	 * Method used to stop interface
	 * @throws RemoteException
	 */
	void stopInterface() throws RemoteException;
	
	/**
	 * Initializes internal gateway interface and start hearing for incoming messages
	 * from wireless sensor network 
	 */
	void startInterface() throws RemoteException;
	
	
	/**
	 * Method used to verify if the gateway interface is started or not
	 * @return a boolean true if the gateway is started, else false
	 * @throws RemoteException 
	 */
	boolean isStarted() throws RemoteException;
	
	
	/**
	 * Method that return the name of the gateway
	 * @return a string name
	 * @throws RemoteException
	 */
	String getName() throws RemoteException;
	
	
	/**
	 * Method used for getting the ip address of the gateway
	 * @return the ip Address
	 * @throws RemoteException
	 */
	String getIpAddress() throws RemoteException;
	
	
	/**
	 * This method return the next gateway in the list of group after that passed as input (gwName) using a specific sequential logic
	 * @param groupName name of the group
	 * @return the name of the next gateway in the list after that passed in the gwName parameter
	 * @throws RemoteException
	 */
	String getNextInGroup(String groupName)throws RemoteException;
	
	
	/**
	 * Method used for getting the pan coordinator mote
	 * @return an object mote
	 * @throws RemoteException
	 */
	Mote getPanCoordinator() throws RemoteException;
	
	
	/**
	 * Method used to get the list of the mote connected with this gateway
	 * @return an arraylist of the motes
	 * @throws RemoteException
	 */
	ArrayList<Mote> getMoteList()  throws RemoteException;
	
	
	/**
	 * Method used to refresh the mote list
	 * @throws RemoteException
	 */
	boolean refreshMoteList() throws RemoteException;
		
	
//SEARCH FUNCTION OBJECT METHODS

	/**
	 * This method is used for searching the class into the arrayList of the loadedObject
	 * @param param list of the parameters that identify the class to search
	 * @param version represent the version of the searched class
	 * @return this class return an array of integer that define for each object searched, if it is on the list, 
	 * and if it is saved on the server
	 * @throws RemoteException
	 */
	int[] searchClass(ArrayList<String> param, int version) throws RemoteException;
		
	/**
	 * This is a searching method which return only the index of the folder in which the class searched is saved
	 * @param list of the object trough which do the searching
	 * @param param list of the parameters that identify the class to search
	 * @param version version class
	 * @return the index of the folder
	 * @throws RemoteException
	 */
	int getSavedListObjectIndex(ArrayList<String>param, int version) throws RemoteException;
	
	
//FUNCTION LOAD AND SAVE METHODS
		
	/**
	 * This method is used to load on the array list the classes that the client need to use for its applications
	 * @param hostName represent the ip address of the client
	 * @param port represent the port number of the client application
	 * @param classToLoad contains the list of the name class to load on server
	 * @param posToLoad contain the position of each class in the arraylist, if the class to load must overwrite another object
	 * @throws IOException, RemoteException 
	 */
	//void getClassClient(/*String clientName, */String hostName, int port, ArrayList<String> classToLoad, ArrayList<String> classToSave, ArrayList<Integer> posToSave) throws IOException, RemoteException;		 

	
	/**
	 * This method is used to get classes for a specific gatway group, save and load them in this gatewateway, 
	 * and then contact the other gateway of the group for passing them these object
	 * @param hostName represent the ip address of the client
	 * @param port represent the port number of the client application
	 * @param groupName is the name of the group
	 * @param classToLoad contains the list of the name class to load on server
	 * @param posToSave contain the position of each class in the arraylist, if the class to load must overwrite another object
	 * @param loadTemp list of the load temp files
	 * @param saveTemp list of the save temp files
	 * @param posToSaveTemp indicates the index of the folder where are the files to send
	 * @throws IOException 
	 * @throws RemoteException
	 */
	//void getGroupClass(/*String clientName,*/ String hostName, int port, String group, ArrayList<String> classToLoad, ArrayList<String> classToSave, ArrayList<Integer> posToSave, ArrayList<String> loadTemp, ArrayList<String> saveTemp, ArrayList<Integer> posToSaveTemp) throws IOException, RemoteException;
	
	
	/**
	 * Method used to allows a gateway to search a specific group of function trough a specific group of gateway
	 * @param group name of the group
	 * @param listLoad list of the class to load on gateway
	 * @param listSave list of the class to save on gateway
	 * @param posToSave index of the folder where to save the new class on gateway
	 * @return a boolean that represent if all the need class are loaded and saved
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws RemoteException
	 * @throws ClassNotFoundException
	 */
	//boolean searchGroupFunction(String group, ArrayList<ObjectSearched> listLoad, ArrayList<ObjectSearched> listSave, ArrayList<Integer> posToSave) throws UnknownHostException, IOException, RemoteException, ClassNotFoundException;
	
	
	/**
	 * Method used to allows a gateway to search a specific group of function in a specific gateway
	 * @param gw WSNGateway object where search
	 * @param listLoad list of the class to load on gateway
	 * @param listSave list of the class to save on gateway
	 * @param posToSave index of the folder where to save the new class on gateway
	 * @return a boolean that represent if all the need class are loaded and saved
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws RemoteException
	 * @throws ClassNotFoundException
	 */
	//boolean gwDownloadClass(WSNGateway gw, ArrayList<ObjectSearched> listLoad, ArrayList<ObjectSearched> listSave, ArrayList<Integer> posToSave) throws UnknownHostException, IOException, RemoteException, ClassNotFoundException;
	
	
	/**
	 * Method used for sending files of the need classes for a speicific gateway
	 * @param gw is the object gateway who request the classes
	 * @param classToLoad list of the class to load
	 * @param classToSave list of the class to save 
	 * @param indexFolder indexes of the folder where the classes are saved
	 * @throws RemoteException
	 */
	//void sendFiles (WSNGateway gw,  ArrayList<String> classToLoad, ArrayList<String> classToSave, ArrayList<Integer> indexFolder) throws RemoteException;
	

//MANAGEMENT FUNCTION LIST METHODS
	
	/**
	 * This method is used by the client to invoke the methods of the objects loaded on the server
	 * @param inputSearched list of parameters input for the searching
	 * @param typeIn represent the inputs type of the method requested
	 * @param paramIn contain the inputs of the method
	 * @return the method return an object that represent the result of the method invoked
	 * @throws RemoteException
	 */
	//Object useMethod(ArrayList<String> inputSearched,String nameMethod, Class typeIn[], Object paramIn[]) throws RemoteException;
		
	
//METHODS MOTE GROUP MANAGEMENT	
	
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
	
//METHODS SENSOR GROUP MANAGEMENT	
	
	/**
	 * This method is used to create a new Sensor group
	 * @param groupName name of the group that the user wants to create
	 * @param sensorList arrayList of the Sensors
	 * @return result indicates if the group is created or not
	 * @throws RemoteException
	 */
	boolean createSensorGroup(String groupName, ArrayList<Sensor> sensorList) throws RemoteException;
	
	
	/**
	 * This method is used to add a new Sensor to an existent group
	 * @param groupName name of the group
	 * @param sensor to add
	 * @return if the operation is positive or not
	 * @throws RemoteException
	 */
	//boolean addSensorToGroup(String groupName, Sensor sensor) throws RemoteException;
	

	/**
	 * This method is used to remove a Sensor from an existent group
	 * @param groupName name of the group
	 * @param mote Sensor to remove
	 * @return if the operation is positive or not
	 * @throws RemoteException
	 */
	//int removeSensorFromGroup(String groupName, Sensor sensor) throws RemoteException;
	
	
	/**
	 * This method is used to delete a SensorGroup
	 * @param groupName name of the group that the user wants to delete
	 * @return result indicates if the group is deleted or not
	 * @throws RemoteException
	 */
	boolean removeSensorGroup(String groupName) throws RemoteException;
	
	
	/**
	 * This method is used to get a SensorGroup
	 * @param groupName name of the group that the user wants
	 * @return the object that represent the group
	 * @throws RemoteException
	 */
	SensorGroup getSensorGroup(String groupName) throws RemoteException;

}
