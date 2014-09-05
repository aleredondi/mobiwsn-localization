 
package client_applications.profiling_system;


import remote_interfaces.WSNGatewayManager;
import remote_interfaces.clients.profiling_system.*;
import client_applications.profiling_system.profiling_system_impl.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.net.MalformedURLException;


/***
* @author Antimo Barbato
*
*/

public class ProfilingSystemMain 
{
	


	public static void main(String[] args) 
	{
			ArrayList<String> moteGroupsNames = new ArrayList<String>();
			String url="//"+ args[0] +"/ProfilingSystem-" + args[1];
			String ip_manager=args[2];
			String GTW_Man_url="//"+ ip_manager +"/GatewayManager-" + args[3]; //get the url of the gateway manager
			WSNGatewayManager gatewayManager=null;
	        System.setProperty("java.security.policy", "src/myjava.policy");
			

	        /*
	         * impostazione SecurityManager
	         */
	        
	        System.setSecurityManager(new SecurityManager());       
	        Thread.currentThread().setName("ProfilingSystem_Thread");
	       


			try
			{

				System.out.println("Connection to WSNGatewayManager to load the mote groups list...");
				
				//record of the remote object in the rmiregistry
				System.out.println("Looking for WSNGatewayManager at " + GTW_Man_url + " ...");
				
				gatewayManager = (WSNGatewayManager)Naming.lookup(GTW_Man_url);
				
				if (gatewayManager != null){
				
					System.out.println("Lookup completed!");

		    		  try {
		    			  if(!gatewayManager.isMoteGroupsListEmpty()){
		    				  moteGroupsNames=gatewayManager.getMoteGroupsNames();
		    			  }
		    		  } 
		    		  catch (RemoteException e) {
						e.printStackTrace();
		    		  }
				}
				else
					System.out.println("No Gateway Manager found!");
			}
			catch(java.net.MalformedURLException muex){System.out.println(muex.getMessage());}
			catch(java.rmi.RemoteException  rex){System.out.println(rex.getMessage());}
			catch(Exception ex){System.out.println(ex.getMessage());}
			
	        
	        
	        
			/*
			 * registrazione oggetto remoto nell'rmiregistry
			 */
	        
	        System.out.println("Registering " + url + " to RMIRegistry...");
	        
			try {

				if(gatewayManager!=null && moteGroupsNames.size()!=0){
					
					ProfilingSystemImpl profilingSystemImpl = new ProfilingSystemImpl(moteGroupsNames,gatewayManager);

					try {
						Naming.rebind(url,profilingSystemImpl);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Ready!");
				}
				else{
					if(gatewayManager==null)
						System.out.println("No Gateway Manager found!");
					if(moteGroupsNames.size()==0)
						System.out.println("No Mote groups found!");
				}
					

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

	        
			

		
	}
	
	

}
	