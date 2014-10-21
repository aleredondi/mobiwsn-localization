package client_applications.home_virtualization_application;

public class HeatingManagementTime {
	private int BeginningHour;
	private int BeginningMinutes;
	private int StoppingHour;
	private int StoppingMinutes;
	int tempValue;
	
	public HeatingManagementTime(int a,int b,int c,int d,int temp){
		BeginningHour=a;
		BeginningMinutes=b;
		StoppingHour=c;
		StoppingMinutes=d;
		tempValue=temp;
	}
	
	public int getBeginningHour(){
		return BeginningHour;
	}
	
	public int getStoppingHour(){
		return StoppingHour;
	}
	public int getBeginningMinutes(){
		return BeginningMinutes;
	}
	
	public int getStoppingMinutes(){
		return StoppingMinutes;
	}
	
	public void setBeginningHour(int a){
		BeginningHour=a;
	}
	
	public void setBeginningMinutes(int b){
		BeginningMinutes=b;
	}
	
	public void setStoppingHour(int a){
		StoppingHour=a;
	}
	
	public void setStoppingMinutes(int b){
		StoppingMinutes=b;
	}
	
	public boolean isIncluded(int hour,int minutes){
		if(hour>BeginningHour || (hour==BeginningHour && minutes>=BeginningMinutes)){
			if(hour<StoppingHour ||(hour==StoppingHour && minutes<StoppingMinutes))
				return true;
			else 
				return false;
		}
		else
			return false;
	}
	
	public int getTemp(){
		return tempValue;
	}
	
	public void setTemp(int a){
		tempValue=a;
	}
	
}