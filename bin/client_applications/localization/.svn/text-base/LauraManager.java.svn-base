package client_applications.localization;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import client_applications.localization.graphics.*;
import remote_interfaces.clients.localization.Status;

import remote_interfaces.WSNGateway;
import remote_interfaces.WSNGatewayManager;
import remote_interfaces.functionality.Functionality;
import remote_interfaces.mote.Mote;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.sensor.SensorType;

import Jama.*;

import javax.measure.Measure;
import javax.measure.quantity.Temperature;
import javax.measure.unit.SI;
import javax.swing.JFrame;

public class LauraManager implements Runnable{
	
	static LauraManager me;
	
	//mobiwsn variables
	static String[] args;
	static LauraMainWindow lmw_main;
	static MobileManager mobile_manager;
	static AnchorManager anchor_manager;
	
	FunctionalitySubscriber f_sub;
	TopologySubscriber t_sub;
	
	
	Thread my_thread;
	ArrayList<Mote> ml;
	
	//laura variables
	private RssiMatrix S = new RssiMatrix(); //matrice degli rssi
	public static ArrayList<MobileNode> mobile_list = new ArrayList<MobileNode>(); //lista dei nodi mobili 
	public static ArrayList<AnchorNode> anchor_list = new ArrayList<AnchorNode>(); //lista delle ancore 
	Random rgen = new Random();
	//constant
	private static double SELF_DISTANCE = 0.04;
	
	//default param
	private LauraParam param = new LauraParam(false,2,0.9,0.9,300);
	
	//file di log
	/*
	static PrintStream p_netStatus;
	static PrintStream p_anchors;
	*/
	
    //For the periodic status   
    //private Timer refTimer = new Timer();
    //private TimerTask refresh = new StatusTask();

	//default constructor
	public LauraManager() throws RemoteException{
		super();
	
		/*
		FileOutputStream fos_netStatus = null;
		FileOutputStream fos_anchors = null;
		try {
			fos_netStatus = new FileOutputStream("Network_status.txt",true);	
			fos_anchors = new FileOutputStream("anchors.txt",true);	
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		
		p_netStatus = new PrintStream(fos_netStatus);
		p_anchors = new PrintStream(fos_anchors);
		*/

		//refTimer.schedule( refresh, 21600000, 21600000 ); // ogni 6 ore
		
		new Thread(this).start();
	}

	
	//main: start thread and graphic console.
	public static void main(String[] arg) {
		args = arg;
		//System.out.println(args[0]);
		try{
			me = new LauraManager();
		}catch (RemoteException e) {
			e.printStackTrace();
		}
		//new Thread(me).start();
	}

	
	//thread started: connect to rmi and init...
	public void run() {
		try {
			f_sub = new FunctionalitySubscriber(this);
			t_sub = new TopologySubscriber(this);
			System.out.println("@vigilo@: Connect to RMI: 127.0.0.1");//args[0]);
			connectToRmi("127.0.0.1"); //args[0]
			lmw_main = new LauraMainWindow(this,anchor_list,mobile_list);
			lmw_main.setVisible(true);
	    	lmw_main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	lmw_main.pack();                        
	     	lmw_main.setLocationRelativeTo(null);  
	     	
	     	mobile_manager = new MobileManager(mobile_list,lmw_main);
	     	anchor_manager = new AnchorManager(anchor_list,lmw_main);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	//connecting to RMI: retrieve mote list from gateways
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
        /*						
			System.setSecurityManager(new SecurityManager());
			
			Thread.currentThread().setName("ClientOnlineManager_Thread");

			rmiObject = new EventAlertImpl();
		
			//TODO: bisogna salvarsi l'indirizzo ip del client e del manager passandoli come parametri:
			//      args[0] = my_ip;
			//      args[1] = manager_ip;
			//      facendo poi la rebind con l'ip del client (my_ip).
			Naming.rebind("//127.0.0.1/ClientManager", rmiObject);*/
			
//        	MobileNode mobile_prova = new MobileNode("MICAZM15",10,10,param,true,true);
//			mobile_list.add(mobile_prova);
//			
//        for(int i=1;i<=14;i++){
//        	AnchorNode anchor_prova = new AnchorNode(Integer.toString(i),0,0,true);
//        	switch(i){
//        	case 1:
//        		anchor_prova.setX(26.5);
//        		anchor_prova.setY(9);
//        		break;
//        	case 2:
//        		anchor_prova.setX(25.3);
//        		anchor_prova.setY(5.5);
//        		break;
//        	case 3:
//        		anchor_prova.setX(19.5);
//        		anchor_prova.setY(5.5);
//        		break;
//        	case 4:
//        		anchor_prova.setX(18.3);
//        		anchor_prova.setY(8.8);
//        		break;
//        	case 5:
//        		anchor_prova.setX(14.6);
//        		anchor_prova.setY(5.5);
//        		break;
//        	case 6:
//        		anchor_prova.setX(12);
//        		anchor_prova.setY(3.95);
//        		break;
//        	case 7:
//        		anchor_prova.setX(6.5);
//        		anchor_prova.setY(9);
//        		break;
//        	case 8:
//        		anchor_prova.setX(0.5);
//        		anchor_prova.setY(3.8);
//        		break;
//        	case 9:
//        		anchor_prova.setX(0.5);
//        		anchor_prova.setY(14.5);
//        		break;
//        	case 10:
//        		anchor_prova.setX(0.5);
//        		anchor_prova.setY(22.5);
//        		break;
//        	case 11: 
//        		anchor_prova.setX(6.5);
//        		anchor_prova.setY(19.5);
//        		break;
//        	case 12:
//        		anchor_prova.setX(14);
//        		anchor_prova.setY(22.5);
//        		break;
//        	case 13:
//        		anchor_prova.setX(17.5);
//        		anchor_prova.setY(19.5);
//        		break;
//        	case 14:
//        		anchor_prova.setX(23.5);
//        		anchor_prova.setY(24);
//        		break;
//        	case 15:
//        		anchor_prova.setX(27);
//        		anchor_prova.setY(20.5);
//        		break;
//        	}
//        	anchor_list.add(anchor_prova);
//        }
        
			
			try {
				WSNGatewayManager gatewayManager;
				//System.out.println("Cerco  " +"//" + manager_url + "/GatewayManager-LAURA" );
				gatewayManager = (WSNGatewayManager) Naming.lookup("//" + manager_url + "/GatewayManager-LAURA");
				//System.out.println(gatewayManager.getRmiAddress());
				
				/*
				* WSNGateway list lookup
				*/
				ArrayList<WSNGateway> gatewayList = gatewayManager.getWSNGatewayList();
				Iterator<WSNGateway> it =gatewayList.iterator();
				
				//while(it.hasNext())
					//System.out.println(it.next().getName());
				
				Iterator<WSNGateway> gi = gatewayList.iterator();
				
				while( gi.hasNext() ) {
					WSNGateway curr_gw = gi.next();
					//System.out.println("Gateway found: " + curr_gw.getName() + " at " + curr_gw.getIpAddress() );
				}
				
				//System.out.println("Prendo il primo gateway");
				ml = gatewayList.get(0).getMoteList();
				gatewayList.get(0).addSubscriber(t_sub);
				Iterator<Mote> gm = ml.iterator();
				
				System.out.println("@vigilo@: Research of the network nodes...");
				
				while( gm.hasNext() ) {
					Mote curr_mote = gm.next();
					System.out.println("------------>Mote found: " + curr_mote.getId());
					
					//LAURA
					//creo i nodi ancora e i nodi mobili
					int ind = curr_mote.getId().indexOf(".");
					if(curr_mote.getId().charAt(ind+1) == 'F' && curr_mote.getMACAddress()!=0){
						AnchorNode new_anchor = new AnchorNode(curr_mote.getId(),0,0,curr_mote.isReachable(),curr_mote);
						
						System.out.println("---------------------->it's a fixed node");
						
						//rfid anchors location						
						switch(new_anchor.getMac()){
						case 1:
							new_anchor.setX(1.3);
							new_anchor.setY(4);
							break;
						case 2:
							new_anchor.setX(24.7);
							new_anchor.setY(6.15);
							break;
						case 3:
							new_anchor.setX(19.3);
							new_anchor.setY(6.1);
							break;
						case 4:
							new_anchor.setX(18);
							new_anchor.setY(7.6);
							break;
						case 5:
							new_anchor.setX(14.6);
							new_anchor.setY(6.15);
							break;
						case 6:
							new_anchor.setX(6);
							new_anchor.setY(7.7);
							break;
						case 7:
							new_anchor.setX(24.8);
							new_anchor.setY(9.1);
							break;
						case 8:
							new_anchor.setX(1.3);
							new_anchor.setY(11.3);
							break;
						case 9:
							new_anchor.setX(1.3);
							new_anchor.setY(21.6);
							break;
						case 10: 
							new_anchor.setX(5.7);
							new_anchor.setY(20);
							break;
						case 11:
							new_anchor.setX(14);
							new_anchor.setY(21.6);
							break;
						case 12:
							new_anchor.setX(17.6);
							new_anchor.setY(20);
							break;
						case 13:
							new_anchor.setX(8);
							new_anchor.setY(4);
							break;
						case 14:
							new_anchor.setX(25.7);
							new_anchor.setY(18);
							break;
						case 15:
							new_anchor.setX(23.6);
							new_anchor.setY(23);
							break;
//						case 16:
//							new_anchor.setX(19.7*1.6);
//							new_anchor.setY(11.18*1.6);
//							break;
//						case 17:
//							new_anchor.setX(19.88*1.6);
//							new_anchor.setY(4.38*1.6);
//							break;
//						case 18:
//							new_anchor.setX(13.48*1.6);
//							new_anchor.setY(11.02*1.6);
//							break;
//						case 19: 
//							new_anchor.setX(1.38*1.6);
//							new_anchor.setY(10.28*1.6);
//							break;
						default:
							new_anchor.setX(rgen.nextDouble()*10);
							new_anchor.setY(rgen.nextDouble()*3);
							break;
						}
						
						anchor_list.add(new_anchor);
						
					}
					else if(curr_mote.getId().charAt(ind+1) == 'M'){
						boolean has_accel = false;
						
						System.out.println("---------------------->it's a MOBILE node");
						
						ArrayList<Sensor> ms = curr_mote.getSensorList();
						
						if (ms != null) {
							Iterator<Sensor> s = ms.iterator();
							
							while( s.hasNext() ) {
								Sensor curr_s = s.next();
								if(curr_s.getType().equals(SensorType.ACCELEROMETERX)){
									has_accel = true;
									System.out.println("---------------------->and it has an accelerometer!");
								}
							}
							
						} 
						//if(!has_accel)
							//System.out.println("non ho l'accelerometro");
						
						MobileNode new_mobile = new MobileNode(curr_mote.getId(),10,10,param,curr_mote.isReachable(),curr_mote,has_accel);
						mobile_list.add(new_mobile);
										
					}
	
					ArrayList<Functionality> mf = curr_mote.getFunctionalityList();
					
				if (mf != null) {
					Iterator<Functionality> f = mf.iterator();
					
					while( f.hasNext() ) {
						Functionality curr_f = f.next();
					
						//System.out.println("---------------------->Functionality found: " + curr_f.getId() );
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
							//System.out.println("---------------------->Ha un publisher e quindi mi sottoscrivo: " + curr_f.getId() );
							if ( curr_f.getPublisher() != null )
								curr_f.getPublisher().addSubscriber(f_sub);
							else
								System.out.println("Errore e' nullo");
						} else {
							System.out.println("Non publica nulla");
						}
					}
					System.out.println();
				} else
					System.out.println("Non ho func");
				}
				System.out.println();
				
			printAnchorsPosition();			
				
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
	
	public void updateTopology(WSNGateway pub, ArrayList<Mote> code) {
		//devo fare il confronto e vedere se è cambiato qualcosa.
		Iterator<Mote> gm = code.iterator();
		try{
			while( gm.hasNext() ) {
				Mote curr_mote = gm.next();
				int ind = curr_mote.getId().indexOf(".");
				if(curr_mote.getId().charAt(ind+1) == 'F' && curr_mote.getMACAddress()!=0){
					boolean found = false;
					for(int i=0;i<anchor_list.size();i++){
						if(curr_mote.getId().equals(anchor_list.get(i).getId())){
							//attiva e basta, disattiverà il manager
							if(curr_mote.isReachable()){
								anchor_list.get(i).setIs_reachable(curr_mote.isReachable());
							}
//							if(!curr_mote.isReachable()){
//								lmw_main.showPopup(anchor_list.get(i).getId());
//							}
							found = true;
							
						}
					}
					if(!found){
						AnchorNode new_anchor = new AnchorNode(curr_mote.getId(),0,0,curr_mote.isReachable(),curr_mote);
											
						System.out.println("------------>Add Fixed Mote:" + curr_mote.getId());
						
						ArrayList<Functionality> mf = curr_mote.getFunctionalityList();
						
						if (mf != null) {
							Iterator<Functionality> f = mf.iterator();
							
							while( f.hasNext() ) {
								Functionality curr_f = f.next();
							
								//System.out.println("---------------------->Functionality found: " + curr_f.getId() );

								if ( curr_f.hasPublisher() ) {
									//System.out.println("---------------------->Ha un publisher e quindi mi sottoscrivo: " + curr_f.getId() );
									if ( curr_f.getPublisher() != null )
										curr_f.getPublisher().addSubscriber(f_sub);
									else
										System.out.println("Errore � nullo");
								} else {
									System.out.println("Non publica nulla");
								}
							}
							System.out.println();
						}
						else{
							System.out.println("non ho func");
						}
						
						
						//rfid anchors location						
						switch(new_anchor.getMac()){
						case 1:
							new_anchor.setX(1.3);
							new_anchor.setY(4);
							break;
						case 2:
							new_anchor.setX(24.7);
							new_anchor.setY(6.15);
							break;
						case 3:
							new_anchor.setX(19.3);
							new_anchor.setY(6.1);
							break;
						case 4:
							new_anchor.setX(18);
							new_anchor.setY(7.6);
							break;
						case 5:
							new_anchor.setX(14.6);
							new_anchor.setY(6.15);
							break;
						case 6:
							new_anchor.setX(6);
							new_anchor.setY(7.7);
							break;
						case 7:
							new_anchor.setX(24.8);
							new_anchor.setY(9.1);
							break;
						case 8:
							new_anchor.setX(1.3);
							new_anchor.setY(15);
							break;
						case 9:
							new_anchor.setX(1.3);
							new_anchor.setY(21.6);
							break;
						case 10: 
							new_anchor.setX(5.7);
							new_anchor.setY(20);
							break;
						case 11:
							new_anchor.setX(14);
							new_anchor.setY(21.6);
							break;
						case 12:
							new_anchor.setX(17.6);
							new_anchor.setY(20);
							break;
						case 13:
							new_anchor.setX(11.8);
							new_anchor.setY(3.85);
							break;
						case 14:
							new_anchor.setX(25.7);
							new_anchor.setY(18);
							break;
						case 15:
							new_anchor.setX(23.6);
							new_anchor.setY(23);
							break;
//						case 1:
//							new_anchor.setX(9.18*1.6);
//							new_anchor.setY(10.22*1.6);
//							break;
//						case 2:
//							new_anchor.setX(7.72*1.6);
//							new_anchor.setY(12.98*1.6);
//							break;
//						case 3:
//							new_anchor.setX(5.32*1.6);
//							new_anchor.setY(10.4*1.6);
//							break;
//						case 4:
//							new_anchor.setX(3.4*1.6);
//							new_anchor.setY(12.42*1.6);
//							break;
//						case 6:
//							new_anchor.setX(12.52*1.6);
//							new_anchor.setY(9.75*1.6);
//							break;
//						case 7:
//							new_anchor.setX(15.6*1.6);
//							new_anchor.setY(8.1*1.6);
//							break;
//						case 8:
//							new_anchor.setX(18.15*1.6);
//							new_anchor.setY(5.95*1.6);
//							break;
//						case 9:
//							new_anchor.setX(18.1*1.6);
//							new_anchor.setY(3.95*1.6);
//							break;
//						case 10: 
//							new_anchor.setX(12.9*1.6);
//							new_anchor.setY(10.7*1.6);
//							break;
//						case 11:
//							new_anchor.setX(12.45*1.6);
//							new_anchor.setY(13.28*1.6);
//							break;
//						case 12:
//							new_anchor.setX(10.7*1.6);
//							new_anchor.setY(13.45*1.6);
//							break;
//						case 13:
//							new_anchor.setX(5.1*1.6);
//							new_anchor.setY(12.8*1.6);
//							break;
//						case 14:
//							new_anchor.setX(1.38*1.6);
//							new_anchor.setY(11.95*1.6);
//							break;
//						case 15:
//							new_anchor.setX(16.3*1.6);
//							new_anchor.setY(8.25*1.6);
//							break;
//						case 16:
//							new_anchor.setX(19.7*1.6);
//							new_anchor.setY(11.18*1.6);
//							break;
//						case 17:
//							new_anchor.setX(19.88*1.6);
//							new_anchor.setY(4.38*1.6);
//							break;
//						case 18:
//							new_anchor.setX(13.48*1.6);
//							new_anchor.setY(11.02*1.6);
//							break;
//						case 19: 
//							new_anchor.setX(1.38*1.6);
//							new_anchor.setY(10.28*1.6);
//							break;
						default:
							new_anchor.setX(rgen.nextDouble()*10);
							new_anchor.setY(rgen.nextDouble()*3);
							break;
						}
						
						anchor_list.add(new_anchor);
					}
				}
				else if(curr_mote.getId().charAt(ind+1) == 'M'){
					boolean found = false;
					for(int i=0;i<mobile_list.size();i++){
						if(curr_mote.getId().equals(mobile_list.get(i).getId())){
							mobile_list.get(i).setIs_reachable(curr_mote.isReachable());
							found = true;
						}
					}
					if(!found){
						
						MobileNode new_mobile = new MobileNode(curr_mote.getId(),10,10,param,curr_mote.isReachable(),curr_mote,false);
						ArrayList<Functionality> mf = curr_mote.getFunctionalityList();

						System.out.println("------------>Add MOBILE Mote:" + curr_mote.getId());
						
						if (mf != null) {
							Iterator<Functionality> f = mf.iterator();
							
							while( f.hasNext() ) {
								Functionality curr_f = f.next();
							
								//System.out.println("---------------------->Functionality found: " + curr_f.getId() );

								if ( curr_f.hasPublisher() ) {
									//System.out.println("---------------------->Ha un publisher e quindi mi sottoscrivo: " + curr_f.getId() );
									if ( curr_f.getPublisher() != null )
										curr_f.getPublisher().addSubscriber(f_sub);
									else
										System.out.println("Errore � nullo");
								} else {
									System.out.println("Non publica nulla");
								}
							}
							System.out.println();
						}
						else{
							System.out.println("non ho func");
						}
						
//						ArrayList<Sensor> ms = curr_mote.getSensorList();
//						
//						if (ms != null) {
//							Iterator<Sensor> s = ms.iterator();
//							
//							while( s.hasNext() ) {
//								Sensor curr_s = s.next();
//								if(curr_s.getType().equals(SensorType.ACCELEROMETERX)){
//									has_accel = true;
//									System.out.println("---------------------->and it has an accelerometer!");
//								}
//							}
//							
//						} 
						
						
						mobile_list.add(new_mobile);
					}
				}

			}
			lmw_main.updateNodesCombo();
			lmw_main.repaint();
			
			printAnchorsPosition();	
			
		}
		catch(RemoteException e){
			
		}
		
	}
		
	public void update(String mote_id, Matrix rssi_list){
		int ind = mote_id.indexOf(".");
		
		if(mote_id.charAt(ind+1) == 'F'){
			//update column
			
			S.updateColumn(rssi_list, mote_id);
			
			//update manager
			for(int i=0;i<anchor_list.size();i++){
				AnchorNode anchor = anchor_list.get(i);
				if(anchor.getId().equals(mote_id)){
					//System.out.println("resetting");
					anchor_manager.resetAnchorMsgCounter(anchor);
				}
			}
			
		}
		
		else if(mote_id.charAt(ind+1) == 'M'){
			//System.out.println("message from: " +mote_id);
			
						
			//update location: first split the rssi_list message in mac and rssi
			//int mac_complete[] = new int[rssi_list.getRowDimension()];
			int mac[] = new int[rssi_list.getRowDimension()];
			Matrix rssi = new Matrix(rssi_list.getRowDimension(),1);
			int size;
			for(int i=0;i<mac.length;i++){
				mac[i] = (int)rssi_list.get(i,0);
				rssi.set(i, 0, rssi_list.get(i, 1));
			}
					

			//rssi_list.print(2, 2);

			
			//retrieve S submatrix
			Matrix S_red = S.getSubMatrix(mac);
			//S_red.print(2, 2);
			
			//compute D submatrix
			Matrix D = getDistanceMatrix(mac);
			//D.print(2, 2);
			
			//retrieve anchor pos
			ArrayList<Point2D> anchor_pos = new ArrayList<Point2D>();
			for(int i=0;i<mac.length;i++){
				for(int k=0;k<anchor_list.size();k++){
					if(anchor_list.get(k).getMac() == mac[i]){
						anchor_pos.add(anchor_list.get(k).getLocation());
						break;
					}
				}
			}

			
			//update location
			Iterator<MobileNode> mi = mobile_list.iterator();
			while(mi.hasNext()){
				MobileNode curr_target = mi.next();
				if(curr_target.getId().equals(mote_id)){
					curr_target.updateLocation(rssi, anchor_pos, S_red, D);
					mobile_manager.resetMobileMsgCounter(curr_target);
					lmw_main.updateStat();
				}
			}
		}
	}
	
	public Matrix getDistanceMatrix(int[] mac){
		Matrix D = new Matrix(mac.length,mac.length,0);
		Point2D ipos = new Point2D();
		//setto la diagonale
		for(int i=0;i<mac.length;i++){
			D.set(i, i, SELF_DISTANCE);
		}
		
		for(int i=0;i<mac.length;i++){
			//retrieve position of i-th anchor
			Iterator<AnchorNode> ani = anchor_list.iterator();
			while(ani.hasNext()){
				AnchorNode curr_anchor = ani.next();
				if(curr_anchor.getMac() == mac[i]){
					ipos = curr_anchor.getLocation();
					break;
				}
			}
			
			//now compute inter node distance
			for(int j=0;j<mac.length;j++){
			//retrieve position of j-th anchor
			Iterator<AnchorNode> anj = anchor_list.iterator();
				while(anj.hasNext()){
					AnchorNode curr_anchor = anj.next();
					if(curr_anchor.getMac() == mac[j] && i!=j){
						D.set(j, i, ipos.distanceFrom(curr_anchor.getLocation()));
					}
				}
			}
		}
		
		return D;
	}
	
	public void setParam(LauraParam param){
		for(int i=0;i<mobile_list.size();i++){
			mobile_list.get(i).setParam(param);
		}
	}
/*
	public void updateStatus(String mote_id, Status status) {
		int ind = mote_id.indexOf(".");
		if(mote_id.charAt(ind+1) == 'M'){
			//update status
			Iterator<MobileNode> mi = mobile_list.iterator();
			while(mi.hasNext()){
				MobileNode curr_target = mi.next();
				if(curr_target.getId().equals(mote_id)){
					curr_target.setStatus(status);
					lmw_main.updatePatientStatus();
				}
			}
		}		
	}
*/	
	public void updateStatus(String mote_id, Short status) {
		int ind = mote_id.indexOf(".");
		
		if(mote_id.charAt(ind+1) == 'M'){
			//update status
			Iterator<MobileNode> mi = mobile_list.iterator();
			while(mi.hasNext()){
				MobileNode curr_target = mi.next();
				if(curr_target.getId().equals(mote_id)){
					curr_target.setStatus(Status.convert(status));
					lmw_main.updatePatientStatus();
				}
			}
		}			
		else if(mote_id.charAt(ind+1) == 'F'){	
			//update val
			Iterator<AnchorNode> mi = anchor_list.iterator();
			while(mi.hasNext()){
				AnchorNode curr_target = mi.next();
				if(curr_target.getId().equals(mote_id)){
					
					if(!curr_target.sensorListIsEmpty())
					{	
						//TODO: bisognerebbe usare l'oggetto sensore del mote...
						
						//Converte il valore di temperatura per l'mts300 dei micaz è da sistemare per gli altri
						double ADC =  (short) status;
						
				/*		Formula presa dal user guide della crossbow page 3 cap 2.2
						2.2 Conversion to Engineering Units
						The Mote’s ADC output can be converted to Kelvin using the following approximation over 0 to
						50 °C:
						    1/T(K) = a + b × ln(Rthr) + c × [ln(Rthr)]3
						    where:
						      Rthr = R1(ADC_FS-ADC)/ADC
						      a = 0.001010024
						      b = 0.000242127
						      c = 0.000000146
						      R1 = 10 kΩ
						      ADC_FS = 1023, and
						      ADC = output value from Mote’s ADC measurement
				*/
						
						final double R1 = 10000; //kOhm
						final double ADC_FS = 1023;
						final double a = 0.001010024;
						final double b = 0.000242127;
						final double c = 0.000000146;
						
						double R_thr = R1*(ADC_FS-ADC)/ADC;
						double T = 1/(a+b*Math.log(R_thr)+c*(Math.pow(Math.log(R_thr), 3)));
						
						Measure<Double,Temperature> measure=Measure.valueOf(new Double(T), SI.KELVIN);

						curr_target.setCurr_temp_val(measure);
					
					}
					
					lmw_main.updateAnchorTemperature();
				}
			}
		}
	}
	
	public void printAnchorsPosition(){
		
		DecimalFormat df = new DecimalFormat("##.###");
		
		//p_anchors.println(DateFormat.getDateTimeInstance ( DateFormat.LONG, DateFormat.LONG ).format(new Date()) );
		
		for(int i=0;i<anchor_list.size();i++){	
			//p_anchors.println(anchor_list.get(i).getMac() +" "+ df.format(anchor_list.get(i).getX()) +" "+ df.format(anchor_list.get(i).getY()) );
		}
	
	}
	
	public void printAnchorPosition(AnchorNode anchor){
		
		DecimalFormat df = new DecimalFormat("##.###");
		
		//p_anchors.println(DateFormat.getDateTimeInstance ( DateFormat.LONG, DateFormat.LONG ).format(new Date()) );		
		//p_anchors.println(anchor.getMac() +" "+ df.format(anchor.getX()) +" "+ df.format(anchor.getY()) );

	}
		
/*	
	public class StatusTask extends TimerTask {
		 
		    public void run() {
				p_netStatus.println(DateFormat.getDateTimeInstance ( DateFormat.LONG, DateFormat.LONG ).format(new Date()) );
			
				// stampo tutti le ancore ed i mobili inattivi
				for(int i=0;i<anchor_list.size();i++){	
					if(!anchor_list.get(i).isIs_reachable())
						p_netStatus.println(anchor_list.get(i).getId());
				}	
				
				for(int i=0;i<mobile_list.size();i++){
					if(!mobile_list.get(i).isIs_reachable())
						p_netStatus.println(mobile_list.get(i).getId());
				}	
				
		    }
	}
*/
}
