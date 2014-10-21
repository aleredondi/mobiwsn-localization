package client_applications.localization;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import client_applications.localization.graphics.LauraMainWindow;

public class AnchorManager {


	private final int MAX_MSG = 2;
	ArrayList<AnchorNode> anchor_list;
	AnchorChecker anchor_checker;
	LauraMainWindow lmw;
	
		public AnchorManager(ArrayList<AnchorNode> anchor_list, LauraMainWindow lmw){
			this.anchor_list = anchor_list;
			anchor_checker = new AnchorChecker(20000);
			this.lmw = lmw;		
		}
		
		public void decAnchorMsgCounter(AnchorNode anchor){
			anchor.setMsg_counter(anchor.getMsg_counter()-1);
		}
		
		public void resetAnchorMsgCounter(AnchorNode anchor){
			//System.out.println("TRUE");
			anchor.setMsg_counter(MAX_MSG);
			anchor.setIs_reachable(true);
			lmw.repaint();
		}
		
		public final class AnchorChecker extends TimerTask{
			
			public AnchorChecker(long period){
				Timer timer = new Timer();
				timer.schedule(this, 1000, period);
			}
			
			public void run() {
				
				for(int i=0;i<anchor_list.size();i++){
					decAnchorMsgCounter(anchor_list.get(i));
					if(anchor_list.get(i).getMsg_counter()==0){
						anchor_list.get(i).setIs_reachable(false);
						//JOptionPane.showMessageDialog(lmw,  "Lost connection with node " + anchor_list.get(i).getId(), "Warning", JOptionPane.WARNING_MESSAGE);
						lmw.repaint();
					}
				}
				
			}
		}
		

	}


