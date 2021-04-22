package app.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import app.AppConstants;
import app.dataManager.BookingManager;
import app.dataManager.InvoiceManager;
import app.dataManager.Menu;
import app.dataManager.OrderManager;
import app.dataManager.Restaurant;
import app.dataManager.TableManager;
import app.model.Booking;
import app.model.Invoice;
import app.model.MenuItem;
import app.model.Order;
import app.model.OrderItem;
import app.model.PromotionItem;
import app.model.Staff;
import app.model.Table;

/**
 * Data Utility handles data related operation like
 * <ul>
 * <li>Reading data from file</li>
 * <li>Writing data to file</li>
 * <li>Generating test data for Order/Booking/Invoices</li>
 * </ul>
 * @author ChiaYu
 */
public class DataUtility implements AppConstants{
	/**
	 * Restore data from the data file.<br>
	 * Procedure:
	 *<ol>
	 *<li>checking if data folder is existed</li>
	 *<li>checking if data file is existed</li>
	 *<li>restore from file</li>
	 *</ol>
	 *@return boolean - whether data is successfully loaded
	 *@see AppConstants#PATH_DATA_FOLDER
	 */
	@SuppressWarnings("unchecked")
	public static boolean loadData() {
		boolean isSuccess = false;
		File dataFolder = new File(PATH_DATA_FOLDER);
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
			return false;
		}
		
		File dataFile = new File(dataFolder, DATA_FILE_NAME);
		if (!dataFile.exists()) {
			return false;
		}
		
		Object[] data;
		FileInputStream fis;
		ObjectInputStream ois;
		
		try {
			fis = new FileInputStream(dataFile.toString());
			ois = new ObjectInputStream(fis);
			data = (Object[]) ois.readObject();
			if (data != null) {
				Restaurant restaurant = Restaurant.getInstance();
				restaurant.setStaffs( (ArrayList<Staff>) data[0] );
				restaurant.getMenu().setItems( (ArrayList<MenuItem>) data[1]  );
				restaurant.getTableManager().setTables( (ArrayList<Table>) data[2] );
				restaurant.getInvoiceManager().setInvoices( (ArrayList<Invoice>) data[3] );
				restaurant.getOrderManager().setOrders( (ArrayList<Order>) data[4] );
				restaurant.getBookingManager().setBookings( (ArrayList<Booking>) data[5] );
			}
			ois.close();
			isSuccess = true;
		} catch (Exception e) {
			if (IS_TESTING) {
				e.printStackTrace();
			}
		} 
		return isSuccess;
	}
	/**
	 * Saves application data into data file
	 * Write data to file
	 * <ol>
	 * <li>create data folder if it is not existed</li>
	 * <li>write to file</li>
	 * </ol>
	 * @return boolean - whether data is successfully loaded
	 * @see AppConstants#PATH_DATA_FOLDER
	 */
	public static boolean saveData() {
		boolean isSuccess = false;
		// check date folder
		File dataFolder = new File(PATH_DATA_FOLDER);
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
		
		// prepare writing to file
		Restaurant restaurant = Restaurant.getInstance();
		Object[] data = {
				restaurant.getStaffs(),
				restaurant.getMenu().getItems(),
				restaurant.getTableManager().getTables(),
				restaurant.getInvoiceManager().getInvoices(),
				restaurant.getOrderManager().getOrders(),
				restaurant.getBookingManager().getBookings()
		};
		
		File dataFile = new File(dataFolder, DATA_FILE_NAME);
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = new FileOutputStream(dataFile.toString());
			oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.close();
			isSuccess = true;
		} catch (Exception e) {
			if (IS_TESTING) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}
	/**
	 * Load staff data
	 */
	public static void loadDefaultStaff() {
		ArrayList<Staff> staffs = new ArrayList<Staff>();
		staffs.add( new Staff("kong0126", 'M', "Alson", "Cashier") );
		staffs.add( new Staff("an0009in", 'M', "Benjamin", "Cashier") );
		staffs.add( new Staff("chsiao001", 'M', "Chia Yu", "Cashier") );
		staffs.add( new Staff("jtan417", 'M', "Jian Wei", "Cashier") );
		staffs.add( new Staff("yching003", 'M', "Yu Xuan", "Cashier") );
		
		Restaurant.getInstance().setStaffs(staffs);
	}
	/**
	 * Load default menu
	 */
	public static void loadWithDefaultMenu() {
		MenuItem m1 = new MenuItem("M01", "Main", "McSpicy", "A Fiery Chicken Burger", 5);
		MenuItem m2 = new MenuItem("M02", "Main", "Big Mac", "A Two All-Beef Patties Burger", 7);
		MenuItem m3 = new MenuItem("M03", "Main", "Filet-O-Fish", "A Fish Fillet Burger", 4);
		MenuItem m4 = new MenuItem("M04", "Main", "McNuggets", "9 Pieces Chicken Nuggets", 4);
		
		MenuItem b1 = new MenuItem("B01", "Drink", "Coca-Cola", "Medium Coke", 3);
		MenuItem b2 = new MenuItem("B02", "Drink", "Green Tea", "Medium Green Tea", 2.5);
		MenuItem b3 = new MenuItem("B03", "Drink", "Milo", "Medium Milo", 2.8);
		MenuItem b4 = new MenuItem("B04", "Drink", "Ribena", "Medium Ribena", 3.5);
		
		MenuItem s1 = new MenuItem("S01", "Side", "Apple Pie", "Crispy Apple Pie", 1);
		MenuItem s2 = new MenuItem("S02", "Side", "Fries", "Crispy Fried French Fries", 3.8);
		
		MenuItem d1 = new MenuItem("D01", "Dessert", "McFlurry", "Mudpie McFlurry Ice Cream Cup", 3);
		MenuItem d2 = new MenuItem("D02", "Dessert", "Vanilla Cone", "Vanilla Ice Cream Cone", 2);

		PromotionItem p1 = new PromotionItem("P01", "Promo", "McSpicy Meal", "", 8);
		p1.getCombination().add(m1);
		p1.getCombination().add(b1);
		p1.getCombination().add(s2);
		p1.setDescription( Arrays.deepToString(p1.getCombination().toArray()) );
		PromotionItem p2 = new PromotionItem("P02", "Promo", "McNuggets Meal", "", 8.2);
		p2.getCombination().add(m4);
		p2.getCombination().add(b2);
		p2.getCombination().add(s2);
		p2.setDescription( Arrays.deepToString(p2.getCombination().toArray()) );
		
		
		ArrayList<MenuItem> list = new ArrayList<MenuItem>();
		list.add(m1);
		list.add(m2);
		list.add(m3);
		list.add(m4);
		list.add(b1);
		list.add(b2);
		list.add(b3);
		list.add(b4);
		list.add(s1);
		list.add(s2);
		list.add(d1);
		list.add(d2);
		list.add(p1);
		list.add(p2);
		Restaurant.getInstance().getMenu().setItems(list);
	}
	/**
	 * Generates dummy data for Bookings
	 * Creates Bookings for AM and PM session with random customer details
	 * Bookings are added to {@link BookingManager} for App to handle the bookings
	 */
	public static void generateBookings() {
		BookingManager bookingMgr = Restaurant.getInstance().getBookingManager(); 
		TableManager tableMgr = Restaurant.getInstance().getTableManager();
		
		// create booking data
		int numBookings = 0;
		int numCustomer = 0;
		Calendar calendar = Calendar.getInstance();
		Booking booking;
		for (int i = 0; i < 5; i++) { // loop day
			numCustomer = 0;
			// for AM
			calendar.set(Calendar.HOUR_OF_DAY, AM_START);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			numBookings = (int) (Math.random() * 10) + 1;
			numBookings = Math.min(numBookings, tableMgr.getTables().size());
			for (int j = 0; j < numBookings; j++) { // loop number of bookings each day
				int phone = (int) (Math.random() * 20000000) + 80000000;
				booking = new Booking("Customer " + ((numCustomer++) + 1), ""+phone, 2, tableMgr.getTables().get(j), calendar.getTime());
				bookingMgr.addBooking(booking);
				calendar.add(Calendar.MINUTE, 10);
			}
			
			// for PM
			calendar.set(Calendar.HOUR_OF_DAY, PM_START);
			numBookings = (int) (Math.random() * 10) + 1;
			numBookings = Math.min(numBookings, tableMgr.getTables().size());
			for (int j = 0; j < numBookings; j++) { // loop number of bookings each day
				int phone = (int) (Math.random() * 20000000) + 80000000;
				booking = new Booking("Customer " + ((numCustomer++) + 1), ""+phone, 2, tableMgr.getTables().get(j), calendar.getTime());
				bookingMgr.addBooking(booking);
				calendar.add(Calendar.MINUTE, 10);
			}
			
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
	}
	/**
	 * Generates dummy data for Orders
	 * Creates Orders and assign them to {@link OrderManager} for handling of Orders
	 * Orders can only be generated when there are more than equal to 1 OrderItem
	 * Dummy orders can only be created during operating session
	 */
	public static void generateOrders() {
		Menu menu = Restaurant.getInstance().getMenu();
		TableManager tableMgr = Restaurant.getInstance().getTableManager();
		OrderManager orderMgr = Restaurant.getInstance().getOrderManager();
		
		Staff user = Restaurant.getInstance().getStaffs().get(0);
		
		if (menu.getItems().size() == 0) {
			System.out.println("Err: No menu item found. Order records are not generated");
			return;
		}
		
		// create order data
		Calendar calendar = Calendar.getInstance();
		char session = Utility.getSession(calendar.getTime());
		if (session == 'a') {
			calendar.set(Calendar.HOUR_OF_DAY, AM_START);
		}else if (session == 'p') {
			calendar.set(Calendar.HOUR_OF_DAY, PM_START);
		} 
		else {
			System.out.println("Err: Not in operating session. Order records are not generated");
			return;
		}
		
		int randNumItems = 0;
		int randQty = 0;
		MenuItem item;
		Order order;
		
		ArrayList<Table> freeTables = tableMgr.getAvailableTables(calendar.getTime(), session);
		if (freeTables.size() == 0) {
			System.out.println("Err: No free table. Order records are not generated");
			return;
		}
		for (int i = 0; i < 5; i++) {
			randNumItems = (int) (Math.random() * 8) + 1;
			randNumItems = Math.min(randNumItems, menu.getItems().size());
			order = new Order(TYPE_WALK_IN, new ArrayList<OrderItem>(), 2, freeTables.get(i), calendar.getTime(), user);
			for (int j = 0; j < randNumItems; j++) {
				item = menu.getItems().get(j);
				randQty = (int) (Math.random() * 3) + 1;
				order.getItems().add(new OrderItem(item, randQty));
			}
			orderMgr.addOrder(order);
			calendar.add(Calendar.MINUTE, 10);
		}
		
		if (IS_TESTING) {
			System.out.println("[Test] order size "+orderMgr.getOrders().size());
			for (Order curOrder : orderMgr.getOrders()) {
				System.out.println(curOrder);
			}
		}
	}
	/**
	 * Generates invoices for testing of application
	 * Invoices generated are random completed orders for testing purposes
	 * @see app.model.Report
	 */
	public static void generateInovices() {
		Menu menu = Restaurant.getInstance().getMenu();
		TableManager tableMgr = Restaurant.getInstance().getTableManager();
		InvoiceManager invoiceMgr = Restaurant.getInstance().getInvoiceManager();
		Staff user = Restaurant.getInstance().getStaffs().get(0);
		
		// create some invoice data
		Invoice curInvoice;
		ArrayList<OrderItem> listItems;
		ArrayList<MenuItem> listMenu = menu.getListMenuItems(false);
		ArrayList<MenuItem> listPromo = menu.getListMenuItems(true);
		int numInvoices;
		int numOrderedItems;
		Calendar calendar = Calendar.getInstance();
		for (int i = 0; i < 10; i++) { // loop on days
			numInvoices = (int) (Math.random() * 10) + 5 ;
			for (int j = 0; j < numInvoices; j++) { // loop on invoices
				listItems = new ArrayList<OrderItem>();
				// standard item
				if (listMenu.size()>0) {
					numOrderedItems = (int) (Math.random() * 10) + 1;
					numOrderedItems = Math.min(numOrderedItems, listMenu.size());
					for (int j2 = 0; j2 < numOrderedItems; j2++) {
						listItems.add( new OrderItem(  listMenu.get(j2), (int) (Math.random() * 3)+1) );
					}
				}
				// promotion items
				if (listPromo.size()>0) {
					numOrderedItems = (int) (Math.random() * 2);
					numOrderedItems = Math.min(numOrderedItems, listPromo.size());
					for (int j2 = 0; j2 < numOrderedItems; j2++) {
						listItems.add( new OrderItem(  listPromo.get(j2), (int) (Math.random() * 3)+1) );
					}
				}
				
				curInvoice = new Invoice(TYPE_WALK_IN, listItems, 2, tableMgr.getTables().get(j), calendar.getTime(), user, true);
				invoiceMgr.addInvoice(curInvoice);
				calendar.add(Calendar.MINUTE, -10);
			}
			calendar.add(Calendar.DAY_OF_YEAR, -1);
		}
	}
	
	
	// [Enhance] Save report to file
	

}
