package client_applications.console;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.GregorianCalendar;

import remote_interfaces.functionality.Functionality;
import remote_interfaces.services.Subscriber;

public class FunctionalitySubscriber extends UnicastRemoteObject implements Subscriber<Functionality, Object> {

	MobiWSNConsoleView app;

	protected FunctionalitySubscriber( MobiWSNConsoleView app) throws RemoteException {
		super();
		this.app = app;
	}

	@Override
	public void update(Functionality pub, Object object) throws RemoteException {
		
		Calendar cal = new GregorianCalendar();
		String AM_PM;
		if (cal.get(Calendar.AM_PM) == 0)
			AM_PM="AM";
		else
			AM_PM="PM";
		
		System.out.println("\n@console@: Day "+cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)+
				" at "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND)+":"+cal.get(Calendar.MILLISECOND)+" "+AM_PM);
		
		System.out.println("@console@: Sono stato avvisato dalla Funzionalità " + pub.getType() +
				" del mote " + pub.getOwnerMoteId() + 
				" che l'object vale " + object +"\n");
	}
}
