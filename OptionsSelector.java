package app;

import app.util.Utility;


public class OptionsSelector {
	public interface OnSelectOptionCallback {
		public void onSelected(int requestCode, int selection);

	}
	
	private int requestCode;
	String header;
	String[] options;
	OnSelectOptionCallback callback;
	boolean exit;
	
	public OptionsSelector(int requestCode, String[] options) {
		this.requestCode = requestCode;
		this.options = options;
		this.exit = false;
	}
	
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	public void setOptions(String[] options) {
		this.options = options;
	}
	
	public void setOnSelectOptionCallback(OnSelectOptionCallback callback) {
		this.callback = callback;
	}
	
	
	public void displayOptions() {
		if (header!=null) {
			System.out.println(header);
		}
		
		// display menu options
		for (int i = 0; i < options.length; i++) {
			System.out.println(String.format("[%d] %s", i+1, options[i]));
		}
		System.out.println(String.format("[%d] %s", options.length+1, "Exit"));
	}
	
	public void askOptions() {
		this.exit = false;
		int selection = -1;
		int min = 1;
		int max = options.length+1;
		// loop asking option
		while(true) {
			if (this.exit) {
				return;
			}
			displayOptions();
			
			selection = Utility.inputInt( String.format("Enter option [%d-%d]: ", min, max));
			System.out.println();
			if (selection == options.length+1) {
				return;
			} else {
				if (this.callback != null) {
					this.callback.onSelected(requestCode, selection);
				}
			}
		} 
	}
	
	public void stopSelection() {
		this.exit = true;
	}
}

