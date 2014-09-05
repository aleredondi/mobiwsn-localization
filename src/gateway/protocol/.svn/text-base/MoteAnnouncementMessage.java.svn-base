
package gateway.protocol;


import remote_interfaces.functionality.FunctionalityType;
import remote_interfaces.sensor.SensorType;

import gateway.protocol.address.InternalNetworkAddressImpl;
import gateway.protocol.basic_message.MiddlewareMessage;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import common.exceptions.ExceptionHandler;


/**
 * @authors Davide Roveran Luca Pietro Borsani
 *
 * this message is sent by a mote in a WSN when a network_discovery message 
 * is received. This message is also sent unsolicited when a mote joins to 
 * a WSN.
 */
public class MoteAnnouncementMessage extends MiddlewareMessage 
{	
	
	private boolean mote_nwk_addr_is_nxle;
	private boolean mote_mac_addr_id_is_nxle;
	private boolean parent_mac_addr_is_nxle;
	private boolean[] sensor_type_is_nxle;
	private boolean[] functionality_type_is_nxle;
	
	private final byte MOTE_NWK_ADRR_POSITION = 0;
	private final byte MOTE_MAC_ADRR_POSITION = 2;
	private final byte PARENT_MAC_ADRR_POSITION = 4;
	private final byte SENSOR_NUMBER_POSITION = 6;
	private final byte GROUP_NUMBER_POSITION = 7;
	private final byte FUNCTIONALITY_NUMBER_POSITION = 8;
	private final byte SENSOR_DESCRIPTOR_POSITION = 9;
	
	private final byte MAX_SENSOR_NUMBER = 5; 
	private final byte MAX_GROUP_NUMBER = 5; 
	private final byte MAX_FUNCTIONALITY_NUMBER = 5; 
	
	public MoteAnnouncementMessage(byte[] data)
	{
		super(data);
		
		mote_nwk_addr_is_nxle = true;
		mote_mac_addr_id_is_nxle = false;
		parent_mac_addr_is_nxle = false;
		
		sensor_type_is_nxle = new boolean[getSensorsNumber()];
		
		for (int i = 0; i<getSensorsNumber(); i++)
			sensor_type_is_nxle[i] = false;
		
		functionality_type_is_nxle = new boolean[getFunctionalitiesNumber()];
		
		for (int j = 0; j<getFunctionalitiesNumber(); j++)
			functionality_type_is_nxle[j] = false;
				
		System.out.println(">MoteAnnouncement message:");
		System.out.println("      Network Address: " + getMoteNetworkAddress() 
				+ " ; Mac Address: " + getMoteMACAddress() 
				+ " ; Parent Mac Address: " + getParentMACAddress());
		
		System.out.println("      Number of sensor installed : " + getSensorsNumber()
				+ " ; Number of its groups : " + getGroupsNumber()
				+ " ; Number of functionalities installed : " + getFunctionalitiesNumber());
		
		for (int k =0; k < getSensorsNumber(); k++) 			
			System.out.println("      Sensor [" + k + "] : type = " + getSensorType(k) + " and id = " + getSensorId(k));	
		
		for (int h =0; h < getGroupsNumber(); h++)
			System.out.println("      Group [" + h + "] : id = " + getGroupId(h));
		
		for (int w =0; w < getFunctionalitiesNumber(); w++)		
			System.out.println("      Functionality [" + w + "] : type = " + getFunctionalityType(w) + 
					" and id = " + getFunctionalityId(w));
		
		System.out.println("");

	}
	
	/* 
	 * Mote announcement message structure:
	 * 
	 * Mote Announcement Header:							9 byte;
	 * 
	 * Sensor descriptor[MAX_SENSOR_NUMBER]:				3 byte * MAX_SENSOR_NUMBER;
	 * Group descriptor[MAX_GROUP_NUMBER]:					1 byte * MAX_GROUP_NUMBER;
	 * Functionality descriptor[MAX_FUNCTIONALITY_NUMBER]:	3 byte * MAX_FUNCTIONALITY_NUMBER;
	 * 
	 * typedef nx_struct mote_announcement_message_t
	 * {
	 * mote_announcement_header_t							announcement_header;
	 * 
	 * sensor_descriptor_t			    					sensors[MAX_SENSOR_NUMBER];
	 * group_descriptor_t			    					groups[MAX_GROUP_NUMBER];
	 * functionality_descriptor_t							functionalities[MAX_FUNCTIONALITY_NUMBER];
	 * } mote_announcement_message_t;
	 * 
	 * where:
	 * 
	 * 		Mote announcement message header structure:
	 * 
	 *		Mote Network Address:			2 byte;
	 *		Mote Mac Address:				2 byte;
	 *		Mote Parent Mac Address:		2 byte;
	 *		Sensor Number:					1 byte;
	 *		Group Number:					1 byte;
	 *		Functionality Number:			1 byte;
	 * 
	 *		typedef nx_struct mote_announcement_header_t
	 *		{
	 *		nx_network_addr_t				mote_network_address;
	 *		nx_am_addr_t					mote_mac_address;
	 *		nx_am_addr_t					parent_mac_address;
	 *		nx_sensor_number_t				sensors_number;
	 *		nx_group_number_t				groups_number;
	 *		nx_functionality_number_t		functionalities_number;
	 *		} mote_announcement_header_t;
	 * 
	 *		Sensor descriptor structure:
 	 *
 	 *		Sensor Type:		2 byte;
 	 *		Sensor Id:			1 byte;
 	 * 
	 *		typedef nx_struct sensor_descriptor_t
	 *		{
	 *		nx_sensor_type_t	sensor_type;
	 *		nx_sensor_id_t		sensor_id;
	 * 		} sensor_descriptor_t;
	 *    
	 * 		Group descriptor structure:
	 * 
	 *		Group Id:		1 byte;
	 *
	 *		typedef nx_struct group_descriptor_t
	 *		{
	 *		nx_group_id_t	group_id;
	 *		} group_descriptor_t;
	 *
	 *		Functionality descriptor structure:
	 * 
	 *		Functionality Type:			2 byte;
	 *		Functionality Id:			1 byte;
	 *
	 *		typedef nx_struct functionality_descriptor_t
	 *		{
	 *		nx_functionality_type_t		functionality_type;
	 *		nx_functionality_id_t		functionality_id;
	 *		} functionality_descriptor_t;
	 *
	 */
	
	/**
	 * @param mote_nwk_address the mote_nwk_address to set
	 */
	public void setMoteNetworkAddress(short mote_nwk_address) 
	{	
		// nx_network_addr_t mote_network_address;
		setShortField(mote_nwk_address, super.getPayload()+MOTE_NWK_ADRR_POSITION, mote_nwk_addr_is_nxle);
	}

	/**
	 * @return the mote_nwk_address
	 */
	public InternalNetworkAddressImpl getMoteNetworkAddress() 
	{
		return new InternalNetworkAddressImpl( getShortField(super.getPayload()+MOTE_NWK_ADRR_POSITION, mote_nwk_addr_is_nxle) );	
	}

	/**
	 * @param mote_mac_address the mote_mac_address to set
	 */
	public void setMoteMACAddress(short mote_mac_address) 
	{
		// nx_am_addr_t	mote_mac_address;
		setShortField(mote_mac_address, super.getPayload()+MOTE_MAC_ADRR_POSITION, mote_mac_addr_id_is_nxle);
	}

	/**
	 * @return the mote_mac_address
	 */
	public short getMoteMACAddress() 
	{
		return getShortField(super.getPayload()+MOTE_MAC_ADRR_POSITION, mote_mac_addr_id_is_nxle);
	}
	
	/**
	 * 
	 * @param parent_mac_address the parent_mac_address to set
	 */
	public void setParentMACAddress(short parent_mac_address)
	{
		// nx_am_addr_t	parent_mac_address;
		setShortField(parent_mac_address, super.getPayload()+PARENT_MAC_ADRR_POSITION, parent_mac_addr_is_nxle);
	}
	
	/**
	 * 
	 * @return  the parent_mac_address
	 */
	public short getParentMACAddress()
	{
		return getShortField(super.getPayload()+PARENT_MAC_ADRR_POSITION, parent_mac_addr_is_nxle);	
	}
	
	/**
	 * @param new_sensors_number to set
	 */
	public void setSensorsNumber(byte new_sensors_number) 
	{
		if (new_sensors_number < MAX_SENSOR_NUMBER)
		{	
			// nx_sensor_number_t sensors_number;
			data_byte[super.getPayload()+SENSOR_NUMBER_POSITION] = new_sensors_number;
		}
		else
			throw new IllegalArgumentException("every mote announcement message cannot have more than "+ MAX_SENSOR_NUMBER +" sensors! ("+ new_sensors_number +" is too long)");
	}

	/**
	 * @return the sensors_number
	 */
	public byte getSensorsNumber() 
	{
		return data_byte[super.getPayload()+SENSOR_NUMBER_POSITION];
	}
	
	/**
	 * @param new_groups_number to set
	 */
	public void setGroupsNumber(byte new_groups_number) 
	{
		if (new_groups_number < MAX_GROUP_NUMBER)
		{		
			// nx_group_number_t groups_number;
			data_byte[super.getPayload()+GROUP_NUMBER_POSITION] = new_groups_number;
		}
		else
			throw new IllegalArgumentException("every mote announcement message cannot have more than "+ MAX_GROUP_NUMBER +" groups! ("+ new_groups_number +" is too long)");
	}
	
	/**
	 * @return the groups_number
	 */
	public byte getGroupsNumber() 
	{
		return data_byte[super.getPayload()+GROUP_NUMBER_POSITION];
	}
	
	/**
	 * @param new_functionalities_number to set
	 */
	public void setFunctionalitiesNumber(byte new_functionalities_number) 
	{
		if (new_functionalities_number < MAX_FUNCTIONALITY_NUMBER)
		{
			// nx_functionality_number_t functionalities_number;
			data_byte[super.getPayload()+FUNCTIONALITY_NUMBER_POSITION] = new_functionalities_number;
		}
		else
			throw new IllegalArgumentException("every mote announcement message cannot have more than "+ MAX_FUNCTIONALITY_NUMBER +" groups! ("+ new_functionalities_number +" is too long)");			
	}
	
	/**
	 * @return the functionalities_number
	 */
	public byte getFunctionalitiesNumber() 
	{
		return data_byte[super.getPayload()+FUNCTIONALITY_NUMBER_POSITION];
	}
	
	/**
	 * set the sensor_type of the n-th sensor
	 * @param new_sensor_type to set
	 * @param n is sensor pointer
	 */
	public void setSensorType(SensorType new_sensor_type, int n)
	{
		if (n < getSensorsNumber())
		{	
			try
			{		
				DecimalFormat res_form = new DecimalFormat("00");	

				byte[] byte_type = (res_form.format(new_sensor_type.ordinal())).getBytes("ASCII");
				
				if (sensor_type_is_nxle[n])
					byte_type = invertByteArray(byte_type);
				
				int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION + (n * 3);
				
				// nx_sensor_type_t sensors[n].sensor_type;
				data_byte[pos] = byte_type[0];
				pos++;
				data_byte[pos] = byte_type[1];
				
			}
			catch (UnsupportedEncodingException ueex)
			{
				ExceptionHandler.getInstance().handleException(ueex);
			}	
			
			/*
			int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION + (n * 3);
			
			// nx_sensor_type_t sensors[n].sensor_type;
			data_byte[pos] = (byte)new_sensor_type.ordinal();	
			*/		
		}
		else
			throw new IllegalArgumentException("this mote announcement message has only "+ getSensorsNumber() +" sensors! ("+ n +" is too long)");			
	}
	
	/**
	 * @return the sensor_type of the n-th sensor
	 */
	public SensorType getSensorType(int n)
	{
		if (n < getSensorsNumber())
		{			
			int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION + (n * 3);
			
			byte[] sensor_type = new byte[2];	
			System.arraycopy(data_byte, pos, sensor_type, 0, 2);
			
			if (sensor_type_is_nxle[n])
				sensor_type = invertByteArray(sensor_type);
			
			return SensorType.convert(new Integer((new String(sensor_type))).intValue());
			
			/*
			int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION + (n * 3);
					
			return SensorType.convert(data_byte[pos]);
			*/
		}
		else
			throw new IllegalArgumentException("this mote announcement message has only "+ getSensorsNumber() +" sensors! ("+ n +" is too long)");
	}
	
	/**
	 * 
	 * @param set the sensor_id of the n-th functionality
	 */
	public void setSensorId(byte new_sensor_id, int n)
	{
		/*if (new_sensor_id.length() > 2) 
			throw new IllegalArgumentException("a maximum of 2 characters is allowed for sensor identiers!");
		else 
		{*/
			if (n < getSensorsNumber())
			{	
				
				int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION+2 + (n * 3);			
				
				// nx_functionality_id_t functionalies[n].functionality_id;
				data_byte[pos] = new_sensor_id;
				
				/*
				try
				{
					
					byte[] byte_Id = new_sensor_id.getBytes("ASCII");
					
					if (sensor_id_is_nxle[n])
						byte_Id = invertByteArray(byte_Id);
				
					int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION+1 + (n * 3);			
			
					// nx_sensor_id_t sensors[n].sensor_id;
					data_byte[pos] = byte_Id[0];
					pos++;
					data_byte[pos] = byte_Id[1];
					
					
				}
				catch (UnsupportedEncodingException ueex)
				{
					ExceptionHandler.getInstance().handleException(ueex);
				}
				*/
				
			}
			else
				throw new IllegalArgumentException("this mote announcement message has only "+ getSensorsNumber() +" sensors! ("+ n +" is too long)");
		//}
	}
	
	/**
	 * 
	 * @return the sensor_id of the n-th functionality
	 */
	public byte getSensorId(int n)
	{
	
		if (n < getSensorsNumber())
		{		
			
			int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION+2 + (n * 3);			

			return data_byte[pos];
		
			/*
			int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION+1 + (n * 3);

			byte[] sensor_id = new byte[2];
			System.arraycopy(data_byte, pos, sensor_id, 0, 2);
			
			if (sensor_id_is_nxle[n])
				sensor_id = invertByteArray(sensor_id);
			
			return new String(sensor_id);
			*/
			
		}
		else
			throw new IllegalArgumentException("this mote announcement message has only "+ getSensorsNumber() +" sensors! ("+ n +" is too long)");

	}
	
	/**
	 * 
	 * @param set the group_id of the n-th group
	 */
	public void setGroupId(byte new_group_id, int n)
	{
		if (n < getGroupsNumber())
		{			
			int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION + (getSensorsNumber() * 3) + n;

			// nx_group_id_t groups[n].group_id;
			data_byte[pos] = new_group_id;			
		}
		else
			throw new IllegalArgumentException("this mote announcement message has only "+ getGroupsNumber() +" groups! ("+ n +" is too long)");
	}
	
	/**
	 * 
	 * @return the group_id of the n-th group
	 */
	public byte getGroupId(int n)
	{
		if (n < getGroupsNumber())
		{			
			int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION + (getSensorsNumber() * 3) + n;
			
			return data_byte[pos];
			
		}
		else
			throw new IllegalArgumentException("this mote announcement message has only "+ getGroupsNumber() +" groups! ("+ n +" is too long)");

	}
	
	/**
	 * 
	 * @param set the functionality_type of the n-th functionality
	 */
	public void setFunctionalityType(FunctionalityType new_functionality_type, int n)
	{
		if (n < getFunctionalitiesNumber())
		{		
			try
			{		
				DecimalFormat res_form = new DecimalFormat("00");	

				byte[] byte_type = (res_form.format(new_functionality_type.ordinal())).getBytes("ASCII");
				
				if (functionality_type_is_nxle[n])
					byte_type = invertByteArray(byte_type);
				
				int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION + (getSensorsNumber() * 3) + getGroupsNumber() + (n * 3);
				
				// nx_functionality_type_t functionalities[n].functionality_type;
				data_byte[pos] = byte_type[0];
				pos++;
				data_byte[pos] = byte_type[1];
				
			}
			catch (UnsupportedEncodingException ueex)
			{
				ExceptionHandler.getInstance().handleException(ueex);
			}			
			
		}
		else
			throw new IllegalArgumentException("this mote announcement message has only "+ getFunctionalitiesNumber() +" functionalities! ("+ n +" is too long)");	
	}
	
	/**
	 * 
	 * @return the functionality_type of the n-th functionality
	 */
	public FunctionalityType getFunctionalityType(int n)
	{	
		if (n < getFunctionalitiesNumber())
		{			
			int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION + (getSensorsNumber() * 3) + getGroupsNumber() + (n * 3);
			
			byte[] functionalityType = new byte[2];	
			System.arraycopy(data_byte, pos, functionalityType, 0, 2);
			
			if (functionality_type_is_nxle[n])
				functionalityType = invertByteArray(functionalityType);
			
			return FunctionalityType.convert(new Integer((new String(functionalityType))).intValue());

		}
		else
			throw new IllegalArgumentException("this mote announcement message has only "+ getFunctionalitiesNumber() +" functionalities! ("+ n +" is too long)");	
	}
	
	/**
	 * 
	 * @param set the functionality_id of the n-th functionality
	 */
	public void setFunctionalityId(byte new_functionality_id, int n)
	{
		if (n < getFunctionalitiesNumber())
		{				
			int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION+2 + (getSensorsNumber() * 3) + getGroupsNumber() + (n * 3);			
			
			// nx_functionality_id_t functionalies[n].functionality_id;
			data_byte[pos] = new_functionality_id;
				
		}
		else
			throw new IllegalArgumentException("this mote announcement message has only "+ getFunctionalitiesNumber() +" functionalities! ("+ n +" is too long)");	
	}
	
	/**
	 * 
	 * @return the functionality_id of the n-th functionality
	 */
	public byte getFunctionalityId(int n)
	{
		if (n < getFunctionalitiesNumber())
		{						
			int pos = super.getPayload()+SENSOR_DESCRIPTOR_POSITION+2 + (getSensorsNumber() * 3) + getGroupsNumber() + (n * 3);	
			
			//System.out.println("pos = "+ pos);
			
			return data_byte[pos];
		}
		else
			throw new IllegalArgumentException("this mote announcement message has only "+ getFunctionalitiesNumber() +" functionalities! ("+ n +" is too long)");	
	}

	
}
