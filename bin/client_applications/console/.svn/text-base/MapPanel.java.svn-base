
package client_applications.console;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import java.util.*;
import java.rmi.RemoteException;


/*
	This class is used to display a map of the network for the user.
	The gateway appears in red, and the regular motes in blue.
	
	TODO : 
			add the gradient feature
*/

class MapPanel extends JPanel implements MouseListener, MouseMotionListener 
{
	private Vector<DrawNode> nodeDatabase;
	
        //private RequestPanel requestPanel;
	//private LegendPanel legendPanel;
	
	private static final int SIZE_COLOR_ARRAY = 10;
	private static Color colorQualityArray[];
	private String moteLegend, parentIdLegend, moteIdLegend,  readingLegend, routeLegend;
	
	private boolean nodeDragged = false;
	private DrawNode nodeMoving;
	private DrawNode selectedNode=null;
	Vector<DrawNode> drawNodeGroup = null;
	//private int timeout = Util.TIMEOUT;
        
        private NodeSelectionListener nodeSelListener;
        
	
	public MapPanel() 
        {
		
		//this.requestPanel = requestPanel;
		
                // the quality array starts from good quality (green) to bad quality (red)
		colorQualityArray = new Color [SIZE_COLOR_ARRAY];
		
                for (int i=0; i<SIZE_COLOR_ARRAY; i++)
			colorQualityArray[i] = new Color(255*i/(SIZE_COLOR_ARRAY-1), 255-255*i/(SIZE_COLOR_ARRAY-1), 0);
		
                // The Strings are initialized at "none" for most of them
		moteLegend = "circle" ;  parentIdLegend = "text + gradient"; 
                moteIdLegend = "text + gradient"; 
		readingLegend = "text"; routeLegend = "line + label";
		
                addMouseListener(this);
		addMouseMotionListener(this);
                
                this.nodeSelListener = null;
	}
        
        
        public void addNodeSelectionListener(NodeSelectionListener theListener)
        {
            this.nodeSelListener = theListener;
        }
        
        public void setMoteDatabaseAndRepaint(Vector<DrawNode> nodeDatabase)
        {
        	
            this.nodeDatabase = nodeDatabase;
            this.repaint();
        }
        

	
        @Override
	public void paint(Graphics g) 
        {
        	
        this.selectedNode=this.nodeSelListener.getSelectedNode();
        this.drawNodeGroup=this.nodeSelListener.getGroup();

		Graphics2D g2 = (Graphics2D) g;
		// we draw first a white area with a border
		Dimension d = getSize();
		g2.setPaint(Color.DARK_GRAY);
		g2.fill(new Rectangle2D.Double(0, 0, d.width, d.height));
		g2.setPaint(Color.white);
		g2.fill(new Rectangle2D.Double(Util.MOTE_RADIUS/2, Util.MOTE_RADIUS/2, d.width-(Util.MOTE_RADIUS), d.height-(Util.MOTE_RADIUS)));
               
		
                this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                
                // we run through the list of motes and display 
		// in first the routes and next each mote
			
		String msg;
                
                if (nodeDatabase == null) return;
                
                try
                {
		
                    // we run through the available legends, to know which one is selected
                    // and we launch the corresponding functions
                    for (DrawNode drawNode : nodeDatabase)    
                    {                   		
                        // draw route line
                        if ("line".equals(routeLegend) || "line + label".equals(routeLegend)) 
                        {
                            if (drawNode.getNodeType() == DrawNodeType.Mote)
                            {
                                if (drawNode.getMoteRef().isReachable())
                                    drawParentRoute(drawNode, g2);
                                
                                // ### DEBUG ###
                           		//System.out.println("\n Node : " + drawNode.getMoteRef().getId());
                           		//System.out.println("\n Gateway : " + drawNode.getGatewayRef().getName() + "\n");
                            	// ### ### ###  

                            }
                            else if (drawNode.getNodeType() == DrawNodeType.Gateway)
                            {
                                drawParentRoute(drawNode, g2);
                            }
                        }

                        if ("line + label".equals(routeLegend)) 
                        {
                            if (drawNode.getNodeType() == DrawNodeType.Mote)
                            {
                                if (drawNode.getMoteRef().isReachable())
                                {
                                  msg = "From Id=" + drawNode.getId() + "\n To Id=" + drawNode.getParentId() ;//+ "\n";

                                  //drawParentRouteLabel(drawNode, msg, g2);
                                }
                            }
                            else if (drawNode.getNodeType() == DrawNodeType.Gateway)
                            {
                                msg = "From Id=" + drawNode.getId() + "\n To Id=" + drawNode.getParentId() ;//+ "\n";

                                //drawParentRouteLabel(drawNode, msg, g2);
                            }
                        }

                        drawTheNode(drawNode, g2);

                        // If the mote is displayed and one of the text legend is choosen
                        if ("circle".equals(moteLegend) 
                                && ("text".equals(parentIdLegend) ||
                                "text".equals(moteIdLegend) || 
                                "text + gradient".equals(parentIdLegend) ||
                                "text + gradient".equals(moteIdLegend))) 
                        {

                            msg = "";
                            
                            if (drawNode.getNodeType() == DrawNodeType.GatewayManager)
                                   msg = " WSNGateway\n  Manager";
                            else
                            {
                                if ("text".equals(moteIdLegend)) { msg = msg.concat("Id = " + drawNode.getId() + "\n"); }
                                if ("text + gradient".equals(moteIdLegend)) { msg = msg.concat("Id = " + drawNode.getId() + "\n");}
                                //if ("text".equals(parentIdLegend)) { msg = msg.concat("Parent Id = " + drawNode.getParentId() + "\n");}
                                //if ("text + gradient".equals(parentIdLegend)) { msg = msg.concat("Parent Id = " + drawNode.getParentId() + "\n");}
                            }
                            drawTheText(drawNode, msg, g2);

                        }
                    }
                
                }
                catch(RemoteException rex)
                {
                   System.out.println("Remote Exception :" + rex.getMessage());
                }              	
	}
	
	/*
            This function draws a mote and adds a 
            black border if the mote is selected.
            Mote.getX() gives the center of the mote, so we have to get 
            the top left corner for the functions fill and draw.
	*/
	private void drawTheNode(DrawNode node, Graphics2D g2) 
        {
			boolean thisNodeIsSelected;
            try
            {   
                if(node != null) 
                {
                    if(node.getNodeType() == DrawNodeType.Mote)
                    {
                        if (!node.getMoteRef().isReachable())
                        {
                            g2.setPaint(Color.lightGray);
                        }
                        else
                        {
                            if (node.isPanCoordinator())

                               g2.setPaint(Color.red);	
                            else
                               g2.setPaint(Color.blue);
                        }
                    

                        g2.fill(new Ellipse2D.Double(
                                                toVirtualX(node.getX())-Util.MOTE_RADIUS, 
                                                toVirtualY(node.getY())-Util.MOTE_RADIUS, 
                                                2*Util.MOTE_RADIUS, 2*Util.MOTE_RADIUS
                                                    )
                           );
                    
                
                        thisNodeIsSelected=false;
                        if(selectedNode!=null){
                        	if(selectedNode.getNodeType() ==DrawNodeType.Mote){ 
                        		if(selectedNode.getMoteRef().getId().equals(node.getMoteRef().getId())){
                        			thisNodeIsSelected=true;
                                    BasicStroke stroke = new BasicStroke(4.0f);
                                    g2.setStroke(stroke);
                        			g2.setPaint(Color.CYAN);
                        		}
                        	}

                        }
            			if(thisNodeIsSelected==false){
                            BasicStroke stroke = new BasicStroke(1.0f);
                            g2.setStroke(stroke);
            			    g2.setPaint(Color.black);
            			}
            			
            			if(belongsToGroup(node)){
                			thisNodeIsSelected=true;
                            BasicStroke stroke = new BasicStroke(4.0f);
                            g2.setStroke(stroke);
                			g2.setPaint(Color.ORANGE);
            			}
            			
                        g2.draw(new Ellipse2D.Double(
                                                     toVirtualX(node.getX())-Util.MOTE_RADIUS, 
                                                     toVirtualY(node.getY())-Util.MOTE_RADIUS, 
                                                     2*Util.MOTE_RADIUS, 2*Util.MOTE_RADIUS
                                                     )
                               );
                    }
                    else if (node.getNodeType() == DrawNodeType.Gateway)
                    {
                        g2.setPaint(Color.green);	
                        
                        g2.fill(new Rectangle2D.Double(
                                                toVirtualX(node.getX())-Util.MOTE_RADIUS, 
                                                toVirtualY(node.getY())-Util.MOTE_RADIUS, 
                                                2*Util.MOTE_RADIUS, 2*Util.MOTE_RADIUS
                                                    )
                           );
                    
                        thisNodeIsSelected=false;
                        if(selectedNode!=null){
                        	if(selectedNode.getNodeType() ==DrawNodeType.Gateway){ 
                        		if(selectedNode.getGatewayRef().getName().equals(node.getGatewayRef().getName())){
                                    BasicStroke stroke = new BasicStroke(4.0f);
                                    g2.setStroke(stroke);
                        			g2.setPaint(Color.CYAN);
                        			thisNodeIsSelected=true;
                        		}
                        	}
                        }
            			if(thisNodeIsSelected==false){
                            BasicStroke stroke = new BasicStroke(1.0f);
                            g2.setStroke(stroke);
            			    g2.setPaint(Color.black);
            			}
            			
            			if(belongsToGroup(node)){
                			thisNodeIsSelected=true;
                            BasicStroke stroke = new BasicStroke(4.0f);
                            g2.setStroke(stroke);
                			g2.setPaint(Color.ORANGE);
            			}
            			
                        g2.draw(new Rectangle2D.Double(
                                                     toVirtualX(node.getX())-Util.MOTE_RADIUS, 
                                                     toVirtualY(node.getY())-Util.MOTE_RADIUS, 
                                                     2*Util.MOTE_RADIUS, 2*Util.MOTE_RADIUS
                                                     )
                               );
                    
                    }
                    else if(node.getNodeType() == DrawNodeType.GatewayManager)
                    {
                        
                        g2.setPaint(Color.magenta);
                    

                        g2.fill(new RoundRectangle2D.Double(
                                                toVirtualX(node.getX())-Util.MOTE_RADIUS, 
                                                toVirtualY(node.getY())-Util.MOTE_RADIUS, 
                                                3*Util.MOTE_RADIUS, 
                                                3*Util.MOTE_RADIUS,8,8
                                                    )
                           );
                    
                        thisNodeIsSelected=false;
                        if(selectedNode!=null){
                        	if(selectedNode.getNodeType() ==DrawNodeType.GatewayManager){ 
                        		if(selectedNode.getId().equals(node.getId())){
                                    BasicStroke stroke = new BasicStroke(4.0f);
                                    g2.setStroke(stroke);
                        			g2.setPaint(Color.CYAN);
                        			thisNodeIsSelected=true;
                        		}
                        	}
                        }
            			if(thisNodeIsSelected==false){
                            BasicStroke stroke = new BasicStroke(1.0f);
                            g2.setStroke(stroke);
            			    g2.setPaint(Color.black);
            			}
                        g2.draw(new RoundRectangle2D.Double(
                                                     toVirtualX(node.getX())-Util.MOTE_RADIUS, 
                                                     toVirtualY(node.getY())-Util.MOTE_RADIUS, 
                                                      3*Util.MOTE_RADIUS, 
                                                        3*Util.MOTE_RADIUS,8,8
                                                     )
                               );
                    }
                        
                        
                    
                }
            }
            catch(RemoteException rex)
            {
               System.out.println("Remote Exception :" + rex.getMessage());
            }
	}
	
	/*
		This function draws some text near of a mote. It takes in
		parameter a string representing the text to print. The 
		character "\n" means a new line.
		Mote.getX() gives the center of the mote, so we have to get 
		the top left corner for the functions fill and draw
	*/
	
	private void drawTheText(DrawNode node, String text, Graphics2D g2) 
        {
		if(node != null && text.length() > 0) 
                {
			g2.setPaint(Color.black);
			BasicStroke stroke = new BasicStroke(1.0f);
			g2.setStroke(stroke);
			g2.setFont(new Font("Serif",Font.PLAIN,12));
			int beginIndex=0, endIndex=0, i=0;
			String msg;
			boolean exit = false;
			do {
				endIndex = text.indexOf("\n", beginIndex);
				if (endIndex == -1)
					msg = text.substring(beginIndex);
				else 
					msg = text.substring(beginIndex, endIndex);
				if (text.indexOf("\n", beginIndex+1) == -1)
					exit = true;
				beginIndex = endIndex+1;
				g2.drawString(msg, 
                                              toVirtualX(node.getX())+2*Util.MOTE_RADIUS, 
                                              toVirtualY(node.getY())+Util.MOTE_RADIUS+(12*i++));
			}while(!exit);
		}
	}
        
	/*
		This function draws a route from the mote to its parent if
		it exists, and deals with the quality and lastTimeSeen variables.
	*/
	
	private void drawParentRoute(DrawNode node, Graphics2D g2) 
        {
            if(node != null) 
            {
               DrawNode parentNode = getParentNode(node, nodeDatabase);
	
               try
               {
                   if(parentNode == null) 
                   {
                        System.out.println("Parent mote (id="+node.getParentId()+") from mote (id="+node.getId()+") not found");
                        return;
                   }
               }
               catch(RemoteException rex)
               {
                   System.out.println("Remote Exception :" + rex.getMessage());
               }
	       
               BasicStroke stroke = new BasicStroke(2.0f);
	       g2.setStroke(stroke);
		
                        /*float tmp = (float)mote.getQuality() / 65535 * (SIZE_COLOR_ARRAY-1);
			float alpha = 255.0f;
			if(mote.getTimeSinceLastTimeSeen()<timeout)
				alpha = (float)mote.getTimeSinceLastTimeSeen() / timeout * 255;*/
		
                g2.setPaint(new Color(125,144,147));
		g2.draw(new Line2D.Double(	
                                            toVirtualX(node.getX()), 
                                            toVirtualY(node.getY()), 
                                            toVirtualX(parentNode.getX()), 
                                            toVirtualY(parentNode.getY())
                                         )
                       );
            }
	}
	
	/*
		This function draws a route from the mote to its parent if
		it exists, and deals with the quality and lastTimeSeen variables.
	*/
	private void drawParentRouteLabel(DrawNode node, String text, Graphics2D g2) 
        {
            if(node != null) 
            {
		DrawNode parentNode = getParentNode (node, nodeDatabase);
		
                try
                {
                    if(parentNode == null) 
                    {
                        System.out.println("Parent node from node (id="+node.getId()+") not found");
                        return;
                    }
                }
                catch(RemoteException rex)
                {
                   System.out.println("Remote Exception :" + rex.getMessage());
                }
                
                g2.setPaint(Color.blue);
                BasicStroke stroke = new BasicStroke(1.0f);
                g2.setStroke(stroke);
                g2.setFont(new Font("Serif",Font.PLAIN,12));
                int beginIndex=0, endIndex=0, i=0;
                int x = (node.getX() + parentNode.getX())/2;
                int y = (node.getY() + parentNode.getY())/2;
                String msg;
                boolean exit = false;
                do {
                        endIndex = text.indexOf("\n", beginIndex);
                        if (endIndex == -1)
                                msg = text.substring(beginIndex);
                        else 
                                msg = text.substring(beginIndex, endIndex);
                        if (text.indexOf("\n", beginIndex+1) == -1)
                                exit = true;
                        beginIndex = endIndex+1;
                        g2.drawString(msg, toVirtualX(x), toVirtualY(y)+(12*i++));
                }while(!exit);
            }
	}
	
	/*
		These both functions translate the x and y values of the mote
		to values for the screen.
	*/
	
	private int toVirtualX(int x) { 
		Dimension d = getSize();
		int tmp = d.width*x/Util.X_MAX;
		if (tmp<Util.MOTE_RADIUS)					// we prevent x to go past the panel
			return Util.MOTE_RADIUS;
		else if (tmp>d.width-Util.MOTE_RADIUS)
			return d.width-Util.MOTE_RADIUS;
		else
			return tmp;
	}
	
	private int toVirtualY(int y) { 
		Dimension d = getSize();
		int tmp = d.height*y/Util.Y_MAX;
		if (tmp<Util.MOTE_RADIUS)					// we prevent y to go past the panel
			return Util.MOTE_RADIUS;
		else if (tmp>d.height-Util.MOTE_RADIUS)
			return d.height-Util.MOTE_RADIUS;
		else
			return tmp;
	}
	
	/*
		These both functions translate the x and y values of the screen
		to real values for the mote.
	*/
	
	private int toRealX(int x) { 
		Dimension d = getSize();
		if (x<Util.MOTE_RADIUS)
			x = Util.MOTE_RADIUS;
		else if (x>d.width-Util.MOTE_RADIUS)
			x = d.width-Util.MOTE_RADIUS;
		return Util.X_MAX*x/d.width;
	}
	
	private int toRealY(int y) { 
		Dimension d = getSize();
		if (y<Util.MOTE_RADIUS)
			y = Util.MOTE_RADIUS;
		else if (y>d.height-Util.MOTE_RADIUS)
			y = d.height-Util.MOTE_RADIUS;
		return Util.Y_MAX*y/d.height;
	}
	
    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
	
	/*
		Function called when a user clicks in the JPanel, either
		he clicks one or more time.
		We check if a mote is in this area and if so, this mote
		becomes selected. Else all the motes of the database are
		unselected.
		The control key lets the user select many motes in the same time.
	*/
	
    public void mouseClicked(MouseEvent e) 
    {
        DrawNode localNode=null;
	int x,y;
	boolean nodeClicked = false;
        
        if (nodeDatabase == null) return;
        
	for (DrawNode theNode : nodeDatabase) 
        {
            x = toVirtualX(theNode.getX());
            y = toVirtualY(theNode.getY());
	
            if (
                    (Math.abs(x-e.getX())<=Util.MOTE_RADIUS) && 
                    (Math.abs(y-e.getY())<=Util.MOTE_RADIUS)
                ) 
            {
                localNode = theNode;
		nodeClicked = true;
		break;
            }
	}
	
        if(localNode != null) 
        {
            if (nodeClicked) 
            {
		if(!e.isControlDown());
                    
                //signaling that a node has been selected
                if (this.nodeSelListener != null)
                    this.nodeSelListener.nodeSelected(localNode);
                
//                    requestPanel.unselectMotes();
//		    requestPanel.selectMote(localMote);
				//Util.debug("clik on mote id = "+localMote.getMoteId());
            } 
            else
            {    
                //signaling that a node has been selected
                if (this.nodeSelListener != null)
                    this.nodeSelListener.nodeUnselected();
            }
        } 
        else
        {
              //requestPanel.unselectMotes();
        }
	
        repaint();
    }
	
    public void mouseMoved(MouseEvent e) {}

	/*
            These three functions are used to move a mote.
            moteMoving is a Mote Object, it's the mote that 
            is dragged by the user, through the mouse.
            moteDragged is a flag to know if the user is still
            moving the mote.
	*/
	
    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) 
    {
            nodeDragged = false;
            nodeMoving = null;
    }
	
    public void mouseDragged(MouseEvent e) 
    {
       int x,y;
       
       if (nodeDatabase == null) return;
       
       if (!nodeDragged) 
       {
            for (DrawNode theNode : nodeDatabase) 
            {
                x = toVirtualX(theNode.getX());
                y = toVirtualY(theNode.getY());
		
                if (
                        (Math.abs(x-e.getX())<=Util.MOTE_RADIUS) && 
                        (Math.abs(y-e.getY())<=Util.MOTE_RADIUS)
                   ) 
                {
                    nodeMoving = theNode;
                    nodeDragged = true;
                    break;
		}
            }
	}
		
       if (nodeMoving != null) 
       {
            if (nodeDragged) 
            {
		nodeMoving.setX(toRealX(e.getX()));
		nodeMoving.setY(toRealY(e.getY()));
            } 
            else
            {
                //requestPanel.unselectMotes();
            }
            
            repaint();
	}
    }
      
      /**
       * find parent mote for a specific mote
       * @param mote mote for which parent has to be searched
       * @param moteList list of mote in the sensor network
       * @return 
       */ 
      private DrawNode getParentNode(DrawNode node, Vector<DrawNode> nodeList)
      {
        try
        {
            if (node.getNodeType() == DrawNodeType.Mote)
            {
                for (DrawNode theNode : nodeList)
                {
                    if (
                        (theNode.getNodeType() == DrawNodeType.Mote) && (theNode.getGatewayRef().equals(node.getGatewayRef())) &&
                        (theNode.getId().equals(node.getParentId())) 
                       )
                        return theNode;
                    else if (
                        (theNode.getNodeType() == DrawNodeType.Gateway) &&
                        (theNode.getId().equals(node.getParentId())) 
                       ) 
                        return theNode;
                }
            }
            else if (node.getNodeType() == DrawNodeType.Gateway)
            {
                for (DrawNode theNode : nodeList)
                {
                    if (theNode.getNodeType() == DrawNodeType.GatewayManager )
                        return theNode;
                } 
            }
                
        }
        
        catch(RemoteException rex)
        {
           System.out.println("Remote Exception :" + rex.getMessage());
           return null;
        }
        
        return null;
      }
      
      private boolean belongsToGroup(DrawNode node){
    	  boolean returnvalue=false;
    	  if(drawNodeGroup!=null){
    		  try{
        		  if(node.getNodeType()==DrawNodeType.Mote){
        			  if(node.getNodeType()==drawNodeGroup.get(0).getNodeType()){
        				  for(int i=0;i<drawNodeGroup.size();i++){
        					  if(drawNodeGroup.get(i).getMoteRef().getId().equals(node.getMoteRef().getId()))
        						  returnvalue=true;
        				  }
        			  }
        		  }
        		  
        		  if(node.getNodeType()==DrawNodeType.Gateway){
        			  if(node.getNodeType()==drawNodeGroup.get(0).getNodeType()){
        				  for(int i=0;i<drawNodeGroup.size();i++){
        					  if(drawNodeGroup.get(i).getGatewayRef().getName().equals(node.getGatewayRef().getName()))
        						  returnvalue=true;
        				  }
        			  }
        		  }
    		  }
     	     catch(RemoteException rex)
             {
                System.out.println("Remote Exception :" + rex.getMessage());
             }
    	  }
    	  return returnvalue;
      }
}
