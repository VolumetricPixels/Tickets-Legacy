package za.net.quantumsicarius.tickets.GUI;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.player.SpoutPlayer;

import za.net.quantumsicarius.tickets.Enums.Screens;

public class AboutGUI {
	
	GenericPopup popup;
	
	GenericTexture logo;
	GenericLabel title;
	
	GenericButton about_button;
	GenericButton commands_button;
	GenericButton credits_button;
	
	GenericLabel page;
	
	GenericLabel text1;
	GenericLabel text2;
	GenericLabel text3;
	
	GenericButton newTicketCommand;
	GenericButton modviewCommand;
	GenericButton ticketviewCommand;
	GenericButton aboutCommand;
	
	public AboutGUI (SpoutPlayer player, Plugin plugin) {
		
		logo = new GenericTexture();
		logo.setUrl("http://www.hawnutor.org/image/nEwvQmd.png");
		logo.setVisible(true);
		logo.setWidth(150);
		logo.setHeight(30);
		logo.setX(30);
		logo.setY(15);
		
		title = new GenericLabel();
		title.setText(ChatColor.AQUA + "Tickets - A Minecraft Support Ticket System");
		title.setWidth(100);
		title.setHeight(10);
		title.setVisible(true);
		title.setY(80);
		title.setX(110);
		
		about_button = new GenericButton();
		about_button.setText("About");
		about_button.setEnabled(false);
		about_button.setHeight(15);
		about_button.setWidth(100);
		about_button.setY(210);
		about_button.setX(60);
		
		commands_button = new GenericButton();
		commands_button.setText("Commands");
		commands_button.setHeight(15);
		commands_button.setWidth(100);
		commands_button.setY(210);
		commands_button.setX(170);
		
		credits_button = new GenericButton();
		credits_button.setText("Credits");
		credits_button.setHeight(15);
		credits_button.setWidth(100);
		credits_button.setY(210);
		credits_button.setX(280);
		
		page = new GenericLabel();
		page.setText("You are on the about page");
		page.setWidth(70);
		page.setHeight(10);
		page.setX(160);
		page.setY(95);
		
		text1 = new GenericLabel();
		text1.setText("This is the about screen");
		text1.setWidth(100);
		text1.setHeight(10);
		text1.setX(120);
		text1.setY(120);
		
		text2 = new GenericLabel();
		text2.setText("This is the about screen");
		text2.setWidth(100);
		text2.setHeight(10);
		text2.setX(120);
		text2.setY(135);
		
		text3 = new GenericLabel();
		text3.setText("This is the about screen");
		text3.setWidth(100);
		text3.setHeight(10);
		text3.setX(120);
		text3.setY(150);
		
		newTicketCommand = new GenericButton();
		newTicketCommand.setVisible(false);
		newTicketCommand.setEnabled(false);
		newTicketCommand.setText("New Ticket");
		newTicketCommand.setTooltip("/ticket new");
		newTicketCommand.setWidth(100);
		newTicketCommand.setHeight(15);
		newTicketCommand.setX(120);
		newTicketCommand.setY(120);
		
		modviewCommand = new GenericButton();
		modviewCommand.setVisible(false);
		modviewCommand.setEnabled(false);
		modviewCommand.setText("Ticket Mod List");
		modviewCommand.setTooltip("/ticket modlist");
		modviewCommand.setWidth(100);
		modviewCommand.setHeight(15);
		modviewCommand.setX(120);
		modviewCommand.setY(140);
		
		ticketviewCommand = new GenericButton();
		ticketviewCommand.setVisible(false);
		ticketviewCommand.setEnabled(false);
		ticketviewCommand.setText("Ticket View");
		ticketviewCommand.setTooltip("/ticket view");
		ticketviewCommand.setWidth(100);
		ticketviewCommand.setHeight(15);
		ticketviewCommand.setX(225);
		ticketviewCommand.setY(120);
		
		aboutCommand = new GenericButton();
		aboutCommand.setVisible(false);
		aboutCommand.setEnabled(false);
		aboutCommand.setText("About Page");
		aboutCommand.setTooltip("/ticket about");
		aboutCommand.setWidth(100);
		aboutCommand.setHeight(15);
		aboutCommand.setX(225);
		aboutCommand.setY(140);
		
		popup = new GenericPopup();
		popup.attachWidgets(plugin, //logo,
				title,
				page,
				about_button,
				commands_button,
				credits_button,
				text1,
				text2,
				text3,
				newTicketCommand,
				aboutCommand,
				ticketviewCommand,
				modviewCommand);
		player.getMainScreen().attachPopupScreen(popup);
	}
	
	public boolean isAboutButton(Button button) {
		if (button == about_button) {
			return true;
		}		
		return false;
	}
	
	public boolean isCommandsButton(Button button) {
		if (button == commands_button) {
			return true;
		}		
		return false;
	}
	
	public boolean isCreditsButton(Button button) {
		if (button == credits_button) {
			return true;
		}		
		return false;
	}
	/**
	 * Returns true if the parsed button is the new ticket command button
	 * @param button
	 * @return
	 */
	public boolean isCommand1Button(Button button) {
		if (button == newTicketCommand) {
			return true;
		}		
		return false;
	}
	/**
	 * Returns true if the parsed button is the mod view command button
	 * @param button
	 * @return
	 */
	public boolean isCommand2Button(Button button) {
		if (button == modviewCommand) {
			return true;
		}		
		return false;
	}
	/**
	 * Returns true if the parsed button is the ticket view command button
	 * @param button
	 * @return
	 */
	public boolean isCommand3Button(Button button) {
		if (button == ticketviewCommand) {
			return true;
		}		
		return false;
	}
	
	/**
	 * Returns true if the parsed button is the about command button
	 * @param button
	 * @return
	 */
	public boolean isCommand4Button(Button button) {
		if (button == aboutCommand) {
			return true;
		}		
		return false;
	}
	
	/**
	 * Sets the GUI's screen to the parsed
	 * @param screens The screen enum
	 */
	public void setScreen(Screens screens) {
		switch (screens) {
			case ABOUT:
				reset();
				setScreenAbout();
				break;
			case COMMANDS:
				reset();
				setScreenCommands();
				break;
			case CREDITS:
				reset();
				setScreenCredits();
				break;
		}
	}
	
	/**
	 * Puts all objects back to the defualt state
	 */
	private void reset() {
		text1.setVisible(false);
		text2.setVisible(false);
		text3.setVisible(false);
		
		modviewCommand.setEnabled(false);
		modviewCommand.setVisible(false);
		
		aboutCommand.setVisible(false);
		aboutCommand.setEnabled(false);
		
		ticketviewCommand.setEnabled(false);
		ticketviewCommand.setVisible(false);
		
		newTicketCommand.setEnabled(false);
		newTicketCommand.setVisible(false);
		
		commands_button.setEnabled(true);
		credits_button.setEnabled(true);
		about_button.setEnabled(true);
	}
	
	/**
	 * Sets the screen to the commands page
	 */
	private void setScreenCommands() {
		modviewCommand.setEnabled(true);
		modviewCommand.setVisible(true);
		
		aboutCommand.setVisible(true);
		aboutCommand.setEnabled(true);
		
		ticketviewCommand.setEnabled(true);
		ticketviewCommand.setVisible(true);
		
		newTicketCommand.setEnabled(true);
		newTicketCommand.setVisible(true);
		
		page.setText("You are on the commands page");
		
		commands_button.setEnabled(false);
	}

	/**
	 * Sets the screen to the credits page
	 */
	private void setScreenCredits() {
		text1.setVisible(true);
		text2.setVisible(true);
		text3.setVisible(true);
		
		text1.setText("This is the credit screen");
		text2.setText("This is the credit screen");
		text3.setText("This is the credit screen");
		
		page.setText("You are on the credits page");
		
		credits_button.setEnabled(false);
	}

	/**
	 * Sets the screen to the about page
	 */
	private void setScreenAbout() {
		text1.setVisible(true);
		text2.setVisible(true);
		text3.setVisible(true);
		
		text1.setText("This is the about screen");
		text2.setText("This is the about screen");
		text3.setText("This is the about screen");
		
		page.setText("You are on the about page");
		
		about_button.setEnabled(false);
	}

}
