package client_applications.localization;

import java.rmi.RemoteException;

import remote_interfaces.mote.Mote;
import remote_interfaces.protocol.NetworkAddress;

public class LauraNode {
	
	protected Mote mote_ref;

	public NetworkAddress getNetwork_address() {
		try {
			return mote_ref.getNetworkAddress();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean sensorListIsEmpty(){
		boolean result = false;
		try {
			result = mote_ref.getSensorList().isEmpty();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String getStatus() {
		
		return "NO STATUS";
	}

	public String getInstantTemp() {

		return "NO TEMP";
	}

}
