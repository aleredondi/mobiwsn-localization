 
package client_applications.profiling_system.profiling_system_impl;




import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

import java.awt.Color;
import javax.swing.BorderFactory;

import remote_interfaces.clients.dve.*;
import remote_interfaces.WSNGatewayManager;
import remote_interfaces.clients.profiling_system.*;
import remote_interfaces.clients.home_virtualization_application.*;

import client_applications.profiling_system.ProfileImpl;
import client_applications.profiling_system.Room;
import client_applications.profiling_system.profiling_system_impl.daily_profiles_recorder.*;


/***
* @author Antimo Barbato
*
*/


public class ProfilingSystemImpl extends UnicastRemoteObject  implements ProfilingSystem{

	private ArrayList<String> moteGroupsNames = new ArrayList<String>();
	private WSNGatewayManager gatewayManager;
	private DailyProfileRecorder DAILYPROFILERECORDER;
	private DVE dve;
    String databaseName="test2";// database name
    String databaseUserName="root";// database user name
    String databaseUserPassword="";// database user password
    String databaseURL="jdbc:mysql://localhost:3306/mysql?";// database URL
    private Vector<String> roomsNames=new Vector<String>();
    private Vector<Integer> roomsIdentifier=new Vector<Integer>();
    private ArrayList<Room> rooms=new ArrayList<Room>();
    private boolean predictedProfilesFound=false;
    private HomeVirtualizationApplication hva=null;
    private int DVEsamplingTime=15; //minuti

    private double[] localPresenceProfile=new double[96];
    private double[] localTempProfile=new double[96];
    private double[] localLightProfile=new double[96];
    
    
    
	public ProfilingSystemImpl(ArrayList<String> MoteGroupsName,WSNGatewayManager gatewayManager) throws RemoteException{
		
		this.moteGroupsNames=MoteGroupsName;
		this.gatewayManager= gatewayManager;
		
		JFrame firstPanel = new JFrame("Profiling System");
		firstPanel.setLayout(null);
		firstPanel.setBounds(350, 170, 300,300);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, 300,300);
		
		JButton StopFunctionality = new JButton();
		StopFunctionality.setBorder(BorderFactory.createLineBorder(Color.white));
		StopFunctionality.setText("Stop");
		StopFunctionality.setName(" StopFunctionality");
		StopFunctionality.setBounds(35,200,230,30);
		StopFunctionality.setVisible(true);
		StopFunctionality.setEnabled(true);
		StopFunctionality.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(final java.awt.event.MouseEvent evt) {
				DAILYPROFILERECORDER.StopFunctionality();
			}
		});
			  
		panel.add( StopFunctionality);
		firstPanel.add(panel);
		firstPanel.setVisible(true);
		
		DAILYPROFILERECORDER = new DailyProfileRecorder (new ArrayList<String>(moteGroupsNames),gatewayManager,this,hva,dve);

	}
	

	public void Subscribe(DVE dve) throws RemoteException{
		
		this.dve = dve;
		System.out.println("Si è sottoscritto un nuovo dve" + dve.toString());
		DAILYPROFILERECORDER.uploadDVE(dve);
		createProfile();
		

	}
	
	
	
	public void setRoomsList() {
		

		try{
            
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 

            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); 
                                                                                                                 
            System.out.println("Connection to MySQL server to load the rooms names!");

            ResultSet rs1;
            Statement stmt = null;
            conn.setCatalog(databaseName); 
            stmt = conn.createStatement();
            
            rs1=stmt.executeQuery("SELECT * FROM rooms;");
            while(rs1.next()){
            	roomsNames.add(rs1.getString("roomname"));
            	roomsIdentifier.add(rs1.getInt("idroom"));
            }
            stmt.close();
            conn.close(); 
        }
        
        catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }	
        
        for(int i=0;i<roomsNames.size();i++){      	
        	rooms.add(new Room(roomsNames.get(i),(int) roomsIdentifier.get(i),predictedProfilesFound,databaseURL,databaseUserName,databaseUserPassword,databaseName));
        	System.out.println("Room found: "+rooms.get(i).getRoomName());
        }
        
		for(Room r:rooms){
			r.setProfiles(ProfileType.PRESENCE);
			r.setProfiles(ProfileType.LIGHT);
			r.setProfiles(ProfileType.TEMPERATURE);
			System.out.println(r.getRoomName()+" profili predetti trovati: "+r.isPredictionAvailabel());
			if(r.isPredictionAvailabel()){
				notifyDVEnewPredictionAvailable(r.getRoomName());
			}
		}
		
	}
	

	public Vector<String> getRoomsList() throws RemoteException{
		System.out.println("Richiesta la lista delle stanza dal "+dve.toString());
		return roomsNames;
		
		
	}
	
	
	public void setPredictedProfile( String room){
		
		System.out.println("Aggiorno i profili predetti per la stanza "+room);
		for(Room r:rooms){
			if(r.getRoomName().equals(room)){
				r.setProfiles(ProfileType.PRESENCE);
				r.setProfiles(ProfileType.LIGHT);
				r.setProfiles(ProfileType.TEMPERATURE);
				if(r.isPredictionAvailabel()){
					System.out.println(r.getRoomName()+" profili predetti trovati: "+r.isPredictionAvailabel());
					notifyDVEnewPredictionAvailable(r.getRoomName());
				}
			}
		}
		
	}
	
	
	public Profile getPredictedProfile( ProfileType type, String room ) throws RemoteException,ProfilingSystemException{
		
		System.out.println("Richiesto il profilo predetto "+type.toString()+" per la stanza "+room+" dal "+dve.toString());
	/*	ProfileImpl p=null;
		for(Room r:rooms){
			if(r.getRoomName().equals(room)){
				if(r.isPredictionAvailabel()){
					p=r.getProfiles(type);
				}
				else 
					throw new ProfilingSystemException("Profilo predetto non disponibile");
			}
		}
		
		resampleMyProfile(p);
		return (Profile) p;*/
		
		Profile local=null;
		if(type==ProfileType.PRESENCE){
		local=new ProfileImpl(room,localPresenceProfile,ProfileType.PRESENCE);
		
		}
		if(type==ProfileType.TEMPERATURE){
			local=new ProfileImpl(room,localTempProfile,ProfileType.TEMPERATURE);
			
			}
		if(type==ProfileType.LIGHT){
			local=new ProfileImpl(room,localLightProfile,ProfileType.LIGHT);
			
			}
		return local;
		}

	
	
	public double getRealTimevalue( ProfileType type, String room ) throws RemoteException,ProfilingSystemException{
		
		System.out.println("Richiesto il valore real time di "+type.toString()+" per la stanza "+room+" dal "+dve.toString());
		double p=0;
		for(Room r:rooms){
			if(r.getRoomName().equals(room)){
				if(r.isRealTimeValueAvailable()){
					p=r.getRealTimeValue(type);
				}
				else 
					throw new ProfilingSystemException("Valore non disponibile");
			}
		}
		return p;
	}
	
	public void notifyDVEnewPredictionAvailable(String roomName){
		
		if(dve!=null){
			System.out.println("Notifico la presenza di nuovi profili per la stanza "+roomName+" al DVE!");
			try {
				dve.newPredictedProfile(ProfileType.PRESENCE, roomName);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				dve.newPredictedProfile(ProfileType.TEMPERATURE, roomName);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				dve.newPredictedProfile(ProfileType.LIGHT, roomName);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void newPresenceValue(String roomName, Object num)
	{
		System.out.println("Numero persone: "+num + " rilevate nella stanza : "+ roomName );
		int presenceValue=0;
		Byte i;
		i =(Byte) num;
		presenceValue= i;
		
		for(Room r:rooms){
			if(r.getRoomName().equals(roomName)){
					r.newPresenceValue(presenceValue);
			}
		}
		notifyNewRealTimeValue(ProfileType.PRESENCE,roomName,(double) presenceValue);
		
	}
	
	public void notifyNewRealTimeValue(ProfileType type, String room, double val ){
		
		if(dve!=null){
			System.out.println("Notifico il nuovo valore di "+type.toString()+" al "+dve.toString());		
			try {
				dve.notifyRealTimeValue(type, room, val );
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void SubscribeHVA(HomeVirtualizationApplication hva) throws RemoteException{
		
		this.hva = hva;
		System.out.println("Si è sottoscritto un nuovo HVA" + hva.toString());
		DAILYPROFILERECORDER.setHVA(hva);
	}
	
	
	public void resampleMyProfile(ProfileImpl profile){
		double[] p=new double[0];
		try {
			p=new double[profile.getValues().length];
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			p=profile.getValues();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int totalSamples=(int) Math.floor(24*60/DVEsamplingTime);
		int difference=0;
		try {
			difference = (int) Math.floor(profile.getValues().length/totalSamples);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int counter;
		double[] finalProfile=new double[totalSamples];
		for(int i=0;i<totalSamples;i++){
			counter=0;
			for(int j=difference*i;j<difference*(i+1);j++){
				counter+=1;
				finalProfile[i]+=p[j];
			}
			finalProfile[i]=finalProfile[i]/counter;
		}
		profile.setProfile(finalProfile);
	}

	public void notifyNewTemperatureDesiredValue(double t,String roomName) throws RemoteException{
		
		for(int i=0;i<96;i++){
			localTempProfile[i]=t;
		}
		

		if (dve != null) {
			dve.newPredictedProfile(ProfileType.TEMPERATURE, roomName);
		}

	}	


	public void notifyNewLightDesiredValue(double t,String roomName) throws RemoteException{
		
		for(int i=0;i<96;i++){
			localLightProfile[i]=t;
		}
		
		if( dve != null ) {
			dve.newPredictedProfile(ProfileType.LIGHT, roomName);
		}

	}	

	public void createProfile(){
		for(int i=0;i<96;i++){
			localPresenceProfile[i]=1;
			localTempProfile[i]=23;
			localLightProfile[i]=470;
		}
		
		
	}
}
