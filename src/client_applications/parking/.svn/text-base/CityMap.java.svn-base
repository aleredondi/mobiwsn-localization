
package client_applications.parking;


import client_applications.classi_remote.NoiseControl;
import client_applications.parking.dijkstra.*;

import remote_interfaces.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class CityMap extends JPanel
{
	private ImageIcon icon;
	private JLabel picture;
	private boolean parkEnabled, routeEnabled, carEnabled, noiseEnabled, carMoveEnabled, half;
	private String dir, group, url;
	private ArrayList<WSNGateway> gwlist;
	private ArrayList<Integer> xMove, yMove;
	private ArrayList<Double> noiseVal;
	private int coordX,coordY, size, pos;
	private ArrayList<Park> parkList;
	private DijkstraAlgorithm da;
	private int xPoints[], yPoints[];
	private Graph gr;
	private double mediaNoise;
	private Image carImage;
	private NoiseControl nc;
	
	private String images_path;
	
	public CityMap()
	{
		images_path = "src/client_applications/parking/images/";
		
		ImageIcon car =new ImageIcon(images_path + "icons/car.png");
		carImage=car.getImage();
		gwlist= new ArrayList<WSNGateway>();
		gr=new Graph();
		this.parkEnabled=this.routeEnabled=this.carEnabled=noiseEnabled=false;
		this.half=true;
		this.mediaNoise=0.0;
		this.icon =new ImageIcon(images_path + "default.jpeg");
		picture=new JLabel(this.icon);
		this.add(this.picture);	
		this.pos=0;
		
		nc= new NoiseControl();
	}

	public void setPark(boolean val)
	{
		this.parkEnabled=val;
	}
	
	public boolean getPark()
	{
		return this.parkEnabled;
	}
	
	public void setRoute(boolean val)
	{
		this.routeEnabled=val;
	}
	
	public boolean getRoute()
	{
		return this.routeEnabled;
	}
	
	
	public void setNoise(boolean val)
	{
		this.noiseEnabled=val;
	}
	
	public void setimage(String p, int x, int y)
	{
		this.dir=p;
		this.coordX=x;
		this.coordY=y;
		this.picture.setIcon(new ImageIcon(dir + "map.jpeg"));
		this.carEnabled=true;
		this.setCarEnabled(true);
		
	}
	
	public void setUrl(String manager)
	{
		this.url=manager;
	}
	
	public void setCarEnabled(boolean val)
	{
		this.carEnabled=val;
	}
	
	public void setCarMove(boolean val)
	{
		this.carMoveEnabled=val;
		this.carEnabled=(!val);
		
	}
	public void setPos()
	{
		this.pos=0;
	}
	
	
	
	public double getMediaNoise()
	{
		Class typeIn[]={String.class, boolean.class};
        Object paramIn[]={this.group, true};
            
        Class typeInmed[]={};
        Object paramInmed[]={};
		WSNGateway first=this.gwlist.get(0);
		
		try 
		{				
			Class typeIngw[]={String.class, String.class};
		    Object paramIngw[]={first.getName(), this.url};
			first.useMethod(nc.getClassIdentificationParameter(), "setGateway", typeIngw, paramIngw);
			
			this.noiseVal=(ArrayList<Double>)first.useMethod(nc.getClassIdentificationParameter(), "getValue", typeIn, paramIn);
			this.mediaNoise=(Double)first.useMethod(nc.getClassIdentificationParameter(), "getMedia", typeInmed, paramInmed);
		} 
		catch (RemoteException e1) {e1.printStackTrace();}
	
		return this.mediaNoise;
	}
	
	public void requestParking(ArrayList<WSNGateway> list, ArrayList<Park> park, String gname)
	{
		this.gwlist=list;
		this.parkList=park;
		this.group=gname;
	}
	
	public void searchAllPath()
	{
		int i, n, x, y, n1, n2;
		char c;
		String num="";
		String s = "";  
	    ArrayList<Node> listn=new ArrayList<Node>();
	    BufferedReader input=null;
	    
		try 
		{
			input = new BufferedReader(new FileReader(this.dir + "data.txt"));
		} 
		catch (FileNotFoundException e1) {e1.printStackTrace();}
	       
		try 
		{
			s = input.readLine();
			if (s.equals("NODE"))
			{  
				s=input.readLine();
				while (!s.equals("EDGE")) 
				{
					i=0;
					num="";
					c=s.charAt(i);
					while(c!=',')
					{
						num=num+c;
						c=s.charAt(++i);
					}
					n= Integer.parseInt(num);
					
					c=s.charAt(++i);
					num="";
					while(c!=',')
					{
						num=num+c;
						c=s.charAt(++i);
					}
					x=Integer.parseInt(num);
					c=s.charAt(++i);
					num="";
					while(true)
					{
						num=num+c;
						i++;
						if(i<s.length())
							c=s.charAt(i);
						else
							break;
					}
					y=Integer.parseInt(num);
					Node node=new Node(n,x,y);
					listn.add(node);
					this.gr.addNode(node);
					s=input.readLine();
				}
				
				s=input.readLine();
				while (s != null) 
				{
					i=0;
					c=s.charAt(0);
					num="";
					while(c!=',')
					{
						num=num+c;
						c=s.charAt(++i);
					}
					n1= Integer.parseInt(num);
					c=s.charAt(++i);
					num="";
					while(true)
					{
						num=num+c;
						i++;
						if(i<s.length())
							c=s.charAt(i);
						else
							break;
					}	
					n2= Integer.parseInt(num);
					this.gr.addEdge(new Edge(listn.get(n1), listn.get(n2)));
					s=input.readLine();
				}
			}	   
		} 
		catch (Exception e) {}
		
		int from = this.gr.getNode(coordX, coordY).getId();
		da= new DijkstraAlgorithm(this.gr);
		this.da.dijkstra(from);
	}

	public void bestRoute()
	{
		ArrayList<Node> path=null;
		double length, dim;
		length=Double.MAX_VALUE;
		int coord[]=null;
		int idTo;
		int from = gr.getNode(coordX, coordY).getId();
		
		for(WSNGateway gw : this.gwlist)
		{
			Park park=null;
			try 
			{
				for(Park p : this.parkList)
				{
					if(p.getName().equals(gw.getName()))
					{
						park=p;
						break;
					}
				}
				if(park.getAvailablePark()>0)
				{
					coord=gw.getCoord();
					idTo=this.gr.getNode(coord[0],coord[1]).getId();
					dim= this.da.getLenghtPath(idTo);

					if(length>dim)
					{
						length=dim;
						path=this.da.getMinimumPath(from,idTo);
					}
				}
			} 
			catch (RemoteException e) {e.printStackTrace();}
		
		}
		
		if(path!=null)
		{
			size=path.size();
			xPoints=new int[size];
			yPoints=new int[size];
			for(int j=0;j<size;j++)
			{
				Node node=path.get(j);
				xPoints[j]=node.getX();
				yPoints[j]=node.getY();
				
			}
			
			setMoveCoordinate();
		}
	}
	
	public void searchSpecificPath(int xFrom, int xTo, int yFrom, int yTo)
	{
		ArrayList<Node> path=null;
		path=this.da.getMinimumPath(this.gr.getNode(xFrom,xTo).getId(),this.gr.getNode(yFrom,yTo).getId());
		size= path.size();
		xPoints=new int[size];
		yPoints=new int[size];
		
		for(int j=0;j<size;j++)
		{
			Node node=path.get(j);
			xPoints[j]=node.getX();
			yPoints[j]=node.getY();
		}
		
		setMoveCoordinate();
	
	}
	
	private void setMoveCoordinate()
	{
		xMove= new ArrayList<Integer>();
		yMove = new ArrayList<Integer>();
		int max, sign;
		int n=9;
		int interval,sum;
		
		for(int j=1;j<xPoints.length;j++)
		{
			xMove.add(xPoints[j-1]);
			if(xPoints[j]>xPoints[j-1])
			{
				sign=1;
				max=xPoints[j];
			}
			else
			{
				sign=-1;
				max=xPoints[j-1];
			}
			
			sum=0;
			
			interval=sign*((Math.abs(xPoints[j] - xPoints[j-1]))/n);
			for(int i=1;i<n;i++)
			{
				sum=(interval*i)+xPoints[j-1];
				xMove.add(sum);
				
			}
			
		}
		xMove.add(xPoints[xPoints.length-1]);
		
		for(int j=1;j<yPoints.length;j++)
		{
			yMove.add(yPoints[j-1]);
			if(yPoints[j]>yPoints[j-1])
			{
				sign=1;
				max=yPoints[j];
			}
			else
			{
				sign=-1;
				max=yPoints[j-1];
			}
			
			sum=0;
			
			interval=sign*((Math.abs(yPoints[j] - yPoints[j-1]))/n);
			for(int i=1;i<n;i++)
			{
				sum=(interval*i)+yPoints[j-1];
				yMove.add(sum);
				
			}
			
		}
		yMove.add(yPoints[yPoints.length-1]);
		
	}
	
	
	public void paint(Graphics g)
	{
		super.paint(g);
		int num=0;
		int coord[]=null;
		
		if(routeEnabled)
		{
			Graphics2D g2d =(Graphics2D)g;
			g2d.setColor(Color.green);
			g2d.setStroke(new BasicStroke(4.0f));
			g2d.drawPolyline(xPoints, yPoints, size);			
		}
		
		
		if(parkEnabled)
		{
			for(int i=0;i<this.gwlist.size();i++)
			{
				num=i+1;
				ImageIcon imageparking =new ImageIcon(images_path + "icons/parking/parking" + num + ".jpeg");
	        
				Image a=imageparking.getImage();
				try 
				{
					coord = gwlist.get(i).getCoord();
				}	 
				catch (RemoteException e) {e.printStackTrace();}
				g.drawImage(a,coord[0]-10,coord[1]-10,this);
	        
			}	
		}
		
		
		
		
		if(noiseEnabled)
		{
			WSNGateway gw=null;
			double noiseZone;
			for(int i=0;i<this.gwlist.size();i++)
			{
				gw=this.gwlist.get(i);
				try 
				{
					coord=gw.getCoord();
				} 
				catch (RemoteException e) {e.printStackTrace();}
				
				noiseZone=this.noiseVal.get(i);
				
				if(noiseZone>=650)
					g.setColor(new Color(255,0,0,100));
				else if(noiseZone>=450 && noiseZone<650)
					g.setColor(new Color(255,150,0,100));
				else if(noiseZone>=250 && noiseZone<450)
					g.setColor(new Color(250,255,0,100));
				else
					g.setColor(new Color(0,255,0,100));

				g.fillOval(coord[0]-100,coord[1]-100, 200, 200);
			}
		}
		
				
		if(carEnabled)
		{	
			g.drawImage(carImage,this.coordX-10,this.coordY-10,this);
		}
		
		if(carMoveEnabled)
		{
			ImageIcon homeIcon =new ImageIcon(images_path + "icons/home.png");
			Image home=homeIcon.getImage();
			g.drawImage(home,coordX-12,coordY-12,this);
			
			if(this.pos >(this.xMove.size()-1))
				this.pos=0;
			
			
				g.drawImage(carImage,xMove.get(pos)-10,yMove.get(pos)-10,this);
				pos++;			
		}
	}

	
}
