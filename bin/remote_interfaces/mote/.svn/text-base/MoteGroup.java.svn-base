
package remote_interfaces.mote;



import java.rmi.*;
import java.util.ArrayList;

import common.exceptions.MiddlewareException;

import remote_interfaces.group.Group;
import remote_interfaces.group.Groupable;


/**
 * @author Alessandro Laurucci
 *
 */
public interface MoteGroup extends Remote, Group<Mote>, Groupable
{

	//void sendAddMotesToGroup() throws RemoteException, MiddlewareException;;
	
	/**
	 * Method used to get the name of the group
	 * @return the name
	 */
	//String getName() throws RemoteException;
	
	
	/**
	 * Method to add function at the mote group
	 * @param functions to add
	 * @throws RemoteException
	 */
	//void addFunctions(ArrayList<ObjectSearched> functions) throws RemoteException;
	
	
	/**
	 * 
	 * @return the information about the group mote functions
	 * @throws RemoteException
	 */
	//FunctionList getFunctions() throws RemoteException;
	
	
	/**
	 * This method is used to add a mote to the list of this group
	 * @param mote is object mote to add
	 * @throws RemoteException 
	 */
	//void addMote(Mote mote) throws RemoteException;
	
	/**
	 * This method is used for getting the id of the group
	 * @return the Id of the group
	 * @throws RemoteException
	 */
	//int getId() throws RemoteException;

	
	/**
	 * This method is used to get the list of the motes that belong to this group
	 * @return the list of motes
	 * @throws RemoteException 
	 */
	//ArrayList<Mote> getMotes() throws RemoteException;
	

	/**
	 * This method is used to remove a Mote from the group
	 * @param IdMote Id of the mote to remove
	 * @throws RemoteException 
	 */
	//void removeMote(String Idmote) throws RemoteException;
	

	/**
	 * This method allow the user to invoke with a single function call a method defined in the Mote class
	 * for all the motes of the group 
	 * @param nameClass name of the class which has the method
	 * @param methodName name of the method to invoke
	 * @param parameterTypes type of the input of the method
	 * @param paramIn list of the input
	 * @return the arrayList of the result of the method for each mote of the group 
	 * @throws RemoteException 
	 */
	//ArrayList<Object> useGroupMethod(String nameClass, String methodName, Class[] parameterTypes, Object[] paramIn) throws RemoteException;
	
	
	/**
	 * This method is used for searching a specific mote in the list of this object
	 * @param id string that represent the name of the mote to search
	 * @return a boolean, true if the mote is in the list, false if it is not.
	 */
	//boolean moteSearch(String id) throws RemoteException;
	
	
	/*@Override
	public void add( Mote o) throws RemoteException;
	
	@Override
	public void add( ArrayList<Mote> o) throws RemoteException;
	
	
	@Override
	public void remove( Mote o ) throws RemoteException;
	
	@Override
	public void remove( ArrayList<Mote> o ) throws RemoteException;*/
	
	/*@Override
	ArrayList<Mote> getFinalList() throws RemoteException;*/


}