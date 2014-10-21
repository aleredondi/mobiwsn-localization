
package remote_interfaces.clients.home_virtualization_application;

import remote_interfaces.clients.dve.DVE;
import remote_interfaces.clients.home_virtualization_application.device.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;



public interface HomeVirtualizationApplication extends Remote
{	

	public void Subscribe(DVE dve) throws RemoteException;
	
	public ArrayList<Device> getDevice(String room) throws RemoteException;
	
	public ArrayList<String> getRoomsName() throws RemoteException;
	
	public void newPresenceValue (String roomName,Object obj) throws RemoteException;

}
