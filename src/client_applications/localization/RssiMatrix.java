package client_applications.localization;
import java.util.*;

import Jama.*;

public class RssiMatrix {
	private static final int SELF_RSSI = -30;
	private ArrayList<RssiColumn> S;
	
	public RssiMatrix(){
		S = new ArrayList();
	}
	
	public void updateColumn(Matrix rssi_list, String id){
		//guardo se ho già ricevuto liste da quel nodo
		boolean found = false;
		Iterator<RssiColumn> ci = S.iterator();
		while(ci.hasNext()){
			RssiColumn curr_col = ci.next();
			if(curr_col.getId().equals(id)){
				//se c'era già lo aggiorno
				curr_col.update(rssi_list);
				found = true;
			}
		}
		
		//se non avevo ancora ricevuto liste da quel nodo, aggiungo la colonna
		if(!found){
			S.add(new RssiColumn(rssi_list, id));
		}	
	}
	
	//gli passo un array di mac
	public Matrix getSubMatrix(int[] mac){
		Matrix sub_S = new Matrix(mac.length,mac.length,-100);
		
		//metto già le diagonali a -25
		for(int i=0;i<mac.length;i++){
			sub_S.set(i, i, SELF_RSSI);
		}

		for(int i=0;i<mac.length;i++){
			//vado a cercare la colonna relativa a quel mac
			Iterator<RssiColumn> ci = S.iterator();
			while(ci.hasNext()){
				RssiColumn curr_col = ci.next();
				if(mac[i] == curr_col.getMac()){
					//cerco gli rssi relativi ai mac indicati...
					for(int j=0;j<mac.length;j++){
						if(j!=i){ 
							Iterator<RssiCell> cli = curr_col.col.iterator();
							while(cli.hasNext()){
								RssiCell curr_cell = cli.next();
								if(mac[j] == curr_cell.getMac()){
									sub_S.set(j, i, curr_cell.getRssi());
								}
							}
						}
					}
				}
			}
		}
		
		return sub_S;
	}
	
}
