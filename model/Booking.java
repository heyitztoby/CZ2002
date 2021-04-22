package app.model;

import java.io.Serializable;
import java.util.Date;

import app.AppConstants;
import app.util.Utility;

public class Booking implements Comparable<Booking>, Serializable{
//	private int id;
	private String name;
	private String contact;
	private int pax;
	private Table table;
	private Date arrivalTime;
	
	public Booking(String name, String contact, int pax, Table table, Date arrivalTime) {
		this.name = name;
		this.contact = contact;
		this.pax = pax;
		this.table = table;
		this.arrivalTime = arrivalTime;
	}
	
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	@Override
	public int compareTo(Booking other) {
		return this.getArrivalTime().compareTo(other.getArrivalTime());
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrivalTime == null) ? 0 : arrivalTime.hashCode());
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Booking other = (Booking) obj;
		if (arrivalTime == null) {
			if (other.arrivalTime != null)
				return false;
		} else if (!arrivalTime.equals(other.arrivalTime))
			return false;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
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

	@Override
	public String toString() {
		return "Booking [name=" + name + ", contact=" + contact + ", pax=" + pax + ", table=" + table
				+ ", arrivalTime=" + arrivalTime + "]";
	}
	
	public void display() {
		System.out.println(String.format("Date: %s", Utility.formatDate(AppConstants.FORMAT_DATE, arrivalTime)));
		System.out.println(String.format("Time: %s", Utility.formatDate(AppConstants.FORMAT_TIME, arrivalTime)));
		System.out.println(String.format("Name: %s", this.name));
		System.out.println(String.format("Contact: %s", this.contact));
		System.out.println(String.format("Pax: %s", this.pax));
		System.out.println(String.format("Table: %s", table.toString()));
		
	}

	


	

}
