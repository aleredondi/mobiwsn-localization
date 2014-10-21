
package client_applications.parking.dijkstra;


import java.util.ArrayList;


public class DijkstraAlgorithm 
{
	private Graph graphObject;
	private double distances[];
	private int pred [];
	
	/**
	 * Constructor
	 * @param g the graph to analyze
	 */
	public DijkstraAlgorithm(Graph g)
	{
		this.graphObject=g;
	} 
	
	/**
	 * Method that return the length of a specific path
	 * @param to destination node id
	 * @return the length of the path
	 */
	public double getLenghtPath(int to)
	{
		double path=distances[to];		
		return path;
	}
	
	/**
	 * This method search all the minimum path from the node from to all the other nodes
	 * @param from the start node
	 */
	public void dijkstra (int from) 
    {
		int node;
		double dist;
		boolean [] visited = new boolean [this.graphObject.size()]; // all false initially
		this.distances= new double [this.graphObject.size()];  // shortest known distance from "s"
		this.pred= new int [this.graphObject.size()];  // preceeding node in path
       	int next;
       	ArrayList<Node> neighbours =null;

       //Initialize all the distance to infinitive
       for (int i=0; i<distances.length; i++) 
       {
          distances[i] = Double.MAX_VALUE;
       }
       
       distances[from] = 0;
 
       for (int i=0; i<distances.length; i++) 
       {
          next = minVertex (distances, visited);
          visited[next] = true;

          // The shortest path to next is dist[next] and trough pred[next].
          neighbours = this.graphObject.getNeighbors(next);
          for (Node n : neighbours) 
          {        	
        	  node = n.getId();
        	  dist = distances[next] + this.graphObject.getEdge(next, node).getWeight();
        	  if (distances[node] > dist) 
        	  {
        		  distances[node] = dist;
        		  pred[node] = next; 
        	  }
          } 
       }
    }
	
	
    private int minVertex (double [] dist, boolean [] visisted) 
    {
       double x = Double.MAX_VALUE;
       int y = -1;   // graph not connected, or no unvisited vertices
       for (int i=0; i<dist.length; i++) 
       {
          if (!visisted[i] && dist[i]<x) 
          {
        	  y=i; 
        	  x=dist[i];
          }
       }
       
       return y;
    }
 
    /**
     * This is the method trough which a client could obtain the minimum path for two specific nodes
     * @param s start node
     * @param e end node
     * @return the list of the nodes that belongs to the minimum path
     */
    public ArrayList<Node> getMinimumPath (int s, int e) 
    {
       ArrayList<Node> path = new ArrayList<Node>();       
       int x = e;
       while (x!=s) 
       {
          path.add (0, this.graphObject.getNode(x));
          x = pred[x];
       }
       path.add (0, this.graphObject.getNode(s));
       return path;
    }
 
}
