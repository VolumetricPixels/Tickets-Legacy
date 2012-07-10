package za.net.quantumsicarius.tickets.GUI;

import java.util.ArrayList;

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

public class ModListGUI {

	GenericPopup popup;
	
	GenericListWidget list;
	ListWidgetItem item;
	
	GenericButton select;
	GenericButton close;
	
	GenericLabel heading;
	
	public ModListGUI(SpoutPlayer player, Plugin plugin, ArrayList<SpoutPlayer> mods, ArrayList<Ticket> open, ArrayList<Ticket> closed) {
		
		heading = new GenericLabel();
		heading.setText(ChatColor.GREEN + "Mod List");
		heading.setWidth(70);
		heading.setHeight(10);
		heading.setX(160);
		heading.setY(10);
		
		list = new GenericListWidget();
		list.setWidth(198);
		list.setHeight(150);
		list.setX(100);
		list.setY(30);
		
		refreshList(mods, open, closed);
		
		select = new GenericButton();
		if (mods.size() == 0 | mods == null) {
			select.setEnabled(false);
		}
		select.setText("Select");
		select.setWidth(96);
		select.setHeight(15);
		select.setX(100);
		select.setY(190);
		
		close = new GenericButton();
		close.setText("Close");
		close.setHeight(15);
		close.setWidth(96);
		close.setY(190);
		close.setX(201);
		
		popup = new GenericPopup();
		popup.attachWidgets(plugin,
				heading,
				list,
				select,
				close);
		
		player.getMainScreen().attachPopupScreen(popup);
	}
	
	public boolean isSelectButton(Button button) {
		if (button == select) {
			return true;
		}
		return false;
	}
	
	public boolean isCloseButton(Button button) {
		if (button == close) {
			return true;
		}
		return false;
	}

	public void refreshList(ArrayList<SpoutPlayer> mods,ArrayList<Ticket> open,ArrayList<Ticket> closed) {
		int opentickets = 0;
		int closedtickets = 0;
		
		for (int i = 0; i < mods.size(); i++) {
			item = new ListWidgetItem();
			item.setTitle(mods.get(i).getName());
			
			if (open != null) {
				for (int a = 0; a < open.size(); a++) {
					if (open.get(a).getAssignee().equals(mods.get(i).getName())) {
						opentickets++;
					}
				}
			}

			if (closed != null) {	
				for (int b = 0; b < closed.size(); b++) {
					if (closed.get(b).getAssignee().equals(mods.get(i).getName())) {
						closedtickets++;
					}
				}
			}
			item.setText(ChatColor.GREEN + "Open: " + ChatColor.WHITE + opentickets + ChatColor.AQUA +" Closed: " + ChatColor.WHITE + closedtickets);
			
			list.addItem(item);
		}
	}
	
	public String getSelected() {
		String title = list.getSelectedItem().getTitle();
		if (title == null) {
			return "";
		}
		return title;
	}
	
}
