
package client_applications.console;


import remote_interfaces.mote.*;
import remote_interfaces.*;


import java.rmi.RemoteException;

/*
    This class is a representation of a mote, based
    on the packets received from this mote.

    TODO :
                    battery support
    BUG :
*/

public class DrawNode 
{
        private Mote moteRef;
        private WSNGateway gatewayRef;
        
        //private int moteId;
        private int x, y;
        
        private DrawNodeType nodeType;
	
        //private int count, reading, hops;
	//private int parentId;
	//private int quality; 		// quality of the route to its parent
	//private long lastTimeSeen; 	// last time a message was emitted by the mote
	//private int battery;
	//private boolean sleeping;
	//private boolean modeAuto;
	//private int samplingPeriod, threshold, sleepDutyCycle, awakeDutyCycle;
	
	/*
            The constructor lets the user create a new mote by using the fields
            of an automatic message.
	 */
	public DrawNode(Mote moteRef, WSNGateway parentGatewayRef) 
        {
            //this.moteId = moteId;
            //this.hops = hops;
            this.moteRef = moteRef;
            
            /*
             * gatewayRef is set to the Gateway of the sensor network
             * the mote belongs to
             */
            this.gatewayRef = parentGatewayRef;

            this.x = (int)(Math.random() * Util.X_MAX);
            this.y = (int)(Math.random() * Util.Y_MAX);

            this.nodeType = DrawNodeType.Mote;
	}
        
        
        public DrawNode(WSNGateway gatewayRef) 
        {
            this.gatewayRef = gatewayRef;

            this.x = (int)(Math.random() * Util.X_MAX);
            this.y = (int)(Math.random() * Util.Y_MAX);

            this.nodeType = DrawNodeType.Gateway;
	}
        
        public DrawNode() 
        {
            /*
             * draw the gatewayManager in the centre of the 
             * graph
             */
            this.x = (int)(Util.X_MAX/2);
            this.y = (int)(Util.Y_MAX/2);

            this.nodeType = DrawNodeType.GatewayManager;
	}
	
	public boolean isPanCoordinator() throws RemoteException 
        {
            if (this.nodeType == DrawNodeType.Mote)
                return this.moteRef.isPanCoordinator() ;
            else
		return false;
	}
        
        public DrawNodeType getNodeType()
        {
            return this.nodeType;
        }
        
        public Mote getMoteRef()
        {
            return this.moteRef;
        }
        
        public WSNGateway getGatewayRef()
        {
            return this.gatewayRef;
        }
        
	public String getId() throws RemoteException
        { 
           if (this.nodeType == DrawNodeType.Mote)
                return this.moteRef.getId();
           else if (this.nodeType == DrawNodeType.Gateway)
                return this.gatewayRef.getName();
           else
                return "WSNGatewayManager";
        }
        
        public String getParentId() throws RemoteException
        { 
            if (this.nodeType == DrawNodeType.Mote)
            {
                if (!this.moteRef.isPanCoordinator()) return this.moteRef.getParentMote().getId();
                else return this.gatewayRef.getName();
            }
            else if (this.nodeType == DrawNodeType.Gateway)
                 return "WSNGatewayManager";
            else return "";
        }

	
	public int getX() { return x;}
	public int getY() { return y;}
	public void setX(int x) { this.x = x;}
	public void setY(int y) { this.y = y;}
	
	/*public void setCount(int count) { this.count = count;}
	public int getCount() { return count;}
	public void setReading(int reading) { this.reading = reading;}
	public int getReading() { return reading;}
	public void setHops(int hops) { this.hops = hops;}
	public int getHops() { return hops;}
	
	public void setQuality(int quality) { this.quality = quality;}
	public int getQuality() { return quality;}
	public void setLastTimeSeen(long lastTimeSeen) { this.lastTimeSeen = lastTimeSeen;}
	public long getLastTimeSeen() { return lastTimeSeen;}
	public long getTimeSinceLastTimeSeen() { 
		Date d = new Date();
		return d.getTime() - lastTimeSeen;
	}
	
	public void setBattery(int battery) { this.battery = battery;}
	public int getBattery() { return battery;}
	public void setSamplingPeriod(int samplingPeriod) { this.samplingPeriod = samplingPeriod;}
	public int getSamplingPeriod() { return samplingPeriod;}
	public void setThreshold(int threshold) { this.threshold = threshold;}
	public int getThreshold() { return threshold;}
	public void setSleepDutyCycle(int sleepDutyCycle) { this.sleepDutyCycle = sleepDutyCycle;}
	public int getSleepDutyCycle() { return sleepDutyCycle;}
	public void setAwakeDutyCycle(int awakeDutyCycle) { this.awakeDutyCycle = awakeDutyCycle;}
	public int getAwakeDutyCycle() { return awakeDutyCycle;}
	public boolean isSleeping() { return sleeping;}
	public void setSleeping() { this.sleeping = true;}
	public void setAwake() { this.sleeping = false;}
	public boolean isInModeAuto() { return modeAuto;}
	public void setModeAuto() { this.modeAuto = true;}
	public void setModeQuery() { this.modeAuto = false;}*/
}