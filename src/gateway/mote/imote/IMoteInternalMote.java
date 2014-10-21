
package gateway.mote.imote;


import gateway.InternalWSNManager;

import gateway.mote.GenericInternalMote;
import gateway.mote.GenericRemoteMote;
import gateway.mote.micaz.MicaZRemoteMote;

import gateway.protocol.address.InternalNetworkAddressImpl;

import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteType;

import java.rmi.RemoteException;


public class IMoteInternalMote extends GenericInternalMote
{

	public IMoteInternalMote(String moteId, InternalNetworkAddressImpl newtorkAddress,
			short macAddress, short parentMacAddress,
			InternalWSNManager manager) {
		super(moteId, newtorkAddress, macAddress, parentMacAddress, manager);
		
		//System.out.println("Creo un generic con imote");
	}

	/**
	 * Compare the specified Object with this MicazInternalMote for equality
	 * @param o the Object to be compared with this MicaZInternalMote
	 * @return true if id, mac address and network address of the two object
	 * are equal
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof IMoteInternalMote)
		{
			IMoteInternalMote mote = (IMoteInternalMote) o;
			
			if (
				mote.getId().equals(this.getId()) &&
				mote.getMACAddress() == this.getMACAddress() &&
				mote.getNetworkAddress() == this.getNetworkAddress()
				)
				return true;
			else
				return false;
		}
		else
			return false;
	}

	@Override
	protected Mote createRemote() {
		try {
			return new IMoteRemoteMote(this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public MoteType getType() {
		return MoteType.IMOTE;
	}
	
}
