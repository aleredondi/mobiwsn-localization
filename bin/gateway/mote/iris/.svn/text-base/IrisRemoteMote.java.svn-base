
package gateway.mote.iris;


import gateway.mote.GenericRemoteMote;


import java.rmi.RemoteException;

/**
 * @author Davide Roveran
 * @version 1.0
 *
 * Facade component used to interact with MicaZ mote
 */
public class IrisRemoteMote extends GenericRemoteMote 
{
	
	/*
	 * reference to the InternalMote object hidden
	 * by this MicaZMote
	 */
	private IrisInternalMote internalMote;	
	
	/**
	 * Constructor 
	 * 
	 * @param networkAddress
	 * @param macAddress
	 * @throws RemoteException 
	 */
	IrisRemoteMote(IrisInternalMote internalMote) throws RemoteException
	{
		super(internalMote);
		this.internalMote = internalMote;
		//System.out.println("Creo un generic remote con iris");
	}
	
}