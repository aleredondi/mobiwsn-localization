package remote_interfaces.services;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Publisher<T,H> extends Remote {  

	public void addSubscriber(Subscriber<T,H> s)  
	    throws RemoteException; 

	  public void removeSubscriber(Subscriber<T,H> s)  
	    throws RemoteException; 
}
