package app.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.AppConstants;
import app.dataManager.Menu;
import app.dataManager.Restaurant;
import app.model.Booking;
import app.model.MenuItem;
import app.model.Table;

public class Utility implements AppConstants {
	private static Scanner sc = new Scanner(System.in);
	
	public static double round(double number) {
		return Math.round(number*100.0)/100.0;
	}
	
//	public static String  repeatPattern(String pattern, int numRepeats) {
//		String str= "";
//		for (int i = 0; i < numRepeats; i++) {
//			str += pattern;
//		}
//		return str;
//	}
	
	public static String capitalize(String string) {
		if (string == null) {
			return null;
		}
		if (string.length() == 0) {
			return string;
		}
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
	}
	

	
	public static int inputInt(String hint) {
		int input;
		while(true) {
			try {
				System.out.print(hint);
				input = sc.nextInt();
				sc.nextLine();
				return input;
			} catch (Exception e) {
				sc.nextLine();
			}
		}
	}
	
	public static int inputInt(String hint, int min, int max) {
		if (max < min) {
//			throw new Exception("max is less than min.");
			System.out.println("Invalid(max < min)");
			System.out.println(String.format("Err: invalid argument! max(%d) < min(%d)", max, min));
			return -1;
		}
		
		int input;
		do {
			input = inputInt(hint);
		} while (input<min || input>max);
		return input;
	}
	
	public static int inputIntAtLeast(String hint, int min) {
		int input;
		do {
			input = inputInt(hint);
		} while (input<min);
		return input;
	}
	
	public static double inputDouble(String hint) {
		double input;
		while(true) {
			try {
				System.out.print(hint);
				input = sc.nextDouble();
				sc.nextLine();
				return input;
			} catch (Exception e) {
				sc.nextLine();
			}
		}
	}
	
	public static double inputDoubleAtLeast(String hint, double min) {
		double input;
		do {
			input = inputDouble(hint);
		} while (input<min);
		return input;
	}
	
	public static String inputString(String hint) {
		String input;
		do {
			System.out.print(hint);
			input = sc.nextLine();
		} while (input.length() == 0);
		return input;
	}
	
	public static String inputString(String hint, int length) {
		if (length<1) {
//			throw new Exception("length should be at least 1");
			System.out.println(String.format("Err: Invalid length(%d) < 1", length));
			return null;
		}
		String input;
		do {
			input = inputString(hint);
		} while (input.length()>length);
		return input;
	}
	
	public static boolean inputBoolean(String hint) {
		String input = null;
		while(true) {
			input = inputString(hint);
			input = input.trim().toLowerCase();
			if (input.equals("y") || input.equals("yes") || input.equals("true")) {
				return true;
			} else if (input.equals("n") || input.equals("no") || input.equals("false")) {
				return false;
			}
		}
	}
	
	public static Date inputDate(String hint) {
		String input;
		while(true) {
			input = inputString(hint);
			if (input.trim().toLowerCase().equals("today")) {
				return getToday();
			}
			if (isValidDate(input)) {
				Date date = parseDate(FORMAT_DATE, input);
				if (IS_TESTING) {
					System.out.println("[Test] parsed Time: "+formatDate(FORMAT_DATETIME, date));
				}
				
				if (formatDate(FORMAT_DATE, date).equalsIgnoreCase(input)) {
					return date;
				} else {
					System.out.println("Err: Date is not valid");
				}
			}
		}
	}
	
	
	public static Date inputDate(String hint, Date startDate, Date endDate) {
		if ( endDate.before(startDate)) {
			System.out.println("Err: Start date is after end date");
			return null;
		}
		Date input;
		while (true) {
			input = inputDate(hint);
			if (IS_TESTING) {
				System.out.println(String.format("[Test] Start\t%s \tisBefore %b", formatDate(FORMAT_DATETIME, startDate), input.before(startDate)));
				System.out.println(String.format("[Test] End\t%s \tisAfter  %b", formatDate(FORMAT_DATETIME, endDate), input.after(endDate)));
			}
			
			if ( input.before(startDate) ) {
				System.out.println(String.format("Err: Entered date cannot be before %s", formatDate(FORMAT_DATE, startDate)));
				continue;
			} else if ( input.after(endDate) ) {
				System.out.println(String.format("Err: Entered date cannot be after %s",  formatDate(FORMAT_DATE, endDate)));
				continue;
			}
			break;
		}
		return input;
	}
	
	public static Date inputTime(String hint, Date date) {
		String input;
		while(true) {
			input = inputString(hint).trim().toLowerCase();
			if (input.equals("q") || input.equals("quit")) {
				return null;
			}else if (isValidTime(input)) {
				String[] strTime = input.split(":");
				int  hr = Integer.parseInt(strTime[0]);
				int min = Integer.parseInt(strTime[1]);
//				System.out.println(String.format("[Test] ", args));
				return changeTime(date, hr, min);
			}
		}
	}
	
	public static Date inputTimeWithinSession(String hint, Date date) {
		Date input;
		char session;
		while (true) {
			input = Utility.inputTime(hint, date);
			if (input == null) {
				return null;
			}
			
			session = Utility.getSession(input);
			if (IS_TESTING) {
				System.out.println(String.format("[Test] DATETIME(%s), SESSION(%c)", formatDate(FORMAT_DATETIME_DETAIL, input), session ));
			}			
			if (isAfterLastActionTime(input, session)) {
				System.out.println("Err: Entered time is after last action time");
				continue;
			}
			if (Utility.isBeforeNow(input)) {
				System.out.println("Err: Entered time is passed");
				continue;
			} else if (session!= 'a' && session!='p') {
				System.out.println("Err: Entered time is not operating hour");
				continue;
			}
			break;
		}
		return input;
	}
	
	
	
	
	public static MenuItem inputMenuItemById() {
		Menu menu =Restaurant.getInstance().getMenu();
		if (menu.getItems().size() == 0) {
			System.out.println("No menu item found.");
			return null;
		}
		MenuItem item = null;
		do {
			item = menu.getItemById(Utility.inputString("Enter item id: "));
		} while (item == null);
		return item;
	}
	

	
	
	
	public static Table inputTableById(ArrayList<Table> listTable) {
		Table selTable=null;
		while (selTable==null) {
			String strTableId = Utility.inputString("Enter Table ID: ");
			for (Table table : listTable) {
				if (table.getId().equalsIgnoreCase(strTableId)) {
					selTable = table;
				}
			}
		}
		return selTable;
	}
	
	public static Booking inputBookingByIndex(ArrayList<Booking> bookings) {
		int min = 1;
		int max = bookings.size();
		String msg = String.format("Enter index [%d-%d]: ", min, max); 
		int index = Utility.inputInt(msg, min, max);
		return bookings.get( index-1 );
	}

	
	
	// Date related
	public static boolean isValidDate(String strDate) {
		// format dd/mm/yyyy
        String regex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$"; 
        Pattern pattern = Pattern.compile(regex); 
        Matcher matcher = pattern.matcher(strDate); 
        return matcher.matches();
	}
	
	public static boolean isValidTime(String strTime) {
        String regex = "^([0-1]\\d|2[0-3]):([0-5]\\d)$"; 
        Pattern pattern = Pattern.compile(regex); 
        Matcher matcher = pattern.matcher(strTime); 
        return matcher.matches();
	}
	
	public static Date parseDate(String format, String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(strDate);
		} catch (Exception e) {
			System.out.println("Err: error parsing date");
			System.out.println(String.format("Entered date: %s", strDate));			
			return null;
		}
	}
	
	public static String formatDate(String format, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	

	
	public static char getSession(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hr = calendar.get(Calendar.HOUR_OF_DAY);
//		System.out.println("");
		if (hr>=AM_START && hr<AM_END) {
			return 'a';
		} else if (hr>=PM_START && hr<PM_END) {
			return 'p';
		} else {
			return 'n'; // n/a not applicable
		}
	}
	
	public static boolean isAfterLastActionTime(Date date, char session) {
		if (session != 'a' && session != 'p') {
			System.out.println("Warning: Invalid session("+session+")");
		}
		
		Calendar calendar = Calendar.getInstance();
		switch (session) {
		case 'a':
			calendar.setTime( Utility.changeTime(date, AM_END, 0) );
			calendar.add(Calendar.MINUTE, -LAST_ACTION_TIME_MIN);
			break;
		case 'b':
			calendar.setTime( Utility.changeTime(date, PM_END, 0) );
			calendar.add(Calendar.MINUTE, -LAST_ACTION_TIME_MIN);
		}
		return date.after(calendar.getTime());
	}
	
	public static Date getToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getTomorrow() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getToday());
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTime();
	}
	
	public static Date getMonthLater() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getToday());
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTime();
	}
	
//	public static Date createDate(int year, int month, int day, int hr, int min) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.YEAR, year);
//		calendar.set(Calendar.MONTH, month);
//		calendar.set(Calendar.DAY_OF_MONTH, day);
//		calendar.set(Calendar.HOUR_OF_DAY, hr);
//		calendar.set(Calendar.MINUTE, min);
//		calendar.set(Calendar.SECOND, 0);
//		calendar.set(Calendar.MILLISECOND, 0);
//		return calendar.getTime();
//	}
	
//	public static Date createDate(int year, int month, int day) {
//		return createDate(year, month, day, 0, 0);
//	}
	
	public static Date changeTime(Date date, int hr, int min) {
		if (IS_TESTING) {
			System.out.println(String.format("[Test] changeTime= date:%s, hr:%d, min:%d", formatDate(FORMAT_DATETIME_DETAIL, date), hr, min));
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hr);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		//date.setTime(calendar.getTimeInMillis());
		return calendar.getTime();
	}

	public static boolean isSameDate(Date date1, Date date2) {
		/*
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH); // Note: zero based!
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.setTime(date2);
		return year == calendar.get(Calendar.YEAR) 
			&&  month == calendar.get(Calendar.MONTH)
			&&  day ==calendar.get(Calendar.DAY_OF_MONTH);
		*/
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
		return sdf.format(date1).equals(sdf.format(date2));
	}
	
	public static boolean isToday(Date date) {
		// maybe should use calendar object to check
		return isSameDate(new Date(), date);
	}
	
	public static boolean isBeforeNow(Date date) {
		Date now = new Date();
		return date.before(now);
	}
	
	public static boolean isBeforeToday(Date date) {
		return date.before( getToday() );
	}
	
	public static boolean isBetween(Date start, Date end, Date date) {
		return date.after(start) && date.before(end);
	}
}
