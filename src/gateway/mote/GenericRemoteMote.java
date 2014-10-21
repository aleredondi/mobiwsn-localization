package gateway.mote;

import gateway.functionality.InternalFunctionality;
import gateway.sensor.InternalSensor;
import gateway.services.GenericRemotePublisher;
import gateway.services.InternalSubscriber;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import remote_interfaces.WSNGateway;
import remote_interfaces.functionality.Functionality;
import remote_interfaces.functionality.FunctionalityType;
import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteType;
import remote_interfaces.protocol.NetworkAddress;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.services.Publisher;
import remote_interfaces.services.Subscriber;

import common.classes.ServerClassLoader;
import common.exceptions.MoteUnreachableException;

public abstract class GenericRemoteMote extends UnicastRemoteObject implements Mote, Publisher<Mote, Mote>, InternalSubscriber<InternalMote, Object> {

	protected Vector<Mote> childMotes;
	private ArrayList<Functionality> functionalityList;
	private ArrayList<Sensor> sensorList;
	protected GenericRemotePublisher<Mote, Mote> myPublisher;

	/*
	 * reference to the InternalMote object hidden
	 * by this MicaZMote
	 */
	private InternalMote internalMote;

	
	//object used to synchronize
	private Object classLock;

	//list of the object loaded from client and used to interact with this mote
	private ArrayList<Object> functions;


	protected GenericRemoteMote(InternalMote internalMote) throws RemoteException {
		super();
		this.internalMote = internalMote;
		this.classLock=new Object();
		functionalityList = new ArrayList<Functionality>(); 
		sensorList = new ArrayList<Sensor>();
		this.childMotes = new Vector<Mote>();
		myPublisher = new GenericRemotePublisher<Mote, Mote>();
		internalMote.addSubscriber(this);
	}

	/**
	 * 
	 * @return identifier assigned to the Mote
	 */
	public String getId() throws RemoteException {
		return internalMote.getId();
	}
	
	public MoteType getType() throws RemoteException {
		return internalMote.getType();
	}

	
	/**
	 * send a message to the mote represented by this Mote, to 
	 * change the identifier of the mote
	 * 
	 * @param id identifier to be assigned to the mote
	 * @return true if the operation is successful;
	 */
	public boolean changeId(String id) throws RemoteException, MoteUnreachableException {
		return internalMote.changeMoteId(id);
	}
	
	/**
     *
	 * @return current network address of the mote
	 */
	public NetworkAddress getNetworkAddress() throws RemoteException {
		return internalMote.getNetworkAddress().getRemote();
	}
	
	/**  
	 * @return MAC address of the mote
	 */
	public short getMACAddress() throws RemoteException {
		return internalMote.getMACAddress();
	}
	
	/**
	 * 
	 * @return true if this Mote is PAN Coordinator of the network
	 */
	public boolean isPanCoordinator() throws RemoteException {
		return internalMote.isPanCoordinator();
	}
	
	/**
	 * sends a message to the mote to read fresh data
	 * @return true if the operation is successful
	 */
	public boolean refreshData() throws RemoteException, MoteUnreachableException {
		return internalMote.refreshData();
	}
		
	/**
	 * Returns a reference to the parent of this Mote.
	 * @return
	 */
	public Mote getParentMote() throws RemoteException {
		InternalMote mote = internalMote.getParentMote();
		if (mote != null)
			return (Mote) mote.getRemote();
		else
			return null;
	}

	@Override
	public int getChildNumber() throws RemoteException {
		return childMotes.size();
	}

	public Vector<Mote> getChildMotes() throws RemoteException {
		return childMotes;
	}
	
	public boolean hasChild( Mote mote ) throws RemoteException {
		return childMotes.contains(mote);
	}
	
	protected void addChild( InternalMote mote ) {
		childMotes.add((Mote) mote.getRemote());
	}
	
	protected void removeChild( InternalMote mote ) {
		childMotes.remove(mote);
	}
	
	protected void updateChilds() {
		Iterator<InternalMote> it = internalMote.getChildMotes().iterator();
		childMotes.removeAllElements();
		
		while (it.hasNext()) {
			childMotes.add((Mote) it.next().getRemote());
		}
	}
	
	/**
	 * 
	 * @return True if the corresponding Mote in the WSN is reachable
	 */
	public boolean isReachable() throws RemoteException {
		return internalMote.isReachable();
	}

	 /**
	 * This method return the gateway with which the mote is connected
	 * @return the name of the Gateway 
	 * @throws RemoteException
	 */
	@Override
	public WSNGateway getWSNGatewayParent() throws RemoteException {
		return internalMote.getWsn().getGateway().getRemoteGateway();
	}
	//Get parent mote
	
	/**
	 * This method add element to the list
	 * @param name of the object
	 * @param groupName name of the group
	 * @param typeIn represent the inputs type of the method requested
	 * @param paramIn contain the inputs of the method
	 * @throws RemoteException
	 * @throws ClassNotFoundException, RemoteException
	 */
	public void addObjectToList(String name, String groupName, Class typeIn[], Object paramIn[])  throws RemoteException, ClassNotFoundException {
		Class cl=null;
		Object element=null;
		ServerClassLoader loader= new ServerClassLoader();
		
		synchronized(this.classLock)
		{
			cl = loader.loadClass(name, internalMote.getWsn().getGateway().getFunctionDirectory() +groupName+"/", true);
			try 
			{
				element = cl.newInstance();
			} 
			catch (InstantiationException e) {e.printStackTrace();} 
			catch (IllegalAccessException e) {e.printStackTrace();}
			
			this.functions.add(0, element);
			element=this.functions.get(0);
			
			Method met=null;
			try 
			{
				met=element.getClass().getDeclaredMethod("startFunction", typeIn);
				met.invoke(element,paramIn);//invoke the method
			} 
			catch (SecurityException e) {e.printStackTrace();} 
			catch (NoSuchMethodException e) {e.printStackTrace();}
			catch (IllegalArgumentException e) {	e.printStackTrace();} 
			catch (IllegalAccessException e) {	e.printStackTrace();} 
			catch (InvocationTargetException e) {	e.printStackTrace();}
			
		}

		
	}
	
	/**
	 * This method is used by the client to invoke the methods of the objects loaded on this mote
	 * @param inputSearched list of parameters input for the searching
	 * @param typeIn represent the inputs type of the method requested
	 * @param paramIn contain the inputs of the method
	 * @return the method return an object that represent the result of the method invoked
	 * @throws RemoteException
	 */
	public Object useMethod(ArrayList<String> inputSearched,String nameMethod, Class typeIn[], Object paramIn[]) throws RemoteException {
		Object output=new Object(); 
//		Method met=null;
//		int index=0;
//		Object toUse=null;
//		
//		//search the object with the name requested by the client
//		synchronized(classLock)
//		{
//			index=ListSearchingManager.searchClassIntoList(this.functions, inputSearched);
//			if(index!=-1)
//			{
//				toUse=this.functions.get(index); //invoke the object founded
//				try 
//				
//				{
//					met = toUse.getClass().getDeclaredMethod(nameMethod,typeIn); //get the method requested
//					output=met.invoke(toUse,paramIn);//invoke the method
//				} 
//				catch (SecurityException e) {	e.printStackTrace();} 
//				catch (NoSuchMethodException e) {	e.printStackTrace();} 
//				catch (IllegalArgumentException e) {	e.printStackTrace();} 
//				catch (IllegalAccessException e) {	e.printStackTrace();} 
//				catch (InvocationTargetException e) {	e.printStackTrace();}
//		
//			}
//		}
			
		return output;
		
	}

	
	/**
	 * Method used for deleting an object from the list
	 * @param inputSearch
	 */
	public void removeObjectFromList(ArrayList<String> inputSearch) throws RemoteException {
//		synchronized(this.classLock)
//		{
//			int position= ListSearchingManager.searchClassIntoList(this.functions, inputSearch);
//			this.functions.remove(position);
//		}
		
	}
	
	
	/**
	 * print the list of the function loaded on gateway
	 * @throws RemoteException
	 */
	public void printList() throws RemoteException {
		System.out.println("\nList of the object loaded");
		
		synchronized(classLock)
		{
	
			for (int i=0;i<this.functions.size();i++)
			{	
				try 
				{	
					System.out.println("name of the object : "+this.functions.get(i).getClass().getName()+"   version : "+this.functions.get(i).getClass().getDeclaredField("version").get(null).toString()+"   path : "+this.functions.get(i).getClass().getDeclaredField("pathSave").get(null).toString()+"   author : "+this.functions.get(i).getClass().getDeclaredField("author").get(null).toString());
				} 
				catch (SecurityException e) {e.printStackTrace();} 
				catch (NoSuchFieldException e) {e.printStackTrace();} 
				catch (IllegalArgumentException e) {e.printStackTrace();} 
				catch (IllegalAccessException e) {e.printStackTrace();}
			}
			System.out.println("\n");

		}
		
	}
	
	//void setMoteGroupName(String mGroupName) throws RemoteException;
	//String getMoteGroupName() throws RemoteException;
	
	@Override
	public String getUniqueId() throws RemoteException {
		return internalMote.getId();
	}
	
	//Functionality
	protected void addFunctionality( InternalFunctionality func ) {
		if (!functionalityList.contains(func)) {
			functionalityList.add(func.getRemoteFunctionality());
		}		
	}
	
	protected void removeFunctionality( InternalFunctionality func ) throws Exception {
		if (!this.functionalityList.contains(func))
			throw new Exception("functionalityList does not contain this func! Mote name:" + internalMote.getId() + ";" +
								 "network address:" + internalMote.getNetworkAddress() + ";"+ 
								 "mac address:" + internalMote.getMACAddress());
		else {
			this.functionalityList.remove(func);
		}		
	}
	
	/**
	 * @return List of Functionalitis currently installed on the node;
	 */
	@Override
	public int getFunctionalityNumber() throws RemoteException {
		return internalMote.getFunctionalityNumber();
	}
	
	/**
	 * @return List of Functionality currently installed on the node;
	 */
	@Override
	public ArrayList<Functionality> getFunctionalityList() throws RemoteException {
		return functionalityList;
	}
	
	@Override
	public boolean isFunctionalityInList(byte id, FunctionalityType type) throws RemoteException {
		return internalMote.isFunctionalityInList(id, type);
	}
	
	//Sensor
	protected void addSensor( InternalSensor sensor ) {
		if (!sensorList.contains(sensor)) {
			sensorList.add((Sensor) sensor.getRemote());
		}		
	}
	
	protected void removeSensor( InternalSensor sensor ) throws Exception {
		if (!this.sensorList.contains(sensor.getRemote()))
			throw new Exception("SensorList does not contain this sensor! Mote name:" + internalMote.getId() + ";" +
								 "network address:" + internalMote.getNetworkAddress() + ";"+ 
								 "mac address:" + internalMote.getMACAddress());
		else {
			this.sensorList.remove(sensor.getRemote());
		}		
	}

	/**
	 * @return List of Sensors currently attached to the node;
	 */
	@Override
	public ArrayList<Sensor> getSensorList() throws RemoteException {
		return sensorList;
	}
	
	@Override
	public void addSubscriber( Subscriber<Mote,Mote> s) throws RemoteException {
		myPublisher.addSubscriber(s);
	}
	
	@Override
	public void removeSubscriber( Subscriber<Mote,Mote> s) throws RemoteException {
		myPublisher.removeSubscriber(s);
	}

	@Override
	public void update(InternalMote pub, Object code) {
		try {
			myPublisher.notifySubscribers(this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}
