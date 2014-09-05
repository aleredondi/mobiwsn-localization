
package client_applications.traking;


import remote_interfaces.*;
import remote_interfaces.mote.Mote;
import remote_interfaces.result.ValueResult;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import client_applications.classi_remote.SensorTracking;


public class ChartManager 
{
	private JFreeChart lChart;
	private XYLineAndShapeRenderer renderer;
	private Color []colorList;
	private boolean onlineOn, isOnline;
	private MoteValueRegister mvrSelected;
	private Mote moteSelected;
	private WSNGatewayManager remoteObject;
	private ArrayList<MoteValueRegister> moteList;
	private SensorTracking st;
	private ArrayList<TimeSeries> listGateway;
	private int index;
	private ArrayList<ColorSeriesManager>gwVisited;
	public ChartManager(WSNGatewayManager remote)
	{
		Color list[]={Color.blue, Color.orange, Color.green, Color.darkGray, Color.pink, Color.yellow, Color.cyan, Color.black, Color.magenta, Color.white};
		colorList=list;	
		this.onlineOn=isOnline=false;
		this.remoteObject=remote;
		this.moteList= new ArrayList<MoteValueRegister>();
		st= new SensorTracking();
		
	}
	
	
	/**
	 * Select mote to track
	 * @param moteSearched id of the mote 
	 */
	public void addMoteToList(String mote)
	{
		MoteValueRegister mvr=new MoteValueRegister(mote);
		moteList.add(mvr);
	}
	
	
	/**
	 * Set the threshold of a single or of a list of motes
	 * @param temp value of the threshold
	 * @param moteSearched  mote to set threshold, if it is null the application set the threshold to all the mote of the list
	 * @throws RemoteException 
	 */
	public void setThreshold(Double temp, String moteSearched) throws RemoteException
	{
		Class typeIn[]={Double.class};
		Object paramIn[]={temp};
	
		if(moteSearched==null)
		{
			for(MoteValueRegister mvr : this.moteList)
			{
				this.getMoteObject(mvr.getMote()).useMethod(st.getClassIdentificationParameter(), "setThreshold", typeIn, paramIn);
			}
		}
		else
		{
			this.getMoteObject(moteSearched).useMethod(st.getClassIdentificationParameter(), "setThreshold", typeIn, paramIn);
			
		}
	}
	
	
	public void connect(String url)
	{
		Class typeIn[]={String.class};
		Object paramIn[]={url};
	
		for(MoteValueRegister mvr: this.moteList)
		{	
			try 
			{
				this.getMoteObject(mvr.getMote()).useMethod(st.getClassIdentificationParameter(), "connect", typeIn, paramIn);
			} 
			catch (RemoteException e) {e.printStackTrace();}
		}
			
	}
	
	
	public void disconnect()
	{
		Class typeIn[]={};
		Object paramIn[]={};
	
		for(MoteValueRegister mvr: this.moteList)
		{	
			try 
			{
				Mote m=this.getMoteObject(mvr.getMote());
				if (m!=null)
					this.getMoteObject(mvr.getMote()).useMethod(st.getClassIdentificationParameter(), "disconnect", typeIn, paramIn);
			} 
			catch (RemoteException e) {e.printStackTrace();}
		}
	}
	
	/**
	 * this method create the chart with the list of value of a specific selected mote
	 * @param moteId 
	 * @return the chart
	 */
	public JFreeChart createChart(String moteId)
	{
		String type;
				
		if(onlineOn)
			type="Online";
		else
			type="Offline";
		
		lChart = ChartFactory.createTimeSeriesChart("Temperature Read Values "+type+"  ("+moteId+")","Date","Values (°C)",null,true,true,false);
		XYPlot lXYPlot = (XYPlot)lChart.getPlot();
		lXYPlot.setBackgroundPaint( Color.lightGray );
		//lXYPlot.setBackgroundPaint( Color.white );
		
		lXYPlot.setDomainGridlinePaint( Color.white );
		lXYPlot.setRangeGridlinePaint( Color.white );
		//lXYPlot.setDomainGridlinePaint( Color.lightGray );
		//lXYPlot.setRangeGridlinePaint( Color.lightGray );
		
		lXYPlot.setAxisOffset( new RectangleInsets( 15.0, 15.0, 15.0, 15.0 ) );
		lXYPlot.setDomainCrosshairVisible( true );
		lXYPlot.setRangeCrosshairVisible( true );
	
		XYItemRenderer lXYItemRenderer = lXYPlot.getRenderer();
		lChart.setBorderVisible(true);
		lChart.setBorderStroke(new BasicStroke(3f));
	
		if ( lXYItemRenderer instanceof XYLineAndShapeRenderer )
		{
			renderer =(XYLineAndShapeRenderer)lXYItemRenderer;
			renderer.setBaseShapesVisible( true );
			renderer.setBaseShapesFilled( true );
			renderer.setStroke(new BasicStroke(2f));
			renderer.setShape(new Ellipse2D.Double(-3,-3,6,6));
			
		}
		
		
		this.index=0;
		this.listGateway=new ArrayList<TimeSeries>();
		
		for(int i=0;i<10;i++)
		{
			this.listGateway.add(new TimeSeries("",Second.class));
			renderer.setSeriesVisibleInLegend(i, false);
			
		}
		
		renderer.setSeriesShapesVisible(0,false);
		gwVisited=new ArrayList<ColorSeriesManager>();
		
		
		try 
		{
			this.searchMote(moteId);
			lXYPlot.setDataset(this.setTimeSeries());
		} 
		catch (RemoteException e) {e.printStackTrace();}
		
		return this.lChart;
		
	}
	
	public void activeOnline()
	{
		this.onlineOn=true;
	}
	
	public void deactiveOnline() throws RemoteException
	{
		this.onlineOn=this.isOnline=false;
		
		for(MoteValueRegister mvr : this.moteList)
		{
			Class typeInOn[]={boolean.class};
			Object paramInOn[]={false};
			
			this.getMoteObject(mvr.getMote()).useMethod(st.getClassIdentificationParameter(), "setOnline", typeInOn, paramInOn);
				
			
		}
					            
	}
	
	private XYDataset setTimeSeries() throws RemoteException
	{
		
		TimeSeries ss=null;
		String actualGw=null;	
		Calendar date, predate;
		Double readValue;
		
		ValueResult preVal=null;
		Class typeIn[]={};
		Object paramIn[]={};
		predate= new GregorianCalendar();
		predate.set(1900,1,1,10,0,0);
		boolean preNull=true;
		Double threshold=(Double)this.moteSelected.useMethod(st.getClassIdentificationParameter(), "getThreshold", typeIn, paramIn);
		
		for(ValueResult value : this.mvrSelected.getvalueList())
		{
			String parent=value.getParent();
			boolean gwSeriesExist=false;
			date=(Calendar)value.getTimeRead();
			
			if(!preNull)
			{
				predate=(Calendar)preVal.getTimeRead();
				date=(Calendar)value.getTimeRead();
			}
			
			if(date.get(date.SECOND)==predate.get(date.SECOND) && date.get(date.MINUTE)==predate.get(date.MINUTE) && date.get(date.HOUR_OF_DAY)==predate.get(date.HOUR_OF_DAY) && date.get(date.DAY_OF_MONTH)==predate.get(date.DAY_OF_MONTH) && date.get(date.YEAR)==predate.get(date.YEAR))
			{
				System.out.println("value already registered");
			}
			else//valore corretto
			{	
				
				if(parent.equals(actualGw))//caso in cui il valore lettto ha lo stesso gateway del valore letto precedentemente
				{
					readValue=(Double)value.getValue().getValue();
					this.listGateway.get(this.index).add( new Second(date.get(date.SECOND),date.get(date.MINUTE),date.get(date.HOUR_OF_DAY),date.get(date.DAY_OF_MONTH),date.get(date.MONTH),date.get(date.YEAR)), readValue);
					gwSeriesExist=true;
				}
				else//valore del gateway differente
				{
					actualGw=parent;
					for(int i=0; i<gwVisited.size();i++)//verifico che il gateway del valore non sia già stato letto in valori precedenti
					{
						ColorSeriesManager gw=gwVisited.get(i);
						if(parent.equals(gw.getGwName())) //se esiste già una serie con quel nome
						{
							index++;
							this.listGateway.get(index).setKey(parent);
							readValue=(Double)value.getValue().getValue();
							this.listGateway.get(index).add( new Second(date.get(date.SECOND),date.get(date.MINUTE),date.get(date.HOUR_OF_DAY),date.get(date.DAY_OF_MONTH),date.get(date.MONTH),date.get(date.YEAR)), readValue);
							
							renderer.setSeriesPaint(index, gwVisited.get(i).getColor());
							gwSeriesExist=true;
							
						}
					}
				}
				
				if (!gwSeriesExist)//se il gateway del valore non è mai stato letto prima aggingo nuova serie
				{
					index++;
					gwVisited.add(new ColorSeriesManager(parent, this.colorList[index-1]));
					this.listGateway.get(index).setKey(parent);
					
					try 
					{
						readValue=(Double)value.getValue().getValue();
						this.listGateway.get(index).add( new Second(date.get(date.SECOND),date.get(date.MINUTE),date.get(date.HOUR_OF_DAY),date.get(date.DAY_OF_MONTH),date.get(date.MONTH),date.get(date.YEAR)), readValue);
						renderer.setSeriesPaint(index, gwVisited.get(index-1).getColor());
						renderer.setSeriesVisibleInLegend(index, true);
						
					}	 	
					catch (RemoteException e) {e.printStackTrace();}
					
					
				}
					
			}
			
			preVal=value;
			preNull=false;	
		}
			
		
		if(threshold<1000)
		{
			tresholdSeries(threshold);
		}
		
		
		TimeSeriesCollection lDataset = new TimeSeriesCollection();
		for(TimeSeries  obj: this.listGateway)
		{	
			lDataset.addSeries( obj );
		}
		
		return lDataset;
	
	}
	
	private void tresholdSeries(Double threshold) throws RemoteException
	{
		Calendar date;
		this.listGateway.get(0).setKey("Treshold");
		date=(Calendar)this.mvrSelected.getvalueList().get(0).getTimeRead();
		this.listGateway.get(0).addOrUpdate( new Second(date.get(date.SECOND),date.get(date.MINUTE),date.get(date.HOUR_OF_DAY),date.get(date.DAY_OF_MONTH),date.get(date.MONTH),date.get(date.YEAR)), threshold);
		date=(Calendar)this.mvrSelected.getvalueList().get(this.mvrSelected.getvalueList().size()-1).getTimeRead();
		this.listGateway.get(0).addOrUpdate( new Second(date.get(date.SECOND),date.get(date.MINUTE),date.get(date.HOUR_OF_DAY),date.get(date.DAY_OF_MONTH),date.get(date.MONTH),date.get(date.YEAR)), threshold);	
		renderer.setSeriesPaint(this.mvrSelected.getvalueList().size()-1, Color.red);
			
		renderer.setSeriesVisibleInLegend(0, true);
		
		
	}
	
	private void searchMote(String moteId) throws RemoteException 
	{
		Class typeIn[]={};
		Object paramIn[]={};
		
		for(MoteValueRegister mvr: this.moteList)
		{
			if(mvr.getMote().equals(moteId))
			{
				this.mvrSelected=mvr;
				 Mote newMote=this.getMoteObject(this.mvrSelected.getMote());
				 
				 if(newMote!=null)
					 this.moteSelected=newMote;

				if(!this.isOnline)
				{
					if(!this.onlineOn)
					{
						mvr.addValueList((ArrayList<ValueResult>)this.moteSelected.useMethod(st.getClassIdentificationParameter(), "getvalueRead", typeIn, paramIn));					
						break;
					}
					else
					{
						Class typeInOn[]={boolean.class};
						Object paramInOn[]={true};
						mvr.addValueList((ArrayList<ValueResult>)this.moteSelected.useMethod(st.getClassIdentificationParameter(), "getvalueRead", typeIn, paramIn));		
						this.moteSelected.useMethod(st.getClassIdentificationParameter(), "setOnline", typeInOn, paramInOn);
						break;
					}
				}
			}
		}		
	}
	
	public void addValue(ValueResult val)
	{
		mvrSelected.getvalueList().add(val);
		Class typeIn[]={};
		Object paramIn[]={};
		Double threshold=null;
		try 
		{
			threshold=(Double)this.getMoteObject(this.moteSelected.getId()).useMethod(st.getClassIdentificationParameter(), "getThreshold", typeIn, paramIn);
			if(threshold<1000)
			{
				tresholdSeries(threshold);
			}
		} 
		catch (RemoteException e1) {e1.printStackTrace();}
		
		Double readValue=null;
		Calendar date=null;
		String parent="";
		boolean gwSeriesExist=false;
		try 
		{
			readValue=(Double)val.getValue().getValue();
			date=(Calendar)val.getTimeRead();
			parent = val.getParent();
		} 
		catch (RemoteException e) {e.printStackTrace();}
		
		if(this.listGateway.get(index).getKey().toString().equals(parent))
		{
			this.listGateway.get(index).add( new Second(date.get(date.SECOND),date.get(date.MINUTE),date.get(date.HOUR_OF_DAY),date.get(date.DAY_OF_MONTH),date.get(date.MONTH),date.get(date.YEAR)), readValue);
			
		}
		else
		{	
			for(int i=0; i<gwVisited.size();i++)//verifico che il gateway del valore non sia già stato letto in valori precedenti
			{
				ColorSeriesManager gw=gwVisited.get(i);
				if(parent.equals(gw.getGwName())) //se esiste già una serie con quel nome
				{
					index++;
					this.listGateway.get(index).setKey(parent);
					this.listGateway.get(index).add( new Second(date.get(date.SECOND),date.get(date.MINUTE),date.get(date.HOUR_OF_DAY),date.get(date.DAY_OF_MONTH),date.get(date.MONTH),date.get(date.YEAR)), readValue);
					
					renderer.setSeriesPaint(index, gwVisited.get(i).getColor());
					renderer.setSeriesVisibleInLegend(index, false);
					gwSeriesExist=true;
				}
			}
			
			if (!gwSeriesExist)//se il gateway del valore non è mai stato letto prima aggingo nuova serie
			{
				index++;
				gwVisited.add(new ColorSeriesManager(parent, this.colorList[index-1]));
				this.listGateway.get(index).setKey(parent);
				this.listGateway.get(index).add( new Second(date.get(date.SECOND),date.get(date.MINUTE),date.get(date.HOUR_OF_DAY),date.get(date.DAY_OF_MONTH),date.get(date.MONTH),date.get(date.YEAR)), readValue);
				renderer.setSeriesPaint(index, gwVisited.get(index-1).getColor());
				renderer.setSeriesVisibleInLegend(index, true);								
			}
			
		}
	}
	
	public Mote getMoteObject(String id) throws RemoteException
	{
		Mote searched=null;
		for(WSNGateway gw : remoteObject.getWSNGatewayList())
		{
			for(Mote m : gw.getMoteList())
			{
				if(m.getId().equals(id) && m.isReachable())
				{
					searched=m;
				}
					
			}
		}
		
		return searched;
	}
}
