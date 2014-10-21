
package common.classes;


import java.io.*;
import java.net.*;


/**
 * This class manage the TCP connection server-side, download the files to save locally, 
 * receive the object to load on the arrayList.
 * @author Alessandro Laurucci
 *
 */
public class ServerClassLoader  extends ClassLoader 
{
	//connection variables
	//private String clientName = null;
	private String hostName = null;
    private int serverPort;
    private Socket socket = null;
    
    //stream variables
    private DataInputStream is = null;
    private DataOutputStream os = null;  
      
    
    
//CONSTRUCTOR METHOD
    /**
     * Default Constructor method 
     */
    public ServerClassLoader() 
    {}
    
    /**
     * Constructor method
     * @param hostName represent the ip address of the client
     * @param serverPort represent the port number where the client application is waiting
     */
    public ServerClassLoader(/*String clientName,*/ String hostName, int serverPort) 
    {
      	super();
      	//this.clientName = clientName;
      	this.hostName = hostName;
      	this.serverPort = serverPort;
    }
    
//METHODS
    
    /**
     * return an Object Socket
     */
    public Socket getSocket()
    {
    	return this.socket;
    }
    
       
    /**
     * This method is used to verify if the class to load is already in the file system in the server, or it has to be received from the client
     * @param nameClass represent the name of the class
     * @param resolve boolean that indicate if the class is linked or it has to be linked
     * @return return the class of the object to load
     */
    public  Class loadClass(String nameClass, boolean resolve) throws  ClassNotFoundException, ClassFormatError 
    {
      	byte[] classBytes = null;
		Class classClass = null;
		
	    try 
	    {
	    	classClass = findSystemClass(nameClass);//search the class into the file system
	    } 
	    catch(ClassNotFoundException cnfe){classClass = null;}
      	
	    
	    if  (classClass == null) //the class is not in the file system, so it has to be searched on the client 
	    {
	    	try 
            {
               	classBytes = loadClassFromClient(nameClass);
            } 
            catch (UnknownHostException uhe){} 
            catch (SocketException se){disconnect();} 
            catch (IOException ioe){disconnect();} 
            catch (ClassNotFoundException cnfe){throw cnfe;}
            
            classClass = defineClass("client_applications.classi_remote."+nameClass, classBytes, 0, classBytes.length);//convert the array bytes into a class
            //classClass = defineClass("client_applications."+clientName+".classi_remote."+nameClass, classBytes, 0, classBytes.length);//convert the array bytes into a class
            
            if (classClass == null) throw new ClassFormatError(nameClass); //verify the correct format of the class 
	    }	
	    
	    if (resolve) 
	    	resolveClass(classClass);
	    
	    return classClass;
    }
  
    
    /**
     * This method is used for sending the name of the class that the client wants to load on server, 
     * so that the server can prepare itself to receive the bytes array of this object
     * @param nameClass represent the name of the class that the server has to loaded
     * @return return the byte array of the loaded object
     * @throws ClassNotFoundException
     * @throws SocketException
     * @throws IOException
     */
    private byte[] loadClassFromClient(String nameClass) throws ClassNotFoundException, SocketException, IOException 
    {
		byte[] classBytes = null;
		int size = 0;
      	// load the class data from the connection
        try 
        {
      
        	size = is.readInt();//read the class dimension
        	if (size == -1) 	throw new ClassNotFoundException(nameClass);
           	
           	//read the dimension of the class and then the array of bytes
           	classBytes = new byte[size];
       		is.read(classBytes);
       	} 
        catch (SocketException se){throw se;} 
        catch (IOException ioe) {throw ioe;}
       	return classBytes;
    }
        
    
    /**
     * This method download the file from the client and save it into the server machine
     * @param oin objectinputstream used to get bytes
     * @param naneFile name of the file to download
     * @param saveDir path where to save the file
     * @return this class return an array of integer in which there are the index of the folder where the object are saved
     * @throws ClassNotFoundException
     * @throws SocketException
     * @throws IOException
     */
    public void downloadClassFromClient(ObjectInputStream oin, String nameFile, String saveDir) throws ClassNotFoundException, SocketException, IOException 
    {
    	String newDir=null;
		
    	// intercept the file sending by the client
    	byte[] packet=null;
    	
		packet = (byte[]) oin.readObject();//get the file
    	newDir=saveDir+nameFile+".class";
    	this.saveFile(nameFile, packet, newDir);

    	System.out.println(" --Downloading finished\n");
    }
    
    
    /**
	 * Method used for searching the first path index not used for a specific classname
	 * @param saveDir name of the root path where search
	 * @param nameFile name of the file to save
	 * @return a string with new path created
	 */
	public String searchDir(String saveDir, String nameFile)
	{
		boolean exist=true;
		
		int j=0;
		
		String newDir="";
		String path="";
		File directory=null;
		
		while (exist)
		{
			path=nameFile+j;
			newDir=saveDir+path+"/";
			directory=new File(newDir); 
			exist = directory.exists();
			j++;
		}
		directory.mkdir(); // create path
		return path;
	}
	
	
    /**
     *  write file on memory
     * @param nameFile name of the new file
     * @param packet byte array of the file
     * @param dir path where save the file
     * @throws IOException
     */
    private void saveFile(String nameFile, byte[] packet, String dir ) throws IOException
    {
    	FileOutputStream fos = null;
		System.out.println(" --receiving file " + nameFile);
		System.out.println(" --Dimension file " + packet.length);
		fos = new FileOutputStream(dir);
		fos.write(packet);//write stream
		fos.close(); //close the stream
    }
    
    
    /**
     * This method is used to load a file.class saved locally on the server 
     * @param nameClass represent the name of the class
     * @param path represent the directory where the object to load is saved
     * @param resolve boolean that indicate if the class is linked or it has to be linked
     * @return return the class of the object to load
     */   
    public synchronized Class loadClass(String className, String path, boolean resolveIt) throws ClassNotFoundException 
    {
    	Class result;
    	byte  classData[]=null;
    	File classFile = null ;
    	FileInputStream fi = null;
    	String nameFile;
    	
    	//try to search the file in the file system
    	try 
    	{
    		result = super.findSystemClass(className);
    		return result;
    	} 
    	catch (ClassNotFoundException e) {}
    	
    	nameFile = className+".class" ;
    	
    	//if the file is not in the file system the server search it in a specific directory
    	try 
    	{
    		//System.out.println("\nnameFile to load is : " + nameFile);
    		classFile = new File(path, nameFile);  
    		fi = new FileInputStream( classFile ) ;
    		classData= new byte[ fi.available() ] ;
    		
    		System.out.println("classData size :" + classData.length );
        
    		fi.read(classData); //read the object
    	}	 
    	catch (IOException e) {e.printStackTrace();}
    	
    	//At this point we try to obtain a bytes array of the object so to load a new one.
    	
    	if (classData == null) //the class is not founded
		throw new ClassNotFoundException();
		
    	System.out.println("className : " + className + "\n");

    	result = defineClass("client_applications.classi_remote."+className,classData, 0, classData.length); //obtain the bytes array
    	//result = defineClass("client_applications."+clientName+".classi_remote."+className,classData, 0, classData.length); //obtain the bytes array
    	
    	if (result == null) 
    		throw new ClassFormatError();
		
    	if (resolveIt) 
    		resolveClass(result);
		
    	return result;
    }
    
        

//METHODS SOCKET MANAGEMENT	    

    /**
     * This method open the streams and the socket connection with the client
     */
    public void connect() throws UnknownHostException, IOException 
    {
    	System.out.println("connect with client");
		try 
    	{
           	System.out.println(hostName+"  "+serverPort);
    		socket = new Socket(hostName, serverPort);
        	is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        	os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

           	System.out.println("\nConnection with "+hostName+" is opened");
           	System.out.println("Stream opened\n");
        } 
    	catch(UnknownHostException uhe){throw uhe;} 
    	catch(IOException ioe){throw ioe;}
    	
    	System.out.println("connected");
    }

    
    /**
     * This method close the streams and the socket connection with the client
     */
    public void advertise()
    {
    	try 
    	{
    		os.writeUTF("ok");
         	os.flush();
      	} 
    	catch(IOException ioe){} 
    	catch (Exception e) {}
    }
    
    
    /**
     * This method close the streams and the socket connection with the client
     */
    public void disconnect()
    {
    	try 
    	{
      		os.close(); os = null;
      		is.close(); is = null;
           	socket.close(); socket = null;
           	System.out.println("\nThe streams are closed");
           	System.out.println("Connection is closed\n");
      	} 
    	catch(IOException ioe){} 
    	catch (Exception e) {}
    }
    
    
}  
