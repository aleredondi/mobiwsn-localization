package client_applications.localization;

public class RssiCell {
	private static final double alpha = 0.9;
	private int mac,rssi;
	
	public RssiCell(int mac, int rssi) {
		this.mac = mac;
		this.rssi = rssi;
	}
	
	public int getMac(){
		return mac;
	}
	
	public int getRssi(){
		return rssi;
	}
	
	public void setRssi(int new_rssi){
		rssi = (int)((1-alpha)*rssi + alpha*new_rssi);
	}

}
