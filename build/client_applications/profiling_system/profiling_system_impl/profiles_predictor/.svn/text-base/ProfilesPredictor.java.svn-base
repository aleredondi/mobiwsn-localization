
package client_applications.profiling_system.profiling_system_impl.profiles_predictor;

import java.sql.*;

import client_applications.profiling_system.profiling_system_impl.*;

/***
* @author Antimo Barbato
*
*/

public class ProfilesPredictor {
	
	private int roomid;
	private int dim;
	private int[] profilesSequence;
	private int T;
    private String databaseName;
    private String databaseUserName;
    private String databaseUserPassword;
    private String databaseURL;
	private int predictedProfile;
	private int profilesNumber;
	private String profileType1="presence profile";
	private String profileType2="temperature profile";
	private String profileType3="light profile";
	private double acceptedReliability=0.75;
	private double reliability;
	private ProfilingSystemImpl profilingSystem;
	
	public ProfilesPredictor(int roomid,int[] profilessequence,int T, String dbURL,String dbUserPassword, String dbUserName,String dbName,int profilesNumber,ProfilingSystemImpl p){
		
		this.dim=profilessequence.length;
		this.profilesSequence=new int[dim];
		this.profilesSequence=profilessequence;
		this.databaseName=dbName;
		this.databaseUserName=dbUserName;
		this.databaseUserPassword=dbUserPassword;
		this.databaseURL=dbURL;
		this.roomid=roomid;
		this.profilingSystem=p;
		this.T=T;
		this.profilesNumber=profilesNumber;
		System.out.println("Profiles predictor called!");
		
	}
	
	//Predicted Profile Computation
	public void predictor(){
		
		System.out.println("Starting prediction!");
		int M=1;
		boolean found=false;
		
		while( !found & M<=T){
			
			nOrderPredictor nOrdPred=new nOrderPredictor(profilesSequence,M,profilesNumber,acceptedReliability);
			found=nOrdPred.PredicteProfile();
			M=M+1;
			if(found){
				predictedProfile=nOrdPred.getPredictedProfile();
				reliability=nOrdPred.getReliability();
			}
		}
		
		if(reliability==0){
			predictedProfile=profilesSequence[profilesSequence.length-1];
		}
	}
	
	public void predictedProfileLoader(){
		
		System.out.println("Recording predicted profile.....!");
		int idtypePresence=0;
		int idtypeLight=0;
		int idtypeTemperature=0;
		
	    
	    try{
            
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 

            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword );                                                                                         
            System.out.println("Connection to MySQL server to store the predicted profiles identifiers!");

            ResultSet rs1;
            ResultSet rs2;
            ResultSet rs3;
            Statement stmt = null;
            	
            conn.setCatalog(databaseName); 
            stmt = conn.createStatement();

            rs1=stmt.executeQuery("Select idtype FROM profileType WHERE profilename='"+profileType1+"';");
            if(rs1.next()) {
            	idtypePresence=rs1.getInt("idtype");
            }
            
            rs2=stmt.executeQuery("Select idtype FROM profileType WHERE profilename='"+profileType2+"';");
            if(rs2.next()) {
            	idtypeTemperature=rs2.getInt("idtype");
            }
            
            rs3=stmt.executeQuery("Select idtype FROM profileType WHERE profilename='"+profileType3+"';");
            if(rs3.next()) {
            	idtypeLight=rs3.getInt("idtype");
            }
            
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String currentTime = sdf.format(dt);
            
            stmt.executeUpdate("UPDATE PredictedProfiles SET idprofile="+predictedProfile+",predictionDate='"+currentTime+"' WHERE idroom="+roomid+" AND idtype="+idtypePresence+";");
            stmt.executeUpdate("UPDATE PredictedProfiles SET idprofile="+predictedProfile+",predictionDate='"+currentTime+"' WHERE idroom="+roomid+" AND idtype="+idtypeTemperature+";");
            stmt.executeUpdate("UPDATE PredictedProfiles SET idprofile="+predictedProfile+",predictionDate='"+currentTime+"' WHERE idroom="+roomid+" AND idtype="+idtypeLight+";");
            stmt.executeUpdate("UPDATE UpdateTable SET updValue="+1+" WHERE idroom="+roomid+" AND idtype="+idtypePresence+";");
            stmt.executeUpdate("UPDATE UpdateTable SET updValue="+1+" WHERE idroom="+roomid+" AND idtype="+idtypeTemperature+";");
            stmt.executeUpdate("UPDATE UpdateTable SET updValue="+1+" WHERE idroom="+roomid+" AND idtype="+idtypeLight+";");
            
            
            stmt.close(); // close the object statement.
            conn.close(); // close the connection 
            System.out.println("The predicted profiles have been stored!");
        }
        
        catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
		
	    
        String roomName="";
	    try{
            
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword );                                                                                         

            ResultSet rs1;
            Statement stmt = null;
            	
            conn.setCatalog(databaseName); 
            stmt = conn.createStatement();

            rs1=stmt.executeQuery("Select roomname FROM rooms WHERE idroom="+"roomid;");
            if(rs1.next()) {
            	roomName=rs1.getString("roomname");
            }
            
            stmt.close(); // close the object statement.
            conn.close(); // close the connection 
        }
        
        catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
        profilingSystem.setPredictedProfile(roomName);
	}
	
	

}









