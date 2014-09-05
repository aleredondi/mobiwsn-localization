
package client_applications.home_virtualization_application;


import java.text.DecimalFormat;
import java.rmi.RemoteException;
import java.util.ArrayList;

import client_applications.home_virtualization_application.Room;


public class House {

	//Variables

	private ArrayList<Room> rooms=new ArrayList<Room>();
	private double temperatureValue=0;
	private double lightValue=0;
	private double consumeValue=0;
	private int presenceValue= 0;
	private int roomsNumber=0;

    
	
	
	/**
	 * Constructor of Room Class
	 */
	
	public House() {

	}
	
	public void addRoom(Room r){
		rooms.add(r);
		roomsNumber+=1;
	}
	
	public Room getRoom(int i){
		return rooms.get(i);
	}
	
	public int getRoomsNumber(){
		return roomsNumber;
	}
	
	public boolean refreshValues(){
		boolean found=false;

		for(Room r:rooms){
        	   try {
        		   r.readRoomSensors();
        		   r.addNewDailyPresenceValue(r.getPresenceValues());
        		   r.addNewDailyTemperatureValue(r.getTemperatureValues());
        		   r.addNewDailyLightValue(r.getLightValues());
        		   
        		   if(r.getPresenceValues()==1)
        			   found=found | true;
			   } 
        	   catch (RemoteException e) {
				   e.printStackTrace();
			   }
		}
		for(Room r:rooms){
				   r.setDeviceStatus(found);
				   r.updateData();
				   r.setCounter();
		}
		temperatureValue=0;
		lightValue=0;
		consumeValue=0;
		presenceValue= 0;
		for(Room r:rooms){
			temperatureValue=temperatureValue+r.getTemperatureValues();
			lightValue=lightValue+r.getLightValues();
			consumeValue=consumeValue+r.getConsumeValues();
			presenceValue=presenceValue + r.getPresenceValues();
		}
		temperatureValue=temperatureValue/roomsNumber;
		lightValue=lightValue/roomsNumber;
		temperatureValue=truncate(temperatureValue);
		lightValue=truncate(lightValue);
		/*if(presenceValue>1)
			presenceValue=1;*/
		
		return found;
	}
	
	
	
	
	public boolean updateValues(){
		boolean found=false;

		for(Room r:rooms){
			if(r.getPresenceValues()==1)
				found=found | true;   
		}
		for(Room r:rooms){
				   r.setDeviceStatus(found);
		}
		temperatureValue=0;
		lightValue=0;
		consumeValue=0;
		presenceValue= 0;
		for(Room r:rooms){
			temperatureValue=temperatureValue+r.getTemperatureValues();
			lightValue=lightValue+r.getLightValues();
			consumeValue=consumeValue+r.getConsumeValues();
			presenceValue=presenceValue + r.getPresenceValues();
		}
		temperatureValue=temperatureValue/roomsNumber;
		lightValue=lightValue/roomsNumber;
		temperatureValue=truncate(temperatureValue);
		lightValue=truncate(lightValue);
		/*if(presenceValue>1)
			presenceValue=1;*/
		
		return found;
	}
	
	
	
	
	public ArrayList<Room> getRooms(){
		return rooms;
	}
	
	public double getTemperature(){
		return temperatureValue;
	}
	
	public double getLight(){
		return lightValue;
	}
	
	public int getPresence(){
		return presenceValue;
	}
	
	public double getConsume(){
		return consumeValue;
	}
	public void setManagementMode(boolean predictedProfileFound){
		for(Room r:rooms)
			r.AIMPredictionFound(predictedProfileFound);
	}
	
    private double truncate (double x)
    {
        DecimalFormat df = new  DecimalFormat ("0.##");
        String d = df.format (x);
        d = d.replaceAll (",", ".");
        Double dbl = new Double (d);
        return dbl.doubleValue ();
    }
}
