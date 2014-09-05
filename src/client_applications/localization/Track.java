package client_applications.localization;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Track {

	ArrayList<Line2D> segments = new ArrayList<Line2D>();
	
	public void addSegment(Line2D segment){
		segments.add(segment);
		System.out.println("Aggiunto Segmento");
	}
	
	public Line2D getSegment(int i){
		return segments.get(i);
	}
	
	public int getNofSegments(){
		return segments.size();
	}
	
}
