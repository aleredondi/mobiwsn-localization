
package gateway.mote.shimmer;


import gateway.InternalWSNManager;
import gateway.mote.InternalMote;
import gateway.protocol.address.InternalNetworkAddressImpl;


/**
 * @author Andrea Scalise

 *
 * defines specific implementation of MoteFactory 
 * methods for IMote motes
 */
public class ShimmerFactory 
{
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.MoteFactory#createMoteInstance()
	 */
	public static InternalMote createMoteInstance(String moteId, InternalNetworkAddressImpl newtorkAddress,
			short macAddress, short parentMacAdress,
			InternalWSNManager manager)
	{
		return new ShimmerInternalMote(moteId, newtorkAddress,
				macAddress, parentMacAdress,
				manager);		
	}
}