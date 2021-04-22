package app.model;

import java.util.ArrayList;

public class PromotionItem extends MenuItem{
	ArrayList<MenuItem> combination;

	public PromotionItem(String id, String type, String name, String description, double price, ArrayList<MenuItem> combination) {
		super(id, type, name, description, price);
		this.combination = combination;
	}
	
	public PromotionItem(String id, String type, String name, String description, double price) {
		this(id, type, name, description, price, new ArrayList<MenuItem>());
	}

	public ArrayList<MenuItem> getCombination() {
		return combination;
	}

	public void setCombination(ArrayList<MenuItem> combination) {
		this.combination = combination;
	}

	
	
}
