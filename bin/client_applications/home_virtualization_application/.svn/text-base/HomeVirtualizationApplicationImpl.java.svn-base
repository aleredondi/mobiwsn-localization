

package client_applications.home_virtualization_application;

import client_applications.home_virtualization_application.devices.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jgraph.*;
import org.jgraph.graph.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import org.jfree.data.xy.*;

import remote_interfaces.clients.dve.*;
import remote_interfaces.clients.home_virtualization_application.*;
import remote_interfaces.clients.home_virtualization_application.device.*;
import remote_interfaces.clients.profiling_system.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


import java.awt.Color;
import java.util.Hashtable;


/***
* @author Antimo Barbato
*
*/

public class HomeVirtualizationApplicationImpl extends UnicastRemoteObject implements HomeVirtualizationApplication, RoomSelectionListener {
	
	private ArrayList<String> roomsNames = new ArrayList<String>();   //Gruppi di mote che sono stati creati nell'interfaccia di gestione della rete
	private ArrayList<Integer> roomsIdentifier=new ArrayList<Integer>();
	private ArrayList<HeatingDevice> heatingDevices = new ArrayList<HeatingDevice>(); // Used to switch off all these
	private ArrayList<WiFiDevice> wifiDevices = new ArrayList<WiFiDevice>();		  // devices when nobody is at home
	private String roomImageName = null;
	private int[][] adiacencyMatrix;       //Matrice che definisce l'adiacenza tra le varie stanze( se il valore è uno in corrisopndenza dei valori X,Y allora la stanza in posizione X e la stanza in posizione Y dell'arraylist saranno adiacenti) 
	private JLabel lblRoomsNumber = new JLabel();
	private JLabel lblHouseTemperatureAvg = new JLabel();
	private JLabel lblHouseLightAvg = new JLabel();
	private JLabel lblHouseConsumeAvg = new JLabel();
	private JLabel lblHousePresence = new JLabel();
	private JLabel lblSelectedRoom = new JLabel();
	private JLabel lblRoomsNumberValue = new JLabel();
	private JLabel lblHouseTemperatureAvgValue = new JLabel();
	private JLabel lblHouseLightAvgValue = new JLabel();
	private JLabel lblHouseConsumeAvgValue = new JLabel();
	private JLabel lblHousePresenceValue = new JLabel();
	private JLabel lblSelectedRoomValue = new JLabel();
	private JLabel lblRoomName = new JLabel();
	private JLabel lblTemperatureAvg = new JLabel();
	private JLabel lblLightAvg = new JLabel();
	private JLabel lblNoiseAvg = new JLabel();
	private JLabel lblPresence = new JLabel();
	private JLabel lblPivotImg = new JLabel();
	private JLabel lblPivotImg2 = new JLabel();
	private JLabel lblConsume = new JLabel();
	private JLabel lblRoomNameValue = new JLabel();
	private JLabel lblTemperatureAvgValue = new JLabel();
	private JLabel lblLightAvgValue = new JLabel();
	private JLabel lblNoiseAvgValue = new JLabel();
	private JLabel lblPresenceValue = new JLabel();
	private JLabel lblConsumeValue = new JLabel();
	private JLabel lblDesiredTemperature = new JLabel();
	private JLabel lblDesiredLight = new JLabel();
	private JLabel lblDesiredTemperatureValue = new JLabel();
	private JLabel lblDesiredLightValue = new JLabel();
	private JButton readButton = new JButton();
	private JButton setValuesButton = new JButton();
	private JTabbedPane tabbedPanel= new JTabbedPane();
	private Room selectedRoom = null;  
	private Room addingRoom=null;
	private ArrayList<DrawRoom> drawRoomList=null;
	private long startTime;    
	private TimerTask refresh;     
	private Timer refTimer; 
	private GraphModel model = new DefaultGraphModel();
    private GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
    private JGraph graph = new JGraph(model, view);
	private int count=0;
	private JScrollPane scrollPane;
	private JPanel graphPanel = new JPanel();
	DefaultGraphCell[] cells;
	private static String images_path = "src/client_applications/home_management/images/";
	private int roomIdentifier;
    private String databaseName;
    private String databaseUserName;
    private String databaseUserPassword;
    private String databaseURL;
	private String presenceProfileName;
	private String temperatureProfileName;
	private String lightProfileName;
	private String realtimePresenceValueName;
	private String realtimeTemperatureValueName;
	private String realtimeLightValueName;
	private Calendar calendar = new GregorianCalendar();
	private String currentDate;
	private String currentDate1;
	private int samplingTime=30;
	private Hashtable<String, String > roomsGroupsAssociation=new Hashtable<String,String>();
	private int profileLength=0;
	private JFrame mainPanel;
    private JPanel panelRoom;
	//private ImagePanel panelRoom;
    private JPanel backgroundPanel;
    private JLabel lblRoomImg;
    private JLabel lblImage;
    private JLabel lblImageAnt;
    private JLabel lblImageLight;
    private JLabel lblImageTemperature;
    private JLabel labelRoomName;
    private JLabel lblSelectDevices;
    private JCheckBox heatingCB;
    private JCheckBox lightCB;
    private JCheckBox tvCB;
    private JCheckBox dvdCB;
    private JCheckBox wifiCB;
    private JButton btnOkay; 
    private JComboBox imageBox;
    private JButton btnLoadRooms=new JButton();
    private JButton btnStartManagement=new JButton();
    private JButton btnStopManagement=new JButton();
    private JButton btnTemperatureManagement=new JButton();
    private JButton btnLightManagement=new JButton();
    private JButton btnElectricityManagement=new JButton();
   // private RoundButton btnStartManagement;
    private boolean startManagementBool=false;
    private JPanel newGraphPanel;
    private Hashtable<String,ArrayList<String>> RoomsVicinity=new Hashtable<String,ArrayList<String>>();
    private Hashtable<String,ImageIcon> RoomsImage=new Hashtable<String,ImageIcon>();
  	private ImageIcon prova=new ImageIcon(images_path + "prova.png");
  	private ImageIcon lightImage;
  	private ImageIcon TemperatureImage;
    private JMenu GenericMenu;
    private JMenu HomeManagementMenu;
    private JMenuItem LoadRooms;
    private JMenuItem startHM;
    private JMenuItem stopHM;
    private JMenuItem TemperatureManagement;
    private JMenuItem LightManagement;
    private JMenuItem ElectricityManagement;
    private JMenuBar menuBar;
    private JMenuBar menuBarIcon;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private int ManagementTime=15;
    private House house=new House();
    private JPanel informationPanel = new JPanel();
    private JPanel HouseInformationPanel = new JPanel();
    private JScrollPane devicesPanel=new JScrollPane();
    private JFreeChart consume;
    private JFreeChart presence;
    private JPanel consumePanel=new JPanel();
    private JPanel presencePanel=new JPanel();
    private JButton EnterTheRoom;
    private boolean localControl=false;
    private ArrayList<RoomsType> types=new ArrayList<RoomsType>();
    private int localIndex=1;
    final int[] position=new int[1];
    private  Hashtable<DeviceType,Integer> kValue=new Hashtable<DeviceType,Integer>();
    private boolean systemStarted=false;
    private DVE dve=null;
    private ProfilingSystem profilingSystem=null;
    private boolean accessControl=true;
    private Hashtable<String,JLabel> labelsRooms=new Hashtable<String,JLabel>();
    private Hashtable<JLabel,DrawRoom> roomsLabels=new Hashtable<JLabel,DrawRoom>();
    private JLabel selectedRoomImage=new JLabel();
    private int ACCEPTEDTEMPERATUREDIFFERENCE=2;
    HomeVirtualizationApplicationImpl hva;
    private JLabel roomPanelTitle=new JLabel();
    private JLabel housePanelTitle=new JLabel();
    private JLabel roomMapTitle=new JLabel();
    private JLabel houseInfoTitle=new JLabel();
    private JLabel roomViewTitle=new JLabel();
    private JLabel roomInfoTitle=new JLabel();
    private FakeProfilers fakeProfiles;
    /** Constructor
	 * 
	 * @param moteGroupsNames - Groups of Mote created previously
	 */
	
	public HomeVirtualizationApplicationImpl(ArrayList<String> RoomsNames,Hashtable<String,String> roomsGroups,ArrayList<Integer> roomsId, String dbURL,String dbName,String dbUserName, String dbUserPassword,String presenceProfile,String temperatureProfile,String lightProfile,String realtimePresenceValue,String realtimeTemperatureValue,String realtimeLightValue,ProfilingSystem p) throws RemoteException {
				    this.roomsNames = RoomsNames;
				    this.roomsIdentifier=roomsId;
				    this.databaseURL=dbURL;
				    this.databaseName=dbName;
				    this.databaseUserName=dbUserName;
				    this.databaseUserPassword=dbUserPassword;
					this.presenceProfileName=presenceProfile;
					this.temperatureProfileName=temperatureProfile;
					this.lightProfileName=lightProfile;
					this.realtimePresenceValueName=realtimePresenceValue;
					this.realtimeTemperatureValueName=realtimeTemperatureValue;
					this.realtimeLightValueName=realtimeLightValue;
					this.roomsGroupsAssociation=roomsGroups;
					this.profileLength=(int) Math.floor((24*60*60-4)/samplingTime);
					this.profilingSystem=p;
					this.hva=hva;
					if(p!=null){
						profilingSystem.SubscribeHVA(this);
					}
					
					RoomsType t1=RoomsType.Bathroom;
					types.add(t1);
					RoomsType t2=RoomsType.Bedroom;
					types.add(t2);
					RoomsType t4=RoomsType.Closet;
					types.add(t4);
					RoomsType t5=RoomsType.Hall;
					types.add(t5);
					RoomsType t6=RoomsType.Kitchen;
					types.add(t6);
					RoomsType t7=RoomsType.Living;
					types.add(t7);
					RoomsType t8=RoomsType.Office;
					types.add(t8);
					
					RoomsImage.put("ora",new ImageIcon(images_path + "rooms/Office.png") );
					
					for(RoomsType t:types){
						//RoomsImage.put(t.toString(),new ImageIcon(images_path + "rooms/Office.png") );
						RoomsImage.put(t.toString(),new ImageIcon(images_path + "rooms/"+t.toString()+".png") );					  	
					}
					
					position[0]=0;
			

					
	}

	  
	/** This method manage the room slected in the GUI
	 *  
	 */
	
	public void start() throws RemoteException{
		
			initRooms();
	
	}
	
    public void roomSelected(DrawRoom selectedRoom){
    	readButton.setEnabled(true);
    	setValuesButton.setEnabled(true);
    	for(int i=0;i<house.getRoomsNumber();i++){
    		if(house.getRoom(i).getRoomName().equals(selectedRoom.getRoomName()))
    			this.selectedRoom=house.getRoom(i);
    	}
    	updateSelectedRoomValues();
    	updateHouseValues();
    	updateTabbedPanel();
    	selectedRoomImage.setVisible(true);
    	updateRoomImage();
    	tabbedPanel.setEnabled(true);
    //	EnterTheRoom.setEnabled(true);

    }
    
	/** This method is uesd when no room is selected in the GUI
	 *  
	 */
    
    public void roomUnselected(){
	  		lblRoomNameValue.setText("");
            lblTemperatureAvgValue.setText("");
			lblLightAvgValue.setText("");
			lblPresenceValue.setText("");
			lblConsumeValue.setText("");
			lblDesiredTemperatureValue.setText("");
			lblDesiredLightValue.setText("");
			readButton.setEnabled(false);
			setValuesButton.setEnabled(false);
			updateTabbedPanel();
			tabbedPanel.setEnabled(false);
			selectedRoom=null;
			EnterTheRoom.setEnabled(false);
			selectedRoomImage.setVisible(false);
	  	
    }
	/** This method creates the interface for the rooms creation
	 *  
	 */
	public void initRooms(){
		 
		  final JFrame newMainPanel = new JFrame("Room settings");
		  lblRoomName = new JLabel ();
		  panelRoom=new JPanel();
	      panelRoom.setBounds(350, 170, 800, 800);
	      
	      lblRoomImg = new JLabel ();
	      lblImage = new JLabel ();
	      lblImageAnt=new JLabel();
	     
	      labelRoomName = new JLabel();
	      lblSelectDevices = new JLabel();
	      heatingCB = new JCheckBox("Heating");
	      lightCB = new JCheckBox("Light");
	      tvCB = new JCheckBox("TV");
	      dvdCB = new JCheckBox("DVD");
	      wifiCB = new JCheckBox("Wifi Router");
	      btnOkay = new JButton();		//Questo bottone permette di non creare una stanza dal gruppo selezionato in quel momento 
	      imageBox = new JComboBox();
	       

	      panelRoom.setLayout(null);

   		  lblRoomName.setText("Room Name:");
      	  lblRoomName.setBounds(10,10,200,20);
      	  lblRoomName.setFont(new Font("DejaVu Sans", Font.BOLD,13));
      	  panelRoom.add(lblRoomName);
      	  
      	  for(RoomsType t:types){
      	  imageBox.addItem(t.toString());
      	  }
      	  
      	
      	  imageBox.setBounds(10, 75, 200, 20);
      	  panelRoom.add(imageBox);
      	  
      	  imageBox.addActionListener(new java.awt.event.ActionListener() {
		      public void actionPerformed(ActionEvent act) {
		    	 
		    	  for(RoomsType t:types){
				    	 if(imageBox.getSelectedItem().equals(t.toString())) {
				    		 lblImage.setIcon(RoomsImage.get(t.toString()));
				    		 roomImageName = images_path + "rooms/"+t.toString()+".png";
				    	 }  
		    	  }
		      }
      	});
      	  
      	  lblRoomImg.setText("Select Image:");
      	  lblRoomImg.setFont(new Font("DejaVu Sans", Font.BOLD,13));
      	  lblRoomImg.setBounds(10,50,200,20);
      	  panelRoom.add(lblRoomImg);
      	  
      	  lblImage.setBounds(240,70,RoomsImage.get("Living").getIconWidth()+30,RoomsImage.get("Living").getIconHeight()+30);
     // 	  lblImage.setBorder(BorderFactory.createEtchedBorder());
      	  roomImageName = images_path + "rooms/"+types.get(0).toString()+".png";
      	  lblImage.setIcon(RoomsImage.get(types.get(0).toString()));
      	  panelRoom.add(lblImage);

      	  labelRoomName.setText(roomsNames.get(0));
      	  labelRoomName.setFont(new Font("DejaVu Sans", Font.PLAIN,13));
      	  labelRoomName.setBounds(120,10,200,20);
      	  panelRoom.add(labelRoomName);
      	  roomIdentifier=roomsIdentifier.get(0);

      	
      	  lblSelectDevices.setBounds(10,430,250,20);
      	  lblSelectDevices.setFont(new Font("DejaVu Sans", Font.BOLD,13));
      	  lblSelectDevices.setText("Select the devices of this room:");
      	  panelRoom.add(lblSelectDevices);
      	  
      	  heatingCB.setBounds(20,460,200,20);
      	  heatingCB.setFont(new Font("DejaVu Sans", Font.PLAIN,13));
      	  heatingCB.setSelected(true);
      	  heatingCB.setEnabled(false);
      	 // heatingCB.setBackground(c);
      	  panelRoom.add(heatingCB);
      	  
      	  lightCB.setBounds(20,490,200,20);
      	  lightCB.setFont(new Font("DejaVu Sans", Font.PLAIN,13));
      	  lightCB.setSelected(true);
      	  lightCB.setEnabled(false);
      	//  lightCB.setBackground(c);
      	  panelRoom.add(lightCB);
      	  
      	  tvCB.setBounds(20,520,260,20);
      	  tvCB.setFont(new Font("DejaVu Sans", Font.PLAIN,13));
      	//  tvCB.setBackground(c);
      	  panelRoom.add(tvCB);
      	  
     	  dvdCB.setBounds(20,550,290,20);
     	  dvdCB.setFont(new Font("DejaVu Sans", Font.PLAIN,13));
     	//  dvdCB.setBackground(c);
      	  panelRoom.add(dvdCB);
      	  
      	  wifiCB.setBounds(20,580,320,20);
      	  wifiCB.setFont(new Font("DejaVu Sans", Font.PLAIN,13));
      	//  wifiCB.setBackground(c);
      	  panelRoom.add(wifiCB);
      	  
      	
      	  
      	  btnOkay.setText("OK");
      	  btnOkay.setName("btnOkay");
      	  btnOkay.setBounds(390,620,130,30);
      	  btnOkay.addMouseListener(new java.awt.event.MouseAdapter() {
      		public void mouseClicked(final java.awt.event.MouseEvent evt) {

      			
      			RoomsType r=types.get(0);
      			for(RoomsType t:types){
      				if(t.toString().equals(imageBox.getSelectedItem().toString()))
      					r=t;
      			}
      		  Room oneRoom = new Room(labelRoomName.getText(),roomsGroupsAssociation.get(labelRoomName.getText()),roomIdentifier,roomImageName,databaseURL,databaseName,databaseUserName,databaseUserPassword,presenceProfileName,temperatureProfileName,lightProfileName,realtimePresenceValueName,realtimeTemperatureValueName,realtimeLightValueName,samplingTime,r);
      		  
      		  if (heatingCB.isSelected())
      		  {   HeatingDevice d=null;
			try {
				d = new HeatingDevice(DeviceType.HEATING,images_path,hva);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      			  oneRoom.addDevice(d);
      			  heatingDevices.add(d);
      		  }
      		  
      		  if (lightCB.isSelected())
				try {
					oneRoom.addDevice(new LightDevice(DeviceType.LIGHT,images_path,hva));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      		  if (tvCB.isSelected())
				try {
					oneRoom.addDevice(new TVDevice(DeviceType.TV,images_path,hva));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      		  if (dvdCB.isSelected())
				try {
					oneRoom.addDevice(new DvdDevice(DeviceType.DVD,images_path,hva));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      		  if (wifiCB.isSelected())
      		  {
      			  WiFiDevice d=null;
				try {
					d = new WiFiDevice(DeviceType.WIFI,images_path,hva);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      			  oneRoom.addDevice(d);
      			  wifiDevices.add(d);
      		  }
				
      		  roomsNames.remove(0);		
      		  roomsIdentifier.remove(0);
      		  house.addRoom(oneRoom);
      		  
      		  if (!roomsNames.isEmpty()){
      			initRooms();
      			newMainPanel.dispose();
      		  }					
      		  else	{
      			adiacencyMatrix=new int[house.getRoomsNumber()][house.getRoomsNumber()];
      			newMainPanel.dispose();
      			setAdiacency(); 
      		  }
      		}
      	});
      	panelRoom.add(btnOkay);
      	ImageIcon img=new ImageIcon(images_path + "antlab.png");
      	lblImageAnt.setBounds(755,5,img.getIconWidth()+5,img.getIconWidth()+5);
      	lblImageAnt.setIcon(img);
      	lblImageAnt.setBorder(BorderFactory.createEtchedBorder());
      	panelRoom.add(lblImageAnt);
      	newMainPanel.add(panelRoom);
      	newMainPanel.setBounds(350, 170, 850, 700);
      	newMainPanel.setLocationRelativeTo(null);
      	newMainPanel.setVisible(true);

      	
	  }
	  
	
	
	
	
	/** This method creates the interface for the adiacency settings
	 * 
	 */
	 private void setAdiacency(){
		 	 
		    try{
	            
	            Class.forName("com.mysql.jdbc.Driver").newInstance(); 

	            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); 

	            ResultSet rs;
	            Statement stmt = null;
	            conn.setCatalog(databaseName);
	            stmt = conn.createStatement(); 

	            Room room1=house.getRoom(1);
	            Room room2=house.getRoom(1);
	            
      			for(int i=0;i<house.getRoomsNumber();i++){
      				ArrayList<String> AdiacRooms=new ArrayList<String>();
        	    	  for(int j=0;j<house.getRoomsNumber();j++){
        		            for(int k=0;k<house.getRoomsNumber();k++){
        		            	if(house.getRoom(k).getRoomIdentifier()==(i+1))
        		            		room1=house.getRoom(k);
        		            }
        		            for(int k=0;k<house.getRoomsNumber();k++){
        		            	if(house.getRoom(k).getRoomIdentifier()==(j+1))
        		            		room2=house.getRoom(k);
        		            }
        	    		  rs=stmt.executeQuery("SELECT value FROM roomsVicinityMatrix WHERE roomName='"+room1.getRoomName()+"' AND houseRoomsName='"+room2.getRoomName()+"';");
        	    		  if(rs.next())
        	    			  adiacencyMatrix[i][j] = rs.getInt("value");
        	    		  if(adiacencyMatrix[i][j]==1){
        	    			  AdiacRooms.add(room2.getRoomName());
        	    		  }
        	    	  }
        	    	  RoomsVicinity.put(room1.getRoomName(), AdiacRooms);

      			}

	            stmt.close(); 
	            conn.close(); // close the connection 
	        }
	        
	        catch (Exception ex) {
	            System.out.println("SQLException: " + ex.getMessage());
	        }
	        showManagementFrame();
	 }
	 
	 
		/** This method creates the GUI
		 *  
		 */
	 
  private void showManagementFrame()
		 {
		  
		  
		  mainPanel = new JFrame("Home Virtualization Application");
	      mainPanel.setBounds(0, 0, 1280,1000);
	      mainPanel.setLayout(null);
	     
	      // Antlab image
          lblImageAnt=new JLabel();
          ImageIcon img=new ImageIcon(images_path + "antlab1.png");
          lblImageAnt.setBounds(1180,10,img.getIconWidth()+5,img.getIconWidth()+5);
          lblImageAnt.setIcon(img);
          lblImageAnt.setBorder(BorderFactory.createEtchedBorder());
          mainPanel.add(lblImageAnt);
	      
	      
		// Upper Menu
	      menuBar=new JMenuBar();
	      GenericMenu=new JMenu("Generic");
	      LoadRooms=new JMenuItem("Load House Rooms",new ImageIcon("src/client_applications/home_management/images/House2.png"));
	      LoadRooms.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_1, ActionEvent.ALT_MASK));

	      // Load the house rooms
	      LoadRooms.addActionListener( new java.awt.event.ActionListener(){
	            public void actionPerformed(ActionEvent e) {
	            	
	                updateRoomsGraph();
	                btnLoadRooms.setEnabled(false);
	                btnStartManagement.setEnabled(true);
	                startHM.setEnabled(true);
	                TemperatureManagement.setEnabled(true);
	                LoadRooms.setEnabled(false);
	                btnTemperatureManagement.setEnabled(true);
	                btnElectricityManagement.setEnabled(true);
	                btnLightManagement.setEnabled(true);
	                LightManagement.setEnabled(true);
	                ElectricityManagement.setEnabled(true);
	                
	            }
	        });
	      GenericMenu.add(LoadRooms);
	      
	      // Used to start the monitoring and managemnet of the house trough the sensor networks
	    
	      startHM=new JMenuItem("Start Home Management",new ImageIcon("src/client_applications/home_management/images/on2.png"));
	      startHM.setEnabled(false);
	      startHM.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_2, ActionEvent.ALT_MASK));

	      startHM.addActionListener( new java.awt.event.ActionListener(){
	            public void actionPerformed(ActionEvent e) {
		                startManagement();
		            	stopHM.setEnabled(true);
		            	startHM.setEnabled(false); 
		                btnStartManagement.setEnabled(false);
		                startManagementBool=true;
		                btnStopManagement.setEnabled(true);
		                updateHouseValues();
		                systemStarted=true;
		               // da commentare dopo
		    			ArrayList<String> roomsN=new ArrayList<String>();
						
						for(Room r:house.getRooms())
							roomsN.add(r.getRoomName());
		                fakeProfiles =new FakeProfilers(roomsN.size(),roomsN);
		                
		                HouseInformationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 3), BorderFactory.createLineBorder(Color.GREEN, 2)));
						selectedRoomImage.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 3), BorderFactory.createLineBorder(Color.GREEN, 2))); 
		                ((MapPanel)newGraphPanel).isDispose(true);
			               newGraphPanel.removeAll();
			               newGraphPanel.setVisible(false);
			               
			                mainPanel.remove((MapPanel)newGraphPanel);
			                JPanel f=new JPanel();
			                f.setLayout(null);
			                f.setBackground(Color.white);
			                f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 2), BorderFactory.createLineBorder(Color.GREEN,3)));

			                for(DrawRoom dr:drawRoomList){
			                final 	JLabel ll=new JLabel();
			                	JLabel ltext=new JLabel();
			                	ltext.setText(dr.getRoomName());
			                	ImageIcon imm=null;
								if(dr.getPresence()>0){
									ll.removeAll();
									imm=new ImageIcon(dr.getRoomImageOn());
									ll.setIcon(imm);
								}else{
									ll.removeAll();
									System.out.println("Sono qui");
									imm=new ImageIcon(dr.getRoomImageOff());
									ll.setIcon(imm);
								}
			                	
			                	ll.setBounds(dr.getXCoordinate(),dr.getYCoordinate(),150,112);
			                	ltext.setFont(new Font("Serif",Font.PLAIN,12));
			                	ltext.setBounds( dr.getXCoordinate()+55, 
			                			dr.getYCoordinate()-imm.getIconHeight()/4+10,100,20);
			                	f.add(ltext);
			                	f.add(ll);
			                	labelsRooms.put(dr.getRoomName(),ll);
			                	roomsLabels.put(ll,dr);
			                	ll.addMouseListener( new java.awt.event.MouseListener(){
			          	            public void mousePressed(MouseEvent arg0) {
			              				roomSelected(roomsLabels.get(ll));
			              				
			          	            }
			          	            public void mouseEntered(MouseEvent arg0) {
		          	            }
			          	            public void mouseReleased(MouseEvent arg0) {
		          	            }
			          	            public void mouseClicked(MouseEvent arg0) {
		          	            }
			          	            public void mouseExited(MouseEvent arg0) {
		          	            }
			          	        });
			                }
				                f.setBounds(5, 130, 680, 500);
				                f.setVisible(true);
			                
			                mainPanel.add(f);
	            }
	        });
	      GenericMenu.add(startHM);
	      
	      
 //	    Used to stop the monitoring and managemnet of the house trough the sensor networks
	    
	      stopHM=new JMenuItem("Stop Home Management",new ImageIcon("src/client_applications/home_management/images/off2.png"));
	      stopHM.setEnabled(false);
	      stopHM.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_3, ActionEvent.ALT_MASK));

	      stopHM.addActionListener( new java.awt.event.ActionListener(){
	            public void actionPerformed(ActionEvent e) {
	            	stopHM.setEnabled(false);
	            	startHM.setEnabled(true); 
	                btnStartManagement.setEnabled(true);
	                btnStopManagement.setEnabled(false);
	                startManagementBool=false;
	                systemStarted=false;
	            }
	        });
	      GenericMenu.add(stopHM);
	      
	      HomeManagementMenu=new JMenu("Home Management");

//	    Used to manage the heating/cooling system of the house trough the sensor networks
	    
	      TemperatureManagement=new JMenuItem("Home Temperature Management",new ImageIcon("src/client_applications/home_management/images/Temperature2.png"));
	      TemperatureManagement.setEnabled(false);
	      TemperatureManagement.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_4, ActionEvent.ALT_MASK));
	      TemperatureManagement.addActionListener( new java.awt.event.ActionListener(){
	            public void actionPerformed(ActionEvent e) {
	            	startTemperatureManagement();
	            }
	        });
	      HomeManagementMenu.add(TemperatureManagement);
	      
//	    Used to manage the lighting system of the house trough the sensor networks
	      
	      LightManagement=new JMenuItem("Home Light Management",new ImageIcon("src/client_applications/home_management/images/Light2.png"));
	      LightManagement.setEnabled(false);
	      LightManagement.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_5, ActionEvent.ALT_MASK));
	      LightManagement.addActionListener( new java.awt.event.ActionListener(){
	            public void actionPerformed(ActionEvent e) {
	            	startLightManagement();
	            }
	        });
	      HomeManagementMenu.add(LightManagement);
	      
//	    Used to manage the electricity of the house 
	      
	      ElectricityManagement=new JMenuItem("Home Electricity Management",new ImageIcon("src/client_applications/home_management/images/Elect2.png"));
	      ElectricityManagement.setEnabled(false);
	      ElectricityManagement.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_6, ActionEvent.ALT_MASK));
	      ElectricityManagement.addActionListener( new java.awt.event.ActionListener(){
	            public void actionPerformed(ActionEvent e) {

	            }
	        });
	      HomeManagementMenu.add(ElectricityManagement);
	      
	      menuBar.add(GenericMenu);
	      menuBar.add(HomeManagementMenu);
	      mainPanel.add(menuBar);
	      mainPanel.setJMenuBar(menuBar);
	      jSeparator1 = new javax.swing.JSeparator();
	      jSeparator1.setName("jSeparator1"); 
	      jSeparator1.setBounds(0, 45, 1160, 5);
	      mainPanel.add(jSeparator1);

	      // Icon associated to the Upper Menu
	      
	      btnLoadRooms=new JButton(new ImageIcon("src/client_applications/home_management/images/House.png"));
	      btnLoadRooms.setEnabled(true);
	      btnLoadRooms.setName("btnLoadRooms"); 
	      btnLoadRooms.setBorder(BorderFactory.createEmptyBorder());
	      btnLoadRooms.setBackground(mainPanel.getBackground());
	      btnLoadRooms.setBounds(3, 3, 40,40);
	      btnLoadRooms.addMouseListener(new java.awt.event.MouseAdapter() {
	            public void mouseClicked(final java.awt.event.MouseEvent evt) {
	                updateRoomsGraph();
	                LoadRooms.setEnabled(false);
	                startHM.setEnabled(true);
	                btnLoadRooms.setEnabled(false);
	                btnStartManagement.setEnabled(true);
	                btnTemperatureManagement.setEnabled(true);
	                btnElectricityManagement.setEnabled(true);
	                btnLightManagement.setEnabled(true);
	                TemperatureManagement.setEnabled(true);
	                LightManagement.setEnabled(true);
	                ElectricityManagement.setEnabled(true);
	            }
	        });
	       mainPanel.add(btnLoadRooms);
		  
		   btnStartManagement=new JButton(new ImageIcon("src/client_applications/home_management/images/on.png"));
		   btnStartManagement.setEnabled(false);
		   btnStartManagement.setName("btnStartManagement");
		   btnStartManagement.setBorder(BorderFactory.createEmptyBorder());
		   btnStartManagement.setBounds(45, 3, 40, 40);
		   btnStartManagement.addMouseListener(new java.awt.event.MouseAdapter() {
		            public void mouseClicked(final java.awt.event.MouseEvent evt) {
		                
		            	startManagement();
		            	stopHM.setEnabled(true);
		            	startHM.setEnabled(false); 
		                btnStartManagement.setEnabled(false);
		                startManagementBool=true;
		                btnStopManagement.setEnabled(true);
		                updateHouseValues();
		                systemStarted=true;

			               // da commentare dopo
		    			ArrayList<String> roomsN=new ArrayList<String>();
						
						for(Room r:house.getRooms())
							roomsN.add(r.getRoomName());
		                fakeProfiles =new FakeProfilers(roomsN.size(),roomsN);
		                
		                HouseInformationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 3), BorderFactory.createLineBorder(Color.GREEN, 2)));
						selectedRoomImage.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 3), BorderFactory.createLineBorder(Color.GREEN, 2)));  
		                
		                ((MapPanel)newGraphPanel).isDispose(true);
			               newGraphPanel.removeAll();
			               newGraphPanel.setVisible(false);
			               
			                mainPanel.remove((MapPanel)newGraphPanel);
			                JPanel f=new JPanel();
			                f.setLayout(null);
			                f.setBackground(Color.white);
			                f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 3), BorderFactory.createLineBorder(Color.GREEN,2)));

			                for(DrawRoom dr:drawRoomList){
			                final 	JLabel ll=new JLabel();
			                	JLabel ltext=new JLabel();
			                	ltext.setText(dr.getRoomName());
			                	ImageIcon imm=null;
								if(dr.getPresence()>0){
									ll.removeAll();
									imm=new ImageIcon(dr.getRoomImageOn());
									ll.setIcon(imm);
								}else{
									ll.removeAll();
									System.out.println("Sono qui");
									imm=new ImageIcon(dr.getRoomImageOff());
									ll.setIcon(imm);
								}
			                	
			                	ll.setBounds(dr.getXCoordinate(), dr.getYCoordinate(),150,112);
			                	ltext.setFont(new Font("Serif",Font.PLAIN,12));
			                	ltext.setBounds( dr.getXCoordinate()+55, 
			                			dr.getYCoordinate()-imm.getIconHeight()/4+10,100,20);
			                	f.add(ltext);
			                	f.add(ll);
			                	labelsRooms.put(dr.getRoomName(),ll);
			                	roomsLabels.put(ll,dr);
			                	ll.addMouseListener( new java.awt.event.MouseListener(){
			          	            public void mousePressed(MouseEvent arg0) {
			              				roomSelected(roomsLabels.get(ll));
			              				
			          	            }
			          	            public void mouseEntered(MouseEvent arg0) {
		          	            }
			          	            public void mouseReleased(MouseEvent arg0) {
		          	            }
			          	            public void mouseClicked(MouseEvent arg0) {
		          	            }
			          	            public void mouseExited(MouseEvent arg0) {
		          	            }
			          	        });
			                }
				                f.setBounds(5, 130, 680, 500);
				                f.setVisible(true);
			                
			                mainPanel.add(f);
		            }
		        });
		  mainPanel.add(btnStartManagement); 
		  
		   btnStopManagement=new JButton(new ImageIcon("src/client_applications/home_management/images/off.png"));
		   btnStopManagement.setEnabled(false);
		   btnStopManagement.setName("btnStopManagement"); 
		   btnStopManagement.setBorder(BorderFactory.createEmptyBorder());
		   btnStopManagement.setBounds(87, 3, 40, 40);
		   btnStopManagement.addMouseListener(new java.awt.event.MouseAdapter() {
		            public void mouseClicked(final java.awt.event.MouseEvent evt) {
		            	stopHM.setEnabled(false);
		            	startHM.setEnabled(true); 
		                btnStartManagement.setEnabled(true);
		                btnStopManagement.setEnabled(false);
		                startManagementBool=false;
		                systemStarted=false;

		            }
		        });
		  mainPanel.add(btnStopManagement); 
		  
		   btnTemperatureManagement=new JButton(new ImageIcon("src/client_applications/home_management/images/Temperature.png"));
		   btnTemperatureManagement.setEnabled(false);
		   btnTemperatureManagement.setName("btnTemperatureManagement");
		   btnTemperatureManagement.setBorder(BorderFactory.createEmptyBorder());
		   btnTemperatureManagement.setBounds(129, 3, 40, 40);
		   btnTemperatureManagement.addMouseListener(new java.awt.event.MouseAdapter() {
		            public void mouseClicked(final java.awt.event.MouseEvent evt) {
		            	startTemperatureManagement();
		            }
		        });
		  mainPanel.add(btnTemperatureManagement); 
		  
		   btnLightManagement=new JButton(new ImageIcon("src/client_applications/home_management/images/Light.png"));
		   btnLightManagement.setEnabled(false);
		   btnLightManagement.setName("btnLightManagement"); 
		   btnLightManagement.setBorder(BorderFactory.createEmptyBorder());
		   btnLightManagement.setBounds(171, 3, 40, 40);
		   btnLightManagement.addMouseListener(new java.awt.event.MouseAdapter() {
		            public void mouseClicked(final java.awt.event.MouseEvent evt) {
		            	startLightManagement();
		            }
		        });
		  mainPanel.add(btnLightManagement); 
		  
		   btnElectricityManagement=new JButton(new ImageIcon("src/client_applications/home_management/images/Elect.png"));
		   btnElectricityManagement.setEnabled(false);
		   btnElectricityManagement.setName("btnElectricityManagement"); 
		   btnElectricityManagement.setBorder(BorderFactory.createEmptyBorder());
		   btnElectricityManagement.setBounds(213, 3, 40, 40);
		   btnElectricityManagement.addMouseListener(new java.awt.event.MouseAdapter() {
		            public void mouseClicked(final java.awt.event.MouseEvent evt) {

		            }
		        });
		   mainPanel.add(btnElectricityManagement); 
	      
	      
	      // Panel Used to represent the House Information
	     
		   newGraphPanel=new MapPanel();
	  	  drawRoomList=new ArrayList<DrawRoom>();
	  	  ((MapPanel)newGraphPanel).addRoomSelectionListener(this);
	  	  newGraphPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
	  	  newGraphPanel.setName("HouseRoomsGraph");
	      org.jdesktop.layout.GroupLayout newGraphPanelLayout = new org.jdesktop.layout.GroupLayout(newGraphPanel);
	      newGraphPanel.setLayout(newGraphPanelLayout);
	      newGraphPanelLayout.setHorizontalGroup(
	          newGraphPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	              .add(0, 666, Short.MAX_VALUE)
	          );
	          newGraphPanelLayout.setVerticalGroup(
	              newGraphPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	              .add(0, 416, Short.MAX_VALUE)
	          );
	      newGraphPanel.setBounds(5, 130, 680, 500);
	      mainPanel.add(newGraphPanel);


          // Sepearator
	      jSeparator2 = new javax.swing.JSeparator();
	      jSeparator2.setName("jSeparator2"); 
	      jSeparator2.setOrientation(SwingConstants.VERTICAL);
	      jSeparator2.setBounds(700, 55, 50, 860);
	      mainPanel.add(jSeparator2);
	      
	      // Panel Used to represent the selected room Information
          
	      HouseInformationPanel.setBounds(5, 690, 680, 180);
	      HouseInformationPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
	      HouseInformationPanel.setLayout(null);
	     // HouseInformationPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
	      HouseInformationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 3), BorderFactory.createLineBorder(Color.red, 2)));
	      mainPanel.add(HouseInformationPanel);
	      
         
	      lblRoomsNumber.setText("Rooms Number :");
	      lblRoomsNumber.setFont(new Font("Serif", Font.BOLD,15));
	      lblRoomsNumber.setBounds(60, 30, 200, 20);
	      HouseInformationPanel.add(lblRoomsNumber);
		  	
	      lblHouseTemperatureAvg.setText("Average Temperature :");
	      lblHouseTemperatureAvg.setFont(new Font("Serif", Font.BOLD,15));
	      lblHouseTemperatureAvg.setBounds(60, 79, 200, 20);
	      HouseInformationPanel.add(lblHouseTemperatureAvg);
		  	
	      lblHouseLightAvg.setText("Average Light :");
	      lblHouseLightAvg.setFont(new Font("Serif", Font.BOLD,15));
	      lblHouseLightAvg.setBounds(390, 79, 200, 20);
	      HouseInformationPanel.add (lblHouseLightAvg);
		  	
	      lblHousePresence.setText("Presence :");
	      lblHousePresence.setFont(new Font("Serif", Font.BOLD,15));
	      lblHousePresence.setBounds(60, 128, 200, 20);
	      HouseInformationPanel.add(lblHousePresence);
		  	
	      lblHouseConsumeAvg.setText("Consumption :");
	      lblHouseConsumeAvg.setFont(new Font("Serif", Font.BOLD,15));
	      lblHouseConsumeAvg.setBounds(390, 128, 200, 20);
	      HouseInformationPanel.add(lblHouseConsumeAvg);
	      
	      lblSelectedRoom.setText("Selected Room :");
	      lblSelectedRoom.setFont(new Font("Serif", Font.BOLD,15));
	      lblSelectedRoom.setBounds(390, 30, 200, 20);
	      HouseInformationPanel.add(lblSelectedRoom);
		  	
	      lblRoomsNumberValue.setText("");
	      lblRoomsNumberValue.setFont(new Font("Serif", Font.BOLD,15));
	      lblRoomsNumberValue.setBounds(200, 30, 200, 20);
	      HouseInformationPanel.add(lblRoomsNumberValue);
		  	
	      lblHouseTemperatureAvgValue.setText("");
	      lblHouseTemperatureAvgValue.setFont(new Font("Serif", Font.BOLD,15));
	      lblHouseTemperatureAvgValue.setBounds(240, 79, 200, 20);
	      HouseInformationPanel.add(lblHouseTemperatureAvgValue); 
		  	
	      lblHouseLightAvgValue.setText("");
	      lblHouseLightAvgValue.setFont(new Font("Serif", Font.BOLD,15));
	      lblHouseLightAvgValue.setBounds(520, 79, 200, 20);
	      HouseInformationPanel.add(lblHouseLightAvgValue);
		  	
		  lblPivotImg2.setIcon(new ImageIcon(images_path + "pivot.png"));
		  lblPivotImg2.setBounds(150, 122, 15, 30);
		  HouseInformationPanel.add(lblPivotImg2);
	      
	      lblHousePresenceValue.setText("");
	      lblHousePresenceValue.setFont(new Font("Serif", Font.BOLD,15));
	      lblHousePresenceValue.setBounds(180, 128, 200, 20);
	      HouseInformationPanel.add(lblHousePresenceValue);
			
	      lblHouseConsumeAvgValue.setText("");
	      lblHouseConsumeAvgValue.setFont(new Font("Serif", Font.BOLD,15));
	      lblHouseConsumeAvgValue.setBounds(520, 128, 200, 20);
	      HouseInformationPanel.add(lblHouseConsumeAvgValue); 

	      lblSelectedRoomValue.setText("");
	      lblSelectedRoomValue.setFont(new Font("Serif", Font.BOLD,15));
	      lblSelectedRoomValue.setBounds(520, 30, 200, 20);
	      HouseInformationPanel.add(lblSelectedRoomValue); 
	      
	      /*
	      EnterTheRoom=new JButton("Enter the selected Room");
	      EnterTheRoom.setEnabled(false);
	      EnterTheRoom.setName("EnterTheRoom"); 
	      EnterTheRoom.setBackground(mainPanel.getBackground());
	      EnterTheRoom.setBounds(420, 249, 200, 30);
	      EnterTheRoom.addMouseListener(new java.awt.event.MouseAdapter() {
	            public void mouseClicked(final java.awt.event.MouseEvent evt) {

	            }
	        });
	      HouseInformationPanel.add(EnterTheRoom);*/
	   
	      
	      tabbedPanel.setFont(new Font("Serif", Font.BOLD,13));
	      tabbedPanel.setBounds(720, 500, 520, 400);
	      
	      
	      XYSeries ConsumeSeries = new XYSeries("Consume");
	      for(int i=0;i<profileLength;i++){
	    	  ConsumeSeries.add(i,0);
	      }
	      XYDataset consumeDataset = new XYSeriesCollection(ConsumeSeries);
	      JFreeChart chart1 = ChartFactory.createXYLineChart ("Room Consume", "Consume", "Time",consumeDataset, PlotOrientation.VERTICAL, true, true, false);
	      ChartPanel chart2=new ChartPanel(chart1);
	      consumePanel.add(chart2);
	      
	      XYSeries PresenceSeries = new XYSeries("Presence");
	      for(int i=0;i<profileLength;i++){
	    	  PresenceSeries.add(i,0);
	      }
	      XYDataset presenceDataset = new XYSeriesCollection(PresenceSeries);
	      JFreeChart chart3 = ChartFactory.createXYLineChart ("Room Presence", "Presence Value", "Time",presenceDataset, PlotOrientation.VERTICAL, true, true, false);
	      ChartPanel chart4=new ChartPanel(chart3);
	      presencePanel.add(chart4);
	      
	      

         
	      lblRoomName.setText("Room Name:");
	      lblRoomName.setFont(new Font("Serif", Font.BOLD,13));
	      lblRoomName.setBounds(10, 20, 100, 20);
	      informationPanel.add(lblRoomName);
		  	
	      lblTemperatureAvg.setText("Temperature:");
	      lblTemperatureAvg.setFont(new Font("Serif", Font.BOLD,13));
	      lblTemperatureAvg.setBounds(10, 75, 100, 20);
	      informationPanel.add(lblTemperatureAvg);
		  	
	      lblLightAvg.setText("Light:");
	      lblLightAvg.setFont(new Font("Serif", Font.BOLD,13));
	      lblLightAvg.setBounds(10, 130, 100, 20);
	      informationPanel.add(lblLightAvg);
		  	
	      lblPresence.setText("Presence:");
	      lblPresence.setFont(new Font("Serif", Font.BOLD,13));
	      lblPresence.setBounds(10, 185, 100, 20);
	      informationPanel.add(lblPresence);
		  	
		  lblPivotImg.setIcon(new ImageIcon(images_path + "pivot.png"));
		  lblPivotImg.setBounds(130, 178, 15, 30);
		  informationPanel.add(lblPivotImg);
		  	
	      lblConsume.setText("Consumption:");
	      lblConsume.setFont(new Font("Serif", Font.BOLD,13));
	      lblConsume.setBounds(10, 240, 100, 20);
	      informationPanel.add(lblConsume);
		  	
	      lblRoomNameValue.setText("");
	      lblRoomNameValue.setFont(new Font("Serif", Font.BOLD,13));
	      lblRoomNameValue.setBounds(130, 20, 100, 20);
	      informationPanel.add(lblRoomNameValue);
		  	
	      lblTemperatureAvgValue.setText("");
	      lblTemperatureAvgValue.setFont(new Font("Serif", Font.BOLD,13));
	      lblTemperatureAvgValue.setBounds(130, 75, 100, 20);
	      informationPanel.add(lblTemperatureAvgValue); 
		  	
	      lblLightAvgValue.setText("");
	      lblLightAvgValue.setFont(new Font("Serif", Font.BOLD,13));
	      lblLightAvgValue.setBounds(130, 130, 100, 20);
	      informationPanel.add(lblLightAvgValue);

		  	
	      lblPresenceValue.setText("");
	      lblPresenceValue.setFont(new Font("Serif", Font.BOLD,13));
	      lblPresenceValue.setBounds(150, 185, 100, 20);
	      informationPanel.add(lblPresenceValue);
			
	      lblConsumeValue.setText("");
	      lblConsumeValue.setFont(new Font("Serif", Font.BOLD,13));
	      lblConsumeValue.setBounds(130, 240, 100, 20);
	      informationPanel.add(lblConsumeValue); 
			
	      lblDesiredTemperature.setText("Desired temp.:");
	      lblDesiredTemperature.setFont(new Font("Serif", Font.BOLD,13));
	      lblDesiredTemperature.setBounds(250, 75, 120, 20);
	      informationPanel.add(lblDesiredTemperature); 
			
	      lblDesiredLight.setText("Desired light:");
	      lblDesiredLight.setFont(new Font("Serif", Font.BOLD,13));
	      lblDesiredLight.setBounds(250, 130, 100, 20);
	      informationPanel.add(lblDesiredLight); 
			
	      lblDesiredTemperatureValue.setText("");
	      lblDesiredTemperatureValue.setFont(new Font("Serif", Font.BOLD,13));
	      lblDesiredTemperatureValue.setBounds(370, 75, 100, 20);
	      informationPanel.add(lblDesiredTemperatureValue); 
			
	      lblDesiredLightValue.setText("");
	      lblDesiredLightValue.setFont(new Font("Serif", Font.BOLD,13));
	      lblDesiredLightValue.setBounds(370, 130, 100, 20);
	      informationPanel.add(lblDesiredLightValue); 

		  
	      readButton.setEnabled(false);
	      readButton.setBounds(40, 300, 130, 25);
	      readButton.setText("Read values");
	      readButton.setName("readButton");
	      readButton.addMouseListener(new java.awt.event.MouseAdapter() {
	    	  public void mouseClicked(java.awt.event.MouseEvent evt) {
           	
           	try {
					selectedRoom.readRoomSensors();
				} catch (RemoteException e) {
					e.printStackTrace();
				}			
				
           }
			});
			informationPanel.add(readButton);
		  	
			setValuesButton.setEnabled(false);
			setValuesButton.setBounds(340, 300, 130, 25);
			setValuesButton.setText("Set values");
			setValuesButton.setName("setValuesButton");
			setValuesButton.addMouseListener(new java.awt.event.MouseAdapter() {
           public void mouseClicked(java.awt.event.MouseEvent evt) {
           	        	
           	final int TEMPERATURE_MIN = 0;
           	final int TEMPERATURE_MAX = 30;
           	final int TEMPERATURE_INIT = (int) (selectedRoom.getDesiredTemperature()*10);
           	final int LIGHT_MIN = 0;
           	final int LIGHT_MAX = 500;
           	final int LIGHT_INIT = (int) (selectedRoom.getDesiredLight()*10); 
           	final JFrame settingsFrame = new JFrame("Set desired values");
           	JLabel lblTitle = new JLabel("Set the desired values");
           	JLabel lblLight = new JLabel("Light");
           	JLabel lblTemperature = new JLabel("Temperature");
           	final JLabel lblLightDesiredValue = new JLabel();
           	final JLabel lblTemperatureDesiredValue = new JLabel();
           	JButton okayButton = new JButton("Update");
           	final JSlider setLight = new JSlider(JSlider.HORIZONTAL,LIGHT_MIN*10,LIGHT_MAX*10,LIGHT_INIT);
           	final JSlider setTemperature = new JSlider(JSlider.HORIZONTAL,TEMPERATURE_MIN*10,TEMPERATURE_MAX*10,TEMPERATURE_INIT);
           	//final JSlider setTemperature = new JSlider(JSlider.HORIZONTAL,LIGHT_MIN*10,LIGHT_MAX*10,LIGHT_INIT);
           
           	settingsFrame.setBounds(350, 350, 300, 350);
           	settingsFrame.setVisible(true);
           	settingsFrame.setLayout(null);
           	
           	lblTitle.setBounds(60, 10, 300, 30);
           	lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
           	lblTitle.setFont(new Font("DejaVu Sans",Font.BOLD,15));
           	settingsFrame.add(lblTitle);
           
           	lblTemperature.setBounds(10,40,250,20);
           	lblTemperature.setFont(new Font("DejaVu Sans",Font.BOLD,13));
           	settingsFrame.add(lblTemperature);
           	
           	lblLight.setBounds(10,160,250,20);
           	lblLight.setFont(new Font("DejaVu Sans",Font.BOLD,13));
           	settingsFrame.add(lblLight);
           	
           	lblTemperatureDesiredValue.setBounds(200,40,250,20);
           	lblTemperatureDesiredValue.setFont(new Font("DejaVu Sans",Font.BOLD,13));
           	lblTemperatureDesiredValue.setText(String.valueOf(selectedRoom.getDesiredTemperature())+" °C");
           	settingsFrame.add(lblTemperatureDesiredValue);
           	
           	lblLightDesiredValue.setBounds(200,160,250,20);
           	lblLightDesiredValue.setFont(new Font("DejaVu Sans",Font.BOLD,13));
           	lblLightDesiredValue.setText(String.valueOf(selectedRoom.getDesiredLight())+" lux");
           	settingsFrame.add(lblLightDesiredValue);
           	
           	setLight.setMajorTickSpacing(1000);
               setLight.setMinorTickSpacing(200);
               setLight.setPaintTicks(true);
               setLight.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
               
               setLight.setFont(new Font("DejaVu Sans",Font.PLAIN,13));
               Hashtable<Integer,JLabel> labelTableLight = new Hashtable<Integer,JLabel>();
               labelTableLight.put(new Integer(LIGHT_MIN*10), new JLabel(String.valueOf(LIGHT_MIN)));
               labelTableLight.put(new Integer(LIGHT_MAX*10), new JLabel(String.valueOf(LIGHT_MAX)));
               labelTableLight.put(new Integer(LIGHT_MAX*10/2), new JLabel(String.valueOf(LIGHT_MAX/2)));
              
               setLight.setLabelTable(labelTableLight);
               setLight.setPaintLabels(true);
               setLight.setBounds(10,180,250,80);
               setLight.addChangeListener(new ChangeListener()
               {
                   public void stateChanged (ChangeEvent event)
                   {
                       JSlider slider=(JSlider)event.getSource();
                       double temp = slider.getValue();
                       temp=temp/10;
                       lblLightDesiredValue.setText(String.valueOf(temp)+" lux");
                  
                   }
               });
               settingsFrame.add(setLight);
               
               
               setTemperature.setMajorTickSpacing(50);
               setTemperature.setMinorTickSpacing(10);
               setTemperature.setPaintTicks(true);
               setTemperature.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
               setTemperature.setFont(new Font("DejaVu Sans",Font.PLAIN,13));
               
               Hashtable<Integer,JLabel> labelTableTemperature = new Hashtable<Integer,JLabel>();
               
               labelTableTemperature.put(new Integer(TEMPERATURE_MIN*10), new JLabel(String.valueOf(TEMPERATURE_MIN)));
               labelTableTemperature.put(new Integer(TEMPERATURE_MAX*10), new JLabel(String.valueOf(TEMPERATURE_MAX)));
	            labelTableTemperature.put(new Integer(TEMPERATURE_MAX*10/2),new JLabel(String.valueOf(TEMPERATURE_MAX/2)));
	            
	            setTemperature.setLabelTable(labelTableTemperature);
	            setTemperature.setPaintLabels(true);
               setTemperature.setBounds(10,60,250,80);
               setTemperature.addChangeListener(new ChangeListener()
               {
                   public void stateChanged (ChangeEvent event)
                   {
                       JSlider slider=(JSlider)event.getSource();
                       double temp = slider.getValue();
                       temp=temp/10;
                       lblTemperatureDesiredValue.setText(String.valueOf(temp)+" °C");
                  
                   }
               });
               settingsFrame.add(setTemperature);
               
               
           	okayButton.setBounds(75,260,150,30);
           	okayButton.addMouseListener(new java.awt.event.MouseAdapter() {
                   public void mouseClicked(java.awt.event.MouseEvent evt) {
                   	double temp;

	                	//boolean found = false;
                   	
                   	temp = setLight.getValue();
                   	temp = temp/10;
                   	selectedRoom.setDesiredLight(temp);
			try {
				profilingSystem.notifyNewLightDesiredValue(temp,selectedRoom.getRoomName());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                   	System.out.println(temp);
                   	temp = setTemperature.getValue();
                   	temp = temp/10;
                   	selectedRoom.setDesiredTemperature(temp);
			try {
				profilingSystem.notifyNewTemperatureDesiredValue(temp,selectedRoom.getRoomName());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                   	System.out.println(selectedRoom.getDesiredTemperature());
                   	System.out.println(selectedRoom.getDesiredLight());
    				if (selectedRoom!=null){
    					updateSelectedRoomValues();
    					updateRoomImage();
    					updateDevicePanel();
    					updatePresenceGraphicPanel();
    					updateConsumeGraphicPanel();
    					DynamicUpdateTabbedPanel();
    				}
                   	
                   	/*                    	
                   	for(Room r: rooms)
   			  		{
                   		
   			  			if(!r.getPresenceValues().isEmpty())
   			  				if(r.getPresenceValues().get(r.getPresenceValues().lastKey())>0)
   			  					found = true;
   			  							
   			  		}
                   	selectedRoom.setConsume(found);
						*/

                   	updateSelectedRoomValues();
                   	updateRoomImage();
                   	updateTabbedPanel();
                   	settingsFrame.dispose();
                   }
           	});
           	settingsFrame.add(okayButton);
        
           }
			});
			
			informationPanel.add(setValuesButton);
         
	      informationPanel.setLayout(null);
	      tabbedPanel.add("Information",informationPanel);
	      tabbedPanel.addTab("Room Devices", devicesPanel);
	      tabbedPanel.addTab("Consumption Panel", consumePanel);
	      tabbedPanel.addTab("Presence Panel", presencePanel);
	      mainPanel.add(tabbedPanel);

	      


           
           selectedRoomImage.setBounds(727, 130,510,304);
           selectedRoomImage.setVisible(true);
        //   selectedRoomImage.setBorder(BorderFactory.createEtchedBorder());
           selectedRoomImage.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 3), BorderFactory.createLineBorder(Color.red, 2)));
           mainPanel.add(selectedRoomImage);
	      
           
           roomPanelTitle.setBounds(910, 20,200,100);
           roomPanelTitle.setText("Room Panel");
           roomPanelTitle.setFont(new Font("Serif", Font.BOLD,25));
           mainPanel.add(roomPanelTitle);
           
           
           housePanelTitle.setBounds(265, 20,200,100);
           housePanelTitle.setText("House Panel");
           housePanelTitle.setFont(new Font("Serif", Font.BOLD,25));
           mainPanel.add(housePanelTitle);
           
           
           roomMapTitle.setBounds(295, 70,200,100);
           roomMapTitle.setText("House Map");
           roomMapTitle.setFont(new Font("Serif", Font.BOLD,18));
           mainPanel.add(roomMapTitle);
           
           
           houseInfoTitle.setBounds(270, 660,200,40);
           houseInfoTitle.setText("House Information");
           houseInfoTitle.setFont(new Font("Serif", Font.BOLD,18));
           mainPanel.add(houseInfoTitle);
          
           
           
           roomViewTitle.setBounds(935, 70,200,100);
           roomViewTitle.setText("Room View");
           roomViewTitle.setFont(new Font("Serif", Font.BOLD,18));
           mainPanel.add(roomViewTitle);
           
           roomInfoTitle.setBounds(905, 430,200,100);
           roomInfoTitle.setText("Room Information");
           roomInfoTitle.setFont(new Font("Serif", Font.BOLD,18));
           mainPanel.add(roomInfoTitle);
           
           mainPanel.setVisible(true);

           
                  
		 }
	    
	/** This method is used to manage the sensor networks
	 *  
	 */
	 public void startManagement(){
       
		 java.util.Date dt = new java.util.Date();
         java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
         currentDate = sdf.format(dt);
         System.out.println(currentDate);
         int prova=0;
         for(Room r:house.getRooms())
        	 r.newDayStarted();
         
          Calendar newCalendar = new GregorianCalendar();
  	      int hour = newCalendar.get(Calendar.HOUR);
	      int minute = newCalendar.get(Calendar.MINUTE);
	      if(!(hour==0 & minute==0)){
	    	  for(Room r:house.getRooms()){
	    		  
	    		  int[] localPresenceProfile=new int[0];
	    		  double[] localTemperatureProfile=new double[0];
	    		  double[] localLightProfile=new double[0];
	    	  // metodo per recuperare le letture dal client
	    	  r.setNewDailyPresenceProfile(localPresenceProfile);
	    	  r.setNewDailyTemperatureProfile(localTemperatureProfile);
	    	  r.setNewDailyLightProfile(localLightProfile);
	    	  }
	         }


		 refreshValues();
         refTimer.schedule(refresh, 4000, samplingTime*1000); //Avviate le letture automatiche dei sensori
            													//Il primo numero indica il ritardo con cui parte il refresh, il secondo è l'intervallo con cui si deve ripetere
 
	 }

	  /** This method updates the lable values for the selected Room
	  *
	  */
	  
	  	private void updateSelectedRoomValues(){
 		
	  		lblRoomNameValue.setText(selectedRoom.getRoomName());
            lblTemperatureAvgValue.setText(selectedRoom.getTemperatureValues()+" �C");
			lblLightAvgValue.setText(selectedRoom.getLightValues()+" lux");
			lblPresenceValue.setText("x "+selectedRoom.getPresenceValues());
			lblConsumeValue.setText(truncate(selectedRoom.getConsumeValues())+" W/h");
		
			lblDesiredTemperatureValue.setText(""+truncate(selectedRoom.getDesiredTemperature())+" �C");
			lblDesiredLightValue.setText(""+truncate((selectedRoom.getLightValues()+selectedRoom.getDesiredLight()))+" lux");
		
			// Commentare solo con dve
			/*
			if(selectedRoom.getHomeManagmenetMode()==HomeManagementMode.AIM_Mode && selectedRoom.getPredictedProfileFound()){
				lblDesiredTemperatureValue.setText(String.valueOf(selectedRoom.getDesiredTemperature())+" °C");
				lblDesiredLightValue.setText(String.valueOf(selectedRoom.getDesiredLight())+" lux");
			}
			else{
				if(selectedRoom.getHomeManagmenetMode()==HomeManagementMode.Manual && selectedRoom.FindDesiredValue()){
					lblDesiredTemperatureValue.setText(String.valueOf(selectedRoom.getDesiredTemperature())+" °C");
					lblDesiredLightValue.setText("Unknown Value");
				}
				else{
					lblDesiredTemperatureValue.setText("Unknown Value");
					lblDesiredLightValue.setText("Unknown Value");
				}
			}*/
			}
	  	
	  	
	  	
	  	
		  /** This method updates the lable values for the selected Room
		  *
		  */
		  
		  	private void updateHouseValues(){
	 		
		  		lblRoomsNumberValue.setText(house.getRoomsNumber()+"");
	            lblHouseTemperatureAvgValue.setText(house.getTemperature()+" �C");
				lblHouseLightAvgValue.setText(house.getLight()+" lux");
				lblHousePresenceValue.setText("x "+house.getPresence());			
				lblHouseConsumeAvgValue.setText(truncate(house.getConsume())+" W/h");
				if(selectedRoom!=null)
					lblSelectedRoomValue.setText(selectedRoom.getRoomName());
				
		  	}
		  	
		  	
	  	/** This method updates the device Panel for the selected Room
	  	 * 
	  	 * @return devicesPanel - device JPanel, this panel will be added to a JTabbedPanel
	  	 */
	  	
	  	
	  	private JScrollPane updateDevicePanel(){
	  		//DEVICES PANEL
	  		
	  		
	  		  	JPanel devicesPanel = new JPanel();
	  		  	
	  		  	final JButton getInfoHeat = new JButton();
	  		  	final JButton getInfoLight = new JButton();
	  			final JButton btnTurnOnTv = new JButton();
	  			final JButton btnTurnOffTv = new JButton();
	  			final JButton btnTurnStandByTv = new JButton();
	  			final JButton getInfoTv = new JButton();
	  			final JButton btnTurnOnDvD = new JButton();
	  			final JButton btnTurnOffDvD = new JButton();
	  			final JButton btnTurnStandByDvD = new JButton();
	  			final JButton getInfoDvd = new JButton();
	  			final JButton btnTurnOnWiFi = new JButton();
	  			final JButton btnTurnOffWiFi = new JButton();
	  			final JButton btnTurnStandByWiFi = new JButton();
	  			final JButton getInfoWiFi = new JButton();
	  			final JScrollPane devicesScrollPanel;

		  		int k=0;
		  		devicesPanel.setLayout(null);
		  		
		  		for(DeviceImpl d: selectedRoom.getDevices()){
		  			JLabel lblImg = new JLabel();
		  			JLabel lblDeviceStatus = new JLabel("Status:");
		  			final JLabel lblDeviceStatusValue = new JLabel();
		  			
		  			lblDeviceStatus.setBounds(120, 55 + (k*120), 100, 20);
		  			lblDeviceStatusValue.setBounds(180, 55 + (k*120), 100, 20);
		  			devicesPanel.add(lblDeviceStatus);
		  			
		  			lblImg.setIcon(new ImageIcon(d.getImgName()));
		  			lblImg.setBounds(10, 10+(120*k), 100, 100);
		  			devicesPanel.add(lblImg);
		  			

		  			try {
						switch (d.getDeviceType()){
						
							case HEATING: {
								
								
								getInfoHeat.setBounds(320,45 + (k*120),150,30);
								getInfoHeat.setText("get Info");
								getInfoHeat.setEnabled(systemStarted);
								devicesPanel.add(getInfoHeat);
								getInfoHeat.addMouseListener(new java.awt.event.MouseAdapter() {
						            public void mouseClicked(java.awt.event.MouseEvent evt) {
						            	showDeviceInfo(DeviceType.HEATING);
						            }
						    	});
								break;
							}
							case LIGHT:{
								
								getInfoLight.setBounds(320,45 + (k*120),150,30);
								getInfoLight.setText("get Info");
								getInfoLight.setEnabled(systemStarted);
								devicesPanel.add(getInfoLight);
								getInfoLight.addMouseListener(new java.awt.event.MouseAdapter() {
						            public void mouseClicked(java.awt.event.MouseEvent evt) {
						            	showDeviceInfo(DeviceType.LIGHT);
						            }
						    	});
								break;
							}
							case TV:{ 	
										final TVDevice tv = (TVDevice) d;
										ImageIcon TvOn=new ImageIcon(images_path + "on2.png");
										btnTurnOnTv.setIcon(TvOn);
										btnTurnOnTv.setBounds(260,20 + (k*120),TvOn.getIconWidth(),TvOn.getIconHeight());
										btnTurnOnTv.setBorder(BorderFactory.createEmptyBorder());
										devicesPanel.add(btnTurnOnTv);
										kValue.remove(DeviceType.TV);
										kValue.put(DeviceType.TV,k);
										btnTurnOnTv.addMouseListener(new java.awt.event.MouseAdapter() {
						                    public void mouseClicked(java.awt.event.MouseEvent evt) {
						                  
						                    	try {
													tv.switchOn();
											} catch (RemoteException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
						                    	updateSelectedRoomValues();
						                    	updateTabbedPanel();
						                    	position[0]=20 + (kValue.get(DeviceType.TV)*120);
						                    }
						            	});
										if (tv.getStatus()==DeviceStatus.ON)
											btnTurnOnTv.setEnabled(false);
										else
											btnTurnOnTv.setEnabled(true);
										
										
										ImageIcon TvOff=new ImageIcon(images_path + "off2.png");
										btnTurnOffTv.setIcon(TvOff);
										btnTurnOffTv.setBounds(260,55 + (k*120),TvOff.getIconWidth(),TvOff.getIconHeight());
										btnTurnOffTv.setBorder(BorderFactory.createEmptyBorder());
										devicesPanel.add(btnTurnOffTv);
										btnTurnOffTv.addMouseListener(new java.awt.event.MouseAdapter() {
						                    public void mouseClicked(java.awt.event.MouseEvent evt) {
						                  
						                    	try {
													tv.switchOff();
												} catch (RemoteException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
						                    	updateSelectedRoomValues();
						                    	updateTabbedPanel();
						                    	position[0]=55 + (kValue.get(DeviceType.TV)*120);
						                    	

						                    }
						            	});
						            	if (tv.getStatus()==DeviceStatus.OFF)
						            		btnTurnOffTv.setEnabled(false);
						            	else
						            		btnTurnOffTv.setEnabled(true);
										
										ImageIcon TvStandBy=new ImageIcon(images_path + "standBy2.png");
										btnTurnStandByTv.setIcon(TvStandBy);
										btnTurnStandByTv.setBounds(260,90 + (k*120),TvStandBy.getIconWidth(),TvStandBy.getIconHeight());
										btnTurnStandByTv.setBorder(BorderFactory.createEmptyBorder());
										devicesPanel.add(btnTurnStandByTv);
										btnTurnStandByTv.addMouseListener(new java.awt.event.MouseAdapter() {
						                    public void mouseClicked(java.awt.event.MouseEvent evt) {		  		                  
						                    	try {
													tv.standBy();
												} catch (RemoteException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
						                    	updateSelectedRoomValues();
						                    	updateTabbedPanel();
						                    	position[0]=90 + (kValue.get(DeviceType.TV)*120);

						                    }
						            	});
						            	if (tv.getStatus()==DeviceStatus.STANDBY)
						            		btnTurnStandByTv.setEnabled(false);
						            	else
						            		btnTurnStandByTv.setEnabled(true);
						            	
										getInfoTv.setBounds(320,45 + (k*120),150,30);
										getInfoTv.setText("get Info");
										getInfoTv.setEnabled(systemStarted);
										devicesPanel.add(getInfoTv);
										getInfoTv.addMouseListener(new java.awt.event.MouseAdapter() {
						                    public void mouseClicked(java.awt.event.MouseEvent evt) {
						                    	showDeviceInfo(DeviceType.TV);
						                    }
						            	});
						            	
										break;
										
										
							}
							case DVD:	{
										final DvdDevice dvd = (DvdDevice) d;
										
										ImageIcon DvdOn=new ImageIcon(images_path + "on2.png");
										btnTurnOnDvD.setIcon(DvdOn);
										btnTurnOnDvD.setBounds(260,20 + (k*120),DvdOn.getIconWidth(),DvdOn.getIconHeight());
										btnTurnOnDvD.setBorder(BorderFactory.createEmptyBorder());
										devicesPanel.add(btnTurnOnDvD);
										btnTurnOnDvD.addMouseListener(new java.awt.event.MouseAdapter() {
						                    public void mouseClicked(java.awt.event.MouseEvent evt) {
						                  
						                    	try {
													dvd.switchOn();
												} catch (RemoteException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
						                    	updateSelectedRoomValues();
						                    	updateTabbedPanel();
						                    	

						                    }
						            	});
										if (dvd.getStatus()==DeviceStatus.ON)
											btnTurnOnDvD.setEnabled(false);
										else
											btnTurnOnDvD.setEnabled(true);
										
										
										ImageIcon DvdOff=new ImageIcon(images_path + "off2.png");
										btnTurnOffDvD.setIcon(DvdOff);
										btnTurnOffDvD.setBounds(260,55 + (k*120),DvdOff.getIconWidth(),DvdOff.getIconHeight());
										btnTurnOffDvD.setBorder(BorderFactory.createEmptyBorder());
										devicesPanel.add(btnTurnOffDvD);
										btnTurnOffDvD.addMouseListener(new java.awt.event.MouseAdapter() {
						                    public void mouseClicked(java.awt.event.MouseEvent evt) {
						                  
						                    	try {
													dvd.switchOff();
												} catch (RemoteException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
						                    	updateSelectedRoomValues();
						                    	updateTabbedPanel();
						                    	

						                    }
						            	});
						            	if (dvd.getStatus()==DeviceStatus.OFF)
						            		btnTurnOffDvD.setEnabled(false);
						            	else
						            		btnTurnOffDvD.setEnabled(true);
										
										ImageIcon DvdStandBy=new ImageIcon(images_path + "standBy2.png");
										btnTurnStandByDvD.setIcon(DvdStandBy);
										btnTurnStandByDvD.setBounds(260,90 + (k*120),DvdStandBy.getIconWidth(),DvdStandBy.getIconHeight());
										btnTurnStandByDvD.setBorder(BorderFactory.createEmptyBorder());
										devicesPanel.add(btnTurnStandByDvD);
										btnTurnStandByDvD.addMouseListener(new java.awt.event.MouseAdapter() {
						                    public void mouseClicked(java.awt.event.MouseEvent evt) {
						                  
						                    	try {
													dvd.standBy();
												} catch (RemoteException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
						                    	updateSelectedRoomValues();
						                    	updateTabbedPanel();
						                    	

						                    }
						            	});
						            	if (dvd.getStatus()==DeviceStatus.STANDBY)
						            		btnTurnStandByDvD.setEnabled(false);
						            	else
						            		btnTurnStandByDvD.setEnabled(true);
						            	
										getInfoDvd.setBounds(320,45 + (k*120),150,30);
										getInfoDvd.setText("get Info");
										getInfoDvd.setEnabled(systemStarted);
										devicesPanel.add(getInfoDvd);
										getInfoDvd.addMouseListener(new java.awt.event.MouseAdapter() {
						                    public void mouseClicked(java.awt.event.MouseEvent evt) {
						                    	showDeviceInfo(DeviceType.DVD);
						                    }
						            	});
										
										break;
							}
							
							case WIFI:	{
								final WiFiDevice WiFi = (WiFiDevice) d;
								
								ImageIcon WiFiOn=new ImageIcon(images_path + "on2.png");
								btnTurnOnWiFi.setIcon(WiFiOn);
								btnTurnOnWiFi.setBounds(260,30 + (k*120),WiFiOn.getIconWidth(),WiFiOn.getIconHeight());
								btnTurnOnWiFi.setBorder(BorderFactory.createEmptyBorder());
								devicesPanel.add(btnTurnOnWiFi);
								btnTurnOnWiFi.addMouseListener(new java.awt.event.MouseAdapter() {
						            public void mouseClicked(java.awt.event.MouseEvent evt) {
						          
						            	try {
											WiFi.switchOn();
										} catch (RemoteException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
						            	updateSelectedRoomValues();
						            	updateTabbedPanel();
						            	

						            }
						    	});
								if (WiFi.getStatus()==DeviceStatus.ON)
									btnTurnOnWiFi.setEnabled(false);
								else
									btnTurnOnWiFi.setEnabled(true);
								
								
								ImageIcon WiFiOff=new ImageIcon(images_path + "off2.png");
								btnTurnOffWiFi.setIcon(WiFiOff);
								btnTurnOffWiFi.setBounds(260,70 + (k*120),WiFiOff.getIconWidth(),WiFiOff.getIconHeight());
								btnTurnOffWiFi.setBorder(BorderFactory.createEmptyBorder());
								devicesPanel.add(btnTurnOffWiFi);
								btnTurnOffWiFi.addMouseListener(new java.awt.event.MouseAdapter() {
						            public void mouseClicked(java.awt.event.MouseEvent evt) {
						          
						            	try {
											WiFi.switchOff();
										} catch (RemoteException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
						            	updateSelectedRoomValues();
						            	updateTabbedPanel();
						            	

						            }
						    	});
						        	if (WiFi.getStatus()==DeviceStatus.OFF)
						        		btnTurnOffWiFi.setEnabled(false);
						        	else
						        		btnTurnOffWiFi.setEnabled(true);
								
									getInfoWiFi.setBounds(320,45 + (k*120),150,30);
									getInfoWiFi.setText("get Info");
									getInfoWiFi.setEnabled(systemStarted);
									devicesPanel.add(getInfoWiFi);
									getInfoWiFi.addMouseListener(new java.awt.event.MouseAdapter() {
						                public void mouseClicked(java.awt.event.MouseEvent evt) {
						                	showDeviceInfo(DeviceType.WIFI);
						                }
						        	});
								
								break;
		}
						}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		  			
		  			lblDeviceStatusValue.setText(d.getDeviceStatusString());
		  			devicesPanel.add(lblDeviceStatusValue);
		  			k++;
		  		}
		  		
		  	
		  	
		  		
		  		devicesPanel.setPreferredSize(new Dimension(470,20+(120*(k))));
	  			
		  		devicesScrollPanel = new JScrollPane(devicesPanel);
		  		devicesScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		  		devicesScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		  		if(position[0]!=0 )
		  			devicesScrollPanel.getVerticalScrollBar().setValue(20 + (position[0]*120)-devicesScrollPanel.getVerticalScrollBar().getHeight());

	  		return devicesScrollPanel;
	  	}
		
	  	
	  	
	  	
	  
	  	
	  	
	  	/** This method updates the consume graphic Panel for the selected Room
	  	 * 
	  	 * @return graphicPanel - consume graphic JPanel, this panel will be added to a JTabbelPanel
	  	 */
	  	
		private JPanel updateConsumeGraphicPanel(){
			
			JPanel graphicPanel = new JPanel();
	  		XYSeries graphicSeries = new XYSeries("Consumption Graphic");
	  		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	  		

	  		double[] s=selectedRoom.getConsumeProfile();
  		 	int counter=selectedRoom.getCounter();
  		 	if(counter!=0){
  	  		for(int i=0;i<counter;i++)
  	  		 {
  	  		 	graphicSeries.add(i*30,s[i]);
  	   		 }

  		  XYSeriesCollection dataset = new XYSeriesCollection();
          dataset.addSeries(graphicSeries);
          
        
          
          JFreeChart chart = ChartFactory.createXYLineChart(null, "Seconds","Watt-hour", dataset,PlotOrientation.VERTICAL, false, true, false  );
 
          ChartPanel chartPanel = new ChartPanel(chart);
	  	  chartPanel.setPreferredSize(new Dimension(450,360));
          graphicPanel.add(chartPanel);
  		 	}
	  		return graphicPanel;
	  	}
		
		
		
		
		
		
	  	/** This method updates the presence graphic Panel for the selected Room
	  	 * 
	  	 * @return graphicPanel - presence graphic JPanel, this panel will be added to a JTabbelPanel
	  	 */
		
		private JPanel updatePresenceGraphicPanel(){
			JPanel graphicPanel = new JPanel();
	  		XYSeries graphicSeries1 = new XYSeries("Predicted Presence Graphic");
		  	XYSeries graphicSeries2 = new XYSeries("Daily Presence Graphic");
		  	
	  	//	double[] t=selectedRoom.getPredictedPresenceProfile();
		  	double[] t=null;
		  	if(systemStarted==true){
		  		t=fakeProfiles.getProfile(selectedRoom.getRoomName());
		  	}
		  	int[] s=selectedRoom.getNewDailyPresenceProfile();
	  		int counter=selectedRoom.getCounter();
	  		if(counter!=0){
  	  		for(int i=0;i<counter;i++){
	  		 		graphicSeries1.add(i*30,s[i]);
  	  		 	}

  	  		 	for(int i=0;i<counter;i++){
  		  		 		graphicSeries2.add(i*30,t[i]);
  	  		 	}	
  		 	
	 	
  		 	

	  		 	
  		  XYSeriesCollection dataset = new XYSeriesCollection();
          dataset.addSeries(graphicSeries1);
          dataset.addSeries(graphicSeries2);
          JFreeChart chart = ChartFactory.createXYLineChart(null, "Seconds","Person Presence/Absence", dataset,PlotOrientation.VERTICAL, false, true, false);

          ChartPanel chartPanel = new ChartPanel(chart);
	  	  chartPanel.setPreferredSize(new Dimension(450,360));
          graphicPanel.add(chartPanel);
	  		}
	  		return graphicPanel;
	  	}
		
		
	  	/** This method updates the tabbed panel and what it contains for the selected Room
	  	 * 
	  	 */
		
	  	
		private void updateTabbedPanel(){
			 
			if(accessControl==true){
			  accessControl=false;
			  int index = tabbedPanel.getSelectedIndex();
			  if (index == -1) index=0;
			  tabbedPanel.removeAll();
			  tabbedPanel.add("Information",informationPanel);
			  tabbedPanel.addTab("Room Devices", updateDevicePanel());
			  tabbedPanel.addTab("Consumption Panel", updateConsumeGraphicPanel());
			  tabbedPanel.addTab("Presence Panel", updatePresenceGraphicPanel());
			  tabbedPanel.setSelectedIndex(index);
			  accessControl=true;
			}
			
		}
		
		
	  	/** This method updates the tabbed panel and what it contains for the selected Room
	  	 * 
	  	 */
		
		
		private void DynamicUpdateTabbedPanel(){
			 
			if(accessControl==true){
			  accessControl=false;
			  int index = tabbedPanel.getSelectedIndex();
			  if (index == -1) index=0;
			  tabbedPanel.setComponentAt(0,informationPanel);
			  tabbedPanel.setComponentAt(1, updateDevicePanel());
			  tabbedPanel.setComponentAt(2, updateConsumeGraphicPanel());
			  tabbedPanel.setComponentAt(3, updatePresenceGraphicPanel());
			  tabbedPanel.setSelectedIndex(index);
			  accessControl=true;
			}
			
		}
		
		
		
		
		/** This method updates rooms' graph
		  * 
		  */
		  
	    private void updateRoomsGraph()
	    {
	    	for(Room r:house.getRooms()){
	    		DrawRoom room=new DrawRoom(r.getRoomName(),RoomsVicinity.get(r.getRoomName()),r.getImgName(),r.getRoomType(),r.getPresenceValues());
	    		drawRoomList.add(room);
	    	}

	        ((MapPanel)newGraphPanel).setRoomDatabaseAndRepaint(drawRoomList);
    	  
	    }
	    	 
		/** This method updates the house graph
		  * 
		  */
	    
		private void updateGraph(){
			((MapPanel)newGraphPanel).setRoomDatabaseAndRepaint(drawRoomList);
		}
		 
		/** This method is used to read the sensor 
		  * 
		  */
		
		private void refreshValues() 
		{
			
			

	        
			refTimer = new Timer();
	       	refresh = new TimerTask() {
	            public void run() 
	            {
	    	        java.util.Date dt1 = new java.util.Date();
	    	        java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
	    	        currentDate1 = sdf1.format(dt1);
	            	  
	            	System.out.print("Reading sensors!");
        		    boolean found=false;
        		    System.out.println(currentDate1);
        	         
        	        Calendar newCalendar = new GregorianCalendar();
        	  	    int hour = newCalendar.get(Calendar.HOUR);
        	  	    int minute = newCalendar.get(Calendar.MINUTE);
        	  	    System.out.println(hour+":"+minute);
        	  	    
        		    if(currentDate.equals(currentDate1)){
        		    	house.refreshValues();
				        
        		    	if(localControl==false)
        		    		localControl=loadPredictedProfile();
        		    	house.setManagementMode(localControl);
        		    	if(systemStarted==true){
	        		    	updateHouseValues();
	        		    	updateDrawRoomPresence();
	        		    	//updateGraph();
	        		    	updateLabels();
	    					if (selectedRoom!=null){
	    						updateSelectedRoomValues();
	    						updateRoomImage();
	    						updateDevicePanel();
	    						updatePresenceGraphicPanel();
	    						updateConsumeGraphicPanel();
	    						DynamicUpdateTabbedPanel();
	    						
	    					}
        		    	}
        		    }
        		    else{
        		    	refresh.cancel();
        		    	startManagement();
        		    	
        		    }
	            }
	            
	    	};
		}	 
		
		
		/** This method loads the predicted profiles
		  * 
		  */
		
		public boolean loadPredictedProfile(){
	         boolean result=false;
		    try{
	            
	            Class.forName("com.mysql.jdbc.Driver").newInstance(); 

	            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); 
	                                                                                                                 
	            System.out.println("Connection to MySQL server to load the predicted profile!");

	            ResultSet rs;
	            ResultSet rs1;
	            ResultSet rs2;
	            ResultSet rs3;
	            ResultSet rs4;
	            Statement stmt = null;
	            conn.setCatalog(databaseName); 
	            stmt = conn.createStatement();
	            
	         	for(Room r:house.getRooms()){
	         		
	         		String currentDate2="";
		            rs=stmt.executeQuery("SELECT predictionDate FROM PredictedProfiles WHERE idroom="+r.getRoomIdentifier()+";");
 		            if(rs.next()){
 		            	currentDate2=rs.getString("predictionDate");
 		            }
	         		
	         		int idType=0;
	         		int idProfile=0;
	         		int dim=0;
	         	    double[] predictedPresenceProfile;
	         	    
	 	            if(currentDate.equals(currentDate2)){
	 	            	
	 	            	result=true;
	 	            	System.out.println(currentDate);
	 	            	System.out.println(currentDate2);
	 	            
	 	            	rs1=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+presenceProfileName+"';");
	 	            	while(rs1.next()){
	 	            		idType=rs1.getInt("idtype");
	 	            	}
	         		
	 	            	stmt.executeUpdate("UPDATE UpdateTable SET updvalue=0 WHERE idroom="+r.getRoomIdentifier()+" AND idtype="+idType+";");
	         		
	 	            	rs2=stmt.executeQuery("SELECT idprofile FROM PredictedProfiles WHERE idroom="+r.getRoomIdentifier()+" AND idtype="+idType+";");
	 	            	while(rs2.next()){
	 	            		idProfile=rs2.getInt("idprofile");
	 	            	}
	         		
	         		
	 	            	rs3=stmt.executeQuery("SELECT MAX(idposition) FROM FinalPresenceProfiles WHERE idroom="+r.getRoomIdentifier()+" AND idprofile="+idProfile+";");

	 	            	while(rs3.next()) {
	 	            		dim=rs3.getInt("MAX(idposition)");
	 	            	}
	         		
	 	            	predictedPresenceProfile=new double[dim];
	         		
	 	            	rs4=stmt.executeQuery("SELECT value FROM FinalPresenceProfiles WHERE idroom="+r.getRoomIdentifier()+" AND idprofile="+idProfile+" ORDER BY idposition;");
	         		
	 	            	int k=0;
	 	            	while(rs4.next()) {
	 	            		predictedPresenceProfile[k]=rs4.getDouble("value");
	 	            		k+=1;
	 	            	}
	         		
	 	            	r.setPredictedPresenceProfile(predictedPresenceProfile);

	 	            }
			    
			    else{
			    		result=false;
			    }
	         	}

		            stmt.close(); // close the object statement
		            conn.close(); // close the connection 
		            
		        }
	            
	            


	        
	        catch (Exception ex) {
	            System.out.println("SQLException: " + ex.getMessage());
	        }
	        return result;
		}
		

		/** This method updates the room graph
		  * 
		  */
		 
		 public void updateDrawRoomPresence(){
			 for(Room r:house.getRooms()){
				 for(DrawRoom s:drawRoomList){
					 if(r.getRoomName().equals(s.getRoomName()))
						 s.setPresence(r.getPresenceValues());
				 }
			 }
		 }
		 
		 /** This method is used to manage the lighting system of the house
		  * 
		  */
		 
		 public void startLightManagement(){
			 

			 final boolean localControl=false;
			   final ArrayList<RoomsType> types=new ArrayList<RoomsType>();
			    final JComboBox LightRoomsComboBox = new JComboBox();
			    final JLabel lblROOMName = new JLabel();
			    final JLabel lblLightValue = new JLabel();
			    final JLabel lblSelect = new JLabel();
			    final JButton changeLightConsume=new JButton();
			    final JComboBox TemperatureRoomsComboBox = new JComboBox();
			    final JLabel lblROOMNameTemp = new JLabel();
			    final JLabel lblROOMNameTemp2 = new JLabel();
			    final JLabel lblTemperatureValue = new JLabel();
			    final JLabel lblSelectTemp = new JLabel();
			    final JButton changeTempConsume;
			    final JRadioButton AIMManagementMode= new JRadioButton();;
			    final JRadioButton ManualMode= new JRadioButton();
			    final JComboBox ProgrammingModeComboBox = new JComboBox();
			    final JComboBox Prova = new JComboBox();
			    final JPanel TemperatureManagementPanel = new JPanel();
			    final JButton btnOK=new JButton();
			    final JButton btnUpdate=new JButton();
			    final JTextArea textAreaNewTemperatureValue = new JTextArea();
			    final JPanel TemperatureRoomPanel = new JPanel();
			    final JFrame TemperatureSettingsFrame = new JFrame("");
			    final JFrame LightSettingsFrame = new JFrame("");
			    final JPanel LightRoomPanel = new JPanel();
			    final JTextArea textAreaNewLightValue = new JTextArea();
			    final JLabel lblSelectProgram = new JLabel();
			    final JLabel lblStartTime = new JLabel();
			    final JLabel lblStopTime = new JLabel();
			    final JLabel lblDesiredTemp = new JLabel();
			    final JButton btnDeleteProgram=new JButton();
			    final JButton btnModifyProgram=new JButton();
			    final JButton btnAddProgram=new JButton();
			    final JButton btnAddThisProgram=new JButton();
			    final JTextArea textAreaNewHourStartTime = new JTextArea();
			    final JTextArea textAreaNewHourStopTime = new JTextArea();
			    final JTextArea textAreaNewMinutesStartTime = new JTextArea();
			    final JTextArea textAreaNewMinutesStopTime = new JTextArea();
			    final JTextArea textAreaNewDesiredTemp = new JTextArea();
			    final JLabel lbl2dot1 = new JLabel();
			    final JLabel lbl2dot2 = new JLabel();
			    final JLabel lblC = new JLabel();
			    final int localIndex=1;
            	LightSettingsFrame.setBounds(350, 350, 500, 380);
            	LightSettingsFrame.setVisible(true);
            	LightSettingsFrame.setLayout(null);
            	JLabel lblTitle = new JLabel("Light System Management");      	
           // 	LightRoomsComboBox = new JComboBox();
              
             lblSelect.setText("Select a Room!");
             lblSelect.setBounds(25,65,150,10);
             LightSettingsFrame.add(lblSelect);
             
     		LightRoomsComboBox.setEditable(false);
     		LightRoomsComboBox.setName("LightRoomsComboBox");
     		
     		for(Room r : house.getRooms()){
     			LightRoomsComboBox.addItem(r.getRoomName());
     		}
     		
     		

     		if(house.getRooms().size()==0){
     			LightRoomsComboBox.setEnabled(false);
     			LightRoomsComboBox.setVisible(false);
     			
     		}
     		else{
     			LightRoomsComboBox.setEnabled(true);
 				LightRoomsComboBox.setVisible(true);
 				LightRoomsComboBox.setSelectedIndex(house.getRooms().size()-1);
     		}

             
            	
             
             LightRoomPanel.setBounds(25, 120, 440, 160);
             LightRoomPanel.setVisible(true);
             LightRoomPanel.setLayout(null);
             LightRoomPanel.setBorder(BorderFactory.createEtchedBorder());
             LightSettingsFrame.add(LightRoomPanel);
             
             lblROOMName.setText("Room Name: No Room Selected");
             lblROOMName.setBounds(10,10,200,20);
             LightRoomPanel.add(lblROOMName);
             
             lblLightValue.setText("Light Consume:");
             lblLightValue.setBounds(10,50,200,20);
             LightRoomPanel.add(lblLightValue);
  
            
             textAreaNewLightValue.setColumns(20);
             textAreaNewLightValue.setBounds(120,50,50,20);
             textAreaNewLightValue.setBorder(BorderFactory.createEtchedBorder());
             textAreaNewLightValue.setEnabled(false);
             textAreaNewLightValue.setVisible(false);
             LightRoomPanel.add(textAreaNewLightValue);
         	
            
             btnUpdate.setEnabled(false);
             btnUpdate.setVisible(false);
             btnUpdate.setName("btnUpdate");
             btnUpdate.setText("Update Value");
             
             btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
                 public void mouseClicked(final java.awt.event.MouseEvent evt) {
                 	
                 	if (textAreaNewLightValue.getText().equals("") )
                 	{
                 		JFrame frameDialogue = new JFrame();
                 		JOptionPane.showMessageDialog(frameDialogue, "Please type a valid value!");
                 	}
                 	else{
                 		System.out.println("dentro!");
                 		double newLightValueConsume=0;
	                    	for(Room r:house.getRooms()){
	        		    		  if(r.getRoomName().equals(LightRoomsComboBox.getSelectedItem().toString())){
	        		    			  for (DeviceImpl d:r.getDevices()){
	        		    				  try {
											if(d.getDeviceType()==DeviceType.LIGHT){
												  newLightValueConsume=d.getConsume(DeviceStatus.ON);
											  }
										} catch (RemoteException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
	        		    			  }
	        		    		  }
	        		    	  }
	                    	changeLightConsume.setEnabled(true);
	                    	try{
	                    	newLightValueConsume = Double.parseDouble(textAreaNewLightValue.getText());
	                    	for(Room r:house.getRooms()){
	        		    		  if(r.getRoomName().equals(LightRoomsComboBox.getSelectedItem().toString())){
	        		    			  for (DeviceImpl d:r.getDevices()){
	        		    				  if(d.getDeviceType()==DeviceType.LIGHT){
	        		    					  d.setConsume(DeviceStatus.ON,newLightValueConsume);
	        		    					  lblLightValue.setText("Light Consume: "+d.getConsume(DeviceStatus.ON)+" W");
	        		    					  changeLightConsume.setEnabled(true);
	        		    				  }
	        		    			  }
	        		    		  }
	        		    	  }
	          
	                    	btnUpdate.setEnabled(false);
	                    	btnUpdate.setVisible(false);
			                textAreaNewLightValue.setEnabled(false);
			                textAreaNewLightValue.setVisible(false);
	                    	}
	                    	catch(Exception a){
	                    		JFrame frameDialogue = new JFrame();
	                    		JOptionPane.showMessageDialog(frameDialogue, "Please type a valid value!");
	                    	}
                 	}
                 }
             });
             btnUpdate.setBounds(220,42,150,30);
            	LightRoomPanel.add(btnUpdate);
             
            	//changeLightConsume=new JButton();
            	changeLightConsume.setEnabled(false);
            	changeLightConsume.setName("changeLightConsume");
            	changeLightConsume.setText("Change Light Consume of This Room");
            	changeLightConsume.addMouseListener(new java.awt.event.MouseAdapter() {
                 public void mouseClicked(final java.awt.event.MouseEvent evt) {
                 	changeLightConsume.setEnabled(false);
                 	lblLightValue.setText("Light Consume: ");
                 	btnUpdate.setEnabled(true);
                 	btnUpdate.setVisible(true);
		            textAreaNewLightValue.setEnabled(true);
		            textAreaNewLightValue.setVisible(true);
                 }
             });
            	changeLightConsume.setBounds(70,100,300,30);
            	LightRoomPanel.add(changeLightConsume);
             
		    	  if(LightRoomsComboBox.getSelectedItem()!=null)
		    	  {
		    	  String currentRoom = LightRoomsComboBox.getSelectedItem().toString();
		    	  lblROOMName.setText("Room Name: "+currentRoom);
		    	  for(Room r:house.getRooms()){
		    		  if(r.getRoomName().equals(currentRoom)){
		    			  boolean lightFound=false;
		    			  for (DeviceImpl d:r.getDevices()){
		    				  try {
								if(d.getDeviceType()==DeviceType.LIGHT){
									  lightFound=lightFound | true;
									  lblLightValue.setText("Light Consume: "+d.getConsume(DeviceStatus.ON)+" W");
									  changeLightConsume.setEnabled(true);
								  }
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    			  }
		    			  if(lightFound==false)
		    				  lblLightValue.setText("Light Consume: No Light in this Room");
		    		  }
		    	  }
		    	  } 
            	
		    	  
	            	LightRoomsComboBox.addActionListener(new java.awt.event.ActionListener() {
	       		      public void actionPerformed(ActionEvent actionEvent) {
	       		    	  
	       	             textAreaNewLightValue.setEnabled(false);
	       	             textAreaNewLightValue.setVisible(false);
	       	             btnUpdate.setEnabled(false);
	       	             btnUpdate.setVisible(false);
	       		    	  if(LightRoomsComboBox.getSelectedItem()!=null)
	       		    	  {
	       		    	  String currentRoom = LightRoomsComboBox.getSelectedItem().toString();
	       		    	  lblROOMName.setText("Room Name: "+currentRoom);
	       		    	  for(Room r:house.getRooms()){
	       		    		  if(r.getRoomName().equals(currentRoom)){
	       		    			  boolean lightFound=false;
	       		    			  for (DeviceImpl d:r.getDevices()){
	       		    				  try {
										if(d.getDeviceType()==DeviceType.LIGHT){
											  lightFound=lightFound | true;
											  lblLightValue.setText("Light Consume: "+d.getConsume(DeviceStatus.ON)+" W");
											  changeLightConsume.setEnabled(true);
										  }
									} catch (RemoteException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	       		    			  }
	       		    			  if(lightFound==false)
	       		    				  lblLightValue.setText("Light Consume: No Light in this Room");
	       		    		  }
	       		    	  }
	       		    	  } 
	       		    	  
	       		    	  }
	       		    	  		        		      
	       		      
	       		    });
	              	LightRoomsComboBox.setBounds(160,54,200,30);
	              	LightSettingsFrame.add(LightRoomsComboBox);	
		    	  
	                
	                btnOK.setEnabled(true);
	                btnOK.setVisible(true);
	                btnOK.setName("btnOK");
	                btnOK.setText("OK");
	                btnOK.addMouseListener(new java.awt.event.MouseAdapter() {
	                    public void mouseClicked(final java.awt.event.MouseEvent evt) {
	                    	LightSettingsFrame.dispose();
	                   /* 	int a=btnUpdate.getMouseListeners().length;
	                    	System.out.println(btnUpdate.getMouseListeners().length);
	                    	for(int i=0;i<a;i++){
	                    	btnUpdate.removeMouseListener(btnUpdate.getMouseListeners()[0]);
	                    	System.out.println(i);
	                    	}
	                    	System.out.println(btnUpdate.getMouseListeners().length);*/
	                    }
	                });
	                btnOK.setBounds(200,300,90,30);
	             	LightSettingsFrame.add(btnOK);
		    	  
            	lblTitle.setBounds(130, 10, 300, 30);
            	lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            	lblTitle.setFont(new Font("DejaVu Sans",Font.BOLD,15));
            	LightSettingsFrame.add(lblTitle);
            
 			lblImageLight=new JLabel();
				lightImage=new ImageIcon(images_path + "Light.png");
				lblImageLight.setBounds(440,5,lightImage.getIconWidth()+5,lightImage.getIconWidth()+5);
				lblImageLight.setIcon(lightImage);
				lblImageLight.setBorder(BorderFactory.createEtchedBorder());
				LightSettingsFrame.add(lblImageLight);
            	

			 
		 }
		 
		 /** This method is used to manage the heating/cooling system of the house
		  * 
		  */
		 
		 public void startTemperatureManagement(){
			 
			 final boolean localControl=false;
			   final ArrayList<RoomsType> types=new ArrayList<RoomsType>();
			    final JComboBox LightRoomsComboBox = new JComboBox();
			    final JLabel lblROOMName = new JLabel();
			    final JLabel lblLightValue = new JLabel();
			    final JLabel lblSelect = new JLabel();
			    final JButton changeLightConsume=new JButton();
			    final JComboBox TemperatureRoomsComboBox = new JComboBox();
			    final JLabel lblROOMNameTemp = new JLabel();
			    final JLabel lblROOMNameTemp2 = new JLabel();
			    final JLabel lblTemperatureValue = new JLabel();
			    final JLabel lblSelectTemp = new JLabel();
			    final JButton changeTempConsume=new JButton();
			    final JRadioButton AIMManagementMode= new JRadioButton();;
			    final JRadioButton ManualMode= new JRadioButton();
			    final JComboBox ProgrammingModeComboBox = new JComboBox();
			    final JComboBox Prova = new JComboBox();
			    final JPanel TemperatureManagementPanel = new JPanel();
			    final JButton btnOK=new JButton();
			    final JButton btnUpdate=new JButton();
			    final JTextArea textAreaNewTemperatureValue = new JTextArea();
			    final JPanel TemperatureRoomPanel = new JPanel();
			    final JFrame TemperatureSettingsFrame = new JFrame("");
			    final JFrame LightSettingsFrame = new JFrame("");
			    final JPanel LightRoomPanel = new JPanel();
			    final JTextArea textAreaNewLightValue = new JTextArea();
			    final JLabel lblSelectProgram = new JLabel();
			    final JLabel lblStartTime = new JLabel();
			    final JLabel lblStopTime = new JLabel();
			    final JLabel lblDesiredTemp = new JLabel();
			    final JButton btnDeleteProgram=new JButton();
			    final JButton btnModifyProgram=new JButton();
			    final JButton btnAddProgram=new JButton();
			    final JButton btnAddThisProgram=new JButton();
			    final JButton btnUpdateThisProgram=new JButton();
			    final JTextArea textAreaNewHourStartTime = new JTextArea();
			    final JTextArea textAreaNewHourStopTime = new JTextArea();
			    final JTextArea textAreaNewMinutesStartTime = new JTextArea();
			    final JTextArea textAreaNewMinutesStopTime = new JTextArea();
			    final JTextArea textAreaNewDesiredTemp = new JTextArea();
			    final JLabel lbl2dot1 = new JLabel();
			    final JLabel lbl2dot2 = new JLabel();
			    final JLabel lblC = new JLabel();
				final JTabbedPane TempManagementTabbedPanel= new JTabbedPane();
				
				
         	TemperatureSettingsFrame.setBounds(350, 350, 500, 570);
         	TemperatureSettingsFrame.setVisible(true);
         	TemperatureSettingsFrame.setLayout(null);
         	JLabel lblTitle = new JLabel("Heating System Management");      	

         	
         	lblSelectTemp.setText("Select a Room!");
         	lblSelectTemp.setBounds(25,65,150,10);
         	TemperatureSettingsFrame.add(lblSelectTemp);
          
         	TemperatureRoomsComboBox.setEditable(false);
         	TemperatureRoomsComboBox.setName("TemperatureRoomsComboBox");
  		
         	for(Room r : house.getRooms()){
         		TemperatureRoomsComboBox.addItem(r.getRoomName());
         	}
         	
  		

         	if(house.getRooms().size()==0){
         		TemperatureRoomsComboBox.setEnabled(false);
         		TemperatureRoomsComboBox.setVisible(false);
         		AIMManagementMode.setEnabled(false);
         		ManualMode.setEnabled(false);
         		
         	}
         	else{
         		TemperatureRoomsComboBox.setEnabled(true);
         		TemperatureRoomsComboBox.setVisible(true);
         		AIMManagementMode.setEnabled(true);
         		ManualMode.setEnabled(true);
         		TemperatureRoomsComboBox.setSelectedIndex(house.getRooms().size()-1);
         	}
 
         	
		  
         	
         	TemperatureRoomPanel.setBounds(25, 120, 440, 160);
         	TemperatureRoomPanel.setVisible(true);
         	TemperatureRoomPanel.setLayout(null);
         	TemperatureRoomPanel.setBorder(BorderFactory.createEtchedBorder());
          
         	TempManagementTabbedPanel.setEnabled(true);
         	TempManagementTabbedPanel.setBounds(25, 130, 440, 345);
         	int index = TempManagementTabbedPanel.getSelectedIndex();
         	if (index == -1) index=0;
         	TempManagementTabbedPanel.add("System Consume",TemperatureRoomPanel);
         	TempManagementTabbedPanel.setSelectedIndex(index);
         	TemperatureSettingsFrame.add( TempManagementTabbedPanel);
		  
         	lblROOMNameTemp.setText("Room Name: No Room Selected");
         	lblROOMNameTemp.setBounds(10,60,200,20);
         	TemperatureRoomPanel.add(lblROOMNameTemp);
         	
	         lblROOMNameTemp2.setText("Room Name: No Room Selected");
	         lblROOMNameTemp2.setBounds(10,10,200,20);
          
         	lblTemperatureValue.setText("Heating System Consume:");
         	lblTemperatureValue.setBounds(10,120,300,20);
         	TemperatureRoomPanel.add(lblTemperatureValue);

         	
         	textAreaNewTemperatureValue.setColumns(10);
         	textAreaNewTemperatureValue.setBounds(180,120,50,20);
         	textAreaNewTemperatureValue.setBorder(BorderFactory.createEtchedBorder());
         	textAreaNewTemperatureValue.setEnabled(false);
         	textAreaNewTemperatureValue.setVisible(false);
         	TemperatureRoomPanel.add(textAreaNewTemperatureValue);
      	
         	
         	btnUpdate.setEnabled(false);
         	btnUpdate.setVisible(false);
         	btnUpdate.setName("btnUpdate");
         	btnUpdate.setText("Update Value");
         	btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
         		public void mouseClicked(final java.awt.event.MouseEvent evt) {
              	
         			if (textAreaNewTemperatureValue.getText().equals("") )
         			{
         				JFrame frameDialogue = new JFrame();
         				JOptionPane.showMessageDialog(frameDialogue, "Please type a valid value!");
         			}
         			else{
         				double newTemperatureValueConsume=0;
	                    	for(Room r:house.getRooms()){
	        		    		  if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
	        		    			  for (DeviceImpl d:r.getDevices()){
	        		    				  try {
											if(d.getDeviceType()==DeviceType.HEATING){
												  newTemperatureValueConsume=d.getConsume(DeviceStatus.ON);
											  }
										} catch (RemoteException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
	        		    			  }
	        		    		  }
	        		    	  }
	                    	changeTempConsume.setEnabled(true);
	                    	try{
	                    	newTemperatureValueConsume = Double.parseDouble(textAreaNewTemperatureValue.getText());
	                    	for(Room r:house.getRooms()){
	        		    		  if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
	        		    			  for (DeviceImpl d:r.getDevices()){
	        		    				  if(d.getDeviceType()==DeviceType.HEATING){
	        		    					  d.setConsume(DeviceStatus.ON,newTemperatureValueConsume);
	        		    					  lblTemperatureValue.setText("Heating System Consume: "+d.getConsume(DeviceStatus.ON)+" W");
	        		    					  changeTempConsume.setEnabled(true);
	        		    				  }
	        		    			  }
	        		    		  }
	        		    	  }
	          
	                    	btnUpdate.setEnabled(false);
	                    	btnUpdate.setVisible(false);
			                textAreaNewTemperatureValue.setEnabled(false);
			                textAreaNewTemperatureValue.setVisible(false);
	                    	}
	                    	catch(Exception a){
	                    		JFrame frameDialogue = new JFrame();
	                    		JOptionPane.showMessageDialog(frameDialogue, "Please type a valid value!");
	                    	}
         			}
         		}
         	});
         	btnUpdate.setBounds(270,112,150,30);
         	TemperatureRoomPanel.add(btnUpdate);
          
         	
         	changeTempConsume.setEnabled(false);
         	changeTempConsume.setName("changeTemperatureConsume");
         	changeTempConsume.setText("Change Heating System Consume of This Room");
         	changeTempConsume.addMouseListener(new java.awt.event.MouseAdapter() {
              public void mouseClicked(final java.awt.event.MouseEvent evt) {
              	changeTempConsume.setEnabled(false);
              	lblTemperatureValue.setText("Heating System Consume: ");
              	btnUpdate.setEnabled(true);
              	btnUpdate.setVisible(true);
		                textAreaNewTemperatureValue.setEnabled(true);
		                textAreaNewTemperatureValue.setVisible(true);
              }
         	});
         	changeTempConsume.setBounds(18,200,400,30);
         	TemperatureRoomPanel.add(changeTempConsume);
          
		    	  if(TemperatureRoomsComboBox.getSelectedItem()!=null)
		    	  {
		    		  String currentRoom = TemperatureRoomsComboBox.getSelectedItem().toString();
		    		  lblROOMNameTemp.setText("Room Name: "+currentRoom);
		    		  lblROOMNameTemp2.setText("Room Name: "+currentRoom);
		    		  for(Room r:house.getRooms()){
		    			  if(r.getRoomName().equals(currentRoom)){
		    				  boolean temperatureFound=false;
		    				  for (DeviceImpl d:r.getDevices()){
		    					  try {
									if(d.getDeviceType()==DeviceType.HEATING){
										  temperatureFound=temperatureFound | true;
										  lblTemperatureValue.setText("Heating System Consume: "+d.getConsume(DeviceStatus.ON)+" W");
										  changeTempConsume.setEnabled(true);

										  if(r.getHomeManagmenetMode()==HomeManagementMode.AIM_Mode)
											  AIMManagementMode.setSelected(true);
										  else
									    	 ManualMode.setSelected(true);
									  }
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		    				  }
		    			  if(temperatureFound==false)
		    				  lblTemperatureValue.setText("Heating System Consume: No Heating System in this Room");
		    			  
		    			  }
		    		  }
		    	  } 
         	
		    	  TemperatureRoomsComboBox.addActionListener(new java.awt.event.ActionListener() {
		    		      public void actionPerformed(ActionEvent actionEvent) {
		    		    	  
		    		          textAreaNewTemperatureValue.setEnabled(false);
		    		          textAreaNewTemperatureValue.setVisible(false);
		    		          btnUpdate.setEnabled(false);
		    		          btnUpdate.setVisible(false);
		    		    	  if(TemperatureRoomsComboBox.getSelectedItem()!=null)
		    		    	  {
		    		    	  String currentRoom = TemperatureRoomsComboBox.getSelectedItem().toString();
		    		    	  lblROOMNameTemp.setText("Room Name: "+currentRoom);
		    		    	  lblROOMNameTemp2.setText("Room Name: "+currentRoom);
		    		    	  for(Room r:house.getRooms()){
		    		    		  if(r.getRoomName().equals(currentRoom)){
		    		    			  boolean temperatureFound=false;
		    		    			  for (DeviceImpl d:r.getDevices()){
		    		    				  try {
											if(d.getDeviceType()==DeviceType.HEATING){
												  temperatureFound=temperatureFound | true;
												  lblTemperatureValue.setText("Heating System Consume: "+d.getConsume(DeviceStatus.ON)+" W");
												  changeTempConsume.setEnabled(true);

												  if(r.getHomeManagmenetMode()==HomeManagementMode.AIM_Mode){
													  AIMManagementMode.setSelected(true);
													  ManualMode.setSelected(false);
												  }
												  else{
													 AIMManagementMode.setSelected(false);
											    	 ManualMode.setSelected(true);
												  }
											  }
										} catch (RemoteException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
		    		    			  }
		    		    			  if(temperatureFound==false)
		    		    				  lblTemperatureValue.setText("Heating System Consume: No Heating System in this Room");
		    		    		  }
		    		    	  }
		    		    	  } 
		    		    	  
				         		if(ProgrammingModeComboBox.getItemCount()!=0){
				         			ProgrammingModeComboBox.removeAllItems();
				         		}
				         		
				         		if(ManualMode.isSelected()){
							         	if(!house.getRooms().isEmpty()){
							         		for(Room r : house.getRooms()){
							         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
							         				
							         				r.setHomeManagementMode(HomeManagementMode.Manual);
							         				if(r.getHomeManagmenetMode()==HomeManagementMode.Manual){
							         					
							         					if(!r.getProgrammingMode().isEmpty()){
							         						for(HeatingManagementTime t :r.getProgrammingMode()){
							         							
							         							ProgrammingModeComboBox.addItem(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes());
							         						}
							         					}
							         				}
							         			}
							         		}
							         	}
					         		
						         	ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
						         	if(ProgrammingModeComboBox.getSelectedItem()==null){
						         		btnDeleteProgram.setEnabled(false);
						         		btnModifyProgram.setEnabled(false);
						         	}
						         	else{
						         		btnDeleteProgram.setEnabled(true);
						         		btnModifyProgram.setEnabled(true);
						         	}
						         	
						         	if(ProgrammingModeComboBox.getSelectedItem()==null && ManualMode.isSelected()){
						         		lblSelectProgram.setEnabled(true);
						         		lblSelectProgram.setText("No Program Available!");
						         		lblStartTime.setEnabled(true);
						         		lblStartTime.setText("Start Time: ");
						         		lblStopTime.setEnabled(true);
						         		lblStopTime.setText("Stop Time: ");
						         		lblDesiredTemp.setEnabled(true);
						         		lblDesiredTemp.setText("Desired Temperature: ");
						         		btnAddProgram.setEnabled(true);
						         	}
						         	if(ProgrammingModeComboBox.getSelectedItem()==null || AIMManagementMode.isSelected()){
						         		ProgrammingModeComboBox.setEnabled(false);
						         	}
						         	else{
						         		btnAddProgram.setEnabled(true);
						         		ProgrammingModeComboBox.setEnabled(true);
						         		ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
						         		lblSelectProgram.setEnabled(true);
						         		lblSelectProgram.setText("Select an Heating Program!");
						         		lblStartTime.setEnabled(true);
						         		lblStopTime.setEnabled(true);
						         		lblDesiredTemp.setEnabled(true);
						         		for(Room r : house.getRooms()){
						         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
						         					if(!r.getProgrammingMode().isEmpty()){
						         						for(HeatingManagementTime t :r.getProgrammingMode()){
						         							if(ProgrammingModeComboBox.getSelectedItem().toString().equals(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes()))
						         							{
						         								lblStartTime.setText("Start Time: "+t.getBeginningHour()+":"+t.getBeginningMinutes());
						         								lblStopTime.setText("Stop Time: "+t.getStoppingHour()+":"+t.getStoppingMinutes());
						         								lblDesiredTemp.setText("Desired Temperature: "+t.getTemp()+" °C");
						         							}
						         						}
						         					}
						         			}
						         		}
						         	}
					         		
				    		    	  String currentRoom = TemperatureRoomsComboBox.getSelectedItem().toString();
				    		    	  for(Room r:house.getRooms()){
				    		    		  if(r.getRoomName().equals(currentRoom)){
				    		    			  
				    			         		lblSelectProgram.setEnabled(true);
				    			         		if(ProgrammingModeComboBox.getSelectedItem()!=null)
				    			         			lblSelectProgram.setText("Select an Heating Program!");
				    			         		else
				    			         			lblSelectProgram.setText("No Program Available!");
				    		    		  }
				    		    	  }
				         		}
				         		else{
					         		lblSelectProgram.setEnabled(false);
					         		lblSelectProgram.setText("");
					         		lblStartTime.setEnabled(false);
					         		lblStartTime.setText("Start Time:");
					         		lblStopTime.setEnabled(false);
					         		lblStopTime.setText("Stop Time:");
					         		lblDesiredTemp.setEnabled(false);
					         		lblDesiredTemp.setText("Desired Temperature: ");
					         		btnAddProgram.setEnabled(false);
					         		btnDeleteProgram.setEnabled(false);
					         		btnModifyProgram.setEnabled(false);
					         		ProgrammingModeComboBox.setEnabled(false);
				         		}
	    	                    	textAreaNewHourStartTime.setEnabled(false);
	    	                    	textAreaNewHourStartTime.setVisible(false);
	    	                    	lbl2dot1.setEnabled(false);
	    	                    	lbl2dot1.setVisible(false);
	    	                    	textAreaNewMinutesStartTime.setEnabled(false);
	    	                    	textAreaNewMinutesStartTime.setVisible(false);
	    	                    	textAreaNewHourStopTime.setEnabled(false);
	    	                    	textAreaNewHourStopTime.setVisible(false);
	    	                    	lbl2dot2.setEnabled(false);
	    	                    	lbl2dot2.setVisible(false);
	    	                    	textAreaNewMinutesStopTime.setEnabled(false);
	    	                    	textAreaNewMinutesStopTime.setVisible(false);
	    	                    	textAreaNewDesiredTemp.setEnabled(false);
	    	                    	textAreaNewDesiredTemp.setVisible(false);
	    	                    	lblC.setEnabled(false);
	    	                    	lblC.setVisible(false);
	    	                    	btnAddThisProgram.setEnabled(false);
	    	                    	btnAddThisProgram.setVisible(false);
	    	                    	btnUpdateThisProgram.setEnabled(false);
	    	                    	btnUpdateThisProgram.setVisible(false);
		    		    	  }
		    		    	  		        		      
		    		      
		    	  });
		    	  TemperatureRoomsComboBox.setBounds(160,54,200,30);
		    	  TemperatureSettingsFrame.add(TemperatureRoomsComboBox);	
		            
		    	 
		         
		         TemperatureManagementPanel.setBounds(25, 120, 450, 300);
		         TemperatureManagementPanel.setVisible(true);
		         TemperatureManagementPanel.setLayout(null);
		         TemperatureManagementPanel.setBorder(BorderFactory.createEtchedBorder());
		         TempManagementTabbedPanel.add("Temperature Management", TemperatureManagementPanel);
		         

		         TemperatureManagementPanel.add(lblROOMNameTemp2);
		         
		         TempManagementTabbedPanel.addMouseListener(new java.awt.event.MouseAdapter(){
			         	public void mouseClicked(final java.awt.event.MouseEvent evt) {
		    		          textAreaNewTemperatureValue.setEnabled(false);
		    		          textAreaNewTemperatureValue.setVisible(false);
		    		          btnUpdate.setEnabled(false);
		    		          btnUpdate.setVisible(false);
		    		    	  if(TemperatureRoomsComboBox.getSelectedItem()!=null)
		    		    	  {
		    		    	  String currentRoom = TemperatureRoomsComboBox.getSelectedItem().toString();
		    		    	  lblROOMNameTemp.setText("Room Name: "+currentRoom);
		    		    	  for(Room r:house.getRooms()){
		    		    		  if(r.getRoomName().equals(currentRoom)){
		    		    			  boolean temperatureFound=false;
		    		    			  for (DeviceImpl d:r.getDevices()){
		    		    				  try {
											if(d.getDeviceType()==DeviceType.HEATING){
												  temperatureFound=temperatureFound | true;
												  lblTemperatureValue.setText("Heating System Consume: "+d.getConsume(DeviceStatus.ON)+" W");
												  changeTempConsume.setEnabled(true);

												  if(r.getHomeManagmenetMode()==HomeManagementMode.AIM_Mode){
													  AIMManagementMode.setSelected(true);
													  ManualMode.setSelected(false);
												  }
												  else{
													 AIMManagementMode.setSelected(false);
											    	 ManualMode.setSelected(true);
												  }
											  }
										} catch (RemoteException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
		    		    			  }
		    		    			  if(temperatureFound==false)
		    		    				  lblTemperatureValue.setText("Heating System Consume: No Heating System in this Room");
		    		    		  }
		    		    	  }
		    		    	  } 
		    		    	  
				         		if(ProgrammingModeComboBox.getItemCount()!=0){
				         			ProgrammingModeComboBox.removeAllItems();
				         		}
				         		
				         		if(ManualMode.isSelected()){
							         	if(!house.getRooms().isEmpty()){
							         		for(Room r : house.getRooms()){
							         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
							         				
							         				r.setHomeManagementMode(HomeManagementMode.Manual);
							         				if(r.getHomeManagmenetMode()==HomeManagementMode.Manual){
							         					
							         					if(!r.getProgrammingMode().isEmpty()){
							         						for(HeatingManagementTime t :r.getProgrammingMode()){
							         							
							         							ProgrammingModeComboBox.addItem(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes());
							         						}
							         					}
							         				}
							         			}
							         		}
							         	}
					         		
						         	ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
						         	if(ProgrammingModeComboBox.getSelectedItem()==null){
						         		btnDeleteProgram.setEnabled(false);
						         		btnModifyProgram.setEnabled(false);
						         	}
						         	else{
						         		btnDeleteProgram.setEnabled(true);
						         		btnModifyProgram.setEnabled(true);
						         	}
						         	
						         	if(ProgrammingModeComboBox.getSelectedItem()==null && ManualMode.isSelected()){
						         		lblSelectProgram.setEnabled(true);
						         		lblSelectProgram.setText("No Program Available!");
						         		lblStartTime.setEnabled(true);
						         		lblStartTime.setText("Start Time: ");
						         		lblStopTime.setEnabled(true);
						         		lblStopTime.setText("Stop Time: ");
						         		lblDesiredTemp.setEnabled(true);
						         		lblDesiredTemp.setText("Desired Temperature: ");
						         		btnAddProgram.setEnabled(true);
						         	}
						         	if(ProgrammingModeComboBox.getSelectedItem()==null || AIMManagementMode.isSelected()){
						         		ProgrammingModeComboBox.setEnabled(false);
						         	}
						         	else{
						         		btnAddProgram.setEnabled(true);
						         		ProgrammingModeComboBox.setEnabled(true);
						         		ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
						         		lblSelectProgram.setEnabled(true);
						         		lblSelectProgram.setText("Select an Heating Program!");
						         		lblStartTime.setEnabled(true);
						         		lblStopTime.setEnabled(true);
						         		lblDesiredTemp.setEnabled(true);
						         		for(Room r : house.getRooms()){
						         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
						         					if(!r.getProgrammingMode().isEmpty()){
						         						for(HeatingManagementTime t :r.getProgrammingMode()){
						         							if(ProgrammingModeComboBox.getSelectedItem().toString().equals(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes()))
						         							{
						         								lblStartTime.setText("Start Time: "+t.getBeginningHour()+":"+t.getBeginningMinutes());
						         								lblStopTime.setText("Stop Time: "+t.getStoppingHour()+":"+t.getStoppingMinutes());
						         								lblDesiredTemp.setText("Desired Temperature: "+t.getTemp()+" °C");
						         							}
						         						}
						         					}
						         			}
						         		}
						         	}
					         		
				    		    	  String currentRoom = TemperatureRoomsComboBox.getSelectedItem().toString();
				    		    	  for(Room r:house.getRooms()){
				    		    		  if(r.getRoomName().equals(currentRoom)){
				    		    			  
				    			         		lblSelectProgram.setEnabled(true);
				    			         		if(ProgrammingModeComboBox.getSelectedItem()!=null)
				    			         			lblSelectProgram.setText("Select an Heating Program!");
				    			         		else
				    			         			lblSelectProgram.setText("No Program Available!");
				    		    		  }
				    		    	  }
				         		}
				         		else{
					         		lblSelectProgram.setEnabled(false);
					         		lblSelectProgram.setText("");
					         		lblStartTime.setEnabled(false);
					         		lblStartTime.setText("Start Time:");
					         		lblStopTime.setEnabled(false);
					         		lblStopTime.setText("Stop Time:");
					         		lblDesiredTemp.setEnabled(false);
					         		lblDesiredTemp.setText("Desired Temperature: ");
					         		btnAddProgram.setEnabled(false);
					         		btnDeleteProgram.setEnabled(false);
					         		btnModifyProgram.setEnabled(false);
					         		ProgrammingModeComboBox.setEnabled(false);
				         		}
	    	                    	textAreaNewHourStartTime.setEnabled(false);
	    	                    	textAreaNewHourStartTime.setVisible(false);
	    	                    	lbl2dot1.setEnabled(false);
	    	                    	lbl2dot1.setVisible(false);
	    	                    	textAreaNewMinutesStartTime.setEnabled(false);
	    	                    	textAreaNewMinutesStartTime.setVisible(false);
	    	                    	textAreaNewHourStopTime.setEnabled(false);
	    	                    	textAreaNewHourStopTime.setVisible(false);
	    	                    	lbl2dot2.setEnabled(false);
	    	                    	lbl2dot2.setVisible(false);
	    	                    	textAreaNewMinutesStopTime.setEnabled(false);
	    	                    	textAreaNewMinutesStopTime.setVisible(false);
	    	                    	textAreaNewDesiredTemp.setEnabled(false);
	    	                    	textAreaNewDesiredTemp.setVisible(false);
	    	                    	lblC.setEnabled(false);
	    	                    	lblC.setVisible(false);
	    	                    	btnAddThisProgram.setEnabled(false);
	    	                    	btnAddThisProgram.setVisible(false);
	    	                    	btnUpdateThisProgram.setEnabled(false);
	    	                    	btnUpdateThisProgram.setVisible(false);
			         	
			         	}
			         });
		         AIMManagementMode.setText("AIM Management Mode");
		         AIMManagementMode.addMouseListener(new java.awt.event.MouseAdapter(){
		         	public void mouseClicked(final java.awt.event.MouseEvent evt) {
		         		ManualMode.setSelected(false);
		         		lblSelectProgram.setEnabled(false);
		         		lblSelectProgram.setText("");
		         		lblStartTime.setEnabled(false);
		         		lblStartTime.setText("Start Time:");
		         		lblStopTime.setEnabled(false);
		         		lblStopTime.setText("Stop Time:");
		         		lblDesiredTemp.setEnabled(false);
		         		lblDesiredTemp.setText("Desired Temp: ");
		         		btnAddProgram.setEnabled(false);
		         		btnDeleteProgram.setEnabled(false);
		         		btnModifyProgram.setEnabled(false);
		         		ProgrammingModeComboBox.setEnabled(false);
		  	    	  String currentRoom = TemperatureRoomsComboBox.getSelectedItem().toString();
	    		    	  for(Room r:house.getRooms()){
	    		    		  if(r.getRoomName().equals(currentRoom)){
	    		    			  r.setHomeManagementMode(HomeManagementMode.AIM_Mode);
	    			         		lblSelectProgram.setEnabled(false);
	    		    		  }
	    		    	  }
		             }
		         	
		         	
		         });
		         AIMManagementMode.setBounds(10,60,200,20);
		         TemperatureManagementPanel.add(AIMManagementMode);
		         
		        
		         ManualMode.setText("Manual Mode");
		         ManualMode.addMouseListener(new java.awt.event.MouseAdapter(){
		         	public void mouseClicked(final java.awt.event.MouseEvent evt) {
		         		AIMManagementMode.setSelected(false);
		         		btnAddProgram.setEnabled(true);
		         		
		         		if(ProgrammingModeComboBox.getItemCount()!=0){
		         			ProgrammingModeComboBox.removeAllItems();
		         		}
		         		
		         		
			         	if(!house.getRooms().isEmpty()){
			         		for(Room r : house.getRooms()){
			         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
			         				
			         				r.setHomeManagementMode(HomeManagementMode.Manual);
			         				if(r.getHomeManagmenetMode()==HomeManagementMode.Manual){
			         					
			         					if(!r.getProgrammingMode().isEmpty()){
			         						for(HeatingManagementTime t :r.getProgrammingMode()){
			         							
			         							ProgrammingModeComboBox.addItem(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes());
			         						}
			         					}
			         				}
			         			}
			         		}
			         	}
			         	
			         	ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
			         	if(ProgrammingModeComboBox.getSelectedItem()==null){
			         		btnDeleteProgram.setEnabled(false);
			         		btnModifyProgram.setEnabled(false);
			         	}
			         	else{
			         		btnDeleteProgram.setEnabled(true);
			         		btnModifyProgram.setEnabled(true);
			         	}
			         	
			         	if(ProgrammingModeComboBox.getSelectedItem()==null && ManualMode.isSelected()){
			         		lblSelectProgram.setEnabled(true);
			         		lblSelectProgram.setText("No Program Available!");
			         		lblStartTime.setEnabled(true);
			         		lblStartTime.setText("Start Time: ");
			         		lblStopTime.setEnabled(true);
			         		lblStopTime.setText("Stop Time: ");
			         		lblDesiredTemp.setEnabled(true);
			         		lblDesiredTemp.setText("Desired Temperature: ");
			         		btnAddProgram.setEnabled(true);
			         	}
			         	if(ProgrammingModeComboBox.getSelectedItem()==null || AIMManagementMode.isSelected()){
			         		ProgrammingModeComboBox.setEnabled(false);
			         	}
			         	else{
			         		btnAddProgram.setEnabled(true);
			         		ProgrammingModeComboBox.setEnabled(true);
			         		ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
			         		lblSelectProgram.setEnabled(true);
			         		lblSelectProgram.setText("Select an Heating Program!");
			         		lblStartTime.setEnabled(true);
			         		lblStopTime.setEnabled(true);
			         		lblDesiredTemp.setEnabled(true);
			         		for(Room r : house.getRooms()){
			         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
			         					if(!r.getProgrammingMode().isEmpty()){
			         						for(HeatingManagementTime t :r.getProgrammingMode()){
			         							if(ProgrammingModeComboBox.getSelectedItem().toString().equals(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes()))
			         							{
			         								lblStartTime.setText("Start Time: "+t.getBeginningHour()+":"+t.getBeginningMinutes());
			         								lblStopTime.setText("Stop Time: "+t.getStoppingHour()+":"+t.getStoppingMinutes());
			         								lblDesiredTemp.setText("Desired Temperature: "+t.getTemp()+" °C");
			         							}
			         						}
			         					}
			         			}
			         		}
			         	}
		         		
	    		    	  String currentRoom = TemperatureRoomsComboBox.getSelectedItem().toString();
	    		    	  for(Room r:house.getRooms()){
	    		    		  if(r.getRoomName().equals(currentRoom)){
	    		    			  
	    			         		lblSelectProgram.setEnabled(true);
	    			         		if(ProgrammingModeComboBox.getSelectedItem()!=null)
	    			         			lblSelectProgram.setText("Select an Heating Program!");
	    			         		else
	    			         			lblSelectProgram.setText("No Program Available!");
	    		    		  }
	    		    	  }
	                    	textAreaNewHourStartTime.setEnabled(false);
	                    	textAreaNewHourStartTime.setVisible(false);
	                    	lbl2dot1.setEnabled(false);
	                    	lbl2dot1.setVisible(false);
	                    	textAreaNewMinutesStartTime.setEnabled(false);
	                    	textAreaNewMinutesStartTime.setVisible(false);
	                    	textAreaNewHourStopTime.setEnabled(false);
	                    	textAreaNewHourStopTime.setVisible(false);
	                    	lbl2dot2.setEnabled(false);
	                    	lbl2dot2.setVisible(false);
	                    	textAreaNewMinutesStopTime.setEnabled(false);
	                    	textAreaNewMinutesStopTime.setVisible(false);
	                    	textAreaNewDesiredTemp.setEnabled(false);
	                    	textAreaNewDesiredTemp.setVisible(false);
	                    	lblC.setEnabled(false);
	                    	lblC.setVisible(false);
	                    	btnAddThisProgram.setEnabled(false);
	                    	btnAddThisProgram.setVisible(false);
	                    	btnUpdateThisProgram.setEnabled(false);
	                    	btnUpdateThisProgram.setVisible(false);
		         	}
		         });
		         ManualMode.setBounds(240,60,150,20);
		         TemperatureManagementPanel.add(ManualMode);
		         
		         	
		          
		         	
		         	ProgrammingModeComboBox.setName("ProgrammingModeComboBox");
		         	
		         	if(ProgrammingModeComboBox.getSelectedItem()!=null)
		         		ProgrammingModeComboBox.removeAllItems();
		         	
		         	if(!house.getRooms().isEmpty()){
		         		for(Room r : house.getRooms()){
		         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
		         				if(r.getHomeManagmenetMode()==HomeManagementMode.Manual){
		         					
		         					if(!r.getProgrammingMode().isEmpty()){
		         						for(HeatingManagementTime t :r.getProgrammingMode()){
		         							ProgrammingModeComboBox.addItem(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes());
		         							
		         						}
		         					}
		         				}
		         			}
		         		}
		         	}
		        	ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
		         	
		         	lblSelectProgram.setText("");
		         	lblSelectProgram.setBounds(10,103,200,20);
		         	TemperatureManagementPanel.add(lblSelectProgram);
		         	lblSelectProgram.setEnabled(false);
		         	
		         	lblStartTime.setText("Start Time: ");
		         	lblStartTime.setBounds(10,132,200,20);
		         	TemperatureManagementPanel.add(lblStartTime);
		         	lblStartTime.setEnabled(false);
		         	
		         	lblStopTime.setText("Stop Time: ");
		         	lblStopTime.setBounds(10,162,200,20);
		         	TemperatureManagementPanel.add(lblStopTime);
		         	lblStopTime.setEnabled(false);
		         	
		         	lblDesiredTemp.setText("Desired Temperature: ");
		         	lblDesiredTemp.setBounds(10,192,200,20);
		         	TemperatureManagementPanel.add(lblDesiredTemp);
		         	lblDesiredTemp.setEnabled(false);
		         	
		         	textAreaNewHourStartTime.setBounds(85,134,20,20);
		         	textAreaNewHourStartTime.setBorder(BorderFactory.createEtchedBorder());
		         	TemperatureManagementPanel.add(textAreaNewHourStartTime);
		         	textAreaNewHourStartTime.setEnabled(false);
		         	textAreaNewHourStartTime.setVisible(false);
		         	
		         	lbl2dot1.setText(" : ");
		         	lbl2dot1.setBounds(106,132,20,20);
		         	TemperatureManagementPanel.add(lbl2dot1);
		         	lbl2dot1.setVisible(false);
		         	
		         	textAreaNewMinutesStartTime.setBounds(117,134,20,20);
		         	textAreaNewMinutesStartTime.setBorder(BorderFactory.createEtchedBorder());
		         	TemperatureManagementPanel.add(textAreaNewMinutesStartTime);
		         	textAreaNewMinutesStartTime.setEnabled(false);
		         	textAreaNewMinutesStartTime.setVisible(false);
		         	
		         	textAreaNewHourStopTime.setBounds(85,164,20,20);
		         	textAreaNewHourStopTime.setBorder(BorderFactory.createEtchedBorder());
		         	TemperatureManagementPanel.add(textAreaNewHourStopTime);
		         	textAreaNewHourStopTime.setEnabled(false);
		         	textAreaNewHourStopTime.setVisible(false);
		         	
		         	lbl2dot2.setText(" : ");
		         	lbl2dot2.setBounds(106,162,20,20);
		         	TemperatureManagementPanel.add(lbl2dot2);
		         	lbl2dot2.setVisible(false);
		         	
		         	textAreaNewMinutesStopTime.setBounds(117,164,20,20);
		         	textAreaNewMinutesStopTime.setBorder(BorderFactory.createEtchedBorder());
		         	TemperatureManagementPanel.add(textAreaNewMinutesStopTime);
		         	textAreaNewMinutesStopTime.setEnabled(false);
		         	textAreaNewMinutesStopTime.setVisible(false);
		         	
		         	
		         	textAreaNewDesiredTemp.setBounds(155,194,20,20);
		         	textAreaNewDesiredTemp.setBorder(BorderFactory.createEtchedBorder());
		         	TemperatureManagementPanel.add(textAreaNewDesiredTemp);
		         	textAreaNewDesiredTemp.setEnabled(false);
		         	textAreaNewDesiredTemp.setVisible(false);
		         	
		         	lblC.setText(" °C ");
		         	lblC.setBounds(176,187,30,30);
		         	TemperatureManagementPanel.add(lblC);
		         	lblC.setVisible(false);
		         	
	            	btnAddThisProgram.setText("Add This Program");
	            	btnAddThisProgram.setName("btnAddThisProgram");
	            	btnAddThisProgram.setBounds(242,160,150,30);
	            	btnAddThisProgram.setEnabled(false);
	            	btnAddThisProgram.setVisible(false);
	            	btnAddThisProgram.addMouseListener(new java.awt.event.MouseAdapter() {

	                    public void mouseClicked(final java.awt.event.MouseEvent evt) {
	                    	
	                     	if (textAreaNewHourStartTime.getText().equals("") || textAreaNewMinutesStartTime.getText().equals("") || textAreaNewHourStopTime.getText().equals("") || textAreaNewMinutesStopTime.getText().equals("") || textAreaNewDesiredTemp.getText().equals(""))
	                     	{
	                     		JFrame frameDialogue = new JFrame();
	                     		JOptionPane.showMessageDialog(frameDialogue, "Please type a valid Heating Program!");
	                     		System.out.println("spazio vuoto!");
	                     	}
	                     	else{
	                     		
	                     		int newHourStartTime=0;
	                     		int newMinutesStartTime=0;
	                     		int newHourStopTime=0;
	                     		int newMinutesStopTime=0;
	                     		int newDesiredTemp=0;

	    	                    try{
	    	                    	newHourStartTime = Integer.parseInt(textAreaNewHourStartTime.getText());
	    	                    	newMinutesStartTime =Integer.parseInt(textAreaNewMinutesStartTime.getText());
	    	                    	newHourStopTime=Integer.parseInt(textAreaNewHourStopTime.getText());
	    	                    	newMinutesStopTime=Integer.parseInt(textAreaNewMinutesStopTime.getText());
	    	                    	newDesiredTemp=Integer.parseInt(textAreaNewDesiredTemp.getText());
	    	                    	
	    	                    	if(controlTime(newHourStartTime,newMinutesStartTime) && controlTime (newHourStopTime,newMinutesStopTime)){
		    	                    	
		    	                    	for(Room r:house.getRooms()){
		    	        		    		  if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
		    	        		    			  r.addTime(newHourStartTime, newMinutesStartTime, newHourStopTime, newMinutesStopTime, newDesiredTemp);
		    	        		    		  }
		    	        		    	  }
		    	          
		    	                    	btnAddProgram.setEnabled(true);
		    	                    	btnDeleteProgram.setEnabled(true);
		    	                    	btnModifyProgram.setEnabled(true);
		    	                    	
		    	                    	ProgrammingModeComboBox.addItem(newHourStartTime+":"+newMinutesStartTime+"-"+newHourStopTime+":"+newMinutesStopTime);
		    	                    
		    	                    	ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
		    	                    	textAreaNewHourStartTime.setEnabled(false);
		    	                    	textAreaNewHourStartTime.setVisible(false);
		    	                    	lbl2dot1.setEnabled(false);
		    	                    	lbl2dot1.setVisible(false);
		    	                    	textAreaNewMinutesStartTime.setEnabled(false);
		    	                    	textAreaNewMinutesStartTime.setVisible(false);
		    	                    	textAreaNewHourStopTime.setEnabled(false);
		    	                    	textAreaNewHourStopTime.setVisible(false);
		    	                    	lbl2dot2.setEnabled(false);
		    	                    	lbl2dot2.setVisible(false);
		    	                    	textAreaNewMinutesStopTime.setEnabled(false);
		    	                    	textAreaNewMinutesStopTime.setVisible(false);
		    	                    	textAreaNewDesiredTemp.setEnabled(false);
		    	                    	textAreaNewDesiredTemp.setVisible(false);
		    	                    	lblC.setEnabled(false);
		    	                    	lblC.setVisible(false);
		    	                    	btnAddThisProgram.setEnabled(false);
		    	                    	btnAddThisProgram.setVisible(false);
		    			         		
		    	                    	for(Room r : house.getRooms()){
		    			         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
		    			         					if(!r.getProgrammingMode().isEmpty()){
		    			         						for(HeatingManagementTime t :r.getProgrammingMode()){
		    			         							if(ProgrammingModeComboBox.getSelectedItem().toString().equals(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes()))
		    			         							{
		    			         								lblStartTime.setText("Start Time: "+t.getBeginningHour()+":"+t.getBeginningMinutes());
		    			         								lblStopTime.setText("Stop Time: "+t.getStoppingHour()+":"+t.getStoppingMinutes());
		    			         								lblDesiredTemp.setText("Desired Temperature: "+t.getTemp()+" °C");
		    			         							}
		    			         						}
		    			         					}
		    			         			}
		    			         		}
		    			         		ProgrammingModeComboBox.setEnabled(true);
		    	                    	lblSelectProgram.setText("Select a Program!");
		    	                    	}
	    	                       	else {
	    	                    		JFrame frameDialogue = new JFrame();
	    	                    		JOptionPane.showMessageDialog(frameDialogue, "Please type a valid day time!");
	    	                    	}
	    	     
	    	                    	}
	    	                    	catch(Exception a){
	    	                    		JFrame frameDialogue = new JFrame();
	    	                    		JOptionPane.showMessageDialog(frameDialogue, "Please type a valid Heating Program!");
	    	                    	}
	                     	}
	                    	
	                    }
	                });
	            	TemperatureManagementPanel.add(btnAddThisProgram);
		         	         //BOTTONI
				    btnAddProgram.setEnabled(false);
				    btnAddProgram.setName("btnAddProgram"); 
				    btnAddProgram.setText("Add Program");
				    btnAddProgram.addMouseListener(new java.awt.event.MouseAdapter() {
           
				    	public void mouseClicked(final java.awt.event.MouseEvent evt) {
				    		
				    		btnDeleteProgram.setEnabled(false);
				    		btnModifyProgram.setEnabled(false);
				    		btnAddProgram.setEnabled(false);
				    		lblStartTime.setText("Start Time: ");
				    		lblStopTime.setText("Stop Time: ");
				    		lblDesiredTemp.setText("Desired Temperature: ");
				         	textAreaNewHourStartTime.setEnabled(true);
				         	textAreaNewHourStartTime.setVisible(true);
				         	textAreaNewHourStartTime.setText("");
				         	lbl2dot1.setVisible(true);
				         	lbl2dot1.setEnabled(true);
				         	textAreaNewMinutesStartTime.setEnabled(true);
				         	textAreaNewMinutesStartTime.setVisible(true);
				         	textAreaNewMinutesStartTime.setText("");
				         	textAreaNewHourStopTime.setEnabled(true);
				         	textAreaNewHourStopTime.setVisible(true);
				         	textAreaNewHourStopTime.setText("");
				         	lbl2dot2.setVisible(true);
				         	lbl2dot2.setEnabled(true);
				         	textAreaNewMinutesStopTime.setEnabled(true);
				         	textAreaNewMinutesStopTime.setVisible(true);
				         	textAreaNewMinutesStopTime.setText("");
				         	textAreaNewDesiredTemp.setEnabled(true);
				         	textAreaNewDesiredTemp.setVisible(true);
				         	textAreaNewDesiredTemp.setText("");
				         	lblC.setVisible(true);
				         	lblC.setEnabled(true);
			            	btnAddThisProgram.setEnabled(true);
			            	btnAddThisProgram.setVisible(true);

            }
        });
				    btnAddProgram.setBounds(45, 222, 350, 20);
				    TemperatureManagementPanel.add(btnAddProgram);
				    
				    
				    btnDeleteProgram.setEnabled(false);
				    btnDeleteProgram.setName("btnDeleteProgram"); 
				    btnDeleteProgram.setText("Delete Program");
				    btnDeleteProgram.addMouseListener(new java.awt.event.MouseAdapter() {
           
				    	public void mouseClicked(final java.awt.event.MouseEvent evt) {
		                	
				    	
				    		HeatingManagementTime removingProgram=null;
				    		if(ProgrammingModeComboBox.getSelectedItem()!=null){
					    		for(Room r : house.getRooms()){
				         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
				         					if(!r.getProgrammingMode().isEmpty()){
				         						for(HeatingManagementTime t :r.getProgrammingMode()){
				         							if(ProgrammingModeComboBox.getSelectedItem().toString().equals(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes()))
				         							{
				         								removingProgram=t;
				         							}
				         						}
				         					}
				         			}
				         		}
				    		}
				    		
				    		for(Room r : house.getRooms()){
			         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
			         				r.removeTime(removingProgram);
			         			}
			         		}
			    		
				    		ProgrammingModeComboBox.removeItem(removingProgram.getBeginningHour()+":"+removingProgram.getBeginningMinutes()+"-"+removingProgram.getStoppingHour()+":"+removingProgram.getStoppingMinutes());
				    		

				         	ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
				         	if(ProgrammingModeComboBox.getSelectedItem()==null){
				         		btnDeleteProgram.setEnabled(false);
				         		btnModifyProgram.setEnabled(false);
				         	}
				         	else{
				         		btnDeleteProgram.setEnabled(true);
				         		btnModifyProgram.setEnabled(true);
				         	}
				         	
				         	if(ProgrammingModeComboBox.getSelectedItem()==null && ManualMode.isSelected()){
				         		lblSelectProgram.setEnabled(true);
				         		lblSelectProgram.setText("No Program Available!");
				         		lblStartTime.setEnabled(true);
				         		lblStartTime.setText("Start Time: ");
				         		lblStopTime.setEnabled(true);
				         		lblStopTime.setText("Stop Time: ");
				         		lblDesiredTemp.setEnabled(true);
				         		lblDesiredTemp.setText("Desired Temperature: ");
				         		btnAddProgram.setEnabled(true);
				         	}
				         	if(ProgrammingModeComboBox.getSelectedItem()==null || AIMManagementMode.isSelected()){
				         		ProgrammingModeComboBox.setEnabled(false);
				         	}
				         	else{
				         		btnAddProgram.setEnabled(true);
				         		ProgrammingModeComboBox.setEnabled(true);
				         		ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
				         		lblSelectProgram.setEnabled(true);
				         		lblSelectProgram.setText("Select an Heating Program!");
				         		lblStartTime.setEnabled(true);
				         		lblStopTime.setEnabled(true);
				         		lblDesiredTemp.setEnabled(true);
				         		for(Room r : house.getRooms()){
				         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
				         					if(!r.getProgrammingMode().isEmpty()){
				         						for(HeatingManagementTime t :r.getProgrammingMode()){
				         							if(ProgrammingModeComboBox.getSelectedItem().toString().equals(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes()))
				         							{
				         								lblStartTime.setText("Start Time: "+t.getBeginningHour()+":"+t.getBeginningMinutes());
				         								lblStopTime.setText("Stop Time: "+t.getStoppingHour()+":"+t.getStoppingMinutes());
				         								lblDesiredTemp.setText("Desired Temperature: "+t.getTemp()+" °C");
				         							}
				         						}
				         					}
				         			}
				         		}
				         	}
				    	}
				    });
				    btnDeleteProgram.setBounds(45, 252, 350, 20);
				    TemperatureManagementPanel.add(btnDeleteProgram);
        
	            	btnUpdateThisProgram.setText("Update This Program");
	            	btnUpdateThisProgram.setName("btnUpdateThisProgram");
	            	btnUpdateThisProgram.setBounds(210,160,182,30);
	            	btnUpdateThisProgram.setEnabled(false);
	            	btnUpdateThisProgram.setVisible(false);
	            	btnUpdateThisProgram.addMouseListener(new java.awt.event.MouseAdapter() {

	                    public void mouseClicked(final java.awt.event.MouseEvent evt) {
	                    	
	                    	
	    		    		HeatingManagementTime RemovingProgram=null;
				    		if(ProgrammingModeComboBox.getSelectedItem()!=null){
					    		for(Room r : house.getRooms()){
				         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
				         					if(!r.getProgrammingMode().isEmpty()){
				         						for(HeatingManagementTime t :r.getProgrammingMode()){
				         							if(ProgrammingModeComboBox.getSelectedItem().toString().equals(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes()))
				         							{
				         								RemovingProgram=t;
				         							}
				         						}
				         					}
				         			}
				         		}
				    		}
	                     	if (textAreaNewHourStartTime.getText().equals("") || textAreaNewMinutesStartTime.getText().equals("") || textAreaNewHourStopTime.getText().equals("") || textAreaNewMinutesStopTime.getText().equals("") || textAreaNewDesiredTemp.getText().equals(""))
	                     	{
	                     		JFrame frameDialogue = new JFrame();
	                     		JOptionPane.showMessageDialog(frameDialogue, "Please type a valid Heating Program!");
	                     		System.out.println("spazio vuoto!");
	                     	}
	                     	else{
	                     		
	                     		int newHourStartTime=0;
	                     		int newMinutesStartTime=0;
	                     		int newHourStopTime=0;
	                     		int newMinutesStopTime=0;
	                     		int newDesiredTemp=0;

	    	                    try{
	    	                    	newHourStartTime = Integer.parseInt(textAreaNewHourStartTime.getText());
	    	                    	newMinutesStartTime =Integer.parseInt(textAreaNewMinutesStartTime.getText());
	    	                    	newHourStopTime=Integer.parseInt(textAreaNewHourStopTime.getText());
	    	                    	newMinutesStopTime=Integer.parseInt(textAreaNewMinutesStopTime.getText());
	    	                    	newDesiredTemp=Integer.parseInt(textAreaNewDesiredTemp.getText());
	    	                    	for(Room r:house.getRooms()){
	    	        		    		  if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
	    	        		    			  r.UpdateTime(RemovingProgram,newHourStartTime, newMinutesStartTime, newHourStopTime, newMinutesStopTime, newDesiredTemp);
	    	        		    		  }
	    	        		    	  }
	    	          
	    	                 		if(ProgrammingModeComboBox.getItemCount()!=0){
	    			         			ProgrammingModeComboBox.removeAllItems();
	    			         		}
	    			         		
	    			         		
	    				         	if(!house.getRooms().isEmpty()){
	    				         		for(Room r : house.getRooms()){
	    				         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
	    				         				
	    				         				r.setHomeManagementMode(HomeManagementMode.Manual);
	    				         				if(r.getHomeManagmenetMode()==HomeManagementMode.Manual){
	    				         					
	    				         					if(!r.getProgrammingMode().isEmpty()){
	    				         						for(HeatingManagementTime t :r.getProgrammingMode()){
	    				         							
	    				         							ProgrammingModeComboBox.addItem(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes());
	    				         						}
	    				         					}
	    				         				}
	    				         			}
	    				         		}
	    				         	}
	    				         	
	    				         	ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
	    				         	if(ProgrammingModeComboBox.getSelectedItem()==null){
	    				         		btnDeleteProgram.setEnabled(false);
	    				         		btnModifyProgram.setEnabled(false);
	    				         	}
	    				         	else{
	    				         		btnDeleteProgram.setEnabled(true);
	    				         		btnModifyProgram.setEnabled(true);
	    				         	}
	    				         	
	    				         	if(ProgrammingModeComboBox.getSelectedItem()==null && ManualMode.isSelected()){
	    				         		lblSelectProgram.setEnabled(true);
	    				         		lblSelectProgram.setText("No Program Available!");
	    				         		lblStartTime.setEnabled(true);
	    				         		lblStartTime.setText("Start Time: ");
	    				         		lblStopTime.setEnabled(true);
	    				         		lblStopTime.setText("Stop Time: ");
	    				         		lblDesiredTemp.setEnabled(true);
	    				         		lblDesiredTemp.setText("Desired Temperature: ");
	    				         		btnAddProgram.setEnabled(true);
	    				         	}
	    				         	if(ProgrammingModeComboBox.getSelectedItem()==null || AIMManagementMode.isSelected()){
	    				         		ProgrammingModeComboBox.setEnabled(false);
	    				         	}
	    				         	else{
	    				         		btnAddProgram.setEnabled(true);
	    				         		ProgrammingModeComboBox.setEnabled(true);
	    				         		ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
	    				         		lblSelectProgram.setEnabled(true);
	    				         		lblSelectProgram.setText("Select an Heating Program!");
	    				         		lblStartTime.setEnabled(true);
	    				         		lblStopTime.setEnabled(true);
	    				         		lblDesiredTemp.setEnabled(true);
	    				         		for(Room r : house.getRooms()){
	    				         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
	    				         					if(!r.getProgrammingMode().isEmpty()){
	    				         						for(HeatingManagementTime t :r.getProgrammingMode()){
	    				         							if(ProgrammingModeComboBox.getSelectedItem().toString().equals(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes()))
	    				         							{
	    				         								lblStartTime.setText("Start Time: "+t.getBeginningHour()+":"+t.getBeginningMinutes());
	    				         								lblStopTime.setText("Stop Time: "+t.getStoppingHour()+":"+t.getStoppingMinutes());
	    				         								lblDesiredTemp.setText("Desired Temperature: "+t.getTemp()+" °C");
	    				         							}
	    				         						}
	    				         					}
	    				         			}
	    				         		}
	    				         	}
	    			         		
	    		    		    	  String currentRoom = TemperatureRoomsComboBox.getSelectedItem().toString();
	    		    		    	  for(Room r:house.getRooms()){
	    		    		    		  if(r.getRoomName().equals(currentRoom)){
	    		    		    			  
	    		    			         		lblSelectProgram.setEnabled(true);
	    		    			         		if(ProgrammingModeComboBox.getSelectedItem()!=null)
	    		    			         			lblSelectProgram.setText("Select an Heating Program!");
	    		    			         		else
	    		    			         			lblSelectProgram.setText("No Program Available!");
	    		    		    		  }
	    		    		    	  }
	    		                    	textAreaNewHourStartTime.setEnabled(false);
	    		                    	textAreaNewHourStartTime.setVisible(false);
	    		                    	lbl2dot1.setEnabled(false);
	    		                    	lbl2dot1.setVisible(false);
	    		                    	textAreaNewMinutesStartTime.setEnabled(false);
	    		                    	textAreaNewMinutesStartTime.setVisible(false);
	    		                    	textAreaNewHourStopTime.setEnabled(false);
	    		                    	textAreaNewHourStopTime.setVisible(false);
	    		                    	lbl2dot2.setEnabled(false);
	    		                    	lbl2dot2.setVisible(false);
	    		                    	textAreaNewMinutesStopTime.setEnabled(false);
	    		                    	textAreaNewMinutesStopTime.setVisible(false);
	    		                    	textAreaNewDesiredTemp.setEnabled(false);
	    		                    	textAreaNewDesiredTemp.setVisible(false);
	    		                    	lblC.setEnabled(false);
	    		                    	lblC.setVisible(false);
	    		                    	btnUpdateThisProgram.setEnabled(false);
	    		                    	btnUpdateThisProgram.setVisible(false);
	    	                    	}
	    	                    	catch(Exception a){
	    	                    		JFrame frameDialogue = new JFrame();
	    	                    		JOptionPane.showMessageDialog(frameDialogue, "Please type a valid Heating Program!");
	    	                    	}
	                     	}
	                    	
	                    }
	                });
	            	TemperatureManagementPanel.add(btnUpdateThisProgram); 
				    
				    
				    btnModifyProgram.setEnabled(false);
				    btnModifyProgram.setName("btnModifyProgram"); 
				    btnModifyProgram.setText("Modify Program");
				    btnModifyProgram.addMouseListener(new java.awt.event.MouseAdapter() {
           
				    	public void mouseClicked(final java.awt.event.MouseEvent evt) {
				    		
				    		HeatingManagementTime RemovingProgram=null;
				    		if(ProgrammingModeComboBox.getSelectedItem()!=null){
					    		for(Room r : house.getRooms()){
				         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
				         					if(!r.getProgrammingMode().isEmpty()){
				         						for(HeatingManagementTime t :r.getProgrammingMode()){
				         							if(ProgrammingModeComboBox.getSelectedItem().toString().equals(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes()))
				         							{
				         								RemovingProgram=t;
				         							}
				         						}
				         					}
				         			}
				         		}
				    		}
				    		
				    		btnDeleteProgram.setEnabled(false);
				    		btnModifyProgram.setEnabled(false);
				    		btnAddProgram.setEnabled(false);
				    		lblStartTime.setText("Start Time: ");
				    		lblStopTime.setText("Stop Time: ");
				    		lblDesiredTemp.setText("Desired Temperature: ");
				         	textAreaNewHourStartTime.setEnabled(true);
				         	textAreaNewHourStartTime.setVisible(true);
				         	textAreaNewHourStartTime.setText(String.valueOf(RemovingProgram.getBeginningHour()));
				         	lbl2dot1.setVisible(true);
				         	lbl2dot1.setEnabled(true);
				         	textAreaNewMinutesStartTime.setEnabled(true);
				         	textAreaNewMinutesStartTime.setVisible(true);
				         	textAreaNewMinutesStartTime.setText(String.valueOf(RemovingProgram.getBeginningMinutes()));
				         	textAreaNewHourStopTime.setEnabled(true);
				         	textAreaNewHourStopTime.setVisible(true);
				         	textAreaNewHourStopTime.setText(String.valueOf(RemovingProgram.getStoppingHour()));
				         	lbl2dot2.setVisible(true);
				         	lbl2dot2.setEnabled(true);
				         	textAreaNewMinutesStopTime.setEnabled(true);
				         	textAreaNewMinutesStopTime.setVisible(true);
				         	textAreaNewMinutesStopTime.setText(String.valueOf(RemovingProgram.getStoppingMinutes()));
				         	textAreaNewDesiredTemp.setEnabled(true);
				         	textAreaNewDesiredTemp.setVisible(true);
				         	textAreaNewDesiredTemp.setText(String.valueOf(RemovingProgram.getTemp()));
				         	lblC.setVisible(true);
				         	lblC.setEnabled(true);
			            	btnUpdateThisProgram.setEnabled(true);
			            	btnUpdateThisProgram.setVisible(true);
			            	
			            }
			        });
				    btnModifyProgram.setBounds(45, 282, 350, 20);
				    TemperatureManagementPanel.add(btnModifyProgram);
				    
				    
				    
		         	if(ProgrammingModeComboBox.getSelectedItem()==null){
		         		btnDeleteProgram.setEnabled(false);
		         		btnModifyProgram.setEnabled(false);
		         	}
		         	else {
		         		btnDeleteProgram.setEnabled(true);
		         		btnModifyProgram.setEnabled(true);
		         	}
		         	if(ProgrammingModeComboBox.getSelectedItem()==null && ManualMode.isSelected()){
		         		lblSelectProgram.setEnabled(true);
		         		lblSelectProgram.setText("No Program Available!");
		         		lblStartTime.setEnabled(true);
		         		lblStartTime.setText("Start Time: ");
		         		lblStopTime.setEnabled(true);
		         		lblStopTime.setText("Stop Time: ");
		         		lblDesiredTemp.setEnabled(true);
		         		lblDesiredTemp.setText("Desired Temperature: ");
		         		btnAddProgram.setEnabled(true);
		         	}
		         	if(ProgrammingModeComboBox.getSelectedItem()==null || AIMManagementMode.isSelected()){
		         		ProgrammingModeComboBox.setEnabled(false);
		         	}
		         	else{
		         		btnAddProgram.setEnabled(true);
		         		ProgrammingModeComboBox.setEnabled(true);
		         		ProgrammingModeComboBox.setSelectedIndex(ProgrammingModeComboBox.getItemCount()-1);
		         		lblSelectProgram.setEnabled(true);
		         		lblSelectProgram.setText("Selecet an Heating Program!");
		         		lblStartTime.setEnabled(true);
		         		lblStopTime.setEnabled(true);
		         		lblDesiredTemp.setEnabled(true);
		         		for(Room r : house.getRooms()){
		         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
		         					if(!r.getProgrammingMode().isEmpty()){
		         						for(HeatingManagementTime t :r.getProgrammingMode()){
		         							if(ProgrammingModeComboBox.getSelectedItem().toString().equals(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes()))
		         							{
		         								lblStartTime.setText("Start Time: "+t.getBeginningHour()+":"+t.getBeginningMinutes());
		         								lblStopTime.setText("Stop Time: "+t.getStoppingHour()+":"+t.getStoppingMinutes());
		         								lblDesiredTemp.setText("Desired Temperature: "+t.getTemp()+" °C");
		         							}
		         						}
		         					}
		         			}
		         		}
		         	}
		         

		    	  ProgrammingModeComboBox.addActionListener(new java.awt.event.ActionListener() {
		    		      public void actionPerformed(ActionEvent actionEvent) {
		    		    	  
		    		    	  if(ProgrammingModeComboBox.getItemCount()!=0){
				         		for(Room r : house.getRooms()){
				         			if(r.getRoomName().equals(TemperatureRoomsComboBox.getSelectedItem().toString())){
				         				
				         				if(!r.getProgrammingMode().isEmpty()){
				         						
				         					for(HeatingManagementTime t :r.getProgrammingMode()){
				         							if(ProgrammingModeComboBox.getSelectedItem().toString().equals(t.getBeginningHour()+":"+t.getBeginningMinutes()+"-"+t.getStoppingHour()+":"+t.getStoppingMinutes()))
				         							{
				         								
				         								lblStartTime.setText("Start Time: "+t.getBeginningHour()+":"+t.getBeginningMinutes());
				         								lblStopTime.setText("Stop Time: "+t.getStoppingHour()+":"+t.getStoppingMinutes());
				         								lblDesiredTemp.setText("Desired Temperature: "+t.getTemp()+" °C");
				         							}
				         					}
				         				}
				         			}
				         		}
		    		    	  }
		    		      }
		    	  });
		    	  ProgrammingModeComboBox.setBounds(250,100,120,30);
		    	  TemperatureManagementPanel.add(ProgrammingModeComboBox);
		           

		          
	             
	              btnOK.setEnabled(true);
	              btnOK.setVisible(true);
	              btnOK.setName("btnOK");
	              btnOK.setText("OK");
	              btnOK.addMouseListener(new java.awt.event.MouseAdapter() {
	                  public void mouseClicked(final java.awt.event.MouseEvent evt) {

	                	  TemperatureSettingsFrame.dispose();
	                    }
	              });
	              btnOK.setBounds(200,490,90,30);
	              TemperatureSettingsFrame.add(btnOK);
		    	  
	              
	              lblTitle.setBounds(110, 10, 300, 30);
	              lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
	              lblTitle.setFont(new Font("DejaVu Sans",Font.BOLD,15));
	              TemperatureSettingsFrame.add(lblTitle);
         
	              lblImageTemperature=new JLabel();
	              TemperatureImage=new ImageIcon(images_path + "Temperature.png");
	              lblImageTemperature.setBounds(440,5,TemperatureImage.getIconWidth()+5,TemperatureImage.getIconWidth()+5);
	              lblImageTemperature.setIcon(TemperatureImage);
	              lblImageTemperature.setBorder(BorderFactory.createEtchedBorder());
	              TemperatureSettingsFrame.add(lblImageTemperature);
				

		 }
		 
		 public boolean controlTime(int hour,int minutes){
			 if(hour>-1 && hour<25 && minutes>-1 && minutes<61)
				 return true;
			 else
				 return false;
		 }

		 
		 public void showDeviceInfo(DeviceType t){
			 final JTabbedPane RoomTabbedPanel= new JTabbedPane();
			 final JFrame deviceFrame;
			 final JLabel roomName=new JLabel();
			 final JLabel DeviceType=new JLabel();
			 deviceFrame = new JFrame("Device Inforamtion");
			 deviceFrame.setBounds(400, 400, 550,550);
			 deviceFrame.setLayout(null);
			 
			 roomName.setText("Room Selected: "+selectedRoom.getRoomName());
			 roomName.setBounds(10,10,200,20);
			 deviceFrame.add(roomName);
			 
			 DeviceType.setText("Device Selected: "+t.toString());
			 DeviceType.setBounds(280,10,200,20);
			 deviceFrame.add(DeviceType);
			 
			 RoomTabbedPanel.add("Daily Consumpition",updateDailyConsumption(t));
			 RoomTabbedPanel.addTab("Daily DeviceStatus", updateDailyDeviceStatus(t));
			 RoomTabbedPanel.setBounds(50, 50, 450, 450);
		     deviceFrame.add(RoomTabbedPanel);
			 deviceFrame.setVisible(true);
		     
		 }
		 
		 
			private JPanel updateDailyConsumption(DeviceType t){
				
				JPanel grPanel = new JPanel();
		  		XYSeries grSeries = new XYSeries("Room Daily Consumption");
		  		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		  		
		  		for(DeviceImpl d:selectedRoom.getDevices()){
		  			try {
						if(d.getDeviceType()==t){
							double[] s=d.getDailyDeviceConsumption();
						 	int counter=selectedRoom.getCounter();
						 	if(counter!=0){
							for(int i=0;i<counter;i++)
							 {
							 	grSeries.add(i*30,s[i]);
							 }
						}
		}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

		  		  XYSeriesCollection dataset = new XYSeriesCollection();
		          dataset.addSeries(grSeries);
	          
	        
	          
		          JFreeChart chart = ChartFactory.createXYLineChart(null, "Seconds","Watt-hour", dataset,PlotOrientation.VERTICAL, false, true, false  );

		          ChartPanel chartPanel = new ChartPanel(chart);
			  	  chartPanel.setPreferredSize(new Dimension(420,420));
			  	  grPanel.add(chartPanel);
	  		 	}
		  		return grPanel;
		  	}
			
			 
			private JPanel updateDailyDeviceStatus(DeviceType t){
				
				JPanel grPanel = new JPanel();
		  		XYSeries grSeries = new XYSeries("Room Daily Device WorkingMode");
		  		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		  		
		  		for(DeviceImpl d:selectedRoom.getDevices()){
		  			try {
						if(d.getDeviceType()==t){
							DeviceStatus[] s=d.getDailyDeviceWorkingMode();
						 	int counter=selectedRoom.getCounter();
						 	if(counter!=0){
							for(int i=0;i<counter;i++)
							 {
								
								if(s[i]==DeviceStatus.ON)
									grSeries.add(i*30,2);
								if(s[i]==DeviceStatus.OFF)
									grSeries.add(i*30,0);
								if(s[i]==DeviceStatus.STANDBY)
									grSeries.add(i*30,1);
								
							 }
						}
		}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

		  		  XYSeriesCollection dataset = new XYSeriesCollection();
		          dataset.addSeries(grSeries);
	          
	        
	          
		          JFreeChart chart = ChartFactory.createXYLineChart(null, "Seconds","DeviceStatus", dataset,PlotOrientation.VERTICAL, false, true, false  );
		          ChartPanel chartPanel = new ChartPanel(chart);
			  	  chartPanel.setPreferredSize(new Dimension(420,420));
			  	  grPanel.add(chartPanel);
	  		 	}
		  		return grPanel;
		  	}
			
			public void Subscribe(DVE dve) throws RemoteException {
				this.dve = dve;
				
				System.out.println("Si è sottoscritto un nuovo dve" + dve.toString());
			}
			
			public ArrayList<Device> getDevice(String room) throws RemoteException{
				
				ArrayList<Device> device=new ArrayList<Device>();
				boolean found=false;
				for(Room r:house.getRooms()){
					if(r.getRoomName().equals(room)){
						for(DeviceImpl d:r.getDevices()){
							device.add((Device) d);
						}
						found=true;
					}
				}
				if(found==true)
					return device;
				else
					return null;
			}
			
			public ArrayList<String> getRoomsName() throws RemoteException{
				ArrayList<String> roomsName=new ArrayList<String>();
				
				for(Room r:house.getRooms())
					roomsName.add(r.getRoomName());
				
				if(roomsName.size()!=0)
					return roomsName;
				else
					return null;
			}
			
			public void newPresenceValue (String roomName,Object obj) throws RemoteException{
				
				int presenceValue=0;
				Byte i;
				i =(Byte) obj;
				presenceValue= i;
				System.out.println("Numero persone: "+presenceValue + " rilevate nella stanza : "+ roomName );
		    	
				for(Room r:house.getRooms()){
		    		if(r.getRoomName().equals(roomName)){
		    			r.setPresenceValue(obj);
		    		}
		    	}
				if(systemStarted==true){
					house.updateValues();
					updateHouseValues();
			    	updateDrawRoomPresence();
			    	updateLabels();
					if (selectedRoom!=null){
						updateSelectedRoomValues();
						updateRoomImage();
						updateDevicePanel();
						updatePresenceGraphicPanel();
						updateConsumeGraphicPanel();
						DynamicUpdateTabbedPanel();
					}
				}
			}
			
			public void updateLabels(){
				for(DrawRoom d:drawRoomList){
					JLabel localLabel=new JLabel();
					localLabel=labelsRooms.get(d.getRoomName());
					if(d.getPresence()>0){
						localLabel.removeAll();
						localLabel.setIcon(new ImageIcon(d.getRoomImageOn()));
						System.out.println(d.getRoomName()+" "+d.getPresence());
					}else{
						localLabel.removeAll();
						localLabel.setIcon(new ImageIcon(d.getRoomImageOff()));
						System.out.println(d.getRoomName()+" "+d.getPresence());
					}
				}
			}
			
			public void updateRoomImage(){
				System.out.println("Faccio l'update delle immagini!");

				if (selectedRoom!=null){
					int one=0;
					int two=0;
					int three=0;
					int four=0;
					
					for (DeviceImpl localDevice:selectedRoom.getDevices()){
						try {
							if(localDevice.getDeviceType()==DeviceType.LIGHT){
								if(localDevice.getDeviceStatus()==DeviceStatus.ON)
									one=1;
							}
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							if(localDevice.getDeviceType()==DeviceType.HEATING){
								if(localDevice.getDeviceStatus()==DeviceStatus.ON)
									two=1;
							}
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if(selectedRoom.getPresenceValues()>0){
						three=1;
					
					if(selectedRoom.getTemperatureValues()>selectedRoom.getDesiredTemperature() && (selectedRoom.getTemperatureValues()-selectedRoom.getDesiredTemperature()>ACCEPTEDTEMPERATUREDIFFERENCE))
						four=1;
					else{
						if(selectedRoom.getTemperatureValues()<selectedRoom.getDesiredTemperature() && (selectedRoom.getDesiredTemperature()-selectedRoom.getTemperatureValues()>ACCEPTEDTEMPERATUREDIFFERENCE))
							four=2;
					}
					}
					selectedRoomImage.setIcon(new ImageIcon(images_path+"rooms/"+selectedRoom.getRoomType().toString()+""+one+""+two+""+three+""+four+".gif"));
					System.out.println(images_path+"rooms/"+selectedRoom.getRoomType().toString()+""+one+""+two+""+three+""+four+".gif");
				}
				
			}
			
			public void uploadPanel(){
				if (selectedRoom!=null){
					updateSelectedRoomValues();
					updateRoomImage();
					updateDevicePanel();
					updatePresenceGraphicPanel();
					updateConsumeGraphicPanel();
					DynamicUpdateTabbedPanel();
				}
			}
			
			
		    private double truncate (double x)
		    {
		        DecimalFormat df = new  DecimalFormat ("0.##");
		        String d = df.format (x);
		        d = d.replaceAll (",", ".");
		        Double dbl = new Double (d);
		        return dbl.doubleValue ();
		    }

}
