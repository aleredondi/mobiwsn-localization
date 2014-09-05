
package client_applications.console;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;

import client_applications.localization.LauraManager;

import common.exceptions.FunctionalityException;
import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;
import common.exceptions.ResponseTimeoutException;

import remote_interfaces.functionality.Functionality;
import remote_interfaces.functionality.FunctionalityType;
import remote_interfaces.mote.*;
import remote_interfaces.result.ValueResult;
import remote_interfaces.sensor.*;
import remote_interfaces.*;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.*;
import java.lang.Thread;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
//import java.util.Date;
import java.util.Vector;

import javax.measure.Measure;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/**
 * The application's main frame.
 */
public class MobiWSNConsoleView extends FrameView implements NodeSelectionListener
{
	
	private final int PRESENCE_DELAY_TIMER_VALUE = 10; // in secondi
	
	TopologySubscriber top_sub;
	FunctionalitySubscriber f_sub;
	
	// Variables declaration - do not modify//GEN-BEGIN:variables

    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnDisconnect;
    private javax.swing.JButton btnRead;
    private javax.swing.JButton btnRefreshList;
    private javax.swing.JButton btnRefreshMote;
    private javax.swing.JButton btnStartGateway;
    private javax.swing.JButton btnStopGateway;
    private javax.swing.JPanel graphPanel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblGateway;
    private javax.swing.JLabel lblIsPANCoord;
    private javax.swing.JLabel lblIsPanCoordinatorValue;
    private javax.swing.JLabel lblIsReachable;
    private javax.swing.JLabel lblIsReachableValue;
    private javax.swing.JLabel lblMACAddrValue;
    private javax.swing.JLabel lblMacAddress;
    private javax.swing.JLabel lblMoteID;
    private javax.swing.JLabel lblMoteID1;
    private javax.swing.JLabel lblMoteID2;
    private javax.swing.JLabel lblMoteIDValue;
    private javax.swing.JLabel lblNwkAddr;
    private javax.swing.JLabel lblNwkAddrValue;
    private javax.swing.JLabel lblParentIDValue;
    private javax.swing.JLabel lblParentMoteId;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTree trNetworkTree;
    private javax.swing.JTextArea txtConsole;
    private javax.swing.JTextField txtGatewayURL;
    private JTabbedPane sensorPanel;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private int control = 1;
    private int firstClick = 0;
    private JDialog aboutBox;

    //INSERITO DA ME
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private javax.swing.JSeparator jSeparator2;
    private JLabel lblGroupManager;
    private JLabel lblGatewayGroups;
    private JLabel lblMoteGroups;
    private JLabel lblGroupName;
    private JLabel lblFunctionality;
    private JList groupList;
    private JButton addGroup;
    private JButton removeGroup;
    private JButton addElement;
    private JButton removeElement;
    private JButton createGroup;
    private JButton startFunctionality;
    private JButton stopFunctionality;
    private JButton loadStoredGroups;
    //private javax.swing.JButton startDailyProfileRecorder;
    private javax.swing.JButton startLAURAclient;
    private JRadioButton gatewayGroupRadioButton;
    private JRadioButton moteGroupRadioButton;
    private ButtonGroup groupRadioButton;
    private JComboBox groupsGatewayComboBox;
    private JComboBox groupsMoteComboBox;
    private JComboBox functionalityComboBox;
    private boolean GroupsFound=false;
    private DefaultListModel List;
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    
    //For the periodic refresh
   // private TimerTask refresh;     
   // private RefreshTimer refTimer; 
    
    // End of variables declaration//GEN-END:variables
    
    Mote selectedMote = null;
    Mote addingMote=null;
    WSNGateway selectedGateway = null;
    WSNGateway addingGateway=null;
    WSNGateway currentGateway = null;
    Vector<DrawNode> drawNodeList = null;
    DrawNode SelectedNode=null;
    Vector<DrawNode> drawNodeGroup;
    
    
    
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private ArrayList<String> gatewayGroupsNames = new ArrayList<String>();   
    private ArrayList<LocalGroup> moteGroups = new ArrayList<LocalGroup>();
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    
    public MobiWSNConsoleView(SingleFrameApplication app) {
        super(app);
           
        try {
			top_sub = new TopologySubscriber(this);
			f_sub = new FunctionalitySubscriber(this);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        initComponents();
        
        trNetworkTree.setRootVisible(true);
        trNetworkTree.putClientProperty("JTree.lineStyle", "Angled");
        
        this.lblMoteIDValue.setText("");
        this.lblNwkAddrValue.setText("");
        this.lblMACAddrValue.setText("");
        this.lblIsPanCoordinatorValue.setText("");
        this.lblParentIDValue.setText("");
        this.lblIsReachableValue.setText("");
        //this.lblGroupInformationValue.setText("");
        
        
        ((MapPanel)graphPanel).addNodeSelectionListener(this);
        
        drawNodeList = new Vector<DrawNode>();
        drawNodeGroup=new Vector<DrawNode>();
        drawNodeGroup=null;
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    
                }
            }
        });
    }
    
    
    public synchronized void nodeSelected(DrawNode selectedNode)
    {
    	SelectedNode=selectedNode;
        try
        {
            /*
             * show node information
             */
            if (selectedNode.getNodeType() == DrawNodeType.Mote)
            {
                this.lblMoteIDValue.setText(selectedNode.getMoteRef().getId());
                this.lblNwkAddrValue.setText(selectedNode.getMoteRef().getNetworkAddress().getString());
                this.lblMACAddrValue.setText(Short.toString(selectedNode.getMoteRef().getMACAddress()));
                this.lblIsPanCoordinatorValue.setText(selectedNode.getMoteRef().isPanCoordinator() ? "True" : "False");
                
                 if (selectedNode.getMoteRef().isPanCoordinator())
                    this.lblParentIDValue.setText(selectedNode.getGatewayRef().getName());
                else {
                	Mote mote = selectedNode.getMoteRef().getParentMote();
                	if ( mote != null )
                        this.lblParentIDValue.setText(mote.getId());
                }
                
                this.lblIsReachableValue.setText(selectedNode.getMoteRef().isReachable() ? "True" : "False");
                
                sensorPanel.removeAll();
                	
                for (Sensor theSensor : selectedNode.getMoteRef().getSensorList())
                {
                    //synchronized(theSensor) {
                	ValueResult valueRes;
					try {
						valueRes = theSensor.getValue( Measure.valueOf(new Double(100000), SI.SECOND) );
                	if (valueRes != null ) {
                	
                		Double value;
                		Unit unit;
            			DecimalFormat f = new DecimalFormat("#.##");
                		
                		if ( theSensor.getType() == SensorType.TEMPERATURE ) {
                			value = valueRes.getValue().doubleValue(SI.CELSIUS);
                			unit = SI.CELSIUS;
                		} else if ( theSensor.getType() == SensorType.LIGHT ) {
            				value = valueRes.getValue().doubleValue(SI.LUX);
            				unit = SI.LUX;
                		} else {
            				value = valueRes.getValue().doubleValue(valueRes.getValue().getUnit());
            				unit = valueRes.getValue().getUnit();
                		}
                			
              
                    JPanel sensor = new JPanel(new GridLayout(2,2));
                	JLabel lblType = new JLabel();
                	JLabel lblValue = new JLabel();
                	JLabel lblSensorType = new JLabel();
                	JLabel lblSensorValue = new JLabel();
                    
        			sensor.setBackground(new Color(0XDDDDDD));
        			sensorPanel.addTab("ID: "+theSensor.getId(), null, sensor, null);
        			lblType.setText(" Sensor Type:");
        			lblValue.setText(" Sensor Value:");
        			lblSensorType.setText(theSensor.getType().toString());
        			lblSensorValue.setText(f.format(value) + " " +unit.toString());
        			sensor.add(lblType);
        			sensor.add(lblSensorType);
        			sensor.add(lblValue);
        			sensor.add(lblSensorValue);
                    }

					} catch (MoteUnreachableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ResponseTimeoutException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 

                	try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
                
                selectedMote = selectedNode.getMoteRef();
                
                startFunctionality.setEnabled(true);
                stopFunctionality.setEnabled(true);
                btnRefreshMote.setEnabled(true);
                btnRead.setEnabled(true);
                currentGateway = selectedGateway;
                addingMote=selectedNode.getMoteRef();
                addingGateway=null;
                functionalityComboBox.setEnabled(true);
                functionalityComboBox.removeAllItems();

                if(selectedMote.getFunctionalityList().isEmpty()!=true){
                	for(int i=0;i<selectedMote.getFunctionalityList().size();i++)
        		        functionalityComboBox.addItem(selectedMote.getFunctionalityList().get(i).getType());
                	    startFunctionality.setEnabled(true);
                	    stopFunctionality.setEnabled(true);
                }
                else {
                	functionalityComboBox.setEnabled(false);
            	    startFunctionality.setEnabled(false);
            	    stopFunctionality.setEnabled(false);
                }

            }
            else
            {
                /*
                 * reset node information label 
                 */
                this.lblMoteIDValue.setText("");
                this.lblNwkAddrValue.setText("");
                this.lblMACAddrValue.setText("");
                this.lblIsPanCoordinatorValue.setText("");
                this.lblParentIDValue.setText("");
                this.lblIsReachableValue.setText("");
                
                sensorPanel.removeAll();
                btnRefreshMote.setEnabled(false);
                btnRead.setEnabled(false);
                startFunctionality.setEnabled(false);
                stopFunctionality.setEnabled(false);
                selectedMote = null;
                currentGateway = selectedGateway;
                addingMote=null;
                addingGateway=selectedNode.getGatewayRef();
                functionalityComboBox.setEnabled(false);
                functionalityComboBox.removeAllItems();
            }
        }
        catch (RemoteException rex)
        {
            txtConsole.append(rex.getMessage() + "\n");
        } catch (MiddlewareException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    public void nodeUnselected()
    {
       /*
        * reset node information label 
        */
        this.lblMoteIDValue.setText("");
        this.lblNwkAddrValue.setText("");
        this.lblMACAddrValue.setText("");
        this.lblIsPanCoordinatorValue.setText("");
        this.lblParentIDValue.setText("");
        this.lblIsReachableValue.setText("");
        
        sensorPanel.removeAll();

        selectedMote = null;
        SelectedNode=null;
        btnRefreshMote.setEnabled(false);
        btnRead.setEnabled(false);
        
    }
    
	public DrawNode getSelectedNode(){
		return SelectedNode;
	}

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = new JFrame();
            aboutBox = new MobiWSNConsoleAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MobiWSNConsoleApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    	
        mainPanel = new javax.swing.JPanel();
        btnRefreshList = new javax.swing.JButton();
        lblGateway = new javax.swing.JLabel();
        txtGatewayURL = new javax.swing.JTextField();
        btnConnect = new javax.swing.JButton();
        btnDisconnect = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        trNetworkTree = new javax.swing.JTree();
        jSeparator1 = new javax.swing.JSeparator();
        btnRead = new javax.swing.JButton();
        lblMoteID = new javax.swing.JLabel();
        lblNwkAddr = new javax.swing.JLabel();
        lblMacAddress = new javax.swing.JLabel();
        lblParentMoteId = new javax.swing.JLabel();
        lblIsPANCoord = new javax.swing.JLabel();
        lblIsReachable = new javax.swing.JLabel();
        lblMoteIDValue = new javax.swing.JLabel();
        lblNwkAddrValue = new javax.swing.JLabel();
        lblMACAddrValue = new javax.swing.JLabel();
        lblParentIDValue = new javax.swing.JLabel();
        lblIsPanCoordinatorValue = new javax.swing.JLabel();
        lblIsReachableValue = new javax.swing.JLabel();
        lblMoteID1 = new javax.swing.JLabel();
        sensorPanel = new javax.swing.JTabbedPane();
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        jSeparator2 = new javax.swing.JSeparator();
        lblGroupManager = new JLabel();
        lblGatewayGroups = new JLabel();
        lblMoteGroups = new JLabel();
        lblGroupName = new JLabel();
        lblFunctionality = new JLabel();
        addGroup = new JButton();
        removeGroup = new JButton();
        //startDailyProfileRecorder = new JButton();
        startLAURAclient = new JButton();
        addElement = new JButton();
        removeElement = new JButton();
        createGroup = new JButton();
        startFunctionality = new JButton();
        stopFunctionality = new JButton();
        loadStoredGroups = new JButton();
        gatewayGroupRadioButton = new JRadioButton();
        moteGroupRadioButton = new JRadioButton();
        groupRadioButton = new ButtonGroup();
        groupsGatewayComboBox = new JComboBox();
        groupsMoteComboBox = new JComboBox();
        functionalityComboBox = new JComboBox();
        List = new DefaultListModel();
        
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        
        
        
        lblMoteID2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtConsole = new javax.swing.JTextArea();
        btnRefreshMote = new javax.swing.JButton();
        btnStopGateway = new javax.swing.JButton();
        btnStartGateway = new javax.swing.JButton();  
        graphPanel = new MapPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
      
        final org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(client_applications.console.MobiWSNConsoleApp.class).getContext().getResourceMap(MobiWSNConsoleView.class);
        btnRefreshList.setText(resourceMap.getString("btnRefreshList.text")); // NOI18N
        btnRefreshList.setName("btnRefreshList"); // NOI18N
        btnRefreshList.setEnabled(false);
        btnRefreshList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshListMouseClicked(evt);
            }
        });
        mainPanel.add(btnRefreshList, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 140, 110, -1));

        lblGateway.setFont(new java.awt.Font("FreeSans", 0, 12));
        lblGateway.setText(resourceMap.getString("lblGateway.text")); // NOI18N
        lblGateway.setName("lblGateway"); // NOI18N
        mainPanel.add(lblGateway, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 10));

        //TODO: andrebbe utilizzato l'ip del manager passato come parametro nel run dell'applicazione 
        txtGatewayURL.setText(resourceMap.getString("txtGatewayURL.text")); // NOI18N        
        txtGatewayURL.setName("txtGatewayURL"); // NOI18N
        mainPanel.add(txtGatewayURL, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 340, -1));

        btnConnect.setText(resourceMap.getString("btnConnect.text")); // NOI18N
        btnConnect.setName("btnConnect"); // NOI18N
        btnConnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(final java.awt.event.MouseEvent evt) {
                btnConnectMouseClicked(evt);
                
                
                if(firstClick == 0){
                	btnStopGateway.setEnabled(true);
                	selectedGateway = MobiWSNConsoleApp.getApplication().gatewayList.get(0);
                	currentGateway = selectedGateway;
                    firstClick=1;
                }
                //Starting the timer
                if(control==1){
                //refreshList(evt);
                //refTimer.getTimer().schedule(refresh, 60000, 60000);
                btnStopGateway.setEnabled(true);
                }
                
                else {
                control = 1;
                btnStartGateway.setEnabled(true);
                }
            }
        });
        mainPanel.add(btnConnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, 110, -1));

        btnDisconnect.setText(resourceMap.getString("btnDisconnect.text")); // NOI18N
        btnDisconnect.setEnabled(false);
        btnDisconnect.setName("btnDisconnect"); // NOI18N
        btnDisconnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(final java.awt.event.MouseEvent evt) {
                btnDisconnectMouseClicked(evt);
                btnRefreshList.setEnabled(false);
                
                //Stopping the timer
              //  refTimer.getTimer().cancel();
            }
        });
        mainPanel.add(btnDisconnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 10, 110, -1));

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        trNetworkTree.removeAll();
        trNetworkTree.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        trNetworkTree.setModel(null);
        trNetworkTree.setName("treeNodeList"); // NOI18N
        trNetworkTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                trNetworkTreeValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(trNetworkTree);

        mainPanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 670, 110));

        jSeparator1.setName("jSeparator1"); // NOI18N
        mainPanel.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 800, 10));


        btnRead.setText(resourceMap.getString("btnRead.text")); // NOI18N
        btnRead.setName("btnRead"); // NOI18N
        btnRead.setEnabled(false);
        btnRead.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReadMouseClicked(evt);
            }
        });
        mainPanel.add(btnRead, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 565, 108, 24));

        lblMoteID.setFont(resourceMap.getFont("lblMoteID.font")); // NOI18N
        lblMoteID.setText(resourceMap.getString("lblMoteID.text")); // NOI18N
        lblMoteID.setName("lblMoteID"); // NOI18N
        mainPanel.add(lblMoteID, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 215, -1, 20));

        lblNwkAddr.setFont(resourceMap.getFont("lblMoteID.font")); // NOI18N
        lblNwkAddr.setText(resourceMap.getString("lblNwkAddr.text")); // NOI18N
        lblNwkAddr.setName("lblNwkAddr"); // NOI18N
        mainPanel.add(lblNwkAddr, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 240, -1, 20));

        lblMacAddress.setFont(resourceMap.getFont("lblMoteID.font")); // NOI18N
        lblMacAddress.setText(resourceMap.getString("lblMacAddress.text")); // NOI18N
        lblMacAddress.setName("lblMacAddress"); // NOI18N
        mainPanel.add(lblMacAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 265, -1, 20));

        lblParentMoteId.setFont(resourceMap.getFont("lblMoteID.font")); // NOI18N
        lblParentMoteId.setText(resourceMap.getString("lblParentMoteId.text")); // NOI18N
        lblParentMoteId.setName("lblParentMoteId"); // NOI18N
        mainPanel.add(lblParentMoteId, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 290, -1, 20));

        lblIsPANCoord.setFont(resourceMap.getFont("lblMoteID.font")); // NOI18N
        lblIsPANCoord.setText(resourceMap.getString("lblIsPANCoord.text")); // NOI18N
        lblIsPANCoord.setName("lblIsPANCoord"); // NOI18N
        mainPanel.add(lblIsPANCoord, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 315, -1, 20));

        lblIsReachable.setFont(resourceMap.getFont("lblMoteID.font")); // NOI18N
        lblIsReachable.setText(resourceMap.getString("lblIsReachable.text")); // NOI18N
        lblIsReachable.setName("lblIsReachable"); // NOI18N
        mainPanel.add(lblIsReachable, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 340, -1, 20));

        lblMoteIDValue.setBackground(resourceMap.getColor("lblMoteIDValue.background")); // NOI18N
        lblMoteIDValue.setText(resourceMap.getString("lblMoteIDValue.text")); // NOI18N
        lblMoteIDValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblMoteIDValue.setMinimumSize(new java.awt.Dimension(30, 30));
        lblMoteIDValue.setName("lblMoteIDValue"); // NOI18N
        mainPanel.add(lblMoteIDValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 215, 100, 20));

        lblNwkAddrValue.setText(resourceMap.getString("lblNwkAddrValue.text")); // NOI18N
        lblNwkAddrValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblNwkAddrValue.setMaximumSize(new java.awt.Dimension(114, 15));
        lblNwkAddrValue.setMinimumSize(new java.awt.Dimension(114, 15));
        lblNwkAddrValue.setName("lblNwkAddrValue"); // NOI18N
        lblNwkAddrValue.setPreferredSize(new java.awt.Dimension(114, 15));
        mainPanel.add(lblNwkAddrValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 240, 100, 20));

        lblMACAddrValue.setText(resourceMap.getString("lblMACAddrValue.text")); // NOI18N
        lblMACAddrValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblMACAddrValue.setName("lblMACAddrValue"); // NOI18N
        mainPanel.add(lblMACAddrValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 265, 100, 20));

        lblParentIDValue.setText(resourceMap.getString("lblParentIDValue.text")); // NOI18N
        lblParentIDValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblParentIDValue.setName("lblParentIDValue"); // NOI18N
        mainPanel.add(lblParentIDValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 290, 100, 20));

        lblIsPanCoordinatorValue.setText(resourceMap.getString("lblIsPanCoordinatorValue.text")); // NOI18N
        lblIsPanCoordinatorValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblIsPanCoordinatorValue.setName("lblIsPanCoordinatorValue"); // NOI18N
        mainPanel.add(lblIsPanCoordinatorValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 315, 100, 20));

        lblIsReachableValue.setText(resourceMap.getString("lblIsReachableValue.text")); // NOI18N
        lblIsReachableValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblIsReachableValue.setName("lblIsReachableValue"); // NOI18N
        mainPanel.add(lblIsReachableValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 340, 100, 20));

        /* - - - - - PREMARINI SCALISE - - - - - */

        lblMoteID1.setFont(resourceMap.getFont("lblMoteInformation.font")); // NOI18N   
        lblMoteID1.setText(resourceMap.getString("lblMoteInformation.text")); // NOI18N 
        lblMoteID1.setName("lblMoteInformation"); // NOI18N
        mainPanel.add(lblMoteID1, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 190, -1, -1));
           
        sensorPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        sensorPanel.setName("scrSensorPane"); // NOI18N
        mainPanel.add(sensorPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 430, 228, 130));
 
        lblMoteID2.setFont(resourceMap.getFont("lblSensorInformation.font")); // NOI18N 
        lblMoteID2.setText(resourceMap.getString("lblSensorInformation.text")); // NOI18N 
        lblMoteID2.setName("lblSensorInformation"); // NOI18N
        mainPanel.add(lblMoteID2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 405, -1, -1));
        
        jScrollPane3.setName("scrConsolePane"); // NOI18N
        jScrollPane3.setViewportView(txtConsole);
        mainPanel.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, 970, 50));
        
        txtConsole.setColumns(20);
        txtConsole.setRows(5);
        txtConsole.setName("txtConsole"); // NOI18N
        
        // GROUP MANAGER //
        jSeparator2.setName("jSeparator2"); // NOI18N
        jSeparator2.setOrientation(SwingConstants.VERTICAL);
        mainPanel.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(937, 10, 100, 590));
        
        //Label//
        lblGroupManager.setFont(resourceMap.getFont("lblMoteInformation.font"));  
        lblGroupManager.setText("Group Manager");
        mainPanel.add(lblGroupManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 125, -1, -1));
        
        lblGatewayGroups.setFont(resourceMap.getFont("lblMoteID.font"));  
        lblGatewayGroups.setText("Groups of Gateways");
        mainPanel.add(lblGatewayGroups, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 215, -1, -1)); 
        
        lblMoteGroups.setFont(resourceMap.getFont("lblMoteID.font"));  
        lblMoteGroups.setText("Groups of Motes");
        mainPanel.add(lblMoteGroups, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 240, -1, -1));
        
        lblGroupName.setFont(resourceMap.getFont("lblMoteInformation.font"));  
        lblGroupName.setText("Group Name:No Group Selected");
        mainPanel.add(lblGroupName, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 345, -1, -1));
        
  
        groupList = new JList(List);
        groupList.setFont(resourceMap.getFont("lblMoteID.font")); 
        groupList.setVisibleRowCount(-1);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.setLayoutOrientation(JList.VERTICAL);
        groupList.setEnabled(false);
        JScrollPane groupListScroller = new JScrollPane(groupList);
		groupListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		mainPanel.add(groupListScroller, new org.netbeans.lib.awtextra.AbsoluteConstraints(950,370,200,189));
        
        //COMBO BOX
  
		
		groupsGatewayComboBox.setEditable(false);
        groupsGatewayComboBox.setVisible(true);
        groupsGatewayComboBox.setEnabled(false);
        groupsGatewayComboBox.setName("groupsGatewayComboBox");
        //mostra i gw appartenenti al gruppo selezionato
        groupsGatewayComboBox.addActionListener(new java.awt.event.ActionListener() {
		      public void actionPerformed(ActionEvent actionEvent) {
		    	  
		    	  if(groupsGatewayComboBox.getSelectedItem()!=null)
		    	  {
		    	  String currentGatewayGroup = groupsGatewayComboBox.getSelectedItem().toString();
		    	  lblGroupName.setText("Group Name:"+currentGatewayGroup);
		    	  List.removeAllElements();
            	
		    	  try 
		    	  {
		    		  for(WSNGateway gwCounter : MobiWSNConsoleApp.getApplication().gatewayManager.getWSNGatewayGroup(currentGatewayGroup)){
		    			  List.addElement(gwCounter.getName());
		    		  }
		    		  if(List.isEmpty())
							groupList.setEnabled(false);
						else
							groupList.setEnabled(true);
		    		  
		    		 
					
		    	  } catch (RemoteException e) {
					
		    		  e.printStackTrace();
		    	  }
		    	  }
		    	  updateGroupSelected();
		    	  drawGroup();
		      }
		    });
        mainPanel.add(groupsGatewayComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(950,265,150,-1));
      
        groupsMoteComboBox.setEditable(false);
        groupsMoteComboBox.setVisible(true);
        groupsMoteComboBox.setEnabled(false);
        groupsMoteComboBox.setName("groupsMoteComboBox");
        groupsMoteComboBox.addActionListener(new java.awt.event.ActionListener() {
		      public void actionPerformed(ActionEvent actionEvent) {
		    	  
		    	  if(groupsMoteComboBox.getSelectedItem()!=null)
		    	  {
		    	
		    	  String currentMoteGroup = groupsMoteComboBox.getSelectedItem().toString();
		    	  lblGroupName.setText("Group Name:"+currentMoteGroup);
		    	  List.removeAllElements();
		    	  System.out.println("Cancellp la lista e la ricreo");
          	
		    	  try
		    	  {
		    		  boolean found = false;
		    		  LocalGroup ag = null;
			    	  System.out.println("Scorro i gruppi di mote");
		    		  for( LocalGroup tg : moteGroups )  {
				    	  System.out.println("Gruppo " + tg.getName() + "uguale a " + currentMoteGroup);		    			  
		    			  if ( tg.getName().equals(currentMoteGroup)) {
		    				  ag = tg;
		    				  found = true;
		    				  break;
		    			  }
		    		  }
		    		  
		    		  if ( found ) {
				    	  System.out.println("Trovato");
        				for(Mote moteCounter : ag.getElements())
            			  List.addElement(moteCounter.getId());
		    		  
		    		  if ( ag.isCreatedOnGateway())
		    			  groupList.setBackground(Color.WHITE);
		    		  else
		    			  groupList.setBackground(Color.RED);
		    		}
		    		  
					if(List.isEmpty())
						groupList.setEnabled(false);
					else
						groupList.setEnabled(true);
					
		    	  } catch (RemoteException e) {
					
		    		  e.printStackTrace();
		    	  }	
		    	  }
		    	  updateGroupSelected();
		    	  drawGroup();
		      }
		    });
        mainPanel.add(groupsMoteComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(950,265,150,-1));
        
        //BOTTONI
        addGroup.setEnabled(false);
        addGroup.setName("addGroup"); 
        addGroup.setText("Add Group");
        addGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(final java.awt.event.MouseEvent evt) {
            	final JFrame frameGroupName = new JFrame("Create a new group");
            	final JLabel lblTypeFrameGroupName = new JLabel ();
            	final JPanel panelGroupName = new JPanel();
            	final JLabel lblNameFrameGroupName = new JLabel ();
            	final JComboBox comboBoxFrameGroupName = new JComboBox();
            	final JTextArea textAreaFrameGroupName = new JTextArea();
            	final JButton okayFrameGroupName = new JButton();
            	final JButton cancelFrameGroupName = new JButton();
            	
            	
            	frameGroupName.setBounds(600, 600, 450, 200);
            	frameGroupName.setLocation(350, 200);
            	frameGroupName.setVisible(true);
            	frameGroupName.add(panelGroupName);
            	panelGroupName.setLayout(null);
            	
            	lblTypeFrameGroupName.setFont(resourceMap.getFont("lblMoteInformation.font"));
            	lblTypeFrameGroupName.setText("Select Group type:");
            	lblTypeFrameGroupName.setBounds(10,10,200,20);
            	panelGroupName.add(lblTypeFrameGroupName);
            	
            	lblNameFrameGroupName.setFont(resourceMap.getFont("lblMoteInformation.font"));
            	lblNameFrameGroupName.setText("Choose the group name:");
            	lblNameFrameGroupName.setBounds(10,50,200,20);
            	panelGroupName.add(lblNameFrameGroupName);
            	
            	comboBoxFrameGroupName.addItem("Group of Gateways");
            	comboBoxFrameGroupName.addItem("Group of Motes");
            	comboBoxFrameGroupName.setBounds(220,10,200,20);
            	panelGroupName.add(comboBoxFrameGroupName);
            	
            	textAreaFrameGroupName.setColumns(20);
            	textAreaFrameGroupName.setBounds(220,50,200,20);
            	panelGroupName.add(textAreaFrameGroupName);
            	
            	okayFrameGroupName.setText("OK");
            	okayFrameGroupName.setName("okayGroupName");
            	okayFrameGroupName.setBounds(50,100,130,30);
            	okayFrameGroupName.addMouseListener(new java.awt.event.MouseAdapter() {
            	final ArrayList<Mote> moteList = new ArrayList<Mote>();
            	final ArrayList<WSNGateway> gatewayList = new ArrayList<WSNGateway>();
                    public void mouseClicked(final java.awt.event.MouseEvent evt) {
                    	boolean addingGroupControl=true;
                    	
                    	if (textAreaFrameGroupName.getText().equals(""))
                    	{
                    		JFrame frameDialogue = new JFrame();
                    		JOptionPane.showMessageDialog(frameDialogue, "Please type a valid name");
                    	}
                    	
                    	else
                    	{
                    		removeGroup.setEnabled(true);
                    		addElement.setEnabled(true);
                    		removeElement.setEnabled(true);
                    		createGroup.setEnabled(true);
                    		//startDailyProfileRecorder.setEnabled(true);
                    		
                    		try {
                    			if (comboBoxFrameGroupName.getSelectedIndex() == 0){
                    				//gatewayList.add(selectedGateway); con questa istruzione aggiunge subito il gw selezionato al gruppo creato, ma se ne può fare a meno.. per i mote deve esere così!
                    				//restituisce vero se il gruppo è stato creato
                    				if(MobiWSNConsoleApp.getApplication().gatewayManager.createWSNGatewayGroup(textAreaFrameGroupName.getText(), gatewayList))
                    				{
                    					gatewayGroupsNames.add(textAreaFrameGroupName.getText());
                    					groupsGatewayComboBox.setEnabled(true);
                    					groupsGatewayComboBox.setVisible(true);
                    					groupsMoteComboBox.setVisible(false);
                    					gatewayGroupRadioButton.setSelected(true);
                    					groupsGatewayComboBox.addItem(textAreaFrameGroupName.getText());
                    					groupsGatewayComboBox.setSelectedIndex(gatewayGroupsNames.size()-1);
                    					frameGroupName.dispose();
                    				
                    				}
                    				else 
                    				{	
                    					final JFrame frameDialogue = new JFrame();
                    					JOptionPane.showMessageDialog(frameDialogue, textAreaFrameGroupName.getText() + " already exists!");
                    				}
                    			}
                    			else if (comboBoxFrameGroupName.getSelectedIndex() == 1)
                    			{
                    			//	for(Mote m:moteList)
                    				//	moteList.remove(m);
                    				if(addingMote!=null){
                    					
                        				moteList.add(addingMote); //IN QUESTO MODO FUNZIONA ADDELEM, MA UN NODO DEVE ESSERE SELEZIONATO!!!
										//METTERE A POSTO! deve essere possibile creare un gruppo anche senza nodi dentro..
                        				
                        				
                        				//if(MobiWSNConsoleApp.getApplication().gatewayManager.createMoteGroup(textAreaFrameGroupName.getText(), moteList))
                        				boolean exist = false;
                        				LocalGroup tempGroup = null;
                        				for( LocalGroup lg : moteGroups )
                        					if (lg.getName().equals(textAreaFrameGroupName.getText())) {
                        						exist = true;
                        						tempGroup = lg;
                        						break;
                        					}
                        						
                        				if (!exist) {
                        					System.out.println("Creo il gruppo " + textAreaFrameGroupName.getText());
                        					tempGroup = new LocalGroup(textAreaFrameGroupName.getText(), false);
                        					moteGroups.add(tempGroup);
                        				
                        					tempGroup.add(moteList);
                        				
                        					groupsMoteComboBox.setEnabled(true);
                        					groupsMoteComboBox.setVisible(true);
                        					groupsGatewayComboBox.setVisible(false);
                        					moteGroupRadioButton.setSelected(true);
                        					groupsMoteComboBox.addItem(textAreaFrameGroupName.getText());
                        					groupsMoteComboBox.setSelectedIndex(moteGroups.size()-1);
                        					/*if(!tempGroup.created)
                        						groupsMoteComboBox.setForeground(Color.RED);
                        					else
                        						groupsMoteComboBox.setForeground(Color.WHITE);*/
                        						
                        					frameGroupName.dispose();
                        				
                        				}
                        				else
                        				{
                        					final JFrame frameDialogue = new JFrame();
                        					JOptionPane.showMessageDialog(frameDialogue, textAreaFrameGroupName.getText() + " already exists!");
                   				
                            			//	moteList.add(selectedMote);
                        				}
                    				}
                    				else{
                    					final JFrame frameDialogue = new JFrame();
                    					JOptionPane.showMessageDialog(frameDialogue, "Error: select a mote before adding a new mote group!");
                    					frameGroupName.dispose();
                                		removeGroup.setEnabled(false);
                                		addElement.setEnabled(false);
                                		removeElement.setEnabled(false);
                                		createGroup.setEnabled(false);
                                		//startDailyProfileRecorder.setEnabled(false);
                                		addingGroupControl=false;
                    				}
                    				
                    			}
                    				
							} catch (RemoteException e) {
								
								e.printStackTrace();
							}

                    	}
                      if(addingGroupControl==true){
      		    	  updateGroupSelected();
    		    	  drawGroup();
                      }
                    }
                });
            	panelGroupName.add(okayFrameGroupName);
            	
           
            	cancelFrameGroupName.setText("Cancel");
            	cancelFrameGroupName.setName("cancelGroupName");
            	cancelFrameGroupName.setBounds(250,100,130,30);
            	cancelFrameGroupName.addMouseListener(new java.awt.event.MouseAdapter() {
                	
                        public void mouseClicked(final java.awt.event.MouseEvent evt) {
                        
                           	frameGroupName.dispose();
                  
                        }
                    });
            	panelGroupName.add(cancelFrameGroupName);
            	
            }
        });
        mainPanel.add(addGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 300, 120, -1));
        
        
        removeGroup.setEnabled(false);
        removeGroup.setName("removeGroup");
        removeGroup.setText("Remove Group");
        removeGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(final java.awt.event.MouseEvent evt) {
            	
            	
            	if(gatewayGroupRadioButton.isSelected())
            	{
            		
            		String currentGatewayGroup = groupsGatewayComboBox.getSelectedItem().toString();
            		
            		try 
            		{	//cancello tutti i gw del gruppo prima di cancellare il gruppo stesso
            			for(WSNGateway gwCounter : MobiWSNConsoleApp.getApplication().gatewayManager.getWSNGatewayGroup(currentGatewayGroup))
            				{
            					if(MobiWSNConsoleApp.getApplication().gatewayManager.removeWSNGatewayFromGroup(currentGatewayGroup,gwCounter))
            						
                        				List.removeElement(gwCounter.getName());
            				
            				}
            			if(MobiWSNConsoleApp.getApplication().gatewayManager.removeWSNGatewayGroup(currentGatewayGroup))
            			{
            				
               				groupsGatewayComboBox.removeItem(currentGatewayGroup);
            				
            				if(groupsGatewayComboBox.getSelectedItem()==null)
            				{
            					groupsGatewayComboBox.setEnabled(false);
            					removeGroup.setEnabled(false);
            					removeElement.setEnabled(false);
                    			addElement.setEnabled(false);
                    			createGroup.setEnabled(false);
                    			//startDailyProfileRecorder.setEnabled(false);
                    			lblGroupName.setText("Group Name:No Group Selected");
            				}
            			}
					} catch (RemoteException e) {
						
						e.printStackTrace();
					}
            		
            	}
            	
            	if(moteGroupRadioButton.isSelected())
            	{
            		
            		String currentMoteGroup = groupsMoteComboBox.getSelectedItem().toString();
            		
            		try 
            		{	//elimino i nodi del gruppo prima di eliminare il gruppo stesso
            		/*	for(Mote moteCounter : MobiWSNConsoleApp.getApplication().gatewayManager.getMoteGroup(currentMoteGroup))
            			{
            				if(MobiWSNConsoleApp.getApplication().gatewayManager.removeMoteFromGroup(currentMoteGroup,moteCounter))
            				List.removeElement(moteCounter.getId());
            			}*/
            			
            			//Lo rimuovo dalla lista locale
            			Iterator<LocalGroup> it = moteGroups.iterator();
            			LocalGroup tg = null;
            			
            			while(it.hasNext()) {
            				tg = it.next();
            				if (tg.getName().equals(currentMoteGroup))
            					break;
            			}
            			
            			//if(MobiWSNConsoleApp.getApplication().gatewayManager.removeMoteGroup(currentMoteGroup))
            			if ( tg != null )
            			{
            				if( tg.isCreatedOnGateway() ) {
            					if(MobiWSNConsoleApp.getApplication().gatewayManager.removeMoteGroup(currentMoteGroup)) {
            				
            						List.removeAllElements();
            						removeElement.setEnabled(false);
            				
            						groupsMoteComboBox.removeItem(currentMoteGroup);
            						moteGroups.remove(tg);
            				
            						if(groupsMoteComboBox.getSelectedItem()==null)
            						{
            							groupsMoteComboBox.setEnabled(false);
            							removeElement.setEnabled(false);
            							addElement.setEnabled(false);
            							createGroup.setEnabled(false);
            							removeGroup.setEnabled(false);
            							//startDailyProfileRecorder.setEnabled(false);
            							lblGroupName.setText("Group Name:No Group Selected");
            						}
            					} else {
            						System.out.println("Errore non doveva succedere");
            					}
            				}

            			}
					} catch (RemoteException e) {
						
						e.printStackTrace();
					}
            		
            	}
		    	  updateGroupSelected();
		    	  drawGroup();
 
            }
        });
        
        lblGroupName.setText("Group Name:No Group Selected");
        mainPanel.add(removeGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 300, 120, -1));
        
  
        addElement.setEnabled(false);
        addElement.setName("addElement");
        addElement.setText("Add Elem");
        addElement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(final java.awt.event.MouseEvent evt) {
            	
						        
            	if(gatewayGroupRadioButton.isSelected())
            	{
            		String currentGatewayGroup = groupsGatewayComboBox.getSelectedItem().toString();
            		if(addingGateway!=null){
                		try {
                			
    						if(MobiWSNConsoleApp.getApplication().gatewayManager.addWSNGatewayToGroup(currentGatewayGroup, addingGateway))
    						{
    							groupList.setEnabled(true);
    							List.addElement(addingGateway.getName());
    								
    						}
    						else
    						{
    							JFrame frameDialogue = new JFrame();
            					JOptionPane.showMessageDialog(frameDialogue, "The gateway "+ addingGateway.getName() + " is already in the group '" + currentGatewayGroup + "'!");
    						}
    					} 
                		
                		catch (RemoteException e) {
    						
    						e.printStackTrace();
    					}
            		}
            		else{
            			JFrame frameDialogue = new JFrame();
    					JOptionPane.showMessageDialog(frameDialogue, "The selected node is not a Gateway!");
					}
            		
            	}
            	
            	if(moteGroupRadioButton.isSelected())
            	{
            		String currentMoteGroup = groupsMoteComboBox.getSelectedItem().toString();
            		
            		if(addingMote == null)
            		{
                		JFrame frameDialogue = new JFrame();
                		JOptionPane.showMessageDialog(frameDialogue, "The selected node is not a mote!");
                	}
            		
            		else
            		{
            			           		
            		try {
            			
						//if(MobiWSNConsoleApp.getApplication().gatewayManager.addMoteToGroup(currentMoteGroup, addingMote))
						Iterator<LocalGroup> it = moteGroups.iterator();
						LocalGroup tp = null;
						
						while( it.hasNext()) {
							tp = it.next();
							if( tp.getName().equals(currentMoteGroup) )
								break;
						}
							
            			if ( tp != null )
            			{
								if( tp.add(addingMote) ) {
									List.addElement(addingMote.getId());
									groupList.setEnabled(true);
									if ( tp.isCreatedOnGateway() ) {
										groupList.setBackground(Color.WHITE);
										if(!MobiWSNConsoleApp.getApplication().gatewayManager.addMoteToGroup(currentMoteGroup, addingMote)) {
											System.out.println("Errore nell'aggiungere il mote ai gruppi sul gateway");
											tp.remove(addingMote);
										}
									} else {
										groupList.setBackground(Color.RED);										
									}
								}
								else
								{
									final JFrame frameDialogue = new JFrame();
		        					JOptionPane.showMessageDialog(frameDialogue, "The mote "+ addingMote.getId() + " is already in the group '" + currentMoteGroup + "'!");
								}
						}
						else
						{
							final JFrame frameDialogue = new JFrame();
        					JOptionPane.showMessageDialog(frameDialogue, "The group "+ currentMoteGroup + " not exist in my arraylist !");
						}
					} 
            		
            		catch (RemoteException e) {
						
						e.printStackTrace();
					}
            		
            		
            	}
            	}
		    	  updateGroupSelected();
		    	  drawGroup();
 
            }
        });
        
        mainPanel.add(addElement, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 565, 120, -1));
        
        
        removeElement.setEnabled(false);
        removeElement.setName("removeElement");
        removeElement.setText("Remove Element");
        removeElement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(final java.awt.event.MouseEvent evt) {
 
            	
            	if(gatewayGroupRadioButton.isSelected())
            	{
            		String currentGatewayGroup = groupsGatewayComboBox.getSelectedItem().toString();
            		
            		try {
						if(MobiWSNConsoleApp.getApplication().gatewayManager.removeWSNGatewayFromGroup(currentGatewayGroup, selectedGateway))
						{
						
						   List.removeElement(selectedGateway.getName());
						   
						   if(List.isEmpty())
							   groupList.setEnabled(false);

						}
						else
						{
							final JFrame frameDialogue = new JFrame();
        					JOptionPane.showMessageDialog(frameDialogue, "The gateway "+ selectedGateway.getName()+" is not present in the group '"+currentGatewayGroup+"'!");
						}
					} 
            		
            		catch (RemoteException e) {
						
						e.printStackTrace();
					}
            		
            	}
            	
            	if(moteGroupRadioButton.isSelected())
            	{
            		String currentMoteGroup = groupsMoteComboBox.getSelectedItem().toString();
            		
            		if(selectedMote == null)
            		{
                		JFrame frameDialogue = new JFrame();
                		JOptionPane.showMessageDialog(frameDialogue, "Select a mote!");
                	}
            		
            		else {
            		try {
						//if(MobiWSNConsoleApp.getApplication().gatewayManager.removeMoteFromGroup(currentMoteGroup, selectedMote))
						Iterator<LocalGroup> it = moteGroups.iterator();
						LocalGroup tp = null;
						
						while( it.hasNext()) {
							tp = it.next();
							if( tp.getName().equals(currentMoteGroup) )
								break;
						}

						if ( tp != null )
						{
							if ( tp.remove(selectedMote) ) {
						
								List.removeElement(selectedMote.getId());
								if ( tp.isCreatedOnGateway() ) {
									if(!MobiWSNConsoleApp.getApplication().gatewayManager.removeMoteFromGroup(currentMoteGroup, selectedMote))
										System.out.println("Errore non sono riuscito a rimuovere l'oggetto dal gateway");
								}
							}
						   
						   if(List.isEmpty())
						   {
							   groupList.setEnabled(false);
							  //Elimina il gruppo se non c'è nessun mote dentro 
							   groupsMoteComboBox.removeItem(currentMoteGroup);
							   moteGroups.remove(tp);
	            				
	            				if(groupsMoteComboBox.getSelectedItem()==null)
	            				{
	            					groupsMoteComboBox.setEnabled(false);
	            					removeElement.setEnabled(false);
	            					addElement.setEnabled(false);
	            					createGroup.setEnabled(false);
	            					removeGroup.setEnabled(false);
	            					//startDailyProfileRecorder.setEnabled(false);
	            				}
							   
						   }

						}
						else
						{
							final JFrame frameDialogue = new JFrame();
        					JOptionPane.showMessageDialog(frameDialogue, "Impossible to delete the mote "+ selectedMote.getId() + " from the group " + currentMoteGroup + "!");
						}
					} 
            		
            		catch (RemoteException e) {
						
						e.printStackTrace();
					}
           
            	}
            	}
		    	  updateGroupSelected();
		    	  drawGroup();
            }
        });
        mainPanel.add(removeElement, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 565, 120, -1));
        
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@
        
        createGroup.setEnabled(false);
        createGroup.setName("createGroup");
        createGroup.setText("Create Group");
        createGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(final java.awt.event.MouseEvent evt) {
            	
            	if(moteGroupRadioButton.isSelected())
            	{
            		String currentMoteGroup = groupsMoteComboBox.getSelectedItem().toString();
            		
            		//Cerco l'array del gruppo
					Iterator<LocalGroup> it = moteGroups.iterator();
					LocalGroup tp = null;
					
					while( it.hasNext()) {
						tp = it.next();
						if( tp.getName().equals(currentMoteGroup) )
							break;
					}

					if ( tp != null ) {
						try {
							if(!MobiWSNConsoleApp.getApplication().gatewayManager.createMoteGroup( tp.getName(), tp.getElements()  ))
							{
								final JFrame frameDialogue = new JFrame();
								JOptionPane.showMessageDialog(frameDialogue, "Impossible to create the group " + currentMoteGroup + "!");
							} else {
								tp.setcreated();
								groupsMoteComboBox.setBackground(Color.WHITE);
								groupsMoteComboBox.setVisible(true);
							}
						} 
	            		catch (RemoteException e) {
							
							e.printStackTrace();
						} catch (HeadlessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
            		
           
            	}
            	else
            	{
            		final JFrame frameDialogue = new JFrame();
            		JOptionPane.showMessageDialog(frameDialogue, "Select a mote group!");
            	}
            	
            }
        });
        mainPanel.add(createGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 180, 180, -1));
        
        
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@
        
        //RADIO BUTTONS
        gatewayGroupRadioButton.setActionCommand("gateway");
        gatewayGroupRadioButton.setEnabled(false);
        gatewayGroupRadioButton.addMouseListener(new java.awt.event.MouseAdapter(){
        	public void mouseClicked(final java.awt.event.MouseEvent evt) {
        		 
        		
        		//Se non ci sono gruppi disabilita alcuni bottoni
        		if(groupsGatewayComboBox.getSelectedItem()==null) 
        		{
        			removeGroup.setEnabled(false);
        			removeElement.setEnabled(false);
        			addElement.setEnabled(false);
        			createGroup.setEnabled(false);
        			lblGroupName.setText("Group Name: No Group Selected");
        			List.removeAllElements();
        		}
        		else 
        		{
        			lblGroupName.setText("Group Name:" +groupsGatewayComboBox.getSelectedItem().toString());
        			
        			List.removeAllElements();
                  	
  		    	  
  		    		  
  		    		  try {
  		    			  for(WSNGateway gwCounter : MobiWSNConsoleApp.getApplication().gatewayManager.getWSNGatewayGroup(groupsGatewayComboBox.getSelectedItem().toString()))
  			    			  List.addElement(gwCounter.getName());
					} catch (RemoteException e) {
						
						e.printStackTrace();
					}
  				
        			removeElement.setEnabled(true);
        			addElement.setEnabled(true);
        			createGroup.setEnabled(true);
        			removeGroup.setEnabled(true);
        		}
        		
        		
        		groupsMoteComboBox.setVisible(false);
            	groupsGatewayComboBox.setVisible(true);
            	updateGroupSelected();
            	drawGroup();
            }
        
        });
        mainPanel.add(gatewayGroupRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 213, -1, -1));
        
        moteGroupRadioButton.setActionCommand("mote");
        moteGroupRadioButton.setEnabled(false);
        moteGroupRadioButton.addMouseListener(new java.awt.event.MouseAdapter(){
        	public void mouseClicked(final java.awt.event.MouseEvent evt) {
        		 
        		//Se non ci sono gruppi disabilita alcuni bottoni
        		if(groupsMoteComboBox.getSelectedItem()==null)
        		{
        			removeGroup.setEnabled(false);
           			removeElement.setEnabled(false);
        			addElement.setEnabled(false);
        			createGroup.setEnabled(false);
        			lblGroupName.setText("Group Name:No Group Selected");
        			List.removeAllElements();
        		}
        		
        		else 
        		{
        			lblGroupName.setText("Group Name:" +groupsMoteComboBox.getSelectedItem().toString());
        			List.removeAllElements();
                  	
  		    	 
  		    		  
  		    		  try {
  		    			  
  		    			boolean found = false;
  		    			LocalGroup ag = null;
  		    			  for( LocalGroup tg : moteGroups )
  		    				if( tg.getName().equals(groupsMoteComboBox.getSelectedItem().toString())) {
  		    					ag = tg;
  		    					found = true;
  		    					break;
  		    				}
						
  		    			  
  		    			  if( ag.isCreatedOnGateway())
  		    				groupsMoteComboBox.setBackground(Color.WHITE);
  		    			  else
    		    				groupsMoteComboBox.setBackground(Color.RED);
  		    				  
  		    			  for(Mote moteCounter : ag.getElements() )
							  List.addElement(moteCounter.getId());
  		    			  
					} catch (RemoteException e) {
						
						e.printStackTrace();
					}
					
        			removeElement.setEnabled(true);
        			addElement.setEnabled(true);
        			createGroup.setEnabled(true);
        			removeGroup.setEnabled(true);
        		}
        		
        		groupsMoteComboBox.setVisible(true);
            	groupsGatewayComboBox.setVisible(false);
            	updateGroupSelected();
            	drawGroup();
            }
        	
        	
        });
        mainPanel.add(moteGroupRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 238, -1, -1));
        
        groupRadioButton.add(gatewayGroupRadioButton);
        groupRadioButton.add(moteGroupRadioButton);

        /*
        startDailyProfileRecorder.setEnabled(false);
        startDailyProfileRecorder.setName("startDailyProfileRecorder");
        startDailyProfileRecorder.setText("Start Daily Profile Recorder");
        startDailyProfileRecorder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
               
            	startDailyProfileRecorderClicked(evt);
            }
        });
        mainPanel.add(startDailyProfileRecorder, new org.netbeans.lib.awtextra.AbsoluteConstraints(992, 609, 200, 50));
        */

        
        startLAURAclient.setEnabled(true);
        startLAURAclient.setName("startLAURAclient");
        startLAURAclient.setText("Start VIGILO demo");
        startLAURAclient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
               
            	startLAURAclient(evt);
            }
        });
        mainPanel.add(startLAURAclient, new org.netbeans.lib.awtextra.AbsoluteConstraints(992, 609, 200, 50));
        
        //Funzionalità
        startFunctionality.setEnabled(false);
        startFunctionality.setName("startFunctionality");
        startFunctionality.setText("Start");
        startFunctionality.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	
            	Functionality f= null;
            	try 
            	{
            		
            		for(int i=0;i<selectedMote.getFunctionalityList().size();i++)
            		{	
	            	    if(functionalityComboBox.getSelectedIndex()==i)
	            		f = selectedMote.getFunctionalityList().get(i);
						
            		}
            		
            		// addPayload è solo per testare lo start
            		// della funzionalità PRESENCE passandogli lo "start_payload"
            		//f.startFunctionality(addPayload(selectedMote.getMACAddress(), f.getType()));

            		if(f.getType() == FunctionalityType.PRESENCE)
            			setPresenceGroupInfo(f);
            		
            		else
            		{
						if ( f.hasPublisher() ) 
							f.getPublisher().addSubscriber(f_sub);
						else
							System.out.println("Non pubblica nulla");
							
            			f.startFunctionality(null);
            		}
            	}
            	catch (RemoteException e) 
				{
					e.printStackTrace();
				} catch (MiddlewareException e) {
					e.printStackTrace();
				} catch (FunctionalityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MoteUnreachableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	//rmiGenerator();
	   
            }

            /*private ArrayList<Object> addPayload(short address, FunctionalityType type) {
				
				if (type == FunctionalityType.PRESENCE)
				{
				
					ArrayList rooms = new ArrayList<String>();
				
					if (address == 1)
					{
						rooms.add("1"); //my_room
						
						rooms.add("2"); //near_room				
						rooms.add("3"); //near_room
						
						rooms.add("16384"); //master_addr 2
						rooms.add("24576"); //master_addr 3
						
						return rooms;
					}
					if (address == 3)
					{
						rooms.add("2"); //my_room
						
						rooms.add("1");     //near_room					
						rooms.add("3");     //near_room
						
						rooms.add("8192");  //master_addr 1
						rooms.add("24576"); //master_addr 3
						 
						return rooms;
					}
					if (address == 5)
					{
						rooms.add("3"); //my_room
						
						rooms.add("1");    //near_room					
						rooms.add("2");     //near_room
						
						rooms.add("8192"); //master_addr 1
						rooms.add("16384"); //master_addr 2
						
						return rooms;
					}
						
				}
				
				return null;
			}*/
			
        });
        mainPanel.add(startFunctionality, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 80, 120, -1));  	 
        
        lblFunctionality.setText("Mote functionality");
        lblFunctionality.setFont(resourceMap.getFont("lblMoteInformation.font"));  
        lblFunctionality.setName("lblFunctionality");
        mainPanel.add(lblFunctionality, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 10, 200, -1));
        

        mainPanel.add(functionalityComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 35, 150, -1));
        functionalityComboBox.setEnabled(false);
        
        stopFunctionality.setEnabled(false);
        stopFunctionality.setName("stopFunctionality");
        stopFunctionality.setText("Stop");
        stopFunctionality.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	Functionality f= null;
				try 
				{
            		for(int i=0;i<selectedMote.getFunctionalityList().size();i++)
            		{	
	            	    if(functionalityComboBox.getSelectedIndex()==i)
	            		f = selectedMote.getFunctionalityList().get(i);
            		}
						f.getPublisher().removeSubscriber(f_sub);
            			f.stopFunctionality();
					
				} 
				catch (RemoteException e) 
				{
					e.printStackTrace();
				} catch (MiddlewareException e) {
					e.printStackTrace();
				} catch (MoteUnreachableException e) {
					e.printStackTrace();
				} catch (FunctionalityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        mainPanel.add(stopFunctionality, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 80, 120, -1));
        
        
        
        //Stored Groups Recovery 
        loadStoredGroups.setEnabled(false);
        loadStoredGroups.setName("loadStoredGroups");
        loadStoredGroups.setText("Load Stored Groups");
        loadStoredGroups.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(java.awt.event.MouseEvent evt) {
            	getStoredGroups();
/*
	    		  try {
		    			  if(!MobiWSNConsoleApp.getApplication().gatewayManager.isWSNGatewayGroupsListEmpty()){
	                    		
		    				  removeGroup.setEnabled(true);
	                    	  addElement.setEnabled(true);
	                    	  removeElement.setEnabled(true);
	                    	  createGroup.setEnabled(true);
	                    	  startDailyProfileRecorder.setEnabled(true);
	                    	  boolean gatewayGroupFound;
	                    	  
	                    		
		    				  ArrayList<String> groupsNames=MobiWSNConsoleApp.getApplication().gatewayManager.getWSNGatewayGroupsNames();
		    				  for(int i=0;i<groupsNames.size();i++){
		    					 
		    					  gatewayGroupFound=false;
		    					  if(!gatewayGroupsNames.isEmpty()){
	  	        					for(int k=0;k<gatewayGroupsNames.size();k++){
	  	        						if(gatewayGroupsNames.get(k).equals(groupsNames.get(i)))
	  	        							gatewayGroupFound=true;
	  	        					}
		    					  }
		    					  if(!gatewayGroupFound){
		    					    gatewayGroupsNames.add(groupsNames.get(i));
		        					groupsGatewayComboBox.setEnabled(true);
		        					groupsGatewayComboBox.setVisible(true);
		        					groupsMoteComboBox.setVisible(false);
		        					gatewayGroupRadioButton.setSelected(true);
		        					groupsGatewayComboBox.addItem(groupsNames.get(i));
		        					groupsGatewayComboBox.setSelectedIndex(gatewayGroupsNames.size()-1);
		        					
		    					  }
		    				  }
		    			  }
	    		  } 
	    		  catch (RemoteException e) {
					e.printStackTrace();
	    		  }
	    		  
	    		  try {
	    			  if(!MobiWSNConsoleApp.getApplication().gatewayManager.isMoteGroupsListEmpty()){
                    		
	    				  removeGroup.setEnabled(true);
                    	  addElement.setEnabled(true);
                    	  removeElement.setEnabled(true);
                    	  createGroup.setEnabled(true);
                    	  startDailyProfileRecorder.setEnabled(true);
                    	  boolean moteGroupFound;
                    		
	    				  ArrayList<String> groupsNames=MobiWSNConsoleApp.getApplication().gatewayManager.getMoteGroupsNames();
	    				  for(int i=0;i<groupsNames.size();i++){
  	        					
	    					  moteGroupFound=false;
	    					  if(!moteGroups.isEmpty()){
	  	        					for(int k=0;k<moteGroups.size();k++){
	  	        						if(moteGroups.get(k).getName().equals(groupsNames.get(i))) {
	  	        							TemporaryGroup tp = moteGroups.get(k);
	  	        							tp.removeAll();
	  	        							
	  	        							//Aggiorno il gruppo locale
	  	        							ArrayList<Mote> gel = MobiWSNConsoleApp.getApplication().gatewayManager.getMoteGroup(tp.getName());
	  	        							tp.add(gel);
	  	        							tp.setcreated();
	  	        							
	  	        							moteGroupFound=true;
	  	        							break;
	  	        						}
	  	        					}
		    					  }
	    					  
	    					  if(!moteGroupFound){
        						TemporaryGroup tp = new TemporaryGroup(groupsNames.get(i), true);	    					    
	    						moteGroups.add(tp);
        						ArrayList<Mote> gel = MobiWSNConsoleApp.getApplication().gatewayManager.getMoteGroup(tp.getName());
        						tp.add(gel);

	    					    groupsMoteComboBox.setEnabled(true);
	        					groupsMoteComboBox.setVisible(true);
	        					groupsGatewayComboBox.setVisible(false);
	        					moteGroupRadioButton.setSelected(true);
	        					groupsMoteComboBox.addItem(groupsNames.get(i));
	        					groupsMoteComboBox.setSelectedIndex(moteGroups.size()-1);

	    					  }
	    				  }
	    			  }
    		  } 
    		  catch (RemoteException e) {
				e.printStackTrace();
    		  }*/
            }
        });
        mainPanel.add(loadStoredGroups, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 150, 180, -1));
        
        
        /* FINE PREMARINI SCALISE */
        
        btnRefreshMote.setText("Mote Discovery"); // NOI18N
        btnRefreshMote.setName("btnRefreshMote"); // NOI18N
        btnRefreshMote.setEnabled(false);
        btnRefreshMote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMoteMouseClicked(evt);
            }
        });
        mainPanel.add(btnRefreshMote, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 565, 108, 24));
        
        btnStopGateway.setText(resourceMap.getString("btnStopGateway.text")); // NOI18N
        btnStopGateway.setEnabled(false);
        btnStopGateway.setName("btnStopGateway"); // NOI18N
        btnStopGateway.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(final java.awt.event.MouseEvent evt) {
                btnStopGatewayMouseClicked(evt);
               control=0;
                // Stopping the timer
              //  refTimer.getTimer().cancel();
            }
        });
        mainPanel.add(btnStopGateway, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 100, 110, -1));

        	
        btnStartGateway.setText(resourceMap.getString("btnStartGateway.text")); // NOI18N
        btnStartGateway.setEnabled(false);
        btnStartGateway.setName("btnStartGateway"); // NOI18N
        btnStartGateway.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(final java.awt.event.MouseEvent evt) {
               
            	btnStartGatewayMouseClicked(evt);
            	control=1;
            	//Starting the timer
            	//refreshList(evt);
                //refTimer.getTimer().schedule(refresh, 60000, 60000);
            }
        });
        mainPanel.add(btnStartGateway, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 60, 110, -1));
        
        graphPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        graphPanel.setName("MobiWSNNetworkGraph");

        org.jdesktop.layout.GroupLayout graphPanelLayout = new org.jdesktop.layout.GroupLayout(graphPanel);
        graphPanel.setLayout(graphPanelLayout);
        graphPanelLayout.setHorizontalGroup(
            graphPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 666, Short.MAX_VALUE)
        );
        graphPanelLayout.setVerticalGroup(
            graphPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 416, Short.MAX_VALUE)
        );

        mainPanel.add(graphPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 670, 420));

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(client_applications.console.MobiWSNConsoleApp.class).getContext().getActionMap(MobiWSNConsoleView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);
       
        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents
    
    
    //############################
    
    /** This method creates the interface for the goup settings
     * @throws RemoteException 
	 * 
	 */
	 private void setPresenceGroupInfo(final Functionality f) throws RemoteException
	 {		 
		 
		  final ArrayList<String> group_name_list = MobiWSNConsoleApp.getApplication().gatewayManager.getMoteGroupsNames();	
		 
		  JButton btnOkay = new JButton("OK");;	  
		  ButtonGroup my_room_group = new ButtonGroup();	  
		  //ButtonGroup other_room_group = new ButtonGroup();	  
		  final ArrayList<JRadioButton> my_room_botton = new ArrayList<JRadioButton>();  
		  final ArrayList<JRadioButton> other_room_botton = new ArrayList<JRadioButton>();	  
		  
		  final JFrame mainPanel = new JFrame("Presence Group Info");	  
		  Container c = new Container();
		  JScrollPane my_groups_scroll = new JScrollPane(c);	  		  
	      
		  JLabel title = new JLabel("My Groups:");     
	      title.setFont(new Font("DejaVu Sans",Font.BOLD,15));
	      title.setBounds(10,10,300,20);
	      c.add(title);
	      	      
	      for(String group_name : group_name_list)
	      {
	    	  boolean find = false;
	    	  
	    	  JRadioButton rB = new JRadioButton(group_name);
	    	  rB.setName(group_name);
	    	  
	    	  ArrayList<Mote> group_motes_list = MobiWSNConsoleApp.getApplication().gatewayManager.getMoteGroup(group_name);
	    	  for(Mote m : group_motes_list)
	    	  {
	    		  if(selectedMote.getId().equals(m.getId()))
	    			  find=true;
	    	  }
	    	  
	    	  if(find)
	    	  {    			  
    			  my_room_group.add(rB);
    			  my_room_botton.add(rB);
    			  c.add(rB);
	    	  }
	    	  else
	    	  {
    			  //other_room_group.add(rB);
    			  other_room_botton.add(rB);    		  
	    	  }

	      }	
	      
		  JLabel title2 = new JLabel("Other Groups:");     
	      title2.setFont(new Font("DejaVu Sans",Font.BOLD,15));
	      title2.setBounds(10,10,300,20);
	      c.add(title2);
	      
	      for(JRadioButton other_botton : other_room_botton)
	    	  c.add(other_botton);
	      	      
      	  btnOkay.addMouseListener(new java.awt.event.MouseAdapter() {
      		public void mouseClicked(final java.awt.event.MouseEvent evt) {
      			
      			for(JRadioButton my_botton : my_room_botton) {
      				if(my_botton.isSelected()) {
      					
      					ArrayList<Object> rooms = new ArrayList<Object>();
      					//Presence delay timer
      					rooms.add(new Integer(PRESENCE_DELAY_TIMER_VALUE));
      					
      					rooms.add(my_botton.getName());
      					
      					for(JRadioButton other_botton : other_room_botton) {
      	      				if(other_botton.isSelected())       	      					
      	      					rooms.add(other_botton.getName());
      					}
      					
      					/*for(int i = 0 ; i< rooms.size(); i++)
      						System.out.println("\nrooms["+i+"]="+rooms.get(i));*/
      					
      					try {
      						
    						if ( f.hasPublisher() ) 
    							f.getPublisher().addSubscriber(f_sub);
    						else
    							System.out.println("Non pubblica nulla");      						
      						
							f.startFunctionality(rooms);
							
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (MiddlewareException e) {
							e.printStackTrace();
						} catch (FunctionalityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MoteUnreachableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
      					
      					//mainPanel.hide();
      					mainPanel.dispose();						
						
      					return;
      					
      				}
      			}
      		
      			final JFrame frameDialogue = new JFrame();
	    		JOptionPane.showMessageDialog(frameDialogue, "Select a group to set my room !");     		
      			
      		}
      	  });
      	
      	  c.add(btnOkay); 
      	  
      	  c.setLayout(new GridLayout(my_room_botton.size()+other_room_botton.size()+3,1));
      	        	  
		  mainPanel.getContentPane().add(my_groups_scroll);
		  mainPanel.setVisible(true);
		  mainPanel.setResizable(false);
		  mainPanel.setLocation(350, 170);
	    	
	      mainPanel.setSize(250, (my_room_botton.size()+other_room_botton.size())*50+150);  
      	  
	 }
	 //############################
    

private void btnRefreshListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshListMouseClicked
    
    try
    {  
        /*
         * send MoteDiscovery Message to gather information from sensor network
         */
        if (selectedGateway == null)
        {
            JOptionPane.showMessageDialog(
            					getFrame(), 
                                "Please, select a Gateway!",
                                "Message", 
                                JOptionPane.INFORMATION_MESSAGE
                                        );          
        } else {
        
        	if(selectedGateway.isStarted()) {
        	
        		selectedGateway.refreshMoteList();
        
        		/*
        		 * wait for MoteAnnouncement responses
        		 */
        		Thread.sleep(3000);
        
        		/*
        		 * update network tree view
        		 */
        		updateNetworkTree();
        		        
        		/*
        		 * refresh network graph
        		 */
        		updateNetworkGraph();

        	} else {
                JOptionPane.showMessageDialog(
    					getFrame(), 
                        "The selected Gateway is not started!",
                        "Message", 
                        JOptionPane.WARNING_MESSAGE
                                );         	
        	}
        }
        
    }   
    catch (Exception ex)
    {
       txtConsole.append("EXCEPTION: " + ex.getMessage() + "\n");
       txtConsole.append("STACKTRACE: "); 
       for (int iCont =0; iCont < ex.getStackTrace().length; iCont++)
       {
            txtConsole.append(ex.getStackTrace()[iCont].toString() + "\n"); 
       }
    }
}//GEN-LAST:event_btnRefreshListMouseClicked

private void btnConnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConnectMouseClicked
    try
    {      	
        System.setProperty("java.security.policy", "src/myjava.policy");
        /*
         * impostazione SecurityManager
         */
        System.setSecurityManager(new SecurityManager());
        
        /*
         * LookUp e ottenimento riferimento a oggetto remoto WSNGatewayManager
         */
        MobiWSNConsoleApp.getApplication().gatewayManager
                  = (WSNGatewayManager) Naming.lookup(txtGatewayURL.getText());
        
        
        /*
         * WSNGateway list lookup
         */
        MobiWSNConsoleApp.getApplication().gatewayList = 
                MobiWSNConsoleApp.getApplication().gatewayManager.getWSNGatewayList();
        
//Mia modifica
        Iterator<WSNGateway> aGateway =  MobiWSNConsoleApp.getApplication().gatewayList.iterator();
        while(aGateway.hasNext()) {
        	aGateway.next().addSubscriber(top_sub);
        }
                  

        if (MobiWSNConsoleApp.getApplication().gatewayList != null)
        {
           JOptionPane.showMessageDialog(getFrame(), "WSNGatewayManager lookup completed, " + 
                                               MobiWSNConsoleApp.getApplication().gatewayList.size() + " Gateways found!", 
                                               "Message", JOptionPane.INFORMATION_MESSAGE);
           
           btnRefreshList.setEnabled(true);
           btnDisconnect.setEnabled(true);
           btnConnect.setEnabled(false);
           addGroup.setEnabled(true);
           gatewayGroupRadioButton.setEnabled(true);
           moteGroupRadioButton.setEnabled(true);
           selectedGateway = currentGateway;
           addGroup.setEnabled(true);
           
           updateNetworkTree();
           
           updateNetworkGraph();
           
           try {
               if(!MobiWSNConsoleApp.getApplication().gatewayManager.isWSNGatewayGroupsListEmpty()){
            	   GroupsFound=true;
               }
           }    		
           catch (RemoteException e) {
				e.printStackTrace();
           }
           try {
               if(!MobiWSNConsoleApp.getApplication().gatewayManager.isMoteGroupsListEmpty()){
            	   GroupsFound=true;
               }
           }    		
           catch (RemoteException e) {
				e.printStackTrace();
           }
           if(GroupsFound==true){
        	   //loadStoredGroups.setEnabled(true);
        	   getStoredGroups();
           }
           
        	   
         
        }
        else
        {
            txtConsole.setText("Gateway not present in gatewayList...");
        }
            
    }
    catch (java.net.MalformedURLException muex)
    {
       txtConsole.append(muex.getMessage() + "\n");
    }
    catch(java.rmi.NotBoundException nbex)
    {
       txtConsole.append(nbex.getMessage() + "\n");
    }
    catch(java.rmi.RemoteException  rex)
    {
       txtConsole.append(rex.getMessage() + "\n");
    }
    catch(Exception ex)
    {
       txtConsole.append(ex.getMessage() + "\n");
    }

}//GEN-LAST:event_btnConnectMouseClicked

private void btnDisconnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDisconnectMouseClicked
    
    try
    {
        /*
         * release reference to WSNGateway and WSNGatewayManager
         */
        selectedGateway = null;
        selectedMote = null;
        
        if (JOptionPane.showConfirmDialog(getFrame(), "Disconnect from WSNGatewayManager?", "Message", 
               JOptionPane.OK_CANCEL_OPTION) 
                == 0)
        {
            MobiWSNConsoleApp.getApplication().gatewayList = null;
            MobiWSNConsoleApp.getApplication().gatewayManager = null;
            
            /*
             * release reference to WSNGateway and WSNGatewayManager
             */
            selectedGateway = null;
            selectedMote = null;
            
            btnConnect.setEnabled(true);
            btnDisconnect.setEnabled(false);
            btnStartGateway.setEnabled(false);
            btnStopGateway.setEnabled(false);
            btnRefreshList.setEnabled(false);
            addGroup.setEnabled(false);
            
            clearNetworkTree();              
            
        }

        
    }
    catch(Exception ex)
    {
       txtConsole.append(ex.getMessage() + "\n");
    }
}//GEN-LAST:event_btnDisconnectMouseClicked

private void trNetworkTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_trNetworkTreeValueChanged
    
    try
    {
        for (WSNGateway theGateway : MobiWSNConsoleApp.getApplication().gatewayList)
        {
            if (
                evt.getNewLeadSelectionPath().getLastPathComponent().toString().contains(theGateway.getName())
                )
            {
                if (!theGateway.isStarted())
                {
                    btnStartGateway.setEnabled(true);
                    btnStopGateway.setEnabled(false);
                }
                else
                {
                    btnStartGateway.setEnabled(false);
                    btnStopGateway.setEnabled(true);
                }
                
                selectedGateway = theGateway;
            }
        }
    }
    catch(Exception ex)
    {
       txtConsole.append(ex.getMessage() + "\n");
    }
    
}//GEN-LAST:event_trNetworkTreeValueChanged

private void btnReadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReadMouseClicked

    try
    {
        if (selectedMote == null)
            JOptionPane.showMessageDialog(getFrame(), "Select a Mote!", "Message", JOptionPane.INFORMATION_MESSAGE); 
        else
        {
            if (!selectedMote.isReachable())
                JOptionPane.showMessageDialog(getFrame(), "Select a reachable Mote!", "Message", JOptionPane.INFORMATION_MESSAGE); 
            else
            {
                if (selectedMote.getSensorList().isEmpty())
                    JOptionPane.showMessageDialog(getFrame(), "Select a mote with at least a sensor!", "Message", JOptionPane.INFORMATION_MESSAGE); 
                else
                {
                    Sensor firstSensor =  selectedMote.getSensorList().get(sensorPanel.getSelectedIndex());
                    ValueResult sensorValue;
					try {
						sensorValue = (ValueResult) firstSensor.getValue( new GregorianCalendar() );
                    
                    /*
                     * show the new value in the sensor table
                     */
                    if (sensorValue != null)
                    {   

                		Double value;
                		Unit unit;
            			DecimalFormat f = new DecimalFormat("#.##");
                		
                		if ( firstSensor.getType() == SensorType.TEMPERATURE ) {
                			value = sensorValue.getValue().doubleValue(SI.CELSIUS);
                			unit = SI.CELSIUS;
                		} else if ( firstSensor.getType() == SensorType.LIGHT ) {
            				value = sensorValue.getValue().doubleValue(SI.LUX);
            				unit = SI.LUX;
                		} else {
            				value = sensorValue.getValue().doubleValue(sensorValue.getValue().getUnit());
            				unit = sensorValue.getValue().getUnit();
                		}
                    	
                		//Integer value = sensorValue.getValue().intValue(sensorValue.getValue().getUnit());
                        int index = sensorPanel.getSelectedIndex();
                        
                        sensorPanel.remove(index);
                        
                        JPanel sensor = new JPanel(new GridLayout(2,2));
                    	JLabel lblType = new JLabel();
                    	JLabel lblValue = new JLabel();
                    	JLabel lblSensorType = new JLabel();
                    	JLabel lblSensorValue = new JLabel();
                        
            			sensor.setBackground(new Color(0XDDDDDD));
               			sensorPanel.add(sensor,null,index);
            			sensorPanel.setTitleAt(index, "ID: "+ firstSensor.getId());
            			lblType.setText(" Sensor Type:");
            			lblValue.setText(" Sensor Value:");
            			lblSensorType.setText( firstSensor.getType().toString() );
            			lblSensorValue.setText(f.format(value) + " " + unit.toString() );
            			sensor.add(lblType);
            			sensor.add(lblSensorType);
            			sensor.add(lblValue);
            			sensor.add(lblSensorValue);
            			sensorPanel.setSelectedIndex(index);
                        
                    } 

					} catch (MoteUnreachableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ResponseTimeoutException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                }
            }
        }
        
    }
    catch(java.rmi.RemoteException  rex)
    {
       txtConsole.append("EXCEPTION: " + rex.getMessage() + "\n");
       txtConsole.append("STACKTRACE: "); 
       for (int iCont = 0; iCont < rex.getStackTrace().length; iCont++)
       {
            txtConsole.append(rex.getStackTrace()[iCont].toString() + "\n"); 
       }
       
    } catch (MiddlewareException e) {
		e.printStackTrace();
	}
         
       
}//GEN-LAST:event_btnReadMouseClicked

private void btnRefreshMoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMoteMouseClicked
    try
    {
        if (selectedMote == null)
            JOptionPane.showMessageDialog(getFrame(), "Select a Mote!", "Message", JOptionPane.INFORMATION_MESSAGE); 
        else
        {
            if (!selectedMote.isReachable())
                JOptionPane.showMessageDialog(getFrame(), "Select a reachable Mote!", "Message", JOptionPane.INFORMATION_MESSAGE); 
            else
            {
                if (selectedMote.refreshData())
                {
                    updateNetworkTree();
                    updateNetworkGraph();
                        
                }
            }
        }
        
    }
    catch(MoteUnreachableException  muex)
    {
       txtConsole.append("EXCEPTION: " + muex.getMessage() + "\n");
       txtConsole.append("STACKTRACE: "); 
       for (int iCont = 0; iCont < muex.getStackTrace().length; iCont++)
       {
            txtConsole.append(muex.getStackTrace()[iCont].toString() + "\n"); 
       }
       
    }
    catch(java.rmi.RemoteException  rex)
    {
       txtConsole.append("EXCEPTION: " + rex.getMessage() + "\n");
       txtConsole.append("STACKTRACE: "); 
       for (int iCont = 0; iCont < rex.getStackTrace().length; iCont++)
       {
            txtConsole.append(rex.getStackTrace()[iCont].toString() + "\n"); 
       }
       
    }
}//GEN-LAST:event_btnRefreshMoteMouseClicked

private void btnStopGatewayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStopGatewayMouseClicked
    
    try
    {
        
        if (!selectedGateway.isStarted())
        {
            JOptionPane.showMessageDialog(getFrame(), 
                                          "Gateway " + selectedGateway.getName()  + " already stopped!", 
                                          "Message" , 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
            
        else
        {       	
            selectedGateway.stopInterface();
            btnStartGateway.setEnabled(true);
            btnStopGateway.setEnabled(false);
            
            updateNetworkTree();
        }
    }
    catch(java.rmi.RemoteException  rex)
    {
       txtConsole.append("EXCEPTION: " + rex.getMessage() + "\n");
       txtConsole.append("STACKTRACE: "); 
       for (int iCont = 0; iCont < rex.getStackTrace().length; iCont++)
       {
            txtConsole.append(rex.getStackTrace()[iCont].toString() + "\n"); 
       }
       
    }
}//GEN-LAST:event_btnStopGatewayMouseClicked

private void btnStartGatewayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStartGatewayMouseClicked
 	
    try
    {
        if (selectedGateway.isStarted())
        {
            JOptionPane.showMessageDialog(getFrame(), 
                                          "Gateway " + selectedGateway.getName()  + " already started!", 
                                          "Message" , 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
            
        else
        {
        	selectedGateway.startInterface();
           	btnStopGateway.setEnabled(true);
            btnStartGateway.setEnabled(false);
            
            updateNetworkTree();
            
        }
        
    }
    catch(java.rmi.RemoteException  rex)
    {
       txtConsole.append("EXCEPTION: " + rex.getMessage() + "\n");
       txtConsole.append("STACKTRACE: "); 
       for (int iCont = 0; iCont < rex.getStackTrace().length; iCont++)
       {
            txtConsole.append(rex.getStackTrace()[iCont].toString() + "\n"); 
       }
       
    }
        
}//GEN-LAST:event_btnStartGatewayMouseClicked

        
    private void updateNetworkTree()
    {
        // Begin treeView construction
        DefaultMutableTreeNode topNode = new DefaultMutableTreeNode("MobiWSN Network");
        
        if (MobiWSNConsoleApp.getApplication().gatewayManager == null)
        {
            JOptionPane.showMessageDialog(getFrame(), "Not connected to a WSNGatewayManager!", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try
        {
            /*
             * add tree nodes to show gateways
             */
            for(WSNGateway theGateway : MobiWSNConsoleApp.getApplication().gatewayList)
            {
                DefaultMutableTreeNode gatewayTreeNode;

                if (theGateway.isStarted())
                     gatewayTreeNode = new DefaultMutableTreeNode(theGateway.getName());
                else
                     gatewayTreeNode = new DefaultMutableTreeNode(theGateway.getName() + " (stopped) ");

                topNode.add(gatewayTreeNode);

                /*
                 * show mote list
                 */
                if (theGateway.isStarted())
                {
                    ArrayList<Mote> moteList = theGateway.getMoteList();

                    if (moteList == null) 
                    {
                        JOptionPane.showMessageDialog(getFrame(), 
                                                      " Gateway " + theGateway.getName() + 
                                                      " returned a null list of mote " , "Message", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    else
                    {
                        for (Mote theMote : moteList)
                        {
                            /*
                             * creating mote node
                             */
                            DefaultMutableTreeNode moteTreeNode;

                            if (theMote.isReachable())
                                 moteTreeNode = new DefaultMutableTreeNode(theMote.getId());
                            else
                                 moteTreeNode = new DefaultMutableTreeNode(theMote.getId() + " (unreachable)");


                            gatewayTreeNode.add(moteTreeNode);

                            /*
                             * creating sensor nodes
                             */
                            ArrayList<Sensor> sensorList = theMote.getSensorList();

                            if (sensorList == null) 
                            {
                                JOptionPane.showMessageDialog(getFrame(), 
                                                      " Mote " + theMote.getId() + 
                                                      " returned a null list of sensors " , "Message", JOptionPane.ERROR_MESSAGE);
                                continue;
                            }
                            else { 
                            	
                            	for (Sensor theSensor : sensorList)
                                {
                            		
                            		//txtConsole.append(theSensor.getId()); 
                            		
                                    DefaultMutableTreeNode sensorTreeNode;

                                  
                                    sensorTreeNode = new DefaultMutableTreeNode(" Sensor " + theSensor.getId());

                                    moteTreeNode.add(sensorTreeNode);
                                                                          
                                }                           	
                            	 
                            }
                            
                        }                       

                    }
                }
                                                
                DefaultTreeModel model = new DefaultTreeModel(topNode);
                                
                trNetworkTree.setModel(model);              
                               
            }
        }
        catch(java.rmi.RemoteException  rex)
        {
           txtConsole.append("EXCEPTION: " + rex.getMessage() + "\n");
           txtConsole.append("STACKTRACE: "); 
           for (int iCont =0; iCont < rex.getStackTrace().length; iCont++)
           {
                txtConsole.append(rex.getStackTrace()[iCont].toString() + "\n"); 
           }

        }
        
    }
    
    
    private void clearNetworkTree()
    {
        trNetworkTree.clearSelection();
        trNetworkTree.removeAll();       
        
        trNetworkTree.setModel(null);
        ((MapPanel)graphPanel).setMoteDatabaseAndRepaint(null);
        
    }
  

    
    private void updateNetworkGraph()
    {
        // DrawNode for WSNGatewayManager
        DrawNode gatewayManagerNode = new DrawNode();
        
        // list of DrawNode to be added to the network
        
        if (!findDrawNodeByNodeId(gatewayManagerNode))
            drawNodeList.add(gatewayManagerNode);
        	
                        
        try
        {
            /*
             * add tree nodes to show gateways
             */
            for(WSNGateway theGateway : MobiWSNConsoleApp.getApplication().gatewayList)
            {
                DrawNode gatewayNode = new DrawNode(theGateway);
                
                
                if (!findDrawNodeByNodeId(gatewayNode))
                    drawNodeList.add(gatewayNode);
                
                /*
                 * show mote list
                 */
                if (theGateway.isStarted())
                {
                    ArrayList<Mote> moteList = theGateway.getMoteList();

                    if (moteList == null) 
                    {
                        JOptionPane.showMessageDialog(getFrame(), 
                                                      " Gateway " + theGateway.getName() + 
                                                      " returned a null list of mote " , "Message", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    else
                    {
                        for (Mote theMote : moteList)
                        {
                            /*
                             * creating mote node
                             */
                            DrawNode moteNode = new DrawNode(theMote, theGateway);

                            if (!findDrawNodeByNodeId(moteNode))
                                drawNodeList.add(moteNode);
                        }

                    }
                }

            }
        }
        catch(java.rmi.RemoteException  rex)
        {
           txtConsole.append("EXCEPTION: " + rex.getMessage() + "\n");
           txtConsole.append("STACKTRACE: "); 
           for (int iCont =0; iCont < rex.getStackTrace().length; iCont++)
           {
                txtConsole.append(rex.getStackTrace()[iCont].toString() + "\n"); 
           }

        }
        
        ((MapPanel)graphPanel).setMoteDatabaseAndRepaint(drawNodeList);
                
    }
    
    private boolean findDrawNodeByNodeId(DrawNode searchNode)
    {
        try
        {
        for (DrawNode theNode : drawNodeList)
        {
            if (theNode.getNodeType() == searchNode.getNodeType())
            {
                if (
                    (theNode.getNodeType() == DrawNodeType.Mote) &&
                    (theNode.getMoteRef().getId().equals(searchNode.getMoteRef().getId())) &&
                    (theNode.getGatewayRef().getName().equals(searchNode.getGatewayRef().getName()))
                   )
                {
                    return true;
                }
                else if (
                         (theNode.getNodeType() == DrawNodeType.Gateway) &&
                         (theNode.getGatewayRef().getName().equals(searchNode.getGatewayRef().getName()))
                        )
                {
                    return true;
                }
                else if (
                         (theNode.getNodeType() == DrawNodeType.GatewayManager) 
                        )
                {
                    return true;
                }
            }
        }
        
        return false;
        }
        catch(java.rmi.RemoteException  rex)
        {
           txtConsole.append("EXCEPTION: " + rex.getMessage() + "\n");
           txtConsole.append("STACKTRACE: "); 
           for (int iCont =0; iCont < rex.getStackTrace().length; iCont++)
           {
                txtConsole.append(rex.getStackTrace()[iCont].toString() + "\n"); 
           }
           
           return false;

        }
    }
    
    /*Function called when the Connect or the StartGateway button is clicked
    private void refreshList(final java.awt.event.MouseEvent evt){
    	
    	refTimer = new RefreshTimer();
       	refresh = new TimerTask() {
            public void run() 
            {
            	btnRefreshListMouseClicked(evt);            
            }
    	};
        
   	
   }
     */

    private void startDailyProfileRecorderClicked(java.awt.event.MouseEvent evt) {
    	

    	//HomeManagement homeManagement = new HomeManagement (new ArrayList<String>(moteGroupsNames));
    	//homeManagement.initRooms();

    	/*
    	try {
    		ArrayList<String> moteGroupsName = new ArrayList<String>();
    		Iterator<LocalGroup> it = moteGroups.iterator();
			while(it.hasNext())
				moteGroupsName.add(it.next().getName());
    		
			
			ProfilingClient PROFILINGCLIENT = new ProfilingClient(moteGroupsName);
		}
    	catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	*/
    	//DailyProfileRecorder DAILYPROFILERECORDER = new DailyProfileRecorder (new ArrayList<String>(stringhe));
    	
    }

    
    private void startLAURAclient(java.awt.event.MouseEvent evt) {
    	
		try{		
			LauraManager me = new LauraManager();
		}catch (RemoteException e) {
			e.printStackTrace();
		}
		
    }
    
    /*
    public void rmiGenerator()
	{
		EventAlertImpl rmiObject= null;
	
		try
		{
						
			System.setSecurityManager(new SecurityManager());
			
			Thread.currentThread().setName("ClientOnlineManager_Thread");

			rmiObject = new EventAlertImpl();
			
			Naming.rebind("//" + MobiWSNConsoleApp.getApplication().my_ip + "/ClientManager", rmiObject);	
			
		}
		

		catch (RemoteException exc){System.out.println("Error in lookup: " + exc.toString());}
		catch (java.net.MalformedURLException exc){System.out.println("Malformed URL: " + exc.toString());}
		catch(Exception ex){System.out.println(ex.toString());}
	}
	*/
    
   /* public class OnlineManager  extends UnicastRemoteObject implements ClientEventInterface
    {

		protected OnlineManager() throws RemoteException 
		{}

		public void eventAllert(ArrayList<Object> input, int val) throws RemoteException 
		{
		
			switch(val)
			{
				case 1: 
					System.out.println("Sensor id: "+input.get(1) + ", mote id :"+ input.get(0) + ", value read: "+ input.get(2));
					break;
				
				case 2: 
					System.out.println("Numero persone: "+input.get(1) + " rilevate dal mote "+ input.get(0));
					break;
		
			}
		}
    }*/
    
	public void update(WSNGateway pub, ArrayList<Mote> code)
			throws RemoteException {
		/*
		 * update network tree view
		 */
		updateNetworkTree();
		        
		/*
		 * refresh network graph
		 */
		updateNetworkGraph();
		
	}
	public void updateGroupSelected(){
		
		drawNodeGroup=new Vector<DrawNode>();
		if(gatewayGroupRadioButton.isSelected())
    	{
			
			if(groupsGatewayComboBox.getSelectedItem()==null)
				drawNodeGroup=null;
			else{
	    		  try {
	    			  
	    			  if(!MobiWSNConsoleApp.getApplication().gatewayManager.getWSNGatewayGroup(groupsGatewayComboBox.getSelectedItem().toString()).isEmpty()){
		    			  for(WSNGateway gwCounter : MobiWSNConsoleApp.getApplication().gatewayManager.getWSNGatewayGroup(groupsGatewayComboBox.getSelectedItem().toString()))
		    				  drawNodeGroup.add(new DrawNode(gwCounter));
	    			  }
	    			  else
	    				  drawNodeGroup=null;
					} catch (RemoteException e) {
						
						e.printStackTrace();
					}	  
			}
    	}
		if(moteGroupRadioButton.isSelected())
    	{
			if(groupsMoteComboBox.getSelectedItem()==null)
				drawNodeGroup=null;
			else{
	    		  try {
	    			  
	    			  //Cerco nei local group
	    			  boolean found = false;
	    			  LocalGroup tempg = null;
    				  if (!groupsMoteComboBox.getSelectedItem().toString().isEmpty())
    					  for( LocalGroup lg : moteGroups )
	    					  if ( lg.getName().equals(groupsMoteComboBox.getSelectedItem().toString())) {
	    						  tempg = lg;
	    						  found = true;
	    					  }
	    			  
	    			  //if(!MobiWSNConsoleApp.getApplication().gatewayManager.getMoteGroup(groupsMoteComboBox.getSelectedItem().toString()).isEmpty()){
		    		if ( found ) {
						for(Mote moteCounter : tempg.getElements() ){
		    				  DrawNode p=new DrawNode(moteCounter,moteCounter.getWSNGatewayParent() );
		    				  drawNodeGroup.add(p);
		    			  }
	    			  }
	    			  else
	    				  drawNodeGroup=null;
		    			  } catch (RemoteException e) {
						
						e.printStackTrace();
					}	  
			}
    	}
		
	}
	

	
	public void drawGroup(){
		((MapPanel)graphPanel).setMoteDatabaseAndRepaint(drawNodeList);
	}

	public Vector<DrawNode> getGroup(){
		return drawNodeGroup;
	}
	
	
    private void getStoredGroups() {
		  try {
			  if(!MobiWSNConsoleApp.getApplication().gatewayManager.isWSNGatewayGroupsListEmpty()){
          		
				  removeGroup.setEnabled(true);
          	  addElement.setEnabled(true);
          	  removeElement.setEnabled(true);
          	  createGroup.setEnabled(true);
          	  //startDailyProfileRecorder.setEnabled(true);
          	  boolean gatewayGroupFound;
          	  
          		
				  ArrayList<String> groupsNames=MobiWSNConsoleApp.getApplication().gatewayManager.getWSNGatewayGroupsNames();
				  LocalGroup tempg = null;
				  
				  for(int i=0;i<groupsNames.size();i++){
					 
					  gatewayGroupFound=false;
					  if(!moteGroups.isEmpty()){
    					for(int k=0;k<moteGroups.size();k++){
    						if(moteGroups.get(k).getName().equals(groupsNames.get(i))) {
    							tempg = moteGroups.get(k);
    							gatewayGroupFound=true;
    							break;
    						}
    					}
					  }
					  
					  if(!gatewayGroupFound) {
						  //Se non lo trovo nella mia lista lo aggiungo
						 tempg = new LocalGroup(groupsNames.get(i), true);
						  
					  }
					  ArrayList<Mote> list = MobiWSNConsoleApp.getApplication().gatewayManager.getMoteGroup(groupsNames.get(i));
					  tempg.add(list);

					 /*groupsGatewayComboBox.setEnabled(true);
  					groupsGatewayComboBox.setVisible(true);
  					groupsMoteComboBox.setVisible(false);
  					gatewayGroupRadioButton.setSelected(true);
  					groupsGatewayComboBox.addItem(groupsNames.get(i));
  					groupsGatewayComboBox.setSelectedIndex(gatewayGroupsNames.size()-1);
  					
					  }*/
				  }
			  }
	  } 
	  catch (RemoteException e) {
		e.printStackTrace();
	  }
	  
	  try {
		  if(!MobiWSNConsoleApp.getApplication().gatewayManager.isMoteGroupsListEmpty()){
      		
		  removeGroup.setEnabled(true);
      	  addElement.setEnabled(true);
      	  removeElement.setEnabled(true);
      	  createGroup.setEnabled(true);
      	  //startDailyProfileRecorder.setEnabled(true);
      	  boolean moteGroupFound;
      		
			  ArrayList<String> groupsNames=MobiWSNConsoleApp.getApplication().gatewayManager.getMoteGroupsNames();
			  for(int i=0;i<groupsNames.size();i++){
					
				  moteGroupFound=false;
				  if(!moteGroups.isEmpty()){
    					for(int k=0;k<moteGroups.size();k++){
    						if(moteGroups.get(k).getName().equals(groupsNames.get(i))) {
    							LocalGroup tp = moteGroups.get(k);
    							tp.removeAll();
    							
    							//Aggiorno il gruppo locale
    							ArrayList<Mote> gel = MobiWSNConsoleApp.getApplication().gatewayManager.getMoteGroup(tp.getName());
    							tp.add(gel);
    							tp.setcreated();
    							
    							moteGroupFound=true;
    							break;
    						}
    					}
					  }
				  
				  if(!moteGroupFound){
					LocalGroup tp = new LocalGroup(groupsNames.get(i), true);	    					    
					moteGroups.add(tp);
					ArrayList<Mote> gel = MobiWSNConsoleApp.getApplication().gatewayManager.getMoteGroup(tp.getName());
					tp.add(gel);

				    groupsMoteComboBox.setEnabled(true);
					groupsMoteComboBox.setVisible(true);
					groupsGatewayComboBox.setVisible(false);
					moteGroupRadioButton.setSelected(true);
					groupsMoteComboBox.addItem(groupsNames.get(i));
					groupsMoteComboBox.setSelectedIndex(moteGroups.size()-1);

				  }
			  }
		  }
} 
catch (RemoteException e) {
	e.printStackTrace();
}
      	
      }


}
