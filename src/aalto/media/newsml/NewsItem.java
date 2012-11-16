package aalto.media.newsml;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * NewsItem class for storing all relevant information of the news item
 *
 */
public class NewsItem {

	// Unique id of the news item
	private String guid;
	// News item version if modiefied and re-send
	private String version;
	// Date and time when the latest version was sent to customer
	private String version_created;
	private Date version_created_date;
	// Type of the news item (Pääjuttu, Kainalo, Tausta...)
	private String type_role;
	// Departement
	private String department;
	// Related categories of the news item
	private String[] categories;

	// Add more fields here.
	
	public NewsItem() {};
	
	/*
	 * Getters and Setter for NewsItem class
	 * 
	 */

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion_created() {
		return version_created;
	}

	public void setVersion_created(String version_created) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'H:m:s");
		try {
			Date date = df.parse(version_created);
			setVersion_created_date(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.version_created = version_created;
	}

	public Date getVersion_created_date() {
		return version_created_date;
	}

	public void setVersion_created_date(Date version_created_date) {
		this.version_created_date = version_created_date;
	}

	public String getType_role() {
		return type_role;
	}

	public void setType_role(String type_role) {
		this.type_role = type_role;
	}	

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}
	
	/*
	 * String presentation of the news item.
	 */
	
	@Override
	public String toString() {
		String s = "NewsItem " + getGuid() + "\n" +
				"Version " + getVersion() + ", sent " + getVersion_created();
		return s;
	}
}
