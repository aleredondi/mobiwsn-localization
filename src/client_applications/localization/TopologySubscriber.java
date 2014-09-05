package client_applications.localization;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


import remote_interfaces.WSNGateway;
import remote_interfaces.mote.Mote;
import remote_interfaces.services.Subscriber;


public class TopologySubscriber extends UnicastRemoteObject implements Subscriber< WSNGateway,ArrayList<Mote> >
{
	
	LauraManager app;

	protected TopologySubscriber( LauraManager app) throws RemoteException {
		super();
		this.app = app;
	}

	@Override
	public void update(WSNGateway pub, ArrayList<Mote> code) throws RemoteException {

		app.updateTopology(pub, code);
	}


}
