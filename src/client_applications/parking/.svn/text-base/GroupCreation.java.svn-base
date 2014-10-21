
package client_applications.parking;


import remote_interfaces.WSNGateway;
import remote_interfaces.WSNGatewayManager;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;


public class GroupCreation {

	public GroupCreation(String manager_ip) {
		
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
       
       try 
       {
    	   ArrayList<WSNGateway> list=remoteObject.getWSNGatewayList();
			remoteObject.createWSNGatewayGroup("sanSiro", list);
       } 
       catch (RemoteException e) {e.printStackTrace();}
       
	}

}