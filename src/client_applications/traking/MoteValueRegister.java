
package client_applications.traking;


import remote_interfaces.result.ValueResult;

import java.util.ArrayList;


public class MoteValueRegister 
{
	private String mote;
	private ArrayList<ValueResult> valueList;
	
	public MoteValueRegister(String id)
	{
		this.mote=id;
		this.valueList= new ArrayList<ValueResult>();
	}
	
	public void setMote(String m)
	{
		this.mote=m;
	}
	
	public String getMote()
	{
		return this.mote;
	}
		
	public void addValue(ValueResult val)
	{
		this.valueList.add(val);
	}
	
	public void addValueList(ArrayList<ValueResult> list)
	{
		this.valueList=list;
	}
	
	public ArrayList<ValueResult> getvalueList()
	{
		return this.valueList;
	}
}
