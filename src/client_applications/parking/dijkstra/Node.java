
package client_applications.parking.dijkstra;


/**
 * Class that represent a node of a graph
 * @author Alessandro Laurucci
 *
 */
public class Node 
{
	private int id; //Number of the node
	
	//Coordinates of the node
	private int coordX; 
	private int coordY;
	
	/**
	 * Constructor
	 * @param num id of the node
	 * @param x coordinate x of the node
	 * @param y coordinate y of the node
	 */
	public Node(int num, int x, int y)
	{
		this.id=num;
		this.coordX=x;
		this.coordY=y;
	}
	
	/**
	 * 
	 * @return the id of the node
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * 
	 * @return the coordinate x of the node
	 */
	public int getX()
	{
		return this.coordX;
	}
	
	/**
	 * 
	 * @return the coordinate y of the node
	 */
	public int getY()
	{
		return this.coordY;
	}
	
}
