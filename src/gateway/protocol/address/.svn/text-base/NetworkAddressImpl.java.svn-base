
package gateway.protocol.address;


public class NetworkAddressImpl {
	short[] addressPart;
	
	NetworkAddressImpl( byte[] address, boolean isBigEndian ) {
		addressPart = new short[5];
		short addressTemp;
		int i;
		
		if(!isBigEndian)
			addressTemp =  (short) ( ( (0x00FF & (short)address[1]) << 8) + (0x00FF & (short)address[0]) ) ;
		else
			addressTemp =  (short) ( ( (0x00FF & (short)address[0]) << 8) + (0x00FF & (short)address[1]) ) ;
		
		for( i = 0; i < 5; i++ ) {
			addressPart[i] = (short) ( addressTemp << ( 3 * i ) & 0x7 );
		}
	}
	
	
	public String toString() {
		String address = new String(" ");
		int i;
		
		for( i = 0; i < 5; i++) {
			address.concat( String.valueOf( addressPart[i] ) + "." ); 
		}
		
		return address;
	}

}
