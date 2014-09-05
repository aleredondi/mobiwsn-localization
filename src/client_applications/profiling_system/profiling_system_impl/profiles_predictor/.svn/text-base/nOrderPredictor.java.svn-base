
package client_applications.profiling_system.profiling_system_impl.profiles_predictor;


/***
* @author Antimo Barbato
*
*/

public class nOrderPredictor {
	private int[] profilesSequence;
	private int order;
	private int profilesNumber;
	private double acceptedReliability;
	private double reliability=0;
	private int predictedProfile;
	private int[] lastDays;
	
	public nOrderPredictor(int[] ProfilesSequence,int order,int profilesNumber,double acceptedReliability){
		this.profilesSequence=new int[ProfilesSequence.length];
		this.profilesSequence=ProfilesSequence;
		this.order=order;
		this.profilesNumber=profilesNumber;
		this.acceptedReliability=acceptedReliability;
	}
	
	public boolean PredicteProfile(){
		lastDays=new int[order];
		int index=0;
		boolean found=false;
		
		for(int k=profilesSequence.length-order;k<profilesSequence.length;k++){
			lastDays[index]=profilesSequence[k];
			index +=1;
		}
		
		
		int counter=0;
		double[] profilesFound=new double[profilesNumber];
		for(int t=0;t<profilesSequence.length-order;t++){
			boolean a=true;
			for(int i=0;i<lastDays.length;i++){
				if(lastDays[i]!=profilesSequence[t+i])
					a=a&false;
			}
			if(a){
				counter+=1;
				profilesFound[profilesSequence[t+order]-1] +=1;
			}
		}
		
		if(counter==0)
			counter=1;
		for(int j=0;j<profilesNumber;j++)
			profilesFound[j]=profilesFound[j]/(counter);
		
		for(int j=0;j<profilesNumber;j++){
			if(profilesFound[j]>=reliability){
				reliability=profilesFound[j];
				predictedProfile=j+1;
			}
		}
		
		if(reliability>acceptedReliability)
			found=true;
		
		return found;
	}
	
	public int getPredictedProfile(){
		return this.predictedProfile;
	}
	
	public double getReliability(){
		return this.reliability;
	}
	
}