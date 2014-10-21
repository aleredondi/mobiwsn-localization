package gateway.functionality.dummy_presence;

import java.rmi.RemoteException;

import gateway.functionality.GenericRemotePublisherFunctionality;
import gateway.functionality.InternalFunctionality;

public class DummyPresenceRemoteFunctionality extends GenericRemotePublisherFunctionality {

	public DummyPresenceRemoteFunctionality(InternalFunctionality my_func) throws RemoteException {
		super(my_func);
	}

}
