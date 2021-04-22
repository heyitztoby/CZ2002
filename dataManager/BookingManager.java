package app.dataManager;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import app.AppConstants;
import app.model.Booking;
import app.model.Table;
import app.util.Utility;

public class BookingManager implements AppConstants{
	private ArrayList<Booking> bookings;

	public BookingManager(ArrayList<Booking> listBookings) {
		this.bookings = listBookings;
		clearExpiredBookings();
		sort();
	}
	
	public BookingManager() {
		this(new ArrayList<Booking>());
	}
	
	public void addBooking(Booking booking) {
		this.bookings.add(booking);
		sort();
	}
	
	public void removeBooking(Booking booking) {
		this.bookings.remove(booking);
	}
	
	public ArrayList<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(ArrayList<Booking> bookings) {
		this.bookings = bookings;
		clearExpiredBookings();
		sort();
	}

	public ArrayList<Booking> getBookingsByPhone(String phone){
		clearExpiredBookings();
		
		ArrayList<Booking> list = new ArrayList<Booking>();
		for (Booking booking : this.bookings) {
			if (booking.getContact().equalsIgnoreCase(phone)) {
				list.add(booking);
			}
		}
		return list;
	}
	
	
	public ArrayList<Booking> getBookingsByDate(Date date){
		clearExpiredBookings();
		
		// when date == null, get all bookings
		ArrayList<Booking> list = new ArrayList<Booking>();
		for (Booking booking : this.bookings) {
			if (date == null) { 
				list.add(booking);
			}else if (Utility.isSameDate(date, booking.getArrivalTime())) {
				list.add(booking);
			}
		}
		return list;
	}
	

	
	public ArrayList<Table> getOccupiedTables(Date date, char session){
		ArrayList<Table> listTables = new ArrayList<Table>();
		if (session == 'a' || session == 'p') {
			ArrayList<Booking> listBookings = getBookingsByDate(date);
			for (Booking booking : listBookings) {
				if (Utility.getSession(booking.getArrivalTime()) == session) {
					listTables.add(booking.getTable());
				}
			}
		}
		return listTables;
		

	}
	
	public void clearExpiredBookings() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.MINUTE, -BOOKING_EXPIRE_TIME_MIN);
//		Date dateExpired = calendar.getTime();
//		Booking curBooking;
//		for (int i = bookings.size()-1; i >=0; i--) {
//			curBooking = bookings.get(i);
//			if (curBooking.getArrivalTime().before(dateExpired)) {
//				if (AppConstants.IS_TESTING) {
//					System.out.println(String.format("[Test] Booking<%s, %s, %s> is expired", curBooking.getName(), curBooking.getContact(), curBooking.getArrivalTime()));					
//				}
//				this.bookings.remove(curBooking);
//			}
//			
//		}
	}
	
	public void sort() {
		Collections.sort(this.bookings);
	}
	
	public void displayBookingByDate(Date date) {
		Booking curBooking;
		ArrayList<Booking> listBooking = getBookingsByDate(date);
		if (listBooking.size() == 0) {
			System.out.println("No booking record found");
			return;
		}
		System.out.println(String.format("%-4s %-8s %-5s %-15s %10s  \t%-3s \t%-3s", "No.", "Date", "Time", "Name", "Contact", "Pax", "Table"));
		for (int i=0; i<listBooking.size(); i++) {
			curBooking = listBooking.get(i);
			System.out.println(String.format("[%2d] %-8s %-5s %-15s %10s  \t%d \t%-3s", 
					i+1, 
					Utility.formatDate(FORMAT_DATE_SHORT, curBooking.getArrivalTime()), 
					Utility.formatDate(FORMAT_TIME, curBooking.getArrivalTime()), 
					curBooking.getName(), 
					curBooking.getContact(), 
					curBooking.getPax(), 
					curBooking.getTable().getId() ));
		}
	}
	
	public void displayBookingByContact(String contact) {
		if (contact == null) {
			System.out.println("No contact provided");
			return;
		}
		ArrayList<Booking> listBooking = getBookingsByPhone(contact);
		if (listBooking.size() == 0) {
			System.out.println("No booking record found");
			return;
		} 
		Booking curBooking;
		System.out.println(String.format("%-4s %-8s %-5s %-15s %10s  \t%-3s \t%-3s", "No.", "Date", "Time", "Name", "Contact", "Pax", "Table"));
		for (int i=0; i<listBooking.size(); i++) {
			curBooking = listBooking.get(i);
			System.out.println(String.format("[%2d] %-8s %-5s %-15s %10s  \t%d \t%-3s", 
					i+1, 
					Utility.formatDate(FORMAT_DATE_SHORT, curBooking.getArrivalTime()), 
					Utility.formatDate(FORMAT_TIME, curBooking.getArrivalTime()), 
					curBooking.getName(), 
					curBooking.getContact(), 
					curBooking.getPax(), 
					curBooking.getTable().getId() ));
		}
		
	}

}
