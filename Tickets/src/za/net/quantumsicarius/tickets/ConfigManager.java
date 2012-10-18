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
	private YamlConfiguration imageYml;
	private File file;
	private File imageFile;
	
	private Logger log;
	
	public ConfigManager(Plugin plugin, Logger log) {
		this.plugin = plugin;
		this.log = log;
		
		file = new File(plugin.getDataFolder() + File.separator + "config.yml");
		imageFile = new File(plugin.getDataFolder() + File.separator + "Pictures" + File.separator +"imageCounter.yml");
		
		yml = new YamlConfiguration();
		imageYml = new YamlConfiguration();
		
		// Config File
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
		
		// Image counter file
		if (!imageFile.exists()) {
			if (!imageFile.getParentFile().exists()) {
				imageFile.getParentFile().mkdirs();
			}
			
			try {
				imageFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		try {
			yml.load(file);
			imageYml.load(imageFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public String getDatabaseHost() {
		String out = yml.getString("DatabaseHost");
		
		if (out == null) {
			log.warning("[Tickets] Unable to get 'DatabaseHost' from config file!");
			return "None";
		}
		
		return out;
	}
	
	public Integer getDataBasePort() {
		Integer out = yml.getInt("DatabasePort");
		
		if (out == null | out == 0) {
			log.warning("[Tickets] Unable to get 'DatabasePort' from config file!");
			return 0;
		}
		
		return out;
	}
	
	public String getDatabaseUser() {
		String out = yml.getString("DatabaseUser");
		
		if (out == null) {
			log.warning("[Tickets] Unable to get 'DatabaseUser' from config file!");	
			return "None";
		}
		
		return out;
	}
	
	public String getDatabasePassword() {
		String pass = yml.getString("DatabasePassword");
		
		if (pass == null) {
			return "";
		}
		
		return pass;
	}
	
	public String getDatabase() {
		String out = yml.getString("Database");
		
		if (out == null) {
			log.warning("[Tickets] Unable to get 'Database' from config file!");
			return "None";
		}

		return out;
	}

	public int getNextPhotoId() {
		Integer out = imageYml.getInt("NextPhotoId");
		
		if (out == null | out == 0) {
			log.warning("[Tickets] Unable to get 'NextPhotoId' from config file!");
			return 1;
		}
		
		return out;
	}

	public void savePhotoid(int nextPhotoId) {
		imageYml.set("NextPhotoId", nextPhotoId);	
		try {
			imageYml.save(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDatabaseType() {		
		String out = yml.getString("DatabaseType");
		
		if (out == null) {
			log.warning("[Tickets] Unable to get 'DatabaseType' from config file!");
			return "None";
		}
		
		return out;
	}
	
	public void reload() {
		try {
			yml.load(file);
			imageYml.load(imageFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}
