
package client_applications.home_virtualization_application.devices;


import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Hashtable;

import client_applications.home_virtualization_application.HomeVirtualizationApplicationImpl;
import remote_interfaces.clients.home_virtualization_application.device.*;

public class LightDevice extends DeviceImpl{
	
	public static final int MAX_LUX = 500;
	double percentageOn;
	double light=500;
	
	
	
	/**Constructor
	 * 
	 * @param t - Device type
	 */
	
	public LightDevice(DeviceType t,String path,HomeVirtualizationApplicationImpl h) throws RemoteException{
		super(t,path,h);
		imgPath=imgPath+"/devices/light.png";
		percentageOn=0;
		consume=new Hashtable<DeviceStatus,Double>();
		consume.put(DeviceStatus.OFF,(double) 0);
		consume.put(DeviceStatus.STANDBY,(double) 0);
		consume.put(DeviceStatus.ON,(double) 10);
		status=DeviceStatus.OFF;
		DeviceConsumption=0;
	}
	
	
	/**This method returns the percentage of lighting of the device
	 * 
	 * @return percentageOn - Percentage of lighting
	 */
	
	public double getPercentageOn(){
		return percentageOn;
	}
	
	
	/**This method switches on the device
	 * 
	 * @param d - Difference between light desired and light read from the sensors
	 */
	
	public void switchOn(double d) throws RemoteException{

		if (d>0 & d<=100){
			percentageOn = d/100;
			status=DeviceStatus.ON;
			light=percentageOn*MAX_LUX;
		}
		else if(d>100){
			percentageOn = 1;
			status=DeviceStatus.ON;
			light=percentageOn*MAX_LUX;
		}
		else{
			percentageOn = 0;
			status=DeviceStatus.OFF;
		}
		DeviceConsumption=consume.get(status);
			
	}
	
	
	/**This method switches off the device
	 * 
	 */
	
	public void switchOff() throws RemoteException{
		percentageOn = 0;
		status=DeviceStatus.OFF;
		DeviceConsumption=consume.get(status);
	}
	
	
	
	
	/**This method returns the consume of the device
	 * 
	 * @return consume - Consume Value
	 */
	
	public double getConsume(){
		return consume.get(status)*percentageOn;
	}
	
	
	/**This method approximates a double
	 * 
	 * @param d - The double to be approximated
	 * @return - The double approximated
	 */
	
	public double getMaxLux(){
		return MAX_LUX;
	}
	
	public double getLight(){
		return light;
	}
	
	public void setLight(double s){
		light=s;
	}

	
}
