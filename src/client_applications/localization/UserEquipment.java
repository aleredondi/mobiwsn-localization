package client_applications.localization;

import java.util.ArrayList;
import java.util.Calendar;

import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.json.JSONException;
import org.json.JSONObject;

public class UserEquipment {

	private String uuid;
	private String uri;
	private String deviceId;
	private String locationServiceId;
	private int displayState;
	private float positionX;
	private float positionY;
	private String wifiMac;
	private String assignedAccessPoint;
	private String activeAppActivity;
	private String activeAppPackage;
	private float rxTotalBps;
	private float rxMobileBps;
	private float rxWifiBps;
	private float txTotalBps;
	private float txMobileBps;
	private float txWifiBps;
	
	//structures for storing data
	ArrayList<Double> txWifiBpsArray = new ArrayList<Double>();
	ArrayList<Double> rxWifiBpsArray = new ArrayList<Double>();
	ArrayList<Double> sumtxrxWifiBpsArray = new ArrayList<Double>();
	ArrayList<Second> timeWifiBpsArray = new ArrayList<Second>();
	
	
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getLocationServiceId() {
		return locationServiceId;
	}
	public void setLocationServiceId(String locationServiceId) {
		this.locationServiceId = locationServiceId;
	}
	public int getDisplayState() {
		return displayState;
	}
	public void setDisplayState(int displayState) {
		this.displayState = displayState;
	}
	public float getPositionX() {
		return positionX;
	}
	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}
	public float getPositionY() {
		return positionY;
	}
	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}
	public String getWifiMac() {
		return wifiMac;
	}
	public void setWifiMac(String wifiMac) {
		this.wifiMac = wifiMac;
	}
	public String getAssignedAccessPoint() {
		return assignedAccessPoint;
	}
	public void setAssignedAccessPoint(String assignedAccessPoint) {
		this.assignedAccessPoint = assignedAccessPoint;
	}
	public String getActiveAppActivity() {
		return activeAppActivity;
	}
	public void setActiveAppActivity(String activeAppActivity) {
		this.activeAppActivity = activeAppActivity;
	}
	public String getActiveAppPackage() {
		return activeAppPackage;
	}
	public void setActiveAppPackage(String activeAppPackage) {
		this.activeAppPackage = activeAppPackage;
	}
	public float getRxTotalBps() {
		return rxTotalBps;
	}
	public void setRxTotalBps(float rxTotalBps) {
		this.rxTotalBps = rxTotalBps;
	}
	public float getRxMobileBps() {
		return rxMobileBps;
	}
	public void setRxMobileBps(float rxMobileBps) {
		this.rxMobileBps = rxMobileBps;
	}
	public float getRxWifiBps() {
		return rxWifiBps;
	}
	public void setRxWifiBps(float rxWifiBps) {
		this.rxWifiBps = rxWifiBps;
	}
	public float getTxTotalBps() {
		return txTotalBps;
	}
	public void setTxTotalBps(float txTotalBps) {
		this.txTotalBps = txTotalBps;
	}
	public float getTxMobileBps() {
		return txMobileBps;
	}
	public void setTxMobileBps(float txMobileBps) {
		this.txMobileBps = txMobileBps;
	}
	public float getTxWifiBps() {
		return txWifiBps;
	}
	public void setTxWifiBps(float txWifiBps) {
		this.txWifiBps = txWifiBps;
	}
	
	public UserEquipment(String uuid, String uri)
	{
		this.setUuid(uuid);
		this.setUri(uri);
		this.setDeviceId("not initialized");
	}
	
	public void updeteUe(JSONObject ue) throws JSONException
	{		
		if(ue.has("device_id") && !ue.isNull("device_id"))
			this.setDeviceId(ue.getString("device_id"));
		if(ue.has("location_service_id") && !ue.isNull("location_service_id"))
			this.setLocationServiceId(ue.getString("location_service_id"));
		if(ue.has("display_state") && !ue.isNull("display_state"))
			this.setDisplayState(ue.getInt("display_state"));
		if(ue.has("position_x") && !ue.isNull("position_x"))
			this.setPositionX((float)ue.getDouble("position_x"));
		if(ue.has("position_y") && !ue.isNull("position_y"))
			this.setPositionY((float)ue.getDouble("position_y"));
		if(ue.has("wifi_mac") && !ue.isNull("wifi_mac"))
			this.setWifiMac(ue.getString("wifi_mac"));
		if(ue.has("assigned_accesspoint") && !ue.isNull("assigned_accesspoint"))
			this.setAssignedAccessPoint(ue.getString("assigned_accesspoint"));
		if(ue.has("active_application_activity") && !ue.isNull("active_application_activity"))
			this.setActiveAppActivity(ue.getString("active_application_activity"));
		if(ue.has("active_application_package") && !ue.isNull("active_application_package"))
			this.setActiveAppPackage(ue.getString("active_application_package"));
		if(ue.has("rx_total_bytes_per_second") && !ue.isNull("rx_total_bytes_per_second"))
			this.setRxTotalBps((float)ue.getDouble("rx_total_bytes_per_second"));
		if(ue.has("rx_mobile_bytes_per_second") && !ue.isNull("rx_mobile_bytes_per_second"))
			this.setRxMobileBps((float)ue.getDouble("rx_mobile_bytes_per_second"));
		
		if(ue.has("rx_wifi_bytes_per_second") && !ue.isNull("rx_wifi_bytes_per_second")){
			double rx_wifi_bps = ue.getDouble("rx_wifi_bytes_per_second");
			this.setRxWifiBps((float)rx_wifi_bps);
			rxWifiBpsArray.add((Double)rx_wifi_bps);
		}
		
		if(ue.has("tx_total_bytes_per_second") && !ue.isNull("tx_total_bytes_per_second"))
			this.setTxTotalBps((float)ue.getDouble("tx_total_bytes_per_second"));
		if(ue.has("tx_mobile_bytes_per_second") && !ue.isNull("tx_mobile_bytes_per_second"))
			this.setTxMobileBps((float)ue.getDouble("tx_mobile_bytes_per_second"));
			
		if(ue.has("tx_wifi_bytes_per_second") && !ue.isNull("tx_wifi_bytes_per_second")){
			double tx_wifi_bps = ue.getDouble("tx_wifi_bytes_per_second");
			this.setTxWifiBps((float)tx_wifi_bps);
			txWifiBpsArray.add((Double)tx_wifi_bps);
		}
		
		if(ue.has("rx_wifi_bytes_per_second") && !ue.isNull("rx_wifi_bytes_per_second") && ue.has("tx_wifi_bytes_per_second") && !ue.isNull("tx_wifi_bytes_per_second")){
			double tx_wifi_bps = ue.getDouble("tx_wifi_bytes_per_second");
			double rx_wifi_bps = ue.getDouble("rx_wifi_bytes_per_second");
			double sum_wifi_bps = tx_wifi_bps + rx_wifi_bps;
			sumtxrxWifiBpsArray.add((Double)sum_wifi_bps);
		}
		
		Calendar cal = Calendar.getInstance();
		timeWifiBpsArray.add(new Second(cal.get(Calendar.SECOND),cal.get(Calendar.MINUTE),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.DATE),cal.get(Calendar.MONTH),cal.get(Calendar.YEAR)));

	}
	
	public ArrayList<Second> getTimeWifiBpsArray() {
		return timeWifiBpsArray;
	}
	
	public ArrayList<Double> getSumtxrxWifiBpsArray() {
		return sumtxrxWifiBpsArray;
	}
	
	public String toString()
	{
		return this.getUri();
	}

}
