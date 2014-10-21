package gateway.functionality;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import common.exceptions.ExceptionHandler;
import common.exceptions.FunctionalityException;
import common.exceptions.MiddlewareException;
import common.exceptions.MoteUnreachableException;
import common.exceptions.ResponseTimeoutException;

import gateway.InternalWSNGateway;
import gateway.comm.MessageListener;
import gateway.group.InternalGroup;
import gateway.mote.InternalMote;
import gateway.protocol.FunctionalityMessage;
import remote_interfaces.functionality.Functionality;
import remote_interfaces.functionality.FunctionalityType;

public abstract class GenericInternalFunctionality implements
		InternalFunctionality {

	protected byte functionalityId; //identification of functionality
	protected FunctionalityType functionalityType; //type of functionality
	protected GenericRemoteFunctionality remote_func;
	protected boolean started;
	
	protected InternalMote owner;
	
	private ArrayList<InternalGroup> myGroupList;

	protected GenericInternalFunctionality(byte func_id, InternalMote m ) {
		this.functionalityId = func_id;
		this.functionalityType = FunctionalityType.INDEFINIBLE;
		createRemote(); 
		this.owner = m;
		this.started = false;	
	}
		
	@Override
	public Functionality getRemoteFunctionality() {
		return remote_func;
	}
	
	protected abstract void createRemote();
	
	@Override
	public void startFunctionality(ArrayList<Object> input) throws MiddlewareException, MoteUnreachableException, FunctionalityException
	{
		FunctionalityCommandMessage fcMessage;

		if ( owner.isReachable() ) {
			if ( !started ) {
				
				started = true;
				
				fcMessage = new FunctionalityCommandMessage(this, setStartFunctionalityPayload(input));				

				fcMessage.setCommandType(FunctionalityCommandType.FC_START);
		
				try
				{
					owner.getWsn().sendMessageTask(fcMessage);
				} catch (ResponseTimeoutException rtex)
				{
					ExceptionHandler.getInstance().handleException(rtex);
				}
			} else
				try {
					throw new FunctionalityException("Just started", this.getRemoteFunctionality());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} else throw new MoteUnreachableException( owner.getId(), owner.getNetworkAddress().getRemote(), owner.getMACAddress() );
	
	}
	
	protected abstract byte[] setStartFunctionalityPayload(ArrayList<Object> input);
	
	public void stopFunctionality() throws MiddlewareException, MoteUnreachableException, RemoteException, FunctionalityException
	{
		
		if ( owner.isReachable() ) {
			if ( started ) {
				
				started = false;
				
				FunctionalityCommandMessage fcMessage = new FunctionalityCommandMessage(this, null);
		
				fcMessage.setCommandType(FunctionalityCommandType.FC_STOP);
		
				try
				{
			
					owner.getWsn().sendMessageTask(fcMessage);
				} catch (ResponseTimeoutException rtex){ExceptionHandler.getInstance().handleException(rtex);}
			} else throw new FunctionalityException("Just stopped", this.getRemoteFunctionality());
		} else throw new MoteUnreachableException( owner.getId(), owner.getNetworkAddress().getRemote(), owner.getMACAddress() );
	}

	@Override
	public byte getId() {
		return this.functionalityId;
	}

	@Override
	public String getOwnerMoteId() {
		return this.owner.getId();
	}

	public InternalMote getOwner()
	{
		return this.owner;
	}

	@Override
	public FunctionalityType getType() {
		return this.functionalityType;
	}
	
	@Override
	public boolean isStarted() {
		return started;
	}
	
	protected void setRemote(GenericRemoteFunctionality func) {
		remote_func = func;
	}
	
	@Override
	public String getUniqueId() {
		return (new Short( functionalityId )).toString();
	}

	@Override
	public UnicastRemoteObject getRemote() {
		return remote_func;
	}
	
	@Override
	public boolean addedToGroup( InternalGroup group ) {
		return true;
	}

	@Override
	public boolean removedFromGroup( InternalGroup group ) {
		return true;
	}
	
	@Override
	public boolean isPartOfGroup( InternalGroup group ) {
		return myGroupList.contains(group);
	}
	
	@Override
	public ArrayList<InternalGroup> getMyGroups() {
		return myGroupList;
	}
	
	@Override
	public InternalWSNGateway getWSNGatewayParent() {
		return owner.getWSNGatewayParent();
	}


}
