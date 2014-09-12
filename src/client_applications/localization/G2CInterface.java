package client_applications.localization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.*;

@SuppressWarnings("deprecation")
public class G2CInterface {
	private HttpClient client;
	private String base_url;

	public G2CInterface(String url){
		client = new DefaultHttpClient();
		this.base_url = url;
	}

	public void setAPIBaseUrl(String url){
		this.base_url = url;
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

	public static void main(String[] args) throws ClientProtocolException, IOException, JSONException {
		
	}

}
