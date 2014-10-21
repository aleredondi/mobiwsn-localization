
package gateway.sensor;


import gateway.InternalWSNGateway;
import gateway.group.GenericInternalGroupable;
import gateway.group.InternalGroup;
import gateway.group.sensor.InternalSensorGroup;
import gateway.mote.InternalMote;
import gateway.protocol.SensorReadMessage;
import gateway.protocol.SensorReadResponseMessage;
import gateway.services.GenericInternalPublisher;
import gateway.services.InternalPublisher;
import gateway.services.InternalSubscriber;

import remote_interfaces.result.ValueResult;
import remote_interfaces.sensor.*;
import remote_interfaces.sensor.Sensor.ReadTimes;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.measure.Measure;
import javax.measure.quantity.Duration;
import javax.measure.quantity.Quantity;

import javax.measure.unit.SI;

import common.exceptions.ExceptionHandler;
import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;
import common.exceptions.ResponseTimeoutException;


/**
 * Class that implements the generic class sensor, and so allows the user to implement only a part of the methods
 * defined in the Sensor interface. In fact the basic methods are here written and ready to be used from the object 
 * of the specific sensor loaded from user on the gateway  
 * @author Alessandro Laurucci
 * @version 1.1
 */
public abstract class GenericInternalSensor<RV extends ValueResult, V, Q extends Quantity> extends GenericInternalGroupable<InternalSensorGroup> implements InternalSensor<RV, V, Q>, InternalPublisher<GenericInternalSensor<RV,V,Q>,RV>
{	
	protected GenericRemoteSensor<RV, V, Q> remoteSensor;
	protected byte sensorId; //identification of sensor
	protected SensorType sensorType; //type of sensor
	
	protected InternalMote owner; //reference to the mote this sensor is installed on
	private String uniqueId;
	private GenericInternalPublisher<GenericInternalSensor<RV, V, Q>,RV> publisher;
	
	private Measure<V,Q> rangeTop;
	private Measure<V,Q> rangeDown;

	private RV readValue;
	
	private ArrayList<InternalGroup> myGroupList;
		

	/**
	 * Constructor
	 * @param sensorId: id to be assigned to the Sensor
	 * @param owner : Mote object that represents the mote this sensor is installed on
	 * @param sender : reference to the MiddlewareMessageSender object used to send messages to the WSN
	 * @param gw is the name of the gateway to which the mote, and so also the sensor, is connected
	 */
	public GenericInternalSensor( byte sensorId, InternalMote owner, Measure<V,Q> rangeTop, Measure<V,Q> rangeDown ) throws RemoteException
	{
		this.sensorId = sensorId;
		this.sensorType = SensorType.UNDEFINED;
		this.owner = owner;
		remoteSensor = createRemote();
		publisher = new GenericInternalPublisher<GenericInternalSensor<RV, V, Q>,RV>();
//		readValues = new ArrayList<RV>();
		
		uniqueId = new String( owner.getId().concat( new Byte( sensorId ).toString() ) );
		
		this.rangeTop = rangeTop;
		this.rangeDown = rangeDown;
	}
	
	protected abstract GenericRemoteSensor createRemote();
	
	
	/**
	 * Compare the specified Object with this Sensor for equality
	 * @param o the Object to be compared with this Sensor
	 * @return true if Id and OwnerMoteId of the two object are equal
	 */
	@Override
	public boolean equals(Object o)
	{
		try
		{
			if (o == null)
				System.out.println("o is null while comparing with : " + this.getId() +"; owner :" + this.getOwnerMoteId());
		}
		catch(Exception ex){System.out.println("Exception getOwner");}
		
		if (o instanceof GenericInternalSensor)
		{
			GenericInternalSensor<RV, V, Q> basicSensor = (GenericInternalSensor<RV, V, Q>) o;
			if (basicSensor.getId() == 0) System.out.println("Exception basicSensor.getId()");
			if ( (this.getId() == basicSensor.getId()) && (this.getOwnerMoteId().equals(basicSensor.getOwnerMoteId())))
				return true;
			else 
				return false;
		}
		else
			return false;
	}
	
	
	/**
	 * 
	 * @return Identifier of this Sensor
	 */
	public byte getId()
	{
		return this.sensorId;
	}
	
	
	/**
	 * 
	 * @return SensorType of this Sensor
	 */
	public SensorType getType()
	{
		return this.sensorType;
	}
	
	
	/**
	 * 
	 * @return Identifier of the Mote this Sensor is installed on 
	 */
	public String getOwnerMoteId()
	{
		return this.owner.getId();
	}

	public InternalMote getOwnerMote()
	{
		return this.owner;
	}

	/**
	 * 
	 * @return the Mote this Sensor is installed on 
	 */
	public InternalMote getOwner()
	{
		return this.owner;
	}
	
	
	/**
	 * Perform a read operation on Sensor and return an object that contain the value reed from the sensor
	 * @return an Object result
	 * @throws MiddlewareException 
	 * @throws MoteUnreachableException 
	 * @throws ResponseTimeoutException 
	 */
	synchronized private void readNewValue() throws MiddlewareException, MoteUnreachableException, ResponseTimeoutException
	{
		
		if ( owner.isReachable() ) {
			SensorReadMessage srMessage = new SensorReadMessage(this);
		
			//TODO: per ora tutte le read sono istantanee, bisognerebbe passare il parametro del tipo di read
			//      attraverso l'interfaccia remota Sensor ed iplementare la gestione delle diverse sensor_read_response		
			//srMessage.setSensorReadType((byte) SensorReadType.SIMPLE_SENSOR_READ.ordinal());
			//		ora il tipo SIMPLE_SENSOR_READ è impostato direttamente nel costruttore
			
			owner.getWsn().sendMessageTask(srMessage);
		} else throw new MoteUnreachableException(owner.getId(), owner.getNetworkAddress().getRemote(), owner.getMACAddress() );
		
		//sensor_value could be meaningless if a responseTimeoutException has been raised
		//return this.getValue();
	}
	
		
	
//	ABSTRACT METHODS	
	/**
	 * Perform a read operation the last value reed from Sensor and return an object that contain this value
	 * @return an Object result
	 * @throws MiddlewareException 
	 * @throws MoteUnreachableException 
	 * @throws ResponseTimeoutException 
	 * @throws RemoteException 
	 */
	synchronized public RV getValue(Measure<Integer,Duration> maxOldness) throws MiddlewareException, MoteUnreachableException, ResponseTimeoutException {
		Calendar actual = new GregorianCalendar();
		actual.add(Calendar.MILLISECOND, -maxOldness.intValue( SI.MILLI(SI.SECOND) ));
		
		return getValue(actual);
	}
	
	synchronized public RV getValue( Calendar maxOldness ) throws MiddlewareException, MoteUnreachableException, ResponseTimeoutException {
//		RV val = readValues.get(readValues.size()); 
		if ( readValue != null ) {
			try {
				if ( readValue.getTimeRead().before(maxOldness) )
					readNewValue();
					//val = readValues.get(readValues.size()); 
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			readNewValue();
		
		return readValue;
		
	}
	
	/**
	 * This method is used to get two value that represent the range for the sensor reading
	 * @return the range
	 */
	public Measure<V,Q> getMaxVal() {
		return rangeTop;
	}

	public Measure<V,Q> getMinVal() {
		return rangeDown;
	}

	
//	private RV getValue() {
//		return readValue;
//	}
	
//METHODS NOT IMPLEMENTED	
		
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.mote.Sensor#setPeriodicRead(boolean, int)
	 */
	/*@Override
	public boolean setPeriodicRead( ReadTimes time, int sampleNumber )
	{
		SensorReadMessage read = new SensorReadMessage(this, (byte) time.ordinal() );
		return false;
	}*/
	
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.mote.Sensor#setReadByThreshold(boolean, byte[], byte[])
	 */
	public boolean setReadByThreshold(boolean boSet, 
								      byte[] upperThreshold, 
								      byte[] lowerThreshold)
	{
//		MiddlewareMessage message = new MiddlewareMessage(this.owner.getName(false));
//		
//		message.setEnable(boSet);
//		message.setThreshold(upperThreshold, lowerThreshold);
//		sender.sendMessageTask(message);
//		
//		return message.isWritten();
		
		return false;
	}
	
	
	protected abstract void convertValue( short val );
	
	protected void setValue( RV val ) {
		readValue = val;
	}
	
	@Override
	public void messageReceived( SensorReadResponseMessage message) {
		if( message.getSensorValueLen() == 1 )
			convertValue(message.getSensorValue(0));
		else {
			//TODO Qui va implementata la ricezione di più valori
			System.out.println("Ho ricevuto più valori ma non c'è lìimplementazione");
		}
			
	}
	
	
	@Override
	public UnicastRemoteObject getRemote() {
		return remoteSensor;
	}
	
	/*Metodi del publisher*/
	@Override
	public void addSubscriber(
			InternalSubscriber<GenericInternalSensor<RV,V,Q>, RV> s) {
		publisher.addSubscriber(s);
		
	}


	@Override
	public void removeSubscriber(
			InternalSubscriber<GenericInternalSensor<RV,V,Q>, RV> s) {
		publisher.removeSubscriber(s);
		
	}
	
	protected void notifySubscribers(RV code) {
		publisher.notifySubscribers(this, code);
		try {
			remoteSensor.notifySubscribers(code);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Override
	public String getUniqueId() {
		return uniqueId;
	}
	
	/*@Override
	public boolean addedToGroup( InternalGroup group ) {
		boolean ans = false;
		
		if ( myGroupList.contains(group) )
			ans = true;
		else {
			myGroupList.add(group);
			ans = true;
		}
		
		return ans;
	}

	@Override
	public boolean removedFromGroup( InternalGroup group ) {
		boolean ans = false;
		
		if ( myGroupList.contains(group) ) {
			myGroupList.remove(group);
			ans = true;
		} else {
			ans = false;
		}
		
		return ans;
	}
	
	@Override
	public boolean isPartOfGroup( InternalGroup group ) {
		return myGroupList.contains(group);
	}
	
	@Override
	public ArrayList<InternalGroup> getMyGroups() {
		return myGroupList;
	}*/
	
	@Override
	public InternalWSNGateway getWSNGatewayParent() {
		return owner.getWSNGatewayParent();
	}

	
}
