package client_applications.localization;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import javax.sound.sampled.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.applet.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.*;
import client_applications.localization.Event;
import client_applications.localization.graphics.EventPopUp;
public class EventManager  {
	
	//liste che tengono gli eventi
	private ArrayList<IntrusionEvent> intrusion_events; 
	private ArrayList<ProximityEvent> proximity_events;
	private ArrayList<MobileNode> mobile_list;
	EventChecker event_checker;
	
	
	public EventManager(ArrayList<MobileNode> mobile_list){
		intrusion_events = new ArrayList<IntrusionEvent>();
		proximity_events = new ArrayList<ProximityEvent>();
		this.mobile_list = mobile_list;
		
		//crea il task di controllo e avviso degli eventi
		event_checker = new EventChecker(1000);
		
	}
	
	//controllo se nella lista c'è già un evento così, altrimenti lo aggiungo
	public void addIntrusionEvent(String patient_id, String rule,Point2D location, BufferedImage bufferedImage){
		//se non c'erano eventi uguali lo lancio e lo notifico
		if(intrusion_events.size()==0){
			IntrusionEvent event = new IntrusionEvent(patient_id,rule,10,location,bufferedImage);
			intrusion_events.add(event);
			displayEvent("Intrusion!",event);

		}
		else{
			boolean found = false;
			for(int k=0; k<intrusion_events.size(); k++){
				//controllo che l'evento non sia già stato lanciato
				if(intrusion_events.get(k).isThrown(patient_id, rule)) found = true;
			}
			if(!found){
				IntrusionEvent event = new IntrusionEvent(patient_id,rule,10,location,bufferedImage);
				intrusion_events.add(event);
				displayEvent("Intrusion!",event);
			}
		}
	}
	
	//controllo se nella lista c'è già un evento così, altrimenti lo aggiungo
	public void addProximityEvent(String patient_id, String patient_enemy_id, Point2D patient_loc, Point2D enemy_loc,BufferedImage bufferedImage){
		//se non c'erano eventi lo lancio e lo notifico
		if(proximity_events.size()==0){
			ProximityEvent event = new ProximityEvent(patient_id,patient_enemy_id,10,patient_loc,enemy_loc,bufferedImage);
			proximity_events.add(event);
			displayEvent("Warning!",event);
		}
		else{
			boolean found = false;
			for(int l=0; l<proximity_events.size(); l++){
				//controllo che l'evento non sia già stato lanciato
				if(proximity_events.get(l).isThrown(patient_id, patient_enemy_id)) found = true;
			}
			if(!found){
				ProximityEvent event = new ProximityEvent(patient_id,patient_enemy_id,10,patient_loc,enemy_loc,bufferedImage);
				proximity_events.add(event);
				displayEvent("Warning!",event);
			}
		}
	}
	

	
	protected void displayEvent(String title,Event event){
	    EventPopUp event_popup = new EventPopUp(event,title,this);	
		event.setDisplayed(true);
		event_popup.setVisible(true);
		//System.out.println(event.toString());
	}
	
	public void remove(EventPopUp event_popup, Event event) {
		if(event instanceof IntrusionEvent){
			intrusion_events.remove(event);
			for(int i=0;i<mobile_list.size();i++){
				if(((IntrusionEvent)event).getPatient_id().equals(mobile_list.get(i).getPatientId())){
					mobile_list.get(i).setHasEvent(false);
					System.out.println("@vigilo@:setting false " + mobile_list.get(i).getPatientId());
				}
			}
		}
	    if(event instanceof ProximityEvent){
			proximity_events.remove(event);
			for(int i=0;i<mobile_list.size();i++){
				if(((ProximityEvent)event).getPatient_id().equals(mobile_list.get(i).getPatientId()) ||
				   ((ProximityEvent)event).getPatient_enemy_id().equals(mobile_list.get(i).getPatientId())	){
					mobile_list.get(i).setHasEvent(false);
					System.out.println("setting false " + mobile_list.get(i).getPatientId());
				}
			}
		}
		event_popup.setVisible(false);
		event.setDisplayed(false);
		//System.out.println(event.toString());
	}

	public void posticipate(EventPopUp event_popup, Event event) {
		event_popup.setVisible(false);
		event.setDisplayed(false);	
	}
	

	public final class EventChecker extends TimerTask{
		
		public EventChecker(long period){
			Timer timer = new Timer();
			timer.schedule(this, 1000, period);
		}
		
		public void run() {
			//checking intrusion events
			for(int i=0;i<intrusion_events.size();i++){
				System.out.println(intrusion_events.get(i).isDisplayed());
				if(intrusion_events.get(i).decTimeout()==0&& !intrusion_events.get(i).isDisplayed()){
					displayEvent("Intrusion!",intrusion_events.get(i));
					intrusion_events.get(i).setDisplayed(true);
				}
			}
			
			//checking proximity events
			for(int i=0;i<proximity_events.size();i++){
				if(proximity_events.get(i).decTimeout()==0 && !proximity_events.get(i).isDisplayed()){
					displayEvent("Warning!",proximity_events.get(i));
					proximity_events.get(i).setDisplayed(true);
				}
			}
		}
	}





	
}

