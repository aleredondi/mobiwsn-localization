
package client_applications.home_virtualization_application;


import client_applications.home_virtualization_application.*;
import client_applications.home_virtualization_application.devices.*;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import remote_interfaces.clients.home_virtualization_application.*;
import remote_interfaces.clients.home_virtualization_application.device.*;

public class Room {

	//Variables
	private String roomName;
	private String imgName;
	private int roomIdentifier;
	private ArrayList<DeviceImpl> devices = new ArrayList<DeviceImpl>();
	private double desiredTemperature;
	private double desiredLight;
	double temperatureValue=0;
	double lightValue=0;
	double consumeValue=0;
	int presenceValue= 0;
	final static double TEMP_RANGE = 1.5;	//Range di temperatura entro il quale il climatizzatore sarà spento
    private String databaseName;
    private String databaseUserName;
    private String databaseUserPassword;
    private String databaseURL;
	private String presenceProfileName;
	private String temperatureProfileName;
	private String lightProfileName;
	private String realtimePresenceValueName;
	private String realtimeTemperatureValueName;
	private String realtimeLightValueName;
	private double[] predictedPresenceProfile;
	private double[] predictedTemperatureProfile;
	private double[] predictedLightProfile;
	private double[] consumeProfile;
	private int[] newDailyPresenceProfile;
	private double[] newDailyTemperatureProfile;
	private double[] newDailyLightProfile;
	private int internalCounter=0;
	private int profileLength;
	private String groupName;
	private int samplingTime;
	private int windowLength=60; //minuti
	private int TimeThreshold=10; //minuti
	private double ProbThreshold=0.65;
	private double TemperatureThreshold=4; //gradi
	private RoomsType type;
    private boolean AIMPredictedProfileFound=false;
    private HomeManagementMode homeManagementMode=HomeManagementMode.AIM_Mode;
    private ArrayList<HeatingManagementTime> ProgrammingMode=new ArrayList<HeatingManagementTime>();
	private boolean FoundDesiredValue=false;
	private boolean justStarted=false;
	
	/**
	 * Constructor of Room Class
	 */
	
	public Room(String roomName,String groupName,int roomId, String imgName,String dbURL,String dbName,String dbUserName, String dbUserPassword,String presenceProfile,String temperatureProfile,String lightProfile,String realtimePresenceValue,String realtimeTemperatureValue,String realtimeLightValue, int samplingTime,RoomsType type) {
		this.roomName=roomName;
		this.groupName=groupName;
		this.imgName=imgName;
		this.roomIdentifier=roomId;
	    this.databaseURL=dbURL;
	    this.databaseName=dbName;
	    this.databaseUserName=dbUserName;
	    this.databaseUserPassword=dbUserPassword;
		this.presenceProfileName=presenceProfile;
		this.temperatureProfileName=temperatureProfile;
		this.lightProfileName=lightProfile;
		this.realtimePresenceValueName=realtimePresenceValue;
		this.realtimeTemperatureValueName=realtimeTemperatureValue;
		this.realtimeLightValueName=realtimeLightValue;
		this.samplingTime=samplingTime;
		this.profileLength=(int) Math.floor((24*60*60-4)/samplingTime);
		this.type=type;
	}
	
	
	
	/**This method returns the name of the room
	 * 
	 * @return roomName - Name of the Room
	 */
	public String getRoomName(){
		return roomName;
	}
	
	public String getGroupName(){
		return groupName;
	}
	
	public RoomsType getRoomType(){
		return type;
	}
	
	
	public int getCounter(){
		return internalCounter;
	}
	
	public void setCounter(){
		if(internalCounter<profileLength-1)
			internalCounter=internalCounter+1;
	}
	
	/**This method returns the name of the image associated to the room
	 * 
	 * @return imgName - Name of the Image
	 */
	public String getImgName(){
		return imgName;
	}
	
	public int getRoomIdentifier(){
		return roomIdentifier;
	}
	
	/**This method sets the image name of the room
	 * 
	 * @param s - Name of the Image
	 */
	public void setImgName(String s)
	{
		imgName=s;
	}
	
	
	public void setPresenceValue(Object num)
	{
		byte i;
		i = (Byte) num;
		this.presenceValue= i;
	}
	
	public void setPredictedPresenceProfile(double[] profile){
		predictedPresenceProfile=new double[profile.length];
		for(int i=0;i<profile.length;i++){
			predictedPresenceProfile[i]=profile[i];
			}
	}
	
	public void setPredictedTemperatureProfile(double[] profile){
		predictedTemperatureProfile=new double[profile.length];
		for(int i=0;i<profile.length;i++){
			predictedTemperatureProfile[i]=profile[i];
			}
	}
	
	public void setPredictedLightProfile(double[] profile){
		predictedLightProfile=new double[profile.length];
		for(int i=0;i<profile.length;i++){
			predictedLightProfile[i]=profile[i];
			}
	}
	
	
	public double[] getPredictedPresenceProfile(){
		return predictedPresenceProfile;
	}
	
	public double[] getPredictedTemperatureProfile(){
		return predictedTemperatureProfile;
	}
	
	public double[] getPredictedLightProfile(){
		return predictedLightProfile;
	}
	
	
	
	public void addNewDailyPresenceValue(int b){
		newDailyPresenceProfile[internalCounter]=b;
	}
	
	public void addNewDailyTemperatureValue(double b){
		newDailyTemperatureProfile[internalCounter]=b;
	}
	
	public void addNewDailyLightValue(double b){
		newDailyLightProfile[internalCounter]=b;
	}
	
	public void setNewDailyPresenceProfile(int[] localPresenceProfile){
		for(int i=0;i<localPresenceProfile.length;i++)
			newDailyPresenceProfile[i]=localPresenceProfile[i];
		internalCounter=localPresenceProfile.length;
	}
	
	public void setNewDailyTemperatureProfile(double[] localTemperatureProfile){
		for(int i=0;i<localTemperatureProfile.length;i++)
			newDailyTemperatureProfile[i]=localTemperatureProfile[i];
		internalCounter=localTemperatureProfile.length;
	}
	
	public void setNewDailyLightProfile(double[] localLightProfile){
		for(int i=0;i<localLightProfile.length;i++)
			newDailyLightProfile[i]=localLightProfile[i];
		internalCounter=localLightProfile.length;
	}
	
	
	public int[] getNewDailyPresenceProfile(){
		return newDailyPresenceProfile;
	}
	
	public double[] getNewDailyTemperatureProfile(){
		return newDailyTemperatureProfile;
	}
	
	public double[] getNewDailyLightProfile(){
		return newDailyLightProfile;
	}
	
	
	public void newDayStarted(){
		internalCounter=0;

		newDailyPresenceProfile=new int[profileLength];
		newDailyTemperatureProfile=new double[profileLength];
		newDailyLightProfile=new double[profileLength];
		
		predictedPresenceProfile=new double[profileLength];
		predictedTemperatureProfile=new double[profileLength];
		predictedLightProfile=new double[profileLength];
		
		consumeProfile=new double[profileLength];
		
		for(DeviceImpl d: devices){
			d.startNewDay(profileLength);
		}
		
		justStarted=true;
	}
	
	
	
	
	/**This method returns the light values in the room
	 * 
	 * @return lightValues - Values of light in the room
	 */
	public double getLightValues(){
		return lightValue;
	}
	
	
	
	/**This method returns the temperature values in the room
	 * 
	 * @return temperatureValues - Values of temperature in the room
	 */
	
	public double getTemperatureValues(){
		return temperatureValue;
	}
	
	
	
	/**This method returns the presence values in the room
	 * 
	 * @return presenceValues - Values of presence in the room
	 */
	
	public int getPresenceValues(){
		return presenceValue;
	}
	
	
	
	/**This method returns the consume values in the room
	 * 
	 * @return consumeValues - Values of consume in the room
	 */
	
	public double getConsumeValues(){
		return consumeValue;
	}
	
	
	
	/**This method return the list of the devices of the room
	 * 
	 * @return devices - ArrayList of the room devices
	 */
	
	public ArrayList<DeviceImpl> getDevices(){
		return devices;
	}
	
	
	
	/**This method returns the desired temperature for the room
	 * 
	 * @return desiredTemperature - Desired temperature
	 */
	
	public double getDesiredTemperature(){
		double t=0;
		for(DeviceImpl d: devices){
			try {
				if(d.getDeviceType()==DeviceType.HEATING){
					HeatingDevice newD=(HeatingDevice) d;
					t=newD.getTemperature();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return t;
	}

	

	/**This method returns the desired light for the room
	 * 
	 * @return desiredLight - Desired Light
	 */
	
	public double getDesiredLight(){
		double t=0;
		for(DeviceImpl d: devices){
			try {
				if(d.getDeviceType()==DeviceType.LIGHT){
					LightDevice newD=(LightDevice) d;
					t=newD.getLight();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return t;
	}
	
	
	
	
	
	
	/**This method reads all the sensors of the rooms and saves the values read
	 * 
	 * @throws RemoteException
	 */
	
	public void readRoomSensors() throws RemoteException
	{
	    try{
            
            Class.forName("com.mysql.jdbc.Driver").newInstance(); // load and register the JDBC driver classes for MySQL.

            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); //connection to the database


            ResultSet rs1;
            ResultSet rs2;
            ResultSet rs3;
            ResultSet rs4;
            ResultSet rs5;
            ResultSet rs6;
            Statement stmt = null;
            conn.setCatalog(databaseName); 
            stmt = conn.createStatement();
        	int idTemperatureType=0;
        	int idLightType=0;
        	int idPresenceType=0;

        	
        	rs1=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+realtimePresenceValueName+"';");
        	while(rs1.next())
        		idPresenceType=rs1.getInt("idtype");
        	
        	rs2=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+realtimeTemperatureValueName+"';");
        	while(rs2.next())
        		idTemperatureType=rs2.getInt("idtype");
        	
        	rs3=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+realtimeLightValueName+"';");
        	while(rs3.next())
        		idLightType=rs3.getInt("idtype");
        	
        	if(justStarted==true){
            	rs4=stmt.executeQuery("SELECT value FROM RealTime WHERE idtype="+idPresenceType+" AND idroom="+roomIdentifier+";");
            	while(rs4.next())
            		presenceValue=rs4.getInt("value");
            	justStarted=false;
        	}
        	
        	
        	rs5=stmt.executeQuery("SELECT value FROM RealTime WHERE idtype="+idTemperatureType+" AND idroom="+roomIdentifier+";");
        	while(rs5.next())
        		temperatureValue=rs5.getInt("value");
         
        	rs6=stmt.executeQuery("SELECT value FROM RealTime WHERE idtype="+idLightType+" AND idroom="+roomIdentifier+";");
        	while(rs6.next())
        		lightValue=rs6.getInt("value");


            stmt.close(); // close the object statement.
            conn.close(); // close the connection 
        }
        
        catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
        

	}
	
	
	
	
	
	/**This method sets the desired value for the temperature
	 * 
	 * @param d - Desired value for the temperature
	 */
	
	public void setDesiredTemperature(double d){
		desiredTemperature = d;
		for(DeviceImpl dev: devices){
			try {
				if(dev.getDeviceType()==DeviceType.HEATING){
					HeatingDevice newD=(HeatingDevice) dev;
					newD.setTemperature(d);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	/**This method sets the desired value for the light
	 * 
	 * @param d - Desired value for the light
	 */
	
	public void setDesiredLight(double d){
		desiredLight = d;
		for(DeviceImpl dev: devices){
			try {
				if(dev.getDeviceType()==DeviceType.LIGHT){
					LightDevice newD=(LightDevice) dev;
					if(d>lightValue){
						double newValue=(d-lightValue)/5;
						newD.setLight(newValue);
					}else{
						newD.setLight(0);
					}
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

	
	
	
	/**This method adds a device to the ArrayList of devices of the room
	 * 
	 * @param d - Device that is added
	 */
	
	public void addDevice(DeviceImpl d){
		devices.add(d);
	}
	
	

	
	
	/**This method sets the consume for the room
	 * 
	 * @param homePresence - This variable is true if there is someone in the house
	 */
	
	public void setDeviceStatus(boolean homePresence)
{
		// Commenta solo quando passi dal dve
		/*
		if(homeManagementMode==HomeManagementMode.AIM_Mode && AIMPredictedProfileFound==true){
			for (DeviceImpl d:devices)
			{
				try {
					switch(d.getDeviceType())
					{
						case HEATING:
						{
							HeatingDevice t;
							t = (HeatingDevice) d;
							int length=(int) (60/samplingTime)*windowLength;
							TimeThreshold=(int) (60/samplingTime)*TimeThreshold;
							int samplesNumber=0;
							double probability=0;
							if((predictedPresenceProfile.length-internalCounter) <=length){
								length=predictedPresenceProfile.length-internalCounter-2;
							}
							double[] localProfile=new double[length];
							for(int i=0;i<length;i++)
								localProfile[i]=predictedPresenceProfile[internalCounter+i];
								
							for(int i=0;i<length;i++){
								if(localProfile[i]!=0)
									probability=probability+localProfile[i];
									samplesNumber+=1;
							}
							probability=probability/samplesNumber;
								
							if(samplesNumber>TimeThreshold && probability>ProbThreshold && (Math.abs(predictedTemperatureProfile[internalCounter]-temperatureValue)>TemperatureThreshold)){							
									try {
										t.switchOn(predictedTemperatureProfile[internalCounter]);
									} catch (RemoteException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							} else
								try {
									t.switchOff();
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
									
							desiredTemperature=predictedTemperatureProfile[internalCounter];

							break;
						}
					
						case TV:
						{
							TVDevice t;
							t = (TVDevice) d;
							if (presenceValue==1){
								if(t.getStatus()==DeviceStatus.OFF){
									try {
										t.standBy();
									} catch (RemoteException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}else{
								if(t.getStatus()==DeviceStatus. STANDBY | homePresence==false){
									try {
										t.switchOff();
									} catch (RemoteException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							
							break;
						}
						
						case DVD:
						{
							DvdDevice t;
							t = (DvdDevice) d;
							if (presenceValue==1){
								if(t.getStatus()==DeviceStatus.OFF){
									try {
										t.standBy();
									} catch (RemoteException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}else{
								if(t.getStatus()==DeviceStatus. STANDBY | homePresence==false){
								try {
									t.switchOff();
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								}
							}
							
							break;
						}
						
						case LIGHT:
						{
							LightDevice t;
							t = (LightDevice) d;
							if (presenceValue==1 & (Math.abs(predictedLightProfile[internalCounter]-lightValue)>25)){
								try {
									t.switchOn(predictedLightProfile[internalCounter]);
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							} else
								try {
									t.switchOff();
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							
							desiredLight=predictedLightProfile[internalCounter];		
							break;
						}
						
						case WIFI:
						{
							WiFiDevice t;
							t = (WiFiDevice) d;
							if (homePresence==false)
								try {
									t.switchOff();
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							else
								try {
									t.switchOn();
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							
							break;
						}
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			for (DeviceImpl d:devices)
			{
				try {
					switch(d.getDeviceType())
					{
						case HEATING:
						{
							HeatingDevice t;
							t = (HeatingDevice) d;
							Calendar calendar = new GregorianCalendar();
					    	  int hour = calendar.get(Calendar.HOUR_OF_DAY);
					    	  int minutes = calendar.get(Calendar.MINUTE);
					    	  boolean intervalFound=false;
					    	  for(int i=0;i<ProgrammingMode.size();i++){
					    		  if(ProgrammingMode.get(i).isIncluded(hour, minutes)){
					    			  desiredTemperature=ProgrammingMode.get(i).getTemp();
					    			  FoundDesiredValue=true;
					    			  intervalFound=true;
					    			  if(Math.abs(ProgrammingMode.get(i).getTemp()-temperatureValue)>1){
					    			  try {
										t.switchOn(ProgrammingMode.get(i).getTemp());
									} catch (RemoteException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}


					    			  }	            			  
					    		  }
					    	  }
					    	  if(intervalFound==false){
					    		  try {
									t.switchOff();
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					    		  FoundDesiredValue=false;
					    	  }
							break;
						}
					
						case TV:
						{
							TVDevice t;
							t = (TVDevice) d;
							
							break;
						}
						
						case DVD:
						{
							DvdDevice t;
							t = (DvdDevice) d;
							
							break;
						}
						
						case LIGHT:
						{
							LightDevice t;
							t = (LightDevice) d;				
							break;
						}
						
						case WIFI:
						{
							WiFiDevice t;
							t = (WiFiDevice) d;				
							
							break;
						}
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		System.out.println(internalCounter);
		System.out.println(consumeProfile.length);
		*/
		
	}
	
	
	public void updateData(){
			double consumeTemp=0;
			
			for (DeviceImpl d:devices)
			{
				try {
					switch(d.getDeviceType())
					{
						case HEATING:
						{
							HeatingDevice t;
							t = (HeatingDevice) d;
							consumeTemp+=t.getConsume();
							break;
						}
					
						case TV:
						{
							TVDevice t;
							t = (TVDevice) d;
							consumeTemp+=t.getConsume();
							
							break;
						}
						
						case DVD:
						{
							DvdDevice t;
							t = (DvdDevice) d;
							consumeTemp+=t.getConsume();
							
							break;
						}
						
						case LIGHT:
						{
							LightDevice t;
							t = (LightDevice) d;
							consumeTemp+=t.getConsume();					
							break;
						}
						
						case WIFI:
						{
							WiFiDevice t;
							t = (WiFiDevice) d;
							consumeTemp+=t.getConsume();
							
							break;
						}
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			consumeValue=consumeTemp;
			
			
			consumeProfile[internalCounter]=consumeValue;
			
			for (DeviceImpl d:devices)
			{
				d.setDeviceWorkingMode(internalCounter);
				d.setDeviceConsumption(internalCounter);
			}
	}
	
	public double[] getConsumeProfile(){
		return consumeProfile;
	}

	/** This method aproximate a double
	 * 
	 * @param d - The value to aproximate
	 * @return - The value aproximated
	 */
	
	private double approximate(double d)
	{
	    
		int decimalPlace = 1;
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_DOWN);
		d = bd.doubleValue();
		return d;
	}
	
	public HomeManagementMode getHomeManagmenetMode(){
		return homeManagementMode;
	}
	
	public void setHomeManagementMode(HomeManagementMode a){
		homeManagementMode=a;
	}
	
	public void AIMPredictionFound(boolean predictedProfileFound){
		AIMPredictedProfileFound=predictedProfileFound;
		
	}
	
	public void addTime(int BeginningHour, int BeginningMinutes, int StoppingHour,int StoppingMinutes,int temp){
		HeatingManagementTime a=new HeatingManagementTime(BeginningHour,BeginningMinutes,StoppingHour,StoppingMinutes,temp);
		ProgrammingMode.add(a);
	}
	
	public void UpdateTime(HeatingManagementTime t,int BeginningHour, int BeginningMinutes, int StoppingHour,int StoppingMinutes,int temp){
		int index=0;
		boolean found=false;
		ArrayList<HeatingManagementTime> LocalProgrammingMode=new ArrayList<HeatingManagementTime>();
		LocalProgrammingMode=ProgrammingMode;
		
		if(!ProgrammingMode.isEmpty()){
			for(int i=0;i<ProgrammingMode.size();i++){
				if(ProgrammingMode.get(i).getBeginningHour()==t.getBeginningHour() && ProgrammingMode.get(i).getBeginningMinutes()==t.getBeginningMinutes() && ProgrammingMode.get(i).getStoppingHour()==t.getStoppingHour() && ProgrammingMode.get(i).getStoppingMinutes()==t.getStoppingMinutes() && ProgrammingMode.get(i).getTemp()==t.getTemp()){
					found=true;
					index=i;
				}
			}
		}
		if(found==true){
			ProgrammingMode.remove(index);
			HeatingManagementTime a=new HeatingManagementTime(BeginningHour,BeginningMinutes,StoppingHour,StoppingMinutes,temp);
			ProgrammingMode.add(a);
		}
	}
	
	
	public void removeTime(HeatingManagementTime t){
		int index=0;
		boolean found=false;
		ArrayList<HeatingManagementTime> LocalProgrammingMode=new ArrayList<HeatingManagementTime>();
		LocalProgrammingMode=ProgrammingMode;
		
		if(!ProgrammingMode.isEmpty()){
			for(int i=0;i<ProgrammingMode.size();i++){
				if(ProgrammingMode.get(i).getBeginningHour()==t.getBeginningHour() && ProgrammingMode.get(i).getBeginningMinutes()==t.getBeginningMinutes() && ProgrammingMode.get(i).getStoppingHour()==t.getStoppingHour() && ProgrammingMode.get(i).getStoppingMinutes()==t.getStoppingMinutes() && ProgrammingMode.get(i).getTemp()==t.getTemp()){
					found=true;
					index=i;
				}
			}
		}
		if(found==true)
			ProgrammingMode.remove(index);

	}
	
	public ArrayList<HeatingManagementTime> getProgrammingMode(){
		return ProgrammingMode;
	}
	
	public boolean FindDesiredValue(){
		return FoundDesiredValue;
	}
	
	public boolean getPredictedProfileFound(){
		return AIMPredictedProfileFound;
	}

}
