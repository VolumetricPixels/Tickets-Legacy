package za.net.quantumsicarius.tickets;

import org.bukkit.plugin.Plugin;

public class ConfigManager {

	Plugin plugin;
	
	public ConfigManager(Plugin plugin) {
		this.plugin = plugin;
		this.plugin.saveDefaultConfig();
	}
	
	public String getDatabaseHost() {
		return this.plugin.getConfig().getString("DatabaseHost");
	}
	
	public Integer getDataBasePort() {
		return this.plugin.getConfig().getInt("DatabasePort");
	}
	
	public String getDatabaseUser() {
		return this.plugin.getConfig().getString("DatabaseUser");
	}
	
	public String getDatabasePassword() {
		String pass = this.plugin.getConfig().getString("DatabasePassword");
		
		if (pass == null) {
			return "";
		}
		
		return pass;
	}
	
	public String getDatabase() {
		return this.plugin.getConfig().getString("Database");
	}

	public int getNextPhotoId() {
		return this.plugin.getConfig().getInt("NextPhotoId");
	}

	public void savePhotoid(int nextPhotoId) {
		this.plugin.getConfig().set("NextPhotoId", nextPhotoId);	
		this.plugin.saveConfig();
	}
}
