
package gateway_manager;


import remote_interfaces.WSNGateway;
import remote_interfaces.mote.Mote;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import common.classes.*;


/**
 * This class is used from the client to manage the TCP connection gateway-side. It contains methods for connecting and disconnecting the gateway, 
 * for searching if a class has to be loaded or saved on a specific gateway.
 * @author Alessandro Laurucci
 * 
 */
public class ClassManager extends Thread
{
	private int port; //thisGateway port number
	private String ipAddress; //thisGateway ipAddress
	
	
	private ServerSocket server; //Socket thisGateway Side 
	private Socket client; // Socket obtained after the connection with other gateway
	
	private WSNGateway remoteObject; //remote Object used to invoke remote methods
	
	private ArrayList<String> classToSave; //names of classes which user want to save on that gateway

	private String dir; //path where the files to send are saved

	
	
//CONSTRUCTORS
		
	/**
	 * Class constructor:
	 * <br>
	 * Used if there are one ore more files to save
	 * @param ipAddress this variable indicate the client ip address
	 * @param port this variable indicate the client port of listening
	 * @param rmiObject this variable indicate the remote Object (WSNGateway) created by the thisGateway and used to invoke remote methods
	 * @param path directory where the files to send are saved
	 */
	public ClassManager(String ipAddr, int portAddr, WSNGateway rmiObject, String path)
	{
    	port=portAddr;
    	ipAddress=ipAddr;
    	remoteObject=rmiObject;
    	dir=path;
    	this.classToSave=new ArrayList<String>();
		
	}
	
	
	/**
	 * Method used to crate and manage a new mote group
	 * Used if there are one ore more files to save
	 * @param csave list of the class to save
	 * @param groupName name of the group
	 * @param ml list of the mote to add to the group
	 */
	public void startExecution(FunctionList csave, String groupName, ArrayList<Mote> ml)
	{
/* 
 * ### DA VERIFICARE
 * 
    	for(ObjectSearched obj : csave.getList())
    	{
    		this.classToSave.add(obj.getName());
    	}
    	
    	try 
    	{
			this.socketCreation();
		} 
    	catch (UnknownHostException e) {e.printStackTrace();} 
    	catch (IOException e) {e.printStackTrace();}
    
    	this.start();
    	try 
    	{
			remoteObject.createMoteGroup(this.ipAddress,6565, groupName, ml, csave, null);
		} 
    	catch (RemoteException e) {e.printStackTrace();} 
    	catch (IOException e) {e.printStackTrace();}
*/
		}
	
//METHODS

	/**
	 * sendFile method has the aim to send the file.class indicated by the user, 
	 * files that the server will download and save into its hard disk  
	 * @throws IOException
	 */
	private void sendFile(ObjectOutputStream oos, String ObjectToSend) throws IOException 
	{
		FileInputStream fis=null; 
		byte[] packet=null;
		
		fis = new FileInputStream(dir+ObjectToSend+".class");
		packet = new byte[ fis.available() ];
		fis.read(packet);
			
		//clean the stream, and write the new Object on it
		oos.reset();
		oos.writeObject(packet);
		oos.flush();
		System.out.println("Object "+ObjectToSend+" sent");
	}	
	
	
//METHODS SOCKET MANAGEMENT	
	
	/**
	 * This method control the if the port number is valid and then create a server socket
	 */
	private void socketCreation() throws UnknownHostException,IOException 
	{
		if ((port > 0x0) && (port <= 0xFFFF))
		{
			System.out.println("\nServer socket creaton");
			server = new ServerSocket(port); 
		}
	}	
	
	
	/**
	 * This method wait for the client connection and for opening the TCP communication
	 */
	private void connect() throws UnknownHostException,IOException 
	{
		try 
		{
			System.out.println("Waiting server connection...");
			client = server.accept();
			System.out.println("The TCP connection with the server is done\n");
		}
		catch (IOException ioe) {System.out.println("Connection failed " + ioe.getMessage());}
	}
	
	
	/**
	 * This method close the socket opened for the TCP communication
	 */
	private void disconnect()
	{
		try 
		{
			client.close();
			server.close();
		} 
		catch (IOException e) {e.printStackTrace();}
		
		System.out.println("\nsocket closed\n");
	}

	
//THREAD METHOD
	
	public void run()
	{
		
		try 
		{
			connect();
		} 
		catch (UnknownHostException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		ObjectOutputStream oos=null; //OBject use to send files
			
		try 
		{
			oos = new ObjectOutputStream(client.getOutputStream());
		} 
		catch (IOException e1) {e1.printStackTrace();} 
		
		for(String name : classToSave)//send save file
		{		
			try 
			{
				sendFile(oos, name);
			} 
			catch (IOException e) {e.printStackTrace();}
		}
		
		try 
		{
			oos.close();
		} 
		catch (IOException e) {e.printStackTrace();} //close the ObjectOutputStream
		
		disconnect();
		
	}
}		