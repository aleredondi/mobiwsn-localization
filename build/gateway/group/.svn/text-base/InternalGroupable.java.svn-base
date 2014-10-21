package gateway.group;

import gateway.InternalWSNGateway;

import java.util.ArrayList;

import remote_interfaces.Remotizable;

public interface InternalGroupable<G extends InternalGroup> extends Remotizable {
	
	//	Oggetti nterni che si possono raggruppare
	String getUniqueId();
	
	boolean addedToGroup( G o);
	
	boolean removedFromGroup( G o);
	
	boolean isPartOfGroup( G group );
	
	ArrayList<G> getMyGroups();
	
	InternalWSNGateway getWSNGatewayParent();

}
