
package client_applications.traking;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;


public class TimeSeriesExample extends JFrame
{
	private static XYLineAndShapeRenderer renderer;

	
	public TimeSeriesExample( String pStrTitle ) {
		   super( pStrTitle );
		   this.setSize(1024, 800);
		   ChartPanel lChartPanel = (ChartPanel)createDemoPanel();
		   lChartPanel.setPreferredSize( new java.awt.Dimension( 500, 270 ) );
		   lChartPanel.setMouseZoomable( true, false );
		   setContentPane( lChartPanel );
		   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		}
	
	public static ChartPanel createDemoPanel() {
		
		JFreeChart lChart = createChart();
		   return new ChartPanel( lChart );
		}
	
	private static XYDataset createDataset() {
		   
		TimeSeriesCollection lDataset = new TimeSeriesCollection();
		
		for(int i=1;i<20;i++)
		{
			TimeSeries lTimeSeries1 = new TimeSeries( "MokaByte Index"+i, Month.class );
		   lTimeSeries1.add( new Month( 1, 2005 ), 175.8 + i*2);
		   lTimeSeries1.add( new Month( 2, 2005), 167.3 + i*2);
		   lTimeSeries1.add( new Month( 3, 2005 ), 153.8 + i*2);
		   lTimeSeries1.add( new Month( 4, 2005 ), 167.6 + i*2);
		   lTimeSeries1.add( new Month( 5, 2005 ), 158.8 + i*2);
		   lTimeSeries1.add( new Month( 6, 2005 ), 148.3 + i*2);
		   lTimeSeries1.add( new Month( 7, 2005), 153.9+ i*2 );
		   lTimeSeries1.add( new Month( 8, 2005 ), 142.7 + i*2);
		   lTimeSeries1.add( new Month( 9, 2005 ), 123.2 + i*2);
		   lTimeSeries1.add( new Month( 10, 2005 ), 131.8 + i*2);
		   lTimeSeries1.add( new Month( 11, 2005 ), 139.6 + i*2);
		   lTimeSeries1.add( new Month( 12, 2005 ), 142.9 + i*2);
		   lTimeSeries1.add( new Month( 1, 2006), 138.7+ i*2 );
		   lTimeSeries1.add( new Month( 2, 2006 ), 137.3 + i*2);
		   lTimeSeries1.add( new Month( 3, 2006 ), 143.9 + i*2);
		   lTimeSeries1.add( new Month( 4, 2006 ), 139.8 + i*2);
		   
		  
		  /*
		   TimeSeries lTimeSeries2 = new TimeSeries( "MokaCode Index2", Month.class );
		   lTimeSeries2.add( new Month( 1, 2005 ), 129.6 );
		   lTimeSeries2.add( new Month( 2, 2005), 123.2 );
		   lTimeSeries2.add( new Month( 3, 2005 ), 117.2 );
		   lTimeSeries2.add( new Month( 4, 2005 ), 124.1 );
		   lTimeSeries2.add( new Month( 5, 2005 ), 122.6 );
		   lTimeSeries2.add( new Month( 6, 2005 ), 119.2 );
		   lTimeSeries2.add( new Month( 7, 2005 ), 116.5 );
		   lTimeSeries2.add( new Month( 8, 2005 ), 112.7 );
		   lTimeSeries2.add( new Month( 9, 2005 ), 101.5 );
		   lTimeSeries2.add( new Month( 10, 2005 ), 106.1 );
		   lTimeSeries2.add( new Month( 11, 2005 ), 110.3 );
		   lTimeSeries2.add( new Month( 12, 2005 ), 111.7 );
		   lTimeSeries2.add( new Month( 1, 2006 ), 111.0 );
		   lTimeSeries2.add( new Month( 2, 2006 ), 109.6 );
		   lTimeSeries2.add( new Month( 3, 2006 ), 113.2 );
		   lTimeSeries2.add( new Month( 4, 2006 ), 111.6 );
		   
		   TimeSeries lTimeSeries3 = new TimeSeries( "MokaCode Index3", Month.class );
		   lTimeSeries3.add( new Month( 1, 2005 ), 130.6 );
		   lTimeSeries3.add( new Month( 2, 2005), 133.2 );
		   lTimeSeries3.add( new Month( 3, 2005 ), 167.2 );
		   lTimeSeries3.add( new Month( 4, 2005 ), 114.1 );
		   lTimeSeries3.add( new Month( 5, 2005 ), 162.6 );
		   lTimeSeries3.add( new Month( 6, 2005 ), 109.2 );
		   lTimeSeries3.add( new Month( 7, 2005 ), 116.5 );
		   lTimeSeries3.add( new Month( 8, 2005 ), 110.7 );
		   lTimeSeries3.add( new Month( 9, 2005 ), 100.5 );
		   lTimeSeries3.add( new Month( 10, 2005 ), 110.1 );
		   lTimeSeries3.add( new Month( 11, 2005 ), 107.3 );
		   lTimeSeries3.add( new Month( 12, 2005 ), 114.7 );
		   lTimeSeries3.add( new Month( 1, 2006 ), 121.0 );
		   lTimeSeries3.add( new Month( 2, 2006 ), 122.6 );
		   lTimeSeries3.add( new Month( 3, 2006 ), 127.2 );
		   lTimeSeries3.add( new Month( 4, 2006 ), 130.6 );
		   
		   TimeSeries lTimeSeries4 = new TimeSeries( "MokaCode Index4", Month.class );
		   lTimeSeries4.add( new Month( 1, 2005 ), 111.6 );
		   lTimeSeries4.add( new Month( 2, 2005), 134.2 );
		   lTimeSeries4.add( new Month( 3, 2005 ), 127.2 );
		   lTimeSeries4.add( new Month( 4, 2005 ), 154.1 );
		   lTimeSeries4.add( new Month( 5, 2005 ), 112.6 );
		   lTimeSeries4.add( new Month( 6, 2005 ), 129.2 );
		   lTimeSeries4.add( new Month( 7, 2005 ), 106.5 );
		   lTimeSeries4.add( new Month( 8, 2005 ), 115.7 );
		   lTimeSeries4.add( new Month( 9, 2005 ), 105.5 );
		   lTimeSeries4.add( new Month( 10, 2005 ), 111.1 );
		   lTimeSeries4.add( new Month( 11, 2005 ), 109.3 );
		   lTimeSeries4.add( new Month( 12, 2005 ), 119.7 );
		   lTimeSeries4.add( new Month( 1, 2006 ), 123.0 );
		   lTimeSeries4.add( new Month( 2, 2006 ), 120.6 );
		   lTimeSeries4.add( new Month( 3, 2006 ), 120.2 );
		   lTimeSeries4.add( new Month( 4, 2006 ), 135.6 );
		   
		   TimeSeries lTimeSeries5 = new TimeSeries( "MokaCode Index4", Month.class );
		   lTimeSeries5.add( new Month( 1, 2005 ), 133.6 );
		   lTimeSeries5.add( new Month( 2, 2005), 144.2 );
		   lTimeSeries5.add( new Month( 3, 2005 ), 120.2 );
		   lTimeSeries5.add( new Month( 4, 2005 ), 167.1 );
		   lTimeSeries5.add( new Month( 5, 2005 ), 99.6 );
		   		 
		   TimeSeries lTimeSeries6 = new TimeSeries( "MokaCode Index4", Month.class );
		   lTimeSeries6.add( new Month( 1, 2005 ), 123.6 );
		   lTimeSeries6.add( new Month( 2, 2005), 44.2 );
		   lTimeSeries6.add( new Month( 3, 2005 ), 12.2 );
		   lTimeSeries6.add( new Month( 4, 2005 ), 67.1 );
		   lTimeSeries6.add( new Month( 5, 2005 ), 89.6 );

		   TimeSeries lTimeSeries7 = new TimeSeries( "MokaCode Index4", Month.class );
		   lTimeSeries7.add( new Month( 1, 2005 ), 33.6 );
		   lTimeSeries7.add( new Month( 2, 2005), 147.2 );
		   lTimeSeries7.add( new Month( 3, 2005 ), 140.2 );
		   lTimeSeries7.add( new Month( 4, 2005 ), 169.1 );
		   lTimeSeries7.add( new Month( 5, 2005 ), 119.6 );
		   
		   TimeSeries lTimeSeries8 = new TimeSeries( "MokaCode Index4", Month.class );
		   lTimeSeries8.add( new Month( 1, 2005 ), 135.6 );
		   lTimeSeries8.add( new Month( 2, 2005), 121.2 );
		   lTimeSeries8.add( new Month( 3, 2005 ), 190.2 );
		   lTimeSeries8.add( new Month( 4, 2005 ), 67.1 );
		   lTimeSeries8.add( new Month( 5, 2005 ), 172.6 );
		  */ 
		 //  TimeSeriesCollection lDataset = new TimeSeriesCollection();
		   lDataset.addSeries( lTimeSeries1 );
		   /*lDataset.addSeries( lTimeSeries2 );
		   lDataset.addSeries( lTimeSeries3 );
		   lDataset.addSeries( lTimeSeries4 );
		   lDataset.addSeries( lTimeSeries5 );
		   lDataset.addSeries( lTimeSeries6 );
		   lDataset.addSeries( lTimeSeries7 );
		   lDataset.addSeries( lTimeSeries8 );
		  if(i==1)
			   renderer.setSeriesPaint(i-1, Color.BLACK);
		   else if(i==2)
			   renderer.setSeriesPaint(i-1, Color.GREEN);
		   else if(i==3)
		   {   
			   renderer.setSeriesPaint(i-1, Color.black);
			   renderer.setSeriesVisibleInLegend(i-1, false);
		   }*/
		   
		}
		   return lDataset;
		}
	
	

	// Creazione dell'oggetto Chart tramite il ChartFactory.
	// I parametri in ingresso al metodo createTimeSeriesChart sono:
	// titolo
	// label per l'asse delle ascisse
	// label per l'asse delle ordinate
	// Dataset con i dati con cui popolare il grafico
	// boolean che indica se creare la legenda oppure no
	// boolean che indica se creare i tooltip oppure no
	// boolean che indica se creare dei collegamenti a punti del grafico
	public static JFreeChart createChart()
	{
	JFreeChart lChart = ChartFactory.createTimeSeriesChart(
	            "Mokabyte Time Series Example",
	            "Date",
	            "Valori",
	            null,
	            true,
	            true,
	            false
	            );

	
	XYPlot lXYPlot = (XYPlot)lChart.getPlot();
	
	
	lXYPlot.setBackgroundPaint( Color.lightGray );
	lXYPlot.setDomainGridlinePaint( Color.white );
	lXYPlot.setRangeGridlinePaint( Color.white );
	lXYPlot.setAxisOffset( new RectangleInsets( 5.0, 5.0, 5.0, 5.0 ) );
	lXYPlot.setDomainCrosshairVisible( true );
	lXYPlot.setRangeCrosshairVisible( true );
	
	XYItemRenderer lXYItemRenderer = lXYPlot.getRenderer();
	lChart.setBorderVisible(true);
	lChart.setBorderStroke(new BasicStroke(10f));
	
	if ( lXYItemRenderer instanceof XYLineAndShapeRenderer )
	{
	    renderer =(XYLineAndShapeRenderer)lXYItemRenderer;
	   renderer.setBaseShapesVisible( true );
	   renderer.setBaseShapesFilled( true );
	  /* renderer.setSeriesPaint(0, Color.BLACK);
	   renderer.setSeriesPaint(2, Color.BLACK);
	   */renderer.setStroke(new BasicStroke(7f));
	  
	   renderer.setShape(new Ellipse2D.Double(-3,-3,6,6)); 
	   
	}
	XYDataset dt= createDataset();
	lXYPlot.setDataset(dt);
	
	return lChart;
	}
	
	public static void main(String []args)
	{
		
		TimeSeriesExample cc= new TimeSeriesExample("grafico");
		cc.setVisible(true);
		
	}
}
