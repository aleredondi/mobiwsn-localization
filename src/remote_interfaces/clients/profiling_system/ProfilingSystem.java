package remote_interfaces.clients.profiling_system;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import remote_interfaces.*;
import remote_interfaces.clients.dve.*;
import remote_interfaces.clients.home_virtualization_application.*;

public interface ProfilingSystem extends Remote {

	public void Subscribe(DVE dve) throws RemoteException;
	
	public void SubscribeHVA(HomeVirtualizationApplication hva) throws RemoteException;
	public void notifyNewTemperatureDesiredValue(double t,String roomName) throws RemoteException;

	public void notifyNewLightDesiredValue(double t,String roomName) throws RemoteException;
	
	public Vector<String> getRoomsList() throws RemoteException;
	
	public Profile getPredictedProfile( ProfileType type, String room ) throws RemoteException,ProfilingSystemException;

	public double getRealTimevalue( ProfileType type, String room ) throws RemoteException,ProfilingSystemException;
	
}
