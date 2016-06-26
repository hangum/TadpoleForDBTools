package com.hangum.tadpole.engine.query.dao.system.userdb;

public class TDBParentDAO {
	
	private String tdbLogingIP = "NotSet";
	private String tdbUserID = "NotSet";
	
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

	
}
