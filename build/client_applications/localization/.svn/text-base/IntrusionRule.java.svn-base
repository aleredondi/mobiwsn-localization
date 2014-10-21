package client_applications.localization;

public class IntrusionRule {
	private Point2D t_l, b_r, t_r, b_l;
	private String name;
	private boolean selected;
	

	public IntrusionRule(Point2D t_l, Point2D b_r, String name){
		this.t_l = t_l;
		this.b_r = b_r;
		this.t_r = new Point2D(b_r.getX(), t_l.getY());
		this.b_l = new Point2D(t_l.getX(), b_r.getY());
		this.name = name;
		this.setSelected(false);
	}
	
	public void setTopLeft(Point2D t_l){
		this.t_l = t_l;
	}
	
	public void setBottomLeft(Point2D b_l){
		this.b_l = b_l;
	}
	
	public void setTopRight(Point2D t_r){
		this.t_r = t_r;
	}
	
	public void setBottomRight(Point2D b_r){
		this.b_r = b_r;
	}
	
	public Point2D getTopLeft(){
		return t_l;
	}
	
	public Point2D getTopRight(){
		return t_r;
	}
	
	public Point2D getBottomLeft(){
		return b_l;
	}
	
	public Point2D getBottomRight(){
		return b_r;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean checkForIntrusion(Point2D target){
		if(target.getX()>=t_l.getX() && target.getX()<=b_r.getX() && 
		   target.getY()>=t_l.getY() && target.getY()<=b_r.getY()) 
			return true;
		else return false;
	}
}
