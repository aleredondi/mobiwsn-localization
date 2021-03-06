

package gateway.mote.iris;


import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteType;

import java.rmi.RemoteException;

import gateway.InternalWSNManager;
import gateway.mote.GenericInternalMote;
import gateway.protocol.address.InternalNetworkAddressImpl;


/**
 * @author Davide Roveran
 *
 */
public class IrisInternalMote extends GenericInternalMote
{
	public IrisInternalMote(String moteId, InternalNetworkAddressImpl newtorkAddress,
			short macAddress, short parentMacAddress,
			InternalWSNManager manager) {
		super(moteId, newtorkAddress, macAddress, parentMacAddress, manager);
		
		//System.out.println("Creo un generic con iris");
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
		if (o instanceof IrisInternalMote)
		{
			IrisInternalMote mote = (IrisInternalMote) o;
			
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
	public Mote createRemote() {
		try {
			return new IrisRemoteMote(this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public MoteType getType() {
		return MoteType.IRIS;
	}
}
