
package client_applications.classi_remote;

import remote_interfaces.*;
import remote_interfaces.mote.Mote;
import remote_interfaces.result.*;
import remote_interfaces.sensor.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.measure.Measure;

import common.classes.ClassGatewayStructure;
import common.exceptions.MiddlewareException;


public class NoiseControl extends ClassGatewayStructure
{
	public static String author="Alessandro";
	public static int version=1;
	public static String pathSave="";
	private String url;
	private WSNGateway gw;
	private WSNGatewayManager remoteObject;
	private double media;
	
	public NoiseControl()
	{
		this.media=0;
	}
	
	
	public ArrayList<String> getClassIdentificationParameter()
	{
		ArrayList<String> input=new ArrayList<String>();
		input.add("NoiseControl");
		input.add(author);
		return input;
	}
	
	public void setGateway(String gateway,  String manager) throws RemoteException
	{
		this.url=manager;
		try 
		{
			this.remoteObject = (WSNGatewayManager)Naming.lookup(this.url);
			this.gw=remoteObject.getWSNGateway(gateway);
			
		} 
		catch (MalformedURLException e) {e.printStackTrace();} 
		catch (NotBoundException e) {e.printStackTrace();}  
	   
		      
	}
	
	public ArrayList<Double> getValue(String groupName, boolean first)
	{
		ArrayList<Double> result= new ArrayList<Double>();
		ArrayList<Double> localResult=new ArrayList<Double>();
		double sum;
		double max=0;
		int num=0;
		Double localMedia;
		String name;
		WSNGateway next=null;
		Class typeIn[]={String.class, boolean.class};
        Object paramIn[]={groupName, false};
		//Measure<Double, Dimensionless> sensorsValue = Measure.valueOf(0.0,NonSI.DECIBEL);
       
        try
		{
			name=this.gw.getNextInGroup(groupName);
			if(name!=null)
			{
				next=this.remoteObject.getWSNGateway(name);
				Class typeIngw[]={String.class, String.class};
			    Object paramIngw[]={next.getName(), this.url};
				next.useMethod(this.getClassIdentificationParameter(), "setGateway", typeIngw, paramIngw);
				result=(ArrayList<Double>)next.useMethod(this.getClassIdentificationParameter(), "getValue", typeIn, paramIn);
			}
			
			for(Mote mote : gw.getMoteList())
			{
				
				for(Sensor sensor : mote.getSensorList())
				{
					if(!mote.isPanCoordinator() && mote.isReachable())
					{
						if(sensor.getType()==SensorType.NOISE)
						{	
							num++;
							for(int j=0;j<3;j++)
							{	
								ValueResult resultVal=(ValueResult)sensor.readValue();
								Measure measure=resultVal.getValue();
								Double value=(Double)measure.getValue();
								if(value>max)
									max=value;
								try 
								{
									Thread.sleep(1000);
								} 
								catch (InterruptedException e) {e.printStackTrace();}
							}
							
							localResult.add(max);
						}
					}
				}
			}
			
		}
		catch (RemoteException exc){System.out.println("Error in lookup: " + exc.toString());}
		catch (MiddlewareException e) {e.printStackTrace();}
		
		sum=0;
		for(Double val : localResult)
		{
			sum=sum+val;
		}
		
		if(num>0)
			localMedia = sum/num;
		else
			localMedia=sum;
		
		result.add(localMedia);
		
		if(first)
		{
			sum=0;
			for(Double val : result)
			{
				sum=sum+val;
			}
			if(result.size()>0)
				this.media=sum/result.size();
			else
				this.media=sum;
		}
		return result;
	}
	
	public Double getMedia()
	{
		return this.media;
		
	}
	
	public int getVersion()
	{
		return version;
	}
	
}
