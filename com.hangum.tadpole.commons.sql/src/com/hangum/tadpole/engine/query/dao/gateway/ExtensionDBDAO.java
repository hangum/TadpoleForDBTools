package com.hangum.tadpole.engine.query.dao.gateway;

import java.sql.Timestamp;

import com.hangum.tadpole.commons.util.DateUtil;

/**
 * extension db dao
 * 
 * id, db_type, host, port, `schema`, db_user, gate_host, gate_port, oracle_name, ext_jdbc_url 
 * 
 * @author hangum
 *
 */
public class ExtensionDBDAO {
	private String id = "";
	private String dev_nm = "";
	private String search_key;
	private String db_type;
	private String host;
	private String port;
	private String instance;
	private String db_user;
	/* servicename, sid */
	private String oracle_name = "";
	
	private String gate_host;
	private String gate_port;
	
	// default value is 00:00
	private Timestamp terms_of_use_starttime 	= new Timestamp(System.currentTimeMillis());

	// default value is 100 years after
	private	Timestamp terms_of_use_endtime  	= new Timestamp(DateUtil.afterMonthToMillsMonth(12));
	
	public ExtensionDBDAO() {
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	

	/**
	 * @return the dev_nm
	 */
	public String getDev_nm() {
		return dev_nm;
	}

	/**
	 * @param dev_nm the dev_nm to set
	 */
	public void setDev_nm(String dev_nm) {
		this.dev_nm = dev_nm;
	}

	/**
	 * @return the search_key
	 */
	public String getSearch_key() {
		return search_key;
	}

	/**
	 * @param search_key the search_key to set
	 */
	public void setSearch_key(String search_key) {
		this.search_key = search_key;
	}

	/**
	 * @return the db_type
	 */
	public String getDb_type() {
		return db_type;
	}

	/**
	 * @param db_type the db_type to set
	 */
	public void setDb_type(String db_type) {
		this.db_type = db_type;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the instance
	 */
	public String getInstance() {
		return instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public void setInstance(String instance) {
		this.instance = instance;
	}

	/**
	 * @return the db_user
	 */
	public String getDb_user() {
		return db_user;
	}

	/**
	 * @param db_user the db_user to set
	 */
	public void setDb_user(String db_user) {
		this.db_user = db_user;
	}

	/**
	 * @return the oracle_name
	 */
	public String getOracle_name() {
		return oracle_name;
	}

	/**
	 * @param oracle_name the oracle_name to set
	 */
	public void setOracle_name(String oracle_name) {
		this.oracle_name = oracle_name;
	}

	/**
	 * @return the gate_host
	 */
	public String getGate_host() {
		return gate_host;
	}

	/**
	 * @param gate_host the gate_host to set
	 */
	public void setGate_host(String gate_host) {
		this.gate_host = gate_host;
	}

	/**
	 * @return the gate_port
	 */
	public String getGate_port() {
		return gate_port;
	}

	/**
	 * @param gate_port the gate_port to set
	 */
	public void setGate_port(String gate_port) {
		this.gate_port = gate_port;
	}

	/**
	 * @return the terms_of_use_starttime
	 */
	public Timestamp getTerms_of_use_starttime() {
		return terms_of_use_starttime;
	}

	/**
	 * @param terms_of_use_starttime the terms_of_use_starttime to set
	 */
	public void setTerms_of_use_starttime(Timestamp terms_of_use_starttime) {
		this.terms_of_use_starttime = terms_of_use_starttime;
	}

	/**
	 * @return the terms_of_use_endtime
	 */
	public Timestamp getTerms_of_use_endtime() {
		return terms_of_use_endtime;
	}

	/**
	 * @param terms_of_use_endtime the terms_of_use_endtime to set
	 */
	public void setTerms_of_use_endtime(Timestamp terms_of_use_endtime) {
		this.terms_of_use_endtime = terms_of_use_endtime;
	}

}
