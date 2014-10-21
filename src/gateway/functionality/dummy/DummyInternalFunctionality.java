package gateway.functionality.dummy;

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

public class DummyInternalFunctionality extends GenericInternalPublisherFunctionality {
	
	public DummyInternalFunctionality(byte func_id, InternalMote m)
	{
		super(func_id, m );
		this.functionalityType = FunctionalityType.DUMMY;
		//System.out.println("Sono una dummy func");
	}

	@Override
	public void createRemote() {		
		try {
			super.setRemote( new DummyRemoteFunctionality(this) );
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected byte[] setStartFunctionalityPayload(ArrayList<Object> input) {

		System.out.println("\nDummyFunctionality does not need to a start payload !");
		
		return null;
	}

	public void messageReceived(FunctionalityMessage message) {
		System.out.println("\n>...Notifying...\n");
		//notifySubscribers(this, null);
	
		DummyMessage d_msg = new DummyMessage(message);

		notifySubscribers(this, d_msg.getDummySensorValue());		
	}


}
