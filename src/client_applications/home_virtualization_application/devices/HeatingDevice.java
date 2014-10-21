
package client_applications.home_virtualization_application.devices;

import java.rmi.RemoteException;
import java.util.Hashtable;

import client_applications.home_virtualization_application.HomeVirtualizationApplicationImpl;
import remote_interfaces.clients.home_virtualization_application.device.*;

public class HeatingDevice extends DeviceImpl{
	
	double temperature=25;
	
	/**Constructor
	 * 
	 * @param t - DeviceType
	 */
	
	public HeatingDevice(DeviceType t,String path,HomeVirtualizationApplicationImpl h) throws RemoteException{
		super(t,path,h);
		imgPath=imgPath+"/devices/heating.png";
		status=DeviceStatus.OFF;
		consume=new Hashtable<DeviceStatus,Double>();
		consume.put(DeviceStatus.OFF,(double) 0);
		consume.put(DeviceStatus.STANDBY,(double) 0);
		consume.put(DeviceStatus.ON,(double) 200);
		DeviceConsumption=0;
	}
	
	
	/** This method returns the status of the device
	 * 
	 * @return status - the status of the device
	 */
	
	public DeviceStatus getStatus(){
		return status;
	}
	
	
	public double getTemperature(){
		return temperature;
	}
	
	public void setTemperature(double t){
		 temperature=t;
	}
	
	/**This method switches on the device
	 * 
	 */
	
	public void switchOn(double t) throws RemoteException{
		this.status= DeviceStatus.ON;
		this.temperature=t;
		DeviceConsumption=consume.get(status);
	}
	
	
	
	
	/**This method switches off the device
	 * 
	 */
	
	public void switchOff() throws RemoteException{
		this.status= DeviceStatus.OFF;
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
