package client_applications.localization;

import java.util.ArrayList;

import client_applications.localization.graphics.LauraMainWindow;

public class SmallCellPowerModel {

	/**
	 * Configuration values.
	 */
	/*
	// power model values (old)
	private final float POWER_SLEEP = 2.0F;
	private final float POWER_NOLOAD = 4.0F;
	private final float POWER_FULLLOAD = 11.0F;
	*/
	// power model values (based on Doc2a page 21)
	private final float POWER_SLEEP = 0.2F; // sleep 4 from doc2a
	private final float POWER_NOLOAD = 2.3F;
	private final float POWER_FULLLOAD = 6.9F;
	// number of bytes per second on full load (rx + tx)
	// TODO: not sure which value is best to use here, e.g.: 54000000 = 54Mbits 
	private final float AP_MAX_BYTES_PER_SECOND = 5000000;

	/**
	 * List containing latest AP data, fetched from the backend
	 */
	private ArrayList<AccessPoint> ap_list = null;

	public SmallCellPowerModel(ArrayList<AccessPoint> access_points_list) {
		this.ap_list = access_points_list;
	}

	/**
	 * Calculate the maximum power consumption. This is the scenario where all
	 * APs are active and under full load.
	 * 
	 * @return
	 */
	public float getMinPowerConsumption() {
		if (ap_list != null)
			return POWER_SLEEP * ap_list.size();
		return 0;
	}

	/**
	 * Calculate the minimum power consumption. This is the scenario where all
	 * APs are sleeping.
	 * 
	 * @return
	 */
	public float getMaxPowerConsumption() {
		if (ap_list != null)
			return POWER_FULLLOAD * ap_list.size();
		return 100;
	}

	/**
	 * Calculate the current power consumption of all APs based on the current
	 * AP model.
	 * 
	 * @return
	 */
	public float getCurrentPowerConsumption() {
		float total_power = 0;
		if (ap_list != null) {
			for (AccessPoint ap : ap_list)
				total_power += getCurrentPowerConsumptionOfAp(ap);
		}
		//System.out.println("POWER: " + total_power);
		return total_power;
	}
	
	
	/**
	 * Return minimum power consumption of given AP.
	 * Typically, constant: POWER_SLEEP
	 * @return
	 */
	public float getMinPowerConsumptionOfAp(AccessPoint ap) {
		return POWER_SLEEP;
	}

	/**
	 * Return minimum power consumption of given AP.
	 * Typically, constant: POWER_FULLLOAD
	 * @return
	 */
	public float getMaxPowerConsumptionOfAp(AccessPoint ap) {
		return POWER_FULLLOAD;
	}

	/**
	 * Calculate the current power consumption for a given AP.
	 * 
	 * @param ap
	 * @return
	 */
	public float getCurrentPowerConsumptionOfAp(AccessPoint ap) {
		// AP is deactivated: return sleep power
		if (!ap.isIs_reachable())
			return POWER_SLEEP;

		// AP is active: interpolate between NO and FULL load power consumption
		float total_bytes_per_second = ap.getRx_bytes_per_second()
				+ ap.getTx_bytes_per_second();
		return POWER_NOLOAD
				+ (POWER_FULLLOAD - POWER_NOLOAD)
				* Math.min(1.0F,
						(total_bytes_per_second / AP_MAX_BYTES_PER_SECOND));
	}

	/**
	 * Return number of APs with 'power_state' != 0
	 * 
	 * @return
	 */
	private int getActiveApCount() {
		int count = 0;
		for (AccessPoint ap : ap_list) {
			if (ap.isIs_reachable())
				count++;
		}
		return count;
	}

}
