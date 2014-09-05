package client_applications.localization.data_retrieving.tracking;

import Jama.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.util.Date;

import remote_interfaces.functionality.Functionality;
import remote_interfaces.services.Subscriber;

public class FunctionalitySubscriber extends UnicastRemoteObject implements Subscriber<Functionality, Object> {

	TrackingDataControl app;

	protected FunctionalitySubscriber( TrackingDataControl app) throws RemoteException {
		super();
		this.app = app;
	}

	@Override
	public void update(Functionality pub, Object object) throws RemoteException {
		boolean mobile = false;
	  	
		if(pub.getOwnerMoteId().charAt(5) == 'M') mobile = true; 
		
		if(object instanceof Integer){
		    Date now = new Date ( );
		    System.out.println ( 
		      DateFormat.getDateTimeInstance ( DateFormat.LONG, DateFormat.LONG ).format(now) );
		    app.printTimeToFile(mobile, now);
			Integer sync,mac;
			mac = Integer.parseInt(pub.getOwnerMoteId().substring(6, 8));
			sync = (Integer)object;
			System.out.println("SYNC: " + sync);
			app.printMacToFile(mobile, mac);
			app.printSyncToFile(mobile, sync);
		}
		if(object instanceof Matrix){
			Matrix list;		
			list = (Matrix)object;
			//app.update(pub.getOwnerMoteId(), list);
			System.out.println("ho ricevuto un messaggio LAURA da " + pub.getOwnerMoteId());
			app.printListToFile(mobile, list);
		}
	}
	
}
