package com.hangum.tadpole.engine.query.dao.system.monitoring;

/**
 * monitoring read type
 * ex) sql, pl/sql, file, rest api... others
 * 
 * @author hangum
 *
 */
public class MonitoringReadTypeDAO {
	
	String type = "";
	String name = "";
	String description = "";

	public MonitoringReadTypeDAO() {
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
