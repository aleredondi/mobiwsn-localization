
package gateway.protocol.basic_message;

// Defines global error codes for error_t in TinyOS

	public enum ErrorCodeType {

		  SUCCESS,          
		  FAIL,            // Generic condition: backwards compatible
		  ESIZE,           // Parameter passed in was too big.
		  ECANCEL,         // Operation cancelled by a call.
		  EOFF,            // Subsystem is not active
		  EBUSY,           // The underlying system is busy; retry later
		  EINVAL,          // An invalid parameter was passed
		  ERETRY,          // A rare and transient failure: can retry
		  ERESERVE,        // Reservation required before usage
		  EALREADY,        // The device state you are requesting is already set
		  ENOMEM,          // Memory required not available
		  ENOACK,          // A packet was not acknowledged
		  ELAST,           // Last enum value
		  
		  UNDEFINED;
		  
		public static ErrorCodeType convert(int i)
		{
			try 
			{
				return values()[i];
			} 
			catch ( ArrayIndexOutOfBoundsException e ) 
			{
				return UNDEFINED;
			}
		}
}
