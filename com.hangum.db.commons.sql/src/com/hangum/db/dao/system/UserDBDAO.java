package com.hangum.db.dao.system;

import java.util.Date;
import java.util.List;

import com.hangum.db.dao.ManagerListDTO;

/**
 * 사용자 디비
 * 
 * @author hangum
 *
 */
public class UserDBDAO {
	int seq;
    int user_seq;
    
    String type;
    String url;
    String database;
    String display_name;
    String host;
    String port;
    String locale;
    String passwd;
    String user;
    Date create_time;
    String delYn;
    
    private ManagerListDTO parent;
    private List<UserDBResourceDAO> listUserDBErd;
    
    public UserDBDAO() {
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getUser_seq() {
		return user_seq;
	}

	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
    
	public ManagerListDTO getParent() {
		return parent;
	}
	
	public void setParent(ManagerListDTO parent) {
		this.parent = parent;
	}

	public List<UserDBResourceDAO> getListUserDBErd() {
		return listUserDBErd;
	}

	public void setListUserDBErd(List<UserDBResourceDAO> listUserDBErd) {
		this.listUserDBErd = listUserDBErd;
	}
	
	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	@Override
	public String toString() {
		return "UserDBDAO [seq=" + seq + ", user_seq=" + user_seq + ", type="
				+ type + ", url=" + url + ", database=" + database
				+ ", display_name=" + display_name + ", host=" + host
				+ ", port=" + port + ", passwd=" + passwd + ", user=" + user
				+ "]";
	}
	
    
}
