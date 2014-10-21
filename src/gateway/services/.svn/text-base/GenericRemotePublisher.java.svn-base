package gateway.services;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Vector;

import remote_interfaces.services.Publisher;
import remote_interfaces.services.Subscriber;

public class GenericRemotePublisher<WHO,WHAT> extends UnicastRemoteObject implements Publisher<WHO,WHAT> {
	
	private Vector<Subscriber<WHO,WHAT>> subscribers; 

	public GenericRemotePublisher() throws RemoteException {
		super();
		 subscribers = new Vector<Subscriber<WHO,WHAT>>();
	}

	@Override
	public void addSubscriber(Subscriber<WHO,WHAT> s) throws RemoteException {
		//Add subscriber to list
	    if ( !subscribers.contains(s) )
	    	subscribers.addElement(s); 
		//System.out.println("Aggiunto il client estreno" + s.toString());
		//System.out.println("Alla coda" + subscribers.size());
	}

	void removeAllSubscribers() {
		//Remove all subscriber
		//Can be invoked only from an internal class of the gateway, not remotely
	    subscribers.removeAllElements(); 
		//System.out.println("Rimossi tutti i client remoti");
	}

	@Override
	public void removeSubscriber(Subscriber<WHO,WHAT> s) throws RemoteException {
		//Remove a subscriber from the list
	    subscribers.removeElement(s); 
		//System.out.println("Rimosso il client estreno" + s.toString());
		//System.out.println("Dalla coda");
	}

	public void notifySubscribers(WHO pub, WHAT code) throws RemoteException { 
		  Vector<Subscriber<WHO,WHAT>> deadSubs = null; 
		  Iterator<Subscriber<WHO,WHAT>> e = subscribers.iterator(); 
		  Subscriber<WHO,WHAT> s;
		  
			//System.out.println("Notifico remoti..." + subscribers.size());
		    while (e.hasNext()) { 
		       s = e.next(); 
		       synchronized (s) {
		    	   //System.out.println("Provo a notificare il client..." + s.toString());
		    	   //System.out.println("Per conto di " + pub.toString() );
		    	   try { 
		    		   s.update(pub, code); 
		    		   //System.out.println("Notifico il client estreno" + s.toString());
		    	   } 
		    	   catch (java.rmi.ConnectException ce) { //serious 
		    		   ce.printStackTrace();
		    		   //System.out.println("Non riesco a contattare " + s.toString());
		    		   if (deadSubs == null) 
		    			   deadSubs = new Vector<Subscriber<WHO,WHAT>>(); 
		    		   //System.out.println("Lo rimuovo dalla lista " + s.toString());
		    		   deadSubs.addElement(s);// must be dead 
		    	   } 
		    	   catch (java.rmi.NoSuchObjectException nsoe){ //serious  
		    		   nsoe.printStackTrace();
		    		   if (deadSubs == null) 
		    			   deadSubs = new Vector<Subscriber<WHO,WHAT>>(); 
		    		   //System.out.println("Lo rimuovo dalla lista " + s.toString());
		    		   deadSubs.addElement(s);// must be dead 
		    	   } 
		    	   catch (java.rmi.RemoteException re) { 
		    		   /*might recover?*/ 
		    		   re.printStackTrace();
		    	   }
		       }
		    } 
		    if (deadSubs != null) { 
		      e = deadSubs.iterator(); 
		      while (e.hasNext()) { 
		        s = e.next(); 
		        subscribers.removeElement(s);  // forget this subscriber 
		      } 
		    } 
	  } 

	  public void notifySubscribers(WHO pub) throws RemoteException { 
		  notifySubscribers(pub, null); 
	  }


}
