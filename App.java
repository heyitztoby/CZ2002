package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import app.dataManager.BookingManager;
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
import app.model.Report;
import app.model.Table;
import app.util.Utility;

public class App implements AppConstants, OptionsSelector.OnSelectOptionCallback {
	public static void main(String[] args) {
		new App().run();
	}

	Order selectedOrder;
	
	// menu selector
	OptionsSelector optsMain;
	OptionsSelector optsOrder;
	OptionsSelector optsOrderActions;
	OptionsSelector optsBooking;
	OptionsSelector optsSetting;
	OptionsSelector optsEditMenuItem;

	public void run() {		
		Restaurant.getInstance();
//		if (!IOUtility.loadData()) {
//			 TODO [Enhance] App.FirstUse: allow user key in store info 
//		};
		Restaurant.getInstance().initWithDefaultData();
		if (IS_TESTING) {
			Restaurant.getInstance().importDummyData();
		}
		
		// select stuff

		initMenu();
		optsMain.askOptions();
//		IOUtility.saveData();
		System.out.println("Program ended.");
	}
	
	public void initMenu() {
		// Create Main Menu
		String headerMain = "==================================================\n" 
				+ "Welcome to <Store Name>\n"
				+ "==================================================";
		String[] arMenuMain = new String[] { 
				"Order", 
				"Reservation", 
				"Table", 
				"Report", 
				"Setting" 
		};
		optsMain = new OptionsSelector(REQ_CODE_MENU_MAIN, arMenuMain);
		optsMain.setOnSelectOptionCallback(this);
		optsMain.setHeader(headerMain);
		
		// order
		String[] arMenuOrder = new String[] {
				"Display list orders",
				"Create order",
				"Select order"
		};
		optsOrder = new OptionsSelector(REQ_CODE_MENU_ORDER, arMenuOrder);
		optsOrder.setOnSelectOptionCallback(this);
		
		// order action
		String[] arMenuOrderAction = new String[] { 
				"Display details",
				"Add item",
				"Update item quantity",
				"Delete item",
				"Print bill invoice",
				"Delete order"
		};
		optsOrderActions = new OptionsSelector(REQ_CODE_MENU_ORDER_ACTION, arMenuOrderAction);
		optsOrderActions.setOnSelectOptionCallback(this);
		
		// booking
		String[] arMenuBooking = new String[] { 
				"Create reservation", 
				"Display reservation",
				"Create order for reservation", 
				"Search by contact", 
				"Remove reservation" 
		};
		optsBooking = new OptionsSelector(REQ_CODE_MENU_BOOKING, arMenuBooking);
		optsBooking.setOnSelectOptionCallback(this);
		
		// setting
		String[] arMenuSetting = new String[] { 
//				"Edit store info", 
//				"Edit stuff", 
				"Edit menu items"
//				"Edit set promotion" 
		};
		optsSetting = new OptionsSelector(REQ_CODE_MENU_SETTING, arMenuSetting);
		optsSetting.setOnSelectOptionCallback(this);
		
		// edit menu item
		String [] arEditMenuItem = new String[] {
				"Display menu",
				"Add menu item",
				"Add promotion item",
				"Edit menu item",
				"Delete menu item"
		};
		optsEditMenuItem = new OptionsSelector(REQ_CODE_MENU_EDIT_MENU_ITEM, arEditMenuItem);
		optsEditMenuItem.setOnSelectOptionCallback(this);
	}


	@Override
	public void onSelected(int requestCode, int selection) {
		switch (requestCode) {
		case REQ_CODE_MENU_MAIN:
			handleMainMenu(selection);
			break;
		case REQ_CODE_MENU_ORDER:
			handleOrderMenu(selection);
			break;
		case REQ_CODE_MENU_ORDER_ACTION:
			handleOrderActionsMenu(selection);
			break;
		case REQ_CODE_MENU_BOOKING:
			handleBookingMenu(selection);
			break;
//		case REQ_CODE_MENU_REPORT:
//			handleReportMenu(selection);
//			break;
		case REQ_CODE_MENU_SETTING:
			handleSettingMenu(selection);
			break;
		case REQ_CODE_MENU_EDIT_MENU_ITEM:
			handleEditMenuItemMenu(selection);
			break;
//		case REQ_CODE_MENU_EDIT_PROMO_ITEM:
//			handleEditPromoItemMenu(selection);
//			break;
		default:
			System.out.println(String.format("onSelected: ReqCode(%d), sel(%d) is incomplete", requestCode, selection));
			break;
		}

	}
	
	public void handleMainMenu(int selection) {
		switch (selection) {
		case 1: // on Order selected
			optsOrder.askOptions();
			break;

		case 2: // booking selected
			optsBooking.askOptions();
			break;

		case 3: // table selected
			Date selectedDate = Utility.inputDate("Enter date [today or DD/MM/YYYY]: ");
			if (Utility.isToday(selectedDate)) {
				Restaurant.getInstance().getTableMgr().displayTablesStatus();
			}else if ( Utility.isBeforeNow(selectedDate) ) {
				System.out.println("Not able to display on past history");
			}else {
				Restaurant.getInstance().getTableMgr().displayTablesStatus(selectedDate);
			}
			break;
			
		case 4:
			// TODO MainMenu.report: need to add user interaction
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			
			Report report = Restaurant.getInstance().getInvoicesMgr().getReport(calendar.getTime(), new Date());
			report.display();
			break;
			
		case 5: // show setting menu
			optsSetting.askOptions();
			break;

		default:
			System.out.println(String.format("[Main] Option (%d) incomplete", selection));
			break;
		}
	}
	
	public void handleOrderMenu(int selection) {
		selectedOrder = null;
		switch (selection) {
		case 1:
			Restaurant.getInstance().getOrderMgr().displayOrders();
			break;
			
		case 2:
			doCreateWalkinOrder();
			break;

		case 3:
			selectedOrder = doSelectOrderByTable();
			optsOrderActions.askOptions();
			break;

		default:
			System.out.println(String.format("[Order] Option (%d) incomplete", selection));
			break;
		}
	}
	
	public void handleOrderActionsMenu(int selection) {
		switch (selection) {
		case 1: // display detail
			selectedOrder.displayDetail();
			System.out.println();
			break;

		case 2:// add item
			Restaurant.getInstance().getMenu().displayMenu();
			doAddOrderItemToOrder(selectedOrder);
			break;
			
		case 3: // update item
			doUpdateOrderItem(selectedOrder);
			break;
		case 4: // remove item
			doDeleteOrderItem(selectedOrder);
			break;
			
		case 5: // print bill invoice
			doPrintInvoice();
			break;
		
		case 6:
			if (Utility.inputBoolean("Delete current order [y/n]? ")) {
				Restaurant.getInstance().getOrderMgr().removeOrder(selectedOrder);
				selectedOrder = null;
				optsOrderActions.stopSelection();
			}
			break;
			
		default:
			System.out.println(String.format("[Order Action] Option (%d) incomplete", selection));
			break;
		}
	}
	
	
	

	public void handleBookingMenu(int selection) {
		Date date;
		switch (selection) {
		case 1:
			doCreateBooking();
			break;
			
		case 2:
			if (Utility.inputBoolean("Display all reservation [y/n]? ")) {
				Restaurant.getInstance().getBookingMgr().displayBookingByDate(null);
			} else {
				date = Utility.inputDate("Enter date [today or DD/MM/YYYY]: ");
				Restaurant.getInstance().getBookingMgr().displayBookingByDate(date);
			}
			break;
			
		case 3://Create order for reservation
			doCreateBookedOrder();
			break;
			
		case 4: //Search by contact
			String contact = Utility.inputString(String.format("Enter contact [max %d char]: ", LENGTH_BOOKING_CONTACT), LENGTH_BOOKING_CONTACT);
			Restaurant.getInstance().getBookingMgr().displayBookingByContact(contact);
			break;
			
		case 5: //  Remove reservation
			doRemoveBooking();
			break;
		default: 
			System.out.println(String.format("[Order] Option (%d) incomplete", selection));
			break;
		}
	}
	

//	public void handleReportMenu(int selection) {
//		// TODO UI.Report: add user selection and input
//	}
	
	public void handleSettingMenu(int selection) {
		// TODO [Enhance] UI.SettingMenu: add ability to change store or stuff
		switch (selection) {
		case 1:
			optsEditMenuItem.askOptions();
			break;

		default:
			System.out.println("[Setting] fn("+selection+") incomplete");
			break;
		}		
	}
	
	public void handleEditMenuItemMenu(int selection) {
		// TODO UI.EditMenuItem to be complete
		switch (selection) {
		case 1:
			Restaurant.getInstance().getMenu().displayMenu();
			break;
			
		case 2: // add menu item
			doAddNewMenuItem();
			break;
			
		case 3:
			doAddNewPromoItem();
			break;
			
		case 4: // edit item
			doUpdateMenuItem();
			break;
			
		case 5: // delete item
			doDeleteMenuItem();
			break;
		default:
			System.out.println("[Edit Menu] fn("+selection+") incomplete");
			break;
		}		
	}
	


//	public void handleEditPromoItemMenu(int selection) {
//		// TODO: UI.EditPromoItem to be complete
//		switch (selection) {
//		case 1:
//			Restaurant.getInstance().getMenu().displayOnlyPromo();
//			break;
//
//		default:
//			System.out.println("[Edit Promo] fn("+selection+") incomplete");
//			break;
//		}
//	}
	
	
	
	public void displayOperationHours() {
		System.out.println(String.format("AM: %d:00 to %d:00", AM_START, AM_END));
		System.out.println(String.format("PM: %d:00 to %d:00", PM_START, PM_END));
		System.out.println(String.format("Last order/booking will be %d mins before end session", LAST_ACTION_TIME_MIN));
	}
	
	
	private boolean canMakeOrder(Date date, char session) {
		if (Restaurant.getInstance().getMenu().getItems().size() == 0) {
			System.out.println("Unable to create order as no menu item found");
			System.out.println("Please update menu at Setting > Menu ");
			return false;
		}
		if (session != 'a' && session != 'p' ) {
			System.out.println("Now is not operating hour, not allow to create order");
			displayOperationHours();
			System.out.println();
			return false;
		}
		if (Utility.isAfterLastActionTime(date, session)) {
			System.out.println("After last order time. not allow to create order");
			displayOperationHours();
			return false;
		}
		return true;
	}
	private void doCreateWalkinOrder() {
		TableManager tableMgr = Restaurant.getInstance().getTableMgr();
		OrderManager orderMgr = Restaurant.getInstance().getOrderMgr();
		Menu menu = Restaurant.getInstance().getMenu();
		Date now = new Date();
		char session = Utility.getSession(now);
		
		if (!canMakeOrder(now, session)) {
			return;
		}
		
		int pax = Utility.inputIntAtLeast("Enter pax [>=1]: ", 1);
		Restaurant.getInstance().getTableMgr().displayFreeTable(now, session, pax);
		ArrayList<Table> freeTables = tableMgr.getAvailableTables(now, session, pax);
		if (freeTables.size() == 0) {
			System.out.println("No remaining table");
			return;
		}
		Table selTable = Utility.inputTableById(freeTables);
		Order order = new Order(pax, selTable);
		if (Utility.inputBoolean("Start taking order [y/n]? ")) {
			menu.displayMenu();
			doAddOrderItemToOrder(order);
		}
		orderMgr.addOrder(order);
		System.out.println("Order created.");
		order.displayDetail();
		System.out.println();
	}
	
	private void doPrintInvoice() {
		if (selectedOrder.getItems().size() == 0) {
			System.out.println("No order item found.");
		}else {
			selectedOrder.displayDetail();
			System.out.println();
			if (!Utility.inputBoolean("Proceed to payment [y/n]? ")) {
				return;
			}
			double total = Utility.round(selectedOrder.calFinalPrice());
			String msg = String.format("Enter paid amount [>=%.2f]: ", total);
			double amtPaid = Utility.inputDoubleAtLeast(msg, total);
			// update hint for insufficient amount?
			
			// print invoice 
			System.out.println();
			System.out.println("<Store details>");
			selectedOrder.displayDetail();
			System.out.println("---------------------------------------");
			System.out.println(String.format("Payment\t\t\t\t %6.2f", amtPaid));
			System.out.println(String.format("Changes\t\t\t\t %6.2f\n", amtPaid-total));
			
			// remove from order list, add into invoice manager
			Restaurant.getInstance().getOrderMgr().removeOrder(selectedOrder);
			Restaurant.getInstance().getInvoicesMgr().addInvoice( new Invoice(selectedOrder, true));
			selectedOrder = null;
			optsOrderActions.stopSelection();
		}
	}
	
	private void doCreateBooking() {
		// ask date time: tomorrow <= inputDate <= today+1M
		Date tmr = Utility.getTomorrow();
		Date monthLater = Utility.getMonthLater();
		System.out.println(String.format("Valid date between %s to %s", Utility.formatDate(FORMAT_DATE, tmr), Utility.formatDate(FORMAT_DATE, monthLater)));
		Date date = Utility.inputDate("Enter date of booking [DD/MM/YYYY]: ", tmr, monthLater);
		displayOperationHours();
		date = Utility.inputTimeWithinSession("Enter arrival time [HH:MM or q=quit]: ", date);
		if (date == null) {
			System.out.println("Create booking cancelled\n");
			return;
		}
		int pax = Utility.inputIntAtLeast("Enter pax: ", 1);
		// ask table
		TableManager tableMgr = Restaurant.getInstance().getTableMgr();
		Table table;
		tableMgr.displayFreeTable(date, Utility.getSession(date), pax);
		ArrayList<Table> freeTables = tableMgr.getAvailableTables(date, Utility.getSession(date), pax);
		if (freeTables.size() > 0) {
			table = Utility.inputTableById(freeTables);
			String name    = Utility.inputString(String.format("Enter name [max %d char]: ", LENGTH_BOOKING_NAME), LENGTH_BOOKING_NAME);
			String contact = Utility.inputString(String.format("Enter contact [max %d char]: ", LENGTH_BOOKING_CONTACT), LENGTH_BOOKING_CONTACT);
			
			// add into bookingMgr
			Booking booking = new  Booking(name, contact, pax, table, date);
			Restaurant.getInstance().getBookingMgr().addBooking(booking);
			System.out.println("Booking created!");
			booking.display();
			System.out.println();
		}
		
	}
	
	private void doCreateBookedOrder() {
		BookingManager bookingMgr = Restaurant.getInstance().getBookingMgr();
		OrderManager orderMgr = Restaurant.getInstance().getOrderMgr();
		Menu menu = Restaurant.getInstance().getMenu();
		// check operating hour
		Date date = new Date();
		char session = Utility.getSession(date);
		if (!canMakeOrder(date, session)) {
			return;
		}
		
		// display today's booking
		ArrayList<Booking> bookings = bookingMgr.getBookingsByDate(date);
		if (bookings == null || bookings.size() == 0) {
			System.out.println("No booking found");
			return;
		}
		bookingMgr.displayBookingByDate(date);
		Booking booking = Utility.inputBookingByIndex(bookings);
		
		// convert into order
		Order order = new Order(TYPE_RESERVATION, booking.getPax(), booking.getTable());
		if (Utility.inputBoolean("Start taking order [y/n]? ")) {
			menu.displayMenu();
			doAddOrderItemToOrder(order);
		}
		orderMgr.addOrder(order);
		bookingMgr.removeBooking(booking);
		
		System.out.println("Order created!");
		order.displayDetail();
		System.out.println();
	}
	
	private void doRemoveBooking() {
		BookingManager bookingMgr = Restaurant.getInstance().getBookingMgr();
		ArrayList<Booking> bookings = bookingMgr.getBookingsByDate(null);
		if (bookings.size() == 0) {
			System.out.println("No booking found");
			return;
		}
		while(bookings.size() > 0) {
			bookingMgr.displayBookingByDate(null);
			Booking booking = Utility.inputBookingByIndex(bookings);
			if (Utility.inputBoolean("Confirm delete %s [y/n]? ")) {
				bookingMgr.removeBooking(booking);
				bookings.remove(booking);
			}
			if (bookings.size() == 0) {
				System.out.println("No remaining booking");
				return;
			}
			if (!Utility.inputBoolean("Continue [y/n]? ")) {
				break;
			}
		}
	}
	
	
	private Order doSelectOrderByTable() {
		OrderManager orderMgr = Restaurant.getInstance().getOrderMgr();
		orderMgr.displayOrders();
		ArrayList<Table> inUseTables = orderMgr.getOccupiedTable();
		Table table = Utility.inputTableById(inUseTables);
		return orderMgr.getOrderByTable(table);
	}
	
	private void doAddOrderItemToOrder(Order order) {
		OrderItem orderingItem;
		do {
			// ask for input for menu
			MenuItem menuItem = Utility.inputMenuItemById();
			
			// ask for quantity for order
			int quantity = 0;
			if (order.hasMenuItem(menuItem)) {
				System.out.println("Previously ordered!");
				System.out.println(order.getOrderItem(menuItem));
				quantity = Utility.inputIntAtLeast("Enter new quantity [>=1]: ", 1);
				
				orderingItem = order.getOrderItem(menuItem);
				orderingItem.setQuantity(quantity);
			}else {
				quantity = Utility.inputIntAtLeast("Enter quantity [>=1]: ", 1);
				
				orderingItem = new OrderItem(menuItem, quantity);
				order.getItems().add(orderingItem);
			}
		} while(Utility.inputBoolean("Continue [y/n]?"));
		System.out.println();
	}
	
	private void doUpdateOrderItem(Order order) {
		System.out.println("Update order item");
		if (order.getItems().size() == 0) {
			System.out.println("No items under this order");
			return;
		}
		
		order.displayListItems(true);
		int min = 1;
		int max = order.getItems().size();
		String msg = String.format("Select item by index [%d-%d]: ", min, max);
		
		// loop update quantity of item
		do {
			int idx = Utility.inputInt(msg, min, max) -1;
			OrderItem orderItem = order.getItems().get(idx);
			System.out.println(String.format("%d x %s", orderItem.getQuantity(), orderItem.getItem().getName()));
			int quantity = Utility.inputIntAtLeast("Enter new quantity [>=1]: ", 1);
			orderItem.setQuantity(quantity);
		} while (Utility.inputBoolean("Continue [y/n]?"));
		System.out.println();
	}
	
	private void doDeleteOrderItem(Order order) {
		System.out.println("Remove order item");
		order.displayListItems(true);
		if (order.getItems().size() == 0) {
			System.out.println("No item to delete");
			return;
		}

		while (order.getItems().size() > 0) {
			int min = 1;
			int max = order.getItems().size();
			String msg = String.format("Select item by index [%d-%d]: ", min, max);
			int idx = Utility.inputInt(msg, min, max) -1;
			OrderItem orderItem = order.getItems().get(idx);

			
			msg = String.format("Deleting %d x %s [y/n]: ", orderItem.getQuantity(), orderItem.getItem().getName());
			if (Utility.inputBoolean(msg)) {
				order.getItems().remove(orderItem);
			}
			if (order.getItems().size() == 0) {
				System.out.println("No remaining item");
				break;
			}
			if ( !Utility.inputBoolean("Continue [y/n]?")) {
				break;
			}
		}
	}
	
	
	private void doAddNewMenuItem() {
		Menu menu = Restaurant.getInstance().getMenu();
		String type = Utility.inputString(String.format("Enter type [max %d char]: ", LENGTH_MENU_ITEM_TYPE), LENGTH_MENU_ITEM_TYPE);
		String id = Utility.inputString(String.format("Enter id [max %d char]: ", LENGTH_MENU_ITEM_ID), LENGTH_MENU_ITEM_ID);
		while (menu.isExistId(id)) {
			System.out.println("Id is existed!");
			id = Utility.inputString(String.format("Enter id [max %d char]: ", LENGTH_MENU_ITEM_ID), LENGTH_MENU_ITEM_ID);
		}
		String name = Utility.inputString(String.format("Enter name [max %d char]: ", LENGTH_MENU_ITEM_NAME), LENGTH_MENU_ITEM_NAME);
		String description = Utility.inputString(String.format("Enter description [max %d char]: ", LENGTH_MENU_ITEM_DESC), LENGTH_MENU_ITEM_DESC);
		double price = Utility.inputDoubleAtLeast(String.format("Enter price [>=%.2f]: ", 0.1), 0.1);
		
		menu.addItem(new MenuItem(id.toUpperCase(), Utility.capitalize(type), name, description, price));
		System.out.println("New Item added into menu");
	}
	
	private void doAddNewPromoItem() {
		Menu menu = Restaurant.getInstance().getMenu();
//		String type = Utility.inputString(String.format("Enter type [max %d char.]: ", LENGTH_MENU_ITEM_TYPE), LENGTH_MENU_ITEM_TYPE);
		String type = "Promo";
		String id = Utility.inputString(String.format("Enter id [max %d char]: ", LENGTH_MENU_ITEM_ID), LENGTH_MENU_ITEM_ID);
		while (menu.isExistId(id)) {
			System.out.println("Id is existed!");
			id = Utility.inputString(String.format("Enter id [max %d char]: ", LENGTH_MENU_ITEM_ID), LENGTH_MENU_ITEM_ID);
		}
		String name = Utility.inputString(String.format("Enter name [max %d char]: ", LENGTH_MENU_ITEM_NAME), LENGTH_MENU_ITEM_NAME);
//		String description = Utility.inputString(String.format("Enter description [max %d char]: ", LENGTH_MENU_ITEM_DESC), LENGTH_MENU_ITEM_DESC);
		double price = Utility.inputDoubleAtLeast(String.format("Enter price [>=%.2f]: ", 0.1), 0.1);
		
		PromotionItem promoItem = new PromotionItem(id.toUpperCase(), Utility.capitalize(type), name, "", price);
		doAddPromoCombinations(promoItem);
		promoItem.setDescription(Arrays.deepToString(promoItem.getCombination().toArray()));
		
		menu.addItem(promoItem);
		System.out.println("New Item added into menu");
	}
	
	private void doAddPromoCombinations(PromotionItem promoItem) {
		Restaurant.getInstance().getMenu().displayNoPromo();		
		while(true ) {
			MenuItem item = Utility.inputMenuItemById();
			if (item instanceof PromotionItem) {
				continue;
			}
			if (promoItem.getCombination().contains(item)) {
				System.out.println(item.getName()+"is existed");
				continue;
			}
			promoItem.getCombination().add(item);
			if (!Utility.inputBoolean("Continue [y/n]?")) {
				break;
			}
		}
//		promoItem.setDescription(Arrays.deepToString(promoItem.getCombination().toArray()));
	}
	
	private void doUpdateMenuItem() {
		Menu menu = Restaurant.getInstance().getMenu();
		menu.displayMenu();
		MenuItem item = Utility.inputMenuItemById();
		
		System.out.println("Previous name: "+ item.getName());
		String name = Utility.inputString(String.format("Enter new name [s=skip, max %d char]: ", LENGTH_MENU_ITEM_NAME), LENGTH_MENU_ITEM_NAME);
		if (!name.equalsIgnoreCase("s")) {
			item.setName(name);
		}
		
		System.out.println("Previous description: "+ item.getDescription());
		String desc = Utility.inputString(String.format("Enter new description [s=skip, max %d char]: ", LENGTH_MENU_ITEM_DESC), LENGTH_MENU_ITEM_DESC);
		if (!desc.equalsIgnoreCase("s")) {
			item.setDescription(desc);
		}
		
		System.out.println("Previous price: "+ item.getPrice());
		double price = Utility.inputDoubleAtLeast(String.format("Enter price [>=%.2f]: ", 0.1), 0.1);
		item.setPrice(price);
		
		if (item instanceof PromotionItem) {
			if (Utility.inputBoolean("Reset combination [y/n]? ")) {
				PromotionItem promoItem = (PromotionItem) item;
				promoItem.getCombination().clear();
				doAddPromoCombinations(promoItem);
			}
		}
	}
	
	private void doDeleteMenuItem() {
		Menu menu = Restaurant.getInstance().getMenu();
		menu.displayMenu();
		while (menu.getItems().size() > 0) {
//			System.out.println(Arrays.deepToString(menu.getItems().toArray()));
			MenuItem item = Utility.inputMenuItemById();
			if (Utility.inputBoolean(String.format("Delete %s-%s [y/n]?", item.getId(), item.getName()))) {
				menu.removeItem(item);
			}
			if (menu.getItems().size() == 0) {
				System.out.println("No remaining item");
				break;
			}
			if (!Utility.inputBoolean("Continue [y/n]?")) {
				break;
			}
		}
	}
	


}
