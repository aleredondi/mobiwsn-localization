
package gateway_manager;


import java.rmi.*;


/*
 * 	A simple class used to instantiate a WSNGatewayManager object to 
 * 	register WSNGateway objects
 */
public class WSNGatewayManagerMain 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		WSNGatewayManagerImpl gatewayManagerImpl= null;
		String my_ip = args[0];
		String url="//"+ my_ip +"/GatewayManager-" + args[1];
		
		/*
		 * send a Discovery Message to WSN;  
		 */
		try
		{
			System.setProperty("java.security.policy","src/myjava.policy");
					
			/*
			 * impostazione SecurityManager
			 */
			System.setSecurityManager(new SecurityManager());
			
			Thread.currentThread().setName("WSNGatewayManagerImpl_Thread");
			
			/*
			 * registrazione oggetto remoto nell'rmiregistry
			 */
			System.out.println("Registering " + url + " to RMIRegistry...");
			
			gatewayManagerImpl = new WSNGatewayManagerImpl(url, my_ip);
			
			Naming.rebind(url, gatewayManagerImpl);
			
			System.out.println("Ready!");		
			
		}
		
		catch(java.net.MalformedURLException muex)
		{
			System.out.println(muex.getMessage());
		}
		catch(java.rmi.RemoteException  rex)
		{
			System.out.println(rex.getMessage());
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
			
	}

}
	
