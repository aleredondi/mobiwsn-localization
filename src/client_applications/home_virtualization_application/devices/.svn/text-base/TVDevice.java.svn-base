
package client_applications.home_virtualization_application.devices;

import java.rmi.RemoteException;
import java.util.Hashtable;

import client_applications.home_virtualization_application.HomeVirtualizationApplicationImpl;
import remote_interfaces.clients.home_virtualization_application.device.*;

public class TVDevice extends DeviceImpl{
	

	DeviceStatus previousStatus;

	/**Constructor
	 * 
	 * @param t - Device Type
	 */
	
	public TVDevice(DeviceType t,String path,HomeVirtualizationApplicationImpl h) throws RemoteException{
		super(t,path,h);
		imgPath=imgPath+"/devices/tv.png";
		status=DeviceStatus.OFF;
		previousStatus=DeviceStatus.OFF;
		consume=new Hashtable<DeviceStatus,Double>();
		consume.put(DeviceStatus.OFF,(double) 0);
		consume.put(DeviceStatus.STANDBY,(double) 2);
		consume.put(DeviceStatus.ON,(double) 20);
		DeviceConsumption=0;
	}
	
	
	
	/** This method returns the status of the device
	 * 
	 * @return status - the status of the device
	 */
	
	public DeviceStatus getStatus(){
		return status;
	}
	
	
	
	
	/** This method returns the previous status of the device
	 * 
	 * @return previousStatus - The previous status of the device
	 */
	
	
	public DeviceStatus getPreviousStatus(){
		return previousStatus;
	}
	
	
	
	/**This method switches on the device
	 * 
	 */
	
	public void switchOn() throws RemoteException{
		this.previousStatus= status;
		this.status= DeviceStatus.ON;
		DeviceConsumption=consume.get(status);
}
	
	
	
	
	/**This method switches off the device
	 * 
	 */

	public void switchOff() throws RemoteException{
		this.previousStatus= status;
		this.status= DeviceStatus.OFF;
		DeviceConsumption=consume.get(status);
	}
	
	
	/**This method puts the device in stand-by
	 * 
	 */
	

	public void standBy() throws RemoteException{
		this.previousStatus= status;
		this.status= DeviceStatus.STANDBY;
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
