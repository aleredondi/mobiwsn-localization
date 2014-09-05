
package client_applications.home_virtualization_application.devices;

import java.util.*;

import remote_interfaces.clients.dve.DVE;
import remote_interfaces.clients.home_virtualization_application.HomeVirtualizationApplication;
import remote_interfaces.clients.home_virtualization_application.device.Device;
import remote_interfaces.clients.home_virtualization_application.device.DeviceStatus;
import remote_interfaces.clients.home_virtualization_application.device.DeviceType;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import client_applications.home_virtualization_application.HomeVirtualizationApplicationImpl;
import client_applications.home_virtualization_application.RoomSelectionListener;


public class DeviceImpl extends UnicastRemoteObject implements Device {
	
	DeviceType type;
	String imgPath;
	DeviceStatus status;
	Hashtable<DeviceStatus,Double> consume; 
	String statusString;
	double[] DailyConsumption;
	DeviceStatus[] DailyWorkingMode;
	double DeviceConsumption;
	String UID="";
	String friendlyName=null;
	String description=null;
	HomeVirtualizationApplicationImpl hva=null;
	
	/**Constructor
	 * 
	 * @param t - Device Type
	 */
	
	public DeviceImpl (DeviceType t, String imagePath,HomeVirtualizationApplicationImpl h) throws RemoteException{
		this.type = t;	
		this.imgPath=imagePath;
		this.hva=h;
	}
	
	
	
	
	/**This method returns the device type
	 * 
	 * @return type - Device Type
	 */
	
	public DeviceType getDeviceType() throws RemoteException{
		return type;
	}
	

	
	/** This method returns the imgName of the device
	 * 
	 * @return imgName - Name of the image
	 */
	
	public String getImgName(){
		return imgPath;
	}
	
	
	/**This method returns the status string of the device
	 * 
	 * @return status - Status string of the device
	 */
	
	public DeviceStatus getDeviceStatus() throws RemoteException{
		return status;
	}
	

	public String getDeviceStatusString(){
		if(status==DeviceStatus.OFF)
			statusString="Off";
		if(status==DeviceStatus.ON)
			statusString="On";
		if(status==DeviceStatus.STANDBY)
			statusString="Stand By";
		
		return statusString;
	}
	
	
	public double getConsume(DeviceStatus s){
		return consume.get(s);
	}
	
	
	public void setConsume(DeviceStatus s,double d){
		consume.remove(s);
		consume.put(s, d);
	}
	
	public void startNewDay(int length){
		DailyConsumption=new double[length];
		DailyWorkingMode=new DeviceStatus[length];
	}
	
	public void setDeviceConsumption(int position){
		DailyConsumption[position]=DeviceConsumption;
	}	
	
	public void setDeviceWorkingMode(int position){
		DailyWorkingMode[position]=status;
	}
	
	public double[] getDailyDeviceConsumption(){
		return DailyConsumption;
	}
	
	
	public DeviceStatus[] getDailyDeviceWorkingMode(){
		return DailyWorkingMode;
	}
	
	public void switchOn(double a) throws RemoteException{
		hva.uploadPanel();
	}
	
	
	public void switchOn() throws RemoteException{
		hva.uploadPanel();
	}
	
	public void switchOff() throws RemoteException{
		hva.uploadPanel();
	}
	
	public void standBy() throws RemoteException{
		hva.uploadPanel();
	}
	
	public double getConsumption() throws RemoteException{
		return DeviceConsumption;
	}
	
	public DeviceStatus getStatus() throws RemoteException{
			return status;
	}

	public void setUID(String a) throws RemoteException{
		UID=a;
	}
	
	public String getUID() throws RemoteException{
		return UID;
	}
	
	public void setFriendlyName(String a) throws RemoteException{
		friendlyName=a;
	}
	
	public String getFriendlyName() throws RemoteException{
		return friendlyName;
	}
	
	public void setDescription(String a) throws RemoteException{
		description=a;
	}
	
	public String getDescription() throws RemoteException{
		return description;
	}
}
