
package client_applications.home_virtualization_application.devices;

import java.rmi.RemoteException;
import java.util.Hashtable;

import client_applications.home_virtualization_application.HomeVirtualizationApplicationImpl;
import remote_interfaces.clients.home_virtualization_application.device.*;

public class WiFiDevice extends DeviceImpl{
	
	public static final int CONSUME = 50;
	
	/**Constructor
	 * 
	 * @param t - Device Type
	 */
	
	public WiFiDevice(DeviceType t,String path,HomeVirtualizationApplicationImpl h) throws RemoteException{
		super(t,path,h);
		imgPath=imgPath+"/devices/wifi.png";
		status=	DeviceStatus.OFF;
		consume=new Hashtable<DeviceStatus,Double>();
		consume.put(DeviceStatus.OFF,(double) 0);
		consume.put(DeviceStatus.STANDBY,(double) 0);
		consume.put(DeviceStatus.ON,(double) 5);
		DeviceConsumption=0;
	}
	
	/** This method returns the status of the device
	 * 
	 * @return status - the status of the device
	 */
	
	public DeviceStatus getStatus(){
		return status;
	}
	
	
	/**This method switches on the device
	 * 
	 */
	
	
	public void switchOn() throws RemoteException{
		status=DeviceStatus.ON;
		DeviceConsumption=consume.get(status);
	}
	
	
	/**This method switches off the device
	 * 
	 */
	
	
	public void switchOff() throws RemoteException{
		status=DeviceStatus.OFF;
		DeviceConsumption=consume.get(status);
	}
	
	

	/**This method returns the consume of the device
	 * 
	 * @return consume - Consume Value
	 */
	
	public double getConsume(){
		return consume.get(status);
	}


}


