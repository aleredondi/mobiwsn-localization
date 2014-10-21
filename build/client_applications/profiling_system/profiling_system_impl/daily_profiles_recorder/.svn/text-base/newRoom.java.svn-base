package client_applications.profiling_system.profiling_system_impl.daily_profiles_recorder;


import remote_interfaces.WSNGatewayManager;
import remote_interfaces.mote.Mote;
import remote_interfaces.clients.profiling_system.*;
import remote_interfaces.result.ValueResult;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.clients.dve.*;


import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import javax.measure.Measure;
import javax.measure.unit.SI;

import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;
import common.exceptions.ResponseTimeoutException;



/***
* @author Antimo Barbato
*
*/


/***
* @author Antimo Barbato
*
*/

public class newRoom {

	//Variables
	private String roomName;
	private String groupName;
	private int presenceValue=0;
	private Double lightValue = 0.0;
	private Double temperatureValue = 0.0;
	double temperatureTemp=0;
	double lightTemp=0;
	int presenceTemp= 0;
	int temperatureCounter=0;
	int lightCounter=0;
	private ArrayList<Integer> newDailyPresence= new ArrayList<Integer>();
	private ArrayList<Integer> oldDailyPresence= new ArrayList<Integer>();
	private ArrayList<Double> newDailyTemperature= new ArrayList<Double>();
	private ArrayList<Double> oldDailyTemperature= new ArrayList<Double>();
	private ArrayList<Double> newDailyLight= new ArrayList<Double>();
	private ArrayList<Double> oldDailyLight= new ArrayList<Double>();
	private long refreshTime=0;
	private int readingTime=30*1000;
	private int roomIdentifier;
	private int[] profilesSequence;
	public ArrayList<String> NearRooms=new ArrayList<String>();
	private WSNGatewayManager gatewayManager;
	private double lastTemperatureValue=0;
	private double lastLightValue=0;
	private DVE dve=null;
	private int ACCEPTEDTEMPERATUREDIFFERENCE=2;
	private int ACCEPTEDLIGHTDIFFERENCE=5;
	
	/**
	 * Constructor of Room Class
	 */
	
	public newRoom(String roomName, String groupName,int roomId,WSNGatewayManager gatewayManager,DVE d){
		this.roomName=roomName;
		this.groupName=groupName;
		this.roomIdentifier=roomId;
		this.gatewayManager=gatewayManager;
		this.dve=d;
	}
	
	
	
	/**This method returns the name of the room
	 * 
	 * @return roomName - Name of the Room
	 */
	public String getRoomName(){
		return roomName;
	}
	
	
	
	/**This method returns the name of the group of mote
	 * 
	 * @return groupName - Name of the group
	 */
	public String getGroupName(){
		return groupName;
	}
	
	public int getRoomIdentifier(){
		return roomIdentifier;
	}
	
	/**This method returns the light values in the room
	 * 
	 * @return lightValues - Values of light in the room
	 */
	public Double getLightValue(){
		return lightValue;
	}
	
	
	
	/**This method returns the temperature values in the room
	 * 
	 * @return temperatureValues - Values of temperature in the room
	 */
	
	public Double getTemperatureValue(){
		return temperatureValue;
	}
	
	
	
	/**This method returns the presence values in the room
	 * 
	 * @return presenceValues - Values of presence in the room
	 */
	
	public Integer getPresenceValue(){
		return presenceValue;
	}
	

	
	
	/**This method reads all the sensors of the rooms and saves the values read
	 * 
	 * @throws RemoteException
	 */
	
	public void readRoomSensors(long newTime) throws RemoteException
	{
		
		if(newTime-refreshTime>readingTime){
		Date newDate= new Date();
		ArrayList<Mote> roomMotes = gatewayManager.getMoteGroup(groupName);
		temperatureTemp=0;
		lightTemp=0;
		temperatureCounter=0;
		lightCounter=0;

		
		for (Mote m: roomMotes)
			for (Sensor s: m.getSensorList())
			{
				
				switch(s.getType())
				{
						
					case TEMPERATURE:
					{
						try {
							ValueResult sensorValue;
							try {
								sensorValue = (ValueResult) s.getValue( Measure.valueOf(new Double(10), SI.SECOND) );

		                	if (sensorValue != null ) {
								temperatureTemp += sensorValue.getValue().doubleValue(SI.CELSIUS);
								temperatureCounter++;
		                	}
							} catch (ResponseTimeoutException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
						}
						catch (MiddlewareException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MoteUnreachableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        	    	  	try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						break;
					}
					case LIGHT:
					{
						try {
							ValueResult sensorValue;
							try {
								sensorValue = (ValueResult) s.getValue( Measure.valueOf(new Double(10), SI.SECOND) );

		                	if (sensorValue != null ) {
								lightTemp += sensorValue.getValue().doubleValue(SI.LUX);
								lightCounter++;
		                	}
							} catch (ResponseTimeoutException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
						}
						catch (MiddlewareException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MoteUnreachableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}

				}
			
			}
		
		

		if (temperatureCounter>0){
			this.temperatureValue=truncate(approximate(temperatureTemp/temperatureCounter));
			
			if(Math.abs(lastTemperatureValue-this.temperatureValue)>=ACCEPTEDTEMPERATUREDIFFERENCE){
				if(dve!=null)
					try{
						
						dve.notifyRealTimeValue(ProfileType.TEMPERATURE, this.getRoomName(), this.temperatureValue);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
		}
		if (lightCounter>0){
			this.lightValue=truncate(approximate(lightTemp/lightCounter));
			if(Math.abs(lastLightValue-this.lightValue)>=ACCEPTEDLIGHTDIFFERENCE){
				if(dve!=null){
					try{
					dve.notifyRealTimeValue(ProfileType.LIGHT, this.getRoomName(), this.lightValue);
					}
			 catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}
					}
		}
		refreshTime=newDate.getTime();
		}
	
	}
	
	
	

	
	/**This method returns the presence values in the room
	 * 
	 * @return presenceValues - Values of presence in the room
	 */
	


	
	public void setPresenceValue(Object num)
	{
		Byte i;
		i =(Byte) num;
		this.presenceValue= i;
		
	}
	
	

	public void setNewDailyPresence(Integer b){
		this.newDailyPresence.add(b);
		
	}
	
	public Integer getOldDailyPresence(int a){
		return oldDailyPresence.get(a);
	}
	
	
	public Integer getOldDailyPresenceSize(){
		return oldDailyPresence.size();
	}
	
	public Integer getNewDailyPresenceSize(){
		return newDailyPresence.size();
	}
	
	
	public void setOldDailyPresence(){
		
		oldDailyPresence.clear();
			for(int i=0;i<newDailyPresence.size();i++){
			oldDailyPresence.add(newDailyPresence.get(i));
			}
		
	this.newDailyPresence.clear();
	}
	
	public int[] getNewDailyPresence(){
		int[] dailyProfile=new int[newDailyPresence.size()];
		for(int i=0;i<dailyProfile.length;i++)
			dailyProfile[i]=newDailyPresence.get(i);
		return dailyProfile;
	}
	
	public void addOldDailyPresence(Integer a){
		oldDailyPresence.add(a);
	}
	
	
	public void setNewDailyTemperature(double b){
		this.newDailyTemperature.add(b);
	}
	
	public double getOldDailyTemperature(int a){
		return oldDailyTemperature.get(a);
	}
	
	
	public Integer getOldDailyTemperatureSize(){
		return oldDailyTemperature.size();
	}
	

	
	public void setOldDailyTemperature(){
		
		oldDailyTemperature.clear();
			for(int i=0;i<newDailyTemperature.size();i++){
			oldDailyTemperature.add(newDailyTemperature.get(i));
			}
		newDailyTemperature.clear();
	}
	
	
	public void addOldDailyTemperature(double a){
		oldDailyTemperature.add(a);
	}
	
	
	public void setNewDailyLight(double b){
		this.newDailyLight.add(b);
	}
	
	public double getOldDailyLight(int a){
		return oldDailyLight.get(a);
	}
	
	
	public Integer getOldDailyLightSize(){
		return oldDailyLight.size();
	}
	
	
	public void setOldDailyLight(){
		
		oldDailyLight.clear();
			for(int i=0;i<newDailyLight.size();i++){
			oldDailyLight.add(newDailyLight.get(i));
			}
		newDailyLight.clear();
	}
	
	public void addOldDailyLight(double a){
		oldDailyLight.add(a);
	}
	

	
	
	private double approximate(double d)
	{
	    
		int decimalPlace = 1;
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_DOWN);
		d = bd.doubleValue();
		return d;
	}
	
	public double truncate( double value){
	    return Math.round( value * Math.pow( 10, 2 ) )/Math.pow( 10, 2 );
	}
	
	public void setProfilesSequence(int [] a){
		this.profilesSequence=a;
	}
	
	public int[] getProfilesSequence(){
		return profilesSequence;
	}
	
	public void uploadDVE(DVE d){
		this.dve=d;
	}
}
