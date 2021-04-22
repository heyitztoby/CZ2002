package app.dataManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import app.AppConstants;
import app.model.Booking;
import app.model.Invoice;
import app.model.MenuItem;
import app.model.Order;
import app.model.OrderItem;
import app.model.PromotionItem;
import app.util.Utility;

public class Restaurant implements AppConstants{
	private static Restaurant instance = null;
	private BookingManager bookingMgr;
	private OrderManager orderMgr;
	private InvoiceManager invoicesMgr;
	private TableManager tableMgr;
	private Menu menu;
	
	public static Restaurant getInstance() {
		// singleton design pattern
		if (instance==null) {
			instance = new Restaurant();
		}
		return instance;
	}
	
	private Restaurant() {
		this.bookingMgr = new BookingManager();
		this.orderMgr = new OrderManager();
		this.invoicesMgr = new InvoiceManager();
		this.tableMgr = new TableManager();
		this.menu = new Menu();
	}

	public BookingManager getBookingMgr() {
		return bookingMgr;
	}

	public OrderManager getOrderMgr() {
		return orderMgr;
	}

	public InvoiceManager getInvoicesMgr() {
		return invoicesMgr;
	}

	public TableManager getTableMgr() {
		return tableMgr;
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public void initWithDefaultData() {
		initWithDefaultMenu();
		
	}
	
	private void initWithDefaultMenu() {
		MenuItem m1 = new MenuItem("M01", "Main", "McSpicy", "", 5);
		MenuItem m2 = new MenuItem("M02", "Main", "Big Mac", "", 7);
		MenuItem m3 = new MenuItem("M03", "Main", "Filet-O-Fish", "", 4);
		MenuItem m4 = new MenuItem("M04", "Main", "McNuggets", "", 4);
		
		MenuItem b1 = new MenuItem("B01", "Drink", "Coca-Cola", "", 3);
		MenuItem b2 = new MenuItem("B02", "Drink", "Green Tea", "", 2.5);
		MenuItem b3 = new MenuItem("B03", "Drink", "Milo", "", 2.8);
		MenuItem b4 = new MenuItem("B04", "Drink", "Ribena", "", 3.5);
		
		MenuItem s1 = new MenuItem("S01", "Side", "Apple Pie", "", 1);
		MenuItem s2 = new MenuItem("S02", "Side", "Fries", "", 3.8);
		
		MenuItem d1 = new MenuItem("D01", "Desert", "McFlurry", "", 3);
		MenuItem d2 = new MenuItem("D02", "Desert", "Vanilla Cone", "", 2);

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
		getMenu().setItems(list);
	}
	
	
	public void importDummyData() {
		// create booking data
		int randNumItems = 0;
		Calendar calendar = Calendar.getInstance();
		Booking booking;
		for (int i = 0; i < 3; i++) {
			calendar.set(Calendar.HOUR_OF_DAY, 11);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			randNumItems = (int) (Math.random() * 5) + 1;
			for (int j = 0; j < randNumItems; j++) {
				booking = new Booking("Customer" + (j + 1), "87654321", 2, this.tableMgr.getTableById("T" + (j + 1)), calendar.getTime());
				bookingMgr.addBooking(booking);
				calendar.add(Calendar.MINUTE, 10);
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		// create order data
		calendar = Calendar.getInstance();
		char session = Utility.getSession(calendar.getTime());
		if (session == 'a') {
			calendar.set(Calendar.HOUR_OF_DAY, AM_START);
		} else if (session == 'p') {
			calendar.set(Calendar.HOUR_OF_DAY, PM_START);
		}

		randNumItems = 0;
		int randQty = 0;
		MenuItem item;
		Order order;
		for (int i = 0; i < 5; i++) {
			randNumItems = (int) (Math.random() * 8) + 1;
			randNumItems = Math.min(randNumItems, menu.getItems().size());
			order = new Order(TYPE_WALK_IN, new ArrayList<OrderItem>(), 2, tableMgr.getTables().get(i), calendar.getTime());
			for (int j = 0; j < randNumItems; j++) {
				item = menu.getItems().get(j);
				randQty = (int) (Math.random() * 3) + 1;
				order.getItems().add(new OrderItem(item, randQty));
			}
			orderMgr.addOrder(order);
			calendar.add(Calendar.MINUTE, 10);
		}
		
		// create some invoice data
		Invoice curInvoice;
		ArrayList<OrderItem> listItems;
		ArrayList<MenuItem> listMenu = this.menu.getListMenuItems(false);
		ArrayList<MenuItem> listPromo = this.menu.getListMenuItems(true);
		int randNumRecords;
		int randNumOrderItems;
		calendar = Calendar.getInstance();
		for (int i = 0; i < 10; i++) { // loop on days
			randNumRecords = (int) (Math.random() * 5) + 5 ;
			for (int j = 0; j < randNumRecords; j++) { // loop on invoices
				listItems = new ArrayList<OrderItem>();
				// standard item
				if (listMenu.size()>0) {
					randNumOrderItems = (int) (Math.random() * 5) + 1;
					randNumOrderItems = Math.min(randNumOrderItems, listMenu.size());
					for (int j2 = 0; j2 < randNumOrderItems; j2++) {
						listItems.add( new OrderItem(  listMenu.get(j2), (int) (Math.random() * 3)+1) );
					}
				}
				// promotion items
				if (listPromo.size()>0) {
					randNumOrderItems = (int) (Math.random() * 2);
					randNumOrderItems = randNumOrderItems>listPromo.size()?listPromo.size():randNumOrderItems;
					if (randNumOrderItems>0) {
						for (int j2 = 0; j2 < randNumOrderItems; j2++) {
							listItems.add( new OrderItem(  listPromo.get(j2), (int) (Math.random() * 3)+1) );
						}
					}
				}
				
				curInvoice = new Invoice(TYPE_WALK_IN, listItems, 2, this.tableMgr.getTableById("T1"), calendar.getTime(), true);
				this.invoicesMgr.addInvoice(curInvoice);
				calendar.add(Calendar.MINUTE, -10);
			}
			calendar.add(Calendar.DAY_OF_YEAR, -1);
		}
	}
}
