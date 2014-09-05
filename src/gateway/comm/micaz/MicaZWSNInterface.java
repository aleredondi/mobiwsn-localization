
package gateway.comm.micaz;


import gateway.protocol.basic_message.MiddlewareMessage;
import gateway.protocol.MiddlewareMessageBuilder;
import gateway.comm.MessageListener;
import gateway.comm.WSNInterface;
import gateway.config.ConfigurationManager;

import java.io.IOException;
import java.util.*;

import common.exceptions.ExceptionHandler;

import net.tinyos.packet.*;
import net.tinyos.util.Dump;


/**
 * @author Davide Roveran
 * @version 1.0
 *
 * Implements basic methods for interaction with a MicaZ wireless sensor network
 * through a serial communication link between the mesh node and the PAN Coordinator
 * mote
 */
public class MicaZWSNInterface implements WSNInterface, net.tinyos.util.Messenger
{
	
	/*
	 * reference to the thread used to read from the serial link with the mote
	 */
	private InteractionThread interactionThread;
	
	private boolean boInterfaceStarted;
	
	/*
	 * list of listeners of MiddlewareMessage
	 */
	private LinkedList<MessageListener> messageListenerList;
	
	/*
	 * reference to a Packetizer object used to read and write byte array 
	 * packets from
	 */
	Packetizer moteInterface;
	
	/*
	 * nx_am_addr_t of the node attached by the serial port
	 */	
	private static byte PAN_COORD_ID;
	
	/**
	 * Constructor
	 *
	 */
	private MicaZWSNInterface() 
	{
		this.boInterfaceStarted = false;
		this.interactionThread= new InteractionThread();
		this.interactionThread.setName("WSNInterface.InteractionThread");
		
		this.messageListenerList = new LinkedList<MessageListener>();
		
		/*
		 * create an instance of Packetizer that uses SerialByteSource as 
		 * source for sending and receiving packets (get port and baud rate 
		 * from ConfigurationManager
		 */
		this.moteInterface = (Packetizer) BuildSource.makeSerial(
					ConfigurationManager.getInstance().getParameter("serial_port"), 
					new Integer(ConfigurationManager.getInstance().
											getParameter("serial_baud_rate")).intValue()
													);
		
		PAN_COORD_ID = Byte.parseByte(ConfigurationManager.getInstance().getParameter("pan_coord_id"));
		
		
	}
	
	/**
	 * Lazy initialization of MicaZWSNInterface instance, used
	 * to achieve thread-safety without using explicit synchronization
	 * constructs
	 */
	private static class MicaZWSNInterfaceContainer
	{
		private static final MicaZWSNInterface instance = new MicaZWSNInterface();
	}
	
	/**
	 * 
	 * @return Singleton Instance of ConfigurationManager
	 */
	public static MicaZWSNInterface getInstance()
	{
		return MicaZWSNInterfaceContainer.instance;
	}
	
	/*
	 * ************* Implementation of net.tinyos.util.Messenger interface **************
	 */
	
	public void message(String arg0)
	{
		// do nothing, DA DECIDERE COME EFFETTUARE I LOG, per ora su console
		System.out.println("MicaZWSNInterface: " + arg0);
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
			
			this.boInterfaceStarted=true;
			
			this.moteInterface.open(this);
			
			this.interactionThread= new InteractionThread();
			this.interactionThread.setName("WSNInterface.InteractionThread");		
			this.interactionThread.start();	
			
			
		}
		catch (IOException ioex)
		{
			ExceptionHandler.getInstance().handleException(ioex);
			this.boInterfaceStarted=false;
		}
			
	}
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.comm.WSNInterface#isStarted()
	 */
	public boolean isStarted()
	{
		return this.boInterfaceStarted;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.comm.WSNInterface#stopInterface()
	 */
	public void stopInterface()
	{
		
		try
		{
						
			this.boInterfaceStarted= false;		
				
			/*
			 * stop internal interactionThread;
			 */
			this.interactionThread.stop();
			
			/*
			 * wait one second before to close the interface;
			 */
			try {
				  Thread.sleep(1000);
			}
			catch (Exception e) {}
			
			/*
			 * close serial interface
			 */
			this.moteInterface.close();	
			
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
		if (!this.messageListenerList.contains(listener))
			this.messageListenerList.add(listener);
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
		
		
		/*
		 * sends the byte array of message through moteInterface
		 */
		try
		{
			return this.moteInterface.writePacket (addSerialHdr(message.getDataBytes()));
		}
		catch (IOException ioex)
		{
			ExceptionHandler.getInstance().handleException(ioex);
			return false;
		}
	}
	
	private byte[] addSerialHdr(byte[] dataBytes) {
		  /*
		  * adding TOS_SERIAL_ACTIVE_MESSAGE_ID and SerialPacket header (see TEP 113 and Serial.h (in nesC Code)
		  */
		byte [] sendPacket = new byte[dataBytes.length + 8];
		
		sendPacket[0] = Serial.TOS_SERIAL_ACTIVE_MESSAGE_ID;
		
		// nx_am_addr_t dest
		sendPacket[1] = 0;
		sendPacket[2] = PAN_COORD_ID;
		
		//nx_am_addr_t src
		sendPacket[3] = 0;
		sendPacket[4] = PAN_COORD_ID;
		
		//nx_uint8_t length
		sendPacket[5] = (byte)dataBytes.length;
		
		//nx_am_group_t group 
		sendPacket[6] = 0;
			
		// nx_am_id_t am_type
		sendPacket[7] = 9;
			
		System.arraycopy(dataBytes, 0, sendPacket, 8, dataBytes.length);
		
		return sendPacket;
		
	}
	
	/**
	 * 
	 * @author Davide Roveran
	 * @version 1.0
	 *<br>
	 * inner thread class of MicaZWSNInterface:
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
				 * corrispondenti al pacchetto di livello network)
				 */
				byte[] dataPacket;
				
				/*
				 * read a byte array packet from the serialSource;
				 */
				try
				{
					
					byte[] packet = moteInterface.readPacket();
					
					int dataLength = packet.length - 8;
					dataPacket = new byte[dataLength];
			        
					System.arraycopy(packet, 8, dataPacket, 0, dataLength);
					
					/*
					 * Stampa l'array di byte (valori esadecimali) corrispondenti al pacchetto 
					 * di livello middleware ricevuto dal pancoordinator 
					 */
					System.out.println();
					Dump.printPacket(System.out, dataPacket);
					System.out.println();
					System.out.flush();
					
					if (!isStarted())
					{
						
						return;
					}
					
					if (packet ==null) continue;
							
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
					System.out.println("packet received , message Building");
					MiddlewareMessage message = MiddlewareMessageBuilder.getMessage(dataPacket);
					
					/*
					 * for each registered listener, notify receipt of a MiddlewareMessage
					 */
					Iterator<MessageListener>  it = messageListenerList.iterator();
					while (it.hasNext()) 
					{
						System.out.println("Calling listeners"); 
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
