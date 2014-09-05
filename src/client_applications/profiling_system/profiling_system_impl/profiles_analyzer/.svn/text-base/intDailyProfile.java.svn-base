
package client_applications.profiling_system.profiling_system_impl.profiles_analyzer;


/***
* @author Antimo Barbato
*
*/

public class intDailyProfile {
	
	int profileLength;;
	int[] profile;
	private int profileIdentifier;
	private int area;
	
	public intDailyProfile(int[] InputProfile,int profileLength){
		profile=new int[profileLength];

		for(int i=0;i<profileLength;i++){
			profile[i]=InputProfile[i];
			}
	}
	
	public void setProfileIdentifier(int profileid){
		this.profileIdentifier=profileid;
	}
	
	public int getProfileIdentifier(){
		return this.profileIdentifier;
	}

	public int[] getDailyProfile(){
			return this. profile;
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