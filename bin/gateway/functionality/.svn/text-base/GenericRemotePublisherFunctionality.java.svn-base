package gateway.functionality;

import gateway.services.GenericRemotePublisher;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import remote_interfaces.functionality.Functionality;
import remote_interfaces.services.Publisher;
import remote_interfaces.services.Subscriber;

import common.exceptions.ExceptionHandler;
import common.exceptions.FunctionalityException;
import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;

public abstract class GenericRemotePublisherFunctionality extends GenericRemoteFunctionality implements Publisher<Functionality,Object> {

		
	private final GenericRemotePublisher<Functionality,Object> myPublisher;
	
	public GenericRemotePublisherFunctionality(InternalFunctionality func) throws RemoteException
	{
		super(func);
		//System.out.println("Creo il mio publisher remoto");
		myPublisher = new GenericRemotePublisher<Functionality,Object>();		
	}
	
	public void startFunctionality(ArrayList<Object> input) throws RemoteException, MiddlewareException, MoteUnreachableException, FunctionalityException
	{
		my_func.startFunctionality(input);
	}
	
	public void stopFunctionality() throws RemoteException, MiddlewareException, MoteUnreachableException, FunctionalityException
	{
		my_func.stopFunctionality();
	}
	
	public boolean hasPublisher() {
		return true;
	}
	
	public Publisher getPublisher() {
		return myPublisher;
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
			
			if (o instanceof GenericRemotePublisherFunctionality)
			{
				GenericRemotePublisherFunctionality func = (GenericRemotePublisherFunctionality) o;
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
	
	
	/* Metodi del publisher */
	  public void addSubscriber(Subscriber<Functionality, Object> s) throws RemoteException {
		  myPublisher.addSubscriber(s);
	  }

	  public void removeSubscriber(Subscriber<Functionality,Object> s) throws RemoteException {
		  myPublisher.removeSubscriber(s);
	  }
	
	protected void notifySubscribers( Object code) throws RemoteException { 
		try {
			myPublisher.notifySubscribers(this, code);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
