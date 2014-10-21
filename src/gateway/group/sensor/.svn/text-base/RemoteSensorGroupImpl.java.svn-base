package gateway.group.sensor;

import java.rmi.RemoteException;

import gateway.group.GenericInternalGroup;
import gateway.group.GenericRemoteGroup;
import gateway.sensor.InternalSensor;
import remote_interfaces.group.Group;
import remote_interfaces.sensor.Sensor;
import remote_interfaces.sensor.SensorGroup;

public class RemoteSensorGroupImpl extends GenericRemoteGroup<Sensor> implements SensorGroup {

	protected RemoteSensorGroupImpl(GenericInternalGroup<InternalSensor, Sensor> internalGroup)
			throws RemoteException {
		super(internalGroup);
	}

}
