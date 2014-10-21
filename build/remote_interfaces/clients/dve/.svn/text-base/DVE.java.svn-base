package remote_interfaces.clients.dve;

import java.rmi.Remote;
import java.rmi.RemoteException;

import remote_interfaces.clients.profiling_system.*;

public interface DVE extends Remote {
	
	public void newPredictedProfile( ProfileType type, String room ) throws RemoteException;

	public void  notifyRealTimeValue(ProfileType type, String room, double val ) throws RemoteException;
}
