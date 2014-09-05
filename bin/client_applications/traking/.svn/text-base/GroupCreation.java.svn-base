
package client_applications.traking;


import remote_interfaces.*;
import remote_interfaces.mote.Mote;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;

import common.classes.FunctionList;
import common.classes.ObjectSearched;


public class GroupCreation {

	public GroupCreation(String manager_ip, String client_ip) throws RemoteException
    {
		System.setSecurityManager(new RMISecurityManager());
		WSNGatewayManager remoteObject=null;
		//get the remote object from the registry
		try
	    {
			System.out.println("Security Manager loaded\n");
			String url = "//"+ manager_ip +"/GatewayManager";
			remoteObject = (WSNGatewayManager)Naming.lookup(url);
	          
			System.out.println("Got remote object\n");      
	    }
		catch (RemoteException exc){System.out.println("Error in lookup: " + exc.toString());}
		catch (java.net.MalformedURLException exc){System.out.println("Malformed URL: " + exc.toString());}
		catch (java.rmi.NotBoundException exc){System.out.println("NotBound: " + exc.toString());} 
	       
		WSNGateway gw1=null;
		WSNGateway gw2=null;
	       
		gw1 = remoteObject.getWSNGatewayList().get(0);
		gw2 = remoteObject.getWSNGatewayList().get(1);
		       
		ArrayList<Mote> moteList= new ArrayList<Mote>();
		moteList.add( gw1.getMoteList().get(1));
		moteList.add( gw2.getMoteList().get(1));
		       
		ArrayList<ObjectSearched> listafunzioni=new ArrayList<ObjectSearched>();
		ObjectSearched element3 = new ObjectSearched("SensorTracking", "Alessandro", 1);
		listafunzioni.add(element3);
		FunctionList functions = new FunctionList(listafunzioni);
	       
		ClassManager cm=new ClassManager(client_ip, 5050, remoteObject,"bin/client_applications/classi_remote/");
		try 
		{
			cm.manageMoteGroup("moteMobile", moteList, functions);
		} 
		catch (IOException e) {e.printStackTrace();}
	         
    }
}
