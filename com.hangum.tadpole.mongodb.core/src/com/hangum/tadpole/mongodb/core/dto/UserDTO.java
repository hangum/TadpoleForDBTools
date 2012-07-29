package com.hangum.tadpole.mongodb.core.dto;

/**
 * UserDTO
 * 
 * @author hangum
 *
 */
public class UserDTO {

	String id;
	String passwd;
	String readOnly;
	
	public UserDTO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
	
}
