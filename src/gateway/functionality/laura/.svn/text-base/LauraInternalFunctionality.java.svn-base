package gateway.functionality.laura;

import gateway.functionality.GenericInternalPublisherFunctionality;
import gateway.functionality.GenericRemotePublisherFunctionality;
import gateway.functionality.presence.PresenceMessage;
import gateway.mote.InternalMote;
import gateway.protocol.FunctionalityMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import remote_interfaces.functionality.FunctionalityType;

public class LauraInternalFunctionality extends GenericInternalPublisherFunctionality {
	
	public LauraInternalFunctionality(byte func_id, InternalMote m)
	{
		super(func_id, m );
		this.functionalityType = FunctionalityType.LAURA;
		//System.out.println("Sono una laura func");
	}

	@Override
	public void createRemote() {		
		try {
			super.setRemote( new LauraRemoteFunctionality(this) );
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected byte[] setStartFunctionalityPayload(ArrayList<Object> input) {

		System.out.println("\nLauraFunctionality does not need to a start payload !");
		
		return null;
	}

	public void messageReceived(FunctionalityMessage message) {
		System.out.println("\n>...Notifying...\n");
		//notifySubscribers(this, null);
		
		
		//TO DO: non mi piace molto, vorrei un unico notifier e poi dentro il programma mi divido
		LauraMessage l_msg = new LauraMessage(message);
		notifySubscribers(this,new Integer(l_msg.getLauraSync()));
		notifySubscribers(this, l_msg.getLauraRssiList());	
		notifySubscribers(this, l_msg.getLauraStatus());
	}


}
