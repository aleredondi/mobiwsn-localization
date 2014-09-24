package client_applications.localization;

import java.util.ArrayList;

import client_applications.localization.graphics.LauraMainWindow;

public class SmallCellPowerModel {

	private final float POWER_SLEEP = 2.0F;
	private final float POWER_NOLOAD = 4.0F;
	private final float POWER_FULLLOAD = 11.0F;
	
	private ArrayList<AccessPoint> ap_list = null;;


	public SmallCellPowerModel(ArrayList<AccessPoint> access_points_list) {
		this.ap_list = access_points_list;
	}
	
	public float getMinPowerConsumption()
	{
		if(ap_list != null)
			return POWER_SLEEP * ap_list.size();
		return 0;
	}
	
	public float getMaxPowerConsumption()
	{
		if(ap_list != null)
			return POWER_FULLLOAD * ap_list.size();
		return 100;
	}
	
	public float getCurrentPowerConsumption()
	{
		if(ap_list != null)
		{
			// TODO use APs network utilization to interpolate between NOLOAD and FULLLOAD
			return POWER_FULLLOAD * getActiveApCount();
		}
		return 0;
	}
	
	private int getActiveApCount()
	{
		int count = 0;
		for(AccessPoint ap : ap_list)
		{
			if (ap.isIs_reachable())
				count++;
		}
		return count;
	}
	
	
	
}
