package client_applications.localization;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ProximityEvent extends Event {


	String patient_id;
	String patient_enemy_id;
	private int timeout;
	private int timeout_check;
	Point2D patient_loc;
	Point2D enemy_loc;
	
	




	
	public ProximityEvent(String patient_id, String patient_enemy_id, int timeout,Point2D patient_loc, Point2D enemy_loc, BufferedImage bufferedImage){
		System.out.println("creo l'evento");
		this.patient_enemy_id = patient_enemy_id;
		this.patient_id = patient_id;
		this.timeout_check = timeout;
		this.timeout = this.timeout_check;
		map_zoom = bufferedImage;
		this.patient_loc = patient_loc;
		this.enemy_loc = enemy_loc;
	}
	
	public boolean isThrown(String patient_id, String patient_enemy_id){
		if(patient_id.equals(this.patient_id) && patient_enemy_id.equals(this.patient_enemy_id)
		   || patient_id.equals(this.patient_enemy_id) && patient_enemy_id.equals(this.patient_id)) return true;
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

	public String getPatient_enemy_id() {
		return patient_enemy_id;
	}

	public void setPatient_enemy_id(String patientEnemyId) {
		patient_enemy_id = patientEnemyId;
	}
	
	public Point2D getPatient_loc() {
		return patient_loc;
	}

	public void setPatient_loc(Point2D patientLoc) {
		patient_loc = patientLoc;
	}

	public Point2D getEnemy_loc() {
		return enemy_loc;
	}

	public void setEnemy_loc(Point2D enemyLoc) {
		enemy_loc = enemyLoc;
	}

}
