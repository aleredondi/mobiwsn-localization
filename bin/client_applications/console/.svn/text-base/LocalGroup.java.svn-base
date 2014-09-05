package client_applications.console;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import remote_interfaces.mote.Mote;

public class LocalGroup {
	
	String name;
	ArrayList<Mote> elements;
	boolean created;
	
	
	LocalGroup( String name, boolean created  ) {
		this.name = name;
		elements = new ArrayList<Mote>();
		this.created = created;
	}
	
	LocalGroup( String name  ) {
		this.name = name;
		elements = new ArrayList<Mote>();
		this.created = false;
	}
	
	public void setcreated() {
		this.created = true;
	}

	public String getName() {
		return name;
	}
	
	public boolean add( Mote mote ) {
		if ( !elements.contains(mote) ) {
			try {
				System.out.println("Aggiungo l'elemento " + mote.getId() + " al gruppo " + name );
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			elements.add(mote);
			return true;
		} else
			return false;
	}

	public boolean add( ArrayList<Mote> motes ) {
		boolean ans = true;
		Iterator<Mote> it = motes.iterator();

		while(it.hasNext()) {
			ans &= add( it.next() );
		}
		
		return ans;
	}

	public boolean remove( Mote mote ) {
		if ( elements.contains(mote) ) {
			elements.remove(mote);
			return true;
		} else
			return false;
	}
	
	public void removeAll() {
		elements.clear();
	}
	
	public ArrayList<Mote> getElements() {
		return elements;
	}
	
	public boolean isCreatedOnGateway() {
		return created;
	}
	
	@Override
	public boolean equals( Object o) {
		if ( o instanceof LocalGroup) {
			return ( (LocalGroup) o ).name.equals(this.name);
		} else
			return false;
	}
}
