package app.dataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import app.AppConstants;
import app.model.Invoice;
import app.model.Report;
import app.util.Utility;

public class InvoiceManager {
	ArrayList<Invoice> invoices;
	
	public InvoiceManager(ArrayList<Invoice> invoices) {
		this.invoices = invoices;
		sort();
	}
	
	public InvoiceManager() {
		this(new ArrayList<Invoice>());
	}
	
	public ArrayList<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(ArrayList<Invoice> invoices) {
		this.invoices = invoices;
	}

	public ArrayList<Invoice> getInvoicesBetween(Date start, Date end) {
		ArrayList<Invoice> list = new ArrayList<Invoice>();
		for (Invoice invoice : invoices) {
			if (Utility.isBetween(start, end, invoice.getCreateTime())) {
				list.add(invoice);
			}
		}
		return list;
	}
	
	public Report getReport(Date start, Date end) {
		return new Report(start, end, getInvoicesBetween(start, end));
	}
	
	public void addInvoice(Invoice invoice) {
		if(!this.invoices.contains(invoice)){
			this.invoices.add(invoice);
			sort();
		} else {
			if (AppConstants.IS_TESTING) {
				System.out.println("[Test] Add: invoice is already exist");
			}
		}
	}
	
	public void removeInvoice(Invoice invoice) {
		if(this.invoices.contains(invoice)){
			this.invoices.remove(invoice);
			sort();
		} else {
			if (AppConstants.IS_TESTING) {
				System.out.println("[Test] Remove: invoice is not exist");
			}
		}
	}
	
	
	public void sort() {
		Collections.sort(invoices);
	}
}
