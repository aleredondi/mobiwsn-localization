
package client_applications.parking.dijkstra;


import java.util.ArrayList;


/**
 * Class used for define a Graph
 * @author Alessandro Laurucci
 *
 */
public class Graph 
{
	public ArrayList<Edge>  edges; //list of the edges
    private ArrayList<Node> nodes; //list of the nodes

    /**
     * Constructor
     */
    public Graph () 
    {
        edges  = new ArrayList<Edge>();
        nodes = new ArrayList<Node>();
    }
    
    /**
     * 
     * @return the number of nodes of the graph
     */
    public int size() 
    { 
    	return nodes.size(); 
    }
    
    /**
     * Add a node to the graph
     * @param n node to add
     */
    public void addNode (Node n) 
    { 
    	nodes.add(n); 
    }
    
    
    /**
     * Add an edge(bidirectional) to the graph
     * @param e edge to add
     */
    public void addEdge (Edge e) 
    { 
    	Edge inverse = new Edge(e.getTo(), e.getFrom(), e.getWeight());
    	edges.add(e);
    	edges.add(inverse);
    	
    	
    }
    
    /**
     * Add an arch to the graph
     * @param e arch to add
     */
    public void addArch (Edge e) 
    { 
    	edges.add(e); 
    }
    
    /**
     * Method for requesting a node
     * @param id number of the node
     * @return the node requested
     */
    public Node getNode (int id)               
    { 
    	Node result=null;
    	for(Node n : nodes)
    	{
    		if (n.getId()==id)
    		{
    			result=n;
    			break;
    		}
    	}
    	return result;
    }
    
    /**
     * Method for requesting a node
     * @param x coordinate x of the node
     * @param y coordinate y of the node
     * @return the node requested
     */
    public Node getNode (int x, int y)               
    { 
    	Node result=null;
    	for(Node n : nodes)
    	{
    		if (n.getX()==x && n.getY()==y)
    		{
    			result=n;
    			break;
    		}
    	}
    	return result;
    }
    
    
    /**
     * Method for requesting a specific edge
     * @param from the first node of the edge
     * @param to the second node of the edge
     * @return the edge requested
     */
    public Edge getEdge (int from, int to)               
    { 
    	Edge result=null;
    	for(Edge e : edges)
    	{
    		if (e.getFrom().getId()==from && e.getTo().getId()==to)
    		{
    			result=e;
    			break;
    		}
    	}
    	return result;
    }
    
    
    /**
     * Method used to obtain the list of the nodes linked to a specific node
     * @param id number of the node for which we are searching neighbours
     * @return the list of nodes
     */
    public ArrayList<Node> getNeighbors (int id) 
    {
    	ArrayList<Node> result= new ArrayList<Node>();
    	boolean found;
    	
    	//scan of the edge defined in the list
    	for(Edge e : this.edges)
    	{
    		if(e.getFrom().getId()==id) //if the edge has the first id vertex like that searched 
    		{	
    			found=false;
    			for(Node n : result) //verify if the node is already added on the list
    	    	{
    	    		if (n.getId()==e.getTo().getId())
    	    		{	
    	    			found=true;
    	    			break;
    	    		}
    	    	} 
    			
    			if (found==false) //add the node to the list if the it was not already added 
    				result.add(e.getTo());
    		}
    	}
    	
        return result;
     }
    
 
    
}
