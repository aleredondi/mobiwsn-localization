
package gateway.protocol;


import gateway.protocol.basic_message.*;

import common.exceptions.MiddlewareException;


/**
 * @authors Davide Roveran Luca Pietro Borsani
 *
 * provides static methods used to parse, check for errors
 * and build MiddlewareMessage instances from array of bytes
 * 
 * provides also MiddlewareMessage templates for rapid message building
 */
public class MiddlewareMessageBuilder 
{
	//MiddlewareMessage.getPayload()+MESSAGE_TYPE_POSITION
	//               17             +          8
	public static byte MIDDLEWARE_MESSAGE_TYPE_POS = 25;
	
	public static MiddlewareMessage getMessage(byte[] data_bytes) throws MiddlewareException
	{		
		 /*
		  * try building the message seeking for MessageType field byte
		  */	
		MiddlewareMessageType msg_type = MiddlewareMessageType.convert(data_bytes[MIDDLEWARE_MESSAGE_TYPE_POS]);
				
		System.out.println(">Received a " + msg_type.toString() + " message\n");
		switch (msg_type)
		{
			case MOTE_DISCOVERY:
			case SENSOR_READ:
			case FUNCTIONALITY_COMMAND:
			case MOTE_GROUP_COMMAND:
				throw new MiddlewareException("MiddlewareMessage of type " +
						msg_type.toString() + " cannot be received by sensor network !");
				
			case MOTE_ANNOUNCEMENT:
				/*
				msg = createMoteAnnouncementMessage(data_bytes);
				break;
				*/
				return new MoteAnnouncementMessage(data_bytes);
				
			case MOTE_LOSS:
				/*
				msg = createMoteLossMessage(data_bytes);
				break;
				*/
				return new MoteLossMessage(data_bytes);			
				
			case SENSOR_READ_RESPONSE:
				/*
				msg = createSensorReadResponseMessage(data_bytes);
				break;
				*/
				return new SensorReadResponseMessage(data_bytes);
				
			case FUNCTIONALITY_ANNOUNCEMENT:
			// qui si possono aggiungere eventuali nuovi messaggi di funzionalità
			// generati dalla WSN verso il gateway (momentaneamente esiste solo l'announcement)
				
				/*
				msg = createFunctionalityAnnouncementMessage(data_bytes);				
				break;
				*/
				return new FunctionalityMessage(data_bytes);
				
			case COMMAND_RECEIVED:
				
				return new CommandReceivedMessage(data_bytes);
				
			default:
					throw new MiddlewareException("Received a message from sensor network with MessageType UNKNOWN !");		
		}
		
	}
	
/*
	private static MoteAnnouncementMessage createMoteAnnouncementMessage(byte[] dataBytes)
	{	
	
		MoteAnnouncementMessage maMessage = new MoteAnnouncementMessage();
		
		createHeader(maMessage, dataBytes);
		
	
		 // setting MoteAnnouncement specific fields
		 
		maMessage.setMoteNetworkAddress((short) ((dataBytes[22] <<8) + dataBytes[21]));
		maMessage.setMoteMACAddress((short) ((dataBytes[23] <<8) + dataBytes[24]));
		maMessage.setParentMACAddress((short) ((dataBytes[25] <<8) + dataBytes[26]));
		
		maMessage.setSensorsNumber(dataBytes[27]);
		maMessage.setGroupsNumber(dataBytes[28]);
		maMessage.setFunctionalitiesNumber(dataBytes[29]);
		
		System.out.println("MoteAnnouncement in creazione: Network Address: " + maMessage.getMoteNetworkAddress() 
								+ " ; Mac Address: " + maMessage.getMoteMACAddress() + " ; Parent Mac Address: " 
								 + maMessage.getParentMACAddress() + 
								 " ;\n Number of sensor installed : " + maMessage.getSensorsNumber()
								 + " ; Number of its groups : " + maMessage.getGroupsNumber()
								 + " ; Number of functionalities installed : " + maMessage.getFunctionalitiesNumber());
			
		int new_pos = 30;
		
		for (int iSens =0; iSens < maMessage.getSensorsNumber(); iSens++) {
			
			byte[] sensorId = new byte[2];
						
			maMessage.setSensorType(SensorType.convert((byte) dataBytes[new_pos]), iSens);
			
			new_pos++;
			
			System.arraycopy(dataBytes, new_pos, sensorId, 0, 2);
			maMessage.setSensorId(new String(sensorId), iSens);
			
			new_pos = new_pos + 2;
			
			System.out.println("Sensor [" + iSens + "] : type = " + maMessage.getSensorType(iSens) + 
				" and id = " + maMessage.getSensorId(iSens));		

		}		
			
		for (int iGroup =0; iGroup < maMessage.getGroupsNumber(); iGroup++) {
			
			maMessage.setGroupId(dataBytes[new_pos], iGroup);
			
			new_pos++;
			
			System.out.println("Group [" + iGroup + "] : id = " + maMessage.getGroupId(iGroup));

		}	
		
		for (int iFun =0; iFun < maMessage.getFunctionalitiesNumber(); iFun++) {
					
			byte[] functionalityType = new byte[2];
					
			System.arraycopy(dataBytes, new_pos, functionalityType, 0, 2);
			maMessage.setFunctionalityType(FunctionalityType.convert(new Integer((new String(functionalityType))).intValue()), iFun);
			
			new_pos = new_pos + 2;
			
			maMessage.setFunctionalityId(dataBytes[new_pos], iFun);
			
			new_pos++;
			
			System.out.println("Functionality [" + iFun + "] : type = " + maMessage.getFunctionalityType(iFun) + 
					" and id = " + maMessage.getFunctionalityId(iFun));

		}
		
		System.out.println("MoteAnnouncement creato..");
		return maMessage;
	}
	
	private static MoteLossMessage createMoteLossMessage(byte[] dataBytes)
	{
		MoteLossMessage mlMessage = new MoteLossMessage();
		
		createHeader(mlMessage, dataBytes);
		
		
		 // setting MoteAnnouncement specific fields
		
		mlMessage.setMoteNetworkAddress((short) ((dataBytes[22] <<8) + dataBytes[21]));
		mlMessage.setMoteMACAddress((short) ((dataBytes[23] <<8) + dataBytes[24]));
		
	
		System.out.println("MoteLoss in creazione: Network Address: " + mlMessage.getMoteNetworkAddress() 
								+ " ; Mac Address: " + mlMessage.getMoteMACAddress() );
		
	
		System.out.println("MoteLoss creato..");
		return mlMessage;
	}
	
	
	private static SensorReadResponseMessage createSensorReadResponseMessage(byte[] dataBytes)
	{
		SensorReadResponseMessage srrMessage = new SensorReadResponseMessage();
		
		createHeader(srrMessage, dataBytes);
		
		
		 // setting SensorReadResponse specific fields
		 
		byte[] sensorId = new byte[2];
		
		System.arraycopy(dataBytes, 21, sensorId, 0, 2);
		
		srrMessage.setSensorId(new String(sensorId));

		srrMessage.setSensorValueLen(dataBytes[23]);

		System.out.println("SensorReadResponse in creazione: from Mote: " + srrMessage.getSourceId() +  
								   "; Sensor Id: " + srrMessage.getSensorId() + "; Sensor Value Len: " + srrMessage.getSensorValueLen() + ";");
	
		for (int iSens = 0; iSens < srrMessage.getSensorValueLen(); iSens++) {

			int new_pos = 24 + (iSens*2);
			srrMessage.setSensorValue((short) ((dataBytes[new_pos] <<8) | (dataBytes[new_pos+1] & 0xff)), iSens);
			System.out.println("Sensor Value [" + iSens + "]: " + srrMessage.getSensorValue(iSens));

		}	
			
	
		System.out.println("SensorReadResponse creato..");
		return srrMessage;
	}
	
	private static FunctionalityAnnouncementMessage createFunctionalityAnnouncementMessage(byte[] dataBytes)
	{
	
		FunctionalityAnnouncementMessage faMessage = null;

		FunctionalityType fType = FunctionalityType.convert(dataBytes[21]);
		
		System.out.println("from functionality:" + fType.toString());
		switch (fType)
		{
		
			case DUMMY:
				
				DummyMessage dMessage = new DummyMessage();
				
				//dMessage.setFunctionalityType(fType);
				
				createHeader(dMessage, dataBytes);
				
				 // setting DummyMessage specific fields
				 
				byte[] sensorId = new byte[2];

				System.arraycopy(dataBytes, 22, sensorId, 0, 2);
				
				dMessage.setSensorId(new String(sensorId));
						
				dMessage.setSensorValue((short) ((dataBytes[24] <<8) | (dataBytes[25] & 0xff)));
				
				System.out.println("DummyMessage in creazione:" +
						"from Mote: " + dMessage.getSourceId() +
						"; Sensor Id: " + dMessage.getSensorId() +
						"; Sensor Value: " + dMessage.getSensorValue());
				
				System.out.println("DummyMessage creato.\n");

				return dMessage;
				
			case PRESENCE:
				
				PresenceMessage pMessage = new PresenceMessage();
				
				//pMessage.setFunctionalityType(fType);
				
				createHeader(pMessage, dataBytes);

				 // setting DummyMessage specific field
				 
				pMessage.setPresenceType(dataBytes[22]);
				pMessage.setPersonNumber(dataBytes[23]);
				
				System.out.println("DummyPresenceMessage in creazione:" +
						"; presence type: " + pMessage.getPresenceType() +
						"; person number: " + pMessage.getPersonNumber());
				
				System.out.println("DummyPresenceMessage creato.\n");

				return pMessage;
				
			case DUMMYPRESENCE:
				
				DummyPresenceMessage dpMessage = new DummyPresenceMessage();
				
				//dpMessage.setFunctionalityType(fType);
				
				createHeader(dpMessage, dataBytes);

				 * setting DummyMessage specific field

				dpMessage.setPersonNumber(dataBytes[22]);								
				
				System.out.println("DummyPresenceMessage in creazione:" +
						"; person number: " + dpMessage.getPersonNumber());
				
				System.out.println("DummyPresenceMessage creato.\n");

				return dpMessage;		
			
			default:
				System.out.println("Received a message from functionality:" + fType.toString());
			
				return faMessage;
				
		}
		
	}
	
	
	private static void createHeader(MiddlewareMessage message, byte[] dataBytes)
	{

		byte[] sourceId = new byte[8];
		byte[] destId = new byte[8];
		
		System.arraycopy(dataBytes, 0, sourceId, 0, 8);
		System.arraycopy(dataBytes, 8, destId, 0, 8);
		
		message.setSourceId(new String(sourceId));
		message.setDestinationId(new String(destId));
				
		message.setMessageId((short) ((dataBytes[17] <<8) + dataBytes[18]));
		message.setCorrelationId((short) ((dataBytes[19] <<8) + dataBytes[20]));
		
		System.out.println("header creato: Source ID: " + message.getSourceId()+ "; Destination Id: " + message.getDestinationId() + 
									"; Message Id: " + message.getMessageId()+"; Corrrelation Id: "+ message.getCorrelationId());

	}
*/

}
