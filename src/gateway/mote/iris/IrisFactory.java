
package gateway.mote.iris;


import gateway.InternalWSNManager;
import gateway.mote.InternalMote;
import gateway.protocol.address.InternalNetworkAddressImpl;


/**
 * @author Davide Roveran
 * @version 1.0
 *
 * defines specific implementation of MoteFactory 
 * methods for MicaZ motes
 */
public class IrisFactory 
{
	/*
	 * (non-Javadoc)
	 * @see mobiwsn.middleware.MoteFactory#createMoteInstance()
	 */
	public static InternalMote createMoteInstance(String moteId, InternalNetworkAddressImpl newtorkAddress,
			short macAddress, short parentMacAddress,
			InternalWSNManager manager)
	{
		return new IrisInternalMote(moteId, newtorkAddress,
				macAddress, parentMacAddress,
				manager);		
	}
	
}