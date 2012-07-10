package za.net.quantumsicarius.tickets;

public class Ticket {
	
	private int id;
	private String title;
	private String category;
	private String description;
	private long time;
	private String author;
	private String assignee;
	private int priority;
	private String location;
	private boolean open;
	private int imageId;
	
	/**
	 * Returns this tickets ID
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Return this tickets title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns this tickets category
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Returns this tickets description
	 */
	public String getDescrption() {
		return description;
	}
	
	/**
	 * Returns this tickets time
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Returns this tickets author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * Returns this tickets assignee
	 * @return The string of the assignee
	 */
	public String getAssignee() {
		return assignee;
	}
	
	/**
	 * Returns this tickets priority rating
	 * @return The integer value of the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * Return this tickets reported location
	 * @return
	 */
	public String getReportedLocation() {
		return location;
	}
	
	/**
	 * Returns this tickets linked image id
	 * @return The integer value of the id
	 */
	public int getImageId() {
		return imageId;
	}
	
	/**
	 * Returns this tickets open or closed state
	 * @return The boolean for open or closed
	 */
	public boolean getOpen() {
		return open;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * Sets this tickets title
	 * @param title The string that will be the title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets this tickets description
	 * @param description The string that will be the description
	 */
	public void setDescription (String description) {
		this.description = description;
	}
	
	/**
	 * Sets this tickets assignee
	 * @param assignee The string that will be the assignee
	 */
	public void setAssignee (String assignee) {
		this.assignee = assignee;
	}
	
	/**
	 * Sets this tickets author
	 * @param author The String that will be the author
	 */
	public void setAuthor (String author) {
		this.author = author;
	}
	
	/**
	 * Sets this tickets time
	 * @param l The integer that will be the time
	 */
	public void setTime(long l) {
		this.time = l;
	}
	/**
	 * Sets this tickets priority
	 * 	0 = Low
	 * 	1 = Normal
	 * 	2 = High
	 * @param i
	 */
	public void setPriority(int i) {
		this.priority = i;
	}
	/**
	 * Sets this tickets category
	 * @param s The string that will be the category
	 */
	public void setCategory(String s) {
		this.category = s;
	}
	/**
	 * Sets this tickets location
	 * @param s The string that will be the location
	 */
	public void setReportedLocation (String s) {
		this.location = s;
	}
	
	/**
	 * Sets this tickets status (Open or closed)
	 * @param b The boolean
	 */
	public void setOpen(boolean b) {
		this.open = b;
	}
	
	/**
	 * Sets this tickets linked image id
	 * @param id The integer id
	 */
	public void setImageId(int id) {
		this.imageId = id;
	}

}
