package gateway.group;


import gateway.services.InternalPublisher;

import java.rmi.RemoteException;
import java.util.ArrayList;

import remote_interfaces.Remotizable;
import remote_interfaces.group.Groupable;


public interface InternalGroup<I extends InternalGroupable, R extends Groupable> extends Remotizable, InternalGroupable<InternalGroup>, InternalPublisher<InternalGroup, InternalGroup>  {

	/**
	 * Method used to get the name of the group
	 * @return the name
	 */
	String getName();
	
	
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
	short getId();

	public void add( I o  );
	public void add( ArrayList<I> o  );

	public void addRemote( R o  );
	public void addRemote( ArrayList<R> o  );
	//public void add( Groupable o  ) throws RemoteException;
	
	public void remove( I o );
	public void remove( ArrayList<I> o );

	public void removeRemote( R o );
	public void removeRemote( ArrayList<R> o );
	
	public void removeAll();
	
		/**
	 * This method is used to get the list of the motes that belong to this group
	 * @return the list of motes
	 * @throws RemoteException 
	 */
	public int getListNumber();
	ArrayList<I> getList();
	
	//ArrayList getFinalList();
	//public ArrayList<R> getRemoteList();
	

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
	ArrayList<Object> useGroupMethod(String nameClass, String methodName, Class[] parameterTypes, Object[] paramIn);
	
	
	/**
	 * This method is used for searching a specific mote in the list of this object
	 * @param id string that represent the name of the mote to search
	 * @return a boolean, true if the mote is in the list, false if it is not.
	 */
	
	boolean isInList(I o);
	
}
