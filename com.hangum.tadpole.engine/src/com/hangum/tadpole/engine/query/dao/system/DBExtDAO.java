package com.hangum.tadpole.engine.query.dao.system;

/**
 * 사용자 디비 확장
 * 
 * @author hangum
 *
 */
public class DBExtDAO {
	
	// -- 이전 디비 정보
	protected String userId;
	protected String dbms_type;
	protected String url;
	protected String host;
	protected String port;
	protected String db;
	protected String users;
	
	
	// -- 이후 게이트웨이 디비 정보 설정
	protected String gate_url;
	protected String gate_host;
	protected String gate_port;
	protected String gate_db;
	protected String gate_users;
	protected String gate_passwd;
	
	public DBExtDAO() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDbms_type() {
		return dbms_type;
	}

	public void setDbms_type(String dbms_type) {
		this.dbms_type = dbms_type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getGate_url() {
		return gate_url;
	}

	public void setGate_url(String gate_url) {
		this.gate_url = gate_url;
	}

	public String getGate_host() {
		return gate_host;
	}

	public void setGate_host(String gate_host) {
		this.gate_host = gate_host;
	}

	public String getGate_port() {
		return gate_port;
	}

	public void setGate_port(String gate_port) {
		this.gate_port = gate_port;
	}

	public String getGate_db() {
		return gate_db;
	}

	public void setGate_db(String gate_db) {
		this.gate_db = gate_db;
	}

	public String getGate_users() {
		return gate_users;
	}

	public void setGate_users(String gate_users) {
		this.gate_users = gate_users;
	}

	public String getGate_passwd() {
		return gate_passwd;
	}

	public void setGate_passwd(String gate_passwd) {
		this.gate_passwd = gate_passwd;
	}


}
