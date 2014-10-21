package client_applications.profiling_system.profiling_system_impl.profiles_analyzer;


/***
* @author Antimo Barbato
*
*/


public class doubleDailyProfile {
	
	private int profileLength;
	private int profileIdentifier;
	private double[] profile;
	private int area;
	
	public doubleDailyProfile(double[] InputProfile,int profileLength){

		profile=new double[profileLength];
		for(int i=0;i<profileLength;i++){
			profile[i]=InputProfile[i];
			}
	}

		public double[] getDailyProfile(){
			return this. profile;
		}

		public void setProfileIdentifier(int profileid){
			this.profileIdentifier=profileid;
		}
		
		public int getProfileIdentifier(){
			return this.profileIdentifier;
		}
		
		public int getProfileArea(){
			
			area=0;
			for(int k=0;k<profileLength;k++)
				area +=profile[k];
			if(area==0)
				area=1;
			return area;
			
		}
}