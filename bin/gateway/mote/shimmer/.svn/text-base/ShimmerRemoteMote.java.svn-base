package gateway.mote.shimmer;


import gateway.mote.GenericRemoteMote;
import gateway.mote.micaz.MicaZInternalMote;
import gateway.sensor.InternalSensor;
import remote_interfaces.functionality.Functionality;
import remote_interfaces.mote.Mote;
import remote_interfaces.mote.MoteType;
import remote_interfaces.sensor.Sensor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import common.classes.ServerClassLoader;

import common.exceptions.MoteUnreachableException;


public class ShimmerRemoteMote extends GenericRemoteMote 
{
	/*
	 * reference to the InternalMote object hidden
	 * by this MicaZMote
	 */
	private ShimmerInternalMote internalMote;	
	
	/**
	 * Constructor 
	 * 
	 * @param networkAddress
	 * @param macAddress
	 * @throws RemoteException 
	 */
	ShimmerRemoteMote(ShimmerInternalMote internalMote) throws RemoteException
	{
		super(internalMote);
		this.internalMote = internalMote;
		//System.out.println("Creo un generic remote con shimmer");
	}


}