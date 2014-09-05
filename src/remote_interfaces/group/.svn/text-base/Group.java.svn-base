package remote_interfaces.group;

import java.rmi.RemoteException;
import java.util.ArrayList;

import remote_interfaces.services.Publisher;

public interface Group<T extends Groupable> extends Groupable, Publisher<Group, Group> {

	/**
	 * Method used to get the name of the group
	 * @return the name
	 */
	String getName() throws RemoteException;
	
	
	/**
	 * Method to add function at the mote group
	 * @param functions to add
	 * @throws RemoteException
	 */
	//void addFunctions(ArrayList<ObjectSearched> functions);
		
	
	/**
	 * This method is used for getting the id of the group
	 * @return the Id of the group
	 * @throws RemoteException
	 */
	//short getId() throws RemoteException;
	

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
	 * Method to add function at the mote group
	 * @param functions to add
	 * @throws RemoteException
	 */
	//void addFunctions(ArrayList<ObjectSearched> functions);
		
	
	
	public void add( T o) throws RemoteException;
	
	public void add( ArrayList<T> o) throws RemoteException;
	
	public void remove( T o ) throws RemoteException;
	public void remove( ArrayList<T> o ) throws RemoteException;
	public void removeAll() throws RemoteException;
	
	//public void remove( String id ) throws RemoteException;
	
		/**
	 * This method is used to get the list of the motes that belong to this group
	 * @return the list of motes
	 * @throws RemoteException 
	 */
	public int getListNumber() throws RemoteException;
	/**
	 * This method is used to get the list of the motes that belong to this group
	 * @return the list of motes
	 * @throws RemoteException 
	 */
	ArrayList<T> getList() throws RemoteException;
	
	//ArrayList getFinalList() throws RemoteException;
		
	/**
	 * This method is used for searching a specific mote in the list of this object
	 * @param id string that represent the name of the mote to search
	 * @return a boolean, true if the mote is in the list, false if it is not.
	 */
	
	boolean isInList( T o) throws RemoteException;
}
