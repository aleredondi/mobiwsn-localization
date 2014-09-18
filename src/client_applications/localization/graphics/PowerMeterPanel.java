package client_applications.localization.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.GradientPaintTransformer;
import org.jfree.ui.StandardGradientPaintTransformer;

import client_applications.localization.AccessPoint;

public class PowerMeterPanel extends JFrame implements ActionListener,
		ChangeListener, ComponentListener {

	private static final long serialVersionUID = 1L;
	private DefaultValueDataset powerConsumtionDataset;
	private ArrayList<AccessPoint> ap_list;
	private LauraMainWindow lmw;
	private boolean is_attached;

	public PowerMeterPanel(ArrayList<AccessPoint> access_points_list,
			LauraMainWindow lmw) {
		this.ap_list = access_points_list;
		this.lmw = lmw;

		// create char panel and add it to frame
		JPanel cp = setupChartPanel();
		setContentPane(cp);
		// set fixed size
		this.setSize(280, 300);
	}

	public void update() {
		// TODO: implement real power consumption colculation
		this.updateMeterValue(Math.random() * 99);
		System.out.println("Power Meter update.");
	}

	private void updateMeterValue(double val) {
		this.powerConsumtionDataset.setValue(val);
	}

	public boolean isAttached() {
		return is_attached;
	}

	public void setAttached(boolean b) {
		is_attached = b;
		if (is_attached) {
			// this.setLocationRelativeTo(lmw);
			// this.setLocation(lmw.getBounds().x +
			// lmw.getWidth(),lmw.getBounds().y);
			this.setLocation(lmw.getBounds().x - this.getWidth(),
					lmw.getBounds().y);
		}
	}

	private JFreeChart setupDialChart(String title, String inner_title,
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
		dialplot.addLayer(dialvalueindicator);
		// ticks
		StandardDialScale standarddialscale = new StandardDialScale(
				lower_bound, upper_bound, -120D, -300D, 10D, 4);
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
				upper_bound - 10D, upper_bound, Color.red);
		standarddialrange.setInnerRadius(0.50D);
		standarddialrange.setOuterRadius(0.53D);
		dialplot.addLayer(standarddialrange);
		StandardDialRange standarddialrange1 = new StandardDialRange(
				upper_bound - 20D, upper_bound - 10D, Color.orange);
		standarddialrange1.setInnerRadius(0.50D);
		standarddialrange1.setOuterRadius(0.53D);
		dialplot.addLayer(standarddialrange1);
		StandardDialRange standarddialrange2 = new StandardDialRange(
				lower_bound, upper_bound - 20D, Color.green);
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
	}

	private JPanel setupChartPanel() {
		// TODO: Calculate power consumption bounds in relation to the number of
		// available APs
		powerConsumtionDataset = new DefaultValueDataset(23D);
		JFreeChart jfreechart = setupDialChart("Small Cell Power Consumption",
				"Watt (W)", powerConsumtionDataset, 0D, 100D, 10D, 4);
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		return chartpanel;
	}

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

	// main: only for debugging
	public static void main(String[] arg) {
		System.out.println("Starting PowerMeterPanel in standalone mode.");
		PowerMeterPanel pmp = new PowerMeterPanel(null, null);
		// pmp.setSize(400, 400);
		pmp.setVisible(true);
		pmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pmp.pack();
	}

}
