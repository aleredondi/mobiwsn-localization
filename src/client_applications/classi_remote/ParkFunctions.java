
package client_applications.classi_remote;

import remote_interfaces.*;
import remote_interfaces.mote.Mote;
import remote_interfaces.result.ValueResult;
import remote_interfaces.sensor.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;
import javax.measure.Measure;

import common.classes.ClassGatewayStructure;
import common.exceptions.MiddlewareException;


public class ParkFunctions extends ClassGatewayStructure
{
	private int numberParking;
	private int freeParking;
	private WSNGateway gw;
	private Double price;
	private double lightTreshold;
	private boolean free;
	public static String author="Alessandro";
	public static int version=1;
	public static String pathSave="";

	public ParkFunctions()
	{
		   
		this.numberParking=0;
		this.freeParking=0;
		this.lightTreshold=500.0;
		this.gw=null;
		Random value= new Random(); 
		this.free=value.nextBoolean();
		if(!this.free)
			this.price=value.nextDouble();
		else
			this.price=0.0;
	}
	
	public ArrayList<String> getClassIdentificationParameter()
	{
		ArrayList<String> input=new ArrayList<String>();
		input.add("ParkFunctions");
		input.add(author);
		return input;
	}
	
	public void setGateway(String gateway,  String url) throws RemoteException
	{
		WSNGatewayManager remoteObject=null;
		//get the remote object from the registry
		try
	    {
	      remoteObject = (WSNGatewayManager)Naming.lookup(url);  
	    }
		catch (RemoteException exc){System.out.println("Error in lookup: " + exc.toString());}
	    catch (java.net.MalformedURLException exc){System.out.println("Malformed URL: " + exc.toString());}
	    catch (java.rmi.NotBoundException exc){System.out.println("NotBound: " + exc.toString());} 
	    
		
		this.gw=remoteObject.getWSNGateway(gateway);
		      
	}
	
	
	public void countParking() throws RemoteException
	{
		this.numberParking=0;
		ArrayList<Mote> moteList=this.gw.getMoteList();
		for(Mote mote : moteList)
		{
			if(!mote.isPanCoordinator() && mote.isReachable())
			{
				ArrayList<Sensor> sensorList=mote.getSensorList();
				for(Sensor sensor : sensorList)
				{
					SensorType type= sensor.getType();
					
					if(type==SensorType.LIGHT)
					{
						this.numberParking++;
						
					}
				}
			}
		}
	}
	
	
	public void verifyFreeParking() throws RemoteException, MiddlewareException
	{
		this.freeParking=0;
		ArrayList<Mote> moteList=gw.getMoteList();
		for(Mote mote : moteList)
		{
			if(!mote.isPanCoordinator() && mote.isReachable())
			{
				ArrayList<Sensor> sensorList=mote.getSensorList();
				for(Sensor sensor : sensorList)
				{
					SensorType type= sensor.getType();
					if(type==SensorType.LIGHT)
					{
						ValueResult result=(ValueResult)sensor.readValue();
						Measure measure=result.getValue();
						Double value=(Double)measure.getValue();
						if(value>this.lightTreshold)
							this.freeParking++;
					}
				}
				
			}
		}
	}
	public int getTotParking()
	{
		return this.numberParking;
	}
	
	public int getFreeParking()
	{
		return this.freeParking;
	}
	
	public Double getPrice()
	{
		return this.price;
	}
	
	public int getVersion()
	{
		return version;
	}
}
