package client_applications.home_virtualization_application;





import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;

import remote_interfaces.WSNGatewayManager;
import remote_interfaces.clients.profiling_system.*;

public class HomeVirtualizationApplicationMain 
{



	public static void main(String[] args) 
	{
		ArrayList<String> roomsNames= new ArrayList<String>();
		ArrayList<Integer> roomsIdentifier=new ArrayList<Integer>();
	    String databaseName="test2";// database name
	    String databaseUserName="root";// database user name
	    String databaseUserPassword="";// database user password
	    String databaseURL="jdbc:mysql://localhost:3306/mysql?";// database URL
		String presenceProfileName="presence profile"; // presence profile type name
		String temperatureProfileName="temperature profile";// temperature profile type name
		String lightProfileName="light profile";// light profile type name
		String realtimePresenceValueName="realtime presence value";// real time presence value type name
		String realtimeTemperatureValueName="realtime temperature value";// real time temperature value type name
		String realtimeLightValueName="realtime light value";// real time light value type name
		Hashtable<String, String > roomsGroupsAssociation=new Hashtable<String,String>();
		HomeVirtualizationApplicationImpl homeVirtualizationApplicationImpl=null;
		String url="//"+ args[0] +"/HVA-" + args[1];
		String Prof_System_url="//"+ args[2] +"/ProfilingSystem-" + args[3]; //get the url of the gateway manager
		ProfilingSystem profilingSystem=null;
		
		try
		{

	        System.setProperty("java.security.policy", "src/myjava.policy");
			
	        /*
	         * impostazione SecurityManager
	         */
	        System.setSecurityManager(new SecurityManager());       
	        Thread.currentThread().setName("HomeVirtualizationApllication_Thread");
	        
			try
			{

				System.out.println("Connection to Profiling System...");
				
				//record of the remote object in the rmiregistry
				System.out.println("Looking for Profiling System at " + Prof_System_url + " ...");
				
				profilingSystem = (ProfilingSystem) Naming.lookup(Prof_System_url);
				
				if (profilingSystem != null){
				
					System.out.println("Lookup completed!");

				}
				else
					System.out.println("No Profiling System found!");
			}
			catch(java.net.MalformedURLException muex){System.out.println(muex.getMessage());}
			catch(java.rmi.RemoteException  rex){System.out.println(rex.getMessage());}
			catch(Exception ex){System.out.println(ex.getMessage());}
			
			
			

			try{
	            
	            Class.forName("com.mysql.jdbc.Driver").newInstance(); 

	            Connection conn = DriverManager.getConnection(databaseURL,databaseUserName,databaseUserPassword ); 
	                                                                                                                 
	            System.out.println("Connection to MySQL server to load the rooms names!");

	            ResultSet rs1;
	            ResultSet rs2;
	            Statement stmt = null;
	            conn.setCatalog(databaseName); 
	            stmt = conn.createStatement();
	            
	            rs1=stmt.executeQuery("SELECT * FROM rooms;");
	            while(rs1.next()){
	            	roomsNames.add(rs1.getString("roomname"));
	            	roomsIdentifier.add(rs1.getInt("idroom"));
	            }

	            rs2=stmt.executeQuery("SELECT * FROM  groupsRoomsTables;");
	            
	            while(rs2.next()) {
	                roomsGroupsAssociation.put(rs2.getString("roomName"),rs2.getString("groupName"));
	            }
	            stmt.close();
	            conn.close(); 
	        }
	        
	        catch (Exception ex) {
	            System.out.println("SQLException: " + ex.getMessage());
	        }	
	        


			/*
			 * registrazione oggetto remoto nell'rmiregistry
			 */
	        
	        System.out.println("Registering " + url + " to RMIRegistry...");
	        
			try {


				homeVirtualizationApplicationImpl = new HomeVirtualizationApplicationImpl(roomsNames,roomsGroupsAssociation,roomsIdentifier,databaseURL,databaseName,databaseUserName,databaseUserPassword,presenceProfileName,temperatureProfileName,lightProfileName,realtimePresenceValueName,realtimeTemperatureValueName,realtimeLightValueName,profilingSystem);
				
				Naming.rebind(url, homeVirtualizationApplicationImpl);
				
				homeVirtualizationApplicationImpl.start();
				
				System.out.println("Ready!");		

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}

}
	
