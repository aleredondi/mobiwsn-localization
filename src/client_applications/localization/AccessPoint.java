package client_applications.localization;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.measure.Measure;
import javax.measure.unit.SI;

import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;

import remote_interfaces.mote.Mote;

//GT DEMO CLASS

public class AccessPoint extends LauraNode{
	
	//variabili private
	private double x,y;
	private String id;
	
	private String mac_address;
	private String ip_address;
	
	private boolean is_reachable;
	private Color color = new Color(20);
	
	private Measure curr_temp_val = null;
	private ArrayList<Double> day_temp_val = new ArrayList<Double>();
	private ArrayList<Minute> day_time_val = new ArrayList<Minute>();
	private ArrayList<Double> quarter_temp_val = new ArrayList<Double>();
	private int quarter_counter = 0;
	private int msg_counter;
	
	QuarterChecker quarter_timer;
	Calendar cal = Calendar.getInstance();
	
	int current_day = cal.get(Calendar.DATE);
	
	public Measure getCurr_temp_val() {
		return curr_temp_val;
	}
	
	public String getInstantTemp() {
		
		if(curr_temp_val != null)
		{	
			DecimalFormat f = new DecimalFormat("#.##");
			
			Double value = (Double) curr_temp_val.getValue() - 273.15;
			
			return f.format(value) + " " + SI.CELSIUS.toString();			
		}
		
		return "NO TEMP";
	}

	public void setCurr_temp_val(Measure currTempVal) {
		curr_temp_val = currTempVal;
		quarter_temp_val.add((Double) curr_temp_val.getValue()- 273.15);
	}
	
	public AccessPoint(String id, double x, double y, boolean reachable, Mote mote_ref){
		this.id = id;
		this.x = x;
		this.y = y;
		this.mote_ref = mote_ref;
		setIs_reachable(reachable);
		quarter_timer = new QuarterChecker(300000);
		this.msg_counter = 2;
	}
	
	public AccessPoint(String id, String mac, String ip, double x, double y, boolean reachable){
		this.id = id;
		this.mac_address = mac;
		this.ip_address = ip;
		this.x = x;
		this.y = y;
		setIs_reachable(reachable);
		quarter_timer = new QuarterChecker(300000);
		this.msg_counter = 2;
	}
	

	
	public AccessPoint(String id, double x, double y, boolean reachable) {
		this.id = id;
		this.x = x;
		this.y = y;
		setIs_reachable(reachable);
		quarter_timer = new QuarterChecker(300000);
		this.msg_counter = 2;
	}

	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public Point2D getLocation(){
		return new Point2D(x,y);
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getIpAddress(){
		return this.ip_address;
	}
	
	public String getMACAddress(){
		return this.mac_address;
	}
	
	
	
	public int getMac(){
		int ind,ind2;
		ind = id.indexOf(".");
		String sub_id = id.substring(ind+1);
		ind2 = sub_id.indexOf(".");
		return Integer.parseInt(sub_id.substring(ind2+1));
	}
	
	public boolean isIs_reachable() {
		return is_reachable;
	}

	public void setIs_reachable(boolean isReachable) {
		is_reachable = isReachable;
		if(!is_reachable){
			this.setColor(Color.lightGray);
		}
		else{
			this.setColor(Color.black);
		}
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public final class QuarterChecker extends TimerTask{
		
		public QuarterChecker(long period){
			Timer timer = new Timer();
			timer.schedule(this, 0, period);
		}
		
		public void run() {
			Calendar cal2 = Calendar.getInstance();
			//controlla se è un nuovo giorno
			if(cal2.get(Calendar.DATE)>current_day){
				current_day = cal2.get(Calendar.DATE);
				//cancella i due array (ev. scrivi su file)
				day_time_val.clear();
				day_temp_val.clear();
			}
			
			//System.out.println("updating TEMP");
			double mean=0;
			
			//calcola la media dell'ultimo quarto d'ora
			for(int i=0;i<quarter_temp_val.size();i++){
				mean = mean +quarter_temp_val.get(i).doubleValue();
			}
			mean = mean/quarter_temp_val.size();
			
			//aggiunge la media nell'array del giorno (asse y)
			day_temp_val.add(mean);
			
			
			//aggiunge il timestamp (asse x) 
			day_time_val.add(new Minute(cal2.get(Calendar.MINUTE),cal2.get(Calendar.HOUR_OF_DAY),cal2.get(Calendar.DATE),cal2.get(Calendar.MONTH),cal2.get(Calendar.YEAR)));

			
			//cancella il buffer del quarto d'ora
			quarter_temp_val.clear();
		}
	}
	
	public ArrayList<Double> getDailyTemp(){
		return day_temp_val;
	}
	
	public ArrayList<Minute> getDailyTime(){
		return day_time_val;
	}
	
	public int getMsg_counter() {
		return msg_counter;
	}

	public void setMsg_counter(int msgCounter) {
		msg_counter = msgCounter;
	}
	

	
}
