
package client_applications.profiling_system.profiling_system_impl.profiles_analyzer;



import client_applications.profiling_system.profiling_system_impl.*;
import client_applications.profiling_system.profiling_system_impl.profiles_analyzer.doubleDailyProfile;
import client_applications.profiling_system.profiling_system_impl.profiles_analyzer.intDailyProfile;
import client_applications.profiling_system.profiling_system_impl.profiles_predictor.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


/***
* @author Antimo Barbato
*
*/

public class RoomLocalDB {
	
	private ArrayList<intDailyProfile> PresenceProfiles=new ArrayList<intDailyProfile>();
	private ArrayList<doubleDailyProfile> TemperatureProfiles=new ArrayList<doubleDailyProfile>();
	private ArrayList<doubleDailyProfile> LightProfiles=new ArrayList<doubleDailyProfile>();
	protected ArrayList<doubleDailyProfile> finalPresenceProfiles=new ArrayList<doubleDailyProfile>();
	private ArrayList<doubleDailyProfile> finalTemperatureProfiles=new ArrayList<doubleDailyProfile>();
	private ArrayList<doubleDailyProfile> finalLightProfiles=new ArrayList<doubleDailyProfile>();
	protected int roomid;
	protected int profileLength;
	protected int samplingTime;
	private int acceptedDelay=8;// accepted delay in minutes
    protected String databaseName;
    protected String databaseUserName;
    protected String databaseUserPassword;
    protected String databaseURL;
    private int T=7;
    private int Tmax=10;
    private int[] profilesSequence;
    private int profilesNumber;
    protected ProfilingSystemImpl profilingSystem;
	
	public RoomLocalDB(int roomid,String dbName,String dbURL,String dbUserName,String dbUserPassword,int profileLength,int samplingTime,ProfilingSystemImpl p) {
          this.roomid=roomid;
          this.profileLength=profileLength;
          this.samplingTime=samplingTime;
  		  this.databaseName=dbName;
		  this.databaseUserName=dbUserName;
		  this.databaseUserPassword=dbUserPassword;
		  this.databaseURL=dbURL;
		  this.profilingSystem=p;
	}
	
	public void addPresenceProfiles(int[] DailyProfile){
		intDailyProfile NEW=new intDailyProfile(DailyProfile,profileLength);
		PresenceProfiles.add(NEW);
	}
	
	public void addTemperatureProfiles(double[] DailyProfile){
		doubleDailyProfile NEW=new doubleDailyProfile(DailyProfile,profileLength);
		TemperatureProfiles.add(NEW);
	}
	
	public void addLightProfiles(double[] DailyProfile){
		doubleDailyProfile NEW=new doubleDailyProfile(DailyProfile,profileLength);
		LightProfiles.add(NEW);
	}
	
	public int getRoomId(){
		return this.roomid;
	}
	
	public void findFinalProfiles(){
		
		//control accepted delay
		
		if(acceptedDelay*60/samplingTime>profileLength)
			acceptedDelay=(int) profileLength*samplingTime/60;
		
		
		// Profiles Area Computation
		int[] area=new int[PresenceProfiles.size()];
		double[][] correlationMeanValues=new double[PresenceProfiles.size()][PresenceProfiles.size()];
		
		int position=0;
		for(intDailyProfile p:PresenceProfiles){
			area[position]=p.getProfileArea();
			position +=1;
		}
		
		// Profiles cross correlation mean values computation 
		for (int kk=0;kk<PresenceProfiles.size();kk++){
	       int jj=kk;
	       while(jj<PresenceProfiles.size()){
	       correlationMeanValues[kk][jj]=crosscorrelationMeanValue(PresenceProfiles.get(kk).getDailyProfile(),PresenceProfiles.get(jj).getDailyProfile(),area[kk],area[jj],profileLength,acceptedDelay);
	        jj+=1;
	       }
		}

		// Profiles association computation
		int dimension=PresenceProfiles.size();
		int[][] profilesMatrix=new int [dimension][dimension];
		for (int k=0;k<PresenceProfiles.size();k++){
			
		    if(columnsSum(profilesMatrix,dimension,k)==0){
		        int j=k;
		        while(j<PresenceProfiles.size()){
		            if((correlationMeanValues[k][j]>=(correlationMeanValues[k][k]-0.12*(correlationMeanValues[j][j]+correlationMeanValues[k][k])))&(columnsSum(profilesMatrix,dimension,j)==0)){
		            	profilesMatrix[k][j]=1;
		            }
		            j=j+1;
		        }
		    }
		}
		

		
		// Final Profiles Number computation
		profilesNumber=0;
		for(int i=0;i<PresenceProfiles.size();i++){
			profilesNumber=profilesNumber+profilesMatrix[i][i];
		}
		
		//Final Profiles identifier definition
		
		int profileIdentifier=1;
		for(int i=0;i<PresenceProfiles.size();i++){
			if(profilesMatrix[i][i]>=1){
				profilesMatrix[i][i]=profileIdentifier;
				profileIdentifier=profileIdentifier+1;
			}
		}

		//Final Profiles Sequence computation
		
		profilesSequence=new int[PresenceProfiles.size()];
		for(int k=0;k<PresenceProfiles.size();k++){
			int j=0;
			while(profilesSequence[k]==0){
				if(profilesMatrix[j][k]>=1)
					profilesSequence[k]=profilesMatrix[j][j];
				j=j+1;
			}
		}
		
		//Final Presence Profiles Computation
		
		for(int i=0;i<profilesNumber;i++){
			double[] profile=new double[profileLength];
			int norm=0;
			for(int k=0;k<PresenceProfiles.size();k++){
				if(profilesSequence[k]==i+1){
					norm=norm+1;
					for(int j=0;j<profileLength;j++)
						profile[j]=profile[j]+PresenceProfiles.get(k).getDailyProfile()[j];
				}
			}
			
			for(int j=0;j<profileLength;j++)
				profile[j]=profile[j]/norm;
			doubleDailyProfile finalprofile=new doubleDailyProfile(profile,profileLength);
			finalPresenceProfiles.add(finalprofile);
		}
		
		
		//Final Temperature Profile Computation
		
		for(int i=0;i<profilesNumber;i++){
			double[] profile=new double[profileLength];
			int norm=0;
			for(int k=0;k<TemperatureProfiles.size();k++){
				if(profilesSequence[k]==i+1){
					norm=norm+1;
					for(int j=0;j<profileLength;j++)
						profile[j]=profile[j]+TemperatureProfiles.get(k).getDailyProfile()[j];
				}
			}
			
			for(int j=0;j<profileLength;j++)
				profile[j]=profile[j]/norm;
			doubleDailyProfile finalprofile=new doubleDailyProfile(profile,profileLength);
			finalTemperatureProfiles.add(finalprofile);
		}
		
	
		
		//Final Light Profile Computation
		
		for(int i=0;i<profilesNumber;i++){
			double[] profile=new double[profileLength];
			int norm=0;
			for(int k=0;k<LightProfiles.size();k++){
				if(profilesSequence[k]==i+1){
					norm=norm+1;
					for(int j=0;j<profileLength;j++)
						profile[j]=profile[j]+LightProfiles.get(k).getDailyProfile()[j];
				}
			}
			
			for(int j=0;j<profileLength;j++)
				profile[j]=profile[j]/norm;
			doubleDailyProfile finalprofile=new doubleDailyProfile(profile,profileLength);
			finalLightProfiles.add(finalprofile);
		}
		
		
		FinalProfileloading(finalPresenceProfiles,finalTemperatureProfiles,finalLightProfiles,profilesSequence,profilesNumber);
		
		
		//Presence profile sequence period computation
		
		
		double[] profilesSequenceAutoCorrelation=new double[2*PresenceProfiles.size()-1];
		profilesSequenceAutoCorrelation=autoCorrelation(profilesSequence,PresenceProfiles.size());
		if(T>Tmax | T>profilesSequence.length){
			if(Tmax<profilesSequence.length)
				T=Tmax;
			else
				T=profilesSequence.length;
		}
	

		

	}
	
	
	public void getPredictedProfile(){
		ProfilesPredictor p=new ProfilesPredictor(roomid,profilesSequence,T,databaseURL,databaseUserPassword,databaseUserName,databaseName,profilesNumber,profilingSystem);
		p.predictor();
		p.predictedProfileLoader();
	}
	
	public int[] getProfilesSequence(){
		return profilesSequence;
	}
	public int columnsSum(int[][] matrix,int dimension,int index){
		int sum=0;
		for(int j=0;j<dimension;j++)
			sum+=matrix[j][index];
		return sum;
	}
	
	public double crosscorrelationMeanValue(int[] x,int[] y,int areax, int areay,int length,int acceptedDelay){
		int[] correlation=new int[2*length-1]; 
	    int sum;

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
	    
	    int delay= (int)(acceptedDelay*60/samplingTime);
	    if(delay>length)
	    	delay=length-1;
	    if(delay==0)
	    	delay=1;
	    int count=0;
	    for(int j=length-delay;j<length+delay-2;j++){
	    	
	    	meanValue+=correlation[j];
	    	count+=1;
	    }
	    meanValue=(meanValue/count)/(areax+areay);
	    return meanValue;
	}
	
	
	
	public double[] autoCorrelation(int[] x,int length){
		double[] crosscorrelation=new double[2*length-1]; 
	    int sum;

	    for (int t=-(length-1);t<length;t++) {
	        sum=0;
	        
	        if(t<0){
		        for (int n=length-1;n>-t+1;n--) {
		            sum+=x[n]*x[n+t];
		        }
	        }
	        else {
	            for (int n=0;n<length-t;n++) {
	               sum+=x[n]*x[n+t];
	            }
	        }
	        crosscorrelation[t+length-1]=sum;
	    }
	    return crosscorrelation;
	}
	
	
	public void FinalProfileloading(ArrayList<doubleDailyProfile> finalPresenceProfiles,ArrayList<doubleDailyProfile> finalTemperatureProfiles,ArrayList<doubleDailyProfile> finalLightProfiles,int[] profilesSequence,int profilesNumber){
	    try{
            
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword );                                                                                              
            System.out.println("Connection to MySQL server to locally load the stored daily profiles");

            ResultSet rs;
            ResultSet rs1;
            ResultSet rs2;
            Statement stmt = null;
            	
            conn.setCatalog(databaseName); 
            stmt = conn.createStatement();
            
            for(int i=1;i<PresenceProfiles.size()+1;i++){
            	stmt.executeUpdate("UPDATE PresenceProfilesSequenceTable SET profileIdentifier="+profilesSequence[i-1]+" WHERE idroom="+roomid+" AND idday="+i+";");
            }
            
            int localProfilesNumber=0;
            rs=stmt.executeQuery("SELECT MAX(idprofile) FROM FinalPresenceProfiles WHERE idroom="+roomid+";");
            while(rs.next()) {
       	     localProfilesNumber=rs.getInt("MAX(idprofile)");
             }
            int localPosition=0;
            for (int i=1;i<profilesNumber+1;i++){
            	if(i<=localProfilesNumber){
            		for(int k=0;k<profileLength;k++){
            			localPosition=k+1;
            	     	stmt.executeUpdate("UPDATE FinalPresenceProfiles SET value="+finalPresenceProfiles.get(i-1).getDailyProfile()[k]+" WHERE idroom="+roomid+" AND idprofile="+i+" AND idposition="+localPosition+";");
            		}
            	}
            	else{
            		for(int k=0;k<profileLength;k++){
            			localPosition=k+1;
            	     	stmt.executeUpdate("INSERT INTO FinalPresenceProfiles SET value="+finalPresenceProfiles.get(i-1).getDailyProfile()[k]+",idroom="+roomid+",idprofile="+i+",idposition="+localPosition+";");
            		}
            	}
            }
            
            localProfilesNumber=0;
            rs1=stmt.executeQuery("SELECT MAX(idprofile) FROM FinalTemperatureProfiles WHERE idroom="+roomid+";");
            while(rs1.next()) {
       	     localProfilesNumber=rs1.getInt("MAX(idprofile)");
             }
            for (int i=1;i<profilesNumber+1;i++){
            	if(i<=localProfilesNumber){
            		for(int k=0;k<profileLength;k++){
            	     	stmt.executeUpdate("UPDATE FinalTemperatureProfiles SET value="+finalPresenceProfiles.get(i-1).getDailyProfile()[k]+" WHERE idroom="+roomid+" AND idprofile="+i+" AND idposition="+(k+1)+";");
            		}
            	}
            	else{
            		for(int k=0;k<profileLength;k++){
            	     	stmt.executeUpdate("INSERT INTO FinalTemperatureProfiles SET value="+finalPresenceProfiles.get(i-1).getDailyProfile()[k]+",idroom="+roomid+",idprofile="+i+",idposition="+(k+1)+";");
            		}
            	}
            }
            
            
            localProfilesNumber=0;
            rs2=stmt.executeQuery("SELECT MAX(idprofile) FROM FinalLightProfiles WHERE idroom="+roomid+";");
            while(rs2.next()) {
       	     localProfilesNumber=rs2.getInt("MAX(idprofile)");
             }
            for (int i=1;i<profilesNumber+1;i++){
            	if(i<=localProfilesNumber){
            		for(int k=0;k<profileLength;k++){
            	     	stmt.executeUpdate("UPDATE FinalLightProfiles SET value="+finalPresenceProfiles.get(i-1).getDailyProfile()[k]+" WHERE idroom="+roomid+" AND idprofile="+i+" AND idposition="+(k+1)+";");
            		}
            	}
            	else{
            		for(int k=0;k<profileLength;k++){
            	     	stmt.executeUpdate("INSERT INTO FinalLightProfiles SET value="+finalPresenceProfiles.get(i-1).getDailyProfile()[k]+",idroom="+roomid+",idprofile="+i+",idposition="+(k+1)+";");
            		}
            	}
            }
            
            stmt.close(); // close the object statement.
            conn.close(); // close the connection 
            System.out.println("Profiles locally stored!");
        }
        
        catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
	}
}