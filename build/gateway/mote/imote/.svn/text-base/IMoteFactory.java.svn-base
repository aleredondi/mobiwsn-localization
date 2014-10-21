
package gateway.mote.imote;


import gateway.InternalWSNManager;
import gateway.mote.InternalMote;
import gateway.protocol.address.InternalNetworkAddressImpl;


/**
 * @author Andrea Scalise

 *
 * defines specific implementation of MoteFactory 
 * methods for IMote motes
 */
public class IMoteFactory 
{
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.MoteFactory#createMoteInstance()
	 */
	public static InternalMote createMoteInstance(String moteId, InternalNetworkAddressImpl newtorkAddress,
			short macAddress, short parentMacAdress,
			InternalWSNManager manager)
	{
		return new IMoteInternalMote(moteId, newtorkAddress,
				macAddress, parentMacAdress,
				manager);		
	}
}