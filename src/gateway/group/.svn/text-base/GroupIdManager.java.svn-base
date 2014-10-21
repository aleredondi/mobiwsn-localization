package gateway.group;


public final class GroupIdManager {
	
	private static GroupIdManager instance = null;
	private final int MAX_NUM = 254;
	private boolean ids[];
	
	private GroupIdManager() {
		int i;
		
		//System.out.println("IDM Sono il gestore di id");
		ids = new boolean[MAX_NUM];
		
		//System.out.println("IDM Inizializzo idBusy");
		for( i = 0; i < MAX_NUM; i++) {
			//System.out.println("IDM Inizializzo la posizione " + i + " dell'array di identificativi");
			ids[i] = false;
		}
		//System.out.println("IDM Pronto");
	}
	
	
	public static synchronized GroupIdManager getInstance() {
		//System.out.println("IDM Mi chiede un'istanza");
		if ( instance == null ) {
			//System.out.println("IDM Non ce l'ho");
			instance = new GroupIdManager();
			//System.out.println("IDM Ho creato " + instance.toString());
		} else {
			//System.out.println("IDM Ce l'ho gia' " +instance.toString());
		}
	
		return instance;
	}
		
	
	public short getId( GenericInternalGroup g) {
		//System.out.println("IDM Assegno un nuovo id");

		short i = 1;
		
		while( i < MAX_NUM ) {
			//System.out.println("IDM scorro la lista " + ids[i]);
			if( !ids[i] ) {
				//System.out.println("IDM assegno l'id " + i);
				ids[i] = true;
				break;
			} else {
				i++;
			}
		}
		
		if ( i >= MAX_NUM) {
			//System.out.println("Non ho id da assgnare");
			i = 0;
		}
		return i;
	}
	
	public void freeId( short id ) {
			ids[id] = false;
	}
	
	public boolean isAssigned( short id ) {
			if( ids[id] && id > 0 )
				return true;
			else
				return false;
	}

}
