package gateway.group.mote;

import java.rmi.RemoteException;
import java.util.ArrayList;

import common.exceptions.MiddlewareException;

import remote_interfaces.group.Group;
import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteGroup;
import remote_interfaces.services.Publisher;
import remote_interfaces.services.Subscriber;

import gateway.group.GenericInternalGroup;
import gateway.group.GenericRemoteGroup;
import gateway.group.InternalGroup;
import gateway.services.GenericRemotePublisher;

public class RemoteMoteGroupImpl extends GenericRemoteGroup<Mote> implements MoteGroup {

	protected InternalMoteGroup internalGroup;

	RemoteMoteGroupImpl(InternalMoteGroup internalGroup) throws RemoteException {
		super((GenericInternalGroup) internalGroup);
		this.internalGroup = internalGroup;
				
		System.out.println("Sono il nuovo gruppo remoto di mote " + internalGroup.getName() );
		
	}

	
	@Override
	public void add(Mote o) throws RemoteException {
		System.out.println("GRG Un client mi chiede di aggiungere il mote " + o.getUniqueId());// + " remoto al gruppo " + internalGroup.getName());
		internalGroup.addRemote(o);
	}
	
	@Override
	public void add(ArrayList<Mote> o) throws RemoteException {
		internalGroup.addRemote(o);
	}

	
	
	@Override
	public void remove(Mote o) throws RemoteException {
		internalGroup.removeRemote(o);
	}

	@Override
	public void remove(ArrayList<Mote> o) throws RemoteException {
		internalGroup.removeRemote(o);
	}
	
	@Override
	public void removeAll() throws RemoteException {
		internalGroup.removeAll();
	}

}
