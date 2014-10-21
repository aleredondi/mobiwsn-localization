package gateway.functionality.laura;

import java.rmi.RemoteException;

import javax.measure.Measure;
import javax.measure.quantity.Temperature;
import javax.measure.unit.SI;

import common.result.ValueResultImpl;

import remote_interfaces.result.ValueResult;
import gateway.protocol.FunctionalityMessage;
import Jama.*;

public class LauraMessage extends FunctionalityMessage
{	

	//private boolean laura_rssi_value_is_nxle;
	
	//private final byte DUMMY_SENSOR_ID_POSITION = 0;
	//private final byte DUMMY_SENSOR_VALUE_POSITION = 1;
	
	private final byte LAURA_SYNC_POSITION = 0;
	private final byte LAURA_SYNC_SIZE = 1;
	private final byte LAURA_STATUS_POSITION = 1;
	private final byte LAURA_STATUS_SIZE = 2;
	private final byte LAURA_SENDER_ID_POSITION = 0;
	private final byte LAURA_RSSI_POSITION = 2;
	private final byte LAURA_CELL_SIZE = 3;
	
	// TODO: da rivedere insieme al getPayload (verificare header di serialmsg)
	private final byte HEADERS = 23;
	
	private int msg_size = (this.getMessageLength() - (HEADERS + LAURA_SYNC_SIZE + LAURA_STATUS_SIZE))/LAURA_CELL_SIZE;
	
	public LauraMessage(FunctionalityMessage f_msg)
	{
		super(f_msg.getDataBytes());
		
		//SCRIVI LA LISTA RICEVUTA
		System.out.println(">>LAURA message:");
		for(int i=0; i< (f_msg.getMessageLength() - (HEADERS + LAURA_SYNC_SIZE + LAURA_STATUS_SIZE))/LAURA_CELL_SIZE;i++){
			System.out.println("    NODE ID: " +getLauraSenderId(i)
							+ "; RSSI: " +getLauraRssi(i)
							+ "; SYNC: " +getLauraSync()
							+ "; STATUS: " +getLauraStatus());
		}
		System.out.println("");
	}
	
	/*
	 * LAURAFunctionality message structure:
	 * 
	 * sync:	1 byte;   
	 * status:	2 byte;
	 * row:		3*(1+MAX_CHILD_PER_NODE+MAX_NEIGHBOUR_ENTRIES);
	 * 
	 * typedef nx_struct{
	 *   nx_uint8_t sync;
	 *   nx_uint16_t status;
	 *	 rssi_cell_t row[1+MAX_CHILD_PER_NODE + MAX_NEIGHBOUR_ENTRIES];
	 * }rssi_topan_msg_t;
	 *
	 *    Sender ID:	2 byte;
	 *    RSSI:        	1 byte;
	 * 
  	 *	  typedef nx_struct{
	 *	    nx_am_addr_t am_addr;
	 *	    nx_int8_t rssi;
	 *	  }rssi_cell_t;
	 *
	 */
	
	 
	public short getLauraSync(){
		return (short)(0xFF & (int)(data_byte[super.getPayload() + LAURA_SYNC_POSITION]));
	}
	
	public short getLauraStatus(){
		return getShortField(super.getPayload()+LAURA_STATUS_POSITION, false);
		//return Status.convert((short)(0xFF & (int)(data_byte[super.getPayload() + LAURA_STATUS_POSITION])));
	}
	
	public short getLauraSenderId(int index){
		return getShortField(super.getPayload()+ LAURA_SYNC_SIZE + LAURA_STATUS_SIZE + (LAURA_SENDER_ID_POSITION+index)*LAURA_CELL_SIZE,false);
	}
	
	public byte getLauraRssi(int index){
		return data_byte[super.getPayload()+ LAURA_SYNC_SIZE + LAURA_STATUS_SIZE + (LAURA_SENDER_ID_POSITION+index)*LAURA_CELL_SIZE + LAURA_RSSI_POSITION];
	}
	
	public Matrix getLauraRssiList(){
		Matrix list = new Matrix(msg_size,2);
		for(int i=0;i<msg_size;i++){
			list.set(i, 0, getLauraSenderId(i));
			list.set(i, 1, getLauraRssi(i));
		}
		return list;
	}
	

	

}