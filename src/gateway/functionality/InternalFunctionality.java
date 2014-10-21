package gateway.functionality;

import java.rmi.RemoteException;
import java.util.ArrayList;

import common.exceptions.FunctionalityException;
import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;

import remote_interfaces.Remotizable;
import remote_interfaces.functionality.Functionality;
import remote_interfaces.functionality.FunctionalityType;
import gateway.comm.MessageListener;
import gateway.group.InternalGroup;
import gateway.group.InternalGroupable;
import gateway.mote.InternalMote;
import gateway.protocol.FunctionalityMessage;

public interface InternalFunctionality extends InternalGroupable<InternalGroup>, Remotizable, MessageListener<FunctionalityMessage> {
	
	public void startFunctionality(ArrayList<Object> input) throws MiddlewareException, MoteUnreachableException, RemoteException, FunctionalityException;
	
	public void stopFunctionality() throws MiddlewareException, MoteUnreachableException, RemoteException, FunctionalityException;

	byte getId();

	FunctionalityType getType();
	
	public InternalMote getOwner();
	
	String getOwnerMoteId();
	
	//String getWSNGatewayParent();

	//MessageSender getSender();
	
	//public void messageReceived(FunctionalityMessage messge);
		
	public Functionality getRemoteFunctionality();
	
	public boolean isStarted();
			
}
