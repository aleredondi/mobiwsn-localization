
package client_applications.traking;


import remote_interfaces.*;
import remote_interfaces.mote.Mote;
import remote_interfaces.result.ValueResult;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import org.jfree.chart.ChartPanel;


public class TrackingFrameApplication extends JFrame 
{	
	private Toolkit toolkit;
	private FlowLayout layout;
	private JTextArea output;
	private JScrollPane scroll;
	private WSNGatewayManager remoteObject;
	private String url, moteSearched, clientUrl, moteSelected;
	private JPanel startImage, buttonPanel;
	private ChartPanel chartPanel;
	private ChartManager manchart;
	private TreeStructure tree;
	private boolean chartCreated, online, singleThreshold, connected;
	private Double temp;
	private JTextField soglia;
	private JLabel title, siren;
	private JButton refresh;
	private Timer time;
	
	private static String manager_ip;
	private static String my_ip;
	
	private static String images_path = "src/client_applications/traking/images/";
	
	
	public TrackingFrameApplication()
	{
		
		super("Tracking Mote"); //set interface name
		setSize(1024,800); //set the window size
		//setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		final Container c=this.getContentPane();
		
		//set the layout of the interface
		layout=new FlowLayout();
		c.setLayout(layout);
		
		//set the windows in the middle of the screen
		toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, 
		size.height/2 - getHeight()/2);

        clientUrl="//"+ my_ip +"/ClientManager";
        this.rmiGenerator();
        
		
//OUTPUT TEXT AREA
		output = new JTextArea(10, 50);
		output.setEditable(false);
		scroll = new JScrollPane(output);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(400,150));
		
//TIMER
		
		ActionListener visibilitySiren = new ActionListener() 
        {
			//action associated to the text field
            public void actionPerformed(ActionEvent event)
            {
            	siren.setVisible(false);
            	time.stop();
            }
        };
        
		time = new Timer(8000, visibilitySiren);
	
//CHART PANEL
		startImage = new JPanel();
		JLabel picture=new JLabel(new ImageIcon(images_path + "default.jpeg"));
		startImage.add(picture);
		chartCreated=false;
		online=false;
		
//BUTTON PANEL
		buttonPanel= new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		
		Icon refreshIcon=new ImageIcon(images_path + "icons/buttonRefresh.png");
		refresh=new JButton(refreshIcon);
		refresh.setVisible(false);
		refresh.addActionListener(new ActionListener() 
        {
			//action associated to the text field
            public void actionPerformed(ActionEvent event)
            {	
            	getContentPane().remove(chartPanel);
            	chartPanel=new ChartPanel(manchart.createChart(moteSearched));
            	chartPanel.setPreferredSize(new Dimension(900,438));
            	getContentPane().add(chartPanel,1);
            	validate();
            }
        });
		
	    title = new JLabel("Set Threshold alarm");
	    title.setPreferredSize(new Dimension(200, 15));
	    title.setVerticalAlignment(SwingConstants.TOP);
	    title.setVisible(false);
	    
	    soglia = new JTextField();
	    soglia.setVisible(false);
	    soglia.addActionListener(new ActionListener() 
        {
			//action associated to the text field
            public void actionPerformed(ActionEvent event)
            {
            	title.setVisible(false);
            	soglia.setVisible(false);
            	
            	temp=Double.parseDouble(event.getActionCommand());
            	try 
            	{
            		if(singleThreshold)
            			manchart.setThreshold(temp, moteSearched);
            		else
            			manchart.setThreshold(temp, null);
				} 
            	catch (RemoteException e) {e.printStackTrace();}
            	
            }
        });
	    
	    Icon sirenImage=new ImageIcon(images_path + "icons/siren.GIF");
		siren = new JLabel(sirenImage);
	    siren.setVisible(false);
	    
	    buttonPanel.add(title);
	    buttonPanel.add(soglia);    		
	    buttonPanel.add(Box.createRigidArea(new Dimension(20,5)));
	    buttonPanel.add(refresh);
	    buttonPanel.add(siren);
		

//SELECT MOTE PANEL
	    tree = new TreeStructure();	    
	    
//CONNECTION PANEL		
	    JPanel connectPanel= new JPanel();
		JLabel rmiLabel = new JLabel("Set rmi server address :   ");
		JTextField rmiUrl = new JTextField(50);
		rmiUrl.setText("//"+ manager_ip +"/GatewayManager");
    	      
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
        	        manchart =new ChartManager(remoteObject);
        	        tree.createTree();
        	        
        	        connected = true;
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
        	     
        	 }
        });

		connectPanel.add(rmiLabel);
		connectPanel.add(rmiUrl);
		connectPanel.add(connectOK);
		connectPanel.add(connectError);
		
		//MENU BAR
		JMenuBar menuBar;
	    JMenu menu, tracking,settingTr;
	    JMenuItem menuItem, trackmenuOn, trackmenuOff, single, multiple;
	  
	    //Create the menu bar.
	    menuBar = new JMenuBar();

	    //Build the first menu.
	    menu = new JMenu("File");
	    menu.setMnemonic(KeyEvent.VK_A);
	    menuBar.add(menu);
	    
	    //a group of JMenuItems
	    tracking = new JMenu("Select tracking");
	    tracking.setMnemonic(KeyEvent.VK_B);
        menu.add(tracking);
	    
        settingTr = new JMenu("Set Treshold");
        settingTr.setMnemonic(KeyEvent.VK_C);
        menu.add(settingTr);   
     
        single = new JMenuItem("Single mote");
        settingTr.add(single);
        single.addActionListener(new ActionListener() 
        {
        	
			//action associated to the text field
            public void actionPerformed(ActionEvent event)
            {
            	if(remoteObject!=null )
            	{
            		title.setVisible(true);
            		soglia.setVisible(true);
            		singleThreshold=true;
            	}
            	else
            		output.append("\nConnect to the WSNGatewayManager and then select the mote to track\n\n");
           } 
        });
        
        multiple = new JMenuItem("All mote");
        settingTr.add(multiple);
        multiple.addActionListener(new ActionListener() 
        {
			//action associated to the text field
            public void actionPerformed(ActionEvent event)
            {
            	if(remoteObject!=null )
            	{
            		title.setVisible(true);		
            		soglia.setVisible(true);
            		singleThreshold=false;
            	}
            	else
            		output.append("\nConnect to the WSNGatewayManager and then select the mote to track\n\n");
            }
        });
        
        trackmenuOn = new JMenuItem("Online Tracking");
        tracking.add(trackmenuOn);
        trackmenuOn.addActionListener(new ActionListener() 
        {
			
        	//action associated to the text field
            public void actionPerformed(ActionEvent event)
            {
            	refresh.setVisible(false);
            	try 
            	{
					manchart.deactiveOnline();
				} 
            	catch (RemoteException e) {e.printStackTrace();}
            	
            	online=true;
            	moteSelected=moteSearched;
            	
            	if(moteSelected!=null && remoteObject!=null )
            	{
            		if (!chartCreated)
            			getContentPane().remove(startImage);
            		
            		else
            			getContentPane().remove(chartPanel);
            		
            		manchart.activeOnline();
            		chartPanel=new ChartPanel(manchart.createChart(moteSelected));
            		chartPanel.setPreferredSize(new Dimension(900,438));
            		getContentPane().add(chartPanel,1);
            		validate();		
            		chartCreated=true;
            	}
            	else
            	{
            		output.append("\nConnect to the WSNGatewayManager and then select the mote to track\n\n");
            	}
      
            }
        });
        
        trackmenuOff = new JMenuItem("Offline Tracking");
        tracking.add(trackmenuOff);
        trackmenuOff.addActionListener(new ActionListener() 
        {
			//action associated to the text field
            public void actionPerformed(ActionEvent event)
            {
            	if(online)
            	{	
            		try 
            		{
						manchart.deactiveOnline();
					} 
            		catch (RemoteException e) {e.printStackTrace();}
            	}
            	
            	moteSelected=moteSearched;
            	
            	if(moteSelected!=null && remoteObject!=null )
            	{
            		if (!chartCreated)
            			getContentPane().remove(startImage);
            		else
            			getContentPane().remove(chartPanel);
            			
            		refresh.setVisible(true);
            		chartPanel=new ChartPanel(manchart.createChart(moteSelected));
            		chartPanel.setPreferredSize(new Dimension(900,438));
            		getContentPane().add(chartPanel,1);
            		validate();		
            		chartCreated=true;
            	}
            	else
            	{
            		output.append("\nConnect to the WSNGatewayManager and then select the mote to track\n\n");
            	}
            	 
            }
        }); 
        
        ImageIcon iconExit = new ImageIcon(images_path + "icons/exit.png");
        menuItem = new JMenuItem("Exit",iconExit);
       	menuItem.setMnemonic(KeyEvent.VK_E);
       	menuItem.addActionListener(new ActionListener() {
       		
       		public void actionPerformed(ActionEvent event) 
            {
                if(online)
                {
                	try 
                	{
                		manchart.deactiveOnline();
                	} 
                	catch (RemoteException e) {e.printStackTrace();}	
                }
                
            	if (connected) manchart.disconnect();
        	    
       			System.exit(0);
          }
        });

       	menu.add(menuItem);
		
	//SETTING FRAME
	   
       	this.setJMenuBar(menuBar);
        
       	c.add(connectPanel);
       	c.add(startImage);
        c.add(Box.createRigidArea(new Dimension(1000,10)));
        c.add(tree);
        c.add(Box.createRigidArea(new Dimension(20,100)));
        
        c.add(scroll);
        c.add(Box.createRigidArea(new Dimension(20,100)));
        c.add(buttonPanel);
       	
    }

	/**
	 * Function used to create the client RMI interface 
	 */
	public void rmiGenerator()
	{
		OnlineManager rmiObject= null;
	
		try
		{
			System.setProperty("java.security.policy", "myjava.policy");
			
			System.setSecurityManager(new SecurityManager());
			
			Thread.currentThread().setName("ClientOnlineManager_Thread");

			rmiObject = new OnlineManager();
						
			Naming.rebind(clientUrl, rmiObject);
			
		}
		
		catch(java.net.MalformedURLException muex){output.append(muex.toString());}
		catch(java.rmi.RemoteException  rex){output.append(rex.toString());}
		catch(Exception ex){output.append(ex.toString());}
	}
	
    /**
     * TreeStructure, show the list of mobile mote
     * @author Alessandro Laurucci
     *
     */
    public class TreeStructure extends JPanel implements TreeSelectionListener 
	{
	    private JTree tree;
	    private DefaultMutableTreeNode top;
	   
	    public TreeStructure() 
	    {
	    	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	       
	    	//Create the nodes.
	    	top = new DefaultMutableTreeNode("Motes");
	         
	        //Create a tree that allows one selection at a time.
	        tree = new JTree(top);
	        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	        tree.addTreeSelectionListener(this); 
	        ImageIcon leafIcon= new ImageIcon(images_path + "icons/point.png");
	        DefaultTreeCellRenderer renderer = 	new DefaultTreeCellRenderer();
	        renderer.setLeafIcon(leafIcon);
	        tree.setCellRenderer(renderer);

	        //Create the scroll pane and add the tree to it. 
	        JScrollPane treeView = new JScrollPane(tree);

	        JLabel title = new JLabel("Choise the mote to track");
		    title.setPreferredSize(new Dimension(200, 15));
		    title.setVerticalAlignment(SwingConstants.TOP);
		    
		    Dimension minimumSize = new Dimension(100, 100);
	        treeView.setMinimumSize(minimumSize);
	        treeView.setPreferredSize(new Dimension(200, 100));
	        
	        //Add Object to jpanel
	        add(title);
	        add(treeView);
	    }

	    public void valueChanged(TreeSelectionEvent e) 
	    {
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
	        if (node == null) return;
	        
	        else if(!node.toString().equals("Motes"))
	        {	
	        	moteSearched=node.toString();
	        	output.append("\nMote to track : "+moteSearched+"\n\n");
	        }
 
	    }
	    
	    public void createTree() throws RemoteException 
	    {
	    	DefaultMutableTreeNode moteNode=null;
	        String moteId=null;
	       
	        for(WSNGateway gw : remoteObject.getWSNGatewayList())
	        {
	        	for(Mote mote : gw.getMoteList())
				{
	        		moteId=mote.getId();
	        		
					if (moteId.charAt(moteId.length()-1)=='M' && mote.isReachable())
					{
						moteNode = new DefaultMutableTreeNode(moteId);
			        	top.add(moteNode);
			        	manchart.addMoteToList(mote.getId());
					}
					
				}
	        		            
	        }
	        
	        manchart.connect(clientUrl);
	        	        
	    }//end of createTree
	    
	}//end of TreeStructure
    

    /**
     * Object used to manage che RMI request from the gateways
     * @author Alessandro Laurucci
     *
     */
    public class OnlineManager  extends UnicastRemoteObject implements ClientEventInterface
    {

		protected OnlineManager() throws RemoteException 
		{}

		public void eventAllert(ArrayList<Object> input, int val) throws RemoteException 
		{
			switch(val)
			{
				case 1: //reading of a new value
					ValueResult newValue=(ValueResult)input.get(0);
					manchart.addValue(newValue);
					validate();
	            	chartCreated=true;
					break;
					
				case 2://threshold allarm
					SoundLoader alarmSound = new SoundLoader(images_path + "alarm.WAV");
					siren.setVisible(true);
					alarmSound.sound();
					Double num=(Double)input.get(1);
					String thresholdval=num.toString();
					String thval="";
					for(int i=0;i<5;i++)
					{	
						if(thresholdval.length()<=i)
							break;
						thval=thval+thresholdval.charAt(i);
							
					}
					output.append("\n"+input.get(0).toString()+" over threshold : "+thval+"\n\n");
					time.restart();
					break;
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
    	
    	try
    	{
			new GroupCreation(manager_ip, my_ip);
		}
    	catch (RemoteException e)
    	{
			e.printStackTrace();
		}
    	
    	TrackingFrameApplication app = new TrackingFrameApplication();
    	app.setVisible(true);
    }
	
}
