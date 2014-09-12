package client_applications.localization.graphics;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import common.exceptions.FunctionalityException;
import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;

import client_applications.localization.AccessPoint;
import client_applications.localization.AnchorNode;
import client_applications.localization.IntrusionRule;
import client_applications.localization.LauraNode;
import client_applications.localization.LauraParam;
import client_applications.localization.MobileNode;
import remote_interfaces.clients.localization.Status;

public class QuickPanel extends JFrame implements ActionListener, ChangeListener, ComponentListener, KeyListener{

	JPanel quick_panel;
	JLabel alpha_c_label;
	JLabel alpha_u_label;
	JLabel cloud_thr_label;
	JComboBox selected_node_combo;
	JTextField selected_node_name;
	JTextField selected_node_status;
	JTextField selected_node_temp;
	JSlider alpha_c_slide;
	JSlider alpha_u_slide;
	JSlider cloud_thr_slide;
	JComboBox rules_combo;
	JComboBox p_rules_combo;
	JButton new_p_rule_btn;
	JButton remove_p_rule_btn;
	JCheckBox dg_check;
	LauraNode selected_node = null;
	JButton new_rule_btn;
	JButton remove_rule_btn;
	JButton set_param_btn;
	JButton set_anchor_pos_btn,set_access_point_pos_btn;
	JLabel anchor_x, access_point_x;
	JLabel anchor_y, access_point_y;
	JPanel anchor_param, access_point_param, access_point_network_address;
	JLabel network_address, access_point_mac, access_point_ip;

	JPanel img_panel;
	ImagePanel node_status_img_panel;



	JTabbedPane tabbed_pane;

	ArrayList<MobileNode> mobile_list;
	ArrayList<AnchorNode> anchor_list;
	//GT DEMO
	ArrayList<AccessPoint> access_points_list;
	LauraMainWindow lmw;

	Image anchor_on_icon, anchor_off_icon, ap_on_icon, ap_off_icon, patient_on_icon, patient_off_icon, access_point_on_icon, access_point_off_icon;
	Image standing, walking, prone,prone_red, supino, supino_red, check_falling,falling,noclass;
	boolean falled = false;

	//parametri da passare al manager
	boolean raw_on, is_attached;
	int weights = 1;
	double alpha_c, alpha_u, cloud_thr;
	ButtonGroup group;

	public QuickPanel(ArrayList<AnchorNode> anchor_list, ArrayList<MobileNode> mobile_list, ArrayList<AccessPoint> access_points_list, LauraMainWindow lmw){

		this.anchor_list = anchor_list;
		this.mobile_list = mobile_list;
		//GT DEMO
		this.access_points_list = access_points_list;
		//
		this.lmw = lmw;


		quick_panel = new JPanel();
		quick_panel.setLayout(new BoxLayout(quick_panel, BoxLayout.Y_AXIS));
		add(quick_panel);
		setVisible(true);
		setSize(230, 500);
		setTitle("Quick panel");
		addWindowListener(new WindowDestroyer());
		addComponentListener(this);
		//setResizable(false);

		//tabbed pane parametri / infos
		tabbed_pane = new JTabbedPane();

		//pannello info
		JPanel info_panel = new JPanel();
		info_panel.setLayout(new BoxLayout(info_panel, BoxLayout.Y_AXIS));

		//pannello parametri
		JPanel param_panel = new JPanel();
		param_panel.setLayout(new BoxLayout(param_panel, BoxLayout.Y_AXIS));

		//pannello regole
		JPanel rules_panel = new JPanel();
		rules_panel.setLayout(new BoxLayout(rules_panel, BoxLayout.Y_AXIS));

		//pannello intrusione
		JPanel intr_rules_panel = new JPanel();
		intr_rules_panel.setLayout(new BoxLayout(intr_rules_panel, BoxLayout.Y_AXIS));

		//pannello prossimità
		JPanel prox_rules_panel = new JPanel();
		prox_rules_panel.setLayout(new BoxLayout(prox_rules_panel, BoxLayout.Y_AXIS));

		//etichetta
		selected_node_combo = new JComboBox();
		selected_node_combo.setRenderer(new NodeCellRenderer());

		selected_node_name = new JTextField("");
		selected_node_name.addKeyListener(this);

		selected_node_status = new JTextField("");
		selected_node_status.setEditable(false);
		network_address = new JLabel("none");

		selected_node_temp= new JTextField("");
		selected_node_temp.setEditable(false);

		//pesi sulle ancore
		final String param_opt[] = {"off", "1", "2"};
		ActionListener weightsActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton aButton = (AbstractButton)actionEvent.getSource();
				if(aButton.getText().equals("1")) weights = 1;
				if(aButton.getText().equals("2")) weights = 2;
				if(aButton.getText().equals("off")) weights = 0;
			}
		};


		JPanel weight_panel = new JPanel(new GridLayout(1, 0));
		weight_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		weight_panel.setMaximumSize(new Dimension(230,100));
		weight_panel.setBorder(BorderFactory.createTitledBorder("Weights options"));
		group = new ButtonGroup();
		JRadioButton aRadioButton;
		for (int i=0; i<param_opt.length; i++) {
			aRadioButton = new JRadioButton (param_opt[i]);
			if(i==1) aRadioButton.setSelected(true);
			weight_panel.add(aRadioButton);
			group.add(aRadioButton);
			aRadioButton.addActionListener(weightsActionListener);
		}




		//attiva la visualizzazione discesa del gradiente
		dg_check = new JCheckBox("Raw estimate");	
		dg_check.setEnabled(false);

		//Seleziona i fattori alpha del modello dinamico
		alpha_c_label = new JLabel("alpha_c = 0.9");
		this.alpha_c = 0.9;
		alpha_c_slide = new JSlider(JSlider.HORIZONTAL,0,100,90);
		alpha_c_slide.setName("alpha_c");
		alpha_c_slide.addChangeListener(this);

		alpha_u_label = new JLabel("alpha_u = 0.5");
		this.alpha_u = 0.5;
		alpha_u_slide = new JSlider(JSlider.HORIZONTAL,0,100,50);
		alpha_u_slide.setName("alpha_u");
		alpha_u_slide.addChangeListener(this);

		cloud_thr_label = new JLabel("cloud_thr = 300");
		this.cloud_thr = 300;
		cloud_thr_slide = new JSlider(JSlider.HORIZONTAL,10,500,300);
		cloud_thr_slide.setName("cloud_thr");
		cloud_thr_slide.addChangeListener(this);



		set_param_btn = new JButton("Apply!");
		set_param_btn.setName("apply_param");
		set_param_btn.setEnabled(false);
		set_param_btn.addActionListener(this);


		//pannello principale
		selected_node_combo.setAlignmentX(LEFT_ALIGNMENT);
		selected_node_combo.setBorder(BorderFactory.createTitledBorder("Param for node:"));
		selected_node_combo.setMaximumSize(new Dimension(230,30));
		//		selected_node_combo.setRenderer(new NodeComboBoxRenderer());
		selected_node_combo.addItem("none");
		quick_panel.add(selected_node_combo);


		//tab 1 : info
		selected_node_name.setAlignmentX(LEFT_ALIGNMENT);
		selected_node_name.setBorder(BorderFactory.createTitledBorder("Target name:"));
		selected_node_name.setMaximumSize(new Dimension(230,50));
		selected_node_name.setBackground(getBackground());
		info_panel.add(selected_node_name);
		selected_node_status.setAlignmentX(LEFT_ALIGNMENT);
		selected_node_status.setBorder(BorderFactory.createTitledBorder("Target status:"));
		selected_node_status.setMaximumSize(new Dimension(230,50));
		selected_node_status.setBackground(getBackground());
		selected_node_status.setVisible(false);
		info_panel.add(selected_node_status);

		//GT DEMO - REMOVE TEMPERATURE
		/*selected_node_temp.setAlignmentX(LEFT_ALIGNMENT);
		selected_node_temp.setBorder(BorderFactory.createTitledBorder("Current temperature:"));
		selected_node_temp.setMaximumSize(new Dimension(230,50));
		selected_node_temp.setBackground(getBackground());
		selected_node_temp.setVisible(false);
		info_panel.add(selected_node_temp);*/

		//info_panel.add(network_address);


		//pannello parametri ancore
		anchor_param = new JPanel();
		anchor_param.setLayout(new BoxLayout(anchor_param, BoxLayout.X_AXIS));
		anchor_param.setBorder(BorderFactory.createTitledBorder("Anchor coord"));

		anchor_x = new JLabel("x: ");
		anchor_x.setMaximumSize(new Dimension(75,30));
		anchor_y = new JLabel("y: ");
		anchor_y.setMaximumSize(new Dimension(75,30));

		//pulsante per settare le coordinate
		set_anchor_pos_btn = new JButton("Set");
		set_anchor_pos_btn.setName("set anchor");
		set_anchor_pos_btn.addActionListener(this);
		set_anchor_pos_btn.setAlignmentX(RIGHT_ALIGNMENT);

		anchor_param.add(anchor_x);
		anchor_param.add(anchor_y);
		anchor_param.add(set_anchor_pos_btn);
		anchor_param.setVisible(false);
		anchor_param.setAlignmentX(LEFT_ALIGNMENT);
		anchor_param.setMaximumSize(new Dimension(230,50));
		info_panel.add(anchor_param);

		//GT DEMO
		//pannello parametri access point
		access_point_param = new JPanel();
		access_point_param.setLayout(new BoxLayout(access_point_param, BoxLayout.X_AXIS));
		access_point_param.setBorder(BorderFactory.createTitledBorder("Access Point coord"));

		access_point_x = new JLabel("x: ");
		access_point_x.setMaximumSize(new Dimension(75,30));
		access_point_y = new JLabel("y: ");
		access_point_y.setMaximumSize(new Dimension(75,30));

		//pulsante per settare le coordinate
		set_access_point_pos_btn = new JButton("Set");
		set_access_point_pos_btn.setName("set access point");
		set_access_point_pos_btn.addActionListener(this);
		set_access_point_pos_btn.setAlignmentX(RIGHT_ALIGNMENT);

		access_point_param.add(access_point_x);
		access_point_param.add(access_point_y);
		access_point_param.add(set_access_point_pos_btn);
		access_point_param.setVisible(false);
		access_point_param.setAlignmentX(LEFT_ALIGNMENT);
		access_point_param.setMaximumSize(new Dimension(230,50));
		info_panel.add(access_point_param);
		
		//Network address
		access_point_network_address = new JPanel();
		access_point_network_address.setLayout(new BoxLayout(access_point_network_address, BoxLayout.Y_AXIS));
		access_point_network_address.setBorder(BorderFactory.createTitledBorder("Details:"));
		access_point_mac = new JLabel("MAC: ");
		access_point_ip = new JLabel("IP: ");
		access_point_network_address.add(access_point_mac);
		access_point_network_address.add(access_point_ip);
		access_point_network_address.setMaximumSize(new Dimension(230,70));
		access_point_network_address.setAlignmentX(LEFT_ALIGNMENT);
		access_point_param.setVisible(false);
		access_point_network_address.setVisible(false);
		
		info_panel.add(access_point_network_address);
		//



		tabbed_pane.addTab("Info", null, info_panel, "node info");


		//tab 2: rules	

		//intrusione
		rules_combo = new JComboBox();
		rules_combo.setAlignmentX(LEFT_ALIGNMENT);
		rules_combo.setMaximumSize(new Dimension(200,30));
		rules_combo.addActionListener(new RuleComboSelection());

		intr_rules_panel.add(rules_combo);

		new_rule_btn = new JButton("New");
		new_rule_btn.setName("new_rule");
		new_rule_btn.addActionListener(this);

		remove_rule_btn = new JButton("Remove");
		remove_rule_btn.setName("remove_rule");
		remove_rule_btn.addActionListener(this);


		JPanel rule_btn_panel = new JPanel();
		rule_btn_panel.setLayout(new BoxLayout(rule_btn_panel, BoxLayout.X_AXIS));
		rule_btn_panel.add(new_rule_btn);
		rule_btn_panel.add(remove_rule_btn);
		rule_btn_panel.setAlignmentX(LEFT_ALIGNMENT);
		intr_rules_panel.add(rule_btn_panel);
		intr_rules_panel.setBorder(BorderFactory.createTitledBorder("Intrusion"));

		//prossimità
		p_rules_combo = new JComboBox();
		p_rules_combo.setAlignmentX(LEFT_ALIGNMENT);
		p_rules_combo.setMaximumSize(new Dimension(200,30));
		prox_rules_panel.add(p_rules_combo);
		prox_rules_panel.setBorder(BorderFactory.createTitledBorder("Proximity"));


		new_p_rule_btn = new JButton("New");
		new_p_rule_btn.setName("new_p_rule");
		new_p_rule_btn.addActionListener(this);

		remove_p_rule_btn = new JButton("Remove");
		remove_p_rule_btn.setName("remove_p_rule");
		remove_p_rule_btn.addActionListener(this);

		JPanel p_rule_btn_panel = new JPanel();
		p_rule_btn_panel.setLayout(new BoxLayout(p_rule_btn_panel, BoxLayout.X_AXIS));
		p_rule_btn_panel.add(new_p_rule_btn);
		p_rule_btn_panel.add(remove_p_rule_btn);
		p_rule_btn_panel.setAlignmentX(LEFT_ALIGNMENT);
		prox_rules_panel.add(p_rule_btn_panel);


		rules_panel.add(intr_rules_panel);
		rules_panel.add(prox_rules_panel);
		tabbed_pane.addTab("Rules", null, rules_panel, "node rules");

		//tab3: param
		param_panel.add(weight_panel);		
		dg_check.setAlignmentX(LEFT_ALIGNMENT);
		param_panel.add(dg_check);	

		alpha_c_label.setAlignmentX(LEFT_ALIGNMENT);
		param_panel.add(alpha_c_label);

		alpha_c_slide.setAlignmentX(LEFT_ALIGNMENT);
		param_panel.add(alpha_c_slide);

		alpha_u_label.setAlignmentX(LEFT_ALIGNMENT);
		param_panel.add(alpha_u_label);

		alpha_u_slide.setAlignmentX(LEFT_ALIGNMENT);
		param_panel.add(alpha_u_slide);

		cloud_thr_label.setAlignmentX(LEFT_ALIGNMENT);
		param_panel.add(cloud_thr_label);

		cloud_thr_slide.setAlignmentX(LEFT_ALIGNMENT);
		param_panel.add(cloud_thr_slide);

		set_param_btn.setAlignmentY(BOTTOM_ALIGNMENT);
		param_panel.add(set_param_btn);

		tabbed_pane.addTab("Parameters", null, param_panel, "node parameters");


		tabbed_pane.setAlignmentX(LEFT_ALIGNMENT);
		quick_panel.add(tabbed_pane);
		tabbed_pane.setEnabled(false);
		quick_panel.setVisible(true);

		//carica i nodi
		updateNodesCombo();
		selected_node_combo.addActionListener(new NodeComboSelection());

		//carica le icone per il menu
		Image aon=null,aoff=null,apon=null,apoff=null,pon=null,poff=null;
		try {
			String filename = "/client_applications/localization/graphics/anchor_on.png";
			URL url = this.getClass().getResource(filename);
			aon = ImageIO.read(url);
			filename = "/client_applications/localization/graphics/anchor_off.png";
			url = this.getClass().getResource(filename);
			aoff = ImageIO.read(url);
			//GT DEMO
			filename = "/client_applications/localization/graphics/anchor_on.png";
			url = this.getClass().getResource(filename);
			apon = ImageIO.read(url);
			filename = "/client_applications/localization/graphics/anchor_off.png";
			url = this.getClass().getResource(filename);
			apoff = ImageIO.read(url);
			//
			filename = "/client_applications/localization/graphics/mobile_on.png";
			url = this.getClass().getResource(filename);
			pon = ImageIO.read(url);
			filename = "/client_applications/localization/graphics/mobile_off.png";
			url = this.getClass().getResource(filename);
			poff = ImageIO.read(url);
			//figure per lo stato

			filename = "/client_applications/localization/graphics/standing_ok.jpg";
			url = this.getClass().getResource(filename);
			standing = ImageIO.read(url);
			filename = "/client_applications/localization/graphics/falling_ok.jpg";
			url = this.getClass().getResource(filename);
			falling = ImageIO.read(url);
			filename = "/client_applications/localization/graphics/check_falling_ok.jpg";
			url = this.getClass().getResource(filename);
			check_falling = ImageIO.read(url);
			filename = "/client_applications/localization/graphics/walking_ok.jpg";
			url = this.getClass().getResource(filename);
			walking = ImageIO.read(url);
			filename = "/client_applications/localization/graphics/prone_ok.jpg";
			url = this.getClass().getResource(filename);
			prone = ImageIO.read(url);
			filename = "/client_applications/localization/graphics/prone_ok_red.jpg";
			url = this.getClass().getResource(filename);
			prone_red = ImageIO.read(url);
			filename = "/client_applications/localization/graphics/supino_ok.jpg";
			url = this.getClass().getResource(filename);
			supino = ImageIO.read(url);
			filename = "/client_applications/localization/graphics/supino_ok_red.jpg";
			url = this.getClass().getResource(filename);
			supino_red = ImageIO.read(url);
			filename = "/client_applications/localization/graphics/noclass_ok.jpg";
			url = this.getClass().getResource(filename);
			noclass = ImageIO.read(url);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		anchor_on_icon = aon.getScaledInstance(32, 16, java.awt.Image.SCALE_SMOOTH);
		patient_on_icon =  pon.getScaledInstance(32, 16, java.awt.Image.SCALE_SMOOTH);
		ap_on_icon = apon.getScaledInstance(32, 16, java.awt.Image.SCALE_SMOOTH);
		anchor_off_icon = aoff.getScaledInstance(32, 16, java.awt.Image.SCALE_SMOOTH);
		patient_off_icon = poff.getScaledInstance(32, 16, java.awt.Image.SCALE_SMOOTH);
		ap_off_icon = apoff.getScaledInstance(32, 16, java.awt.Image.SCALE_SMOOTH);



		//devo farlo qua per motivi di caricamento
		node_status_img_panel = new ImagePanel(standing);
		node_status_img_panel.setAlignmentX(LEFT_ALIGNMENT);
		node_status_img_panel.setMaximumSize(new Dimension(170,225));
		node_status_img_panel.setBackground(getBackground());
		node_status_img_panel.setVisible(false);
		info_panel.add(Box.createRigidArea(new Dimension(5,10)));
		info_panel.add(node_status_img_panel);
	}

	//GESTIONE BOTTONI
	public void actionPerformed(ActionEvent e) {

		JButton source = (JButton)e.getSource();

		if(selected_node!=null){
			if(selected_node instanceof MobileNode){
				//ok dei parametri

				MobileNode node = (MobileNode)selected_node;



				if(source.getName()=="apply_param"){
					LauraParam param = new LauraParam();
					param.setAlpha_c(alpha_c);
					param.setAlpha_u(alpha_u);
					param.setCloud_thr(cloud_thr);
					//System.out.println("param: cloud_thr = " +cloud_thr);
					param.setWeights(weights);
					//System.out.println("setting:" + dg_check.isSelected());
					param.setRaw_on(dg_check.isSelected());  
					node.setParam(param);
				}

				//nuova regola di intusione
				if(source.getName()=="new_rule"){
					lmw.setDrawingRule(true);
					JOptionPane.showMessageDialog(lmw, "Draw a rule on the map or choose from below");
				}

				//rimuovi la regola di intrusione selezionata
				if(source.getName()=="remove_rule" && node.getIntrusionRules().size()>0){
					String name = (String)rules_combo.getSelectedItem();
					for(int i=0;i<node.getIntrusionRules().size();i++){
						IntrusionRule rule = node.getIntrusionRules().get(i);
						if(rule.getName().equals(name)){
							node.getIntrusionRules().remove(i);
							rules_combo.removeItem(rules_combo.getSelectedItem());
							updateRulesCombo();
							break;
						}
					}
				}

				//nuova regola di prossimità
				if(source.getName()=="new_p_rule"){
					if(mobile_list.size() == 1){
						JOptionPane.showMessageDialog(lmw, "No mobile nodes to apply proximity rule");
					}
					else{
						Object[] selection = new Object[mobile_list.size()-1];
						int j=0;
						for(int i=0;i<mobile_list.size();i++){
							if(!mobile_list.get(i).getId().equals(node.getId())){
								selection[j] = (Object)(mobile_list.get(i).getPatientId());
								j++;
							}
						}
						String patient_id =(String)JOptionPane.showInputDialog(lmw, "Select the node to check for proximity", "Proximity rule", JOptionPane.PLAIN_MESSAGE, null, selection, selection[0]);
						if(patient_id!=null){
							node.addProximityRule(patient_id);
							//aggiungi la regola anche all'altro nodo
							for(int i=0;i<mobile_list.size();i++){
								if(mobile_list.get(i).getPatientId().equals(patient_id)){
									mobile_list.get(i).addProximityRule(node.getPatientId());
								}
							}
							updateRulesCombo();
						}
					}
				}

				//rimuovi regola di prossimità selezionata
				if(source.getName()=="remove_p_rule" && node.getProximityRules().size()>0){
					String name = (String)p_rules_combo.getSelectedItem();
					for(int i=0;i<node.getProximityRules().size();i++){
						String rule = node.getProximityRules().get(i);
						if(rule.equals(name)){
							node.getProximityRules().remove(i);

							//ora però devo anche eliminare quella sull'altro nodo!
							for(int j=0;j<mobile_list.size();j++){
								if(mobile_list.get(j).getPatientId().equals(name)){							
									for(int ir=0;ir<mobile_list.get(j).getProximityRules().size();ir++){
										if(mobile_list.get(j).getProximityRules().get(ir).equals(node.getPatientId())){
											mobile_list.get(j).getProximityRules().remove(ir);

										}
									}
								}
							}
							p_rules_combo.removeItem(p_rules_combo.getSelectedItem());
							updateRulesCombo();
							break;
						}
					}
				}
				lmw.selectNode(node);

			}

			else if(source.getName()=="set anchor"){
				lmw.setPositioningAnchors(true);
				JOptionPane.showMessageDialog(lmw, "Click on the map");
			}


			else if(source.getName()=="set access point"){
				lmw.setPositioningAccessPoints(true);
				JOptionPane.showMessageDialog(lmw, "Click on the map");
			}


		}

		else{
			JOptionPane.showMessageDialog(lmw, "You must select a node.");
		}
	}

	//GESTIONE SLIDER
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if(source.getName().equals("alpha_c")){	
			alpha_c_label.setText("alpha_c = " + (double)source.getValue()/100);
			alpha_c = (double)source.getValue()/100;
			alpha_c_label.repaint();
		}
		if(source.getName() == "alpha_u"){
			alpha_u_label.setText("alpha_u = " + (double)source.getValue()/100);
			alpha_u = (double)source.getValue()/100;
			alpha_u_label.repaint();
		}
		if(source.getName() == "cloud_thr"){
			cloud_thr_label.setText("cloud_thr = " + (double)source.getValue());
			cloud_thr = (double)source.getValue();
			cloud_thr_label.repaint();
		}
	}

	public void selectNode(LauraNode selected_node){

		this.selected_node = selected_node;

		//seleziono un nodo 
		if(selected_node!=null){
			selected_node_name.setVisible(true);
			/*	try {
				network_address.setText(selected_node.getNetwork_address().getString());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 */	
			if(selected_node instanceof MobileNode){
				MobileNode node = (MobileNode)selected_node;
				tabbed_pane.setEnabled(true);
				tabbed_pane.setEnabledAt(0, true);
				tabbed_pane.setEnabledAt(1, true);
				tabbed_pane.setEnabledAt(2, true);
				set_param_btn.setEnabled(true);
				dg_check.setEnabled(true);
				anchor_param.setVisible(false);

				updatePatientStatus();
				selected_node_status.setVisible(true);
				//ACCROCCHIO RFID
				if(node.getPatientId().equals("Carrello"))
					node_status_img_panel.setVisible(false);
				else
					node_status_img_panel.setVisible(true);

				selected_node_temp.setVisible(false);

				//scrivi il nome dle nodo e del paziente (SELEZIONA DALLA COMBO)
				//if(!selected_node.getPatientId().equals(selected_node.getId()))
				//selected_node_label.setText(selected_node.getId() + " - " + selected_node.getPatientId());
				//else
				//selected_node_label.setText(selected_node.getId());
				for(int i=0;i<selected_node_combo.getItemCount();i++){
					if(selected_node_combo.getItemAt(i).equals(node.getPatientId())){
						selected_node_combo.setSelectedIndex(i);
					}
				}				

				selected_node_name.setText(node.getPatientId());
				selected_node_name.setBorder(BorderFactory.createTitledBorder("Target Name:"));
				selected_node_name.setEditable(true);
				selected_node_name.setBackground(this.getBackground());

				//carica i parametri
				alpha_c_slide.setValue((int)(100*node.getParam().getAlpha_c()));
				alpha_u_slide.setValue((int)(100*node.getParam().getAlpha_u()));
				cloud_thr_slide.setValue((int)(node.getParam().getCloud_thr()));
				//System.out.println("cloud_thr = " +node.getParam().getCloud_thr());
				//System.out.println("node stat:" + node.getParam().isRaw_on());
				dg_check.setSelected(node.getParam().isRaw_on());
				group.clearSelection();
				Enumeration<AbstractButton> eb = group.getElements();
				switch(node.getParam().getWeights()){
				case 1:
					for(int i=0;i<group.getButtonCount();i++){
						AbstractButton b = eb.nextElement();
						if (b.getText() == "1") b.setSelected(true);
					}
					break;
				case 2:
					for(int i=0;i<group.getButtonCount();i++){
						AbstractButton b = eb.nextElement();
						if (b.getText() == "2") b.setSelected(true);
					}
					break;
				default:
					for(int i=0;i<group.getButtonCount();i++){
						AbstractButton b = eb.nextElement();
						if (b.getText() == "off") b.setSelected(true);
					}
					break;
				}

				//carica le regole di intrusione selezionate
				rules_combo.removeAllItems();
				if(node.getIntrusionRules().size()>0){
					for(int i=0; i<node.getIntrusionRules().size();i++){
						rules_combo.addItem(node.getIntrusionRules().get(i).getName());
					}
				}
				else{
					rules_combo.addItem("no rules for this node");
				}

				//carica le regole di prossimità selezionate
				p_rules_combo.removeAllItems();
				if(node.getProximityRules().size()>0){
					for(int i=0; i<node.getProximityRules().size();i++){
						p_rules_combo.addItem(node.getProximityRules().get(i));
					}
				}
				else{
					p_rules_combo.addItem("no rules for this node");
				}

			}

			else if(selected_node instanceof AnchorNode){
				AnchorNode node = (AnchorNode)selected_node;
				selected_node_name.setText(node.getId());
				selected_node_name.setBorder(BorderFactory.createTitledBorder("Node ID:"));
				selected_node_name.setEditable(false);

				updateAnchorTemperature();
				selected_node_status.setVisible(false);
				node_status_img_panel.setVisible(false);
				selected_node_temp.setVisible(true);

				rules_combo.removeAllItems();

				tabbed_pane.setEnabled(true);
				tabbed_pane.setEnabledAt(0, true);
				tabbed_pane.setEnabledAt(1, false);
				tabbed_pane.setEnabledAt(2, false);
				tabbed_pane.setSelectedIndex(0);
				lmw.map_panel.setDrawingAnchors(true);
				lmw.setAnchorsPanelMenuSelected(true);
				for(int i=0;i<selected_node_combo.getItemCount();i++){
					if(selected_node_combo.getItemAt(i).equals(node.getId())){
						selected_node_combo.setSelectedIndex(i);
					}
				}

				//fai vedere il bottone per le coordinate
				DecimalFormat df = new DecimalFormat("##.##");
				anchor_x.setText("X: " + df.format(node.getX()) + " ");
				anchor_y.setText("Y: " + df.format(node.getY()) + " ");
				anchor_param.setVisible(true);
			}

			else if(selected_node instanceof AccessPoint){
				AccessPoint node = (AccessPoint)selected_node;
				selected_node_name.setText(node.getId());
				selected_node_name.setBorder(BorderFactory.createTitledBorder("Access Point ID:"));
				selected_node_name.setEditable(false);

				//updateAnchorTemperature();
				selected_node_status.setVisible(false);
				node_status_img_panel.setVisible(false);
				//selected_node_temp.setVisible(true);

				rules_combo.removeAllItems();

				tabbed_pane.setEnabled(true);
				tabbed_pane.setEnabledAt(0, true);
				tabbed_pane.setEnabledAt(1, false);
				tabbed_pane.setEnabledAt(2, false);
				tabbed_pane.setSelectedIndex(0);
				lmw.map_panel.setDrawingAccessPoints(true);
				lmw.setAccessPointsPanelMenuSelected(true);
				for(int i=0;i<selected_node_combo.getItemCount();i++){
					if(selected_node_combo.getItemAt(i).equals(node.getId())){
						selected_node_combo.setSelectedIndex(i);
					}
				}

				//fai vedere il bottone per le coordinate
				DecimalFormat df = new DecimalFormat("##.##");
				access_point_x.setText("X: " + df.format(node.getX()) + " ");
				access_point_y.setText("Y: " + df.format(node.getY()) + " ");
				access_point_param.setVisible(true);
				
				access_point_param.setVisible(true);
				access_point_mac.setText("MAC: " + node.getMACAddress());
				access_point_ip.setText("IP:     " + node.getIpAddress());
				access_point_network_address.setVisible(true);
				
				
			}

		}

		//schiaccio fuori 
		else{
			//selected_node_label.setText("none");
			tabbed_pane.setEnabled(false);
			selected_node_status.setVisible(false);
			rules_combo.removeAllItems();
			p_rules_combo.removeAllItems();
			set_param_btn.setEnabled(false);
			selected_node_combo.setSelectedIndex(0);
			anchor_param.setVisible(false);
			selected_node_temp.setVisible(false);
			node_status_img_panel.setImage(noclass);
			selected_node_name.setVisible(false);
			selected_node_name.setAlignmentX(LEFT_ALIGNMENT);
			
			access_point_param.setVisible(false);
			access_point_network_address.setVisible(false);
			
		}

		lmw.repaint();

	}

	public void selectRule(String rule){
		for(int i=0;i<rules_combo.getItemCount();i++){
			if(rules_combo.getItemAt(i).equals(rule)){
				rules_combo.setSelectedIndex(i);
			}
		}
	}

	public void updateNodesCombo(){
		selected_node_combo.removeAllItems();
		selected_node_combo.addItem("none");
		//aggiungi i nodi mobili
		for(int i=0;i<mobile_list.size();i++){
			selected_node_combo.addItem(mobile_list.get(i).getPatientId());
		}

		//aggiungi i nodi ancora
		for(int i=0;i<anchor_list.size();i++){
			selected_node_combo.addItem(anchor_list.get(i).getId());
		}

		//GT DEMO
		//add access points
		for(int i=0;i<access_points_list.size();i++){
			selected_node_combo.addItem(access_points_list.get(i).getId());
		}

		selected_node_combo.repaint();

	}

	public void updateRulesCombo(){
		//carica le regole di intrusione selezionate
		rules_combo.removeAllItems();
		if(selected_node instanceof MobileNode){
			MobileNode node = (MobileNode)selected_node;
			if(node.getIntrusionRules().size()>0){
				for(int i=0; i<node.getIntrusionRules().size();i++){
					rules_combo.addItem(node.getIntrusionRules().get(i).getName());
				}
			}
			else{
				rules_combo.addItem("no rules for this node");
			}

			//carica le regole di prossimità
			p_rules_combo.removeAllItems();
			if(node.getProximityRules().size()>0){
				for(int i=0; i<node.getProximityRules().size();i++){
					p_rules_combo.addItem(node.getProximityRules().get(i));
				}
			}
			else{
				p_rules_combo.addItem("no rules for this node");
			}

			lmw.repaint();
		}
	}

	public void updatePatientStatus(){
		if(selected_node!=null){
			//System.out.println("updating status" + selected_node.getStatus());
			selected_node_status.setText(selected_node.getStatus());
			if(selected_node.getStatus().equals(Status.STANDUP.toString()) || selected_node.getStatus().equals(Status.FLIPPED.toString())){
				node_status_img_panel.setImage(standing);
				falled = false;
				selected_node_status.setText("STAND UP");
				//System.out.println("standing");
			}

			if(selected_node.getStatus().equals(Status.MOVING.toString())){
				node_status_img_panel.setImage(walking);
				falled = false;
				//System.out.println("moving");
			}

			if(selected_node.getStatus().equals(Status.FACE_DOWN.toString())){
				if(!falled)
					node_status_img_panel.setImage(prone);
				else
					node_status_img_panel.setImage(prone_red);
			}

			if(selected_node.getStatus().equals(Status.FACE_UP.toString())){
				if(!falled)
					node_status_img_panel.setImage(supino);
				else
					node_status_img_panel.setImage(supino_red);
			}

			if(selected_node.getStatus().equals(Status.CHECK_FALL.toString()))
				node_status_img_panel.setImage(check_falling);

			if(selected_node.getStatus().equals(Status.FALL_ALARM.toString())){
				node_status_img_panel.setImage(falling);
				falled = true;
			}

			if(selected_node.getStatus().equals(Status.NO_CLASSIFICATION.toString()))
				node_status_img_panel.setImage(noclass);

		}
	}

	public void updateAnchorTemperature(){
		if(selected_node!=null){
			//System.out.println("updating temperature" + selected_node.getInstantTemp());
			selected_node_temp.setText(selected_node.getInstantTemp());
		}
	}

	public class WindowDestroyer extends WindowAdapter{

		public void windowClosing(WindowEvent e){
			lmw.setQuickPanelMenuSelected(false);
			lmw.adParamMenuItem.setEnabled(false);
		}
	}


	public boolean isAttached() {
		return is_attached;
	}

	public void setAttached(boolean b) {
		is_attached = b;
		if(is_attached){
			//this.setLocationRelativeTo(lmw);
			this.setLocation(lmw.getBounds().x + lmw.getWidth(),lmw.getBounds().y);
		}

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		if(is_attached && this.isFocused()){
			lmw.setLocation(this.getBounds().x - lmw.getWidth(), this.getBounds().y);
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

	public class NodeComboSelection implements ActionListener{


		public void actionPerformed(ActionEvent e) {
			boolean found = false;
			JComboBox combo = (JComboBox)e.getSource();

			if(combo.getItemCount()>0){
				//cerco nelle liste il nodo selezionato
				String label = (String)combo.getSelectedItem();
				for(int i=0;i<mobile_list.size();i++){			
					if(label.equals(mobile_list.get(i).getPatientId())){
						lmw.selectNode(mobile_list.get(i));
						found = true;
					}
				}
				if(!found){
					for(int i=0;i<anchor_list.size(); i++){
						if(label.equals(anchor_list.get(i).getId())){
							lmw.selectNode(anchor_list.get(i));
							found = true;
						}
					}
				}
				//GT DEMO
				if(!found){
					for(int i=0;i<access_points_list.size();i++){
						if(label.equals(access_points_list.get(i).getId())){
							lmw.selectNode(access_points_list.get(i));
							found = true;
						}
					}
				}
				//
				if(!found){
					lmw.selectNode(null);
				}

			}
		}

	}

	public class RuleComboSelection implements ActionListener{


		public void actionPerformed(ActionEvent e) {
			JComboBox combo = (JComboBox)e.getSource();

			if(combo.getItemCount()>0){
				MobileNode node = (MobileNode)lmw.selected_node;
				String label = (String)combo.getSelectedItem();
				for(int i=0;i<node.getIntrusionRules().size();i++){
					if(label.equals(node.getIntrusionRules().get(i).getName())){
						node.getIntrusionRules().get(i).setSelected(true);
					}
					else{
						node.getIntrusionRules().get(i).setSelected(false);
					}
				}
				lmw.repaint();
			}
		}

	}

	public class NodeCellRenderer implements ListCellRenderer{

		protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {


			Color theForeground = null;
			Icon theIcon         = null;
			String theText       = null;

			JLabel renderer = (JLabel)defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof String) {
				theForeground    = Color.black;
				theText          = (String)value;

				//cerca se il nodo è mobile o fisso
				if(!value.equals("none"))
					for(int i=0;i<mobile_list.size();i++){
						if(value.equals(mobile_list.get(i).getPatientId())){
							if(mobile_list.get(i).isIs_reachable() && patient_on_icon!=null){
								theForeground = new Color(0,205,0);
								theIcon = new ImageIcon(patient_on_icon);
							}
							else if(patient_off_icon!=null){
								theForeground = Color.red;
								theIcon = new ImageIcon(patient_off_icon);
							}
							if(!mobile_list.get(i).getPatientId().equals(mobile_list.get(i).getId()))
								theText = mobile_list.get(i).getId() + " - " + (String)value;  
						}
					}
				for(int i=0;i<anchor_list.size();i++){
					if(value.equals(anchor_list.get(i).getId())){

						if(anchor_list.get(i).isIs_reachable() && anchor_on_icon!=null){
							theForeground = new Color(0,205,0);
							theIcon = new ImageIcon(anchor_on_icon);
						}
						else if(anchor_off_icon!=null){
							theForeground = Color.red;
							theIcon = new ImageIcon(anchor_off_icon);
						}
					}
				}
				//GT DEMO
				for(int i=0;i<access_points_list.size();i++){
					if(value.equals(access_points_list.get(i).getId())){
						if(access_points_list.get(i).isIs_reachable() && ap_on_icon!=null){
							theForeground = new Color(0,205,0);
							theIcon = new ImageIcon(ap_on_icon);
						}
						else if(ap_off_icon!=null){
							theForeground = Color.red;
							theIcon = new ImageIcon(ap_off_icon);
						}
					}
				}
			} 

			if (isSelected) {
				renderer.setBackground(new Color(198,226,255));
			}
			if (theIcon != null) {
				renderer.setIcon(theIcon);
			}

			renderer.setForeground(theForeground);
			renderer.setText(theText);

			return renderer;
		}

	}


	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER 
				&& selected_node!=null 
				&& selected_node instanceof MobileNode){

			MobileNode node = (MobileNode)selected_node;					
			node.setPatient_id(selected_node_name.getText());
			updateNodesCombo();
			selected_node_combo.setSelectedItem(node.getPatientId());
		}
	}

	public void keyReleased(KeyEvent e) {

	}


	public void keyTyped(KeyEvent e) {

	}

	class ImagePanel extends JPanel {

		private Image img;

		public ImagePanel(String img) {
			this(new ImageIcon(img).getImage());
		}

		public ImagePanel(Image img) {
			this.img = img;
			Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
			setLayout(null);
		}

		public void setImage(Image img){
			this.img = img;		 
			this.repaint();
		}

		public void paintComponent(Graphics g) {
			g.drawImage(img, 0, 0, null);
			//System.out.println("Repainting");
		}

	}


}
