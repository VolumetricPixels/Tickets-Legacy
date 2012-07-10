package za.net.quantumsicarius.tickets.GUI;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericRadioButton;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.player.SpoutPlayer;

public class TicketViewerGUI {
	
	Plugin plugin;
	
	GenericPopup popup;
	
	GenericLabel time;
	
	GenericTextField title;
	GenericTextField description;
	GenericTextField author;
	GenericTextField assignee;
	GenericTextField category;
	
	GenericRadioButton priority_1;
	GenericRadioButton priority_2;
	GenericRadioButton priority_3;
	
	GenericLabel label_description;
	GenericLabel label_author;
	GenericLabel label_time;
	GenericLabel label_category;
	GenericLabel label_assignee;
	GenericLabel label_priority;
	GenericLabel label_title;
	
	GenericButton update_button;
	GenericButton goto_button;
	GenericButton edit_button;
	GenericButton close_button;
	GenericButton gotoLocation_button;
	GenericButton screenshot;
	
	int ImageId;
	int Id;
	SpoutPlayer user;
	
	SpoutPlayer player;

	public TicketViewerGUI(Plugin plugin ,SpoutPlayer player ,String title, String description, String author, String category, long time, String assignee, int priority, int id, SpoutPlayer user, int ImageId) {
		this.plugin = plugin;
		this.Id = id;
		this.user = user;
		this.player = player;
		this.ImageId = ImageId;
		
		label_title = new GenericLabel();
		label_title.setText(ChatColor.AQUA + "Title:");
		label_title.setX(20);
		label_title.setY(15);
		label_title.setWidth(70);
		label_title.setHeight(10);
		
		this.title = new GenericTextField();
		this.title.setText(title);
		this.title.setEnabled(false);
		this.title.setMaximumCharacters(100);
		this.title.setX(100);
		this.title.setY(15);
		this.title.setWidth(210);
		this.title.setHeight(10);
		
		label_description = new GenericLabel(ChatColor.AQUA + "Description:");
		label_description.setX(20);
		label_description.setY(40);
		label_description.setWidth(70);
		label_description.setHeight(10);
		
		this.description = new GenericTextField();
		this.description.setText(description);
		this.description.setEnabled(false);
		this.description.setMaximumCharacters(100);
		this.description.setMaximumLines(20);
		this.description.setX(100);
		this.description.setY(40);
		this.description.setWidth(210);
		this.description.setHeight(50);
		
		this.screenshot = new GenericButton();
		this.screenshot.setText("View Image");
		if (this.ImageId == 0) {
			this.screenshot.setEnabled(false);
			this.screenshot.setTooltip("There was no attached screen shot");
		} else {
			this.screenshot.setEnabled(true);
			this.screenshot.setTooltip("Show the attached screen shot");
		}
		this.screenshot.setX(320);
		this.screenshot.setY(40);
		this.screenshot.setHeight(20);
		this.screenshot.setWidth(90);
		
		label_author = new GenericLabel(ChatColor.AQUA + "Author:");
		label_author.setX(20);
		label_author.setY(100);
		label_author.setWidth(70);
		label_author.setHeight(10);
		
		this.author = new GenericTextField();
		this.author.setText(author);
		this.author.setEnabled(false);
		this.author.setMaximumCharacters(100);
		this.author.setX(100);
		this.author.setY(100);
		this.author.setWidth(210);
		this.author.setHeight(10);
		
		
		// Convert the time to a readable format
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");
		Date resultdate = new Date(time);
		
		label_time = new GenericLabel(ChatColor.AQUA + "Time:");
		label_time.setX(20);
		label_time.setY(120);
		label_time.setWidth(70);
		label_time.setHeight(10);
		
		this.time = new GenericLabel();
		this.time.setText(ChatColor.GREEN + sdf.format(resultdate));
		this.time.setX(100);
		this.time.setY(120); // The same Y as the label
		this.time.setWidth(70);
		this.time.setHeight(10);
		
		label_category = new GenericLabel(ChatColor.AQUA + "Category:");
		label_category.setX(20);
		label_category.setY(140);
		label_category.setWidth(70);
		label_category.setHeight(10);
		
		this.category = new GenericTextField();
		this.category.setText(category);
		this.category.setEnabled(false);
		this.category.setX(100);
		this.category.setY(140); // The same Y as the label
		this.category.setWidth(210);
		this.category.setHeight(10);
		
		label_assignee = new GenericLabel();
		label_assignee.setText(ChatColor.AQUA + "Assignee:");
		label_assignee.setX(20);
		label_assignee.setY(160);
		label_assignee.setWidth(70);
		label_assignee.setHeight(10);
		
		this.assignee = new GenericTextField();
		this.assignee.setText(assignee);
		this.assignee.setEnabled(false);
		this.assignee.setMaximumCharacters(100);
		this.assignee.setMaximumLines(10);
		this.assignee.setX(100);
		this.assignee.setY(160);
		this.assignee.setWidth(210);
		this.assignee.setHeight(10);
		
		label_priority = new GenericLabel();
		label_priority.setText(ChatColor.AQUA + "Priority:");
		label_priority.setX(20);
		label_priority.setY(180);
		label_priority.setWidth(70);
		label_priority.setHeight(10);
		
		this.priority_1 = new GenericRadioButton();
		this.priority_1.setEnabled(false);
		this.priority_1.setX(100);
		this.priority_1.setY(180);
		this.priority_1.setText("Low");
		this.priority_1.setWidth(20);
		this.priority_1.setHeight(10);
		this.priority_1.setGroup(1);
		
		this.priority_2 = new GenericRadioButton();
		this.priority_2.setEnabled(false);
		this.priority_2.setX(140);
		this.priority_2.setY(180);
		this.priority_2.setText("Normal");
		this.priority_2.setWidth(20);
		this.priority_2.setHeight(10);
		this.priority_2.setGroup(1);
		
		this.priority_3 = new GenericRadioButton();
		this.priority_3.setEnabled(false);
		this.priority_3.setX(190);
		this.priority_3.setY(180);
		this.priority_3.setText("High");
		this.priority_3.setWidth(20);
		this.priority_3.setHeight(20);
		this.priority_3.setGroup(1);
		
		goto_button = new GenericButton();
		goto_button.setText("Goto Player");
		goto_button.setX(320);
		goto_button.setY(100);
		goto_button.setWidth(90);
		goto_button.setHeight(20);
		
		if (isAuthorOnline()) {
			goto_button.setTooltip("Teleport to the player");
			goto_button.setEnabled(true);
		} else {
			goto_button.setTooltip("The player is Offline");
			goto_button.setEnabled(false);
		}
		
		update_button = new GenericButton();
		update_button.setText("Update");
		update_button.setTooltip("Saves all changes");
		update_button.setEnabled(false);
		update_button.setX(120);
		update_button.setY(200);
		update_button.setWidth(90);
		update_button.setHeight(20);
		
		edit_button = new GenericButton();
		edit_button.setText("Edit");
		edit_button.setTooltip("Allows you to edit the fields");
		edit_button.setX(220);
		edit_button.setY(200);
		edit_button.setWidth(90);
		edit_button.setHeight(20);
		
		close_button = new GenericButton();
		close_button.setText("Exit");
		close_button.setTooltip("Close this interface");
		close_button.setX(320);
		close_button.setY(200);
		close_button.setWidth(90);
		close_button.setHeight(20);
		
		gotoLocation_button = new GenericButton();
		gotoLocation_button.setText("Goto Location");
		gotoLocation_button.setTooltip("Teleports you to the reported location");
		gotoLocation_button.setX(20);
		gotoLocation_button.setY(200);
		gotoLocation_button.setWidth(90);
		gotoLocation_button.setHeight(20);

		popup = new GenericPopup();
		popup.attachWidgets(plugin, 
				this.label_title,
				this.title, 
				this.label_author,
				this.author, 
				this.label_description, 
				this.description, 
				this.label_time,
				this.time, 
				this.label_category, 
				this.category, 
				this.label_assignee,
				this.assignee,
				this.label_priority,
				this.priority_1,
				this.priority_2,
				this.priority_3,
				this.goto_button,
				this.update_button,
				this.edit_button,
				this.close_button,
				this.gotoLocation_button,
				this.screenshot);
		
		player.getMainScreen().attachPopupScreen(popup);
		
		switch (priority) {
		case (0):
			this.priority_1.setSelected(true);
			break;
		case(1):
			this.priority_2.setSelected(true);
			break;
		case(2):
			this.priority_3.setSelected(true);
			break;
		}
	}
	
	/**
	 * Enables the text fields to allow it to be edited
	 * @param enabled The boolean to activate or deactivate it
	 */
	public void setEditable(boolean enabled) {
		this.description.setEnabled(enabled);
		this.category.setEnabled(enabled);
		this.assignee.setEnabled(enabled);
		this.priority_1.setEnabled(enabled);
		this.priority_2.setEnabled(enabled);
		this.priority_3.setEnabled(enabled);
		this.title.setEnabled(enabled);
	}
	/**
	 * Tests if the button is the gotoButton
	 * @param button The button that will be compared
	 * @return True if the button is the gotoButton
	 */
	public boolean isGotoButton(Button button) {
		if (button == goto_button) {
			return true;
		}
		return false;
	}
	/**
	 * Tests if the button is the updateButton
	 * @param button The button that will be compared
	 * @return True if the button is the updateButton
	 */
	public boolean isUpdateButton(Button button) {
		if (button == update_button) {
			return true;
		}
		return false;
	}
	/**
	 * Tests if the button is the editButton
	 * @param button The button that will be compared
	 * @return True if the button is the editButton
	 */
	public boolean isEditButton(Button button) {
		if (button == edit_button) {
			return true;
		}
		return false;
	}
	
	/**
	 * Tests if the button is the gotoLocationButton
	 * @param button The button that will be compared
	 * @return True if the button is the editButton
	 */
	public boolean isGotoLocationButton(Button button) {
		if (button == gotoLocation_button) {
			return true;
		}
		return false;
	}
	
	/**
	 * Tests if the button is the closeButton
	 * @param button The button that will be compared
	 * @return True if the button is the editButton
	 */
	public boolean isCloseButton(Button button) {
		if (button == close_button) {
			return true;
		}
		return false;
	}
	
	/**
	 * Tests if the button is the ImageButton
	 * @param button The button that will be compared
	 * @return True if the button is the ImageButton
	 */
	public boolean isImageButton(Button button) {
		if (button == this.screenshot) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the player who made the ticket
	 * @return The SpoutPlayer
	 */
	public SpoutPlayer getAuthorPlayer() {
		if (!isAuthorOnline()) {
			return user;
		}
		
		Player player = plugin.getServer().getPlayer(author.getText());
		return SpoutManager.getPlayer(player);
	}
	
	/**
	 * Returns a boolean weather the player is online or not
	 * @return
	 */
	public boolean isAuthorOnline() {
		try {
			if (plugin.getServer().getPlayer(author.getText()).isOnline()) {
				return true;
			}
			else {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	/**
	 * Sets the button enabled or not
	 * @param enabled
	 * @param tooltip
	 */
	public void enableEditButton(boolean enabled, String tooltip) {
		edit_button.setEnabled(enabled);
		edit_button.setTooltip(tooltip);
	}
	
	/**
	 * Sets the button enabled or not
	 * @param enabled
	 */
	public void enableUpdateButton(boolean enabled) {
		update_button.setEnabled(enabled);
	}
	
	/**
	 * Returns the updated description
	 * @return The string that is the description
	 */
	public String getDescription() {
		return this.description.getText();
	}
	
	/**
	 * Returns the updated assignee
	 * @return The string that is the assignee
	 */
	public String getAssignee() {
		return this.assignee.getText();
	}
	
	/**
	 * Returns an integer of the priority
	 * 	0 = Low
	 * 	1 = Normal
	 * 	2 = High
	 * @return The integer
	 */
	public int getPriority() {
		int prio = 0;
		
		if (this.priority_1.isSelected()) {
			prio = 0;
		}
		else if (this.priority_2.isSelected()) {
			prio = 1;
		}
		else if (this.priority_3.isSelected()) {
			prio = 2;
		}
		
		return prio;
	}
	/**
	 * Returns the updated category
	 * @return The string category
	 */
	public String getCategory() {
		return this.category.getText();
	}
	
	/**
	 * Returns the updated title
	 * @return The string title
	 */
	public String getTitle() {
		return this.title.getText();
	}
	
	public int getId() {
		return Id;
	}

	/**
	 * Re-opens the GUI
	 */
	public void open() {
		player.getMainScreen().attachPopupScreen(popup);
	}
}
