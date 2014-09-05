package client_applications.localization.data_retrieving.local;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


import javax.swing.*;

import Jama.Matrix;

import remote_interfaces.WSNGateway;
import remote_interfaces.WSNGatewayManager;
import remote_interfaces.functionality.Functionality;
import remote_interfaces.mote.Mote;
import remote_interfaces.sensor.Sensor;

import client_applications.localization.AnchorNode;
import client_applications.localization.MobileNode;
import client_applications.localization.data_retrieving.local.FunctionalitySubscriber;

public class LocalDataControl implements Runnable {
	FunctionalitySubscriber f_sub;
	static String[] args;
	static LocalDataControl me;
	ArrayList<Mote> ml;
	static PrintStream p_mobile;
	static PrintStream p_anchor;
	FileOutputStream fos_mobile = null;
	static ControlPanel control_panel;
	protected LocalDataControl() throws RemoteException{
		super();
	}
	
	public static void main(String[] arg) {
		args = arg;
		System.out.println(args[0]);
		FileOutputStream fos_anchor = null;
		try {
			fos_anchor = new FileOutputStream("data/anchorRSSI.txt",true);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		p_anchor = new PrintStream(fos_anchor);
//    	
//		try {
//			fos_mobile = new FileOutputStream("mobileRSSI.txt",true);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
//    	p_mobile = new PrintStream(fos_mobile);
		try{
			me = new LocalDataControl();
		}catch (RemoteException e) {
			e.printStackTrace();
		}
		new Thread(me).start();
	}

	public void run() {
		try {
			control_panel = new ControlPanel();
			f_sub = new FunctionalitySubscriber(me);
			connectToRmi(args[0]);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void connectToRmi(String manager_url ) {
		String filename = "myjava.policy";
	    URL url = getClass().getResource(filename);
        System.setProperty("java.security.policy", url.toString());
		
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
				System.out.println("Cerco  " +"//" + manager_url + "/GatewayManager-LAURA" );
				gatewayManager = (WSNGatewayManager) Naming.lookup("//" + manager_url + "/GatewayManager-LAURA");
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
				ml = gatewayList.get(0).getMoteList();
//				gatewayList.get(0).addSubscriber(t_sub);
				Iterator<Mote> gm = ml.iterator();
				
				while( gm.hasNext() ) {
					Mote curr_mote = gm.next();
					System.out.println("------------>Mote found: " + curr_mote.getId() + ", " + curr_mote.getMACAddress());
					
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
//						try {
//							try {
//								curr_f.startFunctionality(null);
//							} catch (FunctionalityException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (MoteUnreachableException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						} catch (MiddlewareException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						if ( curr_f.hasPublisher() ) {
							System.out.println("---------------------->Ha un publisher e quindi mi sottoscrivo: " + curr_f.getId() );
							if ( curr_f.getPublisher() != null )
								curr_f.getPublisher().addSubscriber(f_sub);
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
	}
	
	public void printMacToFile(boolean mobile, Integer mac){
		if(mobile){
			p_mobile.println(mac);
		}
		else{
			p_anchor.println(mac);
		}
	}
	
	public void printTimeToFile(boolean mobile, Date date){
		if(mobile){
			p_mobile.println(DateFormat.getDateTimeInstance ( DateFormat.LONG, DateFormat.LONG ).format(date) );
		}
		else{
			p_anchor.println(DateFormat.getDateTimeInstance ( DateFormat.LONG, DateFormat.LONG ).format(date) );
		}
	}
	public void printSyncToFile(boolean mobile, Integer sync){
		if(mobile){
			p_mobile.println(sync);
		}
		else{
			p_anchor.println(sync);
		}
		
	}
	public void printListToFile(boolean mobile, Matrix rssi_list){
		if(mobile){
			for(int i=0;i<rssi_list.getRowDimension();i++){
				for(int j=0; j<rssi_list.getColumnDimension();j++){
					p_mobile.print(rssi_list.get(i, j) + " ");
				}
				p_mobile.println();
			}
			p_mobile.println();
		}
		else{
			for(int i=0;i<rssi_list.getRowDimension();i++){
				for(int j=0; j<rssi_list.getColumnDimension();j++){
					p_anchor.print(rssi_list.get(i, j) + " ");
				}
				p_anchor.println();
			}
			p_anchor.println();
		}	
	}
	
	public class ControlPanel extends JFrame implements ActionListener{
		
		JPanel panel = new JPanel();
		JButton button = new JButton("Set!");
		JLabel x_l = new JLabel("X:");
		JTextArea x = new JTextArea("x.....");
		JLabel y_l = new JLabel("Y:");
		JTextArea y = new JTextArea("y.....");
		
		
		public ControlPanel(){
			panel.add(x_l);
			panel.add(x);
			panel.add(y_l);
			panel.add(y);
			panel.add(button);
			
			button.addActionListener(this);
			
			this.setContentPane(panel);
			this.setVisible(true);
			this.setTitle("Data Retrieving");
			this.setSize(200,100);
			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		}

		
		public void actionPerformed(ActionEvent arg0) {
			String file = "data/" + x.getText() + "_" + y.getText() + ".txt";
			System.out.println(file);
			try {
				fos_mobile = new FileOutputStream(file,true);
				p_mobile = new PrintStream(fos_mobile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}
