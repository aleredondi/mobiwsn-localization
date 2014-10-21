package client_applications.localization;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class IntrusionEvent extends Event {
	


	private String patient_id;
	private String rule;
	private int timeout;
	private int timeout_check;
	private Point2D event_location;

	public Point2D getEvent_location() {
		return event_location;
	}

	public void setEvent_location(Point2D eventLocation) {
		event_location = eventLocation;
	}

	public IntrusionEvent(String patient_id, String rule, int timeout, Point2D location,BufferedImage bufferedImage){
		this.patient_id = patient_id;
		this.rule = rule;
		this.timeout_check = timeout;
		this.timeout = this.timeout_check;
		this.event_location = location;
	    map_zoom = bufferedImage;
	    
		setDisplayed(false);
	}
	
	public boolean isThrown(String patient_id, String rule){
		if(patient_id.equals(this.patient_id) && rule.equals(this.rule))
			return true;
		else return false;
	}
	

	
	
	public int decTimeout(){
		timeout--;
		//reset timeout
		if(timeout==0){
			timeout = timeout_check;
			return 0;
		}
		else
		return timeout;		
	}
	
	public String getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(String patientId) {
		patient_id = patientId;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	
	
	
	
	
	

}
