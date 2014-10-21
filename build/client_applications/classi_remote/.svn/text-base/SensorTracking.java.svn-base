
package client_applications.classi_remote;

import remote_interfaces.*;
import remote_interfaces.mote.Mote;
import remote_interfaces.result.ValueResult;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.sensor.SensorType;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.measure.Measure;
import javax.measure.quantity.Temperature;
import javax.measure.unit.SI;

import common.result.*;

import common.classes.ClassMoteStructure;
import common.exceptions.MiddlewareException;


public class SensorTracking extends ClassMoteStructure
{
	public static String author="Alessandro";
	public static int version=1;
	public static String pathSave="";
	private WSNGateway newGw;
	private WSNGatewayManager remoteObject;
	private ClientEventInterface remoteClient;
	private String moteId;
	private ArrayList<ValueResultImpl> values;
	private ArrayList<Object> output;
	private Mote mote;
	private Sensor sensor;
	private boolean stop, connected;
	private Boolean online;
	private static int timeout=20000;
	private String clientUrl;
	private Double threshold;
	
	
	public SensorTracking()
	{
		this.online=connected=false;
		this.threshold=1000.0;
	}
	
	public void startFunction(String url, String oldgateway, String newgateway, String id)
	{
		this.moteId=id;
		this.stop=false;
		values=new ArrayList<ValueResultImpl>();
		try
	    {
	        remoteObject = (WSNGatewayManager)Naming.lookup(url);   
	    }
		catch (RemoteException exc){System.out.println("Error in lookup: " + exc.toString());}
	    catch (java.net.MalformedURLException exc){System.out.println("Malformed URL: " + exc.toString());}
	    catch (java.rmi.NotBoundException exc){System.out.println("NotBound: " + exc.toString());} 
	
	    try 
	    {
	    	this.newGw= remoteObject.getWSNGateway(newgateway); 
	    	this.mote=this.searchMote(this.newGw);

	    	for(Sensor s : this.mote.getSensorList())
	    	{
	    		if(s.getType()==SensorType.TEMPERATURE)
	    		{	
	    			this.sensor=s;
	    			break;
	    		}	
	    	}

	    	if (oldgateway!=null)
	    	{	
	    		Mote oldMote=this.searchMote(remoteObject.getWSNGateway(oldgateway)); 
	    		Class typeIn[]={};
	    		Object paramIn[]={};
	    		this.values.addAll((ArrayList<ValueResultImpl>)oldMote.useMethod(this.getClassIdentificationParameter(), "getvalueRead", typeIn, paramIn));
	    		this.online=(Boolean)oldMote.useMethod(this.getClassIdentificationParameter(), "isOnline", typeIn, paramIn);
	    		this.threshold=(Double)oldMote.useMethod(this.getClassIdentificationParameter(), "getThreshold", typeIn, paramIn);
	    		
	    		String cUrl=(String)oldMote.useMethod(this.getClassIdentificationParameter(), "getRmiUrl", typeIn, paramIn);
	    		if(cUrl!=null)
	    		{
	    			System.out.println("connesso con client "+cUrl);
	    			this.connect(cUrl);
	    			this.connected=true;
	    		}
	    	}
	 	    
	    } 
	    catch (RemoteException e) {System.out.println(e.toString());}
	    catch(java.lang.NullPointerException e) {System.out.println("Group not found\n"+e.toString());}
	    
	    this.start();
	    
	 }

	private Mote searchMote(WSNGateway gw) throws RemoteException
	{
		Mote moteSearched=null;
		for(Mote m : gw.getMoteList())
 	    {
 	    	if(m.getId().equals(moteId))
 	    		moteSearched=m;
 	    }
		
		return moteSearched;
	}
	
	public ArrayList<ValueResultImpl> getvalueRead()
	{
		return this.values;
	}
	
	public String getRmiUrl()
	{
		return this.clientUrl;
	}
	
	public void setOnline(boolean val)
	{
		this.online=val;
	}
	
	public Boolean isOnline()
	{
		return this.online;
	}
	
	public void stopReading()
	{
		System.out.println("STOP");
		this.stop=true;
	}
	
	public Double getThreshold()
	{
		return this.threshold;
		
	}
	
	public void setThreshold(Double val)
	{
		this.threshold=val;
	}

	public void disconnect()
	{
		this.connected=false;
		this.threshold=1000.0;
	}
	
	public void run()
	{
		try 
		{
			Thread.sleep(5000);
		} 
		catch (InterruptedException e1) {e1.printStackTrace();}
		
		try
		{
			ValueResult resultVal=null;
			
			for(int i=0;i<2;i++)
			{
				while(mote.isReachable() && !stop)
				{	
					resultVal= (ValueResult)sensor.readValue();
				
					Double readvalue=(Double)resultVal.getValue().getValue();
					readvalue=(readvalue/10-28);
					Measure<Double,Temperature> newVal=Measure.valueOf(readvalue, SI.CELSIUS);
					Calendar date =(Calendar)resultVal.getTimeRead();
					ValueResultImpl val= new ValueResultImpl(resultVal.getPrecision(), resultVal.getUnitMisure(), newVal, resultVal.getRange()[0], resultVal.getRange()[1], resultVal.getParent());
					val.setData(date);
					values.add(val);
				
					if(this.online)
					{
						if(connected)
						{	
							output= new ArrayList<Object>();	
							output.add(val);
							remoteClient.eventAllert(output, 1);
						}
					}
				
					if(readvalue>threshold)
					{	
						if(connected)
						{
							output= new ArrayList<Object>();	
							output.add(moteId);
							output.add(readvalue);
							remoteClient.eventAllert(output,2);
						}	
					}		
				
					Thread.sleep(timeout);
				}
				Thread.sleep(2000);
			}
		}	 
		catch (RemoteException e) {e.printStackTrace();} 
		catch (InterruptedException e) {e.printStackTrace();}
		catch (MiddlewareException e) {e.printStackTrace();}
		stop=false;
		
	}
	
	public void connect(String url)
	{
		clientUrl=url;
		
		try
		{ 
			remoteClient=(ClientEventInterface)Naming.lookup(clientUrl);  
		}
		catch (RemoteException exc){System.out.println("Error in lookup: " + exc.toString());}
		catch (java.net.MalformedURLException exc){System.out.println("Malformed URL: " + exc.toString());}
		catch (java.rmi.NotBoundException exc){System.out.println("NotBound: " + exc.toString());} 
	
		connected=true;
	}

	public int getVersion()
	{
		return version;
	}
	
	
	public ArrayList<String> getClassIdentificationParameter()
	{
		ArrayList<String> input=new ArrayList<String>();
		input.add("SensorTracking");
		input.add(author);
		return input;
	}
	
}