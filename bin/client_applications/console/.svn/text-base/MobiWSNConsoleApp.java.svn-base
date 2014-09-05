
package client_applications.console;


import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import remote_interfaces.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;


/**
 * The main class of the application.
 */
public class MobiWSNConsoleApp extends SingleFrameApplication {

    
    public ArrayList<WSNGateway> gatewayList;
    public WSNGatewayManager gatewayManager;  
    
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new MobiWSNConsoleView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of MobiWSNConsoleApp
     */
    public static MobiWSNConsoleApp getApplication() {
        return Application.getInstance(MobiWSNConsoleApp.class);
    }
    
    /**
     * Main method launching the application.
     */
    public static void main(String[] args) 
    {
        
        System.setProperty("java.security.policy", "src/myjava.policy");
			
        /*
         * impostazione SecurityManager
         */
        System.setSecurityManager(new SecurityManager());       
        
		//EventAlertImpl rmiObject= null;

		// OSS:
		// Registrazione del client all'RMI (stampa a video i messaggi di FUNCTIONALITY_ANNOUNCEMENT finchè non verrà
		// attivata l'HomeManagement, la quale sarà un client a parte e quindi con un altro IP e un'altra interfaccia RMI)
		try 
		{
						
			//System.setSecurityManager(new SecurityManager());
			
			Thread.currentThread().setName("ClientOnlineManager_Thread");

			//rmiObject = new EventAlertImpl();
		
			//TODO: bisogna salvarsi l'indirizzo ip del client e del manager passandoli come parametri:
			//      args[0] = my_ip;
			//      args[1] = manager_ip;
			//      facendo poi la rebind con l'ip del client (my_ip).
			//Naming.rebind("//127.0.0.1/ClientManager", rmiObject);
			
		}
		//catch (RemoteException exc){System.out.println("Error in lookup: " + exc.toString());}
		//catch (java.net.MalformedURLException exc){System.out.println("Malformed URL: " + exc.toString());}
		catch(Exception ex){System.out.println(ex.toString());}        
        
        launch(MobiWSNConsoleApp.class, args);
    }
}
