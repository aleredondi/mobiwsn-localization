
package client_applications.console;


/**
 * @author Raja Jurdak, Antonio Ruzzelli, and Samuel Boivineau
 */

public class Util {
	
	/*
		Mote.java and MoteDatabase.java
		UNKNOWN is used for the battery that is not yet supported.
	*/
	
	public static final int UNKNOWN = -1;
	
	/*
		Scout.java
		MUTEX_WAIT_TIME_MS is the time waited before trying to get
		a mutex and update the database.
		SCOUT_SLEEP_PERIOD is the time in ms that the thread spends
		sleeping.
		When a new mote is received, the thread Scout can ask the status
		of this mote if ASK_STATUS_OF_NEW_MOTE is true.
		This feature SHOULD be set to false, with a big network.
	*/
	
	public static final int MUTEX_WAIT_TIME_MS = 50; 
	public static final int SCOUT_SLEEP_PERIOD = 100;
	public static final boolean ASK_STATUS_OF_NEW_MOTE = true;
	
	/*
		Sender.java
		SENDER_WAIT_TIME is the time that the Sender thread wait
		when a message is sent, before send a new one, to avoid any
		override issue (in ms).
	*/
	
	public static final int SENDER_WAIT_TIME = 2000;
	
	/*
		ConsolePanel.java
		NB_MSG_TYPE is the number of message type filtered.
		Because the values are used in an array, the first one
		MUST be 0, and others follow incrementally
	*/
	public static final byte NB_MSG_TYPE = 4;
	public static final byte MSG_MESSAGE_RECEIVED = 0;
	public static final byte MSG_MESSAGE_SENT = 1;
	public static final byte MSG_MOTE_ADDED = 2;
	public static final byte MSG_MOTE_UPDATED = 3;
	
	/*
		MapPanel.java
		TIMEOUT is in ms and means the time a route spends to disappear.
	*/
	public static final int TIMEOUT = 10000;
	public static final int X_MAX = 1000;
	public static final int Y_MAX = 1000;
	public static final int MOTE_RADIUS = 10;
	
	/*
		Misc Functions
	*/
	
	public static void debug(String s) {
		System.out.println(s);
	}
	
	/*
		These 4 functions are used to translate values from
		human-readable values (%) to computer values (int).
	*/
	
	public static int thresholdToPercent(int t) {
		return (int) (100 * (double)t / 16384);
	}
	
	public static int thresholdToInt(int t) {
		return (int) (65535 * (double)t / 100);
	}
	
	public static int dutyCycleToInt(int d) { return d*100;}
	
	public static int dutyCycleToPercent(int d) { return d/100;}
	
}