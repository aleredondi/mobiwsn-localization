
package client_applications.profiling_system.profiling_system_impl.profiles_analyzer;


import java.sql.*;
import java.util.ArrayList;

import client_applications.profiling_system.profiling_system_impl.*;

/***
* @author Antimo Barbato
*
*/


public class ProfilesAnalyzer {
	
    private String databaseName;
    private String databaseUserName;
    private String databaseUserPassword;
    private String databaseURL;
    private int TrainingPeriod;
    private int profileLength;
    private int samplingTime;
    private ArrayList<RoomLocalDB> roomsDB=new ArrayList<RoomLocalDB>();
    private int[]roomsIdentifier;
    private ProfilingSystemImpl profilingSystem;
    
    
    
	public ProfilesAnalyzer(int TrainingPeriod,String dbName,String dbURL,String dbUserName,String dbUserPassword,int profileLength, int samplingTime,ProfilingSystemImpl p){
		
		this.databaseName=dbName;
		this.databaseUserName=dbUserName;
		this.databaseUserPassword=dbUserPassword;
		this.databaseURL=dbURL;
		this.TrainingPeriod=TrainingPeriod;
		this.profileLength=profileLength;
		this.samplingTime=samplingTime;
		this.profilingSystem=p;
		System.out.println("Profiles analyzer called!");
		
		
	}
	
	public void recordingLocalDB(){
	    try{
            
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword );                                                                                              
            System.out.println("Connection to MySQL server to locally load the stored daily profiles");

            ResultSet rs;
            ResultSet rs4;

            Statement stmt = null;
            	
            conn.setCatalog(databaseName); 
            stmt = conn.createStatement();
                
            rs=stmt.executeQuery("Select MAX(idroom) FROM rooms;");
            while(rs.next()) {
                	roomsIdentifier=new int[rs.getInt("MAX(idroom)")];
            }
            
            rs4=stmt.executeQuery("Select idroom FROM rooms;");
            
            int h=0;
            while(rs4.next()){
            	roomsIdentifier[h]=rs4.getInt("idroom");
            	h+=1;
            }
            
            for(int l=0;l<roomsIdentifier.length;l++){
            	
            	RoomLocalDB roomDB=new RoomLocalDB(roomsIdentifier[l],databaseName,databaseURL,databaseUserName,databaseUserPassword,profileLength,samplingTime,profilingSystem);

            	for(int j=1;j<TrainingPeriod+1;j++){
            		
                	int[] localPresenceProfile=new int[profileLength];
                	double[] localTemperatureProfile=new double[profileLength];
                	double[] localLightProfile=new double[profileLength];
            		ResultSet rs1;
            		ResultSet rs2;
            		ResultSet rs3;
            		
                	

            		rs1=stmt.executeQuery("Select value FROM DailyPresenceProfiles WHERE idroom="+roomsIdentifier[l]+" AND idday="+j+" ORDER BY idposition;");

            		
            		int k=0;
            		while(rs1.next()) {
            			localPresenceProfile[k]=rs1.getInt("value");
            			k+=1;
            		}
            		
            		rs2=stmt.executeQuery("Select value FROM DailyTemperatureProfiles WHERE idroom="+roomsIdentifier[l]+" AND idday="+j+" ORDER BY idposition;");
            		k=0;
            		while(rs2.next()) {
            			localTemperatureProfile[k]=rs2.getDouble("value");
            			k+=1;
            		}
            		
               		rs3=stmt.executeQuery("Select value FROM DailyLightProfiles WHERE idroom="+roomsIdentifier[l]+" AND idday="+j+" ORDER BY idposition;");
            		k=0;
            		while(rs3.next()) {
            			localLightProfile[k]=rs3.getDouble("value");
            			k+=1;
            		}

                	roomDB.addPresenceProfiles(localPresenceProfile);
                	roomDB.addTemperatureProfiles(localTemperatureProfile);
                	roomDB.addLightProfiles(localLightProfile);
            	}
            	roomsDB.add(roomDB);

            }
            stmt.close(); // close the object statement.
            conn.close(); // close the connection 
            System.out.println("Profiles locally stored!");
        }

        catch (Exception ex) {
            System.out.println("SQLException  here: " + ex.getMessage());
        }
 
	}
	
	public void ProfilesAnalysis(){
		
		System.out.println("Starting profiles analysis!");
		for(RoomLocalDB r:roomsDB){
		    r.findFinalProfiles();
		}
		for(RoomLocalDB r:roomsDB){
		    r.getPredictedProfile();
		}
		
	}
	
	public int[] getProfilesSequence(int roomId){
		int [] ProfilesSequence=null;
		for(int i=0;i<roomsDB.size();i++){
			if(roomsDB.get(i).getRoomId()==roomId)
				ProfilesSequence=roomsDB.get(i).getProfilesSequence();
		}
		return ProfilesSequence;
	}
	
}