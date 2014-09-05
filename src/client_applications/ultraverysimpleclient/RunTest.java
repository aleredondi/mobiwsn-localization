package client_applications.ultraverysimpleclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

import common.exceptions.FunctionalityException;
import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;

import remote_interfaces.WSNGateway;
import remote_interfaces.WSNGatewayManager;
import remote_interfaces.functionality.Functionality;
import remote_interfaces.mote.Mote;
import remote_interfaces.sensor.Sensor;

public class RunTest implements Runnable {

	protected RunTest() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	static RunTest me;
	static String[] args;
	ASubscriber sub;
	Thread myThread;
	
	/**
	 * @param args
	 */
	public static void main(String[] argo) {
		args = argo;
		try {
			me = new RunTest();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(me).start();
	}
	
	public void connectToRmi(String manager_url ) {
        System.setProperty("java.security.policy", "src/myjava.policy");
		
        /*
         * impostazione SecurityManager
         */
        System.setSecurityManager(new SecurityManager());       
        

		// OSS:
		// Registrazione del client all'RMI (stampa a video i messaggi di FUNCTIONALITY_ANNOUNCEMENT finchè non verrà
		// attivata l'HomeManagement, la quale sarà un client a parte e quindi con un altro IP e un'altra interfaccia RMI)
						
			System.setSecurityManager(new SecurityManager());
			
/*			Thread.currentThread().setName("ClientOnlineManager_Thread");

			rmiObject = new EventAlertImpl();
		
			//TODO: bisogna salvarsi l'indirizzo ip del client e del manager passandoli come parametri:
			//      args[0] = my_ip;
			//      args[1] = manager_ip;
			//      facendo poi la rebind con l'ip del client (my_ip).
			Naming.rebind("//127.0.0.1/ClientManager", rmiObject);
*/
			try {
				WSNGatewayManager gatewayManager;
				System.out.println("Cerco  " +"//" + manager_url + "/GatewayManager" );
				gatewayManager = (WSNGatewayManager) Naming.lookup("//" + manager_url + "/GatewayManager");
				System.out.println(gatewayManager.getRmiAddress());
				
				/*
				* WSNGateway list lookup
				*/
				ArrayList<WSNGateway> gatewayList = gatewayManager.getWSNGatewayList();
				Iterator<WSNGateway> it =gatewayList.iterator();
				while(it.hasNext())
					System.out.println(it.next().getName());
				
				Iterator<WSNGateway> gi = gatewayList.iterator();
				
				while( gi.hasNext() ) {
					WSNGateway curr_gw = gi.next();
					System.out.println("Gateway found: " + curr_gw.getName() + " at " + curr_gw.getIpAddress() );
				}
				
				System.out.println("Prendo il primo gateway");
				ArrayList<Mote> ml = gatewayList.get(0).getMoteList();
				
				Iterator<Mote> gm = ml.iterator();
				
				while( gm.hasNext() ) {
					Mote curr_mote = gm.next();
					System.out.println("------------>Mote found: " + curr_mote.getId() + " with parent " + curr_mote.getParentMote() );
					

					ArrayList<Sensor> ms = curr_mote.getSensorList();
					
					if (ms != null) {
						Iterator<Sensor> s = ms.iterator();
						
						while( s.hasNext() ) {
							Sensor curr_s = s.next();
							System.out.println("---------------------->Sensor found: " + curr_s.getId() );
						}
						System.out.println();
					} else
						System.out.println("Non ho Sensor");
					
					
					ArrayList<Functionality> mf = curr_mote.getFunctionalityList();
					
				if (mf != null) {
					Iterator<Functionality> f = mf.iterator();
					
					while( f.hasNext() ) {
						Functionality curr_f = f.next();
						System.out.println("---------------------->Functionality found: " + curr_f.getId() );
						try {
							curr_f.startFunctionality(null);
						} catch (MiddlewareException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FunctionalityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MoteUnreachableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if ( curr_f.hasPublisher() ) {
							System.out.println("---------------------->Ha un publisher e quindi mi sottoscrivo: " + curr_f.getId() );
							if ( curr_f.getPublisher() != null )
								curr_f.getPublisher().addSubscriber(sub);
							else
								System.out.println("Errore � nullo");
						} else {
							System.out.println("Non publica nulla");
						}
					}
					System.out.println();
				} else
					System.out.println("Non ho func");
				}
				System.out.println();
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while(true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			

	}


	@Override
	public void run() {
		try {
			sub = new ASubscriber();
			myThread = new Thread(sub);
			myThread.start();
			connectToRmi(args[0]);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
