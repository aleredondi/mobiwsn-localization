
package gateway.protocol.address;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import remote_interfaces.protocol.NetworkAddress;


public class RemoteNetworkAddressImpl extends UnicastRemoteObject implements NetworkAddress {
	InternalNetworkAddress address;

	protected RemoteNetworkAddressImpl( InternalNetworkAddressImpl address) throws RemoteException {
		super();
		
		this.address = address;
	}

	@Override
	public boolean equals(NetworkAddress anotherAddress) {
		//Come fare?
		return false;
	}

	@Override
	public short getAddressPart(int i) throws RemoteException {
		return address.getAddressPart(i);
	}

	@Override
	public int intValue() throws RemoteException {
		return address.intValue();
	}

	@Override
	public String getString() throws RemoteException {
		return address.toString();
	}
}
