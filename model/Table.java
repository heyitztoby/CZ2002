package app.model;

import java.io.Serializable;

public class Table implements Comparable<Table>, Serializable{
	private String id;
	private int seats;
	
	public Table(String id, int seats) {
		this.id = id;
		this.seats = seats;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}


	@Override
	public int compareTo(Table other) {
//		return Comparator.comparing(Table::getSeats)
//	              .thenComparing(Table::getId)
//	              .compare(this, other);
		return Integer.compare(seats, other.getSeats());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + seats;
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
		Table other = (Table) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (seats != other.seats)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Table <" + id + ", " + seats + ">";
	}
	
	
	

}
