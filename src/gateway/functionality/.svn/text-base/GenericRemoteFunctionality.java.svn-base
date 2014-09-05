
package gateway.functionality;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import common.exceptions.ExceptionHandler;
import common.exceptions.FunctionalityException;
import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;
import common.exceptions.ResponseTimeoutException;

import gateway.comm.MessageSender;
import gateway.mote.InternalMote;
import gateway.services.GenericRemotePublisher;

import remote_interfaces.functionality.*;
import remote_interfaces.services.Publisher;


/**
 * @author Borsani Luca Pietro
 * 
 */
//Le funzionalit� sarebbe meglio fossero thrad per evitare che blocchino tutto
public abstract class GenericRemoteFunctionality extends UnicastRemoteObject implements Functionality {
	
	protected InternalFunctionality my_func;
	
	
	public GenericRemoteFunctionality(InternalFunctionality internalFunctionality) throws RemoteException
	{
		this.my_func = internalFunctionality;
	}
	
	public void startFunctionality(ArrayList<Object> input) throws RemoteException, MiddlewareException, MoteUnreachableException, FunctionalityException
	{
		my_func.startFunctionality(input);
	}
		
	public void stopFunctionality() throws RemoteException, MiddlewareException, MoteUnreachableException, FunctionalityException
	{
		my_func.stopFunctionality();
	}
	
	public byte getId() throws RemoteException
	{
		return my_func.getId();
	}
	
	public FunctionalityType getType() throws RemoteException
	{
		return my_func.getType();
	}
	
	public String getOwnerMoteId() throws RemoteException
	{
		return my_func.getOwnerMoteId();
	}
	
	public InternalMote getOwner() throws RemoteException
	{
		return my_func.getOwner();
	}
	
	public boolean hasPublisher() {
		return false;
	}
	
	public Publisher getPublisher() {
		return null;
	}
				
	public boolean equals(Object o)
	{
		try
		{
			try
			{
				if (o == null)
					System.out.println("o is null while comparing with : " + this.getId() +"; owner :" + this.getOwnerMoteId());
			}
			catch(Exception ex){System.out.println("Exception getOwner");}
			
			if (o instanceof GenericRemoteFunctionality)
			{
				GenericRemoteFunctionality func = (GenericRemoteFunctionality) o;
				if (func.getId() == 0) System.out.println("Exception basicFunctionality.getId()");
				if ((this.getId() == func.getId()) && (this.getOwnerMoteId().equals(func.getOwnerMoteId())))
					return true;
				else 
					return false;
			}
			else
				return false;
		}
		catch (RemoteException rex)
		{
			ExceptionHandler.getInstance().handleException(rex);
			return false;
		}
	}
	

}
