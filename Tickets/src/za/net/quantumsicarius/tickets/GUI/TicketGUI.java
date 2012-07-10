package za.net.quantumsicarius.tickets.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.event.screen.TextFieldChangeEvent;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.GenericButton;
//import org.getspout.spoutapi.gui.GenericComboBox; - Not used since its bugged
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.player.SpoutPlayer;

public class TicketGUI implements Listener{
	
	GenericPopup popup;
	
	GenericLabel title;
	GenericTextField inputTitle;
	GenericTextField category;
	GenericTextField inputDescription;
	
	GenericButton button;
	GenericButton cancel_button;
	
	GenericButton screenshot;
	
	List<String> categories;
	List<String> priorities;
	
	SpoutPlayer player;
	
	GenericLabel verify;
	
	int screenId;
	GenericLabel screenid_label;
	
	public TicketGUI(SpoutPlayer player ,Plugin plugin) {
		
		this.player = player;
		this.screenId = 0;
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		priorities = new ArrayList<String>();
		priorities.add("High");
		priorities.add("Normal");
		priorities.add("Low");
		// Get all the types of problems
		//this.categories = categories;
		
		// The title
		title = new GenericLabel(ChatColor.GREEN + "New Ticket");
		title.setX(175);
		title.setY(60);
		
		inputTitle = new GenericTextField();
		inputTitle.setPlaceholder("\u00A78 Enter title...");
		inputTitle.setMaximumCharacters(15);
		inputTitle.setX(90);
		inputTitle.setY(80);
		inputTitle.setWidth(210);
		inputTitle.setHeight(20);
		
		inputDescription = new GenericTextField();
		inputDescription.setPlaceholder("\u00A78 Enter Description...");
		inputDescription.setMaximumCharacters(100);
		inputDescription.setMaximumLines(10);
		inputDescription.setX(90);
		inputDescription.setY(110);
		inputDescription.setWidth(210);
		inputDescription.setHeight(50);
		inputDescription.setDirty(true);
		
		screenshot = new GenericButton();
		screenshot.setText("Add Screenshot");
		screenshot.setHeight(20);
		screenshot.setWidth(90);
		screenshot.setX(310);
		screenshot.setY(110);
		
		screenid_label = new GenericLabel();
		screenid_label.setText("Screen Id: " + ChatColor.AQUA + this.screenId);
		screenid_label.setHeight(10);
		screenid_label.setWidth(70);
		screenid_label.setX(310);
		screenid_label.setY(135);
		
		category = new GenericTextField();
		category.setPlaceholder("\u00A78 Enter category...");
		category.setMaximumCharacters(50);
		category.setX(90);
		category.setY(170);
		category.setWidth(210);
		category.setHeight(20);
		category.setEnabled(true);
		
		button = new GenericButton("Ok");
		button.setX(90);
		button.setY(200);
		button.setWidth(100);
		button.setHeight(20);
		
		cancel_button = new GenericButton("Cancel");
		cancel_button.setX(200);
		cancel_button.setY(200);
		cancel_button.setWidth(100);
		cancel_button.setHeight(20);

		verify = new GenericLabel();
		verify.setVisible(false);
		verify.setX(15);
		verify.setY(10);
		verify.setWidth(70);
		verify.setHeight(10);
		
		popup = new GenericPopup();
		popup.attachWidgets(plugin,title,
				inputTitle,
				inputDescription,
				category,
				button,
				cancel_button,
				screenshot,
				screenid_label,
				verify);
		popup.setVisible(true);
		popup.setDirty(true);
		
		player.getMainScreen().attachPopupScreen(popup);
		
	}
	
	@EventHandler
	public void textFieldEvent(TextFieldChangeEvent event) {
		//System.out.println("Field event!");
		//if (event.getTextField() == inputDescription) {
			//if ((inputDescription.getText().length() % 10) == 0 && inputDescription.getText().length() != 0) {
				//System.out.println("Field event on inputDescription!");
				//String old = inputDescription.getText();
				//inputDescription.setText(old + "[BREAK]");
			//}
		//}
	}
	/**
	 * Gets the category
	 * @return The string category
	 */
	public String getCategory() {
		return category.getText();
	}
	
	/**
	 * Gets the description
	 * @return The string description
	 */
	public String getDescription() {
		return inputDescription.getText();
	}
	
	/**
	 * Gets the title text
	 * @return The string title
	 */
	public String getTitle() {
		return inputTitle.getText();
	}
	
	/**
	 * Tests if button is OK button
	 * @param button The button that will be compared
	 * @return True if it is the button
	 */
	public boolean isOkButton(Button button) {
		if (button == this.button) {
			return true;
		}
		return false;
	}
	/**
	 * Tests if button is Cancel button
	 * @param button The button that will be compared
	 * @return True if it is the button
	 */
	public boolean isCancelButton(Button button) {
		if (button == this.cancel_button) {
			return true;
		}
		return false;
	}
	/**
	 * Tests if button is screen shot button
	 * @param button The button that will be compared
	 * @return True if it is the button
	 */
	public boolean isScreenShotButton (Button button) {
		if (button == this.screenshot) {
			return true;
		}
		return false;
	}
	
	public void open() {
		player.getMainScreen().attachPopupScreen(popup);
	}
	
	/**
	 * Sets the GUI's screen shot id value
	 * @param id The integer id
	 */
	public void setScreenShotId(int id) {
		this.screenId = id;
		screenid_label.setText("Screen Id: " + ChatColor.AQUA + this.screenId);
	}
	
	/**
	 * Returns the attached image id
	 * @return
	 */
	public int getImageId() {
		return this.screenId;
	}

	public boolean validate() {
		if (checkTitle()) {
			if (checkDescription()) {
				if (category.getText().equals("")) {
					category.setText("None");
				}
				return true;
			}
		}
		return false;
	}
	
	private boolean checkTitle() {
		if (inputTitle.getText().length() > 5) {
			return true;
		} 
		
		verify.setText(ChatColor.RED + "Invalid ->");
		verify.setY(90);
		verify.setVisible(true);
		
		return false;
	}
	
	private boolean checkDescription() {
		if (inputDescription.getText().length() > 5) {
			return true;
		}
		
		verify.setText(ChatColor.RED + "Invalid ->");
		verify.setY(110);
		verify.setVisible(true);
		
		return false;
	}

}
