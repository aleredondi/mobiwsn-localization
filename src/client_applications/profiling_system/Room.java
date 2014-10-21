package client_applications.profiling_system;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

import remote_interfaces.clients.profiling_system.*;

public class Room  {

	private String roomName;
	private Hashtable<ProfileType,ProfileImpl> profiles=new Hashtable<ProfileType,ProfileImpl>();
	private boolean predictionAvailable;
    private String databaseName;
    private String databaseUserName;
    private String databaseUserPassword;
    private String databaseURL;
    private int roomID=0;
	private String presenceProfileName="presence profile"; // presence profile type name
	private String temperatureProfileName="temperature profile";// temperature profile type name
	private String lightProfileName="light profile";// light profile type name
	private String RTpresenceValue="realtime presence value";// real time presence value type name
	private String RTtemperatureValue="realtime temperature value";// real time temperature value type name
	private String RTlightValue="realtime light value";// real time light value type name
	private double [] presProf;
	private double [] tempProf;
	private double [] ligProf;
	private boolean realTimeValueAvailable=true;
	private int presenceValue=0;
	
	public Room( String room,int roomIdentifier,boolean a,String databaseURL,String databaseUserName,String databaseUserPassword,String databaseName){
		this.roomName = room;
		this.roomID=roomIdentifier;
		predictionAvailable=a;
		this.databaseName=databaseName;
		this.databaseUserName=databaseUserName;
		this.databaseUserPassword=databaseUserPassword;
		this.databaseURL=databaseURL;
		try {
			profiles.put(ProfileType.PRESENCE, new ProfileImpl(roomName,presProf, ProfileType.PRESENCE));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			profiles.put(ProfileType.TEMPERATURE, new ProfileImpl(roomName,tempProf, ProfileType.TEMPERATURE));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			profiles.put(ProfileType.LIGHT, new ProfileImpl(roomName,ligProf, ProfileType.LIGHT));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setProfiles(ProfileType type){
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
            
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(dt);
            String currentDate2="";
	        rs=stmt.executeQuery("SELECT predictionDate FROM PredictedProfiles WHERE idroom="+roomID+";");
		     if(rs.next()){
		    	 currentDate2=rs.getString("predictionDate");
		     }
         		
         		int idType=0;
         		int idProfile=0;
         		int dim=0;
         	    
 	          if(currentDate.equals(currentDate2)){
 	            	
 	            	predictionAvailable=predictionAvailable& true;
 	            
 	            	rs1=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+presenceProfileName+"';");
 	            	while(rs1.next()){
 	            		idType=rs1.getInt("idtype");
 	            	}

 	            	rs2=stmt.executeQuery("SELECT idprofile FROM PredictedProfiles WHERE idroom="+roomID+" AND idtype="+idType+";");
 	            	while(rs2.next()){
 	            		idProfile=rs2.getInt("idprofile");
 	            	}
         		
         		
 	            	rs3=stmt.executeQuery("SELECT MAX(idposition) FROM FinalPresenceProfiles WHERE idroom="+roomID+" AND idprofile="+idProfile+";");

 	            	while(rs3.next()) {
 	            		dim=rs3.getInt("MAX(idposition)");
 	            	}
         		
 	            	presProf=new double[dim];
         		
 	            	rs4=stmt.executeQuery("SELECT value FROM FinalPresenceProfiles WHERE idroom="+roomID+" AND idprofile="+idProfile+" ORDER BY idposition;");
         		
 	            	int k=0;
 	            	while(rs4.next()) {
 	            		presProf[k]=rs4.getDouble("value");
 	            		k+=1;
 	            	}
         		
 	       		try {
 	   			profiles.remove(ProfileType.PRESENCE);
 	   			profiles.put(ProfileType.PRESENCE, new ProfileImpl(roomName,presProf, ProfileType.PRESENCE));
	 	   		} catch (RemoteException e) {
	 	   			// TODO Auto-generated catch block
	 	   			e.printStackTrace();
	 	   		}
	 	   		
	 	   		
	 	   		
	            	predictionAvailable=predictionAvailable& true;
	 	            
 	            	rs1=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+temperatureProfileName+"';");
 	            	while(rs1.next()){
 	            		idType=rs1.getInt("idtype");
 	            	}

 	            	rs2=stmt.executeQuery("SELECT idprofile FROM PredictedProfiles WHERE idroom="+roomID+" AND idtype="+idType+";");
 	            	while(rs2.next()){
 	            		idProfile=rs2.getInt("idprofile");
 	            	}
         		
         		
 	            	rs3=stmt.executeQuery("SELECT MAX(idposition) FROM FinalTemperatureProfiles WHERE idroom="+roomID+" AND idprofile="+idProfile+";");

 	            	while(rs3.next()) {
 	            		dim=rs3.getInt("MAX(idposition)");
 	            	}
         		
 	            	tempProf=new double[dim];
         		
 	            	rs4=stmt.executeQuery("SELECT value FROM FinalTemperatureProfiles WHERE idroom="+roomID+" AND idprofile="+idProfile+" ORDER BY idposition;");
         		
 	            	k=0;
 	            	while(rs4.next()) {
 	            		tempProf[k]=rs4.getDouble("value");
 	            		k+=1;
 	            	}
         		
 	       		try {
 	   			profiles.remove(ProfileType.TEMPERATURE);
 	   			profiles.put(ProfileType.TEMPERATURE, new ProfileImpl(roomName,tempProf, ProfileType.TEMPERATURE));
	 	   		} catch (RemoteException e) {
	 	   			// TODO Auto-generated catch block
	 	   			e.printStackTrace();
	 	   		}
	 	   		
	 	   		
	 	   		
	 	   		
	            	predictionAvailable=predictionAvailable& true;
	 	            
 	            	rs1=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+lightProfileName+"';");
 	            	while(rs1.next()){
 	            		idType=rs1.getInt("idtype");
 	            	}

 	            	rs2=stmt.executeQuery("SELECT idprofile FROM PredictedProfiles WHERE idroom="+roomID+" AND idtype="+idType+";");
 	            	while(rs2.next()){
 	            		idProfile=rs2.getInt("idprofile");
 	            	}
         		
         		
 	            	rs3=stmt.executeQuery("SELECT MAX(idposition) FROM FinalLightProfiles WHERE idroom="+roomID+" AND idprofile="+idProfile+";");

 	            	while(rs3.next()) {
 	            		dim=rs3.getInt("MAX(idposition)");
 	            	}
         		
 	            	ligProf=new double[dim];
         		
 	            	rs4=stmt.executeQuery("SELECT value FROM FinalLightProfiles WHERE idroom="+roomID+" AND idprofile="+idProfile+" ORDER BY idposition;");
         		
 	            	k=0;
 	            	while(rs4.next()) {
 	            		ligProf[k]=rs4.getDouble("value");
 	            		k+=1;
 	            	}
         		
 	       		try {
 	   			profiles.remove(ProfileType.LIGHT);
 	   			profiles.put(ProfileType.LIGHT, new ProfileImpl(roomName,ligProf, ProfileType.LIGHT));
	 	   		} catch (RemoteException e) {
	 	   			// TODO Auto-generated catch block
	 	   			e.printStackTrace();
	 	   		}

 	          }
	            stmt.close(); // close the object statement
	            conn.close(); // close the connection 
	            
	    }
        catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
       
	}
	
	public ProfileImpl getProfiles(ProfileType type){
		return profiles.get(type);
	}
	
	public String getRoomName(){
		return roomName;
	}
	
	public boolean isPredictionAvailabel(){
		return predictionAvailable;
	}
	
	public double getRealTimeValue(ProfileType type){
		double value=0;
		realTimeValueAvailable=false;
	    try{
            
            Class.forName("com.mysql.jdbc.Driver").newInstance(); // load and register the JDBC driver classes for MySQL.

            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); //connection to the database


            ResultSet rs2;
            ResultSet rs3;
            ResultSet rs5;
            ResultSet rs6;
            Statement stmt = null;
            conn.setCatalog(databaseName); 
            stmt = conn.createStatement();
        	int idTemperatureType=0;
        	int idLightType=0;

        	
        	if(type==ProfileType.PRESENCE){
        		value=presenceValue;
	        	realTimeValueAvailable=true;
        	}
        	
        	
        	if(type==ProfileType.TEMPERATURE){
	        	rs2=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+RTtemperatureValue+"';");
	        	while(rs2.next())
	        		idTemperatureType=rs2.getInt("idtype");
	        	rs5=stmt.executeQuery("SELECT value FROM RealTime WHERE idtype="+idTemperatureType+" AND idroom="+roomID+";");
	        	while(rs5.next())
	        		value=rs5.getInt("value");
	        	realTimeValueAvailable=true;
        	}
        	if(type==ProfileType.LIGHT){
        	rs3=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+RTlightValue+"';");
        	while(rs3.next())
        		idLightType=rs3.getInt("idtype");
         
        	rs6=stmt.executeQuery("SELECT value FROM RealTime WHERE idtype="+idLightType+" AND idroom="+roomID+";");
        	while(rs6.next())
        		value=rs6.getInt("value");
        	realTimeValueAvailable=true;
        	}

            stmt.close(); 
            conn.close();
        }
        
        catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
		
		
		return value;
	}
	
	public boolean isRealTimeValueAvailable(){
		return realTimeValueAvailable;
	}

	public void newPresenceValue(int i)
	{
		this.presenceValue= i;
		
	}

}
