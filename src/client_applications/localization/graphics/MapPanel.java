package client_applications.localization.graphics;



import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import client_applications.localization.*;


/////////////////////////////////////////////////// class MouseGraphicsComponent
class MapPanel extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener {
	public int PPM = 25; //40; //laura = 25 //rfid 37
	public static final int WIDTH = 35;/*22;*/ //28;// x laura
	public static final int HEIGHT = 25;/*10;*/ //17;//  x laura

	public static final int X_OFFSET = 0;//40;  
	public static final int Y_OFFSET = 0;//40;
	private final int NUM_WALLS = 57; 
	//laura 57
	//ale 43
	//3piano 56
	//rfid 53
	//rfid2 18

	private ArrayList<AnchorNode> anchor_list;
	private ArrayList<MobileNode> mobile_list;
	//GT DEMO
	private ArrayList<AccessPoint> access_points_list;
	//

	private boolean drawingRule, drawingAnchors, drawingAccessPoints, positioningAnchors, positioningAccessPoints;
	private boolean drawingNames = true;
	private boolean drawingTrack = false;
	private boolean showingRule = false;
	private boolean showingWalls = false;
	private boolean showingTrack = false;
	private boolean segment_ready = false;
	private boolean locked_to_track = false;
	private Image rule_texture;
	private BufferedImage map_image;
	private Track track;




	private EventManager event_manager; 

	private int t_sX = 0;
	private int t_sY = 0;
	private int t_sX2 = 0;
	private int t_sY2 = 0;
	private int t_lX = 0;
	private int t_lY = 0;
	private int b_rX = 0;
	private int b_rY = 0;

	private int m_x = 0;
	private int m_y = 0;

	LauraMainWindow lmw;
	Point2D[][] walls = new Point2D[2][NUM_WALLS];
	//ScreenRefresher srfr = new ScreenRefresher(250,this);

	private Popup popup;
	boolean popup_showed;


	public MapPanel(ArrayList<AnchorNode> anchor_list, ArrayList<MobileNode> mobile_list, ArrayList<AccessPoint> access_points_list, LauraMainWindow lmw) {


		//this.setPreferredSize(new Dimension(WIDTH*PPM, HEIGHT*PPM));
		this.setPreferredSize(new Dimension(830, 580));
		//3piano 830, 580
		//rfid 860, 710
		//rfid2 800, 650
		this.addMouseWheelListener(this);
		this.addMouseMotionListener(this); 
		this.addMouseListener(this);



		this.anchor_list = anchor_list;
		this.mobile_list = mobile_list;
		//GT DEMO
		this.access_points_list = access_points_list;
		//
		
		event_manager = new EventManager(mobile_list);
		this.lmw = lmw;
		this.buildWalls();

		//carica l'immagine per le regole
		rule_texture = null;
		map_image = null;
		try {
			String filename = "/client_applications/localization/graphics/warning.png";
			URL url = getClass().getResource(filename);
			//System.out.println(url);
			rule_texture = ImageIO.read(url);
			filename = "/client_applications/localization/graphics/laura-def.jpg";
			url = getClass().getResource(filename);
			map_image = ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	//================================================== override paintComponent
	@Override public void paintComponent(Graphics g) {
		//... Paint background.
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		buildAntlab(g);

		g.setColor(Color.black);
		//posizione del mouse
		if(positioningAnchors){
			String str = "X: " + ((double)(m_x - X_OFFSET)/PPM) + ", Y: " + ((double)(m_y - Y_OFFSET)/PPM);
			g.drawString(str, m_x, m_y);
		}


		//disegna le regole del nodo selezionato
		if(lmw.selected_node!=null &&
				this.isShowingRule() && 
				lmw.selected_node instanceof MobileNode){
			float alpha;
			MobileNode node = (MobileNode)lmw.selected_node;

			for(int i=0; i<node.getIntrusionRules().size();i++){			

				IntrusionRule rule = node.getIntrusionRules().get(i);


				if(rule.isSelected())
					alpha = 1f;
				else
					alpha = 0.7f;
				drawRule(g,
						(int)(X_OFFSET + (double)rule.getTopLeft().getX()*PPM),
						(int)(Y_OFFSET + (double)rule.getTopLeft().getY()*PPM),
						(int)(X_OFFSET + (double)rule.getBottomRight().getX()*PPM),
						(int)(Y_OFFSET + (double)rule.getBottomRight().getY()*PPM),alpha);
			}
		}

		// if(drawingRule){
		//	 drawRule(g, t_lX, t_lY, b_rX, b_rY,1);
		// }

		//build antlab
		g.setColor(Color.BLACK);


		g.setColor(Color.RED);
		if(showingTrack && track!=null){
			//System.out.println(track.getNofSegments());
			for(int i=0;i<track.getNofSegments();i++){	
				g.drawLine((int)(track.getSegment(i).getX1()*PPM), (int)(track.getSegment(i).getY1()*PPM), (int)(track.getSegment(i).getX2()*PPM), (int)(track.getSegment(i).getY2()*PPM));
				System.out.println("Segmento  " +i  + ":"  +(int)(track.getSegment(i).getX1()*PPM) + " " + (int)(track.getSegment(i).getY1()*PPM) + " " + (int)(track.getSegment(i).getX2()*PPM) + " " + (int)(track.getSegment(i).getY2()*PPM));
			}

			//linea corrente;
			if(drawingTrack){
				g.drawLine(t_sX, t_sY, t_sX2, t_sY2);
			}

		}


		if(drawingRule){
			drawRule(g, t_lX, t_lY, b_rX, b_rY,1);
		}

		//draw anchors se attivo
		if(this.drawingAnchors){ 
			for(int i=0; i<anchor_list.size();i++){
				int dim=7;
				if(anchor_list.get(i)==lmw.selected_node){
					dim = 12;
				}			 
				g.setColor(anchor_list.get(i).getColor());
				g.fillOval(X_OFFSET + (int)Math.round(anchor_list.get(i).getX()*PPM) - dim/2, Y_OFFSET + (int)Math.round(anchor_list.get(i).getY()*PPM) -dim/2, dim, dim);
				g.drawString(Integer.toString(anchor_list.get(i).getMac()),X_OFFSET + (int)Math.round(anchor_list.get(i).getX()*PPM), Y_OFFSET + (int)Math.round(anchor_list.get(i).getY()*PPM));
			}
		}

		//GT DEMO
		//draw access points if enabled
		if(this.drawingAccessPoints){
			for(int i=0; i<access_points_list.size();i++){
				int dim = 7;
				if(access_points_list.get(i)==lmw.selected_node){
					dim = 12;
				}
				g.setColor(access_points_list.get(i).getColor());
				g.fillOval(X_OFFSET + (int)Math.round(access_points_list.get(i).getX()*PPM) - dim/2, Y_OFFSET + (int)Math.round(access_points_list.get(i).getY()*PPM) -dim/2, dim, dim);
				g.drawString(Integer.toString(access_points_list.get(i).getMac()),X_OFFSET + (int)Math.round(access_points_list.get(i).getX()*PPM), Y_OFFSET + (int)Math.round(access_points_list.get(i).getY()*PPM));
			}
		}
		//

		//draw mobiles (emphasize selected)

		for(int i=0; i<mobile_list.size();i++){
			double x_draw = mobile_list.get(i).getX_draw();
			double y_draw = mobile_list.get(i).getY_draw();
			if(mobile_list.get(i)==lmw.selected_node){
				g.setColor(mobile_list.get(i).getColor());
				g.fillOval(X_OFFSET + (int)Math.round(x_draw*PPM) - 7, Y_OFFSET + (int)Math.round(y_draw*PPM) - 7, 15, 15);	 

				//INSERISCI L'ANIMAZIONE PER LA SELEZIONE QUA

				if(this.drawingNames){
					g.setFont(new Font("Helvetica", Font.BOLD,  14));
					g.setColor(Color.black);
					g.drawString(mobile_list.get(i).getPatientId(), 
							X_OFFSET + (int)Math.round(x_draw*PPM),
							Y_OFFSET + (int)Math.round(y_draw*PPM));
				}
			}
			else{

				g.setColor(mobile_list.get(i).getColor());
				g.fillOval(X_OFFSET + (int)Math.round(x_draw*PPM) - 5, Y_OFFSET + (int)Math.round(y_draw*PPM) - 5, 10, 10);
				g.setColor(Color.white);
				g.fillOval(X_OFFSET + (int)Math.round(x_draw*PPM) - 3, Y_OFFSET + (int)Math.round(y_draw*PPM) - 3, 6, 6);
				if(this.drawingNames){
					g.setFont(new Font("Helvetica", Font.PLAIN,  12));
					g.setColor(Color.black);
					g.drawString(mobile_list.get(i).getPatientId(), 
							X_OFFSET + (int)Math.round(x_draw*PPM),
							Y_OFFSET + (int)Math.round(y_draw*PPM));
				}
			}

			//		 if(mobile_list.get(i).HasEvent()){
			//			 g.setColor(Color.red);
			//			 g.drawOval(X_OFFSET + (int)Math.round(x_draw*PPM) - 10, Y_OFFSET + (int)Math.round(y_draw*PPM) - 10, 20, 20);
			//		 }

		}

		//disegna raw check se attivo!
		g.setColor(Color.darkGray);
		for(int i=0;i <mobile_list.size();i++){
			if(mobile_list.get(i).getParam().isRaw_on()){
				g.fillOval(X_OFFSET + (int)Math.round(mobile_list.get(i).getXRaw()*PPM), Y_OFFSET + (int)Math.round(mobile_list.get(i).getYRaw()*PPM), 10, 10);
				g.drawString(mobile_list.get(i).getPatientId(), 
						X_OFFSET + (int)Math.round(mobile_list.get(i).getXRaw()*PPM),
						Y_OFFSET + (int)Math.round(mobile_list.get(i).getYRaw()*PPM));

				//draw particles
				Point2D[] particles = mobile_list.get(i).getParticles();
				for(int j=0;j<150;j++){

					double px_draw = particles[j].getX();
					double py_draw = particles[j].getY();
					g.setColor(Color.gray);
					g.fillOval(X_OFFSET + (int)Math.round(px_draw*PPM) - 1, Y_OFFSET + (int)Math.round(py_draw*PPM) - 1, 2, 2);
				}


			}
		}
	}


	public void mouseMoved(MouseEvent e) {
		if(positioningAnchors){
			m_x = e.getX();
			m_y = e.getY();
			this.repaint();
		}

		if(drawingTrack){
			t_sX2 = e.getX();
			t_sY2 = e.getY();
			this.repaint();
		}
	}


	public void mouseDragged(MouseEvent e) {
		if(drawingRule){
			b_rX = e.getX();
			b_rY = e.getY();
			this.repaint();
		}
	}


	public void buildWall(Graphics g, double x1, double y1, double x2, double y2){
		g.drawLine(X_OFFSET + (int)Math.round(x1*PPM), Y_OFFSET + (int)Math.round(y1*PPM), X_OFFSET + (int)Math.round(x2*PPM), Y_OFFSET + (int)Math.round(y2*PPM));
	}

	public void buildAntlab(Graphics g){
		//		buildWall(g,0,0,18.6,0);
		//		buildWall(g,0,0,0,3.3);
		//		buildWall(g,0,3.3,3.3,3.3);
		//		buildWall(g,3.3,0,3.3,5.7);
		//		buildWall(g,3.3,5.7,18.6,5.7);
		//		buildWall(g,15.3,0,18.6,0);
		//		buildWall(g,15.3,2,15.3,5.7);
		//		buildWall(g,18.6,0,18.6,5.7);
		//		buildWall(g,15.3,2.1,16.8,2.1);
		g.drawImage(map_image,X_OFFSET,Y_OFFSET,this);
		g.setColor(Color.blue);
		if(this.isShowingWalls()){
			for(int i=0;i<NUM_WALLS;i++){
				buildWall(g,walls[0][i].getX(),walls[0][i].getY(),walls[1][i].getX(),walls[1][i].getY());
			}
		}




		//per laura
		//g.drawImage();

	}

	private void drawRule(Graphics g, int startX, int startY, int endX, int endY, float alpha){
		if(startX < endX && startY < endY){
			Graphics2D g2 = (Graphics2D)g;

			BufferedImage im = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
			Graphics2D g2im = im.createGraphics();

			AlphaComposite ac = 
					AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha); 
			g2im.setComposite(ac);
			g2im.drawImage(rule_texture, 0 ,0, this);
			TexturePaint t_p = new TexturePaint(im, new Rectangle(100,100));

			g2.setPaint(t_p);
			g2.fillRect(startX, startY, endX-startX, endY-startY);
		}
	}

	public void updateStat(){
		checkIntrusionRules();
		checkProximityRules();
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		double x = e.getX();
		double y = e.getY();
		boolean found = false;

		//se sto disegando la regola
		if(drawingRule && e.getButton()!=MouseEvent.BUTTON1){
			this.setDrawingRule(false);
		}

		//se sto posizionando ancore
		if(positioningAnchors){
			AnchorNode anchor = (AnchorNode)lmw.selected_node;
			if(e.getButton()==MouseEvent.BUTTON1){
				anchor.setX((x-X_OFFSET) / PPM);
				anchor.setY((y-Y_OFFSET) / PPM);
			}
			repaint();
			this.setPositioningAnchors(false);
			//aggiorna la posizione
			found = true;
			lmw.selectNode(anchor);

			lmw.printAnchorPosition(anchor);
		}

		//GT DEMO
		//if I am positioning an AP
		if(positioningAccessPoints){
			AccessPoint access_point = (AccessPoint)lmw.selected_node;
			if(e.getButton()==MouseEvent.BUTTON1){
				access_point.setX((x-X_OFFSET) / PPM);
				access_point.setY((y-Y_OFFSET) / PPM);
			}
			repaint();
			this.setPositioningAccessPoints(false);
			//aggiorna la posizione
			found = true;
			lmw.selectNode(access_point);

			//lmw.printAnchorPosition(access_point);
		}
		//

		//se sto disegnando una track
		if(drawingTrack && e.getButton()==MouseEvent.BUTTON1){
			showingTrack = true;
			if(!segment_ready){
				t_sX = e.getX();
				t_sY = e.getY();
				segment_ready = true;
			}
			else{
				track.addSegment(new Line2D.Double(((double)t_sX/PPM),((double)t_sY/PPM),((double)e.getX()/PPM),((double)e.getY()/PPM)));
				t_sX = e.getX();
				t_sY = e.getY();
			}
			repaint();
		}

		if(drawingTrack &&  e.getButton()==MouseEvent.BUTTON3){
			track.addSegment(new Line2D.Double(((double)t_sX/PPM),((double)t_sY/PPM),((double)e.getX()/PPM),((double)e.getY()/PPM)));
			drawingTrack = false;	
			showingTrack = false;
			segment_ready = false;
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		else{
			//seleziono un nodo mobile
			for(int i=0; i<mobile_list.size();i++){
				if(Math.abs(x-X_OFFSET-mobile_list.get(i).getX()*PPM)<15 && Math.abs(y-Y_OFFSET-mobile_list.get(i).getY()*PPM)<15){
					lmw.selectNode(mobile_list.get(i));
					found = true;
				}		
			}

			//seleziono un ancora
			for(int i=0; i<anchor_list.size();i++){
				if(drawingAnchors && Math.abs(x-X_OFFSET-anchor_list.get(i).getX()*PPM)<15 && Math.abs(y-Y_OFFSET-anchor_list.get(i).getY()*PPM)<15){
					lmw.selectNode(anchor_list.get(i));
					found = true;
				}	
			}

			//GT DEMO
			for(int i=0;i<access_points_list.size();i++){
				if(drawingAccessPoints && Math.abs(x-X_OFFSET-access_points_list.get(i).getX()*PPM)<15 && Math.abs(y-Y_OFFSET-access_points_list.get(i).getY()*PPM)<15){
					lmw.selectNode(access_points_list.get(i));
					found = true;
				}
			}
			if(lmw.selected_node instanceof AccessPoint){
				if(e.getClickCount()==2){
					this.setPositioningAccessPoints(true);
				}
			}
			//

			//ancora con doppio click
			if(lmw.selected_node instanceof AnchorNode){
				if(e.getClickCount()==2){
					this.setPositioningAnchors(true);
				}
			}

			//se avevo un nodo mobile selezionato e clicco su una sua regola devo selezionarla
			if(lmw.selected_node instanceof MobileNode){
				MobileNode node = (MobileNode)lmw.selected_node;
				for(int i=0;i<node.getIntrusionRules().size();i++){
					IntrusionRule rule = node.getIntrusionRules().get(i);
					//questo vuol dire che ci ho cliccato dentro
					if(rule.checkForIntrusion(new Point2D((x-X_OFFSET)/PPM,(y-Y_OFFSET)/PPM))){
						rule.setSelected(true);
						found = true;
						lmw.selectRule(rule.getName());
						this.repaint();
					}
					else rule.setSelected(false);
				}
			}
		}
		//non ho selezionato ne un nodo ne un ancora ne un nodo fisso
		if(!found){
			lmw.selectNode(null);
		}



	}

	@Override
	public void mouseEntered(MouseEvent e) {


	}

	@Override
	public void mouseExited(MouseEvent e) {


	}

	@Override
	public void mousePressed(MouseEvent e) {


		if(drawingRule){
			t_lX = e.getX();
			t_lY = e.getY();
		}

		//		else if(drawingTrack){
		//			showingTrack = true;
		//			if(!segment_ready){
		//				t_sX = e.getX();
		//				t_sY = e.getY();
		//				segment_ready = true;
		//			}
		//			if(segment_ready){
		//				track.addSegment(new Line2D.Double(t_sX/PPM,t_sY/PPM,e.getX()/PPM,e.getY()/PPM));
		//				segment_ready = false;
		//			}
		//		}
		//		
		//		else if(drawingTrack &&  e.getButton()==MouseEvent.BUTTON3){
		//			drawingTrack = false;
		//		}


		else if(!drawingTrack && e.getButton()==MouseEvent.BUTTON3){
			double x = e.getX();
			double y = e.getY();
			int at1=0,at6=0,selected=-1;

			//seleziono un ancora

			//accrocchio temperatura
			/*
			for(int i=0; i<anchor_list.size();i++){
				if(anchor_list.get(i).getMac()==7){
					at1 = i;
				}
				if(anchor_list.get(i).getMac()==13){
					at6 = i;
				}
			}

			if(Math.abs(x-X_OFFSET-26.1*PPM)<15 && Math.abs(y-Y_OFFSET-8.5*PPM)<15){
				selected = at1;
			}

			if(Math.abs(x-X_OFFSET-9.8*PPM)<15 && Math.abs(y-Y_OFFSET-2.15*PPM)<15){
				selected = at6;
			}*/

			for(int i=0; i<anchor_list.size();i++){
				if(drawingAnchors && Math.abs(x-X_OFFSET-anchor_list.get(i).getX()*PPM)<15 && Math.abs(y-Y_OFFSET-anchor_list.get(i).getY()*PPM)<15){
					selected = i;
				}
			}

			if(selected>-1){
				//lmw.selectNode(anchor_list.get(selected));

				//popup
				TimeSeries series = new TimeSeries("First");
				ArrayList<Double> temp_array = anchor_list.get(selected).getDailyTemp();
				ArrayList<Minute> temp_array_min = anchor_list.get(selected).getDailyTime();
				for(int j=0;j<temp_array.size();j++){
					System.out.println(temp_array_min.get(j));
					series.add(temp_array_min.get(j),temp_array.get(j));
				}



				TimeSeriesCollection dataset = new TimeSeriesCollection();

				dataset.addSeries(series);

				Calendar cal = Calendar.getInstance();
				int day = cal.get(Calendar.DATE);
				int month = cal.get(Calendar.MONTH) + 1;
				int year = cal.get(Calendar.YEAR);
				String title = new String("Temperature of day: " + day + " - " + month + " - " + year);

				JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Time", "Temp [°]", dataset, false, false, false);
				TextTitle ttitle = chart.getTitle(); 
				ttitle.setFont(new Font("Serif",Font.PLAIN,14));

				ChartPanel panel = new ChartPanel(chart);

				panel.setPreferredSize(new Dimension(350,200));
				PopupFactory factory = PopupFactory.getSharedInstance();
				popup = factory.getPopup(this, panel, (int)x-50, (int)y-50);
				popup.show();
				popup_showed = true;
			}	
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(drawingRule && t_lX < b_rX && t_lY < b_rY && lmw.selected_node instanceof MobileNode){
			MobileNode node = (MobileNode)lmw.selected_node;
			String name = JOptionPane.showInputDialog("Enter a name for the rule","rule");

			if(name!=null){
				while(ruleNameIsUsed(name)){
					name = JOptionPane.showInputDialog("Rule name is already used or not valid, please change rule name");
					if(name == null) break;
				}
				if(name != null){ 
					node.addIntrusionRule(new Point2D((double)(t_lX - X_OFFSET)/PPM,(double)(t_lY - Y_OFFSET)/PPM), new Point2D((double)(b_rX - X_OFFSET)/PPM,(double)(b_rY - Y_OFFSET)/PPM), name);
					lmw.updateRulesCombo();
					lmw.selectRule(name);
				}
			}
			//clear parameters
			this.setDrawingRule(false);
			t_lX = 0;
			t_lY = 0;
			b_rX = 0;
			b_rY = 0;
			repaint();
		}

		if(popup_showed){
			popup.hide();
			popup_showed = false;
		}
	}

	private boolean ruleNameIsUsed(String name) {

		if(name.equals("")){
			return true;
		}
		else if(lmw.selected_node instanceof MobileNode){
			MobileNode node = (MobileNode)lmw.selected_node;
			for(int i=0; i<node.getIntrusionRules().size();i++){
				IntrusionRule rule = node.getIntrusionRules().get(i);
				if(rule.getName().equals(name)) return true;
			}
		}
		return false;
	}

	public boolean isDrawingRule() {
		return drawingRule;
	}

	public void setDrawingRule(boolean drawingRule) {
		this.drawingRule = drawingRule;
		if(drawingRule){
			this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		else{
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public boolean isShowingRule() {
		return showingRule;
	}

	public void setShowingRule(boolean showingRule) {
		this.showingRule = showingRule;
		repaint();
	}

	public boolean isDrawingAnchors() {
		return drawingAnchors;
	}

	public void setDrawingAnchors(boolean drawingAnchors) {
		this.drawingAnchors = drawingAnchors;
		repaint();
	}

	//GT DEMO
	public boolean isDrawingAccessPoints(){
		return drawingAccessPoints;
	}

	public void setDrawingAccessPoints(boolean drawingAccessPoints){
		this.drawingAccessPoints = drawingAccessPoints;
		repaint();
	}
	//

	public boolean isDrawingNames() {
		return drawingNames;
	}


	public void setDrawingNames(boolean drawingNames) {
		this.drawingNames = drawingNames;
		repaint();
	}


	public void setShowingWalls(boolean showingWalls) {
		this.showingWalls = showingWalls;
		repaint();
	}


	public boolean isShowingWalls() {
		return showingWalls;
	}

	public void setDrawingTrack(boolean drawingTrack){
		this.drawingTrack = drawingTrack;
		if(drawingTrack){
			track = new Track();
		}
		this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		System.out.println("CREATA TRACCIA : " + track.getNofSegments());
	}

	public void setShowingTrack(boolean showingTrack){
		this.showingTrack = showingTrack;
		repaint();
	}

	public boolean isShowingTrack(){
		return showingTrack;
	}

	public void setTrackLocking(boolean locked_to_track){
		this.locked_to_track = locked_to_track;
		for(int i=0; i<mobile_list.size();i++){
			mobile_list.get(i).setTrackLocking(locked_to_track);
			mobile_list.get(i).setTrack(track);
		}
	}

	public boolean isTrackLocked(){
		return locked_to_track;
	}



	public void checkIntrusionRules(){
		//per ogni nodo
		for(int i=0; i<mobile_list.size();i++){
			MobileNode mobile_node = mobile_list.get(i);
			//per ogni regola di intrusione
			for(int j=0; j<mobile_node.getIntrusionRules().size(); j++){
				IntrusionRule rule = mobile_node.getIntrusionRules().get(j);
				if(rule.checkForIntrusion(mobile_node.getLocation())){
					int w = 470;
					int h = 280;
					int x_s = X_OFFSET + (int)Math.round(mobile_node.getX()*PPM) - w/2;
					int y_s = Y_OFFSET + (int)Math.round(mobile_node.getY()*PPM) - h/2;
					if(x_s < 0) x_s = 0;
					if(y_s < 0) y_s = 0;
					if(x_s + w > map_image.getWidth()) x_s = map_image.getWidth()-w;
					if(y_s + h > map_image.getHeight()) y_s = map_image.getHeight()-h;
					Point2D node_p = new Point2D(Math.round(mobile_node.getX()*PPM) - x_s,Math.round(mobile_node.getY()*PPM) - y_s);
					event_manager.addIntrusionEvent(mobile_node.getPatientId(),rule.getName(),node_p,map_image.getSubimage(x_s, y_s, w, h));
					mobile_node.setHasEvent(true);
				}
			}
		}
	}

	public void checkProximityRules(){
		//per ogni nodo
		for(int i=0; i<mobile_list.size();i++){
			MobileNode node = mobile_list.get(i);
			for(int j=0; j<node.getProximityRules().size(); j++){
				String prox_rule = node.getProximityRules().get(j);
				//cerco il nodo della regola
				for(int k=0;k<mobile_list.size();k++){
					if(k!=i){
						MobileNode enemy_node = mobile_list.get(k);
						if(enemy_node.getPatientId().equals(prox_rule)){
							System.out.println("Checking");
							if(node.checkForProximity(enemy_node)){
								int w = 470;
								int h = 280;
								int x_s = X_OFFSET + (int)Math.round(node.getX()*PPM) - w/2;
								int y_s = Y_OFFSET + (int)Math.round(node.getY()*PPM) - h/2;
								if(x_s < 0) x_s = 0;
								if(y_s < 0) y_s = 0;
								if(x_s + w > map_image.getWidth()) x_s = map_image.getWidth()-w;
								if(y_s + h > map_image.getHeight()) y_s = map_image.getHeight()-h;
								Point2D node_p = new Point2D(Math.round(node.getX()*PPM) - x_s,Math.round(node.getY()*PPM) - y_s);
								Point2D enemy_p = new Point2D(Math.round(enemy_node.getX()*PPM) - x_s,Math.round(enemy_node.getY()*PPM) - y_s);
								event_manager.addProximityEvent(node.getPatientId(),prox_rule,node_p,enemy_p,map_image.getSubimage(x_s, y_s, w, h));
								node.setHasEvent(true);
							}
						}
					}
				}
			}
		}
	}



	public void mouseWheelMoved(MouseWheelEvent e) {

	}


	public void setPositioningAnchors(boolean positioningAnchors) {
		this.positioningAnchors = positioningAnchors;
		if(positioningAnchors){
			this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		else{
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	//GT DEMO
	public void setPositioningAccessPoints(boolean positioningAccessPoints) {
		this.positioningAccessPoints = positioningAccessPoints;
		if(positioningAccessPoints){
			this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		else{
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	//


	public boolean isPositioningAnchors() {
		return positioningAnchors;
	}


	public final class ScreenRefresher extends TimerTask{
		MapPanel panel;
		public ScreenRefresher(long period, MapPanel panel){
			Timer timer = new Timer();
			timer.schedule(this, 1000, period);
			this.panel = panel;
		}

		public void run() {
			panel.repaint();
		}	
	}

	private void buildWalls(){

		//		//MURI ALE
		//		double x[] = {80, 100,94 ,265,265,226,221,439,439,309,309,567,567,468,468,598,598,624,624,598,598,651,655,748,745,803,798,726,726,756,754,664,668,692,697,609,609,651,644,450,459,558,560,312,312,430,428,223,223,274,270,140,140,197,210,155,160,80 ,80};
		//		double y[] = {474,474,555,559,577,586,848,843,579,573,553,546,570,575,839,839,780,777,568,568,551,546,852,852,548,544,485,485,441,441,205,196,438,441,482,482,465,465,207,207,458,460,485,487,474,465,207,212,463,467,491,480,469,469,196,196,286,291,474};
		//		
		//		for(int i=0;i<x.length-1;i++){
		//		walls[0][i] = new Point2D(x[i]/50,y[i]/50);
		//		walls[1][i] = new Point2D(x[i+1]/50, y[i+1]/50);
		//		System.out.println("wall " + i);
		//		System.out.print("start: ");
		//		walls[0][i].print();
		//		System.out.print("stop: ");
		//		walls[1][i].print();
		//	}

		//MURI LAURA
		double[] x = {200,291,291,254,281,336,397,397,340,340,485,485,460,460,410,410,455,520,560,600,600,545,545,650,650,610,610,640,640,660,655,585,525,455,440,485,560,560,485,420,440,400,350,380,380,320,320,360,360,310,225,135,90,90,260,260,200,200};
		double[] y = {241,241,227,227,133,76,177,230,230,244,244,233,233,217,217,180,180,80,115,185,231,233,244,244,284,284,300,300,340,340,395,450,450,400,350,300,300,284,284,340,400,450,400,330,284,284,300,300,345,415,445,410,335,300,300,284,284,241};

		for(int i=0;i<x.length-1;i++){

			walls[0][i] = new Point2D(x[i]/25,y[i]/25);
			walls[1][i] = new Point2D(x[i+1]/25, y[i+1]/25);
			System.out.println("wall " + i);
			System.out.print("start: ");
			walls[0][i].print();
			System.out.print("stop: ");
			walls[1][i].print();
		}

		//		//MURI 3 PIANO
		//		double x[] = { 15, 125, 125, 70, 70, 140    ,140, 465, 465, 530,530,625,625, 805, 805,625, 625,805,805,645,645,625,625,580,580,570,570,540,535,490  , 490,530,530,500,500,530,530,450,450,480,480,440,440,380, 380, 365, 365, 270, 270, 245, 245, 145, 145, 40, 40, 15, 15};
		//		double y[] = {400, 400, 455, 455, 465,465, 400,400, 330 ,330,280,280,100,100, 305,305 , 320,320, 485,485,345,345,410,410,380,380,475,475,370,370 ,400,400,455,455,470,470,560,560,470,470,455,455,560,560,460 ,460 ,560, 560, 460, 460, 560 , 560 ,490, 490, 460, 460, 400  };
		//
		//		for(int i=0;i<x.length-1;i++){
		//
		//			walls[0][i] = new Point2D(x[i]/PPM,y[i]/PPM);
		//			walls[1][i] = new Point2D(x[i+1]/PPM, y[i+1]/PPM);
		//			//System.out.println("wall " + i);
		//			//System.out.print("start: ");
		//			//walls[0][i].print();
		//			//System.out.print("stop: ");
		//			//walls[1][i].print();
		//		}

		//		//MURI RFID LAB	
		//		double[] x = {774,774,662,662,774,774,632,632,629,629,358,358,629,629,632,632,580,580,629,629,358,358,478,478,358,358,326,326,355,355,12,12,299,299,12,12,91,91,215,215,612,612,218,218,372,372,368,368,218,218,574,574,576,576,774};
		//		double[] y = {22,292,292,295,295,655,655,638,638,655,655,584,584,610,610,404,404,408,408,581,581,408,408,404,404,396,396,401,401,655,655,401,401,396,396,142,142,22,22,295,295,292,292,100,100,54,54,94,94,22,22,250,250,22,22};
		//		
		//		for(int i=0;i<x.length-1;i++){
		//			
		//			walls[0][i] = new Point2D(x[i]/PPM,y[i]/PPM);
		//			walls[1][i] = new Point2D(x[i+1]/PPM, y[i+1]/PPM);
		//			System.out.println("wall " + i);
		//			System.out.print("start: ");
		//			walls[0][i].print();
		//			System.out.print("stop: ");
		//			walls[1][i].print();
		//		}

		//		//MURI RFID LAB 2
		//		double [] x = {960,460,460,45,45,630,630,1030,1030,220,220,910,910,275,275,1000,1000,960,960};
		//		double [] y = {235,235,145,145,840,840,890,890,770,770,300,300,390,390,700,700,390,390,235};
		//
		//		for(int i=0;i<x.length-1;i++){
		//
		//			walls[0][i] = new Point2D(x[i]*0.7/PPM,y[i]*0.7/PPM);
		//			walls[1][i] = new Point2D(x[i+1]*0.7/PPM, y[i+1]*0.7/PPM);
		//			System.out.println("wall " + i);
		//			System.out.print("start: ");
		//			walls[0][i].print();
		//			System.out.print("stop: ");
		//			walls[1][i].print();
		//		}


	}




}
