
package gateway.mote.micaz;


import gateway.mote.GenericRemoteMote;


import java.rmi.RemoteException;

/**
 * @author Davide Roveran
 * @version 1.0
 *
 * Facade component used to interact with MicaZ mote
 */
public class MicaZRemoteMote extends GenericRemoteMote 
{
	
	/*
	 * reference to the InternalMote object hidden
	 * by this MicaZMote
	 */
	private MicaZInternalMote internalMote;	
	
	/**
	 * Constructor 
	 * 
	 * @param networkAddress
	 * @param macAddress
	 * @throws RemoteException 
	 */
	MicaZRemoteMote(MicaZInternalMote internalMote) throws RemoteException
	{
		super(internalMote);
		this.internalMote = internalMote;
		//System.out.println("Creo un generic remote con micaz");
	}
	
}