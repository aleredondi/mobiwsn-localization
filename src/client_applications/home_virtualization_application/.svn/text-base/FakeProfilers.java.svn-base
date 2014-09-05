package client_applications.home_virtualization_application;

import client_applications.home_virtualization_application.devices.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jgraph.*;
import org.jgraph.graph.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import org.jfree.data.xy.*;

import remote_interfaces.clients.dve.*;
import remote_interfaces.clients.home_virtualization_application.HomeVirtualizationApplication;
import remote_interfaces.clients.home_virtualization_application.device.*;
import remote_interfaces.clients.profiling_system.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


import java.awt.Color;
import java.util.Hashtable;
import java.util.Random;

public class FakeProfilers {
	private int roomsNumber=0;
	private Random generator = new Random();
	private int samples=2880;
	private int firstWindowLength=60;
	private int secondWindowLength=360;
	private int thirdWindowLength=120;
	private Hashtable<String,double[]> profiles =new Hashtable<String,double []>();
	
	public FakeProfilers(int num,ArrayList<String> names){
		roomsNumber=num;
		for(int i=0;i<num;i++){
			double[] profile=new double[samples];
			for(int j=0;j<firstWindowLength/6;j++){
				profile[j+firstWindowLength*i]=0.6;
			}
			for(int j=firstWindowLength/6;j<firstWindowLength*5/6;j++){
				profile[j+firstWindowLength*i]=1;
			}
			for(int j=firstWindowLength*5/6;j<firstWindowLength;j++){
				profile[j+firstWindowLength*i]=0.4;
			}
			
			for(int j=1440;j<secondWindowLength/6+1440;j++){
				profile[j+secondWindowLength*i]=0.6;
			}
			for(int j=1440+secondWindowLength/6;j<1440+secondWindowLength*5/6;j++){
				profile[j+secondWindowLength*i]=1;
			}
			for(int j=1440+secondWindowLength*5/6;j<1440+secondWindowLength;j++){
				profile[j+secondWindowLength*i]=0.4;
			}
			
			for(int j=2280;j<2280+thirdWindowLength/6;j++){
				profile[j+thirdWindowLength*i]=0.6;
			}
			for(int j=2280+thirdWindowLength/6;j<2280+thirdWindowLength*5/6;j++){
				profile[j+thirdWindowLength*i]=1;
			}
			for(int j=2280+thirdWindowLength*5/6;j<2280+thirdWindowLength;j++){
				profile[j+thirdWindowLength*i]=0.4;
			}
			
			profiles.put(names.get(i), profile);
			System.out.println(names.get(i)+""+profile.length);
		}
		
	}
	
	public double[] getProfile(String name){
		return profiles.get(name);
	}
}