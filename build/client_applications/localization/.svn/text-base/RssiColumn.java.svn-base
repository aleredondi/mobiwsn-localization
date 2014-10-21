package client_applications.localization;
import java.util.*;

import Jama.*;

public class RssiColumn {
	
	private String id;
	protected ArrayList<RssiCell> col = new ArrayList();
	
	public RssiColumn(Matrix rssi_list, String id){
		this.id = id;
		for(int i=0;i<rssi_list.getRowDimension();i++){
			col.add(new RssiCell((int)rssi_list.get(i, 0),(int)rssi_list.get(i, 1)));
		}
	}
	
	public void update(Matrix rssi_list){
		int j;
		 
		for(int i=0;i<rssi_list.getRowDimension();i++){
			//per ogni mac guardo: se lo avevo di già lo aggiorno, se no lo aggiungo.
			for(j=0;j<col.size();j++){
				RssiCell curr_cell = col.get(j);
				if(curr_cell.getMac() == rssi_list.get(i, 0)){
					curr_cell.setRssi((int)rssi_list.get(i,1));
				}
			}
			
			//questo vuol dire che non l'ho trovato, quindi lo devo aggiungere
			if(j==col.size()){
				col.add(new RssiCell((int)rssi_list.get(i, 0),(int)rssi_list.get(i, 1)));
			}
		}
		
		//qua dovrei controllare la sincronizzazione...

	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public int getMac(){
		int ind,ind2;
		ind = id.indexOf(".");
		String sub_id = id.substring(ind+1);
		ind2 = sub_id.indexOf(".");
		return Integer.parseInt(sub_id.substring(ind2+1));	
	}

}
