
package client_applications.traking;


import remote_interfaces.*;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;


public class provarefresh 
{

	public static void main(String[]  args) throws IOException
	{
	   //Set the security manager for the client
	   System.setSecurityManager(new RMISecurityManager());
	   WSNGatewayManager remoteObject=null;
	   //get the remote object from the registry
	   String url=null;
	   
	   try
       {
          int port= 5050;
          System.out.println("Security Manager loaded\n");
          url = "//"+ args[0] +"/GatewayManager";
          remoteObject = (WSNGatewayManager)Naming.lookup(url);
          
          System.out.println("Got remote object\n");      
       }
	   catch (RemoteException exc){System.out.println("Error in lookup: " + exc.toString());}
       catch (java.net.MalformedURLException exc){System.out.println("Malformed URL: " + exc.toString());}
       catch (java.rmi.NotBoundException exc){System.out.println("NotBound: " + exc.toString());} 
       
       
        
       WSNGateway gw=null;
       try 
       {
    	   gw = remoteObject.getWSNGatewayList().get(0);
       } 
       catch (RemoteException e) {e.printStackTrace();}
       
       
       gw.refreshMoteList();
       try 
       {
    	   Thread.sleep(5000);
       } 
       catch (InterruptedException e) {e.printStackTrace();}
	}
}
