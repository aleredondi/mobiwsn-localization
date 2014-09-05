package gateway.services;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;

import remote_interfaces.services.Publisher;

public class GenericInternalPublisher<WHO,WHAT> implements InternalPublisher<WHO,WHAT> {

	private GenericRemotePublisher<WHO,WHAT> remote_publisher;
	private Vector<InternalSubscriber<WHO,WHAT>> subscribers = new Vector<InternalSubscriber<WHO,WHAT>>(); 

	public GenericInternalPublisher() {
		super();
		// TODO Auto-generated constructor stub
		//System.out.println("Creo il mio publisher interno");
		try {
			remote_publisher = new GenericRemotePublisher<WHO,WHAT>();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addSubscriber(InternalSubscriber<WHO,WHAT> s) {
		//Add subscriber to list
	    if ( !subscribers.contains(s) )
	    	subscribers.addElement(s); 
	    //System.out.println("Aggiunto il client interno " + s.toString() );
	    //System.out.println("Alla coda di subscriber dell'oggetto" + this.toString() );
	}

	@Override
	public void removeSubscriber(InternalSubscriber<WHO,WHAT> s) {
		//Remove a subscriber from the list
	    subscribers.removeElement(s); 
	    //System.out.println("Rimosso il client interno" + s.toString() );
	    //System.out.println("Dalla coda" + subscribers.size());
	}

	  public void notifySubscribers(WHO pub, WHAT code) { 
		  Vector<InternalSubscriber<WHO,WHAT>> deadSubs = null; 
		  Iterator<InternalSubscriber<WHO,WHAT>> e = subscribers.iterator();
		  InternalSubscriber<WHO,WHAT> s;

		  //System.out.println("Notifico i " +  subscribers.size() + " client interni");
		  //System.out.println("Per conto di " + pub.toString());

		  while (e.hasNext()) { 
			  s = e.next(); 
			  synchronized (s) {
				  try { 
					  //System.out.println("Notifico il client interno " + s.toString());
					  s.update(pub, code); 
				  } 
				  catch (java.lang.NullPointerException ex) { //serious 
					  if (deadSubs == null) deadSubs = new Vector<InternalSubscriber<WHO,WHAT>>(); 
					  deadSubs.addElement(s);// must be dead 
				  } 
			  }
		 } 
		 if (deadSubs != null) { 
			 e = deadSubs.iterator(); 
		     while (e.hasNext()) { 
		    	 s = e.next(); 
		    	 subscribers.removeElement(s);   // forget this subscriber 
		     } 
		 }
		 		 
	  } 

	  public void notifySubscribers(WHO pub) { 
		  notifySubscribers(pub, null); 
	  }
	  
	  public Publisher<WHO,WHAT> getRemotePublisher() {
		  return (Publisher<WHO, WHAT>) remote_publisher;
	  }

}
