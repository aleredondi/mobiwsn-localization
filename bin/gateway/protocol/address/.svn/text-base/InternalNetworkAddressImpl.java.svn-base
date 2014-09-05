
package gateway.protocol.address;


import java.rmi.RemoteException;


public class InternalNetworkAddressImpl implements InternalNetworkAddress {
	int address;
	Short[] addressPart;
	RemoteNetworkAddressImpl remoteAddress;
	
	public InternalNetworkAddressImpl( int address ) {
		addressPart = new Short[5];
		int i;
		//System.out.println("Creo un nuovo indirizzo di rete dal valore " + address);
		
		this.address = address;
		for( i = 0; i < 5; i++ ) {
			/*System.out.println("prima parte :" + (short) address );
			System.out.println("prima parte :" + ( ( address ) >> ( 3 * ( 4 - i ) ) ) );
			System.out.println("prima parte :" + ( ( address ) >> ( 3 * ( 4 - i ) ) & 0x7 ) );
			System.out.println("seconda parte :" + (short) ( address >> ( 3 * ( 4 - i ) ) & 0x7 ) );*/
			
			addressPart[i] = new Short(  (short) ( ( address >> 1 ) >> ( 3 * ( 4 - i ) ) & 0x7 ) );
			//System.out.println("Parte  " + i + "valore " + addressPart[i]);
		}

		//System.out.println("Il mio indirizzo è " + this.toString());
		
		try {
			remoteAddress = new RemoteNetworkAddressImpl( this );
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public String toString() {
		String address = new String();
		int i;
		
		for( i = 0; i < 5; i++) {
			
			address = address + addressPart[i].toString() + "."; 
		}
		
		return address;
	}
	
	@Override
	public short getAddressPart( int i) {
		return addressPart[i];
	}
	
	@Override
	public int intValue() {
		return address;
	}
	
	@Override
	public boolean equals( InternalNetworkAddress anotherAddress ) {
		int i;
		boolean ans = true;

		for( i = 0; i < 5; i++) {
			if ( this.getAddressPart(i) != anotherAddress.getAddressPart(i) )
				ans = false;
		}
		
		return ans;
	}


	@Override
	public RemoteNetworkAddressImpl getRemote() {
		return remoteAddress;
	}

}
