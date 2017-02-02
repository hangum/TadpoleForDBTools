package com.hangum.tadpole.engine.query.dao.system.userdb;

public class TDBParentDAO {
	
	private String tdbLogingIP = "NotSet";
	private String tdbUserID = "NotSet";
	
	/** DB의 read_only 여부 
	 * 
	 * mysql 의 경우 SHOW global variables like "read_only";
	 */
	private String readonly = "NO";
	
	public TDBParentDAO() {
	}

	public String getTdbLogingIP() {
		return tdbLogingIP;
	}

	public void setTdbLogingIP(String tdbLogingIP) {
		this.tdbLogingIP = tdbLogingIP;
	}

	public String getTdbUserID() {
		return tdbUserID;
	}

	public void setTdbUserID(String tdbUserID) {
		this.tdbUserID = tdbUserID;
	}

	/**
	 * @return the readonly
	 */
	public String getReadonly() {
		return readonly;
	}

	/**
	 * @param readonly the readonly to set
	 */
	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}


}
