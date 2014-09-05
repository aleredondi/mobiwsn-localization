package gateway;

import gateway.comm.MessageSender;
import gateway.group.mote.InternalMoteGroup;
import gateway.mote.InternalMote;
import gateway.services.GenericRemotePublisher;

import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

import common.classes.ServerClassLoader;
import common.exceptions.MiddlewareException;

import remote_interfaces.WSNGateway;
import remote_interfaces.group.Group;
import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteGroup;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.sensor.SensorGroup;
import remote_interfaces.services.Publisher;
import remote_interfaces.services.Subscriber;

public class WSNGatewayRemoteImpl extends UnicastRemoteObject implements WSNGateway, Publisher<WSNGateway, ArrayList<Mote>> {

	private InternalWSNGateway my_gateway;
	private RemoteWSNManager manager;
	private GenericRemotePublisher<WSNGateway, ArrayList<Mote>> publisher;
	
	private Object classLock;
	private Object memoryLock;	
	
	

	
	WSNGatewayRemoteImpl(InternalWSNGateway my_gateway, RemoteWSNManager manager) throws RemoteException {
		this.my_gateway = my_gateway;
		publisher = new GenericRemotePublisher<WSNGateway, ArrayList<Mote>>();
		this.manager = manager;
	}
	
	
	@Override
	public int[] getCoord() throws RemoteException {
		return my_gateway.getCoord();
	}

	@Override
	public int getSavedListObjectIndex(ArrayList<String> param, int version)
			throws RemoteException {
		//TODO
		return 0;
	}

//	@Override
//	public boolean addSensorToGroup(String groupName, Sensor sensor)
//			throws RemoteException {
//		// TODO Sistemare
//		return true; //my_gateway.addSensorToGroup(groupName, sensor);
//	}
//
//	@Override
//	public boolean createMoteGroup(String groupName, ArrayList<Mote> moteList)
//			throws RemoteException {
//		// TODO Sistemare
//		return true; //my_gateway.createMoteGroup(groupName, moteList);
//	}
//
//	@Override
//	public boolean createMoteGroup(String hostName, int port, String groupName,
//			ArrayList<Mote> moteList, FunctionList functions, String oldgw)
//			throws IOException, RemoteException {
//		// TODO Sistemare
//		return true; //my_gateway.createMoteGroup(hostName, port, groupName, moteList, functions, oldgw);
//	}
//
//	@Override
//	public boolean createSensorGroup(String groupName,
//			ArrayList<Sensor> sensorList) throws RemoteException {
//		// TODO Sistemare
//		return true; //my_gateway.createSensorGroup(groupName, sensorList);
//	}
//
//	@Override
//	public void getClassClient(String hostName, int port,
//			ArrayList<String> classToLoad, ArrayList<String> classToSave,
//			ArrayList<Integer> posToSave) throws IOException, RemoteException {
//		
//		ServerClassLoader loader = new ServerClassLoader(/*clientName,*/ hostName, port);	
//		if(classToLoad.size()+classToSave.size()>0)
//		{	
//			try 
//			{
//				loader.connect();//connect with the client
//			} 
//			catch (UnknownHostException e1) {e1.printStackTrace();} 
//			catch (IOException e1) {e1.printStackTrace();}
//		}
//		
//		if(classToLoad.size()>0)
//		{	
//			this.loadObjectManage(classToLoad, loader);//load functions
//			loader.advertise();	//loading classload finished
//		}
//		
//		//verify if there is something to save on server
//		if(classToSave.size()>0)
//		{	
//			synchronized(this.memoryLock)
//			{
//				System.out.println("Download in progress...\n");
//				ObjectInputStream oin=new ObjectInputStream(loader.getSocket().getInputStream()); //get socket input stream
//			
//				if(classToSave.size()>0)
//				{		
//					this.saveObjectManage(classToSave, posToSave, loader, oin, my_gateway.getClassDirectory());
//				}	
//				oin.close();
//			}
//		}
//		if(classToLoad.size()+classToSave.size()>0)
//		{
//			loader.disconnect();//disconnection
//		}
//	}
//
//	@Override
//	public int[] getCoord() throws RemoteException {
//		return my_gateway.getCoord();
//	}
//
//	@Override
//	public void getGroupClass(String hostName, int port, String group,
//			ArrayList<String> classToLoad, ArrayList<String> classToSave,
//			ArrayList<Integer> posToSave, ArrayList<String> loadTemp,
//			ArrayList<String> saveTemp, ArrayList<Integer> posToSaveTemp)
//			throws IOException, RemoteException {
//
//		ServerClassLoader loader = new ServerClassLoader(/*clientName,*/ hostName, port);	
//		if(classToLoad.size()+classToSave.size()>0)
//		{	
//			try 
//			{
//				loader.connect();//connect with the client
//			} 
//			catch (UnknownHostException e1) {e1.printStackTrace();} 
//			catch (IOException e1) {e1.printStackTrace();}
//		}
//		
//		if(classToLoad.size()>0)
//		{	
//			this.loadObjectManage(classToLoad, loader);//load functions
//			loader.advertise();	//loading classload finished
//		}
//		
//		//verify if there is something to save on server
//		if(classToSave.size()>0)
//		{	
//			synchronized(this.memoryLock)
//			{
//				System.out.println("Download in progress...\n");
//				ObjectInputStream oin=new ObjectInputStream(loader.getSocket().getInputStream()); //get socket input stream
//			
//				if(classToSave.size()>0)
//				{		
//					this.saveObjectManage(classToSave, posToSave, loader, oin, my_gateway.getClassDirectory());
//				}	
//				oin.close();
//			}
//		}
//		if(classToLoad.size()+classToSave.size()>0)
//		{
//			loader.disconnect();//disconnection
//		}
//	}

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
//					result=ListSearchingManager.searchClassIntoList(my_gateway.getLoadedClass(), (ArrayList<String>)element.getClass().getDeclaredMethod("getClassIdentificationParameter").invoke(element));
//				} 
//				catch (IllegalArgumentException e) {e.printStackTrace();} 
//				catch (SecurityException e) {e.printStackTrace();}
//				catch (IllegalAccessException e) {e.printStackTrace();} 
//				catch (InvocationTargetException e) {e.printStackTrace();} 
//				catch (NoSuchMethodException e) {e.printStackTrace();} 
//				
//				my_gateway.manageArrayList(result,element); //load object on the list
//			}
//		}
//		
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
//					result=ListSearchingManager.searchClassIntoList(my_gateway.getLoadedClass(), (ArrayList<String>)element.getClass().getDeclaredMethod("getClassIdentificationParameter").invoke(element));
//					element.getClass().getSuperclass().getDeclaredMethod("setPath", typeIn).invoke(element, paramIn);
//				}	 
//				catch (IllegalArgumentException e) {e.printStackTrace();} 
//				catch (SecurityException e) {e.printStackTrace();}
//				catch (IllegalAccessException e) {e.printStackTrace();} 
//				catch (InvocationTargetException e) {e.printStackTrace();} 
//				catch (NoSuchMethodException e) {e.printStackTrace();} 
//		
//				my_gateway.manageArrayList(result,element); //load object on the list
//			}
//		}
//		
		return path;
	}

	@Override
	public String getIpAddress() throws RemoteException {
		return my_gateway.getIpAddress();
	}

	/*@Override
	public ArrayList<MoteGroup> getMoteGroupList() throws RemoteException {
		// TODO Sistemare
		return null; //my_gateway.getMoteGroupList();
	}*/

	@Override
	public ArrayList<Mote> getMoteList() throws RemoteException {
		return ( manager.getMoteList() );
	}

	@Override
	public String getName() throws RemoteException {
		// TODO Auto-generated method stub
		return my_gateway.getName();
	}

	@Override
	public String getNextInGroup(String groupName) throws RemoteException {
		return my_gateway.getNextInGroup(groupName);
	}

	@Override
	public Mote getPanCoordinator() throws RemoteException {
		return manager.getPanCoordinator();
	}

//	@Override
//	public int getSavedListObjectIndex(ArrayList<String> param, int version)
//			throws RemoteException {
//		return my_gateway.getSavedListObjectIndex(param, version);
//	}


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
//	@Override
//	public boolean gwDownloadClass(WSNGateway gw,
//			ArrayList<ObjectSearched> listLoad,
//			ArrayList<ObjectSearched> listSave, ArrayList<Integer> posToSave)
//			throws UnknownHostException, IOException, RemoteException,
//			ClassNotFoundException {
//		// TODO Auto-generated method stub
//		boolean done=false;
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
//				gw.sendFiles(this, classToLoad, classToSave, indexFolder); //invoke the function for sending files
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
//					saveDir=my_gateway.getClassDirectory();
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
//							res=ListSearchingManager.searchClassIntoList(my_gateway.getLoadedClass(),(ArrayList<String>)element.getClass().getDeclaredMethod("getClassIdentificationParameter").invoke(element));
//							element.getClass().getSuperclass().getDeclaredMethod("setPath", typeIn).invoke(element, paramIn);
//						}		 
//						catch (IllegalArgumentException e) {e.printStackTrace();} 
//						catch (SecurityException e) {e.printStackTrace();}
//						catch (IllegalAccessException e) {e.printStackTrace();} 
//						catch (InvocationTargetException e) {e.printStackTrace();} 
//						catch (NoSuchMethodException e) {e.printStackTrace();} 
//			
//						my_gateway.manageArrayList(res,element); //load object on the list
//					}
//				
//				}
//			
//			
//				for(String load : classToLoad) //download class to load
//				{
//					scl.downloadClassFromClient(oin, load, my_gateway.getTemporaryDirectory());
//					
//					try 
//					{
//						//load the object and create a new instance of it 
//						System.out.println("Class to load is : " + load);
//						cl = scl.loadClass(load,my_gateway.getTemporaryDirectory(), true);
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
//							res=ListSearchingManager.searchClassIntoList(my_gateway.getLoadedClass(), (ArrayList<String>)cl.getDeclaredMethod("getClassIdentificationParameter").invoke(element));
//						} 
//						catch (IllegalArgumentException e) {e.printStackTrace();} 
//						catch (SecurityException e) {e.printStackTrace();}
//						catch (IllegalAccessException e) {e.printStackTrace();} 
//						catch (InvocationTargetException e) {e.printStackTrace();} 
//						catch (NoSuchMethodException e) {e.printStackTrace();}
//					
//						my_gateway.manageArrayList(res,element); //load object on the list
//					}
//				
//				}
//// TODO Sistemare sistemare sistemare				
//				//my_gateway.removeFiles(classToLoad, my_gateway.getTemporaryDirectory()); //remove temp files
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
//		
//		return done;
//	}

	/**
	 * Method used for removing a specific element from an ArrayList
	 * @param list to analize
	 * @param nameClass name of the object to remove
	 * @return the new list updated
	 */
//	private ArrayList<ObjectSearched> removeObjectFromList(ArrayList<ObjectSearched> list, String nameClass)
//	{
//		ArrayList<ObjectSearched> result=new ArrayList<ObjectSearched>();
//		
//		
//		for(ObjectSearched element : list)
//		{
//			if(!element.getName().equals(nameClass))
//				result.add(element);
//		}
//		return result;
//	}

	@Override
	public boolean isStarted() throws RemoteException {
		return my_gateway.isStarted();
	}


	@Override
	public boolean refreshMoteList() throws RemoteException {
		return manager.refreshMoteList();
	}

	/*@Override
	public int removeMoteFromGroup(String groupName, Mote mote)
			throws RemoteException {
		// TODO Sistemare
		return 0;
	}

	@Override
	public boolean removeMoteGroup(String groupName) throws RemoteException {
		// TODO Sistemare
		return false;
	}*/


	@Override
	public int[] searchClass(ArrayList<String> param, int version)
			throws RemoteException {
		// TODO Sistemare
		return null;
	}

//	@Override
//	public boolean searchGroupFunction(String group,
//			ArrayList<ObjectSearched> listLoad,
//			ArrayList<ObjectSearched> listSave, ArrayList<Integer> posToSave)
//			throws UnknownHostException, IOException, RemoteException,
//			ClassNotFoundException {
//		// TODO Sistemare
//		return false;
//	}
//
//	@Override
//	public void sendFiles(WSNGateway gw, ArrayList<String> classToLoad,
//			ArrayList<String> classToSave, ArrayList<Integer> indexFolder)
//			throws RemoteException {
//		// TODO Sistemare
//		
//	}
//
//	@Override
//	public void sendMoteGroupClass(String group, String gwname)
//			throws RemoteException {
//		// TODO Sistemare
//		
//	}

	@Override
	public void startInterface() throws RemoteException {
		my_gateway.startInterface();
	}

	@Override
	public void stopInterface() throws RemoteException {
		my_gateway.stopInterface();
	}

//	@Override
//	public Object useMethod(ArrayList<String> inputSearched, String nameMethod,
//			Class[] typeIn, Object[] paramIn) throws RemoteException {
//		// TODO Sistemare
//		return null;
//	}

	
		
	public void notifyNetworkChange() throws RemoteException {
		publisher.notifySubscribers( this, manager.getMoteList());
	}

	/**
	 * This method is used to create a new Sensor group
	 * @param groupName name of the group that the user wants to create
	 * @param sensorList arrayList of the Sensors
	 * @return result indicates if the group is created or not
	 * @throws RemoteException
	 */
	@Override
	public boolean createSensorGroup(String groupName, ArrayList<Sensor> sensorList) throws RemoteException {
		return manager.createSensorGroup(groupName, sensorList);
	}
	
	
	/**
	 * This method is used to add a new Sensor to an existent group
	 * @param groupName name of the group
	 * @param sensor to add
	 * @return if the operation is positive or not
	 * @throws RemoteException
	 */
//	@Override
//	public
//	boolean addSensorToGroup(String groupName, Sensor sensor) throws RemoteException {
//		return false;
//	}
	

	/**
	 * This method is used to remove a Sensor from an existent group
	 * @param groupName name of the group
	 * @param mote Sensor to remove
	 * @return if the operation is positive or not
	 * @throws RemoteException
	 */
//	@Override
//	public int removeSensorFromGroup(String groupName, Sensor sensor) throws RemoteException {
//		return 0;
//	}
	
	
	/**
	 * This method is used to delete a SensorGroup
	 * @param groupName name of the group that the user wants to delete
	 * @return result indicates if the group is deleted or not
	 * @throws RemoteException
	 */
	@Override
	public boolean removeSensorGroup(String groupName) throws RemoteException {
		return manager.removeSensorGroup(groupName);
	}
	
	
	/**
	 * This method is used to get a SensorGroup
	 * @param groupName name of the group that the user wants
	 * @return the object that represent the group
	 * @throws RemoteException
	 */
	@Override
	public SensorGroup getSensorGroup(String groupName) throws RemoteException {
		return manager.getSensorGroup(groupName);
	}

	/*
	 * Publisher methods
	 * 
	 * (non-Javadoc)
	 * @see remote_interfaces.Publisher#addSubscriber(remote_interfaces.Subscriber)
	 */

	@Override
	public void addSubscriber(Subscriber<WSNGateway, ArrayList<Mote>> s)
			throws RemoteException {
		publisher.addSubscriber(s);
	}

	@Override
	public void removeSubscriber(Subscriber<WSNGateway, ArrayList<Mote>> s)
			throws RemoteException {
		publisher.removeSubscriber(s);
	}


	@Override
	public synchronized boolean createMoteGroup(String groupName, Mote mote)
			throws RemoteException {
		System.out.println("GWR Un client mi chiede di creare il gruppo " + groupName );
		return manager.createMoteGroup(groupName, mote);
	}


	@Override
	public synchronized boolean createMoteGroup(String groupName, ArrayList<Mote> moteList)
			throws RemoteException {
		System.out.println("GWR Un client mi chiede di creare un gruppo " +  groupName + " con una lista di mote");
		return manager.createMoteGroup(groupName, moteList);
	}


	@Override
	public synchronized MoteGroup getMoteGroup(String groupName) throws RemoteException {
		System.out.println("GWR Un client mi chiede di il gruppo " + groupName);
		return manager.getMoteGroup(groupName);
	}


	@Override
	public synchronized ArrayList<MoteGroup> getMoteGroupList() throws RemoteException {
		return manager.getMoteGroupList();
	}


	@Override
	public synchronized boolean removeMoteGroup(String groupName) throws RemoteException {
		return manager.removeMoteGroup(groupName);
	}

}
