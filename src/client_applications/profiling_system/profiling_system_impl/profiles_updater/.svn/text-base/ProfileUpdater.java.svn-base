package client_applications.profiling_system.profiling_system_impl.profiles_updater;

import java.sql.*;
import java.util.ArrayList;

import client_applications.profiling_system.profiling_system_impl.*;



public class ProfileUpdater{
	
	
	   private String databaseName;
	    private String databaseUserName;
	    private String databaseUserPassword;
	    private String databaseURL;
	    private int profilesNumber;
	    private int profileLength;
	    private int samplingTime;
	    private ArrayList<newRoomLocalDB> roomsDB=new ArrayList<newRoomLocalDB>();
		private String profileName1="presence profile";
		private ProfilingSystemImpl profilingSystem;
	 
	    
	    
	    
		public ProfileUpdater(String dbName,String dbURL,String dbUserName,String dbUserPassword, int samplingTime,ProfilingSystemImpl p){
			
			this.databaseName=dbName;
			this.databaseUserName=dbUserName;
			this.databaseUserPassword=dbUserPassword;
			this.databaseURL=dbURL;
			this.samplingTime=samplingTime;
			this.profilingSystem=p;
		}
		
		public void recordingLocalDB(String roomname,int[] currentProfile,int[] profilesSequence){
		   roomsDB.clear(); 
			try{
	            
	            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
	            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword );                                                                                              
	            System.out.println("Connection to MySQL server to read the stored final presence profiles");

	            ResultSet rs;
	            ResultSet rs1;
	            ResultSet rs2;
	            Statement stmt = null;
	            	
	            conn.setCatalog(databaseName); 
	            stmt = conn.createStatement();
	                

	            rs=stmt.executeQuery("Select idroom FROM rooms WHERE roomname='"+roomname+"';");
	            while(rs.next()) {
	                	int id=rs.getInt("idroom");
	                	int idtype=0;
	                	int predictedProfile=0;
	                	
	    	            rs2=stmt.executeQuery("SELECT idtype FROM profileType WHERE profilename='"+profileName1+"';");
	    	            
	    	            while(rs2.next())
	    	            	idtype=rs2.getInt("idtype");
	    	            
	    	            
	                	rs1=stmt.executeQuery("SELECT idprofile FROM PredictedProfiles WHERE idroom="+id+" AND idtype="+idtype+";");
	                	
	                	while(rs1.next())
	                	predictedProfile=rs1.getInt("idprofile");
	                	
	                	newRoomLocalDB roomDB=new newRoomLocalDB(id,databaseName,databaseURL,databaseUserName,databaseUserPassword,profileLength,samplingTime,predictedProfile,currentProfile,profilesSequence,profilingSystem);
	                    roomsDB.add(roomDB);
	                	roomDB.updateProfiles();

	            }

	            stmt.close(); // close the object statement.
	            conn.close(); // close the connection 
	        }

	        catch (Exception ex) {
	            System.out.println("SQLException: " + ex.getMessage());
	        }
		}
	
		
		public void setProfileLength(int length){
			this.profileLength=length;
		}
		
		public void roomUpdating(){
			int[] profSeq1;
			int[] profSeq2;
			int[] potProf2;
			
			for(newRoomLocalDB r1:roomsDB){
				
				if(r1.profileChanged){
					for(newRoomLocalDB r2:roomsDB){
						
						int counter=0;
						if(!r2.profileChanged){
							
							profSeq1=r1.profilesSequence;
							profSeq2=r2.profilesSequence;
							potProf2=r2.potentialProfiles;
							double[] probabilities=new double[potProf2.length];
							for(int i=0;i<profSeq1.length;i++){
								
								if((profSeq1[i]==r1.lastPredictedProfile)&(potProf2[profSeq2[i]-1]!=0)){
									counter=counter+1;
									probabilities[profSeq2[i]-1]=probabilities[profSeq2[i]-1]+1;
								}
							}
							for(int j=0;j<probabilities.length;j++){
								probabilities[j]=probabilities[j]/counter;
							}
							for(int j=0;j<probabilities.length;j++){
								if(probabilities[j]>0.7){
									roomsDB.get(j).profileChanged=true;
									roomsDB.get(j).loadNewProfile();

								}
							}
						
						}
					}
				}
			}
		}

		
	}
