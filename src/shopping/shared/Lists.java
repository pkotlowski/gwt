package shopping.shared;

import java.text.DateFormat;

import org.apache.james.mime4j.field.datetime.DateTime;

public class Lists {
	public Long id;
	public String name;
	public String creationDate;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}


}
