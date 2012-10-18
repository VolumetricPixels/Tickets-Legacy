package za.net.quantumsicarius.tickets.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import za.net.quantumsicarius.tickets.Ticket;
import za.net.quantumsicarius.tickets.Enums.DatabaseTypes;

public class Database {
	
	private Connection connection;
	private DatabaseTypes type;
	
	private Statement statement;
	private ResultSet resultset;
	
	private String host;
	private int port;
	private String database;
	private String username;
	private String password;
	
	private Logger log;
	
	/**
	 * Constructor
	 * @param log The logger instance
	 */
	public Database(DatabaseTypes type ,Logger log) {
		this.type = type;
		this.log = log;
	}
	
	/**
	 * Connects to the database
	 */
	public void connect() {
		try {
			if (type == DatabaseTypes.MySQL) {
				connection = DriverManager.getConnection(getMySQLURL(), username, password);
				log.info("[Tickets] Using MySQL");
			}
			else if (type == DatabaseTypes.SQLite) {
				// register the driver 
				String sDriverName = "org.sqlite.JDBC";
				try {
					Class.forName(sDriverName);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				connection = DriverManager.getConnection(getSQLliteURL());
				log.info("[Tickets] Using SQLite");
			}
			
			log.info("[Tickets] Succesfully connected to the database!");
			
			// Create table
			createTable();
		} catch (SQLException e) {
			log.severe("[Tickets] A SQL error occured while connecting: " + e.getMessage());
		}
	}
	
	/**
	 * Builds the URL to connect to a MySQL database
	 * @return
	 */
	private String getMySQLURL() {
		StringBuilder url = new StringBuilder();
		url.append("jdbc:mysql://");
		url.append(host);
		url.append(":");
		url.append(port);
		url.append("/");
		url.append(database);
		
		return url.toString();
	}
	
	/**
	 * Builds the URL to connect to a SQL lite database
	 * @return
	 */
	private String getSQLliteURL() {
		return "jdbc:sqlite:plugins/Tickets/tickets.db";
	}
	
	/**
	 * Closes the connection to the database
	 */
	public void close() {
		// Close the statement
		try {
			statement.close();
		} catch (SQLException e) {
			log.severe("[Tickets] A SQL error occured while closing the statement: " + e.getMessage());
		} catch (NullPointerException e) {
			// Ignore error
		}
		// Close the result
		try {
			resultset.close();
		} catch (SQLException e) {
			log.severe("[Tickets] A SQL error occured while closing the result set: " + e.getMessage());
		} catch (NullPointerException e) {
			// Ignore error
		}
		// Close the connection
		try {
			connection.close();
		} catch (SQLException e) {
			log.severe("[Tickets] A SQL error occured while closing the connection: " + e.getMessage());
		} catch (NullPointerException e) {
			// Ignore error
		}
	}
	
	/**
	 * Checks to see if the connection is still active
	 * @return True if the connection still exists
	 */
	public boolean isConnected() {
		if (type == DatabaseTypes.MySQL) {
			try {
				return connection != null && !connection.isClosed() && connection.isValid(5000);
			} catch (SQLException e) {
				return false;
			} catch (NullPointerException e) {
				return false;
			}
		}
		else if (type == DatabaseTypes.SQLite) {
			try {
				return connection != null && !connection.isClosed();
			} catch (SQLException e) {
				return false;
			}
		}
		else {
			return false;
		}

	}
	
	
	private void createTable() {
		// If the connection is dead return
		if (!isConnected()) {
			return;
		}
		
		try {
			// Build the query
			StringBuilder query = new StringBuilder();
			query.append("CREATE TABLE IF NOT EXISTS Tickets");
			query.append("(");
			if (type == DatabaseTypes.MySQL) {
				query.append("id int PRIMARY KEY NOT NULL AUTO_INCREMENT,");
			} else if (type == DatabaseTypes.SQLite) {
				query.append("id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,");
			}
			query.append("Open INTEGER,");
			query.append("Author varchar(255),");
			query.append("Time long,");
			query.append("Category varchar(255),");
			query.append("Title varchar(150),");
			query.append("Location varchar(255),");
			query.append("Priority int,");
			query.append("Description varchar(255),");
			query.append("Assignee varchar(255),");
			query.append("ImageId int");
			query.append(")");
			
			// Execute the query
			statement = connection.createStatement();
			statement.execute(query.toString());
		} catch (SQLException e) {
			log.severe("[Tickets] A SQL error occured while executing a Query: " + e.getMessage());
		}
	}
	
	/**
	 * Executes a non returning query
	 * @param query
	 */
	public void query(String query) {
		// If the connection is dead reconnect
		if (!isConnected()) {
			reconnect();
		}
		// Execute query
		try {
			statement = connection.createStatement();
			statement.execute(query);
		} catch (SQLException e) {
			log.severe("[Tickets] A SQL error occured while executing a Query: " + e.getMessage());
		}
	}
	
	public Integer queryId(String query) {
		// If the connection is dead reconnect
		if (!isConnected()) {
			reconnect();
		}
		// Execute query
		try {
			statement = connection.createStatement();
			resultset = statement.executeQuery(query);
			
			while(resultset.next()) {
				return resultset.getInt("id");
			}
		} catch (SQLException e) {
			log.severe("[Tickets] A SQL error occured while executing a Query: " + e.getMessage());
		}
		return 0;
	}
	
	public ResultSet queryRow(String query) {
		// If the connection is dead reconnect
		if (!isConnected()) {
			reconnect();
		}
		// Execute query
		try {
			statement = connection.createStatement();
			resultset = statement.executeQuery(query);
			
			return resultset;
		} catch (SQLException e) {
			log.severe("[Tickets] A SQL error occured while executing a Query: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Closes the connection silently and connects again
	 */
	private void reconnect() {
		// Close the connection
		if (!isConnected()) {
			try {
				connection.close();
			} catch (SQLException e) {
				// Don't do anything with the error
			} catch (NullPointerException e) {
				// Don't do anything with the error
			}
			
			// Connect
			connect();
		}
	}
	

	public void save(Ticket ticket) {
		
		if (ticket.getId() == 0) {
			StringBuilder query = new StringBuilder();
			query.append("INSERT INTO Tickets (Open ,Author, Time, Category, Title, Location, Priority, Description, Assignee, ImageId) VALUES (");
			if (ticket.getOpen()) {
				query.append(1);
			} else {
				query.append(2);
			}
			query.append(",'" + ticket.getAuthor() + "'");
			query.append("," + ticket.getTime());
			query.append(",'" + ticket.getCategory() + "'" );
			query.append(",'" + ticket.getTitle() + "'");
			query.append(",'" + ticket.getReportedLocation() + "'");
			query.append("," + ticket.getPriority());
			query.append(",'" + ticket.getDescrption() + "'" );
			query.append(",'" + ticket.getAssignee() + "'" );
			query.append("," + ticket.getImageId());
			query.append(")");
			
			query(query.toString());
		} else {
			StringBuilder query = new StringBuilder();
			query.append("UPDATE Tickets SET ");
			query.append("Title='"+ ticket.getTitle() +"'");
			query.append(", Description='" + ticket.getDescrption() + "'");
			query.append(", Author='"+ ticket.getAuthor() +"'");
			query.append(", Assignee='" + ticket.getAssignee() + "'");
			query.append(", Time='"+ ticket.getTime() +"'");
			query.append(", Priority='" + ticket.getPriority() + "'");
			query.append(", Location='" + ticket.getReportedLocation() + "'");
			query.append(", ImageId='" + ticket.getImageId() + "'");
			query.append(", Category='"+ ticket.getCategory() +"'");
			if (ticket.getOpen()) {
				query.append(", Open=" + 1);
			} else {
				query.append(", Open=" + 2);
			}
			
			query(query.toString());
		}
	}

	public ArrayList<Integer> queryList(String query) {
		if (!isConnected()) {
			reconnect();
		}
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		try {
			statement = connection.createStatement();
			resultset = statement.executeQuery(query);
			
			while (resultset.next()) {
				list.add(resultset.getInt("id"));
			}
		} catch (SQLException e) {
			log.severe("[Tickets] A SQL error occured while executing a query: " + e.getMessage());
		}
		
		return list;
	}
	
	/**
	 * Returns the host URL
	 * @return
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * Returns the port number
	 * @return
	 */
	public Integer getPort() {
		return port;
	}
	
	/**
	 * Returns the database name
	 * @return
	 */
	public String getDatabase() {
		return database;
	}
	
	/**
	 * Returns the username
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Returns the password
	 * @return
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Returns the database type (MySQL / SQL lite)
	 * @return
	 */
	public DatabaseTypes getDatabaseType() {
		return type;
	}
	
	/**
	 * Returns the classes used logger
	 * @return
	 */
	public Logger getLogger() {
		return log;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setPort(Integer port) {
		this.port = port;
	}
	
	public void setDatabase(String database) {
		this.database = database;
	} 
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setType(DatabaseTypes type) {
		this.type = type;
	}
	
	public void setLogger(Logger log) {
		this.log = log;
	}
}
