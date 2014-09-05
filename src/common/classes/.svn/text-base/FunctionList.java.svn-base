
package common.classes;


import java.io.Serializable;
import java.util.ArrayList;


/**
 * This class is used as an object that contain a list of functions information, in this project is used for example for associating
 * a group of function object to a specific mote group
 * @author Alessandro Laurucci
 *
 */
public class FunctionList implements Serializable
{
	private ArrayList<ObjectSearched> list; //list of the Object that has information about functions
	
	/**
	 * Constructor method through which is set directly the list of the functions 
	 * @param functions list of the function object 
	 */
	public FunctionList(ArrayList<ObjectSearched> functions)
	{
		this.list=new ArrayList<ObjectSearched>();
		this.addObject(functions);
	}
	
	/**
	 * Classsic Constructor method
	 */
	public FunctionList()
	{
		this.list=new ArrayList<ObjectSearched>();
	}
	
	/**
	 * Method used to add new object on the list
	 * @param functions list of function information to add at the list
	 */
	public void addObject(ArrayList<ObjectSearched> functions)
	{
		this.list.addAll(functions);
	}
	
	/**
	 * Method used to get the list of the function associated with this object
	 * @return the arraylist with the information of the functions
	 */
	public ArrayList<ObjectSearched> getList()
	{
		return this.list;
	}
	
}