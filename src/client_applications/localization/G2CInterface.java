package client_applications.localization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.*;

import client_applications.localization.graphics.LauraMainWindow;

@SuppressWarnings("deprecation")
public class G2CInterface {
	private String base_url;
	APChecker ap_checker;
	UEChecker ue_checker;
	AlgoChecker algo_checker;

	public G2CInterface(String url){
		//client_ap = new DefaultHttpClient();
		//client_ue = new DefaultHttpClient();
		this.base_url = url;
	}

	public void setAPIBaseUrl(String url){
		this.base_url = url;
	}
	
	public void updateUeStates(ArrayList<UserEquipment> ueList)
			throws ClientProtocolException, IOException, JSONException {
		HttpClient client_ue;
		if(ueList == null)
		{
			ueList = new ArrayList<UserEquipment>();
		}
		client_ue = new DefaultHttpClient();	
		HttpGet request = new HttpGet(base_url + "/api/ue");
		HttpResponse response = client_ue.execute(request);
		String json = EntityUtils.toString(response.getEntity());
		JSONArray ues = new JSONArray(json);
		ArrayList<String> ue_uris = new ArrayList<String>();
		for (int i = 0; i < ues.length(); i++) {
			ue_uris.add(ues.getString(i));
		}
		
		ArrayList<UserEquipment> removedUe = new ArrayList<UserEquipment>();
		// remove all UEs that are not in backend anymore
		for (UserEquipment local_ue : ueList)
		{
			if(!ue_uris.contains(local_ue.getUri()))
				removedUe.add(local_ue);
		}
		for(UserEquipment rue : removedUe)
			ueList.remove(rue);
		
		// get detailed information about all UEs that are present in backend
		for (String uri : ue_uris) {
			request = new HttpGet(base_url + uri);
			response = client_ue.execute(request);
			json = EntityUtils.toString(response.getEntity());
			JSONObject ue = new JSONObject(json);
			
			// if UE is new, add it to the list
			if(getUeByUri(ueList, uri) == null)
			{
				ueList.add(new UserEquipment(ue.getString("uuid"), ue.getString("uri")));
			}
			
			// update all UE objects with latest json values
			UserEquipment ue_local = getUeByUri(ueList, uri);
			ue_local.updeteUe(ue);
			
			//System.out.println("Fetched: " + ue_local + " @ " + ueList.size());
		}
	}
	
	private UserEquipment getUeByUri(ArrayList<UserEquipment> list, String uri)
	{
		for(UserEquipment ue : list)
		{
			if(ue.getUri().equals(uri))
				return ue;
		}
		return null;
	}
	
	public void updateAccessPointsStatus(ArrayList<AccessPoint> access_points) throws ClientProtocolException, IOException, JSONException{
		HttpClient client_ap;
		client_ap = new DefaultHttpClient();
		HttpGet request = new HttpGet(base_url + "/api/accesspoint");
		HttpResponse response = client_ap.execute(request);
		String json = EntityUtils.toString(response.getEntity());
		JSONArray aps;
		aps = new JSONArray(json);
		
		for(int i=0;i<aps.length();i++){
			request = new HttpGet(base_url + aps.getString(i));
			response = client_ap.execute(request);
			json = EntityUtils.toString(response.getEntity());
			JSONObject ap = new JSONObject(json);

			boolean reachable;
			if(ap.getInt("power_state")==0)
				reachable = false;
			else
				reachable = true;
			
			long rx_bytes, tx_bytes;
			float rx_bytes_per_second, tx_bytes_per_second;
			try{
				rx_bytes = ap.getLong("rx_bytes");
				tx_bytes = ap.getLong("tx_bytes");
				rx_bytes_per_second = (float)ap.getDouble("rx_bytes_per_second");
				tx_bytes_per_second = (float)ap.getDouble("tx_bytes_per_second");
			} catch(Exception e) {
				rx_bytes = 0;
				tx_bytes = 0;
				rx_bytes_per_second = 0;
				tx_bytes_per_second = 0;
				System.err.println(e.getMessage());
			}
			
			//look into the access_point vector and update the state
			for(int j=0;j<access_points.size();j++){
				if(access_points.get(j).getId() == null)
					continue;
				if(access_points.get(j).getId().equals(ap.getString("device_id")))
				{
					access_points.get(j).setIs_reachable(reachable);
					access_points.get(j).setRx_bytes(rx_bytes);
					access_points.get(j).setTx_bytes(tx_bytes);
					access_points.get(j).setRx_bytes_per_second(rx_bytes_per_second);
					access_points.get(j).setTx_bytes_per_second(tx_bytes_per_second);
					access_points.get(j).setTime_bytes_per_second();
				}
			}
		}
	}
	
	

	public ArrayList<AccessPoint> getAccessPoints() throws JSONException, ParseException, IOException{
		HttpClient client_ap;
		client_ap = new DefaultHttpClient();
		ArrayList<AccessPoint> access_points = new ArrayList<AccessPoint>();
		HttpGet request = new HttpGet(base_url + "/api/accesspoint");
		HttpResponse response = client_ap.execute(request);
		String json = EntityUtils.toString(response.getEntity());
		JSONArray aps;
		aps = new JSONArray(json);
		for(int i=0;i<aps.length();i++){
			request = new HttpGet(base_url + aps.getString(i));
			response = client_ap.execute(request);
			json = EntityUtils.toString(response.getEntity());
			JSONObject ap = new JSONObject(json);

			boolean reachable;
			if(ap.getInt("power_state")==0)
				reachable = false;
			else
				reachable = true;

			AccessPoint access_point = new AccessPoint(ap.getString("device_id"),ap.getString("bssid"),"127.0.0.1",ap.getDouble("position_x"),ap.getDouble("position_y"),reachable);
			access_points.add(access_point);
		}
		Collections.sort(access_points);
		return access_points;
	}

	public int updateLocation(String loc_node_id, double x, double y) throws JSONException, ClientProtocolException, IOException{
		HttpClient client_ap;
		client_ap = new DefaultHttpClient();
		HttpPost post = new HttpPost(base_url + "/api/location");
		JSONObject json = new JSONObject();
		json.put("location_service_id", loc_node_id);
		json.put("position_x", x);
		json.put("position_y", y);
		StringEntity input = new StringEntity( json.toString());
		post.setEntity(input);
		HttpResponse response = client_ap.execute(post);
		if(response.getStatusLine().getStatusCode() == 201)
			return 0;
		else
			return -1;
	}
	
	public void updateBackendAlgorithmList(ArrayList<BackendAlgorithm> algorithm_list) throws ClientProtocolException, IOException, JSONException{
		HttpClient client_ap;
		client_ap = new DefaultHttpClient();
		// algorithm list
		HttpGet request = new HttpGet(base_url + "/api/algorithm");
		HttpResponse response = client_ap.execute(request);
		String json = EntityUtils.toString(response.getEntity());
		JSONArray algorithms;
		algorithms = new JSONArray(json);
		// selected algorithm
		HttpGet request2 = new HttpGet(base_url + "/api/algorithm/selected");
		HttpResponse response2 = client_ap.execute(request2);
		String json2 = EntityUtils.toString(response2.getEntity());
		JSONObject selected_dict;
		selected_dict = new JSONObject(json2);
		String selected = selected_dict.getString("selected");
			
		for(int i=0;i<algorithms.length();i++){
			String name = algorithms.getString(i);
			// update local algorithm name vector
			boolean found = false;
			for(BackendAlgorithm ba : algorithm_list)
				if(ba.getName().equals(name))
					found = true;	
			// create new algorithm instance
			if(!found)
				algorithm_list.add(new BackendAlgorithm(name));
		}
		
		// set selected
		for(BackendAlgorithm ba : algorithm_list)
			ba.setSelected(ba.getName().equals(selected));
	}
	
	public int changeBackendAlgorithm(String name)  throws ClientProtocolException, IOException, JSONException
	{
		HttpClient client_ap;
		client_ap = new DefaultHttpClient();
		HttpPut put = new HttpPut(base_url + "/api/algorithm/selected");
		JSONObject json = new JSONObject();
		json.put("selected", name);
		StringEntity input = new StringEntity(json.toString());
		put.setEntity(input);
		HttpResponse response = client_ap.execute(put);
		if(response.getStatusLine().getStatusCode() == 204)
			return 0;
		else
			return -1;
	}

	public static void main(String[] args) throws ClientProtocolException, IOException, JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://127.0.0.1:6680/api/location");
		JSONObject json = new JSONObject();
		json.put("location_service_id", "loc_node1");
		json.put("position_x", 0.0);
		json.put("position_y", 0.0);
		StringEntity input = new StringEntity( json.toString());
		post.setEntity(input);
		HttpResponse response = client.execute(post);

	}

	public void startAPChecker(long period, ArrayList<AccessPoint> access_points_list, LauraMainWindow lmw){
		 ap_checker = new APChecker(period,access_points_list,lmw);
	}
	
	public void startUEChecker(long period, ArrayList<UserEquipment> ue_list, LauraMainWindow lmw){
		 ue_checker = new UEChecker(period,ue_list,lmw);
	}
	
	public void startAlgoChecker(long period, ArrayList<BackendAlgorithm> algorithm_list, LauraMainWindow lmw){
		 algo_checker = new AlgoChecker(period,algorithm_list,lmw);
	}
	
public final class APChecker extends TimerTask{
	
		ArrayList<AccessPoint> access_points_list;
		LauraMainWindow lmw;
		
		public APChecker(long period, ArrayList<AccessPoint> access_points_list, LauraMainWindow lmw){
			Timer timer = new Timer();
			timer.schedule(this, 1000, period);
			this.access_points_list = access_points_list;
			this.lmw = lmw;
		}
		
		public void run() {
			try {
				updateAccessPointsStatus(access_points_list);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lmw.repaint();
			lmw.updatePowerMeterPanel();
			lmw.updateApHistoryPanel();
		}
	}

public final class UEChecker extends TimerTask{
	
	ArrayList<UserEquipment> ue_list;
	LauraMainWindow lmw;
	
	public UEChecker(long period, ArrayList<UserEquipment> ue_list, LauraMainWindow lmw){
		Timer timer = new Timer();
		timer.schedule(this, 1000, period);
		this.ue_list = ue_list;
		this.lmw = lmw;
	}
	
	public void run() {
		try {
			updateUeStates(ue_list);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lmw.updateUeInfoPanel();
	}
}

public final class AlgoChecker extends TimerTask{
	
	ArrayList<BackendAlgorithm> algorithm_list;
	LauraMainWindow lmw;
	
	public AlgoChecker(long period, ArrayList<BackendAlgorithm> algorithm_list, LauraMainWindow lmw){
		Timer timer = new Timer();
		timer.schedule(this, 1000, period);
		this.algorithm_list = algorithm_list;
		this.lmw = lmw;
	}
	
	public void run() {
		try {
			updateBackendAlgorithmList(algorithm_list);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lmw.updateAlgoSelectionMenu();
	}
}

}




