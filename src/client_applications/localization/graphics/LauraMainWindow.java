package client_applications.localization.graphics;

import client_applications.localization.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import greentouch.video.*;

public class LauraMainWindow extends JFrame implements ComponentListener{
	
	
	//manager
	LauraManager main_app;
	ArrayList<MobileNode> mobile_list;
	ArrayList<AnchorNode> anchor_list;
	//GT DEMO
	ArrayList<AccessPoint> access_points_list;
	ArrayList<UserEquipment> ue_list;
	LauraNode selected_node = null;
	 
	
	PowerMeterPanel pm_panel;
	MultiPowerMeterPanel mpm_panel;
	MultiAPHistoryPanel mahp_panel;
	UeInfoPanel ue_panel;
	GreenTouchVideoStreamingEndpoint gtvsep; 
	JPanel video_panel;
	
	//qua passerei i puntatori alle strutture dati dei nodi per disegnarli
	JPanel content;
	MapPanel map_panel; 
	QuickPanel quick_panel;
	JCheckBoxMenuItem showParamMenuItem;
	JCheckBoxMenuItem adParamMenuItem;
	JCheckBoxMenuItem showAnchorsMenuItem ;
	// GT DEMO
	JCheckBoxMenuItem showAccessPointsMenuItem;
	JCheckBoxMenuItem showPMPMenuItem;
	JCheckBoxMenuItem adPMPMenuItem;
	JCheckBoxMenuItem showAPHMenuItem;
	JCheckBoxMenuItem adAPHMenuItem;
	JCheckBoxMenuItem showMPMPMenuItem;
	JCheckBoxMenuItem adMPMPMenuItem;
	JCheckBoxMenuItem showUEPMenuItem;
	JCheckBoxMenuItem adUEPMenuItem;
	//
	JCheckBoxMenuItem showPatientNameMenuItem;
	JCheckBoxMenuItem showPatientRulesMenuItem;
	JCheckBoxMenuItem showWallsMenuItem;
	JMenuItem addTrackMenuItem;
	JCheckBoxMenuItem enableTrackMenuItem;
	JCheckBoxMenuItem showTrackMenuItem;
	

	public LauraMainWindow(LauraManager main_app, ArrayList<AnchorNode> anchor_list, ArrayList<MobileNode> mobile_list, ArrayList<AccessPoint> access_points_list, ArrayList<UserEquipment> ue_list){
		
		
		this.main_app = main_app;
		this.anchor_list = anchor_list;
		this.mobile_list = mobile_list;
		//GT DEMO
		this.access_points_list = access_points_list;
		this.ue_list = ue_list;
		//
		this.setTitle("GreenTouch DEMO");
		map_panel =  new MapPanel(anchor_list, mobile_list, access_points_list, this);
		
		//pannello comandi
		quick_panel = new QuickPanel(anchor_list, mobile_list, access_points_list, this);
		
		// create power meter panel
		pm_panel = new PowerMeterPanel(access_points_list, this);
		pm_panel.setVisible(true);
		
		// create multi power meter panel
		mpm_panel = new MultiPowerMeterPanel(access_points_list, this);
		mpm_panel.setVisible(true);
		
		//create multi ap history panel
		mahp_panel = new MultiAPHistoryPanel(access_points_list, this);
		mahp_panel.setVisible(true);
				
		// create UE info panel
		ue_panel = new UeInfoPanel(ue_list, this);
		ue_panel.setVisible(true);
		
		gtvsep = new GreenTouchVideoStreamingEndpoint();
		GreenTouchVideoStreamingEndpoint.displayIntoFrame(gtvsep);
		
		//video_panel = gtvsep.getMainPanel();
		//video_panel.setPreferredSize(new Dimension(500,500));
		//video_panel.setVisible(true);
		
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
		
		//View - show power meter panel
	    showPMPMenuItem = new JCheckBoxMenuItem("Show Power Meter Panel");
		showPMPMenuItem.addActionListener(menuListener);
		showPMPMenuItem.setSelected(true);
		viewMenu.add(showPMPMenuItem);
		
		//View - attach / detach power meter panel
	    adPMPMenuItem = new JCheckBoxMenuItem("Attach Power Meter Panel");
		adPMPMenuItem.addActionListener(menuListener);
		adPMPMenuItem.setSelected(false);
		viewMenu.add(adPMPMenuItem);
		
		viewMenu.add(new JSeparator());
		
		//View - show ap history panel
	    showAPHMenuItem = new JCheckBoxMenuItem("Show AP History Panel");
	    showAPHMenuItem.addActionListener(menuListener);
	    showAPHMenuItem.setSelected(true);
		viewMenu.add(showAPHMenuItem);
		
		//View - attach / detach show ap history panel
	    adAPHMenuItem = new JCheckBoxMenuItem("Attach AP History Panel");
	    adAPHMenuItem.addActionListener(menuListener);
	    adAPHMenuItem.setSelected(false);
		viewMenu.add(adAPHMenuItem);
		
		viewMenu.add(new JSeparator());
		
		//View - show power meter panel
	    showMPMPMenuItem = new JCheckBoxMenuItem("Show Multi Power Meter Panel");
		showMPMPMenuItem.addActionListener(menuListener);
		showMPMPMenuItem.setSelected(true);
		viewMenu.add(showMPMPMenuItem);
		
		//View - attach / detach power meter panel
	    adMPMPMenuItem = new JCheckBoxMenuItem("Attach Multi Power Meter Panel");
		adMPMPMenuItem.addActionListener(menuListener);
		adMPMPMenuItem.setSelected(false);
		viewMenu.add(adMPMPMenuItem);
		
		viewMenu.add(new JSeparator());
		
		//View - show ue info panel
	    showUEPMenuItem = new JCheckBoxMenuItem("Show UE Info Panel");
		showUEPMenuItem.addActionListener(menuListener);
		showUEPMenuItem.setSelected(true);
		viewMenu.add(showUEPMenuItem);
		
		//View - attach / detach ue info panel
	    adUEPMenuItem = new JCheckBoxMenuItem("Attach UE Info Panel");
		adUEPMenuItem.addActionListener(menuListener);
		adUEPMenuItem.setSelected(false);
		viewMenu.add(adUEPMenuItem);
		
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
		    
		   // power meter panel show 
		    if(actionEvent.getActionCommand() == "Show Power Meter Panel"){
		    	if(pm_panel.isVisible()){
		    		pm_panel.setVisible(false);
		    		adPMPMenuItem.setEnabled(false);
		    	}
		    	else {
		    		pm_panel.setVisible(true);
		    		adPMPMenuItem.setEnabled(true);
		    	}
		    }
		    
		    // power meter panel attach
		    if(actionEvent.getActionCommand() == "Attach Power Meter Panel"){
		    	if(pm_panel.isAttached()){
		    		pm_panel.setAttached(false);
		    	}
		    	else 
		    		pm_panel.setAttached(true);
		    }
		    
		 // multi power meter panel show 
		    if(actionEvent.getActionCommand() == "Show Multi Power Meter Panel"){
		    	if(mpm_panel.isVisible()){
		    		mpm_panel.setVisible(false);
		    		adMPMPMenuItem.setEnabled(false);
		    	}
		    	else {
		    		mpm_panel.setVisible(true);
		    		adMPMPMenuItem.setEnabled(true);
		    	}
		    }
		    
		    // multi power meter panel attach
		    if(actionEvent.getActionCommand() == "Attach Multi Power Meter Panel"){
		    	if(mpm_panel.isAttached()){
		    		mpm_panel.setAttached(false);
		    	}
		    	else 
		    		mpm_panel.setAttached(true);
		    }
		    
		    // ue info panel show 
		    if(actionEvent.getActionCommand() == "Show UE Info Panel"){
		    	if(ue_panel.isVisible()){
		    		ue_panel.setVisible(false);
		    		adUEPMenuItem.setEnabled(false);
		    	}
		    	else {
		    		ue_panel.setVisible(true);
		    		adUEPMenuItem.setEnabled(true);
		    	}
		    }
		    
		    // power meterue info panel attach
		    if(actionEvent.getActionCommand() == "Attach UE Info Panel"){
		    	if(ue_panel.isAttached()){
		    		ue_panel.setAttached(false);
		    	}
		    	else 
		    		ue_panel.setAttached(true);
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
	
	public void updateApHistoryPanel(){
		if(mahp_panel != null)
			mahp_panel.update();
	}
	
	public void updatePowerMeterPanel(){	
		if(pm_panel != null)
			pm_panel.update();
		if(mpm_panel != null)
			mpm_panel.update();
	}
	
	public void updateUeInfoPanel()
	{
		if(ue_panel != null)
			ue_panel.update();
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
		
		if(pm_panel.isAttached() && this.hasFocus()){
			pm_panel.setAttached(true);
		}
		
		if(mpm_panel.isAttached() && this.hasFocus()){
			mpm_panel.setAttached(true);
		}
		
		if(ue_panel.isAttached() && this.hasFocus()){
			ue_panel.setAttached(true);
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
