
package client_applications.parking;


import remote_interfaces.*;
import remote_interfaces.mote.Mote;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import common.classes.*;


/**
 * This class is used from the client to manage the TCP connection client-side. It contains methods for connecting and disconnecting the client, 
 * for searching if a class has to be loaded or saved on server.
 * @author Alessandro Laurucci
 * 
 */
public class ClassManager extends Thread
{
	public String output;
	
	//private String client_name = "parking"; //Client name
	
	private int port; //Client port number
	private String ipAddress; //Client ipAddress
	
	private ServerSocket server; //Socket client Side 
	private Socket client; // Socket obtained after the connection with server
	
	private WSNGateway remoteObject; //remote Object used to invoke remote methods
	private WSNGatewayManager remoteManager; //remote manager Object used to invoke remote methods
	
	private boolean forGroup;
	
	private ArrayList<String> classToSave; //names of classes which user want to save on server
	private ArrayList<String> classToLoad; //names of classes which user want to load
	private ArrayList<String> loadTemp; //name of classes (load) to save as the temp file on the gateway
	private ArrayList<String> saveTemp; //name of classes (save) to save as the temp file on the gateway
	 
	private ArrayList<Integer> posToSave; //indicates the index of the folder where the files, that the client want to send to server, are saved
	private ArrayList<Integer> posToSaveTemp; //indicates the index of the folder where the files, that this gateway has but the client want to send to the other gateway of the group
	private String dir; //path where the files to send are saved
		
//CONSTRUCTORS
	
	/**
	 * Class constructor:
	 * <br>
	 * Used if there are not files to save into server 
	 * @param ipAddress this variable indicate the client ip address
	 * @param port this variable indicate the client port of listening
	 * @param rmiObject this variable indicate the remote Object (WSNGateway) created by the Client and used to invoke remote methods
	 */
	public ClassManager(String ipAddr, int portAddr, WSNGateway rmiObject)
	{
    	port=portAddr;
    	ipAddress=ipAddr;
    	remoteObject=rmiObject;
    }
	
	/**
	 * Class constructor:
	 * <br>
	 * Used if there are not files to save into server 
	 * @param ipAddress this variable indicate the client ip address
	 * @param port this variable indicate the client port of listening
	 * @param rmiObject this variable indicate the remote Object (WSNGatewaymanager) created by the Client and used to invoke remote methods
	 * @param path directory where the files to send are saved
	 */
	public ClassManager(String ipAddr, int portAddr, WSNGatewayManager rmiObject, String path)
	{
    	port=portAddr;
    	ipAddress=ipAddr;
    	this.remoteManager=rmiObject;
    	dir=path; 
    }

	/**
	 * Class constructor:
	 * <br>
	 * Used if there are one ore more files to save into server 
	 * @param ipAddress this variable indicate the client ip address
	 * @param port this variable indicate the client port of listening
	 * @param rmiObject this variable indicate the remote Object (SampleServer) created by the Client and used to invoke remote methods
	 * @param path directory where the files to send are saved
	 */
	public ClassManager(String ipAddr, int portAddr, WSNGateway rmiObject, String path)
	{
    	port=portAddr;
    	ipAddress=ipAddr;
    	remoteObject=rmiObject;
    	dir=path;    	
    }

	
//METHODS
	
	/**
	 * This method is used to verify the classes that the server has to download or simply load from the client. 
	 * It invoke a new thread if the necessity to create a TCP connection with Gateway. 
	 * @param load contains the information about the classes that the server has to load
	 * @param save contains the information about the classes that the server has to download
	 * @throws IOException 
	 */
	public void verifyGatewayClass(FunctionList load, FunctionList save) throws IOException
	{	
		this.forGroup=false;
		boolean toinvoke=this.searchClassOnGateway(load, save);
		
		if (toinvoke)//this condition verify if there are some classes to load on the server ArrayList or directly to send to the server
		{
			try 
			{
				socketCreation();
			} 
			catch (UnknownHostException e1) {output=output+e1.toString();} 
			catch (IOException e1) {output=output+e1.toString();}
			
			this.start(); //the thread for the TCP connection start
			remoteObject.getClassClient(/*client_name,*/ ipAddress, port, classToLoad, classToSave, posToSave);
		}
	}
	
	
	/**
	 * This method is used to verify the classes that the server has to download or simply load from the client, all the classes will be passed in a file
	 * this allows the gateway to load these objects also in the other gateway of a specific group
	 * It invoke a new thread if the necessity to create a TCP connection with Gateway. 
	 * @param load contains the information about the classes that the server has to load
	 * @param save contains the information about the classes that the server has to download
	 * @throws IOException 
	 */
	public void verifyGroupGatewayClass(String group, FunctionList load, FunctionList save) throws IOException
	{	
		this.forGroup=true;
		this.searchClassOnGateway(load, save);
		
		if(this.classToSave.size()+this.classToLoad.size()+this.loadTemp.size()>0)
		{
			try 
			{
				socketCreation();
			} 
			catch (UnknownHostException e1) {output=output+e1.toString();} 
			catch (IOException e1) {output=output+e1.toString();}
		}
		this.start(); //the thread for the TCP connection start
		remoteObject.getGroupClass(/*client_name,*/ ipAddress, port, group, classToLoad, classToSave, posToSave, loadTemp, saveTemp, posToSaveTemp);
		
	}


	/**
	 * This method manage the creation of a group of motes that are connected with different gateway, the client has to send a group of files
	 * that represent the list of the object to create for each mote
	 * @param groupName name of the group
	 * @param moteList list of the mote
	 * @param functions information of the functions to send
	 * @throws IOException
	 */
	public void manageMoteGroup(String groupName, ArrayList<Mote> moteList, FunctionList functions) throws IOException
	{
		//initialize the array with the same dimension of the class array that we have as input parameter
		loadTemp= new ArrayList<String>();
		saveTemp= new ArrayList<String>();
		classToLoad= new ArrayList<String>();
		classToSave= new ArrayList<String>();
		posToSave= new ArrayList<Integer>();
		posToSaveTemp=new ArrayList<Integer>();
		
		try 
		{
			socketCreation();
		} 
		catch (UnknownHostException e1) {output=output+e1.toString();} 
		catch (IOException e1) {output=output+e1.toString();}
		
		for(ObjectSearched obj : functions.getList())
		{
			output=output+obj.getName();
			this.classToSave.add(obj.getName());
		}
		
		this.start();	
		
		try 
		{
			this.remoteManager.createMoteGroup(/*this.client_name,*/ this.ipAddress, this.port,groupName, moteList, functions);
		} 
		catch (RemoteException e) {output=output+e.toString();}
	}
	
	
	/**
	 * 
	 * @param load list of the class to load
	 * @param save list of the class to save
	 * @return a boolean that indicate if it's necessary a TCP connection with gateway or not
	 * @throws RemoteException
	 */
	private boolean searchClassOnGateway(FunctionList loadList, FunctionList saveList) throws RemoteException
	{	
		//this boolean is false until it's verified that one or more classes has to be saved or loaded on server	
		boolean toinvoke=false; 
		
		int result[]=new int[2];
		String className;
		
		//initialize the array with the same dimension of the class array that we have as input parameter
		loadTemp= new ArrayList<String>();
		saveTemp= new ArrayList<String>();
		classToLoad= new ArrayList<String>();
		classToSave= new ArrayList<String>();
		posToSave= new ArrayList<Integer>();
		posToSaveTemp=new ArrayList<Integer>();
		
		//verify class to load	
		for(ObjectSearched toLoad : loadList.getList())
		{
			className=toLoad.getName();
			this.loadTemp.add(className);
			
			try 
			{
				result=this.verifyClass(toLoad.getClassIdentificationParameter(), toLoad.getVersion());
			} 
			catch (IllegalArgumentException e) {output=output+e.toString();}
			catch (SecurityException e) { output=output+e.toString();} 
			
			if(result[0]==-1)//Class not found on server or founded with an old version, so it has to be loaded on it
			{
				output=output+"The class "+className+" has to be loaded\n";
				classToLoad.add(className);
				toinvoke=true; 
			}
			else
				output=output+"The class "+className+" is already loaded on server\n";
			
		}
		
		//verify class to save

		for(ObjectSearched toSave : saveList.getList())
		{
			className=toSave.getName();
			try
			{
				result=this.verifyClass(toSave.getClassIdentificationParameter(), toSave.getVersion());
			} 
			catch (IllegalArgumentException e) {output=output+e.toString();}
			catch (SecurityException e) { output=output+e.toString();} 
			
			if(result[0]==-1)//Class not found on server or founded with an old version, so it has to be loaded on it
			{
				toinvoke=true; 
				classToSave.add(className);
				posToSave.add(result[1]);
				
				output=output+"Class "+className+" has to be saved on server\n";				
			}
			else
			{
				output=output+"The class "+className+" is already saved on server\n";
				this.saveTemp.add(className);
				this.posToSaveTemp.add(result[1]);
			}
		}
		
		return toinvoke;
	}

	
	public boolean classGroupSearching(String group, FunctionList loadList, FunctionList saveList)
	{
		int result[]=new int[2];
		
		boolean done=false;
		boolean toinvoke=false;
		
		ArrayList<ObjectSearched> classToLoad= new ArrayList<ObjectSearched>();
        ArrayList<ObjectSearched> classToSave= new ArrayList<ObjectSearched>();
        posToSave= new ArrayList<Integer>();
        String className=null;
        
		for(ObjectSearched objLoad : loadList.getList())
		{
			className=objLoad.getName();
			result=this.verifyClass(objLoad.getClassIdentificationParameter(), objLoad.getVersion());
			
			if(result[0]==-1)//Class not found on server or founded with an old version, so it has to be loaded on it
			{
				toinvoke=true; 
				classToLoad.add(objLoad);
				
				output=output+"Class "+className+" has to be loaded on server\n";				
			}
			else
			{
				output=output+"The class "+className+" is already loaded on server\n";
			}

		}
		
		
		for(ObjectSearched objSave : saveList.getList())
		{
			className=objSave.getName();
			result=this.verifyClass(objSave.getClassIdentificationParameter(), objSave.getVersion());
			
			if(result[0]==-1)//Class not found on server or founded with an old version, so it has to be loaded on it
			{
				toinvoke=true; 
				classToSave.add(objSave);
				posToSave.add(result[1]);
				
				output=output+"Class "+className+" has to be saved on server \n";				
			}
			else
			{
				output=output+"The class "+className+" is already loaded on server\n";
			}

		}
		
		if(toinvoke)
		{	
			try 
			{
				done=remoteObject.searchGroupFunction(group, classToLoad, classToSave, posToSave);
			} 
			catch (UnknownHostException e) {output=output+e.toString();} 
			catch (RemoteException e) {output=output+e.toString();} 
			catch (IOException e) {output=output+e.toString();} 
			catch (ClassNotFoundException e) {output=output+e.toString();}
		}
		
		return done;
		
	}
	
	/**
	 * This class is used to invoke directly the remote function searchClass on a specific Gateway 
	 * @param vclass the object class to search
	 * @return an array that contain two integer value, one that represent if the object was found or not( -1 / 0)
	 * the second represent the identifier of a directory, if the object is saved on the gateway machine in a file.class
	 */
	private int[] verifyClass(ArrayList<String> param, int version) 
	{
		int result[]=new int[2];
		//invoke the method searchClass to verify what classes are loaded on server
		try 
		{
			result=remoteObject.searchClass(param ,version);
		} 
		catch(IllegalArgumentException e) {output=output+e.toString();} 
		catch(SecurityException e) {output=output+e.toString();} 
		catch (RemoteException e) {output=output+e.toString();} 
		
		return result;
	}	
	
	
	/**
	 * sendFile method has the aim to send the file.class indicated by the user, 
	 * files that the server will download and save into its hardisk  
	 * @throws IOException
	 */
	private void sendFile(ObjectOutputStream oos,String classToSave) throws IOException 
	{
		FileInputStream fis=null;
		byte[] packet=null;
	
		fis = new FileInputStream(dir+classToSave+".class");
		packet = new byte[ fis.available() ];
		fis.read(packet);
			
		//clean the stream, and write the new Object on it
		oos.reset();
		oos.writeObject(packet);
		oos.flush();
		output=output+"\nObject "+classToSave+" sent\n";
		
	  	
	}
	
	
	/**
	 * This method has the aim to send the files that the server has to save in its hardisk
	 * @throws IOException
	 */
	private void loadObject(String load, String path) throws IOException
	{
		byte[] classBytes= null;
		
		String nameClass;
		
		DataOutputStream outStream=null;
		String fileSeparator = System.getProperty( "file.separator" ) ;
		outStream=new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
		
		classBytes = null;
		
		nameClass = load; 

		//if the class is in a packageSe la classe e' in un package sostituisce il . con il separatore di file (es. /)
		nameClass = nameClass.replace('.', fileSeparator.charAt(0));//ELIMINABILE???
		
      	try 
      	{
   	  		//load the class as a byte array from the classpath
			classBytes = loadClassFromFile(nameClass, path);
		} 
      	catch (FileNotFoundException fnfe)
      	{
			output=output+"Class not found\n";
			
			outStream.writeInt(-1);
			outStream.flush();
			
		}
	      	
		outStream.writeInt(classBytes.length); //write the dimension of the class
	   	outStream.write(classBytes, 0, classBytes.length); //send the class
       	outStream.flush();
	}
	
	/**
	 * 
	 * @param name represent the name of the file that it wants to send
	 * @return the method return the byte array of the file opened
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private byte[] loadClassFromFile(String name, String path) throws FileNotFoundException, IOException 
	{
		int size = 0;
		byte[] classBytes=null;
		InputStream is = null;
		
		String nameClass = name + ".class";
		
		try 
		{
			is=new FileInputStream(path+nameClass);
   		   	if (is == null) throw new FileNotFoundException(nameClass);//file not found
		   
   		   	size = is.available();
   		   	classBytes = new byte[size];//create the array of byte
   		   	is.read(classBytes);//read the file
   		   	is.close();
		} 
		catch(FileNotFoundException fnfe){ throw fnfe;} 
		catch(IOException ioe){	throw ioe;}
		
		return classBytes;
	}
	
	
	
//METHODS SOCKET MANAGEMENT	
	
	/**
	 * This method control the if the port number is valid and then create a server socket
	 */
	private void socketCreation() throws UnknownHostException,IOException 
	{
		if ((port > 0x0) && (port <= 0xFFFF))
		{
			output=output+"\nServer socket create on "+port+"\n";
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
			output=output+"Waiting server connection...\n";
			client = server.accept();
			output=output+"The TCP connection with the server is done\n\n";
		}
		catch (IOException ioe) {output=output+"Connection failed " + ioe.getMessage()+"\n";}
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
		catch (IOException e) {output=output+e.toString();}
		
		output=output+"\nsocket closed\n\n";
	}

	
//THREAD METHOD
	
	public void run()
	{
		String response="not";
		DataInputStream inStream=null;
		
		if(this.classToSave.size()+this.classToLoad.size()+this.loadTemp.size()>0)
		{	
			try 
			{
				connect();
			} 
			catch (UnknownHostException e) {output=output+e.toString();} 
			catch (IOException e) {e.toString();}
		}
		
		if(classToLoad.size()>0)
		{	
			for(String load:classToLoad)
			{
				try 
				{
					loadObject(load,dir);
				} 
				catch (IOException e1) {output=output+e1.toString();}
			}

			output=output+"\nLoading finished\n";

			try 
			{
				inStream=new DataInputStream(new BufferedInputStream(client.getInputStream()));
				response=inStream.readUTF();
			} 
			catch (IOException e1) {output=output+e1.toString();}
		
			if(response.equals("ok"))
				output=output+"riceved\n";
		}
		
		
		if(this.classToSave.size()+this.loadTemp.size()>0)
		{
			ObjectOutputStream oos=null; //OBject use to send files
		
			try 
			{
				oos = new ObjectOutputStream(client.getOutputStream());
			} 
			catch (IOException e1) {output=output+e1.toString();} 
		
			output=output+"\nSending files...\n";
		
			for(String save : classToSave) //send the files to save
			{			
				try 
				{
					sendFile(oos, save);
				} 
				catch (IOException e) {output=output+e.toString();}
			}

			if(this.forGroup) //it was invoked the group load function so send the temp object
			{
				for(String loadSave : loadTemp)
				{		
					try 
					{
						sendFile(oos, loadSave);
					} 
					catch (IOException e) {output=output+e.toString();}
				}
			
			}
		
			try 
			{
				oos.close();
			} 
			catch (IOException e) {output=output+e.toString();} //close the ObjectOutputStream
		}
		
		if(this.classToSave.size()+this.classToLoad.size()+this.loadTemp.size()>0)
		{	
			disconnect();		
		}	
	}
	
}

