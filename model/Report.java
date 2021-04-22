package app.model;

import java.util.ArrayList;
import java.util.Date;

import app.AppConstants;
import app.util.Utility;

public class Report implements AppConstants{
	private Date dateStart;
	private Date dateEnd;
	private ArrayList<Invoice> invoices;
	
	public Report(Date dateStart, Date dateEnd, ArrayList<Invoice> invoices) {
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.invoices = invoices;
	}
	
	public void display() {
		// only access to other classes
		if (IS_TESTING) {
			System.out.println("[TEST] Report is coming");
			Invoice curInvoice;
			for (int i = 0; i < invoices.size(); i++) {
				curInvoice = invoices.get(i);
				System.out.println(String.format("[%d] %s", (i+1),curInvoice.toString()));
			}
			System.out.println();
		}

		
		
		displayDate();
		displayTopSales();
		displayMostPopularItem();
		// TODO Report: display more statistics
		// can create more methods, each method display one type of statistics
		// for example, earning by type (walk in or booking)
	}
	
	private void displayDate() {
		/* need to change
		 if 'dateStart' and 'dateEnd' is same date
		 	-> print date is DD/MM/YYYY
		 else 
		    -> print date is DD/MM/YYYY to DD/MM/YYYY
		 */
		if (dateStart == dateEnd)
		{
			System.out.println(String.format("Date: %s", Utility.formatDate(FORMAT_DATE, dateStart)));
		}
		
		else
		{
			System.out.println(String.format("Date: %s to %s", Utility.formatDate(FORMAT_DATE, dateStart),  Utility.formatDate(FORMAT_DATE, dateEnd)));
		}
	}
	
	private void displayTopSales() {
		// rank of profit earn by each item (in descending order) 
		// may also print % of entire sales besides item
		
	}
	
	private void displayMostPopularItem() {
		// can separate normal and promotion item
		
	}
	

	
	
	
	

}
