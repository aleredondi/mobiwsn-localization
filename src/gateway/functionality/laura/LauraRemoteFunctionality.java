package gateway.functionality.laura;

import gateway.comm.MessageSender;
import gateway.functionality.GenericRemoteFunctionality;
import gateway.functionality.GenericInternalFunctionality;
import gateway.functionality.GenericRemotePublisherFunctionality;
import gateway.functionality.InternalFunctionality;
import gateway.mote.InternalMote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import remote_interfaces.functionality.FunctionalityType;

/**
 * @author Borsani Luca Pietro
 * 
 */
public class LauraRemoteFunctionality extends GenericRemotePublisherFunctionality {

	LauraRemoteFunctionality(InternalFunctionality my_func) throws RemoteException {
		super(my_func);
	}
	
}