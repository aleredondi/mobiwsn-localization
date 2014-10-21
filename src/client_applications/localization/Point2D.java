package client_applications.localization;

public class Point2D {
	private double x;
	private double y;
	
	public Point2D(){
		x = 0;
		y = 0;
	}
	
	public Point2D(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public double distanceFrom(Point2D P){
		return Math.sqrt(Math.pow(x - P.getX(),2) + Math.pow(y-P.getY(),2)); 
	}
	
	public Point2D plus(Point2D P){
		return new Point2D(x+P.getX(),y+P.getY());
	}
	
	public Point2D minus(Point2D P){
		return new Point2D(x-P.getX(),y-P.getY());
	}
	
	public Point2D minus(double m){
		return new Point2D(x-m,y-m);
	}
	
	public Point2D mult(double s){
		return new Point2D(x*s,y*s);
	}
	
	public void print(){
		System.out.println("x: " +x +", y: " +y);
	}
}
