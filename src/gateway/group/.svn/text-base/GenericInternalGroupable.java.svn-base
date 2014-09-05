package gateway.group;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public abstract class GenericInternalGroupable<G extends InternalGroup> implements InternalGroupable<G> {

	private ArrayList<G> myGroupList;
	
	public GenericInternalGroupable() {
		myGroupList = new ArrayList<G>();
	}

	@Override
	public boolean addedToGroup( G group ) {
		boolean ans = false;
		
		if ( myGroupList.contains(group) )
			ans = true;
		else {
			myGroupList.add(group);
			ans = true;
		}
		
		return ans;
	}

	@Override
	public ArrayList<G> getMyGroups() {
		return myGroupList;
	}

	/*public String getUniqueId() {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public boolean isPartOfGroup( G group ) {
		return myGroupList.contains(group);
	}

	@Override
	public boolean removedFromGroup( G group ) {
		boolean ans = false;
		
		if ( myGroupList.contains(group) ) {
			System.out.println("Cancellato");
			myGroupList.remove(group);
			ans = true;
		} else {
			ans = false;
		}
		
		return ans;
	}

	public UnicastRemoteObject getRemote() {
		// TODO Auto-generated method stub
		return null;
	}

}
