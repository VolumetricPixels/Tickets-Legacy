package za.net.quantumsicarius.tickets.GUI;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericListWidget;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.ListWidgetItem;
import org.getspout.spoutapi.player.SpoutPlayer;

import za.net.quantumsicarius.tickets.Ticket;


public class TicketListGUI {
	
	HashMap<ListWidgetItem, Integer> itemID;
	
	GenericPopup popup;
	
	GenericListWidget openList;
	ListWidgetItem openItem;
	
	GenericListWidget closedList;
	ListWidgetItem closedItem;
	
	GenericLabel title;
	
	GenericLabel openListLabel;
	GenericLabel closedListLabel;
	
	GenericButton openselect;
	GenericButton closeselect;
	
	GenericButton opendelete;
	GenericButton closedelete;
	
	GenericButton closeTicket;
	GenericButton openTicket;
	
	GenericButton switchButton;
	
	String who;
	
	public TicketListGUI(SpoutPlayer player, Plugin plugin, ArrayList<Ticket> opentickets, ArrayList<Ticket> closedtickets, String who) {
		this.who = who;
		
		itemID = new HashMap<ListWidgetItem, Integer>();
		
		if (who.equals("None")) {
			who = "New";
		} else if (who.equals("")) {
			who = "All";
		}
		
		title = new GenericLabel();
		title.setText(ChatColor.GREEN + "You are viewing: " + ChatColor.YELLOW + who + ChatColor.GREEN + " tickets");
		title.setX(155);
		title.setY(5);
		title.setWidth(70);
		title.setHeight(20);

		/*
		 * Open list side
		 */
		// Buttons Calculated with: (198-10)/3 = Width
		closeTicket = new GenericButton();
		closeTicket.setText("Close");
		closeTicket.setX(10);
		closeTicket.setY(190);
		closeTicket.setHeight(20);
		closeTicket.setWidth(62);
		
		openselect = new GenericButton();
		openselect.setText("Select");
		openselect.setX(77);
		openselect.setY(190);
		openselect.setHeight(20);
		openselect.setWidth(62);
		
		opendelete = new GenericButton();
		opendelete.setText("Delete");
		opendelete.setX(144);
		opendelete.setY(190);
		opendelete.setHeight(20);
		opendelete.setWidth(62);
		
		closeTicket.setEnabled(true);
		closeTicket.setTooltip("There are no tickets to close");
		
		openselect.setEnabled(true);
		openselect.setTooltip("There are not tickets to select");
		
		opendelete.setEnabled(true);
		opendelete.setTooltip("There are no tickets to delete");
		// End of buttons
		
		openListLabel = new GenericLabel();
		openListLabel.setText("There are: " + ChatColor.GREEN + "0" + ChatColor.WHITE + " open tickets");
		openListLabel.setX(10);
		openListLabel.setY(20);
		openListLabel.setWidth(70);
		openListLabel.setHeight(10);
		
		openList = new GenericListWidget();
		openList.setX(10);
		openList.setY(30);
		openList.setHeight(150);
		openList.setWidth(198);
		openList.setDirty(true);
		
		// Update the list
		refreshOpenList(opentickets);
		
		/*
		 * Closed list side
		 */
		// Buttons Calculated with: (198-10)/3 = Width
		openTicket = new GenericButton();
		openTicket.setText("Open");
		openTicket.setX(218);
		openTicket.setY(190);
		openTicket.setHeight(20);
		openTicket.setWidth(62);
		
		closeselect = new GenericButton();
		closeselect.setText("Select");
		closeselect.setX(285);
		closeselect.setY(190);
		closeselect.setHeight(20);
		closeselect.setWidth(62);
		
		closedelete = new GenericButton();
		closedelete.setText("Delete");
		closedelete.setX(352);
		closedelete.setY(190);
		closedelete.setHeight(20);
		closedelete.setWidth(62);
		
		openTicket.setEnabled(true);
		openTicket.setTooltip("Opens the selected ticket");
		
		closeselect.setEnabled(true);
		closeselect.setTooltip("Views the selected ticket");
		
		closedelete.setEnabled(true);
		closedelete.setTooltip("Deletes the selected ticket");
		// End of buttons
		
		closedListLabel = new GenericLabel();
		closedListLabel.setText("There are: " + ChatColor.GREEN + "0" + ChatColor.WHITE + " closed tickets");
		closedListLabel.setX(218);
		closedListLabel.setY(20);
		closedListLabel.setWidth(70);
		closedListLabel.setHeight(10);
		
		closedList = new GenericListWidget();
		closedList.setX(218);
		closedList.setY(30);
		closedList.setHeight(150);
		closedList.setWidth(198);
		closedList.setDirty(true);
		
		// Update the list
		refreshClosedList(closedtickets);
		
		switchButton = new GenericButton();
		switchButton.setText("Switch View");
		switchButton.setTooltip("Show all your tickets");
		switchButton.setX(320);
		switchButton.setY(30);
		switchButton.setWidth(100);
		switchButton.setHeight(20);
		
		popup = new GenericPopup();
		popup.attachWidgets(plugin, title,
				openList,
				openselect,
				closeselect,
				opendelete,
				closedelete,
				//switchButton,
				openListLabel,
				closedListLabel,
				closedList,
				openTicket,
				closeTicket);
		
		player.getMainScreen().attachPopupScreen(popup);
	}

	/**
	 * Returns the selected item from the open list
	 * @return The ListWidgetItem
	 */
	public ListWidgetItem getOpenSelected() {
		return openList.getSelectedItem();
	}
	
	/**
	 * Returns the selected item from the closed list
	 * @return The ListWidgetItem
	 */
	public ListWidgetItem getClosedSelected() {
		return closedList.getSelectedItem();
	}
	
	/**
	 * Return the selected item ID
	 * @return The integer id
	 */
	public int getSelectedOpenId() {
		if (itemID.containsKey(openList.getSelectedItem())) {
			return itemID.get(openList.getSelectedItem());
		}
		return 0;
	}
	
	/**
	 * Test if the button is the open select button
	 * @param button The button that will be compared
	 * @return True if the button is the local button
	 */
	public boolean isOpenSelectButton(Button button) {
		if (button == openselect) {
			return true;
		}
		return false;
	}
	
	/**
	 * Test if the button is the close select button
	 * @param button The button that will be compared
	 * @return True if the button is the local button
	 */
	public boolean isCloseSelectButton(Button button) {
		if (button == closeselect) {
			return true;
		}
		return false;
	}
	
	/**
	 * Test if the button is the open delete button
	 * @param button The button that will be compared
	 * @return True if the button is the local button
	 */
	public boolean isOpenDeleteButton(Button button) {
		if (button == opendelete) {
			return true;
		}
		return false;
	}
	
	/**
	 * Test if the button is the close delete button
	 * @param button The button that will be compared
	 * @return True if the button is the local button
	 */
	public boolean isCloseDeleteButton(Button button) {
		if (button == closedelete) {
			return true;
		}
		return false;
	}
	
	/**
	 * Test if the button is the switchButton
	 * @param button The button that will be compared
	 * @return True if the button is the local button
	 */
	public boolean isSwitchButton(Button button) {
		if (button == switchButton) {
			return true;
		}
		return false;
	}
	
	/**
	 * Test if the button is the open ticket button
	 * @param button The button that will be compared
	 * @return True if the button is the local button
	 */
	public boolean isOpenButton(Button button) {
		if (button == openTicket) {
			return true;
		}
		return false;
	}
	
	/**
	 * Test if the button is the close ticket button
	 * @param button The button that will be compared
	 * @return True if the button is the local button
	 */
	public boolean isCloseButton(Button button) {
		if (button == closeTicket) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the targeted players name this could be blank
	 * @return
	 */
	public String getTarget() {
		return who;
	}

	/**
	 * Refreshes the open list of tickets
	 * @param tickets The ArrayList if tickets
	 */
	public void refreshOpenList(ArrayList<Ticket> tickets) {

		try {
			if (tickets.size() < 1 | tickets == null) {
				openList.clear();
				
				openListLabel.setText("There are: " + ChatColor.GREEN + "0" + ChatColor.WHITE + " open tickets");
				
				closeTicket.setEnabled(false);
				closeTicket.setTooltip("There are no tickets to close");
				
				openselect.setEnabled(false);
				openselect.setTooltip("There are not tickets to select");
				
				opendelete.setEnabled(false);
				opendelete.setTooltip("There are no tickets to delete");
			} else {
				openListLabel.setText("There are: " + ChatColor.GREEN + tickets.size() + ChatColor.WHITE + " open tickets");
				
				openList.clear();
				
				for (int i = 0; i < tickets.size(); i++) {
					openItem = new ListWidgetItem();
					openItem.setTitle(tickets.get(i).getTitle());
					openItem.setText(tickets.get(i).getDescrption());
					itemID.put(openItem, i);
					openList.addItem(openItem);
				}
				closeTicket.setEnabled(true);
				closeTicket.setTooltip("Closes the selected ticket");
				
				openselect.setEnabled(true);
				openselect.setTooltip("Views the selected ticket");
				
				opendelete.setEnabled(true);
				opendelete.setTooltip("Deletes the selected ticket");
				

			}
		} catch (NullPointerException e) {
			openList.clear();
			
			openListLabel.setText("There are: " + ChatColor.GREEN + "0" + ChatColor.WHITE + " open tickets");
			
			closeTicket.setEnabled(false);
			closeTicket.setTooltip("There are no tickets to close");
			
			openselect.setEnabled(false);
			openselect.setTooltip("There are not tickets to select");
			
			opendelete.setEnabled(false);
			opendelete.setTooltip("There are no tickets to delete");
		}

	}
	
	/**
	 * Refreshes the closed list of tickets
	 * @param tickets The ArrayList if tickets
	 */
	public void refreshClosedList(ArrayList<Ticket> tickets) {	
		
		try {
			if (tickets.size() < 1 | tickets == null) {
				closedList.clear();
				
				closedListLabel.setText("There are: " + ChatColor.GREEN + "0" + ChatColor.WHITE + " closed tickets");
				
				openTicket.setEnabled(false);
				openTicket.setTooltip("There are no tickets to open");
				
				closeselect.setEnabled(false);
				closeselect.setTooltip("There are no tickets to select");
				
				closedelete.setEnabled(false);
				closedelete.setTooltip("There are no tickets delete");
			} else {
				closedListLabel.setText("There are: " + ChatColor.GREEN + tickets.size() + ChatColor.WHITE + " closed tickets");
				
				closedList.clear();
				
				for (int i = 0; i < tickets.size(); i++) {
					closedItem = new ListWidgetItem();
					closedItem.setTitle(tickets.get(i).getTitle());
					closedItem.setText(tickets.get(i).getDescrption());
					closedList.addItem(closedItem);
				}
				
				openTicket.setEnabled(true);
				openTicket.setTooltip("Opens the selected ticket");
				
				closeselect.setEnabled(true);
				closeselect.setTooltip("Views the selected ticket");
				
				closedelete.setEnabled(true);
				closedelete.setTooltip("Deletes the selected ticket");
			}
		} catch (NullPointerException e) {
			closedList.clear();
			
			closedListLabel.setText("There are: " + ChatColor.GREEN + "0" + ChatColor.WHITE + " closed tickets");
			
			openTicket.setEnabled(false);
			openTicket.setTooltip("There are no tickets to open");
			
			closeselect.setEnabled(false);
			closeselect.setTooltip("There are no tickets to select");
			
			closedelete.setEnabled(false);
			closedelete.setTooltip("There are no tickets delete");
		}
	}

}
