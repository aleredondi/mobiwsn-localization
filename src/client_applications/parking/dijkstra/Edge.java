
package client_applications.parking.dijkstra;


public class Edge 
{
	private Node from; //first vertex
	private Node to; //second vertex
	private double weight; //weight of the edge
	
	/**
	 * Constructor
	 * @param a first vertex of the arch
	 * @param b second vertex of the arch
	 */
	public Edge(Node a, Node b)
	{
		this.from=a;
		this.to=b;
		
		//Pitagora Theorem
		double basex= Double.valueOf(""+(a.getX()-b.getX()));
		double basey= Double.valueOf(""+(a.getY()-b.getY()));
		this.weight=Math.sqrt(Math.pow(basex,2.0)+Math.pow(basey,2.0));
		
	}
	
	/**
	 * Constructor specifying the weight of the edge
	 * @param a first vertex of the arch
	 * @param b second vertex of the arch
	 * @param w weight of the edge
	 */
	public Edge(Node a, Node b, double w)
	{
		this.from=a;
		this.to=b;
		this.weight=w;
	
	}
	
	/**
	 * 
	 * @return the first vertex
	 */
	public Node getFrom()
	{
		return this.from;
	}
	
	/**
	 * 
	 * @return the second vertex
	 */
	public Node getTo()
	{
		return this.to;
	}
	
	/**
	 * 
	 * @return the weight
	 */
	public double getWeight()
	{
		return this.weight;
	}
}
