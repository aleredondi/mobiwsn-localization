
package gateway.functionality;


import remote_interfaces.functionality.FunctionalityType;
import gateway.functionality.dummy.DummyInternalFunctionality;
import gateway.functionality.dummy_presence.DummyPresenceInternalFunctionality;
import gateway.functionality.presence.PresenceInternalFunctionality;
import gateway.functionality.laura.LauraInternalFunctionality;
import gateway.mote.InternalMote;


public class FunctionalityFactory {

	public static InternalFunctionality createFunctionalityInstance(
									byte func_id,
									FunctionalityType func_type,
									InternalMote mote ) 			
	{
		
	System.out.println("New functionality created: ");
	System.out.println("    Id = " + func_id);
	System.out.println("    Type = " + func_type.name());
	System.out.println("------------------------------------------------------------\n");
	
		
	switch (func_type)
	{
	
			case DUMMY :
				return new DummyInternalFunctionality(func_id, mote );

			case PRESENCE :	
				return new PresenceInternalFunctionality(func_id, mote );
				
			case DUMMYPRESENCE :
				return new DummyPresenceInternalFunctionality(func_id, mote);
				
			case LAURA :
				return new LauraInternalFunctionality(func_id, mote);
							
			default:
				return null;	
	}
	
	}
	
}
