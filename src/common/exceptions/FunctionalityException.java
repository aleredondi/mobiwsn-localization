package common.exceptions;

import java.rmi.RemoteException;

import remote_interfaces.functionality.Functionality;

public class FunctionalityException extends Exception {

	/**
	 * Creates a new FunctionalityException with the specified message
	 * @throws RemoteException 
	 */
	public FunctionalityException(String error, Functionality func) throws RemoteException
	{
		super("Functionality " + func.getId() + " on mote " + func.getOwnerMoteId() + "error: " + error);
	}

}
