package za.net.quantumsicarius.tickets.GUI;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.player.SpoutPlayer;

public class ScreenShotView {
	
	GenericPopup popup;
	Texture image;
	
	GenericButton back;
	GenericButton change;
	
	public ScreenShotView(Plugin plugin, SpoutPlayer player, int id) {
		
		image = new GenericTexture();
		image.setUrl(id + ".png");
		image.setWidth(320);
		image.setHeight(180);
		image.setX(54);
		image.setY(30);
		
		back = new GenericButton();
		back.setText("Back");
		back.setHeight(20);
		back.setWidth(157);
		back.setX(54);
		back.setY(215);
		
		change = new GenericButton();
		change.setEnabled(false);
		change.setText("Change");
		change.setTooltip("Dont want to give this functionality yet");
		change.setHeight(20);
		change.setWidth(157);
		change.setX(216);
		change.setY(215);
		
		popup = new GenericPopup();
		popup.attachWidgets(plugin, image,
				back,
				change);
		
		player.getMainScreen().attachPopupScreen(popup);
	}
	
	public boolean isBackButton (Button button) {
		if (button == back) {
			return true;
		}
		return false;
	}
	
	public boolean isChangeButton (Button button) {
		if (button == change) {
			return true;
		}
		return false;
	}
}
