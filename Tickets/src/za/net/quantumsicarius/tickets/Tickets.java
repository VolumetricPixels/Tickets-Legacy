package za.net.quantumsicarius.tickets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.event.screen.ScreenshotReceivedEvent;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.ListWidgetItem;
import org.getspout.spoutapi.io.FileUtil;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.packet.PacketPreCacheFile;
import org.getspout.spoutapi.player.SpoutPlayer;

import za.net.quantumsicarius.tickets.Database.Database;
import za.net.quantumsicarius.tickets.Enums.DatabaseTypes;
import za.net.quantumsicarius.tickets.Enums.Screens;
import za.net.quantumsicarius.tickets.GUI.AboutGUI;
import za.net.quantumsicarius.tickets.GUI.ModListGUI;
import za.net.quantumsicarius.tickets.GUI.ScreenShotView;
import za.net.quantumsicarius.tickets.GUI.TicketGUI;
import za.net.quantumsicarius.tickets.GUI.TicketListGUI;
import za.net.quantumsicarius.tickets.GUI.TicketViewerGUI;

public class Tickets extends JavaPlugin implements Listener{
	
	// Define Maps
	private Map<SpoutPlayer, TicketGUI> NewTicketGUI;
	private Map<SpoutPlayer, TicketListGUI> TicketListGUIMap;
	private Map<SpoutPlayer, TicketViewerGUI> TicketViewGUIMap;
	private Map<SpoutPlayer, ScreenShotView> ScreenShotGUIMap;
	private Map<SpoutPlayer, Integer> playerTaskId;
	private Map<SpoutPlayer, AboutGUI> AboutGUIMap;
	private Map<SpoutPlayer, ModListGUI> ModlistGUIMap;
	// Define Database
	private Database db;
	// The plugin prefix
	private String PluginTitle = ChatColor.GREEN + "[Tickets] ";
	// The config manager object
	private ConfigManager config;
	// The id for pictures
	private int nextPhotoId;
	// Define Sets
	private Set<SpoutPlayer> screenshotQue;
	
	// The list of categories
	private List<String> categories;
	
	private Logger log;
	
	@Override
	public void onDisable() {
		closeDB();
		
		config.savePhotoid(nextPhotoId);
		
		log.info("[Tickets] Disabled!");
	}

	@Override
	public void onEnable() {
		log = this.getServer().getLogger();
		
		// Instantiate the HashMaps
		NewTicketGUI = new HashMap<SpoutPlayer, TicketGUI>();
		TicketListGUIMap = new HashMap<SpoutPlayer, TicketListGUI>();
		TicketViewGUIMap = new HashMap<SpoutPlayer, TicketViewerGUI>();
		ScreenShotGUIMap = new HashMap<SpoutPlayer, ScreenShotView>();
		playerTaskId = new HashMap<SpoutPlayer, Integer>();
		AboutGUIMap = new HashMap<SpoutPlayer, AboutGUI>();
		ModlistGUIMap = new HashMap<SpoutPlayer, ModListGUI>();
		// Instantiate the HashSets
		screenshotQue = new HashSet<SpoutPlayer>();
		// Instantiate the configuration manager
		config = new ConfigManager(this, log);
		// Get the latest image id
		nextPhotoId = config.getNextPhotoId();
		// Get the categories
		categories = config.getCategories();
		
		// Register event listeners
		getServer().getPluginManager().registerEvents(this, this);
		
		// Connect to database
		setupDB(config.getDatabaseHost(),
				config.getDataBasePort(),
				config.getDatabase(),
				config.getDatabaseUser(), 
				config.getDatabasePassword());
		
		if (db.isConnected()) {
			log.info("[Tickets] Enabled!");
		} else {
			log.severe("[Tickets] Failed to connect to database, Using SQLite!");
			db = new Database(DatabaseTypes.SQLite, log);
			db.connect();
		}
	}
	
	/**
	 * Connect to Database
	 */
	public void setupDB(String host,int port, String Database, String User, String Password) {	
		if (config.getDatabaseType().equalsIgnoreCase("mysql")) {
			db = new Database(DatabaseTypes.MySQL, log);
			db.setDatabase(Database);
			db.setHost(host);
			db.setPort(port);
			db.setUsername(User);
			db.setPassword(Password);
		} else if (config.getDatabaseType().equalsIgnoreCase("sqlite")) {
			db = new Database(DatabaseTypes.SQLite, log);
		} else {
			log.warning("[Tickets] Invalid database type! The supported types are: 'MySQL' and 'SQLite'");
			db = new Database(DatabaseTypes.SQLite, log);
		}
		db.connect();
	}
	
	/**
	 * Close Database connection
	 */
	public void closeDB() {
		db.close();
	}
	
	/*
	 * Called when a command occurs
	 */
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    	
    	if (sender instanceof Player) {
        	if(cmd.getName().equalsIgnoreCase("ticket")){
        		if (args.length > 0) {
        			
            		// Create a new ticket
            		if (args[0].equalsIgnoreCase("new")) {
            			if (sender.hasPermission("tickets.new") | sender.isOp()) {
                			if (SpoutManager.getPlayer((Player) sender).isSpoutCraftEnabled()) {
                				newTicket((Player) sender);
                			}
                			else {
                				//sender.sendMessage(PluginTitle + ChatColor.AQUA + "Will help you soon my child, for now go get SpoutCraft!");
                				if (args.length > 1) {
                    				newNonSpoutTicket((Player) sender, args[1], args[2]);
                				} else {
                					sender.sendMessage(PluginTitle + ChatColor.RED + "Invalid Parameters!");
                				}
                			}
            				
            			} else {
            				sender.sendMessage(ChatColor.RED + "You do not have permission to create a ticket!");
            			}
            		}
            		// View tickets
            		else if (args[0].equalsIgnoreCase("view")) {
            			if (sender.hasPermission("tickets.view") | sender.isOp()) {
            				if (args.length > 1) {
            					if (this.getServer().getPlayer(args[1]) != null) {
                					showTickets((Player) sender, this.getServer().getPlayer(args[1]).getName());
            					}
            					else if (args[1].equalsIgnoreCase("new")) {
            						showTickets((Player) sender, "None");
            					}
            				} else {
                				showTickets((Player) sender, "");
            				}
            			} else {
            				sender.sendMessage(ChatColor.RED + "You do not have permission to view tickets!");
            			}		
            		}
            		// About GUI
            		else if (args[0].equalsIgnoreCase("about")) {
            			SpoutPlayer player = SpoutManager.getPlayer((Player) sender);
            			
            			AboutGUI gui = new AboutGUI(player, this);
            			AboutGUIMap.put(player, gui);
            		}
            		// Moderator list GUI
            		else if (args[0].equalsIgnoreCase("modlist")) {
            			SpoutPlayer player = SpoutManager.getPlayer((Player) sender);
            			
            			if (player.hasPermission("modlist") | player.isOp()) {
                			ModListGUI gui = new ModListGUI(player, this, getMods(), getOpenTickets(""), getClosedTickets(""));
                			ModlistGUIMap.put(player, gui);
            			} else {
            				sender.sendMessage(ChatColor.RED + "You do not have permission to view tickets!");
            			}
            		}
            		// Reload plugin
            		else if (args[0].equalsIgnoreCase("reload")) {      			
            			SpoutPlayer player = SpoutManager.getPlayer((Player) sender);
            			
            			if (player.hasPermission("tickets.reload") | player.isOp()) {
                			config.reload();
                			player.sendMessage("[Tickets] Reloaded config!");
            			} else {
            				sender.sendMessage(ChatColor.RED + "You do not have permission to view tickets!");
            			}
            			
            		}
        		} else {
        			SpoutPlayer player = SpoutManager.getPlayer((Player) sender);
        			
        			player.sendMessage(PluginTitle + ChatColor.AQUA + "Help Menu:");
        			player.sendMessage(ChatColor.AQUA + "/ticket new " + ChatColor.WHITE + "-" + ChatColor.GREEN + " Creates a new ticket");
        			if (player.hasPermission("tickets.view") | player.isOp()) {
        				player.sendMessage(ChatColor.AQUA + "/ticket view " + ChatColor.WHITE + "-" + ChatColor.GREEN + " View all tickets");
        			}
        			if (player.hasPermission("tickets.modlist") | player.isOp()) {
        				player.sendMessage(ChatColor.AQUA + "/ticket modlist " + ChatColor.WHITE + "-" + ChatColor.GREEN + " View all Moderators Statistics");
        			}
        			if (player.hasPermission("tickets.reload") | player.isOp()) {
        				player.sendMessage(ChatColor.AQUA + "/ticket reload " + ChatColor.WHITE + "-" + ChatColor.GREEN + " Reloads the plugin's config file");
        			}
        			player.sendMessage(ChatColor.AQUA + "/ticket about " + ChatColor.WHITE + "-" + ChatColor.GREEN + " Views the plugins about page");
        		}
        		return true;
        	}
    	}
    	else {
    		sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
    		return true;
    	}
    	return false; 
    }
    
    /**
     * Gets all on/off line mods
     * @return
     */
    private ArrayList<SpoutPlayer> getMods() {
    	Player[] onplayers = this.getServer().getOnlinePlayers();
    	
    	ArrayList<SpoutPlayer> players = new ArrayList<SpoutPlayer>();
    	
    	for (int i = 0; i < onplayers.length; i++) {
    		if (onplayers[i].hasPermission("tickets.view")) {
    			players.add(SpoutManager.getPlayer(onplayers[i]));
    		}
    	}
    	
    	return players;
    }
    
    private void newNonSpoutTicket(Player player, String arg1, String arg2) {
		Ticket ticket = new Ticket();
		ticket.setTitle(arg1);
		ticket.setAuthor(player.getName());
		ticket.setDescription(arg2);
		ticket.setAssignee("None");
		ticket.setTime(Calendar.getInstance().getTimeInMillis());
		String location = player.getLocation().getWorld().getName() + "," + player.getLocation().getBlockX() + "," + player.getLocation().getBlockY() + "," + player.getLocation().getBlockZ();
		ticket.setReportedLocation(location);
		ticket.setCategory("Non Spout Player");
		ticket.setOpen(true);
		ticket.setImageId(0);
		
		// Check for there permission
		if (player.hasPermission("tickets.priority")) {
			ticket.setPriority(2);
		}
		else {
			ticket.setPriority(1);
		}
		
		// Save the ticket to Database
		saveTicket(ticket);
		
		player.sendMessage(PluginTitle + ChatColor.AQUA + "Your ticket has been sent!");
		
		Player[] players = this.getServer().getOnlinePlayers();
		
		for (int i = 0; i < players.length; i++) {
			if (players[i].isOp() | players[i].hasPermission("tickets.view")) {
				players[i].sendMessage(PluginTitle + ChatColor.AQUA + "A new ticket has been created by: " + ChatColor.WHITE+ player.getName());
			}
		}
	}

	@EventHandler
    public void PlayerJoin (PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	
    	if (player.getName().equals("QuantumSicarius") | player.getName().equals("Guy_de_Siguro")) {
    		player.sendMessage(ChatColor.AQUA + "This server is using your plugin: '" + ChatColor.GREEN + this.getName() + ChatColor.AQUA + "'");
    	}
    }
    
    /**
     * Returns a list of all the open entries in the Database
     * @return The ArrayList of ticket instances
     * @param assignee The person who's tickets to get ("None" for new ones, "" (empty) for all tickets or "PlayerName" for the persons tickets) 
     */
    public ArrayList<Ticket> getOpenTickets(String assignee) {
    	ArrayList<Ticket> tickets = new ArrayList<Ticket>();
    	ArrayList<Integer> priorities;
    	
    	if (assignee.equals("")) {
    		priorities = db.queryList("SELECT id FROM Tickets WHERE open=1 ORDER BY priority DESC");
    	} else {
    		priorities = db.queryList("SELECT id FROM Tickets WHERE assignee='" + assignee +"' AND open=1 ORDER BY priority DESC");
    	}
    	
    	for (int i = 0; i < priorities.size(); i++) {
    		tickets.add(getTicketInstance(priorities.get(i)));
    	}
    	
    	if (tickets.size() == 0) {
    		return null;
    	}
    	
    	return tickets;
    }
    
    /**
     * Gets a ticket instance
     * @param id The ticket's Id
     * @return The ticket instance
     */
    private Ticket getTicketInstance(int id) {
		Ticket ticket = new Ticket();
		
		ResultSet rs = db.queryRow("SELECT * FROM Tickets WHERE id=" + id + " LIMIT 1");
		try {
			while (rs.next()) {
				ticket.setId(rs.getInt("id"));
				ticket.setOpen(rs.getBoolean("Open"));
				ticket.setAuthor(rs.getString("Author"));
				ticket.setTime(rs.getLong("Time"));
				ticket.setCategory(rs.getString("Category"));
				ticket.setTitle(rs.getString("Title"));
				ticket.setReportedLocation(rs.getString("Location"));
				ticket.setPriority(rs.getInt("Priority"));
				ticket.setDescription(rs.getString("Description"));
				ticket.setAssignee(rs.getString("Assignee"));
				ticket.setImageId(rs.getInt("ImageId"));
			}
		} catch (SQLException e) {
			log.severe("[Tickets] A SQL error occured while gettting a ticket instance: " + e.getMessage());
		}
    	
    	return ticket;
 
    }
    
    /**
     * Returns a list of all the closed entries in the Database
     * @return The ArrayList of ticket instances
     * @param assignee The person who's tickets to get ("None" for new ones, "" (empty) for all tickets or "PlayerName" for the persons tickets) 
     */
    public ArrayList<Ticket> getClosedTickets(String assignee) {
    	ArrayList<Ticket> tickets = new ArrayList<Ticket>();
    	ArrayList<Integer> priorities;
    	
    	if (assignee.equals("")) {
        	priorities = db.queryList("SELECT id FROM Tickets WHERE open=0 ORDER BY priority DESC");
        	
    	} else {
    		priorities = db.queryList("SELECT id FROM Tickets WHERE assignee='" + assignee +"' AND open=0 ORDER BY priority DESC");
    	}	
    	
    	for (int i = 0; i < priorities.size(); i++) {
    		tickets.add(getTicketInstance(priorities.get(i)));
    	}
    	
    	if (tickets.size() == 0) {
    		return null;
    	}
    	
    	return tickets;
    }
    
    /**
     * Selects all tickets in priority of High to Low
     * @return
     */
    //@SuppressWarnings("unused")
	//private ArrayList<Integer> getAllTicketsByIdsDESC() {
    //	return db.CustomQueryResult("SELECT id FROM tickets ORDER BY priority DESC");
    //}
    
    /**
     * Returns a ticket using an unique Id
     * @param id The unique Id for the ticket
     * @return The ticket instance
     */
	private Ticket getTicketById(int id) {
    	return getTicketInstance(id);
    }
    

    /**
     * Gets a unique ticket by comparing the text and title
     * @param title The title of the ticket
     * @param text The description of the ticket
     * @return The Ticket instance
     */
	private Ticket getTicketByText(String title, String text) {
		ResultSet rs = db.queryRow("SELECT id FROM Tickets WHERE Title='" + title +"' AND Description='"+ text +"'");
		
		try {
			while(rs.next()) {
				return getTicketInstance(rs.getInt("id"));
			}
		} catch (SQLException e) {
			log.severe("[Tickets] A SQL error occured while executing a query: " + e.getMessage());
		}
		
		return null;
	}
    
    /**
     * Creates a new GUI for a player to create a new ticket
     * @param player The player that gets the GUI
     */
    public void newTicket(Player player) {
    	SpoutPlayer spoutplayer = SpoutManager.getPlayer(player);
    	// Create a new GUI object
    	TicketGUI popup = new TicketGUI(spoutplayer ,this, categories, config.getTicketTitleLength(), config.getTicketDescriptionLength());
    	
    	// Clean the old map
    	if (NewTicketGUI.containsKey(spoutplayer)) {
    		NewTicketGUI.remove(spoutplayer);
    	}
    	
    	// Add the GUI to the player HashMap
    	NewTicketGUI.put(spoutplayer, popup);
    }
    
    /**
     * Creates a new database entry
     * @param ticket The ticket object
     */
    public void saveTicket(Ticket ticket) {
    	db.save(ticket);
    }
    
    /**
     * Creates a new GUI to show all tickets and to manage them
     * @param player The player who gets the GUI
     */
    private void showTickets(Player player, String who) {
    	SpoutPlayer spoutplayer = SpoutManager.getPlayer(player);
    	// Create a new GUI object
    	TicketListGUI popup = new TicketListGUI(spoutplayer, this, getOpenTickets(who), getClosedTickets(who), who);
    	
    	// Clean old map
    	if (TicketListGUIMap.containsKey(spoutplayer)) {
    		TicketListGUIMap.remove(spoutplayer);
    	}
    	
    	// Add the GUI to the HashMap
    	TicketListGUIMap.put(spoutplayer, popup);
    }
    
    /*
     * Called when a player disconnects
     */
    @EventHandler
    public void PlayerDisconnet(PlayerQuitEvent event) {
    	SpoutPlayer player = SpoutManager.getPlayer(event.getPlayer());
    	
    	if (TicketViewGUIMap.containsKey(player)) {
    		TicketViewGUIMap.remove(player);
    	}
    	
    	if (TicketListGUIMap.containsKey(player)) {
    		TicketListGUIMap.remove(player);
    	}
    	
    	if (NewTicketGUI.containsKey(player)) {
    		NewTicketGUI.remove(player);
    	}
    	
    	if (ScreenShotGUIMap.containsKey(player)) {
    		ScreenShotGUIMap.remove(player);
    	}
    	
    	if (playerTaskId.containsKey(player)) {
    		playerTaskId.remove(player);
    	}
    	
    	if (AboutGUIMap.containsKey(player)) {
    		AboutGUIMap.remove(player);
    	}
    	
    	if (ModlistGUIMap.containsKey(player)) {
    		ModlistGUIMap.remove(player);
    	}
    }
    
    @EventHandler
    public void KeyPress(KeyPressedEvent event) {
    	if (event.getKey() != Keyboard.KEY_RETURN) {
    		return;
    	}
    	
    	SpoutPlayer player = event.getPlayer();
    	
    	if (screenshotQue.contains(player)) {
    		player.sendScreenshotRequest();
    		
        	NewTicketGUI.get(player).open();
    		
    		screenshotQue.remove(player);
    		
    		if (playerTaskId.containsKey(player)) {
    			this.getServer().getScheduler().cancelTask(playerTaskId.get(player));
    		}
    	}
    }
    
    @EventHandler
    public void ScreenShotReceive(ScreenshotReceivedEvent event) {
    	SpoutPlayer player = event.getPlayer();
    	processScreenshot(player, event.getScreenshot());
    }
    
    /**
     * Saves the Image in the folder
     * @param player
     * @param image
     */
	private void processScreenshot(SpoutPlayer player, BufferedImage image) {
		try {
			int imageId = nextPhotoId;
			//short imageId = nextPhotoId;
			File imageFolder = new File(this.getDataFolder(), "Pictures");
			if (!imageFolder.exists())
				imageFolder.mkdir();
			File imageFile = new File(imageFolder, imageId + ".png");
			ImageIO.write(image, "png", imageFile);
			nextPhotoId++;
			player.sendMessage(PluginTitle + ChatColor.AQUA + "Succesfuly saved screenshot " + imageId);
			if (NewTicketGUI.containsKey(player)) {
				NewTicketGUI.get(player).setScreenShotId(imageId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(PluginTitle + "Failed to take screenshot!");
		}
	}
	
	/**
	 * Cache the image for the player
	 * @param player The player who needs to get the cache
	 * @param id The image id
	 */
	private void cachePictureFor(SpoutPlayer player, int id) {
		File imageFolder = new File(this.getDataFolder(), "Pictures");
		File file = new File(imageFolder, id + ".png");
		long crc = -1;
		try {
			crc = FileUtil.getCRC(file, FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (crc !=-1) {
			player.sendPacket(new PacketPreCacheFile(this.getDescription().getName(), file.getPath(), crc, false));
		}
	}
    
    /*
     * Called when a GUI button is pressed
     */
    @EventHandler
    public void ButtonPress(ButtonClickEvent event) {
    	
    	if (event.getButton().getPlugin() != this) {
    		return;
    	}
    	
    	// The Player
    	final SpoutPlayer player = SpoutManager.getPlayer(event.getPlayer());
		// The Button
		final Button button = event.getButton();
    	
    	if (NewTicketGUI.containsKey(player)) {
    		if (NewTicketGUI.get(player).isOkButton(button)) {
    			TicketGUI gui = NewTicketGUI.get(player);
    			
    			if (!gui.validate()) {
    				return;
    			}
    			
    			Ticket ticket = new Ticket();
    			//gui.getTitle(),gui.getCategory() , gui.getDescription(), Calendar.getInstance().getTimeInMillis() , player.getName(), "None"
    			ticket.setTitle(gui.getTitle());
    			ticket.setAuthor(player.getName());
    			ticket.setDescription(gui.getDescription());
    			ticket.setAssignee("None");
    			ticket.setTime(Calendar.getInstance().getTimeInMillis());
    			String location = player.getLocation().getWorld().getName() + "," + player.getLocation().getBlockX() + "," + player.getLocation().getBlockY() + "," + player.getLocation().getBlockZ();
    			ticket.setReportedLocation(location);
    			ticket.setCategory(gui.getCategory());
    			ticket.setOpen(true);
    			ticket.setImageId(gui.getImageId());
    			
    			// Check for there permission
    			if (player.hasPermission("tickets.priority")) {
        			ticket.setPriority(2);
    			}
    			else {
        			ticket.setPriority(1);
    			}
    			
    			// Save the ticket to Database
    			saveTicket(ticket);
    			
    			// Close the players screen
    			player.getMainScreen().closePopup();
    			
    			// Clean the HashMap
    			NewTicketGUI.remove(player);
    			
    			player.sendMessage(PluginTitle + ChatColor.AQUA + "Your ticket has been sent!");
    			
    			Player[] players = this.getServer().getOnlinePlayers();
    			
    			for (int i = 0; i < players.length; i++) {
    				if (players[i].isOp() | players[i].hasPermission("tickets.view")) {
    					players[i].sendMessage(PluginTitle + ChatColor.AQUA + "A new ticket has been created by: " + ChatColor.WHITE+ player.getName());
    				}
    			}
    		}
    		else if (NewTicketGUI.get(player).isScreenShotButton(button)) {
    			// Close the Popup
    			player.getMainScreen().closePopup();
    			// Que the player for screenshot
    			screenshotQue.add(player);
    			// Send player a message
    			player.sendMessage(PluginTitle + ChatColor.AQUA + "Press enter to save screenshot!");
    			// Disabled
    			button.setEnabled(false);
    			
    			this.getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
    				
					@Override
					public void run() {
						
						if (screenshotQue.contains(player)) {
							player.sendMessage(PluginTitle + ChatColor.AQUA + "You took to long to take the screenshot!");
							screenshotQue.remove(player);
							
							button.setEnabled(true);
							
							if (NewTicketGUI.containsKey(player)) {
								NewTicketGUI.get(player).open();
							}
						}
					}
    				
    			}, 200L);
    		}
    		else if (NewTicketGUI.get(player).isCancelButton(event.getButton())) {
    			if (NewTicketGUI.containsKey(player)) {
    				TicketGUI gui = NewTicketGUI.get(player);
    			
    				File imageFolder = new File(this.getDataFolder(), "Pictures");
    				File imageFile = new File(imageFolder, gui.getImageId() + ".png");
    				if(imageFile.exists()) {
    					imageFile.delete();
    				}
    				
    				NewTicketGUI.remove(player);
    			}
    			
    			player.getMainScreen().closePopup();

    		}
    	}
    	
    	if (TicketViewGUIMap.containsKey(player)) {
			Ticket ticket = getTicketById(TicketViewGUIMap.get(player).getId());
    		
    		if (TicketViewGUIMap.get(player).isGotoButton(event.getButton())) {
    			player.teleport(TicketViewGUIMap.get(player).getAuthorPlayer());
    			TicketViewGUIMap.get(player).getAuthorPlayer().sendMessage(PluginTitle + ChatColor.AQUA + "Player: " + ChatColor.WHITE + player.getName() + ChatColor.AQUA + " teleported to you regarding your ticket!");
    		}
    		else if (TicketViewGUIMap.get(player).isEditButton(event.getButton())) {
    			event.getButton().setEnabled(false).setTooltip("Already editing!");
    			TicketViewGUIMap.get(player).setEditable(true);
    			TicketViewGUIMap.get(player).enableUpdateButton(true);
    		}
    		else if (TicketViewGUIMap.get(player).isUpdateButton(event.getButton())) {
    			TicketViewGUIMap.get(player).setEditable(false);
    			TicketViewGUIMap.get(player).enableEditButton(true, "Allows you to edit the fields");
    			TicketViewGUIMap.get(player).enableUpdateButton(false);
    			
    			// Update the ticket	
    			ticket.setAssignee(TicketViewGUIMap.get(player).getAssignee());
    			ticket.setDescription(TicketViewGUIMap.get(player).getDescription());
    			ticket.setCategory(TicketViewGUIMap.get(player).getCategory());
    			ticket.setPriority(TicketViewGUIMap.get(player).getPriority());
    			ticket.setTitle(TicketViewGUIMap.get(player).getTitle());
    			saveTicket(ticket);
    			
    			try {
        			Player assignee = this.getServer().getPlayer(ticket.getAssignee());
        			if (assignee.isOnline()) {
            			assignee.sendMessage(PluginTitle + ChatColor.AQUA + "You have been assigned to ticket: " + ChatColor.GREEN + "'" + ticket.getTitle() + "'");
        			}
    			} catch (NullPointerException e){}
    		}
    		else if (TicketViewGUIMap.get(player).isGotoLocationButton(event.getButton())){
    			Ticket localticket = getTicketById(TicketViewGUIMap.get(player).getId());
    			String[] location_string = localticket.getReportedLocation().split(",");
    			String world = location_string[0];
    			int x = Integer.parseInt(location_string[1]);
    			int y = Integer.parseInt(location_string[2]);
    			int z = Integer.parseInt(location_string[3]);
    			
    			World world_object = this.getServer().getWorld(world); 
    			
    			Location location = world_object.getBlockAt(x, y, z).getLocation();
    			player.teleport(location);
    		}
    		
    		else if (TicketViewGUIMap.get(player).isImageButton(event.getButton())) {
    			player.getMainScreen().closePopup();
    			// Cache the screen shot
    			cachePictureFor(player, ticket.getImageId());
    			// Open the screen shot view
    			ScreenShotView screen = new ScreenShotView(this ,player, ticket.getImageId());
    			// Add to HashMap
    			ScreenShotGUIMap.put(player, screen);
    		}
    		
    		else if (TicketViewGUIMap.get(player).isCloseButton(event.getButton())){
    			player.getMainScreen().closePopup();
    			TicketViewGUIMap.remove(player);
    		}
    	}
    	
    	if(ScreenShotGUIMap.containsKey(player)) {
    		if (ScreenShotGUIMap.get(player).isBackButton(event.getButton())) {
    			player.getMainScreen().closePopup();
    			ScreenShotGUIMap.remove(player);
    			
    			// Open the ticket again
    			if (TicketViewGUIMap.containsKey(player)) {
        			TicketViewGUIMap.get(player).open();
    			}
    		}
    		else if (ScreenShotGUIMap.get(player).isChangeButton(event.getButton())) {
    			player.getMainScreen().closePopup();
    			player.sendMessage(PluginTitle + ChatColor.AQUA + "Press Enter to take a new screenshot!");
    		}
    	}
    	
    	if (TicketListGUIMap.containsKey(player)) {
    		if (TicketListGUIMap.get(player).isOpenSelectButton(event.getButton())) {
    			
    			ListWidgetItem item = TicketListGUIMap.get(player).getOpenSelected();
       			 // If the ticket doesn't exist close it.
    			if (item == null) {
        			return;
        		}
    		
        		player.getMainScreen().closePopup();
        		
        		Ticket ticket = getTicketByText(item.getTitle(), item.getText());
        		
        		if (TicketViewGUIMap.containsKey(player)) {
        			TicketViewGUIMap.get(player).open();
        		} else {
        			
            		String title = ticket.getTitle();
            		String author = ticket.getAuthor();
            		String description = ticket.getDescrption();
            		String category = ticket.getCategory();
            		String assignee = ticket.getAssignee();
            		int priority = ticket.getPriority();
            		long time = ticket.getTime();
            		int Id = ticket.getId();
            		int imageId = ticket.getImageId();
        			
    				TicketViewerGUI ticketViewer = new TicketViewerGUI (
    						this, 
    						player, 
    						title, 
    						description, 
    						author, 
    						category,
    						time, 
    						assignee, 
    						priority, 
    						Id, 
    						player,
    						imageId);
    				// Add player to HashMap
    				TicketViewGUIMap.put(player, ticketViewer);
        		}
        		
    		}
    		else if (TicketListGUIMap.get(player).isCloseSelectButton(event.getButton())) {
    			
    			ListWidgetItem item = TicketListGUIMap.get(player).getClosedSelected();
       			 // If the ticket doesn't exist close it.
    			if (item == null) {
        			return;
        		}
    		
        		player.getMainScreen().closePopup();
        		
        		Ticket ticket = getTicketByText(item.getTitle(), item.getText());
        			
        		String title = ticket.getTitle();
        		String author = ticket.getAuthor();
        		String description = ticket.getDescrption();
        		String category = ticket.getCategory();
        		String assignee = ticket.getAssignee();
        		int priority = ticket.getPriority();
        		long time = ticket.getTime();
        		int Id = ticket.getId();
        		int ImageId = ticket.getImageId();
        		
				TicketViewerGUI ticketViewer = new TicketViewerGUI (
						this, 
						player, 
						title, 
						description, 
						author, 
						category,
						time, 
						assignee, 
						priority, 
						Id, 
						player,
						ImageId);
				// Add player to HashMap
				TicketViewGUIMap.put(player, ticketViewer);
    		}
    		else if (TicketListGUIMap.get(player).isOpenDeleteButton(event.getButton())) {
        		ListWidgetItem item = TicketListGUIMap.get(player).getOpenSelected();
        			
        		if (item == null) {
        			return;
        		}
        			
        		Ticket ticket = getTicketByText(item.getTitle(), item.getText());
        		
        		// Get the image for this ticket
        		File file = new File(this.getDataFolder() + File.separator + "pictures" + File.separator + ticket.getImageId() + ".png");
        		
        		// Delete the image
        		if (file.exists()) {
        			file.delete();
        		}
        		
        		// Delete the ticket entry
        		db.query("DELETE FROM Tickets WHERE Id = " + ticket.getId());
        		
        		String who = TicketListGUIMap.get(player).getTarget();
        		
        		// Update the lists
    			TicketListGUIMap.get(player).refreshClosedList(getClosedTickets(who));
    			TicketListGUIMap.get(player).refreshOpenList(getOpenTickets(who));
    		}
    		else if (TicketListGUIMap.get(player).isCloseDeleteButton(event.getButton())) {
        		ListWidgetItem item = TicketListGUIMap.get(player).getClosedSelected();
    			
        		if (item == null) {
        			return;
        		}
        			
        		Ticket ticket = getTicketByText(item.getTitle(), item.getText());
        		
        		// Get the image for this ticket
        		File file = new File(this.getDataFolder() + File.separator + "pictures" + File.separator + ticket.getImageId() + ".png");
        		
        		// Delete the image
        		if (file.exists()) {
        			file.delete();
        		}
        		
        		db.query("DELETE FROM Tickets WHERE Id = " + ticket.getId());
        		
        		String who = TicketListGUIMap.get(player).getTarget();
        		
    			TicketListGUIMap.get(player).refreshClosedList(getClosedTickets(who));
    			TicketListGUIMap.get(player).refreshOpenList(getOpenTickets(who));
    		}
    		else if (TicketListGUIMap.get(player).isOpenButton(event.getButton())) {
    			ListWidgetItem item = TicketListGUIMap.get(player).getClosedSelected();
    			
    			if (item == null) {
    				return;
    			}
    			
    			Ticket ticket = getTicketByText(item.getTitle(), item.getText());
    			
    			db.query("UPDATE Tickets SET open='" + 1 + "' WHERE Id = " + ticket.getId());
    			
        		String who = TicketListGUIMap.get(player).getTarget();
    			
    			TicketListGUIMap.get(player).refreshClosedList(getClosedTickets(who));
    			TicketListGUIMap.get(player).refreshOpenList(getOpenTickets(who));
    		}
    		else if (TicketListGUIMap.get(player).isCloseButton(event.getButton())) {
    			ListWidgetItem item = TicketListGUIMap.get(player).getOpenSelected();
    			
    			if (item == null) {
    				return;
    			}
    			
    			Ticket ticket = getTicketByText(item.getTitle(), item.getText());
    			
    			db.query("UPDATE Tickets SET open='" + 0 + "' WHERE Id = " + ticket.getId());
    			
        		String who = TicketListGUIMap.get(player).getTarget();
    			
    			TicketListGUIMap.get(player).refreshClosedList(getClosedTickets(who));
    			TicketListGUIMap.get(player).refreshOpenList(getOpenTickets(who));
    		}
    	}
    	if (AboutGUIMap.containsKey(player)) {
    		if (AboutGUIMap.get(player).isAboutButton(event.getButton())) {
    			AboutGUIMap.get(player).setScreen(Screens.ABOUT);
    		}
    		else if(AboutGUIMap.get(player).isCommandsButton(event.getButton())) {
    			AboutGUIMap.get(player).setScreen(Screens.COMMANDS);
    		}
    		else if(AboutGUIMap.get(player).isCreditsButton(event.getButton())) {
    			AboutGUIMap.get(player).setScreen(Screens.CREDITS);
    		}
    		// Commands page
    		else if(AboutGUIMap.get(player).isCommand1Button(event.getButton())) {
    			player.getMainScreen().closePopup();
    			AboutGUIMap.remove(player);
    			
    			TicketGUI gui = new TicketGUI(player, this, categories, config.getTicketTitleLength(), config.getTicketDescriptionLength());
    			NewTicketGUI.put(player, gui);
    		}
    		else if(AboutGUIMap.get(player).isCommand2Button(event.getButton())) {
    			player.getMainScreen().closePopup();
    			AboutGUIMap.remove(player);
    		}
    		else if(AboutGUIMap.get(player).isCommand3Button(event.getButton())) {
    			player.getMainScreen().closePopup();
    			AboutGUIMap.remove(player);
    			
    			showTickets(player, "");
    		}
    		else if(AboutGUIMap.get(player).isCommand4Button(event.getButton())) {
    			AboutGUIMap.get(player).setScreen(Screens.ABOUT);
    		}
    	}
    	if (ModlistGUIMap.containsKey(player)) {
    		if (ModlistGUIMap.get(player).isSelectButton(event.getButton())) {
    			player.getMainScreen().closePopup();
    			
    			showTickets(player, ModlistGUIMap.get(player).getSelected());
    			ModlistGUIMap.remove(player);
    		}
    		else if (ModlistGUIMap.get(player).isCloseButton(event.getButton())) {
    			player.getMainScreen().closePopup();
    			ModlistGUIMap.remove(player);
    		}
    	}
    }
}
