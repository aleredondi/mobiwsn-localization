package gateway.sensor.acceleration;

import gateway.sensor.GenericRemoteSensor;
import gateway.sensor.InternalSensor;

import java.rmi.RemoteException;

import javax.measure.quantity.Dimensionless;

import remote_interfaces.result.ValueResult;

public class AccelerationXRemoteSensor extends GenericRemoteSensor<ValueResult, Double, Dimensionless> {
	protected AccelerationXRemoteSensor(InternalSensor sensor) throws RemoteException {
		super(sensor);
		// TODO Auto-generated constructor stub
	}
}
