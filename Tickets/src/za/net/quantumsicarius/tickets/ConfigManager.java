package za.net.quantumsicarius.tickets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {

	public Plugin plugin;
	
	private InputStream defconfig;
	private YamlConfiguration yml;
	private File file = new File("plugins" + File.separator + "Tickets" + File.separator + "config.yml");
	
	private Logger log;
	
	public ConfigManager(Plugin plugin, Logger log) {
		this.plugin = plugin;
		this.log = log;
		
		yml = new YamlConfiguration();
		
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			
			log.info("[Tickets] Config doesn't exist, creating a new one!");
			
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
		} else {
			log.info("[Tickets] Found config file!");
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
		try {
			return yml.getString("DatabaseHost");
		} catch (Exception e) {
			log.severe("[Tickets] Unable to get 'Database' from config file: " + e.getMessage());
		}
		return "None";
	}
	
	public Integer getDataBasePort() {
		try {
			return yml.getInt("DatabasePort");
		} catch (Exception e) {
			log.severe("[Tickets] Unable to get 'DatabasePort' from config file: " + e.getMessage());
		}
		return 0;
	}
	
	public String getDatabaseUser() {
		try {
			return yml.getString("DatabaseUser");
		} catch (Exception e) {
			log.severe("[Tickets] Unable to get 'DatabaseUser' from config file: " + e.getMessage());
		}
		
		return "None";
	}
	
	public String getDatabasePassword() {
		String pass = yml.getString("DatabasePassword");
		
		if (pass == null) {
			return "";
		}
		
		return pass;
	}
	
	public String getDatabase() {
		try {
			return yml.getString("Database");
		} catch (Exception e) {
			log.severe("[Tickets] Unable to get 'Database' from config file: " + e.getMessage());
		}
		
		return "None";
	}

	public int getNextPhotoId() {
		try {
			return yml.getInt("NextPhotoId");
		} catch (Exception e) {
			log.severe("[Tickets] Unable to get 'NextPhotoId' from config file: " + e.getMessage());
		}
		
		return 0;
	}

	public void savePhotoid(int nextPhotoId) {
		yml.set("NextPhotoId", nextPhotoId);	
		try {
			yml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDatabaseType() {
		try {
			return yml.getString("DatabaseType");
		} catch (Exception e) {
			log.severe("[Tickets] Unable to get 'NextPhotoId' from config file: " + e.getMessage());
		}
		
		return "None";
	}
	
	public void reload() {
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
}
