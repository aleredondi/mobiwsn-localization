package client_applications.profiling_system.profiling_system_impl.daily_profiles_recorder;



import client_applications.profiling_system.profiling_system_impl.*;
import client_applications.profiling_system.profiling_system_impl.profiles_analyzer.*;
import client_applications.profiling_system.profiling_system_impl.profiles_updater.*;

import remote_interfaces.functionality.Functionality;
import remote_interfaces.functionality.FunctionalityType;
import remote_interfaces.mote.Mote;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.sensor.SensorType;
import remote_interfaces.services.Subscriber;
import remote_interfaces.WSNGatewayManager;
import remote_interfaces.clients.home_virtualization_application.*;
import remote_interfaces.clients.dve.*;

import java.awt.Checkbox;
import java.awt.Font;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimerTask;
import javax.swing.*;
import common.exceptions.FunctionalityException;
import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;
import java.util.*;
import java.util.Timer;




/***
* @author Antimo Barbato
*
*/



public class DailyProfileRecorder {
	
	private ArrayList<String> moteGroupsNames = new ArrayList<String>();   //Mote groups created in the "Network Management Interface"
	private ArrayList<newRoom> rooms = new ArrayList<newRoom>();  //House rooms
	private TimerTask refresh;     
	private Timer refTimer=new Timer(); 
	private int functionalityActive=0;
    private Calendar calendar = new GregorianCalendar();
    private Integer day1=0;
    private Integer day2=0;
    private Integer samplingTime=10; // seconds between two presence/temperature/light reads 
    private int TrainingPeriod=5; // training period length in days 
    private String databaseName="test2";// database name
    private String databaseUserName="root";// database user name
    private String databaseUserPassword="";// database user password
    private String databaseURL="jdbc:mysql://localhost:3306/mysql?";// database URL
    private int profileLength;
	private String profileName1="presence profile"; // presence profile type name
	private String profileName2="temperature profile";// temperature profile type name
	private String profileName3="light profile";// light profile type name
	private String profileName4="realtime presence value";// real time presence value type name
	private String profileName5="realtime temperature value";// real time temperature value type name
	private String profileName6="realtime light value";// real time light value type name
	private boolean foundTables=false;
	private boolean newDatabaseStarted=true;
	private int updatingTime=600*1000; // profile updating period in seconds
	private boolean startProfiling=false;
	protected int ffff=0;
	protected long lastUpTime;
	protected long currentTime;
	private int fffflimit=5;
	private ArrayList<String> groupsNames=new ArrayList<String>();
	private Hashtable<String, String > roomsGroupsAssociation=new Hashtable<String,String>();
	private boolean foundDatabase=true;
	private int[][] vicinityMatrix;
	private int roomId=1;
	private Hashtable<String,Integer> roomsId=new Hashtable<String,Integer>();
	ProfileUpdater profUpd;
	PresenceFunctionalitySubscriber f_sub;
	private ArrayList<Object> ROOMSNames=new ArrayList<Object>();
	private WSNGatewayManager gatewayManager;
	private ProfilingSystemImpl profilingSystem;
	private HomeVirtualizationApplication hva=null;
	private DVE dve=null;
	private final int PRESENCE_DELAY_TIMER_VALUE = 180; // in secondi
	
	/** Constructor
	 * 
	 * @param moteGroupsNames - Groups of Mote created previously
	 */
	
	public DailyProfileRecorder(ArrayList<String> moteGroupsName,WSNGatewayManager gatewayManager, ProfilingSystemImpl p,HomeVirtualizationApplication hva,DVE d) {
		
				    this.moteGroupsNames = moteGroupsName;
				    this.gatewayManager=gatewayManager;;
				    this.profileLength=(int) Math.floor((24*60*60-4)/samplingTime);
				    this.profilingSystem=p;
				    this.hva=hva;
				    this.dve=d;
				   // this.profileLength=30;
				     
				    
			        try {
						f_sub = new PresenceFunctionalitySubscriber(this);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    
				    
				    try{
			            
			            Class.forName("com.mysql.jdbc.Driver").newInstance(); // load and register the JDBC driver classes for MySQL.

			            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); //connection to the database
			                                                                                                                  //Get a connection to the database for a
			                                                                                                                  // user named root with a antlab password.
			                                                                                                                 // The URL of the server is jdbc:mysql://localhost:3306/mysql?
			                                                                                                                 
			            System.out.println("Connection to MySQL server to verify the existence of the tables in the selected Database!");

			            ResultSet rs;
			            ResultSet rs1;
			            Statement stmt = null;
			            Statement stmt2 = null;
			            conn.setCatalog(databaseName); // select database "test"
			            stmt = conn.createStatement();//This code invokes the createStatement method 
			                                          //of the Connection interface to get an object of type Statement.
			                                          // A Connection object defines a connection (session) with a specific database.
			            stmt2 = conn.createStatement();    
			            rs=stmt.executeQuery("SELECT * FROM rooms;");
			            while(rs.next()){
			            	this.foundTables=true;
			            	roomsId.put(rs.getString("roomname"),rs.getInt("idroom"));
			            }
			         
			           
			            rs1=stmt2.executeQuery("SELECT * FROM  groupsRoomsTables;");
			            
			            while(rs1.next()) {
			            	
			                groupsNames.add(rs1.getString("groupName"));
			                roomsGroupsAssociation.put(rs1.getString("groupName"), rs1.getString("roomName"));
			            }

			            stmt.close(); // close the object statement.
			            stmt2.close();
			            conn.close(); // close the connection 
			        }
			        
			        catch (Exception ex) {
			            System.out.println("SQLException: " + ex.getMessage());
			        }
			        
					
			        if(foundTables){
			        	
			          System.out.println("A pre-existenting database was found!");
					  final JFrame myPanel = new JFrame("Database Recovery");
				      myPanel.setLayout(null);
				      myPanel.setBounds(350, 170, 620,200);
				      
				      
				      final JPanel panelRoom = new JPanel();
				      panelRoom.setLayout(null);
				      panelRoom.setBounds(0,0, 620,200);
				      

				      final JLabel lblName = new JLabel ();
			   		  lblName.setText("Do you want to use the preexisting database for the user behaviour profiling?");
			      	  lblName.setBounds(10,10,590,20);
			      	  lblName.setFont(new Font("DejaVu Sans", Font.BOLD,13));
			      	  lblName.setVisible(true);
			      	  panelRoom.add(lblName);
			      	  
			      	  
			      	  final JButton btnNotStartNew = new JButton();
			      	  btnNotStartNew.setText("Use the preexisting database");
			      	  btnNotStartNew.setName("btnNotStartNew");
			      	  btnNotStartNew.setBounds(50,120,230,30);
			      	  btnNotStartNew.setVisible(true);
			      	  btnNotStartNew.setEnabled(true);
			      	 
			       	  btnNotStartNew.addMouseListener(new java.awt.event.MouseAdapter() {
			      		public void mouseClicked(final java.awt.event.MouseEvent evt) {
			      			newDatabaseStarted=false;
			      			for(int i=0;i<moteGroupsNames.size();i++){
			      			  boolean found=false;
			      			for(int j=0;j<groupsNames.size();j++){
			      				if(moteGroupsNames.get(i).equals(groupsNames.get(j)))
			      					found=true;
			      			}
			      			foundDatabase=foundDatabase&found;
			      		  }
			      		  for(int i=0;i<groupsNames.size();i++){
			      			  boolean found=false;
			      			for(int j=0;j<moteGroupsNames.size();j++){
			      				if(groupsNames.get(i).equals(moteGroupsNames.get(j)))
			      					found=true;
			      			}
			      			foundDatabase=foundDatabase&found;
			      		  }
			      		  if(foundDatabase==true){
			      		     myPanel.dispose();
			      		     initRoomsFromDatabase();
			      		  }
			      		  else{
			        			final JFrame frameDialogue = new JFrame();
			          			JOptionPane.showMessageDialog(frameDialogue,"It's not possibile togroupsNames use the pre-existing database, the groups/rooms number doesn't correspond with the stored one!Set the rooms names....");
				      		     myPanel.dispose();
				      		     newDatabaseStarted=true;
				      		     initRooms();
				      		  }
				      		}
				      	});
				       	  
				      	panelRoom.add(btnNotStartNew);
				      	  
				      	
				      	final JButton btnStartNew = new JButton();
				      	btnStartNew.setText("Create a new database");
				      	btnStartNew.setName("btnStartNew");
				      	btnStartNew.setVisible(true);
				      	btnStartNew.setEnabled(true);
				     	btnStartNew.setBounds(350,120,230,30);
				      	btnStartNew.addMouseListener(new java.awt.event.MouseAdapter() {
				      		public void mouseClicked(final java.awt.event.MouseEvent evt) {
				      			
				      			newDatabaseStarted=true;
							    try{
						            
						            Class.forName("com.mysql.jdbc.Driver").newInstance(); // load and register the JDBC driver classes for MySQL.
	
						            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); //connection to the database
						                                                                                                                  //Get a connection to the database for a
						                                                                                                                  // user named root with a antlab password.
						                                                                                                                 // The URL of the server is jdbc:mysql://localhost:3306/mysql?
						                                                                                                                 
						            System.out.println("Connection to MySQL server to remove the tables in the selected Database!");
	
						            ResultSet rs;
						            Statement stmt = null;
						            	
						            conn.setCatalog(databaseName); // select database "test"
						            stmt = conn.createStatement();//This code invokes the createStatement method 
						                                          //of the Connection interface to get an object of type Statement.
						                                          // A Connection object defines a connection (session) with a specific database.
						                
						            rs=stmt.executeQuery("Select * FROM rooms;");
						            if(rs.next()) {
						                	
						                	stmt.executeUpdate("DROP TABLE rooms;");
						                	stmt.executeUpdate("DROP TABLE profileType;");
						                	stmt.executeUpdate("DROP TABLE DailyPresenceProfiles;");
						                	stmt.executeUpdate("DROP TABLE DailyTemperatureProfiles;");
						                	stmt.executeUpdate("DROP TABLE DailyLightProfiles;");
						                	stmt.executeUpdate("DROP TABLE FinalPresenceProfiles;");
						                	stmt.executeUpdate("DROP TABLE FinalTemperatureProfiles;");
						                	stmt.executeUpdate("DROP TABLE FinalLightProfiles;");
						                	stmt.executeUpdate("DROP TABLE RealTime;");
						                	stmt.executeUpdate("DROP TABLE PredictedProfiles;");
						                	stmt.executeUpdate("DROP TABLE UpdateTable;");
						                	stmt.executeUpdate("DROP TABLE PresenceProfilesSequenceTable;");
						                	stmt.executeUpdate("DROP TABLE groupsRoomsTables;");
						                	stmt.executeUpdate("DROP TABLE roomsVicinityMatrix;");
						                	
						            }
	
						            stmt.close(); // close the object statement.
						            conn.close(); // close the connection 
						            System.out.println("The tables have been removed!");
						            initRooms();
						        }
						        
						        catch (Exception ex) {
						            System.out.println("SQLException: " + ex.getMessage());
						        }

				        		myPanel.dispose();
				      		}
				      	});
				      	  panelRoom.add(btnStartNew);
					     
				      	  myPanel.add(panelRoom);	
				      	  myPanel.setVisible(true);
			        }
			        
			      	  else {
			      		  initRooms();
			      	  }

	}

	  
	
	/** This method creates the interface for the rooms creation
	 *  
	 */
	public void initRoomsFromDatabase(){
		
		 for(String s:groupsNames){
			 
      		  newRoom oneRoom = new newRoom(roomsGroupsAssociation.get(s),s,roomsId.get(roomsGroupsAssociation.get(s)),gatewayManager,dve);

      		  rooms.add(oneRoom);				
					//if the mote groups is empty, the room matrix is created to define the rooms disposition in the house 
		 }	
		 
		 vicinityMatrix = new int[rooms.size()][rooms.size()];
		    try{
	            
	            Class.forName("com.mysql.jdbc.Driver").newInstance(); // load and register the JDBC driver classes for MySQL.

	            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); //connection to the database
                                                                                                     

	            ResultSet rs;
	            Statement stmt = null;
	            	
	            conn.setCatalog(databaseName); // select database "test"
	            stmt = conn.createStatement();//This code invokes the createStatement method 
	                                          //of the Connection interface to get an object of type Statement.
	                                          // A Connection object defines a connection (session) with a specific database.
	            for(newRoom r: rooms)  { 
	            rs=stmt.executeQuery("Select houseRoomsName FROM roomsVicinityMatrix WHERE roomName='"+r.getGroupName()+"' AND value="+1+";");
	            while(rs.next()) {
	            	r.NearRooms.add(rs.getString("houseRoomsName"));
	            }
	            }
	            stmt.close(); // close the object statement.
	            conn.close(); // close the connection 
	        }
	        
	        catch (Exception ex) {
	            System.out.println("SQLException: " + ex.getMessage());
	        }
		 /*
      		try {
				for(newRoom r:rooms){
				//	MobiWSNConsoleApp.getApplication().gatewayManager.sendAddMotesToGroup(r.getGroupName());
				}

			} 
    		
    		catch (RemoteException e) {
				
				e.printStackTrace();
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MiddlewareException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 */
   	      int hour = calendar.get(Calendar.HOUR);
	      int minute = calendar.get(Calendar.MINUTE);
	    /*  while(!(hour==0 & minute==0)){
		      System.out.println( "pausa");
		      hour = calendar.get(Calendar.HOUR);
    	      minute = calendar.get(Calendar.MINUTE);  
	         }*/
	      profilingSystem.setRoomsList();
	      startRecording(); 

      		  
	}

	  
	

	
	/** This method creates the interface for the rooms creation
	 *  
	 */
	public void initRooms(){
		
		 
		  System.out.println("Defining the rooms....");
		  final JFrame mainPanel = new JFrame("Room settings");
		  final JLabel lblGroupName = new JLabel ();
	      final JPanel panelRoom = new JPanel();
	      final JLabel lblRoomName = new JLabel ();
	      final JLabel labelGroupName = new JLabel();
	      final JTextArea textAreaRoomName = new JTextArea();
	      final JButton btnOkay = new JButton();
	      final JButton btnNotRoom = new JButton(); 

	       
	      mainPanel.setVisible(true);
	      mainPanel.setBounds(350, 170, 500,200);
	    
	      panelRoom.setLayout(null);
	      mainPanel.add(panelRoom);
	      
   		  lblGroupName.setText("Group Name:");
      	  lblGroupName.setBounds(10,10,200,20);
      	  lblGroupName.setFont(new Font("DejaVu Sans", Font.BOLD,13));
      	  panelRoom.add(lblGroupName);
     
      	  lblRoomName.setText("Room Name:");
      	  lblRoomName.setBounds(10,50,200,20);
      	  lblRoomName.setFont(new Font("DejaVu Sans", Font.BOLD,13));
      	  panelRoom.add(lblRoomName);
      	  
      	
      	  labelGroupName.setText(moteGroupsNames.get(0));
      	  labelGroupName.setFont(new Font("DejaVu Sans", Font.PLAIN,13));
      	  labelGroupName.setBounds(220,10,200,20);
      	  panelRoom.add(labelGroupName);
      	
      	  textAreaRoomName.setColumns(20);
      	  textAreaRoomName.setBounds(220,50,200,20);
      	  panelRoom.add(textAreaRoomName);
      	  
      	  btnOkay.setText("OK");
      	  btnOkay.setName("btnOkay");
      	  btnOkay.setBounds(50,120,130,30);
      	  btnOkay.addMouseListener(new java.awt.event.MouseAdapter() {
      		public void mouseClicked(final java.awt.event.MouseEvent evt) {
      		
      		
      		if(!textAreaRoomName.getText().equals(""))
      		{
      		  int control=1;
      		  
      		  for(newRoom r:rooms)
      			  if(r.getRoomName().equals(textAreaRoomName.getText()))
      			  {
      				final JFrame frameDialogue = new JFrame();
          			JOptionPane.showMessageDialog(frameDialogue, "This name already exists!");
          			control=0;
      			  }
      		if(control==1)
      		{
      			
      		  newRoom oneRoom = new newRoom(textAreaRoomName.getText(),labelGroupName.getText(),roomId,gatewayManager,dve);
      		  roomId+=1;
      		  
      		  
      		  rooms.add(oneRoom);				//the created room is added to the rooms Array
      		  moteGroupsNames.remove(0);		//and the mote group associated to this room is deleted
      		  
      		  if (!moteGroupsNames.isEmpty()){	//if the mote groups is not empty the creation room metod is called on the next mote group;
      			  initRooms();
      			  mainPanel.dispose();//if the mote groups is empty, the room matrix is created to define the rooms disposition in the house 
      		  }					
              else								
      		  {
            	  mainPanel.dispose();
            	  if(newDatabaseStarted==true)
            		  createTables(rooms);   
      			  vicinityMatrix = new int[rooms.size()][rooms.size()];
      			  /*
          		try {
					for(newRoom r:rooms){
						MobiWSNConsoleApp.getApplication().gatewayManager.sendAddMotesToGroup(r.getGroupName());
					}

				} 
        		
        		catch (RemoteException e) {
					
					e.printStackTrace();
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MiddlewareException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      			  
      			 */
            	  
            	  
            	  int hour = calendar.get(Calendar.HOUR);
            	  int minute = calendar.get(Calendar.MINUTE);
        		/* while(!(hour==0 & minute==0)){
        			 System.out.println( "pausa");
        			  hour = calendar.get(Calendar.HOUR);
                	  minute = calendar.get(Calendar.MINUTE);  
        		  }*/
        	      profilingSystem.setRoomsList();
            	  setVicinity();
            	  
            	  
	      		  }
	      		  
	      		  
	      		}
	      		}
	      		else
	      		{
	      			final JFrame frameDialogue = new JFrame();
	      			JOptionPane.showMessageDialog(frameDialogue, "Write a name for the room!");
	      		}
	      		
	      	  }
	      	});
	      	panelRoom.add(btnOkay);
	      	  
	      	btnNotRoom.setText("Not a room");
	      	btnNotRoom.setName("notARoom");
	      	btnNotRoom.setBounds(320,120,130,30);
	      	btnNotRoom.addMouseListener(new java.awt.event.MouseAdapter() {
	      		public void mouseClicked(final java.awt.event.MouseEvent evt) {
	      			
	      			moteGroupsNames.remove(0);
	        		  
	        		if (!moteGroupsNames.isEmpty())
	        			  initRooms();
	        		else
	        		{
	              	  if(newDatabaseStarted==true)
	            		  createTables(rooms);  
	      			  vicinityMatrix = new int[rooms.size()][rooms.size()];

	      		      profilingSystem.setRoomsList();
	      			  setVicinity();
	            	  /*
	            		try {
	    					for(newRoom r:rooms){
	    						MobiWSNConsoleApp.getApplication().gatewayManager.sendAddMotesToGroup(r.getGroupName());
	    					}
	
	    				} 
	            		
	            		catch (RemoteException e) {
	    					
	    					e.printStackTrace();
	    				} catch (HeadlessException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				} catch (MiddlewareException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	            	  */
	            	  int hour = calendar.get(Calendar.HOUR);
	            	  int minute = calendar.get(Calendar.MINUTE);
	        		/*  while(!(hour==0 & minute==0)){
	        			  hour = newcalendar.get(Calendar.HOUR);
	                	  minute = newcalendar.get(Calendar.MINUTE);  
	        		  }*/
	
	            	      
	        		}
	        		  
	        		mainPanel.dispose();
	      			
	      		}
	      	});
	      	  panelRoom.add(btnNotRoom);
	
		  }
	  
	
	/** This method creates the interface for the adiacency settings
	 * 
	 */
	 private void setVicinity(){
		 
		  final JButton btnOkay = new JButton();
		  final JFrame mainPanel = new JFrame("Room Vicinity");
		  final ArrayList<Checkbox> vicinityList = new ArrayList<Checkbox>();  //In questa arraylist vengono aggiunte le checkbox selezionate, da qui verrà aggiornata la matrice di adiacenza
		  final JLabel title = new JLabel("Set the rooms vicinities");
		  
		  
		  mainPanel.setVisible(true);
		  mainPanel.setLocation(350, 170);
	      mainPanel.setLayout(null);
	      
	      title.setFont(new Font("DejaVu Sans",Font.BOLD,15));
	      title.setBounds(10,10,300,20);
	      mainPanel.add(title);
	      
	      
	      for(int i=0;i<rooms.size()+1;i++)
	    	  for(int j=0;j<rooms.size()+1;j++)
	    	  {
	    		
	    		if((i==0)&&(j!=0)){
	    			JLabel roomNameLabel = new JLabel(rooms.get(j-1).getRoomName());
	    			roomNameLabel.setBounds(20+(50*j), 20+(50*i), 50, 50);
	    			mainPanel.add(roomNameLabel);
	    		}
	    		
	    		if((j==0)&&(i!=0)){
	    			JLabel roomNameLabel = new JLabel(rooms.get(i-1).getRoomName());
	    			roomNameLabel.setBounds(20+(50*j), 20+(50*i), 50, 50);
	    			mainPanel.add(roomNameLabel);
	    		}
	    		
	    		if ((i>=j)&&(i!=0)&&(j!=0)){
	    			JLabel label = new JLabel("X");
	    			label.setBounds(20+(50*j), 20+(50*i), 50, 50);
	    			mainPanel.add(label);
	    		}
	    		else if((i!=0)&&(j!=0))
	    		{
	    			final Checkbox checkbox = new Checkbox();
	    			checkbox.setName(String.valueOf(i-1)+String.valueOf(j-1));
	    			checkbox.setBounds(20+(50*j), 20+(50*i), 50, 50);
	    			mainPanel.add(checkbox);
	    			
	    			checkbox.addMouseListener(new java.awt.event.MouseAdapter() {
	    			      public void mouseClicked(final java.awt.event.MouseEvent evt) {
	    			    	  
	    			    	  if(checkbox.getState()){
	    			    		  vicinityList.add(checkbox);
	    			    	  }
	    			    	  else{
	    			    		  vicinityList.remove(checkbox);
	    			    	  }
	    			      }
	    			});
	    		}
	    
	    	  }
	      mainPanel.setSize(rooms.size()*50+100, rooms.size()*50+150);
	      
	      btnOkay.setText("OK");
      	  btnOkay.setName("btnOkay");
      	  btnOkay.setBounds(rooms.size()*20,(rooms.size()*50)+70,130,30);
      	  btnOkay.addMouseListener(new java.awt.event.MouseAdapter() {
      		public void mouseClicked(final java.awt.event.MouseEvent evt) {
      		
      			for(int i=0;i<rooms.size();i++){
      	    	  for(int j=0;j<rooms.size();j++)
      	    	  {
      	    		  if(i<j)
      	    		  {
      	    			for(Checkbox k:vicinityList)  
      	    				if(k.getName().equals(String.valueOf(i)+String.valueOf(j)))
      	    				{
      	    					vicinityMatrix[i][j] = 1;
      	    					vicinityMatrix[j][i] = 1;
      	    					break;
      	    				}
      	    				else 
      	    				{
      	    					
      	    					vicinityMatrix[i][j] = 0;
      	    					vicinityMatrix[j][i] = 0;
      	    				}
      	    		  }
      	    		  else if(j==i)
      	    			  	vicinityMatrix[i][j] = 0;
	 
      	    	  }
      			}
      			
			    try{
		            
		            Class.forName("com.mysql.jdbc.Driver").newInstance(); 

		            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); 
		                                                                                                                 
		            System.out.println("Connection to MySQL server to save the set vicinities!");

		            ResultSet rs;
		            Statement stmt = null;
		            conn.setCatalog(databaseName);
		            stmt = conn.createStatement();
		            
	      			for(newRoom r:rooms){
	        	    	  for(newRoom s:rooms){
	        	    		  stmt.executeUpdate("UPDATE roomsVicinityMatrix SET value="+vicinityMatrix[r.getRoomIdentifier()-1][s.getRoomIdentifier()-1]+" WHERE roomName='"+r.getGroupName()+"' AND houseRoomsName='"+s.getGroupName()+"';");
	  	 
	        	    	  }
	        			}  
		


		            stmt.close(); 
		            conn.close(); 
		        }
			   
		        catch (Exception ex) {
		            System.out.println("SQLException: " + ex.getMessage());
		        }
		        


			    try{
		            
		            Class.forName("com.mysql.jdbc.Driver").newInstance(); // load and register the JDBC driver classes for MySQL.

		            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); //connection to the database
	                                                                                                     

		            ResultSet rs;
		            Statement stmt = null;
		            	
		            conn.setCatalog(databaseName); // select database "test"
		            stmt = conn.createStatement();//This code invokes the createStatement method 
		                                          //of the Connection interface to get an object of type Statement.
		                                          // A Connection object defines a connection (session) with a specific database.
		            for(newRoom r: rooms)  { 
		            rs=stmt.executeQuery("Select houseRoomsName FROM roomsVicinityMatrix WHERE roomName='"+r.getGroupName()+"' AND value="+1+";");
		            while(rs.next()) {
		            	r.NearRooms.add(rs.getString("houseRoomsName"));
		            }
		            }
		            stmt.close(); // close the object statement.
		            conn.close(); // close the connection 
		        }
		        
		        catch (Exception ex) {
		            System.out.println("SQLException: " + ex.getMessage());
		        }
		        
      			mainPanel.dispose();
      			startRecording();
      		}
      	  });
      	
      	  mainPanel.add(btnOkay);  
      	
      	 
	    }
	

	 private void startRecording(){
		 
		 		Calendar calendar = new GregorianCalendar();
                day1=calendar.get(Calendar.DAY_OF_MONTH);
                System.out.println("Start Recording!");
                profUpd=new ProfileUpdater(databaseName,databaseURL,databaseUserName,databaseUserPassword,samplingTime,profilingSystem);
                
                if(functionalityActive==0)							//Avvio della funzionalita'� dummyPresence su ogni IMOTE
				{
                	
					
				for(newRoom r:rooms)
				{
					
			
					try {
							for(Mote mote:gatewayManager.getMoteGroup(r.getGroupName()))
							{
								Mote selectedMote = null;
								boolean moteFound = false;
								for(Sensor sensor: mote.getSensorList())
									if(sensor.getType()==SensorType.INFRARED)
									{	
										
										selectedMote = mote;
										moteFound = true;
									}
							
							
							if(moteFound)
							{	
								
								Functionality f= null;
				            	try 
				            	{
				            		
				            		System.out.println(r.getRoomName()+" "+moteFound);
				            		for(int i=0;i<selectedMote.getFunctionalityList().size();i++){
				            		    if(selectedMote.getFunctionalityList().get(i).getType() == FunctionalityType.PRESENCE){
				            		    	
					            		f = selectedMote.getFunctionalityList().get(i);
				      					try {
				      						
				    						if ( f.hasPublisher() ) 
				    							f.getPublisher().addSubscriber(f_sub);
				    						else
				    							System.out.println("Non pubblica nulla");      						
				      						ROOMSNames.clear();
				      						ROOMSNames.add(PRESENCE_DELAY_TIMER_VALUE);
				      						ROOMSNames.add(r.getGroupName());
				      						if(!r.NearRooms.isEmpty()){
				      							for(int y=0;y<r.NearRooms.size();y++){
				      								ROOMSNames.add(r.NearRooms.get(y));
				      							}
				      						}
				      				//		f.stopFunctionality();
				      						System.out.println(selectedMote.getId()+" Start Functionality!");
				  	        	    	  	try {
												Thread.sleep(5000);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											try {
												f.startFunctionality(ROOMSNames);
											} catch (FunctionalityException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											} catch (MoteUnreachableException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
				  	        	    	  	try {
												Thread.sleep(2000);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
										} 
				      					catch (RemoteException e) {
											e.printStackTrace();
										} 

				      					catch (MiddlewareException e) {
											e.printStackTrace();
										}
				            		    }  
				            		}
										
								} 
				            	catch (RemoteException e) 
				            	{
				            		e.printStackTrace();
				            	}
				            	
				            	functionalityActive=1;

							} 	
							}
		    			
					} catch (RemoteException e1) {e1.printStackTrace();}
							
					
				}
				}
                
                
            	Date newDate = new Date();
                long lastUpdateTime= newDate.getTime();
                int f=0;
          		refreshValues(f,lastUpdateTime);
                refTimer.schedule(refresh, 4000, samplingTime*1000); //Avviate le letture automatiche dei sensori
                													//Il primo numero indica il ritardo con cui parte il refresh, il secondo è l'intervallo con cui si deve ripetere  		
      			
      		}
      		

		
		private void refreshValues(int f,long lastUpdateTime) 
		{
			
			lastUpTime=lastUpdateTime;
			
			refTimer = new Timer();
	       	refresh = new TimerTask() {
	            public void run() 
	            {
	            	Calendar calendar = new GregorianCalendar();
	            	day2=calendar.get(Calendar.DAY_OF_MONTH);
	            	Date newDate = new Date();
                    long newTime= newDate.getTime();
                    currentTime=newDate.getTime();
                    System.out.println(day2);
                    System.out.println("Collecting presence,temperature and light values!");
                    
	    			   for(newRoom r:rooms){
	                	   try {
	                		   r.readRoomSensors(newTime);
	    				   } catch (RemoteException e) {
	    					   e.printStackTrace();
	    				   }	
				        }
	            	if(day1==day2){
	           // 	if(ffff<fffflimit){
	            		ffff=ffff+1;
	            	    for(newRoom r:rooms){
	            		    r.setNewDailyTemperature(r.getTemperatureValue());
	            		    r.setNewDailyLight(r.getLightValue());
	            		    r.setNewDailyPresence(r.getPresenceValue());
	            		    
	            		    
		            	    
						    try{
					            
					            Class.forName("com.mysql.jdbc.Driver").newInstance(); 

					            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword );

					            
					            ResultSet rs;
					            ResultSet rs1;
					            ResultSet rs2;
					            ResultSet rs3;
					            int idTypePresence=0;
					            int idTypeTemperature=0;
					            int idTypeLight=0;
					            int RoomId=0;
					            Statement stmt = null;
					            	
					            conn.setCatalog(databaseName); 
					            stmt = conn.createStatement();
					            
					            rs=stmt.executeQuery("SELECT idroom FROM rooms WHERE roomname='"+r.getRoomName()+"';");
					            while(rs.next()){
					            	RoomId=rs.getInt("idroom");
					            }
					            
					            rs1=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+profileName4+"';");

					            if(rs1.next()) {
					            	idTypePresence=rs1.getInt("idtype");
					            	stmt.executeUpdate("UPDATE RealTime SET value="+r.getPresenceValue()+" WHERE idtype="+idTypePresence+" AND idroom="+RoomId+";");	
					            }
					            
					            rs2=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+profileName5+"';");

					            if(rs2.next()) {
					            	idTypeTemperature=rs2.getInt("idtype");
					            	stmt.executeUpdate("UPDATE RealTime SET value="+r.getTemperatureValue()+" WHERE idtype="+idTypeTemperature+" AND idroom="+RoomId+";");	
					            }
					            
					            rs3=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+profileName6+"';");
					            if(rs3.next()) {
					            	idTypeLight=rs3.getInt("idtype");
					            	stmt.executeUpdate("UPDATE RealTime SET value="+r.getLightValue()+" WHERE idtype="+idTypeLight+" AND idroom="+RoomId+";");	
					            }

					            rs.close();
					            stmt.close(); // close the object statement.
					            conn.close(); // close the connection 

					        }
					        
					        catch (Exception ex) {
					            System.out.println("SQLException: " + ex.getMessage());
					        }
	            		}

	            	    if(currentTime-lastUpTime>updatingTime & startProfiling){
	            	    	lastUpTime= newDate.getTime();
	                        profUpd.setProfileLength(rooms.get(0).getNewDailyPresenceSize());
	                        for(newRoom r:rooms){
	                        	profUpd.recordingLocalDB(r.getRoomName(),r.getNewDailyPresence(),r.getProfilesSequence());
	                        }
	                        profUpd.roomUpdating();
	            	    }

	            	}
	            	else {
		    			System.out.println("Stop collecting values, starting to control the collected values!");
	            		refTimer.cancel();
	            		refresh.cancel();
	            		boolean verifyLength=true;
	            		for(newRoom r:rooms){
	            			
	            		r.setOldDailyPresence();
	            		if(r.getOldDailyPresenceSize()<profileLength-30)
	            			verifyLength=false; //ATTENZIONE
	            		else{
	            			for(int i=r.getOldDailyPresenceSize();i<profileLength;i++){
	            				r.addOldDailyPresence(r.getOldDailyPresence(r.getOldDailyPresenceSize()-1));
	            			}
	            		}
	            		
	            		r.setOldDailyTemperature();
	            		if(r.getOldDailyTemperatureSize()<profileLength-30)
	            			verifyLength=false; //ATTENZIONE
	            		else{
	            			for(int i=r.getOldDailyTemperatureSize();i<profileLength;i++){
	            				r.addOldDailyTemperature(r.getOldDailyTemperature(r.getOldDailyTemperatureSize()-1));
	            			}
	            		}
	            		
	            		r.setOldDailyLight();
	            		if(r.getOldDailyLightSize()<profileLength-30)
	            			verifyLength=false; //ATTENZIONE
	            		else{
	            			for(int i=r.getOldDailyLightSize();i<profileLength;i++){
	            				r.addOldDailyLight(r.getOldDailyLight(r.getOldDailyLightSize()-1));
	            			}
	            		}
	            		}
		    			System.out.println("End of controls");
	            		startRecording();
	            	    if(verifyLength==true){
	            		   databaseLoading();
	          	           if(startProfiling==true){
	          	   	            
	         	               	ProfilesAnalyzer PROFILESANALYZER= new ProfilesAnalyzer(TrainingPeriod,databaseName,databaseURL,databaseUserName,databaseUserPassword,profileLength,samplingTime,profilingSystem);
	         	               	PROFILESANALYZER.recordingLocalDB();
	         	               	PROFILESANALYZER.ProfilesAnalysis();
	         	               	for(int i=0;i<rooms.size();i++){
	         	               		rooms.get(i).setProfilesSequence(PROFILESANALYZER.getProfilesSequence(rooms.get(i).getRoomIdentifier()));
	         	               	}
	                      }
	            	    }
	            	    else 
	            	    	System.out.println("The number of collected values is less than what it should be! This data will be delated! ");
	            	}
	            }
	    	};
		}
		
       private void databaseLoading()
       {
    	   startProfiling=true;
    	   System.out.println("Loading data in MySQL DB....");
	        try{
	            Class.forName("com.mysql.jdbc.Driver").newInstance(); // load and register the JDBC driver classes for MySQL.

	            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); //connection to the database
	                                                                                                                  //Get a connection to the database for a
	                                                                                                                  // user named root with a antlab password.
	                                                                                                                 // The URL of the server is jdbc:mysql://localhost:3306/mysql?
	                                                                                                                 
	            System.out.println("Connecting to MySQL server!");
	            
	            for(newRoom r:rooms){
	            	String roomNa;
	            	roomNa=r.getRoomName();
	            	ResultSet rs;
	            	ResultSet rs2;
	            	Statement stmt = null;
	            	Statement stmt2 = null;
	            	int localroomid=0;
	            	int localorderid=0;
	            	
	            	conn.setCatalog(databaseName);
	                stmt = conn.createStatement();
	                stmt2 = conn.createStatement();
	                rs=stmt.executeQuery("Select idroom FROM rooms WHERE roomname='"+roomNa+"';");
	                
	                while(rs.next()) {
	                	localroomid=rs.getInt("idroom");
	                  }
	                rs=stmt.executeQuery("Select MAX(idday) FROM DailyPresenceProfiles WHERE idroom='"+localroomid+"';");
	                rs2=stmt2.executeQuery("Select COUNT(*) FROM DailyPresenceProfiles WHERE idroom='"+localroomid+"';");
	                
	                while(rs2.next()){
                       if(rs2.getInt("COUNT(*)")!=0){
	                      while(rs.next()) {
	                	     localorderid=rs.getInt("MAX(idday)");
	                      }
                       }
	                }
                    
	                if(localorderid<(TrainingPeriod-1))
	                	this.startProfiling=false;
	                
	                if(localorderid<TrainingPeriod){
	                	
	                	int neworder=localorderid+1;
	                    for (int i=0;i<	r.getOldDailyPresenceSize();i++){
	                	      int position=i+1;
	                           stmt.executeUpdate("INSERT INTO DailyPresenceProfiles SET value="+r.getOldDailyPresence(i)+",idroom="+localroomid+",idday="+neworder+",idposition="+position+";");
	                    }
	                }
	                else {
                         stmt.executeUpdate("DELETE FROM DailyPresenceProfiles WHERE idroom="+localroomid+" AND idday=1;");
 	                    for (int i=2;i<TrainingPeriod+1;i++){
 	                    	int neworder=i-1;
	                           stmt.executeUpdate("UPDATE DailyPresenceProfiles SET idday="+neworder+" WHERE idroom="+localroomid+" AND idday="+i+";");
	                    }
	                    for (int i=0;i<	r.getOldDailyPresenceSize();i++){
	                	      int position=i+1;
	                           stmt.executeUpdate("INSERT INTO DailyPresenceProfiles SET value="+r.getOldDailyPresence(i)+",idroom="+localroomid+",idday="+TrainingPeriod+",idposition="+position+";");
	                    }
 	                    
	                }
	                
	                
	                
	                rs=stmt.executeQuery("Select MAX(idday) FROM DailyTemperatureProfiles WHERE idroom='"+localroomid+"';");
	                rs2=stmt2.executeQuery("Select COUNT(*) FROM DailyTemperatureProfiles WHERE idroom='"+localroomid+"';");
	                
	                while(rs2.next()){
                       if(rs2.getInt("COUNT(*)")!=0){
	                      while(rs.next()) {
	                	     localorderid=rs.getInt("MAX(idday)");
	                      }
                       }
	                }
	                
	                if(localorderid<(TrainingPeriod-1))
	                	this.startProfiling=false;
	                
	                if(localorderid<TrainingPeriod){
	                	int neworder=localorderid+1;
	                    for (int i=0;i<	r.getOldDailyTemperatureSize();i++){
	                	      int position=i+1;
	                           stmt.executeUpdate("INSERT INTO DailyTemperatureProfiles SET value="+r.getOldDailyTemperature(i)+",idroom="+localroomid+",idday="+neworder+",idposition="+position+";");
	                    }
	                }
	                else {
                         stmt.executeUpdate("DELETE FROM DailyTemperatureProfiles WHERE idroom="+localroomid+" AND idday=1;");
 	                    for (int i=2;i<TrainingPeriod+1;i++){
 	                    	int neworder=i-1;
	                           stmt.executeUpdate("UPDATE DailyTemperatureProfiles SET idday="+neworder+" WHERE idroom="+localroomid+" AND idday="+i+";");
	                    }
	                    for (int i=0;i<	r.getOldDailyTemperatureSize();i++){
	                	      int position=i+1;
	                           stmt.executeUpdate("INSERT INTO DailyTemperatureProfiles SET value="+r.getOldDailyTemperature(i)+",idroom="+localroomid+",idday="+TrainingPeriod+",idposition="+position+";");
	                    }
 	                    
	                }
	                
	                
	                rs=stmt.executeQuery("Select MAX(idday) FROM DailyLightProfiles WHERE idroom='"+localroomid+"';");
	                rs2=stmt2.executeQuery("Select COUNT(*) FROM DailyLightProfiles WHERE idroom='"+localroomid+"';");
	                
	                while(rs2.next()){
                       if(rs2.getInt("COUNT(*)")!=0){
	                      while(rs.next()) {
	                	     localorderid=rs.getInt("MAX(idday)");
	                      }
                       }
	                }
                    
	                if(localorderid<(TrainingPeriod-1))
	                	this.startProfiling=false;
	                
	                if(localorderid<TrainingPeriod){
	                	int neworder=localorderid+1;
	                    for (int i=0;i<	r.getOldDailyLightSize();i++){
	                	      int position=i+1;
	                           stmt.executeUpdate("INSERT INTO DailyLightProfiles SET value="+r.getOldDailyLight(i)+",idroom="+localroomid+",idday="+neworder+",idposition="+position+";");
	                    }
	                }
	                else {
                         stmt.executeUpdate("DELETE FROM DailyLightProfiles WHERE idroom="+localroomid+" AND idday=1;");
 	                    for (int i=2;i<TrainingPeriod+1;i++){
 	                    	int neworder=i-1;
	                           stmt.executeUpdate("UPDATE DailyLightProfiles SET idday="+neworder+" WHERE idroom="+localroomid+" AND idday="+i+";");
	                    }
	                    for (int i=0;i<	r.getOldDailyLightSize();i++){
	                	      int position=i+1;
	                           stmt.executeUpdate("INSERT INTO DailyLightProfiles SET value="+r.getOldDailyLight(i)+",idroom="+localroomid+",idday="+TrainingPeriod+",idposition="+position+";");
	                    }
	                }
	                stmt2.close();
	                stmt.close(); // close the object statement.
	                rs.close();
	            }
	            conn.close(); // close the connection 
	            
	        }
	        
	        catch (Exception ex) {
	            System.out.println("SQLException there: " + ex.getMessage());
	        }
		
	        System.out.println("End of data loading!");
        }
	
		
		private void createTables(ArrayList<newRoom> homerooms){
			
			
        	
        	Statement stmt = null;
        	
			try{
	            Class.forName("com.mysql.jdbc.Driver").newInstance(); // load and register the JDBC driver classes for MySQL.

	            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); //connection to the database
                                                                                              
	            System.out.println("Connection to MySQL server to create the Database tables!");

	            	
	                conn.setCatalog(databaseName); // selezione database
	                stmt = conn.createStatement();//This code invokes the createStatement method 
	                                          //of the Connection interface to get an object of type Statement.
                	
	                
	                stmt.executeUpdate("CREATE TABLE rooms (idroom smallint unsigned NOT NULL, roomname varchar(30) NOT NULL,  PRIMARY KEY (idroom,roomname));");
	                
	                for (newRoom r:rooms){
	                	stmt.executeUpdate("INSERT INTO rooms SET idroom="+r.getRoomIdentifier()+",roomname='"+r.getRoomName()+"';");
	                	
	                }
	                stmt.executeUpdate("CREATE TABLE profileType (idtype smallint unsigned NOT NULL, profilename varchar(35) NOT NULL, PRIMARY KEY(idtype,profilename));");
                	stmt.executeUpdate("INSERT INTO profileType SET idtype=1,profilename='"+profileName1+"';");
                	stmt.executeUpdate("INSERT INTO profileType SET idtype=2,profilename='"+profileName2+"';");
                	stmt.executeUpdate("INSERT INTO profileType SET idtype=3,profilename='"+profileName3+"';");
                	stmt.executeUpdate("INSERT INTO profileType SET idtype=4,profilename='"+profileName4+"';");
                	stmt.executeUpdate("INSERT INTO profileType SET idtype=5,profilename='"+profileName5+"';");
                	stmt.executeUpdate("INSERT INTO profileType SET idtype=6,profilename='"+profileName6+"';");
                	
                	
                	stmt.executeUpdate("CREATE TABLE DailyPresenceProfiles(idroom smallint unsigned NOT NULL, idday smallint unsigned NOT NULL,idposition smallint unsigned NOT NULL, value smallint unsigned default 0, KEY(idroom),KEY(idday),KEY(idposition));");
                	stmt.executeUpdate("CREATE TABLE DailyTemperatureProfiles(idroom smallint unsigned NOT NULL, idday smallint unsigned NOT NULL,idposition smallint unsigned NOT NULL, value double default 0, KEY(idroom),KEY(idday),KEY(idposition));");
                	stmt.executeUpdate("CREATE TABLE DailyLightProfiles(idroom smallint unsigned NOT NULL, idday smallint unsigned NOT NULL,idposition smallint unsigned NOT NULL, value double default 0, KEY(idroom),KEY(idday),KEY(idposition));");
                	stmt.executeUpdate("CREATE TABLE FinalPresenceProfiles(idroom smallint unsigned NOT NULL, idprofile smallint unsigned NOT NULL,idposition smallint unsigned NOT NULL, value double unsigned default 0, KEY(idroom),KEY(idprofile),KEY(idposition));");
                	stmt.executeUpdate("CREATE TABLE FinalTemperatureProfiles(idroom smallint unsigned NOT NULL, idprofile smallint unsigned NOT NULL,idposition smallint unsigned NOT NULL, value double default 0, KEY(idroom),KEY(idprofile),KEY(idposition));");
                	stmt.executeUpdate("CREATE TABLE FinalLightProfiles(idroom smallint unsigned NOT NULL, idprofile smallint unsigned NOT NULL,idposition smallint unsigned NOT NULL, value double default 0, KEY(idroom),KEY(idprofile),KEY(idposition));");
                	
                	
                	stmt.executeUpdate("CREATE TABLE RealTime(idroom smallint unsigned NOT NULL, idtype smallint unsigned NOT NULL, value double default 0, KEY(idroom),KEY(idtype));");
                	ResultSet rs1;
                	ResultSet rs2;
                	ResultSet rs3;
                	ResultSet rs4;
                	int idPresenceType=0;
                	int idTemperatureType=0;
                	int idLightType=0;
                	
                	rs2=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+profileName4+"';");
                	while(rs2.next())
                		idPresenceType=rs2.getInt("idtype");
                	
                	rs3=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+profileName5+"';");
                	while(rs3.next())
                		idTemperatureType=rs3.getInt("idtype");
                	
                	rs4=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+profileName6+"';");
                	while(rs4.next())
                		idLightType=rs4.getInt("idtype");
                	
                	
                	for (newRoom r:rooms){
                		stmt.executeUpdate("INSERT INTO RealTime SET idtype="+idPresenceType+",idroom="+r.getRoomIdentifier()+";");	
                    	stmt.executeUpdate("INSERT INTO RealTime SET idtype="+idTemperatureType+",idroom="+r.getRoomIdentifier()+";");	
                    	stmt.executeUpdate("INSERT INTO RealTime SET idtype="+idLightType+",idroom="+r.getRoomIdentifier()+";");	
                    }
                	

                	stmt.executeUpdate("CREATE TABLE PredictedProfiles(idroom smallint unsigned NOT NULL, idtype smallint unsigned NOT NULL, idprofile smallint unsigned NOT NULL default 0,predictionDate date, KEY(idroom),KEY(idtype),KEY(predictionDate));");
                	
                	
                	for (newRoom r:rooms){
                		for(int k=1;k<4;k++){
                    	    stmt.executeUpdate("INSERT INTO PredictedProfiles SET idtype="+k+",idroom="+r.getRoomIdentifier()+";");
                        }
                	}
                    
                    
                	stmt.executeUpdate("CREATE TABLE UpdateTable(idroom smallint unsigned NOT NULL, idtype smallint unsigned NOT NULL,updvalue smallint unsigned NOT NULL default 0,KEY(idroom),KEY(idtype));");

                	for (newRoom r:rooms){	
                		for(int k=1;k<7;k++){
                			stmt.executeUpdate("INSERT INTO UpdateTable SET idroom="+r.getRoomIdentifier()+",idtype="+k+";");
                        }
                	}   
                	
                	stmt.executeUpdate("CREATE TABLE PresenceProfilesSequenceTable(idroom smallint unsigned NOT NULL, idday smallint unsigned NOT NULL, profileIdentifier smallint unsigned NOT NULL default 0,KEY(idroom),KEY(idday),KEY(ProfileIdentifier));");

                	for (newRoom r:rooms){
                        for(int j=0;j<TrainingPeriod;j++){
                        	int localidorder=j+1;
                            stmt.executeUpdate("INSERT INTO PresenceProfilesSequenceTable SET idroom="+r.getRoomIdentifier()+",idday="+localidorder+";");
                        }
                	}   

	                stmt.executeUpdate("CREATE TABLE groupsRoomsTables (groupName varchar(30) NOT NULL, roomName varchar(30) NOT NULL,  KEY (groupName,roomName));");
	                
	                for (newRoom r:rooms){
	                	stmt.executeUpdate("INSERT INTO groupsRoomsTables SET groupName='"+r.getGroupName()+"',roomName='"+r.getRoomName()+"';");
	                }
	                
	                stmt.executeUpdate("CREATE TABLE roomsVicinityMatrix(roomName varchar(30) NOT NULL,houseRoomsName varchar(30) NOT NULL,value smallint unsigned NOT NULL default 0,KEY(roomName,houseRoomsName));"); 
	                for (newRoom r:rooms){
	                	for(newRoom s:rooms)
	                		stmt.executeUpdate("INSERT INTO roomsVicinityMatrix SET roomName='"+r.getGroupName()+"',houseRoomsName='"+s.getGroupName()+"';");
	                }

	        }
	        
	        catch (Exception ex) {
	            System.out.println("SQLException: " + ex.getMessage());
	        }
		

	        System.out.println("End of tables definition!");
		}
		
       
       
       /*
		public class OnlineManager  extends UnicastRemoteObject implements ClientEventInterface  {
			
				protected OnlineManager() throws RemoteException 
				{}

				public void eventAllert(ArrayList<Object> input, int val) throws RemoteException 
				{
				
					switch(val)
					{
						case 1: //Dummy functionality
							System.out.println("Sensor id: "+input.get(1) + ", mote id :"+ input.get(0) + ", value read: "+ input.get(2));
							
							break;
						
						case 2: //DummyPresence functionality


							for(newRoom r:rooms)
							{
								for(Mote m :MobiWSNConsoleApp.getApplication().gatewayManager.getMoteGroup(r.getGroupName()))
									if(m.getId().equals(input.get(0)))
									{	
										System.out.println("Numero persone: "+input.get(1) + " rilevate dal mote "+ input.get(0)+ " nel gruppo "+r.getGroupName()+ " nella stanza : "+ r.getRoomName() );
										r.setPresenceValue(input.get(1));

										break;
									}
								
							}
							
								
							break;
				
					}
				}
		    }*/
		
		public class PresenceFunctionalitySubscriber extends UnicastRemoteObject implements Subscriber<Functionality, Object> {

			DailyProfileRecorder app;

			protected PresenceFunctionalitySubscriber( DailyProfileRecorder app) throws RemoteException {
				super();
				this.app = app;
			}

			@Override
			public void update(Functionality pub, Object object) throws RemoteException {
				System.out.println("Sono stato avvisato dalla Funzionalità " + pub.getType() +
						" del mote " + pub.getOwnerMoteId() + 
						" che l'object vale " + object);
				
				
				for(newRoom r:rooms)
				{
					for(Mote m :gatewayManager.getMoteGroup(r.getGroupName()))
						if(m.getId().equals(pub.getOwnerMoteId()))
						{	
							System.out.println("Numero persone: "+object + " rilevate dal mote "+ pub.getOwnerMoteId()+ " nel gruppo "+r.getGroupName()+ " nella stanza : "+ r.getRoomName() );
							
							r.setPresenceValue(object);

							profilingSystem.newPresenceValue(r.getRoomName(),object);	
							if(hva!=null){
								System.out.println("Sono qui");
								try{
								hva.newPresenceValue(r.getRoomName(), object);
								}
						        catch (Exception ex) {
						        	ex.printStackTrace();
						        	
						        }
							}
							break;
						}
					
				}
				
				
			}
		}
		
		
		public void StopFunctionality(){

				for(newRoom r:rooms)
				{
				
					try {
							for(Mote mote:gatewayManager.getMoteGroup(r.getGroupName()))
							{
								Mote selectedMote = null;
								boolean moteFound = false;
								System.out.println(mote.getId());
								for(Sensor sensor: mote.getSensorList())
									if(sensor.getType()==SensorType.INFRARED)
									{	
										
										selectedMote = mote;
										moteFound = true;
									}
							
							
							if(moteFound)
							{	
								
								Functionality f= null;
				            	try 
				            	{
				            		
				            		
				            		for(int i=0;i<selectedMote.getFunctionalityList().size();i++){
				            		    if(selectedMote.getFunctionalityList().get(i).getType() == FunctionalityType.PRESENCE){
				            		    	
					            		f = selectedMote.getFunctionalityList().get(i);
					   				try 
									{
					   					f.getPublisher().removeSubscriber(f_sub);	
					   					System.out.println(selectedMote.getId()+" Stop Functionality!");
			  	        	    	  	try {
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
					   					try {
											f.stopFunctionality();
										} catch (FunctionalityException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
									} 
									catch (RemoteException e) 
									{
										e.printStackTrace();
									} catch (MiddlewareException e) {
										e.printStackTrace();
									} catch (MoteUnreachableException e) {
										e.printStackTrace();
									}
				            		    }  
				            		}
										
								} 
				            	catch (RemoteException e) 
				            	{
				            		e.printStackTrace();
				            	}
							}
							}
					} catch (RemoteException e1) {e1.printStackTrace();}
							
					
				}
			
			
		}
		 
		public void setHVA(HomeVirtualizationApplication hva){
			this.hva=hva;
		}

		public void uploadDVE(DVE dve){
			for(newRoom r: rooms){
				r.uploadDVE(dve);
			}
		}
}
