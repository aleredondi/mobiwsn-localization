
package remote_interfaces.functionality;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import remote_interfaces.services.Publisher;

import common.exceptions.FunctionalityException;
import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;


public interface Functionality extends Remote{
	
	public void startFunctionality(ArrayList<Object> input) throws RemoteException, MiddlewareException, FunctionalityException, MoteUnreachableException;
	
	public void stopFunctionality() throws RemoteException, MiddlewareException, MoteUnreachableException, FunctionalityException;
	
	byte getId() throws RemoteException;

	FunctionalityType getType() throws RemoteException;
	
	String getOwnerMoteId() throws RemoteException;
	
	boolean hasPublisher() throws RemoteException;
	
	//TODO controllare
	Publisher<Functionality, Object> getPublisher() throws RemoteException;
	
}
