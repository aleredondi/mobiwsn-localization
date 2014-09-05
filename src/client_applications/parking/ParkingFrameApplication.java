
package client_applications.parking;


import remote_interfaces.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import client_applications.classi_remote.ParkFunctions;

import common.classes.FunctionList;
import common.classes.ObjectSearched;


public class ParkingFrameApplication extends JFrame 
{	
	private Toolkit toolkit;
	private FlowLayout layout;
	private JTextArea output;
	private JScrollPane scroll;
	private WSNGatewayManager remoteObject;
	private String group, city, url;
	private TreeStructure tree;
	private JPanel checkMenu;
	private JCheckBox noise, parking;
	private CityMap map;
	private ArrayList<WSNGateway> gwList;
	private int coordX, coordY;
	private boolean refreshEN, routeEN;
	private Timer refreshCar;
	private ArrayList<Park> parkList;
	private ParkFunctions pfa;
	
	private static String manager_ip;
	private static String my_ip;
	
	private static String images_path = "src/client_applications/parking/images/";
	
	public ParkingFrameApplication()
	{ 
		
		super("Search Parking"); //set interface name
		setSize(1024,800); //set the window size
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final Container c=this.getContentPane();
		
		
		//set the layout of the interface
		layout=new FlowLayout();
		c.setLayout(layout);
		
		//set the windows in the middle of the screen
		toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, 
		size.height/2 - getHeight()/2);

        
		output = new JTextArea(10, 50);
		output.setEditable(false);
		scroll = new JScrollPane(output);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(400,150));
		gwList=null;
		
//CREATE OBJECT
		pfa= new ParkFunctions();
//MAP PANEL
		
		ActionListener carMove = new ActionListener() 
        {
			//action associated to the text field
            public void actionPerformed(ActionEvent event)
            {
            	if(map.getRoute())
            	{	
            		map.setCarMove(true);
            		map.updateUI();
            		
            	}
            }
        };
        
		map= new CityMap();
		refreshEN=routeEN=false;
		coordX=193;
		coordY=157;
		refreshCar = new Timer(80, carMove);
		
		
		
//CHECK MENU		
		
		CheckBoxHandler chandler = new CheckBoxHandler();
		checkMenu=new JPanel();
		checkMenu.setLayout(new BoxLayout(checkMenu, BoxLayout.Y_AXIS));
		
	    parking=new JCheckBox("Car parks");
	    parking.setMnemonic(KeyEvent.VK_A);
	    
	    noise=new JCheckBox("Noise Areas");
	    noise.setMnemonic(KeyEvent.VK_B);
	    
	    parking.addItemListener(chandler);
        noise.addItemListener(chandler);
	    
	    checkMenu.add(parking);
	    checkMenu.add(noise);
		
	    parking.setVisible(false);
	    noise.setVisible(false);
		
	    
//BUTTON PANEL

	    JPanel buttonPanel= new JPanel();
		
		Icon routeIcon=new ImageIcon(images_path + "icons/buttonRoute.png");
		final JButton showRoute=new JButton(routeIcon);
		showRoute.setVisible(false);
		
		showRoute.addActionListener(new ActionListener() 
        {
			//action associated to the text field
            public void actionPerformed(ActionEvent event)
            {
            	int numPark = 0;
            	
            	if(routeEN)
            	{	
              		map.setRoute(true);
 
              		for (Park p:parkList) {
              			
              			numPark = numPark + p.getAvailablePark();
              		}
              		if (numPark > 0) {
              			
              			map.bestRoute();
               			map.updateUI();
               			refreshCar.restart();
               			
              		}
            	}
            }
        });
            
		Icon refreshIcon=new ImageIcon(images_path + "icons/buttonRefresh.png");
		final JButton refresh=new JButton(refreshIcon);
		refresh.setVisible(false);
		
		refresh.addActionListener(new ActionListener() 
        {
			//action associated to the text field
            public void actionPerformed(ActionEvent event)
            {
        	    showRoute.setVisible(true);
        	    parking.setVisible(true);
        	    try 
        	    {
            		Class typeIn[]={};
                    Object paramIn[]={};
                    Class typeInGw[]={String.class, String.class};
                    
            		for(WSNGateway gwGroup: gwList)
					{
						gwGroup.refreshMoteList();
						try 
						{
							Thread.sleep(2000);
						} 
						catch (InterruptedException e){e.printStackTrace();}
						
						Object paramInGw[]={gwGroup.getName(), url};
						gwGroup.useMethod(pfa.getClassIdentificationParameter(), "setGateway", typeInGw, paramInGw);
						gwGroup.useMethod(pfa.getClassIdentificationParameter(), "countParking", typeIn, paramIn);
						gwGroup.useMethod(pfa.getClassIdentificationParameter(), "verifyFreeParking", typeIn, paramIn);
				
					}
            		if(!refreshEN)
            		{
            			map.searchAllPath();
            			refreshEN=true;
            		}
            		
        	    } catch (RemoteException e) {output.append(e.toString());} 	    
        	    
            }
        });
		
		buttonPanel.add(refresh);
		buttonPanel.add(showRoute);

//TREE PANEL
		tree = new TreeStructure();
		parkList=new ArrayList<Park>();
				
		
//CONNECTION PANEL
		JPanel connectPanel= new JPanel();
		JLabel rmiLabel = new JLabel("Set rmi server address :   ");
		final JTextField rmiUrl = new JTextField(50);
		rmiUrl.setEnabled(false);
		       
		
		//set connection ok icon
		Icon check=new ImageIcon(images_path + "icons/check.png");
        final JLabel connectOK= new JLabel("Connected", check, SwingConstants.LEFT);
        connectOK.setForeground(new Color(41,130,11));
        connectOK.setVisible(false);
        
        //set connection wrong icon
        Icon error=new ImageIcon(images_path + "icons/error.png");
        final JLabel connectError= new JLabel("Connection Error", error, SwingConstants.LEFT);
        connectError.setForeground(new Color(200,0,0));       
        connectError.setVisible(false);
		
		rmiUrl.addActionListener(new ActionListener() 
        {
			//action associated to the text field
            public void actionPerformed(ActionEvent event)
            {
            	//Set the security manager for the client
        		System.setSecurityManager(new RMISecurityManager());
        		//get the remote object from the registry
        		try
        	    {
        			output.append("\nSecurity Manager loaded\n\n");
        			url=event.getActionCommand();
        	        remoteObject = (WSNGatewayManager)Naming.lookup(url);  
        	        output.append("Got remote object\n\n");    
        	        connectOK.setVisible(true);
        	        connectError.setVisible(false);
        	                	        
        	    }
        		catch (RemoteException exc)
        		{
        			output.append("Error in lookup: " + exc.toString());
        			connectError.setVisible(true);
        		}
        	    catch (java.net.MalformedURLException exc)
        	    {
        	    	output.append("Malformed URL: " + exc.toString());
        	    	connectError.setVisible(true);
        	    	connectOK.setVisible(false);
        	    }
        	    catch (java.rmi.NotBoundException exc)
        	    {
        	    	output.append("NotBound: " + exc.toString());
        	    	connectError.setVisible(true);
        	    	connectOK.setVisible(false);
        	    } 
        	    
        	    WSNGateway gw=null;
        	    try 
        	    {
        	    	gwList= remoteObject.getWSNGatewayGroup(group); 
        	    	gw = gwList.get(0);
        	    } 
        	    catch (RemoteException e) {output.append(e.toString());}
        	    catch(java.lang.NullPointerException e) {output.append("Group not found\n"+e.toString());}
        	    
        	    ArrayList<ObjectSearched> load = new  ArrayList<ObjectSearched>();
        	    ArrayList<ObjectSearched> save = new  ArrayList<ObjectSearched>();
        	    save.add(new ObjectSearched("ParkFunctions","Alessandro",1));
        	    save.add(new ObjectSearched("NoiseControl","Alessandro",1));
        	    
        	    FunctionList toload= new FunctionList(load);
        	    FunctionList tosave= new FunctionList(save);
        	    
                ClassManager cm=new ClassManager(my_ip, 5050, gw, "bin/client_applications/classi_remote/"); //"192.168.0.17"
        	    try 
        	    {
					cm.verifyGroupGatewayClass(group, toload, tosave);
				} 
        	    catch (IOException e) {output.append(e.toString());}
        	
        	    output.append(cm.output);
        	    refresh.setVisible(true);
        	 }
        });

		connectPanel.add(rmiLabel);
		connectPanel.add(rmiUrl);
		connectPanel.add(connectOK);
		connectPanel.add(connectError);
		
		
		//MENU BAR
		JMenuBar menuBar;
	    JMenu menu, submenu, cityMenu;
	    JMenuItem menuItem;
	  
	    //Create the menu bar.
	    menuBar = new JMenuBar();

	    //Build the first menu.
	    menu = new JMenu("File");
	    menu.setMnemonic(KeyEvent.VK_C);
	    menuBar.add(menu);
	    
	    //a group of JMenuItems
	    submenu = new JMenu("Select Map");
        submenu.setMnemonic(KeyEvent.VK_D);
        menu.add(submenu);
        
      
	    
        ImageIcon iconExit = new ImageIcon(images_path + "icons/exit.png");
        menuItem = new JMenuItem("Exit",iconExit);
       	menuItem.setMnemonic(KeyEvent.VK_E);
       	menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) 
            {
                System.exit(0);
            }
        });

       	menu.add(menuItem);
       	
       	File folders= new File(images_path + "maps/");
       	String listDir[]=folders.list();
       	for(final String cityName : listDir)
       	{
       		cityMenu = new JMenu(cityName);
            submenu.add(cityMenu);

            File subFolder=new File(images_path + "maps/"+cityName+"/");
            String listSubDir[]=subFolder.list();
            for(String subnameFile : listSubDir)
            {
            	menuItem = new JMenuItem(subnameFile);
            	menuItem.addActionListener(new ActionListener() 
                {
        			//action associated to the text field
                    public void actionPerformed(ActionEvent event)
                    {
                    	city=cityName;
                    	group=event.getActionCommand();
                    	map.setimage(images_path + "maps/"+ cityName +"/"+ group +"/", coordX, coordY);
                    	rmiUrl.setText("//"+ manager_ip +"/GatewayManager");
                    	rmiUrl.setEnabled(true);
                    }
                });
                cityMenu.add(menuItem);
            }
       	}        

       	
//SETTING FRAME
		this.setJMenuBar(menuBar);
        
        c.add(connectPanel);
        c.add(buttonPanel);
        
        c.add(map);
       
        c.add(tree);
        c.add(Box.createRigidArea(new Dimension(20,100)));
         
        c.add(scroll);
        c.add(Box.createRigidArea(new Dimension(20,100)));
        
        c.add(checkMenu);
                
    }
	
	

	public class TreeStructure extends JPanel implements TreeSelectionListener 
	{
	    private JTree tree;
	    private DefaultMutableTreeNode top;
	    private ArrayList<DefaultMutableTreeNode> pri;
	    private ArrayList<WSNGateway> gwlist;
	    //Optionally play with line styles.  Possible values are
	    //"Angled" (the default), "Horizontal", and "None".
	   
	    //Optionally set the look and feel.
	    public TreeStructure() 
	    {
	    	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	       
	    	//Create the nodes.
	    	top = new DefaultMutableTreeNode("Parking");
	         
	        //Create a tree that allows one selection at a time.
	        tree = new JTree(top);
	        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	        tree.addTreeSelectionListener(this); 
	        ImageIcon leafIcon= new ImageIcon(images_path + "icons/point.png");
	        DefaultTreeCellRenderer renderer = 	new DefaultTreeCellRenderer();
	        renderer.setLeafIcon(leafIcon);
	        tree.setCellRenderer(renderer);


	        gwlist=new ArrayList<WSNGateway>();
	        pri=new ArrayList<DefaultMutableTreeNode>();
	        
	        //Create the scroll pane and add the tree to it. 
	        JScrollPane treeView = new JScrollPane(tree);

	        JLabel title = new JLabel("List of parking");
	        title.setPreferredSize(new Dimension(200, 15));
	        title.setVerticalAlignment(SwingConstants.TOP);
	       
	        Dimension minimumSize = new Dimension(100, 100);
	        treeView.setMinimumSize(minimumSize);
	        treeView.setPreferredSize(new Dimension(200, 100));
	        
	        //Add Object to jpanel
	        add(title);
	        add(treeView);
	    }

	    /** Required by TreeSelectionListener interface. */
	    public void valueChanged(TreeSelectionEvent e) 
	    {
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
	        int coord[]=null;
	        WSNGateway gw=null;
	        String parent;
	        
	        if (node == null) return;
	        else if ((node.toString().equals("SEARCH ROUTE")))
	        {
	        	parent = node.getParent().toString();
	        	try 
	        	{
	        		gw=remoteObject.getWSNGateway("WSN_GW"+parent.charAt(parent.length()-1));
	        		coord=gw.getCoord();
				} 
	        	catch (RemoteException e1) {e1.printStackTrace();}
	        	
	        	map.searchSpecificPath(coordX, coordY, coord[0], coord[1]);
	        	map.setRoute(true);
	        	map.updateUI();
	        	refreshCar.restart();
	        }
	  
	    }

	    public ArrayList<Park> createTree(ArrayList<WSNGateway> list) throws RemoteException 
	    {
	    	this.gwlist=list;
	    	
	    	ArrayList<Park> listPark= new ArrayList<Park>();
	    	DefaultMutableTreeNode category = null;
	        DefaultMutableTreeNode parking=null;
	        Class typeIn[]={};
	        Object paramIn[]={};
	        
	        for(WSNGateway gw : gwlist)
	        {
	        	String name=gw.getName();
	        	category = new DefaultMutableTreeNode("Park"+name.charAt(name.length()-1));
	        	top.add(category);
	        
	        	Double price=(Double)gw.useMethod(pfa.getClassIdentificationParameter(), "getPrice", typeIn, paramIn);
	        	if(price>0)
	        		parking = new DefaultMutableTreeNode("Price/hour: "+price.toString().charAt(0)+""+price.toString().charAt(1)+""+price.toString().charAt(2)+""+price.toString().charAt(3));
	        	else
	        		parking = new DefaultMutableTreeNode("Price: free");
	        	
	        	category.add(parking);
	        		
	        	Integer num=(Integer)gw.useMethod(pfa.getClassIdentificationParameter(), "getTotParking", typeIn, paramIn);
	        	parking = new DefaultMutableTreeNode("Tot car parks: "+num);
	        	category.add(parking);
	        
	        	Integer free= (Integer)gw.useMethod(pfa.getClassIdentificationParameter(), "getFreeParking", typeIn, paramIn);
	        	parking = new DefaultMutableTreeNode("Available car parks: "+free);
	        	category.add(parking);
	        	
	        	parking = new DefaultMutableTreeNode("SEARCH ROUTE");
	        	category.add(parking);
	        	
	        	Park p = new Park(name, price, num, free);
	        	listPark.add(p);
	        	
	        }
	        
	        return listPark;
	    }
	   
	    
	    public void removeNodes()
	    {
	    	top.removeAllChildren();   	
	    }
	    
	}

	
    public class CheckBoxHandler implements ItemListener
	{
    	// Listens to the check boxes
        public void itemStateChanged(ItemEvent e) 
        {
          Object source = e.getItem();

            if (source == parking) 
            {
            	if(e.getStateChange() == ItemEvent.DESELECTED)
            	{
            		map.setPark(false);
            		routeEN=false;
            		map.setRoute(false);
            		map.setimage(images_path + "maps/"+city+"/"+ group+ "/", coordX, coordY);
            		map.setCarMove(false);
            		map.setPos();
            		refreshCar.stop();
            		
            		tree.removeNodes();
            		
            		tree.updateUI();
            		map.updateUI();
            		
            	}
            	else
            	{
            		try 
                	{
						parkList=tree.createTree(gwList);
						tree.updateUI();
					} 
                	catch (RemoteException e1) {e1.printStackTrace();}
                	
                	map.requestParking(gwList, parkList, group);
                	map.setPark(true);
                	map.updateUI();
                	routeEN=true;
                	noise.setVisible(true);
                	
            	}
            } 
            else if (source == noise) 
            {
            	if(e.getStateChange() == ItemEvent.DESELECTED)
            	{
            		map.setNoise(false);
            		map.updateUI();
            		}
            	else
            	{
            		
            		String noise;
            		map.setUrl(url);
            		map.setNoise(true);
            		map.updateUI();
            		
            		
            		double media=map.getMediaNoise();
            		if(media>=650)
    					noise="VERY HIGH";
    				else if(media>=450 && media<650)
    					noise="HIGH";
    				else if(media>=250 && media<450)
    					noise="NORMAL";
    				else
    					noise="LOW";
            		
            		output.append("\n\nZONE NOISE = "+noise+"\n\n");
                	
            	}
            } 
    
        }
	}

	
    
    
    public static void main(String[] args)
    {
    	manager_ip = args[0];
    	my_ip = args[1];   	
    	
        System.setProperty("java.security.policy", "src/myjava.policy");
		
        /*
         * impostazione SecurityManager
         */
        System.setSecurityManager(new SecurityManager());   
    	   	
    	new GroupCreation(manager_ip);
    	    	
    	ParkingFrameApplication app = new ParkingFrameApplication();
    	app.setVisible(true);
    }
	
}
