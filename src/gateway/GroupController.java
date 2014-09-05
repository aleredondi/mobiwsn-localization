package gateway;

import gateway.group.InternalGroup;
import gateway.services.InternalSubscriber;

public class GroupController implements
		InternalSubscriber<InternalGroup, InternalGroup> {
	
	private InternalWSNManager manager; 
	
	GroupController(InternalWSNManager manager) {
		this.manager = manager;
	}
	
	public void addController( InternalGroup group ) {		
		group.addSubscriber(this);
	}

	public void update(InternalGroup pub, InternalGroup code) {
		System.out.println("Sono stato avvisato che è cambiato il gruppo " + pub.getName());
		if (pub.getListNumber() == 0) {
			System.out.println("Il gruppo è vuoto e quindi lo rimuovo");
			manager.removeMoteGroup(pub.getName());
		}
	}

}
