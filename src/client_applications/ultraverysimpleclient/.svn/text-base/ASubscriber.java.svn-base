package client_applications.ultraverysimpleclient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import remote_interfaces.functionality.Functionality;
import remote_interfaces.services.Subscriber;

public class ASubscriber extends UnicastRemoteObject implements Subscriber<Functionality, Object>, Runnable {
	
	boolean prova;

	protected ASubscriber() throws RemoteException {
		super();
		prova = false;
	}

	@Override
	public void update(Functionality pub, Object code) throws RemoteException {
		System.out.println("Sono stato avvisato da " + pub.getId());
		prova = true;
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(30000);
				System.out.println("Sono stato avvisato almeno una volta? " + prova);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

}
