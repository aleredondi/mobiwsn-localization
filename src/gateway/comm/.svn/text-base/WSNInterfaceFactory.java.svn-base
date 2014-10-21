
package gateway.comm;


/**
 * @author Davide Roveran
 * @version 1.0
 * <br><br>
 * 
 * Provieds a getInterface() method to select the right type 
 * of communicationInterface to the mote
 */
public class WSNInterfaceFactory 
{

	public static WSNInterface getInterface(String comm)
	{
		/*
		MoteConnectionType moteConnectionType = 
			MoteConnectionType.valueOf(
								ConfigurationManager.getInstance().getParameter("mote_conn_type")
									  					);
		
		switch (moteConnectionType)
		{
			case SERIAL:
		*/
				return SerialWSNInterface.getInstance(comm);
				
		/* NOTE: DEPRECATI:
		
			case MICAZ_SERIAL :
				return MicaZWSNInterface.getInstance();
				
			case MICAZ_ETHERNET :
				//return new Mica2Factory();
				break;
				
		}

		return null;
		*/
				
	}
	
	
}
