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
	
	/**
	 * Calculate the maximum power consumption.
	 * This is the scenario where all APs are active and under full load.
	 * @return
	 */
	public float getMinPowerConsumption()
	{
		if(ap_list != null)
			return POWER_SLEEP * ap_list.size();
		return 0;
	}
	
	/**
	 * Calculate the minimum power consumption.
	 * This is the scenario where all APs are sleeping.
	 * @return
	 */
	public float getMaxPowerConsumption()
	{
		if(ap_list != null)
			return POWER_FULLLOAD * ap_list.size();
		return 100;
	}
	
	/**
	 * Calculate the current power consumption based on the current AP model.
	 * @return
	 */
	public float getCurrentPowerConsumption()
	{
		if(ap_list != null)
		{
			// TODO use APs network utilization to interpolate between NOLOAD and FULLLOAD to make model more realistic
			// other solution: use network data provided by UE interface to do the same job (sum up all traffic of UEs assigned to one AP)
			return POWER_FULLLOAD * getActiveApCount();
		}
		return 0;
	}
	
	/**
	 * Return number of APs with 'power_state' != 0
	 * @return
	 */
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
