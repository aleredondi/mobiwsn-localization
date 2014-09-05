package common;

import java.io.*;
import java.net.URL;

import java.rmi.Naming;
import java.rmi.RemoteException;

import gateway.WSNGatewayInternalImpl;
import gateway.WSNGatewayRemoteImpl;
import gateway_manager.WSNGatewayManagerImpl;
import client_applications.console.MobiWSNConsoleApp;
import remote_interfaces.WSNGateway;
import common.exceptions.RegisteringGatewayException;

public class ProjectMain {

	//static ProjectMain me;
	
	static String ip = "127.0.0.1";
	static String managerUrl = "//"+ ip +"/GatewayManager-LAURA";
	static String comm;
	
	static WSNGatewayManagerImpl gatewayManager;
	static WSNGatewayInternalImpl gateway;
	
	Process proc = null;

	
	public static void main(String[] args) {	
		/*me =*/ new ProjectMain(args);
	}
	
	public ProjectMain(String[] args){
		/**
		 * @param args
		 */		
		comm = args[0];
		
		//System.setProperty("java.security.policy","myjava.policy");
		String filename = "myjava.policy";
	    URL url = getClass().getResource(filename);
        System.setProperty("java.security.policy", url.toString());
        
		/*
		 * impostazione SecurityManager
		 */
		System.setSecurityManager(new SecurityManager());
		
		//do_command("rmiregistry &");

		startWSNGatewayManager();
		
		startWSNGateway();
		
		MobiWSNConsoleApp.launch(MobiWSNConsoleApp.class, args);
		
	}
	
	
	static void startWSNGatewayManager()
	{		
		/*
		 * registrazione oggetto remoto nell'rmiregistry
		 */
		System.out.println("#ProjectMain#: Registering " + managerUrl + " to RMIRegistry...");
		
		try {
			gatewayManager = new WSNGatewayManagerImpl(managerUrl, ip);
		}
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try
		{			
			Naming.rebind(managerUrl, gatewayManager);
		}
		catch(java.net.MalformedURLException muex)
		{
			System.out.println(muex.getMessage());
		}
		catch(java.rmi.RemoteException  rex)
		{
			System.out.println(rex.getMessage());
		}
		
		System.out.println("#ProjectMain#: WSNGatewayManager Ready!\n");
	
	}

	static void startWSNGateway()
	{
		System.out.println("#ProjectMain#: Registering WSNGateway...");
		
		/*
		 * ricerca dell'oggetto remoto nell'rmiregistry
		 */
		//System.out.println("Looking for WSNGatewayManager at " + managerUrl + " ...");
		
		//WSNGatewayManager gatewayManager = (WSNGatewayManager)Naming.lookup(managerUrl);		
		
		//if (gatewayManager != null)
		//{
			/*
			 * registrazione del gateway sul manager
			 */
			//System.out.println("Lookup completed,  registering WSNGateway ...");	
			
			try {
				gateway = new WSNGatewayInternalImpl(managerUrl, gatewayManager, comm, "WSN_GW1");
			
				WSNGateway rgw = gateway.getRemoteGateway();

				gatewayManager.registerGateway(rgw);
			}
			catch (RegisteringGatewayException rge)
			{
				// TODO non funziona
				System.out.println("#ProjectMain#: A gateway with the same name is already registered!");
				return;
			}
		//}
            catch (RemoteException rex)
            {
    			System.out.println(rex.getMessage());
			}
		
		/*
		 * start del gateway
		 */
		System.out.println("#ProjectMain#: Starting WSNGateway...");
		
		gateway.serverStart();
		
		/*
		 * aggiornamento della lista dei nodi già connessi al gateway
		 */
		
		WSNGatewayRemoteImpl gateway_remote = (WSNGatewayRemoteImpl) gateway.getRemoteGateway();
		try {
			gateway_remote.refreshMoteList();
		} catch (RemoteException rex)
		{
			System.out.println(rex.getMessage());
		}			
					
		System.out.println("#ProjectMain#: WSNGatewayReady!\n");
	
	}
	
	private static void do_command(String command){
		File wd = new File("/home");
		Process proc = null;
		try {
			proc = Runtime.getRuntime().exec("/bin/bash", null, wd);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		if (proc != null) {
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
			out.println(command);
			out.println("exit");
			try {
				String line;
				while ((line = in.readLine()) != null) {
					System.out.println(line);
				}
				proc.waitFor();
				in.close();
				out.close();
				proc.destroy();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
