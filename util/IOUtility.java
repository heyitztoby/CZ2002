package app.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import app.AppConstants;
import app.dataManager.Restaurant;
import app.model.Booking;
import app.model.Invoice;
import app.model.MenuItem;
import app.model.Order;
import app.model.Table;


public class IOUtility implements AppConstants{
	
	@SuppressWarnings("unchecked")
	public static boolean loadData() {
		boolean success = false;
		File dataFolder = new File(PATH_DATA_FOLDER);
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
		
		File dataFile = new File(dataFolder, DATA_FILE_NAME);
		if (!dataFile.exists()) {
			return false;
		}
		
		Object[] data;
		FileInputStream fis;
		ObjectInputStream ois;
		
		try {
			fis = new FileInputStream(dataFile.toString());
			ois = new ObjectInputStream(fis);
			data = (Object[]) ois.readObject();
			if (data != null) {
				
				Restaurant restaurant = Restaurant.getInstance();
				restaurant.getMenu().setItems( (ArrayList<MenuItem>) data[0]  );
				restaurant.getTableMgr().setTables( (ArrayList<Table>) data[1] );
				restaurant.getOrderMgr().setOrders( (ArrayList<Order>) data[2] );
				restaurant.getBookingMgr().setBookings( (ArrayList<Booking>) data[3] );
				restaurant.getInvoicesMgr().setInvoices( (ArrayList<Invoice>) data[4] );

			}
			ois.close();
			System.out.println("Data loaded");
			success = true;
		} catch (IOException | ClassCastException | ClassNotFoundException e) {
			if (IS_TESTING) {
				e.printStackTrace();
			}
		} 
		return success;
	}
	

	public static void saveData() {
		// check date folder
		File dataFolder = new File(PATH_DATA_FOLDER);
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
		
		// prepare writing to file
		Restaurant restaurant = Restaurant.getInstance();
		Object[] data = {
				restaurant.getMenu().getItems(),
				restaurant.getTableMgr().getTables(),
				restaurant.getOrderMgr().getOrders(),
				restaurant.getBookingMgr().getBookings(),
				restaurant.getInvoicesMgr().getInvoices()
		};
		
		File dataFile = new File(dataFolder, DATA_FILE_NAME);
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = new FileOutputStream(dataFile.toString());
			oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.close();
			System.out.println("Data saved");
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// TODO [Enhance] Save report to file
	

}
