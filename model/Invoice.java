package app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Invoice implements Comparable<Invoice>, Serializable{
	private String type;
	private ArrayList<OrderItem> items;
	private int pax;
	private Table table;
	private Date createTime;
	private boolean isPaid;
	
	public Invoice(String type, ArrayList<OrderItem> items, int pax, Table table, Date createTime, boolean isPaid) {
		this.type = type;
		this.items = items;
		this.pax = pax;
		this.table = table;
		this.createTime = createTime;
		this.isPaid = isPaid;
	}
	
	public Invoice(Order order, Date date, boolean isPaid) {
		this(order.getType(),
			 order.getItems(),
			 order.getPax(),
			 order.getTable(),
			 date,
			 isPaid);
	}
	public Invoice(Order order, boolean isPaid) {
		this(order, new Date(), isPaid);
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

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
	
	
	@Override
	public int compareTo(Invoice o) {
		return this.getCreateTime().compareTo(o.getCreateTime());
	}

	@Override
	public String toString() {
		return "Invoice [type=" + type + ", items=" + items + ", pax=" + pax + ", table=" + table + ", createTime="
				+ createTime + ", isPaid=" + isPaid + "]";
	}
	
	
	
	
}
