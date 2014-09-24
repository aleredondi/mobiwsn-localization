package client_applications.localization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.*;

import client_applications.localization.graphics.LauraMainWindow;

@SuppressWarnings("deprecation")
public class G2CInterface {
	private HttpClient client;
	private String base_url;
	APChecker ap_checker;

	public G2CInterface(String url){
		client = new DefaultHttpClient();
		this.base_url = url;
	}

	public void setAPIBaseUrl(String url){
		this.base_url = url;
	}
	
	public void updateAccessPointsStatus(ArrayList<AccessPoint> access_points) throws ClientProtocolException, IOException, JSONException{

		HttpGet request = new HttpGet(base_url + "/api/accesspoint");
		HttpResponse response = client.execute(request);
		String json = EntityUtils.toString(response.getEntity());
		JSONArray aps;
		aps = new JSONArray(json);
		
		for(int i=0;i<aps.length();i++){
			request = new HttpGet(base_url + aps.getString(i));
			response = client.execute(request);
			json = EntityUtils.toString(response.getEntity());
			JSONObject ap = new JSONObject(json);

			boolean reachable;
			if(ap.getInt("power_state")==0)
				reachable = false;
			else
				reachable = true;
			
			//look into the access_point vector and update the state
			for(int j=0;j<access_points.size();j++){
				if(access_points.get(j).getId() == null)
					continue;
				if(access_points.get(j).getId().equals(ap.getString("device_id")))
				{
					access_points.get(j).setIs_reachable(reachable);
					//System.out.println("Update match: " + ap.getString("device_id"));
				}
			}
		}
	}
	
	

	public ArrayList<AccessPoint> getAccessPoints() throws JSONException, ParseException, IOException{
		ArrayList<AccessPoint> access_points = new ArrayList<AccessPoint>();
		HttpGet request = new HttpGet(base_url + "/api/accesspoint");
		HttpResponse response = client.execute(request);
		String json = EntityUtils.toString(response.getEntity());
		JSONArray aps;
		aps = new JSONArray(json);
		for(int i=0;i<aps.length();i++){
			request = new HttpGet(base_url + aps.getString(i));
			response = client.execute(request);
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
		return access_points;
	}

	public int updateLocation(String loc_node_id, float x, float y) throws JSONException, ClientProtocolException, IOException{
		HttpPost post = new HttpPost(base_url + "/api/location");
		JSONObject json = new JSONObject();
		json.put("location_service_id", loc_node_id);
		json.put("position_x", x);
		json.put("position_y", y);
		StringEntity input = new StringEntity( json.toString());
		post.setEntity(input);
		HttpResponse response = client.execute(post);
		if(response.getStatusLine().getStatusCode() == 201)
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
		}
	}

}
