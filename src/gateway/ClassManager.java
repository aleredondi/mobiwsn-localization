
package gateway;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import remote_interfaces.WSNGateway;

import common.classes.ServerClassLoader;


/**
 * This class is used from the client to manage the TCP connection gateway-side. It contains methods for connecting and disconnecting the gateway, 
 * for searching if a class has to be loaded or saved on a specific gateway.
 * @author Alessandro Laurucci
 * 
 */
public class ClassManager extends Thread
{
	//private String gw_name = ConfigurationManager.getInstance().getParameter("gateway_name"); //thisGateway name
	
	private int port; //thisGateway port number
	private String ipAddress; //thisGateway ipAddress
	
	
	private ServerSocket server; //Socket thisGateway Side 
	private Socket client; // Socket obtained after the connection with other gateway
	
	private WSNGateway remoteObject; //remote Object used to invoke remote methods
	
	private ArrayList<String> classMoteGroup; //names of group mote classes which gateway has to send to another gateway
	private ArrayList<String> classToSave; //names of classes which user want to save on that gateway
	private ArrayList<String> classToLoad; //names of classes which user want to load 
	private ArrayList<Integer> posToSave; //indicates the index of the folder where the files, that this gateway want to the other gateway, are saved
	private ArrayList<Integer> indexFolder; //indicates the index of the folder where the files, that this gateway want from the other gateway, are saved
	private ArrayList<Integer> posToSaveTemp; //indicates the index of the folder where the files, that this gateway want to send to the other gateway, are saved
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
	 * @param csave list of the class to save
	 * @param index identification of the folder where the files are saved
	 */
	public ClassManager(String ipAddr, int portAddr, WSNGateway rmiObject, String path, ArrayList<String> csave, ArrayList<Integer> index)
	{
    	port=portAddr;
    	ipAddress=ipAddr;
    	remoteObject=rmiObject;
    	dir=path;
    	this.classToLoad=new ArrayList<String>();
    	this.classToSave=new ArrayList<String>();
    	this.classToSave.addAll(csave);
    	
    	this.indexFolder = new ArrayList<Integer>();
    	this.indexFolder.addAll(index);
    	
    	this.posToSave= new ArrayList<Integer>();
    	this.posToSaveTemp=new ArrayList<Integer>();
    	
    	this.classMoteGroup= new ArrayList<String>();
    }
	
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
    	this.classToLoad=new ArrayList<String>();
    	this.classToSave=new ArrayList<String>();
    	this.indexFolder = new ArrayList<Integer>();
    	this.posToSave= new ArrayList<Integer>();
    	this.posToSaveTemp=new ArrayList<Integer>();
    	
    	this.classMoteGroup= new ArrayList<String>();
    }

	
//METHODS
	
	public void sendMoteGroupClass(ArrayList<String> list)
	{
		this.classMoteGroup=list;
		
		try 
		{
			socketCreation();
		} 
		catch (UnknownHostException e1) {e1.printStackTrace();} 
		catch (IOException e1) {e1.printStackTrace();}

		this.start();
	}
	
	
	/**
	 * This method is used to verify the classes that the other gateway has to download or simply load from the thisGateway. 
	 * It invoke a new thread if the necessity to create a TCP connection with Gateway. 
	 * @param loadList contains the name of the classes that the server has to load
	 * @param saveList contains the name of the classes that the server has to download
	 * @param saveTempList contains the name of the classes that the server has to download and are saved as temp files
	 * @param index of the folder where the files are saved
	 * @param posTemp indicates the index of the folder where are the files to send
	 * @throws IOException 
	 */
	public void verifyGatewayClass(ArrayList<String> loadList, ArrayList<String> saveList, ArrayList<String> saveTempList, ArrayList<Integer> index, ArrayList<Integer> posTemp) throws IOException
	{	
		//this boolean is false until it's verified that one or more classes has to be saved or loaded on server		
		indexFolder.addAll(index);
		
		this.posToSaveTemp=posTemp;
		
		boolean toinvoke=this.searchClassOnGateway(loadList, saveList, saveTempList); 
		
		if (toinvoke)//this condition verify if there are some classes to load on the server ArrayList or directly to send to the server
		{
			try 
			{
				socketCreation();
			} 
			catch (UnknownHostException e1) {e1.printStackTrace();} 
			catch (IOException e1) {e1.printStackTrace();}
			
			this.start(); //the thread for the TCP connection start
//TODO:
//			remoteObject.getClassClient(/*gw_name,*/ ipAddress, port, classToLoad, classToSave, posToSave);
//DA SISTEMARE!
		}				
	}

	
	/**
	 * Method used for searching a class on a specific gateway
	 * @param loadList contains the name of the classes that the server has to load
	 * @param saveList contains the name of the classes that the server has to download
	 * @param saveTempList contains the name of the classes that the server has to download and are saved as temp files
	 * @throws RemoteException 
	 */
	private boolean searchClassOnGateway(ArrayList<String> loadList, ArrayList<String> saveList, ArrayList<String> saveTempList) throws RemoteException
	{	
		//this boolean is false until it's verified that one or more classes has to be saved or loaded on server	
		boolean toinvoke=false; 
		String toSave, folder;
		int result[]=new int[2];
					
		
		//verify temp class to load	
		for(String toLoad : loadList)
		{
			result=this.verifyClass(toLoad, dir+"tempFiles/");
		
			if(result[0]==-1)//Class not found on server or founded with an old version, so it has to be loaded on it
			{
				System.out.println("The class "+toLoad+" has to be loaded");
				classToLoad.add(toLoad);
				toinvoke=true; 
			}
			else
			{
				System.out.println("The class "+toLoad+" is already loaded on server ");
			}
		}
		
		//verify class to save
		for(int i=0;i<saveList.size();i++)
		{
			toSave= saveList.get(i);
			folder=saveList.get(i)+indexFolder.get(i);
			result=this.verifyClass(toSave, dir+"classi/"+folder+"/");

			if(result[0]==-1)//Class not found on server or founded with an old version, so it has to be loaded on it
			{
				toinvoke=true; 
				classToSave.add(toSave);
				posToSave.add(result[1]);
				
				System.out.println("Class "+toSave+" has to be saved on server ");				
			}
			else
			{
				System.out.println("The class "+toSave+" is already loaded on server ");
			}
		}
		
		//verify temp class to save
		for(int i=0;i<saveTempList.size();i++)
		{
			toSave= saveTempList.get(i);
			int pos=posToSaveTemp.get(i);
			folder=toSave+pos;
			result=this.verifyClass(toSave, dir+"classi/"+folder+"/");

			if(result[0]==-1)//Class not found on server or founded with an old version, so it has to be loaded on it
			{
				toinvoke=true; 
				classToSave.add(toSave);
				posToSave.add(result[1]);
				
				System.out.println("Class "+toSave+" has to be saved on server ");				
			}
			else
			{
				System.out.println("The class "+toSave+" is already loaded on server ");
			}
		}
		return toinvoke;
	}
	
	
	/**
	 * Method used to verify if some class are present on a gateway and in what position are saved
	 * @param vclass the object class to search
	 * @param path directory where the classes are saved
	 * @return an array that contain two integer value one that represent if the object was found or not(-1 / 0)
	 * the second represent the identifier of a directory, if the object is saved on the gateway machine in a file.class
	 */
	private int[] verifyClass(String vclass, String path)
	{
		int result[]=new int[2];
		String className;
		Class cl=null;
		
		ServerClassLoader scl= new ServerClassLoader();
		try 
		{
			cl=scl.loadClass(vclass, path, true);
		} 
		catch (ClassNotFoundException e1) {e1.printStackTrace();}
		
		className=cl.getName();
			
		//invoke the method searchClass to verify what classes are loaded on server
		try 
		{
			Object element=cl.newInstance();
			result=remoteObject.searchClass((ArrayList<String>)cl.getDeclaredMethod("getClassIdentificationParameter").invoke(element), (Integer)cl.getDeclaredMethod("getVersion").invoke(element));
		} 
		catch(IllegalArgumentException e) {e.printStackTrace();} 
		catch(SecurityException e) {e.printStackTrace();} 
		catch(IllegalAccessException e) {e.printStackTrace();}
		catch(InvocationTargetException e) {e.printStackTrace();} 
		catch(NoSuchMethodException e) {e.printStackTrace();} 
		catch (RemoteException e) {e.printStackTrace();	} 
		catch (InstantiationException e) {e.printStackTrace();}
		
		return result;
	}
	
	
	/**
	 * sendFile method has the aim to send the file.class indicated by the user, 
	 * files that the server will download and save into its hard disk  
	 * @throws IOException
	 */
	public void sendFile(ObjectOutputStream oos, String ObjectToSend, String path) throws IOException 
	{
		FileInputStream fis=null; 
		byte[] packet=null;
		
		fis = new FileInputStream(dir+path+ObjectToSend+".class");
		packet = new byte[ fis.available() ];
		fis.read(packet);
			
		//clean the stream, and write the new Object on it
		oos.reset();
		oos.writeObject(packet);
		oos.flush();
		System.out.println("Object "+ObjectToSend+" sent");
	}
	
	
	/**
	 * This method has the aim to send the files that the server has to save in its hard disk
	 * @throws IOException
	 */
	private void loadObject(String nameClass, String path) throws IOException
	{
		byte[] classBytes= null;
	
		DataOutputStream outStream=null;
		String fileSeparator = System.getProperty( "file.separator" ) ;
		outStream=new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
		
		classBytes = null;
	
		//if the class is in a packageSe substitute the  . with the separator file (es. /)
		nameClass = nameClass.replace('.', fileSeparator.charAt(0));
	     	
		try 
	   	{
      		//load the class as a byte array from the classpath
			classBytes = loadClassFromFile(nameClass, path);
		} 
	   	catch (FileNotFoundException fnfe)
	   	{
			System.out.println("Class not found");
			
			outStream.writeInt(-1);
			outStream.flush();		
		}	      	
		
	   	outStream.writeInt(classBytes.length); //write the dimension of the class
		outStream.write(classBytes, 0, classBytes.length); //send the class
	    outStream.flush();

	}
	
	
	/**
	 * Method used to load an object from a file.class saved on the hardisk in a byte array
	 * @param name represent the name of the file that it wants to send
	 * @return the method return the byte array of the file opened
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private byte[] loadClassFromFile(String name, String path) throws FileNotFoundException, IOException 
	{
		int size = 0;
		InputStream is = null;
		byte  classData[]=null;
    	
		String nameClass = name + ".class";
		
		try 
		{
   		   is = new FileInputStream(path+nameClass);
   		  
   		   if (is == null) throw new FileNotFoundException(nameClass);//file not found
   		  
   		   size = is.available();
   		   classData = new byte[size];//create the array of byte
   		   is.read(classData);//read the file
   		   is.close();
			
		} 
		catch(FileNotFoundException fnfe){ throw fnfe;} 
		catch(IOException ioe){	throw ioe;}
		
		return classData;
	}
	
	
	
//METHODS SOCKET MANAGEMENT	
	
	/**
	 * This method control the if the port number is valid and then create a server socket
	 */
	public void socketCreation() throws UnknownHostException,IOException 
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
		DataInputStream inStream=null;
		String response="not";
		
		if(classToLoad.size()+classToSave.size()+this.classMoteGroup.size()>0)
		{
			try 
			{
				connect();
			} 
			catch (UnknownHostException e) {e.printStackTrace();} 
			catch (IOException e) {e.printStackTrace();}
		}
		
		if(classToLoad.size()>0)
		{
			for(String load: classToLoad)
			{
				try 
				{
					loadObject(load, dir+"tempFiles/");
				} 
				catch (IOException e1) {e1.printStackTrace();}
			}
			System.out.println("\nLoading finished");
			try 
			{
				inStream=new DataInputStream(new BufferedInputStream(client.getInputStream()));
				response=inStream.readUTF();
			} 
			catch (IOException e1) {e1.printStackTrace();}
		
			if(response.equals("ok"))
				System.out.println("riceved");
		}
		
		if(classToSave.size()+classMoteGroup.size()>0)
		{
			int j=0;
			ObjectOutputStream oos=null; //OBject use to send files
			
			try 
			{
				oos = new ObjectOutputStream(client.getOutputStream());
			} 
			catch (IOException e1) {e1.printStackTrace();} 
		
			String path;
			
			for(int i=0;i<indexFolder.size();i++)//send save file
			{		
				String save=classToSave.get(j);
				try 
				{
					path="classi/"+save+indexFolder.get(i)+"/";
					sendFile(oos, save, path);
				} 
				catch (IOException e) {e.printStackTrace();}
				j++;
			}
			
			for(int i=0;i<this.posToSaveTemp.size();i++) //send save temp file
			{		
				String save=classToSave.get(j);
				try 
				{
					path="classi/"+save+posToSaveTemp.get(i)+"/";
					sendFile(oos, save, path);
				} 
				catch (IOException e) {e.printStackTrace();}
				j++;
			}
			
			for(int i=0;i<this.classMoteGroup.size();i++) //send save temp file
			{
				String save=classMoteGroup.get(i);
				
				try 
				{
					sendFile(oos, save, "");
				} 
				catch (IOException e) {e.printStackTrace();}
				
			}

			try 
			{
				oos.close();
			} 
			catch (IOException e) {e.printStackTrace();} //close the ObjectOutputStream
		}
		
		
		if(classToLoad.size()+classToSave.size()+this.classMoteGroup.size()>0)
		{
			disconnect();
		}
	}
}		