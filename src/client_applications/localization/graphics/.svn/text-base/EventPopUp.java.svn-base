package client_applications.localization.graphics;


import client_applications.localization.Event;
import client_applications.localization.EventManager;
import client_applications.localization.IntrusionEvent;
import client_applications.localization.Point2D;
import client_applications.localization.ProximityEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class EventPopUp extends JFrame implements ActionListener {

	JPanel content;
	JPanel center,bottom,right_image,right_image_info,up;
	GifPanel center_image;
	InfoPanel info1, info2;
	ImagePanel map_zoom;
	JButton ok, cancel;
	Clip alarm;
	EventManager event_manager;
	Event event;
	boolean isToRepeat = true;
	
	public EventPopUp(Event event, String title, EventManager event_manager){
		this.event = event;
		this.event_manager = event_manager;
		up = new JPanel();// new TransparentBackground(this);
		bottom = new JPanel(); //new TransparentBackground(this);
		
		ok = new JButton("Posticipa...");
		ok.setName("ok");
		ok.setAlignmentX(LEFT_ALIGNMENT);
		ok.addActionListener(this);
		cancel = new JButton("Risolto!");
		cancel.setName("annulla");
		cancel.addActionListener(this);
		
		bottom.setLayout(new BoxLayout(bottom,BoxLayout.X_AXIS));
		bottom.add(Box.createRigidArea(new Dimension(100,10)));
		bottom.add(ok);
		bottom.add(Box.createRigidArea(new Dimension(120,10)));
		bottom.add(cancel);
		bottom.setBorder(BorderFactory.createRaisedBevelBorder());
		
		center = new JPanel();//new TransparentBackground(this);
		center.setLayout(new BoxLayout(center,BoxLayout.X_AXIS));
		
		
		

		right_image = new JPanel();//new TransparentBackground(this);
		right_image.setLayout(new BoxLayout(right_image,BoxLayout.Y_AXIS));
		
		//sottopannello per le info
		right_image_info = new JPanel();
		right_image_info.setLayout(new BoxLayout(right_image_info,BoxLayout.X_AXIS));
		
		if(event instanceof IntrusionEvent){
			IntrusionEvent ie = (IntrusionEvent)event;
			if(ie.getRule().equals("Pallet Rotativo")){
				center_image = new GifPanel("intrusione-pallet.gif");
			}
			else if(ie.getRule().equals("Macchinari pericolosi")){
				center_image = new GifPanel("intrusione-macchinari.gif");
			}
			
			
			center_image.setBorder(BorderFactory.createRaisedBevelBorder());
			info1 = new InfoPanel("worker7.gif");
			info1.setInfo(((IntrusionEvent) event).getPatient_id(), ((IntrusionEvent) event).getRule());
			
			map_zoom = new ImagePanel(event.getMap_zoom(),true);
			map_zoom.setMaximumSize(new Dimension(470,280));
			right_image.add(map_zoom);
			right_image.add(Box.createRigidArea(new Dimension(10,10)));
			
			right_image.add(right_image_info);
			right_image_info.add(info1);
			right_image_info.add(Box.createRigidArea(new Dimension(220,100)));
			
		}
		if(event instanceof ProximityEvent){
			center_image = new GifPanel("gif-animata-prox.gif");
			center_image.setBorder(BorderFactory.createRaisedBevelBorder());
			map_zoom = new ImagePanel(event.getMap_zoom(),true);
			map_zoom.setMaximumSize(new Dimension(470,280));
			
			
			info1 = new InfoPanel("patient_small.jpg");
			info1.setInfo(((ProximityEvent) event).getPatient_id(), "boh");
			
			right_image.add(map_zoom);
			right_image.add(Box.createRigidArea(new Dimension(10,10)));
			
			
			right_image.add(right_image_info);
			right_image_info.add(info1);
			info2 = new InfoPanel("patient_small.jpg");
			info2.setInfo(((ProximityEvent) event).getPatient_enemy_id(), "boh");
			right_image_info.add(Box.createRigidArea(new Dimension(10,20)));
			right_image_info.add(info2);
		}
		
		
			
				
		center.add(Box.createRigidArea(new Dimension(10,400)));
		center.add(center_image);
		center.add(Box.createRigidArea(new Dimension(10,400)));
		center.add(right_image);
		center.setBorder(BorderFactory.createRaisedBevelBorder());
		

		
		content = new JPanel();
		content.setLayout(new BorderLayout(10,0));
		
		content.add(center,BorderLayout.CENTER);
		content.add(bottom,BorderLayout.SOUTH);
		content.add(up,BorderLayout.NORTH);
		content.setBorder(BorderFactory.createEmptyBorder());
		
		//fai suonare l'allarme
		SoundRepeater sound_repeater = new SoundRepeater(5000);
		alarm = playAlarm();
		
		
		this.setTitle(title);
		this.setSize(new Dimension(1000,500));
	    // Get the size of the screen
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();    
	    // Determine the new location of the window
	    int w = this.getSize().width;
	    int h = this.getSize().height;
	    int x = (dim.width-w)/2;
	    int y = (dim.height-h)/2;
	    // Move the window
	    this.setLocation(x, y);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		this.setContentPane(content);
		
	}
	
	public static void main(String[] args) {
		IntrusionEvent event = new IntrusionEvent("Ale", "Pallet Rotativo", 1000, new Point2D(0,0),new BufferedImage(10,10,10));
		EventPopUp me = new EventPopUp(event,"prova", new EventManager(null));
		me.setVisible(true);
	}
	
	public Clip playAlarm(){
		String filename = "/client_applications/localization/sounds/extreme_alarm.wav";
		URL url = getClass().getResource(filename);
		AudioFileFormat aff;
		AudioInputStream ais;
		Clip ol = null;
		try
		{
		aff=AudioSystem.getAudioFileFormat(url);
		ais=AudioSystem.getAudioInputStream(url);
		AudioFormat af=aff.getFormat();
		DataLine.Info info = new DataLine.Info(
		Clip.class,
		ais.getFormat(),
		((int) ais.getFrameLength() *
		af.getFrameSize()));
		ol = (Clip) AudioSystem.getLine(info);
		ol.open(ais);
		ol.start();
		}
		catch(UnsupportedAudioFileException ee){}
		catch(IOException ea){}
		catch(LineUnavailableException LUE){} 
		
		return ol;
	}
	
	class ImagePanel extends JPanel{
		private BufferedImage image;
		boolean location = false;

		
		public ImagePanel(BufferedImage image, boolean location){
			this.image = image;
			//WritableRaster raster = Raster.createWritableRaster(image.getSampleModel(), null);
			//image.copyData(raster);
		 	this.setMaximumSize(new Dimension(image.getWidth(this),image.getHeight(this)));
		 	this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		 	this.location = location;
		}
		
		public ImagePanel(String image_file){
		    image = null;

		  	try {
		 		String filename = "/client_applications/localization/graphics/";
		 		URL url = getClass().getResource(filename + image_file);
		 		image = ImageIO.read(url);
		 	} catch (IOException e) {
		 		e.printStackTrace();
		 	}
		 	this.setMaximumSize(new Dimension(image.getWidth(this),image.getHeight(this)));
		 	this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		}
		
		public void paintComponent(Graphics g){
			g.drawImage(image,0,0,this);
			if(location){
				if(event instanceof IntrusionEvent){
					g.setColor(Color.red);
					g.fillOval(((int)((IntrusionEvent) event).getEvent_location().getX()-7), (int)((IntrusionEvent) event).getEvent_location().getY()-7, 15, 15);
					g.setColor(Color.black);
					g.setFont(new Font("Helvetica", Font.BOLD,  14));
					g.drawString(((IntrusionEvent) event).getPatient_id(), ((int)((IntrusionEvent) event).getEvent_location().getX()-7), ((int)((IntrusionEvent) event).getEvent_location().getY()-7));
				}
				if(event instanceof ProximityEvent){
					g.setColor(Color.red);
					g.fillOval(((int)((ProximityEvent) event).getPatient_loc().getX()-7), (int)((ProximityEvent) event).getPatient_loc().getY()-7, 15, 15);
					g.fillOval(((int)((ProximityEvent) event).getEnemy_loc().getX()-7), (int)((ProximityEvent) event).getEnemy_loc().getY()-7, 15, 15);
					g.setColor(Color.black);
					g.setFont(new Font("Helvetica", Font.BOLD,  14));
					g.drawString(((ProximityEvent) event).getPatient_id(), ((int)((ProximityEvent) event).getPatient_loc().getX()-7), (int)((ProximityEvent) event).getPatient_loc().getY()-7);
					int delta = 0;
					if(Math.abs((((ProximityEvent) event).getPatient_loc().getX()-7)-(((ProximityEvent) event).getEnemy_loc().getX()-7))<40
							&& !(Math.abs((((ProximityEvent) event).getPatient_loc().getY()-7)-(((ProximityEvent) event).getEnemy_loc().getY()-7))<30))
						delta = 25;
					g.drawString(((ProximityEvent) event).getPatient_enemy_id(), ((int)((ProximityEvent) event).getEnemy_loc().getX()-7), (int)((ProximityEvent) event).getEnemy_loc().getY()-7 + delta);
				}
			}
				
				
			
		}
		
		public void paintLocation(Graphics g){
        	g.setColor(Color.red);    	
			g.fillOval(235-7, 140-7, 15, 15);
		}
		

	}
	
	class GifPanel extends JPanel{
		private Icon image;
		
		public GifPanel(String image_file){
		    image = null;
	
	 		String filename = "/client_applications/localization/graphics/";
	 		URL url = getClass().getResource(filename + image_file);
	 		image = new ImageIcon(url);
	
	 	this.setMaximumSize(new Dimension(image.getIconWidth(),image.getIconHeight()));
	 	this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		image.paintIcon(this, g2, 0, 0);
	}
	}
	
	class InfoPanel extends JPanel{
		private ImagePanel patient_image;
		private JPanel infos = new JPanel();
		private JLabel patient_name = new JLabel("Nome: ");
		private JLabel patient_surname = new JLabel("Cognome: ");
		private JLabel patient_birthdate = new JLabel("Data nascita: ");
		private JLabel patient_date = new JLabel("Mansione: ");

		
		public InfoPanel(String filename){
			patient_image = new ImagePanel(filename);
			patient_image.setAlignmentX(LEFT_ALIGNMENT);
			infos.setLayout(new BoxLayout(infos,BoxLayout.Y_AXIS));
			infos.add(patient_name);
			infos.add(patient_surname);
			infos.add(patient_birthdate);
			infos.add(patient_date);
			//infos.add(patient_infos);
			infos.setAlignmentX(LEFT_ALIGNMENT);
			
			this.setMaximumSize(new Dimension(220,100));
			this.setBorder(BorderFactory.createRaisedBevelBorder());
			this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));;
			this.add(patient_image);
			this.add(Box.createRigidArea(new Dimension(10,10)));
			this.add(infos);
		}
		
		public void setInfo(String name, String info){
			Font font = new Font("Helvetica", Font.PLAIN,  9);
			patient_name.setFont(font);
			patient_name.setText("Nome: " + name);
			
			//RFID
			if(name.equals("Ale")){
			patient_surname.setFont(font);
			patient_birthdate.setFont(font);
			patient_date.setFont(font);
			//patient_infos.setFont(font);
			patient_surname.setText("Cognome: Rossi");
			patient_birthdate.setText("Nato: 10/07/1984");
			patient_date.setText("Mansione: Tecnico Installatore");
		}
			//LAURA
//			if(name.equals("Ale")){
//				patient_surname.setFont(font);
//				patient_birthdate.setFont(font);
//				patient_date.setFont(font);
//				patient_infos.setFont(font);
//				patient_surname.setText("Cognome: Rossi");
//				patient_birthdate.setText("Nato: 10/07/1984");
//				patient_date.setText("Ricovero: 25/03/1999");
//				patient_infos.setText("Note: Paziente aggressivo!");
//			}
//			
//			if(name.equals("Luca")){
//				patient_surname.setFont(font);
//				patient_birthdate.setFont(font);
//				patient_date.setFont(font);
//				patient_infos.setFont(font);
//				patient_surname.setText("Cognome: Bianchi");
//				patient_birthdate.setText("Nato: 10/05/1983");
//				patient_date.setText("Ricovero: 11/06/11993");
//				patient_infos.setText("Note: Paziente epilettico!");
//			}
//			
//			if(name.equals("Marco")){
//				patient_surname.setFont(font);
//				patient_birthdate.setFont(font);
//				patient_date.setFont(font);
//				patient_infos.setFont(font);
//				patient_surname.setText("Cognome: Verdi");
//				patient_birthdate.setText("Nato: 16/04/1976");
//				patient_date.setText("Ricovero: 17/08/1994");
//				patient_infos.setText("Note: Paziente aggressivo!");
//			}
//			
//			if(name.equals("Matteo")){
//				patient_surname.setFont(font);
//				patient_birthdate.setFont(font);
//				patient_date.setFont(font);
//				patient_infos.setFont(font);
//				patient_surname.setText("Cognome: Rossi");
//				patient_birthdate.setText("Nato: 14/03/1975");
//				patient_date.setText("Ricovero: 25/10/1999");
//				patient_infos.setText("Note: Paziente epilettico!");
//			}
			
			
		}
		
	}
	
	class SoundRepeater extends TimerTask{

		public SoundRepeater(long period){
			Timer timer = new Timer();
			timer.schedule(this, 1000, period);
		}
		
		public void run() {
			if(!alarm.isRunning() && isToRepeat){
				alarm.stop();
				alarm.start();
			}
		}
		
	}
	
	class WindowsDestroyer extends WindowAdapter{

		public void windowClosing(WindowEvent e){
			
		}
	}

	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		alarm.stop();
		if(source.getName() == "ok"){
			
			event_manager.posticipate(this,event);
		}
		if(source.getName() == "annulla"){
			event_manager.remove(this,event);
		}
		isToRepeat = false;
		this.setVisible(false);
	}

	
	
	
	

}
