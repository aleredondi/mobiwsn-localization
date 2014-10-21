
package common.classes;


import java.util.ArrayList;


public abstract class ClassMoteStructure extends Thread
{
	public String pathSave="";
	
	public void setPath(String dir)
	{
		this.pathSave=dir;
		
	}
	public String getPath()
	{
		return this.pathSave;
		
	}
	
	public abstract ArrayList<String> getClassIdentificationParameter();
	public abstract int getVersion();
	public abstract void startFunction(String url, String oldgateway, String newgateway, String id);	
	
}
