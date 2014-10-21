
package client_applications.traking;


import java.awt.Color;


public class ColorSeriesManager 
{
	private String gwName;
	private Color color;
	
	public ColorSeriesManager(String name, Color c)
	{
		this.gwName=name;
		this.color=c;
	}
	
	public String getGwName()
	{
		return this.gwName;
	}
	
	public Color getColor()
	{
		return this.color;
	}
}
