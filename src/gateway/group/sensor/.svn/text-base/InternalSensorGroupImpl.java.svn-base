package gateway.group.sensor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

import remote_interfaces.group.Groupable;
import remote_interfaces.sensor.Sensor;

import gateway.InternalWSNManager;
import gateway.group.GenericInternalGroup;
import gateway.group.InternalGroup;
import gateway.group.InternalGroupable;
import gateway.group.mote.RemoteMoteGroupImpl;
import gateway.mote.InternalMote;
import gateway.sensor.InternalSensor;

public class InternalSensorGroupImpl extends GenericInternalGroup<InternalSensor, Sensor> implements InternalSensorGroup {

	public InternalSensorGroupImpl(String name, InternalSensor first, InternalWSNManager wsn) {
		super(name, first, wsn);

		short id;

//		id = SingletonGroupIdManager.getIdManager().getId(this);
//		if( id != 0)
//			setGroupId(id);
	}

	@Override
	protected void createRemote() {
		try {
			setRemote(new RemoteSensorGroupImpl(this) );
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void removeRemoteFromList(Groupable o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRemote(Sensor o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRemote(ArrayList<Sensor> o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRemote(Sensor o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRemote(ArrayList<Sensor> o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Object> useGroupMethod(String nameClass,
			String methodName, Class[] parameterTypes, Object[] paramIn) {
		// TODO Auto-generated method stub
		return null;
	}

/*	@Override
	public ArrayList<Object> useGroupMethod(String nameClass,
			String methodName, Class[] parameterTypes, Object[] paramIn) {
		// TODO Auto-generated method stub
		return null;
	}
/*
	@Override
	public UnicastRemoteObject getRemote() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void addRemoteToList( Groupable o ) {
		//TODO metterci la lista dei sensori
		/*Iterator<InternalMote> it = wsn
		InternalMote el = null;
		boolean found = false;
		
		try {
			System.out.println("GIG Un client mi chiede di aggiungere un mote " + o.getUniqueId() + " remoto al gruppo " + name );
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while( it.hasNext() ) {
			el = it.next();
			System.out.println("GIG Prendo l'elemento " + el.getUniqueId() +  "della lista interna"); 
			try {
				if ( el.getUniqueId().equals( o.getUniqueId() ) ) {
					found = true;	
					break;
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (found) {
			System.out.println("GIG Ho trovato il corrispondente interno e quindi lo aggiungo");
			addToList(el);
		}else {
			System.out.println("GIG C'� un errore, non ho trovato l'oggetto interno");
		}*/
		
}
