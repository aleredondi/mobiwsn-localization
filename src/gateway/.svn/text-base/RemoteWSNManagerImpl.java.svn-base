package gateway;

import gateway.group.mote.InternalMoteGroup;
import gateway.group.mote.RemoteMoteGroupImpl;
import gateway.group.sensor.InternalSensorGroup;
import gateway.mote.InternalMote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteGroup;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.sensor.SensorGroup;

public class RemoteWSNManagerImpl extends UnicastRemoteObject implements RemoteWSNManager {

	private InternalWSNManagerImpl manager;

	/*
	 * list currently present in the WSN
	 */
	private ArrayList<Mote> moteList;
		
	/*
	 * reference to then WSN PAN Coordinator
	 */
	private Mote networkPanCoordinator;
	

	//Hashtables used for searching the group
//	private Hashtable<String,MoteGroupImpl> moteGroupList;
//	private Hashtable<String,SensorGroupImpl> sensorGroupList;
	private ArrayList<MoteGroup> moteGroup;
	private ArrayList<SensorGroup> sensorGroup;

	//Objects used for synchronization
	private Object lockMoteList;
	private Object lockGroupList;

	
	RemoteWSNManagerImpl( InternalWSNManagerImpl manager ) throws RemoteException {
		
		//System.out.println("WSNMR Sono il WSN manager remoto");
		
		this.manager = manager;
		moteList = new ArrayList<Mote>();
		moteGroup = new ArrayList<MoteGroup>();
		sensorGroup = new ArrayList<SensorGroup>();
		
		lockGroupList = new Object();
	}

	@Override
	public InternalMote getMote(String id) throws RemoteException {
		return manager.getInternalMote(id);
	}

	@Override
	public ArrayList<Mote> getMoteList() throws RemoteException {
		return moteList;
	}

	@Override
	public int getMoteNumber() throws RemoteException {
		return moteList.size();
	}

	@Override
	public Mote getPanCoordinator() throws RemoteException {
		return (Mote) manager.getPanCoordinator().getRemote();
	}

	@Override
	public boolean refreshMoteList() throws RemoteException {
		return manager.refreshMoteList();
	}
	
	protected void add(Mote mote) throws Exception {
		if (!moteList.contains(mote) ) {
			moteList.add(mote);
		} else {
			throw new Exception("Esiste gia'");
		}
	}

	public void remove(Remote remote) {
		moteList.remove(remote);
		
	}

	
	
	//Group methods
	
	@Override
	public synchronized MoteGroup getMoteGroup(String groupName) throws RemoteException {
		Iterator<MoteGroup> it = moteGroup.iterator();
		MoteGroup mote = null;
		
		System.out.println("WSNMR Un client mi chiede il gruppo " + groupName + " scorro la lista");
		while( it.hasNext() ) {
			mote = it.next();
			System.out.println("WSNMR Cerco " + mote.getName());
			if ( mote.getName().equals(groupName) ) {
				System.out.println("WSNMR trovato " + mote.getName());
				break;
			}
		}
		
		return  mote;
	}


	@Override
	public synchronized boolean createMoteGroup(String groupName, Mote mote)
			throws RemoteException {
		System.out.println("WSNMR Un client chiede di creare il gruppo " + groupName);
		synchronized ( lockGroupList ){
			return manager.createInternalMoteGroup(groupName, mote);
		}
	}
	
	protected synchronized boolean createRemoteMoteGroup(InternalMoteGroup group) {
		System.out.println("WSNMR aggiungo " + group.toString()+ " alla lista dei gruppi esterna");
		System.out.println("WSNMR aggiungo " + group.getRemote().toString()+ " alla lista dei gruppi esterna");
		moteGroup.add( (MoteGroup) group.getRemote());
		System.out.println("WSNMR Fatto");
		return true;
	}


	@Override
	public synchronized boolean createMoteGroup(String groupName, ArrayList<Mote> moteList)
			throws RemoteException {
		synchronized ( lockGroupList ){
			System.out.println("WSNMR Un client chiede di creare il gruppo " + groupName + " con una lista di mote");
			return manager.createInternalMoteGroup(groupName, moteList);
		}
	}


	@Override
	public synchronized ArrayList<MoteGroup> getMoteGroupList() throws RemoteException {
		return moteGroup;
	}

	@Override
	public synchronized boolean removeMoteGroup(String groupName) throws RemoteException {
		return manager.removeInternalMoteGroup(groupName);
	}
	
	protected synchronized boolean removeRemoteMoteGroup( MoteGroup group) {
		return moteGroup.remove(group);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean createSensorGroup(String groupName, Sensor sensor)
			throws RemoteException {
		return manager.createInternalSensorGroup(groupName, sensor);	}

	@Override
	public boolean createSensorGroup(String groupName,
			ArrayList<Sensor> sensorList) throws RemoteException {
		return manager.createInternalSensorGroup(groupName, sensorList);
	}

	protected boolean createRemoteSensorGroup(InternalSensorGroup group) {
		sensorGroup.add((SensorGroup) group.getRemote());
		return true;
	}

	@Override
	public SensorGroup getSensorGroup(String groupName) throws RemoteException {
		Iterator<SensorGroup> it = sensorGroup.iterator();
		SensorGroup sensor = null;
		
		while( it.hasNext() ) {
			sensor = it.next();
			if ( sensor.getName() == groupName ) {
				break;
			}
		}
		
		return sensor;
	}

	@Override
	public ArrayList<SensorGroup> getSensorGroupList() throws RemoteException {
		return sensorGroup;
	}

	@Override
	public boolean removeSensorGroup(String groupName) throws RemoteException {
		return manager.removeInternalSensorGroup(groupName);
	}
	
	protected boolean removeRemoteSensorGroup( SensorGroup group) {
		return sensorGroup.remove(group);
	}


}
