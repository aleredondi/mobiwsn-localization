package gateway.mote;

import gateway.InternalWSNGateway;
import gateway.InternalWSNManager;
import gateway.comm.MessageListener;
import gateway.comm.MessageSender;
import gateway.functionality.FunctionalityFactory;
import gateway.functionality.InternalFunctionality;
import gateway.group.GenericInternalGroupable;
import gateway.group.InternalGroup;
import gateway.group.mote.InternalMoteGroup;
import gateway.protocol.FunctionalityMessage;
import gateway.protocol.address.*;
import gateway.protocol.basic_message.MiddlewareMessage;
import gateway.protocol.MoteDiscoveryMessage;
import gateway.protocol.SensorReadResponseMessage;
import gateway.sensor.InternalSensor;
import gateway.sensor.SensorFactory;
import gateway.services.GenericInternalPublisher;
import gateway.services.InternalPublisher;
import gateway.services.InternalSubscriber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import common.exceptions.ExceptionHandler;
import common.exceptions.MoteUnreachableException;

import remote_interfaces.functionality.FunctionalityType;
import remote_interfaces.group.Groupable;
import remote_interfaces.mote.Mote;
import remote_interfaces.protocol.NetworkAddress;
import remote_interfaces.sensor.SensorType;


public abstract class GenericInternalMote extends GenericInternalGroupable<InternalMoteGroup> implements InternalMote, InternalPublisher<InternalMote, Object> {

	/*
	 * private fields definition
	 */
	private String moteId;

	protected short macAddress;

	private InternalNetworkAddress networkAddress;

	private short parentMote = Mote.MAC_NULL_ADDRESS;
		
	private boolean reachable;

	protected Vector<InternalMote> childMotes;
	
	/*
	 * reference to the gateway MiddlewareMessageSender component
	 */
	protected InternalWSNManager wsn;

	//private short macAddress;
	
	private GenericRemoteMote remoteMote;
	
	//private ArrayList<Sensor> sensorList;
	//private SensorManager sensor;
	private ArrayList<InternalSensor> sensorList;
	
	//private ArrayList<Functionality> functionalityList;
	//private FunctionalityManager functionality;
	private ArrayList<InternalFunctionality> functionalityList;
	
	private ArrayList<InternalGroup> myGroupList;
	
	private GenericInternalPublisher<InternalMote, Object> myPublisher;
	
	
	
	/*
	 * reference to the gateway MiddlewareMessageSender component
	 */
	//private WSNGatewayInternal gateway;
	//private MiddlewareMessageSender sender;
	

	public GenericInternalMote( String moteId, InternalNetworkAddress newtorkAddress, short macAddress, short parentMacAddress, InternalWSNManager manager )
	{
		this.macAddress = macAddress;
		this.wsn = manager;
		//System.out.println("Creo un generic con mac " + macAddress);
		//System.out.println("Creo un generic con nwk_addr " + newtorkAddress);
		
		this.moteId = moteId;
		this.networkAddress = newtorkAddress;
		this.childMotes = new Vector<InternalMote>();

		this.reachable=false;
		
		functionalityList = new ArrayList<InternalFunctionality>(); 
		sensorList = new ArrayList<InternalSensor>(); 
		myGroupList = new ArrayList<InternalGroup>();
		myPublisher = new GenericInternalPublisher<InternalMote, Object>();

		remoteMote = (GenericRemoteMote) createRemote();
		
		this.parentMote = parentMacAddress;
		updateParent();
		
		this.setMoteReachability(true);
		
		//Una volta creato mi cerco i figli
		updateChilds();

		System.out.println("------------------------------------------------------------");
		System.out.println("New mote created:");
		System.out.println("    Id = " + this.getId());
		System.out.println("    Type = " + this.getType());
		System.out.println("    Mac = " + this.getMACAddress());
		System.out.println("    Network = " + this.getNetworkAddress());
		if ( this.getParentMote() != null )
			System.out.println("    Parent = " + this.getParentMote().getId() );
		else
			System.out.println("    I'm pan coordinator!");
		System.out.println("------------------------------------------------------------\n");	
		
	}
	
	protected abstract Mote createRemote();
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.mote.InternalMote#getId()
	 */
	@Override
	public String getId() 
	{
		return this.moteId;
	}
	
	@Override
	public synchronized void setId(String id) 
	{
		this.moteId = id;
	}
	
	@Override
	public boolean hasParent() {
		if (parentMote > -1 )
			return true;
		else
			return false;
	}

	@Override
	public synchronized void setParentMote(InternalMote parentMote) {
		if ( parentMote != null ) {
			setParentMote(parentMote.getMACAddress());
		} else {
			//Unset del padre
			setParentMote( Mote.MAC_NULL_ADDRESS );
		}		
	}

	@Override
	public synchronized void setParentMote(short parentMote)
	{		
		//Se ho un padre sono raggiungibile
		if ( parentMote != Mote.MAC_NULL_ADDRESS ) {
			this.setMoteReachability(true);

			//Se cambio padre devo aggiornare i figli
			if ( this.parentMote != Mote.MAC_NULL_ADDRESS && this.parentMote != parentMote ) {
				//Avevo già un padre e quindi mi devo togliere
				InternalMote oldParent = wsn.getInternalMote(parentMote);
				if ( oldParent != null )
					oldParent.removeChild(this);

				//Se cambio padre devo rimuovere tutti i miei figli
				this.removeAllChild();
			}

			//Mi aggiungo come figlio al nuovo mote padre
			InternalMote newParent = null;
			if ( !isPanCoordinator() ) {
				newParent = wsn.getInternalMote(parentMote);
				if (newParent != null ) {
					//Se esiste il parent
					newParent.addChild(this);
				}
			}
			myPublisher.notifySubscribers(this, newParent);				
		} else {
			if ( !isPanCoordinator() ) {
				//System.out.println("Mi è stato cancellato il padre e quindi mi setto come irraggiungibile");
				this.setMoteReachability(false);
			} else
				this.setMoteReachability(true);
		}
		
		this.parentMote = parentMote;
	}
	

	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.mote.InternalMote#getNetworkAddress()
	 */
	@Override
	public InternalNetworkAddress getNetworkAddress() 
	{
		return this.networkAddress;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.mote.InternalMote#setNetworkAddress(short)
	 */
	@Override
	public synchronized void setNetworkAddress(InternalNetworkAddress networkAddress) 
	{
		this.networkAddress = networkAddress;
		myPublisher.notifySubscribers(this, networkAddress);
	}

	@Override
	public short getMACAddress() {
		return macAddress;
	}

	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.mote.InternalMote#setMACAddress(short)
	 */
	@Override
	public synchronized void setMACAddress(short macAddress)
	{
		this.macAddress = macAddress;
		myPublisher.notifySubscribers(this, new Short(macAddress));
	}
	
	@Override
	public int getChildNumber() {
		return this.childMotes.size();
	}

	@Override
	public Vector<InternalMote> getChildMotes() {
		return childMotes;
	}

	@Override
	public boolean hasChild( InternalMote mote ) {
		if ( this.childMotes.contains(mote))
			return true;
		else
			return false;
	}
	
	@Override
	public void addChild( InternalMote mote) {
		if ( !this.childMotes.contains(mote)) {
			this.childMotes.add(mote);
			remoteMote.addChild(mote);
			myPublisher.notifySubscribers(this);
		}				
	}
	

	@Override
	public void removeChild( InternalMote mote) {
		if ( childMotes.contains(mote)) {
			this.childMotes.remove(mote);
			remoteMote.removeChild(mote);
			myPublisher.notifySubscribers(this);
		}		
	}
	
	@Override
	public void removeAllChild() {
		Iterator <InternalMote> it = childMotes.iterator();
		InternalMote mote;
		
		while(it.hasNext()) {
			mote = it.next();
			if ( mote.getParentMote().equals(this) )
				mote.setParentMote(null);
		}
		
		childMotes.removeAllElements();
		myPublisher.notifySubscribers(this);
		
	}

	private void updateParent() {
		InternalMote parent = wsn.getInternalMote( this.parentMote );
		
		if (parent != null ) {
			parent.addChild(this);
		}
	}
	
	@Override
	public void updateChilds() {
		Iterator<InternalMote> it = wsn.getMoteList().iterator();
		InternalMote aMote;
		this.childMotes.removeAllElements();
				
		while(it.hasNext()) {
			aMote = it.next();
			
			if ( aMote.hasParent() ) {
				if( aMote.getParentMacAddress() == macAddress ) {
					addChild(aMote);
				}
			}
		}
		remoteMote.updateChilds();
	}

	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.mote.InternalMote#setMiddlewareMessageSender(mobiwsn.middleware.comm.MiddlewareMessageSender)
	 */
	public synchronized void setMiddlewareMessageSender(MessageSender sender)
	{
		//TODO Aggiustare
		//super.sender = sender;
	}
	
	
	@Override
	public InternalWSNManager getWsn() {
		return wsn;
	}

	@Override
	public synchronized void setWsn(InternalWSNManager wsn) {
		this.wsn = wsn;
	}


	/*public WSNGatewayInternal getGateway() {
		return this.gateway;
	}
	
	public void setWSNGateway( WSNGatewayInternal gateway ) {
		this.gateway = gateway;
	}*/
	

	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.mote.InternalMote#getParentMote()
	 */
	
	@Override
	public InternalMote getParentMote() 
	{
		return wsn.getInternalMote(parentMote);
	}
	
	@Override
	public short getParentMacAddress() 
	{
		return parentMote;
	}

	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.mote.Mote#getSensorList()
	 */
	/*public ArrayList<Sensor> getSensorList() 
	{
		return this.sensorList;
	}*/
	

	/* (non-Javadoc)
	 * @see mobiwsn.middleware.mote.Mote#isPanCoordinator()
	 */
	@Override
	public boolean isPanCoordinator() 
	{
		/*
		 * by now a requirement is that the mote attached to the gateway
		 * must have macAddress equal to PAN_COORD_ID
		 */
		if (this.macAddress == PAN_COORD_ID)
			return true;
		else return false;
	}

	
	/* (non-Javadoc)
	 * @see mobiwsn.middleware.mote.Mote#changeMoteId(java.lang.String)
	 */
	public boolean changeMoteId(String id) throws MoteUnreachableException
	{
		if (!this.isReachable()) throw new MoteUnreachableException(this.getId(), this.getNetworkAddress().getRemote(), this.getMACAddress());
		
		//		sender.sendMessageTask(new MiddlewareMessage);
		//		this.moteId = id;
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.mote.InternalMote#refreshData()
	 */
	public synchronized boolean refreshData() throws MoteUnreachableException
	{
		if (!this.isReachable()) throw new MoteUnreachableException(this.getId(), this.getNetworkAddress().getRemote(), this.getMACAddress());
		
		/*
		 * Creates a new MoteDiscoveryMessage and sends it to the mote
		 * for refreshing internalMote data
		 */
		MoteDiscoveryMessage mdMessage = new MoteDiscoveryMessage(this);
		
		System.out.println("\nSend a MoteDiscoveryMessage to: " + mdMessage.getDstMoteId() + "\n");
				
		try
		{
			wsn.sendMessageTask((MiddlewareMessage)mdMessage);
		}
		catch (Exception ex)
		{
			ExceptionHandler.getInstance().handleException(ex);
			
			return true;
		}
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.mote.InternalMote#setMoteReachability(boolean)
	 */
	public synchronized void setMoteReachability(boolean reachable)
	{
		boolean mustNotify = false;
				
		//System.out.println("Il nodo " + this.getUniqueId() + " dienta raggiungibile?" + reachable);
		//Se divento irraggiungibile anche tutti i miei figli sono irraggiungibili
		if ( !reachable ) {
			//System.out.println("Setto irraggiungibili i miei figli");
			Iterator<InternalMote> it = childMotes.iterator();
			
			while( it.hasNext() ) {
				it.next().setMoteReachability(reachable);
			}
			
			//System.out.println("Cancello la lista dei miei figli");
			this.removeAllChild();
		}
			
		if ( reachable != this.reachable ) {			
			//System.out.println("Ho cambiato il mio stato, notifico");
			mustNotify = true;
		}
		
		this.reachable = reachable;

		if( mustNotify)
			myPublisher.notifySubscribers(this, new Boolean(reachable));
	}
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.mote.InternalMote#isReachable()
	 */
	public boolean isReachable()
	{
		return this.reachable;
	}
	
	
	/*
	 * follow parentMote chain until PanCoordinator and 
	 * return PanCoordinatorMoteId;
	 */
	protected String getPanCoordinatorId()
	{
		InternalMote mote = this;
		while (!mote.getParentMote().isPanCoordinator())
			mote = mote.getParentMote();
		
		return mote.getId();
		
	}
	
	
	//SensorManager
	@Override
	public void addSensor(InternalSensor sensor) throws Exception {
		
		synchronized(sensorList) {
			if (!sensorList.contains(sensor)) {
				sensorList.add(sensor);
			
				remoteMote.addSensor(sensor);
			}
		}
	}

	
	@Override
	public void addSensor(byte sensorId, SensorType type) {
		boolean found = false;
		
		synchronized(sensorList) {
			for ( InternalSensor sens : sensorList ) {
				if ( type.equals(sens.getType()) ) {
					found = true;
					break;
				}
			}
		
			if (!found) {
				InternalSensor sens_2_add = SensorFactory.createSensorInstance(sensorId, type, this);
				if ( sens_2_add != null ) {
					sensorList.add(sens_2_add);
				
					remoteMote.addSensor(sens_2_add);
				}
			}
		}
		
	}
	
	/**
	 * Removes a sensor to the sensor list
	 * @param sensor Sensor object to be removed
	 * @throws Exception 
	 * @throws Exception an Exception is thrown if the Sensor object isn't contained in sensor list
	 */
	@Override
	public void removeSensor(byte sensorId, SensorType type) throws Exception {
		InternalSensor aSensor;
		aSensor = findSensorByIdAndType(sensorId, type );

		synchronized(sensorList) {
			if ( aSensor != null ) {
				sensorList.remove(aSensor);
			
				remoteMote.removeSensor(aSensor);

			} else 
				throw new Exception("sensorList does not contain this sensor! Mote name:" + getId() + ";" +
									"network address:" + getNetworkAddress() + ";"+ 
									"mac address:" + getMACAddress());
		}
	}
	
	@Override
	public void removeSensor( InternalSensor sensor) throws Exception {
		synchronized(sensorList) {
			if (!this.sensorList.contains(sensor))
				throw new Exception("sensorList does not contain this func! Mote name:" + this.getId() + ";" +
								 	"network address:" + this.getNetworkAddress() + ";"+ 
								 	"mac address:" + this.getMACAddress());
			else {
				this.sensorList.remove(sensor);
			
				remoteMote.removeSensor(sensor);
			}
		}
		
	}
	
	@Override
	public boolean isSensorInList( byte sensorId, SensorType type ) {
		if (findSensorByIdAndType(sensorId, type) != null)
			return true;
		else return false;
	}

	@Override
	public int getSensorNumber() {
		synchronized(sensorList) {
			return sensorList.size();
		}
	}
	
	@Override
	public ArrayList<InternalSensor> getSensorList() {
		synchronized(sensorList) {
			return sensorList;
		}
	}
	
	//Functionality
	@Override
	public void addFunctionality(byte id, FunctionalityType type) throws Exception {
		boolean found = false;
		
		synchronized(functionalityList) {
			for ( InternalFunctionality func : functionalityList ) {
				if ( type.equals(func.getType()) ) {
					found = true;
					break;
				}
			}
		}
		
		if (!found) {
			InternalFunctionality func_2_add = FunctionalityFactory.createFunctionalityInstance(id, type, this);
			if ( func_2_add != null ) {
				functionalityList.add(func_2_add);
				
				remoteMote.addFunctionality(func_2_add);
			}
		}
		
	}
	
	@Override
	public void addFunctionality(InternalFunctionality func) throws Exception {
		synchronized(functionalityList) {
			if (!functionalityList.contains(func)) {
				functionalityList.add(func);
			
				remoteMote.addFunctionality(func);
			}
		}
	}

	@Override
	public void removeFunctionality(InternalFunctionality func) throws Exception {
		synchronized(functionalityList) {
			if (!this.functionalityList.contains(func))
				throw new Exception("functionalityList does not contain this func! Mote name:" + this.getId() + ";" +
									"network address:" + this.getNetworkAddress() + ";"+ 
									"mac address:" + this.getMACAddress());
			else {
				this.functionalityList.remove(func);
			
				remoteMote.removeFunctionality(func);
			}
		}
		
	}

	/**
	 * Removes a sensor to the sensor list
	 * @param sensor Sensor object to be removed
	 * @throws Exception an Exception is thrown if the Sensor object isn't contained in sensor list
	 */
	@Override
	public synchronized void removeFunctionality(byte funcId, FunctionalityType type)  throws Exception {
		InternalFunctionality aFunc;
		aFunc = findFunctionalityByIdAndType(funcId, type );

		synchronized(functionalityList) {
			if ( aFunc != null ) {
				functionalityList.remove(aFunc);
			
				remoteMote.removeFunctionality(aFunc);

			} else
				throw new Exception("sensorList does not contain this sensor! Mote name:" + getId() + ";" +
									"network address:" + getNetworkAddress() + ";"+ 
									"mac address:" + getMACAddress());
		}
	}
	
	
	@Override
	public int getFunctionalityNumber() {
		synchronized(functionalityList) {
			functionalityList.trimToSize();
			return functionalityList.size();
		}
	}
	
	@Override
	public ArrayList<InternalFunctionality> getFunctionalityList() {		
		synchronized(functionalityList) {
			return functionalityList;
		}
	}
	
	@Override
	public boolean isFunctionalityInList(byte id, FunctionalityType type) {
		if (findFunctionalityByIdAndType(id, type) != null)
			return true;
		else return false;
	}


	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	/*@Override
	public InternalSensor getFunctionality(byte id) {
		return functionality.g;
	}*/

	/*@Override
	public InternalSensor getSensor(byte id) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!



	/*@Override
	public ArrayList<Functionality> getRemoteFunctionalityList() {
		return functionality.getRemoteList();
	}*/
	
	
	
	public /*Interfaccia groupable*/
	String getUniqueId() {
		return moteId;
	}
	
	@Override
	public GenericRemoteMote getRemote() {
		return remoteMote;
	}


	//Interfaccia MessageListener
	public void messageReceived( MiddlewareMessage message ) {
		boolean found = false;

		switch(message.getMessageType())
		{
			case SENSOR_READ_RESPONSE:
				try {
					
					for (InternalSensor sensor : sensorList ) {
						if( sensor.getId() == ((SensorReadResponseMessage) message).getReadRespSensorId() ) {
							sensor.messageReceived((SensorReadResponseMessage) message);
							found = true;
						}
					}
					
					if (!found) //throw new Exception("Funzionalit� non trovata");
						System.out.println("Funzionalit� non trovata");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			break;
	
			case FUNCTIONALITY_ANNOUNCEMENT:

				try {
					
					for (InternalFunctionality func : functionalityList ) {
						if( func.getId() == ((FunctionalityMessage) message).getFunctionalityId()) {
							func.messageReceived( (FunctionalityMessage) message);
							found = true;
						}
					}
					
					if (!found) //throw new Exception("Funzionalit� non trovata");
						System.out.println("Funzionalit� non trovata");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
	
			default:
				//throw UnknownMessageException(message);
		}
		
		this.setMoteReachability(true);
		
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
	
	
	/**
	 * Compare the specified Object with this MicazInternalMote for equality
	 * @param o the Object to be compared with this MicaZInternalMote
	 * @return true if id, mac address and network address of the two object
	 * are equal
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof GenericInternalMote)
		{
			GenericInternalMote mote = (GenericInternalMote) o;
			
			if (
				mote.getId().equals(this.getId()) &&
				mote.getMACAddress() == this.getMACAddress() &&
				mote.getNetworkAddress() == this.getNetworkAddress()
				)
				return true;
			else
				return false;
		}
		else
			return false;
	}

	
	
	
	private InternalFunctionality findFunctionalityByIdAndType(byte sensorId, FunctionalityType type ) {
		Iterator<InternalFunctionality> it = functionalityList.iterator();
		InternalFunctionality found = null;
		
		while(it.hasNext()) {
			found = it.next();
			if ( found.getId() == sensorId && found.getType().equals(type) )
				return found;
		}
		
		return null;
		
	}

	private InternalSensor findSensorByIdAndType(byte sensorId, SensorType type ) {
		Iterator<InternalSensor> it = sensorList.iterator();
		InternalSensor found = null;
		
		while(it.hasNext()) {
			found = it.next();
			if ( found.getId() == sensorId && found.getType().equals(type) )
				return found;
		}
		
		return null;
		
	}
	
	@Override
	public InternalWSNGateway getWSNGatewayParent() {
		return wsn.getGateway();
	}
	
	
	
	@Override
	public void addSubscriber(InternalSubscriber<InternalMote, Object> s) {
		myPublisher.addSubscriber(s);
	}


	@Override
	public void removeSubscriber(InternalSubscriber<InternalMote, Object> s) {
		myPublisher.removeSubscriber(s);
	}

}
