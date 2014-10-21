package gateway.group;


import gateway.InternalWSNGateway;
import gateway.InternalWSNManager;
import gateway.mote.InternalMote;
import gateway.services.GenericInternalPublisher;
import gateway.services.InternalPublisher;
import gateway.services.InternalSubscriber;

import java.util.ArrayList;
import java.util.Iterator;

import remote_interfaces.group.Groupable;


public abstract class GenericInternalGroup<I extends InternalGroupable, R extends Groupable> extends GenericInternalGroupable<InternalGroup> implements InternalGroup<I, R>, InternalSubscriber<I, Object>, InternalPublisher<InternalGroup, InternalGroup> {
	
	private ArrayList<I> elements;
	private short id;
	protected String name;
	protected GenericRemoteGroup remoteGroup;
	protected InternalWSNManager wsn;
	protected GenericInternalPublisher<InternalGroup, InternalGroup> myPublisher;

	protected GenericInternalGroup( String name, I first, InternalWSNManager wsn) {
		super();
		
		System.out.println("GIG Sono un nuovo gruppo con nome " + name);
		elements = new ArrayList<I>(1);
		this.name = name;
		this.wsn = wsn;

		GroupIdManager m = GroupIdManager.getInstance();
		System.out.println("Chiedo un id a " + m.toString());
		id = m.getId(this);
		System.out.println("GIG " + id);
		if( id != 0) {
			setGroupId(id);		
			System.out.println("GIG Sono il nuovo gruppo di mote " + name +" con id " + id);
		} else {
			System.out.println("GIG Errore id 0 non assegnabile");
		}
		
		myPublisher = new GenericInternalPublisher<InternalGroup, InternalGroup>();
		createRemote();
		add(first);
	}
	
	protected GenericInternalGroup( String name, ArrayList<I> list, InternalWSNManager wsn) {
		super();
		
		System.out.println("GIG Sono un nuovo gruppo con nome " + name);
		elements = new ArrayList<I>(1);
		this.name = name;
		this.wsn = wsn;

		GroupIdManager m = GroupIdManager.getInstance();
		System.out.println("Chiedo un id a " + m.toString());
		id = m.getId(this);
		System.out.println("GIG " + id);
		if( id != 0) {
			setGroupId(id);		
			System.out.println("GIG Sono il nuovo gruppo di mote " + name +" con id " + id);
		} else {
			System.out.println("GIG Errore id 0 non assegnabile");
		}
		
		myPublisher = new GenericInternalPublisher<InternalGroup, InternalGroup>();
		createRemote();
		
		Iterator<I> it = list.iterator();
			while(it.hasNext() )
				add(it.next());
	}

	protected abstract void createRemote();
	
	protected void setRemote( GenericRemoteGroup group ) {
		this.remoteGroup = group;
	}
	
	protected void setGroupId( short id ) {
		this.id = id;		
	}

	//Manca il caso di oggetti interni raggruppabili ma non remotizzabili 
	/*GenericInternalGroup(short id, String name, Groupable first) {
		elements = new ArrayList<InternalGroupable>(1);
		this.id = id;
		this.name = name;
		add( first );
	}*/
	
	//Funzione interna che aggiunge l'elemento al gruppo
	protected void addToList(I o) {
		System.out.println("GIG Aggiungo l'oggetto " + o.getUniqueId() +"  al gruppo " + name);
		if (!elements.contains(o)) {
			elements.add(o);
			o.addedToGroup(this);
			System.out.println("GIG Aggiunto localmente");
			remoteGroup.addRemote(  (Groupable) o.getRemote() ); 
		}		
	}

	@Override
	public void add(I o) {
		addToList(o);
		
		myPublisher.notifySubscribers(this, this);
	}

	@Override
	public void add(ArrayList<I> o) {
		Iterator<I> it = o.iterator();
		
		while (it.hasNext())
			addToList(it.next());
		
		myPublisher.notifySubscribers(this);
	}

	//protected abstract void addRemoteToList( Groupable o );
	

	@Override
	public short getId() {
		return id;
	}

	@Override
	public ArrayList<I> getList() {
		return (ArrayList<I>) elements;
	}

	@Override
	public int getListNumber() {
		return elements.size();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isInList(I o) {
		return elements.contains(o);
	}

	protected void removeFromList( I o ) {
		System.out.println("GIG Lo tolgo");
		elements.remove(o);
		o.removedFromGroup(this);
		remoteGroup.removeRemote( o);		
	}
	
	@Override
	public void remove(I o) {
		removeFromList(o);
		myPublisher.notifySubscribers(this);
	}
	
	@Override
	public void remove(ArrayList<I> o) {
		Iterator<I> it = o.iterator();
		boolean changed = false;
		
		while(it.hasNext()) {
			removeFromList(it.next());
			changed = true;
		}
		
		if ( changed )
			myPublisher.notifySubscribers(this);
	}
	

	/*@Override
	public void remove(String id) {
		Iterator<InternalGroupable> it = elements.iterator();
		InternalGroupable temp;
		
		while(it.hasNext()) {
			temp = it.next();
			if ( temp.getUniqueId().equals(id) )
				remove(temp);
		}
	}*/
	
	@Override
	public void removeAll() {
		Iterator<I> it = elements.iterator();
		boolean changed = false;
		
		while(it.hasNext()) {
			it.next().removedFromGroup(this);
			changed = true;
		}
		
		elements.clear();
		
		if  ( changed ) 
			myPublisher.notifySubscribers(this);
	}
		
	
	
	@Override
	public String getUniqueId() {
		return name;
	}

	public InternalWSNGateway getWSNGatewayParent() {
		return wsn.getGateway();
	}
	
/*	@Override
	public ArrayList<I> getFinalList() {
		return elements;
	}*/
	
	
	@Override
	public void addSubscriber( InternalSubscriber<InternalGroup, InternalGroup> s ) {
		myPublisher.addSubscriber(s);
	}

	@Override
	public void removeSubscriber( InternalSubscriber<InternalGroup, InternalGroup> s ) {
		myPublisher.removeSubscriber(s);
	}
	
	@Override
	public void update(I pub, Object code) {
		myPublisher.notifySubscribers(this);
	}

}

		
