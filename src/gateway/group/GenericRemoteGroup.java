package gateway.group;


import gateway.InternalWSNGateway;
import gateway.services.GenericRemotePublisher;
import gateway.services.InternalSubscriber;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import remote_interfaces.Remotizable;
import remote_interfaces.WSNGateway;
import remote_interfaces.group.Group;
import remote_interfaces.group.Groupable;
import remote_interfaces.services.Publisher;
import remote_interfaces.services.Subscriber;


public abstract class GenericRemoteGroup<I extends Groupable> extends UnicastRemoteObject implements Group<I>, Groupable, Publisher<Group, Group>, InternalSubscriber<InternalGroup, InternalGroup> {
	
	private ArrayList<I> elements;
	protected InternalGroup internalGroup;
	protected GenericRemotePublisher<Group, Group> myPublisher;

	protected GenericRemoteGroup( GenericInternalGroup internalGroup ) throws RemoteException {
		System.out.println("GRG Sono un geenric remote group di nome " + internalGroup.getName());
		this.internalGroup = internalGroup;
		elements = new ArrayList<I>();
		
		myPublisher = new GenericRemotePublisher<Group, Group>();
		internalGroup.addSubscriber(this);

	}

	@Override
	public void add(I o) throws RemoteException {
		System.out.println("GRG Un client mi chiede di aggiungere il mote " +o.getUniqueId() + " remoto al gruppo " + internalGroup.getName());
		internalGroup.addRemote(o);
	}
	
	@Override
	public void add(ArrayList<I> o) throws RemoteException {
		internalGroup.addRemote(o);
	}

	protected void addRemote(I o) {
		System.out.println("GRG Aggiungo remoto");
		if (!elements.contains(o)) {
			System.out.println("GRG Aggiunto remoto");
			elements.add(o);		
		}
	}

	/*@Override
	public short getId() throws RemoteException {
		return internalGroup.getId();
	}*/

	@Override
	public ArrayList<I> getList() throws RemoteException {
		return (ArrayList<I>) elements;
	}

	@Override
	public int getListNumber() throws RemoteException {
		return elements.size();
	}

	@Override
	public String getName() throws RemoteException {
		return internalGroup.getName();
	}

	@Override
	public boolean isInList(I o) throws RemoteException {		
		return elements.contains(o);
	}

	@Override
	public void remove(I o) throws RemoteException {
		internalGroup.removeRemote(o);
	}

	@Override
	public void remove(ArrayList<I> o) throws RemoteException {
		internalGroup.removeRemote(o);
	}
	
	@Override
	public void removeAll() throws RemoteException {
		internalGroup.removeAll();
	}

	protected void removeRemote(Remotizable o) {
		elements.remove(o.getRemote());
	}
	
	@Override
	public String getUniqueId() {
		return internalGroup.getUniqueId();
	}

	/*@Override
	public void remove(String id) throws RemoteException {
		internalGroup.remove(id);		
	}*/
	
	@Override
	public WSNGateway getWSNGatewayParent() {
		return internalGroup.getWSNGatewayParent().getRemoteGateway();
	}
	
	/*@Override 
	public ArrayList<I> getFinalList() {
		return elements;
	}*/
	
	@Override
	public void addSubscriber( Subscriber<Group, Group> s) throws RemoteException {
		myPublisher.addSubscriber(s);
	}

	@Override
	public void removeSubscriber( Subscriber<Group, Group> s) throws RemoteException {
		myPublisher.removeSubscriber(s);
	}
	
	@Override
	  public void update(InternalGroup pub, InternalGroup code) {
		try {
			myPublisher.notifySubscribers(this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
