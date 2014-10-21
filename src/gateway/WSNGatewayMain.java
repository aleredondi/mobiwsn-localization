
package gateway;


import remote_interfaces.WSNGateway;
import remote_interfaces.WSNGatewayManager;

import java.rmi.*;

import common.exceptions.RegisteringGatewayException;


/**
 * This class represent the main class of the gateway, it is the only executable class of the gateway packet
 * @author Alessandro Laurucci
 *
 */
public class WSNGatewayMain 
{

	public static void main(String[] args) 
	{
		WSNGatewayInternalImpl gatewayImpl= null;
		String ip_manager=args[0];
		
		String comm=args[2];

		String name_gw=args[3];		

		String url="//"+ ip_manager +"/GatewayManager-" + args[1]; //get the url of the gateway manager
		
		try
		{
	        System.setProperty("java.security.policy", "src/myjava.policy");
			
	        /*
	         * impostazione SecurityManager
	         */
	        System.setSecurityManager(new SecurityManager());   

			//Thread.currentThread().setName("Main_Thread");
			
			System.out.println("Creating WSNGateway...");
			
			//record of the remote object in the rmiregistry
			System.out.println("Looking for WSNGatewayManager at " + url + " ...");
			WSNGatewayManager gatewayManager = (WSNGatewayManager)Naming.lookup(url);
			
			if (gatewayManager != null)
			{
				System.out.println("Lookup completed,  registering WSNGateway ...");
				gatewayImpl = new WSNGatewayInternalImpl(ip_manager, gatewayManager, comm, name_gw);
				WSNGateway rgw = gatewayImpl.getRemoteGateway();
				//System.out.println(rgw.toString());
				try {
					gatewayManager.registerGateway(rgw);
				} catch (RegisteringGatewayException e) {
					// TODO non funziona
					System.out.println("A gateway with the same name is already registered.");
					return;
				}
			}
			
			//Loading saved classes
			System.out.println("Starting WSN_Gateway...\n");
			gatewayImpl.serverStart();
			
			//refresh the list of the motes connected to gateway
			//gatewayImpl.refreshMoteList();			
						
			System.out.println("\nReady!\n");
		
		}
		
		catch(java.net.MalformedURLException muex){System.out.println(muex.getMessage());}
		catch(java.rmi.RemoteException  rex){System.out.println(rex.getMessage());}
		catch(Exception ex){System.out.println(ex.getMessage());}

	}

}
	
