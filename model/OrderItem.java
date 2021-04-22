package app.model;

import java.io.Serializable;

public class OrderItem implements Comparable<OrderItem>, Serializable{
	private MenuItem item;
	private int quantity;
	
	public OrderItem(MenuItem item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	public MenuItem getItem() {
		return item;
	}

	public void setItem(MenuItem item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double calTotalPrice() {
		return this.getItem().getPrice()*this.quantity;
	}
	
	@Override
	public int compareTo(OrderItem o) {
		if(this.getQuantity()<o.getQuantity()) {
			return 1;
		} else if(this.getQuantity()>o.getQuantity()) {
			return -1;
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + quantity;
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
		OrderItem other = (OrderItem) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return quantity+"x"+item.getName();
	}
	
	
}
