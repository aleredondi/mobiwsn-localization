package gateway.functionality;

import java.rmi.Remote;
import java.rmi.RemoteException;

import gateway.comm.MessageSender;
import gateway.mote.InternalMote;
import gateway.services.GenericInternalPublisher;
import gateway.services.GenericRemotePublisher;
import gateway.services.InternalPublisher;
import gateway.services.InternalSubscriber;

public abstract class GenericInternalPublisherFunctionality extends GenericInternalFunctionality  implements InternalPublisher<GenericInternalPublisherFunctionality,Object> {

	private final GenericInternalPublisher<GenericInternalPublisherFunctionality,Object> myPublisher;
	
	protected GenericRemotePublisherFunctionality remote_func;

	
	protected GenericInternalPublisherFunctionality(byte func_id,
			InternalMote m) {
		super(func_id, m);
		myPublisher = new GenericInternalPublisher<GenericInternalPublisherFunctionality,Object>();
		//remote_func =(GenericRemotePublisherFunctionality<WHAT>) getRemoteFunctionality();		
	}
	
	  public void addSubscriber(InternalSubscriber<GenericInternalPublisherFunctionality,Object> s){
		  myPublisher.addSubscriber(s);
	  }

	  public void removeSubscriber(InternalSubscriber<GenericInternalPublisherFunctionality,Object> s) {
		  myPublisher.removeSubscriber(s);
	  }
		

		protected void notifySubscribers(GenericInternalPublisherFunctionality pub, Object code) { 
			myPublisher.notifySubscribers(pub, code);
			try {
				remote_func.notifySubscribers(code);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		protected void setRemote(GenericRemotePublisherFunctionality func) {
			super.setRemote(func);
			remote_func = func;
		}


}
