package com.hangum.tadpole.engine.query.dao.system.monitoring;

/**
 * monitoring type
 * ex) CPU, NETWORK_IO,  SESSION_LIST
 * 
 * @author hangum
 *
 */
public class MonitoringTypeDAO {

	String type = "";
	String name = "";
	String description = "";
	
	public MonitoringTypeDAO() {
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
