
package remote_interfaces.clients.home_virtualization_application.device;



import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;


public interface Device extends Remote
{	
	
	public void switchOn() throws RemoteException;
	
	public void switchOn(double a) throws RemoteException;
		
	public void switchOff() throws RemoteException;
	
	public void standBy() throws RemoteException;
	
	public double getConsumption() throws RemoteException;

	public DeviceStatus getStatus() throws RemoteException;
	
	public DeviceType getDeviceType() throws RemoteException;
	
	public void setUID(String a) throws RemoteException;
	
	public String getUID() throws RemoteException;
	
	public void setFriendlyName(String a) throws RemoteException;
	
	public String getFriendlyName() throws RemoteException;
	
	public void setDescription(String a) throws RemoteException;
	
	public String getDescription() throws RemoteException;
}
