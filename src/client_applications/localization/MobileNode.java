package client_applications.localization;


import java.awt.Color;
import java.awt.geom.Line2D;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import remote_interfaces.clients.localization.Status;

import remote_interfaces.mote.Mote;

import Jama.*;

public class MobileNode extends LauraNode{
	public int PPM = 25;
	//dati del nodo
	
	//stima col pf
	private double x,y;
	//stima con dg 
	private boolean raw_on, is_reachable;
	private double x_raw, y_raw;
	
	//coordinate da disegnare
	private double x_draw, y_draw, old_x_draw, old_y_draw, new_x_draw, new_y_draw;
	private double T = 0.25;
	private boolean hasEvent = false;
	
	//file di log
	//static PrintStream p_mobile;

	public boolean HasEvent() {
		return hasEvent;
	}


	public void setHasEvent(boolean hasEvent) {
		this.hasEvent = hasEvent;
	}

	private int msg_counter;


	private String id;
	private String patient_id;
	private Color color = new Color(0);
	private Status status;
	private boolean has_accel;


	private final int RSSI_NOT_VALID = -100;
	private final int RSSI_THR = -80;
	private  int N_OF_PARTICLES = 400;
	private final double RESAMPLING_THR = 0.8*Math.log(N_OF_PARTICLES)/Math.log(2);

	//cloud thr
	double cloud_thr = 40;	//rfid -300
	private final double CLOUD_THRESHOLD = Math.pow(10, -cloud_thr);
	
	private final double INITW = 0.0025;
	private final double DRAW_THR = 0.25;
		
	//AR for position and speed
	private double alpha_c = 0.9;
	private double alpha_u = 0.9;
	
	//weight opt
	int weight_opt = 1;
	
	
	
	//STEP SIZE (lateration)
	private final double alpha = 0.1;
	
	//particle filtering
	private boolean pf_is_initialized = false;
	Point2D[] particles = new Point2D[N_OF_PARTICLES];
	Point2D[] particlesPred = new Point2D[N_OF_PARTICLES];
	Point2D[] u_hat = new Point2D[N_OF_PARTICLES];
	Point2D u = new Point2D(0,0);
	Point2D target;
	double[] wpf = new double[N_OF_PARTICLES];
	
	//tracks
	Track track;
	boolean locked_to_track;
	
	//regole di intrusione
	ArrayList<IntrusionRule> intrusion_rules = new ArrayList<IntrusionRule>();
	ArrayList<String> proximity_rules = new ArrayList<String>();
	
	Random rgen = new Random();
	
	//questa sarà da togliere da qua
	private final int NUM_WALLS = 57;
    //laura 57
    //ale 43
    //3piano 56
    //rfid 53
    //rfid2 18

	Point2D walls[][] = new Point2D[2][NUM_WALLS];
	
	public MobileNode(String id, double x, double y, LauraParam param, boolean reachable, boolean has_accel){
		this.x = x;
		this.y = y;
		this.old_x_draw = x;
		this.new_x_draw = x;
		this.old_y_draw = y;
		this.new_y_draw = y;
		this.id = id;
		this.patient_id = id;
		this.alpha_c = param.alpha_c;
		this.alpha_u = param.alpha_u;
		this.cloud_thr = param.cloud_thr;
		this.weight_opt = param.weights;
		this.msg_counter = 1;;
		this.has_accel = has_accel;
		setIs_reachable(reachable);
		
		//demo laura
		//System.out.println("addingRule");
		//intrusion_rules.add(new IntrusionRule(new Point2D(11.5,5.5),new Point2D(15,9),"Stanza Computer"));

//		if(id.equals("MICAzM22")){
//		if(id.equals("MICAzM22")){
//		if(id.equals("MICAzM22")){
//			this.patient_id = "Ale";
//		}
//		if(id.equals("MICAzM23")){
//			this.patient_id = "Luca";
//		}
//		if(id.equals("MICAzM24")){
//			this.patient_id = "Marco";
//		}
//		if(id.equals("MICAzM25")){
//			this.patient_id = "Matteo";
//		}
		buildWalls();
	}
	
	
	
	public MobileNode(String id, double x, double y, LauraParam param, boolean reachable, Mote mote_ref, boolean has_accel){
		this.x = x;
		this.y = y;
		this.old_x_draw = x;
		this.new_x_draw = x;
		this.old_y_draw = y;
		this.new_y_draw = y;
		this.id = id;
		this.patient_id = id;
		this.alpha_c = param.alpha_c;
		this.alpha_u = param.alpha_u;
		this.cloud_thr = cloud_thr;
		this.weight_opt = param.weights;
		this.msg_counter = 1;
		this.mote_ref = mote_ref;
		this.has_accel = has_accel;
		this.status = Status.NO_CLASSIFICATION;
		setIs_reachable(reachable);

/*
		FileOutputStream fos_mobile = null;
		try {
			fos_mobile = new FileOutputStream("node"+ mote_ref.getMACAddress() +".txt",true);		
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (RemoteException eR) {
			eR.printStackTrace();
		}
		p_mobile = new PrintStream(fos_mobile);
*/

		//System.out.println("addingRule");

		//demo rfid2
//		intrusion_rules.add(new IntrusionRule(new Point2D(0.5,1),new Point2D(6,10),"Pallet Rotativo"));
//		intrusion_rules.add(new IntrusionRule(new Point2D(16,20),new Point2D(25,24),"Macchinari pericolosi"));
		
		//demo laura
		//intrusion_rules.add(new IntrusionRule(new Point2D(11.5,5.5),new Point2D(15,9),"Stanza Computer"));

//		if(id.equals("IRIS.M.15")){
//			this.patient_id = "Carrello";
//		}
//		if(id.equals("SHIMMER.M.16")){
//			this.patient_id = "Persona";
//		}

		buildWalls();
	}
	
	public String getStatus(){
		return status.name();
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public double getXRaw(){
		return this.x_raw;
	}
	
	public double getYRaw(){
		return this.y_raw;
	}
	
	public double getX_draw() {
//		// store value of x_draw
//		double temp = x_draw;
//		//update 
//		x_draw = old_x_draw + T*(new_x_draw-old_x_draw);
//		incT();
//		return temp;
		return x_draw;
	}

	public void setX_draw(double xDraw) {
		x_draw = xDraw;
	}

	public double getY_draw() {
//		double temp = y_draw;
//		//update 
//		y_draw = old_y_draw + T*(new_y_draw-old_y_draw);
//		incT();
//		return temp;
		return y_draw;
	}

	public void setY_draw(double yDraw) {
		y_draw = yDraw;
	}
	

	public void setPatient_id(String patientId) {
		patient_id = patientId;
	}
	
	
	public Point2D getLocation(){
		return new Point2D(x,y);
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getPatientId(){
		return this.patient_id;
	}
	
	
	public void updateLocation(Matrix rssi_list, ArrayList<Point2D> anchor_pos, Matrix S, Matrix D){
		
		//System.out.println("S: ");
		//S.print(2, 2);
		//System.out.println("D: ");
	    //D.print(2, 2);
		
		//PHASE 1: COMPUTE SDM MATRIX AND D VECTOR
		SingularValueDecomposition SVD = S.svd();
		double sigma[] = SVD.getSingularValues();
		Matrix W = SVD.getS();
		Matrix V = SVD.getV();
		Matrix Ut = SVD.getU().transpose();	
		double sum_sigma= 0, temp_sum = 0, tau = 0;
		int index = 0;
		
		
		//compute and filter SVD
		for(int i=0;i<sigma.length;i++){
			sum_sigma = sum_sigma + sigma[i];
		}

		for(int i=0;i<sigma.length;i++){
			temp_sum = temp_sum + sigma[i];
			tau = temp_sum / sum_sigma;
			index++;
			if(tau>0.98 && tau<1) 
				break;
		}
		
		//System.out.println("index:"+ index);
		if(index!=W.getColumnDimension()){
			for(int j=index-1;j<W.getColumnDimension();j++){
				W.set(j, j, 0);
			}
		}
		
		//compute Scross
		Matrix Wcross = new Matrix(W.getRowDimension(),W.getColumnDimension(),0);
		for(int i=0;i<W.getRowDimension();i++){
			if(W.get(i, i)!=0)
			Wcross.set(i, i, 1/W.get(i, i));
		}
		Matrix Scross_svd = V.times(Wcross).times(Ut);

		
		//find d vector
		Matrix Dres = D.copy();

		for(int i=0;i<Dres.getColumnDimension();i++){
			for(int j=0; j<Dres.getRowDimension();j++)
				Dres.set(i, j, Math.log(D.get(i, j)));
		}
		
		//compute SDM matrix
		Matrix T_svd = Dres.times(Scross_svd);

		
		//find distance estimate
		Matrix d_svd = T_svd.times(rssi_list);
		//System.out.println("MULT:");
		//T_svd.print(2, 2);
		//rssi_list.print(2, 2);
		
		
		for(int i=0;i<d_svd.getRowDimension();i++){
			d_svd.set(i, 0, Math.exp(d_svd.get(i, 0)));
		}
		//System.out.println("exp(d_svd):");
		//d_svd.print(2, 3);
		
		//compute anchor weights
		double w[] = new double[d_svd.getRowDimension()];
		
		switch(weight_opt){
		case 1:
			for(int i=0;i<w.length;i++){
				w[i] = 1/d_svd.get(i, 0);
			}
			//System.out.println("1");
			break;
		case 2:
			for(int i=0;i<w.length;i++){
				w[i] = 1/(d_svd.get(i, 0)*d_svd.get(i,0));
			}
			//System.out.println("2");
			break;
		default:
			for(int i=0;i<w.length;i++){
				w[i] = 1;
			}
			//System.out.println("0");
			break;
		}
			
			

		
		//normalize anchor weights
		temp_sum = 0;
		for(int i=0;i<w.length;i++){
			temp_sum = temp_sum + w[i];
		}
		for(int i=0;i<w.length;i++){
			w[i] = w.length*(w[i]/temp_sum);
		}
		
		
		if(pf_is_initialized){
			
			
			if(raw_on){
				Point2D raw_target = laterate(d_svd, anchor_pos, w);
				this.x_raw = raw_target.getX();
				this.y_raw = raw_target.getY();
				//raw_target.print();
			}
			
			//PHASE 2: PARTICLE FILTERING	
			//predict particles
			Point2D randX, randU;
			for(int i=0;i<N_OF_PARTICLES;i++){
				randX = new Point2D(1*rgen.nextGaussian(),1*rgen.nextGaussian());
				randU = new Point2D(0.6*rgen.nextGaussian(),0.6*rgen.nextGaussian());
				particlesPred[i] = particles[i].plus(u_hat[i]).plus(randX);
				
				if(collision(particles[i], particlesPred[i]))
					particlesPred[i] = particles[i];
				
				u_hat[i] =  u.plus(randU);
			}	
			
			//assign weights
			for(int i=0;i<N_OF_PARTICLES;i++){
				temp_sum = 0;
					for(int k=0;k<anchor_pos.size();k++){
						temp_sum = temp_sum + w[k]*Math.pow(particlesPred[i].distanceFrom(anchor_pos.get(k))-d_svd.get(k, 0),2);
					}				
					wpf[i] = wpf[i]*Math.exp(-temp_sum);
			}
				
			temp_sum = 0;
			for(int i=0;i<N_OF_PARTICLES;i++){
				temp_sum = temp_sum + wpf[i];
			}
			//System.out.println("particle sum: " + temp_sum);
			
			//CHECK FOR CLOUD GOODNESS
			////// TO DO
			//se le particelle fanno troppo schifo vuol dire che sono lontano dalla vera funzione di costo, quindi devo re-inizializzare
			//in realtà faccio la discesa del gradiente normale e ricentro quindi la mia nuvola.
			//if(collision(target,laterate(d_svd, anchor_pos, w))){
			//	temp_sum = temp_sum * Math.pow(10, -20);
			//}
			
			//if(temp_sum<CLOUD_THRESHOLD){
			if(temp_sum<Math.pow(10, -cloud_thr)){
				//System.out.println("fan schifo!");
				target = laterate(d_svd,anchor_pos,w);
				//System.out.print("target per il salto: ");
				//target.print();
				//System.out.println("mia posizione verde:" + this.x + " " + this.y);
				if(isInsideWalls(target)){
					System.out.println("SALTO!");
					this.x = target.getX();
					this.y = target.getY();
					//this.x_draw = this.x;
					//this.y_draw = this.y;
					target = initParticleFilter(d_svd,anchor_pos,w);	
					this.setColor(Color.gray);
				}
				else{
					System.out.println("NON SALTO!");
					this.setColor(Color.red);
				}
			}
			else{
				this.setColor(Color.green);

				
				//normalize weights
				for(int i=0;i<N_OF_PARTICLES;i++){
					wpf[i] = wpf[i]/temp_sum;
				}

				//estimate new target

				Point2D temp = new Point2D(0,0);
				for(int i=0;i<N_OF_PARTICLES;i++){
					temp = temp.plus(particlesPred[i].mult(wpf[i]));
				}
				Point2D oldtarget = new Point2D(target.getX(), target.getY());
				target = target.plus(u).mult(1 - alpha_c);
				target = target.plus(temp).mult(alpha_c);
				
				u = u.mult(1 - alpha_u);
				u = target.minus(oldtarget).mult(alpha_u);

				this.x = target.getX();
				this.y = target.getY();
				//target.print();
				
				//CONTROLLO BUG NAN!!!!
				if(new Double(target.getX()).isNaN() || new Double(target.getY()).isNaN()){
					//ripristina il target a quello vecchio
					this.x = oldtarget.getX();
					this.y = oldtarget.getY();
					target.setX(this.x);
					target.setY(this.y);
					
					//SCRIVI SUL FILE DI ERRORE!
					 try{
					    	FileOutputStream fos = new FileOutputStream("debugNAN.txt",true);
					    	PrintStream p = new PrintStream(fos);  
					    	printOnFile(S, p, "S:");
					    	printOnFile(D, p, "D:");
					    	printOnFile(rssi_list, p, "rssi_list:");
					    	printOnFile(T_svd, p ,"T_svd:");
					    	printOnFile(d_svd, p ,"d_svd:");
					    }
					    catch(Exception e){
					    	System.err.println ("Error writing to file");
					    }
				}
				
				
				//assegna le coordinate da disegnare
				double distance = target.distanceFrom(new Point2D(x_draw,y_draw));
				if(distance>DRAW_THR){
					this.x_draw = this.x;
					this.y_draw = this.y;
				}
				
				if(locked_to_track){
					 Point2D tp;
					 tp = getNearestPoint(x_draw,y_draw);
					 x_draw = tp.getX();
					 y_draw = tp.getY();
				}


				//CHECK FOR RESAMPLING

				//compute effective particles
						temp_sum = 0;
						for(int i=0;i<N_OF_PARTICLES;i++){
							temp_sum = temp_sum + wpf[i]*wpf[i];
						}
						temp_sum = 1/temp_sum;
						//System.out.println("effective: " + temp_sum);
						if(temp_sum<N_OF_PARTICLES*0.5){
							resample(particles, particlesPred, wpf);
							//System.out.println("Resampling!" + temp_sum);
						}

				//compute entropy
//				temp_sum = 0;
//				for(int i=0;i<N_OF_PARTICLES;i++){
//					temp_sum = temp_sum + wpf[i]*(Math.log(wpf[i])/Math.log(2));
//				}
//				System.out.println("entropy: " + temp_sum);
//				if(-temp_sum<RESAMPLING_THR){
//					resample(particles, particlesPred, wpf);
//					System.out.println("Resampling!");
//				}
			}
		}
		
		else{
			target = initParticleFilter(d_svd, anchor_pos, w);
		}
		
		DecimalFormat df = new DecimalFormat("##.###");
		
		System.out.println("Target Position:");
		target.print();
		
		//p_mobile.println(DateFormat.getDateTimeInstance ( DateFormat.LONG, DateFormat.LONG ).format(new Date()) +" "+ df.format(target.getX()) +" "+ df.format(target.getY()) );
		
	}
	
	public void resample(Point2D[] particles, Point2D particlesPred[], double wpf[]){
		double cs[] = new double[N_OF_PARTICLES];
		cs[0] = wpf[0];
		for(int i=1;i<N_OF_PARTICLES;i++){
			cs[i] = cs[i-1] + wpf[i];
		}
		
		for(int i=0;i<N_OF_PARTICLES;i++){
			double ind = rgen.nextDouble();
			int j;
			for(j=0;j<N_OF_PARTICLES;j++){
				if(ind < cs[j]) break;
			}
			particles[i] = particlesPred[j];
			wpf[i] = INITW;
		}
		
		
	}
	
	public boolean collision(Point2D A, Point2D B){
		double den,rnum,snum,r,s;
		boolean res = false;
		//Point2D res = new Point2D();
		for(int i=0; i<NUM_WALLS; i++){
			Point2D C = walls[0][i]; 
			Point2D D = walls[1][i];
			den = (B.getX() - A.getX())*(D.getY() - C.getY()) - (B.getY() - A.getY())*(D.getX() - C.getX());
			if(den != 0) 
			{
				rnum = (A.getY() - C.getY())*(D.getX() - C.getX()) - (A.getX() - C.getX())*(D.getY() - C.getY()); 
			    snum = (A.getY() - C.getY())*(B.getX() - A.getX()) - (A.getX() - C.getX())*(B.getY() - A.getY());
			    r = rnum/den;
			    s = snum/den;
			    if(r>0 && s>0 && r<1 && s<1){
			    	return true;
			    }
			}
		}
		return false;
	}
	
	public boolean isInsideWalls(Point2D P){
		int dx=0,sx=0;
		double m,q;
		for(int i=0;i<NUM_WALLS;i++){
			Point2D A = walls[0][i];
			Point2D B = walls[1][i];
			//controllo intersezione
			if((P.getY()>A.getY() && P.getY()<B.getY()) || (P.getY()<A.getY() && P.getY()>B.getY())){
				//System.out.println("Taglio: ");
				//A.print();
				//B.print();
				//controllo se sta a destra 
				if(P.getX()>A.getX() && P.getX()>B.getX()){ 
					sx++;
					//System.out.println("sx");
				}
				else{
					//controllo se sta a sinistra
					if(P.getX()<A.getX() && P.getX()<B.getX()){ 
						dx++;
						//System.out.println("dx");
					}
					//caso sfigato
					else{
						m = (A.getY() - B.getY())/(A.getX() - B.getX());
						q = A.getY() - m*A.getY();
						if(m>0){
							if(P.getY()<m*P.getX()+q){
								dx++;
								//System.out.println("sx");
							}
							else{ 
								sx++;
								//System.out.println("dx");
							}
						}
						else{
							if(P.getY()<m*P.getX()+q) {
								sx++;
								//System.out.println("dx");
							}
							else{
								dx++;
								//System.out.println("sx");
							}
						}
					}
				}	
			}
		}
		//System.out.println("sx : " + sx + "dx: " + dx);
		if(sx%2==0 && dx%2==0) return false;
		else return true;
		
	}
	
	
	private Point2D initParticleFilter(Matrix d_svd, ArrayList<Point2D> anchor_pos, double[] w){
		//qua devo laterare per trovare la prima posizione 
		//e inizializzare le particles
		
	   Point2D target = laterate(d_svd,anchor_pos,w);
		//Point2D target = new Point2D(this.getX(),this.getY());
	   if(!isInsideWalls(target)){
		   this.setIs_reachable(false);
		   target = new Point2D(0,0);
	   }
	   
	   //3. inizializzazione del particle filter
		for(int i=0;i<N_OF_PARTICLES;i++){
			particles[i] = new Point2D(target.getX(),target.getY());
			wpf[i] = INITW;
			u_hat[i] = new Point2D(0,0);
		}
	   
		pf_is_initialized = true;
		return target;

	}
		
	public Point2D laterate(Matrix d_svd, ArrayList<Point2D> anchor_pos, double[] w){
		//1. trova l'ancora stimata più vicina da cui partire per discendere il gradiente
		double min = d_svd.get(0, 0);	
		int index = 0;
		for(int i=0;i<d_svd.getRowDimension();i++){
			if(d_svd.get(i, 0)<min){
				min = d_svd.get(i, 0);
				index = i;
			}
		}
		
		Point2D target = new Point2D(anchor_pos.get(index).getX(), anchor_pos.get(index).getY());
		
		//2. discesa del gradiente per trovare la posizione stimata
		for(int k=0; k<100; k++){
			Point2D summ = new Point2D();
			for(int i=0;i<anchor_pos.size();i++){
				double tempd = target.distanceFrom(anchor_pos.get(i));
				if(tempd == 0) tempd = 1;
				Point2D amount = anchor_pos.get(i).minus(target);
				amount = amount.mult(1-d_svd.get(i, 0)/tempd);
				summ = summ.plus(amount).mult(w[i]);
			}
			target = target.plus(summ.mult(alpha));
	   }
		
		return target;
	}
	
	
	public void setParam(LauraParam param){
		this.alpha_c = param.alpha_c;
		this.alpha_u = param.alpha_u;
		this.weight_opt = param.weights;
		this.raw_on = param.raw_on;
		this.cloud_thr = param.cloud_thr;
	}
	
	public LauraParam getParam(){
		return new LauraParam(this.raw_on, this.weight_opt, this.alpha_c, this.alpha_u, this.cloud_thr);
	}
	
	public void addIntrusionRule(Point2D top_left, Point2D bottom_right, String name){
		intrusion_rules.add(new IntrusionRule(top_left,bottom_right,name));
	}
	
	public void addProximityRule(String node_name){
		proximity_rules.add(node_name);
	}

	public ArrayList<IntrusionRule> getIntrusionRules(){
		return intrusion_rules;
	}
	
	public ArrayList<String> getProximityRules(){
		return proximity_rules;
	}
	
	public boolean checkForProximity(MobileNode node){
		if(this.getLocation().distanceFrom(node.getLocation())< 2 && !collision(this.getLocation(),node.getLocation())){
			return true;
		}
		else return false;
	}
	
	public boolean isIs_reachable() {
		return is_reachable;
	}

	public void setIs_reachable(boolean isReachable) {
		is_reachable = isReachable;
		if(!is_reachable){
			this.setColor(Color.lightGray);
		}
		else{
			//this.setColor(Color.green);
		}
	}
	
	public boolean hasAccelerometer(){
		return has_accel;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public int getMsg_counter() {
		return msg_counter;
	}

	public void setMsg_counter(int msgCounter) {
		msg_counter = msgCounter;
	}
	
	private void printOnFile(Matrix A, PrintStream p, String name){
		p.println(name);
		for(int i=0;i<A.getRowDimension();i++){
			for(int j=0;j<A.getColumnDimension();j++){
				p.print(A.get(i, j) + " ");
			}
			p.println();
		}
		p.println();
	}
	
	private void buildWalls(){

//		//MURI ALE
//
//		double x[] = {80, 100,94 ,265,265,226,221,439,439,309,309,567,567,468,468,598,598,624,624,598,598,651,655,748,745,803,798,726,726,756,754,664,668,692,697,609,609,651,644,450,459,558,560,312,312,430,428,223,223,274,270,140,140,197,210,155,160,80 ,80};
//		double y[] = {474,474,555,559,577,586,848,843,579,573,553,546,570,575,839,839,780,777,568,568,551,546,852,852,548,544,485,485,441,441,205,196,438,441,482,482,465,465,207,207,458,460,485,487,474,465,207,212,463,467,491,480,469,469,196,196,286,291,474};
//		
//		for(int i=0;i<x.length-1;i++){
//		walls[0][i] = new Point2D(x[i]/40,y[i]/40);
//		walls[1][i] = new Point2D(x[i+1]/40, y[i+1]/40);
//		System.out.println("wall " + i);
//		System.out.print("start: ");
//		walls[0][i].print();
//		System.out.print("stop: ");
//		walls[1][i].print();
//	}

//		//MURI LAURA
		double[] x = {200,291,291,254,281,336,397,397,340,340,485,485,460,460,410,410,455,520,560,600,600,545,545,650,650,610,610,640,640,660,655,585,525,455,440,485,560,560,485,420,440,400,350,380,380,320,320,360,360,310,225,135,90,90,260,260,200,200};
		double[] y = {241,241,227,227,133,76,177,230,230,244,244,233,233,217,217,180,180,80,115,185,231,233,244,244,284,284,300,300,340,340,395,450,450,400,350,300,300,284,284,340,400,450,400,330,284,284,300,300,345,415,445,410,335,300,300,284,284,241};
				
		for(int i=0;i<x.length-1;i++){
			walls[0][i] = new Point2D(x[i]/25,y[i]/25);
			walls[1][i] = new Point2D(x[i+1]/25, y[i+1]/25);
			System.out.println("wall " + i);
			System.out.print("start: ");
			walls[0][i].print();
			System.out.print("stop: ");
			walls[1][i].print();
		}
		
//		//MURI 3 PIANO
//		double x[] = { 15, 125, 125, 70, 70, 140    ,140, 465, 465, 530,530,625,625, 805, 805,625, 625,805,805,645,645,625,625,580,580,570,570,540,535,490  , 490,530,530,500,500,530,530,450,450,480,480,440,440,380, 380, 365, 365, 270, 270, 245, 245, 145, 145, 40, 40, 15, 15};
//		double y[] = {400, 400, 455, 455, 465,465, 400,400, 330 ,330,280,280,100,100, 305,305 , 320,320, 485,485,345,345,410,410,380,380,475,475,370,370 ,400,400,455,455,470,470,560,560,470,470,455,455,560,560,460 ,460 ,560, 560, 460, 460, 560 , 560 ,490, 490, 460, 460, 400  };
//
//      for(int i=0;i<x.length-1;i++){
//
//			walls[0][i] = new Point2D(x[i]/PPM,y[i]/PPM);
//			walls[1][i] = new Point2D(x[i+1]/PPM, y[i+1]/PPM);
//			//System.out.println("wall " + i);
//			//System.out.print("start: ");
//			//walls[0][i].print();
//			//System.out.print("stop: ");
//			//walls[1][i].print();
//		}
		
//		//MURI RFID LAB	
//		double[] x = {774,774,662,662,774,774,632,632,629,629,358,358,629,629,632,632,580,580,629,629,358,358,478,478,358,358,326,326,355,355,12,12,299,299,12,12,91,91,215,215,612,612,218,218,372,372,368,368,218,218,574,574,576,576,774};
//		double[] y = {22,292,292,295,295,655,655,638,638,655,655,584,584,610,610,404,404,408,408,581,581,408,408,404,404,396,396,401,401,655,655,401,401,396,396,142,142,22,22,295,295,292,292,100,100,54,54,94,94,22,22,250,250,22,22};
//		
//		for(int i=0;i<x.length-1;i++){
//			
//			walls[0][i] = new Point2D(x[i]/PPM,y[i]/PPM);
//			walls[1][i] = new Point2D(x[i+1]/PPM, y[i+1]/PPM);
//			System.out.println("wall " + i);
//			System.out.print("start: ");
//			walls[0][i].print();
//			System.out.print("stop: ");
//			walls[1][i].print();
//		}

//		//MURI RFID LAB 2
//		double [] x = {960,460,460,45,45,630,630,1030,1030,220,220,910,910,275,275,1000,1000,960,960};
//		double [] y = {235,235,145,145,840,840,890,890,770,770,300,300,390,390,700,700,390,390,235};
//
//		for(int i=0;i<x.length-1;i++){
//
//			walls[0][i] = new Point2D(x[i]*0.7/PPM,y[i]*0.7/PPM);
//			walls[1][i] = new Point2D(x[i+1]*0.7/PPM, y[i+1]*0.7/PPM);
//			System.out.println("wall " + i);
//			System.out.print("start: ");
//			walls[0][i].print();
//			System.out.print("stop: ");
//			walls[1][i].print();
//		}
		
	}
	
	public Point2D[] getParticles(){
		return particles;
	}
	
	public Point2D[][] getWalls(){
		return walls;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	public void setTrackLocking(boolean lockedToTrack) {
		this.locked_to_track = lockedToTrack;		
	}
	
	 private Point2D getNearestPoint(double xDraw, double yDraw) {
			double min_dist = 10000, temp_dist;
			double[] distances = new double[track.getNofSegments()];
			int[] seg_indexes = new int[track.getNofSegments()];
			Point2D[] new_pos = new Point2D[track.getNofSegments()];
			Line2D temp_segment = null;

			//1. calcolo le distanze
			for(int i=0;i<track.getNofSegments();i++){
				Line2D segment = track.getSegment(i);
				distances[i] = segment.ptLineDist(xDraw, yDraw);
				seg_indexes[i] = i;
				new_pos[i] = new Point2D(0,0);
			}
		
			//2. ordino l'array 
			for(int i=0;i<track.getNofSegments()-1;i++){
				for(int j=i+1;j<track.getNofSegments();j++){
					if(distances[j]<distances[i]){
						
						int temp;
						double tempd;
						
					   tempd = distances[i];
						distances[i] = distances[j];
						distances[j] = tempd;
						
						temp = seg_indexes[i];
						seg_indexes[i] = seg_indexes[j];
						seg_indexes[j] = temp;
					}
				}
			}
//			
			for(int i=0;i<track.getNofSegments();i++){
				System.out.println(seg_indexes[i] + ": " + distances[i]);
			}
//			
			//provo tutti i segmenti in ordine e salvo le distanze che avrei. alla fine prendo la minore;
			
			double minx,miny,maxx,maxy;
			
			for(int i=0;i<track.getNofSegments();i++){			
				temp_segment = track.getSegment(seg_indexes[i]);
				
				double m;
				if(temp_segment.getX2() - temp_segment.getX1()!=0){
				   m = (temp_segment.getY2() - temp_segment.getY1()) / (temp_segment.getX2() - temp_segment.getX1());
				   System.out.println("m: " +m);
				   if(m==0){
					   m=0.001;
				   }
				   //2. risolvi il sistema
				   double q1 = temp_segment.getY1()-m*temp_segment.getX1();
				   double q2 = yDraw + (1/m)*xDraw;
				   double x = ((q2-q1)*m)/((m*m)+1);
				   new_pos[i].setX(x);
				   new_pos[i].setY(m*x + q1);
				  // System.out.println("risolvo");
				}
				
				else{
				   //2. in questo caso è facilissimo
					new_pos[i].setX(temp_segment.getX1());
					new_pos[i].setY(yDraw);
				}
				

				//sbattimento per capire se sono nel segmento
			
				if(temp_segment.getX1() < temp_segment.getX2()){
					  minx = temp_segment.getX1();
					  maxx = temp_segment.getX2();
				}
				else{
					minx = temp_segment.getX2();
					maxx = temp_segment.getX1();
				}
				
				if(temp_segment.getY1() < temp_segment.getY2()){
					  miny = temp_segment.getY1();
					  maxy = temp_segment.getY2();
				}
				else{
					miny = temp_segment.getY2();
					maxy = temp_segment.getY1();
				}
				
				
				if(new_pos[i].getX()<minx) new_pos[i].setX(minx);
				if(new_pos[i].getX()>maxx) new_pos[i].setX(maxx);
				if(new_pos[i].getY()<miny) new_pos[i].setY(miny);
				if(new_pos[i].getY()>maxy) new_pos[i].setY(maxy);
				
			}	
			
			System.out.println("fatto");
			//a questo punto ho un sacco di punti e devo trovare quello con distanza minore
			double min_distance = 100000;
			int index = 0;
			for(int i=0;i<track.getNofSegments();i++){
				double temp = new_pos[i].distanceFrom(new Point2D(xDraw,yDraw));
				if (temp<min_distance){
					min_distance = temp;
					index = i;
				}
			}
			
			System.out.println("posizione prescelta: " + index);
			return new_pos[index];
			
		 }


	public void setTrack(Track track) {
		this.track = track;
	}

}
