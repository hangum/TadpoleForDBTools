/*******************************************************************************

 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.system;

import java.sql.Timestamp;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * user_db_resource dao
 * 
 * @author hangum
 *
 */
public class UserDBResourceDAO {
	int resource_seq;
	/** sql, erd */
	String resource_types;
	int user_seq;
	int db_seq;
	String name = "";
	String shared_type = PublicTadpoleDefine.SHARED_TYPE.PUBLIC.name();
	String description = "";
	
	// 공유자원 rest-api 지원 여부.
	String restapi_yesno = PublicTadpoleDefine.YES_NO.NO.name();
	String restapi_uri = "";
	String restapi_key = "";
	
	// 기타 부가 정보. 
	Timestamp create_time;
	String sqliteCreate_time;
	String delYn;
	
	String usernames = "";
	
	// db object tree 표현을 위해
	UserDBDAO parent;
	
	// 기본 데이터 저장시 사용하는  값입니다. (일반적으로는 사용하지 않습니다)
	String dataString = "";
	
	public UserDBResourceDAO() {
	}

	/**
	 * @return the resource_seq
	 */
	public int getResource_seq() {
		return resource_seq;
	}

	/**
	 * @param resource_seq the resource_seq to set
	 */
	public void setResource_seq(int resource_seq) {
		this.resource_seq = resource_seq;
	}

	/**
	 * @return the resource_types
	 */
	public String getResource_types() {
		return resource_types;
	}

	/**
	 * @param resource_types the resource_types to set
	 */
	public void setResource_types(String resource_types) {
		this.resource_types = resource_types;
	}

	/**
	 * @return the user_seq
	 */
	public int getUser_seq() {
		return user_seq;
	}

	/**
	 * @param user_seq the user_seq to set
	 */
	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}

	/**
	 * @return the db_seq
	 */
	public int getDb_seq() {
		return db_seq;
	}

	/**
	 * @param db_seq the db_seq to set
	 */
	public void setDb_seq(int db_seq) {
		this.db_seq = db_seq;
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
	 * @return the shared_type
	 */
	public String getShared_type() {
		return shared_type;
	}

	/**
	 * @param shared_type the shared_type to set
	 */
	public void setShared_type(String shared_type) {
		this.shared_type = shared_type;
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

	/**
	 * @return the create_time
	 */
	public Timestamp getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public UserDBDAO getParent() {
		return parent;
	}

	public void setParent(UserDBDAO parent) {
		this.parent = parent;
	}
	
	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	/**
	 * @return the restapi_yesno
	 */
	public String getRestapi_yesno() {
		return restapi_yesno;
	}

	/**
	 * @param restapi_yesno the restapi_yesno to set
	 */
	public void setRestapi_yesno(String restapi_yesno) {
		this.restapi_yesno = restapi_yesno;
	}

	/**
	 * @return the restapi_key
	 */
	public String getRestapi_key() {
		return restapi_key;
	}

	/**
	 * @param restapi_key the restapi_key to set
	 */
	public void setRestapi_key(String restapi_key) {
		this.restapi_key = restapi_key;
	}

	public String getRestapi_uri() {
		return restapi_uri;
	}

	public void setRestapi_uri(String restapi_uri) {
		this.restapi_uri = restapi_uri;
	}

	/**
	 * @return the usernames
	 */
	public String getUsernames() {
		return usernames;
	}

	/**
	 * @param usernames the usernames to set
	 */
	public void setUsernames(String usernames) {
		this.usernames = usernames;
	}

	/**
	 * @return the sqliteCreate_time
	 */
	public String getSqliteCreate_time() {
		return sqliteCreate_time;
	}

	/**
	 * @param sqliteCreate_time the sqliteCreate_time to set
	 */
	public void setSqliteCreate_time(String sqliteCreate_time) {
		this.sqliteCreate_time = sqliteCreate_time;
	}

	/**
	 * @return the dataString
	 */
	public String getDataString() {
		return dataString;
	}

	/**
	 * @param dataString the dataString to set
	 */
	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

}
