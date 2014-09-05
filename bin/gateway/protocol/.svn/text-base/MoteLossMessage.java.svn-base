
package gateway.protocol;

import gateway.protocol.address.InternalNetworkAddress;
import gateway.protocol.address.InternalNetworkAddressImpl;
import gateway.protocol.basic_message.MiddlewareMessage;


/**
 * @authors Davide Roveran Luca Pietro Borsani
 *
 * this message is sent by a mote in a WSN when network layer signals that an association
 * with a child mote is lost.
 */
public class MoteLossMessage extends MiddlewareMessage 
{
	
	private boolean mote_loss_nwk_addr_is_nxle;

	private final byte MOTE_LOSS_NWK_ADRR_POSITION = 0;
	
	public MoteLossMessage(byte[] data)
	{
		super(data);
		
		mote_loss_nwk_addr_is_nxle = true;
			
		System.out.println(">MoteLoss message:");
		System.out.println("      Network Address: " + getMoteLossNwkAddress() + "\n");
	}
	
	/**
	 * Mote loss message structure:
	 * 
	 * Mote Network Address:	2byte;
	 * 
	 * typedef nx_struct 
	 * {
	 * nx_network_addr_t		mote_network_address;
	 * } mote_loss_message_t;
	 * 
	 */
	
	/**
	 * @param mote_nwk_address the mote_nwk_address to set
	 */
	public void setMoteLossNwkAddress(short mote_nwk_address) 
	{
		setShortField(mote_nwk_address, super.getPayload()+MOTE_LOSS_NWK_ADRR_POSITION, mote_loss_nwk_addr_is_nxle);
/*		
		if (mote_loss_nwk_addr_is_nxle)
			mote_nwk_address = nxTOnxleType(mote_nwk_address);
		
		// nx_network_addr_t mote_network_address;
 		data_byte[super.getPayload()+MOTE_LOSS_NWK_ADRR_POSITION] = (byte)( (mote_nwk_address & 0xFF00) >> 8);
		data_byte[super.getPayload()+MOTE_LOSS_NWK_ADRR_POSITION+1] = (byte)( mote_nwk_address & 0xFF);
*/	
	}

	/**
	 * @return the mote_nwk_address
	 */
	public InternalNetworkAddress getMoteLossNwkAddress() 
	{
		return new InternalNetworkAddressImpl( getShortField(super.getPayload()+MOTE_LOSS_NWK_ADRR_POSITION, mote_loss_nwk_addr_is_nxle) );
/*
		if (mote_loss_nwk_addr_is_nxle)
			return (short) ( (data_byte[super.getPayload()+MOTE_LOSS_NWK_ADRR_POSITION+1] <<8) + data_byte[super.getPayload()+MOTE_LOSS_NWK_ADRR_POSITION] );
		else
			return (short) ( (data_byte[super.getPayload()+MOTE_LOSS_NWK_ADRR_POSITION] <<8) + data_byte[super.getPayload()+MOTE_LOSS_NWK_ADRR_POSITION+1] );
*/			
	}
	
}
