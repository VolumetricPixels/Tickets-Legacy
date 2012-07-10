package za.net.quantumsicarius.tickets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {

	Plugin plugin;
	
	InputStream defconfig;
	YamlConfiguration yml;
	File file = new File("plugins" + File.separator + "Tickets" + File.separator + "config.yml");
	
	public ConfigManager(Plugin plugin) {
		this.plugin = plugin;
		
		yml = new YamlConfiguration();
		
		if (!file.exists()) {
			try {			
				OutputStream out = new FileOutputStream(file);				
				
				byte buf[]=new byte[1024];
				int len;
				
				defconfig = plugin.getResource("config.yml");
				
				while((len = defconfig.read(buf)) > 0) {
					out.write(buf,0,len);
				}
				
				out.close();
				defconfig.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
			
		try {
			yml.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public String getDatabaseHost() {
		return yml.getString("DatabaseHost");
	}
	
	public Integer getDataBasePort() {
		return yml.getInt("DatabasePort");
	}
	
	public String getDatabaseUser() {
		return yml.getString("DatabaseUser");
	}
	
	public String getDatabasePassword() {
		String pass = yml.getString("DatabasePassword");
		
		if (pass == null) {
			return "";
		}
		
		return pass;
	}
	
	public String getDatabase() {
		return yml.getString("Database");
	}

	public int getNextPhotoId() {
		return yml.getInt("NextPhotoId");
	}

	public void savePhotoid(int nextPhotoId) {
		yml.set("NextPhotoId", nextPhotoId);	
		try {
			yml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
