package app.dataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import app.AppConstants;
import app.model.Invoice;
import app.model.Order;
import app.model.Table;
import app.util.Utility;

public class OrderManager implements AppConstants{
	private ArrayList<Order> orders;

	public OrderManager(ArrayList<Order> orders) {
		this.orders = orders;
		clearExpiredOrder();
		sort();
	}
	
	public OrderManager() {
		this(new ArrayList<Order>());
	}

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
		clearExpiredOrder();
		sort();
	}
	
	
	public void addOrder(Order order) {
		if (!orders.contains(order)) {
			orders.add(order);
		}
	}
	
	public void removeOrder(Order order) {
		if (orders.contains(order)) {
			orders.remove(order);
		}
	}
	
	public Order getOrderByTable(Table table) {
		clearExpiredOrder();
		
		for (Order order : orders) {
			if (order.getTable().equals(table)) {
				return order;
			}
		}
		return null;
	} 
	
	public ArrayList<Table> getOccupiedTable(){
		clearExpiredOrder();
		
		ArrayList<Table> list = new ArrayList<Table>();
		for (Order order : orders) {
			list.add(order.getTable() );
		}
		return list;
	}
	
	public void clearExpiredOrder() {
		/* TODO OrderMgr.clearExpiredOrder to be implement
		 	Order should happen within session?
		 	once order is over 2or3 hours, we should auto clear and add to lost?
		 */
//		Order order;
//		Invoice invoice;
//		Date now = new Date();
//		Date endSession = new Date();
//		endSession = Utility.changeTime(endSession, AM_END, ORDER_EXPIRE_TIME_MIN);
//		if (now.after( endSession )) {
//			System.out.println("[Test] now is after AM_END");
//			for (int i = orders.size()-1; i >= 0; i--) {
//				order = orders.remove(i);
//				invoice = new Invoice(order, order.getCreateTime(), false);
//				Restaurant.getInstance().getInvoicesMgr().addInvoice( invoice );
//			}
//		}
//		endSession = Utility.changeTime(endSession, PM_END, ORDER_EXPIRE_TIME_MIN);
//		if (now.after( endSession )) {
//			System.out.println("[Test] now is after PM_END");
//			for (int i = orders.size()-1; i >= 0; i--) {
//				order = orders.remove(i);
//				invoice = new Invoice(order, order.getCreateTime(), false);
//				Restaurant.getInstance().getInvoicesMgr().addInvoice( invoice );
//			}
//		}
	}
	
	public void displayOrders() {
		clearExpiredOrder(); 
		
		Order order;
		if (orders.size() == 0) {
				System.out.println("No order record found");
		}else {
			System.out.println(String.format("%-4s %-5s %-3s %-5s %-8s", "No.", "Table", "Pax", "Items", "Subtatoal"));
			for (int i=0; i<orders.size(); i++) {
				order = orders.get(i);
				System.out.println(String.format("%-4s %-5s %-3s %-5s %8.2f",
						"["+(i+1)+"]",
						order.getTable().getId(),
						order.getPax(),
						order.getTotalItems(),
						order.calSubTotal()
				));
			}
			System.out.println();
		}
		
	}
	
	public void sort() {
		Collections.sort(this.orders);
	}
	
	
	
	

}
