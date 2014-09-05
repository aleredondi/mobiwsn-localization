package client_applications.localization;

import Jama.*;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import remote_interfaces.clients.localization.Status;

import remote_interfaces.functionality.Functionality;
import remote_interfaces.services.Subscriber;

public class FunctionalitySubscriber extends UnicastRemoteObject implements Subscriber<Functionality, Object> {

	LauraManager app;

	protected FunctionalitySubscriber( LauraManager app) throws RemoteException {
		super();
		this.app = app;
	}

	@Override
	public void update(Functionality pub, Object object) throws RemoteException {

		if(object instanceof Integer){
			Integer sync;
			sync = (Integer)object;
			System.out.println("@vigilo@: SYNC = " + sync);
		}
		if(object instanceof Matrix){
			Matrix list;		
			list = (Matrix)object;
			app.update(pub.getOwnerMoteId(), list);
			System.out.println("@vigilo@: Received a message from Mote: " + pub.getOwnerMoteId() + "\n");
		}
/*
		if(object instanceof Status){
			Status status;
			status = (Status)object;;
			app.updateStatus(pub.getOwnerMoteId(), status);
		}
*/
		if(object instanceof Short){
			Short status;
			status = (Short)object;;
			app.updateStatus(pub.getOwnerMoteId(), status);
			System.out.println("@vigilo@: status = " + Status.convert(status) + "\n");
		}
	
	}
	
}
