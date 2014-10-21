package gateway;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import common.exceptions.ExceptionHandler;
import common.exceptions.MiddlewareException;
import common.exceptions.ResponseTimeoutException;

import remote_interfaces.Remotizable;
import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteGroup;
import remote_interfaces.mote.MoteType;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.sensor.SensorGroup;
import gateway.comm.MessageSender;
import gateway.functionality.FunctionalityFactory;
import gateway.functionality.InternalFunctionality;
import gateway.group.mote.InternalMoteGroup;
import gateway.group.mote.InternalMoteGroupImpl;
import gateway.group.sensor.InternalSensorGroup;
import gateway.group.sensor.InternalSensorGroupImpl;
import gateway.mote.InternalMote;
import gateway.mote.MoteFactory;
import gateway.protocol.address.InternalNetworkAddress;
import gateway.protocol.address.InternalNetworkAddressImpl;
import gateway.protocol.basic_message.MiddlewareMessage;
import gateway.protocol.MoteAnnouncementMessage;
import gateway.protocol.MoteDiscoveryMessage;
import gateway.protocol.MoteLossMessage;
import gateway.sensor.InternalSensor;
import gateway.sensor.SensorFactory;


public class InternalWSNManagerImpl implements InternalWSNManager, Remotizable {

	RemoteWSNManagerImpl remoteManager;
	
	/*
	 * private members definition
	 */
	private MoteFactory moteFactory;

	/*
	 * list currently present in the WSN
	 */
	private ArrayList<InternalMote> moteList;
		
	/*
	 * reference to then WSN PAN Coordinator
	 */
	private InternalMote networkPanCoordinator;
	
	/*
	 * internal reference to MiddlewareResponseListener
	 */
	private InternalWSNGateway gateway;
	private MessageSender sender;

	//Hashtables used for searching the group
//	private Hashtable<String,MoteGroupImpl> moteGroupList;
//	private Hashtable<String,SensorGroupImpl> sensorGroupList;
	private ArrayList<InternalMoteGroup> moteGroup;
	private GroupController groupController;
	
	
	private ArrayList<InternalSensorGroup> sensorGroup;

	//Objects used for synchronization
	private Object lockMoteList;
	private Object lockGroupList;

	
	InternalWSNManagerImpl(InternalWSNGateway gateway) {
		
		try {
			//System.out.println("WSNMI Creo il wsnmr");
			remoteManager = new RemoteWSNManagerImpl(this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.gateway = gateway;
		this.sender = (MessageSender) gateway;
		this.moteList = new ArrayList<InternalMote>();
		this.networkPanCoordinator = null;
		this.moteGroup = new ArrayList<InternalMoteGroup>();
		groupController = new GroupController(this);
		
		this.sensorGroup = new ArrayList<InternalSensorGroup>();
		
		//Instanciate management group objects
		//this.moteGroupList=new Hashtable<String,RemoteMoteGroupImpl>();
		//this.sensorGroupList=new Hashtable<String,SensorGroupImpl>();
		
		//Instanciate lock Objects
		this.lockMoteList=new Object();
		this.lockGroupList = new Object();
		
		this.moteFactory = new MoteFactory();
	
		
	}
	
	/**
	 * Method used for getting the pan coordinator mote
	 * @return an object mote
	 * @throws RemoteException
	 */
	@Override
	public InternalMote getPanCoordinator()
	{
		return this.networkPanCoordinator;
	}

	/**
	 * Method used to get the list of the mote connected with this gateway
	 * @return an arraylist of the motes
	 * @throws RemoteException
	 */
	@Override
	public ArrayList<InternalMote> getMoteList()
	{
		return this.moteList;
	}

	/*public ArrayList<Mote> getRemoteMoteList()
	{
		return remoteManager.getMoteList();
	}*/

	/**
	 * Method used to refresh the mote list
	 * @throws RemoteException
	 */
	@Override
	public boolean refreshMoteList()
	{
		/*
		 * Creates a new MoteDiscoveryMessage and sends it to the mote
		 * for refreshing internalMote data
		 */
		
		MiddlewareMessage mdMessage = new MoteDiscoveryMessage(null);
				
		//System.out.println("\nSend a MoteDiscoveryMessage to: " + mdMessage.getDstMoteId() + "\n");
		
		try
		{ 
			sender.sendMessageTask(mdMessage);
		}
		catch (Exception ex)
		{
			ExceptionHandler.getInstance().handleException(ex);
			
			return false;
		}
		
		return true;
	}
	
	/*
	 * ****************************************
	 * definition of MiddlewareMessage handlers
	 * ****************************************
	 */
	
	/**
	 * Handler of mote_announcement messages
	 */
	void moteAnnouncementMessageHandler(MiddlewareMessage message)
	{
		MoteAnnouncementMessage maMessage = 
			(MoteAnnouncementMessage) message;
		
		boolean moteFound = false;
		InternalMote mote = null;
		
		/*
		 * checks whether the mote is already present in moteList
		 * (if so, updates internal mote informations)
		 */
		//stampaSuFile("Ricevuto un Mote Announcement dal nodo "+maMessage.getSrcMoteId()+" con nwk_addr: "+maMessage.getMoteNetworkAddress());
		
		//E' qui?
		mote = findMoteById( maMessage.getSrcMoteId() );
		
		if (mote != null)
		{
			synchronized (mote)
			{				
				/*
				 * mote_announcement message is from a mote already present in list so
				 * mote infomation are updated.
				 */
				updateMote(mote, maMessage.getSrcMoteId(), maMessage.getMoteMACAddress(), maMessage.getMoteNetworkAddress(), maMessage.getParentMACAddress());
				
				/*
				 * update mote sensorList
				 */
				try {
					setSensorList(mote, maMessage);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					setFunctionalityList(mote, maMessage);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				/*
				 * sets WSNGatewayImpl reference to networkPanCoordinator
				 */
				if (mote.isPanCoordinator()) { 
					this.networkPanCoordinator = mote;
				}
				
				mote.setMoteReachability(true);
			}
			
		}
		else
		{
			
				/*
				 * 	creates a new Mote (and a Facade Object for user access)
				 * and adds internalMote to moteList and mote to networkMoteList
				 */
				mote = createMote(maMessage.getSrcMoteId(), maMessage.getMoteType(true) , maMessage.getMoteNetworkAddress(),
						maMessage.getMoteMACAddress(), maMessage.getParentMACAddress());
			
			
				/*
				 * sets WSNGatewayImpl reference to networkPanCoordinator
				 */
				if (mote.isPanCoordinator()) this.networkPanCoordinator = mote;
						
				//mote.setMoteReachability(true);
			
				/*
				 * update mote sensorList
				 */
				try {
					setSensorList(mote, maMessage);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					setFunctionalityList(mote, maMessage);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			//if the mote is identify as mote in movement (M) the gateway has to get the list of the functions
			//associated at the group of the mote, if it belongs to a group for now the search of the mote 
			//is done trough all the gateweway, but we can think to do it into a specific group for example
			
//			String id= mote.getId();
//			boolean foundMote=false;
//			
//			if(id.charAt(id.length()-1)=='M')
//			{
//				try {
//					
//				ArrayList<WSNGateway> gwList=gateway.getManager().getWSNGatewayList();
//				
//				String gwold = gateway.getManager().searchingMoteInNetwork(id);
//				if (gwold!=null)
//				{
//					WSNGateway gw= gateway.getManager().getWSNGateway(gwold);
//					ArrayList<Mote> moteList=gw.getMoteList();
//					
//					for(Mote searchedMote : moteList) //verify trough the list of mote if there is one with the same id
//					{
//						if (searchedMote.getId().equals(id)) //the mote is present in the list of this gateway
//						{
//							ArrayList<MoteGroup> groupList=gw.getMoteGroupList();
//							
//							for(MoteGroup moteGroup : groupList) //verify if it belongs to some groups
//							{
//								foundMote=moteGroup.moteSearch(id);	
//								
//								if(foundMote)//the mote belongs to this specific group
//								{
//									//remove Mote from the group of the old gateway and add it on this gateway, creating the new group if it is necessary
//									String name=moteGroup.getName();
//									gateway.getManager().addMoteToGroup(name, (Mote) mote.getRemote());
//									gateway.getManager().removeMoteFromGroup(name, searchedMote);
//
//									gw.removeMote(id);
//										
//								}//end if(foundmote)
//								
//							}//end for(MoteGroup moteGroup : groupList)
//								
//						}//end if (searchedMote.getId().equals(id)) 
//											
//					}// end for(Mote searchedMote : moteList)
//						
//				}//end if(gwold!=null))
//				
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
//				
//			}//end if(id.charAt(id.length()-1)=='M')
//		
		}//end else
		
		gateway.notifyNetworkChange();
		
	}//end method
	
			
	
	/**
	 * Handler of MoteLoss messages
	 */
	void moteLossMessageHandler(MiddlewareMessage message) throws RemoteException
	{
		
		MoteLossMessage mlMessage = 
			(MoteLossMessage) message;
		
		/*
		 * checks whether the mote is already present in moteList
		 * (if so, updates internal mote informations)
		 */
		//stampaSuFile("Ricevuto un Mote Loss dal nodo "+mlMessage.getSrcMoteId()+ " riferito al figlio con nwk_addr: "+mlMessage.getMoteLossNwkAddress());			
			
		System.out.println("> Searching for " + mlMessage.getMoteLossNwkAddress().toString() + "...");
		ArrayList<InternalMote> motes = findMoteByNetworkAddress(mlMessage.getMoteLossNwkAddress());
		Iterator<InternalMote> it = motes.iterator();
		
		while ( it.hasNext() ) {
			InternalMote mote = it.next();
			System.out.println(">    " + mote.getUniqueId()+" is now unreachable!");
			mote.setMoteReachability(false);
		}
					
		gateway.notifyNetworkChange();
		
	}

	
	
	
	/*
	 * Private methods
	 */
	private InternalMote createMote(String moteId, MoteType type, InternalNetworkAddressImpl newtorkAddress,
			short macAddress, short parentMACAddress )  {
		
		InternalMote mote;
						
		//parentMote = findMoteByMACAddress(parentMACAddress);
			

		/*
		 * creates a new Mote (and a Facade Object for user access)
		 * and adds internalMote to moteList and mote to networkMoteList
		 */
		mote = this.moteFactory.createMoteInstance(moteId, type, newtorkAddress,
				macAddress, parentMACAddress,
				this);
					
				
		if (mote.isPanCoordinator()) {
			//Rimuovo il facade
			//moteList.remove(mote.getParentMote());
			mote.setParentMote(null);
		}
		
		moteList.add(mote);
		try {
			remoteManager.add((Mote) mote.getRemote());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return mote;
	}

//	private InternalMote createMote(String moteId, MoteType type, short newtorkAddress,
//			short macAddress, short parentMACAddress )  {
//		
//		//InternalMote parentMote;
//		InternalMote theMote;
//						
//		//parentMote = findMoteByMACAddress(parentMACAddress);
//
//		theMote = createMote(moteId, type, newtorkAddress,
//				macAddress, parentMACAddress );
//		
//		//Devo assegnare il figlio al padre
//		/*if ( parentMote != null )
//			parentMote.addChild(theMote);*/
//		
//		/*
//		 * creates a new Mote (and a Facade Object for user access)
//		 * and adds internalMote to moteList and mote to networkMoteList
//		 */
//		return theMote;
//
//	}

	
	private void updateMote( InternalMote mote, String moteId, short moteMACAddress, InternalNetworkAddress newtorkAddress,
			short parentMACAddress ) {

		InternalMote parentMote;
		
		mote.setId(moteId);
		mote.setMACAddress(moteMACAddress);

		if (mote.getNetworkAddress() != newtorkAddress ) {
			//Il mote ha cambiato indirizzo di rete e quindi padre
			mote.setNetworkAddress(newtorkAddress);
			
			if (!mote.isPanCoordinator())
				mote.setParentMote(parentMACAddress);
			
		}
			
		// TODO controllare che non faccia casino
		mote.updateChilds();
		//Non dovrebbe servire perché lo fa già nell'announcement
		//gateway.notifyNetworkChange();
		
	}
	
	
	private InternalMote findMoteById(String moteId)
	{
		Iterator<InternalMote> it = moteList.iterator();
		InternalMote mote;
		
		while(it.hasNext()) {
			mote = it.next();
			if ( mote.getId() != null )
				if( mote.getId().equals(moteId) )
					return mote;
		}
		
		/*for (InternalMote mote : moteList)
		{
			if ( mote.getId().equals(moteId) )
				return mote;
		}*/
		return null;
	}

	/**
	 * Methdod that returns a reference to the mote (InternalMote) with the MAC address
	 * specified, or null if a mote is not found. 
	 * @param moteMACAddress MAC address of the mote to find;
	 * @return the InternalMote reference found, or null;
	 */
	private InternalMote findMoteByMACAddress(short moteMACAddress)
	{
		for (InternalMote mote : moteList)
		{
			if (mote.getMACAddress() == moteMACAddress)
				return mote;
		}
		return null;
	}
	

	/**
	 * Methdod that returns a reference to the mote (InternalMote) with the MAC address
	 * specified, or null if a mote is not found. 
	 * @param moteMACAddress MAC address of the mote to find;
	 * @return the InternalMote reference found, or null;
	 */
	private ArrayList<InternalMote> findMoteByNetworkAddress(InternalNetworkAddress networkAddress)
	{
		//E' fatto così perché potrebbero esserci casi in cui due nodi hanno lo
		//stesso indirizzo di rete (uno non raggiungibile vecchio e uno che si è preso
		//lo stesso indirizzo
		ArrayList<InternalMote> foundList = new ArrayList<InternalMote>();
		
		for (InternalMote mote : moteList)
		{
			//System.out.println("Scorro la lista guardo il mote " + mote.getUniqueId() + " con addr di rete " + mote.getNetworkAddress().toString() + " rispetto " + networkAddress.toString() + " è uguale " + mote.getNetworkAddress().equals(networkAddress));
			if ( mote.getNetworkAddress().equals(networkAddress) )
				foundList.add( mote );
		}
		
		return foundList;
	}

	/**
	 * Method that returns a reference to the facade mote (Mote) with the MAC address
	 * specified, or null if a mote is not found. 
	 * @param moteMACAddress MAC address of the mote to find;
	 * @return the Mote reference found, or null;
	 */
/*	private InternalMote findFacadeMoteByMACAddress(short moteMACAddress)
	{
		for (InternalMote mote : networkMoteList)
		{
			if (mote.getMACAddress() == moteMACAddress)
				return mote;
		}
		return null;
	}*/
	
	
	
	
	/**
	 * update references to child motes for this mote
	 * @param parentMote
	 */
	//Questo metodo va in mote
	/*private void setChildMotes(InternalMote parentMote)
	{
		for (InternalMote childMote : this.networkMoteList)
		{
			/*
			 * Since there must be a single instance of InternalMote for each
			 * mote in the WSN, we can use == operator to compare 
			 * childMote.getParentMote() and parentMote for equality
			 */
			/*if (childMote.getParentMote() == parentMote)
				if (!parentMote.getChildMotes().contains(childMote))
						parentMote.getChildMotes().add(childMote);
					
		}
	}*/
	
	
	/**
	 * updates SensorList of InternalMote passed as argument, basing on the content
	 * of MoteAnnouncementMessage
	 * @param mote the mote of which update the sensor list
	 * @param maMessage message from which gathering information
	 * @throws RemoteException
	 */
	private void setSensorList(InternalMote mote, MoteAnnouncementMessage maMessage) throws RemoteException
	{
		/*
		 * since we cannot call dinamically a method we have to check sensor_types
		 * one by one
		 */
		InternalSensor sensor = null;
		Iterator<InternalSensor> it;
		boolean found = false;
		
		try
		{				
			for (int iSens =0; iSens < maMessage.getSensorsNumber(); iSens++) {

				it = mote.getSensorList().iterator();
				InternalSensor tempSens;
				
				while( it.hasNext()) {
					tempSens = it.next();
					if ( tempSens.getType() == maMessage.getSensorType(iSens) && tempSens.getId() == maMessage.getSensorId(iSens) )
						found = true;
				}
			
					 
				if (!found ) {
					sensor = SensorFactory.createSensorInstance(maMessage.getSensorId(iSens), maMessage.getSensorType(iSens), mote );

					try {
						mote.addSensor(sensor);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				}
			}
			
			//Dopo di questo controllo se aveva sensori che ora non ha più
			it = mote.getSensorList().iterator();
			ArrayList<InternalSensor> sensorToRemove = new ArrayList<InternalSensor>();
			
			while( it.hasNext() ) {
				sensor = it.next();
				found = false;
				
				for (int iSens = 0; iSens < maMessage.getSensorsNumber(); iSens++) {				
					if ( sensor.getType() == maMessage.getSensorType(iSens) && sensor.getId() == maMessage.getSensorId(iSens) ) {
						//Ok il sensore è nell'announcement
						found = true;
					}
				}
				
				if (!found) {
						//Il sensore non è più installato sul mote, quindi lo rimuovo
						sensorToRemove.add(sensor);
						System.out.println("Rimosso il sensore " + sensor.getId() + "dal mote");
				}
			}
			
			it = sensorToRemove.iterator();
			while(it.hasNext()) {
				try {
					mote.removeSensor(it.next());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
		catch (NullPointerException npex)
		{
			/*
			 * if a nullpointerException is thrown handlException and return from setSensorInfo
			 */
			ExceptionHandler.getInstance().handleException(npex);
			return;
		}
	
	}
	
	private void setFunctionalityList(InternalMote mote, MoteAnnouncementMessage maMessage) throws RemoteException
	{
		/*
		 * since we cannot call dinamically a method we have to check functionality_types
		 * one by one
		 */
		InternalFunctionality functionality =  null;
		Iterator<InternalFunctionality> it;
		boolean found = false;
		
		try
		{				
			for (int iFunc = 0; iFunc < maMessage.getFunctionalitiesNumber(); iFunc++) {			
				it = mote.getFunctionalityList().iterator();
				InternalFunctionality tempFunc;
				
				while( it.hasNext()) {
					tempFunc = it.next();
					if ( tempFunc.getType() == maMessage.getFunctionalityType(iFunc) && tempFunc.getId() == maMessage.getFunctionalityId(iFunc) )
						found = true;
				}

				if (!found ) {				
					try {
						functionality = FunctionalityFactory.createFunctionalityInstance(maMessage.getFunctionalityId(iFunc), maMessage.getFunctionalityType(iFunc), mote );
						mote.addFunctionality(functionality);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
			//Dopo di questo controllo se aveva sensori che ora non ha più
			it = mote.getFunctionalityList().iterator();
			ArrayList<InternalFunctionality> funcToRemove = new ArrayList<InternalFunctionality>();
			
			while( it.hasNext() ) {
				functionality = it.next();
				found = false;
				
				for (int iFunc = 0; iFunc < maMessage.getFunctionalitiesNumber(); iFunc++) {				
					//System.out.println("Controllo la funzuionalità " +functionality.getType() + " " + functionality.getId() + " con " + maMessage.getFunctionalityType(iFunc) + " " + maMessage.getFunctionalityId(iFunc) );
					if ( functionality.getType() == maMessage.getFunctionalityType(iFunc) && functionality.getId() == maMessage.getFunctionalityId(iFunc) ) {
						//Ok il sensore è nell'announcement
						//System.out.println("trovata");
						found = true;
					}
				}
				
				if (!found) {
						//Il sensore non è più installato sul mote, quindi lo rimuovo
						funcToRemove.add(functionality);
						System.out.println("Rimossa la funzionalità " + functionality.getId() + "dal mote");
				}
			}
			
			it = funcToRemove.iterator();
			while(it.hasNext()) {
				try {
					mote.removeFunctionality(it.next());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
				
		}
		catch (NullPointerException npex)
		{
			ExceptionHandler.getInstance().handleException(npex);
			return;
		}
	
	}


	@Override
	public int getMoteNumber() {
		return moteList.size();
	}

	@Override
	public void removeMote(String moteId) {
		InternalMote mote = findMoteById( moteId );
		if( mote != null ) {
			if ( mote.getRemote() != null )
				remoteManager.remove(mote.getRemote());
			moteList.remove(mote);
		}
	}

	/*@Override
	public InternalMote getInternalMote(Mote mote) {
		try {
			return findMoteById( mote.getId());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}*/

	@Override
	public InternalMote getInternalMote(String id) {
		return findMoteById( id);
	}
	
	@Override
	public InternalMote getInternalMote(short macAddress) {
		return findMoteByMACAddress( macAddress );
	}
	

	/**
	 * sends the message passed and waits for the MiddlewareMessage response
	 * if expected
	 * @param message MiddlewareMessage object to be sent to the wsn
	 * @throws MiddlewareException 
	 * @throws ResponseTimeoutException 
	 */
	public void sendMessageTask(MiddlewareMessage message) throws MiddlewareException, ResponseTimeoutException {
		try {
			sender.sendMessageTask(message);
		} catch (ResponseTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (MiddlewareException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public InternalWSNGateway getGateway() {
		return gateway;
	}
	
	public void setGateway( InternalWSNGateway gw ) {
		gateway = gw;
	}

	@Override
	public UnicastRemoteObject getRemote() {
		return remoteManager;
	}

	
	
	//MOTE GROUP METHODS
	
	@Override
	public boolean createMoteGroup(String groupName, InternalMote mote) {
		ArrayList<InternalMote> tempList = new ArrayList<InternalMote>(1);
		tempList.add(mote);
		return createMoteGroup(groupName, tempList);
	}
	
	protected boolean createInternalMoteGroup(String groupName, Mote mote) throws RemoteException {
		System.out.println("Creo il gruppo");
		
		Iterator<InternalMote> it = moteList.iterator();
		InternalMote tempMote = null;
		
		while( it.hasNext() ) {
			tempMote = it.next();
			if ( tempMote.getUniqueId().equals( mote.getUniqueId() ) ) {
				break;
			}
		}
		
		if (tempMote == null)
			return false;
		else		
			return createMoteGroup( groupName, tempMote );
	} 

	@Override
	public boolean createMoteGroup(String groupName, ArrayList<InternalMote> mote) {
		
		synchronized (lockGroupList ) {
			boolean found = false;
			Iterator<InternalMoteGroup> it = moteGroup.iterator();
			InternalMoteGroup temp;
		
			System.out.println("WSNMI Un client interno mi chiede di creare il gruppo " + groupName );
			while( it.hasNext() ) {
				temp = it.next();
				System.out.println("WSNMI Scorro la lista di gruppi " + temp.getName() );
				if ( temp.getName().equals(groupName) ) {
					found = true;
					break;
				}
			}
		
			if (found) {
				System.out.println("Il gruppo esiste gi�");
				return false;
			}
		
			System.out.println("WSNMI Creo il nuovo gruppo " + groupName );
			InternalMoteGroup group = new InternalMoteGroupImpl(groupName, mote, this);
			moteGroup.add( group );
			groupController.addController(group);
			System.out.println("WSNMI Gruppo " + group.getName() + " creato,lo aggiungo alla lista dei gruppi interna");
			remoteManager.createRemoteMoteGroup(group);
		}
		
		return true;
	}

	protected boolean createInternalMoteGroup(String groupName, ArrayList<Mote> mote) throws RemoteException {
		Iterator<Mote> it = mote.iterator();
		ArrayList<InternalMote> tempMote = new ArrayList<InternalMote>(mote.size());
		InternalMote tempMote2;
		Mote rTempMote;
		boolean found;
		
//		synchronized (lockGroupList ) {
			System.out.println("WSNMR Creato il gruppo " + groupName + " con una lista di mote estreni");

			if ( !it.hasNext())
			return false;
		
			//Trovo il mote interno
			while( it.hasNext() ) {		
				rTempMote = it.next();
				System.out.println("Prendo il primo mote della lista " + rTempMote.getUniqueId());
				found = false;

				Iterator<InternalMote> it2 = moteList.iterator();
				while (it2.hasNext()) {
					tempMote2 = it2.next();
					System.out.println("Scorro la lista interna e prendo il mote " + tempMote2.getUniqueId());
					if( tempMote2.getUniqueId().equals( rTempMote.getUniqueId() ) ) {
						System.out.println("Sono uguali, ho trovato il corrispettivo interno");
						tempMote.add(tempMote2);
						found = true;
						break;
					}
				}
			
				if (!found) {
					System.out.println("Erore non ho trovato neanche un mote interno");
					//potrei anche lanciare un'eccezione perch� non c'� quel nodo
					return false;
				}
			}
		
			//Ho creato la lista di internal mote adesso posso creare il gruppo		
			return createMoteGroup( groupName, tempMote);
//		}
	}

	@Override
	public InternalMoteGroup getMoteGroup(String groupName) {
		Iterator<InternalMoteGroup> it = moteGroup.iterator();
		InternalMoteGroup mote = null;
		
//		synchronized (lockGroupList ) {
		while( it.hasNext() ) {
			mote = it.next();
			if ( mote.getName().equals( groupName ) ) {
				break;
			}
		}
//		}
		
		return mote;
	}

	@Override
	public ArrayList<InternalMoteGroup> getMoteGroupList() {
		return moteGroup;
	}

	@Override
	public boolean removeMoteGroup(String groupName) {
		
		synchronized (lockGroupList ) {
			
			boolean found = false;
			Iterator<InternalMoteGroup> it = moteGroup.iterator();
			int i = 1;
			InternalMoteGroup group = null;
			
			while( it.hasNext() ) {
				group = it.next();
				if ( group.getName().equals( groupName ) ) {
					removeMoteGroup( group );
					found = true;
					break;
				}
				
				i++;
			}
			
			return found;
		}
	}
	
	protected void removeMoteGroup( InternalMoteGroup group ) {
		remoteManager.removeRemoteMoteGroup((MoteGroup) group.getRemote());
		//moteGroup.get(position).removeAll();
		group.removeAll();
		moteGroup.remove(group);
	}

	protected boolean removeInternalMoteGroup(String groupName) {
		return this.removeMoteGroup(groupName);
	}

	@Override
	public boolean createSensorGroup(String groupName, InternalSensor sensor) {
		boolean found = false;
		Iterator<InternalSensorGroup> it = sensorGroup.iterator();
		
		while( it.hasNext() ) {
			if ( it.next().getName().equals( groupName ) ) {
				found = true;
				break;
			}
		}
		
		if (found)
			return false;
		
		InternalSensorGroup group = new InternalSensorGroupImpl(groupName, sensor, this);
		sensorGroup.add( group );
		remoteManager.createRemoteSensorGroup(group);
		
		return false;
	}
	
	protected boolean createInternalSensorGroup(String groupName, Sensor sensor) throws RemoteException {
		InternalMote tempMote = getInternalMote( sensor.getOwnerMoteId() );
		InternalSensor tempSensor = null;
		
		Iterator<InternalSensor> it = tempMote.getSensorList().iterator();
		
		while( it.hasNext() ) {
			tempSensor = it.next();
			if ( tempSensor.getUniqueId().equals( sensor.getUniqueId() ) ) {
				break;
			}
		}
		
		if (tempSensor == null)
			return false;
		else		
			return createSensorGroup( groupName, tempSensor );
	} 

	@Override
	public boolean createSensorGroup(String groupName,
			ArrayList<InternalSensor> sensorList) {
		Iterator<InternalSensor> it = sensorList.iterator();
		
		if ( !it.hasNext())
			return false;
		
		if( !createSensorGroup(groupName, it.next()) )
			return false;
		
		InternalSensorGroup group = getSensorGroup( groupName );
		while( it.hasNext() ) {
			group.add(it.next());
		}
		
		remoteManager.createRemoteSensorGroup(group);
		return true;
	}

	protected boolean createInternalSensorGroup(String groupName, ArrayList<Sensor> sensor) throws RemoteException {
		Iterator<Sensor> it = sensor.iterator();

		
		ArrayList<InternalSensor> tempSensor = new ArrayList<InternalSensor>(sensor.size());
		Sensor tempSensor2;
		InternalSensor tempSensor3;
		InternalMote tempMote;
		boolean found = false;
		
		if ( !it.hasNext())
			return false;
		
		//Trovo il mote interno
		while( it.hasNext() ) {
			tempSensor2 = it.next();
			tempMote = findMoteByUniqueId( tempSensor2.getOwnerMote().getUniqueId() );

		if ( tempMote == null )
		return false;
		
		//Trovo il sensore interno
		Iterator<InternalSensor> it2 = tempMote.getSensorList().iterator();
		while (it2.hasNext()) {
			tempSensor3 = it2.next();
			if( tempSensor3.getUniqueId().equals( tempSensor2.getUniqueId() ) ) {
				tempSensor.add(tempSensor3);
					found = true;
					break;
				}
			}
			
			if (!found) {
				//potrei anche lanciare un'eccezione perch� non c'� quel nodo
				return false;
			}
		}
		
		//Ho creato la lista di internal mote adesso posso creare il gruppo		
		return createSensorGroup( groupName, tempSensor);
	}

	@Override
	public InternalSensorGroup getSensorGroup(String groupName) {
		Iterator<InternalSensorGroup> it = sensorGroup.iterator();
		InternalSensorGroup sensor = null;
		
		while( it.hasNext() ) {
			sensor = it.next();
			if ( sensor.getName().equals( groupName ) ) {
				break;
			}
		}
		
		return sensor;
	}

	@Override
	public boolean removeSensorGroup(String groupName) {
		boolean found = false;
		Iterator<InternalSensorGroup> it = sensorGroup.iterator();
		InternalSensorGroup group = null;
		
		while( it.hasNext() ) {
			group = it.next();
			if ( group.getName().equals( groupName ) ) {
				remoteManager.removeRemoteSensorGroup((SensorGroup) group.getRemote());
				moteGroup.remove(group);
				found = true;
			}
		}
		
		
		return found;
	}
	
	protected boolean removeInternalSensorGroup(String groupName) {
		return this.removeSensorGroup(groupName);
	}

	

	
	private InternalMote findMoteByUniqueId( String id ) {
		Iterator<InternalMote> it  = moteList.iterator();
		InternalMote res = null;
		
		while( it.hasNext() ) {
			res = it.next();
			if ( res.getUniqueId().equals(id)) {
				return res;
			}
		}
		
		return res;
	}

    private void stampaSuFile(String s) {
    	
		Calendar cal = new GregorianCalendar();
		String AM_PM;
		if (cal.get(Calendar.AM_PM) == 0)
			AM_PM="AM";
		else
			AM_PM="PM";
		
		String time = "\nDay "+cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)+
				" at "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND)+":"+cal.get(Calendar.MILLISECOND)+" "+AM_PM;
    	
        try {
            FileWriter fileout = new FileWriter("Topology_messages.txt",true);
            BufferedWriter filebuf = new BufferedWriter(fileout);
            PrintWriter printout = new PrintWriter(filebuf);

            printout.println(time);
            printout.println(s);

            printout.close();
        }

        catch (Exception e) {
            System.out.println("ERRORE: "+e);
        }
    }
	
}
