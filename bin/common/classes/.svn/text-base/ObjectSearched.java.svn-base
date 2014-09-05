
package common.classes;


import java.io.Serializable;
import java.util.ArrayList;


public class ObjectSearched implements Serializable
{
	private String className;
	private String author;
	private int version;
	private ArrayList<String> parameters;
	
	public ObjectSearched(String name, String aut, int val)
	{
		this.className=name;
		this.author=aut;
		this.version=val;
		this.parameters=new ArrayList<String>();
		this.parameters.add(this.className);
		this.parameters.add(this.author);
		
	}
	
	public String getName() 
	{
		return this.className;
	}
	
	public String getAuthor() 
	{
		return this.author;
	}
	
	public int getVersion() 
	{
		return this.version;
	}
	
	public ArrayList<String> getClassIdentificationParameter()
	{
		return this.parameters;
	}
	
}