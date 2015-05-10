package com.hangum.tadpole.engine.query.dao.system.monitoring;

/**
 * Message Send type
 * ex) 이메일, SMS, KILL AFTER 이메일, KILL AFTER SMS
 * 
 * @author hangum
 *
 */
public class MessageSendTypeDAO {
	String type = "";
	String name = "";
	String description = "";
	
	public MessageSendTypeDAO() {
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
