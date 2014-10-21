
package gateway.protocol;

import gateway.mote.InternalMote;
import gateway.protocol.basic_message.MiddlewareMessage;
import gateway.protocol.basic_message.MiddlewareMessageType;


/**
 * @authors Davide Roveran, Luca Pietro Borsani
 *
 * Define a particular MiddlewareMessage sent by the Gateway of a WSN
 * (i.e. by the PAN Coordinator) to a specific sensor, to a group or to all sensors in the network, 
 * to request mote_announcement messages
 */
public class MoteDiscoveryMessage extends MiddlewareMessage 
{
	
	private final byte MOTE_DISCOVERY_MSG_LEN = 0;
	
	/*public MoteDiscoveryMessage(byte[] data)
	{
		super(data);
	}*/
	
	public MoteDiscoveryMessage(InternalMote dest_mote)
	{
		byte len = (byte) (super.getPayload()+MOTE_DISCOVERY_MSG_LEN);
		
		data_byte = new byte[len];
		
		addSerialHdr(len);			
	
		setMessageType(MiddlewareMessageType.MOTE_DISCOVERY);
		
		//setCorrelationId((short)0);
		
		setDestinationMessage(dest_mote);
		
	}
	
	/*
	 * typedef nx_struct mote_discovery_message_t
	 * {
	 * } mote_discovery_message_t;
	 *
	 * It does not contain any other field 
	 */
}
