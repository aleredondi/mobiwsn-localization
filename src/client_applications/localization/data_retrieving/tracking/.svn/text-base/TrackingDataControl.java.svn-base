package client_applications.localization.data_retrieving.tracking;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import client_applications.localization.Point2D;
import client_applications.localization.data_retrieving.tracking.FunctionalitySubscriber;


public class TrackingDataControl implements Runnable {
	FunctionalitySubscriber f_sub;
	static String[] args;
	static TrackingDataControl me;
	ArrayList<Mote> ml;
	static PrintStream p_mobile;
	static PrintStream p_anchor;
	FileOutputStream fos_mobile = null;
	static ControlPanel control_panel;
	protected TrackingDataControl() throws RemoteException{
		super();
	}
	Point2D currentPoint = new Point2D(0,0);
	//tracking antlab
	//double path_x[] = {2.262, 3.162, 4.062, 4.962, 5.862, 5.862, 6.762, 7.662, 8.562, 9.462, 10.362, 11.262, 12.162, 13.062, 13.962, 14.862, 15.762, 16.662, 17.562, 18.462, 18.462, 18.462, 18.462, 17.562};	
	//double path_y[] = {3.23,   3.23,  3.23,  3.23,  3.23,  4.13,  4.13,  4.13,  4.13,  4.13,   4.13,   4.13,   4.13,   4.13,   4.13,   4.13,   4.13,   4.13,   4.13,   4.13,   3.23,   2.33,   1.43,   1.43};

	//tracking 3 piano
	double path_x[] = {2.262, 3.162, 4.062, 4.962, 5.862, 5.862, 6.762, 7.662, 8.562, 9.462, 10.362, 11.262, 12.162, 13.062, 13.962, 14.862, 15.762, 16.662, 17.562, 18.462,
			18.462, 18.462, 18.462, 18.462, 19.362, 20.262, 21.162, 22.062, 22.962, 22.962, 23.862, 24.762, 24.762, 25.662, 26.562, 27.462, 28.362,
			29.262, 30.162, 30.162, 30.162, 30.162, 29.262, 28.362, 27.462, 26.562, 25.662, 25.662, 25.662, 25.662, 24.762, 24.762, 23.862, 23.862,
			24.762, 24.762, 24.762, 24.762, 24.762, 25.662, 26.562, 27.462, 28.362, 29.262, 30.162};	
	double path_y[] = {3.23,   3.23,  3.23,  3.23,  3.23,  4.13,  4.13,  4.13,  4.13,  4.13,   4.13,   4.13,   4.13,   4.13,   4.13,   4.13,   4.13,   4.13,   4.13,   4.13,
			5.03, 5.93, 6.83, 7.73, 7.73, 7.73, 7.73, 7.73, 7.73, 8.63, 8.63, 8.63, 7.73, 7.73, 7.73, 7.73, 7.73, 7.73, 7.73, 6.83, 5.93,
			5.03, 5.03, 5.03, 5.03, 5.03, 5.03, 5.93, 6.83, 7.73, 7.73, 8.63, 8.63, 9.53, 9.53, 10.43, 11.33, 12.23, 13.13, 13.13,  13.13,
			13.13, 13.13, 13.13, 13.13};

	public static void main(String[] arg) {
		
		
		args = arg;
		System.out.println(args[0]);
		FileOutputStream fos_anchor =null ,fos_mobile = null;
		try {
			fos_anchor = new FileOutputStream("src/client_applications/localization/data_retrieving/data/tracking3piano_4/anchorRSSItracking.txt",true);
			fos_mobile = new FileOutputStream("src/client_applications/localization/data_retrieving/data/tracking3piano_4/mobileRSSItracking.txt",true);
			
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		p_anchor = new PrintStream(fos_anchor);
		p_mobile = new PrintStream(fos_mobile);
//    	
//		try {
//			fos_mobile = new FileOutputStream("mobileRSSI.txt",true);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
//    	p_mobile = new PrintStream(fos_mobile);
		try{
			me = new TrackingDataControl();
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
			p_mobile.println(currentPoint.getX());
			p_mobile.println(currentPoint.getY());
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
	
	public class ControlPanel extends JFrame implements KeyListener{
		int count = 0;
		JPanel panel = new JPanel();
		JButton button = new JButton("NEXT!");
		JLabel x_l = new JLabel("X:");
		//JTextArea x = new JTextArea("x.....");
		JLabel y_l = new JLabel("Y:");
		//JTextArea y = new JTextArea("y.....");
		boolean dir = true;
		
		public ControlPanel(){
			panel.add(x_l);
			//panel.add(x);
			panel.add(y_l);
			//panel.add(y);
			panel.add(button);
			System.out.println(path_x.length);
			System.out.println(path_y.length);
			button.addKeyListener(this);
			currentPoint.setX(path_x[0]);
			currentPoint.setY(path_y[0]);
			this.setContentPane(panel);
			this.setVisible(true);
			this.setTitle("Data Retrieving");
			this.setSize(200,100);
			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		}

		



		@Override
		public void keyPressed(KeyEvent arg0) {		
			if(arg0.getKeyCode() == KeyEvent.VK_SPACE){
				
				currentPoint.setX(path_x[count]);
				currentPoint.setY(path_y[count]);
				System.out.println(count);
				currentPoint.print();
				if(dir){
					count++;
					if(count==path_x.length){
						count--;
						dir = false;
					}
				}
				else
				{
					count--;
					if(count<0)
						count = 0;
				}
			}
			
		}


		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
