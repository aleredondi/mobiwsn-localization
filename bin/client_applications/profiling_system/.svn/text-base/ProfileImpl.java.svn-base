package client_applications.profiling_system;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import remote_interfaces.clients.profiling_system.*;
import remote_interfaces.clients.profiling_system.*;

public class ProfileImpl extends UnicastRemoteObject implements Profile {

	String room;
	double[] profile;
	ProfileType type;
	
	public ProfileImpl( String room, double[] profile, ProfileType type ) throws RemoteException {
		super();
		
		this.room = room;
		this.profile = profile;
		this.type = type;
	}

	public String getRoom() throws RemoteException {
		return room;
	}
	
	public ProfileType getType() throws RemoteException {
		return type;
	}

	public double getValue(int i) throws RemoteException {
		if ( i >= 0 && i < profile.length )
			return profile[i];
		else
			return -1;
	}

	public double[] getValues() throws RemoteException {
		return profile;
	}
	
	public void setProfile(double[] a){
		profile=new double[a.length];
		profile=a;
	}

}
