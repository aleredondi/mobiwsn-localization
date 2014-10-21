
package client_applications.home_virtualization_application;



import remote_interfaces.*;
import java.util.ArrayList;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;

import client_applications.home_virtualization_application.RoomsType;

/*
    This class is a representation of a mote, based
    on the packets received from this mote.

    TODO :
                    battery support
    BUG :
*/

public class DrawRoom 
{

        
	    public int X_MAX = 1000;
	    public int Y_MAX = 1000;
        private int x, y;
        private static String images_path = "src/client_applications/home_management/images/";
        private String RoomName;
        private String imagePathOn;
        private String imagePathOff;
        private String[] adiacencyRoom;
        public int RoomRadius;
        private RoomsType t;
        private int presence=0;
        private int xCoordinate=0;
        private int yCoordinate=0;
	
	/*
            The constructor lets the user create a new mote by using the fields
            of an automatic message.
	 */
	public DrawRoom(String RoomName,ArrayList<String> adiacRoom,String imagePathO,RoomsType t,int presence) 
        {


            this.x = (int)(Math.random() * X_MAX);
            this.y = (int)(Math.random() * Y_MAX);
            this.RoomName=RoomName;
            this.t=t;
            System.out.println(t.toString());
            this.imagePathOn = images_path + "rooms/"+t.toString()+"On.gif";
          //  this.imagePathOn = images_path + "rooms/LivingOn.gif";
            this.imagePathOff = images_path + "rooms/"+t.toString()+"Off.png";
           
            this.presence=presence;
            
    	   this.adiacencyRoom=new String[adiacRoom.size()];
            for(int i=0;i<adiacRoom.size();i++)
            	this.adiacencyRoom[i]=adiacRoom.get(i);
           BufferedImage img = null;
           try {
                img = ImageIO.read(new File(this.imagePathOff));
            } catch (IOException e) {
            }
            this.RoomRadius=img.getWidth();
            
	}
        
        
	public void setPresence(int a){
		presence=a;
	}
	
	public int getPresence(){
		return presence;
	}
	
	public String getRoomName()  
        {
		return RoomName;
	}
	
    public String getRoomImageOn()
    {
        return imagePathOn;
    }
    public String getRoomImageOff()
    {
        return imagePathOff;
    }
	public int getX() { 
		return x;
		}
	public int getY() {
		return y;
		}
	public void setX(int x) {
		this.x = x;
		}
	public void setY(int y) { 
		this.y = y;
		}
	public String[] getAdiacencyRoom(){
		return adiacencyRoom;
	}
	public void setXCoordinate(int xx){
		xCoordinate=xx;
	}
	public void setYCoordinate(int yy){
		yCoordinate=yy;
	}
	public int getXCoordinate(){
		return xCoordinate;
	}
	public int getYCoordinate(){
		return yCoordinate;
	}

}