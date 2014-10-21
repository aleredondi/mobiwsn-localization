package gateway;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

import common.classes.ObjectSearched;
import common.classes.ServerClassLoader;
import common.exceptions.ExceptionHandler;
import common.exceptions.MiddlewareException;
import common.exceptions.ResponseTimeoutException;

import remote_interfaces.WSNGateway;
import remote_interfaces.WSNGatewayManager;
import remote_interfaces.mote.Mote;

import gateway.comm.MessageListener;
import gateway.comm.MessageSender;
import gateway.comm.MiddlewareCorrelateMessageListener;
import gateway.comm.WSNInterface;
import gateway.comm.WSNInterfaceFactory;
import gateway.config.ConfigurationManager;
import gateway.group.mote.InternalMoteGroup;
import gateway.mote.InternalMote;
import gateway.mote.MoteFactory;
import gateway.protocol.basic_message.MiddlewareMessage;
import gateway.protocol.basic_message.MiddlewareMessageType;
import gateway.protocol.basic_message.PckScopeType;
import gateway.services.GenericInternalPublisher;
import gateway.services.InternalPublisher;
import gateway.services.InternalSubscriber;

/**
 * 
 * @author Alessandro Laurucci
 * @version 1.1
 *
 * Defines the basic gateway component to a wireless sensor network.
 * Depending on the specific PAN Coordinator mote connected to the Mesh Router,
 * this component must create the right interface component with which it can
 * exchange information from/to the WSN, following the rules defined for the middleware
 * protocol
 */
public class WSNGatewayInternalImpl implements InternalWSNGateway, MessageListener<MiddlewareMessage>, MessageSender<MiddlewareMessage>, InternalPublisher<InternalWSNGateway, ArrayList<InternalMote>>
{

	//My object for remote accessibility
	private WSNGatewayRemoteImpl remoteGateway;
	
	/*
	 * Object of the server manager obtained after the rmiregistration
	 */
	private WSNGatewayManager remoteObject;
		
	private GenericInternalPublisher<InternalWSNGateway,ArrayList<InternalMote>> publisher;
	/*
	 * List of the Object loaded on server that client can use
	 */
	private ArrayList<Object> loaded_class;
	
	/*
	 * private members definition
	 */
	private MoteFactory moteFactory;
	
	/*
	 * address and name of the gateway
	 */
	private String ipAddress;
	private String name;
	
	/*
	 * interface to the WSN
	 */
	private WSNInterface wsnInterface;
	
	private InternalWSNManagerImpl manager;
	
	/*
	 * internal reference to MiddlewareResponseListener
	 */
	private GatewaySender gatewaySender;
	
	/*
	 * identify the last idgroup used
	 */
	private int idgroupMote;
	
	/*
	 * identify the last idgroup used
	 */
	private int idgroupSensor;
	
	private int coordX;
	private int coordY;
		
	//Objects used for synchronization
	private Object lockSensorList;
	private Object memoryLock;
	private Object classLock;
	
	// paths
	private String gw_path = "bin/gateway/";
	
	private String classi_dir = gw_path + "classi/";
	private String temp_dir = gw_path + "tempFiles/";
	private String moteFunct_dir = gw_path + "moteFunctions/";

	
	/**
	 * Constructor method
	 * @param moteType type of motes in the WSN
	 * @param ip address of the gateway
	 * @param rmi WSNGatewayManager object
	 * @throws RemoteException
	 */
	public WSNGatewayInternalImpl( String ip, WSNGatewayManager rmi, String comm, String name) throws RemoteException
	{
		this.remoteObject=rmi;
		
		/*
		 * Initialize the IdgroupCounter 
		 */
		this.idgroupMote=0;
		this.idgroupSensor=0;
				
		/*
		 * Instance interface to wsn and addListener for incoming middleware messages
		 */
		wsnInterface = WSNInterfaceFactory.getInterface(comm);
		wsnInterface.addListener(this);
		
		/*
		 * create a component to  make sinchronous the sending of a middleware message and the reception 
		 * of the response.  
		 */
		gatewaySender = new GatewaySender(
							Short.parseShort(
									ConfigurationManager.getInstance().getParameter("response_timeout")
													)
																);

		
		manager = new InternalWSNManagerImpl(this);
		publisher = new GenericInternalPublisher<InternalWSNGateway,ArrayList<InternalMote>>();
		/*
		 * Instanciate the array list and create the object used for the synchronization
		 */
		loaded_class=new ArrayList<Object>();
		
		//start interface
		this.startInterface();
		
		
		//Instanciate lock Objects
		this.lockSensorList=new Object();
      	this.memoryLock =new Object();
		this.classLock=new Object();		
		
		this.ipAddress=ip;
		this.name = name;
		
		this.coordX=Integer.parseInt(ConfigurationManager.getInstance().getParameter("coordX"));
		this.coordY=Integer.parseInt(ConfigurationManager.getInstance().getParameter("coordY"));

		// se necessario creo le cartelle classi, tempFiles e moteFunctions nei bin
			
		if((new File (classi_dir)).exists()) {
			//System.out.println("\n" + classi_dir + " esiste !");
		}
		else {
		    if ((new File(classi_dir)).mkdir()) {
		      System.out.println("\n>Directory: " + classi_dir + " created");
		    }
		}

		if((new File (temp_dir)).exists()) {
			//System.out.println("\n" +temp_dir + " esiste !");
		}
		else {
		    if ((new File(temp_dir)).mkdir()) {
			      System.out.println("\n>Directory: " + temp_dir + " created");
		    }
		}

		if((new File (moteFunct_dir)).exists()) {
			//System.out.println("\n" +moteFunct_dir + " esiste !");
		}
		else {
		    if ((new File(moteFunct_dir)).mkdir()) {
			      System.out.println("\n>Directory: " + moteFunct_dir + " created");
		    }
		}

		//Creo l'oggeto remoto
		remoteGateway = new WSNGatewayRemoteImpl( this, (RemoteWSNManager) manager.getRemote());
	}
	
	
	public WSNGatewayManager getManager() {
		return remoteObject;
	}

	
	/**
	 * This method is used to get the coordinates of the gateway
	 * @return an array with the coordinates of the gateway
	 * @throws RemoteException
	 */
	public int[] getCoord()
	{
		int coord[]=new int[2];
		coord[0]=this.coordX;
		coord[1]=this.coordY;
		return coord;
	}
	
	/**
	 * Method used to start interface
	 * @throws RemoteException
	 */
	public void startInterface()
	{
		/*
		 * start Interface (open ports, start Interaction thread....
		 */
		wsnInterface.startInterface();
	}
	
	/**
	 * Method used to stop interface
	 * @throws RemoteException
	 */
	public void stopInterface()
	{
		wsnInterface.stopInterface();
	}
	
	
	/**
	 * Method used to verify if the gateway interface is started or not
	 * @return a boolean true if the gateway is started, else false
	 * @throws RemoteException 
	 */
	public boolean isStarted()
	{
		return wsnInterface.isStarted();
	}
	
	
	/**
	 * Method that return the name of the gateway
	 * @return a string name
	 * @throws RemoteException
	 */
	public String getName()
	{
		return this.name;
	}
	
	
	/**
	 * Method used for getting the ip address of the gateway
	 * @return the ip Address
	 * @throws RemoteException
	 */
	public String getIpAddress()
	{
		return this.ipAddress;
	}
	
	
	/**
	 * This method return the next gateway in the list of group after that passed as input (gwName) using a specific sequential logic
	 * @param groupName name of the group
	 * @return the name of the next gateway in the list after that passed in the gwName parameter
	 * @throws RemoteException
	 */
	public String getNextInGroup(String groupName)throws RemoteException
	{
		String name=null;
		WSNGateway gw=null;
		ArrayList<WSNGateway> gwlist=this.remoteObject.getWSNGatewayGroup(groupName);
		for(int i=0;i<gwlist.size();i++)
		{
			gw=gwlist.get(i);
			if(gw.getName().equals(this.getName()) && gwlist.size()>i+1)
			{
				name= gwlist.get(i+1).getName();
				break;
			}
				
		}
		return name;
	}
	
	 /**
     * This method is used by the server while its starting, to initialize the array list with the object saved in its hardisk
     */
	public void serverStart()
	{
		
		String dir=classi_dir;  //classpath where the file.class are saved on the server	
    	File folder=new File(dir);
    	
    	String listDir[]=folder.list(); //obtain the name of the folders saved on the server in the directory "dir"
    	String nameFile;
    	String nameFolder;
    	
    	
    	Object element=null;
    	Class cl=null; 
    	
    	char arr[];
    	
    	if (listDir != null)
    	{
    		
	    		for (int i=0; i<listDir.length;i++)
	    		{

//TODO	    			
//	    		ServerClassLoader scl =new ServerClassLoader();
				nameFolder=listDir[i];
	    		System.out.println(nameFolder);
	    		arr=nameFolder.toCharArray();
	    		nameFile="";
	    		
	    		//obtain the name of the directory without the index in the last character of the name
	    		for(int k=0; k<arr.length-1;k++)
				{	
					nameFile=nameFile+arr[k]; 
				}
/*
	    		//obtain the name of the directory without the index in the last character of .class
	    		for(int k=0; k<arr.length-6;k++)
				{	
					nameFile=nameFile+arr[k]; 
				}
*/	    		
//	    		try 
//	    		{
	    			System.out.println("nameFile :" + nameFile);
	    			System.out.println("dir :" + dir);
//TODO
//	    			cl=scl.loadClass(nameFile, dir+nameFolder+"/", true);//obtain the class object
	    			//cl=scl.loadClass(nameFile, dir, true);//obtain the class object
//				} 
//	    		catch (ClassNotFoundException e) {e.printStackTrace();}
	    		
	    		try 
	    		{
					element=cl.newInstance(); //create a new instance of the class 
				} 
	    		catch (InstantiationException e) {e.printStackTrace();} 
				catch (IllegalAccessException e) {e.printStackTrace();}
	    		
				try 
				{
					
					Class typeIn[]={String.class};
					Object paramIn[]={nameFolder};
					
					//invoke the method setPath to modify the variable "path" of the object loaded
					element.getClass().getSuperclass().getDeclaredMethod("setPath", typeIn).invoke(element, paramIn); 
				} 
				catch (IllegalArgumentException e) {e.printStackTrace();} 
				catch (SecurityException e) {e.printStackTrace();} 
				catch (IllegalAccessException e) {e.printStackTrace();} 
				catch (InvocationTargetException e) {e.printStackTrace();} 
				catch (NoSuchMethodException e) {e.printStackTrace();}
			
				manageArrayList(-1, element);//add the object on the list			
	    	}
	    		
    	}
	}
	
	
	/**
	 * Method that manage the received message
	 */
	public void messageReceived(MiddlewareMessage message) 
	{
		try
		{
			
			/*
			 * then the responseListener appended by the gatewaySender
			 * in the sendMessageTask method is notified, and so the sendMessageTask
			 * method can go on and return;
			 */			
			this.gatewaySender.notifyCorrelateMessageListeners(message);
			
			switch(message.getMessageType())
			{
							
				case MOTE_ANNOUNCEMENT:

					/*
					 * first the message is handled by the WSNGatewayImpl
					 * (calling the proper messageHandler)
					 */
					manager.moteAnnouncementMessageHandler(message);

					break;
			
				case MOTE_LOSS:

					/*
					 * first the message is handled by the WSNGatewayImpl
					 * (calling the proper messageHandler)
					 */
					manager.moteLossMessageHandler(message);
				
					break;
			
				case SENSOR_READ_RESPONSE:
				case FUNCTIONALITY_ANNOUNCEMENT:
			
					//this.functionalityAnnouncementMessageHandler(message);
					InternalMote mote = manager.getInternalMote(message.getSrcMoteId());
					if ( mote != null ) {
						mote.messageReceived(message);
					}
			
					break;
					
				case COMMAND_RECEIVED:
					
					// insieme al notifyCorrelateMessageListeners si potrebbe gestire l'error_code scritto nel messaggio
					//manager.commandReceivedMessageHendler(message);
					
				default:
					// non può arrivare un messaggio con message type diverso da quelli definiti dato che 
					// verrebbe filtrato dal MiddlewareMessageBilder
		
			}
		
		}
		catch (Exception ex)
		{
			ExceptionHandler.getInstance().handleException(ex);
		}
		
	}
	
	
	
	/**
	 * returns a reference to the internal MiddlewareMessageSender object
	 * @return the sender message
	 */
	public MessageSender getMessageSender()
	{
		return this.gatewaySender;
	}
	
	
	/**
	 * Internal implementation of MiddlewareMessageSender.sendMessageTask
	 * @return the objetc gateway sender
	 */
	private class GatewaySender implements MessageSender<MiddlewareMessage> 
	{
		/*
		 * used to set message id of messages sen by the Gateway/Pan Coordinator
		 */
		private short panCoordinatorMessageId;
		
		/*
		 * response waiting time (in milliseconds)
		 */
		private short responseTimeout;
		
		/*
		 * internal list of listeners for correlate messages
		 */
		private Vector<MiddlewareCorrelateMessageListener> correlateMessageListenerList;
		
		/**
		 * Constructor
		 */
		public GatewaySender(short responseTimeout)
		{
			this.panCoordinatorMessageId = 0;
			this.responseTimeout = responseTimeout;
			this.correlateMessageListenerList = new Vector<MiddlewareCorrelateMessageListener>();
		}
		
		/**
		 * Method used to manage the sending of messages
		 * @throws ResponseTimeoutException
		 * @throws MiddlewareException 
		 */
		public synchronized void sendMessageTask(MiddlewareMessage message) throws ResponseTimeoutException, MiddlewareException
		{
			boolean addCorrelateMessageListener=false;
			
			/*
			 * the add of a response listener depends on MiddlewareMessageType and message originator
			 */
			switch (message.getMessageType())
			{
			
				case MOTE_ANNOUNCEMENT:
				case MOTE_LOSS:
				case SENSOR_READ_RESPONSE:
				case FUNCTIONALITY_ANNOUNCEMENT:
				case COMMAND_RECEIVED:
					throw new MiddlewareException(">MiddlewareMessage of type " +
							message.getMessageType().toString() + " cannot be sent to sensor network !");

				case MOTE_DISCOVERY:							
				case SENSOR_READ:
				case FUNCTIONALITY_COMMAND:
				//case MOTE_GROUP_COMMAND:
										
					if ( !message.getPckScope().equals(PckScopeType.BROADCAST) && !message.getPckScope().equals(PckScopeType.TO_GROUP) )
						addCorrelateMessageListener = true;
					else
						addCorrelateMessageListener = false;
					break;
					
				case MOTE_GROUP_COMMAND: // andrebbe sistemato il controllo dest (MOTE_GROUP_COMMAND) == src (COMMAND_RRECEIVED)
					break;				 // dato che il primo è l'id del gruppo mentre il secondo è il mote id
					
				default:
					throw new IllegalArgumentException(">MiddlewareMessage cannot be sent to sensor network because it has a MessageType UNKNOWN ! ");
			}
			
			if ( message.getMessageType().equals(MiddlewareMessageType.MOTE_GROUP_COMMAND) || message.getPckScope().equals(PckScopeType.TO_GROUP) )
			{
				System.out.println("\n>Sending a "+ message.getMessageType().toString()+ " message to group with id: "
						+ message.getDstGroupId() +"\n");
			}
			else
			{	
				System.out.println("\n>Sending a "+ message.getMessageType().toString()+ " message to mote: "
						+ message.getDstMoteId() +"\n");
			}					
			
			
			/*
			 * set messageId
			 */
			message.setMessageId(++panCoordinatorMessageId);
			
			InternalMote pan_coord = manager.getPanCoordinator();
			if (pan_coord!= null)
			{
				message.setNwkSrc(pan_coord.getNetworkAddress());
				message.setSrcMoteId(pan_coord.getId());
			}
			else
			{
				message.setNwkSrc(Mote.NWK_NULL_ADDRESS);
				message.setSrcMoteId(Mote.MOTE_NULL_ID);
			}
			
			
			if (addCorrelateMessageListener)
			{
				System.out.println("\n>Adding correlateMessageListener");	
				/*
				 *  creates a new correlateMessageListener object to wait for a correlate message
				 *  to be received and by processed by WSNGatewayImpl 
				 */
				MiddlewareCorrelateMessageListener listener = new MiddlewareCorrelateMessageListener(message);
				
				correlateMessageListenerList.add(listener);
				
				synchronized (listener)
				{
					/*
					 * send the message and wait for a notification of 
					 * received message
					 */
					wsnInterface.sendMessage(message);
					
					try
					{
						listener.wait(responseTimeout);
						
						/*
						 * isResponseReceived = true means that a response to message
						 * has been received and processed by gateway. Otherwise a 
						 * ResponseTimeoutException must be raised
						 */
						if (!listener.isCorrelateMessageReceived()) 
							throw new ResponseTimeoutException(message.getClass().getSimpleName(),message.getSrcMoteId(),message.getDstMoteId());
						
						
						/*
						 * when listener of message is notified by WSNGatewayImpl, it means that
						 * WSNGatewayImpl has received a message response for this message, and 
						 * the sendMessageTask can return;
						 */
					}
					catch (InterruptedException iex){ExceptionHandler.getInstance().handleException(iex);}
					
				}
				
				/*
				 * the listener is removed from list when notification has been performed.
				 */
				correlateMessageListenerList.remove(listener);
			}
			else
			{
				/*
				 * in this case a response message has not to be waited for and so the
				 * sendMessageTask returns when the wsnInterface.sendMessage() call
				 * returns.
				 */
					
				wsnInterface.sendMessage(message);
			
			}
		}
		
		/**
		 * notifies registered listeners waiting for correlateMessage
		 * @param correlateMessage MiddlewareMessage object listeners are waiting for
		 */
		private void notifyCorrelateMessageListeners(MiddlewareMessage correlateMessage)
		{
			/*
			 * read the content of message header and see if there is a listener
			 * for this correlate message
			 */
			synchronized( correlateMessageListenerList ) {
				for (MiddlewareCorrelateMessageListener listener : correlateMessageListenerList)
				{
					synchronized (listener)
					{
				
						if (correlateMessage.isCorrelateWith(listener.getCorrelateMessage()))
						{
							listener.setCorrelateMessageReceived(true);
							listener.notify();
							System.out.println("\n>ResponseMessage received!");
						}
					}
				}
			}
		}
	}
	
	
//SEARCH FUNCTION OBJECT METHODS

	/**
	 * This method is used for searching the class into the arrayList of the loadedObject
	 * @param param list of the parameters that identify the class to search
	 * @param version represent the version of the searched class
	 * @return this class return an array of integer that define for each object searched, if it is on the list, 
	 * and if it is saved on the server
	 */
	public int[] searchClass(ArrayList<String> param, int version)
	{
		//first value: if=-1 the object is not on the list or there is an old version; if=0 it is already on server
		//second value: define the index of the folder if the file is saved on server,  if this value remain -1 the object is not saved on server 
		int result[]={-1,-1}; 

//TODO
//		Object element;
//		int position;
//		boolean found;
//		
//		
//		//searching class "nameClass" in the ArrayList
//		System.out.println("Searching the class "+param.get(0)+" trough the loaded Objects");
//		synchronized(classLock)
//		{
//			position=ListSearchingManager.searchClassIntoList(this.loaded_class, param);
//			if(position!=-1)
//			{	
//				element= this.loaded_class.get(position);
//				
//				found=ListSearchingManager.verifyVersion(element, version);
//				
//				//the class in the position i has the same or better version then the searched class 
//				//so it has not to be loaded
//				if(found)
//				{
//					result[0]=0;
//					result[1]=ListSearchingManager.getPathObject(element);
//					return result;
//				}
//				//the class in the position i is an older version of the searched class so it has to be loaded
//				else 
//				{
//					result[1]=ListSearchingManager.getPathObject(element);	
//					return result;
//				} 
//			}
//		} 
			
		return result;
		
	}
	
	/**
	 * This is a searching method which return only the index of the folder in which the class searched is saved
	 * @param list of the object trough which do the searching
	 * @param param list of the parameters that identify the class to search
	 * @param version version class
	 * @return the index of the folder
	 */
	public int getSavedListObjectIndex(ArrayList<String>param, int version)
	{

//TODO
//		try {
//			return ListSearchingManager.getFolderIndex(this.loaded_class, param, version);
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return version;
	}
		
//FUNCTION LOAD AND SAVE METHODS
	
	/**
	 * Method used to get the object to load from client, and then load them in the function arraylist
	 * @param classToLoad name list of the class to load
	 * @param loade object used for reading data stream from client
	 */
	private void loadObjectManage(ArrayList<String> classToLoad, ServerClassLoader loader)
	{
//		int result=-1;
//		Object element=null;
//		Class cl=null;
//		
//		//while cycle for loading the object requested
//		for(String load : classToLoad)
//		{	
//			try 
//			{
//				//load the object and create a new instance of it 
//				System.out.println("Class to load is : " + load);
//				cl = loader.loadClass(load, true);
//				element = cl.newInstance();
//			} 
//			catch (InstantiationException e) {e.printStackTrace();} 
//			catch (IllegalAccessException e) {e.printStackTrace();}
//			catch (ClassNotFoundException e) {e.printStackTrace();}
//				
//			synchronized(classLock)
//			{
//				try 
//				{
//					result=ListSearchingManager.searchClassIntoList(this.loaded_class, (ArrayList<String>)element.getClass().getDeclaredMethod("getClassIdentificationParameter").invoke(element));
//				} 
//				catch (IllegalArgumentException e) {e.printStackTrace();} 
//				catch (SecurityException e) {e.printStackTrace();}
//				catch (IllegalAccessException e) {e.printStackTrace();} 
//				catch (InvocationTargetException e) {e.printStackTrace();} 
//				catch (NoSuchMethodException e) {e.printStackTrace();} 
//				
//				manageArrayList(result,element); //load object on the list
//			}
//		}
		
	}
	
	
	/**
	 * 
	 * @param classToSave list of the class to save
	 * @param posToSave list of the idnex position where save the class
	 * @param loader object used for managing the conenction
	 * @param oin sata stream with other client/gateway
	 * @param saveFolder folder where save the files
	 */
	private ArrayList<Integer> saveObjectManage(ArrayList<String> classToSave, ArrayList<Integer> posToSave, ServerClassLoader loader, ObjectInputStream oin, String saveFolder)
	{
		ArrayList<Integer> path= new ArrayList<Integer>();
//		File directory=null;
//		String saveDir;
//		Class cl=null;
//		Object element=null;
//		
//		String dir;
//		int result=-1;
//		
//		//save the class in the arraylist classToSave
//		for(int j=0;j<classToSave.size();j++)
//		{		
//			String nameClass=classToSave.get(j);
//
//			int pos=posToSave.get(j);
//
//			if(pos!=-1) // the class is already present on server so the new file has to overwrite the old file
//			{
//				dir= nameClass+pos;
//				path.add(pos);
//				saveDir=saveFolder+dir+"/";
//
//				try 
//				{
//					loader.downloadClassFromClient(oin, nameClass, saveDir);//invoke the method for downloading files
//				}	 	
//				catch (SocketException e) {e.printStackTrace();} 
//				catch (ClassNotFoundException e) {e.printStackTrace();} 
//				catch (IOException e) {e.printStackTrace();} 
//
//			}			
//			else // we have to save the file in a new directory
//			{
//				dir=loader.searchDir(saveFolder, nameClass);
//				path.add(dir.charAt(dir.length()-1)-'0');
//				saveDir=saveFolder+dir+"/";
//							
//				//create the new directory
//				directory=new File(saveDir);
//				directory.mkdir();	
//				
//				try 
//				{
//					loader.downloadClassFromClient(oin, nameClass, saveDir);//invoke the method for downloading files
//				}	 
//				catch (SocketException e) {e.printStackTrace();} 
//				catch (ClassNotFoundException e) {e.printStackTrace();} 
//				catch (IOException e) {e.printStackTrace();} 
//			}			
//			
////			 ### ### ###
///*			
//			dir= nameClass;
//			path.add(pos);
//			saveDir=saveFolder;
//			
//			try 
//			{
//				loader.downloadClassFromClient(oin, nameClass, saveDir);//invoke the method for downloading files
//			}	 	
//			catch (SocketException e) {e.printStackTrace();} 
//			catch (ClassNotFoundException e) {e.printStackTrace();} 
//			catch (IOException e) {e.printStackTrace();} 
//*/					
////			 ### ### ###
//
//			
//			//load the object and create a new instance of it
//			try 
//			{
//				System.out.println("Dir :" + saveDir);		
//				System.out.println("Class to load is : " + nameClass);
//				
//				cl = loader.loadClass(nameClass, saveDir, true);
//				element = cl.newInstance();
//			}	 
//			catch (InstantiationException e) {e.printStackTrace();} 
//			catch (IllegalAccessException e) {e.printStackTrace();}
//			catch (ClassNotFoundException e) {e.printStackTrace();}
//		
//			//the object to load is not present on list so it is loaded on first position
//			synchronized(classLock)
//			{
//				Class typeIn[]={String.class};
//				Object paramIn[]={dir};
//		
//				try 
//				{
//					result=ListSearchingManager.searchClassIntoList(this.loaded_class, (ArrayList<String>)element.getClass().getDeclaredMethod("getClassIdentificationParameter").invoke(element));
//					element.getClass().getSuperclass().getDeclaredMethod("setPath", typeIn).invoke(element, paramIn);
//				}	 
//				catch (IllegalArgumentException e) {e.printStackTrace();} 
//				catch (SecurityException e) {e.printStackTrace();}
//				catch (IllegalAccessException e) {e.printStackTrace();} 
//				catch (InvocationTargetException e) {e.printStackTrace();} 
//				catch (NoSuchMethodException e) {e.printStackTrace();} 
//		
//				manageArrayList(result,element); //load object on the list
//			}
//		}
		
		return path;
	}
	
	
	/**
	 * This method is used to load on the array list the classes that the client need to use for its applications
	 * @param hostName represent the ip address of the client
	 * @param port represent the port number of the client application
	 * @param classToLoad contains the list of the name class to load on server
	 * @param posToLoad contain the position of each class in the arraylist, if the class to load must overwrite another object
	 * @throws IOException, RemoteException 
	 */
	public void getClassClient(/*String clientName,*/ String hostName, int port, ArrayList<String> classToLoad, ArrayList<String> classToSave, ArrayList<Integer> posToSave) throws IOException, RemoteException
	{
		
		ServerClassLoader loader = new ServerClassLoader(/*clientName,*/ hostName, port);	
		if(classToLoad.size()+classToSave.size()>0)
		{	
			try 
			{
				loader.connect();//connect with the client
			} 
			catch (UnknownHostException e1) {e1.printStackTrace();} 
			catch (IOException e1) {e1.printStackTrace();}
		}
		
		if(classToLoad.size()>0)
		{	
			this.loadObjectManage(classToLoad, loader);//load functions
			loader.advertise();	//loading classload finished
		}
		
		//verify if there is something to save on server
		if(classToSave.size()>0)
		{	
			synchronized(this.memoryLock)
			{
				System.out.println("Download in progress...\n");
				ObjectInputStream oin=new ObjectInputStream(loader.getSocket().getInputStream()); //get socket input stream
			
				if(classToSave.size()>0)
				{		
					this.saveObjectManage(classToSave, posToSave, loader, oin, classi_dir);
				}	
				oin.close();
			}
		}
		if(classToLoad.size()+classToSave.size()>0)
		{
			loader.disconnect();//disconnection
		}
	}		 

	
	/**
	 * This method is used to get classes for a specific gatway group, save and load them in this gatewateway, 
	 * and then contact the other gateway of the group for passing them these object
	 * @param hostName represent the ip address of the client
	 * @param port represent the port number of the client application
	 * @param groupName is the name of the group
	 * @param classToLoad contains the list of the name class to load on server
	 * @param posToSave contain the position of each class in the arraylist, if the class to load must overwrite another object
	 * @param loadTemp list of the load temp files
	 * @param saveTemp list of the save temp files
	 * @param posToSaveTemp indicates the index of the folder where are the files to send
	 * @throws IOException 
	 * @throws RemoteException
	 */
	public void getGroupClass(/*String clientName,*/ String hostName, int port, String group, ArrayList<String> classToLoad, ArrayList<String> classToSave, ArrayList<Integer> posToSave, ArrayList<String> loadTemp, ArrayList<String> saveTemp, ArrayList<Integer> posToSaveTemp) throws IOException, RemoteException
	{
		
//		String saveDir;
//		String tempDir=temp_dir;
//		
//		ArrayList<Integer> path=new ArrayList<Integer>();
//		ServerClassLoader loader = new ServerClassLoader(/*clientName,*/ hostName, port);
//		
//		if(classToLoad.size()+classToSave.size()+loadTemp.size()>0)
//		{	
//			try 
//			{
//				loader.connect();//connect with the client
//			} 
//			catch (UnknownHostException e1) {e1.printStackTrace();} 
//		}
//		
//		if(classToLoad.size()>0)
//		{
//			this.loadObjectManage(classToLoad, loader);//load functions
//			loader.advertise();	//loading classload finished
//		}
//		
//		if(classToSave.size()+loadTemp.size()>0)
//		{	
//			synchronized(this.memoryLock)
//			{
//				System.out.println("Download in progress...\n");
//				ObjectInputStream oin=new ObjectInputStream(loader.getSocket().getInputStream()); //get socket input stream
//			
//				if(classToSave.size()>0)
//				{		
//					path.addAll(this.saveObjectManage(classToSave, posToSave, loader, oin, classi_dir));
//				}
//			
//				//save in these files the temp class
//				for(String lTemp : loadTemp)
//				{
//					saveDir=tempDir;
//					try 
//					{
//						loader.downloadClassFromClient(oin, lTemp, saveDir);//invoke the method for downloading files
//					}	 	
//					catch (SocketException e) {e.printStackTrace();} 
//					catch (ClassNotFoundException e) {e.printStackTrace();} 
//				}
//						
//				oin.close();
//			}
//		}
//		
//		System.out.println("\nInstall functions in the group\n");
//		//Get gateway of the group
//		ArrayList<WSNGateway> gwlist=this.remoteObject.getWSNGatewayGroup(group);
//		
//		for(WSNGateway gw : gwlist)
//		{
//			if(!gw.getName().equals(this.getName()))
//			{	
//				ClassManager cm = new ClassManager(this.getIpAddress(),6060, gw, gw_path);
//				cm.verifyGatewayClass(loadTemp, classToSave, saveTemp, path, posToSaveTemp);
//			}
//		}
//		
//		//remove temp files
//		this.removeFiles(classToLoad, temp_dir);
//		this.removeFiles(loadTemp, temp_dir);
//
	}
			
		
	/**
	 * This method is used to remove temp files
	 * @param classes names of the file to remove
	 */
	private void removeFiles(ArrayList<String> classes, String dir)
	{
		for(String file : classes)
		{	
			String command="rm "+dir+file+".class";
			Runtime run = Runtime.getRuntime();
			try 
			{
				run.exec(command);
			} 
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	/**
	 * Method used for removing a specific path
	 * @param dir path to remove
	 */
	private void removePath(String dir) throws RemoteException
	{
		String command="rm -rf "+dir;
		Runtime run = Runtime.getRuntime();
		try 
		{
			run.exec(command);
		} 
		catch (IOException e) {e.printStackTrace();}
	}
	
	/**
	 * Method used to allows a gateway to search a specific group of function trough a specific group of gateway
	 * @param group name of the group
	 * @param listLoad list of the class to load on gateway
	 * @param listSave list of the class to save on gateway
	 * @param posToSave index of the folder where to save the new class on gateway
	 * @return a boolean that represent if all the need class are loaded and saved
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws RemoteException
	 * @throws ClassNotFoundException
	 */
	public boolean searchGroupFunction(String group, ArrayList<ObjectSearched> listLoad, ArrayList<ObjectSearched> listSave, ArrayList<Integer> posToSave) throws UnknownHostException, IOException, RemoteException, ClassNotFoundException 
	{	
		boolean done=false;
//				
//		//Get gateway of the group
//		ArrayList<WSNGateway> gwlist=this.remoteObject.getWSNGatewayGroup(group);
//		
//		for(WSNGateway gw : gwlist) //obtain the gateways of the psecific group
//		{
//			done=this.gwDownloadClass(gw, listLoad, listSave, posToSave);
//		}
//		
		return done;
	}
	
	
	/**
	 * Method used to allows a gateway to search a specific group of function in a specific gateway
	 * @param gw WSNGateway object where search
	 * @param listLoad list of the class to load on gateway
	 * @param listSave list of the class to save on gateway
	 * @param posToSave index of the folder where to save the new class on gateway
	 * @return a boolean that represent if all the need class are loaded and saved
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws RemoteException
	 * @throws ClassNotFoundException
	 */
	public boolean gwDownloadClass(WSNGateway gw, ArrayList<ObjectSearched> listLoad, ArrayList<ObjectSearched> listSave, ArrayList<Integer> posToSave) throws UnknownHostException, IOException, RemoteException, ClassNotFoundException 
	{
		boolean done=false;
//		int result[]=new int[2];
//		int res=0;
//		String toSave, toLoad;
//		
//		if(!gw.getName().equals(this.getName())) //control that in the gateway group there's not itself
//		{
//			ArrayList<String> classToSave=new ArrayList<String>();//class to save from this specific gateway
//			ArrayList<String> classToLoad=new ArrayList<String>();//class to load from this specific gateway
//			
//			ArrayList<Integer> indexFolder= new ArrayList<Integer>();//index where to search the class on this gateway
//			
//			ArrayList<ObjectSearched> listPartialSave= new ArrayList<ObjectSearched>();
//			ArrayList<ObjectSearched> listPartialLoad= new ArrayList<ObjectSearched>();
//			
//			listPartialSave.addAll(listSave);// copy the listsave in this list for cycling
//			listPartialLoad.addAll(listLoad);// copy the listload in this list for cycling
//			
//			for(int i=0; i<listPartialSave.size();i++) //verify what classes to save has this specific gateway in its memory
//			{
//				//Get the i object from the list, and search if the gateway has it
//				ObjectSearched element=listPartialSave.get(i);
//				toSave=element.getName();
//				result=gw.searchClass(element.getClassIdentificationParameter(), element.getVersion());
//				
//				if (result[1]!=-1) //the gateway has this object
//				{
//					int val=gw.getSavedListObjectIndex(element.getClassIdentificationParameter(), element.getVersion()); //verify if the object is saved or only loaded
//					
//					if(val!=-1) //the class is saved so it has to be downloaded
//					{	
//						listSave=this.removeObjectFromList(listSave, toSave);//update the list of the class to search eliminating this one which is found
//						classToSave.add(toSave);
//						indexFolder.add(val);
//					}
//				}
//			}
//			
//			for(int i=0; i<listPartialLoad.size();i++) //verify what classes to load has this specific gateway in its memory
//			{
//				//Get the i object from the list, and search if the gateway has it
//				ObjectSearched element=listPartialLoad.get(i);
//				toLoad=element.getName();
//				result=gw.searchClass(element.getClassIdentificationParameter(), element.getVersion());
//				
//				if (result[1]!=-1) //the gateway has this object
//				{
//					int val=gw.getSavedListObjectIndex(element.getClassIdentificationParameter(), element.getVersion()); //verify if the object is saved or only loaded
//						
//					if(val!=-1) //the class is present so it has to be downloaded
//					{
//						listLoad=this.removeObjectFromList(listLoad, toLoad);//update the list of the class to search eliminating this one which is found
//						classToLoad.add(toLoad);
//						indexFolder.add(val);
//					}
//				}
//			}
//			
//			if(listPartialLoad.size()>0 || listPartialSave.size()>0)
//			{
//				ServerClassLoader scl= new ServerClassLoader(/*gw.getName(),*/ gw.getIpAddress(), 7070); 
//				gw.sendFiles(this.remoteGateway, classToLoad, classToSave, indexFolder); //invoke the function for sending files
//			
//				try 
//				{
//					Thread.sleep(2000); //wait the open fo the socket from other gateway
//				} 
//				catch (InterruptedException e) {e.printStackTrace();}
//			
//				scl.connect();//connect with gateway
//			
//				ObjectInputStream oin=new ObjectInputStream(scl.getSocket().getInputStream()); //get socket input stream
//				Object element=null;
//				Class cl=null;
//			
//			
//				String saveDir=null;
//				String path=null;
//			
//				for(int i=0;i<classToSave.size();i++)//download class to save
//				{
//					saveDir=classi_dir;
//					toSave=classToSave.get(i);
//					int pos =posToSave.get(i);
//				
//					if(pos!=-1) // the class is already present on server so the new file has to overwrite the old file
//					{
//						path=toSave+pos;
//						saveDir=saveDir+path+"/";
//						try 
//						{
//							scl.downloadClassFromClient(oin, toSave, saveDir);//invoke the method for downloading files
//						}	 	
//						catch (SocketException e) {e.printStackTrace();} 
//						catch (ClassNotFoundException e) {e.printStackTrace();} 
//					}	
//			
//					else // we have to save the file in a new directory
//					{
//						path=scl.searchDir(saveDir, toSave);
//						saveDir=saveDir+path+"/";
//						
//						//create the new directory
//						File directory=new File(saveDir);
//						directory.mkdir();
//						
//						try 
//						{
//							scl.downloadClassFromClient(oin, toSave, saveDir);//invoke the method for downloading files
//						}	 
//						catch (SocketException e) {e.printStackTrace();} 
//						catch (ClassNotFoundException e) {e.printStackTrace();} 
//					}	
//				
//					try 
//					{						
//						System.out.println("Class to load is : " + toSave);					
//						cl = scl.loadClass(toSave,  saveDir, true);
//						element = cl.newInstance();
//					}	 
//					catch (InstantiationException e) {e.printStackTrace();} 
//					catch (IllegalAccessException e) {e.printStackTrace();}
//					catch (ClassNotFoundException e) {e.printStackTrace();}
//			
//					//the object to load is not present on list so it is loaded on first position
//					synchronized(classLock)
//					{
//						Class typeIn[]={String.class};
//						Object paramIn[]={path};
//						
//						try 
//						{
//							res=ListSearchingManager.searchClassIntoList(this.loaded_class,(ArrayList<String>)element.getClass().getDeclaredMethod("getClassIdentificationParameter").invoke(element));
//							element.getClass().getSuperclass().getDeclaredMethod("setPath", typeIn).invoke(element, paramIn);
//						}		 
//						catch (IllegalArgumentException e) {e.printStackTrace();} 
//						catch (SecurityException e) {e.printStackTrace();}
//						catch (IllegalAccessException e) {e.printStackTrace();} 
//						catch (InvocationTargetException e) {e.printStackTrace();} 
//						catch (NoSuchMethodException e) {e.printStackTrace();} 
//			
//						manageArrayList(res,element); //load object on the list
//					}
//				
//				}
//			
//			
//				for(String load : classToLoad) //download class to load
//				{
//					scl.downloadClassFromClient(oin, load, temp_dir);
//					
//					try 
//					{
//						//load the object and create a new instance of it 
//						System.out.println("Class to load is : " + load);
//						cl = scl.loadClass(load,temp_dir, true);
//						element = cl.newInstance();
//					} 
//					catch (InstantiationException e) {e.printStackTrace();} 
//					catch (IllegalAccessException e) {e.printStackTrace();}
//					catch (ClassNotFoundException e) {e.printStackTrace();}
//					
//					synchronized(classLock)
//					{
//						try 
//						{
//							res=ListSearchingManager.searchClassIntoList(this.loaded_class, (ArrayList<String>)cl.getDeclaredMethod("getClassIdentificationParameter").invoke(element));
//						} 
//						catch (IllegalArgumentException e) {e.printStackTrace();} 
//						catch (SecurityException e) {e.printStackTrace();}
//						catch (IllegalAccessException e) {e.printStackTrace();} 
//						catch (InvocationTargetException e) {e.printStackTrace();} 
//						catch (NoSuchMethodException e) {e.printStackTrace();}
//					
//						manageArrayList(res,element); //load object on the list
//					}
//				
//				}
//				this.removeFiles(classToLoad, temp_dir); //remove temp files
//			
//			
//				//close stream and disconnect
//				oin.close();
//				scl.disconnect();
//			}				
//		}
//		
//		//verify if all classes are loaded and saved
//		if(listLoad.size()==0 && listSave.size()==0)
//			done= true;
		
		return done;
	}
	
	
	
	/**
	 * Method used for removing a specific element from an ArrayList
	 * @param list to analize
	 * @param nameClass name of the object to remove
	 * @return the new list updated
	 */
	private ArrayList<ObjectSearched> removeObjectFromList(ArrayList<ObjectSearched> list, String nameClass)
	{
		ArrayList<ObjectSearched> result=new ArrayList<ObjectSearched>();
		
		
		for(ObjectSearched element : list)
		{
			if(!element.getName().equals(nameClass))
				result.add(element);
		}
		return result;
	}
	
	
	/**
	 * Method used for sending files of the need classes for a speicific gateway
	 * @param gw is the object gateway who request the classes
	 * @param classToLoad list of the class to load
	 * @param classToSave list of the class to save 
	 * @param indexFolder indexes of the folder where the classes are saved
	 * @throws RemoteException
	 */
	public void sendFiles (WSNGateway gw,  ArrayList<String> classToLoad, ArrayList<String> classToSave, ArrayList<Integer> indexFolder) throws RemoteException
	{
//		classToSave.addAll(classToLoad);
//		ClassManager cm= new ClassManager(this.getIpAddress(), 7070, gw, gw_path, classToSave, indexFolder);
//		
//		try 
//		{
//			cm.socketCreation();
//		} 
//		catch (UnknownHostException e) {e.printStackTrace();} 
//		catch (IOException e) {e.printStackTrace();}
//		
//		cm.start();
	}
	

//MANAGEMENT FUNCTION LIST METHODS
	
	/**
	 * This method is used by the client to invoke the methods of the objects loaded on the server
	 * @param inputSearched list of parameters input for the searching
	 * @param typeIn represent the inputs type of the method requested
	 * @param paramIn contain the inputs of the method
	 * @return the method return an object that represent the result of the method invoked
	 */
	public Object useMethod(ArrayList<String> inputSearched,String nameMethod, Class typeIn[], Object paramIn[])
	{
		Object output=new Object(); 
//		Method met=null;
//		int index=0;
//		Object toUse=null;
//		
//		//search the object with the name requested by the client
//		synchronized(classLock)
//		{
//			index=ListSearchingManager.searchClassIntoList(this.loaded_class, inputSearched);
//			if(index!=-1)
//			{
//				toUse=loaded_class.get(index); //invoke the object founded
//				try 
//				
//				{
//					met = toUse.getClass().getDeclaredMethod(nameMethod,typeIn); //get the method requested
//					output=met.invoke(toUse,paramIn);//invoke the method
//				} 
//				catch (SecurityException e) {	e.printStackTrace();} 
//				catch (NoSuchMethodException e) {	e.printStackTrace();} 
//				catch (IllegalArgumentException e) {	e.printStackTrace();} 
//				catch (IllegalAccessException e) {	e.printStackTrace();} 
//				catch (InvocationTargetException e) {	e.printStackTrace();}
//		
//			}
//		}
//			
		return output;
	}

	
	/**
	 * This method manage the operation ob the array list
	 * @param pos identify the position of the older version of the object on the list
	 * @param element identify the object to load on the list
	 */
	public void manageArrayList(int pos, Object element)
	{
		if (pos==-1)//the object is not present on the list
		{
			loaded_class.add(0,element);
			System.out.println("The object is added on the list\n");
			
		}
		else
		{
			loaded_class.remove(pos);
			loaded_class.add(0,element);
			System.out.println("The object overwrite the old version in the list\n");
		}
		
	}
	
		
	/**
	 * print the list of the function loaded on gateway
	 */
	public void printList()
	{
		System.out.println("\nList of the object loaded");
		Object element;
		synchronized(classLock)
		{
	
			for (int i=0;i<loaded_class.size();i++)
			{	
				element=loaded_class.get(i);
				try 
				{	
					System.out.println("name of the object : "+element.getClass().getName()+"   version : "+element.getClass().getDeclaredMethod("getVersion").invoke(element)+"   path : "+element.getClass().getSuperclass().getDeclaredMethod("getPath").invoke(element)+"   author : "+loaded_class.get(i).getClass().getDeclaredField("author").get(null).toString());
				} 
				catch (SecurityException e) {e.printStackTrace();} 
				catch (NoSuchFieldException e) {e.printStackTrace();} 
				catch (IllegalArgumentException e) {e.printStackTrace();} 
				catch (IllegalAccessException e) {e.printStackTrace();} 
				catch (InvocationTargetException e) {e.printStackTrace();} 
				catch (NoSuchMethodException e) {e.printStackTrace();}
			}
			System.out.println("\n");

		}
	}
	
		
	
	
	@Override
	public String getFunctionDirectory() {
		return moteFunct_dir;
	}
	 
	@Override
	public String getClassDirectory() {
		return classi_dir;
	}
	
	@Override
	public Object getMemoryLock() {
		return this.memoryLock;
	}

	public String getTemporaryDirectory() {
		return temp_dir;
	}
	
	public ArrayList<Object> getLoadedClass() {
		return loaded_class;
	}


	@Override
	public int getMoteNumber() {
		//TODO
		return 0;
	}

	@Override
	public void sendMessageTask(MiddlewareMessage message)
			throws ResponseTimeoutException, MiddlewareException {
		gatewaySender.sendMessageTask(message);
	}

	@Override
	public WSNGateway getRemoteGateway() {
		return remoteGateway;
	}


/*
 * (non-Javadoc)
 * Publisher methods
 * @see gateway.services.InternalPublisher#addSubscriber(gateway.services.InternalSubscriber)
 */
	
	
	@Override
	public void addSubscriber(
			InternalSubscriber<InternalWSNGateway, ArrayList<InternalMote>> s) {
		publisher.addSubscriber(s);
		
	}

	@Override
	public void removeSubscriber(
			InternalSubscriber<InternalWSNGateway, ArrayList<InternalMote>> s) {
		publisher.removeSubscriber(s);
		
	}


	@Override
	public void notifyNetworkChange() {
		publisher.notifySubscribers( this, manager.getMoteList());
		try {
			remoteGateway.notifyNetworkChange();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}


	
	//GROUP METHODS
	@Override
	public boolean createMoteGroup(String groupName, InternalMote mote) {
		return manager.createMoteGroup(groupName, mote);
	}


	@Override
	public boolean createMoteGroup(String groupName,
			ArrayList<InternalMote> moteList) {
		return manager.createMoteGroup(groupName, moteList);
	}


	@Override
	public InternalMoteGroup getMoteGroup(String groupName) {
		return manager.getMoteGroup(groupName);
	}


	@Override
	public ArrayList<InternalMoteGroup> getMoteGroupList() {
		return manager.getMoteGroupList();
	}
		
}
