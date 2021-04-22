package app.dataManager;

import java.util.ArrayList;
import java.util.Collections;

import app.model.MenuItem;
import app.model.PromotionItem;

public class Menu {
	private ArrayList<MenuItem> items;
	
	public Menu(ArrayList<MenuItem> items) {
		this.items = items;
		sort();
	}

	public Menu() {
		this(new ArrayList<MenuItem>());
	}

	public ArrayList<MenuItem> getItems(){
		return this.items;
	}

	public void setItems(ArrayList<MenuItem> items) {
		this.items = items;
		sort();
	}
	
	public boolean isExistId(String id) {
		boolean exist = false;
		for (MenuItem menuItem : items) {
			if (menuItem.getId().equalsIgnoreCase(id)) {
				exist = true;
				break;
			}
		}
		return exist;
	}
	
	public ArrayList<String> getTypes(){
		ArrayList<String> list = new ArrayList<String>();
		for (MenuItem item: items) {
			if (!list.contains(item.getType())) {
				list.add(item.getType());
			}
		}
		return list;
	}
	
	public ArrayList<MenuItem> getItemsOfType(String type) {
		ArrayList<MenuItem> list = new ArrayList<MenuItem>();
		for (MenuItem item: items) {
			if (item.getType().equals(type)) {
				list.add(item);
			}
		}
		return list;
	}
	
	public MenuItem getItemById(String id) {
		for (MenuItem menuItem : items) {
			if (menuItem.getId().equalsIgnoreCase(id)) {
				return menuItem;
			}
		}
		return null;
	}
	
//	public boolean isIdExist(MenuItem item) {
//		for (MenuItem menuItem : items) {
//			if (menuItem.getId().equals(item.getId())) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	public void addItem(MenuItem item) {
		if (!this.items.contains(item)) {
			this.items.add(item);
			sort();
		}
	}
	
	public void removeItem(MenuItem item) {
		if (this.items.contains(item)) {
			this.items.remove(item);
		}
	}
	
	public ArrayList<MenuItem> getListMenuItems(boolean isPromo) {
		ArrayList<MenuItem> list = new ArrayList<MenuItem>();
		for (MenuItem menuItem : this.items) {
			if (isPromo) {
				if (menuItem instanceof PromotionItem) {
					list.add(menuItem);
				}
			}else {
				if (!(menuItem instanceof PromotionItem)) {
					list.add(menuItem);
				}
			}
		}
		return list;
	} 
	

	
	public void sort() {
		Collections.sort(this.items);
	}
	
	

	
	public void displayMenu() {
		displayMenu(this.items);
	}
	
//	public void displayOnlyPromo() {
//		displayMenu(getListMenuItems(true));
//	}
//	
	public void displayNoPromo() {
		displayMenu(getListMenuItems(false));
	}
	
	private void displayMenu(ArrayList<MenuItem> list) {
		// to discuss about text length
		if (list.size() == 0) {
			System.out.println("No menu item found");
		} else {
			System.out.println(String.format("%-4s \t%-6s \t%-4s \t%-25s \t%-30s \t%-6s", "No.", "Type","Id", "Name", "Desc", "Price"));
			MenuItem item;
			for (int i = 0; i < list.size(); i++) {
				item = list.get(i); 
				System.out.println(String.format("[%2d] \t%-6s \t%-4s \t%-25s \t%-30s \t%.2f", i+1,item.getType(), item.getId(), item.getName(), item.getDescription(), item.getPrice()));
			}
		}
	}


}
