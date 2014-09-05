package gateway.sensor;

import gateway.services.GenericRemotePublisher;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;

import javax.measure.Measure;
import javax.measure.quantity.Duration;
import javax.measure.quantity.Quantity;

import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;
import common.exceptions.ResponseTimeoutException;

import remote_interfaces.WSNGateway;
import remote_interfaces.mote.Mote;
import remote_interfaces.result.ValueResult;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.sensor.SensorType;
import remote_interfaces.services.Publisher;
import remote_interfaces.services.Subscriber;

public abstract class GenericRemoteSensor<RV extends ValueResult, V, Q extends Quantity> extends UnicastRemoteObject implements Sensor<RV, V, Q>, Publisher<Sensor,RV> {

	private InternalSensor<RV,V,Q> sensor;
	private GenericRemotePublisher<Sensor,RV> publisher;
	
	protected GenericRemoteSensor( InternalSensor<RV,V,Q> sensor ) throws RemoteException {
		this.sensor = sensor;
		publisher = new GenericRemotePublisher<Sensor,RV>();
	}
	
	@Override
	public byte getId() throws RemoteException {
		return sensor.getId();
	}

	@Override
	public String getOwnerMoteId() throws RemoteException {
		return sensor.getOwnerMoteId();
	}

	@Override
	public Mote getOwnerMote() throws RemoteException {
		return (Mote) sensor.getOwnerMote().getRemote();
	}

	@Override
	public SensorType getType() throws RemoteException {
		return sensor.getType();
	}

	@Override
	public RV getValue(Measure<Integer, Duration>  maxOldness) throws RemoteException, MiddlewareException, MoteUnreachableException, ResponseTimeoutException {
		return sensor.getValue(maxOldness);
	}

	@Override
	public RV getValue(Calendar  maxOldness) throws RemoteException, MiddlewareException, MoteUnreachableException, ResponseTimeoutException {
		return sensor.getValue(maxOldness);
	}

	@Override
	public Measure<V, Q> getMaxVal() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Measure<V, Q> getMinVal() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getValueLength() throws RemoteException {
		return sensor.getValueLength();
	}

//	@Override
//	public Object readValue() throws RemoteException, MiddlewareException {
//		return sensor.readValue();
//	}

/*	@Override
	public boolean setPeriodicRead(boolean boSet, int milliSecondPeriod)
			throws RemoteException {
		return sensor.setPeriodicRead(boSet, milliSecondPeriod);
	}*/

/*	@Override
	public boolean setReadByThreshold(boolean boSet, byte[] upperThreshold,
			byte[] lowerThreshold) throws RemoteException {
		return sensor.setReadByThreshold(boSet, upperThreshold, lowerThreshold);
	}
*/	
	
	/*Metodi del publisher*/
	@Override
	public void addSubscriber(Subscriber<Sensor, RV> s)
			throws RemoteException {
		publisher.addSubscriber(s);
		
	}

	@Override
	public void removeSubscriber(Subscriber<Sensor, RV> s)
			throws RemoteException {
		publisher.removeSubscriber(s);
		
	}
	

	protected void notifySubscribers(RV code) throws RemoteException {
		publisher.notifySubscribers(this, code);
	}

	@Override
	public String getUniqueId() throws RemoteException {
		return sensor.getUniqueId();
	}
	
	 /**
	 * This method return the gateway with which the mote is connected
	 * @return the name of the Gateway 
	 * @throws RemoteException
	 */
	@Override
	public WSNGateway getWSNGatewayParent() throws RemoteException {
		return sensor.getOwnerMote().getWsn().getGateway().getRemoteGateway();
	}

}
