
package gateway.functionality.presence;

import gateway.functionality.GenericInternalPublisherFunctionality;
import gateway.group.mote.InternalMoteGroup;
import gateway.mote.InternalMote;
import gateway.protocol.FunctionalityMessage;

import java.rmi.RemoteException;

import java.util.ArrayList;

import remote_interfaces.functionality.FunctionalityType;


/**
 * @author Borsani Luca Pietro
 * 
 */
public class PresenceInternalFunctionality extends GenericInternalPublisherFunctionality{

	private InternalMoteGroup my_room;
	
	private ArrayList<InternalMoteGroup> near_rooms; // conterranno i gruppi che rappresentano le stanze adiacenti

	public PresenceInternalFunctionality(byte func_id, InternalMote m)
	{
		super(func_id, m );
		this.functionalityType = FunctionalityType.PRESENCE;

		this.my_room = null;		
		
		this.near_rooms = null;
		
		//System.out.println("Sono una presence func");

	}

	@Override
	protected byte[] setStartFunctionalityPayload(ArrayList<Object> input) {
		
		/*
		 * PresenceStart message structure:
		 *
		 * Presence delay timer				1 byte;
		 * My Room: 						1 byte;
		 * Rooms Number: 					1 byte;
		 * Near Rooms List:   				1 byte * MAX_NEAR_ROOMS;
		 * Master Network Address of Rooms: 2 byte * MAX_NEAR_ROOMS;
		 *
		 * typedef nx_struct 
		 * {
		 *  nx_group_id_t       my_room;
		 *  nx_uint8_t          rooms_number;
		 *  nx_group_id_t       near_room_list[MAX_NEAR_ROOMS];
		 *  nx_network_addr_t   nwk_addr_master_room[MAX_NEAR_ROOMS];
		 * } presence_start_message_t;
		 * 
		 */
		
		final int PRESENCE_DELAY_TIMER_SIZE=1;
		final int MY_ROOM_SIZE=1;
		final int ROOMS_NUMBER_SIZE=1;
		final int NEAR_ROOM_LIST_SIZE=1;
		//Lunghezza dell'identificativo di gruppo in byte
		final int GROUP_SIZE=1;
		//Lunghezza dell'indirizzo di rete in byte	
		final int NWK_ADDR_SIZE=2;
		
		if (input == null)
			return null;
			
		else
		{		
			// 3 byte per ogni stanza vicina + 1 per esplicitare il numero di vicini
			// + 1 per indicare la propria stanza
			byte[] start_payload = new byte[ (input.size() - 2) * (GROUP_SIZE + NWK_ADDR_SIZE ) 
			                                 + ( PRESENCE_DELAY_TIMER_SIZE + MY_ROOM_SIZE + ROOMS_NUMBER_SIZE ) ]; 
			
			my_room = owner.getWsn().getMoteGroup((String) input.get(1));
			System.out.println("Prendo la stanza " + input.get(1) + " che e' " + my_room.getName());
			
			final int PRESENCE_TIMER_POS = 0;
			final int MY_ROOM_POS = PRESENCE_TIMER_POS + PRESENCE_DELAY_TIMER_SIZE;
			final int ROOMS_NUMBER_POS = MY_ROOM_POS + MY_ROOM_SIZE;
			final int FIRST_NEAR_ROOM_POS = ROOMS_NUMBER_POS + ROOMS_NUMBER_SIZE;
			

			System.out.println("Scrivo nel byte " + PRESENCE_TIMER_POS + " il valore " + (byte) ((Integer) input.get(0)).byteValue());
			start_payload[PRESENCE_TIMER_POS] = (byte) ((Integer) input.get(0)).byteValue();
			
			System.out.println("Scrivo sul byte " + MY_ROOM_POS + " con l'id della stanza " + my_room.getId());
			start_payload[MY_ROOM_POS] = (byte) my_room.getId();
					
			System.out.println("Scrivo sul byte " + ROOMS_NUMBER_POS + " il numero di vicini " + (byte) (input.size()-2));
			//- 2 perche' il primo campo dell'array e' il tempo e il secondo e' il gruppo della mia stanza
			start_payload[ROOMS_NUMBER_POS] = (byte) (input.size()-2);
			
			if(start_payload[ROOMS_NUMBER_POS] == 0)
			{
				System.out.println("NON ci sono stanze adiacenti in cui controllare la presenza.");
				//in pratica si suppone che le persone non si possano fermare immobili in questa stanza
				// (per esempio l'anticamera d'ingresso)
			}
			else
			{
				System.out.println("Ci sono " + start_payload[ROOMS_NUMBER_POS] + " stanze adiacenti : ");	
						
				near_rooms = new ArrayList<InternalMoteGroup>();
			
				/*
				 * it adds a group id for each near room
				 */							
				for (int n = 0; n<input.size()-2; n++)		
				{
					//+ 2 perche' il primo campo dell'array e' il tempo e il secondo e' il gruppo della mia stanza
					near_rooms.add(owner.getWsn().getMoteGroup((String) input.get(n+2)));
					
					start_payload[FIRST_NEAR_ROOM_POS+n] = (byte) near_rooms.get(n).getId();
				}
				
				/*
				 * it adds a group master address for each near room
				 */	
				for (int m = 0; m<near_rooms.size(); m++)
				{
					int pos = FIRST_NEAR_ROOM_POS + near_rooms.size() + (m*NWK_ADDR_SIZE);
					
					short master_addr = (short) near_rooms.get(m).getList().get(0).getNetworkAddress().intValue();

					start_payload[pos] = (byte)( master_addr & 0xFF );
					pos++;
					start_payload[pos] = (byte)( (master_addr & 0xFF00) >> 8 );
				}
				
			}
			
			return start_payload;
		}

	}

	@Override
	protected void createRemote() {
		try {
			super.setRemote( new PresenceRemoteFunctionality(this) );
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void messageReceived(FunctionalityMessage message) {
		System.out.println("\n>...Notifying...\n");
		//notifySubscribers(this, null);
	
		PresenceMessage p_msg = new PresenceMessage(message);

		notifySubscribers(this, p_msg.getPersonNumber());
	}


}