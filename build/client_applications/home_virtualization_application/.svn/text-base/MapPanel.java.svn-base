package client_applications.home_virtualization_application;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import java.util.*;
import java.rmi.RemoteException;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;



class MapPanel extends JPanel implements MouseListener, MouseMotionListener 
{
	private ArrayList<DrawRoom> roomDatabase;
	private static final int SIZE_COLOR_ARRAY = 10;
	private static Color colorQualityArray[];
	private String roomLegend, parentIdLegend, moteIdLegend,  readingLegend, routeLegend;
	private int Room_x_Diameter=150;
	private int Room_y_Diameter=112;
	private int bordWidth=10;
	private boolean roomDragged = false;
	private DrawRoom roomMoving;
	public  int X_MAX = 980;
	public  int Y_MAX = 980;
	public boolean isDispose=false;
        
    private RoomSelectionListener roomSelListener;
        
	
	public MapPanel() 
        {
		
                // the quality array starts from good quality (green) to bad quality (red)
		colorQualityArray = new Color [SIZE_COLOR_ARRAY];
		
                for (int i=0; i<SIZE_COLOR_ARRAY; i++)
			colorQualityArray[i] = new Color(255*i/(SIZE_COLOR_ARRAY-1), 255-255*i/(SIZE_COLOR_ARRAY-1), 0);
		
                // The Strings are initialized at "none" for most of them
		roomLegend = "circle" ;  parentIdLegend = "text + gradient"; 
                moteIdLegend = "text + gradient"; 
		readingLegend = "text"; routeLegend = "line + label";
		
                addMouseListener(this);
		addMouseMotionListener(this);
                
                this.roomSelListener = null;
	}
        
        
        public void addRoomSelectionListener(RoomSelectionListener theListener)
        {
            this.roomSelListener = theListener;
        }
        
        public void removeRoomSelectionListener()
        {
            this.roomSelListener = null;
        }
        
        public void setRoomDatabaseAndRepaint(ArrayList<DrawRoom> roomDatabase)
        {
            this.roomDatabase=roomDatabase;
            this.repaint();
        }
        public void isDispose(boolean a){
        	isDispose=a;
        	repaint();
        }
        
        public ArrayList<DrawRoom> getRoomList(){
        	return roomDatabase;
        }
	
        @Override
	public void paint(Graphics g) 
        {
        	
		Graphics2D g2 = (Graphics2D) g;
		
		if(isDispose==false){
			// we draw first a white area with a border
			Dimension d = getSize();
			g2.setPaint(Color.BLACK);
			g2.fill(new Rectangle2D.Double(0, 0, d.width, d.height));
			g2.setPaint(Color.red);
			g2.fill(new Rectangle2D.Double(bordWidth/4, bordWidth/4, d.width-(bordWidth/2), d.height-(bordWidth/2)));
			g2.setPaint(Color.white);
			g2.fill(new Rectangle2D.Double(bordWidth/2, bordWidth/2, d.width-(bordWidth), d.height-(bordWidth)));
	               
			
	                this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
	                
	                // we run through the list of motes and display 
			// in first the routes and next each mote
				
			
	                
	                if (roomDatabase == null) return;
	 
	                    // we run through the available legends, to know which one is selected
	                    // and we launch the corresponding functions
	                drawTheLink(roomDatabase, g2);   
	                for (DrawRoom drawRoom : roomDatabase)  { 
	                    	drawTheRoom(drawRoom, g2);
	                    	drawTheText(drawRoom, g2);
	                }
                    
		}
		else{
			g=null;
			g.dispose();
		}
             	
	}
	
	/*
            This function draws a mote and adds a 
            black border if the mote is selected.
            Mote.getX() gives the center of the mote, so we have to get 
            the top left corner for the functions fill and draw.
	*/
	private void drawTheRoom(DrawRoom room, Graphics2D g2) 
        {
                if(room != null) 
                {
                	BufferedImage img = null;
                    try {
                    	if(room.getPresence()>0)
                    		img = ImageIO.read(new File(room.getRoomImageOn()));
                    	
                    	else
                    		img = ImageIO.read(new File(room.getRoomImageOff()));
                    } catch (IOException e) {
                    }
                    
                    g2.drawImage(img, toVirtualX(room.getX())-Room_x_Diameter/2, toVirtualY(room.getY())-Room_y_Diameter/2, null);
                    room.setXCoordinate(toVirtualX(room.getX())-Room_x_Diameter/2);
                    room.setYCoordinate(toVirtualY(room.getY())-Room_y_Diameter/2);
                    Room_x_Diameter=img.getWidth();
                    Room_y_Diameter=img.getHeight();

                }        
	}
	
	/*
		This function draws some text near of a mote. It takes in
		parameter a string representing the text to print. The 
		character "\n" means a new line.
		Mote.getX() gives the center of the mote, so we have to get 
		the top left corner for the functions fill and draw
	*/
	
	private void drawTheText(DrawRoom room, Graphics2D g2) 
        {
		if(room != null) 
                {
			String text=room.getRoomName();
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
                                              toVirtualX(room.getX())-20, 
                                              toVirtualY(room.getY())-Room_y_Diameter/2+(12*i++)-6);
			}while(!exit);
		}
	}
        
	/*
		This function draws a route from the mote to its parent if
		it exists, and deals with the quality and lastTimeSeen variables.
	*/
	private void drawTheLink(ArrayList<DrawRoom> roomdatabase,  Graphics2D g2) 
        {
		String text="";
            if(roomdatabase != null) 
            {
            	for(DrawRoom room1:roomdatabase){
            		String[] adiacencyRoom=room1.getAdiacencyRoom();
            		for(int ii=0;ii<adiacencyRoom.length;ii++){
            			DrawRoom room2=null;
            			int k=0;
            			boolean found=false;
            			while(found==false){
            				if(roomDatabase.get(k).getRoomName().equals(adiacencyRoom[ii])){
            					found=true;
            					room2=roomDatabase.get(k);
            				}
            				k=k+1;
            			}
                        BasicStroke stroke = new BasicStroke(2.0f);
             	        g2.setStroke(stroke);
                        g2.setPaint(new Color(125,144,147));
             		    g2.draw(new Line2D.Double(	
                                                         toVirtualX(room1.getX()), 
                                                         toVirtualY(room1.getY()), 
                                                         toVirtualX(room2.getX()), 
                                                         toVirtualY(room2.getY())
                                                      )
                                    );
                    }
            		}
            	}
	}
	
	/*
		These both functions translate the x and y values of the mote
		to values for the screen.
	*/
	
	private int toVirtualX(int x) { 
		Dimension d = getSize();
		int tmp = d.width*x/X_MAX;
		if (tmp<Room_x_Diameter/2+bordWidth)					// we prevent x to go past the panel
			return Room_x_Diameter/2+bordWidth;
		else if (tmp>d.width-Room_x_Diameter/2-bordWidth)
			return d.width-Room_x_Diameter/2-bordWidth;
		else
			return tmp;
	}
	
	private int toVirtualY(int y) { 
		Dimension d = getSize();
		int tmp = d.height*y/Y_MAX;
		if (tmp<Room_y_Diameter/2+bordWidth)					// we prevent y to go past the panel
			return Room_y_Diameter/2+bordWidth;
		else if (tmp>d.height-Room_y_Diameter/2-bordWidth)
			return d.height-Room_y_Diameter/2-bordWidth;
		else
			return tmp;
	}
	
	/*
		These both functions translate the x and y values of the screen
		to real values for the mote.
	*/
	
	private int toRealX(int x) { 
		Dimension d = getSize();
		if (x<Room_x_Diameter/2+bordWidth)
			x = Room_x_Diameter/2+bordWidth;
		else if (x>d.width-Room_x_Diameter/2-bordWidth)
			x = d.width-Room_x_Diameter/2-bordWidth;
		return X_MAX*x/d.width;
	}
	
	private int toRealY(int y) { 
		Dimension d = getSize();
		if (y<Room_y_Diameter/2+bordWidth)
			y = Room_y_Diameter/2+bordWidth;
		else if (y>d.height-Room_y_Diameter/2-bordWidth)
			y = d.height-Room_y_Diameter/2-bordWidth;
		return Y_MAX*y/d.height;
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
        DrawRoom localRoom=null;
	int x,y;
	boolean roomClicked = false;
        
        if (roomDatabase == null) return;
        
	for (DrawRoom theRoom : roomDatabase) 
        {
            x = toVirtualX(theRoom.getX());
            y = toVirtualY(theRoom.getY());
	
            if (
                    (Math.abs(x-e.getX())<=Room_x_Diameter/2) && 
                    (Math.abs(y-e.getY())<=Room_y_Diameter/2)
                ) 
            {
                localRoom = theRoom;
		roomClicked = true;
		break;
            }
	}
	
        if(localRoom != null) 
        {
            if (roomClicked) 
            {
		if(!e.isControlDown());
                    
                //signaling that a node has been selected
                if (this.roomSelListener != null)
                    this.roomSelListener.roomSelected(localRoom);
                
//                    requestPanel.unselectMotes();
//		    requestPanel.selectMote(localMote);
				//Util.debug("clik on mote id = "+localMote.getMoteId());
            } 
            else
            {    
                //signaling that a node has been selected
                if (this.roomSelListener != null)
                    this.roomSelListener.roomUnselected();
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
            roomDragged = false;
            roomMoving = null;
    }
	
    public void mouseDragged(MouseEvent e) 
    {
       int x,y;
       
       if (roomDatabase == null) return;
       
       if (!roomDragged) 
       {
            for (DrawRoom theNode : roomDatabase) 
            {
                x = toVirtualX(theNode.getX());
                y = toVirtualY(theNode.getY());
		
                if (
                        (Math.abs(x-e.getX())<=Room_x_Diameter/2) && 
                        (Math.abs(y-e.getY())<=Room_y_Diameter/2)
                   ) 
                {
                    roomMoving = theNode;
                    roomDragged = true;
                    break;
		}
            }
	}
		
       if (roomMoving != null) 
       {
            if (roomDragged) 
            {
		roomMoving.setX(toRealX(e.getX()));
		roomMoving.setY(toRealY(e.getY()));
            } 
            else
            {
                //requestPanel.unselectMotes();
            }
            
            repaint();
	}
    }
      



}
