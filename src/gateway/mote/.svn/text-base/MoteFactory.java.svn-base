
package gateway.mote;


import gateway.InternalWSNManager;
import gateway.mote.imote.IMoteFactory;
import gateway.mote.iris.IrisFactory;
import gateway.mote.micaz.MicaZFactory;
import gateway.mote.shimmer.ShimmerFactory;
import gateway.protocol.address.InternalNetworkAddressImpl;

import java.util.Vector;

import remote_interfaces.mote.MoteType;


/**
 * @author Davide Roveran
 * <br><br>
 * 
 * AbstractFactory, defines abstract methods for the different specific components
 * to be created
 */
public class MoteFactory 
{
	/**
	 * Return specific concrete factory for moteType motes
	 * @param moteType type of mote in the WSN
	 * 
	 * @return
	 */
	/*public static MoteFactory getFactory(MoteType moteType)
	{
		switch (moteType)
		{
			case MICAZ :
				return new MicaZFactory();
				
			case MICA2 :
				//return new Mica2Factory();
				break;

			case IMOTE:
				return new IMoteFactory();

			case IRIS:
				return new IrisFactory();
			
		}
		
		return null;
		
	}*/
	
	/**
	 * creates a new specific concrete Mote
	 * @return internal Mote object
	 */
	public InternalMote createMoteInstance(String moteId, MoteType moteType, 
			InternalNetworkAddressImpl newtorkAddress, short macAddress, short parentMacAdress,
			InternalWSNManager manager) {
		
		switch (moteType)
		{
			case MICAZ :
				return MicaZFactory.createMoteInstance(moteId, newtorkAddress, macAddress, parentMacAdress, manager);

			case IMOTE:
				return IMoteFactory.createMoteInstance(moteId, newtorkAddress, macAddress, parentMacAdress, manager);
				
			case IRIS:
				return IrisFactory.createMoteInstance(moteId, newtorkAddress, macAddress, parentMacAdress, manager);
			
			case SHIMMER:
				return ShimmerFactory.createMoteInstance(moteId, newtorkAddress, macAddress, parentMacAdress, manager);
				
		}
		
		return null;
	}
	
	/**
	 * creates a new specific concrete facade Mote, to provide 
	 * an appropriate subset of methods to the users
	 * 
	 * @param internalMote internal Mote object to hide
	 * @return new facade Mote object
	 */
	/*public InternalMote createMoteFacadeInstance(short macAddress, WSNManager manager) {

		
		return new FacadeMoteImpl( macAddress, manager );
	}*/
	

}
