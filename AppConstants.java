package app;

import java.nio.file.Paths;

public interface AppConstants {
	// app specific constants
	// 
	final boolean IS_TESTING = true;
	final String APP_VERSION = "0.0.1";
//	final long SEC_TO_MS = 1000;
//	final long MIN_TO_MS = 60*SEC_TO_MS;
//	final long HR_TO_MS  = 60*MIN_TO_MS;
	final int LENGTH_MENU_ITEM_TYPE 	= 6;
	final int LENGTH_MENU_ITEM_ID	 	= 3;
	final int LENGTH_MENU_ITEM_NAME 	= 15;
	final int LENGTH_MENU_ITEM_DESC 	= 15;
	final int LENGTH_BOOKING_NAME 		= 15;
	final int LENGTH_BOOKING_CONTACT 	= 10;
	
	
	// STORE RELATED
	// [Enhance] read from file
	final boolean GST_IS_INCLUSIVE = false;
	final int LAST_ACTION_TIME_MIN 		= 30; // before end session 
	final int ORDER_EXPIRE_TIME_MIN 	= 30;
	final int BOOKING_EXPIRE_TIME_MIN 	= 15;
	final int AM_START 	= 11;
	final int AM_END 	= 15;
	final int PM_START 	= 17;
	final int PM_END 	= 21;
	final double SERVICE_CHARGE = 10.0; 
	final double GST    = 7.0; 
	final String PATH_PROJECT = System.getProperty("user.dir");
	final String PATH_DATA_FOLDER = Paths.get(PATH_PROJECT, "data").toString();
	final String DATA_FILE_NAME = "store.dat";
	
	
	
	// request codes
	final int REQ_CODE_MENU_MAIN     			= 0;
	final int REQ_CODE_MENU_ORDER    			= 1;
	final int REQ_CODE_MENU_ORDER_ACTION        = 2;
	final int REQ_CODE_MENU_BOOKING  			= 3;
	final int REQ_CODE_MENU_TABLE    			= 4;
	final int REQ_CODE_MENU_REPORT   			= 5;
	final int REQ_CODE_MENU_SETTING  			= 6;
	final int REQ_CODE_MENU_EDIT_STORE  		= 7;
	final int REQ_CODE_MENU_EDIT_STUFF  		= 8;
	final int REQ_CODE_MENU_EDIT_MENU_ITEM  	= 9;
//	final int REQ_CODE_MENU_EDIT_PROMO_ITEM  	= 10;
	
	// format (to be discuss)
	final String FORMAT_DATE = "dd/MM/yyyy";
	final String FORMAT_DATE_SHORT = "dd/MM/yy";
	final String FORMAT_MONTH = "MM/yyyy";
	final String FORMAT_TIME = "HH:mm";
	final String FORMAT_DATETIME = "dd/MM/yyyy HH:mm:ss.SSS";
	final String FORMAT_DATETIME_DETAIL = "dd/MM/yyyy HH:mm:ss.SSS";
	
	final String TYPE_WALK_IN = "Walk in";
	final String TYPE_RESERVATION = "Reservation";
	
}
