package gateway.services;

public interface InternalSubscriber<WHO,WHAT> {
		  public void update(WHO pub, WHAT code);
}
