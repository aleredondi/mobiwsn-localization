package client_applications.localization.graphics;

import java.awt.Color;
import java.awt.Dimension;
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
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.GradientPaintTransformer;
import org.jfree.ui.StandardGradientPaintTransformer;

import client_applications.localization.AccessPoint;
import client_applications.localization.SmallCellPowerModel;
import client_applications.localization.UserEquipment;

public class UeInfoPanel extends JFrame implements ActionListener,
ChangeListener, ComponentListener {

	private static final long serialVersionUID = 1L;

	private ArrayList<UserEquipment> ue_list;
	private LauraMainWindow lmw;
	private boolean is_attached;

	private JComboBox<String> comboUeSelection;
	private JTable tableUeData;
	private JFreeChart chart;
	DefaultTableModel modelUeData;
	private String selectedUeUri = null;




	String[][] rowData = { { "Device ID", "Test UE 1" },
			{ "Active APP", "Youtube" } };

	String[] columnNames = { "Key", "Value" };

	public UeInfoPanel(ArrayList<UserEquipment> ue_list, LauraMainWindow lmw) {
		this.ue_list = ue_list;
		this.lmw = lmw;

		this.setTitle("UE Info");
		this.createLayout();

		this.pack();
		this.setSize(280, 300);
	}

	/**
	 * Public update method. Should be called periodically or when something in
	 * the UE model changes.
	 */
	public void update() {
		this.updateComboUeSelection();
		this.updateTableUeData();
		this.updateChartUeThr();
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

	private void createLayout() {
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout((LayoutManager) new BoxLayout(infoPanel,
				BoxLayout.PAGE_AXIS));

		this.comboUeSelection = new JComboBox<String>();

		this.comboUeSelection.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				// get selected index to fetch UUID or URI from ue_list
				int idx = comboUeSelection.getSelectedIndex();
				if (idx < 0 || idx >= ue_list.size())
				{
					selectedUeUri = null;
					return;
				}
				selectedUeUri = ue_list.get(idx).getUri();
				updateTableUeData();
			}
		});

		this.tableUeData = new JTable();
		this.modelUeData = new DefaultTableModel(null, columnNames);
		this.tableUeData.setModel(this.modelUeData);
		infoPanel.add(this.comboUeSelection);
		infoPanel.add(new JScrollPane(this.tableUeData));
		infoPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));



		// THROUGHPUT CHART
		updateChartUeThr();
		chart = ChartFactory.createTimeSeriesChart("THR", "Time", "Throughput", null, false, false, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		//infoPanel.add(chartPanel);

		/*Calendar cal2 = Calendar.getInstance();
		Minute min = new Minute(cal2.get(Calendar.MINUTE),cal2.get(Calendar.HOUR_OF_DAY),cal2.get(Calendar.DATE),cal2.get(Calendar.MONTH),cal2.get(Calendar.YEAR));
		TimeSeries series = new TimeSeries("First");
		for(int i=0;i<10;i++){
			series.addOrUpdate(min,i);
		}*/
		//TimeSeriesCollection dataset = new TimeSeriesCollection();
		//dataset.addSeries(series);

		//Calendar cal = Calendar.getInstance();
		//int day = cal.get(Calendar.DATE);
		//int month = cal.get(Calendar.MONTH) + 1;
		//int year = cal.get(Calendar.YEAR);
		//String title = new String("Temperature of day: " + day + " - " + month + " - " + year);
		//JFreeChart chart = ChartFactory.createTimeSeriesChart("THR", "Time", "Throughput", dataset, false, false, false);

		//TextTitle ttitle = chart.getTitle(); 
		//ttitle.setFont(new Font("Serif",Font.PLAIN,14));
		//ChartPanel chartPanel = new ChartPanel(chart);
		//chartPanel.setPreferredSize(new Dimension(350,200));

		infoPanel.add(chartPanel);
		this.getContentPane().add(infoPanel);

	}

	private void updateComboUeSelection() {
		if (this.comboUeSelection == null || this.ue_list == null)
			return;

		// skip update if nothing has changed
		// a bit fuzzy in the case where one UE is removed and another one is
		// added,
		// but should be sufficient for a first demo
		if (this.comboUeSelection.getItemCount() == this.ue_list.size())
			return;

		this.comboUeSelection.removeAllItems();
		for (UserEquipment ue : this.ue_list) {
			this.comboUeSelection.addItem(ue.getDeviceId());
		}
	}

	private void updateChartUeThr(){
		TimeSeries series = new TimeSeries("First");
		UserEquipment ue = getUeByUri(this.selectedUeUri);
		if(ue!=null){
			for(int i=0;i<ue.getTimeWifiBpsArray().size();i++){
				series.add(ue.getTimeWifiBpsArray().get(i),ue.getSumtxrxWifiBpsArray().get(i));
			}
			TimeSeriesCollection thr_dataset = new TimeSeriesCollection();
			thr_dataset.addSeries(series);
			chart.getXYPlot().setDataset(thr_dataset);
		}
	}

	private void updateTableUeData() {
		System.out.println(selectedUeUri);

		// remove all rows from model
		this.modelUeData.setRowCount(0);

		// add new data to model
		UserEquipment ue = getUeByUri(this.selectedUeUri);
		if (ue != null)
		{
			// define rows
			String [][] data = {
					{"Device ID", ue.getDeviceId()},
					{"Location Service ID", ue.getLocationServiceId()},
					{"Display State", ue.getDisplayState() > 0 ? "on" : "off"},
					{"Active Application", ue.getActiveAppPackage()},
					{"Position X", String.valueOf(ue.getPositionX())},
					{"Position Y", String.valueOf(ue.getPositionY())},
					{"Wi-Fi Mac", ue.getWifiMac()},
					{"URI", ue.getUri()},
					{"Assigned AP", ue.getAssignedAccessPoint()},
					{"WiFi bytes/s", String.valueOf(ue.getRxWifiBps() + ue.getTxWifiBps())},
					{"Mobile byte/s", String.valueOf(ue.getRxMobileBps() + ue.getTxMobileBps())}
			};

			// add each row
			for(String [] row : data)
				this.modelUeData.addRow(row);
		}

		// send update notification to table view
		this.modelUeData.fireTableDataChanged();
	}

	private UserEquipment getUeByUri(String uri)
	{
		for(UserEquipment ue : ue_list)
		{
			if(ue.getUri().equals(uri))
				return ue;
		}
		return null;
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
		/*
		 * System.out.println("Starting PowerMeterPanel in standalone mode.");
		 * PowerMeterPanel pmp = new PowerMeterPanel(null, null); //
		 * pmp.setSize(400, 400); pmp.setVisible(true);
		 * pmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); pmp.pack();
		 */
	}

}
