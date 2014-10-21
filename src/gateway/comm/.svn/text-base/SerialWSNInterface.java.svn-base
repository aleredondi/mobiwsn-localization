
package gateway.comm;


//import remote_interfaces.mote.Mote;

//import gateway.WSNGatewayImpl;
import gateway.config.ConfigurationManager;
import gateway.protocol.basic_message.MiddlewareMessage;
import gateway.protocol.MiddlewareMessageBuilder;

import common.exceptions.ExceptionHandler;

import java.io.IOException;
//import java.rmi.RemoteException;
import java.util.*;

import net.tinyos.packet.*;
import net.tinyos.util.Dump;
import net.tinyos.util.PrintStreamMessenger;


/**
 * @authors Davide Roveran Luca Pietro Borsani
 *
 * Implements basic methods for interaction with a mote wireless sensor network
 * through a serial communication link between the mesh node and the PAN Coordinator
 * mote
 */
public class SerialWSNInterface implements WSNInterface, net.tinyos.util.Messenger
{
	//private WSNGatewayImpl gw_ref;
	
	/*
	 * reference to the thread used to read from the serial link with the mote
	 */
	private InteractionThread interaction_thread;
	
	private boolean interface_started;
	
	/*
	 * list of listeners of MiddlewareMessage
	 */
	private LinkedList<MessageListener> message_listener_list;
	
	/*
	 * reference to a PacketSource object used to read and write byte array 
	 * packets from
	 */
	//String source = ConfigurationManager.getInstance().getParameter("serial_host"); //"sf@localhost:9002";
	String source;
	PacketSource mote_interface;
	
	PhoenixSource phoenix;
	
	// nx_am_addr_t of the node attached by the serial port	
	private static byte PAN_COORD_ID;
	
	
	/**
	 * Constructor
	 *
	 */
	private SerialWSNInterface(String comm) 
	{
		this.interface_started = false;
		
		this.message_listener_list = new LinkedList<MessageListener>();
		
		this.source = comm;
		
		this.phoenix = BuildSource.makePhoenix(source, PrintStreamMessenger.err);
		
		this.phoenix.setResurrection();
		
		/*
		 * create an instance of PacketSource
		 */
		this.mote_interface = this.phoenix.getPacketSource();//BuildSource.makePacketSource(source);
		
		PAN_COORD_ID = Byte.parseByte(ConfigurationManager.getInstance().getParameter("pan_coord_id"));
		
	}
	
	/**
	 * Lazy initialization of SerialWSNInterface instance, used
	 * to achieve thread-safety without using explicit synchronization
	 * constructs
	 */
	private static class SerialWSNInterfaceContainer
	{
		private static SerialWSNInterface instance;
		
		public SerialWSNInterfaceContainer(String comm)
		{
			SerialWSNInterfaceContainer.instance = new SerialWSNInterface(comm);
		}
	}
	
	/**
	 * 
	 * @return Singleton Instance of ConfigurationManager
	 */
	public static SerialWSNInterface getInstance(String comm)
	{
		new SerialWSNInterfaceContainer(comm);
			
		return SerialWSNInterfaceContainer.instance;
	}
	
	/*
	 * ************* Implementation of net.tinyos.util.Messenger interface **************
	 */
	
	public void message(String arg0)
	{
		// do nothing, DA DECIDERE COME EFFETTUARE I LOG, per ora su console
		System.out.println(">SerialWSNInterface: " + arg0);
	}
	
	
	/*
	 * ************* Implementation of mobiwsn.middleware.comm.WSNInterface interface **************
	 */
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.comm.WSNInterface#startInterface()
	 */
	public void startInterface()
	{
			
		/*
		 * open interface to the mote
		 */
		try
		{
			
			this.interface_started=true;
			
			this.mote_interface.open(this);

			this.interaction_thread= new InteractionThread();
			this.interaction_thread.setName("WSNInterface.InteractionThread");		
			this.interaction_thread.start();	
			
		}
		catch (IOException ioex)
		{
			ExceptionHandler.getInstance().handleException(ioex);
			this.interface_started=false;
		}
			
	}
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.comm.WSNInterface#isStarted()
	 */
	public boolean isStarted()
	{
		return this.interface_started;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.comm.WSNInterface#stopInterface()
	 */
	public void stopInterface()
	{
		try
		{
						
			this.interface_started= false;		
				
			/*
			 * stop internal interactionThread;
			 */
			this.interaction_thread.stop();
			
			/*
			 * wait one second before to close the interface;
			 */
			try {
				  Thread.sleep(1000);
			}
			catch (InterruptedException e) {}
			
			/*
			 * close serial interface
			 */
			this.mote_interface.close();	
			
		}
		catch (IOException ioex)
		{
			ExceptionHandler.getInstance().handleException(ioex);
		}

	}
	
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.comm.WSNInterface#addListener(mobiwsn.middleware.comm.MiddlewareMessageListener)
	 */
	public synchronized void addListener(MessageListener listener)
	{
		if (!this.message_listener_list.contains(listener))
			this.message_listener_list.add(listener);
		else 
			throw new IllegalArgumentException("listener already added to messageListenerList!");
	}
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.comm.WSNInterface#sendMessage(mobiwsn.middleware.protocol.MiddlewareMessage)
	 */
	public boolean sendMessage(MiddlewareMessage message) 
	{
		if (message == null)
			throw new IllegalArgumentException("message cannot be null!");
		
		boolean result = false;
		
		//byte [] byte_to_send = addNwkGroupHdr(message.getDataBytes(), message.getDestinationId());
		
		// Stampa l'array di byte (valori esadecimali) corrispondenti al pacchetto inviato al pancoordinator 
		System.out.println();
		Dump.printPacket(System.out, message.getDataBytes());
		System.out.println();
		System.out.flush();
		
		/*
		 * sends the byte array of message through moteInterface
		 */
		try
		{
			result = this.mote_interface.writePacket (/*addSerialHdr(byte_to_send)*/ message.getDataBytes());
			
			/*
			 * wait 500 ms after to write packet;
			 */
			Thread.sleep(500);
						
		}
		catch (IOException ioex )
		{
			ExceptionHandler.getInstance().handleException(ioex);
			
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		return result;
		
	}
	/*
	private byte[] addNwkGroupHdr(byte[] data_bytes, String dest) {
			
		  
		  // adding network header (see Network.h in codice_sensori code)
		  
		byte [] byte_to_send = new byte[data_bytes.length + 11];

		//forwarding_header_t
		byte_to_send[0] = 0;
		
		PckScopeType pck_scope;
		
		if (dest.equals("FFFFFFFF"))
			pck_scope = PckScopeType.BROADCAST;
		else
			pck_scope = PckScopeType.UNICAST;
		
		//nx_control_field_t ctrl_fld										// TODO: impostarli meglio quando sposterò gli hdr in middleware msg:
		byte_to_send[2] = (byte)(8 << 4);                                     // - header_len è pari a 8, in binario 1000, ed è definito nei bit tra 0 e 3;
		byte_to_send[2] = (byte)(byte_to_send[2] | (pck_scope.ordinal() << 1));	// - il Packet Scope è definito nei bit 4-6 (tra 0 e 15);
		byte_to_send[1] = (byte)(2 << 6);                                     // - il Payload Type è definito nei bit 7-9 ma cosidero solo 8-9
															                //   dato che, essendo MIDDLEWARE = 2, in binario vale 10. 
		
		//nx_network_id_t nwk_id
		byte_to_send[3] = 0;
		byte_to_send[4] = 0;
				
		try {
					
			short nwk_addr = MiddlewareMessage.NWK_NULL_ADDRESS;
					
			//insert sensor_value
			byte_to_send[6] = (byte)((nwk_addr & 65280) >> 8);
			byte_to_send[5] = (byte)(nwk_addr & 255);
			
			ArrayList<Mote> mote_list = SerialWSNInterfaceContainer.instance.gw_ref.getMoteList();
			
			if (!dest.equals("FFFFFFFF"))
			{
				for(Mote searched_mote : mote_list)
				{
					if (searched_mote.getId().equals(dest)) {
						nwk_addr = searched_mote.getNetworkAddress();
						break;
					}
				}
			}
			
			//nx_network_addr_t nwk_dst_addr
			byte_to_send[8] = (byte)((nwk_addr & 65280) >> 8);
			byte_to_send[7] = (byte)(nwk_addr & 255);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
			
	 	//nx_group_id_t group_id;
		byte_to_send[9] = 0;
		
	 	//nx_area_id_t area_id;		
		byte_to_send[10] = 0;
		
		System.arraycopy(data_bytes, 0, byte_to_send, 11, data_bytes.length);
		
		return byte_to_send;
		
	}
		
	private byte[] addSerialHdr(byte[] byte_to_send) {
		  
		  // adding TOS_SERIAL_ACTIVE_MESSAGE_ID and SerialPacket header (see TEP 113 and Serial.h (in nesC Code)
		  
		byte [] send_packet = new byte[byte_to_send.length + 8];
		
		send_packet[0] = Serial.TOS_SERIAL_ACTIVE_MESSAGE_ID;
		
		// nx_am_addr_t dest
		send_packet[1] = 0;
		send_packet[2] = PAN_COORD_ID;
		
		//nx_am_addr_t src
		send_packet[3] = 0;
		send_packet[4] = PAN_COORD_ID;
		
		//nx_uint8_t length
		send_packet[5] = (byte)byte_to_send.length;
		
		//nx_am_group_t group 
		send_packet[6] = 0;
			
		// nx_am_id_t am_type
		send_packet[7] = 9;
			
		System.arraycopy(byte_to_send, 0, send_packet, 8, byte_to_send.length);
		
		// Stampa l'array di byte (valori esadecimali) corrispondenti al pacchetto 
		// di livello middleware inviato al pancoordinator 
		
		System.out.println();
		Dump.printPacket(System.out, send_packet);
		System.out.println();
		System.out.flush();
		
		return send_packet;
		
	}
*/
	/**
	 * 
	 * @author Davide Roveran
	 * @version 1.0
	 *<br>
	 * inner thread class of SerialWSNInterface:
	 * - reads bytes arrays of packets received through the interface to the mote 
	 * - generates MiddlewareMessage object
	 * - notify MiddlewareMessageListeners
	 */
	private class InteractionThread extends Thread
	{
		
		public void run()
		{
			while (isStarted())
			{
				/*
				 * ipotesi : lettura da packetizer() (readPacket restituisce un'array di byte
				 * corrispondenti al pacchetto di livello network compreso l'header seriale)
				 */
				//byte[] data_packet;
				byte[] packet;
				
				/*
				 * read a byte array packet from the serialSource;
				 */
				try
				{
					
					/*byte[]*/ packet = mote_interface.readPacket();				
					/*					
					int dataLength = packet.length - 19;
					data_packet = new byte[dataLength];
			        
					System.arraycopy(packet, 19, data_packet, 0, dataLength);
					*/
					/*
					 * Stampa l'array di byte (valori esadecimali) corrispondenti al pacchetto 
					 * di livello middleware ricevuto dal pancoordinator 
					 */
					System.out.println();
					Dump.printPacket(System.out, /*data_packet*/packet);
					System.out.println();
					System.out.flush();

					if (!isStarted())
					{
						
						return;
					}
					
					if (/*data_*/packet ==null) continue;
							
				}
				catch (IOException ioex)
				{
					ExceptionHandler.getInstance().handleException(ioex);
					continue;
				}
				
				try
				{
					/*
					 * construct a MiddlewareMessage
					 */
					//System.out.println("packet received , message Building...");
					MiddlewareMessage message = MiddlewareMessageBuilder.getMessage(/*data_*/packet);
					
					/*
					 * for each registered listener, notify receipt of a MiddlewareMessage
					 */
					Iterator<MessageListener>  it = message_listener_list.iterator();
					while (it.hasNext()) 
					{
						//System.out.println("Calling listeners"); 
					    MessageListener listener = (MessageListener)it.next() ;
					    listener.messageReceived(message);
					}
				}
				catch (Exception ex)
				{
					ExceptionHandler.getInstance().handleException(ex);
					continue;
				}
			}
			
			System.out.println("Exiting from Interaction Thread...");	
			
		}
	}

}
