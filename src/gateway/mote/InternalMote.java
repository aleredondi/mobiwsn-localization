package gateway.mote;

import java.util.ArrayList;
import java.util.Vector;

import common.exceptions.MoteUnreachableException;

import remote_interfaces.Remotizable;
import remote_interfaces.functionality.FunctionalityType;
import remote_interfaces.mote.MoteType;
import remote_interfaces.sensor.SensorType;
import gateway.InternalWSNManager;
import gateway.config.ConfigurationManager;
import gateway.functionality.InternalFunctionality;
import gateway.group.InternalGroupable;
import gateway.group.mote.InternalMoteGroup;
import gateway.protocol.address.*;
import gateway.protocol.basic_message.MiddlewareMessage;
import gateway.sensor.InternalSensor;
import gateway.services.InternalPublisher;

/**
 * @author Davide Roveran
 *
 * interface of a generic internal mote object
 * used by WSNGateway object to represent motes in the WSN
 */
public interface InternalMote extends InternalGroupable<InternalMoteGroup>, Remotizable, InternalPublisher<InternalMote, Object>
{
	static short PAN_COORD_ID = Short.parseShort(ConfigurationManager.getInstance().getParameter("pan_coord_id"));
	static String MOTE_BROADCAST_ID = "FFFFFFFF";
	static String MOTE_NULL_ID = "00000000";
	static short NWK_NULL_ADDRESS = (short)0xFFFF;

	/**
	 * @return identifier assigned to the Mote
	 */
	String getId();
	
	MoteType getType();
	
	/**
	 * sets the identifier of the mote
	 * 
	 * @param id identifier to be assigned to the mote
	 */
	void setId(String id);
	
	/**
	 * 
	 * @return true if this Mote is PAN Coordinator of the network
	 */
	boolean isPanCoordinator();

	/**
	 * @return True if the corresponding mote in the WSN is still reachable
	 */
	public boolean isReachable();

	/**  
	 * @return MAC address of the mote
	 */
	short getMACAddress();

	/**
	 * sets the mac address of the InternalMote object
	 * 
	 * @param macAddress the mac address to set
	 */
	void setMACAddress(short macAddress);

	/**
     *
	 * @return current network address of the mote
	 */
	InternalNetworkAddress getNetworkAddress();
	
	/**
     * sets the network address of the InternalMote object 
	 * 
	 * @param networkAddress the network address to set
	 */
	void setNetworkAddress(InternalNetworkAddress networkAddress);
	
		
	/**
	 * 
	 * @return mote's Parent InternalMote (may be null if this mote is PAN Coordinator)
	 */
	boolean hasParent();
	InternalMote getParentMote();
	short getParentMacAddress();
	
	/**
	 * set the reference to the parent mote
	 * @param parentMote the parent mote;
	 */
	void setParentMote(InternalMote parentMote);
	void setParentMote(short parentacAddress);
	
	/**
	 * @return child mote list (may be empty if the mote is a leaf)
	 */
	int getChildNumber();
	
	Vector<InternalMote> getChildMotes();
	
	boolean hasChild( InternalMote mote );
	
	void addChild(InternalMote mote);
	
	void removeChild(InternalMote mote);
	
	void removeAllChild();	

	public void updateChilds();
	
	InternalWSNManager getWsn();
	
	void setWsn( InternalWSNManager gw );
	
	/**
	 * Set the MiddlewareMessageSender object of the mote
	 * 
	 * @param sender the sender
	 */
	//public void setMiddlewareMessageSender(MiddlewareMessageSender sender);
	
	/**
	 * Sends a mote_discovery message to this InternalMote and waits 
	 * for a mote_announcement message with the updated data
	 * @return true if the operation is overall successful (mote_announcement
	 * message is receivd and updates are performed);
	 * @throws MoteUnreachableException 
	 */
	public boolean refreshData() throws MoteUnreachableException;
	
	/**
	 * Sends a message to the mote represented by this InternalMote to change
	 * mote identifier
	 * @param moteId the identifier to be set
	 * @return true if the operation is successful
	 * @throws MoteUnreachableException 
	 */
	public boolean changeMoteId(String moteId) throws MoteUnreachableException;
	
	/**
	 * Sets reachability state of InternalMote
	 * 
	 * @param reachable reachability state to be set
	 */
	public void setMoteReachability(boolean reachable);
	
		
	public void messageReceived( MiddlewareMessage message );
	

	//Sensor
	void addSensor( byte sensorId, SensorType type ) throws Exception;
	void addSensor( InternalSensor sensor ) throws Exception;
	
	void removeSensor( InternalSensor sensor ) throws Exception;
	void removeSensor( byte sensorId, SensorType type ) throws Exception;
	
	//InternalSensor getSensor(byte id );
	/**
	 * @return List of Sensors currently attached to the node;
	 */
	int getSensorNumber();
	ArrayList<InternalSensor> getSensorList();
	boolean isSensorInList(byte id, SensorType type);
	
	

	//Functionality
	void addFunctionality( byte sensorId, FunctionalityType type ) throws Exception;
	void addFunctionality( InternalFunctionality func ) throws Exception;
	
	void removeFunctionality( InternalFunctionality func ) throws Exception;
	void removeFunctionality( byte sensorId, FunctionalityType type ) throws Exception;
	
	/**
	 * @return List of Functionalitis currently installed on the node;
	 */
	int getFunctionalityNumber();
	ArrayList<InternalFunctionality> getFunctionalityList();
	//ArrayList<Functionality> getRemoteFunctionalityList();
	boolean isFunctionalityInList(byte id, FunctionalityType type);
	
	
	//Per avere la lista dei grupi a cui appartengo
	
	
}
