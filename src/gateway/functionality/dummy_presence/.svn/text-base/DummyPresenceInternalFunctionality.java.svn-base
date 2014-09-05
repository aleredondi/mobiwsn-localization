package gateway.functionality.dummy_presence;

import gateway.comm.MiddlewareMessageSender;
import gateway.functionality.GenericInternalFunctionality;
import gateway.functionality.GenericInternalPublisherFunctionality;
import gateway.functionality.dummy.DummyMessage;
import gateway.functionality.dummy.DummyRemoteFunctionality;
import gateway.functionality.presence.PresenceMessage;
import gateway.mote.InternalMote;
import gateway.protocol.FunctionalityMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import remote_interfaces.functionality.FunctionalityType;

/**
 * @author Borsani Luca Pietro
 * 
 */
public class DummyPresenceInternalFunctionality extends GenericInternalPublisherFunctionality{
	

	public DummyPresenceInternalFunctionality(byte func_id, InternalMote m)
	{
		super(func_id, m );
		this.functionalityType = FunctionalityType.DUMMYPRESENCE;
		//System.out.println("Sono una dummy presence func");
	}

	@Override
	protected byte[] setStartFunctionalityPayload(ArrayList<Object> input) {

		System.out.println("\nDummyPresenceFunctionality does not need to a start payload !");
		
		return null;
	}

	@Override
	protected void createRemote() {
		try {
			super.setRemote( new DummyPresenceRemoteFunctionality(this) );
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void messageReceived(FunctionalityMessage message) {
		System.out.println("\n>...Notifying...\n");
		//notifySubscribers(this, null);	
	
		DummyPresenceMessage dp_msg = new DummyPresenceMessage(message);

		notifySubscribers(this, dp_msg.getPersonNumber());
	}

}