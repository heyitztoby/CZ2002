package app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import app.AppConstants;
import app.util.Utility;

public class Order implements Comparable<Order>, Serializable{
//	private int id;
	private String type;
	private ArrayList<OrderItem> items;
	private int pax;
	private Table table;
	private Date createTime;
	
	
	public Order(String type, ArrayList<OrderItem> items, int pax, Table table, Date createTime) {
		this.type = type;
		this.items = items;
		this.pax = pax;
		this.table = table;
		this.createTime = createTime;
	}
	
	public Order(String type, int pax, Table table) {
		this(type, new ArrayList<OrderItem>(), pax, table, new Date());
	}
	
	public Order(int pax, Table table) {
		this(AppConstants.TYPE_WALK_IN, new ArrayList<OrderItem>(), pax, table, new Date());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
	
	public ArrayList<OrderItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<OrderItem> items) {
		this.items = items;
	}

	public int getPax() {
		return pax;
	}

	public void setPax(int pax) {
		this.pax = pax;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public boolean hasMenuItem(MenuItem menuItem) {
		for (OrderItem orderItem : items) {
			if (orderItem.getItem().equals(menuItem)) {
				return true;
			}
		}
		return false;
	}
	
	public OrderItem getOrderItem(MenuItem menuItem) {
		for (OrderItem orderItem : items) {
			if (orderItem.getItem().equals(menuItem)) {
				return orderItem;
			}
		}
		return null;
	}
	
	public double calSubTotal() {
		double amt = 0;
		for (OrderItem orderItem : items) {
			amt += orderItem.getItem().getPrice() * orderItem.getQuantity();
		}
		return amt;
	}
	
	public double calServFee() {
		return AppConstants.SERVICE_CHARGE*0.01*calSubTotal();
	}
	
	public double calGst() {
		double subtotal = calSubTotal();
		double serv     = calServFee();
		double gst		= AppConstants.GST*0.01*(subtotal+serv);
		return gst;
	}
	
	public double calFinalPrice() {
		return calSubTotal()+calServFee()+calGst();
	}
	
	public int getTotalItems() {
		int count = 0;
		for (OrderItem orderItem : items) {
			count += orderItem.getQuantity();
		}
		return count;
	}
	
	public void displayDetail() {
		double subTotal   = calSubTotal();
		double service = calServFee();
		double gst = calGst();
		System.out.println( String.format("Date\t: %s \tTime\t: %s", 
				Utility.formatDate(AppConstants.FORMAT_DATE, this.createTime), 
				Utility.formatDate(AppConstants.FORMAT_TIME, this.createTime)) );
		System.out.println( String.format("Table\t: %s \t\tPax\t: %s", this.table.getId(), this.pax) );
		System.out.println("---------------------------------------");
		displayListItems(false);
		System.out.println("---------------------------------------");
		System.out.println(String.format("Subtotal\t\t\t %6.2f", subTotal));
		System.out.println(String.format("Service (%.1f%%)\t\t\t %6.2f", AppConstants.SERVICE_CHARGE, service));
		System.out.println(String.format("GST (%.1f%%)\t\t\t %6.2f", AppConstants.GST, gst));
		System.out.println(String.format("Total \t\t\t\t %6.2f", subTotal+service+gst));
		if (AppConstants.IS_TESTING) {
			// check rounding issue
		}
	}
	
	public void displayListItems(boolean showIndex) {
		OrderItem orderItem;
		if (this.items.size()==0) {
			System.out.println("No order item");
		}else {
			for (int i=0; i<this.items.size(); i++) {
				if (showIndex) {
					System.out.print(String.format("[%d]", i+1));
				}
				orderItem = this.items.get(i);
				System.out.println(String.format("%2dx %-29s %5.2f", orderItem.getQuantity(), orderItem.getItem().getName(), orderItem.getQuantity()*orderItem.getItem().getPrice()));
				if (orderItem.getItem() instanceof PromotionItem) {
					PromotionItem promoItem = (PromotionItem) orderItem.getItem();
					for (MenuItem item : promoItem.getCombination()) {
						System.out.println("  - "+item.getName());
					}
				}
			}
		}
	}



	@Override
	public int compareTo(Order o) {
		return this.createTime.compareTo(o.getCreateTime());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + pax;
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (pax != other.pax)
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}
	
	
	
	

}
