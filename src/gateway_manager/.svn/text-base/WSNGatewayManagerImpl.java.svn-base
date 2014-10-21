
package gateway_manager;


import remote_interfaces.*;
import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteGroup;
import remote_interfaces.group.*;
import remote_interfaces.sensor.*;
import remote_interfaces.services.Subscriber;

import gateway.services.InternalSubscriber;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import common.exceptions.MiddlewareException;
import common.exceptions.RegisteringGatewayException;


/**
 * Class that implements all the need manager methods, used to manage its
 * functions, it allows the manager to manage group, to register new gateway,
 * and allows so the client to has information about these things
 * 
 * @author Alessandro Laurucci
 * 
 */
public class WSNGatewayManagerImpl extends UnicastRemoteObject implements
		WSNGatewayManager, Subscriber<Group, Group> {

	// Object used for synchronization
	private Object lockGatewayList;

	private Object lockGatewayGroupList;

	private Object lockMoteGroupList;

	private Object lockSensorGroupList;

	private Object memoryLock;

	private String ipAddress;

	private String rmiAddress;

	private ArrayList<WSNGateway> wsnGatewayList;

	// This hashtable contain the list of WSNGateway that belong to each created
	// group
	private Hashtable<String, ArrayList<WSNGateway>> WSNGatewayGroupList;

	// This hashtable contain the list of mote that belong to each created group
	private Hashtable<String, ArrayList<WSNGateway>> moteGroupList;

	// This hashtable contain the list of sensor that belong to each created
	// group
	private Hashtable<String, ArrayList<WSNGateway>> sensorGroupList;

	// paths
	private static String temp_dir = "bin/gateway_manager/tempFiles/";

	public WSNGatewayManagerImpl(String rmi, String ip) throws RemoteException {
		this.wsnGatewayList = new ArrayList<WSNGateway>();
		this.WSNGatewayGroupList = new Hashtable<String, ArrayList<WSNGateway>>();
		this.moteGroupList = new Hashtable<String, ArrayList<WSNGateway>>();
		this.sensorGroupList = new Hashtable<String, ArrayList<WSNGateway>>();
		this.lockGatewayGroupList = new Object();
		this.lockGatewayList = new Object();
		this.lockMoteGroupList = new Object();
		this.lockSensorGroupList = new Object();
		this.memoryLock = new Object();
		this.ipAddress = ip;
		this.rmiAddress = rmi;

		if ((new File(temp_dir)).exists()) {
			// System.out.println("\n" +temp_dir + " esiste !");
		} else {
			if ((new File(temp_dir)).mkdir()) {
				System.out.println("\nDirectory: " + temp_dir + " created");
			}
		}
	}

	/**
	 * @return the rmi address of manager
	 * @throws RemoteException
	 */
	public String getRmiAddress() throws RemoteException {
		return this.rmiAddress;
	}

	/**
	 * Methods which return the list of the gateway recorded on this manager
	 * 
	 * @return list of gateways
	 * @throws RemoteException
	 */
	public ArrayList<WSNGateway> getWSNGatewayList() throws RemoteException {
		synchronized (this.lockGatewayList) {
			return wsnGatewayList;
		}
	}

	/**
	 * This method is used to receive the pointer of a specific gateway object
	 * 
	 * @param gwName
	 *            is the name of the gateway
	 * @return the Object WSNGateway requested
	 * @throws RemoteException
	 */
	public WSNGateway getWSNGateway(String gwName) throws RemoteException {
		WSNGateway result = null;
		synchronized (this.lockGatewayList) {
			for (int i = 0; i < wsnGatewayList.size(); i++) {
				if (wsnGatewayList.get(i).getName().equals(gwName)) {
					result = wsnGatewayList.get(i);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * This method is used to register a new Gateway on the list
	 * 
	 * @param gateway
	 *            represent the new gateway to add
	 * @return the result of the registration
	 * @throws RegisteringGatewayException 
	 */
	public boolean registerGateway(WSNGateway gateway) throws RemoteException, RegisteringGatewayException {
		boolean check = false;
		boolean result = false;
		int i = 0;

		System.out.print(">Registering gateway. Checking registered gateways...");
		synchronized (lockGatewayList) {
			for (i = 0; i < wsnGatewayList.size(); i++) {
				try {
					if (wsnGatewayList.get(i).getName().equals(gateway.getName())) {
						check = true;
						break;
					} 
				} catch (java.lang.NullPointerException ex) {
						//Il vecchio non è più raggiungibile quindi lo sovrascrivo
						System.out.print("An old gateway is dead (poveretto)...removing...");
						//this.unregisterGateway(gateway);
						this.wsnGatewayList.remove(i);
						System.out.println("done.");
				} catch (RemoteException e) {
					//Se il gateway è irraggiungibile lo elimino
					System.out.print("An old gateway is dead (poveretto)...removing...");
					//this.unregisterGateway(gateway);
					this.wsnGatewayList.remove(i);
					/*System.out.print("done. Adding new gateway...");
					this.wsnGatewayList.add(gateway);
					System.out.println(this.wsnGatewayList.get(
							wsnGatewayList.size() - 1).getName().toString());*/
					//result = true;
				}
			}
			if (!check) {
				System.out.print("done. Group not found. Creating...");
				this.wsnGatewayList.add(gateway);
				System.out.println(this.wsnGatewayList.get(
					wsnGatewayList.size() - 1).getName().toString());
				uploadGroups( gateway );
				result = true;
				System.out.println("done.");
			} else {
				//Ho già un gateway con lo stesso nome
				//provo a contattarlo per sapere se è vivo
				
				// TODO non funziona
				System.out.print("done. Existing group. Checking old one...");
/*				try {
					try {
						wsnGatewayList.get(i).getName();
					} catch (java.lang.NullPointerException ex) {
						//Il vecchio non è più raggiungibile quindi lo sovrascrivo
						System.out.print("done. Old one is dead...overwriting....");
						this.wsnGatewayList.set(i, gateway);
						System.out.println("done.");
					}
					
					System.out.println("Arrivo qui");*/
					//Non sono andato in eccezione quindi c'è ancora
					throw new RegisteringGatewayException("Already in list");
					
					
/*				} catch (RemoteException e) {
					//Se il gateway è irraggiungibile lo elimino
					this.unregisterGateway(gateway);
					this.wsnGatewayList.add(gateway);
					System.out.println(this.wsnGatewayList.get(
							wsnGatewayList.size() - 1).getName().toString());
					result = true;
				}*/
				
			}

		}
		return result;
	}
	
	@Override
	public boolean forceRegisterGateway( WSNGateway gateway )throws RemoteException {
		boolean result = false;
		int i = 0;

		synchronized (lockGatewayList) {
			for (i = 0; i < wsnGatewayList.size(); i++) {
				if (wsnGatewayList.get(i).getName().equals(gateway.getName())) {
					break;
				}
			}
				this.wsnGatewayList.set(i, gateway);
				System.out.println("OverWriting " + this.wsnGatewayList.get(
						wsnGatewayList.size() - 1).getName().toString());
				result = true;

		}
		return result;
		
	}
	
	public boolean unregisterGateway(WSNGateway gateway) throws RemoteException {
		int i = 0;
		
		synchronized (lockGatewayList) {
			for (i = 0; i < wsnGatewayList.size(); i++) {
				if (wsnGatewayList.get(i).getName().equals(gateway.getName())) {
					wsnGatewayList.remove(i);
					return true;
				}
			}
			return false;
		}		
	}

	// METHODS FOR MANAGER WSNGATEWAYGROUP

	/**
	 * This method is used to create a new WSNGatewayGroup
	 * 
	 * @param groupName
	 *            represent the name of the group that the user wants to create
	 * @param gwList
	 *            represent a String list that contains the name of the gateway
	 *            of the new created group
	 * @return result indicates if the group is created or not
	 * @throws RemoteException
	 */
	public boolean createWSNGatewayGroup(String groupName,
			ArrayList<WSNGateway> gwList) throws RemoteException {
		boolean result = false;
		synchronized (lockGatewayGroupList) {
			if (!WSNGatewayGroupList.containsKey(groupName))
				
			{
				System.out.println("Creating group '"+groupName + "'");
				WSNGatewayGroupList.put(groupName, gwList);
				result = true;
				System.out.println("Created!");
			}
		}
		return result;
	}

	/**
	 * This method is used to delete a WSNGatewayGroup
	 * 
	 * @param groupName
	 *            represent the name of the group that the user wants to delete
	 * @return result indicates if the group is deleted or not
	 * @throws RemoteException
	 */
	public boolean removeWSNGatewayGroup(String groupName)
			throws RemoteException {
		boolean result = false;
		synchronized (lockGatewayGroupList) {
			if (WSNGatewayGroupList.containsKey(groupName))
				
			{
				
				WSNGatewayGroupList.remove(groupName);
				result = true;
				System.out.println("Group '"+groupName + "' deleted");
			}
		}
		return result;
	}

	/**
	 * This method is used to add a WSNGateway to an existent group
	 * 
	 * @param groupName
	 *            represent the name of the group
	 * @param gwName
	 *            is the name of the Gateway
	 * @return result indicates if the Gateway is added/it is already on the
	 *         list (true) or not and it is not added (false)
	 * @throws RemoteException
	 */
	public boolean addWSNGatewayToGroup(String groupName, WSNGateway gwName)
			throws RemoteException {
		boolean result = false;
		boolean gatewayPresent = false; //premarini_scalise
		synchronized (lockGatewayGroupList) {
			if (WSNGatewayGroupList.containsKey(groupName))
				
			{
				ArrayList<WSNGateway> listGroup = WSNGatewayGroupList.get(groupName);
				
				
				for (int i = 0; i < listGroup.size(); i++) {
					if (listGroup.get(i).getName().equals(gwName.getName())) {
						result = false;
						gatewayPresent = true;
						break;
					}
				}
				if (!gatewayPresent) {
					System.out.println("Adding "+gwName.getName() + " to group '" + groupName + "'");
					listGroup.add(gwName);
					// WSNGatewayGroupList.put(groupName,listGroup);
					result = true;
					System.out.println("Added!");
				}
				
			}
		}
		return result;
	}

	/**
	 * This method is used to remove a WSNGateway from a group
	 * 
	 * @param groupName
	 *            represent the name of the group
	 * @param gwName
	 *            is the name of the Gateway
	 * @return result indicates if the Gateway is added/it is already on the
	 *         list (true) or not and it is not added (false)
	 * @throws RemoteException
	 */
	public boolean removeWSNGatewayFromGroup(String groupName, WSNGateway gwName)
			throws RemoteException {
		boolean result = false;
		synchronized (lockGatewayGroupList) {
			if (WSNGatewayGroupList.containsKey(groupName))
				
			{
				ArrayList<WSNGateway> listGroup = WSNGatewayGroupList.get(groupName);
				for (int i = 0; i < listGroup.size(); i++) {
					if (listGroup.get(i).getName().equals(gwName.getName())) {
						listGroup.remove(i);
						// WSNGatewayGroupList.put(groupName,listGroup);
						System.out.println(gwName.getName() + " deleted from group '"+groupName + "'");
						result = true;
						
					//In questo modo è possibile togliere un gw da un gruppo e aggiungerlo di nuovo
					//	if (listGroup.size() == 0)
					//		this.WSNGatewayGroupList.remove(groupName);

						break;

					}
				}

			}
		}
		return result;
	}

	/**
	 * This method is used to get a list of WSNGateway that belong to a specific
	 * group
	 * 
	 * @param groupName
	 *            represent the name of the group
	 * @return an arrayList that contains the Gateway belong to a specific group
	 * @throws RemoteException
	 */
	public ArrayList<WSNGateway> getWSNGatewayGroup(String groupName)
			throws RemoteException {
		ArrayList<WSNGateway> groupList;
		synchronized (this.lockGatewayGroupList) {
			groupList = WSNGatewayGroupList.get(groupName);
		}
		return groupList;
	}

	
	/**
	 * This method is used to verify if the Gateway Groups list is empty  
	 * 
	 * @return boolean 
	 *            to specify if the gateway groups list is empty or not
	 * @throws RemoteException
	 */
	public boolean isWSNGatewayGroupsListEmpty()
			throws RemoteException {
		boolean empty=true;
		synchronized (this.lockGatewayGroupList) {
			if(WSNGatewayGroupList.size()!=0)
				empty=false;
		}
		return empty;
	}
	
	/**
	 * This method is used to get the names of the groups of gateway   
	 * 
	 * @return ArrayList<String> 
	 *            to specify the names of the groups
	 * @throws RemoteException
	 */
	public ArrayList<String> getWSNGatewayGroupsNames()throws RemoteException{
		
		ArrayList<String> groupsName=new ArrayList<String>();
		 
		Enumeration e = WSNGatewayGroupList.keys();
		while( e.hasMoreElements() ){
			groupsName.add(e.nextElement().toString());
		}	
		return groupsName;
	}
	

	// METHODS FOR MANAGE MOTE GROUP

	@Override
	public boolean createMoteGroup(String groupName, Mote mote) throws RemoteException {
		ArrayList<Mote> tempList = new ArrayList<Mote>(1);
		tempList.add(mote);
		return createMoteGroup(groupName, tempList);
	}
	
	/**
	 * This method is used to create a new MoteGroup
	 * 
	 * @param groupName
	 *            represent the name of the group that the user wants to create
	 * @param moteList
	 *            represent the list of the motes for the new created group
	 * @return result indicates if the group is created or not
	 * @throws RemoteException
	 */
	@Override
	public boolean createMoteGroup(String groupName, ArrayList<Mote> moteList) throws RemoteException 
	{
		boolean result = false;
		ArrayList<WSNGateway> parents = new ArrayList<WSNGateway>();

		synchronized (lockMoteGroupList) {
			System.out.println("Creating group '"+groupName + "'");
			if (!this.moteGroupList.containsKey(groupName)) {
				if(moteList.size()!=0){
					parents = createMoteGroupOnNextGateway( groupName, (ArrayList<Mote>) moteList.clone(), parents );
				}

				moteGroupList.put(groupName, parents);
				
				result = true;
	
				System.out.println("Created!");
			}
		}
		if(!result)System.out.println("Not created!");
		
		return result;

	}
	
	
	private ArrayList<WSNGateway> createMoteGroupOnNextGateway(String groupName, ArrayList<Mote> moteList, ArrayList<WSNGateway> parents ) throws RemoteException {
		// Creo il gruppo su ogni gateway
		//Iterator<Mote> it = moteList.iterator();
		ArrayList<Mote> ml = new ArrayList<Mote>();
		//Mote amote;
		WSNGateway gwFather = null;
		//Mote mote = null;

		if (moteList.size() > 0) {
			//Cerco tutti i mote di quel gateway
			for( Mote amote : moteList ) {
				if ( gwFather == null ) {
					gwFather = amote.getWSNGatewayParent();
					ml.add(amote);
				} else {
					if ( amote.getWSNGatewayParent().equals(gwFather)) {
						ml.add(amote);
					}
				}
			}
			
			//Li ho trovati tutti, creo il gruppo
			System.out.println("Creo il gruppo " + groupName + " sul gateway " + gwFather.getName());
			gwFather.createMoteGroup(groupName, ml);
			parents.add(gwFather);
			//parents.add(wsnGatewayList.get( wsnGatewayList.indexOf(gwFather)));
			gwFather.getMoteGroup(groupName).addSubscriber(this);

			Iterator<Mote> iml = ml.iterator();
			while(iml.hasNext()) {
				moteList.remove(iml.next());
			}
			//Ricerco di nuovo gli altri gateway
			return createMoteGroupOnNextGateway(groupName, moteList, parents);
		} else
			return parents;
				
		
		
	}

	/**
	 * This method is used to create a new MoteGroup
	 * 
	 * @param hostName
	 *            represent the ip address of the client
	 * @param port
	 *            represent the port number of the client application
	 * @param groupName
	 *            represent the name of the group that the user wants to create
	 * @param moteList
	 *            represent the list of the motes for the new created group
	 * @param functions
	 *            list of the functions associated to a specific group
	 * @return result indicates if the group is created or not
	 * @throws IOException,
	 *             RemoteException
	 */
//	public boolean createMoteGroup(/*String client_name,*/ String hostName, int port, String groupName,
//			ArrayList<Mote> moteList, FunctionList function)
//			throws IOException, RemoteException {
//		boolean result = false;
//		ArrayList<WSNGateway> parents = new ArrayList<WSNGateway>();
//		String nameGW;
//		Mote mote = null;
//		WSNGateway gwFather = null;
//		String saveDir = temp_dir;
//
//		synchronized (this.memoryLock) {
//			ServerClassLoader loader = new ServerClassLoader(/*client_name,*/ hostName, port);
//
//			try {
//				loader.connect();// connect with the client
//
//			} catch (UnknownHostException e1) {
//				e1.printStackTrace();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//
//			System.out.println("Download in progress...\n");
//			ObjectInputStream oin = new ObjectInputStream(loader.getSocket()
//					.getInputStream());
//
//			for (ObjectSearched obj : function.getList()) {
//				String nameClass = obj.getName();
//
//				try {
//					loader.downloadClassFromClient(oin, nameClass, saveDir);// invoke
//																			// the
//																			// method
//																			// for
//																			// downloading
//																			// files
//				} catch (SocketException e) {
//					e.printStackTrace();
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//			oin.close();
//			loader.disconnect();
//		}
//
//		// prepare itself to send files to all the gateway which has one ore
//		// more mote of the group
//		synchronized (lockMoteGroupList) {
//			System.out.println("Creating group '"+groupName + "'");
//			if (!this.moteGroupList.containsKey(groupName)) {
//				for (int i = 0; i < moteList.size(); i++) {
//					boolean found = false;
//					ArrayList<Mote> ml = new ArrayList<Mote>();
//					mote = moteList.get(i);
//					ml.add(mote);
//					nameGW = mote.getWSNGAtewayParent();
//					gwFather = this.getWSNGateway(nameGW);
//
//					// verify if there is already the parent of this gateway in
//					// the hashtable of the mote group
//					for (WSNGateway wsn : parents) {
//						if (wsn.getName().equals(nameGW))
//							found = true;
//					}
//
//					// if there isn't add it, create group on that gateway and
//					// send files to it
//					if (!found) {
//						ClassManager cm = new ClassManager(this.ipAddress,
//								6565, gwFather, temp_dir);
//						cm.startExecution(function, groupName, ml);
//						parents.add(gwFather);
//					}
//
//					else // add mote to the existent group
//					{
//						gwFather.addMoteToGroup(groupName, mote, null);
//					}
//
//				}
//
//				moteGroupList.put(groupName, parents);
//				result = true;
//				System.out.println("Created!");
//				
//			}
//
//			// remove files of the function group in the temp directory
//			for (ObjectSearched obj : function.getList()) {
//				String nameClass = obj.getName();
//				String command = "rm " + temp_dir + nameClass + ".class";
//				Runtime run = Runtime.getRuntime();
//
//				try {
//					run.exec(command);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//		}
//
//		return result;
//
//	}

	/**
	 * This method is used to delete a moteGroup
	 * 
	 * @param groupName
	 *            represent the name of the group that the user wants to delete
	 * @return result indicates if the group is deleted or not
	 * @throws RemoteException
	 */
	public boolean removeMoteGroup(String groupName) throws RemoteException {
		boolean result = false;
		synchronized (lockMoteGroupList)
		{
			if (moteGroupList.containsKey(groupName))
				
			{
				ArrayList<WSNGateway> gwlist = moteGroupList.get(groupName);
				
				
				for (WSNGateway gw : gwlist)
					gw.removeMoteGroup(groupName);
			 
				moteGroupList.remove(groupName);
				result = true;
				System.out.println("Group '"+ groupName + "' deleted!");
			}
		}
		return result;

	}

	/**
	 * This method is used to add a mote to an existent group
	 * 
	 * @param gpName
	 *            represent the name of the group
	 * @param mote
	 *            is the mote to add
	 * @return result indicates if the Gateway is added/it is already on the
	 *         list (true) or not and it is not added (false)
	 * @throws RemoteException
	 */
	public boolean addMoteToGroup(String gpName, Mote mote)
			throws RemoteException {
		boolean result = false;
		String gwold = null;

		synchronized (lockMoteGroupList) {
			// If there is the name of gateway of the mote in the list, there is
			// also an object
			// MoteGroup on gateway for this group

			if (moteGroupList.containsKey(gpName)) {

				if (mote.getId().charAt(mote.getId().length() - 1) == 'M')
					gwold = this.searchingMoteInNetwork(mote.getId());

				ArrayList<WSNGateway> listGroup = moteGroupList.get(gpName);
				
				// search the gateway that contain the specific mote and add it
				// on the object MoteGroup
				for (WSNGateway gw : listGroup)
				{
					//	if(gw.getMoteList().contains(mote)) {motePresent = true; result = false;}

					if ( gw.getMoteGroup(gpName) != null )
					if ( gw.getMoteGroup(gpName).getList() != null)
					if(!gw.getMoteGroup(gpName).getList().contains(mote))
					if (gw.equals(mote.getWSNGatewayParent())) 
					{
						
						System.out.println(gw.getName());
						System.out.println( gw.getMoteGroup(gpName).getListNumber() );
						
						//TODO Sistemare
						gw.getMoteGroup(gpName).add( mote /*, gwold*/);
						System.out.println( gw.getMoteGroup(gpName).getListNumber() );

						result = true;
						System.out.println("Added "+mote.getId() + " to group '" + gpName + "'");
						break;
					}
				
				}
			//	 if there is not the name of the gateway wee need to create a
				//new object MoteGroup
				// on the Gateway
		/*		if (result != true) {
					ArrayList<Mote> moteList = new ArrayList<Mote>();
					WSNGateway gw = this.getWSNGateway(mote.getWSNGAtewayParent());
					listGroup.add(gw);

					WSNGateway gwSender = listGroup.get(0);
					MoteGroup mgroup = gwSender.getMoteGroup(gpName);
					moteList.add(mote);

					if (mgroup.getFunctions().getList().size() > 0) {
						ArrayList<String> toSend = new ArrayList<String>();
						MoteGroup mg = gwSender.getMoteGroup(gpName);
						gwSender.sendMoteGroupClass(gpName, gw.getName());

						try {
							gw.createMoteGroup(gwSender.getIpAddress(), 7575,
									gpName, moteList, mg.getFunctions(), gwold);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else
						gw.createMoteGroup(gpName, moteList);

					result = true;
					
				}*/
			}
		}
		return result;
	}

	/**
	 * This method is used to remove a Mote from a group
	 * 
	 * @param groupName
	 *            represent the name of the group
	 * @param mote
	 *            is the mote to remove
	 * @return result indicates if the Mote is added/it is already on the list
	 *         (true) or not and it is not added (false)
	 * @throws RemoteException
	 */
	public boolean removeMoteFromGroup(String groupName, Mote mote)
			throws RemoteException {
		boolean result = false;
		
		synchronized (lockMoteGroupList) {
			
			if (moteGroupList.containsKey(groupName))
				
			{
				ArrayList<WSNGateway> listGroup = moteGroupList.get(groupName);
				// search the gateway that contain the specific mote and add it
				// on the object MoteGroup
				
				
				for (int i = 0; i < listGroup.size(); i++)
				{
					WSNGateway gw = listGroup.get(i);
				
					if(gw.getMoteGroup(groupName).getList().contains(mote))
						if (gw.equals(mote.getWSNGatewayParent())) 
						{
							gw.getMoteGroup(groupName).remove(mote);
							//Questo adesso viene fatto automaticamente
							/*if ( gw.getMoteGroup(groupName).getListNumber() == 0 ) 
							{
								listGroup.remove(i);
	
								if (listGroup.size() == 0) 
								{
									moteGroupList.remove(groupName);
								}
							}*/
							result = true;
							
							System.out.println(mote.getId() + " deleted from group '"+groupName + "'");
							
							break;
						}
				}

			}

		}
		return result;
	}

	/**
	 * This method is used to get a list of Mote that belong to a specific group
	 * 
	 * @param groupName
	 *            represent the name of the group
	 * @return an arrayList that contains the Mote belong to a specific group
	 * @throws RemoteException
	 */
	public ArrayList<Mote> getMoteGroup(String groupName)
			throws RemoteException {
		ArrayList<Mote> moteList = new ArrayList<Mote>();
		ArrayList<WSNGateway> groupList = new ArrayList<WSNGateway>();
		synchronized (this.lockMoteGroupList) {
			if (moteGroupList.get(groupName) != null) {
				groupList.addAll(moteGroupList.get(groupName));
				MoteGroup mg = null;
				// For each gateway on the list the GatewayManager obtain the
				// list of the group searched attached on that Gateway
				for (WSNGateway gw : groupList) {
					mg = gw.getMoteGroup(groupName);
					if ( mg != null ) {
						ArrayList<Mote> list = mg.getList();
						if ( list != null )
							for (Mote mote : list ) {
								moteList.add(mote);
							}
					}
				}
			}
		}
		return moteList;
	}

	/**
	 * This method allows the user to invoke a specific method for a group a
	 * motes using a single invocation
	 * 
	 * @param nameClass
	 *            name of the class which has the method
	 * @param method
	 *            is the name of the method to use for the group
	 * @param groupName
	 *            is the name of the group
	 * @param parameterTypes
	 *            are the types of the input parameters
	 * @param paramIn
	 *            are the input parameters for the method (null if it hasn't
	 *            parameters)
	 * @return the result of the method desired used on each mote of the group
	 * @throws RemoteException
	 */
	public ArrayList<Object> useGroupMoteMethod(String nameClass,
			String method, String groupName, Class[] parameterTypes,
			Object[] paramIn) throws RemoteException {
		ArrayList<Object> result = new ArrayList<Object>();
		synchronized (this.lockMoteGroupList) {
			ArrayList<WSNGateway> groupList = moteGroupList.get(groupName);
			MoteGroup mg = null;
			// For each gateway on the list the GatewayManager obtain the list
			// of the group searched attached on that Gateway
			for (WSNGateway gw : groupList) {
				mg = gw.getMoteGroup(groupName);
				
				//TODO Sistemare
//				result.addAll(mg.useGroupMethod(nameClass, method,
//						parameterTypes, paramIn));

			}
		}
		return result;

	}

	/**
	 * Search the previous gateway to which a mobile mote was connected
	 * 
	 * @param id
	 *            of the mote to search
	 * @return the name of the gateway which has the mote connected
	 * @throws RemoteException
	 */
	public String searchingMoteInNetwork(String id) throws RemoteException {
		ArrayList<WSNGateway> gwList = this.getWSNGatewayList();
		boolean foundMote = false;
		String gwParent = null;

		for (WSNGateway gw : gwList) // search in the list of gateways, which
										// of them has this mote
		{
			ArrayList<Mote> moteList = gw.getMoteList();
			for (Mote searchedMote : moteList) // verify trough the list of
												// mote if there is one with the
												// same id
			{
				if (searchedMote.getId().equals(id)
						&& !searchedMote.isReachable()) // the mote is present
														// in the list of this
														// gateway
					gwParent = gw.getName();

			}// end for(Mote searchedMote : moteList)

		}// end for(WSNGateway gw : gwList)

		return gwParent;
	}

	/**
	 * This method is used to verify if the Motes Groups list is empty  
	 * 
	 * @return boolean 
	 *            to specify if the motes list is empty or not
	 * @throws RemoteException
	 */
	public boolean isMoteGroupsListEmpty()
			throws RemoteException {
		boolean empty=true;
		synchronized (this.lockMoteGroupList) {
			if(moteGroupList.size()!=0)
				empty=false;
		}
		return empty;
	}
	
	
	/**
	 * This method is used to get the names of the groups of motes   
	 * 
	 * @return ArrayList<String> 
	 *            to specify the names of the groups
	 * @throws RemoteException
	 */
	public ArrayList<String> getMoteGroupsNames()throws RemoteException{
		
		ArrayList<String> groupsName=new ArrayList<String>();
		 
		Enumeration e = moteGroupList.keys();
		while( e.hasMoreElements() ){
			groupsName.add(e.nextElement().toString());
		}	
		return groupsName;
	}
	
	
	// METHODS FOR MANAGE SENSOR GROUP

	/**
	 * This method is used to create a new SensorGroup
	 * 
	 * @param groupName
	 *            represent the name of the group that the user wants to create
	 * @param sensorList
	 *            represent a list that contains the sensor to add on the new
	 *            created group
	 * @return result indicates if the group is created or not
	 * @throws RemoteException
	 */
	public boolean createSensorGroup(String groupName,
			ArrayList<Sensor> sensorList) throws RemoteException {
		boolean result = false;
		ArrayList<WSNGateway> parents = new ArrayList<WSNGateway>();
		String nameGW;
		Sensor sensor = null;
		WSNGateway gwFather = null;

		synchronized (lockSensorGroupList) {
			if (!this.sensorGroupList.containsKey(groupName)) {
				for (int i = 0; i < sensorList.size(); i++) {
					boolean found = false;
					ArrayList<Sensor> sl = new ArrayList<Sensor>();
					sensor = sensorList.get(i);
					sl.add(sensor);
					gwFather = sensorList.get(i).getOwnerMote().getWSNGatewayParent();
					//gwFather = this.getWSNGateway(nameGW);

					/*for (WSNGateway wsn : parents) {
						if (wsn.getName().equals(nameGW))
							found = true;
					}*/

					if ( gwFather != null ) {
						gwFather.createSensorGroup(groupName, sl);
						parents.add(gwFather);
					} else {
						gwFather.getSensorGroup(groupName).add(sensor);
					}
				}

				sensorGroupList.put(groupName, parents);

				result = true;
			}
		}
		return result;

	}

	/**
	 * This method is used to delete a sensorGroup
	 * 
	 * @param groupName
	 *            represent the name of the group that the user wants to delete
	 * @return result indicates if the group is deleted or not
	 * @throws RemoteException
	 */
	public boolean removeSensorGroup(String groupName) throws RemoteException {
		boolean result = false;
		synchronized (lockSensorGroupList) {
			if (sensorGroupList.containsKey(groupName))
				
			{
				ArrayList<WSNGateway> gwlist = sensorGroupList.get(groupName);

				for (WSNGateway gw : gwlist)
					gw.removeSensorGroup(groupName);

				sensorGroupList.remove(groupName);

				result = true;
			}
		}
		return result;
	}

	/**
	 * This method is used to add a sensor to an existent group
	 * 
	 * @param groupName
	 *            represent the name of the group
	 * @param sensor
	 *            is the sensor to add
	 * @return result indicates if the Gateway is added/it is already on the
	 *         list (true) or not and it is not added (false)
	 * @throws RemoteException
	 */
	public boolean addSensorToGroup(String groupName, Sensor sensor)
			throws RemoteException {
		boolean result = false;
		synchronized (lockSensorGroupList) {
			// If there is the name of gateway of the sensor in the list, there
			// is also an object
			// SensorGroup on gateway for this group

			if (sensorGroupList.containsKey(groupName)) {
				ArrayList<WSNGateway> listGroup = sensorGroupList
						.get(groupName);
				// search the gateway that contain the specific sensor and add
				// it on the object MoteGroup
				for (WSNGateway gw : listGroup) {
					if (gw.equals(sensor.getOwnerMote().getWSNGatewayParent())) {
						gw.getSensorGroup(groupName).add(sensor);
						result = true;
						break;
					}
				}
				// if there is not the name of the gateway wee need to create a
				// new object SensorGroup
				// on the Gateway
				if (result != true) {
					WSNGateway gw = sensor.getOwnerMote().getWSNGatewayParent();
					listGroup.add(gw);
					ArrayList<Sensor> sensorList = new ArrayList<Sensor>();
					sensorList.add(sensor);
					gw.createSensorGroup(groupName, sensorList);
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * This method is used to remove a Sensor from a group
	 * 
	 * @param groupName
	 *            represent the name of the group
	 * @param sensor
	 *            is the sensor to remove
	 * @return result indicates if the Sensor is added/it is already on the list
	 *         (true) or not and it is not added (false)
	 * @throws RemoteException
	 */
	public boolean removeSensorFromGroup(String groupName, Sensor sensor)
			throws RemoteException {
		boolean result = false;
		
		synchronized (lockSensorGroupList) {
			if (sensorGroupList.containsKey(groupName))
				
			{
				ArrayList<WSNGateway> listGroup = sensorGroupList
						.get(groupName);
				// search the gateway that contain the specific sensor and add
				// it on the object SensorGroup
				for (int i = 0; i < listGroup.size(); i++) {
					WSNGateway gw = listGroup.get(i);
					if (gw.equals(sensor.getOwnerMote().getWSNGatewayParent())) {
						gw.getSensorGroup(groupName).remove(sensor);
						if ( gw.getSensorGroup(groupName).getListNumber() == 0) {
							listGroup.remove(i);

							if (listGroup.size() == 0) {
								sensorGroupList.remove(groupName);

							}
						}
						result = true;
						break;
					}
				}

			}

		}
		return result;
	}

	/**
	 * This method is used to get a list of Sensor that belong to a specific
	 * group
	 * 
	 * @param groupName
	 *            represent the name of the group
	 * @return an arrayList that contains the Sensor belong to a specific group
	 * @throws RemoteException
	 */
	public ArrayList<Sensor> getSensorGroup(String groupName)
			throws RemoteException {
		ArrayList<Sensor> sensorList = new ArrayList<Sensor>();
		ArrayList<WSNGateway> groupList = new ArrayList<WSNGateway>();

		synchronized (this.lockSensorGroupList) {
			if (sensorGroupList.get(groupName) != null) {
				groupList.addAll(sensorGroupList.get(groupName));
				SensorGroup sg = null;
				// For each gateway on the list the GatewayManager obtain the
				// list of the group searched attached on that Gateway
				for (WSNGateway gw : groupList) {
					sg = gw.getSensorGroup(groupName);
					
					//TODO Sistemare
//					for (Sensor sensor : sg.getSensors()) {
//						sensorList.add(sensor);
//					}
				}
			}
		}
		return sensorList;
	}

	/**
	 * This method allows the user to invoke a specific method for a group a
	 * Sensors using a single invocation
	 * 
	 * @param method
	 *            is the name of the method to use for the group
	 * @param groupName
	 *            is the name of the group
	 * @param parameterTypes
	 *            are the types of the input parameters
	 * @param paramIn
	 *            are the input parameters for the method (null if it hasn't
	 *            parameters)
	 * @return the result of the method desired used on each Sensor of the group
	 * @throws RemoteException
	 */
	public ArrayList<Object> useGroupSensorMethod(String method,
			String groupName, Class[] parameterTypes, Object[] paramIn)
			throws RemoteException {
		ArrayList<Object> result = new ArrayList<Object>();
		synchronized (this.lockSensorGroupList) {
			ArrayList<WSNGateway> groupList = sensorGroupList.get(groupName);
			SensorGroup mg = null;

			// For each gateway on the list the GatewayManager obtain the list
			// of the group searched attached on that Gateway
			for (WSNGateway gw : groupList) {
				mg = gw.getSensorGroup(groupName);
				
				//TODOD SIstemare
//				result.addAll(mg
//						.useGroupMethod(method, parameterTypes, paramIn));
			}
		}
		return result;

	}
	
	void uploadGroups( WSNGateway gateway ) throws RemoteException {
		MoteGroup aGroup;
		ArrayList<MoteGroup> groups = gateway.getMoteGroupList();
		
		Iterator<MoteGroup> it = groups.iterator();
		while(it.hasNext()) {
			aGroup = it.next();
			if( this.getMoteGroupsNames().contains(aGroup.getName())) {
				// TODO Devo aggiornare la lista dei parent ma può
				// esserci casino perché la lista dei wsngateway la aggiorno
				//automaticamente ma quella dei parents no
				if ( !moteGroupList.get(aGroup.getName()).contains(aGroup.getWSNGatewayParent())) {
					ArrayList<WSNGateway> tempParents = new ArrayList<WSNGateway>(1);
					tempParents.add(aGroup.getWSNGatewayParent());
					moteGroupList.put( aGroup.getName(), tempParents );
				}
			} else {
				ArrayList<WSNGateway> tempParents = new ArrayList<WSNGateway>(1);
				tempParents.add(aGroup.getWSNGatewayParent());
				
				moteGroupList.put( aGroup.getName(), tempParents );
			}
				
		}
	}

	public void update(Group pub, Group code) throws RemoteException {
		// TODO Auto-generated method stub
		if ( pub.getListNumber() == 0) {
			System.out.println("Il gruppo è vuoto quindi lo tolgo");
			ArrayList<WSNGateway> myGateway = moteGroupList.get(pub.getName());
			if (myGateway != null)
				myGateway.remove(pub.getWSNGatewayParent());
			
			if (myGateway.isEmpty())
				moteGroupList.remove(pub.getName());
		}
	}

	/*public boolean sendAddMotesToGroup(String groupName) throws RemoteException, MiddlewareException {
		
		synchronized (this.lockSensorGroupList) {
			ArrayList<WSNGateway> gw_list = this.moteGroupList.get(groupName);
			
			if (gw_list.size() == 0)
				return false;
			else
			{
				for (WSNGateway gw : gw_list)
					gw.getMoteGroup(groupName).sendAddMotesToGroup();
			
				return true;
			}
			
		}	
		
	}*/
	
	
}
