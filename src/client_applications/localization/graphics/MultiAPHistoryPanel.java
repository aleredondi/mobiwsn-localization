package client_applications.localization.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.GradientPaintTransformer;
import org.jfree.ui.StandardGradientPaintTransformer;

import client_applications.localization.AccessPoint;
import client_applications.localization.SmallCellPowerModel;

public class MultiAPHistoryPanel extends JFrame implements ActionListener,
		ChangeListener, ComponentListener {

	private static final long serialVersionUID = 1L;
	private ArrayList<DefaultValueDataset> powerConsumptionDatasetPerAp;
	private ArrayList<AccessPoint> ap_list;
	private LauraMainWindow lmw;
	private boolean is_attached;
	private SmallCellPowerModel powerModel;
	private ArrayList<JFreeChart> charts = new ArrayList();

	public MultiAPHistoryPanel(ArrayList<AccessPoint> access_points_list,
			LauraMainWindow lmw) {
		this.ap_list = access_points_list;
		this.lmw = lmw;
		this.powerModel = new SmallCellPowerModel(access_points_list);

		this.setSize(100, 100);

		createLayout();

	}

	private void createLayout() {
		if (this.ap_list == null)
			return;

		int numberOfAps = this.ap_list.size();
		this.powerConsumptionDatasetPerAp = new ArrayList<DefaultValueDataset>();

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout((LayoutManager) new BoxLayout(layoutPanel,
				BoxLayout.LINE_AXIS));

		boolean first = true;
		for (AccessPoint ap : this.ap_list) {
			// create meter and add it panel
			//DefaultValueDataset ds = new DefaultValueDataset(23D);
			//this.powerConsumtionDatasetPerAp.add(ds);

			//JFreeChart jfreechart = setupDialChart(ap.getId(), "Watt (W)", ds,
			//		Math.min(0, powerModel.getMinPowerConsumptionOfAp(ap)),
			//		powerModel.getMaxPowerConsumptionOfAp(ap) * 1.1, 2D, 1);
			//ChartPanel chartpanel = new ChartPanel(jfreechart);
			TimeSeries series = new TimeSeries("First");
			for(int i=0;i<ap.getTimeWifiBpsArray().size();i++){
				series.add(ap.getTimeWifiBpsArray().get(i),ap.getSumtxrxWifiBpsArray().get(i));
			}
			TimeSeriesCollection thr_dataset = new TimeSeriesCollection();
			thr_dataset.addSeries(series);
			JFreeChart chart;
			if(first){
				chart = ChartFactory.createTimeSeriesChart(ap.getId(), "Time", "Throughput [Mbps]", null, false, false, false);
				first = false;
			}
			else{
				chart = ChartFactory.createTimeSeriesChart(ap.getId(), "Time", null, null, false, false, false);
			}
			
			chart.getXYPlot().setDataset(thr_dataset);
			chart.getXYPlot().getRangeAxis().setRange(0, 50);
			chart.getPlot().setBackgroundPaint(Color.WHITE);
			charts.add(chart);
			ChartPanel chartpanel = new ChartPanel(chart);
			layoutPanel.add(chartpanel);
		}
		this.getContentPane().add(layoutPanel);

		this.setTitle("Access Points Throughput");
		// set size according to number
		this.setSize(200 * numberOfAps, 240);
	}

	/**
	 * Chart setup. Meter visualization should only be changed on this method.
	 * 
	 * @param title
	 * @param inner_title
	 * @param valuedataset
	 * @param lower_bound
	 * @param upper_bound
	 * @param tick_increment
	 * @param minor_tick_count
	 * @return
	 */
	/*private JFreeChart setupDialChart(String title, String inner_title,
			ValueDataset valuedataset, double lower_bound, double upper_bound,
			double tick_increment, int minor_tick_count) {
		// general
		DialPlot dialplot = new DialPlot();
		dialplot.setDataset(valuedataset);
		dialplot.setDialFrame(new StandardDialFrame());
		dialplot.setBackground(new DialBackground());
		// description: Watt (W)
		DialTextAnnotation dialtextannotation = new DialTextAnnotation(
				inner_title);
		dialtextannotation.setFont(new Font("Dialog", 1, 14));
		dialtextannotation.setRadius(0.7D);
		dialplot.addLayer(dialtextannotation);
		// show numeric value
		DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
		dialvalueindicator.setOutlinePaint(Color.black);
		dialvalueindicator.setNumberFormat(new DecimalFormat("###,##0.00"));
		dialplot.addLayer(dialvalueindicator);
		// ticks
		StandardDialScale standarddialscale = new StandardDialScale(
				lower_bound, upper_bound, -120D, -300D, 2D, 4);
		standarddialscale.setMajorTickIncrement(tick_increment);
		standarddialscale.setMinorTickCount(minor_tick_count);
		standarddialscale.setTickRadius(0.88D);
		standarddialscale.setTickLabelOffset(0.15D);
		standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
		standarddialscale.setTickLabelPaint(Color.black);
		dialplot.addScale(0, standarddialscale);
		dialplot.addPointer(new org.jfree.chart.plot.dial.DialPointer.Pin());
		DialCap dialcap = new DialCap();
		dialplot.setCap(dialcap);
		// colored ranges
		StandardDialRange standarddialrange = new StandardDialRange(
				upper_bound * 0.8, upper_bound, Color.red);
		standarddialrange.setInnerRadius(0.50D);
		standarddialrange.setOuterRadius(0.53D);
		dialplot.addLayer(standarddialrange);
		StandardDialRange standarddialrange1 = new StandardDialRange(
				upper_bound * 0.6, upper_bound * 0.8, Color.orange);
		standarddialrange1.setInnerRadius(0.50D);
		standarddialrange1.setOuterRadius(0.53D);
		dialplot.addLayer(standarddialrange1);
		StandardDialRange standarddialrange2 = new StandardDialRange(
				lower_bound, upper_bound * 0.6, Color.green);
		standarddialrange2.setInnerRadius(0.50D);
		standarddialrange2.setOuterRadius(0.53D);
		dialplot.addLayer(standarddialrange2);
		// background
		GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(
				255, 255, 255), new Point(), new Color(170, 220, 170));
		DialBackground dialbackground = new DialBackground(gradientpaint);
		dialbackground
				.setGradientPaintTransformer((GradientPaintTransformer) new StandardGradientPaintTransformer(
						GradientPaintTransformType.VERTICAL));
		dialplot.setBackground(dialbackground);
		// custom pointer
		dialplot.removePointer(0);
		org.jfree.chart.plot.dial.DialPointer.Pointer pointer = new org.jfree.chart.plot.dial.DialPointer.Pointer();
		pointer.setFillPaint(Color.darkGray);
		dialplot.addPointer(pointer);
		// create chart
		return new JFreeChart(title, dialplot);
	}*/

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public void update() {
		int j =0;
		for (AccessPoint ap : ap_list) {
			/*DefaultValueDataset ds = this.powerConsumtionDatasetPerAp
					.get(ap_list.indexOf(ap));
			ds.setValue(powerModel.getCurrentPowerConsumptionOfAp(ap));*/
			TimeSeries series = new TimeSeries("First");
			for(int i=0;i<ap.getTimeWifiBpsArray().size();i++){
				series.add(ap.getTimeWifiBpsArray().get(i),ap.getSumtxrxWifiBpsArray().get(i));
			}
			TimeSeriesCollection thr_dataset = new TimeSeriesCollection();
			thr_dataset.addSeries(series);
			charts.get(j).getXYPlot().setDataset(thr_dataset);
			j++;
		}
	}

	public boolean isAttached() {
		return is_attached;
	}

	public void setAttached(boolean b) {
		is_attached = b;
		if (is_attached) {
			this.setLocation(lmw.getBounds().x,
					lmw.getBounds().y + lmw.getBounds().height);
		}
	}

}
