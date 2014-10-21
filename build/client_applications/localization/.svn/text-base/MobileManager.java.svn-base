package client_applications.localization;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import client_applications.localization.graphics.LauraMainWindow;



public class MobileManager {
	private final int MAX_MSG = 5;
	ArrayList<MobileNode> mobile_list;
	MobileChecker mobile_checker;
	LauraMainWindow lmw;
	
	public MobileManager(ArrayList<MobileNode> mobile_list, LauraMainWindow lmw){
		this.mobile_list = mobile_list;
		mobile_checker = new MobileChecker(1000);
		this.lmw = lmw;
	}
	
	public void decMobileMsgCounter(MobileNode mobile){
		mobile.setMsg_counter(mobile.getMsg_counter()-1);
	}
	
	public void resetMobileMsgCounter(MobileNode mobile){
		mobile.setMsg_counter(MAX_MSG);
		mobile.setIs_reachable(true);
	}
	
	public final class MobileChecker extends TimerTask{
		
		public MobileChecker(long period){
			Timer timer = new Timer();
			timer.schedule(this, 1000, period);
		}
		
		public void run() {
			
			for(int i=0;i<mobile_list.size();i++){
				decMobileMsgCounter(mobile_list.get(i));
				if(mobile_list.get(i).getMsg_counter()==0){
					mobile_list.get(i).setIs_reachable(false);
					lmw.repaint();
				}
			}
			
		}
	}

}

