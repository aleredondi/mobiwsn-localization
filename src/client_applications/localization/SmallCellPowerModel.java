package client_applications.localization;

import java.util.ArrayList;

import client_applications.localization.graphics.LauraMainWindow;

public class SmallCellPowerModel {

	private final float POWER_SLEEP = 2.0F;
	private final float POWER_NOLOAD = 4.0F;
	private final float POWER_FULLLOAD = 11.0F;
	
	private ArrayList<AccessPoint> ap_list;


	public SmallCellPowerModel(ArrayList<AccessPoint> access_points_list) {
		this.ap_list = access_points_list;
	}
	
	public float getMinPowerConsumption()
	{
		return POWER_SLEEP * ap_list.size();
	}
	
	public float getMaxPowerConsumption()
	{
		return POWER_FULLLOAD * ap_list.size();
	}
	
	public float getCurrentPowerConsumption()
	{
		return POWER_FULLLOAD * getActiveApCount();
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
