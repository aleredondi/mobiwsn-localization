package gateway.functionality.presence;

import java.rmi.RemoteException;

import gateway.functionality.GenericRemotePublisherFunctionality;
import gateway.functionality.InternalFunctionality;

public class PresenceRemoteFunctionality extends GenericRemotePublisherFunctionality {

	public PresenceRemoteFunctionality(InternalFunctionality my_func) throws RemoteException {
		super(my_func);
	}

}
