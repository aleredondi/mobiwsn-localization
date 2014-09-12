package client_applications.localization.graphics;

import client_applications.localization.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LauraMainWindow extends JFrame implements ComponentListener{
	
	
	//manager
	LauraManager main_app;
	ArrayList<MobileNode> mobile_list;
	ArrayList<AnchorNode> anchor_list;
	//GT DEMO
	ArrayList<AccessPoint> access_points_list;
	LauraNode selected_node = null;
	
	//qua passerei i puntatori alle strutture dati dei nodi per disegnarli
	JPanel content;
	MapPanel map_panel; 
	QuickPanel quick_panel;
	JCheckBoxMenuItem showParamMenuItem;
	JCheckBoxMenuItem adParamMenuItem;
	JCheckBoxMenuItem showAnchorsMenuItem ;
	// GT DEMO
	JCheckBoxMenuItem showAccessPointsMenuItem;
	//
	JCheckBoxMenuItem showPatientNameMenuItem;
	JCheckBoxMenuItem showPatientRulesMenuItem;
	JCheckBoxMenuItem showWallsMenuItem;
	JMenuItem addTrackMenuItem;
	JCheckBoxMenuItem enableTrackMenuItem;
	JCheckBoxMenuItem showTrackMenuItem;
	

	public LauraMainWindow(LauraManager main_app, ArrayList<AnchorNode> anchor_list, ArrayList<MobileNode> mobile_list, ArrayList<AccessPoint> access_points_list){
		
		
		this.main_app = main_app;
		this.anchor_list = anchor_list;
		this.mobile_list = mobile_list;
		//GT DEMO
		this.access_points_list = access_points_list;
		//
		this.setTitle("GreenTouch DEMO");
		map_panel =  new MapPanel(anchor_list, mobile_list, access_points_list, this);
		
		//pannello comandi
		quick_panel = new QuickPanel(anchor_list, mobile_list, access_points_list, this);
		
		
		////////////////////////////////////////////////////////
		//MENU
		ActionListener menuListener = new MenuActionListener();
		JMenuBar menubar = new JMenuBar();
		this.setJMenuBar(menubar);
		
		//FILE
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menubar.add(fileMenu);
		
		//File - load map
		JMenuItem loadMenuItem = new JMenuItem("Load Map", KeyEvent.VK_L);
		loadMenuItem.addActionListener(menuListener);
		fileMenu.add(loadMenuItem);
		
		//File - exit
		JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		exitMenuItem.addActionListener(menuListener);
		fileMenu.add(exitMenuItem);
		
		//TRACKS
		JMenu tracksMenu = new JMenu("Tracks");
		tracksMenu.setMnemonic(KeyEvent.VK_T);
		menubar.add(tracksMenu);
		
		//Tracks - Add Track
	    addTrackMenuItem = new JMenuItem("Add Track");
		addTrackMenuItem.addActionListener(menuListener);
		addTrackMenuItem.setSelected(true);
		tracksMenu.add(addTrackMenuItem);
		
		//Tracks - Enable Track
	    enableTrackMenuItem = new JCheckBoxMenuItem("Lock Target To Track");
	    enableTrackMenuItem.addActionListener(menuListener);
	    enableTrackMenuItem.setSelected(false);
		tracksMenu.add(enableTrackMenuItem);
		
		
		//VIEW
		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic(KeyEvent.VK_V);
		menubar.add(viewMenu);
		
		//View - show quick param panel
	    showParamMenuItem = new JCheckBoxMenuItem("Show Quick Panel");
		showParamMenuItem.addActionListener(menuListener);
		showParamMenuItem.setSelected(true);
		viewMenu.add(showParamMenuItem);
		
		//View - attach / detach quick panel
	    adParamMenuItem = new JCheckBoxMenuItem("Attach Quick Panel");
		adParamMenuItem.addActionListener(menuListener);
		adParamMenuItem.setSelected(false);
		viewMenu.add(adParamMenuItem);
		
		viewMenu.add(new JSeparator());
		
		//View - show patient name
		showPatientNameMenuItem = new JCheckBoxMenuItem("Show Node Names");
		showPatientNameMenuItem.addActionListener(menuListener);
		showPatientNameMenuItem.setSelected(true);
		viewMenu.add(showPatientNameMenuItem);
		
		//View - show patient rules
		showPatientRulesMenuItem = new JCheckBoxMenuItem("Show Node Rules");
		showPatientRulesMenuItem.addActionListener(menuListener);
		showPatientRulesMenuItem.setSelected(true);
		viewMenu.add(showPatientRulesMenuItem);
		
		viewMenu.add(new JSeparator());
		
		//View - show anchors
		showAnchorsMenuItem = new JCheckBoxMenuItem("Show Anchors");
		showAnchorsMenuItem.addActionListener(menuListener);
		viewMenu.add(showAnchorsMenuItem);
		
		//GT DEMO
		//View - show MobiMesh AP
		showAccessPointsMenuItem = new JCheckBoxMenuItem("Show Access Points");
		showAccessPointsMenuItem.addActionListener(menuListener);
		viewMenu.add(showAccessPointsMenuItem);
		//
		
		//View - show walls
		showWallsMenuItem = new JCheckBoxMenuItem("Show Walls");
		showWallsMenuItem.addActionListener(menuListener);
		viewMenu.add(showWallsMenuItem);
		
		//View - show track
		showTrackMenuItem = new JCheckBoxMenuItem("Show Track");
		showTrackMenuItem.addActionListener(menuListener);
		viewMenu.add(showTrackMenuItem);
		
		
		//////////////////////////////////////////////////////////

		
		//main
		addComponentListener(this);
		content = new JPanel();
		content.add(map_panel);
		this.setContentPane(content);
	}
	
	


	
	public void updateStat(){
		map_panel.updateStat();
		repaint();
	}
	
	public void selectNode(LauraNode selected_node){
		this.selected_node = selected_node;
		quick_panel.selectNode(selected_node);
		if(!quick_panel.isVisible()){
			quick_panel.setVisible(true);
			showParamMenuItem.setSelected(true);
		}
	}
	
	
	public void selectRule(String rule){
		quick_panel.selectRule(rule);
	}
	
	
	//menu principale
	class MenuActionListener implements ActionListener {
		  public void actionPerformed (ActionEvent actionEvent) {
		    
			//esci
		    if(actionEvent.getActionCommand() == "Exit"){
		    	System.exit(0);
		    }
		    
		    //visualizza ancore
		    if(actionEvent.getActionCommand() == "Show Anchors"){
		    	if(map_panel.isDrawingAnchors()){
		    		map_panel.setDrawingAnchors(false);
		    	}
		    	else map_panel.setDrawingAnchors(true);
		    }
		    
		    //GT DEMO
		    //show access points
		    if(actionEvent.getActionCommand() == "Show Access Points"){
		    	if(map_panel.isDrawingAccessPoints()){
		    		map_panel.setDrawingAccessPoints(false);
		    	}
		    	else map_panel.setDrawingAccessPoints(true);
		    }
		    //
		    
		    //visualizza pannello comandi
		    if(actionEvent.getActionCommand() == "Show Quick Panel"){
		    	if(quick_panel.isVisible()){
		    		quick_panel.setVisible(false);
		    		adParamMenuItem.setEnabled(false);
		    	}
		    	else {
		    		quick_panel.setVisible(true);
		    		adParamMenuItem.setEnabled(true);
		    	}
		    }
		    
		    //attacca le due finestre
		    if(actionEvent.getActionCommand() == "Attach Quick Panel"){
		    	if(quick_panel.isAttached()){
		    		quick_panel.setAttached(false);
		    	}
		    	else 
		    		quick_panel.setAttached(true);
		    }
		    
		    //visualizza i nomi dei nodi mobili
		    if(actionEvent.getActionCommand() == "Show Node Names"){
		    	if(map_panel.isDrawingNames()){
		    		map_panel.setDrawingNames(false);
		    	}
		    	else map_panel.setDrawingNames(true);
		    }
		    
		    //visualizza le regole per il nodo selezionato
		    if(actionEvent.getActionCommand() == "Show Node Rules"){
		    	if(map_panel.isShowingRule()){
		    		map_panel.setShowingRule(false);
		    	}
		    	else map_panel.setShowingRule(true);
		    }
		    
		    //Visualizza i muri
		    if(actionEvent.getActionCommand() == "Show Walls"){
		    	if(map_panel.isShowingWalls()){
		    		map_panel.setShowingWalls(false);
		    	}
		    	else map_panel.setShowingWalls(true);
		    	
		    }
		    
		    //Visualizza i binari
		    if(actionEvent.getActionCommand() == "Show Track"){
		    	if(map_panel.isShowingTrack()){
		    		map_panel.setShowingTrack(false);
		    	}
		    	else map_panel.setShowingTrack(true);
		    }
		    
		    //Attiva la creazione dei binari
		    if(actionEvent.getActionCommand() == "Add Track"){
		    	JOptionPane.showMessageDialog(map_panel, "Draw a track, right click to finish", "Track", JOptionPane.INFORMATION_MESSAGE);
		    	map_panel.setShowingTrack(false);
		    	map_panel.setDrawingTrack(true);
		    }
		    
		    //Attiva il lock del target ai binari
		    
		    if(actionEvent.getActionCommand() == "Lock Target To Track"){
		    	if(map_panel.isTrackLocked()){
		    		map_panel.setTrackLocking(false);
		    	}
		    	else map_panel.setTrackLocking(true);
		    }
		    
		    
		  }
	}

	public void setDrawingRule(boolean b) {
		map_panel.setDrawingRule(b);
	}
	
	public void setPositioningAnchors(boolean b){
		map_panel.setPositioningAnchors(b);
	}
	
	//GT DEMO
	public void setPositioningAccessPoints(boolean b){
		map_panel.setPositioningAccessPoints(b);
	}
	//

	public void updateRulesCombo() {
		quick_panel.updateRulesCombo();	
	}



	public void setQuickPanelMenuSelected(boolean b) {
		showParamMenuItem.setSelected(b);
		
	}
	
	public void setAnchorsPanelMenuSelected(boolean b) {
		showAnchorsMenuItem.setSelected(b);
		map_panel.setDrawingAnchors(b);
	}
	
	//GT DEMO
	public void setAccessPointsPanelMenuSelected(boolean b){
		showAccessPointsMenuItem.setSelected(b);
		map_panel.setDrawingAccessPoints(b);
	}
	//


	public void updateNodesCombo() {
		quick_panel.updateNodesCombo();		
	}

	public void updatePatientStatus(){
		quick_panel.updatePatientStatus();
	}

	public void updateAnchorTemperature(){
		quick_panel.updateAnchorTemperature();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void componentMoved(ComponentEvent e) {
		if(quick_panel.isAttached() && this.hasFocus()){
			quick_panel.setAttached(true);
		}
		
	}





	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}





	public void startFunc(String id) {
		// TODO Auto-generated method stub
		
	}





	public void stopFunc(String id) {
		// TODO Auto-generated method stub
		
	}


	public void showPopup(String nodeID){
		JOptionPane.showMessageDialog(this,  "Lost connection with node " + nodeID, "Warning", JOptionPane.WARNING_MESSAGE);
	}

	public void printAnchorPosition(AnchorNode anchor)
	{
		main_app.printAnchorPosition(anchor);
	}	
	

	

}
