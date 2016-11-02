package com.hangum.tadpole.engine.utils;

/**
 * License dao
 * 
 * @author hangum
 *
 */
public class LicenseDAO {
	boolean isValidate = false;
	boolean isEnterprise = false;
	String msg = "";
	
	public LicenseDAO() {
	}
	
	/**
	 * @return the isEnterprise
	 */
	public boolean isEnterprise() {
		return isEnterprise;
	}

	/**
	 * @param isEnterprise the isEnterprise to set
	 */
	public void setEnterprise(boolean isEnterprise) {
		this.isEnterprise = isEnterprise;
	}

	/**
	 * @return the isValidate
	 */
	public boolean isValidate() {
		return isValidate;
	}

	/**
	 * @param isValidate the isValidate to set
	 */
	public void setValidate(boolean isValidate) {
		this.isValidate = isValidate;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
