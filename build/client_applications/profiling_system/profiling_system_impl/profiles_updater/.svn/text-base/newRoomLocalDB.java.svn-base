
package client_applications.profiling_system.profiling_system_impl.profiles_updater;


import client_applications.profiling_system.profiling_system_impl.*;
import client_applications.profiling_system.profiling_system_impl.profiles_analyzer.RoomLocalDB;
import client_applications.profiling_system.profiling_system_impl.profiles_analyzer.doubleDailyProfile;
import client_applications.profiling_system.profiling_system_impl.profiles_analyzer.intDailyProfile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;





public class newRoomLocalDB extends RoomLocalDB {
	
	private ArrayList<doubleDailyProfile> adjustedFinalProfiles=new ArrayList<doubleDailyProfile>();
	private intDailyProfile todayProfile;
	private int[] todayprofile;
    private int firstPredictedProfile;
    public int lastPredictedProfile;
    private double[] area;
    private double todayArea=0;
    private int acceptedDelay=8; // acepted delay in minutes
    public int[] potentialProfiles;
    private double A=0.12;
    private double B=0.1;
    public boolean profileChanged=false;
	private String profileName1="presence profile";
	private String profileName2="temperature profile";
	private String profileName3="light profile";
	private int profilesNumber=0;
	public int[] profilesSequence;

	
	public newRoomLocalDB(int roomid,String dbName,String dbURL,String dbUserName,String dbUserPassword,int profileLength,int samplingTime,int firstPredictedProfile,int[] currentProfile,int[] profilesSequence,ProfilingSystemImpl p) {
		super(roomid,dbName,dbURL,dbUserName,dbUserPassword,profileLength,samplingTime,p);
		this.firstPredictedProfile=firstPredictedProfile;
		this.lastPredictedProfile=firstPredictedProfile;
		this.todayProfile= new intDailyProfile(currentProfile,currentProfile.length);
		this.profilesSequence=profilesSequence;
		
	}
	
	
	public void updateProfiles(){
		
		//final profiles loading
		int dim=todayProfile.getDailyProfile().length;
		
		//accepted delay control
		if(acceptedDelay*60/samplingTime>dim)
			acceptedDelay=(int) dim*samplingTime/60;
		
	    try{
            
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword );                                                                                              
            System.out.println("Connection to MySQL server to locally load the stored daily profiles");

            
            ResultSet rs;
            Statement stmt = null;
            	
            conn.setCatalog(databaseName); 
            stmt = conn.createStatement();
                
            rs=stmt.executeQuery("SELECT MAX(idprofile) FROM FinalPresenceProfiles WHERE idroom="+roomid+";");
            while(rs.next())
            	profilesNumber=rs.getInt("MAX(idprofile)");

                for(int j=1;j<profilesNumber+1;j++){
                	
                	double[] localPresenceProfile=new double[dim];
                	
                	for(int k=1;k<dim+1;k++){
                		ResultSet rs1;
                		rs1=stmt.executeQuery("Select value FROM FinalPresenceProfiles WHERE idroom="+roomid+" AND idprofile="+j+" AND idposition="+k+";");

                		while(rs1.next()) {
                			localPresenceProfile[k-1]=rs1.getInt("value");
                		}
                	}
                	doubleDailyProfile local=new doubleDailyProfile(localPresenceProfile,dim);
                	adjustedFinalProfiles.add(local);
            	}
            stmt.close(); // close the object statement.
            conn.close(); // close the connection 
            System.out.println("Profiles locally stored!");
        }

        catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }

		double[] correlationMeanValues=new double[adjustedFinalProfiles.size()];
		
		// Profiles Area Computation
		area=new double[adjustedFinalProfiles.size()];
		for(int i=0;i<adjustedFinalProfiles.size();i++)
			area[i]=adjustedFinalProfiles.get(i).getProfileArea();
		todayArea=todayProfile.getProfileArea();
		
		// Profiles cross correlation mean values computation 
		for (int kk=0;kk<adjustedFinalProfiles.size();kk++)
	       correlationMeanValues[kk]=crossCorrelationMeanValue(adjustedFinalProfiles.get(kk).getDailyProfile(),todayProfile.getDailyProfile(),area[kk],todayArea,todayProfile.getDailyProfile().length,acceptedDelay);
		
		double TodayProfileAutoCorrelationMeanValue=intCrossCorrelationMeanValue(todayProfile.getDailyProfile(),todayProfile.getDailyProfile(),todayArea,todayArea,todayProfile.getDailyProfile().length,acceptedDelay);
		
		
		// potential profiles for the selected room
		potentialProfiles=new int[profilesNumber];
		for(int i=0;i<potentialProfiles.length;i++){
			if(TodayProfileAutoCorrelationMeanValue-correlationMeanValues[i]<=A*(TodayProfileAutoCorrelationMeanValue+correlationMeanValues[i])){
				potentialProfiles[i]=1;
			}
		}
		
		// profile update
		
		for(int i=0;i<profilesNumber;i++){
			if(((correlationMeanValues[i]-correlationMeanValues[firstPredictedProfile])>=B*(correlationMeanValues[i]-correlationMeanValues[firstPredictedProfile-1]))&(correlationMeanValues[i]>correlationMeanValues[lastPredictedProfile-1])){
				lastPredictedProfile=i+1;
				profileChanged=true;
			}
		}
		
		if(profileChanged)
			loadNewProfile();
	}
			
		
	
	
	public double crossCorrelationMeanValue(double[] x,int[] y,double areax, double areay,int length,int accepDelay){
		
		
		double[] correlation=new double[2*length-1]; 
	    double sum;
	    

	    for (int t=-(length-1);t<length;t++) {
	        sum=0;
	        
	        if(t<0){
		        for (int n=length-1;n>-t+1;n--) {
		            sum+=x[n]*y[n+t];
		        }
	        }
	        else {
	            for (int n=0;n<length-t;n++) {
	               sum+=x[n]*y[n+t];
	            }
	        }
	        correlation[t+length-1]=sum;
	    }
	    double meanValue=0;
	    
	    int delay=(int) accepDelay*60/samplingTime;
	    int count=0;
	    for(int j=length-delay;j<length+delay;j++){
	    	meanValue+=correlation[j];
	    	count+=1;
	    }
	    meanValue=(meanValue/count)/(areax+areay);
	    return meanValue;
	}
	

	
public double intCrossCorrelationMeanValue(int[] x,int[] y,double areax, double areay,int length,int accepDelay){
		
		
		double[] correlation=new double[2*length-1]; 
	    double sum;
	    
	    if(accepDelay>length)
	    	accepDelay=length-1;

	    for (int t=-(length-1);t<length;t++) {
	        sum=0;
	        
	        if(t<0){
		        for (int n=length-1;n>-t+1;n--) {
		            sum+=x[n]*y[n+t];
		        }
	        }
	        else {
	            for (int n=0;n<length-t;n++) {
	               sum+=x[n]*y[n+t];
	            }
	        }
	        correlation[t+length-1]=sum;
	    }
	    double meanValue=0;
	    
	    int delay=(int) accepDelay*60/samplingTime;
	    int count=0;
	    for(int j=length-delay;j<length+delay;j++){
	    	meanValue+=correlation[j];
	    	count+=1;
	    }
	    meanValue=(meanValue/count)/(areax+areay);
	    return meanValue;
	}
	

	public void loadNewProfile(){
		
	    try{
            
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword );                                                                                              
            System.out.println("Connection to MySQL server to upload the predicted profile");

            ResultSet rs1;
            ResultSet rs2;
            ResultSet rs3;
            Statement stmt = null;
            int idTypePresence=0;
            int idTypeLight=0;
            int idTypeTemperature=0;
            	
            conn.setCatalog(databaseName); 
            stmt = conn.createStatement();
            
            rs1=stmt.executeQuery("SELECT idtype FROM profileType WHERE idroom="+roomid+" AND profilename='"+profileName1+"';");
            rs2=stmt.executeQuery("SELECT idtype FROM profileType WHERE idroom="+roomid+" AND profilename='"+profileName2+"';");
            rs3=stmt.executeQuery("SELECT idtype FROM profileType WHERE idroom="+roomid+" AND profilename='"+profileName3+"';");
            
            while(rs1.next()) {
            	idTypePresence= rs1.getInt("idtype");
              }
            while(rs2.next()) {
            	idTypeTemperature= rs2.getInt("idtype");
              }
            while(rs3.next()) {
            	idTypeLight= rs3.getInt("idtype");
              }
            
            
            stmt.executeUpdate("UPDATE PredictedProfiles SET idprofile="+lastPredictedProfile+" WHERE idroom="+roomid+" AND idtype="+idTypePresence+";");
            stmt.executeUpdate("UPDATE PredictedProfiles SET idprofile="+lastPredictedProfile+" WHERE idroom="+roomid+" AND idtype="+idTypeLight+";");
            stmt.executeUpdate("UPDATE PredictedProfiles SET idprofile="+lastPredictedProfile+" WHERE idroom="+roomid+" AND idtype="+idTypeTemperature+";");
            
            stmt.executeUpdate("UPDATE UpdateTable SET updValue=1 WHERE idroom="+roomid+" AND idtype="+idTypePresence+";");
            stmt.executeUpdate("UPDATE UpdateTable SET updValue=1 WHERE idroom="+roomid+" AND idtype="+idTypeLight+";");
            stmt.executeUpdate("UPDATE UpdateTable SET updValue=1 WHERE idroom="+roomid+" AND idtype="+idTypeTemperature+";");
            
            stmt.close(); // close the object statement.
            conn.close(); // close the connection 
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