package gateway.group.mote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import common.exceptions.ExceptionHandler;
import common.exceptions.MiddlewareException;
import common.exceptions.ResponseTimeoutException;

import remote_interfaces.group.Groupable;
import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteGroup;

import gateway.InternalWSNManager;
import gateway.group.GenericInternalGroup;
import gateway.group.InternalGroup;
import gateway.group.InternalGroupable;
import gateway.mote.InternalMote;

public class InternalMoteGroupImpl extends GenericInternalGroup<InternalMote, Mote> implements InternalMoteGroup {
	
	private InternalMote group_master; // inizialmente è il primo elemento aggiunto
									   // (dato che abbaimo deciso che per ora il master è il primo elemento della lista)

	public InternalMoteGroupImpl( String name, InternalMote first, InternalWSNManager wsn) {			
		super( name, first, wsn);
		first.addSubscriber(this);
		
		//this.group_master = first;
		
		System.out.println("IMG Sono il nuovo gruppo interno di mote " + name );

	}

	public InternalMoteGroupImpl( String name, ArrayList<InternalMote> list, InternalWSNManager wsn) {		
		super( name, list, wsn);
		Iterator<InternalMote> it = list.iterator();
		while(it.hasNext())
			it.next().addSubscriber(this);
		
		//this.group_master = list.get(0);
		
		System.out.println("IMG Sono il nuovo gruppo interno di mote " + name );

	}

	@Override
	protected void createRemote() {
		try {
			System.out.println("IMG Creo il gruppo di mote remoto " );
			setRemote(new RemoteMoteGroupImpl(this) );
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public RemoteMoteGroupImpl getRemote() {
		return (RemoteMoteGroupImpl) remoteGroup;
	}

	@Override
	public ArrayList<Object> useGroupMethod(String nameClass,
			String methodName, Class[] parameterTypes, Object[] paramIn) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void add(InternalMote mote ) {
		System.out.println("InternalMote: aggiungo un mote");
		super.add(mote);
		
		mote.addSubscriber(this);
		
		sendAddMotesToGroup();
	}

	@Override
	public void add(ArrayList<InternalMote> mote ) {
		System.out.println("InternalMote: aggiungo una lista di mote");
		super.add(mote);
		
		Iterator<InternalMote> it = mote.iterator();
		while(it.hasNext())
			it.next().addSubscriber(this);
		
		sendAddMotesToGroup();
	}
	
	@Override
	public void addRemote( ArrayList<Mote> o ) {
		System.out.println("InternalMote: aggiungo un mote remoto");
			Iterator<InternalMote> it = wsn.getMoteList().iterator();
			Iterator<Mote> input = o.iterator();
			InternalMote el = null;
			Mote elext = null;
			ArrayList<InternalMote> list = new ArrayList<InternalMote>();
			boolean found = false;
			
			while( input.hasNext() ) {
				elext = input.next();
				
				try {
					System.out.println("GIG Un client mi chiede di aggiungere un mote " + elext.getUniqueId() + " remoto al gruppo " + name );
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				while( it.hasNext() ) {
					el = it.next();
					System.out.println("GIG Prendo l'elemento " + el.getUniqueId() +  "della lista interna"); 
					try {
						if ( el.getUniqueId().equals( elext.getUniqueId() ) ) {
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
					list.add(el);
				}else {
					System.out.println("GIG C'� un errore, non ho trovato l'oggetto interno");
				}
			}
			

			add(list);
		 
		}
	 
	@Override
	public void addRemote( Mote o ) {
		 ArrayList<Mote> temp = new ArrayList<Mote>(1);
		 temp.add(o);
		 
		 addRemote(temp);
	}


	@Override
	public void remove( InternalMote mote ) {
	
		System.out.println("Devo mandare il messaggio per toglierlo dal gruppo");
		
		sendRemoveFromGroup( mote );
		mote.removeSubscriber(this);
		
		super.remove(mote);
		
		if ( mote.equals(group_master) && (super.getListNumber() > 0) )
		{
			System.out.println("Devo mandare il messaggio per riassegnare il master del gruppo");
			
			group_master = this.getList().get(0);
			sendAddMotesToGroup();
		}	
	
	}

	@Override
	public void remove( ArrayList<InternalMote> mote ) {
		boolean thereIsMaster = false;
		
		System.out.println("Devo mandare i messaggi per toglierli dal gruppo");
		
		Iterator<InternalMote> it = mote.iterator();
		while(it.hasNext())
		{
			InternalMote m = it.next();		
			
			// lo rimuovo solo se non è il master
			if ( !m.equals(group_master) ) 
			{
				sendRemoveFromGroup( m );
				m.removeSubscriber(this);
			}
			else
				thereIsMaster = true;
		}
		
		if ( thereIsMaster )
		{
			// rimuovo il master
			sendRemoveFromGroup( group_master );
			group_master.removeSubscriber(this);
			
		}
		
		super.remove(mote);	
			
		if (super.getListNumber() > 0)
		{			
			System.out.println("Devo mandare il messaggio per riassegnare il master del gruppo");
			
			group_master = this.getList().get(0);
			sendAddMotesToGroup();
		}
			
	}

	@Override
	public void removeAll() {
		boolean thereIsMaster = false;
		
		System.out.println("Devo mandare i messaggi per togliere tutti i mote dal gruppo");
		
		Iterator<InternalMote> it = getList().iterator();
		while(it.hasNext())
		{
			InternalMote m = it.next();		
			
			if ( !m.equals(group_master) ) 
			{
				sendRemoveFromGroup( m );
				m.removeSubscriber(this);
			}
			else
				thereIsMaster = true;
				
		}

		// se devo il master lo rimuovo per ultimo
		if ( thereIsMaster )
		{
			sendRemoveFromGroup(group_master);
			group_master.removeSubscriber(this);
		}
		
		super.removeAll();		
	}
	
	@Override
	public void removeRemote(Mote o) {
		ArrayList<Mote> temp = new ArrayList<Mote>(1);
		temp.add(o);
		
		removeRemote(temp);
	}
	
	@Override
	public void removeRemote( ArrayList<Mote> o) {
		Iterator<InternalMote> it = wsn.getMoteList().iterator();
		Iterator<Mote> input = o.iterator();
		InternalMote el = null;
		Mote elext = null;
		ArrayList<InternalMote> list = new ArrayList<InternalMote>();
		boolean found = false;
		
		while( input.hasNext() ) {
			elext = input.next();
			
			try {
				System.out.println("GIG Un client mi chiede di togliere un mote " + elext.getUniqueId() + " remoto al gruppo " + name );
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			while( it.hasNext() ) {
				el = it.next();
				System.out.println("GIG Prendo l'elemento " + el.getUniqueId() +  "della lista interna"); 
				try {
					if ( el.getUniqueId().equals( elext.getUniqueId() ) ) {
						found = true;
						break;
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
			if (found) {
				System.out.println("GIG Ho trovato il corrispondente interno e quindi lo tolgo");
				list.add(el);
			}else {
				System.out.println("GIG C'� un errore, non ho trovato l'oggetto interno");
			}
		}
		

		remove(list);
			
	}

	@Override
	public void update(InternalMote pub, Object code) {
		
		if ( code instanceof Boolean ) {
			
			System.out.println("Vengo notificato dal mote " + pub.getUniqueId());
			if ( pub.equals(group_master) && !pub.isReachable() )
			{		
				System.out.println("Il master del gruppo non è più raggiungibile");

				for (InternalMote m : this.getList())
					if (m.isReachable() && !m.equals(group_master))
						group_master = m;
				
				// se non c'è neanche un membro del gruppo raggiungibile il senAddToGroup
				// non verrà fatto dato che il group_manager è rimasto lo stesso (irraggiungibile)
				
			}

			sendAddMotesToGroup();
			
		}
		super.update(pub, code);
	}
	
	public InternalMote getGroupMaster() {
		return group_master;
	}
	
	protected void removeRemoteFromList(Groupable o) {
		Iterator<InternalMote> it = wsn.getMoteList().iterator();
		InternalMote el = null;
		
		try {
			System.out.println("GIG Un client mi chiede di togliere un mote " + o.getUniqueId() );
			
			while(it.hasNext()) {
				el = it.next();
				if( el.getUniqueId().equals(o.getUniqueId()) ) {
					System.out.println("GIG Trovato nella lista interna");
					break;
				}
			}
			
			removeFromList(el);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void sendAddMotesToGroup() {
		
		if (group_master == null)
			this.group_master = getList().get(0);
		
		if ( group_master.isReachable() ) {
			
			System.out.println("GIG Il group master è raggiungibile...invio l'addMoteToGroup");
			
			AddMoteToGroupMessage amtgMessage = new AddMoteToGroupMessage(this);				
				
			try
			{
				wsn.sendMessageTask(amtgMessage);
			}	
			catch (ResponseTimeoutException rtex) {
				ExceptionHandler.getInstance().handleException(rtex);
			} catch (MiddlewareException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
			System.out.println("GIG Il group master non è più raggiungibile");
		
	}
	
	private void sendRemoveFromGroup( InternalMote mote ) {
		
		RemoveMoteFromGroupMessage rmtgMessage;

		rmtgMessage = new RemoveMoteFromGroupMessage(this, mote);
		rmtgMessage.setCommandType(MoteGroupCommandType.GC_REMOVE);
			
		try
		{
			wsn.sendMessageTask(rmtgMessage);
		}
		catch (ResponseTimeoutException rtex)
		{
			ExceptionHandler.getInstance().handleException(rtex);
		} catch (MiddlewareException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	

	
}
